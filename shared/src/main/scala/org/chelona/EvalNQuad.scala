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

import org.chelona.EvalNQuad._

object EvalNQuad {
  def apply(output: (Term, Term, Term, Term) ⇒ Int, basePath: String, label: String) = new EvalNQuad(output, basePath, label)

  sealed trait ReturnType extends NQuadReturnType
}

class EvalNQuad(output: (Term, Term, Term, Term) ⇒ Int, basePath: String, label: String) extends ReturnType {

  import org.chelona.NQuadAST._

  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]

  var bCount = 0

  def renderStatement(ast: NTripleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case Quad(s, p, o, g) ⇒ output(s, p, o, g)
      case _ => 0
    }
  }

  def evalStatement(expr: NTripleType): RDFReturnType = {
    expr match {
      case ASTStatement(subject, predicate, obj, graph, comment) ⇒
        comment match {
          case Some(c) ⇒ evalStatement(c);
          case None    ⇒
        }
        graph match {
          case Some(g) ⇒
            ((evalStatement(subject), evalStatement(predicate), evalStatement(obj), evalStatement(g)): @unchecked) match {
              case (NQuadString(s), NQuadString(p), NQuadString(o), NQuadString(g)) ⇒ Quad(s, p, o, g)
            }
          case None ⇒
            ((evalStatement(subject), evalStatement(predicate), evalStatement(obj)): @unchecked) match {
              case (NQuadString(s), NQuadString(p), NQuadString(o)) ⇒ Quad(s, p, o, defaultGraph)
            }
        }
      case ASTGraphLabel(rule)    ⇒ evalStatement(rule)
      case ASTTripleComment(rule) ⇒ evalStatement(rule)
      case ASTSubject(rule)       ⇒ evalStatement(rule)
      case ASTPredicate(rule)     ⇒ evalStatement(rule)
      case ASTObject(rule)        ⇒ evalStatement(rule)
      case ASTLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case NQuadString(s) ⇒ s.value
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIriRef(v) ⇒ NQuadString(Term(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case NQuadString(s) ⇒ s.value
            }), NQuadTokenTypes.STRING_LITERAL | NQuadTokenTypes.IRIREF))
            case ASTLangTag(v) ⇒ NQuadString(Term(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case NQuadString(s) ⇒ s.value
            }), NQuadTokenTypes.STRING_LITERAL | NQuadTokenTypes.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)            ⇒ NQuadString(Term(token, NQuadTokenTypes.LANGTAG))
      case ASTIriRef(token)             ⇒ NQuadString(Term("<" + token + ">", NQuadTokenTypes.IRIREF))
      case ASTStringLiteralQuote(token) ⇒ NQuadString(Term("\"" + token + "\"", NQuadTokenTypes.STRING_LITERAL))
      case ASTBlankNodeLabel(token)     ⇒ NQuadString(Term(setBlankNodeName("_:" + token), NQuadTokenTypes.BLANK_NODE_LABEL))
      case ASTComment(token)            ⇒ NQuadComment(Term(token, NQuadTokenTypes.COMMENT))
      case ASTBlankLine(token)          ⇒ NQuadComment(Term(token, NQuadTokenTypes.BLANK_LINE))
    }
  }

  private def setBlankNodeName(key: String) = {
    if (!blankNodeMap.contains(key)) {
      bCount += 1
      blankNodeMap += key → ("_:b" + label + bCount)
    }
    blankNodeMap.getOrElse(key, "This should never be returned")
  }
}
