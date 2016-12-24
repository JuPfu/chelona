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

object EvalNTriples {
  def apply(output: (Term, Term, Term, Term) ⇒ Int, basePath: String, label: String) = new EvalNTriples(output, basePath, label)
}

class EvalNTriples(output: (Term, Term, Term, Term) ⇒ Int, basePath: String, label: String) {

  import NTripleAST._

  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]

  var bCount: Long = 0L

  def renderStatement(ast: NTripleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case RDFTriple(s, p, o) ⇒ output(s, p, o, defaultGraph)
      case _                  ⇒ 0
    }
  }

  def evalStatement(expr: NTripleType): RDFReturnType = {
    expr match {
      case NTripleAST.ASTTriple(subject, predicate, obj, comment) ⇒
        comment match { case Some(c) ⇒ evalStatement(c); case None ⇒ }
        ((evalStatement(subject), evalStatement(predicate), evalStatement(obj)): @unchecked) match {
          case (RDFString(s), RDFString(p), RDFString(o)) ⇒ RDFTriple(s, p, o)
        }
      case NTripleAST.ASTSubject(rule)   ⇒ evalStatement(rule)
      case NTripleAST.ASTPredicate(rule) ⇒ evalStatement(rule)
      case NTripleAST.ASTObject(rule)    ⇒ evalStatement(rule)
      case NTripleAST.ASTIriRef(token)   ⇒ RDFString(Term("<" + token + ">", RDFTokenTypes.IRIREF))
      case NTripleAST.ASTLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case RDFString(element) ⇒ element.value
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case NTripleAST.ASTIriRef(v) ⇒ RDFString(Term(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case RDFString(element) ⇒ element.value
            }), RDFTokenTypes.STRING_LITERAL_QUOTE | RDFTokenTypes.IRIREF))
            case NTripleAST.ASTLangTag(v) ⇒ RDFString(Term(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case RDFString(element) ⇒ element.value
            }), RDFTokenTypes.STRING_LITERAL_QUOTE | RDFTokenTypes.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case NTripleAST.ASTStringLiteralQuote(token) ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL_QUOTE))
      case NTripleAST.ASTLangTag(token)            ⇒ RDFString(Term(token, RDFTokenTypes.LANGTAG))
      case NTripleAST.ASTBlankNodeLabel(token)     ⇒ RDFString(Term(setBlankNodeName("_:" + token), RDFTokenTypes.BLANK_NODE_LABEL))
      case NTripleAST.ASTComment(token)            ⇒ RDFComment(Term(token, RDFTokenTypes.COMMENT))
      case NTripleAST.ASTTripleComment(rule)       ⇒ evalStatement(rule)
      case NTripleAST.ASTBlankLine(token)          ⇒ RDFComment(Term(token, RDFTokenTypes.BLANK_LINE))
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