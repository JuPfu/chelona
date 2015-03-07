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

import java.io.StringWriter
import java.util.Calendar

import org.parboiled2.{ ParseError, ParserInput }
import org.scalatest.FlatSpec

import scala.util.Failure

class ChelonaEARLSpec extends FlatSpec {

  def earlOut(testcase: String, passed: Boolean) = {
    val assertedBy = "<https://github.com/JuPfu#me>"
    val subject = "<https://github.com/JuPfu/chelona>"
    val test = testcase // "IRI_subject"
    val outcome = if (passed) "passed" else "failed"
    val datum = Calendar.getInstance.getTime
    val mode = "automatic"
    val earl_assertion = s"""[ a earl:Assertion;\n  earl:assertedBy ${assertedBy};\n  earl:subject ${subject};\n  earl:test <http://www.w3.org/2013/TurtleTests/manifest.ttl#${test}>;\n  earl:result [\n    a earl:TestResult;\n    earl:outcome earl:${outcome};\n    dc:date "${datum}"^^xsd:dateTime];\n  earl:mode earl:${mode} ] .")"""
    System.err.println(earl_assertion)
  }

  "The input file ./TurtleTests/IRI_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_spo.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in IRI_spo.nt")
      earlOut("IRI_subject", true)
    } catch {
      case _: Exception ⇒ earlOut("IRI_subject", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/IRI_with_four_digit_numeric_escape.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_with_four_digit_numeric_escape.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_with_four_digit_numeric_escape.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in IRI_with_four_digit_numeric_escape.nt")
      earlOut("IRI_with_four_digit_numeric_escape", true)
    } catch {
      case _: Exception ⇒ earlOut("IRI_with_four_digit_numeric_escape", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/IRI_with_all_punctuation.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in IRI_with_all_punctuation.nt")
      earlOut("IRI_with_all_punctuation", true)
    } catch {
      case _: Exception ⇒ earlOut("IRI_with_all_punctuation", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/bareword_a_predicate.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_a_predicate.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_a_predicate.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in bareword_a_predicate.nt")
      earlOut("bareword_a_predicate", true)
    } catch {
      case _: Exception ⇒ earlOut("bareword_a_predicate", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/old_style_prefix.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/old_style_prefix.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/old_style_prefix.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in old_style_prefix.nt")
      earlOut("old_style_prefix", true)
    } catch {
      case _: Exception ⇒ earlOut("old_style_prefix", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/SPARQL_style_prefix.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/SPARQL_style_prefix.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/SPARQL_style_prefix.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in SPARQL_style_prefix.nt")
      earlOut("SPARQL_style_prefix", true)
    } catch {
      case _: Exception ⇒ earlOut("SPARQL_style_prefix", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/prefixed_IRI_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefixed_IRI_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefixed_IRI_object.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in prefixed_IRI_object.nt")
      earlOut("prefixed_IRI_object", true)
    } catch {
      case _: Exception ⇒ earlOut("prefixed_IRI_object", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/prefix_only_IRI.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_only_IRI.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_only_IRI.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in prefix_only_IRI.nt")
      earlOut("prefix_only_IRI", true)
    } catch {
      case _: Exception ⇒ earlOut("prefix_only_IRI", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/prefix_with_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_with_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_with_PN_CHARS_BASE_character_boundaries.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in prefix_with_PN_CHARS_BASE_character_boundaries.nt")
      earlOut("prefix_with_PN_CHARS_BASE_character_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("prefix_with_PN_CHARS_BASE_character_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/prefix_with_non_leading_extras.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_with_non_leading_extras.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_with_non_leading_extras.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in prefix_with_non_leading_extras.nt")
      earlOut("prefix_with_non_leading_extras", true)
    } catch {
      case _: Exception ⇒ earlOut("prefix_with_non_leading_extras", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/default_namespace_IRI.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/default_namespace_IRI.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/default_namespace_IRI.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in default_namespace_IRI.nt")
      earlOut("default_namespace_IRI", true)
    } catch {
      case _: Exception ⇒ earlOut("default_namespace_IRI", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/prefix_reassigned_and_used.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_reassigned_and_used.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_reassigned_and_used.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in prefix_reassigned_and_used.nt")
      earlOut("prefix_reassigned_and_used", true)
    } catch {
      case _: Exception ⇒ earlOut("prefix_reassigned_and_used", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/reserved_escaped_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/reserved_escaped_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/reserved_escaped_localName.nt").mkString
    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in reserved_escaped_localName.nt")
      earlOut("reserved_escaped_localName", true)
    } catch {
      case _: Exception ⇒ earlOut("reserved_escaped_localName", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/percent_escaped_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/percent_escaped_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/percent_escaped_localName.nt").mkString
    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in percent_escaped_localName.nt")
      earlOut("percent_escaped_localName", true)
    } catch {
      case _: Exception ⇒ earlOut("percent_escaped_localName", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/HYPHEN_MINUS_in_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.nt").mkString

    try {
      assert(output.toString == nt.toString)
      earlOut("HYPHEN_MINUS_in_localName", true)
    } catch {
      case _: Exception ⇒ earlOut("HYPHEN_MINUS_in_localName", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/underscore_in_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/underscore_in_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/underscore_in_localName.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in underscore_in_localName.nt")
      earlOut("underscore_in_localName", true)
    } catch {
      case _: Exception ⇒ earlOut("underscore_in_localName", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/localname_with_COLON.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localname_with_COLON.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localname_with_COLON.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in localname_with_COLON.nt")
      earlOut("localname_with_COLON", true)
    } catch {
      case _: Exception ⇒ earlOut("localname_with_COLON", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries_isomorphic.nt")
      earlOut("localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.nt")
      earlOut("localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_nfc_PN_CHARS_BASE_character_boundaries.nt")
      earlOut("localName_with_nfc_PN_CHARS_BASE_character_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("localName_with_nfc_PN_CHARS_BASE_character_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/localName_with_leading_underscore.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_leading_underscore.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_leading_underscore.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_leading_underscore.nt")
      earlOut("localName_with_leading_underscore", true)
    } catch {
      case _: Exception ⇒ earlOut("localName_with_leading_underscore", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/localName_with_leading_digit.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_leading_digit.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_leading_digit.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_leading_digit.nt")
      earlOut("localName_with_leading_digit", true)
    } catch {
      case _: Exception ⇒ earlOut("localName_with_leading_digit", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/localName_with_non_leading_extras.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_non_leading_extras.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_non_leading_extras_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in localName_with_non_leading_extras_isomorphic.nt")
      earlOut("localName_with_non_leading_extras_isomorphic", true)
    } catch {
      case _: Exception ⇒ earlOut("localName_with_non_leading_extras_isomorphic", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/old_style_base.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/old_style_base.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/old_style_base.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in old_style_base.nt")
      earlOut("old_style_base", true)
    } catch {
      case _: Exception ⇒ earlOut("old_style_base", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/SPARQL_style_base.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/SPARQL_style_base.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/SPARQL_style_base.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in SPARQL_style_base.nt")
      earlOut("SPARQL_style_base", true)
    } catch {
      case _: Exception ⇒ earlOut("SPARQL_style_base", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_subject.nt")
      earlOut("labeled_blank_node_subject", true)
    } catch {
      case _: Exception ⇒ earlOut("labeled_blank_node_subject", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_object.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_object.nt")
      earlOut("labeled_blank_node_object", true)
    } catch {
      case _: Exception ⇒ earlOut("labeled_blank_node_object", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.nt")
      earlOut("labeled_blank_node_with_PN_CHARS_BASE_character_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("labeled_blank_node_with_PN_CHARS_BASE_character_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_with_leading_underscore.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_underscore.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_underscore.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_with_leading_underscore.nt")
      earlOut("labeled_blank_node_with_leading_underscore", true)
    } catch {
      case _: Exception ⇒ earlOut("labeled_blank_node_with_leading_underscore", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_with_leading_digit.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_digit.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_digit.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_with_leading_digit.nt")
      earlOut("labeled_blank_node_with_leading_digit", true)
    } catch {
      case _: Exception ⇒ earlOut("labeled_blank_node_with_leading_digit", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_with_non_leading_extras.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_non_leading_extras.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_with_non_leading_extras.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_with_non_leading_extras.nt")
      earlOut("labeled_blank_node_with_non_leading_extras", true)
    } catch {
      case _: Exception ⇒ earlOut("labeled_blank_node_with_non_leading_extras", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/anonymous_blank_node_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/anonymous_blank_node_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/anonymous_blank_node_subject.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in anonymous_blank_node_subject.nt")
      earlOut("anonymous_blank_node_subject", true)
    } catch {
      case _: Exception ⇒ earlOut("anonymous_blank_node_subject", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/anonymous_blank_node_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/anonymous_blank_node_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/anonymous_blank_node_object.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in anonymous_blank_node_object.nt")
      earlOut("anonymous_blank_node_object ", true)
    } catch {
      case _: Exception ⇒ earlOut("anonymous_blank_node_object", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/sole_blankNodePropertyList.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/sole_blankNodePropertyList.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/sole_blankNodePropertyList.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in sole_blankNodePropertyList.nt")
      earlOut("sole_blankNodePropertyList", true)
    } catch {
      case _: Exception ⇒ earlOut("sole_blankNodePropertyList", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_as_subject.nt")
      earlOut("blankNodePropertyList_as_subject", true)
    } catch {
      case _: Exception ⇒ earlOut("blankNodePropertyList_as_subject", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_as_object.nt")
      earlOut("blankNodePropertyList_as_object", true)
    } catch {
      case _: Exception ⇒ earlOut("blankNodePropertyList_as_object", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_with_multiple_triples.nt")
      earlOut("blankNodePropertyList_with_multiple_triples", true)
    } catch {
      case _: Exception ⇒ earlOut("blankNodePropertyList_with_multiple_triples", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/nested_blankNodePropertyLists.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/nested_blankNodePropertyLists.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/nested_blankNodePropertyLists.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in nested_blankNodePropertyLists.nt")
      earlOut("nested_blankNodePropertyLists", true)
    } catch {
      case _: Exception ⇒ earlOut("nested_blankNodePropertyLists", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_containing_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_containing_collection_isomorphic.nt")
      earlOut("blankNodePropertyList_containing_collection", true)
    } catch {
      case _: Exception ⇒ earlOut("blankNodePropertyList_containing_collection", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/collection_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/collection_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/collection_subject_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in collection_subject_isomorphic.nt")
      earlOut("collection_subject", true)
    } catch {
      case _: Exception ⇒ earlOut("collection_subject", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/collection_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/collection_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/collection_object_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in collection_object_isomorphic.nt")
      earlOut("collection_object", true)
    } catch {
      case _: Exception ⇒ earlOut("collection_object", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/empty_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/empty_collection.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/empty_collection.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in empty_collection.nt")
      earlOut("empty_collection", true)
    } catch {
      case _: Exception ⇒ earlOut("empty_collection", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/nested_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/nested_collection.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/nested_collection_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in nested_collection_isomorphic.nt")
      earlOut("nested_collection", true)
    } catch {
      case _: Exception ⇒ earlOut("nested_collection", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/first.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/first.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/first_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in first_isomorphic.nt")
      earlOut("first", true)
    } catch {
      case _: Exception ⇒ earlOut("first", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/last.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/last.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/last_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in last_isomorphic.nt")
      earlOut("last", true)
    } catch {
      case _: Exception ⇒ earlOut("last", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1.nt")
      earlOut("LITERAL1", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL1", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_ascii_boundaries_isomorphic.nt")
      earlOut("LITERAL1_ascii_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL1_ascii_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_with_UTF8_boundaries.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_with_UTF8_boundaries.nt")
      earlOut("LITERAL1_with_UTF8_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL1_with_UTF8_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_all_controls.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_all_controls.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_all_controls_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_all_controls_isomorphic.nt")
      earlOut("LITERAL1_all_controls", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL1_all_controls", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_all_punctuation.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_all_punctuation.nt")
      earlOut("LITERAL1_all_punctuation", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL1_all_punctuation", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1.nt")
      earlOut("LITERAL_LONG1", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG1", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_ascii_boundaries_isomorphic.nt")
      earlOut("LITERAL1_all_punctuation", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL1_all_punctuation", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_UTF8_boundaries_isomorphic.nt")
      earlOut("LITERAL_LONG1_with_UTF8_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG1_with_UTF8_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_1_squote.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_1_squote.nt")
      earlOut("LITERAL_LONG1_with_1_squote", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG1_with_1_squote", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_2_squotes.nt")
      earlOut("LITERAL_LONG1_with_2_squotes", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG1_with_2_squotes", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL2.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL2.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL2.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL2.nt")
      earlOut("LITERAL2", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL2", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL2_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL2_ascii_boundaries_isomorphic.nt")
      earlOut("LITERAL2_ascii_boundaries_isomorphic", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL2_ascii_boundaries_isomorphic", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL2_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL2_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL2_with_UTF8_boundaries.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL2_with_UTF8_boundaries.nt")
      earlOut("LITERAL2_with_UTF8_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL2_with_UTF8_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2.nt")
      earlOut("LITERAL_LONG2", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG2", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_ascii_boundaries_isomorphic.nt")
      earlOut("LITERAL_LONG2_ascii_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG2_ascii_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_UTF8_boundaries.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_UTF8_boundaries.nt")
      earlOut("LITERAL_LONG2_with_UTF8_boundaries", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG2_with_UTF8_boundaries", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_1_squote.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_1_squote.nt")
      earlOut("LITERAL_LONG2_with_1_squote", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG2_with_1_squote", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_2_squotes.nt")
      earlOut("LITERAL_LONG2_with_2_squotes", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG2_with_2_squotes", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_CHARACTER_TABULATION.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_CHARACTER_TABULATION.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_CHARACTER_TABULATION_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_CHARACTER_TABULATION_isomorphic.nt")
      earlOut("literal_with_CHARACTER_TABULATION", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_CHARACTER_TABULATION", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_BACKSPACE.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_BACKSPACE.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_BACKSPACE_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_BACKSPACE_isomorphic.nt")
      earlOut("literal_with_BACKSPACE", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_BACKSPACE", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_LINE_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_LINE_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_LINE_FEED_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_LINE_FEED_isomorphic.nt")
      earlOut("literal_with_LINE_FEED", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_LINE_FEED", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_CARRIAGE_RETURN.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_CARRIAGE_RETURN.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_CARRIAGE_RETURN.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_CARRIAGE_RETURN.nt")
      earlOut("literal_with_CARRIAGE_RETURN", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_CARRIAGE_RETURN", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_FORM_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_FORM_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_FORM_FEED_isomorphic.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_FORM_FEED_isomorphic.nt")
      earlOut("literal_with_FORM_FEED_isomorphic", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_FORM_FEED_isomorphic", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_REVERSE_SOLIDUS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_REVERSE_SOLIDUS.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_REVERSE_SOLIDUS.nt")
      earlOut("literal_with_REVERSE_SOLIDUS", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_REVERSE_SOLIDUS", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_CHARACTER_TABULATION.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_CHARACTER_TABULATION.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_CHARACTER_TABULATION.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_CHARACTER_TABULATION.nt")
      earlOut("literal_with_escaped_CHARACTER_TABULATION", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_escaped_CHARACTER_TABULATION", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_BACKSPACE.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_BACKSPACE.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_BACKSPACE.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_BACKSPACE.nt")
      earlOut("literal_with_escaped_BACKSPACE", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_escaped_BACKSPACE", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_LINE_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_LINE_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_LINE_FEED.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_LINE_FEED.nt")
      earlOut("literal_with_escaped_LINE_FEED", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_escaped_LINE_FEED", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_CARRIAGE_RETURN.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_CARRIAGE_RETURN.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_CARRIAGE_RETURN.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_CARRIAGE_RETURN.nt")
      earlOut("literal_with_escaped_CARRIAGE_RETURN", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_escaped_CARRIAGE_RETURN", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_escaped_FORM_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_escaped_FORM_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_escaped_FORM_FEED.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_escaped_FORM_FEED.nt")
      earlOut("literal_with_escaped_FORM_FEED", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_escaped_FORM_FEED", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_numeric_escape4.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape4.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape4.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_numeric_escape4.nt")
      earlOut("literal_with_numeric_escape4", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_numeric_escape4", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_with_numeric_escape8.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape8.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape8.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_with_numeric_escape8.nt")
      earlOut("literal_with_numeric_escape8", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_with_numeric_escape8", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/IRIREF_datatype.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRIREF_datatype.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRIREF_datatype.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in IRIREF_datatype.nt")
      earlOut("IRIREF_datatype", true)
    } catch {
      case _: Exception ⇒ earlOut("IRIREF_datatype", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/prefixed_name_datatype.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefixed_name_datatype.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefixed_name_datatype.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in prefixed_name_datatype.nt")
      earlOut("prefixed_name_datatype", true)
    } catch {
      case _: Exception ⇒ earlOut("prefixed_name_datatype", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/bareword_integer.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_integer.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_integer.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in bareword_integer.nt")
      earlOut("bareword_integer", true)
    } catch {
      case _: Exception ⇒ earlOut("bareword_integer", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/bareword_decimal.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_decimal.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_decimal.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in bareword_decimal.nt")
      earlOut("bareword_decimal", true)
    } catch {
      case _: Exception ⇒ earlOut("bareword_decimal", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/bareword_double.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_double.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_double.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in bareword_double.nt")
      earlOut("bareword_double", true)
    } catch {
      case _: Exception ⇒ earlOut("bareword_double", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/double_lower_case_e.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/double_lower_case_e.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/double_lower_case_e.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in double_lower_case_e.nt")
      earlOut("double_lower_case_e", true)
    } catch {
      case _: Exception ⇒ earlOut("double_lower_case_e", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/negative_numeric.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/negative_numeric.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/negative_numeric.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in negative_numeric.nt")
      earlOut("negative_numeric", true)
    } catch {
      case _: Exception ⇒ earlOut("negative_numeric", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/positive_numeric.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/positive_numeric.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/positive_numeric.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in positive_numeric.nt")
      earlOut("positive_numeric", true)
    } catch {
      case _: Exception ⇒ earlOut("positive_numeric", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/numeric_with_leading_0.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/numeric_with_leading_0.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/numeric_with_leading_0.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in numeric_with_leading_0.nt")
      earlOut("numeric_with_leading_0", true)
    } catch {
      case _: Exception ⇒ earlOut("numeric_with_leading_0", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_true.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_true.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_true.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_true.nt")
      earlOut("literal_true", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_true", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/literal_false.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_false.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_false.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in literal_false.nt")
      earlOut("literal_false", true)
    } catch {
      case _: Exception ⇒ earlOut("literal_false", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/langtagged_non_LONG.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_non_LONG.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_non_LONG.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in langtagged_non_LONG.nt")
      earlOut("langtagged_non_LONG", true)
    } catch {
      case _: Exception ⇒ earlOut("langtagged_non_LONG", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/langtagged_LONG.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_LONG.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_LONG.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in langtagged_LONG.nt")
      earlOut("langtagged_LONG", true)
    } catch {
      case _: Exception ⇒ earlOut("langtagged_LONG", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/objectList_with_two_objects.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/objectList_with_two_objects.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/objectList_with_two_objects.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in objectList_with_two_objects.nt")
      earlOut("objectList_with_two_objects", true)
    } catch {
      case _: Exception ⇒ earlOut("objectList_with_two_objects", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/predicateObjectList_with_two_objectLists.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/predicateObjectList_with_two_objectLists.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/predicateObjectList_with_two_objectLists.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in predicateObjectList_with_two_objectLists.nt")
      earlOut("objectList_with_two_objects", true)
    } catch {
      case _: Exception ⇒ earlOut("objectList_with_two_objects", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/repeated_semis_at_end.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/repeated_semis_at_end.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/repeated_semis_at_end.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in repeated_semis_at_end.nt")
      earlOut("repeated_semis_at_end", true)
    } catch {
      case _: Exception ⇒ earlOut("repeated_semis_at_end", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/repeated_semis_not_at_end.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/repeated_semis_not_at_end.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/repeated_semis_not_at_end.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in repeated_semis_not_at_end.nt")
      earlOut("repeated_semis_not_at_end", true)
    } catch {
      case _: Exception ⇒ earlOut("repeated_semis_not_at_end", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/comment_following_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/comment_following_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/comment_following_localName.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in comment_following_localName.nt")
      earlOut("comment_following_localName", true)
    } catch {
      case _: Exception ⇒ earlOut("comment_following_localName", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/number_sign_following_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/number_sign_following_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/number_sign_following_localName.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in number_sign_following_localName.nt")
      earlOut("number_sign_following_localName", true)
    } catch {
      case _: Exception ⇒ earlOut("number_sign_following_localName", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/comment_following_PNAME_NS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in comment_following_PNAME_NS.nt")
      earlOut("comment_following_PNAME_NS", true)
    } catch {
      case _: Exception ⇒ earlOut("comment_following_PNAME_NS", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/number_sign_following_PNAME_NS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/number_sign_following_PNAME_NS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/number_sign_following_PNAME_NS.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in number_sign_following_PNAME_NS.nt")
      earlOut("number_sign_following_PNAME_NS", true)
    } catch {
      case _: Exception ⇒ earlOut("number_sign_following_PNAME_NS", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_REVERSE_SOLIDUS.nt")
      earlOut("LITERAL_LONG2_with_REVERSE_SOLIDUS", true)
    } catch {
      case _: Exception ⇒ earlOut("LITERAL_LONG2_with_REVERSE_SOLIDUS", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-05.ttl" must "fail" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-05.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    try {
      val res = parser.turtleDoc.run() match {
        case scala.util.Success(tripleCount) ⇒
          true
        case Failure(e: ParseError) ⇒
          System.err.println("File './TurtleTests/turtle-syntax-bad-num-05.ttl': " + parser.formatError(e))
          false
        case Failure(e) ⇒
          System.err.println("File './TurtleTests/turtle-syntax-bad-num-05.ttl': Unexpected error during parsing run: " + e)
          false
      }
      earlOut("turtle-syntax-bad-num-05", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bad-num-05 Input", true)
    }

    output.close()
  }

  "The input file ./TurtleTests/two_LITERAL_LONG2s.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/two_LITERAL_LONG2s.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/two_LITERAL_LONG2s.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in two_LITERAL_LONG2s.nt")
      earlOut("two_LITERAL_LONG2s", true)
    } catch {
      case _: Exception ⇒ earlOut("two_LITERAL_LONG2s", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/langtagged_LONG_with_subtag.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in langtagged_LONG_with_subtag.nt")
      earlOut("langtagged_LONG_with_subtag", true)
    } catch {
      case _: Exception ⇒ earlOut("langtagged_LONG_with_subtag", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-file-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-file-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-file-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-file-01.nt")
      earlOut("turtle-syntax-file-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-file-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-file-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-file-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-file-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-file-02.nt")
      earlOut("turtle-syntax-file-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-file-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-file-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-file-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-file-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-file-03.nt")
      earlOut("turtle-syntax-file-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-file-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-uri-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-uri-01.nt")
      earlOut("turtle-syntax-uri-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-uri-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-uri-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-uri-02.nt")
      earlOut("turtle-syntax-uri-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-uri-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-uri-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-uri-03.nt")
      earlOut("turtle-syntax-uri-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-uri-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-uri-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-uri-04.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-uri-04.nt")
      earlOut("turtle-syntax-uri-04", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-uri-04", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-base-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-base-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-base-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-base-01.nt")
      earlOut("turtle-syntax-base-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-base-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-base-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-base-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-base-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-base-02.nt")
      earlOut("turtle-syntax-base-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-base-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-base-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-base-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-base-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-base-03.nt")
      earlOut("turtle-syntax-base-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-base-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-base-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-base-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-base-04.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-base-04.nt")
      earlOut("turtle-syntax-base-04", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-base-04", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-01.nt")
      earlOut("turtle-syntax-prefix-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of triples generated should have been 0")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-02.nt")
      earlOut("turtle-syntax-prefix-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-03.nt")
      earlOut("turtle-syntax-prefix-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-04.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-04.nt")
      earlOut("turtle-syntax-prefix-04", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-04", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-05.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-05.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-05.nt")
      earlOut("turtle-syntax-prefix-05", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-05", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-06.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-06.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-06.nt")
      earlOut("turtle-syntax-prefix-06", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-06", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-07.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-07.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-07.nt")
      earlOut("turtle-syntax-prefix-07", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-07", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-08.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-08.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-08.nt")
      earlOut("turtle-syntax-prefix-08", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-08", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-09.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-09.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-prefix-09.nt")
      earlOut("turtle-syntax-prefix-09", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-prefix-09", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-01.nt")
      earlOut("turtle-syntax-string-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-02.nt")
      earlOut("turtle-syntax-string-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-03.nt")
      earlOut("turtle-syntax-string-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-04.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-04.nt")
      earlOut("turtle-syntax-string-04", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-04", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-05.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-05.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-05.nt")
      earlOut("turtle-syntax-string-05", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-05", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-06.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-06.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-06.nt")
      earlOut("turtle-syntax-string-06", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-06", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-07.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-07.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-07.nt")
      earlOut("turtle-syntax-string-07", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-07", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-08.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-08.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-08.nt")
      earlOut("turtle-syntax-string-08", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-08", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-09.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-09.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-09.nt")
      earlOut("turtle-syntax-string-09", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-09", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-10.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-10.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-10.nt")
      earlOut("turtle-syntax-string-10", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-10", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-string-11.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-string-11.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-string-11.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-string-11.nt")
      earlOut("turtle-syntax-string-11", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-string-11", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-str-esc-01.nt")
      earlOut("turtle-syntax-str-esc-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-str-esc-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-str-esc-02.nt")
      earlOut("turtle-syntax-str-esc-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-str-esc-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-str-esc-03.nt")
      earlOut("turtle-syntax-str-esc-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-str-esc-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-pname-esc-01.nt")
      earlOut("turtle-syntax-pname-esc-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-pname-esc-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-pname-esc-02.nt")
      earlOut("turtle-syntax-pname-esc-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-pname-esc-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-pname-esc-03.nt")
      earlOut("turtle-syntax-pname-esc-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-pname-esc-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-01.nt")
      earlOut("turtle-syntax-bnode-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-02.nt")
      earlOut("turtle-syntax-bnode-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-03.nt")
      earlOut("turtle-syntax-bnode-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-04.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-04.nt")
      earlOut("turtle-syntax-bnode-04", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-04", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-05.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-05.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-05.nt")
      earlOut("turtle-syntax-bnode-05", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-05", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-06.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-06.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-06.nt")
      earlOut("turtle-syntax-bnode-06", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-06", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-07.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-07.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-07.nt")
      earlOut("turtle-syntax-bnode-07", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-07", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-08.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-08.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-08.nt")
      earlOut("turtle-syntax-bnode-08", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-08", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-09.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-09.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-09.nt")
      earlOut("turtle-syntax-bnode-09", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-09", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-10.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-10.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-bnode-10.nt")
      earlOut("turtle-syntax-bnode-10", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-bnode-10", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-01.nt")
      earlOut("turtle-syntax-number-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-02.nt")
      earlOut("turtle-syntax-number-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-03.nt")
      earlOut("turtle-syntax-number-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-04.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-04.nt")
      earlOut("turtle-syntax-number-04", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-04", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-05.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-05.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-05.nt")
      earlOut("turtle-syntax-number-05", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-05", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-06.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-06.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-06.nt")
      earlOut("turtle-syntax-number-06", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-06", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-07.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-07.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-07.nt")
      earlOut("turtle-syntax-number-07", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-07", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-08.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-08.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-08.nt")
      earlOut("turtle-syntax-number-08", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-08", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-09.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-09.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-09.nt")
      earlOut("turtle-syntax-number-09", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-09", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-10.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-10.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-10.nt")
      earlOut("turtle-syntax-number-10", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-10", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-number-11.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-number-11.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-number-11.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-number-11.nt")
      earlOut("turtle-syntax-number-11", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-number-11", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-datatypes-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-datatypes-01.nt")
      earlOut("turtle-syntax-datatypes-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-datatypes-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-datatypes-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-datatypes-02.nt")
      earlOut("turtle-syntax-datatypes-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-datatypes-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-kw-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-kw-01.nt")
      earlOut("turtle-syntax-kw-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-kw-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-kw-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-kw-02.nt")
      earlOut("turtle-syntax-kw-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-kw-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-kw-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-kw-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-kw-03.nt")
      earlOut("turtle-syntax-kw-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-kw-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-01.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-01.nt")
      earlOut("turtle-syntax-struct-01", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-struct-01", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-02.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-02.nt")
      earlOut("turtle-syntax-struct-02", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-struct-02", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-03.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-03.nt")
      earlOut("turtle-syntax-struct-03", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-struct-03", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-04.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-04.nt")
      earlOut("turtle-syntax-struct-04", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-struct-04", false)
    }

    output.close()
  }

  "The input file ./TurtleTests/turtle-syntax-struct-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-05.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-syntax-struct-05.nt").mkString

    try {
      assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-syntax-struct-05.nt")
      earlOut("turtle-syntax-struct-05", true)
    } catch {
      case _: Exception ⇒ earlOut("turtle-syntax-struct-05", false)
    }

    output.close()
  }



  "The input file ./TurtleTests/lantag_with_subtag.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/lantag_with_subtag.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/lantag_with_subtag.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in lantag_with_subtag.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-struct-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-struct-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-eval-struct-01.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-eval-struct-01.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-struct-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-struct-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-eval-struct-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-eval-struct-02.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-01_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-01_isomorphic.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-02.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-02.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-03.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-03.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-04.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-04.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-05.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-05_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-05_isomorphic.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-06.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-06_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-06_isomorphic.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-07.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-07.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-07.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-08.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-08_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-08_isomorphic.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-09.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-09.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-09.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-10.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-10_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-10_isomorphic.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-11.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-11.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-11.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-11.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-12.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-12.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-12.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-12.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-13.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-13.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-13.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-13.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-14.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-14.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-14_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-14_isomorphic.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-15.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-15.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-15_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-15_isomorphic.nt")

    output.close()
  }
  "The input file ./TurtleTests/turtle-subm-16.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-16.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-16_isomorphic.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-16_isomorphic.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-17.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-17.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-17.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-17.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-18.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-18.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-18.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-18.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-19.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-19.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-19.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-19.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-20.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-20.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-20.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-20.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-21.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-21.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-21.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-21.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-22.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-22.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-22.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-22.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-23.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-23.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-23.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-23.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-24.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-24.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-24.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-24.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-25.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-25.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-25.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-25.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-26.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-26.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(22), "Number of triples generated should have been 22")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-26.nt").mkString

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-26.nt")

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-27.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-27.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-27_isomorphic.nt").mkString
    System.err.println("OUTPUT=>" + output.toString())
    System.err.println("NT=>" + nt.toString())

    assert(output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-27_isomorphic.nt")

    output.close()
  }

}
