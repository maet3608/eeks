package au.com.eeks.eobject

import java.awt.event.MouseEvent.MOUSE_DRAGGED
import java.awt.event.InputEvent.{BUTTON2_MASK,CTRL_MASK}
import edu.umd.cs.piccolo.event.{PInputEventFilter, PInputEvent, PBasicInputEventHandler}

/**
 * Allows to pan the content of containers
 */
trait EPannable extends EContainer {

  camera.addInputEventListener(new EPanEvent(this))

  private class EPanEvent(container:EContainer) extends PBasicInputEventHandler {
    setEventFilter(new PInputEventFilter(MOUSE_DRAGGED&BUTTON2_MASK))

    override def mouseDragged(event:PInputEvent) {
      val ctrlPressed = (event.getModifiers & CTRL_MASK) > 0
      val camera = if(ctrlPressed) container.top.camera else event.getCamera
      val pos = if(ctrlPressed) camera.localToView(event.getCanvasPosition) else event.getPosition
      if(ctrlPressed) camera.localToView(pos)
      if(camera.getViewBounds.contains(pos)) {
        val c = if(ctrlPressed) camera.getViewScale else 1.0
        val d = if(ctrlPressed) event.getCanvasDelta else event.getDelta
        camera.translateView(d.getWidth/c, d.getHeight/c)
        camera.repaint()
        event.setHandled(true)
      }
    }
  }
}