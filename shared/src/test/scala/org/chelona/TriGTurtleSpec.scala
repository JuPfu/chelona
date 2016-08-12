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

import java.io.{ BufferedWriter, FileOutputStream, OutputStreamWriter, StringWriter }
import java.nio.charset.StandardCharsets
import java.util.Calendar

import org.parboiled2.{ ParseError, ParserInput }
import org.scalatest.FlatSpec

import scala.util.Failure

class TriGTurtleSpec extends FlatSpec with RDFTriGOutput {

  val earl = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./earl.ttl"), StandardCharsets.UTF_8))

  def earlOut(testcase: String, passed: Boolean) = {
    System.err.flush()
    val assertedBy = "<https://github.com/JuPfu#me>"
    val subject = "<https://github.com/JuPfu/chelona>"
    val test = testcase // "IRI_subject"
    val outcome = if (passed) "passed" else "failed"
    val datum = Calendar.getInstance.getTime
    val mode = "automatic"
    val earl_assertion = s"""[ a earl:Assertion;\n  earl:assertedBy ${assertedBy};\n  earl:subject ${subject};\n  earl:test <http://www.w3.org/2013/TurtleTests/manifest.ttl#${test}>;\n  earl:result [\n    a earl:TestResult;\n    earl:outcome earl:${outcome};\n    dc:date "${datum}"^^xsd:dateTime];\n  earl:mode earl:${mode} ] .\n"""
    earl.write(earl_assertion); earl.flush()
  }

  "The input file ./TurtleTests/IRI_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_subject.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_spo.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in IRI_spo.nt")
    earlOut("IRI_subject", true)

    output.close()
  }

  "The input file ./TurtleTests/IRI_with_four_digit_numeric_escape.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_with_four_digit_numeric_escape.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_with_four_digit_numeric_escape.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in IRI_with_four_digit_numeric_escape.nt")
    earlOut("IRI_with_four_digit_numeric_escape", true)

    output.close()
  }

  "The input file ./TurtleTests/IRI_with_eight_digit_numeric_escape.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_with_eight_digit_numeric_escape.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_with_four_digit_numeric_escape.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in IRI_with_eight_digit_numeric_escape.nt")
    earlOut("IRI_with_eight_digit_numeric_escape", true)

    output.close()
  }

  "The input file ./TurtleTests/IRI_with_all_punctuation.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in IRI_with_all_punctuation.nt")
    earlOut("IRI_with_all_punctuation", true)

    output.close()
  }

  "The input file ./TurtleTests/bareword_a_predicate.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_a_predicate.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_a_predicate.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in bareword_a_predicate.nt")
    earlOut("bareword_a_predicate", true)

    output.close()
  }

  "The input file ./TurtleTests/old_style_prefix.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/old_style_prefix.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/old_style_prefix.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in old_style_prefix.nt")
    earlOut("old_style_prefix", true)

    output.close()
  }

  "The input file ./TurtleTests/SPARQL_style_prefix.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/SPARQL_style_prefix.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/SPARQL_style_prefix.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in SPARQL_style_prefix.nt")
    earlOut("SPARQL_style_prefix", true)

    output.close()
  }

  "The input file ./TurtleTests/prefixed_IRI_predicate.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefixed_IRI_predicate.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefixed_IRI_predicate.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in prefixed_IRI_object.nt")
    earlOut("prefixed_IRI_predicate", true)

    output.close()
  }

  "The input file ./TurtleTests/prefixed_IRI_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefixed_IRI_object.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefixed_IRI_object.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in prefixed_IRI_object.nt")
    earlOut("prefixed_IRI_object", true)

    output.close()
  }

  "The input file ./TurtleTests/prefix_only_IRI.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_only_IRI.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_only_IRI.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in prefix_only_IRI.nt")
    earlOut("prefix_only_IRI", true)

    output.close()
  }

  "The input file ./TurtleTests/prefix_with_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_with_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_with_PN_CHARS_BASE_character_boundaries.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in prefix_with_PN_CHARS_BASE_character_boundaries.nt")
    earlOut("prefix_with_PN_CHARS_BASE_character_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/prefix_with_non_leading_extras.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_with_non_leading_extras.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_with_non_leading_extras.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in prefix_with_non_leading_extras.nt")
    earlOut("prefix_with_non_leading_extras", true)

    output.close()
  }

  "The input file ./TurtleTests/default_namespace_IRI.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/default_namespace_IRI.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/default_namespace_IRI.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in default_namespace_IRI.nt")
    earlOut("default_namespace_IRI", true)

    output.close()
  }

  "The input file ./TurtleTests/prefix_reassigned_and_used.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_reassigned_and_used.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_reassigned_and_used.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in prefix_reassigned_and_used.nt")
    earlOut("prefix_reassigned_and_used", true)

    output.close()
  }

  "The input file ./TurtleTests/reserved_escaped_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/reserved_escaped_localName.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/reserved_escaped_localName.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in reserved_escaped_localName.nt")
    earlOut("reserved_escaped_localName", true)

    output.close()
  }

  "The input file ./TurtleTests/percent_escaped_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/percent_escaped_localName.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/percent_escaped_localName.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in percent_escaped_localName.nt")
    earlOut("percent_escaped_localName", true)

    output.close()
  }

  "The input file ./TurtleTests/HYPHEN_MINUS_in_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.nt").mkString

    assert(output.toString == nt.toString)
    earlOut("HYPHEN_MINUS_in_localName", true)

    output.close()
  }

  "The input file ./TurtleTests/underscore_in_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/underscore_in_localName.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/underscore_in_localName.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in underscore_in_localName.nt")
    earlOut("underscore_in_localName", true)

    output.close()
  }

  "The input file ./TurtleTests/localname_with_COLON.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localname_with_COLON.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localname_with_COLON.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in localname_with_COLON.nt")
    earlOut("localname_with_COLON", true)

    output.close()
  }

  "The input file ./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries_isomorphic.nt")
    earlOut("localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.nt")
    earlOut("localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_nfc_PN_CHARS_BASE_character_boundaries.nt")
    earlOut("localName_with_nfc_PN_CHARS_BASE_character_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/localName_with_leading_underscore.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_leading_underscore.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_leading_underscore.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_leading_underscore.nt")
    earlOut("localName_with_leading_underscore", true)

    output.close()
  }

  "The input file ./TurtleTests/localName_with_leading_digit.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_leading_digit.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_leading_digit.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_leading_digit.nt")
    earlOut("localName_with_leading_digit", true)

    output.close()
  }

  "The input file ./TurtleTests/localName_with_non_leading_extras.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_non_leading_extras.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_non_leading_extras_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_non_leading_extras_isomorphic.nt")
    earlOut("localName_with_non_leading_extras", true)

    output.close()
  }

  "The input file ./TurtleTests/old_style_base.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/old_style_base.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/old_style_base.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in old_style_base.nt")
    earlOut("old_style_base", true)

    output.close()
  }

  "The input file ./TurtleTests/SPARQL_style_base.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/SPARQL_style_base.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/SPARQL_style_base.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in SPARQL_style_base.nt")
    earlOut("SPARQL_style_base", true)

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_subject.nt")
    earlOut("labeled_blank_node_subject", true)

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_object.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_object.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_object.nt")
    earlOut("labeled_blank_node_object", true)

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.nt")
    earlOut("labeled_blank_node_with_PN_CHARS_BASE_character_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_with_leading_underscore.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_underscore.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_underscore.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_with_leading_underscore.nt")
    earlOut("labeled_blank_node_with_leading_underscore", true)

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_with_leading_digit.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_digit.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_digit.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_with_leading_digit.nt")
    earlOut("labeled_blank_node_with_leading_digit", true)

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_with_non_leading_extras.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_non_leading_extras.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_non_leading_extras.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_with_non_leading_extras.nt")
    earlOut("labeled_blank_node_with_non_leading_extras", true)

    output.close()
  }

  "The input file ./TurtleTests/anonymous_blank_node_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/anonymous_blank_node_subject.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/anonymous_blank_node_subject.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in anonymous_blank_node_subject.nt")
    earlOut("anonymous_blank_node_subject", true)

    output.close()
  }

  "The input file ./TurtleTests/anonymous_blank_node_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/anonymous_blank_node_object.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/anonymous_blank_node_object.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in anonymous_blank_node_object.nt")
    earlOut("anonymous_blank_node_object", true)

    output.close()
  }

  "The input file ./TurtleTests/sole_blankNodePropertyList.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/sole_blankNodePropertyList.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/sole_blankNodePropertyList.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in sole_blankNodePropertyList.nt")
    earlOut("sole_blankNodePropertyList", true)

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_as_subject.nt")
    earlOut("blankNodePropertyList_as_subject", true)

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_as_object.nt")
    earlOut("blankNodePropertyList_as_object", true)

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_with_multiple_triples.nt")
    earlOut("blankNodePropertyList_with_multiple_triples", true)

    output.close()
  }

  "The input file ./TurtleTests/nested_blankNodePropertyLists.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/nested_blankNodePropertyLists.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/nested_blankNodePropertyLists.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in nested_blankNodePropertyLists.nt")
    earlOut("nested_blankNodePropertyLists", true)

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_containing_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_containing_collection_isomorphic.nt")
    earlOut("blankNodePropertyList_containing_collection", true)

    output.close()
  }

  "The input file ./TurtleTests/collection_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/collection_subject.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/collection_subject_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in collection_subject_isomorphic.nt")
    earlOut("collection_subject", true)

    output.close()
  }

  "The input file ./TurtleTests/collection_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/collection_object.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/collection_object_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in collection_object_isomorphic.nt")
    earlOut("collection_object", true)

    output.close()
  }

  "The input file ./TurtleTests/empty_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/empty_collection.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/empty_collection.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in empty_collection.nt")
    earlOut("empty_collection", true)

    output.close()
  }

  "The input file ./TurtleTests/nested_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/nested_collection.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/nested_collection_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in nested_collection_isomorphic.nt")
    earlOut("nested_collection", true)

    output.close()
  }

  "The input file ./TurtleTests/first.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/first.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/first_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in first_isomorphic.nt")
    earlOut("first", true)

    output.close()
  }

  "The input file ./TurtleTests/last.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/last.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/last_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in last_isomorphic.nt")
    earlOut("last", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1.nt")
    earlOut("LITERAL1", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_ascii_boundaries_isomorphic.nt")
    earlOut("LITERAL1_ascii_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_with_UTF8_boundaries.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_with_UTF8_boundaries.nt")
    earlOut("LITERAL1_with_UTF8_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_all_controls.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_all_controls.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_all_controls_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_all_controls_isomorphic.nt")
    earlOut("LITERAL1_all_controls", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_all_punctuation.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_all_punctuation.nt")
    earlOut("LITERAL1_all_punctuation", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1.nt")
    earlOut("LITERAL_LONG1", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_ascii_boundaries_isomorphic.nt")
    earlOut("LITERAL_LONG1_ascii_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_UTF8_boundaries_isomorphic.nt")
    earlOut("LITERAL_LONG1_with_UTF8_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_1_squote.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_1_squote.nt")
    earlOut("LITERAL_LONG1_with_1_squote", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_2_squotes.nt")
    earlOut("LITERAL_LONG1_with_2_squotes", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL2.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL2.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL2.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL2.nt")
    earlOut("LITERAL2", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL2_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL2_ascii_boundaries_isomorphic.nt")
    earlOut("LITERAL2_ascii_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL2_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL2_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL2_with_UTF8_boundaries.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL2_with_UTF8_boundaries.nt")
    earlOut("LITERAL2_with_UTF8_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2.nt")
    earlOut("LITERAL_LONG2", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_ascii_boundaries_isomorphic.nt")
    earlOut("LITERAL_LONG2_ascii_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_UTF8_boundaries.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_UTF8_boundaries.nt")
    earlOut("LITERAL_LONG2_with_UTF8_boundaries", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_1_squote.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_1_squote.nt")
    earlOut("LITERAL_LONG2_with_1_squote", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_2_squotes.nt")
    earlOut("LITERAL_LONG2_with_2_squotes", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_CHARACTER_TABULATION.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_CHARACTER_TABULATION.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_CHARACTER_TABULATION_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_CHARACTER_TABULATION_isomorphic.nt")
    earlOut("literal_with_CHARACTER_TABULATION", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_BACKSPACE.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_BACKSPACE.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_BACKSPACE_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_BACKSPACE_isomorphic.nt")
    earlOut("literal_with_BACKSPACE", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_LINE_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_LINE_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_LINE_FEED_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_LINE_FEED_isomorphic.nt")
    earlOut("literal_with_LINE_FEED", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_CARRIAGE_RETURN.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_CARRIAGE_RETURN.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_CARRIAGE_RETURN.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_CARRIAGE_RETURN.nt")
    earlOut("literal_with_CARRIAGE_RETURN", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_FORM_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_FORM_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_FORM_FEED_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_FORM_FEED_isomorphic.nt")
    earlOut("literal_with_FORM_FEED", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_REVERSE_SOLIDUS.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_REVERSE_SOLIDUS.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_REVERSE_SOLIDUS.nt")
    earlOut("literal_with_REVERSE_SOLIDUS", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_CHARACTER_TABULATION.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_CHARACTER_TABULATION.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_CHARACTER_TABULATION.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_CHARACTER_TABULATION.nt")
    earlOut("literal_with_escaped_CHARACTER_TABULATION", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_BACKSPACE.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_BACKSPACE.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_BACKSPACE.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_BACKSPACE.nt")
    earlOut("literal_with_escaped_BACKSPACE", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_LINE_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_LINE_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_LINE_FEED.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_LINE_FEED.nt")
    earlOut("literal_with_escaped_LINE_FEED", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_CARRIAGE_RETURN.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_CARRIAGE_RETURN.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_CARRIAGE_RETURN.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_CARRIAGE_RETURN.nt")
    earlOut("literal_with_escaped_CARRIAGE_RETURN", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_FORM_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_FORM_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_FORM_FEED.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_FORM_FEED.nt")
    earlOut("literal_with_escaped_FORM_FEED", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_numeric_escape4.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape4.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape4.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_numeric_escape4.nt")
    earlOut("literal_with_numeric_escape4", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_with_numeric_escape8.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape8.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape8.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_numeric_escape8.nt")
    earlOut("literal_with_numeric_escape8", true)

    output.close()
  }

  "The input file ./TurtleTests/IRIREF_datatype.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRIREF_datatype.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRIREF_datatype.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in IRIREF_datatype.nt")
    earlOut("IRIREF_datatype", true)

    output.close()
  }

  "The input file ./TurtleTests/prefixed_name_datatype.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefixed_name_datatype.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefixed_name_datatype.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in prefixed_name_datatype.nt")
    earlOut("prefixed_name_datatype", true)

    output.close()
  }

  "The input file ./TurtleTests/bareword_integer.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_integer.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_integer.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in bareword_integer.nt")
    earlOut("bareword_integer", true)

    output.close()
  }

  "The input file ./TurtleTests/bareword_decimal.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_decimal.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_decimal.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in bareword_decimal.nt")
    earlOut("bareword_decimal", true)

    output.close()
  }

  "The input file ./TurtleTests/bareword_double.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_double.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_double.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in bareword_double.nt")
    earlOut("bareword_double", true)

    output.close()
  }

  "The input file ./TurtleTests/double_lower_case_e.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/double_lower_case_e.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/double_lower_case_e.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in double_lower_case_e.nt")
    earlOut("double_lower_case_e", true)

    output.close()
  }

  "The input file ./TurtleTests/negative_numeric.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/negative_numeric.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/negative_numeric.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in negative_numeric.nt")
    earlOut("negative_numeric", true)

    output.close()
  }

  "The input file ./TurtleTests/positive_numeric.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/positive_numeric.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/positive_numeric.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in positive_numeric.nt")
    earlOut("positive_numeric", true)

    output.close()
  }

  "The input file ./TurtleTests/numeric_with_leading_0.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/numeric_with_leading_0.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/numeric_with_leading_0.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in numeric_with_leading_0.nt")
    earlOut("numeric_with_leading_0", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_true.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_true.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_true.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_true.nt")
    earlOut("literal_true", true)

    output.close()
  }

  "The input file ./TurtleTests/literal_false.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_false.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_false.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_false.nt")
    earlOut("literal_false", true)

    output.close()
  }

  "The input file ./TurtleTests/langtagged_non_LONG.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_non_LONG.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_non_LONG.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in langtagged_non_LONG.nt")
    earlOut("langtagged_non_LONG", true)

    output.close()
  }

  "The input file ./TurtleTests/langtagged_LONG.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_LONG.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_LONG.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in langtagged_LONG.nt")
    earlOut("langtagged_LONG", true)

    output.close()
  }

  "The input file ./TurtleTests/lantag_with_subtag.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/lantag_with_subtag.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/lantag_with_subtag.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in lantag_with_subtag.nt")
    earlOut("lantag_with_subtag", true)

    output.close()
  }

  "The input file ./TurtleTests/objectList_with_two_objects.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/objectList_with_two_objects.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/objectList_with_two_objects.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in objectList_with_two_objects.nt")
    earlOut("objectList_with_two_objects", true)

    output.close()
  }

  "The input file ./TurtleTests/predicateObjectList_with_two_objectLists.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/predicateObjectList_with_two_objectLists.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/predicateObjectList_with_two_objectLists.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in predicateObjectList_with_two_objectLists.nt")
    earlOut("predicateObjectList_with_two_objectLists", true)

    output.close()
  }

  "The input file ./TurtleTests/repeated_semis_at_end.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/repeated_semis_at_end.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/repeated_semis_at_end.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in repeated_semis_at_end.nt")
    earlOut("repeated_semis_at_end", true)

    output.close()
  }

  "The input file ./TurtleTests/repeated_semis_not_at_end.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/repeated_semis_not_at_end.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/repeated_semis_not_at_end.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in repeated_semis_not_at_end.nt")
    earlOut("repeated_semis_not_at_end", true)

    output.close()
  }

  "The input file ./TurtleTests/comment_following_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/comment_following_localName.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/comment_following_localName.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in comment_following_localName.nt")
    earlOut("comment_following_localName", true)

    output.close()
  }

  "The input file ./TurtleTests/number_sign_following_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/number_sign_following_localName.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/number_sign_following_localName.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in number_sign_following_localName.nt")
    earlOut("number_sign_following_localName", true)

    output.close()
  }

  "The input file ./TurtleTests/comment_following_PNAME_NS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in comment_following_PNAME_NS.nt")
    earlOut("comment_following_PNAME_NS", true)

    output.close()
  }

  "The input file ./TurtleTests/number_sign_following_PNAME_NS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/number_sign_following_PNAME_NS.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/number_sign_following_PNAME_NS.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in number_sign_following_PNAME_NS.nt")
    earlOut("number_sign_following_PNAME_NS", true)

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_REVERSE_SOLIDUS.nt")
    earlOut("LITERAL_LONG2_with_REVERSE_SOLIDUS", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-LITERAL2_with_langtag_and_datatype.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-LITERAL2_with_langtag_and_datatype.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-LITERAL2_with_langtag_and_datatype.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-LITERAL2_with_langtag_and_datatype.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-LITERAL2_with_langtag_and_datatype", !res)

    output.close()
  }

  "The input file ./TurtleTests/two_LITERAL_LONG2s.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/two_LITERAL_LONG2s.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/two_LITERAL_LONG2s.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in two_LITERAL_LONG2s.nt")
    earlOut("two_LITERAL_LONG2s", true)

    output.close()
  }

  "The input file ./TurtleTests/langtagged_LONG_with_subtag.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in langtagged_LONG_with_subtag.nt")
    earlOut("langtagged_LONG_with_subtag", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-file-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-file-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-file-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-file-01.nt")
    earlOut("turtle-syntax-file-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-file-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-file-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-file-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-file-02.nt")
    earlOut("turtle-syntax-file-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-file-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-file-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-file-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-file-03.nt")
    earlOut("turtle-syntax-file-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-uri-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-uri-01.nt")
    earlOut("turtle-syntax-uri-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-uri-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-uri-02.nt")
    earlOut("turtle-syntax-uri-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-uri-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-uri-03.nt")
    earlOut("turtle-syntax-uri-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-uri-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-uri-04.nt")
    earlOut("turtle-syntax-uri-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-base-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-base-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-base-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-base-01.nt")
    earlOut("turtle-syntax-base-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-base-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-base-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-base-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-base-02.nt")
    earlOut("turtle-syntax-base-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-base-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-base-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-base-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-base-03.nt")
    earlOut("turtle-syntax-base-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-base-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-base-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-base-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-base-04.nt")
    earlOut("turtle-syntax-base-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-01.nt")
    earlOut("turtle-syntax-prefix-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-02.nt")
    earlOut("turtle-syntax-prefix-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-03.nt")
    earlOut("turtle-syntax-prefix-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-04.nt")
    earlOut("turtle-syntax-prefix-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-05.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-05.nt")
    earlOut("turtle-syntax-prefix-05", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-06.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-06_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-06.nt")
    earlOut("turtle-syntax-prefix-06", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-07.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-07.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-07.nt")
    earlOut("turtle-syntax-prefix-07", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-08.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-08.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-08.nt")
    earlOut("turtle-syntax-prefix-08", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-09.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-09.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-09.nt")
    earlOut("turtle-syntax-prefix-09", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-01.nt")
    earlOut("turtle-syntax-string-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-02.nt")
    earlOut("turtle-syntax-string-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-03.nt")
    earlOut("turtle-syntax-string-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-04.nt")
    earlOut("turtle-syntax-string-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-05.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-05.nt")
    earlOut("turtle-syntax-string-05", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-06.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-06.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-06.nt")
    earlOut("turtle-syntax-string-06", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-07.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-07.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-07.nt")
    earlOut("turtle-syntax-string-07", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-08.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-08.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-08.nt")
    earlOut("turtle-syntax-string-08", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-09.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-09.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-09.nt")
    earlOut("turtle-syntax-string-09", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-10.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-10.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-10.nt")
    earlOut("turtle-syntax-string-10", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-11.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-11.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-11.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-11.nt")
    earlOut("turtle-syntax-string-11", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-str-esc-01.nt")
    earlOut("turtle-syntax-str-esc-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-str-esc-02.nt")
    earlOut("turtle-syntax-str-esc-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-str-esc-03.nt")
    earlOut("turtle-syntax-str-esc-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-pname-esc-01.nt")
    earlOut("turtle-syntax-pname-esc-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-pname-esc-02.nt")
    earlOut("turtle-syntax-pname-esc-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-03_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-pname-esc-03.nt")
    earlOut("turtle-syntax-pname-esc-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-01.nt")
    earlOut("turtle-syntax-bnode-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-02.nt")
    earlOut("turtle-syntax-bnode-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-03.nt")
    earlOut("turtle-syntax-bnode-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-04.nt")
    earlOut("turtle-syntax-bnode-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-05.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-05.nt")
    earlOut("turtle-syntax-bnode-05", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-06.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-06.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-06.nt")
    earlOut("turtle-syntax-bnode-06", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-07.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-07.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-07.nt")
    earlOut("turtle-syntax-bnode-07", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-08.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-08.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-08.nt")
    earlOut("turtle-syntax-bnode-08", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-09.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-09.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-09.nt")
    earlOut("turtle-syntax-bnode-09", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-10.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-10.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-10.nt")
    earlOut("turtle-syntax-bnode-10", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-01.nt")
    earlOut("turtle-syntax-number-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-02.nt")
    earlOut("turtle-syntax-number-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-03.nt")
    earlOut("turtle-syntax-number-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-04.nt")
    earlOut("turtle-syntax-number-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-05.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-05.nt")
    earlOut("turtle-syntax-number-05", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-06.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-06.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-06.nt")
    earlOut("turtle-syntax-number-06", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-07.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-07.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-07.nt")
    earlOut("turtle-syntax-number-07", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-08.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-08.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-08.nt")
    earlOut("turtle-syntax-number-08", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-09.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-09.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-09.nt")
    earlOut("turtle-syntax-number-09", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-10.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-10.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-10.nt")
    earlOut("turtle-syntax-number-10", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-11.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-11.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-11.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-11.nt")
    earlOut("turtle-syntax-number-11", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-datatypes-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-datatypes-01.nt")
    earlOut("turtle-syntax-datatypes-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-datatypes-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-datatypes-02.nt")
    earlOut("turtle-syntax-datatypes-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-kw-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-kw-01.nt")
    earlOut("turtle-syntax-kw-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-kw-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-kw-02.nt")
    earlOut("turtle-syntax-kw-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-kw-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-kw-03.nt")
    earlOut("turtle-syntax-kw-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-01.nt")
    earlOut("turtle-syntax-struct-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-02.nt")
    earlOut("turtle-syntax-struct-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-03.nt")
    earlOut("turtle-syntax-struct-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-04.nt")
    earlOut("turtle-syntax-struct-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-05.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-05.nt")
    earlOut("turtle-syntax-struct-05", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-lists-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-lists-01.nt")
    earlOut("turtle-syntax-lists-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-lists-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-lists-02.nt")
    earlOut("turtle-syntax-lists-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-lists-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-lists-03.nt")
    earlOut("turtle-syntax-lists-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-lists-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-lists-04.nt")
    earlOut("turtle-syntax-lists-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-lists-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(19), "Number of triples generated should have been 19")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-lists-05.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-lists-05.nt")
    earlOut("turtle-syntax-lists-05", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-uri-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-uri-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-uri-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-uri-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-05.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-05.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-uri-05", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-prefix-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-prefix-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-prefix-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-prefix-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-05.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-05.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-prefix-05", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-base-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-base-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-base-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-base-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-base-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-base-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-base-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-base-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-base-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-05.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-05.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-05", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-06.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-06.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-06.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-06.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-06", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-07.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-07.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-07.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-07.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-07", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-kw-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-kw-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-kw-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-kw-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-05.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-05.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-kw-05", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-05.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-05.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-05", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-06.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-06.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-06.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-06.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-06", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-07.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-07.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-07.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-07.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-07", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-08.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-08.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-08.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-08.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-08", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-09.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-09.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-09.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-09.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-09", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-10.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-10.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-10.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-10.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-10", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-11.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-11.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-11.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-11.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-11", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-12.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-12.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-12.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-12.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-12", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-13.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-13.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-13.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-13.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-n3-extras-13", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-08.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-08.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-08.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-08.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-08", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-09.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-09.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-09.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-09.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-09", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-10.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-10.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-10.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-10.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-10", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-11.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-11.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-11.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-11.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-11", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-12.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-12.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-12.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-12.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-12", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-13.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-13.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-13.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-13.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-13", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-14.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-14.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-14.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-14.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-14", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-15.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-15.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-15.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-15.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-15", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-16.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-16.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-16.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-16.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-16", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-17.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-17.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-17.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-17.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-struct-17", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-lang-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-lang-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-lang-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-lang-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-lang-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-esc-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-esc-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-esc-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-esc-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-esc-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-esc-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-esc-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-esc-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-esc-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-esc-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-esc-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-esc-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-pname-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-pname-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-pname-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-pname-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-pname-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-pname-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-pname-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-pname-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-pname-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-string-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-string-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-string-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-string-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-05.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-05.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-string-05", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-06.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-06.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-06.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-06.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-string-06", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-07.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-07.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-07.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-07.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-string-07", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-num-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-num-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-num-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-num-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-05.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-05.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-num-05", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-struct-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-struct-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-eval-struct-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-eval-struct-01.nt")
    earlOut("turtle-eval-struct-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-struct-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-struct-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-eval-struct-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-eval-struct-02.nt")
    earlOut("turtle-eval-struct-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-01_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-01.nt")
    earlOut("turtle-subm-01", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-02.nt")
    earlOut("turtle-subm-02", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-03.nt")
    earlOut("turtle-subm-03", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-04.nt")
    earlOut("turtle-subm-04", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-05.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-05_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-05.nt")
    earlOut("turtle-subm-05", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-06.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-06_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-06.nt")
    earlOut("turtle-subm-06", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-07.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-07.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-07.nt")
    earlOut("turtle-subm-07", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-08.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-08_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-08.nt")
    earlOut("turtle-subm-08", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-09.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-09.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-09.nt")
    earlOut("turtle-subm-09", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-10.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-10_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-10.nt")
    earlOut("turtle-subm-10", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-11.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-11.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-11.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-11.nt")
    earlOut("turtle-subm-11", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-12.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-12.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-12.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-12.nt")
    earlOut("turtle-subm-12", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-13.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-13.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-13.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-13.nt")
    earlOut("turtle-subm-13", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-14.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-14.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-14_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-14.nt")
    earlOut("turtle-subm-14", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-15.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-15.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-15_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-15.nt")
    earlOut("turtle-subm-15", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-16.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-16.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-16_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-16.nt")
    earlOut("turtle-subm-16", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-17.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-17.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-17.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-17.nt")
    earlOut("turtle-subm-17", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-18.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-18.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-18.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-18.nt")
    earlOut("turtle-subm-18", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-19.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-19.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-19.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-19.nt")
    earlOut("turtle-subm-19", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-20.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-20.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-20.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-20.nt")
    earlOut("turtle-subm-20", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-21.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-21.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-21.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-21.nt")
    earlOut("turtle-subm-21", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-22.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-22.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-22.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-22.nt")
    earlOut("turtle-subm-22", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-23.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-23.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-23.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-23.nt")
    earlOut("turtle-subm-23", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-24.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-24.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-24.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-24.nt")
    earlOut("turtle-subm-24", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-25.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-25.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-25.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-25.nt")
    earlOut("turtle-subm-25", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-26.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-26.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(22), "Number of triples generated should have been 22")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-26.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-26.nt")
    earlOut("turtle-subm-26", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-27.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-27.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-27.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-27.nt")
    earlOut("turtle-subm-27", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-bad-01.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-bad-01.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-01.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-eval-bad-01", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-bad-02.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-bad-02.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-02.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-eval-bad-02", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-bad-03.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-bad-03.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-03.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-eval-bad-03", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-bad-04.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-bad-04.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-04.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-eval-bad-04", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-blank-label-dot-end.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-blank-label-dot-end.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-blank-label-dot-end.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-blank-label-dot-end.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-blank-label-dot-end", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ln-dash-start.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ln-dash-start.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-dash-start.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-dash-start.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-ln-dash-start", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ln-escape-start.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ln-escape-start.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-escape-start.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-escape-start.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-ln-escape-start", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ln-escape.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ln-escape.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-escape.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-escape.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-ln-escape", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-missing-ns-dot-end.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-missing-ns-dot-end.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-missing-ns-dot-end.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-missing-ns-dot-end.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-missing-ns-dot-end", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-missing-ns-dot-start.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-missing-ns-dot-start.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-missing-ns-dot-start.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-missing-ns-dot-start.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-missing-ns-dot-start", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ns-dot-end.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ns-dot-end.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ns-dot-end.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ns-dot-end.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-ns-dot-end", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ns-dot-start.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ns-dot-start.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ns-dot-start.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ns-dot-start.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-ns-dot-start", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-number-dot-in-anon.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-number-dot-in-anon.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-number-dot-in-anon.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-number-dot-in-anon.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-bad-number-dot-in-anon", !res)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-blank-label.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-blank-label.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.trigDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-blank-label.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-blank-label.ttl': Unexpected error during parsing run: " + e)
        false
    }
    earlOut("turtle-syntax-blank-label", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-ln-colons.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-ln-colons.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-ln-colons.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-ln-colons")
    earlOut("turtle-syntax-ln-colons", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-ln-dots.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-ln-dots.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-ln-dots.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-ln-dots")
    earlOut("turtle-syntax-ln-dots", true)

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-ns-dots.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-ns-dots.ttl").mkString

    val output = new StringWriter()

    val parser = TriGParser(input, tupleWriter(output)_, false, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.trigDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-ns-dots.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-ns-dots")
    earlOut("turtle-syntax-ns-dots", true)

    output.close()
  }
}
