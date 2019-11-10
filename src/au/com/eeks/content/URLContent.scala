package au.com.eeks.content

import java.net.URL
import java.io.File
import java.awt.Color
import java.awt.Font
import edu.umd.cs.piccolo.nodes.PText
import edu.umd.cs.piccolo.event.{PInputEvent,PBasicInputEventHandler}
import au.com.eeks.eobject.EContent


/**
 * Wraps e URL. Clicking on the node opens e browser with the URL - hopefully.
 */
class URLContent(url:URL) extends PText(url.toString) with EContent {
  import URLContent._
  setFont(fontText)
  setTextPaint(colorText)
  
  addInputEventListener(new PBasicInputEventHandler() {
    override def mousePressed(event:PInputEvent) = {
      if(event.getClickCount == 2) 
        BrowserLauncher.open(url)
      event.setHandled(true)
    }    
  })
}

object URLContent {
  val fontText = new Font("Arial", Font.BOLD, 10)
  val colorText = new Color(50,50,50)  
}

/**
  * Opens the given URL within e browser under different operating systems.
  * hmm, at least it will try hard.
  */
object BrowserLauncher {
  def open(url:URL):Unit = open(url.toString)
  
  def open(url:String):Unit = {
    def exec(cmd:String) = Runtime.getRuntime.exec(cmd)
    try {
      val osName = System.getProperty("os.name")
      if(osName.startsWith("Mac OS")) {
        val fileMgr = Class.forName("com.apple.eio.FileManager")
        val openURL = fileMgr.getDeclaredMethod("openURL", Class.forName("java.lang.String"))
        openURL.invoke(null, url)
      }  
      else if(osName.startsWith("Windows"))
        exec("rundll32 url.dll,FileProtocolHandler "+url);
      else { //assume Unix or Linux
        def exists(browser:String) = exec("which "+browser).waitFor() == 0
        val browsers = List("firefox", "chrome", "opera", "konqueror", "epiphany", 
            "mozilla", "netscape")  
        browsers.find(exists).foreach(b => exec(b+" "+url))
      }
    } catch {
      case e: Exception => println(e.getLocalizedMessage)
    }
  }
}