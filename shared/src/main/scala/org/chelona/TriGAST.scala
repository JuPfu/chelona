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

trait TriGAST extends TurtleAST with RDFASTType {

  case class ASTTrigDoc(rule: TurtleType) extends TriGAST

  case class ASTBlock(rule: TurtleType) extends TriGAST

  case class ASTLabelOrSubjectBlock(labelOrSubject: ASTLabelOrSubject, wrappedGraph: ASTWrappedGraph) extends TriGAST

  case class ASTTriplesOrGraph(labelOrSubject: TurtleType, rule: TurtleType) extends TriGAST

  case class ASTTriple2BlankNodePropertyList(blankNodePropertyList: TurtleType, predicateObjectList: Option[TurtleType]) extends TriGAST

  case class ASTTriple2Collection(collection: TurtleType, predicateObjectList: TurtleType) extends TriGAST

  case class ASTWrappedGraph(triplesBlock: Option[TurtleType]) extends TriGAST

  case class ASTTriplesBlock(triples: Seq[TurtleType]) extends TriGAST

  case class ASTLabelOrSubject(rule: TurtleType) extends TriGAST
}

object TriGAST extends TriGAST
