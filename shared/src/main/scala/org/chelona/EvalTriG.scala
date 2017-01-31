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

object EvalTriG {
  def apply(output: List[RDFReturnType] ⇒ Int, basePath: String, label: String) = new EvalTriG(output, basePath, label)
}

class EvalTriG(output: List[RDFReturnType] ⇒ Int, basePath: String, label: String) {

  import TriGAST._

  val prefixMap = scala.collection.mutable.Map.empty[String, String]
  val blankNodeMap = scala.collection.mutable.Map.empty[String, String]
  val subjectStack = scala.collection.mutable.Stack.empty[Term]
  val predicateStack = scala.collection.mutable.Stack.empty[Term]

  var curSubject: Term = Term("---Not valid subject---", RDFTokenTypes.STRING_LITERAL)
  var curPredicate: Term = Term("---Not valid predicate---", RDFTokenTypes.STRING_LITERAL)
  var curGraph: Term = Term("", RDFTokenTypes.STRING_LITERAL)

  var aCount = 0
  var bCount = 0
  var cCount = 0

  def renderStatement(ast: TurtleType): Int = {
    (evalStatement(ast): @unchecked) match {
      case RDFTuples(t)  ⇒ output(t)
      case RDFString(s)  ⇒ 0
      case RDFComment(c) ⇒ 0
      case _             ⇒ 0
    }
  }

  def evalStatement(expr: TurtleType): RDFReturnType = {

    expr match {
      // evalStatement is called for each statement seperatedly
      // case ASTTrigDoc(rule) ⇒ evalStatement(rule)
      case ASTStatement(rule) ⇒
        /* some clean up at the beginning of a new trig statement */
        subjectStack.clear
        predicateStack.clear
        /* evaluate a trig statement */
        evalStatement(rule)
      case ASTBlock(rule) ⇒
        rule match {
          case ASTWrappedGraph(tb) ⇒ curGraph = Term("", RDFTokenTypes.STRING_LITERAL)
          case _                   ⇒
        }
        evalStatement(rule)
      case ASTLabelOrSubjectBlock(l, s) ⇒
        evalStatement(l); evalStatement(s)
      case ASTTriplesOrGraph(l, rule) ⇒
        evalStatement(l)
        rule match { case TurtleAST.ASTPredicateObjectList(po) ⇒ curGraph = Term("", RDFTokenTypes.STRING_LITERAL); case _ ⇒ }
        evalStatement(rule)
      case ASTTriple2BlankNodePropertyList(b, p) ⇒
        curGraph = Term("", RDFTokenTypes.STRING_LITERAL)
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = Term("_:b" + bCount, RDFTokenTypes.BLANK_NODE_LABEL)
        val sub = evalStatement(b)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (RDFTuples(ts), RDFTuples(ps)) ⇒ RDFTuples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case ASTTriple2Collection(c, p) ⇒
        curGraph = Term("", RDFTokenTypes.STRING_LITERAL)
        cCount += 1
        ((evalStatement(c), evalStatement(p)): @unchecked) match {
          case (RDFTuples(cs), RDFTuples(ps)) ⇒ RDFTuples(cs ::: ps)
        }
      case ASTWrappedGraph(t) ⇒ t match {
        case Some(g) ⇒ evalStatement(g)
        case None    ⇒ RDFNone()
      }
      case ASTTriplesBlock(t) ⇒ RDFTuples(traverseTriples(t, Nil))
      case ASTLabelOrSubject(ls) ⇒
        val l = evalStatement(ls)
        (l: @unchecked) match { case RDFString(label) ⇒ curGraph = label; curSubject = label }
        l
      case TurtleAST.ASTIri(rule) ⇒ (rule: @unchecked) match {
        case TurtleAST.ASTIriRef(i) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(Term(s, t)) ⇒ RDFString(Term("<" + addIriPrefix(s) + ">", t | RDFTokenTypes.IRIREF))
        }
        case TurtleAST.ASTPrefixedName(n) ⇒ evalStatement(rule)
      }
      case TurtleAST.ASTIriRef(token) ⇒ RDFString(Term(token, RDFTokenTypes.IRIREF))
      case TurtleAST.ASTPrefixedName(rule) ⇒ (rule: @unchecked) match {
        case TurtleAST.ASTPNameNS(p) ⇒
          (evalStatement(rule): @unchecked) match {
            case RDFString(Term(s, t)) ⇒ RDFString(Term("<" + addPrefix(s, "") + ">", t | RDFTokenTypes.PNAMENS))
          }
        case TurtleAST.ASTPNameLN(p, l) ⇒ evalStatement(rule)
      }
      case TurtleAST.ASTDirective(rule) ⇒ evalStatement(rule)
      case TurtleAST.ASTPrefixID(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (RDFString(ps), RDFString(is)) ⇒ definePrefix(ps.value, is.value)
        }
        RDFNone()
      case TurtleAST.ASTBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case RDFString(bs) ⇒ addBasePrefix(bs.value)
        }
        RDFNone()
      case TurtleAST.ASTSparqlBase(rule) ⇒
        (evalStatement(rule): @unchecked) match {
          case RDFString(bs) ⇒ addBasePrefix(bs.value)
        }
        RDFNone()
      case TurtleAST.ASTSparqlPrefix(p, i) ⇒
        ((evalStatement(p), evalStatement(i)): @unchecked) match {
          case (RDFString(ps), RDFString(is)) ⇒ definePrefix(ps.value, is.value)
        }
        RDFNone()
      case TurtleAST.ASTTriples(s, p) ⇒
        ((evalStatement(s), evalStatement(p)): @unchecked) match {
          case (RDFTuples(ts), RDFTuples(ps))      ⇒ RDFTuples(ts ::: ps)
          case (RDFString(subject), RDFTuples(ps)) ⇒ RDFTuples(ps)
        }
      case TurtleAST.ASTBlankNodeTriples(s, p) ⇒
        subjectStack.push(curSubject)
        predicateStack.push(curPredicate)
        bCount += 1
        curSubject = Term("_:b" + bCount, RDFTokenTypes.BLANK_NODE_LABEL)
        val sub = evalStatement(s)
        val retval = p match {
          case Some(po) ⇒ ((sub, evalStatement(po)): @unchecked) match {
            case (RDFTuples(ts), RDFTuples(ps)) ⇒ RDFTuples(ts ::: ps)
          }
          case None ⇒ sub
        }
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        retval
      case TurtleAST.ASTPredicateObjectList(predicateObjectlist) ⇒
        predicateObjectlist match {
          case po ⇒ RDFTuples(traversePredicateObjectList(po, Nil))
        }
      case TurtleAST.ASTPo(verb, obj) ⇒
        (evalStatement(verb): @unchecked) match {
          case RDFString(token) ⇒ curPredicate = token
        }
        evalStatement(obj)
      case TurtleAST.ASTObjectList(rule) ⇒ RDFTuples(traverseTriples(rule, Nil))
      case TurtleAST.ASTVerb(rule)       ⇒ evalStatement(rule)
      case TurtleAST.ASTIsA(token)       ⇒ RDFString(Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>", RDFTokenTypes.ISA))
      case TurtleAST.ASTSubject(rule) ⇒ (rule: @unchecked) match {
        case TurtleAST.ASTIri(i) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(Term(s, t)) ⇒ curSubject = Term(s, t | RDFTokenTypes.IRIREF); RDFString(curSubject)
        }
        case TurtleAST.ASTBlankNode(b) ⇒ (evalStatement(rule): @unchecked) match {
          case RDFString(Term(s, t)) ⇒ curSubject = Term(s, t | RDFTokenTypes.BLANK_NODE_LABEL); RDFString(curSubject)
        }
        case TurtleAST.ASTCollection(c) ⇒ cCount += 1; evalStatement(rule)
      }
      case TurtleAST.ASTPredicate(rule) ⇒
        val predicate = evalStatement(rule)
        (predicate: @unchecked) match {
          case RDFString(s) ⇒ curPredicate = s
        }
        predicate
      case TurtleAST.ASTObject(l) ⇒ (l: @unchecked) match {
        case TurtleAST.ASTIri(v) ⇒ (evalStatement(l): @unchecked) match {
          case RDFString(o) ⇒ RDFQuad(curSubject, curPredicate, o, curGraph);
        }
        case TurtleAST.ASTBlankNode(v) ⇒ (evalStatement(l): @unchecked) match {
          case RDFString(o) ⇒ RDFQuad(curSubject, curPredicate, o, curGraph);
        }
        case TurtleAST.ASTLiteral(literal) ⇒
          (evalStatement(l): @unchecked) match {
            case RDFString(o) ⇒ RDFQuad(curSubject, curPredicate, o, curGraph);
          }
        case TurtleAST.ASTCollection(v) ⇒
          l match {
            case TurtleAST.ASTCollection(Vector()) ⇒
              // empty collection
              RDFTuples(RDFQuad(curSubject, curPredicate, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", RDFTokenTypes.IRIREF), curGraph) :: Nil)
            case _ ⇒
              subjectStack.push(curSubject)
              cCount += 1
              curSubject = Term(getCollectionName, RDFTokenTypes.BLANK_NODE_LABEL)
              predicateStack.push(curPredicate)
              curPredicate = Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", RDFTokenTypes.IRIREF)
              (evalStatement(l): @unchecked) match {
                case RDFTuples(t) ⇒
                  val oldSubject = curSubject
                  curSubject = subjectStack.pop
                  curPredicate = predicateStack.pop
                  RDFTuples(RDFQuad(curSubject, curPredicate, oldSubject, curGraph) :: t)
              }
          }
        case TurtleAST.ASTBlankNodePropertyList(v) ⇒
          subjectStack.push(curSubject)
          predicateStack.push(curPredicate)
          bCount += 1
          curSubject = Term("_:b" + bCount, RDFTokenTypes.BLANK_NODE_LABEL)
          val bnode = curSubject
          (evalStatement(l): @unchecked) match {
            case RDFTuples(t) ⇒
              curSubject = subjectStack.pop
              curPredicate = predicateStack.pop
              RDFTuples(RDFQuad(curSubject, curPredicate, bnode, curGraph) :: t)
          }
      }
      case TurtleAST.ASTLiteral(rule)               ⇒ evalStatement(rule)
      case TurtleAST.ASTBlankNodePropertyList(rule) ⇒ evalStatement(rule)
      case TurtleAST.ASTCollection(rule) ⇒
        curSubject = Term(getCollectionName, RDFTokenTypes.BLANK_NODE_LABEL)
        subjectStack.push(curSubject)
        curPredicate = Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", RDFTokenTypes.IRIREF)
        predicateStack.push(curPredicate)
        val res = RDFTuples(traverseCollection(rule, Nil))
        curSubject = subjectStack.pop
        curPredicate = predicateStack.pop
        res
      case TurtleAST.ASTNumericLiteral(rule) ⇒ evalStatement(rule)
      case TurtleAST.ASTInteger(token)       ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#integer>", RDFTokenTypes.INTEGER))
      case TurtleAST.ASTDecimal(token)       ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#decimal>", RDFTokenTypes.DECIMAL))
      case TurtleAST.ASTDouble(token)        ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#double>", RDFTokenTypes.DOUBLE))
      case TurtleAST.ASTRdfLiteral(string, optionalPostfix) ⇒
        val literal = (evalStatement(string): @unchecked) match {
          case RDFString(s) ⇒ s
        }
        (optionalPostfix: @unchecked) match {
          case Some(postfix) ⇒ (postfix: @unchecked) match {
            case TurtleAST.ASTIri(v) ⇒ RDFString(Term(literal.value + "^^" + ((evalStatement(postfix): @unchecked) match {
              case RDFString(Term(s, t)) ⇒ s
            }), RDFTokenTypes.IRIREF))
            case TurtleAST.ASTLangTag(v) ⇒ RDFString(Term(literal.value + "@" + ((evalStatement(postfix): @unchecked) match {
              case RDFString(Term(s, t)) ⇒ s
            }), RDFTokenTypes.LANGTAG))
          }
          case None ⇒ evalStatement(string)
        }
      case TurtleAST.ASTLangTag(token)                      ⇒ RDFString(Term(token, RDFTokenTypes.LANGTAG))
      case TurtleAST.ASTBooleanLiteral(token)               ⇒ RDFString(Term("\"" + token + "\"^^<http://www.w3.org/2001/XMLSchema#boolean>", RDFTokenTypes.BOOLEAN_LITERAL))
      case TurtleAST.ASTString(rule)                        ⇒ evalStatement(rule)
      case TurtleAST.ASTStringLiteralQuote(token)           ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL_QUOTE))
      case TurtleAST.ASTStringLiteralSingleQuote(token)     ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL_SINGLE_QUOTE))
      case TurtleAST.ASTStringLiteralLongSingleQuote(token) ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL_LONG_SINGLE_QUOTE))
      case TurtleAST.ASTStringLiteralLongQuote(token)       ⇒ RDFString(Term("\"" + token + "\"", RDFTokenTypes.STRING_LITERAL_LONG_QUOTE))
      case TurtleAST.ASTPNameNS(prefix) ⇒
        prefix match {
          case Some(pn_prefix) ⇒ evalStatement(pn_prefix)
          case None            ⇒ RDFString(Term("", RDFTokenTypes.PNAMENS))
        }
      case TurtleAST.ASTPNameLN(namespace, local) ⇒
        ((evalStatement(namespace), evalStatement(local)): @unchecked) match {
          case (RDFString(pname_ns), RDFString(pn_local)) ⇒ RDFString(Term("<" + addPrefix(pname_ns.value, pn_local.value) + ">", RDFTokenTypes.PNAMELN))
        }
      case TurtleAST.ASTPNPrefix(token)       ⇒ RDFString(Term(token, RDFTokenTypes.PNPREFIX))
      case TurtleAST.ASTPNLocal(token)        ⇒ RDFString(Term(token, RDFTokenTypes.PNLOCAL))
      case TurtleAST.ASTBlankNode(rule)       ⇒ evalStatement(rule)
      case TurtleAST.ASTBlankNodeLabel(token) ⇒ RDFString(Term(setBlankNodeName("_:" + token), RDFTokenTypes.BLANK_NODE_LABEL))
      case TurtleAST.ASTAnon(token) ⇒
        aCount += 1; RDFString(Term("_:a" + label + aCount, RDFTokenTypes.ANON))
      case TurtleAST.ASTComment(token) ⇒ RDFComment(Term(token, RDFTokenTypes.COMMENT))
    }
  }

  @tailrec
  private def traversePredicateObjectList(l: Seq[TurtleAST], triples: List[RDFQuad]): List[RDFQuad] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case RDFTuples(tl) ⇒ traversePredicateObjectList(xs, triples ::: tl)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseTriples(l: Seq[TurtleAST], triples: List[RDFQuad]): List[RDFQuad] = l match {
    case x +: xs ⇒ (evalStatement(x): @unchecked) match {
      case RDFQuad(s, p, o, g) ⇒ traverseTriples(xs, triples :+ RDFQuad(s, p, o, g))
      case RDFTuples(t)        ⇒ traverseTriples(xs, triples ::: t)
    }
    case Nil ⇒ triples
  }

  @tailrec
  private def traverseCollection(l: Seq[TurtleAST], triples: List[RDFQuad]): List[RDFQuad] = l match {
    case x +: xs ⇒
      val oldSubject = curSubject
      (evalStatement(x): @unchecked) match {
        case RDFQuad(s, p, o, g) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = Term(getCollectionName, RDFTokenTypes.BLANK_NODE_LABEL)
          triples ::: (RDFQuad(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", RDFTokenTypes.IRIREF), o, g) :: (RDFQuad(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", RDFTokenTypes.IRIREF), curSubject, g) :: Nil))
        } else {
          triples :+ RDFQuad(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#first>", RDFTokenTypes.IRIREF), o, g)
        })
        case RDFTuples(t) ⇒ traverseCollection(xs, if (xs != Nil) {
          cCount += 1
          curSubject = Term(getCollectionName, RDFTokenTypes.BLANK_NODE_LABEL)
          triples ::: (t :+ RDFQuad(oldSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", RDFTokenTypes.IRIREF), curSubject, curGraph))
        } else {
          triples ::: t
        })
      }
    case Nil ⇒ triples ::: RDFQuad(curSubject, Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#rest>", RDFTokenTypes.IRIREF), Term("<http://www.w3.org/1999/02/22-rdf-syntax-ns#nil>", RDFTokenTypes.IRIREF), curGraph) :: Nil
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

  private def hasScheme(iri: String) = new SchemeIdentifier(iri).scheme
}
