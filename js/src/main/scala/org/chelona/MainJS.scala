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

//import java.io.{ BufferedWriter, File, OutputStreamWriter, Writer }
//import java.nio.charset.StandardCharsets

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
//import org.chelona.GetCmdLineArgs.Config

//import org.chelona.SPOReturnValue.SPOTriple
//import org.parboiled2.{ ErrorFormatter, ParseError, ParserInput }

//import scala.io.BufferedSource
//import scala.util.{ Failure, Success, Try }
@JSExport
object MainJS extends JSApp {
  @JSExport
  def main(): Unit = {
    /*
  val cmdLineArgs = argsParser.parse(args, Config())

  if (cmdLineArgs.isEmpty) {
    sys.exit(1)
  }

  if (cmdLineArgs.get.version) {
    System.err.println(chelona_version)
    sys.exit(2)
  }
*/
    println("Hello world from MAINJS!")

    val input = """<http://chelona.org/AHM-vocs> <http://purl.org/dc/terms/title> <http://chelona.org/Dataset> .
                  <http://chelona.org/AHM-vocs> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://chelona.org/Dataset> .
                  <http://chelona.org/AHM-vocs> <http://www.swi-prolog.org/rdf/library/source> <http://purl.org/collections/nl/am/> .
                  <http://chelona.org/AHM-vocs> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.swi-prolog.org/rdf/library/CloudNode> .
                  <http://chelona.org/AHM-vocs> <http://purl.org/dc/terms/title> "Amsterdam Museum (people database)"@nl .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/dataDump> <http://purl.org/collections/nl/am/> .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/subset> <http://chelona.org/am-people-rdagr2-schema.ttl> .
                  <http://chelona.org/AHM-vocs> <http://purl.org/dc/terms/title> "Amsterdam Museum (vocabularies)"@nl .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/subset> <http://chelona.org/am-people.ttl> .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/subset> <http://chelona.org/AHM-people> .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/subset> <http://chelona.org/ElementsGr2.rdf> .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/subset> <http://chelona.org/AHM-thesaurus> .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/subset> <http://chelona.org/AHM-alignments> .
                  <http://chelona.org/AHM-vocs> <http://www.swi-prolog.org/rdf/library/source> <http://chelona.org/Dataset> .
                  <http://chelona.org/AHM-vocs> <http://www.swi-prolog.org/rdf/library/source> <http://purl.org/collections/nl/am/> .
                  <http://chelona.org/AHM-vocs> <http://purl.org/dc/terms/title> "Amsterdam Museum (metadata)"@nl .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/subset> <http://chelona.org/am-schema.ttl> .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/subset> <http://chelona.org/am-data.ttl> .
                  <http://chelona.org/AHM-vocs> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://chelona.org/Dataset> .
                  <http://chelona.org/AHM-vocs> <http://www.swi-prolog.org/rdf/library/source> <http://purl.org/collections/nl/am/> .
                  <http://chelona.org/AHM-vocs> <http://purl.org/dc/terms/title> "Amsterdam Museum (thesaurus)"@nl .
                  <http://chelona.org/AHM-vocs> <http://chelona.org/dataDump> <http://chelona.org/am-thesaurus-schema.ttl> .
                  <http://chelona.org/AHM-alignments> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://chelona.org/am-thesaurus.ttl> .
                  <http://chelona.org/AHM-align-highprec> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://chelona.org/Linkset> .
                  <http://chelona.org/AHM-align-highprec> <http://www.swi-prolog.org/rdf/library/source> <http://purl.org/collections/ahm/> .
                  <http://chelona.org/AHM-align-highprec> <http://purl.org/dc/terms/title> "Amsterdam Museum vocabulary alignments"@nl .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> <http://chelona.org/AHM-align-midprec> .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> <http://chelona.org/AHM-align-highprec> .
                  <http://chelona.org/AHM-alignments> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://chelona.org/Dataset> .
                  <http://chelona.org/AHM-align-highprec> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.swi-prolog.org/rdf/library/CloudNode> .
                  <http://chelona.org/AHM-align-highprec> <http://www.swi-prolog.org/rdf/library/source> <http://purl.org/collections/ahm/> .
                  <http://chelona.org/AHM-align-highprec> <http://www.swi-prolog.org/rdf/library/source> "Amsterdam Museum"@nl .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> <http://chelona.org/AHM-people> .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> <http://chelona.org/AHM-thesaurus> .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> <http://chelona.org/AHM-metadata> .
                  <http://chelona.org/AHM-align-lowprec> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://chelona.org/AHM-alignments> .
                  <http://chelona.org/AHM-align-highprec> <http://www.swi-prolog.org/rdf/library/source> <http://chelona.org/Linkset> .
                  <http://chelona.org/AHM-align-highprec> <http://purl.org/dc/terms/title> <http://purl.org/collections/ahm/> .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> "Amsterdam Museum high precision vocabulary alignments"@nl .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> <http://chelona.org/alignments/am_to_aat_nonamb.ttl> .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> <http://chelona.org/alignments/am_to_dbp_high.ttl> .
                  <http://chelona.org/AHM-align-midprec> <http://purl.org/dc/terms/title> <http://chelona.org/alignments/am_to_ulannl_nonamb.ttl> .
                  <http://chelona.org/AHM-align-lowprec> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://chelona.org/alignments/am_to_geonl.ttl> .
                  <http://chelona.org/AHM-align-lowprec> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://chelona.org/Linkset> .
                  <http://chelona.org/AHM-align-lowprec> <http://www.swi-prolog.org/rdf/library/source> <http://purl.org/collections/ahm/> .
                  <http://chelona.org/AHM-align-lowprec> <http://purl.org/dc/terms/title> "Amsterdam Museum low precision vocabulary alignments"@nl .
                  <http://chelona.org/AHM-align-lowprec> <http://chelona.org/dataDump> <http://chelona.org/alignments/am_to_dbp_low_nonamb.ttl> .
                  <http://chelona.org/AHM-align-midprec> <http://chelona.org/dataDump> <http://chelona.org/Linkset> .
                  <http://chelona.org/AHM-align-midprec> <http://www.swi-prolog.org/rdf/library/source> <http://purl.org/collections/ahm/> .
                  <http://chelona.org/AHM-align-lowprec> <http://chelona.org/dataDump> "Amsterdam Museum mid precision vocabulary alignments"@nl .
                  <http://chelona.org/AHM-align-lowprec> <http://chelona.org/dataDump> <http://chelona.org/alignments/am_to_aat_amb.ttl> .
                  <http://chelona.org/AHM-align-lowprec> <http://chelona.org/dataDump> <http://chelona.org/alignments/am_to_ulannl_amb.ttl> .
                  """

    val input2 = "\t\n\n\t\t\n\t\n    \n    <s> #\n\n\n#\n\t\n \t\t<p>#\n\t\t\n    \n\t\t<o>#\n        \n   \n\n\n       .#"

    val input3 = "_:chinese a \"\"\"我喜歡中國的食物，\n侍者從不粗魯，\n想想很多事情，他們所做的打動，\n有毛澤東思想，道家思想，易經和國際象棋。\"\"\" ."

    JSLayer(input3, true, false)
    /*
    val file = new File("/Users/jp/Downloads/chelona/trig.ttl") //cmdLineArgs.get.file
    val validate = false // cmdLineArgs.get.validate
    val verbose = true // cmdLineArgs.get.verbose

    if (verbose) {
      System.err.println((if (!validate) "Convert: " else "Validate: ") + file.getCanonicalPath)
      System.err.flush()
    }

    val ms: Double = System.currentTimeMillis

    val inputfile: Try[BufferedSource] = Try {
      io.Source.fromFile(file)(StandardCharsets.UTF_8)
    }

    if (inputfile.isFailure) {
      System.err.println("Error: " + inputfile.failed.get)
      sys.exit(3)
    }

    val base = "" // cmdLineArgs.get.base
    val label = if (false /*cmdLineArgs.get.uid*/ ) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

    lazy val input: ParserInput = inputfile.get.mkString

    val output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))

    def tripleWriter(bo: Writer)(triple: List[SPOReturnValue]): Int = {
      triple.asInstanceOf[List[SPOTriple]].map(triple ⇒ bo.write(s"${triple.s.text} ${triple.p.text} ${triple.o.text} .\n")).length
    }
    */
    /*
    val parser = ChelonaParser(input, tripleWriter(output) _, validate, base, label)

    val res = parser.turtleDoc.run()

    output.close()

    res match {
      case Success(tripleCount) ⇒
        val me: Double = System.currentTimeMillis - ms
        if (verbose) {
          if (!validate) {
            System.err.println("Input file '" + file.getCanonicalPath + "' converted in " + (me / 1000.0) + "sec " + tripleCount + " triples (triples per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
          } else {
            System.err.println("Input file '" + file.getCanonicalPath + "' composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
          }
        }
      case Failure(e: ParseError) ⇒ if (true /*!cmdLineArgs.get.trace*/ ) System.err.println("File '" + file.getCanonicalPath + "': " + parser.formatError(e)) else System.err.println("File '" + file.getCanonicalPath + "': " + parser.formatError(e, new ErrorFormatter(showTraces = true)))
      case Failure(e)             ⇒ System.err.println("File '" + file.getCanonicalPath + "': Unexpected error during parsing run: " + e)
    }

  */
    System.out.println("EXITING MAINJS")
  }
}
