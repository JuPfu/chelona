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

object NTMain extends App with JSONLDFlatOutput {

  /* get command line arguments */
  val cmdLineArgs = argsParser.parse(args, Config())

  if (cmdLineArgs.isEmpty) {
    sys.exit(1)
  }

  if (cmdLineArgs.get.version) {
    System.err.println(chelona_version)
    sys.exit(2)
  }

  val file = cmdLineArgs.get.file.head
  val validate = cmdLineArgs.get.validate
  val verbose = cmdLineArgs.get.verbose

  if (verbose) {
    System.err.println((if (!validate) "Convert: " else "Validate: ") + file.getCanonicalPath)
  }

  val inputfile: Try[BufferedSource] = Try { io.Source.fromFile(file)(StandardCharsets.UTF_8) }

  if (inputfile.isFailure) {
    System.err.println("Error: " + inputfile.failed.get)
    sys.exit(3)
  }

  val base = cmdLineArgs.get.base
  val label = if (cmdLineArgs.get.uid) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString else ""

  val trace = cmdLineArgs.get.trace

  /* open output stream */
  val output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))

  /* initialize output */
  jsonldFlatWriterInit(output)()

  /* AST evaluation procedure. Here is the point to provide your own flavour, if you like. */
  val evalNT = new EvalNTriples(jsonLDFlatWriter(output)_, base, label)

  /* Looping in steps of n lines through the input file.
     Gigabyte or Terrabyte sized files can be converted, while heap size needed should be a maximum of about 1 GB
     for n chosen to be about 100000 lines.
  */
  NTriplesParser.parseAll(file.getCanonicalPath, inputfile.get, evalNT.renderStatement, validate, base, label, verbose, trace, 250000)

  /* finalize output */
  jsonldFlatWriterTrailer(output)()

  output.close()
}
