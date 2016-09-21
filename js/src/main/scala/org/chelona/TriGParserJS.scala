package org.chelona

import java.io.{ StringWriter, Writer }

import org.chelona.TurtleReturnValue.TurtleTriple
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

    def triGWriter(bo: Writer)(triple: List[RDFReturnType]): Int = {
      def formatter(token: String, `type`: Int) = {
        if (TurtleBitValue.isIRIREF(`type`))
          "&lt;" + token.substring(1, token.length - 1) + "&gt;"
        else
          token
      }

      triple.map {
        case TriGTuple(s, p, o, g) ⇒ {
          /* type information has yet to be added to EvalTriG */
          // val subject = formatter(s, type1)
          // val predicate = formatter(p, type2)
          //val `object` = formatter(o, type3)

          //bo.write(subject + " " + predicate + " " + `object` + " .\n")
          bo.write(s + " " + p + " " + o + " " + g + " .\n")
        }
      }.length
    }

    val output = new StringWriter()

    /* AST evaluation procedure. Here is the point to provide your own flavour, if you like. */
    val evalTriG = new EvalTriG(triGWriter(output) _, base, label)

    val parser = TriGParser(input, evalTriG.renderStatement, validate, base, label)

    val res = parser.turtleDoc.run()

    res match {
      case Success(tripleCount) ⇒
        val me: Double = System.currentTimeMillis - ms
        if (!validate) {
          ParseReport.information = "Input file converted in " + (me / 1000.0) + "sec " + tripleCount + " triples (triples per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")"
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
