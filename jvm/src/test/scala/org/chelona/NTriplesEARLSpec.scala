/*
* Copyright (C) 2015-2016 Juergen Pfundt
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

//import org.chelona.{ EvalNT, NTriplesParser, RDFNTOutput }
import org.parboiled2.{ ParseError, ParserInput }
import org.scalatest.FlatSpec

import scala.util.Failure

class NTriplesEARLSpec extends FlatSpec with RDFNTOutput {

    val earl = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( "./ntriplesearl.nt" ), StandardCharsets.UTF_8 ) )

    def earlOut( testcase: String, passed: Boolean ) = {
        System.err.flush()
        val assertedBy = "<https://github.com/JuPfu#me>"
        val subject = "<https://github.com/JuPfu/chelona>"
        val test = testcase // "IRI_subject"
        val outcome = if ( passed ) "passed" else "failed"
        val datum = Calendar.getInstance.getTime
        val datum_format = new java.text.SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS" )
        val mode = "automatic"
        val earl_assertion = s"""[ a earl:Assertion;\n  earl:assertedBy ${assertedBy};\n  earl:subject ${subject};\n  earl:test <http://www.w3.org/2013/N-TriplesTests/manifest.ttl#${test}>;\n  earl:result [\n    a earl:TestResult;\n    earl:outcome earl:${outcome};\n    dc:date "${datum_format.format( datum )}"^^xsd:dateTime];\n  earl:mode earl:${mode} ] .\n"""
        earl.write( earl_assertion );
        earl.flush()
    }

    "The input file ./NTripleTests/nt-syntax-file-02.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // Only comment
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-file-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 0 ), "Number of triples generated should have been 0" )

        earlOut( "nt-syntax-file-02", parser.ntriplesDoc.run() == scala.util.Success( 0 ) )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-string-02.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // mismatching string literal open/close (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-string-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-02.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-02.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-string-02", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-string-04.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // long single string literal (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-string-04.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-04.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-04.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-string-04", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-datatypes-02.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // integer as xsd:string
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-datatypes-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-datatypes-02.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-datatypes-02.nt" )
        earlOut( "nt-syntax-datatypes-02", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-num-03.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // no numbers in N-Triples (float) (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-num-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-num-03.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-num-03.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-num-03", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-base-01.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // @base not allowed in N-Triples (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-base-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-base-01.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-base-01.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-base-01", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-datatypes-01.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // xsd:byte literal
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-datatypes-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-datatypes-01.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-datatypes-01.nt" )
        earlOut( "nt-syntax-datatypes-01", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-uri-01.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // Only IRIs
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-uri-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-uri-01.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-uri-01.nt" )
        earlOut( "nt-syntax-uri-01", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-file-03.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // One comment, one empty line
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-file-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 0 ), "Number of triples generated should have been 0" )

        earlOut( "nt-syntax-file-03", parser.ntriplesDoc.run() == scala.util.Success( 0 ) )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-esc-01.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad string escape (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-esc-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-esc-01.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-esc-01.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-esc-01", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-03.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : bad long escape (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-03.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-03.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-03", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_REVERSE_SOLIDUS2.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // REVERSE SOLIDUS at end of literal
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_REVERSE_SOLIDUS2.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_REVERSE_SOLIDUS2.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_REVERSE_SOLIDUS2.nt" )
        earlOut( "literal_with_REVERSE_SOLIDUS2", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-string-03.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // langString literal with region
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-string-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-string-03.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-string-03.nt" )
        earlOut( "nt-syntax-string-03", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-lang-01.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // langString with bad lang (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-lang-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-lang-01.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-lang-01.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-lang-01", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_all_punctuation.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal_all_punctuation '!"#$%&()...'
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_all_punctuation.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_all_punctuation.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_all_punctuation.nt" )
        earlOut( "literal_all_punctuation", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bnode-02.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // bnode object
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bnode-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 2 ), "Number of triples generated should have been 2" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-bnode-02-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-bnode-02.nt" )
        earlOut( "nt-syntax-bnode-02", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-esc-02.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad string escape (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-esc-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-esc-02.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-esc-02.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-esc-02", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_ascii_boundaries.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal_ascii_boundaries '\x00\x26\x28...'
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_ascii_boundaries.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_ascii_boundaries.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_ascii_boundaries.nt" )
        earlOut( "literal_ascii_boundaries", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-string-02.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // langString literal
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-string-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-string-02.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-string-02.nt" )
        earlOut( "nt-syntax-string-02", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_2_squotes.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with 2 squotes "x''y"
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_2_squotes.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_2_squotes.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_2_squotes.nt" )
        earlOut( "literal_with_2_squotes", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-string-01.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // string literal
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-string-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-string-01.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-string-01.nt" )
        earlOut( "nt-syntax-string-01", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-prefix-01.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // @prefix not allowed in n-triples (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-prefix-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-prefix-01.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-prefix-01.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-prefix-01", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-01.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : space (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-01.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-01.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-01", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_CARRIAGE_RETURN.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with CARRIAGE RETURN
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_CARRIAGE_RETURN.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_CARRIAGE_RETURN.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_CARRIAGE_RETURN.nt" )
        earlOut( "literal_with_CARRIAGE_RETURN", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/langtagged_string.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // langtagged string "x"@en
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/langtagged_string.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/langtagged_string.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in langtagged_string.nt" )
        earlOut( "langtagged_string", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-struct-02.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // N-Triples does not have predicateObjectList (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-struct-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-struct-02.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-struct-02.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-struct-02", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-string-03.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // single quotes (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-string-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-03.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-03.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-string-03", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_LINE_FEED.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with LINE FEED
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_LINE_FEED.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_LINE_FEED.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_LINE_FEED.nt" )
        earlOut( "literal_with_LINE_FEED", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-num-01.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // no numbers in N-Triples (integer) (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-num-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-num-01.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-num-01.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-num-01", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-num-02.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // no numbers in N-Triples (decimal) (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-num-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-num-02.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-num-02.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-num-02", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-str-esc-03.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // string literal with long Unicode escape
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-str-esc-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-str-esc-03-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-str-esc-03.nt" )
        earlOut( "nt-syntax-str-esc-03", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/literal_all_controls.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal_all_controls '\x00\x01\x02\x03\x04...'
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_all_controls.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_all_controls-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_all_controls.nt" )
        earlOut( "literal_all_controls", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_UTF8_boundaries.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal_with_UTF8_boundaries '\x80\x7ff\x800\xfff...'
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_UTF8_boundaries.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_UTF8_boundaries.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_UTF8_boundaries.nt" )
        earlOut( "literal_with_UTF8_boundaries", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-07.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : relative IRI not allowed in predicate (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-07.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-07.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-07.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-07", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_dquote.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with dquote "x"y"
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_dquote.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_dquote.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_dquote.nt" )
        earlOut( "literal_with_dquote", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-04.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : character escapes not allowed (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-04.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-04.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-04.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-04", !res )

        output.close()
    }

    "The input file ./NTripleTests/comment_following_triple.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // Tests comments after a triple
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/comment_following_triple.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 5 ), "Number of triples generated should have been 5" )

        val nt = io.Source.fromFile( "./NTriplesTests/comment_following_triple-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in comment_following_triple.nt" )
        earlOut( "comment_following_triple", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_FORM_FEED.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with FORM FEED
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_FORM_FEED.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_FORM_FEED.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_FORM_FEED.nt" )
        earlOut( "literal_with_FORM_FEED", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-str-esc-02.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // string literal with Unicode escape
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-str-esc-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-str-esc-02-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-str-esc-02.nt" )
        earlOut( "nt-syntax-str-esc-02", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-string-01.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // mismatching string literal open/close (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-string-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-01.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-01.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-string-01", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_numeric_escape4.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with numeric escape4 \\u
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_numeric_escape4.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_numeric_escape4-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_numeric_escape4.nt" )
        earlOut( "literal_with_numeric_escape4", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-05.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : character escapes not allowed (2) (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-05.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-05.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-05.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-05", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_squote.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with squote "x'y"
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_squote.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_squote.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_squote.nt" )
        earlOut( "literal_with_squote", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-file-01.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // Empty file
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-file-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 0 ), "Number of triples generated should have been 0" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-file-01.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-file-01.nt" )
        earlOut( "nt-syntax-file-01", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-esc-03.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad string escape (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-esc-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-esc-03.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-esc-03.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-esc-03", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_REVERSE_SOLIDUS.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with REVERSE SOLIDUS
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_REVERSE_SOLIDUS.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_REVERSE_SOLIDUS.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_REVERSE_SOLIDUS.nt" )
        earlOut( "literal_with_REVERSE_SOLIDUS", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/lantag_with_subtag.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // lantag with subtag "x"@en-us
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/lantag_with_subtag.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/lantag_with_subtag.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in lantag_with_subtag.nt" )
        earlOut( "lantag_with_subtag", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_BACKSPACE.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with BACKSPACE
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_BACKSPACE.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_BACKSPACE.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_BACKSPACE.nt" )
        earlOut( "literal_with_BACKSPACE", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-02.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : bad escape (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-02.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-02.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-02", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bnode-03.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // Blank node labels may start with a digit
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bnode-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 2 ), "Number of triples generated should have been 2" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-bnode-03-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-bnode-03.nt" )
        earlOut( "nt-syntax-bnode-03", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_2_dquotes.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with 2 squotes """a""b"""
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_2_dquotes.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_2_dquotes.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_2_dquotes.nt" )
        earlOut( "literal_with_2_dquotes", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-string-07.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // string literal with no start (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-string-07.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-07.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-07.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-string-07", !res )

        output.close()
    }

    "The input file ./NTripleTests/minimal_whitespace.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // tests absense of whitespace between subject, predicate, object and end-of-statement
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/minimal_whitespace.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 6 ), "Number of triples generated should have been 6" )

        val nt = io.Source.fromFile( "./NTriplesTests/minimal_whitespace-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in minimal_whitespace.nt" )
        earlOut( "minimal_whitespace", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-struct-01.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // N-Triples does not have objectList (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-struct-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-struct-01.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-struct-01.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-struct-01", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-subm-01.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // Submission test from Original RDF Test Cases
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-subm-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 30 ), "Number of triples generated should have been 30" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-subm-01-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-subm-01.nt" )
        earlOut( "nt-syntax-subm-01", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-uri-04.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // Legal IRIs
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-uri-04.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-uri-04-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-uri-04.nt" )
        earlOut( "nt-syntax-uri-04", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-08.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : relative IRI not allowed in object (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-08.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-08.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-08.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-08", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bnode-01.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // bnode subject
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bnode-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-bnode-01-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-bnode-01.nt" )
        earlOut( "nt-syntax-bnode-01", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-string-06.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // string literal with no end (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-string-06.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-06.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-06.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-string-06", !res )

        output.close()
    }

    "The input file ./NTripleTests/literal.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal """x"""
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal.nt" )
        earlOut( "literal", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-uri-03.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // IRIs with long Unicode escape
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-uri-03.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-uri-03-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-uri-03.nt" )
        earlOut( "nt-syntax-uri-03", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_CHARACTER_TABULATION.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with CHARACTER TABULATION
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_CHARACTER_TABULATION.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_CHARACTER_TABULATION.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_CHARACTER_TABULATION.nt" )
        earlOut( "literal_with_CHARACTER_TABULATION", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-string-05.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // long double string literal (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-string-05.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-05.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-string-05.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-string-05", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-str-esc-01.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // string literal with escaped newline
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-str-esc-01.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-str-esc-01.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-str-esc-01.nt" )
        earlOut( "nt-syntax-str-esc-01", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/literal_with_numeric_escape8.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // literal with numeric escape8 \U
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/literal_with_numeric_escape8.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/literal_with_numeric_escape8-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in literal_with_numeric_escape8.nt" )
        earlOut( "literal_with_numeric_escape8", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-uri-02.nt" must "succeed" taggedAs ( TestNTriplesPositiveSyntax ) in {
        // IRIs with Unicode escape
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-uri-02.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        assert( parser.ntriplesDoc.run() == scala.util.Success( 1 ), "Number of triples generated should have been 1" )

        val nt = io.Source.fromFile( "./NTriplesTests/nt-syntax-uri-02-isomorphic.nt" ).mkString

        assert( output.toString == nt.toString, "Triples generated should be exactly as in nt-syntax-uri-02.nt" )
        earlOut( "nt-syntax-uri-02", output.toString == nt.toString )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-09.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : relative IRI not allowed in datatype (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-09.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-09.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-09.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-09", !res )

        output.close()
    }

    "The input file ./NTripleTests/nt-syntax-bad-uri-06.nt" must "fail" taggedAs ( TestNTriplesNegativeSyntax ) in {
        // Bad IRI : relative IRI not allowed in subject (negative test)
        lazy val input: ParserInput = io.Source.fromFile( "./NTriplesTests/nt-syntax-bad-uri-06.nt" ).mkString

        val output = new StringWriter()

        val evalNT = new EvalNTriples( ntripleWriter( output )_, "http://www.w3.org/2013/NTriplesTests", "" )

        val parser = NTriplesParser( input, evalNT.renderStatement, false, "http://www.w3.org/2013/NTriplesTests", "" )

        val res = parser.ntriplesDoc.run() match {
            case scala.util.Success( tripleCount ) ⇒
                true
            case Failure( e: ParseError ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-06.nt': " + parser.formatError( e ) )
                false
            case Failure( e ) ⇒
                System.err.println( "File './NTriplesTests/nt-syntax-bad-uri-06.nt': Unexpected error during parsing run: " + e )
                false
        }
        earlOut( "nt-syntax-bad-uri-06", !res )

        output.close()
    }

}
