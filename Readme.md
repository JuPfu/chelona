**cheló̱na**  A Parboiled2 Based Scala Parser for the W3C RDF 1.1 Turtle Language

Introduction
============

*Cheló̱na* (χελώνα) is the greek word for turtle. At the time working on this software I spent a beautiful holiday on the island of Rhodos, so this seemed to be an appropriate name for a turtle parser.

With *Cheló̱na* you can
- validate the syntax and semantic of a W3C RDF 1.1 Turtle file
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

    _:c1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#first> "1"^^<http://www.w3.org/2001/XMLSchema#integer> .
    _:c1 <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> _:c2 .
    _:c2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#first> "2.0"^^<http://www.w3.org/2001/XMLSchema#decimal> .
    _:c2 <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> _:c3 .
    _:c3 <http://www.w3.org/1999/02/22-rdf-syntax-ns#first> "3E1"^^<http://www.w3.org/2001/XMLSchema#double> .
    _:c3 <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> <http://www.w3.org/1999/02/22-rdf-syntax-ns#nil> .
    _:c1 <http://example.org/stuff/1.0/p> "w" .

Installation
============
	
Create a *Cheló̱na* JAR with all dependencies
--------------------------------------------

The *sbt-assembly* plugin located at https://github.com/sbt/sbt-assembly is used to create a *Cheló̱na* JAR containing all dependencies.
Move into the *Cheló̱na* directory.
From the command line type 

    sbt assembly
	
This should generate an archive

    target/scala-2.11/chelona-assembly-x.x.x.jar

where x.x.x denotes the version information, e.g. chelona-assembly-0.9.0.jar.

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

    scala -cp ./target/scala-2.11/chelona-assembly-0.9.0.jar org.chelona.Main --verbose ./examples/example1.ttl > example1_n3.ttl

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

When passing the parameter '-v' or '--validate' on the command line, *Cheló̱na* will do a syntax check. No output file is generated.

    scala -cp ./target/scala-2.11/chelona-assembly-0.9.0.jar org.chelona.Main --validate --verbose examples/example1.ttl

	Validate: examples/example1.ttl
    Input file 'examples/example1.ttl' composed of 7 statements successfully validated in 0.099sec (statements per second = 71)

Unique blank node names
-----------------------

For sake of convenience the next examples assume that an alias chelona has been created.

    alias chelona="scala -cp ./target/scala-2.11/chelona-assembly-0.9.0.jar org.chelona.Main"

The '--uid' command line argument instructs *Cheló̱na* to use a unique identifier for blank nodes.

    chelona --uid examples/example23.ttl

    _:c83cecd897cf243a9a6203bad3f5f0b411 <http://www.w3.org/1999/02/22-rdf-syntax-ns#first> "1"^^<http://www.w3.org/2001/XMLSchema#integer> .
    _:c83cecd897cf243a9a6203bad3f5f0b411 <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> _:c83cecd897cf243a9a6203bad3f5f0b412 .
    _:c83cecd897cf243a9a6203bad3f5f0b412 <http://www.w3.org/1999/02/22-rdf-syntax-ns#first> "2.0"^^<http://www.w3.org/2001/XMLSchema#decimal> .
    _:c83cecd897cf243a9a6203bad3f5f0b412 <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> _:c83cecd897cf243a9a6203bad3f5f0b413 .
    _:c83cecd897cf243a9a6203bad3f5f0b413 <http://www.w3.org/1999/02/22-rdf-syntax-ns#first> "3E1"^^<http://www.w3.org/2001/XMLSchema#double> .
    _:c83cecd897cf243a9a6203bad3f5f0b413 <http://www.w3.org/1999/02/22-rdf-syntax-ns#rest> <http://www.w3.org/1999/02/22-rdf-syntax-ns#nil> .
    _:c83cecd897cf243a9a6203bad3f5f0b411 <http://example.org/stuff/1.0/p> "w" .

Base definition
---------------

Relative IRIs like <#green-goblin> are resolved relative to the current base IRI. When no base is defined, the value passed by the
'--base' parameter is prepended to the relative IRI.
File 'base.ttl' consist of the single triple statement with two relative IRIs.

    <#green-goblin> a <#comic-hero> .

The relative IRIs are prepended to the "http://marvel/universe" string.

    chelona --base "http://marvel/universe" base.ttl

    <http://marvel/universe/#green-goblin> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://marvel/universe/#comic-hero> .

With $PWD resolving to "/Users/jp/chelona" the '--base' parameter produces a prefix "<file:///Users/jp/chelona/" which is prepended to the relative IRI.

    chelona --base "file://"$PWD base.ttl

    <file:///Users/jp/chelona/#green-goblin> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <file:///Users/jp/chelona/#comic-hero> .

Error handling
--------------

In case of an error *Cheló̱na* will display an error message and give a hint where the problem occurred.

    chelona --validate --verbose TurtleTests/turtle-syntax-bad-struct-02.ttl

    Validate: TurtleTests/turtle-syntax-bad-struct-02.ttl
    File 'TurtleTests/turtle-syntax-bad-struct-02.ttl': Invalid input '=', expected predicate or http://www.w3.org/1999/02/22-rdf-syntax-ns#type (line 2, column 40):
    <http://www.w3.org/2013/TurtleTests/s> = <http://www.w3.org/2013/TurtleTests/o> .

Some internal parsing information will be emitted in case of an error when "--trace" is used as command line argument.

    chelona --validate --verbose TurtleTests/turtle-syntax-bad-struct-02.ttl

    Validate: TurtleTests/turtle-syntax-bad-struct-02.ttl
    File 'TurtleTests/turtle-syntax-bad-struct-02.ttl': Invalid input '=', expected IRIREF or prefixedName (line 2, column 40):
    <http://www.w3.org/2013/TurtleTests/s> = <http://www.w3.org/2013/TurtleTests/o> .
                                           ^

    5 rules mismatched at error location:
      ...oc/ *:-58 /statement/ |:-39 /triples/ |:-39 /predicateObjectList/ + /po/ /verb/ | /predicate/ /iri/ | /IRIREF/ atomic / '<'
      ...| /predicate/ /iri/ | /prefixedName/ | /PNAME_LN/ /PNAME_NS/ ? /PN_PREFIX/ atomic / capture / PN_CHARS_BASE:<CharPredicate>
      ... |:-39 /triples/ |:-39 /predicateObjectList/ + /po/ /verb/ | /predicate/ /iri/ | /prefixedName/ | /PNAME_LN/ /PNAME_NS/ ':'
      ...po/ /verb/ | /predicate/ /iri/ | /prefixedName/ | /PNAME_NS/ ? /PN_PREFIX/ atomic / capture / PN_CHARS_BASE:<CharPredicate>
      .../statement/ |:-39 /triples/ |:-39 /predicateObjectList/ + /po/ /verb/ | /predicate/ /iri/ | /prefixedName/ | /PNAME_NS/ ':'

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

