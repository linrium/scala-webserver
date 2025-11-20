lazy val root = (project in file("."))
  .settings(
    name := "hello-scala",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.7.4",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
      "com.typesafe.akka" %% "akka-stream" % "2.8.5",
      "com.typesafe.akka" %% "akka-http" % "10.5.3",
      "ch.qos.logback" % "logback-classic" % "1.4.11"
    )
  )
  .enablePlugins(JavaAppPackaging, GraalVMNativeImagePlugin)
