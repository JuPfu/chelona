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

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter, StringWriter}
import java.nio.charset.StandardCharsets
import java.util.Calendar

import org.parboiled2.{ParseError, ParserInput}
import org.scalatest.FlatSpec

import scala.util.Failure

class TrigEARLSpec extends FlatSpec {

  val earl = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./trigearl.trig"), StandardCharsets.UTF_8))

  def earlOut(testcase: String, passed: Boolean) = {
    System.err.flush()
    val assertedBy = "<https://github.com/JuPfu#me>"
    val subject = "<https://github.com/JuPfu/chelona>"
    val test = testcase // "IRI_subject"
    val outcome = if (passed) "passed" else "failed"
    val datum = Calendar.getInstance.getTime
    val mode = "automatic"
    val earl_assertion = s"""[ a earl:Assertion;\n  earl:assertedBy ${assertedBy};\n  earl:subject ${subject};\n  earl:test <http://www.w3.org/2013/TurtleTests/manifest.trig#${test}>;\n  earl:result [\n    a earl:TestResult;\n    earl:outcome earl:${outcome};\n    dc:date "${datum}"^^xsd:dateTime];\n  earl:mode earl:${mode} ] .\n"""
    earl.write(earl_assertion);
    earl.flush()
  }


  "The input file ./TrigTests/trig-syntax-str-esc-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // string literal with long Unicode escape
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-str-esc-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-str-esc-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-str-esc-03.nq")
    earlOut("trig-syntax-str-esc-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-kw-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // 'true' cannot be used as subject (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-kw-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-kw-04", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-file-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Only comment
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-file-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-file-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-file-02.nq")
    earlOut("trig-syntax-file-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // langString literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-02.nq")

    earlOut("trig-syntax-string-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/prefix_only_IRI.trig" must "succeed" taggedAs (TestTrigEval) in {
    // prefix-only IRI (p:)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/prefix_only_IRI.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/prefix_only_IRI.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in prefix_only_IRI.nq")
    earlOut("prefix_only_IRI", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/sole_blankNodePropertyList.trig" must "succeed" taggedAs (TestTrigEval) in {
    // sole blankNodePropertyList [ <p> <o> ] .
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/sole_blankNodePropertyList.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nq = io.Source.fromFile("./TrigTests/sole_blankNodePropertyList.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in sole_blankNodePropertyList.nq")
    earlOut("sole_blankNodePropertyList", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-missing-ns-dot-end.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Prefix must not end in dot (error in triple, not prefix directive like trig-syntax-bad-ns-dot-end)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-missing-ns-dot-end.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")


    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-missing-ns-dot-end.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-missing-ns-dot-end.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-missing-ns-dot-end", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Empty PREFIX
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-03.nq")
    earlOut("trig-syntax-prefix-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL2_with_UTF8_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL2_with_UTF8_boundaries '\x80\x7ff\x800\xfff...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL2_with_UTF8_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/LITERAL2_with_UTF8_boundaries.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL2_with_UTF8_boundaries.nq")
    earlOut("LITERAL2_with_UTF8_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-06.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // labeled bnode subject
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-06.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-06.nq")
    earlOut("trig-syntax-bnode-06", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-09.trig" must "succeed" taggedAs (TestTrigEval) in {
    // empty collection
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-09.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-09.nq")
    earlOut("trig-subm-09", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-eval-bad-01.trig" must "fail" taggedAs (TestTrigNegativeEval) in {
    // Bad IRI : good escape, bad charcater (negative evaluation test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-eval-bad-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-eval-bad-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-eval-bad-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-eval-bad-01", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-struct-06.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // missing '.'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-struct-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-struct-06.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-struct-06.nq")
    earlOut("trig-syntax-struct-06", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-12.trig" must "succeed" taggedAs (TestTrigEval) in {
    // - and _ in names and qnames
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-12.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(8), "Number of triples generated should have been 8")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-12.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-12.nq")
    earlOut("trig-subm-12", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-esc-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad string escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-esc-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")



    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-esc-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-esc-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-esc-01", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-minimal-whitespace-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // tests absense of whitespace in various positions
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-minimal-whitespace-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(56), "Number of triples generated should have been 56")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-minimal-whitespace-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-minimal-whitespace-01.nq")
    earlOut("trig-syntax-minimal-whitespace-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-prefix-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // No prefix (2) (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-prefix-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-prefix-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-09.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // double literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-09.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-09.nq")
    earlOut("trig-syntax-number-09", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // bnode subject
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-01.nq")
    earlOut("trig-syntax-bnode-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-05.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // @prefix with no suffix
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-05.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-05.nq")
    earlOut("trig-syntax-prefix-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-eval-bad-04.trig" must "fail" taggedAs (TestTrigNegativeEval) in {
    // Bad IRI : {abc} (negative evaluation test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-eval-bad-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-eval-bad-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-eval-bad-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-eval-bad-04", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-06.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Turtle does not allow bnodes-as-predicates (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-06.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-06.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-06", !res)

    output.close()
  }

  "The input file ./TrigTests/collection_subject.trig" must "succeed" taggedAs (TestTrigEval) in {
    // collection subject
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/collection_subject.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/collection_subject_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in collection_subject.nq")
    earlOut("collection_subject", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-14.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // literal as subject (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-14.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-14.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-14.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-14", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // N3 paths not in Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-04", !res)

    output.close()
  }

  "The input file ./TrigTests/blankNodePropertyList_as_object.trig" must "succeed" taggedAs (TestTrigEval) in {
    // blankNodePropertyList as object <s> <p> [ … ] .
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/blankNodePropertyList_as_object.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/blankNodePropertyList_as_object_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in blankNodePropertyList_as_object.nq")
    earlOut("blankNodePropertyList_as_object", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-turtle-05.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // TriG can parse Turtle (bare blankNodePropertyList)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-turtle-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-turtle-05.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-turtle-05.nq")
    earlOut("trig-turtle-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL1_with_UTF8_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL1_with_UTF8_boundaries '\x80\x7ff\x800\xfff...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL1_with_UTF8_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/LITERAL1_with_UTF8_boundaries.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL1_with_UTF8_boundaries.nq")
    earlOut("LITERAL1_with_UTF8_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-12.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @forAll is not Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-12.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-12.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-12.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-12", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // N3 is...of not in Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-05", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-15.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // literal as predicate (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-15.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-15.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-15.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-15", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    //
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-04.nq")
    earlOut("trig-kw-graph-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-01.trig" must "succeed" taggedAs (TestTrigEval) in {
    // Blank subject
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TriGTests/trig-subm-01.trig", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-01_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-01.nq")
    earlOut("trig-subm-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-lists-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // mixed list
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-lists-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-lists-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-lists-02.nq")
    earlOut("trig-syntax-lists-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-22.trig" must "succeed" taggedAs (TestTrigEval) in {
    // boolean literals
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-22.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-22.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-22.nq")
    earlOut("trig-subm-22", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // GRAPH needs {}
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-03", !res)

    output.close()
  }

  "The input file ./TrigTests/first.trig" must "succeed" taggedAs (TestTrigEval) in {
    // first, not last, non-empty nested collection
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/first.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(14), "Number of triples generated should have been 14")

    val nq = io.Source.fromFile("./TrigTests/first_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in first.nq")
    earlOut("first", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-10.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // long langString literal with embedded newline
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-10.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-10.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-10.nq")
    earlOut("trig-syntax-string-10", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL2.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL2 "x"
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL2.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/LITERAL2.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL2.nq")
    earlOut("LITERAL2", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-datatypes-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // integer as xsd:string
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-datatypes-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-datatypes-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-datatypes-02.nq")
    earlOut("trig-syntax-datatypes-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/literal_with_escaped_LINE_FEED.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with escaped LINE FEED
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_escaped_LINE_FEED.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_escaped_LINE_FEED.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_escaped_LINE_FEED.nq")
    earlOut("literal_with_escaped_LINE_FEED", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-ns-dot-start.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Prefix must not start with dot
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-ns-dot-start.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ns-dot-start.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ns-dot-start.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-ns-dot-start", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-10.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // A graph may not be named with an empty collection
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-10.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-10.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-10.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-10", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-14.trig" must "succeed" taggedAs (TestTrigEval) in {
    // bare : allowed
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-14.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-14_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-14.nq")
    earlOut("trig-subm-14", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-ln-colons.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Colons in pname local names
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-ln-colons.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-ln-colons.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-ln-colons.nq")
    earlOut("trig-syntax-ln-colons", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-str-esc-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // string literal with Unicode escape
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-str-esc-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-str-esc-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-str-esc-02.nq")
    earlOut("trig-syntax-str-esc-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-base-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // base with relative IRIs
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-base-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-base-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-base-04.nq")
    earlOut("trig-syntax-base-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-09.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Named graph may be named with PNAME
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-09.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-09.nq")
    earlOut("trig-kw-graph-09", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/negative_numeric.trig" must "succeed" taggedAs (TestTrigEval) in {
    // negative numeric
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/negative_numeric.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/negative_numeric.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in negative_numeric.nq")
    earlOut("negative_numeric", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-07.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // long string literal with embedded single- and double-quotes
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-07.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-07.nq")
    earlOut("trig-syntax-string-07", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-pname-esc-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // pname with back-slash escapes (2)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-pname-esc-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-pname-esc-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-pname-esc-02.nq")
    earlOut("trig-syntax-pname-esc-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-eval-struct-01.trig" must "succeed" taggedAs (TestTrigEval) in {
    // triple with IRIs
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-eval-struct-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-eval-struct-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-eval-struct-01.nq")
    earlOut("trig-eval-struct-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // negative integer literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-02.nq")
    earlOut("trig-syntax-number-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/last.trig" must "succeed" taggedAs (TestTrigEval) in {
    // last, not first, non-empty nested collection
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/last.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(14), "Number of triples generated should have been 14")

    val nq = io.Source.fromFile("./TrigTests/last_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in last.nq")
    earlOut("last", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-ln-escape.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad hex escape in local name
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-ln-escape.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ln-escape.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ln-escape.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-ln-escape", !res)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG2_with_UTF8_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG2_with_UTF8_boundaries '\x80\x7ff\x800\xfff...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG2_with_UTF8_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG2_with_UTF8_boundaries.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG2_with_UTF8_boundaries.nq")
    earlOut("LITERAL_LONG2_with_UTF8_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-base-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @base inside graph (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-base-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-base-04", !res)

    output.close()
  }

  "The input file ./TrigTests/IRI_with_four_digit_numeric_escape.trig" must "succeed" taggedAs (TestTrigEval) in {
    // IRI with four digit numeric escape
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/IRI_with_four_digit_numeric_escape.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/IRI_with_four_digit_numeric_escape.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in IRI_with_four_digit_numeric_escape.nq")
    earlOut("IRI_with_four_digit_numeric_escape", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-string-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // mismatching string literal open/close (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-string-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-string-02", !res)

    output.close()
  }

  "The input file ./TrigTests/IRIREF_datatype.trig" must "succeed" taggedAs (TestTrigEval) in {
    // IRIREF datatype ""^^<t>
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/IRIREF_datatype.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/IRIREF_datatype.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in IRIREF_datatype.nq")
    earlOut("IRIREF_datatype", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/literal_with_CARRIAGE_RETURN.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with CARRIAGE RETURN
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_CARRIAGE_RETURN.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_CARRIAGE_RETURN_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_CARRIAGE_RETURN.nq")
    earlOut("literal_with_CARRIAGE_RETURN", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/number_sign_following_localName.trig" must "succeed" taggedAs (TestTrigEval) in {
    // number sign following localName
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/number_sign_following_localName.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/number_sign_following_localName.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in number_sign_following_localName.nq")
    earlOut("number_sign_following_localName", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-string-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // mismatching string literal long/short (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-string-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-string-03", !res)

    output.close()
  }

  "The input file ./TrigTests/collection_object.trig" must "succeed" taggedAs (TestTrigEval) in {
    // collection object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/collection_object.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/collection_object_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in collection_object.nq")
    earlOut("collection_object", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // = is not Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-base-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // @base
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-base-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-base-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-base-01.nq")
    earlOut("trig-syntax-base-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // localName with assigned, NFC-normalized, basic-multilingual-plane PN CHARS BASE character boundaries (p:AZazÀÖØöø...)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.nq")
    earlOut("localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-collection-graph-bad-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // A graph may not be named with an empty collection
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-collection-graph-bad-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-collection-graph-bad-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-collection-graph-bad-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-collection-graph-bad-01", !res)

    output.close()
  }

  "The input file ./TrigTests/literal_with_CHARACTER_TABULATION.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with CHARACTER TABULATION
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_CHARACTER_TABULATION.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_CHARACTER_TABULATION_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_CHARACTER_TABULATION.nq")
    earlOut("literal_with_CHARACTER_TABULATION", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-esc-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad string escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-esc-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-esc-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-esc-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-esc-03", !res)

    output.close()
  }

  "The input file ./TrigTests/prefix_with_PN_CHARS_BASE_character_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // prefix with PN CHARS BASE character boundaries (prefix: AZazÀÖØöø...:)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/prefix_with_PN_CHARS_BASE_character_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/prefix_with_PN_CHARS_BASE_character_boundaries.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in prefix_with_PN_CHARS_BASE_character_boundaries.nq")
    earlOut("prefix_with_PN_CHARS_BASE_character_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-07.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Turtle does not allow labeled bnodes-as-predicates (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-07.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-07.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-07", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-pname-esc-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // pname with back-slash escapes
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-pname-esc-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-pname-esc-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-pname-esc-01.nq")
    earlOut("trig-syntax-pname-esc-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-lists-05.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // mixed lists with embedded lists
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-lists-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(19), "Number of triples generated should have been 19")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-lists-05.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-lists-05.nq")
    earlOut("trig-syntax-lists-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-19.trig" must "succeed" taggedAs (TestTrigEval) in {
    // positive integer, decimal and doubles
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-19.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-19.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-19.nq")
    earlOut("trig-subm-19", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-ln-dash-start.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Local name must not begin with dash
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-ln-dash-start.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ln-dash-start.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ln-dash-start.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-ln-dash-start", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Turtle does not allow literals-as-subjects (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-04", !res)

    output.close()
  }

  "The input file ./TrigTests/langtagged_LONG.trig" must "succeed" taggedAs (TestTrigEval) in {
    // langtagged LONG """x"""@en
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/langtagged_LONG.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/langtagged_LONG.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in langtagged_LONG.nq")
    earlOut("langtagged_LONG", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-num-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad number format (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-num-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-num-05", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-LITERAL2_with_langtag_and_datatype.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad number format (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-LITERAL2_with_langtag_and_datatype.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-LITERAL2_with_langtag_and_datatype.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-LITERAL2_with_langtag_and_datatype.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-LITERAL2_with_langtag_and_datatype", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // GRAPH not followed by DOT
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-16.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // bnode as predicate (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-16.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-16.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-16.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-16", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-uri-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // IRIs with long Unicode escape
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-uri-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-uri-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-uri-03.nq")
    earlOut("trig-syntax-uri-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-missing-ns-dot-start.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Prefix must not start with dot (error in triple, not prefix directive like trig-syntax-bad-ns-dot-end)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-missing-ns-dot-start.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-missing-ns-dot-start.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-missing-ns-dot-start.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-missing-ns-dot-start", !res)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG2_with_2_squotes.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG2 with 2 squotes """a""b"""
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG2_with_2_squotes.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG2_with_2_squotes.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG2_with_2_squotes.nq")
    earlOut("LITERAL_LONG2_with_2_squotes", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/comment_following_PNAME_NS.trig" must "succeed" taggedAs (TestTrigEval) in {
    // comment following PNAME_NS
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/comment_following_PNAME_NS.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/comment_following_PNAME_NS.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in comment_following_PNAME_NS.nq")
    earlOut("comment_following_PNAME_NS", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-11.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // A graph may not be named with a collection
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-11.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-11.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-11.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-11", !res)

    output.close()
  }

  "The input file ./TrigTests/percent_escaped_localName.trig" must "succeed" taggedAs (TestTrigEval) in {
    // percent-escaped local name
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/percent_escaped_localName.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/percent_escaped_localName.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in percent_escaped_localName.nq")
    earlOut("percent_escaped_localName", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-09.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // squote long string literal with embedded single- and double-quotes
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-09.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-09.nq")
    earlOut("trig-syntax-string-09", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-26.trig" must "succeed" taggedAs (TestTrigEval) in {
    // Variations on decimal canonicalization
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-26.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(22), "Number of triples generated should have been 22")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-26.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-26.nq")
    earlOut("trig-subm-26", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/localname_with_COLON.trig" must "succeed" taggedAs (TestTrigEval) in {
    // localname with COLON
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/localname_with_COLON.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/localname_with_COLON.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in localname_with_COLON.nq")
    earlOut("localname_with_COLON", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-06.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // negative decimal literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-06.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-06.nq")
    earlOut("trig-syntax-number-06", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-base-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // BASE inside graph (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-base-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-base-05", !res)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG1.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG1 '''x'''
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG1.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG1.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG1.nq")
    earlOut("LITERAL_LONG1", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-pname-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // '~' must be escaped in pname (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-pname-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-pname-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-pname-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-pname-01", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-07.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @keywords is not Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-07.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-07.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-07", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-prefix-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @prefix without URI (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-prefix-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-prefix-03", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-11.trig" must "succeed" taggedAs (TestTrigEval) in {
    // decimal integer canonicalization
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-11.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(10), "Number of triples generated should have been 10")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-11.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-11.nq")
    earlOut("trig-subm-11", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-kw-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // boolean literal (output.toString == nq.toString
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-kw-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-kw-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-kw-01.nq")
    earlOut("trig-syntax-kw-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/bareword_double.trig" must "succeed" taggedAs (TestTrigEval) in {
    // bareword double
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/bareword_double.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/bareword_double.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in bareword_double.nq")
    earlOut("bareword_double", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-17.trig" must "succeed" taggedAs (TestTrigEval) in {
    // floating point number
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-17.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-17.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-17.nq")
    earlOut("trig-subm-17", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-08.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // integer literal with decimal lexical confusion
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-08.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-08.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-08.nq")
    earlOut("trig-syntax-number-08", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-struct-07.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // trailing ';' no '.'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-struct-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-struct-07.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-struct-07.nq")
    earlOut("trig-syntax-struct-07", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-number-dot-in-anon.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Dot delimeter may not appear in anonymous nodes
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-number-dot-in-anon.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-number-dot-in-anon.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-number-dot-in-anon.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-number-dot-in-anon", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-string-07.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Long literal with extra squote (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-string-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-07.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-07.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-string-07", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-kw-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // 'true' cannot be used as object (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-kw-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-kw-05", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // integer literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-01.nq")
    earlOut("trig-syntax-number-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Turtle is not NQuads (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-03", !res)

    output.close()
  }

  "The input file ./TrigTests/old_style_prefix.trig" must "succeed" taggedAs (TestTrigEval) in {
    // old-style prefix
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/old_style_prefix.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/old_style_prefix.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in old_style_prefix.nq")
    earlOut("old_style_prefix", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-collection-graph-bad-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // A graph may not be named with a collection
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-collection-graph-bad-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-collection-graph-bad-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-collection-graph-bad-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-collection-graph-bad-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-turtle-bad-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // TriG is not N-Quads
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-turtle-bad-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-turtle-bad-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-turtle-bad-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-turtle-bad-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-25.trig" must "succeed" taggedAs (TestTrigEval) in {
    // repeating a @prefix changes pname definition
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-25.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-25.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-25.nq")
    earlOut("trig-subm-25", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/positive_numeric.trig" must "succeed" taggedAs (TestTrigEval) in {
    // positive numeric
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/positive_numeric.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/positive_numeric.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in positive_numeric.nq")
    earlOut("positive_numeric", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-num-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad number format (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-num-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-num-04", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-num-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad number format (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-num-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-num-01", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-prefix-07.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // PREFIX inside graph (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-prefix-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-07.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-07.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-prefix-07", !res)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG2_ascii_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG2_ascii_boundaries '\x00\x21\x23...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG2_ascii_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG2_ascii_boundaries_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG2_ascii_boundaries.nq")
    earlOut("LITERAL_LONG2_ascii_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/localName_with_non_leading_extras.trig" must "succeed" taggedAs (TestTrigEval) in {
    // localName with_non_leading_extras (_:a·̀ͯ‿.⁀)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/localName_with_non_leading_extras.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/localName_with_non_leading_extras.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in localName_with_non_leading_extras.nq")
    earlOut("localName_with_non_leading_extras", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/nested_collection.trig" must "succeed" taggedAs (TestTrigEval) in {
    // nested collection (())
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/nested_collection.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(10), "Number of triples generated should have been 10")

    val nq = io.Source.fromFile("./TrigTests/nested_collection_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in nested_collection.nq")
    earlOut("nested_collection", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-06.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    //
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-06.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-06.nq")
    earlOut("trig-kw-graph-06", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-05.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Use of empty prefix inside named graph
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-05.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-05.nq")
    earlOut("trig-kw-graph-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL1_all_controls.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL1_all_controls '\x00\x01\x02\x03\x04...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL1_all_controls.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL1_all_controls_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL1_all_controls.nq")
    earlOut("LITERAL1_all_controls", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG1_with_1_squote.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG1 with 1 squote '''a'b'''
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG1_with_1_squote.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG1_with_1_squote.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG1_with_1_squote.nq")
    earlOut("LITERAL_LONG1_with_1_squote", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-11.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @forSome is not Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-11.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-11.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-11.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-11", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-esc-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad string escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-esc-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-esc-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-esc-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-esc-04", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-base-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // BASE
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-base-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-base-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-base-02.nq")
    earlOut("trig-syntax-base-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-uri-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Legal IRIs
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-uri-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-uri-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-uri-04.nq")
    earlOut("trig-syntax-uri-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-lang-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // langString with bad lang (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-lang-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-lang-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-lang-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-lang-01", !res)

    output.close()
  }

  "The input file ./TrigTests/langtagged_non_LONG.trig" must "succeed" taggedAs (TestTrigEval) in {
    // langtagged non-LONG "x"@en
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/langtagged_non_LONG.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/langtagged_non_LONG.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in langtagged_non_LONG.nq")
    earlOut("langtagged_non_LONG", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // PreFIX
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-02.nq")
    earlOut("trig-syntax-prefix-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // {} fomulae not in Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-01", !res)

    output.close()
  }

  "The input file ./TrigTests/bareword_a_predicate.trig" must "succeed" taggedAs (TestTrigEval) in {
    // bareword a predicate
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/bareword_a_predicate.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/bareword_a_predicate.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in bareword_a_predicate.nq")
    earlOut("bareword_a_predicate", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-09.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // bnode property list
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-09.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-09.nq")
    earlOut("trig-syntax-bnode-09", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG1_with_UTF8_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG1_with_UTF8_boundaries '\x80\x7ff\x800\xfff...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG1_with_UTF8_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG1_with_UTF8_boundaries.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG1_with_UTF8_boundaries.nq")
    earlOut("LITERAL_LONG1_with_UTF8_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/labeled_blank_node_graph.trig" must "succeed" taggedAs (TestTrigEval) in {
    // labeled blank node graph
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/labeled_blank_node_graph.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/labeled_blank_node_graph.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in labeled_blank_node_graph.nq")
    earlOut("labeled_blank_node_graph", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-08.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Named graph may be named with BNode []
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-08.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-08.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-08.nq")
    earlOut("trig-kw-graph-08", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-10.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Named graph with PNAME and empty graph
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-10.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-10.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-10.nq")
    earlOut("trig-kw-graph-10", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-07.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // GRAPH may not include a GRAPH
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-07.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-07.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-07", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-11.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // double literal no fraction
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-11.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-11.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-11.nq")
    earlOut("trig-syntax-number-11", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-list-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Free-standing list of zero elements : bad syntax
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-list-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-list-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-list-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-list-04", !res)

    output.close()
  }

  "The input file ./TrigTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // localName with nfc-normalize PN CHARS BASE character boundaries (p:AZazÀÖØöø...)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in localName_with_nfc_PN_CHARS_BASE_character_boundaries.nq")
    earlOut("localName_with_nfc_PN_CHARS_BASE_character_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG1_with_2_squotes.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG1 with 2 squotes '''a''b'''
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG1_with_2_squotes.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG1_with_2_squotes.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG1_with_2_squotes.nq")
    earlOut("LITERAL_LONG1_with_2_squotes", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-base-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @base in wrong case (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-base-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-base-02", !res)

    output.close()
  }

  "The input file ./TrigTests/alternating_iri_graphs.trig" must "succeed" taggedAs (TestTrigEval) in {
    // alternating graphs with IRI names
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/alternating_iri_graphs.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/alternating_iri_graphs.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in alternating_iri_graphs.nq")
    earlOut("alternating_iri_graphs", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-08.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // long string literal with embedded newline
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-08.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-08.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-08.nq")
    earlOut("trig-syntax-string-08", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-list-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Free-standing list inside {} : bad syntax
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-list-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-list-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-list-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-list-03", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-turtle-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // TriG can parse Turtle (blankNodePropertyList subject)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-turtle-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-turtle-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-turtle-04.nq")
    earlOut("trig-turtle-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-kw-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // boolean literal (false)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-kw-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-kw-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-kw-02.nq")
    earlOut("trig-syntax-kw-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-file-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Empty file
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-file-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-file-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-file-01.nq")
    earlOut("trig-syntax-file-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/literal_true.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal true
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_true.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_true.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_true.nq")
    earlOut("literal_true", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-list-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Free-standing list outside {} : bad syntax
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-list-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-list-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-list-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-list-01", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Empty @prefix with % escape
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-04.nq")
    earlOut("trig-syntax-prefix-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-10.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // extra '.' (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-10.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-10.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-10.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-10", !res)

    output.close()
  }

  "The input file ./TrigTests/comment_following_localName.trig" must "succeed" taggedAs (TestTrigEval) in {
    // comment following localName
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/comment_following_localName.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/comment_following_localName.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in comment_following_localName.nq")
    earlOut("comment_following_localName", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-num-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad number format (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-num-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-num-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // squote string literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-04.nq")
    earlOut("trig-syntax-string-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-09.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // extra '.' (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-09.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-09.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-09", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-15.trig" must "succeed" taggedAs (TestTrigEval) in {
    // simple long literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-15.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-15_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-15.nq")
    earlOut("trig-subm-15", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/literal_with_escaped_CARRIAGE_RETURN.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with escaped CARRIAGE RETURN
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_escaped_CARRIAGE_RETURN.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_escaped_CARRIAGE_RETURN.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_escaped_CARRIAGE_RETURN.nq")
    earlOut("literal_with_escaped_CARRIAGE_RETURN", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-07.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // labeled bnode subject and object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-07.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-07.nq")
    earlOut("trig-syntax-bnode-07", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-10.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // mixed bnode property list and triple
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-10.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-10.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-10.nq")
    earlOut("trig-syntax-bnode-10", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-07.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Named graph may be named with BNode _:a
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-07.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-07.nq")
    earlOut("trig-kw-graph-07", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-eval-bad-03.trig" must "fail" taggedAs (TestTrigNegativeEval) in {
    // Bad IRI : hex 3E is  (negative evaluation test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-eval-bad-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-eval-bad-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-eval-bad-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-eval-bad-03", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-03.trig" must "succeed" taggedAs (TestTrigEval) in {
    // , operator
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-03.nq")
    earlOut("trig-subm-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-uri-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad IRI : character escapes not allowed (2) (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-uri-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-uri-05", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-num-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad number format (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-num-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-num-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-num-03", !res)

    output.close()
  }

  "The input file ./TrigTests/labeled_blank_node_with_leading_digit.trig" must "succeed" taggedAs (TestTrigEval) in {
    // labeled blank node with_leading_digit (_:0)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/labeled_blank_node_with_leading_digit.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/labeled_blank_node_with_leading_digit.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in labeled_blank_node_with_leading_digit.nq")
    earlOut("labeled_blank_node_with_leading_digit", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-08.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // bare bnode property list
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-08.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-08.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-08.nq")
    earlOut("trig-syntax-bnode-08", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-uri-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Only IRIs
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-uri-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-uri-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-uri-01.nq")
    earlOut("trig-syntax-uri-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-turtle-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // TriG can parse Turtle
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-turtle-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-turtle-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-turtle-01.nq")
    earlOut("trig-turtle-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/prefix_reassigned_and_used.trig" must "succeed" taggedAs (TestTrigEval) in {
    // prefix reassigned and used
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/prefix_reassigned_and_used.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/prefix_reassigned_and_used.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in prefix_reassigned_and_used.nq")
    earlOut("prefix_reassigned_and_used", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/prefix_with_non_leading_extras.trig" must "succeed" taggedAs (TestTrigEval) in {
    // prefix with_non_leading_extras (_:a·̀ͯ‿.⁀)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/prefix_with_non_leading_extras.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/prefix_with_non_leading_extras.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in prefix_with_non_leading_extras.nq")
    earlOut("prefix_with_non_leading_extras", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-turtle-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // TriG can parse Turtle (repeated PREFIX)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-turtle-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-turtle-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-turtle-02.nq")
    earlOut("trig-turtle-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-base-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // @base with relative IRIs
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-base-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-base-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-base-03.nq")
    earlOut("trig-syntax-base-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-kw-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // 'a' cannot be used as subject (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-kw-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-kw-02", !res)

    output.close()
  }

  "The input file ./TrigTests/IRI_with_eight_digit_numeric_escape.trig" must "succeed" taggedAs (TestTrigEval) in {
    // IRI with eight digit numeric escape (\U)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/IRI_with_eight_digit_numeric_escape.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/IRI_with_eight_digit_numeric_escape.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in IRI_with_eight_digit_numeric_escape.nq")
    earlOut("IRI_with_eight_digit_numeric_escape", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-27.trig" must "succeed" taggedAs (TestTrigEval) in {
    // Repeating @base changes base for relative IRI lookup
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-27.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-27_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-27.nq")
    earlOut("trig-subm-27", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/double_lower_case_e.trig" must "succeed" taggedAs (TestTrigEval) in {
    // double lower case e
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/double_lower_case_e.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/double_lower_case_e.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in double_lower_case_e.nq")
    earlOut("double_lower_case_e", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/labeled_blank_node_subject.trig" must "succeed" taggedAs (TestTrigEval) in {
    // labeled blank node subject
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/labeled_blank_node_subject.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/labeled_blank_node_subject.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in labeled_blank_node_subject.nq")
    earlOut("labeled_blank_node_subject", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/objectList_with_two_objects.trig" must "succeed" taggedAs (TestTrigEval) in {
    // objectList with two objects … <o1> ,<o2>
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/objectList_with_two_objects.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/objectList_with_two_objects.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in objectList_with_two_objects.nq")
    earlOut("objectList_with_two_objects", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-uri-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad IRI : bad escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-uri-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-uri-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-pname-esc-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // pname with back-slash escapes (3)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-pname-esc-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-pname-esc-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-pname-esc-03.nq")
    earlOut("trig-syntax-pname-esc-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/literal_false.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal false
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_false.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_false.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_false.nq")
    earlOut("literal_false", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-07.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // positive decimal literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-07.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-07.nq")
    earlOut("trig-syntax-number-07", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/predicateObjectList_with_two_objectLists.trig" must "succeed" taggedAs (TestTrigEval) in {
    // predicateObjectList with two objectLists … <o1> ,<o2>
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/predicateObjectList_with_two_objectLists.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/predicateObjectList_with_two_objectLists.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in predicateObjectList_with_two_objectLists.nq")
    earlOut("predicateObjectList_with_two_objectLists", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-base-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @base without URI (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-base-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-base-01", !res)

    output.close()
  }

  "The input file ./TrigTests/localName_with_leading_underscore.trig" must "succeed" taggedAs (TestTrigEval) in {
    // localName with leading underscore (p:_)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/localName_with_leading_underscore.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/localName_with_leading_underscore.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in localName_with_leading_underscore.nq")
    earlOut("localName_with_leading_underscore", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/nested_blankNodePropertyLists.trig" must "succeed" taggedAs (TestTrigEval) in {
    // nested blankNodePropertyLists [ <p1> [ <p2> <o2> ] ; <p3> <o3> ]
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/nested_blankNodePropertyLists.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/nested_blankNodePropertyLists.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in nested_blankNodePropertyLists.nq")
    earlOut("nested_blankNodePropertyLists", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-06.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // GRAPH - Must close {}
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        // due to Parboiled2 formatting error earl output somewhat earlier
        System.err.println("File './TrigTests/trig-graph-bad-06.trig': " + e /*parser.formatError(e)*/)
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-06.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-06", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-pname-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad %-sequence in pname (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-pname-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-pname-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-pname-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-pname-02", !res)

    output.close()
  }

  "The input file ./TrigTests/literal_with_REVERSE_SOLIDUS.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with REVERSE SOLIDUS
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_REVERSE_SOLIDUS.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_REVERSE_SOLIDUS.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_REVERSE_SOLIDUS.nq")
    earlOut("literal_with_REVERSE_SOLIDUS", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/labeled_blank_node_object.trig" must "succeed" taggedAs (TestTrigEval) in {
    // labeled blank node object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/labeled_blank_node_object.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/labeled_blank_node_object.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in labeled_blank_node_object.nq")
    earlOut("labeled_blank_node_object", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/default_namespace_IRI.trig" must "succeed" taggedAs (TestTrigEval) in {
    // default namespace IRI (:ln)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/default_namespace_IRI.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/default_namespace_IRI.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in default_namespace_IRI.nq")
    earlOut("default_namespace_IRI", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-08.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // underscore is a legal pname character
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-08.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-08.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-08.nq")
    earlOut("trig-syntax-prefix-08", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-05.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // decimal literal (no leading digits)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-05.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-05.nq")
    earlOut("trig-syntax-number-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/labeled_blank_node_with_leading_underscore.trig" must "succeed" taggedAs (TestTrigEval) in {
    // labeled blank node with_leading_underscore (_:_)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/labeled_blank_node_with_leading_underscore.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/labeled_blank_node_with_leading_underscore.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in labeled_blank_node_with_leading_underscore.nq")
    earlOut("labeled_blank_node_with_leading_underscore", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-07.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // dash is a legal pname character
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-07.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-07.nq")
    earlOut("trig-syntax-prefix-07", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // decimal literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-04.nq")
    earlOut("trig-syntax-number-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-base-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // BASE without URI (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-base-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-base-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-base-03", !res)

    output.close()
  }

  "The input file ./TrigTests/repeated_semis_at_end.trig" must "succeed" taggedAs (TestTrigEval) in {
    // repeated semis at end <s> <p> <o> ;; <p2> <o2> .
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/repeated_semis_at_end.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/repeated_semis_at_end.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in repeated_semis_at_end.nq")
    earlOut("repeated_semis_at_end", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/HYPHEN_MINUS_in_localName.trig" must "succeed" taggedAs (TestTrigEval) in {
    // HYPHEN-MINUS in local name
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/HYPHEN_MINUS_in_localName.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/HYPHEN_MINUS_in_localName.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in HYPHEN_MINUS_in_localName.nq")
    earlOut("HYPHEN_MINUS_in_localName", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-05.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // bnode property list subject
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-05.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-05.nq")
    earlOut("trig-syntax-bnode-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-list-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Free-standing list of zero-elements outside {} : bad syntax
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-list-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-list-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-list-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-list-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-prefix-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @prefix without ':' (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-prefix-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-prefix-05", !res)

    output.close()
  }

  "The input file ./TrigTests/old_style_base.trig" must "succeed" taggedAs (TestTrigEval) in {
    // old-style base
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/old_style_base.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/old_style_base.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in old_style_base.nq")
    earlOut("old_style_base", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-18.trig" must "succeed" taggedAs (TestTrigEval) in {
    // empty literals, normal and long variant
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-18.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-18.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-18.nq")
    earlOut("trig-subm-18", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-uri-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad IRI : character escapes not allowed (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-uri-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-uri-04", !res)

    output.close()
  }

  "The input file ./TrigTests/bareword_integer.trig" must "succeed" taggedAs (TestTrigEval) in {
    // bareword integer
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/bareword_integer.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/bareword_integer.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in bareword_integer.nq")
    earlOut("bareword_integer", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL2_ascii_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL2_ascii_boundaries '\x00\x09\x0b\x0c\x0e\x21\x23...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL2_ascii_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL2_ascii_boundaries_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL2_ascii_boundaries.nq")
    earlOut("LITERAL2_ascii_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // labeled blank node with PN_CHARS_BASE character boundaries (_:AZazÀÖØöø...)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.nq")
    earlOut("labeled_blank_node_with_PN_CHARS_BASE_character_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/anonymous_blank_node_graph.trig" must "succeed" taggedAs (TestTrigEval) in {
    // anonymous blank node graph
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/anonymous_blank_node_graph.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/anonymous_blank_node_graph.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in anonymous_blank_node_graph.nq")
    earlOut("anonymous_blank_node_graph", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-eval-bad-02.trig" must "fail" taggedAs (TestTrigNegativeEval) in {
    // Bad IRI : hex 3C is < (negative evaluation test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-eval-bad-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-eval-bad-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-eval-bad-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-eval-bad-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-05.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // squote langString literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-05.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-05.nq")
    earlOut("trig-syntax-string-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-02.trig" must "succeed" taggedAs (TestTrigEval) in {
    // @prefix and qnames
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-02.nq")
    earlOut("trig-subm-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-struct-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // predicate list with object list and dangling ';'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-struct-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-struct-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-struct-03.nq")
    earlOut("trig-syntax-struct-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-turtle-bad-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Trailing dot required in Turtle block
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-turtle-bad-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-turtle-bad-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-turtle-bad-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-turtle-bad-01", !res)

    output.close()
  }

  "The input file ./TrigTests/literal_with_escaped_CHARACTER_TABULATION.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with escaped CHARACTER TABULATION
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_escaped_CHARACTER_TABULATION.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_escaped_CHARACTER_TABULATION.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_escaped_CHARACTER_TABULATION.nq")
    earlOut("literal_with_escaped_CHARACTER_TABULATION", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Turtle is not N3 (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-02", !res)

    output.close()
  }

  "The input file ./TrigTests/LITERAL1_all_punctuation.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL1_all_punctuation '!"#$%&()...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL1_all_punctuation.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL1_all_punctuation_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL1_all_punctuation.nq")
    earlOut("LITERAL1_all_punctuation", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/lantag_with_subtag.trig" must "succeed" taggedAs (TestTrigEval) in {
    // lantag with subtag "x"@en-us
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/lantag_with_subtag.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/lantag_with_subtag.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in lantag_with_subtag.nq")
    earlOut("lantag_with_subtag", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-uri-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // IRIs with Unicode escape
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-uri-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-uri-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-uri-02.nq")
    earlOut("trig-syntax-uri-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/prefixed_IRI_predicate.trig" must "succeed" taggedAs (TestTrigEval) in {
    // prefixed IRI predicate
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/prefixed_IRI_predicate.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/prefixed_IRI_predicate.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in prefixed_IRI_predicate.nq")
    earlOut("prefixed_IRI_predicate", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-ln-dots.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Dots in pname local names
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-ln-dots.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-ln-dots.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-ln-dots.nq")
    earlOut("trig-syntax-ln-dots", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-lists-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // lists of lists
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-lists-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-lists-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-lists-04.nq")
    earlOut("trig-syntax-lists-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-ns-dot-end.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Prefix must not end in dot
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-ns-dot-end.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ns-dot-end.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ns-dot-end.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-ns-dot-end", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-lists-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // isomorphic list as subject and object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-lists-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-lists-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-lists-03.nq")
    earlOut("trig-syntax-lists-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/number_sign_following_PNAME_NS.trig" must "succeed" taggedAs (TestTrigEval) in {
    // number sign following PNAME_NS
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/number_sign_following_PNAME_NS.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/number_sign_following_PNAME_NS.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in number_sign_following_PNAME_NS.nq")
    earlOut("number_sign_following_PNAME_NS", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-21.trig" must "succeed" taggedAs (TestTrigEval) in {
    // long literal ending in double quote
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-21.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-21.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-21.nq")
    earlOut("trig-subm-21", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // string literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-01.nq")
    earlOut("trig-syntax-string-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/blankNodePropertyList_as_subject.trig" must "succeed" taggedAs (TestTrigEval) in {
    // blankNodePropertyList as subject [ … ] <p> <o> .
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/blankNodePropertyList_as_subject.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/blankNodePropertyList_as_subject.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in blankNodePropertyList_as_subject.nq")
    earlOut("blankNodePropertyList_as_subject", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // bnode property list object (2)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-04.nq")
    earlOut("trig-syntax-bnode-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/prefixed_IRI_object.trig" must "succeed" taggedAs (TestTrigEval) in {
    // prefixed IRI object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/prefixed_IRI_object.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/prefixed_IRI_object.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in prefixed_IRI_object.nq")
    earlOut("prefixed_IRI_object", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-08.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @graph is not a keyword
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-08.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-08.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-08.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-08", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-06.trig" must "succeed" taggedAs (TestTrigEval) in {
    // non-empty [] as subject and object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(8), "Number of triples generated should have been 8")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-06_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-06.nq")
    earlOut("trig-subm-06", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-struct-05.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // predicate list with multiple ;;
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-struct-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-struct-05.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-struct-05.nq")
    earlOut("trig-syntax-struct-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-05.trig" must "succeed" taggedAs (TestTrigEval) in {
    // empty [] as subject and object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-05_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-05.nq")
    earlOut("trig-subm-05", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-eval-struct-02.trig" must "succeed" taggedAs (TestTrigEval) in {
    // triple with IRIs and embedded whitespace
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-eval-struct-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-eval-struct-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-eval-struct-02.nq")
    earlOut("trig-eval-struct-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-06.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // N3 paths not in Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-06.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-06.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-06", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-ns-dots.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Dots in namespace names
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-ns-dots.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-ns-dots.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-ns-dots.nq")
    earlOut("trig-syntax-ns-dots", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/anonymous_blank_node_object.trig" must "succeed" taggedAs (TestTrigEval) in {
    // anonymous blank node object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/anonymous_blank_node_object.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/anonymous_blank_node_object_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in anonymous_blank_node_object.nq")
    earlOut("anonymous_blank_node_object", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-string-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // mismatching string literal open/close (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-string-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-string-01", !res)

    output.close()
  }

  "The input file ./TrigTests/IRI_subject.trig" must "succeed" taggedAs (TestTrigEval) in {
    // IRI subject
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/IRI_subject.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/IRI_subject.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in IRI_subject.nq")
    earlOut("IRI_subject", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-13.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @keywords is not Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-13.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-13.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-13.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-13", !res)

    output.close()
  }

  "The input file ./TrigTests/literal_with_FORM_FEED.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with FORM FEED
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_FORM_FEED.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_FORM_FEED_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_FORM_FEED.nq")
    earlOut("literal_with_FORM_FEED", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-kw-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // 'a' cannot be used as object (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-kw-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-kw-03", !res)

    output.close()
  }

  "The input file ./TrigTests/underscore_in_localName.trig" must "succeed" taggedAs (TestTrigEval) in {
    // underscore in local name
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/underscore_in_localName.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/underscore_in_localName.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in underscore_in_localName.nq")
    earlOut("underscore_in_localName", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-prefix-06.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @prefix inside graph (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-prefix-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-06.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-06.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-prefix-06", !res)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG2.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG2 """x"""
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG2.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG2.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG2.nq")
    earlOut("LITERAL_LONG2", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-turtle-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // TriG can parse Turtle (blankNodePropertyList subject)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-turtle-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nq = io.Source.fromFile("./TrigTests/trig-turtle-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-turtle-03.nq")
    earlOut("trig-turtle-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // langString literal with region
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-03.nq")
    earlOut("trig-syntax-string-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-08.trig" must "succeed" taggedAs (TestTrigEval) in {
    // simple collection
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-08.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(10), "Number of triples generated should have been 10")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-08_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-08.nq")
    earlOut("trig-subm-08", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // N3 paths not in Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-03", !res)

    output.close()
  }

  "The input file ./TrigTests/SPARQL_style_base.trig" must "succeed" taggedAs (TestTrigEval) in {
    // SPARQL-style base
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/SPARQL_style_base.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/SPARQL_style_base.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in SPARQL_style_base.nq")
    earlOut("SPARQL_style_base", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Named graph may be empty
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-03.trig")
    earlOut("trig-kw-graph-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-09.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // => is not Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-09.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-09.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-09", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-kw-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // 'a' as keyword
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-kw-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-kw-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-kw-03.nq")
    earlOut("trig-syntax-kw-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-08.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @keywords is not Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-08.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-08.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-08.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-08", !res)

    output.close()
  }

  "The input file ./TrigTests/literal_with_BACKSPACE.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with BACKSPACE
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_BACKSPACE.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_BACKSPACE_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_BACKSPACE.nq")
    earlOut("literal_with_BACKSPACE", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-uri-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad IRI : bad long escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-uri-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-uri-03", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-16.trig" must "succeed" taggedAs (TestTrigEval) in {
    // long literals with escapes
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-16.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-16_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-16.nq")
    earlOut("trig-subm-16", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Turtle does not allow literals-as-predicates (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-05", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-06.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // colon is a legal pname character
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-06.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-06.nq")
    earlOut("trig-syntax-prefix-06", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // bnode object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-02.nq")
    earlOut("trig-syntax-bnode-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-12.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // subject, predicate, no object (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-12.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-12.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-12.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-12", !res)

    output.close()
  }

  "The input file ./TrigTests/LITERAL1_ascii_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL1_ascii_boundaries '\x00\x09\x0b\x0c\x0e\x26\x28...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL1_ascii_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL1_ascii_boundaries_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL1_ascii_boundaries.nq")
    earlOut("LITERAL1_ascii_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/SPARQL_style_prefix.trig" must "succeed" taggedAs (TestTrigEval) in {
    // SPARQL-style prefix
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/SPARQL_style_prefix.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/SPARQL_style_prefix.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in SPARQL_style_prefix.nq")
    earlOut("SPARQL_style_prefix", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-esc-02.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad string escape (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-esc-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-esc-02.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-esc-02.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-esc-02", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-turtle-06.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // TriG can parse Turtle (collection subject and object)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-turtle-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(13), "Number of triples generated should have been 13")

    val nq = io.Source.fromFile("./TrigTests/trig-turtle-06.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-turtle-06.nq")
    earlOut("trig-turtle-06", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/two_LITERAL_LONG2s.trig" must "succeed" taggedAs (TestTrigEval) in {
    // two LITERAL_LONG2s testing quote delimiter overrun
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/two_LITERAL_LONG2s.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/two_LITERAL_LONG2s.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in two_LITERAL_LONG2s.nq")
    earlOut("two_LITERAL_LONG2s", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-string-06.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Long literal with extra quote (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-string-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-06.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-06.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-string-06", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-bnodeplist-graph-bad-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // A graph may not be named with a blankNodePropertyList
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-bnodeplist-graph-bad-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-bnodeplist-graph-bad-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-bnodeplist-graph-bad-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-bnodeplist-graph-bad-01", !res)

    output.close()
  }

  "The input file ./TrigTests/IRI_with_all_punctuation.trig" must "succeed" taggedAs (TestTrigEval) in {
    // IRI with all punctuation
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/IRI_with_all_punctuation.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/IRI_with_all_punctuation.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in IRI_with_all_punctuation.nq")
    earlOut("IRI_with_all_punctuation", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-struct-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // predicate list with object list
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-struct-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-struct-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-struct-02.nq")
    earlOut("trig-syntax-struct-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/labeled_blank_node_with_non_leading_extras.trig" must "succeed" taggedAs (TestTrigEval) in {
    // labeled blank node with_non_leading_extras (_:a·̀ͯ‿.⁀)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/labeled_blank_node_with_non_leading_extras.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/labeled_blank_node_with_non_leading_extras.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in labeled_blank_node_with_non_leading_extras.nq")
    earlOut("labeled_blank_node_with_non_leading_extras", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG1_ascii_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG1_ascii_boundaries '\x00\x26\x28...'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG1_ascii_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG1_ascii_boundaries_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG1_ascii_boundaries.nq")
    earlOut("LITERAL_LONG1_ascii_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-ln-escape-start.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad hex escape at start of local name
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-ln-escape-start.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ln-escape-start.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-ln-escape-start.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-ln-escape-start", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-23.trig" must "succeed" taggedAs (TestTrigEval) in {
    // comments
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-23.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(14), "Number of triples generated should have been 14")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-23.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-23.nq")
    earlOut("trig-subm-23", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // GRAPH needs {}
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-04", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-str-esc-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // string literal with escaped newline
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-str-esc-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-str-esc-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-str-esc-01.nq")
    earlOut("trig-syntax-str-esc-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-07.trig" must "succeed" taggedAs (TestTrigEval) in {
    // 'a' as predicate
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-07.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-07.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-07.nq")
    earlOut("trig-subm-07", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bnode-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // bnode property list object
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bnode-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-bnode-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-bnode-03.nq")
    earlOut("trig-syntax-bnode-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/literal_with_numeric_escape8.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with numeric escape8 \U
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_numeric_escape8.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_numeric_escape8.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_numeric_escape8.nq")
    earlOut("literal_with_numeric_escape8", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // GRAPH and a name, not several
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-05", !res)

    output.close()
  }

  "The input file ./TrigTests/localName_with_leading_digit.trig" must "succeed" taggedAs (TestTrigEval) in {
    // localName with leading digit (p:_)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/localName_with_leading_digit.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/localName_with_leading_digit.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in localName_with_leading_digit.nq")
    earlOut("localName_with_leading_digit", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-10.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // negative double literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-10.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-10.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-10.nq")
    earlOut("trig-syntax-number-10", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-pname-03.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad unicode escape in pname (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-pname-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-pname-03.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-pname-03.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-pname-03", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-uri-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Bad IRI : space (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-uri-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-uri-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-uri-01", !res)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG2_with_1_squote.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL_LONG2 with 1 squote """a"b"""
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG2_with_1_squote.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG2_with_1_squote.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG2_with_1_squote.nq")
    earlOut("LITERAL_LONG2_with_1_squote", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-blank-label-dot-end.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Blank node label must not end in dot
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-blank-label-dot-end.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-blank-label-dot-end.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-blank-label-dot-end.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-blank-label-dot-end", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-datatypes-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // xsd:byte literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-datatypes-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-datatypes-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-datatypes-01.nq")
    earlOut("trig-syntax-datatypes-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/langtagged_LONG_with_subtag.trig" must "succeed" taggedAs (TestTrigEval) in {
    // langtagged LONG with subtag """Cheers"""@en-UK
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/langtagged_LONG_with_subtag.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/langtagged_LONG_with_subtag.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in langtagged_LONG_with_subtag.nq")
    earlOut("langtagged_LONG_with_subtag", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/blankNodePropertyList_containing_collection.trig" must "succeed" taggedAs (TestTrigEval) in {
    // blankNodePropertyList containing collection [ <p1> ( … ) ]
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/blankNodePropertyList_containing_collection.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/blankNodePropertyList_containing_collection_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in blankNodePropertyList_containing_collection.nq")
    earlOut("blankNodePropertyList_containing_collection", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-04.trig" must "succeed" taggedAs (TestTrigEval) in {
    // ; operator
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-04.nq")
    earlOut("trig-subm-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/blankNodePropertyList_with_multiple_triples.trig" must "succeed" taggedAs (TestTrigEval) in {
    // blankNodePropertyList with multiple triples [ <s> <p> ; <s2> <p2> ]
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/blankNodePropertyList_with_multiple_triples.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/blankNodePropertyList_with_multiple_triples.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in blankNodePropertyList_with_multiple_triples.nq")
    earlOut("blankNodePropertyList_with_multiple_triples", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-n3-extras-10.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // <= is not Turtle (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-n3-extras-10.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-10.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-n3-extras-10.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-n3-extras-10", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-06.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // squote langString literal with region
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-06.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-06.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-06.nq")
    earlOut("trig-syntax-string-06", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-string-05.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Long literal with missing end (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-string-05.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-05.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-05.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-string-05", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-string-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // mismatching long string literal open/close (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-string-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-string-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-string-04", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-number-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // positive integer literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-number-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-number-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-number-03.nq")
    earlOut("trig-syntax-number-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-blank-label.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Characters allowed in blank node labels
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-blank-label.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-blank-label.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-blank-label.nq")
    earlOut("trig-syntax-blank-label", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/literal_with_LINE_FEED.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with LINE FEED
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_LINE_FEED.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_LINE_FEED_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_LINE_FEED.nq")
    earlOut("literal_with_LINE_FEED", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-10.trig" must "succeed" taggedAs (TestTrigEval) in {
    // integer datatyped literal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-10.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(8), "Number of triples generated should have been 8")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-10_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-10.nq")
    earlOut("trig-subm-10", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/empty_collection.trig" must "succeed" taggedAs (TestTrigEval) in {
    // empty collection ()
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/empty_collection.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/empty_collection.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in empty_collection.nq")
    earlOut("empty_collection", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-struct-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // object list
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-struct-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-struct-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-struct-01.nq")
    earlOut("trig-syntax-struct-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-string-11.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // squote long langString literal with embedded newline
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-string-11.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-string-11.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-string-11.nq")
    earlOut("trig-syntax-string-11", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-02.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Trailing . not necessary inside {}
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-02.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-02.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-02.nq")
    earlOut("trig-kw-graph-02", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-file-03.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // One comment, one empty line
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-file-03.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-file-03.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-file-03.nq")
    earlOut("trig-syntax-file-03", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-prefix-04.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // @prefix without prefix name (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-prefix-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-04.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-04.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-prefix-04", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-lists-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // empty list
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-lists-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-lists-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-lists-01.nq")
    earlOut("trig-syntax-lists-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/alternating_bnode_graphs.trig" must "succeed" taggedAs (TestTrigEval) in {
    // alternating graphs with BNode names
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/alternating_bnode_graphs.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nq = io.Source.fromFile("./TrigTests/alternating_bnode_graphs_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in alternating_bnode_graphs.nq")
    earlOut("alternating_bnode_graphs", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-09.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // percents in pnames
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-09.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-09.nq")
    earlOut("trig-syntax-prefix-09", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-prefix-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // No prefix (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-prefix-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-prefix-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-prefix-01", !res)

    output.close()
  }

  "The input file ./TrigTests/literal_with_escaped_BACKSPACE.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with escaped BACKSPACE
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_escaped_BACKSPACE.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_escaped_BACKSPACE.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_escaped_BACKSPACE.nq")
    earlOut("literal_with_escaped_BACKSPACE", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/bareword_decimal.trig" must "succeed" taggedAs (TestTrigEval) in {
    // bareword decimal
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/bareword_decimal.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/bareword_decimal.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in bareword_decimal.nq")
    earlOut("bareword_decimal", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/prefixed_name_datatype.trig" must "succeed" taggedAs (TestTrigEval) in {
    // prefixed name datatype ""^^p:t
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/prefixed_name_datatype.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/prefixed_name_datatype.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in prefixed_name_datatype.nq")
    earlOut("prefixed_name_datatype", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/anonymous_blank_node_subject.trig" must "succeed" taggedAs (TestTrigEval) in {
    // anonymous blank node subject
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/anonymous_blank_node_subject.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/anonymous_blank_node_subject_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in anonymous_blank_node_subject.nq")
    earlOut("anonymous_blank_node_subject", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/literal_with_escaped_FORM_FEED.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with escaped FORM FEED
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_escaped_FORM_FEED.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_escaped_FORM_FEED.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_escaped_FORM_FEED.nq")
    earlOut("literal_with_escaped_FORM_FEED", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/reserved_escaped_localName.trig" must "succeed" taggedAs (TestTrigEval) in {
    // reserved-escaped local name
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/reserved_escaped_localName.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/reserved_escaped_localName.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in reserved_escaped_localName.nq")
    earlOut("reserved_escaped_localName", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.trig" must "succeed" taggedAs (TestTrigEval) in {
    // localName with assigned, NFC-normalized PN CHARS BASE character boundaries (p:AZazÀÖØöø...)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries_isomorphic.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.nq")
    earlOut("localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL1.trig" must "succeed" taggedAs (TestTrigEval) in {
    // LITERAL1 'x'
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL1.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL1.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL1.nq")
    earlOut("LITERAL1", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-09.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // Directives not allowed inside GRAPH
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-09.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-09.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-09.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-09", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-24.trig" must "succeed" taggedAs (TestTrigEval) in {
    // no final mewline
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-24.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-24.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-24.nq")
    earlOut("trig-subm-24", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/numeric_with_leading_0.trig" must "succeed" taggedAs (TestTrigEval) in {
    // numeric with leading 0
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/numeric_with_leading_0.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/numeric_with_leading_0.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in numeric_with_leading_0.nq")
    earlOut("numeric_with_leading_0", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-kw-graph-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // Named graphs can be proceeded by GRAPH
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-kw-graph-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nq = io.Source.fromFile("./TrigTests/trig-kw-graph-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-kw-graph-01.nq")
    earlOut("trig-kw-graph-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-13.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // subject, predicate, no object (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-13.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-13.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-13.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-13", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-20.trig" must "succeed" taggedAs (TestTrigEval) in {
    // negative integer, decimal and doubles
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-20.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(6), "Number of triples generated should have been 6")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-20.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-20.nq")
    earlOut("trig-subm-20", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-prefix-01.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // @prefix
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-prefix-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-prefix-01.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-prefix-01.nq")
    earlOut("trig-syntax-prefix-01", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-graph-bad-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // GRAPH but no name - GRAPH is not used with the default graph
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-graph-bad-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-graph-bad-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-graph-bad-01", !res)

    output.close()
  }

  "The input file ./TrigTests/repeated_semis_not_at_end.trig" must "succeed" taggedAs (TestTrigEval) in {
    // repeated semis not at end <s> <p> <o> ;;.
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/repeated_semis_not_at_end.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/repeated_semis_not_at_end.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in repeated_semis_not_at_end.nq")
    earlOut("repeated_semis_not_at_end", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-kw-01.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // 'A' is not a keyword (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-kw-01.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-01.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-kw-01.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-kw-01", !res)

    output.close()
  }

  "The input file ./TrigTests/trig-subm-13.trig" must "succeed" taggedAs (TestTrigEval) in {
    // tests for rdf:_<numbers> and other qnames starting with _
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-subm-13.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(8), "Number of triples generated should have been 8")

    val nq = io.Source.fromFile("./TrigTests/trig-subm-13.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-subm-13.nq")
    earlOut("trig-subm-13", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-bad-struct-17.trig" must "fail" taggedAs (TestTrigNegativeSyntax) in {
    // labeled bnode as predicate (negative test)
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-bad-struct-17.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-17.trig': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TrigTests/trig-syntax-bad-struct-17.trig': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("trig-syntax-bad-struct-17", !res)

    output.close()
  }

  "The input file ./TrigTests/literal_with_numeric_escape4.trig" must "succeed" taggedAs (TestTrigEval) in {
    // literal with numeric escape4
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/literal_with_numeric_escape4.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/literal_with_numeric_escape4.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in literal_with_numeric_escape4.nq")
    earlOut("literal_with_numeric_escape4", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/trig-syntax-struct-04.trig" must "succeed" taggedAs (TestTrigPositiveSyntax) in {
    // predicate list with multiple ;;
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/trig-syntax-struct-04.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/trig-syntax-struct-04.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in trig-syntax-struct-04.nq")
    earlOut("trig-syntax-struct-04", output.toString == nq.toString)

    output.close()
  }

  "The input file ./TrigTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.trig" must "succeed" taggedAs (TestTrigEval) in {
    // REVERSE SOLIDUS at end of LITERAL_LONG2
    lazy val input: ParserInput = io.Source.fromFile("./TrigTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.trig").mkString

    val output = new StringWriter()

    val parser = TrigParser(input, output, false, "http://www.w3.org/2013/TrigTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nq = io.Source.fromFile("./TrigTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.nq").mkString

    assert(output.toString == nq.toString, "Quads generated should be exactly as in LITERAL_LONG2_with_REVERSE_SOLIDUS.nq")
    earlOut("LITERAL_LONG2_with_REVERSE_SOLIDUS", output.toString == nq.toString)

    output.close()
  }
}
