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

import scala.annotation.tailrec
import scala.util.Success

object EvalN3 {
  def apply(basePath: String, label: String) = new EvalN3(basePath, label)
}

class EvalN3(basePath: String, label: String) {

  import org.chelona.ChelonaParser._

  val prefixMap2 = scala.collection.mutable.Map.empty[String, String]
  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[String]
  val predicateStack = scala.collection.mutable.Stack.empty[String]

  var curSubject: String = "---Not valid subject---"
  var curPredicate: String = "---Not valid predicate---"
  var aCount = 0
  var bCount = 0
  var cCount = 0

  def renderStatement(ast: TurtleAST, writer: List[SPOTriple] ⇒ Int): Int = {
    (evalStatement(ast): @unchecked) match {
      case SPOTriples(t) ⇒ writer(t)
      case SPOString(s)  ⇒ 0
      case SPOComment(c) ⇒ 0
    }
  }

  def evalStatement(expr: TurtleAST): SPOReturnValue = {

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
          case SPOString(s) ⇒ SPOString("<" + addIriPrefix(s) + ">")
        }
        case ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case ASTIriRef(token) ⇒ SPOString(token)
      case ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match {
            case SPOString(s) ⇒ SPOString("<" + addPrefix(s, "") + ">")
          }
        case ASTPNameLN(p, l) ⇒ evalStatement(rule)
      }
      case ASTDirective(rule) ⇒ evalStatement(rule)
      case ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (SPOString(ps), SPOString(is)) ⇒ definePrefix(ps, is)
        }
        SPOString("")
      case ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case SPOString(bs) ⇒ addBasePrefix(bs)
        }
        SPOString("")
      case ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case SPOString(bs) ⇒ addBasePrefix(bs)
        }
        SPOString("")
      case ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (SPOString(ps), SPOString(is)) ⇒ definePrefix(ps, is)
        }
        SPOString("")
      case ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (SPOTriples(ts), SPOTriples(ps))     ⇒ SPOTriples(ts ::: ps)
          case (SPOString(subject), SPOTriples(ps)) ⇒ SPOTriples(ps)
        }
      case ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = "_:b" + bCount
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
      case ASTIsA(token)       ⇒ SPOString("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")
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
              SPOTriples(SPOTriple(curSubject, curPredicate, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>") :: Nil)
            case _ ⇒
              subjectStack.push(curSubject)
              cCount += 1
              curSubject = getCollectionName
              predicateStack.push(curPredicate)
              curPredicate = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>"
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
          curSubject = "_:b" + bCount
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
        curSubject = getCollectionName
        subjectStack.push(curSubject)
        curPredicate = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>"
        predicateStack.push(curPredicate)
        val res = SPOTriples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case ASTInteger(token)       ⇒ SPOString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#integer>")
      case ASTDecimal(token)       ⇒ SPOString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#decimal>")
      case ASTDouble(token)        ⇒ SPOString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#double>")
      case ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case SPOString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case ASTIri(v) ⇒ SPOString(literal + "^^" + ((evalStatement(postfix): @unchecked) match {
              case SPOString(s) ⇒ s
            }))
            case ASTLangTag(v) ⇒ SPOString(literal + "@" + ((evalStatement(postfix): @unchecked) match {
              case SPOString(s) ⇒ s
            }))
          }
          case None ⇒ evalStatement(string)
        }
      case ASTLangTag(token)                      ⇒ SPOString(token)
      case ASTBooleanLiteral(token)               ⇒ SPOString("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#boolean>")
      case ASTString(rule)                        ⇒ evalStatement(rule)
      case ASTStringLiteralQuote(token)           ⇒ SPOString("\"" + token + "\"")
      case ASTStringLiteralSingleQuote(token)     ⇒ SPOString("\"" + token + "\"")
      case ASTStringLiteralLongSingleQuote(token) ⇒ SPOString("\"" + token + "\"")
      case ASTStringLiteralLongQuote(token)       ⇒ SPOString("\"" + token + "\"")
      case ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix)
          case None            ⇒ SPOString("")
        }
      case ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (SPOString(pname_ns), SPOString(pn_local)) ⇒ SPOString("<" + addPrefix(pname_ns, pn_local) + ">")
        }
      case ASTPNPrefix(token)       ⇒ SPOString(token)
      case ASTPNLocal(token)        ⇒ SPOString(token)
      case ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case ASTBlankNodeLabel(token) ⇒ SPOString(setBlankNodeName("_:" + token))
      case ASTAnon(token) ⇒
        aCount += 1; SPOString("_:a" + label + aCount)
      case ASTComment(token) ⇒ SPOComment(token)
      case ASTBlank(token)   ⇒ SPOString(token)
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
          curSubject = getCollectionName
          triples ::: (SPOTriple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", o) :: (SPOTriple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", curSubject) :: Nil))
        } else {
          triples :+ SPOTriple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", o)
        })
        case SPOTriples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = getCollectionName
          triples ::: (t :+ SPOTriple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", curSubject))
        } else {
          triples ::: t
        })
      }
    case Nil ⇒ triples ::: SPOTriple(curSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>") :: Nil
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
