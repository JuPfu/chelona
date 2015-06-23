package org.chelona

import java.io.Writer

import org.chelona.NTriplesParser.NTAST

import org.parboiled2._

import scala.util.Success

object NTriplesParser extends NTripleAST {

  def apply(input: ParserInput, output: Writer, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") = {
    new NTriplesParser(input, output, validate, basePath, label)
  }

  def NTWriter(bo: Writer)(s: String, p: String, o: String): Int = {
    bo.write(s + " " + p + " " + o + " .\n"); 1
  }

  sealed trait NTAST extends NTripleAST
  /*
  case class ASTBlankLine(token: String) extends NTripleAST

  case class ASTComment(token: String) extends NTripleAST

  case class ASTTriple(subject: NTripleAST, predicate: NTripleAST, `object`: NTripleAST, comment: Option[ASTComment]) extends NTripleAST

  case class ASTTripleComment(rule: NTripleAST) extends NTripleAST

  case class ASTSubject(rule: NTripleAST) extends NTripleAST

  case class ASTPredicate(rule: NTripleAST) extends NTripleAST

  case class ASTObject(rule: NTripleAST) extends NTripleAST

  case class ASTLangTag(token: String) extends NTripleAST

  case class ASTIriRef(token: String) extends NTripleAST

  case class ASTLiteral(token: NTripleAST, postfix: Option[NTripleAST]) extends NTripleAST

  case class ASTStringLiteralQuote(token: String) extends NTripleAST

  case class ASTBlankNodeLabel(token: String) extends NTripleAST

  //case class ASTGraphLabel(rule: NTripleAST) extends NTripleAST
  */
}

class NTriplesParser(val input: ParserInput, val output: Writer, validate: Boolean = false, val basePath: String = "http://chelona.org", val label: String = "") extends Parser with StringBuilding with NTAST {

  import org.chelona.CharPredicates._

  import org.chelona.NTriplesParser.NTWriter

  import org.parboiled2.CharPredicate.{ Alpha, AlphaNum, Digit, HexDigit }

  private def hexStringToCharString(s: String) = s.grouped(4).map(cc ⇒ (Character.digit(cc(0), 16) << 12 | Character.digit(cc(1), 16) << 8 | Character.digit(cc(2), 16) << 4 | Character.digit(cc(3), 16)).toChar).filter(_ != '\u0000').mkString("")

  val nt = new EvalNT(basePath, label)

  val tripleOutput = NTWriter(output)_

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
        if (!validate)
          nt.renderStatement(ast, tripleOutput)
        else
          ast match {
            case ASTComment(s) ⇒ 0
            case _             ⇒ 1
          }
      } else 0)).*(EOL) ~ EOL.? ~ EOI ~> ((v: Seq[Int]) ⇒ v.foldLeft(0L)(_ + _))
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
        str("\\u007D") | str("\\u007d") | str("\\U0000007D") | str("\\U0000007d")) ~ UCHAR).*) ~ push(sb.toString) ~ '>' ~> ((iri: String) ⇒ (test(isAbsoluteIRIRef(iri)) | fail("relative IRI not allowed: " + iri)) ~ push(iri)) ~> ASTIriRef ~ ws
  }

  //[9]	STRING_LITERAL_QUOTE	::=	'"' ([^#x22#x5C#xA#xD] | ECHAR | UCHAR)* '"'
  def STRING_LITERAL_QUOTE = rule {
    atomic('"' ~ clearSB ~ (noneOf("\"\\\r\n") ~ appendSB | UCHAR | ECHAR).* ~ '"') ~ push(sb.toString) ~ ws ~> ASTStringLiteralQuote
  }

  //[141s]	BLANK_NODE_LABEL	::=	'_:' (PN_CHARS_U | [0-9]) ((PN_CHARS | '.')* PN_CHARS)?
  def BLANK_NODE_LABEL = rule {
    atomic(str("_:") ~ capture(PN_CHARS_U_DIGIT ~ (PN_CHARS | &(DOT.+ ~ PN_CHARS) ~ DOT.+ ~ PN_CHARS | isHighSurrogate ~ isLowSurrogate).*)) ~> ASTBlankNodeLabel ~ ws
  }

  //[10]	UCHAR	::=	'\\u' HEX HEX HEX HEX | '\\U' HEX HEX HEX HEX HEX HEX HEX HEX
  def UCHAR = rule {
    atomic(str("\\u") ~ capture(4.times(HexDigit))) ~> ((s: String) ⇒ appendSB(hexStringToCharString(s))) |
      atomic(str("\\U") ~ capture(8.times(HexDigit))) ~> ((s: String) ⇒ appendSB(hexStringToCharString(s)))
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
}
