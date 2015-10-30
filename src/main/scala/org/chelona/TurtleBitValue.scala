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

object TurtleBitValue {
  val EMPTY = 0

  val BLANK_NODE_LABEL = 1
  val ISA = BLANK_NODE_LABEL << 1
  val IRIREF = ISA << 1
  val PREFIXID = IRIREF << 1
  val BASE = PREFIXID << 1
  val SPARQLBASE = BASE << 1
  val SPARQLPREFIX = SPARQLBASE << 1
  val INTEGER = SPARQLPREFIX << 1
  val DECIMAL = INTEGER << 1
  val DOUBLE = DECIMAL << 1
  val STRING_LITERAL_QUOTE = IRIREF << 1
  val LANGTAG = STRING_LITERAL_QUOTE << 1
  val ANON = LANGTAG << 1
  val BOOLEAN_LITERAL = ANON << 1
  val PNAMENS = BOOLEAN_LITERAL << 1
  val PNAMELN = PNAMENS << 1
  val PNPREFIX = PNAMELN << 1
  val PNLOCAL = PNPREFIX << 1
  val BLANK_LINE = PNLOCAL << 1
  val COMMENT = BLANK_LINE << 1

  type TurtleToken = Int
}