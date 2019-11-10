package au.com.eeks.content

import java.awt.{Toolkit,Image}
import java.net.URL
import au.com.eeks.eobject.EContent
import edu.umd.cs.piccolo.nodes.PImage

/**
 * Image content from different sources.
 */
class ImageContent(image:Image) extends PImage(image) with EContent {
  def this(url:URL) = this(Toolkit.getDefaultToolkit.getImage(url))
  def this(filepath:String) = this(Toolkit.getDefaultToolkit.getImage(filepath))
}

