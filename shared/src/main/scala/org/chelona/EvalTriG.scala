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

import org.chelona.EvalTriG._

import scala.annotation.tailrec

object EvalTriG {
  def apply(output: List[TriGReturnValue] ⇒ Int, basePath: String, label: String) = new EvalTriG(output, basePath, label)

  sealed trait TGReturnValue extends TriGReturnValue
}

class EvalTriG(output: List[TriGReturnValue] ⇒ Int, basePath: String, label: String) extends TGReturnValue {

  import org.chelona.TriGParser._

  val prefixMap = scala.collection.mutable.Map.empty[String, String]
  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[TriGElement]
  val predicateStack = scala.collection.mutable.Stack.empty[TriGElement]

  var curSubject: TriGElement = TriGElement("---Not valid subject---", TriGBitValue.STRING_LITERAL)
  var curPredicate: TriGElement = TriGElement("---Not valid predicate---", TriGBitValue.STRING_LITERAL)
  var curGraph: TriGElement = TriGElement("", TriGBitValue.STRING_LITERAL)
  var aCount = 0
  var bCount = 0
  var cCount = 0

  def renderStatement(ast: TurtleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case TriGTuples(t)  ⇒ output(t)
      case TriGString(s)  ⇒ 0
      case TriGComment(c) ⇒ 0
      case _ => 0
    }
  }

  def evalStatement(expr: TurtleType): TriGReturnValue = {
    expr match {
      case ASTTrigDoc(rule) ⇒ evalStatement(rule)
      case ASTStatement(rule) ⇒
        /* some clean up at the beginning of a new trig statement */
        subjectStack.clear
        predicateStack.clear
        /* evaluate a trig statement */
        evalStatement(rule)
      case ASTBlock(rule) ⇒
        rule match {
          case ASTWrappedGraph(tb) ⇒ curGraph = TriGElement("", TriGBitValue.STRING_LITERAL)
          case _                   ⇒
        }
        evalStatement(rule)
      case ASTLabelOrSubjectBlock(l, s) ⇒
        evalStatement(l); evalStatement(s)
      case ASTTriplesOrGraph(l, rule) ⇒
        evalStatement(l); rule match { case ASTPredicateObjectList(po) ⇒ curGraph = TriGElement("", TriGBitValue.STRING_LITERAL); case _ ⇒ }; evalStatement(rule)
      case ASTTriple2BlankNodePropertyList(b, p) ⇒
        curGraph = TriGElement("", TriGBitValue.STRING_LITERAL)
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = TriGElement("_:b" + bCount, TriGBitValue.BLANK_NODE_LABEL)
        val sub = evalStatement(b)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (TriGTuples(ts), TriGTuples(ps)) ⇒ TriGTuples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case ASTTriple2Collection(c, p) ⇒
        curGraph = TriGElement("", TriGBitValue.STRING_LITERAL)
        cCount += 1
        ((evalStatement(c), evalStatement(p)): @unchecked) match {
          case (TriGTuples(cs), TriGTuples(ps)) ⇒ TriGTuples(cs ::: ps)
        }
      case ASTWrappedGraph(t) ⇒ t match {
        case Some(g) ⇒ evalStatement(g)
        case None    ⇒ TriGNone() //TriGString("")
      }
      case ASTTriplesBlock(t) ⇒ TriGTuples(traverseTriples(t, Nil))
      case ASTLabelOrSubject(ls) ⇒
        val l = evalStatement(ls)
        (l: @unchecked) match { case TriGString(label) ⇒ curGraph = label; curSubject = label }
        l
      case ASTIri(rule) ⇒ (rule: @unchecked) match {
        case ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case TriGString(TriGElement(s, t)) ⇒ TriGString(TriGElement("<" + addIriPrefix(s) + ">", t | TriGBitValue.IRIREF))
        }
        case ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case ASTIriRef(token) ⇒ TriGString(TriGElement(token, TriGBitValue.IRIREF))
      case ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match {
            case TriGString(TriGElement(s, t)) ⇒ TriGString(TriGElement("<" + addPrefix(s, "") + ">", t | TriGBitValue.PNAMENS))
          }
        case ASTPNameLN(p, l) ⇒ evalStatement(rule)
      }
      case ASTDirective(rule) ⇒ evalStatement(rule)
      case ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (TriGString(TriGElement(ps,t1)), TriGString(TriGElement(is, t2))) ⇒ definePrefix(ps, is)
        }
        TriGNone()
      case ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case TriGString(TriGElement(bs, t)) ⇒ addBasePrefix(bs)
        }
        TriGNone()
      case ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case TriGString(TriGElement(bs, t)) ⇒ addBasePrefix(bs)
        }
        TriGNone()
      case ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (TriGString(TriGElement(ps, t1)), TriGString(TriGElement(is, t2))) ⇒ definePrefix(ps, is)
        }
        TriGNone()
      case ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (TriGTuples(ts), TriGTuples(ps))      ⇒ TriGTuples(ts ::: ps)
          case (TriGString(subject), TriGTuples(ps)) ⇒ TriGTuples(ps)
        }
      case ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = TriGElement("_:b" + bCount, TriGBitValue.BLANK_NODE_LABEL)
        val sub = evalStatement(s)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (TriGTuples(ts), TriGTuples(ps)) ⇒ TriGTuples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case ASTPredicateObjectList(predicateObjectlist) ⇒
        predicateObjectlist match {
          case po ⇒ TriGTuples(traversePredicateObjectList(po, Nil))
        }
      case ASTPo(verb, obj) ⇒
        (evalStatement(verb): @unchecked) match {
          case TriGString(token) ⇒ curPredicate = token
        }
        evalStatement(obj)
      case ASTObjectList(rule) ⇒ TriGTuples(traverseTriples(rule, Nil))
      case ASTVerb(rule)       ⇒ evalStatement(rule)
      case ASTIsA(token)       ⇒ TriGString(TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>", TriGBitValue.ISA))
      case ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case TriGString(TriGElement(s, t)) ⇒ curSubject = TriGElement(s, t | TriGBitValue.IRIREF); TriGString(curSubject)
        }
        case ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case TriGString(TriGElement(s, t)) ⇒ curSubject = TriGElement(s, t | TriGBitValue.BLANK_NODE_LABEL); TriGString(curSubject)
        }
        case ASTCollection(c) ⇒ cCount += 1; evalStatement(rule)
      }
      case ASTPredicate(rule) ⇒
        val predicate = evalStatement(rule)
        (predicate: @unchecked) match {
          case TriGString(s) ⇒ curPredicate = s
        }
        predicate
      case ASTObject(l) ⇒ (l: @unchecked) match {
        case ASTIri(v) ⇒ (evalStatement(l): @unchecked) match {
          case TriGString(o) ⇒ TriGTuple(curSubject, curPredicate, o, curGraph);
        }
        case ASTBlankNode(v) ⇒ (evalStatement(l): @unchecked) match {
          case TriGString(o) ⇒ TriGTuple(curSubject, curPredicate, o, curGraph);
        }
        case ASTLiteral(literal) ⇒
          (evalStatement(l): @unchecked) match {
            case TriGString(o) ⇒ TriGTuple(curSubject, curPredicate, o, curGraph);
          }
        case ASTCollection(v) ⇒
          l match {
            case ASTCollection(Vector()) ⇒
              // empty collection
              TriGTuples(TriGTuple(curSubject, curPredicate, TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", TriGBitValue.IRIREF), curGraph) :: Nil)
            case _ ⇒
              subjectStack.push(curSubject)
              cCount += 1
              curSubject = TriGElement(getCollectionName, TriGBitValue.BLANK_NODE_LABEL)
              predicateStack.push(curPredicate)
              curPredicate = TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TriGBitValue.IRIREF)
              (evalStatement(l): @unchecked) match {
                case TriGTuples(t) ⇒
                  val oldSubject = curSubject
                  curSubject = subjectStack.pop
                  curPredicate = predicateStack.pop
                  TriGTuples(TriGTuple(curSubject, curPredicate, oldSubject, curGraph) :: t)
              }
          }
        case ASTBlankNodePropertyList(v) ⇒
          subjectStack.push(curSubject)
          predicateStack.push(curPredicate)
          bCount += 1
          curSubject = TriGElement("_:b" + bCount, TriGBitValue.BLANK_NODE_LABEL)
          val bnode = curSubject
          (evalStatement(l): @unchecked) match {
            case TriGTuples(t) ⇒
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              TriGTuples(TriGTuple(curSubject, curPredicate, bnode, curGraph) :: t)
          }
      }
      case ASTLiteral(rule)               ⇒ evalStatement(rule)
      case ASTBlankNodePropertyList(rule) ⇒ evalStatement(rule)
      case ASTCollection(rule) ⇒
        curSubject = TriGElement(getCollectionName, TriGBitValue.BLANK_NODE_LABEL)
        subjectStack.push(curSubject)
        curPredicate = TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TriGBitValue.IRIREF)
        predicateStack.push(curPredicate)
        val res = TriGTuples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case ASTInteger(token)       ⇒ TriGString(TriGElement("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#integer>", TriGBitValue.INTEGER))
      case ASTDecimal(token)       ⇒ TriGString(TriGElement("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#decimal>", TriGBitValue.DECIMAL))
      case ASTDouble(token)        ⇒ TriGString(TriGElement("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#double>", TriGBitValue.DOUBLE))
      case ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case TriGString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIri(v) ⇒ TriGString(TriGElement(literal.text + "^^" + ((evalStatement(postfix): @unchecked) match {
              case TriGString(TriGElement(s, t)) ⇒ s
            }), TriGBitValue.IRIREF))
            case ASTLangTag(v) ⇒ TriGString(TriGElement(literal.text + "@" + ((evalStatement(postfix): @unchecked) match {
              case TriGString(TriGElement(s, t)) ⇒ s
            }), TriGBitValue.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)                      ⇒ TriGString(TriGElement(token, TriGBitValue.LANGTAG))
      case ASTBooleanLiteral(token)               ⇒ TriGString(TriGElement("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#boolean>", TriGBitValue.BOOLEAN_LITERAL))
      case ASTString(rule)                        ⇒ evalStatement(rule)
      case ASTStringLiteralQuote(token)           ⇒ TriGString(TriGElement("\"" + token + "\"", TriGBitValue.STRING_LITERAL_QUOTE))
      case ASTStringLiteralSingleQuote(token)     ⇒ TriGString(TriGElement("\"" + token + "\"", TriGBitValue.STRING_LITERAL_SINGLE_QUOTE))
      case ASTStringLiteralLongSingleQuote(token) ⇒ TriGString(TriGElement("\"" + token + "\"", TriGBitValue.STRING_LITERAL_LONG_SINGLE_QUOTE))
      case ASTStringLiteralLongQuote(token)       ⇒ TriGString(TriGElement("\"" + token + "\"", TriGBitValue.STRING_LITERAL_LONG_QUOTE))
      case ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix)
          case None            ⇒ TriGString(TriGElement("", TriGBitValue.PNAMENS))
        }
      case ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (TriGString(TriGElement(pname_ns, t1)), TriGString(TriGElement(pn_local, t2))) ⇒ TriGString(TriGElement("<" + addPrefix(pname_ns, pn_local) + ">", TriGBitValue.PNAMELN))
        }
      case ASTPNPrefix(token)       ⇒ TriGString(TriGElement(token, TriGBitValue.PNPREFIX))
      case ASTPNLocal(token)        ⇒ TriGString(TriGElement(token, TriGBitValue.PNLOCAL))
      case ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case ASTBlankNodeLabel(token) ⇒ TriGString(TriGElement(setBlankNodeName("_:" + token),TriGBitValue.BLANK_NODE_LABEL))
      case ASTAnon(token) ⇒
        aCount += 1; TriGString(TriGElement("_:a" + label + aCount, TriGBitValue.ANON))
      case ASTComment(token) ⇒ TriGComment(TriGElement(token, TriGBitValue.COMMENT))
    }
  }

  @tailrec
  private def traversePredicateObjectList(l: Seq[TurtleAST], triples: List[TriGTuple]): List[TriGTuple] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case TriGTuples(tl) ⇒ traversePredicateObjectList(xs, triples ::: tl)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseTriples(l: Seq[TurtleAST], triples: List[TriGTuple]): List[TriGTuple] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case TriGTuple(s, p, o, g) ⇒ traverseTriples(xs, triples :+ TriGTuple(s, p, o, g))
      case TriGTuples(t)         ⇒ traverseTriples(xs, triples ::: t)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseCollection(l: Seq[TurtleAST], triples: List[TriGTuple]): List[TriGTuple] = l match {
    case x +: xs ⇒
      val oldSubject = curSubject
      (evalStatement(x): @unchecked) match {
        case TriGTuple(s, p, o, g) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = TriGElement(getCollectionName, TriGBitValue.BLANK_NODE_LABEL)
          triples ::: (TriGTuple(oldSubject, TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TriGBitValue.IRIREF), o, g) :: (TriGTuple(oldSubject, TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TriGBitValue.IRIREF), curSubject, g) :: Nil))
        } else {
          triples :+ TriGTuple(oldSubject, TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", TriGBitValue.IRIREF), o, g)
        })
        case TriGTuples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = TriGElement(getCollectionName, TriGBitValue.BLANK_NODE_LABEL)
          triples ::: (t :+ TriGTuple(oldSubject, TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TriGBitValue.IRIREF), curSubject, curGraph))
        } else {
          triples ::: t
        })
      }
    case Nil ⇒ triples ::: TriGTuple(curSubject, TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", TriGBitValue.IRIREF), TriGElement("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", TriGBitValue.IRIREF), curGraph) :: Nil
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
    val prefix = prefixMap.getOrElse(pname_ns, "" /*basePath*/ )
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
