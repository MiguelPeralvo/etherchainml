name := "etherchainml"

scalaVersion := "2.11.8"

javaOptions in(Test, run) += "-XX:+UseConcMarkSweepGC"

javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")

parallelExecution in Test := false

version := "1.0"

sbtVersion := "0.13.7"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.0.0",
  "org.apache.spark" %% "spark-sql" % "2.0.0",
  "org.apache.spark" %% "spark-mllib" % "2.0.0",
  "org.apache.spark" %% "spark-catalyst" % "2.0.0",
  "org.apache.spark" %% "spark-streaming" % "2.0.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.15",
  "com.typesafe.akka" %% "akka-remote" % "2.3.15",
  "com.typesafe.akka" %% "akka-cluster" % "2.3.15",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.15",
  "org.specs2" %% "specs2-core" % "3.6.4" % "test",
  "org.specs2" %% "specs2-junit" % "3.6.4" % "test",
  "org.specs2" %% "specs2-mock" % "3.6.4" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
  "junit" % "junit" % "4.10" % "provided",
  "org.scalacheck" %% "scalacheck" % "1.12.1" % "test",
 //"com.cloudphysics" % "jerkson_2.10" % "0.6.3",
  "com.holdenkarau" %% "spark-testing-base" % "2.0.0_0.4.4",
  "org.scalaz" %% "scalaz-core" % "7.2.3",
  "org.scalaj" %% "scalaj-http" % "2.2.1",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.apache.kafka" %% "kafka" % "0.8.2.1"
)

retrieveManaged := true


