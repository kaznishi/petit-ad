package customize

object MyRoutes {
  val myJodaRoutes = new com.github.tototoshi.play2.routes.JodaRoutes {
    override val format: String = "yyyy/MM/dd"
  }
}