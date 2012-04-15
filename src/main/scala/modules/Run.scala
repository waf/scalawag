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
    
  def handlers = {
    case RunCommand(language, source) => run(language, source)
  }
  
  def run(language:String, source:String) = {
    
    val params = Map(
      "l" -> friendlyNames.getOrElse(language, language),
      "s" -> source
    )

    Http(url(api) <<? params >- { response =>
      val json = JSON.parseFull(response).get.asInstanceOf[Map[String, Any]]
      val error = json("stderr").asInstanceOf[String]
      val output = json("stdout").asInstanceOf[String]
      if(!error.isEmpty)
        sender ! Colors.RED + error.lines.toList.last
      else if (!output.isEmpty)
        sender ! Colors.GREEN + output.lines.toList.head
    })
  }
}