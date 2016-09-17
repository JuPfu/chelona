package org.chelona

import java.io.{ StringWriter, Writer }

import org.chelona.SPOReturnValue.SPOTriple
import org.chelona.TriGReturnValue.TriGTuple
import org.parboiled2.{ ErrorFormatter, ParseError, ParserInput }

import scala.scalajs.js.annotation.JSExport
import scala.util.{ Failure, Success }

@JSExport
object TurtleParserJS {
  @JSExport
  def parse(rdf_input: String, verbose: Boolean = true, validate: Boolean = false, base: String = "", uid: Boolean = false): String = {

    val ms: Double = System.currentTimeMillis

    val label = if (uid) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

    lazy val input: ParserInput = rdf_input

    def tripleWriter(bo: Writer)(triple: List[RDFReturnType]): Int = {
      def formatter(token: String, `type`: Int) = {
        if (TurtleBitValue.isIRIREF(`type`))
          //if ((`type` & TurtleBitValue.IRIREF) == TurtleBitValue.IRIREF)
          "&lt;" + token.substring(1, token.length - 1) + "&gt;"
        else
          token
      }
      triple.map {
        case SPOTriple(TurtleElement(s, type1), TurtleElement(p, type2), TurtleElement(o, type3)) ⇒ {
          val subject = formatter(s, type1)
          val predicate = formatter(p, type2)
          val `object` = formatter(o, type3)

          bo.write(subject + " " + predicate + " " + `object` + " .\n")
        }
        case _ ⇒ System.err.println("MAP ERROR")
      }.length
    }

    val output = new StringWriter()

    val parser = ChelonaParser(input, tripleWriter(output) _, validate, base, label)

    val res = parser.turtleDoc.run()

    res match {
      case Success(tripleCount) ⇒
        val me: Double = System.currentTimeMillis - ms
        if (verbose) {
          if (!validate) {
            System.err.println("Input file converted in " + (me / 1000.0) + "sec " + tripleCount + " triples (triples per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
            output.toString
          } else {
            System.err.println("Input file composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
            "Input file composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")"
          }
        } else {
          if (!validate) {
            output.toString
          } else {
            "Input file composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")"
          }
        }
      case Failure(e: ParseError) ⇒ {
        System.err.println(parser.formatError(e))
        parser.formatError(e)
      }
      case Failure(e) ⇒ {
        System.err.println(" Unexpected error during parsing run: " + e)
        " Unexpected error during parsing run: " + e
      }
    }
  }
}