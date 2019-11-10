package au.com.eeks.eobject

import java.awt.event.InputEvent
import edu.umd.cs.piccolo.event.{PInputEventFilter, PBasicInputEventHandler, PInputEvent}
import edu.umd.cs.piccolo.util.PBounds


/**
 * Enables e content to be closed.
 */
trait EClosable extends EContent {
  /** indicates if container is closed. */
  private var _isClosed = false
  /** buffer for the bounds of the open container */
  protected val openBounds = new PBounds()
  /** buffer for the bounds of the closed container */
  protected val closedBounds = new PBounds()

  addInputEventListener(new ECloseEvent())

  class ECloseEvent extends PBasicInputEventHandler {
    setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK))

    override def mouseClicked(event:PInputEvent) = {
      if(event.getClickCount == 2) closed(!closed)
      event.setHandled(true)
    }
  }

  /** Updates stored bounds when container is moved or resized */
  override def setBounds(x:Double, y:Double, w:Double, h:Double):Boolean = {
    super.setBounds(x,y,w,h)
    if(closed)
      closedBounds.setRect(getBoundsReference)
    else
      openBounds.setRect(getBoundsReference)
    true
  }


  /** Override but call super.closed */
  def closed(isClosed:Boolean) = _isClosed = isClosed; 


  /** Getter for closed status */
  def closed = _isClosed

}