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

import scala.util.Success

object NTriplesParser extends NTripleAST {

  def apply(input: ParserInput, output: (String*) ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") = {
    new NTriplesParser(input, output, validate, basePath, label)
  }

  sealed trait NTAST extends NTripleAST
}

class NTriplesParser(val input: ParserInput, val output: (String*) ⇒ Int, validate: Boolean = false, val basePath: String = "http://chelona.org", val label: String = "") extends Parser with StringBuilding with NTAST {

  import org.chelona.CharPredicates._

  import org.parboiled2.CharPredicate.{ Alpha, AlphaNum, Digit, HexDigit }

  private def hexStringToCharString(s: String) = s.grouped(4).map(cc ⇒ (Character.digit(cc(0), 16) << 12 | Character.digit(cc(1), 16) << 8 | Character.digit(cc(2), 16) << 4 | Character.digit(cc(3), 16)).toChar).filter(_ != '\u0000').mkString("")

  val nt = new EvalNT(output, basePath, label)

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
  //[161s]
  implicit def wspStr(s: String): Rule0 = rule {
    quiet(str(s)) ~ ws
  }

  def ws = rule {
    quiet((anyOf(" \t").*))
  }

  //
  def comment = rule {
    quiet('#' ~ capture(noneOf("\n").*)) ~> ASTComment
  }

  //[1]	ntriplesDoc	::=	triple? (EOL triple)* EOL?
  def ntriplesDoc = rule {
    (triple ~> ((ast: NTripleAST) ⇒
      if (!__inErrorAnalysis) {
        if (!validate) {
          asynchronous((nt.renderStatement, ast)); 1
        } else
          ast match {
            case ASTComment(s) ⇒ 0
            case _             ⇒ 1
          }
      } else { if (!validate) { worker.join(10); worker.shutdown() }; 0 })).*(EOL) ~ EOL.? ~ EOI ~> ((v: Seq[Int]) ⇒ {
      if (!validate) {
        worker.join(10)
        worker.shutdown()

        while (!astQueue.isEmpty) {
          val (eval, ast) = astQueue.dequeue()
          worker.sum += eval(ast)
        }
      }

      if (validate) v.foldLeft(0)(_ + _)
      else worker.sum
    }
    )
  }

  //[2] triple	::=	subject predicate object '.'
  def triple: Rule1[NTripleAST] = rule {
    ws ~ (subject ~ predicate ~ obj ~ "." ~ comment.? ~> ASTTriple | comment ~> ASTTripleComment) | blank_line ~> ASTBlankLine
  }

  def blank_line = rule { quiet(capture(anyOf(" \t").+)) }

  //[3]	subject	::=	IRIREF | BLANK_NODE_LABEL
  def subject = rule {
    (IRIREF | BLANK_NODE_LABEL) ~> ASTSubject
  }

  //[4]	predicate	::=	IRIREF
  def predicate: Rule1[ASTPredicate] = rule {
    IRIREF ~> ASTPredicate
  }

  //[5]	object	::=	IRIREF | BLANK_NODE_LABEL | literal
  def obj = rule {
    (IRIREF | BLANK_NODE_LABEL | literal) ~> ASTObject
  }

  //[6]	literal	::=	STRING_LITERAL_QUOTE ('^^' IRIREF | LANGTAG)?
  def literal = rule {
    STRING_LITERAL_QUOTE ~ (ws ~ LANGTAG | "^^" ~ IRIREF).? ~> ASTLiteral ~ ws
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
      !(((str("\\u000") | str("\\u001") | str("\\U0000000") | str("\\U0000001")) ~ HexDigit) |
        str("\\u0020") | str("\\U00000020") | str("\\u0034") | str("\\U00000034") |
        str("\\u003C") | str("\\u003c") | str("\\U0000003C") | str("\\U0000003c") |
        str("\\u003E") | str("\\u003e") | str("\\U0000003E") | str("\\U0000003e") |
        str("\\u005C") | str("\\u005c") | str("\\U0000005C") | str("\\U0000005c") |
        str("\\u005E") | str("\\u005e") | str("\\U0000005E") | str("\\U0000005E") |
        str("\\u0060") | str("\\U00000060") |
        str("\\u007B") | str("\\u007b") | str("\\U0000007B") | str("\\U0000007b") |
        str("\\u007C") | str("\\u007c") | str("\\U0000007C") | str("\\U0000007c") |
        str("\\u007D") | str("\\u007d") | str("\\U0000007D") | str("\\U0000007d")) ~ UCHAR(false)).*) ~ push(sb.toString) ~ '>' ~> ((iri: String) ⇒ (test(isAbsoluteIRIRef(iri)) | fail("relative IRI not allowed: " + iri)) ~ push(iri)) ~> ASTIriRef ~ ws
  }

  //[9]	STRING_LITERAL_QUOTE	::=	'"' ([^#x22#x5C#xA#xD] | ECHAR | UCHAR)* '"'
  def STRING_LITERAL_QUOTE = rule {
    atomic('"' ~ clearSB ~ (noneOf("\"\\\r\n") ~ appendSB | UCHAR(true) | ECHAR).* ~ '"') ~ push(sb.toString) ~ ws ~> ASTStringLiteralQuote
  }

  //[141s]	BLANK_NODE_LABEL	::=	'_:' (PN_CHARS_U | [0-9]) ((PN_CHARS | '.')* PN_CHARS)?
  def BLANK_NODE_LABEL = rule {
    atomic(str("_:") ~ capture(PN_CHARS_U_DIGIT ~ (PN_CHARS | &(ch('.').+ ~ PN_CHARS) ~ ch('.').+ ~ PN_CHARS | isHighSurrogate ~ isLowSurrogate).*)) ~> ASTBlankNodeLabel ~ ws
  }

  //[10]	UCHAR	::=	'\\u' HEX HEX HEX HEX | '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
  def UCHAR(flag: Boolean) = rule {
    atomic(str("\\u") ~ capture(4.times(HexDigit))) ~> ((s: String) ⇒ maskQuotes(flag, s)) |
      atomic(str("\\U") ~ capture(8.times(HexDigit))) ~> ((s: String) ⇒ maskQuotes(flag, s))
  }

  //[153s]	ECHAR	::=	'\' [tbnrf"'\]
  def ECHAR = rule {
    atomic(str("\\") ~ appendSB ~ ECHAR_CHAR ~ appendSB)
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