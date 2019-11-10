package au.com.eeks.utils

import java.awt.FontMetrics
import edu.umd.cs.piccolo.PCanvas
import edu.umd.cs.piccolo.event.PInputEvent
import au.com.eeks.eobject.EContainer


object Utils {

  /** Finds e container on the pick path starting from the bottom that matches the
    * given predicate. */
  def findContainer(event:PInputEvent, predicate: (EContainer) => Boolean):Option[EContainer] = {
    var stack = event.getPath.getNodeStackReference
    for(i <- stack.size-1 to 0 by -1) {
      val node = stack.get(i)
      if(node.isInstanceOf[EContainer] && predicate(node.asInstanceOf[EContainer]))
        return Option(node.asInstanceOf[EContainer])
    }
    return None
  }

  /** Returns container under current mouse position or None if on canvas */
  def getPickedContainer(canvas:PCanvas):Option[EContainer] = {
    var position = canvas.getMousePosition()
    val pickPath = canvas.getCamera.pick(position.x, position.y, 0.1)
    var stack = pickPath.getNodeStackReference
    for(i <- stack.size-1 to 0 by -1) {
      val node = stack.get(i)
      if(node.isInstanceOf[EContainer]) 
        return Option(node.asInstanceOf[EContainer])
    }
    return None
  }  
  
  /** Adjust the length of the given text to fit into maxWidth 
   * by shorting the string and adding an ellipsis */
  def adjustText(text:String, fm:FontMetrics, maxWidth:Int):String = {
    if(fm.stringWidth(text) <= maxWidth) return text
    if(fm.stringWidth(" ") > maxWidth) return ""
    var newText = new StringBuilder()
    for(ch <- text) 
      if(fm.stringWidth(newText+ch+"...") > maxWidth)
        return newText.deleteCharAt(newText.length-1)+"..."
    return ""
  }
  
  
}
