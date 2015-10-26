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

import org.parboiled2.{ ParseError, ParserInput, ErrorFormatter }

import java.lang.{ StringBuilder ⇒ JStringBuilder }

class ChelonaErrorFormatter(block: Long = 0L,
                            showExpected: Boolean = true,
                            showPosition: Boolean = true,
                            showLine: Boolean = true,
                            showTraces: Boolean = false,
                            showFrameStartOffset: Boolean = true,
                            expandTabs: Int = -1,
                            traceCutOff: Int = 120) extends ErrorFormatter {

  /**
   * Formats the given [[ParseError]] into the given StringBuilder
   * using the settings configured for this formatter instance.
   */
  override def format(sb: JStringBuilder, error: ParseError, input: ParserInput): JStringBuilder = {
    formatProblem(sb, error, input)
    import error._
    if (showExpected) formatExpected(sb, error)
    if (showPosition) sb.append(" (line ").append((block + position.line)).append(", column ").append(position.column).append(')')
    if (showLine) formatErrorLine(sb.append(':').append('\n'), error, input)
    if (showTraces) sb.append('\n').append('\n').append(formatTraces(error)) else sb
  }
}
