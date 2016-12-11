/*
* Copyright (C) 2014-2016 Juergen Pfundt
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

trait NTripleAST extends RDFAST {

  type NTripleType = NTripleAST

  case class ASTBlankLine(token: String) extends NTripleType

  case class ASTComment(token: String) extends NTripleType

  case class ASTNTriplesDoc(triple: Option[NTripleType], triples: Seq[NTripleType]) extends NTripleType

  case class ASTTriple(subject: NTripleType, predicate: NTripleType, `object`: NTripleType, comment: Option[ASTComment]) extends NTripleType

  case class ASTTripleComment(rule: NTripleType) extends NTripleType

  case class ASTSubject(rule: NTripleType) extends NTripleType

  case class ASTPredicate(rule: NTripleType) extends NTripleType

  case class ASTObject(rule: NTripleType) extends NTripleType

  case class ASTLangTag(token: String) extends NTripleType

  case class ASTIriRef(token: String) extends NTripleType

  case class ASTLiteral(token: NTripleType, postfix: Option[NTripleType]) extends NTripleType

  case class ASTStringLiteralQuote(token: String) extends NTripleType

  case class ASTBlankNodeLabel(token: String) extends NTripleType
}

object NTripleAST extends NTripleAST