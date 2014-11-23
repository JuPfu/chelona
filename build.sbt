import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

scalaVersion := "2.11.2"

val commonSettings = Seq(
  version := "0.8.0",
  scalaVersion := "2.11.2",
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
    "-encoding", "UTF-8",
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

val parboiled2       = "org.parboiled"   %% "parboiled"        % "2.0.1"
val scalaTest        = "org.scalatest"   % "scalatest_2.11"    % "2.2.1" % "test"

/////////////////////// PROJECTS /////////////////////////
scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)

libraryDependencies ++= Seq( parboiled2, scalaTest )
//  "org.parboiled" %% "parboiled" % "2.0.1",
//  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
//)
  
lazy val root = project.in(file("."))
   .settings(formattingSettings: _*)
   .settings(libraryDependencies ++= Seq(parboiled2,scalaTest))
//  .aggregate(chelona)
//
//lazy val chelona = project
//   .settings(formattingSettings: _*)
   .settings(libraryDependencies ++= Seq(parboiled2,scalaTest))
