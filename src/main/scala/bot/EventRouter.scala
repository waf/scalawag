package bot
import jerklib.listeners.IRCEventListener
import jerklib.events.IRCEvent
import jerklib.events._
import scala.actors.Actor
import jerklib.Session
import jerklib.events.TopicEvent

class EventRouter extends Actor with IRCEventListener {

  val modules = ModuleLoader.getModules().map(m => m.start())
  
  def receiveEvent(ev: IRCEvent) = this ! ev
  
  def act() {
    loop {
      react {
        case e: ConnectionCompleteEvent =>
          Scalawag.initialChannels.foreach(e.getSession().join(_))
        case e: JoinCompleteEvent =>
          e.getChannel().say("hi")
        case msg: MessageEvent  => 
          modules.foreach(mod => mod ! msg)
      }
    }
  }
}