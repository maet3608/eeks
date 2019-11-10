package au.com.eeks.utils

import au.com.eeks.content._
import edu.umd.cs.piccolo.nodes.PHtmlView
import java.net.URL
import java.io.File
import java.awt.geom.Point2D
import edu.umd.cs.piccolo.PCanvas

import java.awt.{Point,Image,Toolkit}
import java.awt.datatransfer._
import java.io.{InputStream}
import java.awt.dnd._
import au.com.eeks.eobject.{EContentContainer, EContent, EContainer}
import collection.JavaConversions

/**
 * Drag and drop support.
 */
class DragDropSupport(canvas:PCanvas) extends DropTargetListener  {
  new DropTarget(canvas, this)

  def dragEnter(dtde:DropTargetDragEvent) = {}
  def dragExit(dtde:DropTargetEvent) = {}
  def dragOver(dtde:DropTargetDragEvent) = {}
  def dropActionChanged(dtde:DropTargetDragEvent) = {}

  def drop(dtde:DropTargetDropEvent) = {
    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE)
    DataTransfer.readContents(dtde.getTransferable)
    dtde.dropComplete(true)
  }
}


class DragDropSupportTest(canvas:PCanvas) extends DropTargetListener  {
  new DropTarget(canvas, this)

  def dragEnter(dtde:DropTargetDragEvent) = {}
  def dragExit(dtde:DropTargetEvent) = {}
  def dragOver(dtde:DropTargetDragEvent) = {}
  def dropActionChanged(dtde:DropTargetDragEvent) = {}

  def drop(dtde:DropTargetDropEvent) = {
    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE)
    val tr = dtde.getTransferable()
    for(flavor <- tr.getTransferDataFlavors() )
        println("Flavor: " + flavor.getMimeType())
    dtde.dropComplete(true)
  }
}

/**
 * Support for copy and paste via system clip board.
 */
class CopyPasteSupport(canvas:PCanvas) extends ClipboardOwner {
  def lostOwnership(clipboard:Clipboard, contents:Transferable) = { /*do nothing*/ }
  
  def writeClipboardContents(text:String) = 
    Toolkit.getDefaultToolkit.getSystemClipboard.setContents(new StringSelection(text),this)
}


object DataTransfer {
  private val mime = """(.+); +class=([^;]+).*""".r
  private var canvas:PCanvas = _

  def apply(pcanvas:PCanvas) = {
    canvas = pcanvas
    new DragDropSupport(canvas)
    new CopyPasteSupport(canvas)
  }

  private def getContainerAndPosition: (EContainer,Point) = {
    var position = canvas.getMousePosition()
    val pickPath = canvas.getCamera.pick(position.x, position.y, 0.1)
    var stack = pickPath.getNodeStackReference
    for(i <- stack.size-1 to 0 by -1) {
      val node = stack.get(i)
      if(node.isInstanceOf[EContainer]) {
        val container = node.asInstanceOf[EContainer]
        pickPath.canvasToLocal(position,container)
        container.camera.localToView(position)
        return (container,position)
      }  
    }
    throw new RuntimeException("No container to receive transferred data!")
  }

  def readContents(contents:Transferable):Unit = {
    val flavors = contents.getTransferDataFlavors
    val (container,position) = getContainerAndPosition


    def addContent(content:EContent) = {
      container.add(content)
      container.repaint()
    }

    def isMimeType(flavor:DataFlavor, mime:String) =  flavor.getMimeType.startsWith(mime)
    def isMimeHTML(flavor:DataFlavor) = isMimeType(flavor,"text/html; class=java.lang.String")
    def isMimeURL(flavor:DataFlavor)  = isMimeType(flavor,"text/uri-list;")
    def isMimeText(flavor:DataFlavor) = isMimeType(flavor,"text/plain; class=java.lang.String")
    
    def isText = flavors.exists(isMimeText)
    def isURL = flavors.exists(isMimeURL)
    def isHTML = flavors.exists(isMimeHTML)
    def isImage = contents.isDataFlavorSupported(DataFlavor.imageFlavor)
    def isFileList = flavors.exists(_.isFlavorJavaFileListType)
    def isInputStream = flavors.exists(_.isRepresentationClassInputStream)
    def isType[T](flavor:DataFlavor) = contents.getTransferData(flavor).isInstanceOf[T]
    
    def getAs[T](flavor:DataFlavor) = contents.getTransferData(flavor).asInstanceOf[T]
    def getFileList = getAs[java.util.List[File]](flavors.filter(_.isFlavorJavaFileListType).head)
    def getImage = getAs[java.awt.Image](DataFlavor.imageFlavor)
    def getURL = getAs[java.net.URL](flavors.filter(isType[java.net.URL]).head)
    def getHTML = getAs[java.lang.String](flavors.filter(isMimeHTML).head)
    def getText = getAs[java.lang.String](flavors.filter(isMimeText).head)
    def getInputStream = getAs[InputStream](flavors.filter(_.isRepresentationClassInputStream).head)
        
    if(isURL) 
      addContent(ContainerFactory.url(getURL, position))
    else if(isHTML) 
      addContent(ContainerFactory.html(getHTML, position))
    else if(isText) 
      addContent(ContainerFactory.text(getText, position))
    else if(isImage) 
      addContent(ContainerFactory.image(getImage, position))
    else if(isFileList) {
      val filesnames = getFileList
      val pos = new Point(position.getX.toInt, position.getY.toInt)
      val it = filesnames.iterator
      while(it.hasNext) {
        addContent(ContainerFactory.file(it.next, pos))
        pos.translate(0,50)
      }
    }  
   // else if(isInputStream) {
   //   val reader = new BufferedReader(new InputStreamReader(getInputStream))
   //   println(reader.readLine())      
   // }
  }  
}

object ContainerFactory {
  
  def url(url:URL, position:Point2D) = {
    val name = "URL: "+url.toString().take(20).mkString
    val container = new EContentContainer(name)
    val content = new URLContent(url)
    container.setWidth(200)
    container.setHeight(37)
    container.setOffset(position)
    container.add(content,false)
    container
  }
  
  def file(file:File, position:Point2D) = {
    val container = new EContentContainer(file.getName)
    val content = new FileContent(file)
    container.setBounds(position.getX, position.getY, 100, 37)
    container.add(content,false)
    container
  }

  def image(image:Image, position:Point2D) = {
    val container = new EContentContainer("Image")
    val content = new ImageContent(image)
    container.setBounds(position.getX, position.getY, 0,0)
    container.add(content,true)
    container
  }

  def text(text:String, position:Point2D) = {
    val limitSize = (text.lines.map(_.length).reduceLeft(_ max _) > 80)
    val name = text.lines.next
    val container = new EContentContainer(name)
    val content = new TextContent(text)
    content.setConstrainWidthToTextWidth(!limitSize)
    //content.setConstrainHeightToTextHeight(false)
    if(limitSize) content.setBounds(0,0,300,200)
    container.setOffset(position)
    container.add(content,true)
    container
  }
  
  def html(html:String, position:Point2D) = {
    val container = new EContentContainer("HTML")
    val content = new PHtmlView(html) with EContent
    content.setWidth(300)
    container.setOffset(position)
    container.add(content, true)  
    container
  }  
}





