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

import scala.annotation.tailrec
import scala.util.Success

object EvalTriG {
  def apply(output: List[TriGReturnValue] ⇒ Int, basePath: String, label: String) = new EvalTriG(output, basePath, label)

  sealed trait TGReturnValue extends TriGReturnValue
}

class EvalTriG(output: List[TriGReturnValue] ⇒ Int, basePath: String, label: String) extends TGReturnValue {

  import org.chelona.TriGParser._

  val prefixMap = scala.collection.mutable.Map.empty[String, String]
  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[String]
  val predicateStack = scala.collection.mutable.Stack.empty[String]

  var curSubject: String = "---Not valid subject---"
  var curPredicate: String = "---Not valid predicate---"
  var curGraph: String = ""
  var aCount = 0
  var bCount = 0
  var cCount = 0

  def renderStatement(ast: TurtleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case TriGTuples(t)  ⇒ output(t)
      case TriGString(s)  ⇒ 0
      case TriGComment(c) ⇒ 0
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
            case (TriGTuples(ts), TriGTuples(ps)) ⇒ TriGTuples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case ASTTriple2Collection(c, p) ⇒
        curGraph = ""
        cCount += 1
        ((evalStatement(c), evalStatement(p)): @unchecked) match {
          case (TriGTuples(cs), TriGTuples(ps)) ⇒ TriGTuples(cs ::: ps)
        }
      case ASTWrappedGraph(t) ⇒ t match {
        case Some(g) ⇒ evalStatement(g)
        case None    ⇒ TriGString("")
      }
      case ASTTriplesBlock(t) ⇒ TriGTuples(traverseTriples(t, Nil))
      case ASTLabelOrSubject(ls) ⇒
        val l = evalStatement(ls)
        (l: @unchecked) match { case TriGString(label) ⇒ curGraph = label; curSubject = label; l }
      case ASTIri(rule) ⇒ (rule: @unchecked) match {
        case ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case TriGString(s) ⇒ TriGString("<" + addIriPrefix(s) + ">")
        }
        case ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case ASTIriRef(token) ⇒ TriGString(token)
      case ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match {
            case TriGString(s) ⇒ TriGString("<" + addPrefix(s, "") + ">")
          }
        case ASTPNameLN(p, l) ⇒ evalStatement(rule)
      }
      case ASTDirective(rule) ⇒ evalStatement(rule)
      case ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (TriGString(ps), TriGString(is)) ⇒ definePrefix(ps, is)
        }
        TriGString("")
      case ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case TriGString(bs) ⇒ addBasePrefix(bs)
        }
        TriGString("")
      case ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case TriGString(bs) ⇒ addBasePrefix(bs)
        }
        TriGString("")
      case ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (TriGString(ps), TriGString(is)) ⇒ definePrefix(ps, is)
        }
        TriGString("")
      case ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (TriGTuples(ts), TriGTuples(ps))      ⇒ TriGTuples(ts ::: ps)
          case (TriGString(subject), TriGTuples(ps)) ⇒ TriGTuples(ps)
        }
      case ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = "_:b" + bCount
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
      case ASTIsA(token)       ⇒ TriGString("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")
      case ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case TriGString(s) ⇒ curSubject = s; TriGString(curSubject)
        }
        case ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case TriGString(s) ⇒ curSubject = s; TriGString(curSubject)
        }
        case ASTCollection(c) ⇒ cCount += 1; evalStatement(rule)
      }
      case ASTPredicate(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case TriGString(s) ⇒ curPredicate = s
        }
        TriGString(curPredicate)
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
              TriGTuples(TriGTuple(curSubject, curPredicate, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", curGraph) :: Nil)
            case _ ⇒
              subjectStack.push(curSubject)
              cCount += 1
              curSubject = getCollectionName
              predicateStack.push(curPredicate)
              curPredicate = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>"
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
          curSubject = "_:b" + bCount
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
        curSubject = getCollectionName
        subjectStack.push(curSubject)
        curPredicate = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>"
        predicateStack.push(curPredicate)
        val res = TriGTuples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case ASTInteger(token)       ⇒ TriGString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#integer>")
      case ASTDecimal(token)       ⇒ TriGString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#decimal>")
      case ASTDouble(token)        ⇒ TriGString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#double>")
      case ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case TriGString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIri(v) ⇒ TriGString(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case TriGString(s) ⇒ s
            }))
            case ASTLangTag(v) ⇒ TriGString(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case TriGString(s) ⇒ s
            }))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)                      ⇒ TriGString(token)
      case ASTBooleanLiteral(token)               ⇒ TriGString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#boolean>")
      case ASTString(rule)                        ⇒ evalStatement(rule)
      case ASTStringLiteralQuote(token)           ⇒ TriGString("\"" + token + "\"")
      case ASTStringLiteralSingleQuote(token)     ⇒ TriGString("\"" + token + "\"")
      case ASTStringLiteralLongSingleQuote(token) ⇒ TriGString("\"" + token + "\"")
      case ASTStringLiteralLongQuote(token)       ⇒ TriGString("\"" + token + "\"")
      case ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix)
          case None            ⇒ TriGString("")
        }
      case ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (TriGString(pname_ns), TriGString(pn_local)) ⇒ TriGString("<" + addPrefix(pname_ns, pn_local) + ">")
        }
      case ASTPNPrefix(token)       ⇒ TriGString(token)
      case ASTPNLocal(token)        ⇒ TriGString(token)
      case ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case ASTBlankNodeLabel(token) ⇒ TriGString(setBlankNodeName("_:" + token))
      case ASTAnon(token) ⇒
        aCount += 1; TriGString("_:a" + label + aCount)
      case ASTComment(token) ⇒ TriGComment(token)
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
          curSubject = getCollectionName
          triples ::: (TriGTuple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", o, g) :: (TriGTuple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", curSubject, g) :: Nil))
        } else {
          triples :+ TriGTuple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", o, g)
        })
        case TriGTuples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = getCollectionName
          triples ::: (t :+ TriGTuple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", curSubject, curGraph))
        } else {
          triples ::: t
        })
      }
    case Nil ⇒ triples ::: TriGTuple(curSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", curGraph) :: Nil
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

  private def hasScheme(iri: String) = SchemeIdentifier(iri).scheme.run() match {
    case Success(s) ⇒ true
    case _          ⇒ false
  }
}
