package au.com.eeks

import eobject.{EDefaultContainer, ERootContainer}
import java.awt.event.MouseEvent
import java.awt.event.MouseAdapter
import java.awt.MenuItem
import java.awt.PopupMenu
import java.util.Random
import java.awt.{Rectangle,BasicStroke}
import java.awt.Window
import java.awt.Toolkit
import javax.swing._
import javax.swing.border.LineBorder
import java.awt.Color
import java.awt.Frame.MAXIMIZED_BOTH
import java.awt.geom.Point2D
import edu.umd.cs.piccolo.PCamera
import edu.umd.cs.piccolo.PLayer
import edu.umd.cs.piccolo.PCanvas
import edu.umd.cs.piccolo.event._
import edu.umd.cs.piccolox.event.PStyledTextEventHandler
import edu.umd.cs.piccolo.nodes.{PText,PPath}
import edu.umd.cs.piccolox.PFrame
import utils.DataTransfer


/**
 * Zoomable Information Management Software.
 */
class Eeks extends PFrame("eeks", false, null) {
  //private var menu = new JPopupMenu()
  setSize(1000,700)
  //setExtendedState(getExtendedState|MAXIMIZED_BOTH)
  
  override def initialize:Unit = {
    val canvas = getCanvas
    val layer = canvas.getLayer
    DataTransfer(canvas)
    
    val root = ERootContainer(this)

    /*
    val c1 = new EDefaultContainer("C1")
    c1.setBounds(20,20,400,400)
    root += c1

    val c2 = new EDefaultContainer("C2")
    c2.setBounds(50,50,300,300)
    c1 += c2

    val c3 = new EDefaultContainer("C3")
    c3.setBounds(100,100,50,50)
    c2 += c3

    val c4 = new EDefaultContainer("C4")
    c4.setBounds(160,160,50,50)
    c2 += c4
    */
    
    //menu.add(new JMenuItem("new Container"))
    //canvas.addMouseListener(new MouseAdapter() {
    //  override def mouseReleased(event:MouseEvent) = {
    //    if(event.isPopupTrigger) 
    //      menu.show(event.getComponent, event.getX, event.getY)
    //  }
    //})    
    
    //fillBackground(canvas)

    //val textHandler = new EStyledTextEventHandler(canvas)
    //canvas.addInputEventListener(textHandler)
    
    canvas.removeInputEventListener(canvas.getPanEventHandler)
    canvas.removeInputEventListener(canvas.getZoomEventHandler)
  }
  
  
  /* private def fillBackground(canvas:PCanvas) = {
    val layer = canvas.getLayer
    val rand = new Random()
    
    canvas.setBackground(new Color(250,250,250))
    for(i <- 0 to 5) {
      val gray = (220+20*rand.nextFloat).toInt
      println(gray)
      val x = canvas.getWidth*rand.nextFloat
      val y = canvas.getHeight*rand.nextFloat
      val w = canvas.getWidth*rand.nextFloat
      val h = canvas.getHeight*rand.nextFloat
      val e = PPath.createEllipse(x,y,w,h)
      e.setPaint(null)
      e.setStrokePaint(new Color(gray,gray,gray))
      e.setStroke(new BasicStroke(1 + 10*rand.nextFloat))
      e.setPickable(false)
      layer.addChild(e)
    }  
  }*/
  
  private def fillBackground(canvas:PCanvas) = {
    canvas.setBackground(new Color(250,250,250))
    val text = new PText("eeks!")
    text.setOffset(canvas.getWidth/100, 0)
    text.scale(canvas.getWidth/text.getWidth)
    text.setTextPaint(new Color(245,245,250))
    canvas.getLayer.addChild(text)
  }  
}


object Eeks extends App {
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
  new Eeks()
}