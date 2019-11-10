package au.com.eeks.eobject

import edu.umd.cs.piccolo.event.{PInputEventFilter, PInputEvent, PBasicInputEventHandler}
import java.awt.event.MouseEvent.MOUSE_WHEEL
import java.awt.event.InputEvent.CTRL_MASK

/**
 * Allows to zoom the content of containers
 */
trait EZoomable extends EContainer {

  camera.addInputEventListener(new EZoomEvent(this))

  private class EZoomEvent(container:EContainer) extends PBasicInputEventHandler {
    setEventFilter(new PInputEventFilter(MOUSE_WHEEL&CTRL_MASK))

    override def mouseWheelRotated(event:PInputEvent):Unit = {
      val ctrlPressed = (event.getModifiers & CTRL_MASK) > 0
      val camera = if(ctrlPressed) container.top.camera else event.getCamera
      val pos = if(ctrlPressed) camera.localToView(event.getCanvasPosition) else event.getPosition
      val newScale = 1.0 - 0.2*event.getWheelRotation
      camera.scaleViewAboutPoint(newScale, pos.getX, pos.getY)
      camera.repaint()
      event.setHandled(true)
    }
  }
}