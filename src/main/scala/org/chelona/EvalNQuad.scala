/*
* Copyright (C) 2014-2015 Juergen Pfundt
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
  def apply(output: (NQuadElement, NQuadElement, NQuadElement, NQuadElement) ⇒ Int, basePath: String, label: String) = new EvalNQuad(output, basePath, label)

  sealed trait NQReturnValue extends NQuadReturnValue
}

class EvalNQuad(output: (NQuadElement, NQuadElement, NQuadElement, NQuadElement) ⇒ Int, basePath: String, label: String) extends NQReturnValue {

  import org.chelona.NQuadAST._

  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]

  var bCount = 0

  def renderStatement(ast: NTripleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case NQuadQuad(s, p, o, g) ⇒ output(s, p, o, g)
      case NQuadString(s)        ⇒ 0
      case NQuadComment(c)       ⇒ 0
    }
  }

  def evalStatement(expr: NTripleType): NQuadReturnValue = {
    expr match {
      case ASTStatement(subject, predicate, obj, graph, comment) ⇒
        comment match {
          case Some(c) ⇒ evalStatement(c);
          case None    ⇒
        }
        graph match {
          case Some(g) ⇒
            ((evalStatement(subject), evalStatement(predicate), evalStatement(obj), evalStatement(g)): @unchecked) match {
              case (NQuadString(s), NQuadString(p), NQuadString(o), NQuadString(g)) ⇒ NQuadQuad(s, p, o, g)
            }
          case None ⇒
            ((evalStatement(subject), evalStatement(predicate), evalStatement(obj)): @unchecked) match {
              case (NQuadString(s), NQuadString(p), NQuadString(o)) ⇒ NQuadQuad(s, p, o, NQuadElement("", NQuadBitValue.STRING_LITERAL_QUOTE))
            }
        }
      case ASTGraphLabel(rule)    ⇒ evalStatement(rule)
      case ASTTripleComment(rule) ⇒ evalStatement(rule)
      case ASTSubject(rule)       ⇒ evalStatement(rule)
      case ASTPredicate(rule)     ⇒ evalStatement(rule)
      case ASTObject(rule)        ⇒ evalStatement(rule)
      case ASTLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case NQuadString(s) ⇒ s.text
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIriRef(v) ⇒ NQuadString(NQuadElement(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case NQuadString(s) ⇒ s.text
            }), NQuadBitValue.STRING_LITERAL_QUOTE | NQuadBitValue.IRIREF))
            case ASTLangTag(v) ⇒ NQuadString(NQuadElement(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case NQuadString(s) ⇒ s.text
            }), NQuadBitValue.STRING_LITERAL_QUOTE | NQuadBitValue.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)            ⇒ NQuadString(NQuadElement(token, NQuadBitValue.LANGTAG))
      case ASTIriRef(token)             ⇒ NQuadString(NQuadElement("<" + token + ">", NQuadBitValue.IRIREF))
      case ASTStringLiteralQuote(token) ⇒ NQuadString(NQuadElement("\"" + token + "\"", NQuadBitValue.STRING_LITERAL_QUOTE))
      case ASTBlankNodeLabel(token)     ⇒ NQuadString(NQuadElement(setBlankNodeName("_:" + token), NQuadBitValue.BLANK_NODE_LABEL))
      case ASTComment(token)            ⇒ NQuadComment(NQuadElement(token, NQuadBitValue.COMMENT))
      case ASTBlankLine(token)          ⇒ NQuadComment(NQuadElement(token, NQuadBitValue.BLANK_LINE))
    }
  }

  private def setBlankNodeName(key: String) = {
    if (!blankNodeMap.contains(key)) {
      bCount += 1
      blankNodeMap += key -> ("_:b" + label + bCount)
    }
    blankNodeMap.getOrElse(key, "This should never be returned")
  }
}
