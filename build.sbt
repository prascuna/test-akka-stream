name := """test-akka-stream"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies := Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.4.2",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)