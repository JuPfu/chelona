/*
* Copyright (C) 2014 Juergen Pfundt
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

object CharPredicates {
  import org.parboiled2.CharPredicate
  import org.parboiled2.CharPredicate.{ Alpha, Digit }

  val DOT = CharPredicate('.')
  val SIGN = CharPredicate('+', '-')
  val COLON = CharPredicate(':')

  val IRIREF_CHAR = CharPredicate('\u0021' to '\uFFFF') -- CharPredicate("<>\"{}|^`\\")

  val PN_CHARS_BASE = Alpha ++ CharPredicate('\u00C0' to '\u00D6', '\u00D8' to '\u00F6', '\u00F8' to '\u02FF', '\u0370' to '\u037D', '\u037F' to '\u1FFF',
    '\u200C' to '\u200D', '\u2070' to '\u218F', '\u2C00' to '\u2FeF', '\u3001' to '\uD7FF', '\uF900' to '\uFDCF',
    '\uFDF0' to '\uFFFD')

  val PN_CHARS_U = PN_CHARS_BASE ++ CharPredicate('_')

  val PN_CHARS_U_DIGIT = PN_CHARS_U ++ Digit

  val PN_CHARS_U_COLON_DIGIT = PN_CHARS_U_DIGIT ++ COLON

  val PN_CHARS = PN_CHARS_U ++ CharPredicate('-') ++ Digit ++ CharPredicate('\u00B7', '\u0300' to '\u036F', '\u203F' to '\u2040')

  val PN_CHARS_COLON = PN_CHARS ++ COLON

  val PNAME_LN_CHARS = PN_CHARS_U ++ COLON ++ Digit

  val ECHAR_CHAR = CharPredicate("tbnrf\"'\\")

  val LOCAL_ESC = CharPredicate('_', '~', '.', '-', '!', '$', '&', "'", '(', ')', '*', '+', ',', ';', '=', '/', '?', '#', '@', '%')

  val WS = CharPredicate(" \t")
}
