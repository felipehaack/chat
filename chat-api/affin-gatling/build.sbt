enablePlugins(GatlingPlugin, JavaAppPackaging)

addCompilerPlugin(paradise)

libraryDependencies ++= Seq(
  Gatlingn.core % Test,
  Gatlingn.highcharts % Test
)

javaOptions in Gatling := Seq(
  "-server",
  "-Xms1g",
  "-Xmx1g",
  "-XX:NewSize=512m",
  "-XX:ReservedCodeCacheSize=128m",
  "-XX:+UseG1GC",
  "-XX:MaxGCPauseMillis=30",
  "-XX:+UseNUMA",
  "-XX:-UseBiasedLocking",
  "-XX:+AlwaysPreTouch",
  "-Dio.netty.eventLoopThreads=" + Math.min(Math.max(sys.runtime.availableProcessors / 2, 1), 8)
)