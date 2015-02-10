package org.chelona

  import java.io.StringWriter

  import org.parboiled2.ParserInput
  import org.scalatest.FlatSpec

  /**
   * This specification shows how to create examples using the "acceptance" style
   */
  class ChelonaSpec extends FlatSpec {

    "The input file ./TurtleTests/HYPHEN_MINUS_in_localName.ttl" must "succeed" taggedAs (TurtleSyntax) in {

      lazy val input: ParserInput = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.ttl").mkString

      val output = new StringWriter()

      val parser = ChelonaParser(input, output, false)

      assert(parser.turtleDoc.run() == scala.util.Success(1), "Number of triples generated should have been 1")

      val nt = io.Source.fromFile("./TurtleTests/HYPHEN_MINUS_in_localName.nt").mkString

      assert( output.toString == nt.toString )

      output.close()
    }

}
