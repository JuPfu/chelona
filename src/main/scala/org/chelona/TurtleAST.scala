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

trait TurtleAST {

  type TurtleType = TurtleAST

  case class ASTTurtleDoc(rule: TurtleAST) extends TurtleAST

  case class ASTStatement(rule: TurtleAST) extends TurtleAST

  case class ASTComment(token: String) extends TurtleAST

  case class ASTDirective(rule: TurtleAST) extends TurtleAST

  case class ASTPrefixID(namespace: TurtleAST, iri: TurtleAST) extends TurtleAST

  case class ASTBase(iri: TurtleAST) extends TurtleAST

  case class ASTSparqlBase(iri: TurtleAST) extends TurtleAST

  case class ASTSparqlPrefix(namespace: TurtleAST, iri: TurtleAST) extends TurtleAST

  case class ASTTriples(subject: TurtleAST, predicateObjectList: TurtleAST) extends TurtleAST

  case class ASTBlankNodeTriples(blankNodePropertyList: TurtleAST, predicateObjectList: Option[TurtleAST]) extends TurtleAST

  case class ASTPredicateObjectList(predicateObjectList: Seq[TurtleAST]) extends TurtleAST

  case class ASTPo(verb: TurtleAST, obj: TurtleAST) extends TurtleAST

  case class ASTObjectList(rule: Seq[TurtleAST]) extends TurtleAST

  case class ASTVerb(rule: TurtleAST) extends TurtleAST

  case class ASTIsA(token: String) extends TurtleAST

  case class ASTSubject(rule: TurtleAST) extends TurtleAST

  case class ASTPredicate(rule: TurtleAST) extends TurtleAST

  case class ASTObject(rule: TurtleAST) extends TurtleAST

  case class ASTLiteral(rule: TurtleAST) extends TurtleAST

  case class ASTBlankNodePropertyList(rule: TurtleAST) extends TurtleAST

  case class ASTCollection(rule: Seq[TurtleAST]) extends TurtleAST

  case class ASTNumericLiteral(rule: TurtleAST) extends TurtleAST

  case class ASTInteger(token: String) extends TurtleAST

  case class ASTDecimal(token: String) extends TurtleAST

  case class ASTDouble(token: String) extends TurtleAST

  case class ASTRdfLiteral(string: TurtleAST, postfix: Option[TurtleAST]) extends TurtleAST

  case class ASTLangTag(token: String) extends TurtleAST

  case class ASTBooleanLiteral(token: String) extends TurtleAST

  case class ASTString(rule: TurtleAST) extends TurtleAST

  case class ASTStringLiteralQuote(token: String) extends TurtleAST

  case class ASTStringLiteralSingleQuote(token: String) extends TurtleAST

  case class ASTStringLiteralLongSingleQuote(token: String) extends TurtleAST

  case class ASTStringLiteralLongQuote(token: String) extends TurtleAST

  case class ASTIri(rule: TurtleAST) extends TurtleAST

  case class ASTIriRef(token: String) extends TurtleAST

  case class ASTPrefixedName(rule: TurtleAST) extends TurtleAST

  case class ASTPNameNS(rule: Option[TurtleAST]) extends TurtleAST

  case class ASTPNameLN(namespace: TurtleAST, local: TurtleAST) extends TurtleAST

  case class ASTPNPrefix(token: String) extends TurtleAST

  case class ASTPNLocal(token: String) extends TurtleAST

  case class ASTBlankNode(rule: TurtleAST) extends TurtleAST

  case class ASTBlankNodeLabel(token: String) extends TurtleAST

  case class ASTAnon(token: String) extends TurtleAST
}
