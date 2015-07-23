package org.chelona

trait TriGReturnValue extends SPOReturnValue {

  case class TrigString(s: String) extends TriGReturnValue

  case class TrigTuple(s: String, p: String, o: String, g: String) extends TriGReturnValue

  case class TrigTuples(values: List[TrigTuple]) extends TriGReturnValue

  case class TrigComment(value: String) extends TriGReturnValue

}

object TriGReturnValue extends TriGReturnValue
