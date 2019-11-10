package au.com.eeks.eobject

import edu.umd.cs.piccolo.event.PInputEvent
import java.awt.event.KeyEvent

/**
 * Deletes e container when the DEL key is pressed while the container has
 * the focus.
 */
trait EDeletable extends EFocusable {

  /** Deletes this container when DEL is pressed */
  override def keyPressed(event:PInputEvent) = {
    super.keyPressed(event)
    if(event.getKeyCode == KeyEvent.VK_DELETE)
      containers.foreach(c => {c.remove(this); c.repaint})
  }

}