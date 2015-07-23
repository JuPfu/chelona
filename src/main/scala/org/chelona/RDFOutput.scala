package org.chelona

import java.io.Writer

import org.chelona.SPOReturnValue.SPOTriple
import org.chelona.TriGReturnValue.TrigTuple

trait RDFTripleOutput extends RDFReturnType {
  def tripleWriter(bo: Writer)(triple: List[RDFReturnType]): Int = {
    triple.asInstanceOf[List[SPOTriple]].map(t ⇒ bo.write(t.s + " " + t.p + " " + t.o + " .\n")).length
  }
}

trait RDFTriGOutput extends RDFReturnType {
  def tupleWriter(bo: Writer)(tuple: List[RDFReturnType]): Int = {
    tuple.asInstanceOf[List[TrigTuple]].map(t ⇒ bo.write(t.s + " " + t.p + " " + t.o + (if (t.g != "") " " + t.g + " .\n" else " .\n"))).length
  }
}
