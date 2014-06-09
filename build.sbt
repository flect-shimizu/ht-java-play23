name := """ht-java-play23"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

resolvers += "Spy Repository" at "http://files.couchbase.com/maven2"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  javaJpa,
  "spy" % "spymemcached" % "2.8.9",
  "com.sendgrid" % "sendgrid-java" % "0.3.0",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.5.Final",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc4"
)
