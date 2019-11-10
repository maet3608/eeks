package au.com.eeks.eobject

import scala.collection.JavaConversions
import edu.umd.cs.piccolox.handles.PBoundsHandle
import java.awt.Color
import edu.umd.cs.piccolo.util.PPaintContext
import au.com.eeks.eobject.EResizable._

/**
 * Adds resize handles to e content object/
 */
trait EResizable extends EContent {

  PBoundsHandle.addBoundsHandlesTo(this)

  /** List of bounds handles */  
  protected val boundsHandles = JavaConversions.asScalaIterator(getChildrenIterator).
    filter(_.isInstanceOf[PBoundsHandle]).
    map(_.asInstanceOf[PBoundsHandle]).toList

  /** Shrink handles and write color */
  for(handle <- boundsHandles) {
    handle.setScale(0.3)
    handle.setStrokePaint(lineColorHandle)
  }

  /** Only content with focus can be resized. Typically implemented by EFocusable */
  def focused:Boolean

  /** Draws the resize handles when content has focus */
  override def paint(paintContext:PPaintContext) = {
    super.paint(paintContext)
    boundsHandles.foreach(_.setVisible(focused))
  }

}

/**
 * Default properties
 */
object EResizable {
  val lineColorHandle = new Color(136,152,206)
}