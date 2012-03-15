package bot
import scala.util.matching.Regex
import scala.actors.Actor

/**
 * A module that responds to IRC Events. `commands` PartialFunction should match over jerklib.events.* 
 */
trait Module extends Actor {
  
  def commands: PartialFunction[Any, Unit]
  
  def act() {
    loop {
      react { commands }
    }
  }
}