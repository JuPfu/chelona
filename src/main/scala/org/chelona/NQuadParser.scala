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

import org.chelona.NQuadParser.NQAST

import org.parboiled2._

import scala.collection.mutable
import scala.io.BufferedSource
import scala.util.{ Failure, Success }

object NQuadParser {

  def apply(input: ParserInput, renderStatement: (NTripleAST) ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") = {
    new NQuadParser(input, renderStatement, validate, basePath, label)
  }

  sealed trait NQAST extends NQuadAST

  def parseAll(filename: String, inputBuffer: BufferedSource, renderStatement: (NTripleAST) ⇒ Int, validate: Boolean, base: String, label: String, verbose: Boolean, trace: Boolean, n: Int): Unit = {

    val ms: Double = System.currentTimeMillis

    var parseQueue = mutable.Queue[NQuadParser]()
    val worker = new NQuadsThreadWorker(parseQueue, filename, validate, verbose, trace)

    worker.setName("NQuadParser")
    worker.start()

    val lines = if (n < 50000) 100000 else if (n > 1000000) 1000000 else n

    var tripleCount: Long = 0L

    val iterator = inputBuffer.getLines()

    while (iterator.hasNext) {
      if (parseQueue.length > 1024) Thread.sleep(100) // a more sophiticated throtteling should be supplied
      asynchronous(NQuadParser(iterator.take(lines).mkString("\n"), renderStatement, validate, base, label))
    }

    worker.shutdown()
    worker.join()

    tripleCount += worker.tripleCount

    while (parseQueue.nonEmpty) {
      val parser = parseQueue.dequeue()
      val res = parser.nquadsDoc.run()
      res match {
        case Success(count)         ⇒ tripleCount += count
        case Failure(e: ParseError) ⇒ if (!trace) System.err.println("File '" + filename + "': " + parser.formatError(e, new ChelonaErrorFormatter(block = tripleCount))) else System.err.println("File '" + filename + "': " + parser.formatError(e, new ChelonaErrorFormatter(block = tripleCount, showTraces = true)))
        case Failure(e)             ⇒ System.err.println("File '" + filename + "': Unexpected error during parsing run: " + e)
      }
    }

    if (verbose) {
      val me: Double = System.currentTimeMillis - ms
      if (!validate) {
        System.err.println("Input file '" + filename + "' converted in " + (me / 1000.0) + "sec " + tripleCount + " triples (triples per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
      } else {
        System.err.println("Input file '" + filename + "' composed of " + tripleCount + " statements successfully validated in " + (me / 1000.0) + "sec (statements per second = " + ((tripleCount * 1000) / me + 0.5).toInt + ")")
      }
    }

    def asynchronous(parser: NQuadParser) = parseQueue.synchronized {
      parseQueue.enqueue(parser)
      if (parseQueue.nonEmpty) parseQueue.notify()
    }
  }
}

class NQuadParser(input: ParserInput, renderStatement: (NTripleAST) ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") extends NTriplesParser(input: ParserInput, renderStatement: (NTripleAST) ⇒ Int, validate, basePath, label) with NQAST {

  //[1]	nquadsDoc	::=	statement? (EOL statement)* EOL?
  def nquadsDoc = rule {
    (statement ~> ((ast: NTripleType) ⇒
      if (!__inErrorAnalysis) {
        if (!validate) {
          asynchronous((renderStatement, ast)); 1
        } else
          ast match {
            case ASTComment(s) ⇒ 0
            case _             ⇒ 1
          }
      } else { if (!validate) { worker.join(10); worker.shutdown() }; 0 })).*(EOL) ~ EOL.? ~ EOI ~> ((v: Seq[Int]) ⇒ {
      if (!validate) {
        worker.shutdown()
        worker.join()

        while (astQueue.nonEmpty) {
          val (renderStatement, ast) = astQueue.dequeue()
          worker.sum += renderStatement(ast)
        }
      }

      if (validate) v.sum else worker.sum
    }
    )
  }

  //[2]	statement	::=	subject predicate object graphLabel? '.'
  def statement: Rule1[NTripleType] = rule {
    ws ~ (subject ~ predicate ~ `object` ~ graphLabel.? ~ '.' ~ ws ~ comment.? ~> ASTStatement | comment ~> ASTTripleComment) | quiet(anyOf(" \t").+) ~ push("") ~> ASTBlankLine
  }

  //[6]	graphLabel	::=	IRIREF | BLANK_NODE_LABEL
  def graphLabel = rule {
    (IRIREF | BLANK_NODE_LABEL) ~> ASTGraphLabel
  }
}
