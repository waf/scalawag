package bot
import jerklib.listeners.IRCEventListener
import jerklib.events.IRCEvent
import jerklib.events._
import scala.actors.Actor
import jerklib.Session
import jerklib.events.TopicEvent
import jerklib.Channel

class EventRouter extends Actor with IRCEventListener {

  val modules = ModuleLoader.getModules().map(m => m.start())
  var channel:Channel = null
  
  def receiveEvent(ev: IRCEvent) = this ! ev
  
  def act() {
    loop {
      react {
        case e: ConnectionCompleteEvent =>
          e.getSession().join(Scalawag.channel)
        case e: JoinCompleteEvent =>
          channel = e.getChannel()
        case msg: MessageEvent  => 
          val futures = modules.foreach(_ ! msg)
        case response: String  =>
          channel.say(response)
        case other: IRCEvent => println("EventRouter: " + other)
      }
    }
  }
}