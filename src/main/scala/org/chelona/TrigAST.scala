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

trait TrigAST[TrigAST <: TurtleAST] extends TurtleAST {

  case class ASTTrigDoc(rule: TrigAST) extends TurtleAST

  case class ASTBlock(rule: TrigAST) extends TurtleAST

  case class ASTLabelOrSubjectBlock(labelOrSubject: ASTLabelOrSubject, wrappedGraph: ASTWrappedGraph) extends TurtleAST

  case class ASTTriplesOrGraph(labelOrSubject: TrigAST, rule: TrigAST) extends TurtleAST

  case class ASTBlankNodeTriples2(blankNodePropertyList: TrigAST, predicateObjectList: Option[TrigAST]) extends TurtleAST

  case class ASTCollectionTriples2(collection: TrigAST, predicateObjectList: TrigAST) extends TurtleAST

  case class ASTWrappedGraph(triplesBlock: Option[TrigAST]) extends TurtleAST

  case class ASTTriplesBlock(triples: Seq[TrigAST]) extends TurtleAST

  case class ASTLabelOrSubject(rule: TrigAST) extends TurtleAST
}
