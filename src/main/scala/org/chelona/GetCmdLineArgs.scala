/*
* Copyright (C) 2014 Juergen Pfundt
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

import java.io.File

import scopt._

object GetCmdLineArgs {

  case class Config(validate: Boolean = false,
                    out: File = new File("."),
                    files: Seq[File] = Seq(),
                    verbose: Boolean = false,
                    version: Boolean = false,
                    debug: Boolean = false)

  val argsParser = new OptionParser[Config]("Cheló̱na") {
    head("Cheló̱na", "0.8")
    help("help") text ("prints this usage text")
    opt[Unit]('v', "validate") action { (_, c) ⇒ c.copy(validate = true) } text ("validate input file")
    opt[Unit]("verbose") action { (_, c) ⇒ c.copy(verbose = true) } text ("give some additional information")
    opt[Unit]("version") action { (_, c) ⇒ c.copy(version = true) } text ("Cheló̱na version information")
    arg[File]("<file>...") unbounded () minOccurs (1) action { (x, c) ⇒ c.copy(files = c.files :+ x) } text ("input ttl-file")
  }
}
