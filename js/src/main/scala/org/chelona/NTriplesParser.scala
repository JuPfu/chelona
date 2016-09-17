/*
* Copyright (C) 2014, 2015, 2016 Juergen Pfundt
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
import scala.scalajs.js.annotation.JSExport
import scala.util.{ Failure, Success }

@JSExport
object NTriplesParser extends NTripleAST {

  def apply(input: ParserInput, renderStatement: (NTripleAST) ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "", verbose: Boolean = true, trace: Boolean = false) = {
    new NTriplesParser(input, renderStatement, validate, basePath, label, verbose, trace)
  }

  sealed trait NTAST extends NTripleAST
}

class NTriplesParser(val input: ParserInput, val renderStatement: (NTripleAST) ⇒ Int, validate: Boolean = false, val basePath: String = "http://chelona.org", val label: String = "", val verbose: Boolean = true, val trace: Boolean = false) extends Parser with StringBuilding with NTAST {

  import org.chelona.CharPredicates._
  import org.parboiled2.CharPredicate.{ Alpha, AlphaNum, HexDigit }

  private def hexStringToCharString(s: String) = s.grouped(4).map(cc ⇒ (Character.digit(cc(0), 16) << 12 | Character.digit(cc(1), 16) << 8 | Character.digit(cc(2), 16) << 4 | Character.digit(cc(3), 16)).toChar).filter(_ != '\u0000').mkString("")

  def ws = rule {
    quiet(anyOf(" \t").*)
  }

  //
  def comment = rule {
    quiet('#' ~ capture(noneOf("\n").*)) ~> ASTComment
  }

  //[1]	ntriplesDoc	::=	triple? (EOL triple)* EOL?
  def ntriplesDoc: Rule1[Int] = rule {
    (triple ~> ((ast: NTripleAST) ⇒
      if (!__inErrorAnalysis) {
        if (!validate) {
          renderStatement(ast)
        } else
          ast match {
            case ASTComment(s) ⇒ 0
            case _             ⇒ 1
          }
      } else {
        0
      })).*(EOL) ~ EOL.? ~ EOI ~> ((v: Seq[Int]) ⇒ {
      v.sum
    })
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

  private def hasScheme(iri: String) = SchemeIdentifier(iri)

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