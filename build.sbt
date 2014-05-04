organization := "org.diydrones"

name := "droneapi-java"

description := "A client library for drone API servers"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6") // Build for java6 because old android devices require it

autoScalaLibrary := false // This is a pure java project

crossPaths := false // disable using the Scala version in output paths and artifacts

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "2.5.0" withSources()

libraryDependencies += "org.zeromq" % "jeromq" % "0.3.2" withSources()

mainClass in (Compile, run) := Some( "com.geeksville.apiproxy.TestClient")

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

publishMavenStyle := true

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/diydrones/droneapi-java"))

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
<developers>
  <developer>
    <id>kevinh</id>
    <name>Kevin Hester</name>
    <email>kevinh@geeksville.com</email>
    <organization>3D Robotics</organization>
    <roles>
      <role>architect</role>
    </roles>
  </developer>
</developers>
<scm>
  <url>git@github.com:3drobotics/japi-proxy.git</url>
  <connection>scm:git@github.com:3drobotics/japi-proxy.git</connection>
</scm>)
