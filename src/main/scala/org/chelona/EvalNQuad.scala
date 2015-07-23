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
  def apply(basePath: String, label: String) = new EvalNQuad(basePath, label)

  sealed trait NQuadReturnValue

  case class NQuadString(s: String) extends NQuadReturnValue

  case class NQuadQuad(s: String, p: String, o: String, g: String) extends NQuadReturnValue

  case class NQuadComment(value: String) extends NQuadReturnValue
}

class EvalNQuad(basePath: String, label: String) {

  import org.chelona.NQuadAST._

  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]

  var bCount = 0

  def renderStatement(ast: NTripleType, writer: (String, String, String, String) ⇒ Int): Int = {
    (evalStatement(ast): @unchecked) match {
      case NQuadQuad(s, p, o, g) ⇒ writer(s, p, o, g)
      case NQuadString(s)        ⇒ 0
      case NQuadComment(c)       ⇒ 0
    }
  }

  def evalStatement(expr: NTripleType): NQuadReturnValue = {
    expr match {
      case ASTStatement(subject, predicate, obj, graph, comment) ⇒
        comment match { case Some(c) ⇒ evalStatement(c); case None ⇒ }
        graph match {
          case Some(g) ⇒
            ((evalStatement(subject), evalStatement(predicate), evalStatement(obj), evalStatement(g)): @unchecked) match {
              case (NQuadString(s1), NQuadString(p1), NQuadString(o1), NQuadString(g1)) ⇒ NQuadQuad(s1, p1, o1, g1)
            }
          case None ⇒
            ((evalStatement(subject), evalStatement(predicate), evalStatement(obj)): @unchecked) match {
              case (NQuadString(s1), NQuadString(p1), NQuadString(o1)) ⇒ NQuadQuad(s1, p1, o1, "")
            }
        }
      case ASTTripleComment(rule) ⇒ evalStatement(rule)
      case ASTSubject(rule)       ⇒ evalStatement(rule)
      case ASTPredicate(rule)     ⇒ evalStatement(rule)
      case ASTObject(rule)        ⇒ evalStatement(rule)
      case ASTLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case NQuadString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIriRef(v) ⇒ NQuadString(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case NQuadString(s) ⇒ s
            }))
            case ASTLangTag(v) ⇒ NQuadString(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case NQuadString(s) ⇒ s
            }))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)            ⇒ NQuadString(token)
      case ASTIriRef(token)             ⇒ NQuadString("<" + token + ">")
      case ASTStringLiteralQuote(token) ⇒ NQuadString("\"" + token + "\"")
      case ASTBlankNodeLabel(token)     ⇒ NQuadString(setBlankNodeName("_:" + token))
      case ASTComment(token)            ⇒ NQuadComment(token)
      case ASTBlankLine(token)          ⇒ NQuadComment(token)
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
