import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

val commonSettings = Seq(
  version := "1.2.0",
  scalaVersion := "2.11.8",
  name := "Chelona",
  organization := "com.github.jupfu",
  homepage := Some(new URL("http://github.com/JuPfu/chelona")),
  description := "W3C RDF 1.1 Turtle Parser",
  startYear := Some(2014),
  licenses := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  javacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-source", "1.8",
    "-target", "1.8",
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
    "-target:jvm-1.8"))

val formattingSettings = scalariformSettings ++ Seq(
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(PreserveDanglingCloseParenthesis, true))

/////////////////////// PROJECTS /////////////////////////

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"

resolvers += Resolver.sonatypeRepo("public")

resolvers += Resolver.typesafeRepo("releases")

parallelExecution in Test := true

test in assembly := {}

scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(RewriteArrowSymbols, true)
  .setPreference(AlignParameters, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(PreserveDanglingCloseParenthesis, true)


lazy val chelona = crossProject.in(file("."))
  .settings(commonSettings: _*)
  .settings(scalariformSettings: _*)
  .settings(formattingSettings: _*)
  .settings(publishingSettings: _*)
  .settings(libraryDependencies ++=
    Seq(
      "org.parboiled" %%% "parboiled" % "2.1.3",
      "com.chuusai" %%% "shapeless" % "2.3.0",
      "org.scalatest" %%% "scalatest" % "3.0.0" % Test)
    )
  .jvmSettings(libraryDependencies += "com.github.scopt" %% "scopt" % "3.5.0")
  .jsSettings( )

lazy val chelonaJVM = chelona.jvm
lazy val chelonaJS = chelona.js

lazy val root = project.in(file("."))
  .settings(commonSettings:_*)
  .settings(
    mainClass in assembly := Some("org.chelona.TurtleMain"),
    mainClass in Compile := (mainClass in chelonaJVM in Compile).value
  )

/////////////////////// PUBLISH /////////////////////////

lazy val publishingSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  useGpg := false,
  useGpgAgent := false,
  sonatypeProfileName := "JuPfu",
  publishTo <<= version { v: String =>
    val nexus = "https://oss.sonatype.org/"
    if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
                        },
  pomIncludeRepository := { _ => false },
  pomExtra :=
    <scm>
      <connection>scm:git:github.com/jupfu/chelona</connection>
      <developerConnection>scm:git:git@github.com:jupfu/chelona.git</developerConnection>
      <url>github.com/jupfu/chelona</url>
    </scm>
      <developers>
        <developer>
          <id>JuPfu</id>
          <name>Jürgen Pfundt</name>
          <url>http://github.com/jupfu</url>
        </developer>
      </developers>)

