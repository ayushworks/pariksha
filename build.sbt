name := "pareeksha"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.2" % Test
)

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := {
  <url>https://github.com/ayushworks/pareeksha</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:ayushworks/pareeksha.git</url>
      <connection>scm:git@github.com:ayushworks/pareeksha.git</connection>
    </scm>
    <developers>
      <developer>
        <id>ayushworks</id>
        <name>ayush</name>
        <url>http://github.com/ayushworks</url>
      </developer>
    </developers>
}