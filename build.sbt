name := "acnh-music-player"

version := "0.1"

scalaVersion := "2.12.7"

trapExit := false

libraryDependencies ++= Seq(
  "org.scalaj"                 %% "scalaj-http"        % "2.4.2",
  "com.typesafe.play"          %% "play-json"          % "2.6.9",
  "com.malliina"               %% "util-audio"         % "2.5.0",
  "com.typesafe.scala-logging" %% "scala-logging"      % "3.9.2", 
  "ch.qos.logback"             %  "logback-classic"    % "1.2.3",
  "org.scalatest"              %% "scalatest"          % "3.0.7" % "test"
)