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

import org.chelona.EvalTurtle._

import scala.annotation.tailrec
import scala.util.Success

object EvalTurtle {
  def apply(output: List[SPOReturnValue] ⇒ Int, basePath: String, label: String) = new EvalTurtle(output, basePath, label)

  sealed trait TurtleReturnValue extends SPOReturnValue
}

class EvalTurtle(output: List[SPOReturnValue] ⇒ Int, basePath: String, label: String) extends TurtleReturnValue {

  import org.chelona.ChelonaParser._

  val prefixMap = scala.collection.mutable.Map.empty[String, String]
  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[TurtleElement]
  val predicateStack = scala.collection.mutable.Stack.empty[TurtleElement]

  var curSubject = TurtleElement("---Not valid subject---", TurtleBitValue.EMPTY)
  var curPredicate = TurtleElement("---Not valid predicate---", TurtleBitValue.EMPTY)
  var aCount = 0
  var bCount = 0
  var cCount = 0

  def renderStatement(ast: TurtleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case SPOTriples(t) ⇒ output(t)
      case SPOString(s)  ⇒ 0
      case SPOComment(c) ⇒ 0
    }
  }

  def evalStatement(expr: TurtleType): SPOReturnValue = {

    expr match {
      case ASTTurtleDoc(rule) ⇒ evalStatement(rule)
      case ASTStatement(rule) ⇒
        /* some clean up at the beginning of a new trig statement */
        subjectStack.clear
        predicateStack.clear
        /* evaluate a turtle statement */
        evalStatement(rule)
      case ASTIri(rule) ⇒ (rule: @unchecked) match {
        case ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ SPOString(TurtleElement("<" + addIriPrefix(s.text) + ">", TurtleBitValue.IRIREF))
        }
        case ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case ASTIriRef(token) ⇒ SPOString(TurtleElement(token, TurtleBitValue.IRIREF))
      case ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match {
            case SPOString(s) ⇒ SPOString(TurtleElement("<" + addPrefix(s.text, "") + ">", TurtleBitValue.PNAMENS))
          }
        case ASTPNameLN(p, l) ⇒ evalStatement(rule)
      }
      case ASTDirective(rule) ⇒ evalStatement(rule)
      case ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (SPOString(ps), SPOString(is)) ⇒ definePrefix(ps.text, is.text)
        }
        SPOString(TurtleElement("", TurtleBitValue.PREFIXID))
      case ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case SPOString(bs) ⇒ addBasePrefix(bs.text)
        }
        SPOString(TurtleElement("", TurtleBitValue.BASE))
      case ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case SPOString(bs) ⇒ addBasePrefix(bs.text)
        }
        SPOString(TurtleElement("", TurtleBitValue.SPARQLBASE))
      case ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (SPOString(ps), SPOString(is)) ⇒ definePrefix(ps.text, is.text)
        }
        SPOString(TurtleElement("", TurtleBitValue.SPARQLPREFIX))
      case ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (SPOTriples(ts), SPOTriples(ps))     ⇒ SPOTriples(ts ::: ps)
          case (SPOString(subject), SPOTriples(ps)) ⇒ SPOTriples(ps)
        }
      case ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = TurtleElement("_:b" + bCount, TurtleBitValue.BLANK_NODE_LABEL)
        val sub = evalStatement(s)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (SPOTriples(ts), SPOTriples(ps)) ⇒ SPOTriples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case ASTPredicateObjectList(predicateObjectlist) ⇒
        predicateObjectlist match {
          case po ⇒ SPOTriples(traversePredicateObjectList(po, Nil))
        }
      case ASTPo(verb, obj) ⇒
        (evalStatement(verb): @unchecked) match {
          case SPOString(token) ⇒ curPredicate = token
        }
        evalStatement(obj)
      case ASTObjectList(rule) ⇒ SPOTriples(traverseTriples(rule, Nil))
      case ASTVerb(rule)       ⇒ evalStatement(rule)
      case ASTIsA(token)       ⇒ SPOString(TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>", TurtleBitValue.IRIREF | TurtleBitValue.ISA))
      case ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ curSubject = s; SPOString(curSubject)
        }
        case ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ curSubject = s; SPOString(curSubject)
        }
        case ASTCollection(c) ⇒ cCount += 1; evalStatement(rule)
      }
      case ASTPredicate(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ curPredicate = s
        }
        SPOString(curPredicate)
      case ASTObject(l) ⇒ (l: @unchecked) match {
        case ASTIri(v) ⇒ (evalStatement(l): @unchecked) match {
          case SPOString(o) ⇒ SPOTriple(curSubject, curPredicate, o);
        }
        case ASTBlankNode(v) ⇒ (evalStatement(l): @unchecked) match {
          case SPOString(o) ⇒ SPOTriple(curSubject, curPredicate, o);
        }
        case ASTLiteral(literal) ⇒
          (evalStatement(l): @unchecked) match {
            case SPOString(o) ⇒ SPOTriple(curSubject, curPredicate, o);
          }
        case ASTCollection(v) ⇒
          l match {
            case ASTCollection(Vector()) ⇒
              // empty collection
              SPOTriples(SPOTriple(curSubject, curPredicate, TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", TurtleBitValue.IRIREF)) :: Nil)
            case _ ⇒
              subjectStack.push(curSubject)
              cCount += 1
              curSubject = TurtleElement(getCollectionName, TurtleBitValue.BLANK_NODE_LABEL)
              predicateStack.push(curPredicate)
              curPredicate = TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TurtleBitValue.IRIREF)
              (evalStatement(l): @unchecked) match {
                case SPOTriples(t) ⇒
                  val oldSubject = curSubject
                  curSubject = subjectStack.pop
                  curPredicate = predicateStack.pop
                  SPOTriples(SPOTriple(curSubject, curPredicate, oldSubject) :: t)
              }
          }
        case ASTBlankNodePropertyList(v) ⇒
          subjectStack.push(curSubject)
          predicateStack.push(curPredicate)
          bCount += 1
          curSubject = TurtleElement("_:b" + bCount, TurtleBitValue.BLANK_NODE_LABEL)
          val bnode = curSubject
          (evalStatement(l): @unchecked) match {
            case SPOTriples(t) ⇒
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              SPOTriples(SPOTriple(curSubject, curPredicate, bnode) :: t)
          }
      }
      case ASTLiteral(rule)               ⇒ evalStatement(rule)
      case ASTBlankNodePropertyList(rule) ⇒ evalStatement(rule)
      case ASTCollection(rule) ⇒
        curSubject = TurtleElement(getCollectionName, TurtleBitValue.BLANK_NODE_LABEL)
        subjectStack.push(curSubject)
        curPredicate = TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TurtleBitValue.IRIREF)
        predicateStack.push(curPredicate)
        val res = SPOTriples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case ASTInteger(token)       ⇒ SPOString(TurtleElement("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#integer>", TurtleBitValue.INTEGER))
      case ASTDecimal(token)       ⇒ SPOString(TurtleElement("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#decimal>", TurtleBitValue.DECIMAL))
      case ASTDouble(token)        ⇒ SPOString(TurtleElement("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#double>", TurtleBitValue.DOUBLE))
      case ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case SPOString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIri(v) ⇒ SPOString(TurtleElement(literal.text + "^^" + ((evalStatement(postfix): @unchecked) match {
              case SPOString(s) ⇒ s.text
            }), TurtleBitValue.STRING_LITERAL_QUOTE | TurtleBitValue.IRIREF))
            case ASTLangTag(v) ⇒ SPOString(TurtleElement(literal.text + "@" + ((evalStatement(postfix): @unchecked) match {
              case SPOString(s) ⇒ s.text
            }), TurtleBitValue.STRING_LITERAL_QUOTE | TurtleBitValue.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)                      ⇒ SPOString(TurtleElement(token, TurtleBitValue.LANGTAG))
      case ASTBooleanLiteral(token)               ⇒ SPOString(TurtleElement("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#boolean>", TurtleBitValue.BOOLEAN_LITERAL))
      case ASTString(rule)                        ⇒ evalStatement(rule)
      case ASTStringLiteralQuote(token)           ⇒ SPOString(TurtleElement("\"" + token + "\"", TurtleBitValue.STRING_LITERAL_QUOTE))
      case ASTStringLiteralSingleQuote(token)     ⇒ SPOString(TurtleElement("\"" + token + "\"", TurtleBitValue.STRING_LITERAL_QUOTE))
      case ASTStringLiteralLongSingleQuote(token) ⇒ SPOString(TurtleElement("\"" + token + "\"", TurtleBitValue.STRING_LITERAL_QUOTE))
      case ASTStringLiteralLongQuote(token)       ⇒ SPOString(TurtleElement("\"" + token + "\"", TurtleBitValue.STRING_LITERAL_QUOTE))
      case ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix)
          case None            ⇒ SPOString(TurtleElement("", TurtleBitValue.PNAMENS))
        }
      case ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (SPOString(pname_ns), SPOString(pn_local)) ⇒ SPOString(TurtleElement("<" + addPrefix(pname_ns.text, pn_local.text) + ">", TurtleBitValue.PNAMELN))
        }
      case ASTPNPrefix(token)       ⇒ SPOString(TurtleElement(token, TurtleBitValue.PNPREFIX))
      case ASTPNLocal(token)        ⇒ SPOString(TurtleElement(token, TurtleBitValue.PNLOCAL))
      case ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case ASTBlankNodeLabel(token) ⇒ SPOString(TurtleElement(setBlankNodeName("_:" + token), TurtleBitValue.BLANK_NODE_LABEL))
      case ASTAnon(token) ⇒
        aCount += 1
        SPOString(TurtleElement("_:a" + label + aCount, TurtleBitValue.ANON))
      case ASTComment(token) ⇒ SPOComment(TurtleElement(token, TurtleBitValue.COMMENT))
    }
  }

  @tailrec
  private def traversePredicateObjectList(l: Seq[TurtleAST], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case SPOTriples(tl) ⇒ traversePredicateObjectList(xs, triples ::: tl)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseTriples(l: Seq[TurtleAST], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case SPOTriple(s, p, o) ⇒ traverseTriples(xs, triples :+ SPOTriple(s, p, o))
      case SPOTriples(t)      ⇒ traverseTriples(xs, triples ::: t)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseCollection(l: Seq[TurtleAST], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case x +: xs ⇒
      val oldSubject = curSubject
      (evalStatement(x): @unchecked) match {
        case SPOTriple(s, p, o) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = TurtleElement(getCollectionName, TurtleBitValue.BLANK_NODE_LABEL)
          triples ::: (SPOTriple(oldSubject, TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TurtleBitValue.IRIREF), o) :: (SPOTriple(oldSubject, TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TurtleBitValue.IRIREF), curSubject) :: Nil))
        } else {
          triples :+ SPOTriple(oldSubject, TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TurtleBitValue.IRIREF), o)
        })
        case SPOTriples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = TurtleElement(getCollectionName, TurtleBitValue.BLANK_NODE_LABEL)
          triples ::: (t :+ SPOTriple(oldSubject, TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TurtleBitValue.IRIREF), curSubject))
        } else {
          triples ::: t
        })
      }
    case Nil ⇒ triples ::: SPOTriple(curSubject, TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TurtleBitValue.IRIREF), TurtleElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", TurtleBitValue.IRIREF)) :: Nil
  }

  private def definePrefix(key: String, value: String) = {
    if (value.startsWith("//") || hasScheme(value))
      prefixMap += key -> value
    else if (value.endsWith("/") || value.endsWith("#")) {
      if (!prefixMap.contains(key))
        prefixMap += key -> value
      else
        prefixMap += key -> (prefixMap.getOrElse(key, basePath) + value)
    } else prefixMap += key -> value
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
      prefixMap += "" -> iri
    else {
      val prefix = prefixMap.getOrElse("", basePath)
      if (prefix.endsWith("/") || prefix.endsWith("#"))
        prefixMap += "" -> (prefix + iri)
      else if (prefix.length > 0)
        prefixMap += "" -> (prefix + "/" + iri)
      else prefixMap += "" -> iri
    }
  }

  private def setBlankNodeName(key: String) = {
    if (!blankNodeMap.contains(key)) {
      bCount += 1
      blankNodeMap += key -> ("_:b" + label + bCount)
    }
    blankNodeMap.getOrElse(key, "This should never be returned")
  }

  private def getCollectionName = "_:c" + label + cCount

  private def hasScheme(iri: String) = SchemeIdentifier(iri)
}