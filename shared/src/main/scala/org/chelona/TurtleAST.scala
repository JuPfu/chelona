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

trait TurtleAST extends RDFAST {

  type TurtleType = TurtleAST

  case class ASTTurtleDoc(rule: TurtleType) extends TurtleType

  case class ASTStatement(rule: TurtleType) extends TurtleType

  case class ASTComment(token: String) extends TurtleType

  case class ASTDirective(rule: TurtleType) extends TurtleType

  case class ASTPrefixID(namespace: TurtleType, iri: TurtleType) extends TurtleType

  case class ASTBase(iri: TurtleType) extends TurtleType

  case class ASTSparqlBase(iri: TurtleType) extends TurtleType

  case class ASTSparqlPrefix(namespace: TurtleType, iri: TurtleType) extends TurtleType

  case class ASTTriples(subject: TurtleType, predicateObjectList: TurtleType) extends TurtleType

  case class ASTBlankNodeTriples(blankNodePropertyList: TurtleType, predicateObjectList: Option[TurtleType]) extends TurtleType

  case class ASTPredicateObjectList(predicateObjectList: Seq[TurtleType]) extends TurtleType

  case class ASTPo(verb: TurtleType, obj: TurtleType) extends TurtleType

  case class ASTObjectList(rule: Seq[TurtleType]) extends TurtleType

  case class ASTVerb(rule: TurtleType) extends TurtleType

  case class ASTIsA(token: String) extends TurtleType

  case class ASTSubject(rule: TurtleType) extends TurtleType

  case class ASTPredicate(rule: TurtleType) extends TurtleType

  case class ASTObject(rule: TurtleType) extends TurtleType

  case class ASTLiteral(rule: TurtleType) extends TurtleType

  case class ASTBlankNodePropertyList(rule: TurtleType) extends TurtleType

  case class ASTCollection(rule: Seq[TurtleType]) extends TurtleType

  case class ASTNumericLiteral(rule: TurtleType) extends TurtleType

  case class ASTInteger(token: String) extends TurtleType

  case class ASTDecimal(token: String) extends TurtleType

  case class ASTDouble(token: String) extends TurtleType

  case class ASTRdfLiteral(string: TurtleType, postfix: Option[TurtleType]) extends TurtleType

  case class ASTLangTag(token: String) extends TurtleType

  case class ASTBooleanLiteral(token: String) extends TurtleType

  case class ASTString(rule: TurtleType) extends TurtleType

  case class ASTStringLiteralQuote(token: String) extends TurtleType

  case class ASTStringLiteralSingleQuote(token: String) extends TurtleType

  case class ASTStringLiteralLongSingleQuote(token: String) extends TurtleType

  case class ASTStringLiteralLongQuote(token: String) extends TurtleType

  case class ASTIri(rule: TurtleType) extends TurtleType

  case class ASTIriRef(token: String) extends TurtleType

  case class ASTPrefixedName(rule: TurtleType) extends TurtleType

  case class ASTPNameNS(rule: Option[TurtleType]) extends TurtleType

  case class ASTPNameLN(namespace: TurtleType, local: TurtleType) extends TurtleType

  case class ASTPNPrefix(token: String) extends TurtleType

  case class ASTPNLocal(token: String) extends TurtleType

  case class ASTBlankNode(rule: TurtleType) extends TurtleType

  case class ASTBlankNodeLabel(token: String) extends TurtleType

  case class ASTAnon(token: String) extends TurtleType
}

object TurtleAST extends TurtleAST