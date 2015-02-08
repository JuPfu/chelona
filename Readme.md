**cheló̱na**  A Parboiled2 Based Scala Parser for the W3C RDF 1.1 Turtle Language

Introduction
============

*Cheló̱na* (χελώνα) is the greek word for turtle. At the time working on this software I spent a beautiful holiday on the island of Rhodos, so this seemed to be an appropriate name for a turtle parser.

With *Cheló̱na* you can
- validate the syntax and semantic of a W3C RDF 1.1 turtle file
- convert a turtle file into its canonical triple form
- convert a turtle file into another format, e.g. JSON (to be done)

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

Installation
============
	
Create a *Cheló̱na* JAR with all dependencies
--------------------------------------------

The *sbt-assembly* plugin is used to create a *Cheló̱na* JAR containing all dependencies. Move into the *Cheló̱na* directory.
From the command line type 

    sbt assembly
	
This should generate an archive

    target/scala-2.11/chelona-assembly-x.x.x.jar

where x.x.x denotes the version information, e.g. chelona-assembly-0.8.0.jar.

Running *Cheló̱na* from the command line
----------------------------------------

Conversion of the example1.ttl file from the examples directory into the simple S-P-O Turtle format (N3)

	<#green-goblin> rel:enemyOf    <#spiderman> 	;
	    a foaf:Person ;    # in the context of the Marvel universe
	    foaf:name "Green Goblin" ;
		foaf:mail "GreenGoblin@marvel.com" .

	<#spiderman>
	    rel:enemyOf <#green-goblin> ;
	    a foaf:Person ;
	    foaf:name "Spiderman", "Человек-паук"@ru .

is done with the command shown here: 

    scala -cp ./target/scala-2.11/chelona-assembly-0.8.0.jar org.chelona.Main ./examples/example1.ttl > example1_n3.ttl

The output generated lists the name of the output file and the number of generated triples:

	Convert: ./examples/example1.ttl
    Input file './examples/example1.ttl' converted in 0.064sec 8 triples (triples per second = 125)
	
Inspecting the output file 'example1_n3.ttl' should give this result:

	<http://example.org/#green-goblin> <http://www.perceive.net/schemas/relationship/enemyOf> <http://example.org/#spiderman> .
	<http://example.org/#green-goblin> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
	<http://example.org/#green-goblin> <http://xmlns.com/foaf/0.1/name> "Green Goblin" .
	<http://example.org/#green-goblin> <http://xmlns.com/foaf/0.1/mail> "GreenGoblin@marvel.com" .
	<http://example.org/#spiderman> <http://www.perceive.net/schemas/relationship/enemyOf> <http://example.org/#green-goblin> .
	<http://example.org/#spiderman> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> .
	<http://example.org/#spiderman> <http://xmlns.com/foaf/0.1/name> "Spiderman" .
	<http://example.org/#spiderman> <http://xmlns.com/foaf/0.1/name> "Человек-паук"@ru .

Validation of a Turtle File
---------------------------

When passing the parameter '-v' or '--validate' on the command line, *Cheló̱na* will do a syntax check. No output file is
 generated.

    scala -cp ./target/scala-2.11/chelona-assembly-0.8.0.jar org.chelona.Main -v ./examples/example1.ttl

	Validate: ./examples/example1.ttl
    Input file './examples/example1.ttl' composed of 7 statements successfully validated in 0.107sec (statements per second = 65)

In case of an error *Cheló̱na* will display an error message and gives a hint where the problem occurred.


What *Cheló̱na* does in detail:
==============================
- parses the ttl file
- reports syntax errors
- builds an abstract syntax tree for each valid turtle statement
- resolves prefix declarations
- unescapes numeric and string escape sequences in string-literal productions
- unescapes numeric escape sequences in Iriref productions for output format 'raw'
- transforms each turtle statment into the canonical subject-predicate-object (s-p-o) format
- skolemisation (Replacing blank nodes with IRIs) (to be done)


License
=======

*Cheló̱na* is released under the `Apache License 2.0`

http://en.wikipedia.org/wiki/Apache_license

