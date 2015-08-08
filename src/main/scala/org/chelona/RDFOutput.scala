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

import java.io.Writer

import org.chelona.SPOReturnValue.SPOTriple
import org.chelona.TriGReturnValue.TriGTuple

trait RDFTripleOutput extends RDFReturnType {
  def tripleWriter(bo: Writer)(triple: List[RDFReturnType]): Int = {
    triple.asInstanceOf[List[SPOTriple]].map(t ⇒ bo.write(t.s + " " + t.p + " " + t.o + " .\n")).length
  }
}

trait RDFTriGOutput extends RDFReturnType {
  def tupleWriter(bo: Writer)(tuple: List[RDFReturnType]): Int = {
    tuple.asInstanceOf[List[TriGTuple]].map(t ⇒ bo.write(t.s + " " + t.p + " " + t.o + (if (t.g != "") " " + t.g + " .\n" else " .\n"))).length
  }
}

trait RDFNTOutput extends RDFReturnType {
  def ntWriter(bo: Writer)(spo: String*): Int = {
    bo.write(spo(0) + " " + spo(1) + " " + spo(2) + " .\n"); 1
  }
}

trait RDFQuadOutput extends RDFReturnType {
  def quadWriter(bo: Writer)(spog: String*): Int = {
    bo.write(spog(0) + " " + spog(1) + " " + spog(2) + (if (spog(3) != "") " " + spog(3) + " .\n" else " .\n")); 1
  }
}