import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

val commonSettings = Seq(
  version := "1.1.0",
  scalaVersion := "2.11.7",
  name := "Chelona",
  organization := "org.jupfu",
  homepage := Some(new URL("http://github.com/JuPfu/chelona")),
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
    "-Yopt:_",
    "-language:_",
    "-target:jvm-1.6"))

val formattingSettings = scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(PreserveDanglingCloseParenthesis, true))

useGpg := true

/////////////////////// DEPENDENCIES /////////////////////////

val parboiled2 = "org.parboiled" %% "parboiled" % "2.1.0"
val scopt = "com.github.scopt" %% "scopt" % "3.3.0"
val scalaTest = "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

/////////////////////// PROJECTS /////////////////////////

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += Resolver.sonatypeRepo("public")

parallelExecution in Test := true

test in assembly := {}

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)

libraryDependencies ++= Seq(parboiled2, scopt, scalaTest)

lazy val chelona = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(formattingSettings: _*)
  .settings(publishingSettings: _*)
  .settings(libraryDependencies ++= Seq(parboiled2))

/////////////////////// PUBLISH /////////////////////////

val publishingSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  useGpg := true,
  publishTo <<= version { v: String =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
                        },
  pomIncludeRepository := { _ => false },
  pomExtra :=
      <scm>
        <url>git@github.com:jupfu/chelona.git</url>
        <connection>scm:git:git@github.com:jupfu/chelona.git</connection>
      </scm>
      <developers>
        <developer>
          <id>jupfu</id>
          <name>Jürgen Pfundt</name>
          <url>http://github.com/jupfu</url>
        </developer>
      </developers>)
