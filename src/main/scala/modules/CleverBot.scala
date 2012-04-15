package modules
import bot.Module
import jerklib.events.MessageEvent
import bot.SimpleMessage
import bot.Scalawag
import jerklib.Channel
import dispatch.Http
import dispatch.url
import scala.collection.mutable.Map
import scala.collection.immutable.Map.Map2
import scala.collection.mutable.LinkedHashMap
import scala.collection.mutable.ListMap
import java.security.MessageDigest

class CleverBot extends Module with SimpleMessage {

  val MessageToMe = Scalawag.nick + ": (?<text>.*)" r
  
  val apiUrl =  "http://cleverbot.com/webservicemin"
    
  def handlers = {
    case MessageToMe(text) => respond(text)
  }
  
  def respond(message: String) {
    val http = new Http
    var request = url(apiUrl)
    val handler = request >>> System.out
    
    CleverBot.postData("stimulus") = message + "%0A"
    var data = collapse(CleverBot.postData)
    CleverBot.postData("icognocheck") = md5(data.substring(9,29))
    data = collapse(CleverBot.postData)
    
    Http(request << data >- { response =>
  	  val parsed = response.split('\r')
  	  val answer = parsed(0)
  	  sender ! answer.replace("Cleverbot", Scalawag.nick)
  	  
  	  // i sure hope we never get a malformed response...
  	  CleverBot.postData("sessionid") = parsed(1)
  	  CleverBot.postData("prevref") = parsed(10)
  	  pushHistory(message)
  	  pushHistory(answer)
  	})
  }
  
  def md5(str: String) = {
    MessageDigest.getInstance("MD5").digest(str.getBytes)
    .map(0xFF & _).map("%02x".format(_)).mkString
  }
  
  def collapse(keyvals: Map[String, String]) = {
    keyvals.foldLeft("")((acc, keyval) => acc + "&" + keyval._1 + "=" + keyval._2).tail
  }
  
  def pushHistory(answer:String) {
    for (i <- 8 until 2 by -1) {
      CleverBot.postData("vText" + i) = CleverBot.postData("vText" + (i-1)) 
    }
    CleverBot.postData("vText2") = answer
  }
}

object CleverBot {
  val postData = new LinkedHashMap() += (
    "stimulus" -> "",
    "start" -> "y",
    "sessionid" -> "",
    "vText8" -> "",
    "vText7" -> "",
    "vText6" -> "",
    "vText5" -> "",
    "vText4" -> "",
    "vText3" -> "",
    "vText2" -> "",
    "icognoid" -> "wsf",
    "icognocheck" -> "",
    "fno" -> "0",
    "prevref" -> "",
    "emotionaloutput" -> "",
    "emotionalhistory" -> "",
    "asbotname" -> "",
    "ttsvoice" -> "",
    "typing" -> "",
    "lineref" -> "",
    "sub" -> "Say",
    "islearning" -> "1",
    "cleanslate" -> "false"
  )
}