lazy val root = (project in file(".")).
  settings(
    name := "MitLware-solutions",
    version := "1.2",
    scalaVersion := "2.12.0"
  )

libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test"
