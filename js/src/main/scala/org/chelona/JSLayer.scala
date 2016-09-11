package org.chelona

import java.io._
import java.nio.charset.StandardCharsets

import org.chelona.SPOReturnValue.SPOTriple
import org.parboiled2.{ ErrorFormatter, ParseError, ParserInput }

import scala.util.{ Failure, Success }

/**
 * Created by jp on 15.08.16.
 */
object JSLayer {

  def apply(rdf_input: String, verbose: Boolean = true, validate: Boolean = false) = {
    new JSLayer(rdf_input, verbose, validate)
  }
}

class JSLayer(rdf_input: String, verbose: Boolean, validate: Boolean) {
  System.out.println("AT BEGINNING OF JSLAYER " + rdf_input)

  //val file = new File(fileName) //cmdLineArgs.get.file
  //val validate = false // cmdLineArgs.get.validate
  //val verbose = true // cmdLineArgs.get.verbose
  /*
  if (verbose) {
    System.err.println((if (!validate) "Convert: " else "Validate: ") + file.getCanonicalPath)
    System.err.flush()
  }
*/
  val ms: Double = System.currentTimeMillis
  /*
  val inputfile: Try[InputStreamReader] = Try {
    new InputStreamReader(new FileInputStream (fileName),StandardCharsets.UTF_8 )
    //io.Source.fromFile(fileName)(StandardCharsets.UTF_8)
  }

  if (inputfile.isFailure) {
    System.err.println("Error: " + inputfile.failed.get)
    sys.exit(3)
  }
*/
  //  val path = Paths.get(fileName)
  /*
  val fileChannel: Try[FileChannel] = Try { FileChannel.open(path, util.EnumSet.of(StandardOpenOption.READ)) }

  if (fileChannel.isFailure) {
    System.err.println("Error: " + fileChannel.failed.get)
    sys.exit(3)
  }
*/
  //val file = path.toFile.getCanonicalPath
  /*
  val f = new FileInputStream (fileName)
  System.err.println("FileInputStream " + fileName + " size = " + f.getChannel().size())
*/
  //System.err.println("File "+file + " size = " + path.toFile.length())

  val base = "" // cmdLineArgs.get.base
  val label = if (false /*cmdLineArgs.get.uid*/ ) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

  lazy val input: ParserInput = rdf_input

  val output = /*new BufferedWriter(*/ new OutputStreamWriter(System.out, StandardCharsets.UTF_8) /*)*/

  def tripleWriter(bo: Writer)(triple: List[SPOReturnValue]): Int = {
    System.err.println("triple= " + triple)
    triple.asInstanceOf[List[SPOTriple]].map(triple ⇒ bo.write(s"${triple.s.text} ${triple.p.text} ${triple.o.text} .\n")).length
  }
  /**/

  val file = "chelona.ttl"
  val parser = ChelonaParser(input, tripleWriter(output) _, validate, base, label)

  val res = parser.turtleDoc.run()

  output.close()

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

  System.out.println("EXITING JSLayer")
}
