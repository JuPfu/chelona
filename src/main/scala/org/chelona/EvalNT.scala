package org.chelona

import org.chelona.EvalNT._

object EvalNT {
  def apply(basePath: String, label: String) = new EvalNT(basePath, label)

  sealed trait NTReturnValue

  case class NTString(s: String) extends NTReturnValue

  case class NTTriple(s: String, p: String, o: String) extends NTReturnValue

  case class NTComment(value: String) extends NTReturnValue
}

class EvalNT(basePath: String, label: String) {

  import org.chelona.NTriplesParser._

  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]

  var bCount = 0

  def renderStatement(ast: NTripleType, writer: (String, String, String) ⇒ Int): Int = {
    (evalStatement(ast): @unchecked) match {
      case NTTriple(s, p, o) ⇒ writer(s, p, o)
      case NTString(s)       ⇒ 0
      case NTComment(c)      ⇒ 0
    }
  }

  def evalStatement(expr: NTripleType): NTReturnValue = {
    expr match {
      case ASTTriple(subject, predicate, obj, comment) ⇒
        comment match { case Some(comment) ⇒ evalStatement(comment); case None ⇒ }
        ((evalStatement(subject), evalStatement(predicate), evalStatement(obj)): @unchecked) match {
          case (NTString(s1), NTString(p1), NTString(o1)) ⇒ NTTriple(s1, p1, o1)
        }
      case ASTTripleComment(rule) ⇒ evalStatement(rule)
      case ASTSubject(rule)       ⇒ evalStatement(rule)
      case ASTPredicate(rule)     ⇒ evalStatement(rule)
      case ASTObject(rule)        ⇒ evalStatement(rule)
      case ASTLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case NTString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIriRef(v) ⇒ NTString(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case NTString(s) ⇒ s
            }))
            case ASTLangTag(v) ⇒ NTString(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case NTString(s) ⇒ s
            }))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)            ⇒ NTString(token)
      case ASTIriRef(token)             ⇒ NTString("<" + token + ">")
      case ASTStringLiteralQuote(token) ⇒ NTString("\"" + token + "\"")
      case ASTBlankNodeLabel(token)     ⇒ NTString(setBlankNodeName("_:" + token))
      case ASTComment(token)            ⇒ NTComment(token)
      case ASTBlankLine(token)          ⇒ NTComment(token)
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
