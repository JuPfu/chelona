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

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.nt").mkString

    assert( output.toString == nt.toString )

    output.close()
  }

  "The input file ./TurtleTests/IRIREF_datatype.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRIREF_datatype.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRIREF_datatype.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in IRIREF_datatype.nt" )

    output.close()
  }

  "The input file ./TurtleTests/IRI_with_all_punctuation.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in IRI_with_all_punctuation.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_all_controls.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_all_controls.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_all_controls.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_all_controls.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_all_punctuation.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_all_punctuation.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL1_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL1_ascii_boundaries.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL2_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL2_ascii_boundaries.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_ascii_boundaries.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_1_squote.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_1_squote.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG1_with_2_squotes.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_ascii_boundaries.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_1_squote.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_1_squote.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_2_squotes.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_LONG2_with_REVERSE_SOLIDUS.nt" )

    output.close()
  }

  "The input file ./TurtleTests/LITERAL_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/LITERAL_with_UTF8_boundaries.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/LITERAL_with_UTF8_boundaries.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in LITERAL_with_UTF8_boundaries.nt" )

    output.close()
  }

  "The input file ./TurtleTests/bareword_a_predicate.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_a_predicate.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_a_predicate.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in bareword_a_predicate.nt" )

    output.close()
  }

  "The input file ./TurtleTests/bareword_decimal.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_decimal.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_decimal.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in bareword_decimal.nt" )

    output.close()
  }

  "The input file ./TurtleTests/bareword_double.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/bareword_double.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/bareword_double.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in bareword_double.nt" )

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_as_object.nt" )

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_as_subject.nt" )

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_containing_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_containing_collection.nt" )

    output.close()
  }

  "The input file ./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in blankNodePropertyList_with_multiple_triples.nt" )

    output.close()
  }

  "The input file ./TurtleTests/collection_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/collection_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/collection_object.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in collection_object.nt" )

    output.close()
  }

  "The input file ./TurtleTests/collection_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/collection_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of triples generated should have been 3")

    val nt = io.Source.fromFile("./TurtleTests/collection_subject.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in collection_subject.nt" )

    output.close()
  }

  "The input file ./TurtleTests/comment_following_PNAME_NS.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in comment_following_PNAME_NS.nt" )

    output.close()
  }

  "The input file ./TurtleTests/double_lower_case_e.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/double_lower_case_e.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/double_lower_case_e.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in double_lower_case_e.nt" )

    output.close()
  }

  "The input file ./TurtleTests/empty_collection.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/empty_collection.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of triples generated should have been 2")

    val nt = io.Source.fromFile("./TurtleTests/empty_collection.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in empty_collection.nt" )

    output.close()
  }

  "The input file ./TurtleTests/first.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/first.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/first.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in first.nt" )

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_object.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_object.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_object.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_object.nt" )

    output.close()
  }

  "The input file ./TurtleTests/labeled_blank_node_subject.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in labeled_blank_node_subject.nt" )

    output.close()
  }

  "The input file ./TurtleTests/langtagged_LONG_with_subtag.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in langtagged_LONG_with_subtag.nt" )

    output.close()
  }

  "The input file ./TurtleTests/langtagged_non_LONG.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/langtagged_non_LONG.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/langtagged_non_LONG.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in langtagged_non_LONG.nt" )

    output.close()
  }

  "The input file ./TurtleTests/lantag_with_subtag.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/lantag_with_subtag.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/lantag_with_subtag.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in lantag_with_subtag.nt" )

    output.close()
  }

  "The input file ./TurtleTests/last.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/last.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of triples generated should have been 7")

    val nt = io.Source.fromFile("./TurtleTests/last.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in last.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_false.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_false.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_false.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_false.nt" )

    output.close()
  }

  "The input file ./TurtleTests/literal_true.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/literal_true.ttl").mkString

    val output = new StringWriter()

    val parser = ChelonaParser(input, output, false, true)

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

    val nt = io.Source.fromFile("./TurtleTests/literal_true.nt").mkString

    assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_true.nt" )

    output.close()
  }
}
