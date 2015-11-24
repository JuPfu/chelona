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

import org.chelona.NTriplesParser.NTAST

import org.parboiled2._

import scala.collection.mutable
import scala.io.BufferedSource

import scala.util.{ Failure, Success }

object NTriplesParser extends NTripleAST {

  def apply(input: ParserInput, renderStatement: (NTripleAST) ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "", verbose: Boolean = true, trace: Boolean = false) = {
    new NTriplesParser(input, renderStatement, validate, basePath, label, verbose, trace)
  }

  sealed trait NTAST extends NTripleAST

  def parseAll(filename: String, inputBuffer: BufferedSource, renderStatement: (NTripleAST) ⇒ Int, validate: Boolean, base: String, label: String, verbose: Boolean, trace: Boolean, n: Int): Unit = {

    val ms: Double = System.currentTimeMillis

    var parseQueue = mutable.Queue[NTriplesParser]()
    val worker = new NTriplesThreadWorker(parseQueue, filename, validate, verbose, trace)

    worker.setName("NTriplesParser")
    worker.start()

    val lines = if (n < 50000) 100000 else if (n > 1000000) 1000000 else n

    var tripleCount: Long = 0L

    val iterator = inputBuffer.getLines()

    while (iterator.hasNext) {
      if (parseQueue.length > 1024) Thread.sleep(100) // a more sophiticated throtteling should be supplied
      asynchronous(NTriplesParser(iterator.take(lines).mkString("\n"), renderStatement, validate, base, label))
    }

    worker.shutdown()
    worker.join()

    tripleCount += worker.tripleCount

    while (parseQueue.nonEmpty) {
      val parser = parseQueue.dequeue()
      val res = parser.ntriplesDoc.run()
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

    def asynchronous(parser: NTriplesParser) = parseQueue.synchronized {
      parseQueue.enqueue(parser)
      if (parseQueue.nonEmpty) parseQueue.notify()
    }
  }
}

class NTriplesParser(val input: ParserInput, val renderStatement: (NTripleAST) ⇒ Int, validate: Boolean = false, val basePath: String = "http://chelona.org", val label: String = "", val verbose: Boolean = true, val trace: Boolean = false) extends Parser with StringBuilding with NTAST {

  import org.chelona.CharPredicates._

  import org.parboiled2.CharPredicate.{ Alpha, AlphaNum, HexDigit }

  private def hexStringToCharString(s: String) = s.grouped(4).map(cc ⇒ (Character.digit(cc(0), 16) << 12 | Character.digit(cc(1), 16) << 8 | Character.digit(cc(2), 16) << 4 | Character.digit(cc(3), 16)).toChar).filter(_ != '\u0000').mkString("")

  /*
 Parsing of the turtle data is done in the main thread.
 Evaluation of the abstract syntax tree for each turtle statement is passed to a separate thread "TurtleASTWorker".
 The ast evaluation procedure n3.renderStatement and the ast for a statement are placed in a queue.
 The abstract syntax trees of the Turtle statements are evaluated in sequence!
 Parsing continues immmediatly.
 ---P--- denotes the time for parsing a Turtle statement
 A       denotes administration time for the worker thread
 Q       denotes the time for enqueueing or dequeueing an ast of a Turtle statement
 ++E++   denotes the time for evaluating an ast of a Turtle statement
 Without worker thread parsing and evaluation of Turtle ast statements is done sequentially in one thread:
 main thread:   ---P---++E++---P---++E++---P---++E++---P---++E++---P---++E++---P---++E++...
 The main thread enqueues an ast of a parsed Turtle statement.
 The worker thread dequeues an ast of a Turtle statement and evaluates it.
 main thread:   AAAAA---P---Q---P---Q---P---Q---P---Q---P---Q---P---Q---P---...
 worker thread:               Q++E++   Q++E++      Q++E++Q++E++Q++E++ Q++E++
 Overhead for administration, e.g. waiting, notifying, joining and shutting down of the worker thread is not shown
 in the schematic illustration. Only some initial administrative effort is depicted. For small Turtle data it is
 usually faster to not use a worker thread due to the overhead involved to create, manage and dispose it.
 It takes some statements until catching up of the delay caused by the worker thread overhead is successful.
 For simple Turtle data, which consists mostly of simple s-p-o triples, the ast evaluation is rather short. The
 overhead for managing a worker thread compensates the time gain of evaluating the ast in a separate thread.
 +E+     denotes the time for evaluating an ast of a simple s-p-o Turtle statement
 main thread:   ---P---+E+---P---+E+---P---+E+---P---+E+---P---+E+---P---+E+...
 Use the 'thread' option for Turtle data which actually uses explicit Turtle syntax like prefixes,
 predicate object-lists, collections, etc.
 */

  var astQueue = mutable.Queue[(NTripleType ⇒ Int, NTripleType)]()
  val worker = new ASTThreadWorker(astQueue)

  if (!validate) {
    worker.setName("NTripleASTWorker")
    worker.start()
  }

  /*
   Enqueue ast for a NTriple statement
   */
  def asynchronous(ast: (NTripleType ⇒ Int, NTripleType)) = astQueue.synchronized {
    astQueue.enqueue(ast)
    if (astQueue.length > 10) astQueue.notify()
  }

  def ws = rule {
    quiet(anyOf(" \t").*)
  }

  //
  def comment = rule {
    quiet('#' ~ capture(noneOf("\n").*)) ~> ASTComment
  }

  //[1]	ntriplesDoc	::=	triple? (EOL triple)* EOL?
  def ntriplesDoc: Rule1[Long] = rule {
    (triple ~> ((ast: NTripleAST) ⇒
      if (!__inErrorAnalysis) {
        if (!validate) {
          asynchronous((renderStatement, ast)); 1
        } else
          ast match {
            case ASTComment(s) ⇒ 0
            case _             ⇒ 1
          }
      } else {
        if (!validate) {
          worker.join(10)
          worker.shutdown()
        }
        0
      }
    )).*(EOL) ~ EOL.? ~ EOI ~> ((v: Seq[Int]) ⇒ {
      if (!validate) {
        worker.shutdown()
        worker.join()

        while (astQueue.nonEmpty) {
          val (renderStatement, ast) = astQueue.dequeue()
          worker.sum += renderStatement(ast)
        }
      }

      if (validate) v.sum
      else worker.sum
    }
    )
  }

  //[2] triple	::=	subject predicate object '.'
  def triple: Rule1[NTripleAST] = rule {
    ws ~ (subject ~ predicate ~ `object` ~ '.' ~ ws ~ comment.? ~> ASTTriple | comment ~> ASTTripleComment) | quiet(anyOf(" \t").+) ~ push("") ~> ASTBlankLine
  }

  //[3]	subject	::=	IRIREF | BLANK_NODE_LABEL
  def subject = rule {
    run {
      cursorChar match {
        case '<' ⇒ IRIREF
        case '_' ⇒ BLANK_NODE_LABEL
        case _   ⇒ MISMATCH
      }
    } ~> ASTSubject
  }

  //[4]	predicate	::=	IRIREF
  def predicate: Rule1[ASTPredicate] = rule {
    IRIREF ~> ASTPredicate
  }

  //[5]	object	::=	IRIREF | BLANK_NODE_LABEL | literal
  def `object` = rule {
    run {
      cursorChar match {
        case '<' ⇒ IRIREF
        case '"' ⇒ literal
        case '_' ⇒ BLANK_NODE_LABEL
        case _   ⇒ MISMATCH
      }
    } ~> ASTObject
  }

  //[6]	literal	::=	STRING_LITERAL_QUOTE ('^^' IRIREF | LANGTAG)?
  def literal = rule {
    STRING_LITERAL_QUOTE ~ ws ~ (LANGTAG | '^' ~ '^' ~ ws ~ IRIREF).? ~> ASTLiteral ~ ws
  }

  //[144s]	LANGTAG	::=	'@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*
  def LANGTAG = rule {
    atomic("@" ~ capture(Alpha.+ ~ ('-' ~ AlphaNum.+).*)) ~ ws ~> ASTLangTag
  }

  //[7]	EOL	::=	[#xD#xA]+
  def EOL = rule {
    ('\r'.? ~ '\n').+
  }

  //[8]	IRIREF	::=	'<' ([^#x00-#x20<>"{}|^`\] | UCHAR)* '>'
  def IRIREF = rule {
    atomic('<' ~ clearSB ~ (IRIREF_CHAR ~ appendSB |
      !((("\\u000" | "\\u001" | "\\U0000000" | "\\U0000001") ~ HexDigit) |
        "\\u0020" | "\\U00000020" | "\\u0034" | "\\U00000034" |
        "\\u003C" | "\\u003c" | "\\U0000003C" | "\\U0000003c" |
        "\\u003E" | "\\u003e" | "\\U0000003E" | "\\U0000003e" |
        "\\u005C" | "\\u005c" | "\\U0000005C" | "\\U0000005c" |
        "\\u005E" | "\\u005e" | "\\U0000005E" | "\\U0000005E" |
        "\\u0060" | "\\U00000060" |
        "\\u007B" | "\\u007b" | "\\U0000007B" | "\\U0000007b" |
        "\\u007C" | "\\u007c" | "\\U0000007C" | "\\U0000007c" |
        "\\u007D" | "\\u007d" | "\\U0000007D" | "\\U0000007d") ~ UCHAR(false)).*) ~ push(sb.toString) ~ '>' ~> ((iri: String) ⇒ (test(isAbsoluteIRIRef(iri)) | fail("relative IRI not allowed: " + iri)) ~ push(iri)) ~> ASTIriRef ~ ws
  }

  //[9]	STRING_LITERAL_QUOTE	::=	'"' ([^#x22#x5C#xA#xD] | ECHAR | UCHAR)* '"'
  def STRING_LITERAL_QUOTE = rule {
    atomic('"' ~ clearSB ~ (noneOf("\"\\\r\n") ~ appendSB | UCHAR(true) | ECHAR).* ~ '"') ~ push(sb.toString) ~ ws ~> ASTStringLiteralQuote
  }

  //[141s]	BLANK_NODE_LABEL	::=	'_:' (PN_CHARS_U | [0-9]) ((PN_CHARS | '.')* PN_CHARS)?
  def BLANK_NODE_LABEL = rule {
    atomic("_:" ~ capture(PN_CHARS_U_DIGIT ~ (PN_CHARS | &(ch('.').+ ~ PN_CHARS) ~ ch('.').+ ~ PN_CHARS | isHighSurrogate ~ isLowSurrogate).*)) ~> ASTBlankNodeLabel ~ ws
  }

  //[10]	UCHAR	::=	'\\u' HEX HEX HEX HEX | '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
  def UCHAR(flag: Boolean) = rule {
    atomic("\\u" ~ capture(4.times(HexDigit))) ~> (maskQuotes(flag, _)) |
      atomic("\\U" ~ capture(8.times(HexDigit))) ~> (maskQuotes(flag, _))
  }

  //[153s]	ECHAR	::=	'\' [tbnrf"'\]
  def ECHAR = rule {
    atomic("\\" ~ appendSB ~ ECHAR_CHAR ~ appendSB)
  }

  private def isAbsoluteIRIRef(iriRef: String): Boolean = {
    iriRef.startsWith("//") || hasScheme(iriRef)
  }

  private def hasScheme(iri: String) = SchemeIdentifier(iri).scheme.run() match {
    case Success(s) ⇒ true
    case _          ⇒ false
  }

  private def maskQuotes(flag: Boolean, s: String) = {
    val c = hexStringToCharString(s)
    if (c.compare("\"") != 0)
      appendSB(c)
    else {
      if (flag)
        appendSB("\\" + c)
      else
        appendSB("\\u0022")
    }
  }
}