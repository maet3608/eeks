package au.com.eeks.content

import java.awt.Graphics2D
import java.awt.Graphics
import edu.umd.cs.piccolo.PCanvas
import java.awt.Color
import java.awt.Font
import edu.umd.cs.piccolo.util.PPaintContext
import edu.umd.cs.piccolo.PNode
import au.com.eeks.eobject.EContent

/**
 * A single line string.
 */
class StringContent(string:String, canvas:PCanvas) extends PNode with EContent {
  import StringContent._
  
  initBounds()
  
  private def stringBounds(g2:Graphics2D) = {
    g2.setFont(fontString)
    g2.getFontMetrics().getStringBounds(string,g2).getFrame
  }
  
  private def initBounds():Unit = {
    val sb = stringBounds(canvas.getGraphics.asInstanceOf[Graphics2D])
    setBounds(0,0,sb.getWidth,sb.getHeight)
  }
  
  override def paint(paintContext:PPaintContext) = {
    val g2 = paintContext.getGraphics
    g2.setPaint(colorString)
    g2.setFont(fontString)
    val sb = stringBounds(g2)
    val x = getBounds.x
    val y = getBounds.y-sb.getY.toInt
    g2.drawString(string, x.toFloat, y.toFloat)
    //g2.drawRect(getBounds.x.toInt, getBounds.y.toInt, getBounds.width.toInt, getBounds.height.toInt)
  }  
}

object StringContent {
  val fontString = new Font("Arial", Font.BOLD, 10)
  val colorString = new Color(50,50,50)  
}