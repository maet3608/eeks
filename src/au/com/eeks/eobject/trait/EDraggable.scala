package au.com.eeks.eobject

import java.awt.event.MouseEvent.MOUSE_DRAGGED
import java.awt.event.InputEvent.BUTTON1_MASK
import edu.umd.cs.piccolo.event.{PInputEventFilter, PInputEvent, PBasicInputEventHandler}
import edu.umd.cs.piccolo.PCamera
import java.awt.event.{MouseEvent, InputEvent}

/**
 * Allows to drag content
 */
trait EDraggable extends EContent {

  addInputEventListener(new EDragEvent(this))

  /** Left click and dragging on content moves it around */
  private class EDragEvent(content:EContent) extends PBasicInputEventHandler {
    setEventFilter(new PInputEventFilter(BUTTON1_MASK))

    override def mouseDragged(event:PInputEvent) = {
      val camera = event.getCamera
      val pos = event.getPosition
      if(camera.getViewBounds().contains(pos)) {
        val d = event.getDelta
        // if node is pinned it is e child of the camera and scale needs to be considered
        val c = if(content.getParent.isInstanceOf[PCamera]) camera.getViewScale else 1.0
        content.translate(d.getWidth*c, d.getHeight*c)
        event.setHandled(true)
      }
    }
  }
}