package au.com.eeks.content

import java.awt.Color
import java.awt.Font
import edu.umd.cs.piccolo.nodes.PText
import au.com.eeks.eobject.EContent

/**
 * A multi-line text.
 */
class TextContent(text:String) extends PText(text) with EContent {
  import TextContent._
  setFont(fontText)
  setTextPaint(colorText)
  
def sort(xs: Array[Int]): Array[Int] =
  if(xs.length <= 1) xs
  else {
    val pivot = xs(xs.length / 2)
    Array.concat(
      sort(xs filter (pivot >)),
           xs filter (pivot ==),
      sort(xs filter (pivot <)))
  }
  
}

object TextContent {
  val fontText = new Font("Arial", Font.BOLD, 10)
  val colorText = new Color(50,50,50)  
}