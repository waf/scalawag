package bot
import jerklib.events.MessageEvent
import jerklib.Channel

/**
 * A module that responds to string messages
 * Simplifies the Module implementation
 */
trait SimpleMessage extends Module {
  
  def handlers: PartialFunction[String, Unit]
    
  def commands = {
    case m : MessageEvent =>
      val msg = m.getMessage()
      if(handlers.isDefinedAt(msg))
        handlers(msg)
  }
}
