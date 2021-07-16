//import Dependencies._

name := "scala"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("kafka.tutorial.scala")

lazy val KafkaVersion = "2.6.0"
lazy val ConfluentVersion = "6.2.0"

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List()),
    name := "scalastream",
    resolvers ++= Seq(
      Opts.resolver.mavenLocalFile,
      "Confluent" at "https://packages.confluent.io/maven"
    ),
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.2",
      "org.apache.kafka" % "kafka-clients" % KafkaVersion,
//      "org.apache.kafka" % "connect-json" % KafkaVersion,
//      "org.apache.kafka" % "connect-runtime" % KafkaVersion,
      "org.apache.kafka" % "kafka-streams" % KafkaVersion,
      "org.apache.kafka" %% "kafka-streams-scala" % KafkaVersion,
      "com.fasterxml.jackson.core" % "jackson-databind" % KafkaVersion,
//      "org.apache.kafka" % "connect-runtime" % "2.1.0",
      "io.confluent" % "kafka-json-serializer" % ConfluentVersion,
      "javax.ws.rs" % "javax.ws.rs-api" % "2.1.1" artifacts( Artifact("javax.ws.rs-api", "jar", "jar"))
    )
  )