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

import org.parboiled2._
import org.parboiled2.CharPredicate.{ Alpha, AlphaNum }

import scala.util.Success

object SchemeIdentifier {

  val SchemeChar = AlphaNum ++ '+' ++ '-' ++ '.'

  def apply(input: ParserInput): Boolean = {
    new SchemeIdentifier(input).scheme.run() match {
      case Success(s) ⇒ true
      case _          ⇒ false
    }
  }
}

class SchemeIdentifier(val input: ParserInput) extends Parser {

  import SchemeIdentifier._

  // scheme ::= ALPHA ( ALPHA / DIGIT / "+" / "-" / "." )*
  def scheme = rule { capture(Alpha ~ SchemeChar.*) ~ ':' }
}
