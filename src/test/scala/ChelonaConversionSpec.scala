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

import org.parboiled2.ParserInput
import org.scalatest.FlatSpec

class ChelonaConversionSpec extends FlatSpec {

  "The input file ./TurtleTests/HYPHEN_MINUS_in_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.nt").mkString

    assert( output.toString == nt.toString )

    output.close()
  }

  "The input file ./TurtleTests/IRIREF_datatype.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRIREF_datatype.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRIREF_datatype.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in IRIREF_datatype.nt" )

    output.close()
  }

  "The input file ./TurtleTests/IRI_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_spo.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in IRI_spo.nt" )

    output.close()
  }

  "The input file ./TurtleTests/IRI_with_all_punctuation.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in IRI_with_all_punctuation.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_all_controls.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_all_controls.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_all_controls_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_all_controls_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_all_punctuation.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_all_punctuation.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_ascii_boundaries_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL2_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL2_ascii_boundaries_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_ascii_boundaries_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_1_squote.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_1_squote.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_2_squotes.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_ascii_boundaries_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_1_squote.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_1_squote.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_2_squotes.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_REVERSE_SOLIDUS.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_UTF8_boundaries_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/bareword_a_predicate.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_a_predicate.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_a_predicate.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in bareword_a_predicate.nt" )

    output.close()
  }

  "The input file ./TurtleTests/bareword_decimal.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_decimal.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_decimal.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in bareword_decimal.nt" )

    output.close()
  }

  "The input file ./TurtleTests/bareword_double.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_double.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_double.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in bareword_double.nt" )

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_as_object.nt" )

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_as_subject.nt" )

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_containing_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_containing_collection_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_with_multiple_triples.nt" )

    output.close()
  }

  "The input file ./TurtleTests/collection_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/collection_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/collection_object_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in collection_object_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/collection_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/collection_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/collection_subject_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in collection_subject_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/comment_following_PNAME_NS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in comment_following_PNAME_NS.nt" )

    output.close()
  }

  "The input file ./TurtleTests/double_lower_case_e.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/double_lower_case_e.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/double_lower_case_e.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in double_lower_case_e.nt" )

    output.close()
  }

  "The input file ./TurtleTests/empty_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/empty_collection.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/empty_collection.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in empty_collection.nt" )

    output.close()
  }

  "The input file ./TurtleTests/first.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/first.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/first_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in first_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_object.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_object.nt" )

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_subject.nt" )

    output.close()
  }

  "The input file ./TurtleTests/langtagged_LONG_with_subtag.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in langtagged_LONG_with_subtag.nt" )

    output.close()
  }

  "The input file ./TurtleTests/langtagged_non_LONG.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_non_LONG.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_non_LONG.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in langtagged_non_LONG.nt" )

    output.close()
  }

  "The input file ./TurtleTests/lantag_with_subtag.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/lantag_with_subtag.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/lantag_with_subtag.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in lantag_with_subtag.nt" )

    output.close()
  }

  "The input file ./TurtleTests/last.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/last.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/last_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in last_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_false.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_false.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_false.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_false.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_true.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_true.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_true.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_true.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_with_BACKSPACE.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_BACKSPACE.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_BACKSPACE_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_BACKSPACE_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_with_CARRIAGE_RETURN.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_CARRIAGE_RETURN.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_CARRIAGE_RETURN.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_CARRIAGE_RETURN.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_with_CHARACTER_TABULATION.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_CHARACTER_TABULATION.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_CHARACTER_TABULATION_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_CHARACTER_TABULATION_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_with_FORM_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_FORM_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_FORM_FEED_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_FORM_FEED_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_with_LINE_FEED.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_LINE_FEED.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_LINE_FEED_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_LINE_FEED_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_REVERSE_SOLIDUS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_REVERSE_SOLIDUS.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_REVERSE_SOLIDUS.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_with_numeric_escape4.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape4.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_with_numeric_escape4.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_numeric_escape4.nt" )

    output.close()
  }

  "The input file ./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.nt" )

    output.close()
  }

  "The input file ./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.nt" )

    output.close()
  }

  "The input file ./TurtleTests/localName_with_leading_digit.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_leading_digit.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_leading_digit.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in localName_with_leading_digit.nt" )

    output.close()
  }

  "The input file ./TurtleTests/localName_with_leading_underscore.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_leading_underscore.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_leading_underscore.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in localName_with_leading_underscore.nt" )

    output.close()
  }

  "The input file ./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in localName_with_nfc_PN_CHARS_BASE_character_boundaries.nt" )

    output.close()
  }

  "The input file ./TurtleTests/localName_with_non_leading_extras.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localName_with_non_leading_extras.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localName_with_non_leading_extras_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in localName_with_non_leading_extras_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/localname_with_COLON.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/localname_with_COLON.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/localname_with_COLON.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in localname_with_COLON.nt" )

    output.close()
  }

  "The input file ./TurtleTests/negative_numeric.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/negative_numeric.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/negative_numeric.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in negative_numeric.nt" )

    output.close()
  }

  "The input file ./TurtleTests/nested_blankNodePropertyLists.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/nested_blankNodePropertyLists.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/nested_blankNodePropertyLists.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in nested_blankNodePropertyLists.nt" )

    output.close()
  }

  "The input file ./TurtleTests/nested_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/nested_collection.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/nested_collection_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in nested_collection_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/number_sign_following_PNAME_NS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/number_sign_following_PNAME_NS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/number_sign_following_PNAME_NS.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in number_sign_following_PNAME_NS.nt" )

    output.close()
  }

  "The input file ./TurtleTests/number_sign_following_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/number_sign_following_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/number_sign_following_localName.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in number_sign_following_localName.nt" )

    output.close()
  }

  "The input file ./TurtleTests/numeric_with_leading_0.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/numeric_with_leading_0.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/numeric_with_leading_0.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in numeric_with_leading_0.nt" )

    output.close()
  }

  "The input file ./TurtleTests/objectList_with_two_objects.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/objectList_with_two_objects.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/objectList_with_two_objects.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in objectList_with_two_objects.nt" )

    output.close()
  }

  "The input file ./TurtleTests/percent_escaped_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/percent_escaped_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/percent_escaped_localName.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in percent_escaped_localName.nt" )

    output.close()
  }

  "The input file ./TurtleTests/positive_numeric.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/positive_numeric.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/positive_numeric.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in positive_numeric.nt" )

    output.close()
  }

  "The input file ./TurtleTests/predicateObjectList_with_two_objectLists.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/predicateObjectList_with_two_objectLists.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/predicateObjectList_with_two_objectLists.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in predicateObjectList_with_two_objectLists.nt" )

    output.close()
  }

  "The input file ./TurtleTests/prefix_reassigned_and_used.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/prefix_reassigned_and_used.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/prefix_reassigned_and_used.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in prefix_reassigned_and_used.nt" )

    output.close()
  }

  "The input file ./TurtleTests/repeated_semis_not_at_end.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/repeated_semis_not_at_end.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/repeated_semis_not_at_end.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in repeated_semis_not_at_end.nt" )

    output.close()
  }

  "The input file ./TurtleTests/reserved_escaped_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/reserved_escaped_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/reserved_escaped_localName.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in reserved_escaped_localName.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-struct-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-struct-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-eval-struct-01.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-eval-struct-01.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-eval-struct-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-eval-struct-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-eval-struct-02.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-eval-struct-02.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-01.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-01.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-01.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-02.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-02.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-02.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-02.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-03.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-03.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-03.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-03.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-04.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-04.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-04.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-04.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-05.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-05.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-05_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-05_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-06.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-06.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-06_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-06_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-07.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-07.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-07.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-07.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-08.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-08.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-08_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-08_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-09.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-09.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-09.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-09.nt" )

    output.close()
  }


  "The input file ./TurtleTests/turtle-subm-10.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-10.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-10_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-10_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-11.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-11.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-11.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-11.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-12.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-12.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-12.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-12.nt" )

    output.close()
  }


  "The input file ./TurtleTests/turtle-subm-13.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-13.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of triples generated should have been 4")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-13.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-13.nt" )

    output.close()
  }


  "The input file ./TurtleTests/turtle-subm-14.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-14.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-14_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-14_isomorphic.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-15.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-15.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-15_isomorphic.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-15_isomorphic.nt" )

    output.close()
  }
  "The input file ./TurtleTests/turtle-subm-16.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-16.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-16.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-16.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-17.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-17.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-17.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-17.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-18.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-18.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-18.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-18.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-19.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-19.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-19.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-19.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-20.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-20.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-20.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-20.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-21.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-21.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-21.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-21.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-22.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-22.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-22.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-22.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-23.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-23.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-23.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-23.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-24.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-24.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-24.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-24.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-25.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-25.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-25.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-25.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-26.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-26.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(22), "Number of triples generated should have been 22")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-26.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-26.nt" )

    output.close()
  }

  "The input file ./TurtleTests/turtle-subm-27.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/turtle-subm-27.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of triples generated should have been 5")

    val nt = io.Source.fromFile("./TurtleTests/turtle-subm-27.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in turtle-subm-27.nt" )

    output.close()
  }

  "The input file ./TurtleTests/two_LITERAL_LONG2s.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/two_LITERAL_LONG2s.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/two_LITERAL_LONG2s.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in two_LITERAL_LONG2s.nt" )

    output.close()
  }

  "The input file ./TurtleTests/underscore_in_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/underscore_in_localName.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/underscore_in_localName.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in underscore_in_localName.nt" )

    output.close()
  }
}
