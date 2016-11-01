/*
* Copyright (C) 2014-2016 Juergen Pfundt
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

import java.io.Writer

trait RDFTurtleOutput extends RDFReturnType {
  def turtleWriter(bo: Writer)(triple: List[RDFReturnType]): Int = {
    triple.map {
      case RDFReturnType.Triple(s, p, o) ⇒ {
        bo.write(s.value + " " + p.value + " " + o.value + " .\n")
      }
    }.length
  }
}

trait RDFTriGOutput extends RDFReturnType {
  def trigWriter(bo: Writer)(tuple: List[RDFReturnType]): Int = {
    tuple.map {
      case RDFReturnType.Quad(s, p, o, g) ⇒ {
        bo.write(s.value + " " + p.value + " " + o.value + (if (g.value.length > 0) " " + g.value + " .\n" else " .\n"))
      }
    }.length
  }
}

trait RDFNTOutput extends RDFReturnType {
  def ntripleWriter(bo: Writer)(s: Term, p: Term, o: Term, g: Term = defaultGraph): Int = {
    bo.write(s"${s.value} ${p.value} ${o.value} .\n"); 1
  }
}

trait RDFQuadOutput extends RDFReturnType {
  def quadWriter(bo: Writer)(s: Term, p: Term, o: Term, g: Term): Int = {
    bo.write(s"${s.value} ${p.value} ${o.value}" + (if (g.value.isEmpty) " .\n" else s" ${g.value} .\n")); 1
  }
}

trait JSONLDFlatOutput extends RDFReturnType {
  /* Simple JSON output for NTriples */
  val sb: StringBuilder = new StringBuilder()

  def jsonldFlatWriterInit(bo: Writer)(s: String = "[\n"): Unit = {
    bo.write(s)
    if ( sb.nonEmpty) sb.clear()

  }

  def jsonLDFlatWriterTriple(bo: Writer)(triple: List[RDFReturnType]): Int = {
    val writer = jsonLDFlatWriter(bo)_
    triple.map {
      case Triple(s, p, o) ⇒ {
        writer(s, p, o, defaultGraph)
      }
    }.length
  }

  def jsonLDFlatWriterQuad(bo: Writer)(quad: List[RDFReturnType]): Int = {
    val writer = jsonLDFlatWriter(bo)_
    quad.map {
      case Quad(s, p, o, g) ⇒ {
        writer(s, p, o, g)
      }
    }.length
  }

  def jsonLDFlatWriter(bo: Writer)(s: Term, p: Term, o: Term, g: Term = defaultGraph): Int = {

    if ( sb.nonEmpty) {
      sb.append(",\n")
      bo.write(sb.toString())
      sb.clear()
    }

    //[3]	subject	::=	IRIREF | BLANK_NODE_LABEL
    if ( TokenTypes.isIRIREF(s.termType) ) {
      sb.append("  {\n" + """  "@id": """" + s.value.substring(1, s.value.length-1) + """"""" + ",\n")
    }
    else if ( TokenTypes.isBLANK_NODE_LABEL(s.termType)) {
      sb.append("  {\n" + """  "@id": """" + s.value + """"""" + ",\n")
    }

    //[4]	predicate	::=	IRIREF
    if( TokenTypes.isIRIREF(p.termType) ) {
      sb.append("""  """" + p.value.substring(1, p.value.length-1) + """"""" + ":\n")
    }

    //[5]	object	::=	IRIREF | BLANK_NODE_LABEL | STRING_LITERAL_QUOTE ('^^' IRIREF | LANGTAG)?
    if ( TokenTypes.isIRIREF(o.termType) && !TokenTypes.isSTRING_LITERAL_QUOTE(o.termType)) {
      sb.append("""    { "@id": """" + o.value.substring(1, o.value.length-1) + """"""" + "}\n  }")
    }
    else if ( TokenTypes.isBLANK_NODE_LABEL(o.termType)) {
      sb.append("""    { "@id": """" + o.value + """"""" + " }\n  }")
    }
    else if ( TokenTypes.isSTRING_LITERAL_QUOTE(o.termType) ) {
       if ( TokenTypes.isLANGTAG(o.termType)) {
         val parts = o.value.split("@")
         sb.append("    {\n    " + """"@type": """" + parts(1) + """"""" + ",\n")
         sb.append("""    "@value": """ + parts(0) + "\n    }\n  }")
       } else if ( TokenTypes.isLITERALTAG(o.termType)) {
         val parts = o.value.split("\\^\\^")
         sb.append("    {\n    " + """"@type": """" + parts(1).substring(1, parts(1).length-1) + """"""" + ",\n")
         sb.append("""    "@value": """ + parts(0) + "\n    }\n  }")
       }
       else {
         sb.append("""    { "@value": """ + o.value + " }\n  }")
       }
    }
    1
  }

  def jsonldFlatWriterTrailer(bo: Writer)(s: String="\n]"): Unit = {
    if ( sb.nonEmpty) {
      bo.write(sb.toString())
      sb.clear()
    }
    bo.write(s)
  }
}