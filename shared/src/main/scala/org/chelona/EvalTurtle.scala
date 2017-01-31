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

  import TurtleAST._

  val prefixMap = scala.collection.mutable.Map.empty[String, String]
  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[Term]
  val predicateStack = scala.collection.mutable.Stack.empty[Term]

  var curSubject = Term("---Not valid subject---", RDFTokenTypes.EMPTY)
  var curPredicate = Term("---Not valid predicate---", RDFTokenTypes.EMPTY)
  var aCount = 0
  var bCount = 0
  var cCount = 0

  def renderStatement(ast: TurtleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case RDFTriples(t) ⇒ output(t)
      case RDFString(s)  ⇒ 0
      case RDFComment(c) ⇒ 0
    }
  }

  def evalStatement(expr: org.chelona.TurtleAST): RDFReturnType = {

    expr match {
      // evalStatement is called seperately for each statement
      // case ASTTurtleDoc( rule ) ⇒ evalStatement( rule )
      case ASTStatement(rule) ⇒
        /* some clean up at the beginning of a new trig statement */
        subjectStack.clear
        predicateStack.clear
        /* evaluate a turtle statement */
        evalStatement(rule)
      case ASTIri(rule) ⇒ (rule: @unchecked) match {
        case ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(s) ⇒ RDFString(Term("<" + addIriPrefix(s.value) + ">", RDFTokenTypes.IRIREF))
        }
        case ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case ASTIriRef(token) ⇒ RDFString(Term(token, RDFTokenTypes.IRIREF))
      case ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match {
            case RDFString(s) ⇒ RDFString(Term("<" + addPrefix(s.value, "") + ">", RDFTokenTypes.PNAMENS))
          }
        case ASTPNameLN(p, l) ⇒ evalStatement(rule)
      }
      case ASTDirective(rule) ⇒ evalStatement(rule)
      case ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (RDFString(ps), RDFString(is)) ⇒ definePrefix(ps.value, is.value)
        }
        RDFString(Term("", RDFTokenTypes.PREFIXID))
      case ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case RDFString(bs) ⇒ addBasePrefix(bs.value)
        }
        RDFString(Term("", RDFTokenTypes.BASE))
      case ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case RDFString(bs) ⇒ addBasePrefix(bs.value)
        }
        RDFString(Term("", RDFTokenTypes.SPARQLBASE))
      case ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (RDFString(ps), RDFString(is)) ⇒ definePrefix(ps.value, is.value)
        }
        RDFString(Term("", RDFTokenTypes.SPARQLPREFIX))
      case ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (RDFTriples(ts), RDFTriples(ps))     ⇒ RDFTriples(ts ::: ps)
          case (RDFString(subject), RDFTriples(ps)) ⇒ RDFTriples(ps)
        }
      case ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = Term("_:b" + bCount, RDFTokenTypes.BLANK_NODE_LABEL)
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
      case ASTPredicateObjectList(predicateObjectlist) ⇒
        predicateObjectlist match {
          case po ⇒ RDFTriples(traversePredicateObjectList(po, Nil))
        }
      case ASTPo(verb, obj) ⇒
        (evalStatement(verb): @unchecked) match {
          case RDFString(token) ⇒ curPredicate = token
        }
        evalStatement(obj)
      case ASTObjectList(rule) ⇒ RDFTriples(traverseTriples(rule, Nil))
      case ASTVerb(rule)       ⇒ evalStatement(rule)
      case ASTIsA(token)       ⇒ RDFString(Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>", RDFTokenTypes.IRIREF | RDFTokenTypes.ISA))
      case ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(s) ⇒ curSubject = s; RDFString(curSubject)
        }
        case ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(Term(s, t)) ⇒ curSubject = Term(s, t | RDFTokenTypes.BLANK_NODE_LABEL); RDFString(curSubject)
        }
        case ASTCollection(c) ⇒ cCount += 1; evalStatement(rule)
      }
      case ASTPredicate(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case RDFString(s) ⇒ curPredicate = s
        }
        RDFString(curPredicate)
      case ASTObject(l) ⇒ (l: @unchecked) match {
        case ASTIri(v) ⇒ (evalStatement(l): @unchecked) match {
          case RDFString(o) ⇒ RDFTriple(curSubject, curPredicate, o);
        }
        case ASTBlankNode(v) ⇒ (evalStatement(l): @unchecked) match {
          case RDFString(o) ⇒ RDFTriple(curSubject, curPredicate, o);
        }
        case ASTLiteral(literal) ⇒
          (evalStatement(l): @unchecked) match {
            case RDFString(o) ⇒ RDFTriple(curSubject, curPredicate, o);
          }
        case ASTCollection(v) ⇒
          l match {
            case ASTCollection(Vector()) ⇒
              // empty collection
              RDFTriples(RDFTriple(curSubject, curPredicate, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", RDFTokenTypes.IRIREF)) :: Nil)
            case _ ⇒
              subjectStack.push(curSubject)
              cCount += 1
              curSubject = Term(getCollectionName, RDFTokenTypes.BLANK_NODE_LABEL)
              predicateStack.push(curPredicate)
              curPredicate = Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", RDFTokenTypes.IRIREF)
              (evalStatement(l): @unchecked) match {
                case RDFTriples(t) ⇒
                  val oldSubject = curSubject
                  curSubject = subjectStack.pop
                  curPredicate = predicateStack.pop
                  RDFTriples(RDFTriple(curSubject, curPredicate, oldSubject) :: t)
              }
          }
        case ASTBlankNodePropertyList(v) ⇒
          subjectStack.push(curSubject)
          predicateStack.push(curPredicate)
          bCount += 1
          curSubject = Term("_:b" + bCount, RDFTokenTypes.BLANK_NODE_LABEL)
          val bnode = curSubject
          (evalStatement(l): @unchecked) match {
            case RDFTriples(t) ⇒
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              RDFTriples(RDFTriple(curSubject, curPredicate, bnode) :: t)
          }
      }
      case ASTLiteral(rule)               ⇒ evalStatement(rule)
      case ASTBlankNodePropertyList(rule) ⇒ evalStatement(rule)
      case ASTCollection(rule) ⇒
        curSubject = Term(getCollectionName, RDFTokenTypes.BLANK_NODE_LABEL)
        subjectStack.push(curSubject)
        curPredicate = Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", RDFTokenTypes.IRIREF)
        predicateStack.push(curPredicate)
        val res = RDFTriples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case ASTInteger(token)       ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#integer>", RDFTokenTypes.INTEGER))
      case ASTDecimal(token)       ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#decimal>", RDFTokenTypes.DECIMAL))
      case ASTDouble(token)        ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#double>", RDFTokenTypes.DOUBLE))
      case ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case RDFString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIri(v) ⇒ RDFString(Term(literal.value + "^^" + ((evalStatement(postfix): @unchecked) match {
              case RDFString(s) ⇒ s.value
            }), RDFTokenTypes.STRING_LITERAL | RDFTokenTypes.IRIREF))
            case ASTLangTag(v) ⇒ RDFString(Term(literal.value + "@" + ((evalStatement(postfix): @unchecked) match {
              case RDFString(s) ⇒ s.value
            }), RDFTokenTypes.STRING_LITERAL | RDFTokenTypes.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)                      ⇒ RDFString(Term(token, RDFTokenTypes.LANGTAG))
      case ASTBooleanLiteral(token)               ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#boolean>", RDFTokenTypes.BOOLEAN_LITERAL))
      case ASTString(rule)                        ⇒ evalStatement(rule)
      case ASTStringLiteralQuote(token)           ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL | RDFTokenTypes.STRING_LITERAL_QUOTE))
      case ASTStringLiteralSingleQuote(token)     ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL | RDFTokenTypes.STRING_LITERAL_SINGLE_QUOTE))
      case ASTStringLiteralLongSingleQuote(token) ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL | RDFTokenTypes.STRING_LITERAL_LONG_SINGLE_QUOTE))
      case ASTStringLiteralLongQuote(token)       ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL | RDFTokenTypes.STRING_LITERAL_LONG_QUOTE))
      case ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix)
          case None            ⇒ RDFString(Term("", RDFTokenTypes.PNAMENS))
        }
      case ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (RDFString(pname_ns), RDFString(pn_local)) ⇒ RDFString(Term("<" + addPrefix(pname_ns.value, pn_local.value) + ">", RDFTokenTypes.PNAMELN))
        }
      case ASTPNPrefix(token)       ⇒ RDFString(Term(token, RDFTokenTypes.PNPREFIX))
      case ASTPNLocal(token)        ⇒ RDFString(Term(token, RDFTokenTypes.PNLOCAL))
      case ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case ASTBlankNodeLabel(token) ⇒ RDFString(Term(setBlankNodeName("_:" + token), RDFTokenTypes.BLANK_NODE_LABEL))
      case ASTAnon(token) ⇒
        aCount += 1
        RDFString(Term("_:a" + label + aCount, RDFTokenTypes.ANON))
      case ASTComment(token) ⇒ RDFComment(Term(token, RDFTokenTypes.COMMENT))
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
          curSubject = Term(getCollectionName, RDFTokenTypes.BLANK_NODE_LABEL)
          triples ::: (RDFTriple(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", RDFTokenTypes.IRIREF), o) :: (RDFTriple(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", RDFTokenTypes.IRIREF), curSubject) :: Nil))
        } else {
          triples :+ RDFTriple(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", RDFTokenTypes.IRIREF), o)
        })
        case RDFTriples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = Term(getCollectionName, RDFTokenTypes.BLANK_NODE_LABEL)
          triples ::: (t :+ RDFTriple(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", RDFTokenTypes.IRIREF), curSubject))
        } else {
          triples ::: t
        })
      }
    case Nil ⇒ triples ::: RDFTriple(curSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", RDFTokenTypes.IRIREF), Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", RDFTokenTypes.IRIREF)) :: Nil
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

  private def hasScheme(iri: String) = new SchemeIdentifier(iri).scheme
}