/*
* Copyright (C) 2014, 2015, 2016 Juergen Pfundt
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

import java.io.{ StringWriter, Writer }

import org.chelona.TriGReturnValue.TriGTuple
import org.parboiled2.{ ParseError, ParserInput }

import scala.scalajs.js.annotation.JSExport
import scala.util.{ Failure, Success }

@JSExport
object TriGParserJS {

  object ParseReport { var information: String = "No information available." }

  @JSExport
  def getInformation(): String = { ParseReport.information }

  @JSExport
  def parse(rdf_input: String, verbose: Boolean = true, validate: Boolean = false, base: String = "", uid: Boolean = false): String = {

    val ms: Double = System.currentTimeMillis

    val label = if (uid) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

    lazy val input: ParserInput = rdf_input

    def triGHTMLWriter(bo: Writer)(triple: List[RDFReturnType]): Int = {
      def formatter(token: String, `type`: Int) = {
        if (TriGBitValue.isIRIREF(`type`))
          "&lt;" + token.substring(1, token.length - 1) + "&gt;"
        else
          token
      }

      triple.map {
        case TriGTuple(s, p, o, g) ⇒ {
          val subject = formatter(s.text, s.tokenType)
          val predicate = formatter(p.text, p.tokenType)
          val `object` = formatter(o.text, o.tokenType)
          val graph = formatter(g.text, g.tokenType)

          bo.write(subject + " " + predicate + " " + `object` + (if (graph.length > 0) " " + graph + " .\n" else " .\n"))
        }
      }.length
    }

    val output = new StringWriter()

    /* AST evaluation procedure. Here is the point to provide your own flavour, if you like. */
    val evalTriG = new EvalTriG(triGHTMLWriter(output) _, base, label)

    val parser = TriGParser(input, evalTriG.renderStatement, validate, base, label)

    val res = parser.trigDoc.run()

    res match {
      case Success(tripleCount) ⇒
        val me: Double = System.currentTimeMillis - ms
        if (!validate) {
          ParseReport.information = "Input file converted in " + (me / 1000.0) + "sec " + tripleCount + " quads (quads per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")"
        } else {
          ParseReport.information = "Input file composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")"
        }
      case Failure(e: ParseError) ⇒ {
        ParseReport.information = parser.formatError(e)
      }
      case Failure(e) ⇒ {
        ParseReport.information = " Unexpected error during parsing run: " + e
      }
    }
    output.toString
  }
}
