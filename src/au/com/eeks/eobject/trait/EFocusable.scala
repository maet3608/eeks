package au.com.eeks.eobject

import edu.umd.cs.piccolo.event.{PInputEvent, PBasicInputEventHandler}

/**
 * Content gains focus when mouse enters its area. This event listener ensures
 * that the content with the focus has also the keyboard focus.
 */
trait EFocusable extends EContent {
  /** indicates if object has focus. */
  private var _hasFocus = false

  /** gains focus when mouse enters content area */
  addInputEventListener(new EFocusEvent(this))

  class EFocusEvent(focusable:EFocusable) extends PBasicInputEventHandler {
    override def mouseEntered(event:PInputEvent) =  {
      focused(true)
      event.getInputManager().setKeyboardFocus(this)
      event.setHandled(true)
    }
    override def mouseExited(event:PInputEvent) = {
      focused(false)
      event.getInputManager().setKeyboardFocus(null)
      event.setHandled(true)
    }
    override def keyPressed(event:PInputEvent) = focusable.keyPressed(event)
   }

  /** Override to react to key events */
  def keyPressed(event:PInputEvent) = {}

  /** Override to repaint object if necessary but call super.focused */
  def focused(hasFocus:Boolean) =  _hasFocus = hasFocus

  /** Getter for focus */
  def focused = _hasFocus

}