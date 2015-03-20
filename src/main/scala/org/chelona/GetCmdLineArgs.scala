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
                    file: Seq[File] = Seq(),
                    verbose: Boolean = false,
                    uid: Boolean = false,
                    base: String = "http://chelona.org",
                    out: String = "N3",
                    version: Boolean = false,
                    trace: Boolean = false)

  val argsParser = new OptionParser[Config]("Cheló̱na") {
    head("cheló̱na", "version 0.9")
    help("help") text "prints this usage text"
    opt[Unit]("verbose") action { (_, c) ⇒ c.copy(verbose = true) } text "give some additional information"
    opt[Unit]("version") action { (_, c) ⇒ c.copy(version = true) } text "Cheló̱na version information"
    opt[Unit]('v', "validate") action { (_, c) ⇒ c.copy(validate = true) } text "validate input file"
    opt[Unit]('t', "trace") action { (_, c) ⇒ c.copy(trace = true) } text "display error trace"
    opt[Unit]('u', "uid") action { (_, c) ⇒ c.copy(uid = true) } text "use UID for blank nodes"
    opt[String]('b', "base") optional () action { (x, c) ⇒ c.copy(base = x) } text "base URI"
    opt[String]('f', "fmt") optional () action { (x, c) ⇒ c.copy(out = x.toLowerCase) } text "output format"
    arg[File]("<file>") minOccurs (1) maxOccurs (1) valueName ("<file>") action { (x, c) ⇒ c.copy(file = c.file :+ x) } text "input ttl-file"
  }
}
