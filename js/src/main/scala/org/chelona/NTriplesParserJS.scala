package org.chelona

import java.io.{ StringWriter, Writer }

import org.parboiled2.{ ParseError, ParserInput }

import scala.scalajs.js.annotation.JSExport
import scala.util.{ Failure, Success }

@JSExport
object NTriplesParserJS {

  object ParseReport {
    var information: String = "No information available."
  }

  @JSExport
  def getInformation(): String = {
    ParseReport.information
  }

  @JSExport
  def parse(rdf_input: String, verbose: Boolean = true, validate: Boolean = false, base: String = "", uid: Boolean = false): String = {

    val ms: Double = System.currentTimeMillis

    val label = if (uid) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

    lazy val input: ParserInput = rdf_input

    def ntWriter(bo: Writer)(s: NTripleElement, p: NTripleElement, o: NTripleElement): Int = {
      def formatter(token: String, `type`: Int) = {
        if (NTripleBitValue.isIRIREF(`type`))
          "&lt;" + token.substring(1, token.length - 1) + "&gt;"
        else
          token
      }

      val subject = formatter(s.text, s.tokenType)
      val predicate = formatter(p.text, p.tokenType)
      val `object` = formatter(o.text, o.tokenType)

      bo.write(subject + " " + predicate + " " + `object` + " .\n")
      1
    }

    val output = new StringWriter()

    /* AST evaluation procedure. Here is the point to provide your own flavour, if you dare. */
    val evalNT = new EvalNT(ntWriter(output) _, base, label)

    val parser = NTriplesParser(input, evalNT.renderStatement, validate, base, label)

    val res = parser.ntriplesDoc.run()

    res match {
      case Success(tripleCount) ⇒
        val me: Double = System.currentTimeMillis - ms
        if (!validate) {
          ParseReport.information = "Input file converted in " + (me / 1000.0) + "sec " + tripleCount + " triples (triples per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")"
        } else {
          ParseReport.information = "Input file composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")"
        }
      case Failure(e: ParseError) ⇒ {
        System.err.println(parser.formatError(e))
        ParseReport.information = parser.formatError(e)
      }
      case Failure(e) ⇒ {
        ParseReport.information = " Unexpected error during parsing run: " + e
      }
    }
    output.toString
  }
}
