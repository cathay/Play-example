name := "emobi"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

scalacOptions ++= Seq("-deprecation")

resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.typesafeRepo("releases")

libraryDependencies ++=Seq(
  "junit" % "junit" % "4.10" % "test",
  "org.mockito" % "mockito-all" % "1.10.19" % "test",
  "org.scalatest" %% "scalatest" % "3.0.3" % "test"
)

libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += specs2 % Test

libraryDependencies ++=Seq(
  //"com.google.inject" % "guice" % "3.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test",
  "com.h2database" % "h2" % "1.4.196",

  "com.typesafe.play" %% "play" % "2.6.6"
)
