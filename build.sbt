lazy val root = (project in file(".")).
  settings(
    name := "statelet",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test"
