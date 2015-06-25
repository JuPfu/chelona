/*
* Copyright (C) 2014-2015 Juergen Pfundt
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

import java.io.Writer

import org.chelona.NQuadParser.NQAST

import org.parboiled2._

object NQuadParser extends NQuadAST[NTripleAST] {

  def apply(input: ParserInput, output: Writer, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") = {
    new NQuadParser(input, output, validate, basePath, label)
  }

  def quadWriter(bo: Writer)(s: String, p: String, o: String, g: String): Int = {
    bo.write(s + " " + p + " " + o + (if (g != "") " " + g + " .\n" else " .\n")); 1
  }

  sealed trait NQAST extends NQuadAST[NTripleAST]
}

class NQuadParser(input: ParserInput, output: Writer, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") extends NTriplesParser(input: ParserInput, output, validate, basePath, label) with NQAST {

  import org.chelona.NQuadParser.quadWriter

  val quad = new EvalNQuad(basePath, label)

  val quadOutput = quadWriter(output)_

  //[1]	nquadsDoc	::=	statement? (EOL statement)* EOL?
  def nquadsDoc = rule {
    (statement ~> ((ast: NTripleAST) ⇒
      if (!__inErrorAnalysis) {
        if (!validate)
          quad.renderStatement(ast, quadOutput)
        else
          ast match {
            case ASTComment(s) ⇒ 0
            case _             ⇒ 1
          }
      } else 0)).*(EOL) ~ EOL.? ~ EOI ~> ((v: Seq[Int]) ⇒ v.foldLeft(0L)(_ + _))
  }

  //[2]	statement	::=	subject predicate object graphLabel? '.'
  def statement: Rule1[NTripleAST] = rule {
    ws ~ (subject ~ predicate ~ obj ~ graphLabel.? ~ "." ~ comment.? ~> ASTStatement | comment ~> ASTTripleComment) | blank_line ~> ASTBlankLine
  }

  //[6]	graphLabel	::=	IRIREF | BLANK_NODE_LABEL
  def graphLabel = rule {
    (IRIREF | BLANK_NODE_LABEL) ~> ASTGraphLabel
  }
}
