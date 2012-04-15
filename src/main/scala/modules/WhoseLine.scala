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
  
  def message(channel:Channel, message:String) {
    val response = message match {
      case SaveLine(line) => save(line)
      case DropLine(line) => drop(line)
      case GetLine() => getRandomResponse()
      case _ => ""
    }
    channel.say(response)
  }
  
  def getRandomResponse() = {
    
    val randomLine = responses.find()
      .limit(1)
      .skip( Math.random * responses.count intValue)
      .toList;
    
    randomLine.first("line").toString()
  }
  
  def save(line: String) = {
    if(invalidResponses.exists(line.matches(_))) {
      Colors.RED + "rejected"
    } else {
      responses.save(MongoDBObject("line" -> line))
      Colors.GREEN + "saved"      
    }
    
  }
  
  def drop(line: String) = {
    val result = responses.remove(MongoDBObject("line" -> line))
    if(result.getN() > 0)
      Colors.GREEN + "dropped"
    else
      Colors.RED + "no results dropped"
  }
}