**cheló̱na**  A Parboiled2 Based Scala Parser for the W3C RDF 1.1 Turtle Language

Introduction
============

*Cheló̱na* (χελώνα) is the greek word for turtle. At the time working on this software I spent a beautiful holiday on the island of Rhodos, so this seemed to be an appropriate name for a turtle parser.

With *Cheló̱na* you can
- validate the syntax and semantic of a W3C RDF 1.1 turtle file
- convert a turtle file into its canonical triple form
- convert a turtle file into another format, e.g. JSON

First Example
=============

A simple turtle file taken from the W3C recommendation found at http://www.w3.org/TR/turtle/ :

    @prefix foaf: <http://xmlns.com/foaf/0.1/> .

    _:alice foaf:knows _:bob .
    _:bob foaf:knows _:alice .

*Cheló̱na* dissolves the statements into the canonical form, which is represented by pure subject, predicate and object components:

    _:alice <http://xmlns.com/foaf/0.1/knows> _:bob .
    _:bob <http://xmlns.com/foaf/0.1/knows> _:alice .
	
Second Example
==============

The next turtle file is listed as example 15 in the W3C RDF 1.1 Turtle Definition document at http://www.w3.org/TR/turtle :

    @prefix foaf: <http://xmlns.com/foaf/0.1/> .

    # Someone knows someone else, who has the name "Bob".
    [] foaf:knows [ foaf:name "Bob" ] .

*Cheló̱na* transforms the statements into the simple subject-predicate-object (s-p-o) format:

    _:a1 <http://xmlns.com/foaf/0.1/knows> _:b1 .
    _:b1 <http://xmlns.com/foaf/0.1/name> "Bob" .
	
Third Example
============

This is example 23 from the W3C RDF 1.1 Terse RDF Triple Language definition

	@prefix : <http://example.org/stuff/1.0/> .
	(1 2.0 3E1) :p "w" .
	
*Cheló̱na* resolves the collection and gives the equivalent sequence of turtle statements in the canonical form:

	_:c0 rdf:first 1 .
	_:c0 rdf:rest _:c1 .
	_:c1 rdf:first 2.0 .
	_:c1 rdf:rest _:c2 .
	_:c2 rdf:first 3E1 .
	_:c2 rdf:rest rdf:nil .
	_:c0 <http://example.org/stuff/1.0/p> "w" .

Convert a TTL-File from SBT
===========================

Start sbt from the local chelona directory and add a path to the sbt run command to convert a ttl-file.

    sbt

    run "./testfiles/example1.ttl"

The output generated should be something like this

    Convert:./testfiles/example1.ttl
    Number of triples written to file './testfiles/out.ttl': 8
    [success] Total time: 0 s, completed 02.12.2014 21:48:56

	
What *Cheló̱na* does in detail:
==============================
- parses the ttl file
- reports syntax errors
- builds an abstract syntax tree
- resolves prefix declarations
- transforms each turtle statment into the canonical subject-predicate-object (s-p-o) format
- unescapes numeric and string escape sequences in string-literal productions (to be done)
- unescapes numeric escape sequences in Iriref productions (to be done)

Installation
============

To be done

License
=======

*Cheló̱na* is released under the `Apache License 2.0`

http://en.wikipedia.org/wiki/Apache_license

