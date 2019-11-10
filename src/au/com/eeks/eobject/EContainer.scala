package au.com.eeks.eobject

import edu.umd.cs.piccolo.{PCamera, PLayer}
import scala.collection.JavaConversions

/**
 * Top most object. Does essentially nothing but allows us to pass
 * EObjects instead of PNodes around, which may come in handy.
 */

class EContainer(name:String) extends EContent {
  /** Looks at content on layer */
  val camera = new PCamera
  /** Layer that carries content nodes */
  val layer = new PLayer

  setName(name)
  addChild(camera)
  
  camera.scaleView(1.0)
  camera.addLayer(layer)
  layer.setParent(camera)

  /** Returns the top most container for this container */
  def top:EContainer = containers.headOption match {case None=>this; case c=>c.get.top}

  /** Iterator over all content objects of the container */
  def contents = JavaConversions.asScalaIterator(layer.getChildrenIterator).
          filter(_.isInstanceOf[EContent]).map(_.asInstanceOf[EContent])

  /** Adds content to the container */
  def add(content:EContent) = { layer.addChild(content); content.addContainer(this) }

  /** Removes content from the container */
  def remove(content:EContent) = { layer.removeChild(content); content.removeContainer(this) }
}