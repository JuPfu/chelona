/*
* Copyright (C) 2014-2015 Juergen Pfundt
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

object NTMain extends App {

  val cmdLineArgs = argsParser.parse(args, Config())

  if (cmdLineArgs.isEmpty) {
    sys.exit(1)
  }

  if (cmdLineArgs.get.version) {
    System.err.println(chelona_version)
    sys.exit(2)
  }

  val file = cmdLineArgs.get.file
  val validate = cmdLineArgs.get.validate
  val verbose = cmdLineArgs.get.verbose

  if (verbose) {
    System.err.println((if (!validate) "Convert: " else "Validate: ") + file.head.getCanonicalPath)
  }

  val inputfile: Try[BufferedSource] = Try { io.Source.fromFile(file.head)(StandardCharsets.UTF_8) }

  if (inputfile.isFailure) {
    System.err.println("Error: " + inputfile.failed.get)
    sys.exit(3)
  }

  val base = cmdLineArgs.get.base
  val label = if (cmdLineArgs.get.uid) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString else ""

  val trace = cmdLineArgs.get.trace

  val output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))

  /*
    This is the user defined procedure, which contains your business logic.
   */
  def ntWriter(bo: Writer)(s: NTripleElement, p: NTripleElement, o: NTripleElement): Int = {
    /* Modify the output string to emit subject (s.text), predicate(p.text) and object (o.text) fitted to your needs. */
    /* Identification or filtering with respect to token type can be done via attribute tokenType, e.g. */
    /* if ( s.tokenType & NTripleBitValue.IRIREF ) {
          // e.g. strip leading '<' and trailing '>'
       }
       else if ( s.tokenType & NTripleBitValue.STRING_LITERAL_QUOTE ) {
          if ( s.tokenType & NTripleBitValue.LANGTAG ) {
             // e.g. only select english strings
          }
          // e.g. strip leading '"' and trailing '"'
       }
       else if ( s.tokenType & NTripleBitValue.BLANK_NODE ) {
          // e.g. replace blank_node by absolute IRIREF
       }
    */
    bo.write(s"${s.text} ${p.text} ${o.text} .\n"); 1
  }

  /* AST evaluation procedure. Here is the point to provide your own flavour, if you dare. */
  val evalNT = new EvalNT(ntWriter(output)_, base, label)

  /* Looping in steps of n lines through the input file.
     Gigabyte or Terrabyte sized files can be converted, while heap size needed should be a maximum of about 1 GB
     for n chosen to be about 100000 lines.
  */
  NTriplesParser.parseAll(file.head.getCanonicalPath, inputfile.get, evalNT.renderStatement, validate, base, label, verbose, trace, 50000)

  output.close()
}
