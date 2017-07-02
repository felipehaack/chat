name := "affin"

version := "1.0"

scalaVersion in ThisBuild := "2.11.8"

val common = Project("affin-common", file("affin-common"))

val api = Project("affin-api", file("affin-api"))
  .dependsOn(common)

val gatling = Project("affin-gatling", file("affin-gatling"))
  .dependsOn(common)

val root = Project("affin", file("."))
  .aggregate(common, api, gatling)