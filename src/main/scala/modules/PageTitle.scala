package modules
import bot.SimpleMessage
import jerklib.Channel
import bot.Module
import dispatch.Http
import dispatch.url
import dispatch.tagsoup.TagSoupHttp._

class PageTitle extends Module with SimpleMessage {

  val UrlCommand = """.*(?<url>http://[^ ]+).*""".r
  
  def message(channel:Channel, message:String) {
    message match {
      case UrlCommand(url) => printTitle(channel, url)
      case _ =>
    }
  }
  
  def printTitle(channel:Channel, pageurl:String) {
    val ignored = List("stackexchange.com", "stackoverflow.com", "youtube.com", "wikipedia.org")
    if(ignored.forall(!pageurl.contains(_))) {
      Http x (url(pageurl) </> { html =>
        val title = (html \ "head" \ "title").text + (html \ "title").text
        channel.say(title)
      })
    }
  }
}