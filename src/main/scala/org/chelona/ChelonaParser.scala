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

import java.io.{ File, BufferedWriter }

import org.parboiled2._

import scala.annotation.tailrec
import scala.language.implicitConversions

import scala.util.{ Failure, Success }

object ChelonaParser {

  import org.parboiled2.CharPredicate.{ Alpha, Digit }

  val DOT = CharPredicate('.')
  val SIGN = CharPredicate('+', '-')
  val COLON = CharPredicate(':')

  val IRIREF_CHAR = CharPredicate('\u0021' to '\uFFFF') -- CharPredicate("<>\"{}|^`\\")

  val PN_CHARS_BASE = Alpha ++ CharPredicate('\u00C0' to '\u00D6', '\u00D8' to '\u00F6', '\u00F8' to '\u02FF', '\u0370' to '\u037D', '\u037F' to '\u1FFF',
    '\u200C' to '\u200D', '\u2070' to '\u218F', '\u2C00' to '\u2FeF', '\u3001' to '\uD7FF', '\uF900' to '\uFDCF',
    '\uFDF0' to '\uFFFD')

  val PN_CHARS_U = PN_CHARS_BASE ++ CharPredicate('_')

  val PN_CHARS_U_DIGIT = PN_CHARS_U ++ Digit

  val PN_CHARS_U_COLON_DIGIT = PN_CHARS_U_DIGIT ++ COLON

  val PN_CHARS = PN_CHARS_U ++ CharPredicate('-') ++ Digit ++ CharPredicate('\u00B7', '\u0300' to '\u036F', '\u203F' to '\u2040')

  val PN_CHARS_COLON = PN_CHARS ++ COLON

  val PN_CHARS_DOT = PN_CHARS ++ DOT

  val PN_CHARS_DOT_COLON = PN_CHARS_COLON ++ DOT

  val PNAME_LN_CHARS = PN_CHARS_U ++ COLON ++ Digit

  val ECHAR_CHAR = CharPredicate("tbnrf\"'\\")

  val LOCAL_ESC = CharPredicate('_', '~', '.', '-', '!', '$', '&', "'", '(', ')', '*', '+', ',', ';', '=', '/', '?', '#', '@', '%')

  val WS = CharPredicate(" \t")

  val echarMap = Map('t' -> '\t', 'b' -> '\b', 'n' -> '\n', 'r' -> '\r', 'f' -> '\f', '"' -> '"', '\'' -> '\'', '\\' -> '\\')

  val prefixMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[String]
  val predicateStack = scala.collection.mutable.Stack.empty[String]
  var curSubject: String = "---Not valid subject---"
  var curPredicate: String = "---Not valid predicate---"
  var aCount = 0
  var bCount = 0
  var cCount = 0

  def main(args: Array[String]) {
    lazy val inputfile: ParserInput = io.Source.fromFile(args(0)).mkString
    println("Convert:" + args(0))
    ChelonaParser(inputfile)
  }

  def apply(input: ParserInput) = {
    val parser = new ChelonaParser(input)
    val res = parser.turtleDoc.run()

    res match {
      case scala.util.Success(ast) ⇒
        println("Number of triples written to file " + "'./testfiles/out.ttl': " + render(ast, tripleWriter))
      case Failure(e: ParseError) ⇒ println("Expression is not valid: " + parser.formatError(e))
      case Failure(e)             ⇒ println("Unexpected error during parsing run: " + e)
    }
    bo.close()
  }

  val bo = new BufferedWriter(new java.io.FileWriter(new File("./testfiles/out.ttl")))

  def tripleWriter(triple: List[SPOTriple]): Int = {
    triple.map(t ⇒ bo.write(t.s + " " + t.p + " " + t.o + " .\n"))
    triple.length
  }

  def tripleRawWriter(triple: List[SPOTriple]): Int = {
    triple.map(t ⇒ bo.write(t.s + " " + t.p + " " + t.o + "\n"))
    triple.length
  }

  def renderStatement(ast: AST, writer: List[SPOTriple] ⇒ Int): Int = {
    (evalStatement(ast): @unchecked) match {
      case SPOTriples(t) ⇒ writer(t)
      case SPOString(s)  ⇒ 0
      case SPOComment(c) ⇒ 0
    }
  }

  def render(ast: Seq[AST], writer: List[SPOTriple] ⇒ Long): Long = {
    @tailrec
    def evalLoop(e: Seq[AST], statementCount: Long): Long = e match {
      case Nil ⇒ statementCount
      case x +: xs ⇒ evalLoop(xs, statementCount + ((evalStatement(x): @unchecked) match {
        case SPOTriples(t) ⇒ writer(t)
        case SPOString(s)  ⇒ 0L
        case SPOComment(c) ⇒ 0L
      }))
    }
    evalLoop(ast, 0L)
  }

  def evalStatement(expr: AST): SPOReturnValue = {
    expr match {
      case ASTTurtleDoc(rule) ⇒ evalStatement(rule)
      case ASTStatement(rule) ⇒
        /* some clean up at the beginning of a new turtle statement */
        subjectStack.clear
        predicateStack.clear
        /* evaluate a turtle statement */
        evalStatement(rule)
      case ASTComment(rule)   ⇒ SPOComment(rule)
      case ASTDirective(rule) ⇒ evalStatement(rule)
      case ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (SPOString(ps), SPOString(is)) ⇒ definePrefix(ps, is)
        }
        SPOString("")
      case ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case SPOString(bs) ⇒ definePrefix("@", bs)
        }
        SPOString("")
      case ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case SPOString(bs) ⇒ definePrefix("@", bs)
        }
        SPOString("")
      case ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (SPOString(ps), SPOString(is)) ⇒ definePrefix(ps, is)
        }
        SPOString("")
      case ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (SPOTriples(ts), SPOTriples(ps))     ⇒ SPOTriples(ts ::: ps)
          case (SPOString(subject), SPOTriples(ps)) ⇒ SPOTriples(ps)
        }
      case ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        curSubject = "_:b" + bCount
        bCount += 1
        val sub = evalStatement(s)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (SPOTriples(ts), SPOTriples(ps)) ⇒ SPOTriples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case ASTPredicateObjectList(predicateObject, predicateObjectlist) ⇒
        (evalStatement(predicateObject): @unchecked) match {
          case SPOTriples(t) ⇒ predicateObjectlist match {
            case Some(po) ⇒ SPOTriples(t ::: traversePredicateObjectList(po, Nil))
            case None     ⇒ SPOTriples(t)
          }
        }
      case ASTPo(verb, obj) ⇒
        (evalStatement(verb): @unchecked) match {
          case SPOString(token) ⇒ curPredicate = token
        }
        evalStatement(obj)
      case ASTObjectList(rule) ⇒ SPOTriples(traverseObjectList(rule, Nil))
      case ASTVerb(rule)       ⇒ evalStatement(rule)
      case ASTIsA(token)       ⇒ SPOString("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")
      case ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ curSubject = s; SPOString(curSubject)
        }
        case ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ curSubject = s; SPOString(curSubject)
        }
        case ASTCollection(c) ⇒ evalStatement(rule)
      }
      case ASTPredicate(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ curPredicate = s
        }
        SPOString(curPredicate)
      case ASTObject(l) ⇒ (l: @unchecked) match {
        case ASTIri(v) ⇒ (evalStatement(l): @unchecked) match {
          case SPOString(o) ⇒ SPOTriple(curSubject, curPredicate, o);
        }
        case ASTBlankNode(v) ⇒ (evalStatement(l): @unchecked) match {
          case SPOString(o) ⇒ SPOTriple(curSubject, curPredicate, o);
        }
        case ASTLiteral(l) ⇒
          (evalStatement(l): @unchecked) match {
            case SPOString(o) ⇒ SPOTriple(curSubject, curPredicate, o);
          }
        case ASTCollection(v) ⇒
          subjectStack.push(curSubject)
          cCount += 1
          curSubject = "_:c" + cCount
          predicateStack.push(curPredicate)
          curPredicate = "rdf:first"
          (evalStatement(l): @unchecked) match {
            case SPOTriples(t) ⇒
              val oldSubject = curSubject
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              SPOTriples(SPOTriple(curSubject, curPredicate, oldSubject) :: t)
          }
        case ASTBlankNodePropertyList(v) ⇒
          subjectStack.push(curSubject)
          predicateStack.push(curPredicate)
          bCount += 1
          curSubject = "_:b" + bCount
          val bnode = curSubject
          (evalStatement(l): @unchecked) match {
            case SPOTriples(t) ⇒
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              SPOTriples(SPOTriple(curSubject, curPredicate, bnode) :: t)
          }
      }
      case ASTLiteral(rule)               ⇒ evalStatement(rule)
      case ASTBlankNodePropertyList(rule) ⇒ evalStatement(rule)
      case ASTCollection(rule) ⇒
        curSubject = "_:c" + cCount
        subjectStack.push(curSubject)
        curPredicate = "rdf:first"
        predicateStack.push(curPredicate)
        val res = SPOTriples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case ASTInteger(token)       ⇒ SPOString(token)
      case ASTDecimal(token)       ⇒ SPOString(token)
      case ASTDouble(token)        ⇒ SPOString(token)
      case ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case SPOString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIri(v) ⇒ SPOString(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case SPOString(s) ⇒ s
            }))
            case ASTLangTag(v) ⇒ SPOString(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case SPOString(s) ⇒ s
            }))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)                      ⇒ SPOString(token)
      case ASTBooleanLiteral(token)               ⇒ SPOString(token)
      case ASTString(rule)                        ⇒ evalStatement(rule)
      case ASTStringLiteralQuote(token)           ⇒ SPOString("\"" + token + "\"")
      case ASTStringLiteralSingleQuote(token)     ⇒ SPOString("'" + token + "'")
      case ASTStringLiteralLongSingleQuote(token) ⇒ SPOString("'''" + token + "'''")
      case ASTStringLiteralLongQuote(token)       ⇒ SPOString("\"\"\"" + token + "\"\"\"")
      case ASTIri(rule) ⇒ (rule: @unchecked) match {
        case ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ SPOString("<" + addBasePrefix(s) + ">")
        }
        case ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case ASTIriRef(token)      ⇒ SPOString(token)
      case ASTPrefixedName(rule) ⇒ evalStatement(rule)
      case ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix);
          case None            ⇒ SPOString("")
        }
      case ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (SPOString(pname_ns), SPOString(pn_local)) ⇒ SPOString("<" + addPrefix(pname_ns, pn_local) + ">")
        }
      case ASTPNPrefix(token)       ⇒ SPOString(token)
      case ASTPNLocal(token)        ⇒ SPOString(token)
      case ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case ASTBlankNodeLabel(token) ⇒ SPOString("_:" + token)
      case ASTAnon(token)           ⇒ aCount += 1; SPOString("_:a" + aCount)
    }
  }

  @tailrec
  private def traversePredicateObjectList(l: Seq[Option[AST]], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case Nil ⇒ triples
    case x +: xs ⇒ x match {
      case Some(po) ⇒ (evalStatement(po): @unchecked) match {
        case SPOTriples(tl) ⇒ traversePredicateObjectList(xs, triples ::: tl)
      }
      case None ⇒ triples
    }
  }

  @tailrec
  private def traverseObjectList(l: Seq[AST], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case Nil ⇒ triples
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case SPOTriple(s, p, o) ⇒ traverseObjectList(xs, triples :+ SPOTriple(s, p, o))
      case SPOTriples(t)      ⇒ traverseObjectList(xs, triples ::: t)
    }
  }

  @tailrec
  private def traverseCollection(l: Seq[AST], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case Nil ⇒ triples ::: SPOTriple(curSubject, "rdf:rest", "rdf:nil") :: Nil
    case x +: xs ⇒
      val oldSubject = curSubject
      if (xs != Nil) {
        cCount += 1
        curSubject = "_:c" + cCount
      }
      (evalStatement(x): @unchecked) match {
        case SPOTriple(s, p, o) ⇒ traverseCollection(xs, if (xs != Nil) {
          triples ::: (SPOTriple(oldSubject, "rdf:first", o) :: (SPOTriple(oldSubject, "rdf:rest", curSubject) :: Nil))
        } else {
          triples :+ SPOTriple(oldSubject, "rdf:first", o)
        })
        case SPOTriples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          triples ::: (t :+ SPOTriple(oldSubject, "rdf:rest", curSubject))
        } else {
          triples ::: t
        })
      }
  }

  private def definePrefix(key: String, value: String) = {
    if (value.startsWith("//") | value.toLowerCase.startsWith("http://"))
      prefixMap += key -> value
    else if (value.endsWith("/")) {
      if (!prefixMap.contains(key))
        prefixMap += key -> value
      else
        prefixMap += key -> (prefixMap.getOrElse("@", "http://base/not/found") + value)
    } else prefixMap += key -> value
  }

  private def addPrefix(pname_ns: String, pn_local: String): String = {
    val prefix = prefixMap.getOrElse(pname_ns, "http://key/" + pname_ns + "/not/found")
    if (prefix.endsWith("/") || prefix.endsWith("#"))
      prefix + pn_local
    else
      prefix + "/" + pn_local
  }

  private def addBasePrefix(x: String) = {
    if (!x.startsWith("/") && !x.toLowerCase.startsWith("http://")) {
      val prefix = prefixMap.getOrElse("@", "http://base/not/found")
      if (prefix.endsWith("/") || prefix.endsWith("#"))
        prefix + x
      else
        prefix + "/" + x
    } else x
  }

  private def hexStringToCharString(s: String) = s.grouped(2).map(cc ⇒ (Character.digit(cc(0), 16) << 4 | Character.digit(cc(1), 16)).toChar).mkString("")

  sealed trait SPOReturnValue

  case class SPOString(s: String) extends SPOReturnValue

  case class SPOTriple(s: String, p: String, o: String) extends SPOReturnValue

  case class SPOTriples(values: List[SPOTriple]) extends SPOReturnValue

  case class SPOComment(value: String) extends SPOReturnValue

  sealed trait AST

  case class ASTTurtleDoc(rule: AST) extends AST

  case class ASTStatement(rule: AST) extends AST

  case class ASTComment(token: String) extends AST

  case class ASTDirective(rule: AST) extends AST

  case class ASTPrefixID(namespace: AST, iri: AST) extends AST

  case class ASTBase(iri: AST) extends AST

  case class ASTSparqlBase(iri: AST) extends AST

  case class ASTSparqlPrefix(namespace: AST, iri: AST) extends AST

  case class ASTTriples(subject: AST, predicateObjectList: AST) extends AST

  case class ASTBlankNodeTriples(blankNodePropertyList: AST, predicateObjectList: Option[AST]) extends AST

  case class ASTPredicateObjectList(predicateObject: AST, predicateObjectList: Option[Seq[Option[AST]]]) extends AST

  case class ASTPo(verb: AST, obj: AST) extends AST

  case class ASTObjectList(rule: Seq[AST]) extends AST

  case class ASTVerb(rule: AST) extends AST

  case class ASTIsA(token: String) extends AST

  case class ASTSubject(rule: AST) extends AST

  case class ASTPredicate(rule: AST) extends AST

  case class ASTObject(rule: AST) extends AST

  case class ASTLiteral(rule: AST) extends AST

  case class ASTBlankNodePropertyList(rule: AST) extends AST

  case class ASTCollection(rule: Seq[AST]) extends AST

  case class ASTNumericLiteral(rule: AST) extends AST

  case class ASTInteger(token: String) extends AST

  case class ASTDecimal(token: String) extends AST

  case class ASTDouble(token: String) extends AST

  case class ASTRdfLiteral(string: AST, postfix: Option[AST]) extends AST

  case class ASTLangTag(token: String) extends AST

  case class ASTBooleanLiteral(token: String) extends AST

  case class ASTString(rule: AST) extends AST

  case class ASTStringLiteralQuote(token: String) extends AST

  case class ASTStringLiteralSingleQuote(token: String) extends AST

  case class ASTStringLiteralLongSingleQuote(token: String) extends AST

  case class ASTStringLiteralLongQuote(token: String) extends AST

  case class ASTIri(rule: AST) extends AST

  case class ASTIriRef(token: String) extends AST

  case class ASTPrefixedName(rule: AST) extends AST

  case class ASTPNameNS(rule: Option[AST]) extends AST

  case class ASTPNameLN(namespace: AST, local: AST) extends AST

  case class ASTPNPrefix(token: String) extends AST

  case class ASTPNLocal(token: String) extends AST

  case class ASTBlankNode(rule: AST) extends AST

  case class ASTBlankNodeLabel(token: String) extends AST

  case class ASTAnon(token: String) extends AST

}

class ChelonaParser(val input: ParserInput) extends Parser with StringBuilding {

  import org.chelona.ChelonaParser._
  import org.parboiled2.CharPredicate.{ Alpha, AlphaNum, Digit, HexDigit }

  //[161s]
  implicit def wspStr(s: String): Rule0 = rule {
    str(s) ~ WS.* ~ (('#' ~ noneOf("\r\n").* ~ "\r".? ~ "\n") | ("\r".? ~ "\n").?)
  }

  def ws = rule {
    WS.* ~ (('#' ~ noneOf("\r\n").* ~ "\r".? ~ "\n") | ("\r".? ~ "\n").?)
  }

  //[1] turtleDoc 	::= 	statement*
  def turtleDoc = rule {
    statement.* ~ EOI
  }

  //[2] statement 	::= 	directive | triples '.'
  def statement: Rule1[AST] = rule {
    (directive | triples ~ "." | comment) ~> ASTStatement
  }

  //
  def comment = rule {
    WS.* ~ '#' ~ capture(noneOf("\r\n").*) ~> ASTComment ~!~ (("\r".? ~ "\n") | EOI)
  }

  //[3] directive 	::= 	prefixID | base | sparqlPrefix | sparqlBase
  def directive = rule {
    (prefixID | base | sparqlPrefix | sparqlBase) ~> ASTDirective
  }

  //[4] prefixID 	::= 	'@prefix' PNAME_NS IRIREF '.'
  def prefixID = rule {
    atomic("@prefix") ~ PNAME_NS ~ ws ~ IRIREF ~> ASTPrefixID ~!~ ws ~ "."
  }

  //[5] base 	::= 	'@base' IRIREF '.'
  def base = rule {
    atomic("@base") ~ IRIREF ~> ASTBase ~!~ ws ~ "."
  }

  //[5s] sparqlBase 	::= 	"BASE" IRIREF
  def sparqlBase = rule {
    atomic(ignoreCase("base")) ~ ws ~ IRIREF ~> ASTSparqlBase ~!~ ws
  }

  //[6s] sparqlPrefix 	::= 	"PREFIX" PNAME_NS IRIREF
  def sparqlPrefix = rule {
    atomic(ignoreCase("prefix")) ~ ws ~ PNAME_NS ~ ws ~ IRIREF ~> ASTSparqlPrefix ~!~ ws
  }

  //[6] triples 	::= 	subject predicateObjectList | blankNodePropertyList predicateObjectList?
  def triples = rule {
    subject ~ predicateObjectList ~> ASTTriples | blankNodePropertyList ~ predicateObjectList.? ~> ASTBlankNodeTriples
  }

  //[7] predicateObjectList 	::= 	verb objectList (';' (verb objectList)?)*
  def predicateObjectList = rule {
    po ~ (";" ~ (po.?).*(";")).? ~> ASTPredicateObjectList
  }

  def po = rule {
    (verb ~ objectList) ~> ASTPo
  }

  //[8] objectList 	::= 	object (',' object)*
  def objectList = rule {
    `object`.+(",") ~> ASTObjectList
  }

  //[9] verb 	::= 	predicate | 'a'
  def verb = rule {
    (predicate | isA) ~> ASTVerb ~ ws
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
  def `object`: Rule1[AST] = rule {
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
    capture(SIGN.? ~ Digit.+) ~> ASTInteger ~ ws
  }

  //[20] DECIMAL 	::= 	[+-]? [0-9]* '.' [0-9]+
  def DECIMAL = rule {
    capture(SIGN.? ~ Digit.* ~ DOT ~ Digit.+) ~> ASTDecimal ~ ws
  }

  //[21] DOUBLE 	::= 	[+-]? ([0-9]+ '.' [0-9]* EXPONENT | '.' [0-9]+ EXPONENT | [0-9]+ EXPONENT)
  def DOUBLE = rule {
    capture(SIGN.? ~ (Digit.+ ~ DOT ~ Digit.* ~ EXPONENT | DOT ~ Digit.+ ~ EXPONENT | Digit.+ ~ EXPONENT)) ~> ASTDouble ~ ws
  }

  //[154s] EXPONENT 	::= 	[eE] [+-]? [0-9]+
  def EXPONENT = rule {
    ignoreCase("e") ~ SIGN.? ~ Digit.+
  }

  //[128s] RDFLiteral 	::= 	String (LANGTAG | '^^' iri)?
  def rdfLiteral = rule {
    string ~ (LANGTAG | str("^^") ~ iri).? ~ ws ~> ASTRdfLiteral
  }

  //[144s] LANGTAG 	::= 	'@' [a-zA-Z]+ ('-' [a-zA-Z0-9]+)*
  def LANGTAG = rule {
    '@' ~ capture(Alpha.+ ~ ('-' ~ AlphaNum.+).*) ~> ASTLangTag
  }

  //[133s] BooleanLiteral 	::= 	'true' | 'false'
  def booleanLiteral = rule {
    capture(atomic(str("true")) | atomic(str("false"))) ~> ASTBooleanLiteral ~ ws
  }

  //[17] String 	::= 	STRING_LITERAL_QUOTE | STRING_LITERAL_SINGLE_QUOTE | STRING_LITERAL_LONG_SINGLE_QUOTE | STRING_LITERAL_LONG_QUOTE
  def string = rule {
    (STRING_LITERAL_LONG_SINGLE_QUOTE | STRING_LITERAL_LONG_QUOTE | STRING_LITERAL_QUOTE | STRING_LITERAL_SINGLE_QUOTE) ~> ASTString
  }

  //[22] STRING_LITERAL_QUOTE   ::=     '"' ([^#x22#x5C#xA#xD] | ECHAR | UCHAR)* '"' /* #x22=" #x5C=\ #xA=new line #xD=carriage return */
  def STRING_LITERAL_QUOTE = rule {
    '"' ~ clearSB ~ (noneOf("\"\\\r\n") ~ appendSB | ECHAR | UCHAR).* ~ '"' ~ push(sb.toString) ~> ASTStringLiteralQuote
  }

  //[23] '" ([^#x27#x5C#xA#xD] | ECHAR | UCHAR)* "'" /* #x27=' #x5C=\ #xA=new line #xD=carriage return */
  def STRING_LITERAL_SINGLE_QUOTE = rule {
    '\'' ~ clearSB ~ (noneOf("'\\\r\n") ~ appendSB | ECHAR | UCHAR).* ~ '\'' ~ push(sb.toString) ~> ASTStringLiteralSingleQuote
  }

  //[24] STRING_LITERAL_LONG_SINGLE_QUOTE       ::=     "'''" (("'" | "''")? ([^'\] | ECHAR | UCHAR))* "'''"
  def STRING_LITERAL_LONG_SINGLE_QUOTE = rule {
    str("'''") ~ clearSB ~ (capture(('\'' ~ '\'' ~ !'\'' | '\'' ~ !('\'' ~ '\'')).?) ~> ((s: String) ⇒ appendSB(s)) ~ (noneOf("\'\\") ~ appendSB | ECHAR | UCHAR)).* ~ str("'''") ~ push(sb.toString) ~> ASTStringLiteralLongSingleQuote
  }

  //[25] STRING_LITERAL_LONG_QUOTE      ::=     '"""' (('"' | '""')? ([^"\] | ECHAR | UCHAR))* '"""'
  def STRING_LITERAL_LONG_QUOTE = rule {
    str("\"\"\"") ~ clearSB ~ (capture(('"' ~ '"' ~ !'"' | '"' ~ !('"' ~ '"')).?) ~> ((s: String) ⇒ appendSB(s)) ~ (noneOf("\"\\") ~ appendSB | ECHAR | UCHAR)).* ~ str("\"\"\"") ~ push(sb.toString) ~> ASTStringLiteralLongQuote
  }

  //[26] UCHAR  ::=     '\\u' HEX HEX HEX HEX | '\U' HEX HEX HEX HEX HEX HEX HEX HEX
  def UCHAR = rule {
    str("\\u") ~ capture(4.times(HexDigit)) ~> ((s: String) ⇒ appendSB(hexStringToCharString(s))) |
      str("\\U") ~ capture(8.times(HexDigit)) ~> ((s: String) ⇒ appendSB(hexStringToCharString(s)))
  }

  //[159s] ECHAR        ::=     '\' [tbnrf"'\]
  def ECHAR = rule {
    str("\\") ~ ECHAR_CHAR ~ appendSB(echarMap(lastChar))
  }

  //[135s] iri 	::= 	IRIREF | PrefixedName
  def iri: Rule1[AST] = rule {
    (IRIREF | prefixedName) ~> ASTIri ~ ws
  }

  //[18] IRIREF 	::= 	'<' ([^#x00-#x20<>"{}|^`\] | UCHAR)* '>'
  /* #x00=NULL #01-#x1F=control codes #x20=space */
  def IRIREF = rule {
    '<' ~ clearSB ~ (IRIREF_CHAR ~ appendSB |
      !(((str("\\u000") | str("\\u001") | str("\\U0000000") | str("\\U0000001")) ~ HexDigit) |
        str("\\u0020") | str("\\U00000020") | str("\\u0034") | str("\\U00000034") |
        str("\\u003C") | str("\\u003c") | str("\\U0000003C") | str("\\U0000003c") |
        str("\\u003E") | str("\\u003e") | str("\\U0000003E") | str("\\U0000003e") |
        str("\\u005C") | str("\\u005c") | str("\\U0000005C") | str("\\U0000005c") |
        str("\\u005E") | str("\\u005e") | str("\\U0000005E") | str("\\U0000005E") |
        str("\\u0060") | str("\\U00000060") |
        str("\\u007B") | str("\\u007b") | str("\\U0000007B") | str("\\U0000007b") |
        str("\\u007C") | str("\\u007c") | str("\\U0000007C") | str("\\U0000007c") |
        str("\\u007D") | str("\\u007d") | str("\\U0000007D") | str("\\U0000007d")) ~ UCHAR).* ~ push(sb.toString) ~ '>' ~> ASTIriRef
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
    PNAME_NS ~ PN_LOCAL ~> ASTPNameLN
  }

  //[167s] N_PREFIX 	::= 	PN_CHARS_BASE ((PN_CHARS | '.')* PN_CHARS)?
  /* A prefix name may not start or end with a '.' (DOT), but is allowed to have any number of '.' in between.
	 The predicate "&(DOT.* ~ PN_CHARS)", looks ahead and checks if the rule in braces will be fullfilled.
	 It does so without interfering with the parsing process.

	 Example:
	 [] <b> c:d.1..2...3.
	 Due to the predicate the last '.' is not part of the local name. The accepted name is "c:d.1..2...3",
	 with the last '.' being recognized as triple terminator.
	 */
  def PN_PREFIX = rule {
    capture(PN_CHARS_BASE ~ ((PN_CHARS_DOT ~ &(DOT.* ~ PN_CHARS)).* ~ PN_CHARS).?) ~> ASTPNPrefix
  }

  //[168s] PN_LOCAL 	::= 	(PN_CHARS_U | ':' | [0-9] | PLX) ((PN_CHARS | '.' | ':' | PLX)* (PN_CHARS | ':' | PLX))?
  /* A local name may not start or end with a '.' (DOT), but is allowed to have any number of '.' in between.
	 The predicate "&(PLX | DOT.* ~ PN_CHARS_COLON)", looks ahead and checks if the rule in braces will be fullfilled.
	 It does so without interfering with the parsing process.

	 Example:
	 [] <b> c:d.1..2...3.
	 Due to the predicate the last '.' is not part of the local name. The accepted name is "c:d.1..2...3",
	 with the last '.' being recognized as triple terminator.
	 */
  def PN_LOCAL = rule {
    capture((PLX | PN_CHARS_U_COLON_DIGIT) ~ (((PLX | PN_CHARS_DOT_COLON) ~ &(PLX | DOT.* ~ PN_CHARS_COLON)).* ~ (PLX | PN_CHARS_COLON)).?) ~> ASTPNLocal
  }

  //[169s] PLX 	::= 	PERCENT | PN_LOCAL_ESC
  def PLX = rule {
    PERCENT | PN_LOCAL_ESC
  }

  //[170s] PERCENT 	::= 	'%' HEX HEX
  def PERCENT = rule {
    '%' ~ HexDigit ~ HexDigit
  }

  //[172s] PN_LOCAL_ESC 	::= 	'\' ('_' | '~' | '.' | '-' | '!' | '$' | '&' | "'" | '(' | ')' | '*' | '+' | ',' | ';' | '=' | '/' | '?' | '#' | '@' | '%')
  def PN_LOCAL_ESC = rule {
    '\\' ~ LOCAL_ESC
  }

  //[137s] BlankNode 	::= 	BLANK_NODE_LABEL | ANON
  def blankNode = rule {
    (BLANK_NODE_LABEL | ANON) ~> ASTBlankNode
  }

  //[141s] BLANK_NODE_LABEL 	::= 	'_:' (PN_CHARS_U | [0-9]) ((PN_CHARS | '.')* PN_CHARS)?
  /* A blank node label is allowed to contain dots ('.'), but it is forbidden as last character of the recognized label name.
	 The reason for this is, when '.' is used as last character of a blank node label, it collides with triple termination,
	 which is signaled by '.', too.
	 The predicate "&(DOT.* ~ PN_CHARS)", looks ahead and checks if the rule in braces will be fullfilled.
	 It does so without interfering with the parsing process.

	 Example:
	 <a> <b> _:c.1..2...3.
	 Due to the predicate the last '.' is not part of the blank node label. The accepted name is "_:c.1..2...3",
	 with the last '.' being recognized as triple terminator.
	 */
  def BLANK_NODE_LABEL = rule {
    str("_:") ~ capture(PN_CHARS_U_DIGIT ~ ((PN_CHARS_DOT ~ &(DOT.* ~ PN_CHARS)).* ~ PN_CHARS).?) ~> ASTBlankNodeLabel ~ ws
  }

  //[162s] ANON 	::= 	'[' WS* ']'
  def ANON = rule {
    capture("[" ~ "]") ~> ASTAnon
  }
}