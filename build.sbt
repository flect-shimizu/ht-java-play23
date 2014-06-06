name := """ht-java-play23"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  javaJpa,
  "com.sendgrid" % "sendgrid-java" % "0.3.0",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.5.Final",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc4"
)
