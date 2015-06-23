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

trait NTripleAST {

  case class ASTBlankLine(token: String) extends NTripleAST

  case class ASTComment(token: String) extends NTripleAST

  case class ASTNTriplesDoc(triple: Option[NTripleAST], triples: Seq[NTripleAST]) extends NTripleAST

  case class ASTTriple(subject: NTripleAST, predicate: NTripleAST, `object`: NTripleAST, comment: Option[ASTComment]) extends NTripleAST

  case class ASTTripleComment(rule: NTripleAST) extends NTripleAST

  case class ASTSubject(rule: NTripleAST) extends NTripleAST

  case class ASTPredicate(rule: NTripleAST) extends NTripleAST

  case class ASTObject(rule: NTripleAST) extends NTripleAST

  case class ASTLangTag(token: String) extends NTripleAST

  case class ASTIriRef(token: String) extends NTripleAST

  case class ASTLiteral(token: NTripleAST, postfix: Option[NTripleAST]) extends NTripleAST

  case class ASTStringLiteralQuote(token: String) extends NTripleAST

  case class ASTBlankNodeLabel(token: String) extends NTripleAST
}
