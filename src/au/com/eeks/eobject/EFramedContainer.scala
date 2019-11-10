package au.com.eeks.eobject

import edu.umd.cs.piccolo.util.PPaintContext
import java.awt.geom.RoundRectangle2D
import au.com.eeks.eobject.EFramedContainer._
import java.awt.{Insets, BasicStroke, Color}
import edu.umd.cs.piccolo.event.PInputEvent
import java.awt.event.KeyEvent


/**
 * A resizable, focusable container with e frame defined by insets.
 */
class EFramedContainer(name:String) extends EContainer(name)
        with EFocusable with EResizable {
  /** Insets (top,left,bottom,right) of frame. */
  val insets = new Insets(20,3,3,3)
  /** Radius of frame corners */
  var radius = 10
  /** Frame/background area of the container */
  private val frame:RoundRectangle2D = new RoundRectangle2D.Double()

  camera.setPaint(fillColorContent)    

  /** Repaint when focus has changed */
  override def focused(hasFocus:Boolean) = {
    super.focused(hasFocus)
    repaint()
  }

  /** Updates bounds for camera and frame */
  override def setBounds(x:Double, y:Double, w:Double, h:Double):Boolean = {
    if(super.setBounds(x,y,w,h)) {
      frame.setRoundRect(x,y,w,h,radius,radius)
      camera.setBounds(x+insets.left, y+insets.top,
                       w-insets.left-insets.right, h-insets.top-insets.bottom)
      true
    }
    false
  }

  /** Draws the container frame */
  override def paint(paintContext:PPaintContext) = {
    super.paint(paintContext)
    val g2 = paintContext.getGraphics
    g2.setStroke(strokeFrame)
    g2.setPaint(fillColorFrameDefault)
    g2.fill(frame)
    g2.setPaint(if(focused) lineColorFrameFocus else lineColorFrameDefault)
    g2.draw(frame)
  }
}


/**
 * Default properties
 */
object EFramedContainer {
  //val strokeFrame = new PFixedWidthStroke(1.0f)
  val strokeFrame = new BasicStroke(1.0f)
  val lineColorFrameFocus = new Color(86,102,156)
  val lineColorFrameDefault = new Color(136,152,206)
  val fillColorFrameDefault = new Color(172,191,233)

  val lineColorContent = new Color(200,200,200)
  val fillColorContent = new Color(255,255,255)
}