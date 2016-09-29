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

import org.parboiled2._

import scala.collection.mutable
import scala.util.{ Failure, Success }

final class NQuadsThreadWorker(parseQueue: mutable.Queue[NQuadParser], filename: String, validate: Boolean, verbose: Boolean, trace: Boolean) extends Thread {

  var terminated = false

  var tripleCount = 0L

  final def poll(): Option[NQuadParser] = parseQueue.synchronized {
    while (parseQueue.isEmpty && !terminated) parseQueue.wait()
    if (!terminated) Some(parseQueue.dequeue()) else None
  }

  import scala.annotation.tailrec

  @tailrec final override def run() = poll() match {
    case Some((parser)) ⇒
      val res = parser.nquadsDoc.run()
      res match {
        case Success(count)         ⇒ tripleCount += count
        case Failure(e: ParseError) ⇒ if (!trace) System.err.println("File '" + filename + "': " + parser.formatError(e, new ChelonaErrorFormatter(block = tripleCount))) else System.err.println("File '" + filename + "': " + parser.formatError(e, new ChelonaErrorFormatter(block = tripleCount, showTraces = true)))
        case Failure(e)             ⇒ System.err.println("File '" + filename + "': Unexpected error during parsing run: " + e)
      }
      run()
    case None ⇒
  }

  final def shutdown() = parseQueue.synchronized {
    if (!terminated) {
      terminated = true
      parseQueue.notify()
    }
  }
}
