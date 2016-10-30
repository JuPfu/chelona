/*
* Copyright (C) 2014, 2015, 2016 Juergen Pfundt
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.chelona

import org.chelona.EvalNTriples._

object EvalNTriples {
  def apply(output: (Term, Term, Term, Term) ⇒ Int, basePath: String, label: String) = new EvalNTriples(output, basePath, label)

  sealed trait ReturnType extends NTripleReturnType
}

class EvalNTriples(output: (Term, Term, Term, Term) ⇒ Int, basePath: String, label: String) extends ReturnType {

  import org.chelona.NTriplesParser._

  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]

  var bCount: Long = 0L

  def renderStatement(ast: NTripleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case Triple(s, p, o) ⇒ output(s, p, o, defaultGraph)
      case _                 ⇒ 0
    }
  }

  def evalStatement(expr: NTripleType): RDFReturnType = {
    expr match {
      case ASTTriple(subject, predicate, obj, comment) ⇒
        comment match { case Some(c) ⇒ evalStatement(c); case None ⇒ }
        ((evalStatement(subject), evalStatement(predicate), evalStatement(obj)): @unchecked) match {
          case (NTString(s), NTString(p), NTString(o)) ⇒ Triple(s, p, o)
        }
      case ASTSubject(rule)   ⇒ evalStatement(rule)
      case ASTPredicate(rule) ⇒ evalStatement(rule)
      case ASTObject(rule)    ⇒ evalStatement(rule)
      case ASTIriRef(token)   ⇒ NTString(Term("<" + token + ">", NTripleTokenTypes.IRIREF))
      case ASTLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case NTString(element) ⇒ element.value
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIriRef(v) ⇒ NTString(Term(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case NTString(element) ⇒ element.value
            }), NTripleTokenTypes.STRING_LITERAL_QUOTE | NTripleTokenTypes.IRIREF))
            case ASTLangTag(v) ⇒ NTString(Term(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case NTString(element) ⇒ element.value
            }), NTripleTokenTypes.STRING_LITERAL_QUOTE | NTripleTokenTypes.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTStringLiteralQuote(token) ⇒ NTString(Term("\"" + token + "\"", NTripleTokenTypes.STRING_LITERAL_QUOTE))
      case ASTLangTag(token)            ⇒ NTString(Term(token, NTripleTokenTypes.LANGTAG))
      case ASTBlankNodeLabel(token)     ⇒ NTString(Term(setBlankNodeName("_:" + token), NTripleTokenTypes.BLANK_NODE_LABEL))
      case ASTComment(token)            ⇒ NTComment(Term(token, NTripleTokenTypes.COMMENT))
      case ASTTripleComment(rule)       ⇒ evalStatement(rule)
      case ASTBlankLine(token)          ⇒ NTComment(Term(token, NTripleTokenTypes.BLANK_LINE))
    }
  }

  private def setBlankNodeName(key: String) = {
    if (!blankNodeMap.contains(key)) {
      bCount += 1L
      blankNodeMap += key → ("_:b" + label + bCount)
    }
    blankNodeMap.getOrElse(key, "This should never be returned")
  }
}