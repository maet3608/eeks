package au.com.eeks.eobject

import scala.math.abs
import java.awt.event.InputEvent
import java.awt.event.MouseEvent.MOUSE_DRAGGED
import java.awt.event.InputEvent.BUTTON3_MASK
import edu.umd.cs.piccolo.{PNode}
import java.awt.geom.{Rectangle2D, Point2D}
import edu.umd.cs.piccolo.event.{PInputEvent, PDragSequenceEventHandler, PInputEventFilter}
import edu.umd.cs.piccolo.util.PBounds

/**
 * Enables e container to create sub containers within its area.
 * A new container of e specific size is created by dragging the mouse.
 */
trait EContainerable extends EContainer {

  addInputEventListener(new ESubContainerEvent(this))

  /** Creates e new container from e dragging event */
  class ESubContainerEvent(container:EContainer) extends PDragSequenceEventHandler {
    setEventFilter(new PInputEventFilter(BUTTON3_MASK))

    var newContainer:EDefaultContainer = null
    var startPos:Point2D = null
    var bounds = new Rectangle2D.Double(0,0,0,0)
    setMinDragStartDistance(10)

    /** records start position of mouse drag */
    override def mousePressed(event:PInputEvent) = {
      super.mousePressed(event)
      startPos = event.getPosition()
      event.setHandled(true)
    }

    /** creates new, empty, transparent container */
    override def dragActivityFirstStep(event:PInputEvent) = {
      newContainer = new EDefaultContainer("New Container")
      newContainer.setBounds(startPos.getX,startPos.getY,0,0)
      newContainer.camera.setViewScale(1.0)
      newContainer.setTransparency(0.3f)
      container.add(newContainer)
      event.setHandled(true)
    }

    /** updates size of new container */
    override def dragActivityStep(event:PInputEvent) = {
      updateBounds(event.getPosition())
      event.setHandled(true)
    }

    /** accepts container if big enough and fills with underlying content */
    override def dragActivityFinalStep(event:PInputEvent):Unit = {
      updateBounds(event.getPosition())
      if(bounds.width<20 || bounds.height<20)  // container too small
        container.remove(newContainer)         // remove
      else {
        addContentsUnderneath()
        newContainer.setTransparency(1.0f)
      }
      event.setHandled(true)
    }

    /** adds any content underneath to the new container */
    private def addContentsUnderneath() = {
      for(content <- container.contents.toList) {
         // if content != newContainer && newContainer.isInside(content) ) {
        printf("%s %s %s\n",content,newContainer.isInside(content),content.getBoundsReference)
        //newContainer.camera.localToView(content.getOffset)
        //newContainer.add(content)     // add to new container
        //container.remove(content)     // remove from old container
      }
    }

    /** Updates the size of the new container according to the dragged distance */
    private def updateBounds(currPos:Point2D) = {
      bounds.x = if(startPos.getX < currPos.getX) startPos.getX else currPos.getX
      bounds.y = if(startPos.getY < currPos.getY) startPos.getY else currPos.getY
      bounds.width = abs(currPos.getX-startPos.getX)
      bounds.height = abs(currPos.getY-startPos.getY)
      newContainer.setBounds(bounds)
    }
  }
}