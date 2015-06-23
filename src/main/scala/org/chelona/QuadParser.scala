package org.chelona

import java.io.Writer

import org.chelona.QuadParser.NQAST

import org.parboiled2._

object QuadParser extends NQuadAST[NTripleAST] {

  def apply(input: ParserInput, output: Writer, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") = {
    new QuadParser(input, output, validate, basePath, label)
  }

  def quadWriter(bo: Writer)(s: String, p: String, o: String, g: String): Int = {
    bo.write(s + " " + p + " " + o + (if (g != "") " " + g + " .\n" else " .\n")); 1
  }

  sealed trait NQAST extends NQuadAST[NTripleAST]
}

class QuadParser(input: ParserInput, output: Writer, validate: Boolean = false, basePath: String = "http://chelona.org", label: String = "") extends NTriplesParser(input: ParserInput, output, validate, basePath, label) with NQAST {

  import org.chelona.QuadParser.quadWriter

  val quad = new EvalQuad(basePath, label)

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
