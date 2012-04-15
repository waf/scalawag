name := "scalawag"

version := "0.1"

scalaVersion := "2.9.1"

libraryDependencies ~= { seq =>
  val dispatch = "0.8.8"
  seq ++ Seq(
    "com.mongodb.casbah" %% "casbah" % "2.1.5-1",
    "net.databinder" %% "dispatch-core" % dispatch,
    "net.databinder" %% "dispatch-oauth" % dispatch,
    "net.databinder" %% "dispatch-nio" % dispatch,
    /* Twine doesn't need the below dependencies, but it simplifies
     * the Dispatch tutorials to keep it here for now. */
    "net.databinder" %% "dispatch-http" % dispatch,
    "net.databinder" %% "dispatch-tagsoup" % dispatch,
    "net.databinder" %% "dispatch-jsoup" % dispatch
  )
}
