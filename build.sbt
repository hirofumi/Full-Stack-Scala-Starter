lazy val scalaV = "2.12.2"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.1",
    guice,
    filters,
    specs2 % Test
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).settings(pbSettings ++ Seq(protoroutesPlay26Router := true)).
  enablePlugins(PlayScala, Protoroutes).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  scalaJSUseMainModuleInitializer := true,
  scalacOptions ++= Seq("-Xmax-classfile-name","78"),
  scalaJSUseMainModuleInitializer in Test := false,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.4",
    "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    "com.thoughtworks.binding" %%% "dom" % "11.0.0",
    "com.thoughtworks.binding" %%% "futurebinding" % "11.0.0",
    "fr.hmil" %%% "roshttp" % "2.1.0"
  )
).settings(pbSettings ++ Seq(protoroutesAjax := true)).
  enablePlugins(Protoroutes, ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSWeb)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the server project at sbt startup
onLoad in Global ~= (_ andThen ("project server" :: _))

lazy val pbSettings = Seq(
  PB.protoSources in Compile := Seq(baseDirectory.value.getParentFile / "protobuf"),
  PB.protoSources in Compile += protoroutesDependencyProtoPath.value
)
