package au.com.eeks.content

import java.io.File
import java.awt.Color
import java.awt.Font
import edu.umd.cs.piccolo.nodes.PText
import edu.umd.cs.piccolo.event.{PInputEvent,PBasicInputEventHandler}
import au.com.eeks.eobject.EContent


/**
 * Link to some file. Clicking on the node starts the application associated 
 * with the file extension (under windows only)
 */
class FileContent(file:File) extends PText(file.getAbsolutePath) with EContent {
  import FileContent._
  setFont(fontText)
  setTextPaint(colorText)
  
  addInputEventListener(new PBasicInputEventHandler() {
    override def mousePressed(event:PInputEvent) = {
      if(event.getClickCount == 2) {
         //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+filepath)
         Runtime.getRuntime().exec("cmd /c start \"%s\" \"%s\"".
             format(file.getName,file.getAbsolutePath))
         //val pb = new ProcessBuilder("cmd /c start "+filepath)
         //val env = pb.environment
         //pb.directory("write dir where program runs")
         //val p = pb.start
      }
      event.setHandled(true)
    }    
  })

}

object FileContent {
  val fontText = new Font("Arial", Font.BOLD, 10)
  val colorText = new Color(50,50,50)  
}