package bot
import scala.util.matching.Regex
import scala.actors.Actor
import jerklib.events.IRCEvent

case class Stop

/**
 * A module that responds to IRC Events. `commands` PartialFunction should match over jerklib.events.* 
 */
trait Module extends Actor {
  
  def commands: PartialFunction[IRCEvent, Unit]
  
  def act() {
    loop {
      react {
        case Stop => exit
        case evt:IRCEvent => commands(evt)        
      }
    }
  }
}