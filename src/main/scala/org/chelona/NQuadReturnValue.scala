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

trait NQuadReturnValue extends RDFReturnType {

  case class NQuadString(s: String) extends NQuadReturnValue

  case class NQuadQuad(s: String, p: String, o: String, g: String) extends NQuadReturnValue

  case class NQuadComment(value: String) extends NQuadReturnValue
}

object NQuadReturnValue extends NQuadReturnValue
