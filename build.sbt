organization := "com.geeksville"

name := "japi-proxy"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

autoScalaLibrary := false // This is a pure java project

crossPaths := false // disable using the Scala version in output paths and artifacts
 
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.2.3"

libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.2.3"