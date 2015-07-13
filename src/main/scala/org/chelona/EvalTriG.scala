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

import org.chelona.EvalTriG._
import org.chelona.TriGParser._

import scala.annotation.tailrec
import scala.util.Success

object EvalTriG {
  def apply(basePath: String, label: String) = new EvalTriG(basePath, label)

  sealed trait TrigReturnValue

  case class TrigString(s: String) extends TrigReturnValue

  case class TrigTuple(s: String, p: String, o: String, g: String) extends TrigReturnValue

  case class TrigTuples(values: List[TrigTuple]) extends TrigReturnValue

  case class TrigComment(value: String) extends TrigReturnValue
}

class EvalTriG(basePath: String, label: String) {

  import org.chelona.ChelonaParser._

  val prefixMap2 = scala.collection.mutable.Map.empty[String, String]
  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[String]
  val predicateStack = scala.collection.mutable.Stack.empty[String]

  var curSubject: String = "---Not valid subject---"
  var curPredicate: String = "---Not valid predicate---"
  var curGraph: String = ""
  var aCount = 0
  var bCount = 0
  var cCount = 0

  def renderStatement(ast: TurtleType, writer: List[TrigTuple] ⇒ Int): Int = {
    (evalStatement(ast): @unchecked) match {
      case TrigTuples(t)  ⇒ writer(t)
      case TrigString(s)  ⇒ 0
      case TrigComment(c) ⇒ 0
    }
  }

  def evalStatement(expr: TurtleType): TrigReturnValue = {
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
          case ASTWrappedGraph(tb) ⇒ curGraph = ""
          case _                   ⇒
        }
        evalStatement(rule)
      case ASTLabelOrSubjectBlock(l, s) ⇒
        evalStatement(l); evalStatement(s)
      case ASTTriplesOrGraph(l, rule) ⇒
        evalStatement(l); rule match { case ASTPredicateObjectList(po) ⇒ curGraph = ""; case _ ⇒ }; evalStatement(rule)
      case ASTTriple2BlankNodePropertyList(b, p) ⇒
        curGraph = ""
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = "_:b" + bCount
        val sub = evalStatement(b)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (TrigTuples(ts), TrigTuples(ps)) ⇒ TrigTuples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case ASTTriple2Collection(c, p) ⇒
        curGraph = ""
        ((evalStatement(c), evalStatement(p)): @unchecked) match {
          case (TrigTuples(cs), TrigTuples(ps)) ⇒ TrigTuples(cs ::: ps)
        }
      case ASTWrappedGraph(t) ⇒ t match {
        case Some(g) ⇒ evalStatement(g)
        case None    ⇒ TrigString("")
      }
      case ASTTriplesBlock(t) ⇒ TrigTuples(traverseTriples(t, Nil))
      case ASTLabelOrSubject(ls) ⇒
        val l = evalStatement(ls)
        (l: @unchecked) match { case TrigString(label) ⇒ curGraph = label; curSubject = label; l }
      case ASTIri(rule) ⇒ (rule: @unchecked) match {
        case ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case TrigString(s) ⇒ TrigString("<" + addIriPrefix(s) + ">")
        }
        case ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case ASTIriRef(token) ⇒ TrigString(token)
      case ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match {
            case TrigString(s) ⇒ TrigString("<" + addPrefix(s, "") + ">")
          }
        case ASTPNameLN(p, l) ⇒ evalStatement(rule)
      }
      case ASTDirective(rule) ⇒ evalStatement(rule)
      case ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (TrigString(ps), TrigString(is)) ⇒ definePrefix(ps, is)
        }
        TrigString("")
      case ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case TrigString(bs) ⇒ addBasePrefix(bs)
        }
        TrigString("")
      case ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case TrigString(bs) ⇒ addBasePrefix(bs)
        }
        TrigString("")
      case ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (TrigString(ps), TrigString(is)) ⇒ definePrefix(ps, is)
        }
        TrigString("")
      case ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (TrigTuples(ts), TrigTuples(ps))      ⇒ TrigTuples(ts ::: ps)
          case (TrigString(subject), TrigTuples(ps)) ⇒ TrigTuples(ps)
        }
      case ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = "_:b" + bCount
        val sub = evalStatement(s)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (TrigTuples(ts), TrigTuples(ps)) ⇒ TrigTuples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case ASTPredicateObjectList(predicateObjectlist) ⇒
        predicateObjectlist match {
          case po ⇒ TrigTuples(traversePredicateObjectList(po, Nil))
        }
      case ASTPo(verb, obj) ⇒
        (evalStatement(verb): @unchecked) match {
          case TrigString(token) ⇒ curPredicate = token
        }
        evalStatement(obj)
      case ASTObjectList(rule) ⇒ TrigTuples(traverseTriples(rule, Nil))
      case ASTVerb(rule)       ⇒ evalStatement(rule)
      case ASTIsA(token)       ⇒ TrigString("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")
      case ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case TrigString(s) ⇒ curSubject = s; TrigString(curSubject)
        }
        case ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case TrigString(s) ⇒ curSubject = s; TrigString(curSubject)
        }
        case ASTCollection(c) ⇒ cCount += 1; evalStatement(rule)
      }
      case ASTPredicate(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case TrigString(s) ⇒ curPredicate = s
        }
        TrigString(curPredicate)
      case ASTObject(l) ⇒ (l: @unchecked) match {
        case ASTIri(v) ⇒ (evalStatement(l): @unchecked) match {
          case TrigString(o) ⇒ TrigTuple(curSubject, curPredicate, o, curGraph);
        }
        case ASTBlankNode(v) ⇒ (evalStatement(l): @unchecked) match {
          case TrigString(o) ⇒ TrigTuple(curSubject, curPredicate, o, curGraph);
        }
        case ASTLiteral(literal) ⇒
          (evalStatement(l): @unchecked) match {
            case TrigString(o) ⇒ TrigTuple(curSubject, curPredicate, o, curGraph);
          }
        case ASTCollection(v) ⇒
          l match {
            case ASTCollection(Vector()) ⇒
              // empty collection
              TrigTuples(TrigTuple(curSubject, curPredicate, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", curGraph) :: Nil)
            case _ ⇒
              subjectStack.push(curSubject)
              cCount += 1
              curSubject = getCollectionName
              predicateStack.push(curPredicate)
              curPredicate = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>"
              (evalStatement(l): @unchecked) match {
                case TrigTuples(t) ⇒
                  val oldSubject = curSubject
                  curSubject = subjectStack.pop
                  curPredicate = predicateStack.pop
                  TrigTuples(TrigTuple(curSubject, curPredicate, oldSubject, curGraph) :: t)
              }
          }
        case ASTBlankNodePropertyList(v) ⇒
          subjectStack.push(curSubject)
          predicateStack.push(curPredicate)
          bCount += 1
          curSubject = "_:b" + bCount
          val bnode = curSubject
          (evalStatement(l): @unchecked) match {
            case TrigTuples(t) ⇒
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              TrigTuples(TrigTuple(curSubject, curPredicate, bnode, curGraph) :: t)
          }
      }
      case ASTLiteral(rule)               ⇒ evalStatement(rule)
      case ASTBlankNodePropertyList(rule) ⇒ evalStatement(rule)
      case ASTCollection(rule) ⇒
        curSubject = getCollectionName
        subjectStack.push(curSubject)
        curPredicate = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>"
        predicateStack.push(curPredicate)
        val res = TrigTuples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case ASTInteger(token)       ⇒ TrigString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#integer>")
      case ASTDecimal(token)       ⇒ TrigString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#decimal>")
      case ASTDouble(token)        ⇒ TrigString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#double>")
      case ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case TrigString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIri(v) ⇒ TrigString(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case TrigString(s) ⇒ s
            }))
            case ASTLangTag(v) ⇒ TrigString(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case TrigString(s) ⇒ s
            }))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)                      ⇒ TrigString(token)
      case ASTBooleanLiteral(token)               ⇒ TrigString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#boolean>")
      case ASTString(rule)                        ⇒ evalStatement(rule)
      case ASTStringLiteralQuote(token)           ⇒ TrigString("\"" + token + "\"")
      case ASTStringLiteralSingleQuote(token)     ⇒ TrigString("\"" + token + "\"")
      case ASTStringLiteralLongSingleQuote(token) ⇒ TrigString("\"" + token + "\"")
      case ASTStringLiteralLongQuote(token)       ⇒ TrigString("\"" + token + "\"")
      case ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix)
          case None            ⇒ TrigString("")
        }
      case ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (TrigString(pname_ns), TrigString(pn_local)) ⇒ TrigString("<" + addPrefix(pname_ns, pn_local) + ">")
        }
      case ASTPNPrefix(token)       ⇒ TrigString(token)
      case ASTPNLocal(token)        ⇒ TrigString(token)
      case ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case ASTBlankNodeLabel(token) ⇒ TrigString(setBlankNodeName("_:" + token))
      case ASTAnon(token) ⇒
        aCount += 1; TrigString("_:a" + label + aCount)
      case ASTComment(token) ⇒ TrigComment(token)
    }

  }

  @tailrec
  private def traversePredicateObjectList(l: Seq[TurtleAST], triples: List[TrigTuple]): List[TrigTuple] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case TrigTuples(tl) ⇒ traversePredicateObjectList(xs, triples ::: tl)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseTriples(l: Seq[TurtleAST], triples: List[TrigTuple]): List[TrigTuple] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case TrigTuple(s, p, o, g) ⇒ traverseTriples(xs, triples :+ TrigTuple(s, p, o, g))
      case TrigTuples(t)         ⇒ traverseTriples(xs, triples ::: t)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseCollection(l: Seq[TurtleAST], triples: List[TrigTuple]): List[TrigTuple] = l match {
    case x +: xs ⇒
      val oldSubject = curSubject
      (evalStatement(x): @unchecked) match {
        case TrigTuple(s, p, o, g) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = getCollectionName
          triples ::: (TrigTuple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", o, g) :: (TrigTuple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", curSubject, g) :: Nil))
        } else {
          triples :+ TrigTuple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", o, g)
        })
        case TrigTuples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = getCollectionName
          triples ::: (t :+ TrigTuple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", curSubject, curGraph))
        } else {
          triples ::: t
        })
      }
    case Nil ⇒ triples ::: TrigTuple(curSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", curGraph) :: Nil
  }

  private def definePrefix(key: String, value: String) = {
    if (value.startsWith("//") || hasScheme(value))
      prefixMap2 += key -> value
    else if (value.endsWith("/") || value.endsWith("#")) {
      if (!prefixMap2.contains(key))
        prefixMap2 += key -> value
      else
        prefixMap2 += key -> (prefixMap2.getOrElse(key, basePath) + value)
    } else prefixMap2 += key -> value
  }

  private def addPrefix(pname_ns: String, pn_local: String): String = {
    val prefix = prefixMap2.getOrElse(pname_ns, "" /*basePath*/ )
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
        basePath + prefixMap2.getOrElse("", basePath) + pn_local
      } else {
        if (pn_local != "")
          prefixMap2.getOrElse("", basePath) + "/" + pn_local
        else
          prefixMap2.getOrElse("", basePath)
      }
    }
  }

  private def addIriPrefix(pn_local: String): String = {
    if (pn_local.startsWith("//") || hasScheme(pn_local))
      pn_local
    else {
      val prefix = prefixMap2.getOrElse("", basePath)
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
          basePath + prefixMap2.getOrElse("", basePath) + pn_local
        } else {
          if (pn_local != "")
            prefixMap2.getOrElse("", basePath) + "/" + pn_local
          else
            prefixMap2.getOrElse("", basePath)
        }
      }
    }
  }

  private def addBasePrefix(iri: String) = {
    if (iri.startsWith("//") || hasScheme(iri))
      prefixMap2 += "" -> iri
    else {
      val prefix = prefixMap2.getOrElse("", basePath)
      if (prefix.endsWith("/") || prefix.endsWith("#"))
        prefixMap2 += "" -> (prefix + iri)
      else if (prefix.length > 0)
        prefixMap2 += "" -> (prefix + "/" + iri)
      else prefixMap2 += "" -> iri
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

  private def hasScheme(iri: String) = SchemeIdentifier(iri).scheme.run() match {
    case Success(s) ⇒ true
    case _          ⇒ false
  }
}
