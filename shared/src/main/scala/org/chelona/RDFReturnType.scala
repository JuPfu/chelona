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

trait RDFReturnType {

  val defaultGraph: Term = Term("", RDFTokenTypes.DEFAULT_GRAPH)

  case class RDFTriple(s: Term, p: Term, o: Term /*, g: Term = defaultGraph*/ ) extends RDFReturnType
  case class RDFQuad(s: Term, p: Term, o: Term, g: Term) extends RDFReturnType
  case class RDFTriples(values: List[RDFTriple]) extends RDFReturnType
  case class RDFTuples(values: List[RDFQuad]) extends RDFReturnType
  case class RDFString(s: Term) extends RDFReturnType
  case class RDFComment(value: Term) extends RDFReturnType
  case class RDFNone() extends RDFReturnType
}

object RDFReturnType extends RDFReturnType
