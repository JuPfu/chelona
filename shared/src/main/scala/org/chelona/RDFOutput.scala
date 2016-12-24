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

trait RDFTurtleOutput {

  def turtleWriter(bo: Writer)(triple: List[RDFReturnType]): Int = {
    triple.map {
      case RDFTriple(s, p, o) ⇒ bo.write(s.value + " " + p.value + " " + o.value + " .\n")
    }.length
  }
}

object RDFTurtleOutput extends RDFTurtleOutput

trait RDFTriGOutput {
  def trigWriter(bo: Writer)(tuple: List[RDFReturnType]): Int = {
    tuple.map {
      case RDFQuad(s, p, o, g) ⇒ {
        bo.write(s.value + " " + p.value + " " + o.value + (if (g.value.length > 0) " " + g.value + " .\n" else " .\n"))
      }
    }.length
  }
}

object RDFTriGOutput extends RDFTriGOutput

trait RDFNTOutput {
  def ntripleWriter(bo: Writer)(s: Term, p: Term, o: Term, g: Term = defaultGraph): Int = {
    bo.write(s"${s.value} ${p.value} ${o.value} .\n"); 1
  }
}

object RDFNTOutput extends RDFNTOutput

trait RDFQuadOutput {
  def quadWriter(bo: Writer)(s: Term, p: Term, o: Term, g: Term): Int = {
    bo.write(s"${s.value} ${p.value} ${o.value}" + (if (g.value.isEmpty) " .\n" else s" ${g.value} .\n")); 1
  }
}

object RDFQuadOutput extends RDFQuadOutput

trait JSONLDFlatOutput {
  /* Simple JSON output */
  val sb: StringBuilder = new StringBuilder()

  def jsonldFlatWriterInit(bo: Writer)(s: String = "[\n"): Unit = {
    bo.write(s)
    if (sb.nonEmpty) sb.clear()

  }

  def jsonLDFlatWriterTriple(bo: Writer)(triple: List[RDFReturnType]): Int = {
    val writer = jsonLDFlatWriter(bo)_
    triple.map {
      case RDFTriple(s, p, o) ⇒ {
        writer(s, p, o, defaultGraph)
      }
    }.length
  }

  def jsonLDFlatWriterQuad(bo: Writer)(quad: List[RDFReturnType]): Int = {
    val writer = jsonLDFlatWriter(bo)_
    quad.map {
      case RDFQuad(s, p, o, g) ⇒ {
        writer(s, p, o, g)
      }
    }.length
  }

  def jsonLDFlatWriter(bo: Writer)(s: Term, p: Term, o: Term, g: Term = defaultGraph): Int = {

    if (sb.nonEmpty) {
      sb.append(",\n")
      bo.write(sb.toString())
      sb.clear()
    }

    //[3]	subject	::=	IRIREF | BLANK_NODE_LABEL
    if (RDFTokenTypes.isIRIREF(s.termType)) {
      sb.append("  {\n" + """  "@id": """" + s.value.substring(1, s.value.length - 1) + """"""" + ",\n")
    } else if (RDFTokenTypes.isBLANK_NODE_LABEL(s.termType)) {
      sb.append("  {\n" + """  "@id": """" + s.value + """"""" + ",\n")
    }

    //[4]	predicate	::=	IRIREF
    if (RDFTokenTypes.isIRIREF(p.termType)) {
      sb.append("""  """" + p.value.substring(1, p.value.length - 1) + """"""" + ":\n")
    }

    //[5]	object	::=	IRIREF | BLANK_NODE_LABEL | STRING_LITERAL_QUOTE ('^^' IRIREF | LANGTAG)?
    if (RDFTokenTypes.isIRIREF(o.termType) && !RDFTokenTypes.isSTRING_LITERAL_QUOTE(o.termType)) {
      sb.append("""    { "@id": """" + o.value.substring(1, o.value.length - 1) + """"""" + "}\n  }")
    } else if (RDFTokenTypes.isBLANK_NODE_LABEL(o.termType)) {
      sb.append("""    { "@id": """" + o.value + """"""" + " }\n  }")
    } else if (RDFTokenTypes.isSTRING_LITERAL_QUOTE(o.termType)) {
      if (RDFTokenTypes.isLANGTAG(o.termType)) {
        val parts = o.value.split("@")
        sb.append("    {\n    " + """"@type": """" + parts(1) + """"""" + ",\n")
        sb.append("""    "@value": """ + parts(0) + "\n    }\n  }")
      } else if (RDFTokenTypes.isLITERALTAG(o.termType)) {
        val parts = o.value.split("\\^\\^")
        sb.append("    {\n    " + """"@type": """" + parts(1).substring(1, parts(1).length - 1) + """"""" + ",\n")
        sb.append("""    "@value": """ + parts(0) + "\n    }\n  }")
      } else {
        sb.append("""    { "@value": """ + o.value + " }\n  }")
      }
    }
    1
  }

  def jsonldFlatWriterTrailer(bo: Writer)(s: String = "\n]"): Unit = {
    if (sb.nonEmpty) {
      bo.write(sb.toString())
      sb.clear()
    }
    bo.write(s)
  }
}

object JSONLDFlatOutput extends JSONLDFlatOutput