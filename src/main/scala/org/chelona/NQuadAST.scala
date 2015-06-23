package org.chelona

trait NQuadAST[NQuadAST <: NTripleAST] extends NTripleAST {
  case class ASTStatement(subject: NQuadAST, predicate: NQuadAST, `object`: NQuadAST, graph: Option[ASTGraphLabel], comment: Option[ASTComment]) extends NTripleAST
  case class ASTGraphLabel(rule: NQuadAST) extends NTripleAST
}
