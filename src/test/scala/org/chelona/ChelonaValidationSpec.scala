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

import org.parboiled2.{ ParseError, ParserInput }
import org.scalatest.FlatSpec

import scala.util.Failure

class ChelonaValidationSpec extends FlatSpec {

  "The input file ./TurtleTests/turtle-syntax-file-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-file-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of statements validated should have been 0")
  }

  "The input file ./TurtleTests/turtle-syntax-file-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-file-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of statements validated should have been 0")
  }

  "The input file ./TurtleTests/turtle-syntax-file-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-file-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(0), "Number of statements validated should have been 0")
  }

  "The input file ./TurtleTests/turtle-syntax-uri-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-uri-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-uri-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-uri-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-uri-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-uri-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-uri-04.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-uri-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-base-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-base-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-base-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-base-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-base-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-base-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-base-04.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-base-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-04.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-05.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-06.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-06.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-07.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-07.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-08.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-08.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-prefix-09.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-prefix-09.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-syntax-string-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-04.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-05.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-06.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-06.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-07.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-07.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-08.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-08.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-09.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-09.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-10.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-10.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-string-11.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-string-11.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-str-esc-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-str-esc-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-pname-esc-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-pname-esc-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-04.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-05.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-06.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-06.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-07.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-07.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-08.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-08.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-09.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-09.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-syntax-bnode-10.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bnode-10.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of statements validated should have been 4")
  }

  "The input file ./TurtleTests/turtle-syntax-number-01.ttl" must "succeed" taggedAs (TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-04.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-05.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-06.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-06.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-07.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-07.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-08.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-08.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-09.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-09.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-number-10.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-10.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }
  "The input file ./TurtleTests/turtle-syntax-number-11.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-number-11.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-datatypes-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-datatypes-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-datatypes-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-syntax-kw-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-kw-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-kw-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-kw-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-syntax-kw-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-kw-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-struct-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-struct-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-struct-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-struct-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-struct-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-struct-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-struct-04.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-struct-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-struct-05.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-struct-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-lists-01.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-lists-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-lists-02.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-lists-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-lists-03.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-lists-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-lists-04.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-lists-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-lists-05.ttl" must "succeed" taggedAs (TestTurtlePositiveSyntax, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-lists-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at line 2, column 1  ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at line 2, column 1  ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at line 2, column 1  ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-04.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at line 2, column 1")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-uri-05.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-uri-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-uri-05.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at line 2, column 1 ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-04.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-prefix-05.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-prefix-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-prefix-05.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-base-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-base-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-base-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-base-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-base-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-base-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-base-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-04.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-05.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-05.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-06.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-06.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-06.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-06.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-07.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-07.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-07.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-07.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-04.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-kw-05.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-kw-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-kw-05.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-04.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-05.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-05.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-05.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-06.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-06.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-06.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-06.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-07.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-07.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-07.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-07.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-08.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-08.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-08.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-08.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-09.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-09.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-09.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-09.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-10.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-10.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-10.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-10.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-11.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-11.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-11.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-11.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-12.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-12.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-12.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-12.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-n3-extras-13.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-n3-extras-13.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-13.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-n3-extras-13.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-08.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-08.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-08.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-08.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-09.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-09.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-09.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-09.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-10.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-10.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-10.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-10.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-11.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-11.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-11.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-11.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-12.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-12.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-12.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-12.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-13.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-13.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-13.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-13.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-14.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-14.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-14.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-14.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-15.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-15.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-15.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-15.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-16.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-16.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-16.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-16.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-struct-17.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-struct-17.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-17.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-struct-17.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-lang-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-lang-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-lang-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-lang-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-esc-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-esc-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-esc-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-esc-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-esc-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-esc-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-esc-04.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-esc-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-esc-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-pname-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-pname-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-pname-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-pname-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-pname-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-pname-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-pname-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-03.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-04.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-04.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-05.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-05.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-05.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-06.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-06.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-06.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-06.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-string-07.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-string-07.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-07.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-string-07.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-01.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-02.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-03.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-03.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-04.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-04.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-num-05.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, TurtleSyntaxBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-num-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-05.ttl': " + e /*parser.formatError(e)*/ )
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-num-05.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-eval-struct-01.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-eval-struct-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-eval-struct-02.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-eval-struct-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/turtle-subm-01.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-02.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(6), "Number of statements validated should have been 6")
  }

  "The input file ./TurtleTests/turtle-subm-03.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-04.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-05.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-05.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-subm-06.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-06.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-subm-07.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-07.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-08.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-08.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-09.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-09.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-10.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-10.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-subm-11.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-11.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of statements validated should have been 5")
  }

  "The input file ./TurtleTests/turtle-subm-12.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-12.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(8), "Number of statements validated should have been 8")
  }

  "The input file ./TurtleTests/turtle-subm-13.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-13.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(7), "Number of statements validated should have been 7")
  }

  "The input file ./TurtleTests/turtle-subm-14.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-14.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-subm-15.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-15.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-16.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-16.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-subm-17.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-17.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-18.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-18.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-subm-19.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-19.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of statements validated should have been 4")
  }

  "The input file ./TurtleTests/turtle-subm-20.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-20.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of statements validated should have been 4")
  }

  "The input file ./TurtleTests/turtle-subm-21.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-21.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-22.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-22.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-subm-23.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-23.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(5), "Number of statements validated should have been 5")
  }

  "The input file ./TurtleTests/turtle-subm-24.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-24.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-subm-25.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-25.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/turtle-subm-26.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-26.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(22), "Number of statements validated should have been 22")
  }

  "The input file ./TurtleTests/turtle-subm-27.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleSyntax) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-subm-27.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(9), "Number of statements validated should have been 9")
  }

  "The input file ./TurtleTests/turtle-eval-bad-01.ttl" must "fail" taggedAs (TestTurtleNegativeEval, TurtleEvalBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-eval-bad-01.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-01.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-01.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-eval-bad-02.ttl" must "fail" taggedAs (TestTurtleNegativeEval, TurtleEvalBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-eval-bad-02.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-02.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-02.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-eval-bad-03.ttl" must "fail" taggedAs (TestTurtleNegativeEval, TurtleEvalBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-eval-bad-03.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-03.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-03.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-eval-bad-04.ttl" must "fail" taggedAs (TestTurtleNegativeEval, TurtleEvalBad) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-eval-bad-04.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-04.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-eval-bad-04.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/IRI_subject.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/IRI_subject.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/IRI_with_four_digit_numeric_escape.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/IRI_with_four_digit_numeric_escape.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/IRI_with_eight_digit_numeric_escape.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/IRI_with_eight_digit_numeric_escape.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/IRI_with_all_punctuation.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/IRI_with_all_punctuation.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/bareword_a_predicate.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/bareword_a_predicate.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/old_style_prefix.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/old_style_prefix.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/SPARQL_style_prefix.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/SPARQL_style_prefix.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/prefixed_IRI_predicate.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/prefixed_IRI_predicate.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/prefixed_IRI_object.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/prefixed_IRI_object.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/prefix_only_IRI.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/prefix_only_IRI.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/prefix_with_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/prefix_with_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/prefix_with_non_leading_extras.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/prefix_with_non_leading_extras.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/default_namespace_IRI.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/default_namespace_IRI.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/prefix_reassigned_and_used.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/prefix_reassigned_and_used.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/reserved_escaped_localName.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/reserved_escaped_localName.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/percent_escaped_localName.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/percent_escaped_localName.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/HYPHEN_MINUS_in_localName.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/underscore_in_localName.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/underscore_in_localName.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/localname_with_COLON.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/localname_with_COLON.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_bmp_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/localName_with_assigned_nfc_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/localName_with_nfc_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/localName_with_leading_underscore.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/localName_with_leading_underscore.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/localName_with_leading_digit.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/localName_with_leading_digit.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/localName_with_non_leading_extras.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/localName_with_non_leading_extras.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/old_style_base.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/old_style_base.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/SPARQL_style_base.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/SPARQL_style_base.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/labeled_blank_node_subject.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/labeled_blank_node_subject.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/labeled_blank_node_object.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/labeled_blank_node_object.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/labeled_blank_node_with_PN_CHARS_BASE_character_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/labeled_blank_node_with_leading_underscore.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_underscore.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/labeled_blank_node_with_leading_digit.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/labeled_blank_node_with_leading_digit.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/labeled_blank_node_with_non_leading_extras.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/labeled_blank_node_with_non_leading_extras.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/anonymous_blank_node_subject.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/anonymous_blank_node_subject.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/anonymous_blank_node_object.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/anonymous_blank_node_object.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/sole_blankNodePropertyList.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/sole_blankNodePropertyList.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_subject.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_subject.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/blankNodePropertyList_as_object.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/blankNodePropertyList_as_object.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/blankNodePropertyList_with_multiple_triples.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/nested_blankNodePropertyLists.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/nested_blankNodePropertyLists.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/blankNodePropertyList_containing_collection.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/blankNodePropertyList_containing_collection.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/collection_subject.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/collection_subject.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/collection_object.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/collection_object.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/empty_collection.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/empty_collection.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/nested_collection.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/nested_collection.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/first.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/first.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/last.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/last.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL1.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL1.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL1_ascii_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL1_ascii_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL1_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL1_with_UTF8_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL1_all_controls.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL1_all_controls.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL1_all_punctuation.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL1_all_punctuation.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG1.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG1.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG1_ascii_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_UTF8_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_1_squote.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_1_squote.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG1_with_2_squotes.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL2.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL2.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL2_ascii_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL2_ascii_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL2_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL2_with_UTF8_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG2.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG2.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG2_ascii_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_UTF8_boundaries.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_UTF8_boundaries.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_1_squote.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_1_squote.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_2_squotes.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_CHARACTER_TABULATION.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_CHARACTER_TABULATION.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_BACKSPACE.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_BACKSPACE.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_LINE_FEED.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_LINE_FEED.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_CARRIAGE_RETURN.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_CARRIAGE_RETURN.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_FORM_FEED.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_FORM_FEED.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_REVERSE_SOLIDUS.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_escaped_CHARACTER_TABULATION.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_escaped_CHARACTER_TABULATION.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_escaped_BACKSPACE.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_escaped_BACKSPACE.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_escaped_LINE_FEED.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_escaped_LINE_FEED.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_escaped_CARRIAGE_RETURN.ttl" must "succeed" taggedAs (TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_escaped_CARRIAGE_RETURN.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_escaped_FORM_FEED.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_escaped_FORM_FEED.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_numeric_escape4.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_numeric_escape4.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_with_numeric_escape8.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_with_numeric_escape8.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/IRIREF_datatype.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/IRIREF_datatype.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/prefixed_name_datatype.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/prefixed_name_datatype.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/bareword_integer.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/bareword_integer.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/bareword_decimal.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/bareword_decimal.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/bareword_double.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/bareword_double.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/double_lower_case_e.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/double_lower_case_e.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/negative_numeric.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/negative_numeric.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/positive_numeric.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/positive_numeric.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/numeric_with_leading_0.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/numeric_with_leading_0.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_true.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_true.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/literal_false.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/literal_false.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/langtagged_non_LONG.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/langtagged_non_LONG.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/langtagged_LONG.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/langtagged_LONG.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/lantag_with_subtag.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/lantag_with_subtag.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/objectList_with_two_objects.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/objectList_with_two_objects.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/predicateObjectList_with_two_objectLists.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/predicateObjectList_with_two_objectLists.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/repeated_semis_at_end.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/repeated_semis_at_end.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/repeated_semis_not_at_end.ttl" must "succeed" taggedAs (TestTurtleEval, TurtleAtomic) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/repeated_semis_not_at_end.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of statements validated should have been 1")
  }

  "The input file ./TurtleTests/comment_following_localName.ttl" must "succeed" taggedAs (TestTurtleEval, JeremyCarroll) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/comment_following_localName.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/number_sign_following_localName.ttl" must "succeed" taggedAs (TestTurtleEval, JeremyCarroll) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/number_sign_following_localName.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/comment_following_PNAME_NS.ttl" must "succeed" taggedAs (TestTurtleEval, JeremyCarroll) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/comment_following_PNAME_NS.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/number_sign_following_PNAME_NS.ttl" must "succeed" taggedAs (TestTurtleEval, JeremyCarroll) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/number_sign_following_PNAME_NS.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl" must "succeed" taggedAs (TestTurtleEval, DaveBeckett) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/LITERAL_LONG2_with_REVERSE_SOLIDUS.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-LITERAL2_with_langtag_and_datatype.ttl" must "fail" taggedAs (TestTurtleNegativeSyntax, DaveBeckett) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-LITERAL2_with_langtag_and_datatype.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-LITERAL2_with_langtag_and_datatype.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-LITERAL2_with_langtag_and_datatype.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/two_LITERAL_LONG2s.ttl" must "succeed" taggedAs (TestTurtleEval, DaveBeckett) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/two_LITERAL_LONG2s.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(3), "Number of statements validated should have been 3")
  }

  "The input file ./TurtleTests/langtagged_LONG_with_subtag.ttl" must "succeed" taggedAs (TestTurtleEval, DaveBeckett) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/langtagged_LONG_with_subtag.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-blank-label-dot-end.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-blank-label-dot-end.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-blank-label-dot-end.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-blank-label-dot-end.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-number-dot-in-anon.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-number-dot-in-anon.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-number-dot-in-anon.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-number-dot-in-anon.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ln-dash-start.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ln-dash-start.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-dash-start.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-dash-start.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ln-escape.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ln-escape.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-escape.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-escape.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ln-escape-start.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ln-escape-start.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-escape-start.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ln-escape-start.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ns-dot-end.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ns-dot-end.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ns-dot-end.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ns-dot-end.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-ns-dot-start.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-ns-dot-start.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ns-dot-start.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-ns-dot-start.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-missing-ns-dot-end.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-missing-ns-dot-end.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-missing-ns-dot-end.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-missing-ns-dot-end.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-bad-missing-ns-dot-start.ttl" must "fail" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-bad-missing-ns-dot-start.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    val res = parser.turtleDoc.run() match {
      case scala.util.Success(tripleCount) ⇒
        true
      case Failure(e: ParseError) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-missing-ns-dot-start.ttl': " + parser.formatError(e))
        false
      case Failure(e) ⇒
        System.err.println("File './TurtleTests/turtle-syntax-bad-missing-ns-dot-start.ttl': Unexpected error during parsing run: " + e)
        false
    }

    assert(res == false, "Validation should have failed at statement ")
  }

  "The input file ./TurtleTests/turtle-syntax-ln-dots.ttl" must "succeed" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-ln-dots.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of statements validated should have been 4")
  }

  "The input file ./TurtleTests/turtle-syntax-ln-colons.ttl" must "succeed" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-ln-colons.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(6), "Number of statements validated should have been 6")
  }

  "The input file ./TurtleTests/turtle-syntax-ns-dots.ttl" must "succeed" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-ns-dots.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(2), "Number of statements validated should have been 2")
  }

  "The input file ./TurtleTests/turtle-syntax-blank-label.ttl" must "succeed" taggedAs (DavidRobillard) in {

    lazy val input: ParserInput = scala.io.Source.fromFile("./TurtleTests/turtle-syntax-blank-label.ttl").mkString

    val parser = ChelonaParser(input, null, true, "http://www.w3.org/2013/TurtleTests", "")

    assert(parser.turtleDoc.run() == scala.util.Success(4), "Number of statements validated should have been 4")
  }
}
