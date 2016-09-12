/*
* Copyright (C) 2015 Juergen Pfundt
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

import java.io.{ BufferedWriter, FileOutputStream, OutputStreamWriter, StringWriter }
import java.nio.charset.StandardCharsets
import java.util.Calendar

import org.parboiled2.{ ParseError, ParserInput }
import org.scalatest.FlatSpec

import scala.util.Failure

class NQuadEARLSpec extends FlatSpec with RDFQuadOutput {

  val earl = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./quadearl.nq"), StandardCharsets.UTF_8))

  def earlOut(testcase: String, passed: Boolean) = {
    System.err.flush()
    val assertedBy = "<https://github.com/JuPfu#me>"
    val subject = "<https://github.com/JuPfu/chelona>"
    val test = testcase // "IRI_subject"
    val outcome = if (passed) "passed" else "failed"
    val datum = Calendar.getInstance.getTime
    val datum_format = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    val mode = "automatic"
    val earl_assertion = s"""[ a earl:Assertion;\n  earl:assertedBy ${assertedBy};\n  earl:subject ${subject};\n  earl:test <http://www.w3.org/2013/TrigTests/manifest.nq#${test}>;\n  earl:result [\n    a earl:TestResult;\n    earl:outcome earl:${outcome};\n    dc:date "${datum_format.format(datum)}"^^xsd:dateTime];\n  earl:mode earl:${mode} ] .\n"""
    earl.write(earl_assertion);
    earl.flush()
  }

  "The input file ./NQuadTests/nt-syntax-bad-struct-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // N-Triples does not have objectList (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-struct-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-struct-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-struct-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-struct-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-base-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // @base not allowed in N-Triples (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-base-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-base-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-base-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-base-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-file-03.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // One comment, one empty line
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-file-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(0), "Number of quads generated should have been 0")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-file-03_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-file-03.nt")
    earlOut("nt-syntax-file-03", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bad-uri-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Graph name URI must be absolute (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bad-uri-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-uri-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-uri-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nq-syntax-bad-uri-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-string-02.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // mismatching string literal open/close (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-string-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-02.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-02.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-string-02", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_REVERSE_SOLIDUS.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with REVERSE SOLIDUS
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_REVERSE_SOLIDUS.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_REVERSE_SOLIDUS.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_REVERSE_SOLIDUS.nt")
    earlOut("literal_with_REVERSE_SOLIDUS", true)

    output.close()
  }

  "The input file ./NQuadTests/minimal_whitespace.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // tests absense of whitespace between subject, predicate, object and end-of-statement
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/minimal_whitespace.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(6), "Number of quads generated should have been 6")

    val nt = io.Source.fromFile("./NQuadTests/minimal_whitespace_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in minimal_whitespace.nt")
    earlOut("minimal_whitespace", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-prefix-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // @prefix not allowed in n-triples (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-prefix-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-prefix-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-prefix-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-prefix-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_2_dquotes.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with 2 squotes """"""a""""b""""""
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_2_dquotes.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_2_dquotes.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_2_dquotes.nt")
    earlOut("literal_with_2_dquotes", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-uri-02.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // IRIs with Unicode escape
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-uri-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-uri-02_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-uri-02.nt")
    earlOut("nt-syntax-uri-02", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-string-02.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // langString literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-string-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-string-02.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-string-02.nt")
    earlOut("nt-syntax-string-02", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bad-quint-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // N-Quads does not have a fifth element (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bad-quint-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-quint-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-quint-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nq-syntax-bad-quint-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-struct-02.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // N-Triples does not have predicateObjectList (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-struct-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-struct-02.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-struct-02.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-struct-02", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_numeric_escape4.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with numeric escape4 \\u
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_numeric_escape4.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_numeric_escape4_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_numeric_escape4.nt")
    earlOut("literal_with_numeric_escape4", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-uri-04.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // URI graph with simple literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-uri-04.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-uri-04_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-uri-04.nt")
    earlOut("nq-syntax-uri-04", true)

    output.close()
  }

  "The input file ./NQuadTests/literal_all_controls.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal_all_controls '\x00\x01\x02\x03\x04...'
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_all_controls.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_all_controls_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_all_controls.nt")
    earlOut("literal_all_controls", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bnode-04.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // BNode graph with simple literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-04.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-04_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-bnode-04.nt")
    earlOut("nq-syntax-bnode-04", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bad-literal-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Graph name may not be a simple literal (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bad-literal-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-literal-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-literal-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nq-syntax-bad-literal-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-03.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : bad long escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-03.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-03.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-03", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-07.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : relative IRI not allowed in predicate (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-07.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-07.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-07.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-07", !res)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bnode-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // BNode graph with URI triple
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-01_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-bnode-01.nt")
    earlOut("nq-syntax-bnode-01", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-string-03.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // single quotes (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-string-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-03.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-03.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-string-03", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-04.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : character escapes not allowed (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-04.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-04.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-04.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-04", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bnode-03.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // Blank node labels may start with a digit
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bnode-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(2), "Number of quads generated should have been 2")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-bnode-03_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-bnode-03.nt")
    earlOut("nt-syntax-bnode-03", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-uri-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // URI graph with URI triple
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-uri-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-uri-01_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-uri-01.nt")
    earlOut("nq-syntax-uri-01", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-str-esc-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // string literal with escaped newline
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-str-esc-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-str-esc-01.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-str-esc-01.nt")
    earlOut("nt-syntax-str-esc-01", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-subm-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // Submission test from Original RDF Test Cases
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-subm-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(30), "Number of quads generated should have been 30")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-subm-01_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-subm-01.nt")
    earlOut("nt-syntax-subm-01", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-string-03.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // langString literal with region
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-string-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-string-03.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-string-03.nt")
    earlOut("nt-syntax-string-03", true)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_CHARACTER_TABULATION.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with CHARACTER TABULATION
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_CHARACTER_TABULATION.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_CHARACTER_TABULATION.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_CHARACTER_TABULATION.nt")
    earlOut("literal_with_CHARACTER_TABULATION", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-datatypes-02.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // integer as xsd:string
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-datatypes-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-datatypes-02.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-datatypes-02.nt")
    earlOut("nt-syntax-datatypes-02", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-uri-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // Only IRIs
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-uri-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-uri-01.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-uri-01.nt")
    earlOut("nt-syntax-uri-01", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-string-04.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // long single string literal (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-string-04.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-04.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-04.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-string-04", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_LINE_FEED.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with LINE FEED
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_LINE_FEED.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_LINE_FEED.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_LINE_FEED.nt")
    earlOut("literal_with_LINE_FEED", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-lang-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // langString with bad lang (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-lang-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-lang-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-lang-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-lang-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-str-esc-02.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // string literal with Unicode escape
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-str-esc-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-str-esc-02_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-str-esc-02.nt")
    earlOut("nt-syntax-str-esc-02", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-esc-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad string escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-esc-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-esc-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-esc-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-esc-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-uri-06.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // URI graph with datatyped literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-uri-06.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-uri-06_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-uri-06.nt")
    earlOut("nq-syntax-uri-06", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-num-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // no numbers in N-Triples (integer) (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-num-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-num-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-num-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-num-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-uri-03.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // URI graph with BNode object
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-uri-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-uri-03_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-uri-03.nt")
    earlOut("nq-syntax-uri-03", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-string-06.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // string literal with no end (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-string-06.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-06.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-06.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-string-06", !res)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bnode-06.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // BNode graph with datatyped literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-06.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-06_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-bnode-06.nt")
    earlOut("nq-syntax-bnode-06", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-09.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : relative IRI not allowed in datatype (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-09.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-09.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-09.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-09", !res)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bnode-03.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // BNode graph with BNode object
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-03_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-bnode-03.nt")
    earlOut("nq-syntax-bnode-03", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-string-05.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // long double string literal (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-string-05.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-05.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-05.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-string-05", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-02.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : bad escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-02.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-02.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-02", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-06.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : relative IRI not allowed in subject (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-06.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-06.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-06.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-06", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-esc-02.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad string escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-esc-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-esc-02.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-esc-02.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-esc-02", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_ascii_boundaries.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal_ascii_boundaries '\x00\x26\x28...'
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_ascii_boundaries.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_ascii_boundaries.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_ascii_boundaries.nt")
    earlOut("literal_ascii_boundaries", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-str-esc-03.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // string literal with long Unicode escape
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-str-esc-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-str-esc-03_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-str-esc-03.nt")
    earlOut("nt-syntax-str-esc-03", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bnode-02.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // bnode object
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bnode-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(2), "Number of quads generated should have been 2")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-bnode-02_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-bnode-02.nt")
    earlOut("nt-syntax-bnode-02", true)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_squote.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with squote ""x'y""
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_squote.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_squote.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_squote.nt")
    earlOut("literal_with_squote", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bad-literal-03.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Graph name may not be a datatyped literal (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bad-literal-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-literal-03.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-literal-03.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nq-syntax-bad-literal-03", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-file-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // Empty file
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-file-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(0), "Number of quads generated should have been 0")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-file-01.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-file-01.nt")
    earlOut("nt-syntax-file-01", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-num-03.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // no numbers in N-Triples (float) (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-num-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-num-03.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-num-03.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-num-03", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_CARRIAGE_RETURN.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with CARRIAGE RETURN
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_CARRIAGE_RETURN.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_CARRIAGE_RETURN.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_CARRIAGE_RETURN.nt")
    earlOut("literal_with_CARRIAGE_RETURN", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-uri-03.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // IRIs with long Unicode escape
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-uri-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-uri-03_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-uri-03.nt")
    earlOut("nt-syntax-uri-03", true)

    output.close()
  }

  "The input file ./NQuadTests/lantag_with_subtag.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // lantag with subtag ""x""@en-us
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/lantag_with_subtag.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/lantag_with_subtag.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in lantag_with_subtag.nt")
    earlOut("lantag_with_subtag", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bad-literal-02.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Graph name may not be a language tagged literal (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bad-literal-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-literal-02.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nq-syntax-bad-literal-02.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nq-syntax-bad-literal-02", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_REVERSE_SOLIDUS2.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // REVERSE SOLIDUS at end of literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_REVERSE_SOLIDUS2.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_REVERSE_SOLIDUS2.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_REVERSE_SOLIDUS2.nt")
    earlOut("literal_with_REVERSE_SOLIDUS2", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-num-02.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // no numbers in N-Triples (decimal) (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-num-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-num-02.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-num-02.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-num-02", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-datatypes-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // xsd:byte literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-datatypes-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-datatypes-01.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-datatypes-01.nt")
    earlOut("nt-syntax-datatypes-01", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-string-07.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // string literal with no start (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-string-07.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-07.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-07.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-string-07", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_UTF8_boundaries.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal_with_UTF8_boundaries '\x80\x7ff\x800\xfff...'
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_UTF8_boundaries.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_UTF8_boundaries.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_UTF8_boundaries.nt")
    earlOut("literal_with_UTF8_boundaries", true)

    output.close()
  }

  "The input file ./NQuadTests/literal_all_punctuation.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal_all_punctuation '!""#$%&()...'
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_all_punctuation.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_all_punctuation.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_all_punctuation.nt")
    earlOut("literal_all_punctuation", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-uri-04.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // Legal IRIs
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-uri-04.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-uri-04_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-uri-04.nt")
    earlOut("nt-syntax-uri-04", true)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_FORM_FEED.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with FORM FEED
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_FORM_FEED.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_FORM_FEED.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_FORM_FEED.nt")
    earlOut("literal_with_FORM_FEED", true)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_dquote.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with dquote ""x""y""
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_dquote.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_dquote.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_dquote.nt")
    earlOut("literal_with_dquote", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-esc-03.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad string escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-esc-03.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-esc-03.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-esc-03.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-esc-03", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_BACKSPACE.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with BACKSPACE
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_BACKSPACE.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_BACKSPACE.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_BACKSPACE.nt")
    earlOut("literal_with_BACKSPACE", true)

    output.close()
  }

  "The input file ./NQuadTests/langtagged_string.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // langtagged string ""x""@en
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/langtagged_string.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/langtagged_string.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in langtagged_string.nt")
    earlOut("langtagged_string", true)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_numeric_escape8.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with numeric escape8 \U
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_numeric_escape8.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_numeric_escape8_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_numeric_escape8.nt")
    earlOut("literal_with_numeric_escape8", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bnode-02.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // BNode graph with BNode subject
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-02_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-bnode-02.nt")
    earlOut("nq-syntax-bnode-02", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : space (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-05.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : character escapes not allowed (2) (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-05.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-05.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-05.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-05", !res)

    output.close()
  }

  "The input file ./NQuadTests/comment_following_triple.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // Tests comments after a triple
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/comment_following_triple.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(5), "Number of quads generated should have been 5")

    val nt = io.Source.fromFile("./NQuadTests/comment_following_triple_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in comment_following_triple.nt")
    earlOut("comment_following_triple", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-uri-05.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // URI graph with language tagged literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-uri-05.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-uri-05_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-uri-05.nt")
    earlOut("nq-syntax-uri-05", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-file-02.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // Only comment
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-file-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(0), "Number of quads generated should have been 0")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-file-02_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-file-02.nt")
    earlOut("nt-syntax-file-02", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-string-01.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // mismatching string literal open/close (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-string-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-01.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-string-01.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-string-01", !res)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-uri-02.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // URI graph with BNode subject
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-uri-02.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-uri-02_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-uri-02.nt")
    earlOut("nq-syntax-uri-02", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bnode-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // bnode subject
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bnode-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-bnode-01_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-bnode-01.nt")
    earlOut("nt-syntax-bnode-01", true)

    output.close()
  }

  "The input file ./NQuadTests/nq-syntax-bnode-05.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // BNode graph with language tagged literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-05.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nq-syntax-bnode-05_isomorphic.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nq-syntax-bnode-05.nt")
    earlOut("nq-syntax-bnode-05", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-bad-uri-08.nq" must "fail" taggedAs (TestNQuadsNegativeSyntax) in {
    // Bad IRI : relative IRI not allowed in object (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-bad-uri-08.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    val res = parser.nquadsDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-08.nq': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './NQuadTests/nt-syntax-bad-uri-08.nq': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("nt-syntax-bad-uri-08", !res)

    output.close()
  }

  "The input file ./NQuadTests/literal_with_2_squotes.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal with 2 squotes ""x''y""
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal_with_2_squotes.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal_with_2_squotes.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal_with_2_squotes.nt")
    earlOut("literal_with_2_squotes", true)

    output.close()
  }

  "The input file ./NQuadTests/literal.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // literal """"""x""""""
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/literal.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/literal.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in literal.nt")
    earlOut("literal", true)

    output.close()
  }

  "The input file ./NQuadTests/nt-syntax-string-01.nq" must "succeed" taggedAs (TestNQuadsPositiveSyntax) in {
    // string literal
    lazy val input: ParserInput = io.Source.fromFile("./NQuadTests/nt-syntax-string-01.nq").mkString

    val output = new StringWriter()

    val quad = new EvalNQuad(quadWriter(output)_, "http://www.w3.org/2013/NQuadTests", "")

    val parser = NQuadParser(input, quad.renderStatement, false, "http://www.w3.org/2013/NQuadTests", "")

    assert(parser.nquadsDoc.run() == scala.util.Success(1), "Number of quads generated should have been 1")

    val nt = io.Source.fromFile("./NQuadTests/nt-syntax-string-01.nq").mkString

    assert(output.toString == nt.toString, "Quads generated should be exactly as in nt-syntax-string-01.nt")
    earlOut("nt-syntax-string-01", true)

    output.close()
  }

}
