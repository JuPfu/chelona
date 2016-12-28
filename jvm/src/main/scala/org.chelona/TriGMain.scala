/*
* Copyright (C) 2014-2016 Juergen Pfundt
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

import org.chelona.GetCmdLineArgs._

import org.parboiled2.{ ErrorFormatter, ParseError, ParserInput }

import java.io.{ BufferedWriter, OutputStreamWriter }
import java.nio.charset.StandardCharsets

import scala.io.BufferedSource
import scala.util.{ Failure, Success, Try }

object TriGMain extends App {

  // parse command line arguments
  val cmdLineArgs = argsParser.parse(args, Config())

  // exit program if no command line arguments had been found
  if (cmdLineArgs.isEmpty) {
    sys.exit(1)
  }

  // display chelona version if requested and exit program
  if (cmdLineArgs.get.version) {
    System.err.println(chelona_version)
    sys.exit(2)
  }

  // fetch command line arguments from command line parser
  val file = cmdLineArgs.get.file.head
  val validate = cmdLineArgs.get.validate
  val verbose = cmdLineArgs.get.verbose
  val fmt = cmdLineArgs.get.fmt

  // if verbose mode had been requested, write some information to standard error
  if (verbose) {
    System.err.println((if (!validate) "Convert: " else "Validate: ") + file)
    System.err.flush()
  }

  // start timer
  val ms: Double = System.currentTimeMillis

  // try to open input file using UTF_8 charset
  val inputfile: Try[BufferedSource] = Try { io.Source.fromFile(file)(StandardCharsets.UTF_8) }

  // if input file could not be opened, write error message to standard error and exit program
  if (inputfile.isFailure) {
    System.err.println("Error: " + inputfile.failed.get)
    sys.exit(3)
  }

  // fetch base definition from command line parser
  val base = cmdLineArgs.get.base
  // if uid had been requested from the command line, build unique random label
  val label = if (cmdLineArgs.get.uid) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

  // prepare input data for TriG parser
  lazy val input: ParserInput = inputfile.get.mkString

  // output is written to standard out
  val output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))

  // import dependencies for selected output format
  import RDFTriGOutput._
  import JSONLDFlatOutput._

  /* select output format requested from command line */
  val writer = if (fmt.equals("n3")) trigWriter(output) _ else jsonLDFlatWriterQuad(output) _

  if (fmt.equals("json-ld")) {
    /* initialize json-ld output */
    jsonldFlatWriterInit(output)()
  }

  // create parser for TriG data
  val parser = TriGParser(input, writer, validate, base, label)

  // run parser
  val res = parser.trigDoc.run()

  // write termination sequence
  if (fmt.equals("json-ld")) {
    /* finalize json-ld output */
    jsonldFlatWriterTrailer(output)()
  }

  output.close()

  // depending on outcome of parsing the TriG data, write some information to standard error
  res match {
    case Success(tripleCount) ⇒
      val me: Double = System.currentTimeMillis - ms
      if (verbose) {
        if (!validate) {
          System.err.println("Input file '" + file.getCanonicalPath + "' converted in " + (me / 1000.0) + "sec " + tripleCount + " quads (quads per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
        } else {
          System.err.println("Input file '" + file.getCanonicalPath + "' composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
        }
      }
    case Failure(e: ParseError) ⇒ if (!cmdLineArgs.get.trace) System.err.println("File '" + file.getCanonicalPath + "': " + parser.formatError(e)) else System.err.println("File '" + file.getCanonicalPath + "': " + parser.formatError(e, new ErrorFormatter(showTraces = true)))
    case Failure(e)             ⇒ System.err.println("File '" + file.getCanonicalPath + "': Unexpected error during parsing run: " + e)
  }
}
