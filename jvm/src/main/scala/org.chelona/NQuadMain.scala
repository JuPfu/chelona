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

import java.io.{ Writer, OutputStreamWriter, BufferedWriter }
import java.nio.charset.StandardCharsets

import org.chelona.GetCmdLineArgs._

import scala.io.BufferedSource
import scala.util.Try

object NQuadMain extends App {

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
  val file = cmdLineArgs.get.file.head.getCanonicalPath()
  val validate = cmdLineArgs.get.validate
  val verbose = cmdLineArgs.get.verbose
  val fmt = cmdLineArgs.get.fmt

  // if verbose mode had been requested, write some information to standard error
  if (verbose) {
    System.err.println((if (!validate) "Convert: " else "Validate: ") + file)
  }

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

  // pass on some more detailed information from parboiled in case of error
  val trace = cmdLineArgs.get.trace

  // output is written to standard out
  val output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))

  // import dependencies for selected output format
  import RDFQuadOutput._
  import JSONLDFlatOutput._

  // select output format requested from command line
  val writer = if (fmt.equals("n3")) quadWriter(output) _ else jsonLDFlatWriter(output)_

  if (fmt.equals("json-ld")) {
    /* initialize json-ld output */
    jsonldFlatWriterInit(output)()
  }

  /* Looping in steps of n lines through the input file.
   Gigabyte sized files can be converted, while heap size needed should be a maximum of about 1 GB
   for n chosen to be about 100000 lines.
  */
  NQuadParser.parseAll(file, inputfile.get, writer, validate, base, label, verbose, trace, 250000)

  // write termination sequence
  if (fmt.equals("json-ld")) {
    /* finalize json-ld output */
    jsonldFlatWriterTrailer(output)()
  }

  output.close()
}

