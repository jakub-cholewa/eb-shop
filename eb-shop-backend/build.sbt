name := "eb-shop"

version := "1.0"

lazy val `eb-shop` = (project in file("."))
  .enablePlugins(PlayScala, LauncherJarPlugin)
  .settings(watchSources ++= (baseDirectory.value / "public" ** "*").get)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers += Resolver.jcenterRepo
resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice )
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "org.xerial"        %  "sqlite-jdbc" % "3.30.1",
  "com.mohiva" %% "play-silhouette" % "6.1.1",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "6.1.1",
  "com.mohiva" %% "play-silhouette-persistence" % "6.1.1",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "6.1.1",
  "com.mohiva" %% "play-silhouette-totp" % "6.1.1",
  "com.typesafe.play" %% "play-mailer" % "7.0.1",
  "com.typesafe.play" %% "play-mailer-guice" % "7.0.1",
  "com.enragedginger" %% "akka-quartz-scheduler" % "1.8.3-akka-2.6.x",
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "com.iheart" %% "ficus" % "1.4.7"
)

enablePlugins(DockerPlugin)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown
dockerExposedPorts := Seq(9000)
packageName in Docker := "skellen/" +  packageName.value
