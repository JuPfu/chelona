/*
* Copyright (C) 2014, 2015, 2016 Juergen Pfundt
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

import scala.annotation.tailrec

object EvalTurtle {
  def apply(output: List[RDFReturnType] ⇒ Int, basePath: String, label: String) = new EvalTurtle(output, basePath, label)
}

class EvalTurtle(output: List[RDFReturnType] ⇒ Int, basePath: String, label: String) {

  val prefixMap = scala.collection.mutable.Map.empty[String, String]
  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[Term]
  val predicateStack = scala.collection.mutable.Stack.empty[Term]

  var curSubject = Term("---Not valid subject---", TokenTypes.EMPTY)
  var curPredicate = Term("---Not valid predicate---", TokenTypes.EMPTY)
  var aCount = 0
  var bCount = 0
  var cCount = 0

  def renderStatement(ast: TurtleAST): Int = {
    (evalStatement(ast): @unchecked) match {
      case RDFTriples(t) ⇒ output(t)
      case RDFString(s)  ⇒ 0
      case RDFComment(c) ⇒ 0
    }
  }

  def evalStatement(expr: TurtleAST): RDFReturnType = {

    import TurtleAST._

    expr match {
      // evalStatement is called seperately for each statement
      // case ASTTurtleDoc( rule ) ⇒ evalStatement( rule )
      case TurtleAST.ASTStatement(rule) ⇒
        /* some clean up at the beginning of a new trig statement */
        subjectStack.clear
        predicateStack.clear
        /* evaluate a turtle statement */
        evalStatement(rule)
      case TurtleAST.ASTIri(rule) ⇒ (rule: @unchecked) match {
        case TurtleAST.ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(s) ⇒ RDFString(Term("<" + addIriPrefix(s.value) + ">", TokenTypes.IRIREF))
        }
        case TurtleAST.ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case TurtleAST.ASTIriRef(token) ⇒ RDFString(Term(token, TokenTypes.IRIREF))
      case TurtleAST.ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case TurtleAST.ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match {
            case RDFString(s) ⇒ RDFString(Term("<" + addPrefix(s.value, "") + ">", TokenTypes.PNAMENS))
          }
        case TurtleAST.ASTPNameLN(p, l) ⇒ evalStatement(rule)
      }
      case TurtleAST.ASTDirective(rule) ⇒ evalStatement(rule)
      case TurtleAST.ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (RDFString(ps), RDFString(is)) ⇒ definePrefix(ps.value, is.value)
        }
        RDFString(Term("", TokenTypes.PREFIXID))
      case TurtleAST.ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case RDFString(bs) ⇒ addBasePrefix(bs.value)
        }
        RDFString(Term("", TokenTypes.BASE))
      case TurtleAST.ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case RDFString(bs) ⇒ addBasePrefix(bs.value)
        }
        RDFString(Term("", TokenTypes.SPARQLBASE))
      case TurtleAST.ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (RDFString(ps), RDFString(is)) ⇒ definePrefix(ps.value, is.value)
        }
        RDFString(Term("", TokenTypes.SPARQLPREFIX))
      case TurtleAST.ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (RDFTriples(ts), RDFTriples(ps))     ⇒ RDFTriples(ts ::: ps)
          case (RDFString(subject), RDFTriples(ps)) ⇒ RDFTriples(ps)
        }
      case TurtleAST.ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = Term("_:b" + bCount, TokenTypes.BLANK_NODE_LABEL)
        val sub = evalStatement(s)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (RDFTriples(ts), RDFTriples(ps)) ⇒ RDFTriples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case TurtleAST.ASTPredicateObjectList(predicateObjectlist) ⇒
        predicateObjectlist match {
          case po ⇒ RDFTriples(traversePredicateObjectList(po, Nil))
        }
      case TurtleAST.ASTPo(verb, obj) ⇒
        (evalStatement(verb): @unchecked) match {
          case RDFString(token) ⇒ curPredicate = token
        }
        evalStatement(obj)
      case TurtleAST.ASTObjectList(rule) ⇒ RDFTriples(traverseTriples(rule, Nil))
      case TurtleAST.ASTVerb(rule)       ⇒ evalStatement(rule)
      case TurtleAST.ASTIsA(token)       ⇒ RDFString(Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>", TokenTypes.IRIREF | TokenTypes.ISA))
      case TurtleAST.ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case TurtleAST.ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(s) ⇒ curSubject = s; RDFString(curSubject)
        }
        case TurtleAST.ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(Term(s, t)) ⇒ curSubject = Term(s, t | TokenTypes.BLANK_NODE_LABEL); RDFString(curSubject)
        }
        case TurtleAST.ASTCollection(c) ⇒ cCount += 1; evalStatement(rule)
      }
      case TurtleAST.ASTPredicate(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case RDFString(s) ⇒ curPredicate = s
        }
        RDFString(curPredicate)
      case TurtleAST.ASTObject(l) ⇒ (l: @unchecked) match {
        case TurtleAST.ASTIri(v) ⇒ (evalStatement(l): @unchecked) match {
          case RDFString(o) ⇒ RDFTriple(curSubject, curPredicate, o);
        }
        case TurtleAST.ASTBlankNode(v) ⇒ (evalStatement(l): @unchecked) match {
          case RDFString(o) ⇒ RDFTriple(curSubject, curPredicate, o);
        }
        case TurtleAST.ASTLiteral(literal) ⇒
          (evalStatement(l): @unchecked) match {
            case RDFString(o) ⇒ RDFTriple(curSubject, curPredicate, o);
          }
        case TurtleAST.ASTCollection(v) ⇒
          l match {
            case TurtleAST.ASTCollection(Vector()) ⇒
              // empty collection
              RDFTriples(RDFTriple(curSubject, curPredicate, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", TokenTypes.IRIREF)) :: Nil)
            case _ ⇒
              subjectStack.push(curSubject)
              cCount += 1
              curSubject = Term(getCollectionName, TokenTypes.BLANK_NODE_LABEL)
              predicateStack.push(curPredicate)
              curPredicate = Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TokenTypes.IRIREF)
              (evalStatement(l): @unchecked) match {
                case RDFTriples(t) ⇒
                  val oldSubject = curSubject
                  curSubject = subjectStack.pop
                  curPredicate = predicateStack.pop
                  RDFTriples(RDFTriple(curSubject, curPredicate, oldSubject) :: t)
              }
          }
        case TurtleAST.ASTBlankNodePropertyList(v) ⇒
          subjectStack.push(curSubject)
          predicateStack.push(curPredicate)
          bCount += 1
          curSubject = Term("_:b" + bCount, TokenTypes.BLANK_NODE_LABEL)
          val bnode = curSubject
          (evalStatement(l): @unchecked) match {
            case RDFTriples(t) ⇒
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              RDFTriples(RDFTriple(curSubject, curPredicate, bnode) :: t)
          }
      }
      case TurtleAST.ASTLiteral(rule)               ⇒ evalStatement(rule)
      case TurtleAST.ASTBlankNodePropertyList(rule) ⇒ evalStatement(rule)
      case TurtleAST.ASTCollection(rule) ⇒
        curSubject = Term(getCollectionName, TokenTypes.BLANK_NODE_LABEL)
        subjectStack.push(curSubject)
        curPredicate = Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TokenTypes.IRIREF)
        predicateStack.push(curPredicate)
        val res = RDFTriples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case TurtleAST.ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case TurtleAST.ASTInteger(token)       ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#integer>", TokenTypes.INTEGER))
      case TurtleAST.ASTDecimal(token)       ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#decimal>", TokenTypes.DECIMAL))
      case TurtleAST.ASTDouble(token)        ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#double>", TokenTypes.DOUBLE))
      case TurtleAST.ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case RDFString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case TurtleAST.ASTIri(v) ⇒ RDFString(Term(literal.value + "^^" + ((evalStatement(postfix): @unchecked) match {
              case RDFString(s) ⇒ s.value
            }), TokenTypes.STRING_LITERAL | TokenTypes.IRIREF))
            case TurtleAST.ASTLangTag(v) ⇒ RDFString(Term(literal.value + "@" + ((evalStatement(postfix): @unchecked) match {
              case RDFString(s) ⇒ s.value
            }), TokenTypes.STRING_LITERAL | TokenTypes.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case TurtleAST.ASTLangTag(token)                      ⇒ RDFString(Term(token, TokenTypes.LANGTAG))
      case TurtleAST.ASTBooleanLiteral(token)               ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#boolean>", TokenTypes.BOOLEAN_LITERAL))
      case TurtleAST.ASTString(rule)                        ⇒ evalStatement(rule)
      case TurtleAST.ASTStringLiteralQuote(token)           ⇒ RDFString(Term("\"" + token + "\"", TokenTypes.STRING_LITERAL | TokenTypes.STRING_LITERAL_QUOTE))
      case TurtleAST.ASTStringLiteralSingleQuote(token)     ⇒ RDFString(Term("\"" + token + "\"", TokenTypes.STRING_LITERAL | TokenTypes.STRING_LITERAL_SINGLE_QUOTE))
      case TurtleAST.ASTStringLiteralLongSingleQuote(token) ⇒ RDFString(Term("\"" + token + "\"", TokenTypes.STRING_LITERAL | TokenTypes.STRING_LITERAL_LONG_SINGLE_QUOTE))
      case TurtleAST.ASTStringLiteralLongQuote(token)       ⇒ RDFString(Term("\"" + token + "\"", TokenTypes.STRING_LITERAL | TokenTypes.STRING_LITERAL_LONG_QUOTE))
      case TurtleAST.ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix)
          case None            ⇒ RDFString(Term("", TokenTypes.PNAMENS))
        }
      case TurtleAST.ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (RDFString(pname_ns), RDFString(pn_local)) ⇒ RDFString(Term("<" + addPrefix(pname_ns.value, pn_local.value) + ">", TokenTypes.PNAMELN))
        }
      case TurtleAST.ASTPNPrefix(token)       ⇒ RDFString(Term(token, TokenTypes.PNPREFIX))
      case TurtleAST.ASTPNLocal(token)        ⇒ RDFString(Term(token, TokenTypes.PNLOCAL))
      case TurtleAST.ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case TurtleAST.ASTBlankNodeLabel(token) ⇒ RDFString(Term(setBlankNodeName("_:" + token), TokenTypes.BLANK_NODE_LABEL))
      case TurtleAST.ASTAnon(token) ⇒
        aCount += 1
        RDFString(Term("_:a" + label + aCount, TokenTypes.ANON))
      case TurtleAST.ASTComment(token) ⇒ RDFComment(Term(token, TokenTypes.COMMENT))
    }
  }

  @tailrec
  private def traversePredicateObjectList(l: Seq[TurtleAST], triples: List[RDFTriple]): List[RDFTriple] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case RDFTriples(tl) ⇒ traversePredicateObjectList(xs, triples ::: tl)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseTriples(l: Seq[TurtleAST], triples: List[RDFTriple]): List[RDFTriple] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case RDFTriple(s, p, o) ⇒ traverseTriples(xs, triples :+ RDFTriple(s, p, o))
      case RDFTriples(t)      ⇒ traverseTriples(xs, triples ::: t)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseCollection(l: Seq[TurtleAST], triples: List[RDFTriple]): List[RDFTriple] = l match {
    case x +: xs ⇒
      val oldSubject = curSubject
      (evalStatement(x): @unchecked) match {
        case RDFTriple(s, p, o) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = Term(getCollectionName, TokenTypes.BLANK_NODE_LABEL)
          triples ::: (RDFTriple(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TokenTypes.IRIREF), o) :: (RDFTriple(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TokenTypes.IRIREF), curSubject) :: Nil))
        } else {
          triples :+ RDFTriple(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TokenTypes.IRIREF), o)
        })
        case RDFTriples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = Term(getCollectionName, TokenTypes.BLANK_NODE_LABEL)
          triples ::: (t :+ RDFTriple(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TokenTypes.IRIREF), curSubject))
        } else {
          triples ::: t
        })
      }
    case Nil ⇒ triples ::: RDFTriple(curSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TokenTypes.IRIREF), Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", TokenTypes.IRIREF)) :: Nil
  }

  private def definePrefix(key: String, value: String) = {
    if (value.startsWith("//") || hasScheme(value))
      prefixMap += key → value
    else if (value.endsWith("/") || value.endsWith("#")) {
      if (!prefixMap.contains(key))
        prefixMap += key → value
      else
        prefixMap += key → (prefixMap.getOrElse(key, basePath) + value)
    } else prefixMap += key → value
  }

  private def addPrefix(pname_ns: String, pn_local: String): String = {
    val prefix = prefixMap.getOrElse(pname_ns, "")
    if (prefix.startsWith("//") || hasScheme(prefix)) {
      if (prefix.endsWith("/") || prefix.endsWith("#"))
        prefix + pn_local
      else {
        if (pn_local != "")
          prefix + "/" + pn_local
        else
          prefix
      }
    } else {
      if (prefix.endsWith("/") || prefix.endsWith("#")) {
        basePath + prefixMap.getOrElse("", basePath) + pn_local
      } else {
        if (pn_local != "")
          prefixMap.getOrElse("", basePath) + "/" + pn_local
        else
          prefixMap.getOrElse("", basePath)
      }
    }
  }

  private def addIriPrefix(pn_local: String): String = {
    if (pn_local.startsWith("//") || hasScheme(pn_local))
      pn_local
    else {
      val prefix = prefixMap.getOrElse("", basePath)
      if (prefix.startsWith("//") || hasScheme(prefix)) {
        if (prefix.endsWith("/") || prefix.endsWith("#"))
          prefix + pn_local
        else {
          if (pn_local != "")
            prefix + "/" + pn_local
          else
            prefix
        }
      } else {
        if (prefix.endsWith("/") || prefix.endsWith("#")) {
          basePath + prefixMap.getOrElse("", basePath) + pn_local
        } else {
          if (pn_local != "")
            prefixMap.getOrElse("", basePath) + "/" + pn_local
          else
            prefixMap.getOrElse("", basePath)
        }
      }
    }
  }

  private def addBasePrefix(iri: String) = {
    if (iri.startsWith("//") || hasScheme(iri))
      prefixMap += "" → iri
    else {
      val prefix = prefixMap.getOrElse("", basePath)
      if (prefix.endsWith("/") || prefix.endsWith("#"))
        prefixMap += "" → (prefix + iri)
      else if (prefix.length > 0)
        prefixMap += "" → (prefix + "/" + iri)
      else prefixMap += "" → iri
    }
  }

  private def setBlankNodeName(key: String) = {
    if (!blankNodeMap.contains(key)) {
      bCount += 1
      blankNodeMap += key → ("_:b" + label + bCount)
    }
    blankNodeMap.getOrElse(key, "This should never be returned")
  }

  private def getCollectionName = "_:c" + label + cCount

  private def hasScheme(iri: String) = SchemeIdentifier(iri)
}