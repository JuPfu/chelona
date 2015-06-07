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

import java.io.{ OutputStreamWriter, BufferedWriter }
import java.nio.charset.StandardCharsets

import org.chelona.GetCmdLineArgs._
import org.parboiled2.{ ErrorFormatter, ParseError, ParserInput }

import scala.io.BufferedSource
import scala.util.{ Try, Success, Failure }

object TrigMain extends App {

  val cmdLineArgs = argsParser.parse(args, Config())

  if (cmdLineArgs == None) {
    sys.exit(1)
  }

  if (cmdLineArgs.get.version) {
    System.err.println("Cheló̱n version 1.0")
    sys.exit(2)
  }

  val file = cmdLineArgs.get.file
  val validate = cmdLineArgs.get.validate
  val verbose = cmdLineArgs.get.verbose

  if (verbose) {
    System.err.println((if (!validate) "Convert: " else "Validate: ") + file.head)
    System.err.flush()
  }

  val ms: Double = System.currentTimeMillis

  val inputfile: Try[BufferedSource] = Try { io.Source.fromFile(file.head)(StandardCharsets.UTF_8) }

  if (inputfile.isFailure) {
    System.err.println("Error: " + inputfile.failed.get)
    sys.exit(3)
  }

  val base = cmdLineArgs.get.base
  val label = if (cmdLineArgs.get.uid) java.util.UUID.randomUUID.toString.filter((c: Char) ⇒ c != '-').mkString("") else ""

  lazy val input: ParserInput = inputfile.get.mkString

  val output = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8))

  val parser = TrigParser(input, output, validate, base, label)

  val res = parser.trigDoc.run()

  output.close()

  res match {
    case Success(tripleCount) ⇒
      val me: Double = System.currentTimeMillis - ms
      if (verbose) {
        if (!validate) {
          System.err.println("Input file '" + file.head + "' converted in " + (me / 1000.0) + "sec " + tripleCount + " quads (quads per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
        } else {
          System.err.println("Input file '" + file.head + "' composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
        }
      }
    case Failure(e: ParseError) ⇒ if (!cmdLineArgs.get.trace) System.err.println("File '" + file.head + "': " + parser.formatError(e)) else System.err.println("File '" + file.head + "': " + parser.formatError(e, new ErrorFormatter(showTraces = true)))
    case Failure(e)             ⇒ System.err.println("File '" + file.head + "': Unexpected error during parsing run: " + e)
  }
}
