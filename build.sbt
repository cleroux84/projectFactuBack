name := """projectFactu"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"
libraryDependencies ++= Seq(
  ws
)

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.4.2",
)
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.46"
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)
//libraryDependencies ++= Seq(
//  "com.typesafe.slick" %% "slick" % "2.1.0",
//  "joda-time" % "joda-time" % "2.4",
//  "org.joda" % "joda-convert" % "1.6",
//  "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
//)




// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
