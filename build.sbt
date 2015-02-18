import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

scalaVersion := "2.11.5"

version := "0.8.0"

val commonSettings = Seq(
  version := "0.8.0",
  scalaVersion := "2.11.5",
  name := "Chelona",
  organization := "org.chelona",
  homepage := Some(new URL("http://chelona.org")),
  description := "W3C RDF 1.1 Turtle Parser",
  startYear := Some(2014),
  licenses := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  javacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-source", "1.6",
    "-target", "1.6",
    "-Xlint:unchecked",
    "-Xlint:deprecation"),
  scalacOptions ++= List(
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-deprecation",
    "-Xlint",
    "-language:_",
    "-target:jvm-1.6"))

val formattingSettings = scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(PreserveDanglingCloseParenthesis, true))
	
/////////////////////// DEPENDENCIES /////////////////////////

val parboiled2       = "org.parboiled"   %% "parboiled"        % "2.1.0-SNAPSHOT"
val scopt            = "com.github.scopt" %% "scopt" % "3.3.0"
//val scalaUri         = "com.netaporter" %% "scala-uri" % "0.4.4"
val scalaTest        = "org.scalatest"   % "scalatest_2.11"    % "2.2.1" % "test"


/////////////////////// PROJECTS /////////////////////////

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += Resolver.sonatypeRepo("public")

test in assembly := {}

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)

libraryDependencies ++= Seq( parboiled2, scopt, scalaTest )
  
lazy val chelona = project.in(file("."))
   .settings(formattingSettings: _*)
   .settings(libraryDependencies ++= Seq(parboiled2))

