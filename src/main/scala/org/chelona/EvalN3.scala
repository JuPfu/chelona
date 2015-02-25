/*
* Copyright (C) 2014 Juergen Pfundt
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

import org.parboiled2.{ ParseError, ParserInput }

import scala.annotation.tailrec
import scala.util.{ Failure, Success }

object EvalN3 {
  import org.chelona.ChelonaParser._

  def evalStatement(expr: AST): SPOReturnValue = {

    expr match {
      case ASTTurtleDoc(rule) ⇒ evalStatement(rule)
      case ASTStatement(rule) ⇒
        /* some clean up at the beginning of a new turtle statement */
        subjectStack.clear
        predicateStack.clear
        /* evaluate a turtle statement */
        evalStatement(rule)
      case ASTIri(rule) ⇒ (rule: @unchecked) match {
        case ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ SPOString("<" + addPrefix("", s) + ">")
        }
        case ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case ASTIriRef(token) ⇒ SPOString(token)
      case ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match { case SPOString(s) ⇒ SPOString("<" + addPrefix(s, "") + ">") }
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
      case ASTPredicateObjectList(predicateObject, predicateObjectlist) ⇒
        (evalStatement(predicateObject): @unchecked) match {
          case SPOTriples(t) ⇒ predicateObjectlist match {
            case Some(po) ⇒ SPOTriples(t ::: traversePredicateObjectList(po, Nil))
            case None     ⇒ SPOTriples(t)
          }
        }
      case ASTPo(verb, obj) ⇒
        (evalStatement(verb): @unchecked) match {
          case SPOString(token) ⇒ curPredicate = token
        }
        evalStatement(obj)
      case ASTObjectList(rule) ⇒ SPOTriples(traverseObjectList(rule, Nil))
      case ASTVerb(rule)       ⇒ evalStatement(rule)
      case ASTIsA(token)       ⇒ SPOString("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")
      case ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ curSubject = s; SPOString(curSubject)
        }
        case ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case SPOString(s) ⇒ curSubject = s; SPOString(curSubject)
        }
        case ASTCollection(c) ⇒ evalStatement(rule)
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
        case ASTLiteral(l) ⇒
          (evalStatement(l): @unchecked) match {
            case SPOString(o) ⇒ SPOTriple(curSubject, curPredicate, o);
          }
        case ASTCollection(v) ⇒
          subjectStack.push(curSubject)
          cCount += 1
          curSubject = "_:c" + cCount
          predicateStack.push(curPredicate)
          curPredicate = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>"
          (evalStatement(l): @unchecked) match {
            case SPOTriples(t) ⇒
              val oldSubject = curSubject
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              SPOTriples(SPOTriple(curSubject, curPredicate, oldSubject) :: t)
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
        curSubject = "_:c" + cCount
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
        aCount += 1; SPOString("_:a" + aCount)
      case ASTComment(rule) ⇒ SPOComment(rule)
    }
  }

  @tailrec
  private def traversePredicateObjectList(l: Seq[Option[AST]], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case Nil ⇒ triples
    case x +: xs ⇒ x match {
      case Some(po) ⇒ (evalStatement(po): @unchecked) match {
        case SPOTriples(tl) ⇒ traversePredicateObjectList(xs, triples ::: tl)
      }
      case None ⇒ triples
    }
  }

  @tailrec
  private def traverseObjectList(l: Seq[AST], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case Nil ⇒ triples
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case SPOTriple(s, p, o) ⇒ traverseObjectList(xs, triples :+ SPOTriple(s, p, o))
      case SPOTriples(t)      ⇒ traverseObjectList(xs, triples ::: t)
    }
  }

  @tailrec
  private def traverseCollection(l: Seq[AST], triples: List[SPOTriple]): List[SPOTriple] = l match {
    case Nil ⇒ triples ::: SPOTriple(curSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>") :: Nil
    case x +: xs ⇒
      val oldSubject = curSubject
      if (xs != Nil) {
        cCount += 1
        curSubject = "_:c" + cCount
      }
      (evalStatement(x): @unchecked) match {
        case SPOTriple(s, p, o) ⇒ traverseCollection(xs, if (xs != Nil) {
          triples ::: (SPOTriple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", o) :: (SPOTriple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", curSubject) :: Nil))
        } else {
          triples :+ SPOTriple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", o)
        })
        case SPOTriples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          triples ::: (t :+ SPOTriple(oldSubject, "<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", curSubject))
        } else {
          triples ::: t
        })
      }
  }

  private def definePrefix(key: String, value: String) = {
    if (value.startsWith("//") || hasScheme(value))
      prefixMap2 += key -> value
    else if (value.endsWith("/")) {
      if (!prefixMap2.contains(key))
        prefixMap2 += key -> value
      else
        prefixMap2 += key -> (prefixMap2.getOrElse(key, "http://chelona.org") + value)
    } else if (value.endsWith("#")) prefixMap2 += key -> (prefixMap2.getOrElse(key, "http://chelona.org") + value)
    else prefixMap2 += key -> value
  }

  private def addPrefix(pname_ns: String, pn_local: String): String = {
    if (pname_ns != "" || (!pn_local.startsWith("/") && !hasScheme(pn_local))) {
      val prefix = prefixMap2.getOrElse(pname_ns, "http://chelona.org")
      if (prefix.endsWith("/") || prefix.endsWith("#")) {
        prefix + pn_local
      } else {
        prefix + "/" + pn_local
      }
    } else {
      pn_local
    }
  }

  private def addBasePrefix(iri: String) = {
    val p = if (!iri.startsWith("/") && !hasScheme(iri)) {
      val prefix = prefixMap2.getOrElse("", "http://chelona.org")
      if (prefix.endsWith("/") || prefix.endsWith("#"))
        prefixMap2 += "" -> (prefix + iri)
      else if (prefix.length > 0)
        prefixMap2 += "" -> (prefix + "/" + iri)
      else prefixMap2 += "" -> iri
    } else prefixMap2 += "" -> iri
    p.getOrElse("", "http://chelona.org")
  }

  private def setBlankNodeName(key: String) = {
    if (!blankNodeMap.contains(key)) {
      bCount += 1
      blankNodeMap += key -> ("_:b" + bCount)
    }
    blankNodeMap.getOrElse(key, "This should never be returned")
  }

  private def hasScheme(iri: String) = {
    lazy val input: ParserInput = iri

    val parser = SchemeIdentifier(input)

    parser.scheme.run() match {
      case Success(s)             ⇒ true
      case Failure(e: ParseError) ⇒ false
      case Failure(e)             ⇒ false
    }
  }
}
