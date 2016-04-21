name := "just-us"

version := "1.0"

scalaVersion := "2.11.7"

val akkaV         = "2.4.4"
val slickV        = "3.1.1"
val slickPgV      = "0.11.2"
val sessionV      = "0.2.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka"                           %% "akka-http-core"                                 % akkaV,
  "com.typesafe.akka"                           %% "akka-http-experimental"                         % akkaV,
  "com.typesafe.akka"                           %% "akka-http-testkit"                              % akkaV,
  "com.typesafe.akka"                           %% "akka-http-spray-json-experimental"              % akkaV,
  "com.typesafe.slick"                          %% "slick"                                          % slickV,
  "com.typesafe.slick"                          %% "slick-hikaricp"                                 % slickV,
  "com.github.tminglei"                         %% "slick-pg"                                       % slickPgV,
  "com.github.tminglei"                         %% "slick-pg_date2"                                 % slickPgV,
  "com.softwaremill.akka-http-session"          %% "core"                                           % sessionV,
  "com.softwaremill.akka-http-session"          %% "jwt"                                            % sessionV,
  "ch.megard"                                   %% "akka-http-cors"                                 % "0.1.1",
  "com.typesafe.scala-logging"                  %% "scala-logging"                                  % "3.1.0",
  "ch.qos.logback"                              %  "logback-classic"                                % "1.1.2",
  "com.github.t3hnar"                           %% "scala-bcrypt"                                   % "2.5",
  "org.scalatest"                               %% "scalatest"                                      % "2.2.6"   % "test"
)