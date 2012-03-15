package bot
import jerklib.events.MessageEvent
import jerklib.Channel

/**
 * A module that responds to string messages
 * Simplifies the Module implementation
 */
trait SimpleMessage extends Module {
  
  def message(channel: Channel, message: String)
  
  def commands = {
    case msg : MessageEvent =>
      message(msg.getChannel(), msg.getMessage())
  }
}
