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

import org.chelona.EvalNT._

object EvalNT {
  def apply(output: (NTripleElement, NTripleElement, NTripleElement) ⇒ Int, basePath: String, label: String) = new EvalNT(output, basePath, label)

  sealed trait NTReturnValue extends NTripleReturnValue
}

class EvalNT(output: (NTripleElement, NTripleElement, NTripleElement) ⇒ Int, basePath: String, label: String) extends NTReturnValue {

  import org.chelona.NTriplesParser._

  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]

  var bCount: Long = 0L

  def renderStatement(ast: NTripleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case NTTriple(s, p, o) ⇒ output(s, p, o)
      case NTString(s)       ⇒ 0
      case NTComment(c)      ⇒ 0
    }
  }

  def evalStatement(expr: NTripleType): NTripleReturnValue = {
    expr match {
      case ASTTriple(subject, predicate, obj, comment) ⇒
        comment match { case Some(c) ⇒ evalStatement(c); case None ⇒ }
        ((evalStatement(subject), evalStatement(predicate), evalStatement(obj)): @unchecked) match {
          case (NTString(s), NTString(p), NTString(o)) ⇒ NTTriple(s, p, o)
        }
      case ASTSubject(rule)   ⇒ evalStatement(rule)
      case ASTPredicate(rule) ⇒ evalStatement(rule)
      case ASTObject(rule)    ⇒ evalStatement(rule)
      case ASTIriRef(token)   ⇒ NTString(NTripleElement("<" + token + ">", NTripleBitValue.IRIREF))
      case ASTLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case NTString(element) ⇒ element.text
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIriRef(v) ⇒ NTString(NTripleElement(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case NTString(element) ⇒ element.text
            }), NTripleBitValue.STRING_LITERAL_QUOTE | NTripleBitValue.IRIREF))
            case ASTLangTag(v) ⇒ NTString(NTripleElement(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case NTString(element) ⇒ element.text
            }), NTripleBitValue.STRING_LITERAL_QUOTE | NTripleBitValue.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTStringLiteralQuote(token) ⇒ NTString(NTripleElement("\"" + token + "\"", NTripleBitValue.STRING_LITERAL_QUOTE))
      case ASTLangTag(token)            ⇒ NTString(NTripleElement(token, NTripleBitValue.LANGTAG))
      case ASTBlankNodeLabel(token)     ⇒ NTString(NTripleElement(setBlankNodeName("_:" + token), NTripleBitValue.BLANK_NODE_LABEL))
      case ASTComment(token)            ⇒ NTComment(NTripleElement(token, NTripleBitValue.COMMENT))
      case ASTTripleComment(rule)       ⇒ evalStatement(rule)
      case ASTBlankLine(token)          ⇒ NTComment(NTripleElement(token, NTripleBitValue.BLANK_LINE))
    }
  }

  private def setBlankNodeName(key: String) = {
    if (!blankNodeMap.contains(key)) {
      bCount += 1L
      blankNodeMap += key -> ("_:b" + label + bCount)
    }
    blankNodeMap.getOrElse(key, "This should never be returned")
  }
}