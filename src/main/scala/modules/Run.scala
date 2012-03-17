package modules
import bot.SimpleMessage
import bot.Module
import jerklib.Channel
import dispatch.Http
import dispatch.url
import scala.util.parsing.json.JSON
import jerklib.util.Colors
import jerklib.events.MessageEvent

class Run extends Module with SimpleMessage {

  val RunCommand = """!run (?<language>[^ ]*) (?<source>.*)""".r
  val api = "http://api.dan.co.jp/lleval.cgi"
  
  val friendlyNames = Map(
    "python" -> "py",
    "python3" -> "py3",
    "ruby" -> "rb",
    "perl" -> "pl",
    "javascript" -> "js"
  )    
    
  def message(channel:Channel, message:String) {
    message match {
      case RunCommand(language, source) => run(channel, language, source)
      case _ =>
    }
  }
  
  def run(channel: Channel, language:String, source:String) = {
    
    val params = Map(
      "l" -> friendlyNames.getOrElse(language, language),
      "s" -> source
    )

    Http(url(api) <<? params >- { response =>
      val json = JSON.parseFull(response).get.asInstanceOf[Map[String, Any]]
      val error = json("stderr").asInstanceOf[String]
      val output = json("stdout").asInstanceOf[String]
      if(!error.isEmpty)
        channel.say(Colors.RED + error.lines.toList.last)
      else if (!output.isEmpty)
        channel.say(Colors.GREEN + output.lines.toList.head)
    })
  }
}