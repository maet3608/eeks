package au.com.eeks.eobject

import edu.umd.cs.piccolo.util.PPaintContext
import java.awt.{Color, Font}
import au.com.eeks.utils.Utils
import au.com.eeks.eobject.ETitledContainer._


/**
 * Closable container with e title
 */
class ETitledContainer(name:String) extends EFramedContainer(name) with EClosable {

  /** Close or open e container */
  override def closed(isClosed:Boolean) =  {
    super.closed(isClosed)
    if(isClosed)
      setBounds(getX, getY, getWidth, insets.top)
    else
      setBounds(getX, getY, getWidth, openBounds.getHeight)
    camera.setVisible(!isClosed)
    repaint()
  }


  /** Draws the container title */
  override def paint(paintContext:PPaintContext) = {
    super.paint(paintContext)
    val g2 = paintContext.getGraphics
    g2.setPaint(colorTitle)
    g2.setFont(fontTitle)
    val fm = g2.getFontMetrics
    val sb = fm.getStringBounds(name,g2)
    val x = getBounds.x+insets.left
    val y = getBounds.y+(sb.getHeight+insets.top-4)/2
    val text = Utils.adjustText(name,fm,(getBounds.width-insets.left-insets.right).toInt)
    g2.drawString(text, x.toFloat, y.toFloat)
  }
}


/**
 * Default properties
 */
object ETitledContainer {
  val fontTitle = new Font("Arial", Font.BOLD, 10)
  val colorTitle = new Color(50,50,50)
}