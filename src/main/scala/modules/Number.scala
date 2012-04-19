package modules
import bot.Module
import bot.SimpleMessage
import dispatch.Http
import dispatch.url
import jerklib.util.Colors

class Number extends Module with SimpleMessage {

  val Number = """!num (?<n>[0-9]+)""".r
  val Random = """!num""".r
  val api = "http://numbersapi.com/"
  
  def handlers = {
    case Number(n) => getNumber(n)
    case Random() => getNumber("random") // haha latin
  }
  
  def getNumber(n:String) {
    makeRequest(api + n)
  }
  
  def makeRequest(requestUrl:String) = {
    Http(url(requestUrl) >- { response =>
      sender ! Colors.GREEN + response
    })
  }
}