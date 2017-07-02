enablePlugins(PlayScala, DockerPlugin, VersionGenerator)

addCompilerPlugin(paradise)

libraryDependencies ++= Seq(
  logging,
  logstash,
  rabbitmq,
  filters,
  relate,
  ficus,
  scalaGuice,
  Accord.core,
  Javax.mail,
  scalatest % Test,
  scalatestPlay % Test,
  mockito % Test,
  guiceTestkit % Test
)

libraryDependencies += filters

javaOptions in Test ++= Seq(
  "-Dconfig.resource=application-test.conf",
  "-Dlogger.resource=logback-test.xml"
)

sourceGenerators in Compile += VersionGenerator.generate.taskValue