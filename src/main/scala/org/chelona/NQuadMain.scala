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

object NQuadMain extends App {

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
  val label = if (cmdLineArgs.get.uid) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

  val trace = cmdLineArgs.get.trace

  val output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))

  def quadWriter(bo: Writer)(s: NQuadElement, p: NQuadElement, o: NQuadElement, g: NQuadElement): Int = {
    bo.write(s"${s.text} ${p.text} ${o.text}" + (if (g.text.isEmpty) " .\n" else s" ${g.text} .\n")); 1
  }

  val evalQuad = new EvalNQuad(quadWriter(output)_, base, label)

  NQuadParser.parseAll(file.head.getName, inputfile.get, evalQuad.renderStatement, validate, base, label, verbose, trace, 250000)

  output.close()
}

