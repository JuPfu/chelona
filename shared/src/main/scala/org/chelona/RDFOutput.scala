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

import org.chelona.TurtleReturnValue.TurtleTriple
import org.chelona.TriGReturnValue.TriGTuple

trait RDFTurtleOutput extends RDFReturnType {
  def tripleWriter(bo: Writer)(triple: List[RDFReturnType]): Int = {
    triple.map { case TurtleTriple(s, p, o) ⇒ { bo.write(s.text + " " + p.text + " " + o.text + " .\n") } }.length
  }
}

trait RDFTriGOutput extends RDFReturnType {
  def tupleWriter(bo: Writer)(tuple: List[RDFReturnType]): Int = {
    tuple.map {
      case TriGTuple(s, p, o, g) ⇒ {
        bo.write(s.text + " " + p.text + " " + o.text + (if (g.text.length > 0) " " + g.text + " .\n" else " .\n"))
      }
    }.length
  }
}

trait RDFNTOutput extends RDFReturnType {
  def ntWriter(bo: Writer)(s: NTripleElement, p: NTripleElement, o: NTripleElement): Int = {
    bo.write(s"${s.text} ${p.text} ${o.text} .\n"); 1
  }
}

trait RDFQuadOutput extends RDFReturnType {
  def quadWriter(bo: Writer)(s: NQuadElement, p: NQuadElement, o: NQuadElement, g: NQuadElement): Int = {
    bo.write(s"${s.text} ${p.text} ${o.text}" + (if (g.text.isEmpty) " .\n" else s" ${g.text} .\n")); 1
  }
}

trait JSONLDOutput extends RDFReturnType {
  /* Simple JSON output for NTriples */
  val sb: StringBuilder = new StringBuilder()

  def jsonWriterInit(bo: Writer)(s: String = "[\n"): Unit = {
    bo.write(s)
    if ( sb.nonEmpty) {
      sb.clear()
    }
  }

  def jsonWriter(bo: Writer)(s: NTripleElement, p: NTripleElement, o: NTripleElement): Int = {

    if ( sb.nonEmpty) {
      sb.append(",\n")
      bo.write(sb.toString())
      sb.clear()
    }

    //[3]	subject	::=	IRIREF | BLANK_NODE_LABEL
    if ( NTripleBitValue.isIRIREF(s.tokenType) ) {
      sb.append("  {\n" + """  "@id": """" + s.text.substring(1, s.text.length-1) + """"""" + ",\n")
    }
    else if ( NTripleBitValue.isBLANK_NODE_LABEL(s.tokenType)) {
      sb.append("  {\n" + """  "@id": """" + s.text + """"""" + ",\n")
    }

    //[4]	predicate	::=	IRIREF
    if( NTripleBitValue.isIRIREF(p.tokenType) ) {
      sb.append("""  """" + p.text.substring(1, p.text.length-1) + """"""" + ":\n")
    }

    //[5]	object	::=	IRIREF | BLANK_NODE_LABEL | STRING_LITERAL_QUOTE ('^^' IRIREF | LANGTAG)?
    if ( NTripleBitValue.isIRIREF(o.tokenType) && !NTripleBitValue.isSTRING_LITERAL_QUOTE(o.tokenType)) {
      sb.append("""    { "@id": """" + o.text.substring(1, o.text.length-1) + """"""" + "}\n  }")
    }
    else if ( NTripleBitValue.isBLANK_NODE_LABEL(o.tokenType)) {
      sb.append("""    { "@id": """" + o.text + """"""" + " }\n  }")
    }
    else if ( NTripleBitValue.isSTRING_LITERAL_QUOTE(o.tokenType) ) {
       if ( NTripleBitValue.isLANGTAG(o.tokenType)) {
         val parts = o.text.split("@")
         sb.append("    {\n    " + """"@type": """" + parts(1) + """"""" + ",\n")
         sb.append("""    "@value": """ + parts(0) + "\n    }\n  }")
       } else if ( NTripleBitValue.isLITERALTAG(o.tokenType)) {
         val parts = o.text.split("\\^\\^")
         sb.append("    {\n    " + """"@type": """" + parts(1).substring(1, parts(1).length-1) + """"""" + ",\n")
         sb.append("""    "@value": """ + parts(0) + "\n    }\n  }")
       }
       else {
         sb.append("""    { "@value": """ + o.text + " }\n  }")
       }
    }

    1
  }

  def jsonWriterTrailer(bo: Writer)(s: String="\n]"): Unit = {
    if ( sb.nonEmpty) {
      bo.write(sb.toString())
      sb.clear()
    }

    bo.write(s)
  }
}