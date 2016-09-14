package org.chelona

import java.io._
import java.nio.charset.{ Charset, StandardCharsets }

import org.chelona.SPOReturnValue.SPOTriple
import org.parboiled2.{ ErrorFormatter, ParseError, ParserInput }

import scala.util.{ Failure, Success }

object JSLayer {

  def apply(rdf_input: String, verbose: Boolean = true, validate: Boolean = false) = {
    new JSLayer(rdf_input, verbose, validate)
  }
}

class JSLayer(rdf_input: String, verbose: Boolean, validate: Boolean) {

  val ms: Double = System.currentTimeMillis

  val base = "" // cmdLineArgs.get.base
  val label = if (false /*cmdLineArgs.get.uid*/ ) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

  lazy val input: ParserInput = rdf_input

  //val output = /*new BufferedWriter(*/ new OutputStreamWriter(System.out, "UTF-8" /*StandardCharsets.UTF_8*/ ) /*)*/

  def tripleWriter(triple: List[SPOReturnValue]): Int = {
    triple.map { case SPOTriple(TurtleElement(s, l1), TurtleElement(p, l2), TurtleElement(o, l3)) ⇒ { System.out.println(s"${s} ${p} ${o} .") } }.length
  }

  val file = "chelona.ttl"
  val parser = ChelonaParser(input, tripleWriter, validate, base, label)

  val res = parser.turtleDoc.run()

  //output.close()

  res match {
    case Success(tripleCount) ⇒
      val me: Double = System.currentTimeMillis - ms
      if (verbose) {
        if (!validate) {
          System.err.println("Input file '" + file + "' converted in " + (me / 1000.0) + "sec " + tripleCount + " triples (triples per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
        } else {
          System.err.println("Input file '" + file + "' composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
        }
      }
    case Failure(e: ParseError) ⇒ if (true /*!cmdLineArgs.get.trace*/ ) System.err.println("File '" + file + "': " + parser.formatError(e)) else System.err.println("File '" + file + "': " + parser.formatError(e, new ErrorFormatter(showTraces = true)))
    case Failure(e)             ⇒ System.err.println("File '" + file + "': Unexpected error during parsing run: " + e)
  }
}
