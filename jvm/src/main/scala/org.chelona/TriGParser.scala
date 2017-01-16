/*
* Copyright (C) 2014-2016 Juergen Pfundt
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

import org.parboiled2._

import scala.language.implicitConversions

object TriGParser {

  def apply(input: ParserInput, output: List[RDFReturnType] ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") = {
    new TriGParser(input, output, validate, basePath, label)
  }
}

class TriGParser(input: ParserInput, output: List[RDFReturnType] ⇒ Int, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") extends ChelonaParser(input: ParserInput, output, validate, basePath, label) {

  import TriGAST._

  override lazy val renderStatement = EvalTriG(output, basePath, label).renderStatement _

  //[1] trigDoc 	::= 	statement*
  def trigDoc = rule {
    (statement ~> ((ast: TurtleType) ⇒
      if (!__inErrorAnalysis) {
        if (!validate) {
          asynchronous((renderStatement, ast)); 1
        } else
          ast match {
            case ASTStatement(ASTComment(s)) ⇒ 0
            case _                           ⇒ 1
          }
      } else {
        if (!validate) {
          if (astQueue.nonEmpty) {
            worker.shutdown()
            worker.join()

            while (astQueue.nonEmpty) {
              val (renderStatement, ast) = astQueue.dequeue()
              worker.sum += renderStatement(ast)
            }
          }
        }
        0
      })).* ~ EOI ~> ((v: Seq[Int]) ⇒ {
      if (!validate) {
        worker.shutdown()
        worker.join()

        while (!astQueue.isEmpty) {
          val (renderStatement, ast) = astQueue.dequeue()
          worker.sum += renderStatement(ast)
        }
      }

      worker.quit()

      if (validate) v.sum
      else worker.sum
    })
  }

  //[2] trigDoc 	::= 	directive | block
  override def statement = rule {
    (directive | block | comment) ~> ASTStatement
  }

  //[2g] block	::=	triplesOrGraph | wrappedGraph | triples2 | "GRAPH" labelOrSubject wrappedGraph
  def block: Rule1[TurtleAST] = rule {
    (triplesOrGraph | wrappedGraph | triples2) ~> ASTBlock |
      atomic(ignoreCase("graph")) ~ ws ~ labelOrSubject ~ wrappedGraph ~> ASTLabelOrSubjectBlock
  }

  //[3g]	triplesOrGraph	::=	labelOrSubject (wrappedGraph | predicateObjectList '.')
  def triplesOrGraph = rule {
    labelOrSubject ~ (wrappedGraph | predicateObjectList ~ ".") ~> ASTTriplesOrGraph
  }

  //[4g]	triples2	::=	blankNodePropertyList predicateObjectList? '.' | collection predicateObjectList '.'
  def triples2 = rule {
    (blankNodePropertyList ~ predicateObjectList.? ~> ASTTriple2BlankNodePropertyList |
      collection ~ predicateObjectList ~> ASTTriple2Collection) ~ "."
  }

  //[5g]	wrappedGraph	::=	'{' triplesBlock? '}'
  def wrappedGraph = rule {
    "{" ~ triplesBlock.? ~ "}" ~> ASTWrappedGraph
  }

  //[6g]	triplesBlock	::=	triples ('.' triplesBlock?)?
  def triplesBlock = rule {
    triples.+(('.' ~ ws)) ~ ('.' ~ ws).? ~> ASTTriplesBlock
  }

  //[7g]	labelOrSubject	::=	iri | BlankNode
  def labelOrSubject = rule {
    (iri | blankNode) ~> ASTLabelOrSubject
  }
}

