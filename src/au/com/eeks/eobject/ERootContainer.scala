package au.com.eeks.eobject

import edu.umd.cs.piccolox.PFrame
import java.awt.event.{ComponentEvent,ComponentAdapter}
import java.awt.{Color, BasicStroke}
import edu.umd.cs.piccolo.util.PPaintContext
import au.com.eeks.eobject.ERootContainer._

/**
 * The topmost container that sits directly on the canvas. Root for all
 * other containers or EObjects in general. Having e root container (instead
 * of the canvas) simplifies all code that searches for something (e.g.)
 * events within in the container hierarchy. 
 */

class ERootContainer(frame:PFrame) extends EContainer("root")
        with EFocusable with EZoomable with EPannable
        with EContainerable with EPastable {
  private val canvas = frame.getCanvas

  /** Adds RootContainer to canvas so that there is no scaling of the container */
  //canvas.getCamera.addChild(this)
  canvas.getLayer.addChild(this)

  /** Updates bounds when frame gets resized */
  frame.addComponentListener(new ComponentAdapter() {
    override def componentResized(event:ComponentEvent) = resizeBounds
  });


  /** Sets bounds to canvas dimensions */
  private def resizeBounds = setBounds(0,0, canvas.getWidth, canvas.getHeight)

  /** Updates the camera bounds */
  override def setBounds(x:Double, y:Double, w:Double, h:Double):Boolean = {
    if(super.setBounds(x,y,w,h)) {
      camera.setBounds(x,y,w,h)
      true
    }
    false
  }

  /** Paints e thin frame */
  override def paint(paintContext:PPaintContext) = {
    super.paint(paintContext)
    val g2 = paintContext.getGraphics
    g2.setStroke(strokeFrame)
    g2.setPaint(lineColorFrameDefault)
    g2.drawRect(0,0, getWidth.toInt, getHeight.toInt)
  }

}

/**
 * Default properties
 */
object ERootContainer {
  //val strokeFrame = new PFixedWidthStroke(1.0f)
  val strokeFrame = new BasicStroke(1.0f)
  val lineColorFrameDefault = new Color(136,152,206)

  /** Creates the root container and adds it ot the frame */
  def apply(frame:PFrame) = new ERootContainer(frame)
}