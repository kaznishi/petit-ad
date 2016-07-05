import play.sbt.routes.RoutesKeys

name := """petit-ad"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
//  jdbc,
  cache,
  ws,
  "com.typesafe.play" %% "play-slick" % "1.1.1",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "com.h2database" % "h2" % "1.4.190",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.json4s" %% "json4s-ext" % "3.2.11",
  "com.github.tototoshi" %% "play-json4s-native" % "0.4.1",
  "com.github.tototoshi" %% "play-json4s-test-native" % "0.4.1" % "test",
  "com.github.tototoshi" %% "play-joda-routes-binder" % "1.1.0",
//  "mysql" % "mysql-connector-java" % "5.1.36",
//  specs2 % Test,
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.scalatestplus" %% "play" % "1.4.0-M4" % "test"
)

RoutesKeys.routesImport += "customize.MyRoutes.myJodaRoutes._"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
