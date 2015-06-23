package org.chelona

import org.chelona.EvalQuad._

object EvalQuad {
  def apply(basePath: String, label: String) = new EvalQuad(basePath, label)

  sealed trait SPOReturnValue

  case class SPOString(s: String) extends SPOReturnValue

  case class SPOQuad(s: String, p: String, o: String, g: String) extends SPOReturnValue

  case class SPOComment(value: String) extends SPOReturnValue
}

class EvalQuad(basePath: String, label: String) {

  import org.chelona.QuadParser._

  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]

  var bCount = 0

  def renderStatement(ast: NTripleAST, writer: (String, String, String, String) ⇒ Int): Int = {
    (evalStatement(ast): @unchecked) match {
      case SPOQuad(s, p, o, g) ⇒ writer(s, p, o, g)
      case SPOString(s)        ⇒ 0
      case SPOComment(c)       ⇒ 0
    }
  }

  def evalStatement(expr: NTripleAST): SPOReturnValue = {
    expr match {
      case ASTStatement(subject, predicate, obj, graph, comment) ⇒
        comment match { case Some(c) ⇒ evalStatement(c); case None ⇒ }
        graph match {
          case Some(g) ⇒
            ((evalStatement(subject), evalStatement(predicate), evalStatement(obj), evalStatement(g)): @unchecked) match {
              case (SPOString(s1), SPOString(p1), SPOString(o1), SPOString(g1)) ⇒ SPOQuad(s1, p1, o1, g1)
            }
          case None ⇒
            ((evalStatement(subject), evalStatement(predicate), evalStatement(obj)): @unchecked) match {
              case (SPOString(s1), SPOString(p1), SPOString(o1)) ⇒ SPOQuad(s1, p1, o1, "")
            }
        }
      case ASTTripleComment(rule) ⇒ evalStatement(rule)
      case ASTSubject(rule)       ⇒ evalStatement(rule)
      case ASTPredicate(rule)     ⇒ evalStatement(rule)
      case ASTObject(rule)        ⇒ evalStatement(rule)
      case ASTLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case SPOString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIriRef(v) ⇒ SPOString(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case SPOString(s) ⇒ s
            }))
            case ASTLangTag(v) ⇒ SPOString(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case SPOString(s) ⇒ s
            }))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)            ⇒ SPOString(token)
      case ASTIriRef(token)             ⇒ SPOString("<" + token + ">")
      case ASTStringLiteralQuote(token) ⇒ SPOString("\"" + token + "\"")
      case ASTBlankNodeLabel(token)     ⇒ SPOString(setBlankNodeName("_:" + token))
      case ASTComment(token)            ⇒ SPOComment(token)
      case ASTBlankLine(token)          ⇒ SPOComment(token)
    }
  }

  private def setBlankNodeName(key: String) = {
    if (!blankNodeMap.contains(key)) {
      bCount += 1
      blankNodeMap += key -> ("_:b" + label + bCount)
    }
    blankNodeMap.getOrElse(key, "This should never be returned")
  }
}
