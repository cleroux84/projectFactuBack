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
  "com.typesafe.play" %% "play-json" % "2.9.1",
  "com.typesafe.play" %% "play-json-joda" % "2.9.1",
  "com.hhandoko" %% "play28-scala-pdf" % "4.3.0"
)
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.4.2"
)
resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.46"
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)

//libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.19"




// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
