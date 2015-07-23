package org.chelona

trait SPOReturnValue extends RDFReturnType {

  case class SPOString(s: String) extends SPOReturnValue

  case class SPOTriple(s: String, p: String, o: String) extends SPOReturnValue

  case class SPOTriples(values: List[SPOTriple]) extends SPOReturnValue

  case class SPOComment(value: String) extends SPOReturnValue

}

object SPOReturnValue extends SPOReturnValue