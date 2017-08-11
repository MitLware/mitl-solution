lazy val root = (project in file(".")).
  settings(
    name := "MitLware-solution",
    version := "0.1.0",
    scalaVersion := "2.12.0"
  )

libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test "

resolvers+=Resolver.bintrayRepo("mitlware", "mitl-support")
