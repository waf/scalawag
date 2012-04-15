package bot
import jerklib.ConnectionManager
import jerklib.Profile

object Scalawag {

  val channel = "#rhblazeix"
  val nick = "scalawag2"
  val server = "irc.freenode.net"
  
  def main(args: Array[String]) {
    val manager = new ConnectionManager(new Profile(nick))

    val session = manager.requestConnection(server)

    val router = new EventRouter()
    router.start()
    session.addIRCEventListener(router)
  }
}
