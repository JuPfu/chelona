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

import org.chelona.ChelonaParser._
import org.parboiled2._

import scala.scalajs.js.annotation.JSExport

@JSExport
object ChelonaParser {

  def apply(input: ParserInput, renderStatement: TurtleAST ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") = {
    new ChelonaParser(input, renderStatement, validate, basePath, label)
  }
}

class ChelonaParser(val input: ParserInput, val renderStatement: TurtleAST ⇒ Int, validate: Boolean = false, val basePath: String = "http://chelona.org", val label: String = "") extends Parser with StringBuilding {

  import scala.collection.mutable

  import org.chelona.CharPredicates._
  import org.parboiled2.CharPredicate.{ Alpha, AlphaNum, Digit, HexDigit }

  import TurtleAST._

  private def hexStringToCharString(s: String) = s.grouped(4).map(cc ⇒ (Character.digit(cc(0), 16) << 12 | Character.digit(cc(1), 16) << 8 | Character.digit(cc(2), 16) << 4 | Character.digit(cc(3), 16)).toChar).filter(_ != '\u0000').mkString("")

  val prefixMap = scala.collection.mutable.Map.empty[String, String]

  //[161s]
  implicit def wspStr(s: String): Rule0 = rule {
    quiet(str(s)) ~ ws
  }

  def ws = rule { quiet((anyOf(" \n\r\t").+ | anyOf(" \t").* ~ '#' ~ noneOf("\n\r").*).*) }

  //[1] turtleDoc 	::= 	statement*
  def turtleDoc = rule {
    anyOf(" \n\r\t").* ~ (statement ~> ((ast: TurtleAST) ⇒
      if (!__inErrorAnalysis) {
        if (!validate) {
          renderStatement(ast)
        } else
          ast match {
            case ASTStatement(ASTComment(s)) ⇒ 0
            case _                           ⇒ 1
          }
      } else { 0 })).* ~ EOI ~> ((v: Seq[Int]) ⇒ {
      v.sum
    })
  }

  //[2] statement 	::= 	directive | triples '.'
  def statement: Rule1[TurtleAST] = rule {
    (directive | triples ~ "." | comment) ~> ASTStatement
  }

  //
  def comment = rule {
    quiet('#' ~ capture(noneOf("\n").*) ~> ASTComment ~ '\n' ~ anyOf(" \n\r\t").*)
  }

  //[3] directive 	::= 	prefixID | base | sparqlPrefix | sparqlBase
  def directive = rule {
    (prefixID | base | sparqlPrefix | sparqlBase) ~> ASTDirective
  }

  //[4] prefixID 	::= 	'@prefix' PNAME_NS IRIREF '.'
  def prefixID = rule {
    atomic("@prefix") ~ PNAME_NS ~ ws ~ IRIREF ~> ((p: ASTPNameNS, i: ASTIriRef) ⇒ run(definePrefix(p, i)) ~ push(p) ~ push(i)) ~> ASTPrefixID ~ ws ~ "."
  }

  //[5] base 	::= 	'@base' IRIREF '.'
  def base = rule {
    atomic("@base") ~ IRIREF ~> ((i: ASTIriRef) ⇒ run(definePrefix("", i)) ~ push(i)) ~> ASTBase ~ ws ~ "."
  }

  //[5s] sparqlBase 	::= 	"BASE" IRIREF
  def sparqlBase = rule {
    atomic(ignoreCase("base")) ~ ws ~ IRIREF ~> ((i: ASTIriRef) ⇒ run(definePrefix("", i)) ~ push(i)) ~> ASTSparqlBase ~ ws
  }

  //[6s] sparqlPrefix 	::= 	"PREFIX" PNAME_NS IRIREF
  def sparqlPrefix = rule {
    atomic(ignoreCase("prefix")) ~ ws ~ PNAME_NS ~ ws ~ IRIREF ~> ((p: ASTPNameNS, i: ASTIriRef) ⇒ run(definePrefix(p, i)) ~ push(p) ~ push(i)) ~> ASTSparqlPrefix ~ ws
  }

  //[6] triples 	::= 	subject predicateObjectList | blankNodePropertyList predicateObjectList?
  def triples: Rule1[TurtleAST] = rule {
    subject ~ predicateObjectList ~> ASTTriples | blankNodePropertyList ~ predicateObjectList.? ~> ASTBlankNodeTriples
  }

  //[7] predicateObjectList 	::= 	verb objectList (';' (verb objectList)?)*
  def predicateObjectList = rule {
    po.+((';' ~ ws).+) ~ ((';' ~ ws).+ | ws) ~> ASTPredicateObjectList
  }

  def po = rule {
    verb ~ objectList ~> ASTPo
  }

  //[8] objectList 	::= 	object (',' object)*
  def objectList = rule {
    `object`.+(",") ~> ASTObjectList
  }

  //[9] verb 	::= 	predicate | 'a'
  def verb = rule {
    (predicate | isA.named("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) ~> ASTVerb ~ ws
  }

  def isA = rule {
    capture("a") ~> ASTIsA
  }

  //[10] subject 	::= 	iri | BlankNode | collection
  def subject = rule {
    (iri | blankNode | collection) ~> ASTSubject
  }

  //[11] predicate 	::= 	iri
  def predicate = rule {
    iri ~> ASTPredicate
  }

  //[12] object 	::= 	iri | BlankNode | collection | blankNodePropertyList | literal
  def `object`: Rule1[TurtleAST] = rule {
    (iri | blankNode | collection | blankNodePropertyList | literal) ~> ASTObject
  }

  //[13] literal 	::= 	RDFLiteral | NumericLiteral | BooleanLiteral
  def literal = rule {
    (rdfLiteral | numericLiteral | booleanLiteral) ~> ASTLiteral
  }

  //[14] blankNodePropertyList 	::= 	'[' predicateObjectList ']'
  def blankNodePropertyList = rule {
    "[" ~ (predicateObjectList ~> ASTBlankNodePropertyList) ~ "]"
  }

  //[15] collection 	::= 	'(' object* ')'
  def collection = rule {
    "(" ~ `object`.* ~> ASTCollection ~ ")"
  }

  //[16] NumericLiteral 	::= 	INTEGER | DECIMAL | DOUBLE
  /* Order choice is important here (see https :// github.com / sirthias / parboiled2#id22 for further information)
  */
  def numericLiteral = rule {
    (DOUBLE | DECIMAL | INTEGER) ~> ASTNumericLiteral
  }

  //[19] INTEGER 	::= 	[+-]? [0-9]+
  def INTEGER = rule {
    atomic(capture(SIGN.? ~ Digit.+)) ~> ASTInteger ~ ws
  }

  //[20] DECIMAL 	::= 	[+-]? [0-9]* '.' [0-9]+
  def DECIMAL = rule {
    atomic(capture(SIGN.? ~ Digit.* ~ ch('.') ~ Digit.+)) ~> ASTDecimal ~ ws
  }

  //[21] DOUBLE 	::= 	[+-]? ([0-9]+ '.' [0-9]* EXPONENT | '.' [0-9]+ EXPONENT | [0-9]+ EXPONENT)
  def DOUBLE = rule {
    atomic(capture(SIGN.? ~ (Digit.+ ~ ch('.') ~ Digit.* | ch('.') ~ Digit.+ | Digit.+) ~ EXPONENT)) ~> ASTDouble ~ ws
  }

  //[154s] EXPONENT 	::= 	[eE] [+-]? [0-9]+
  def EXPONENT = rule {
    ignoreCase('e') ~ SIGN.? ~ Digit.+
  }

  //[128s] RDFLiteral 	::= 	String (LANGTAG | '^^' iri)?
  def rdfLiteral = rule {
    string ~ (ws ~ LANGTAG | "^^" ~ iri).? ~ ws ~> ASTRdfLiteral
  }

  //[144s] LANGTAG 	::= 	'@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*
  def LANGTAG = rule {
    atomic("@" ~ capture(Alpha.+ ~ ('-' ~ AlphaNum.+).*)) ~> ASTLangTag
  }

  //[133s] BooleanLiteral 	::= 	'true' | 'false'
  def booleanLiteral = rule {
    atomic(capture(str("true") | str("false"))) ~> ASTBooleanLiteral ~ ws
  }

  //[17] String 	::= 	STRING_LITERAL_QUOTE | STRING_LITERAL_SINGLE_QUOTE | STRING_LITERAL_LONG_SINGLE_QUOTE | STRING_LITERAL_LONG_QUOTE
  def string = rule {
    (STRING_LITERAL_LONG_SINGLE_QUOTE | STRING_LITERAL_LONG_QUOTE | STRING_LITERAL_QUOTE | STRING_LITERAL_SINGLE_QUOTE) ~> ASTString
  }

  //[22] STRING_LITERAL_QUOTE   ::=     '"' ([^#x22#x5C#xA#xD] | ECHAR | UCHAR)* '"' /* #x22=" #x5C=\ #xA=new line #xD=carriage return */
  def STRING_LITERAL_QUOTE = rule {
    '"' ~ clearSB ~ (noneOf("\"\\\r\n") ~ appendSB | UCHAR(true) | ECHAR).* ~ '"' ~ push(sb.toString) ~> ASTStringLiteralQuote
  }

  //[23] '" ([^#x27#x5C#xA#xD] | ECHAR | UCHAR)* "'" /* #x27=' #x5C=\ #xA=new line #xD=carriage return */
  def STRING_LITERAL_SINGLE_QUOTE = rule {
    '\'' ~ clearSB ~ (noneOf("'\"\\\r\n") ~ appendSB | '"' ~ appendSB("\\\"") | UCHAR(true) | ECHAR).* ~ '\'' ~ push(sb.toString) ~> ASTStringLiteralSingleQuote
  }

  //[24] STRING_LITERAL_LONG_SINGLE_QUOTE       ::=     "'''" (("'" | "''")? ([^'\] | ECHAR | UCHAR))* "'''"
  def STRING_LITERAL_LONG_SINGLE_QUOTE = rule {
    str("'''") ~ clearSB ~ (capture(('\'' ~ '\'' ~ !'\'' | '\'' ~ !('\'' ~ '\'')).?) ~> ((s: String) ⇒ appendSB(s.replaceAllLiterally("\"", "\\\""))) ~ (capture(noneOf("\'\\\"")) ~> ((s: String) ⇒ run(maskEsc(s))) | '"' ~ appendSB("\\\"") | UCHAR(true) | ECHAR)).* ~ str("'''") ~ push(sb.toString) ~> ASTStringLiteralLongSingleQuote
  }

  //[25] STRING_LITERAL_LONG_QUOTE      ::=     '"""' (('"' | '""')? ([^"\] | ECHAR | UCHAR))* '"""'
  def STRING_LITERAL_LONG_QUOTE = rule {
    str("\"\"\"") ~ clearSB ~ (capture(('"' ~ '"' ~ !'"' | '"' ~ !('"' ~ '"')).?) ~> ((s: String) ⇒ appendSB(s.replaceAllLiterally("\"", "\\\""))) ~ (capture(noneOf("\"\\")) ~> ((s: String) ⇒ run(maskEsc(s))) | UCHAR(true) | ECHAR)).* ~ str("\"\"\"") ~ push(sb.toString) ~> ASTStringLiteralLongQuote
  }

  //[26] UCHAR  ::=     '\\u' HEX HEX HEX HEX | '\U' HEX HEX HEX HEX HEX HEX HEX HEX
  def UCHAR(flag: Boolean) = rule {
    atomic(str("\\u") ~ capture(4.times(HexDigit))) ~> ((s: String) ⇒ maskQuotes(flag, s)) |
      atomic(str("\\U") ~ capture(8.times(HexDigit))) ~> ((s: String) ⇒ maskQuotes(flag, s))
  }

  //[159s] ECHAR        ::=     '\' [tbnrf"'\]
  def ECHAR = rule {
    atomic(str("\\") ~ appendSB ~ ECHAR_CHAR ~ appendSB)
  }

  //[135s] iri 	::= 	IRIREF | PrefixedName
  def iri: Rule1[TurtleAST] = rule {
    (IRIREF | prefixedName) ~> ASTIri ~ ws
  }

  //[18] IRIREF 	::= 	'<' ([^#x00-#x20<>"{}|^`\] | UCHAR)* '>'
  /* #x00=NULL #01-#x1F=control codes #x20=space */
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
        str("\\u007D") | str("\\u007d") | str("\\U0000007D") | str("\\U0000007d")) ~ UCHAR(false)).*) ~ push(sb.toString) ~ '>' ~> ASTIriRef
  }

  //[136s] PrefixedName 	::= 	PNAME_LN | PNAME_NS
  def prefixedName = rule {
    (PNAME_LN | PNAME_NS) ~> ASTPrefixedName
  }

  //[139s] PNAME_NS 	::= 	PN_PREFIX? ':'
  def PNAME_NS = rule {
    PN_PREFIX.? ~> ASTPNameNS ~ ':'
  }

  //[140s] PNAME_LN 	::= 	PNAME_NS PN_LOCAL
  def PNAME_LN = rule {
    PNAME_NS ~ PN_LOCAL ~> ((ns: ASTPNameNS, local: ASTPNLocal) ⇒ (test(addPrefix(ns, local)) | fail("name space - PNAME_NS=\"" + ns + "\" might be undefined")) ~ push(ns) ~ push(local)) ~> ASTPNameLN
  }

  //[167s] N_PREFIX 	::= 	PN_CHARS_BASE ((PN_CHARS | '.')* PN_CHARS)?
  /* A prefix name may not start or end with a '.' (DOT), but is allowed to have any number of '.' in between.
	 The predicate "&(ch('.').+ ~ PN_CHARS)", looks ahead and checks if the rule in braces will be fullfilled.
	 It does so without interfering with the parsing process.
	 Example:
	 [] <b> c:d.1..2...3.
	 Due to the predicate the last '.' is not part of the local name. The accepted name is "c:d.1..2...3",
	 with the last '.' being recognized as triple terminator.
	 */
  def PN_PREFIX = rule {
    atomic(capture(PN_CHARS_BASE ~ (PN_CHARS | &(ch('.').+ ~ PN_CHARS) ~ ch('.').+ ~ PN_CHARS | isHighSurrogate ~ isLowSurrogate).*)) ~> ASTPNPrefix
  }

  //[168s] PN_LOCAL 	::= 	(PN_CHARS_U | ':' | [0-9] | PLX) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX))?
  /* A local name may not start or end with a '.' (DOT), but is allowed to have any number of '.' in between.
	 The predicate "&(ch('.').+ ~ PN_CHARS_COLON)", looks ahead and checks if the rule in braces will be fullfilled.
	 It does so without interfering with the parsing process.
	 Example:
	 [] <b> c:d.1..2...3.
	 Due to the predicate the last '.' is not part of the local name. The accepted name is "c:d.1..2...3",
	 with the last '.' being recognized as triple terminator.
	 */
  def PN_LOCAL = rule {
    clearSB ~ atomic((PLX | PN_CHARS_U_COLON_DIGIT ~ appendSB) ~ (PLX | PN_CHARS_COLON ~ appendSB | &(ch('.').+ ~ PN_CHARS_COLON) ~ (ch('.') ~ appendSB).+ ~ PN_CHARS_COLON ~ appendSB | isHighSurrogate ~ appendSB ~ isLowSurrogate ~ appendSB).*) ~ push(sb.toString) ~> ASTPNLocal
  }

  //[169s] PLX 	::= 	PERCENT | PN_LOCAL_ESC
  def PLX = rule {
    PERCENT | PN_LOCAL_ESC
  }

  //[170s] PERCENT 	::= 	'%' HEX HEX
  def PERCENT = rule {
    atomic('%' ~ appendSB ~ HexDigit ~ appendSB ~ HexDigit ~ appendSB)
  }

  //[172s] PN_LOCAL_ESC 	::= 	'\' ('_' | '~' | '.' | '-' | '!' | '$' | '&' | "'" | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%')
  def PN_LOCAL_ESC = rule {
    atomic('\\' ~ LOCAL_ESC ~ appendSB)
  }

  //[137s] BlankNode 	::= 	BLANK_NODE_LABEL | ANON
  def blankNode = rule {
    (BLANK_NODE_LABEL | ANON) ~> ASTBlankNode
  }

  //[141s] BLANK_NODE_LABEL 	::= 	'_:' (PN_CHARS_U | [0-9]) ((PN_CHARS | '.')* PN_CHARS)?
  /* A blank node label is allowed to contain dots ('.'), but it is forbidden as last character of the recognized label name.
	 The reason for this is, when '.' is used as last character of a blank node label, it collides with triple termination,
	 which is signaled by '.', too.
	 The predicate "&(ch('.').* ~ PN_CHARS)", looks ahead and checks if the rule in braces will be fullfilled.
	 It does so without interfering with the parsing process.
	 Example:
	 <a> <b> _:c.1..2...3.
	 Due to the predicate the last '.' is not part of the blank node label. The accepted name is "_:c.1..2...3",
	 with the last '.' being recognized as triple terminator.
	 */
  def BLANK_NODE_LABEL = rule {
    atomic(str("_:") ~ capture(PN_CHARS_U_DIGIT ~ (PN_CHARS | &(ch('.').+ ~ PN_CHARS) ~ ch('.').+ ~ PN_CHARS | isHighSurrogate ~ isLowSurrogate).*)) ~> ASTBlankNodeLabel ~ ws
  }

  //[162s] ANON 	::= 	'[' WS* ']'
  def ANON = rule {
    atomic(capture("[" ~ "]")) ~> ASTAnon
  }

  private def definePrefix(key: ASTPNameNS, value: ASTIriRef): Unit = {
    val pname = (key: @unchecked) match {
      case ASTPNameNS(rule) ⇒ (rule: @unchecked) match {
        case Some(ASTPNPrefix(token)) ⇒ token
        case None                     ⇒ ""
      }
    }

    definePrefix(pname, value)
  }

  private def definePrefix(key: String, iriRef: ASTIriRef): Unit = {
    val value = iriRef.token
    if (value.startsWith("//") || hasScheme(value))
      prefixMap += key → value
    else if (value.endsWith("/")) {
      if (!prefixMap.contains(key))
        prefixMap += key → value
      else
        prefixMap += key → (prefixMap.getOrElse(key, basePath) + value)
    } else prefixMap += key → value
  }

  private def addPrefix(pname_ns: ASTPNameNS, pn_local: ASTPNLocal): Boolean = {
    val ns = (pname_ns: @unchecked) match {
      case ASTPNameNS(rule) ⇒ (rule: @unchecked) match {
        case Some(ASTPNPrefix(token)) ⇒ token
        case None                     ⇒ ""
      }
    }
    prefixMap.contains(ns)
  }

  private def hasScheme(iri: String) = SchemeIdentifier(iri)

  private def maskQuotes(flag: Boolean, s: String) = {
    val c = hexStringToCharString(s)
    if (c.compare("\"") != 0)
      appendSB(c)
    else {
      if (flag)
        appendSB("\\\"")
      else
        appendSB("\\u0022")
    }
  }

  private def maskEsc(s: String) = {
    var c = s.charAt(0)
    if (c < ' ') {
      if (c == '\n') appendSB("\\n")
      else if (c == '\r') appendSB("\\r")
      else if (c == '\t') appendSB("\\t")
      else if (c == '\f') appendSB("\\f")
      else if (c == '\b') appendSB("\\b")
      else appendSB(s)
    } else appendSB(s)
  }
}