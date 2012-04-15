package modules
import bot.Module
import bot.SimpleMessage
import jerklib.Channel
import jerklib.util.Colors
import com.mongodb.casbah.Imports._

class WhoseLine extends Module with SimpleMessage {

  val responses = MongoConnection()("WhoseLine")("responses")
  val invalidResponses = List("""!.*""", """\..*""")
  
  val GetLine = """(?i).*you know what they say about .*""".r
  val SaveLine = """!line (?<line>.+)""".r
  val DropLine = """!dropline (?<line>.+)""".r
  
  def handlers = {
    case SaveLine(line) => save(line)
    case DropLine(line) => drop(line)
    case GetLine() => getRandomResponse()
  }
  
  def getRandomResponse() = {
    
    val randomLine = responses.find()
      .limit(1)
      .skip( Math.random * responses.count intValue)
      .toList;
    
    sender ! randomLine.first("line").toString()
  }
  
  def save(line: String) = {
    
    if(invalidResponses.exists(line.matches(_))) {
      sender ! Colors.RED + "rejected"
    } else {
      responses.save(MongoDBObject("line" -> line))
      sender ! Colors.GREEN + "saved"
    }
  }
  
  def drop(line: String) = {
    val result = responses.remove(MongoDBObject("line" -> line))
    
    if(result.getN() > 0)
      sender ! Colors.GREEN + "dropped"
    else
      sender ! Colors.RED + "no results dropped"
  }
}