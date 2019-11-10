package au.com.eeks.eobject

import java.awt.event.InputEvent
import edu.umd.cs.piccolo.event.{PInputEventFilter, PBasicInputEventHandler, PInputEvent}
import au.com.eeks.utils.Utils

/**
 * Allows content to be pinned on camera of the container it resides in.
 * This means it does not scale or drags with rest of the container content.
 */
trait EPinnable extends EContent {
  /** indicates if object has focus. */
  private var _isPinned = false

  addInputEventListener(new EPinEvent(this))

  /** Pins content to the camera of the container */
  private class EPinEvent(content:EContent) extends PBasicInputEventHandler {
    setEventFilter(new PInputEventFilter(InputEvent.BUTTON3_MASK))

    override def mouseClicked(event:PInputEvent) = {
      if(event.getClickCount == 1) pinned(!pinned)
      // finds container of this content and pins/unpins content to/from it
      //Utils.findContainer(event, (c) => c != content).foreach(pin)
      content.containers.foreach(pin)
      event.setHandled(true)
    }
  }

  /** Pins or unpins from the given container */
  def pin(container:EContainer) = {
    println("container="+container)
    if(pinned) {
      println("EPinnable:pinning")
      //this.setBounds(50,50,50,50)
      //container.camera.localToView(this.getOffset)
      val bounds = this.getBoundsReference
      val offset = this.getOffset
      //println(bounds)
      //container.layer.localToGlobal(bounds)
      //container.camera.localToView(bounds)
      val c =  container.camera.getViewScale
      this.setScale(c)
      //this.setOffset(offset.getX*c,offset.getY*c)
      this.setOffset(0,0)
      //this.setBounds(bounds.getX,bounds.getY,bounds.getWidth*c,bounds.getHeight*c)
      //this.translate(offset.getX*c, offset.getY*c)
      this.setTransparency(0.7f)
      container.layer.removeChild(this); container.camera.addChild(this)
    }
    else {
      println("EPinnable:unpinning")
      val offset = this.getOffset
      //val c =  1.0/container.camera.getViewScale
      //this.setScale(c)
      //this.setOffset(offset.getX*c,offset.getY*c)
      //this.setBounds(bounds.getX,bounds.getY,bounds.getWidth/c,bounds.getHeight/c)
      container.camera.removeChild(this); container.layer.addChild(this)
      this.setTransparency(1.0f)
    }

  }
  
  /** Override to repaint object if necessary but call super.pinned
   * You can't use this method to pin an object. Use pin() instead */
  def pinned(isPinned:Boolean):Unit =  _isPinned = isPinned

  /** Getter for focus */
  def pinned = _isPinned

}