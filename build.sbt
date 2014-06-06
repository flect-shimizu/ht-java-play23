name := """ht-java-play23"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.sendgrid" % "sendgrid-java" % "0.3.0",
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)
