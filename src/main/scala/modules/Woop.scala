package modules
import bot.Module
import bot.SimpleMessage
import jerklib.Channel
import jerklib.util.Colors

class Woop extends Module with SimpleMessage {

  val DefaultWoop = """!woop""".r
  val WoopCount = """!woop (?<num>[0-9]+\.?[0-9]*)""".r
  val VariableWoop = """!woo(?<num>o+)p""".r
  val VariableWoopCount = """!wooo+p (?<num>[0-9]+\.?[0-9]*)""".r
  
  def handlers = {
      case WoopCount(num) => woop(num.toFloat)
      case VariableWoop(num) => woop(num.length + 2)
      case VariableWoopCount(num) => woop(num.toFloat)
      case DefaultWoop() => woop(10)
  }
  
  def woop(num: Float) = {
    val whole = 1 max num.toInt min 20
    val partials = ((num - num.floor)*4).round
    sender ! Colors.BOLD + Colors.RED + "WOOP " * whole + "WOOP".slice(0, partials)
  }
}
