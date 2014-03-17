organization := "com.3drobotics"

name := "japi-proxy"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.3"

autoScalaLibrary := false // This is a pure java project

crossPaths := false // disable using the Scala version in output paths and artifacts

libraryDependencies += "com.google.protobuf" % "protobuf-java" % "2.5.0"

mainClass in (Compile, run) := Some( "com.geeksville.apiproxy.TestClient")

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

publishMavenStyle := true

licenses := Seq("Apache 2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/3drobotics/japi-proxy"))


