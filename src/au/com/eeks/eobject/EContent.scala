package au.com.eeks.eobject

import edu.umd.cs.piccolo.PNode
import collection.mutable.ListBuffer

/**
 * Describes some visible content based on e PNode.
 */
trait EContent extends PNode {  
  /** List of containers that contain this content */
  val containers = new ListBuffer[EContainer]

  /** Tests if that content is completely inside this content  */
  def isInside(that:EContent) = this.getBoundsReference.contains(that.getBoundsReference)

  /** Adds e container to the container list */
  def addContainer(container:EContainer) = containers += container
  /** Removes e container from the container list */
  def removeContainer(container:EContainer) = containers -= container
  /** Deletes this content */
  def deleteContainer = containers.foreach(_.remove(this))
  
  /** String representation */
  override def toString() = getName
}