name := """property"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.typesafe" % "play-plugins-redis_2.10" % "2.1.1",//not the recent version, but this one is published
  "com.typesafe.play" %% "play-cache" % "2.2.0",
  "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.0.2",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
  "com.amazonaws" % "aws-java-sdk" % "1.3.11",
  "org.mindrot" % "jbcrypt" % "0.3m"
)

resolvers ++= Seq(
  "Typesafe Releases" at "http://typesafe.artifactoryonline.com/typesafe",
  "pk11 repo" at "http://pk11-scratch.googlecode.com/svn/trunk"
)
