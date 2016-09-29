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

import scala.collection.mutable

final class ASTThreadWorker[T](astQueue: mutable.Queue[(T ⇒ Int, T)]) extends Thread {

  @volatile var terminated = false
  var sum = 0L

  final def poll(): Option[(T ⇒ Int, T)] = astQueue.synchronized {
    while (astQueue.isEmpty && !terminated) astQueue.wait(10)
    if (!terminated) Some(astQueue.dequeue()) else None
  }

  import scala.annotation.tailrec

  @tailrec final override def run() = poll() match {
    case Some((renderStatement, ast)) ⇒
      sum += renderStatement(ast); run()
    case None ⇒
  }

  final def shutdown() = astQueue.synchronized {
    if (!terminated) {
      terminated = true
      astQueue.notify()
    }
  }

  final def quit() = astQueue.synchronized {
    terminated = true
    astQueue.clear()
  }

}

