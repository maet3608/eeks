package au.com.eeks.utils



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;
import edu.umd.cs.piccolox.nodes.PStyledText;

/**
 * Based on code by Lance Good
 */
class EStyledTextEventHandler(canvas:PCanvas) extends PBasicInputEventHandler() {
  protected var editor:JTextComponent = null
  protected var docListener:DocumentListener = null
  protected var editedText:PStyledText = null

  private val filter = new PInputEventFilter
  filter.setOrMask(InputEvent.BUTTON1_MASK | InputEvent.BUTTON3_MASK)
  setEventFilter(filter)
  initEditor(DefaultTextEditor())

  protected def initEditor(newEditor:JTextComponent):Unit = {
  editor = newEditor
  canvas.setLayout(null)
  canvas.add(editor)
  editor.setVisible(false)
  docListener = createDocumentListener
}


protected def createDocumentListener:DocumentListener = {
    new DocumentListener() {
      def removeUpdate(e:DocumentEvent):Unit = reshapeEditorLater
      def insertUpdate(e:DocumentEvent):Unit = reshapeEditorLater
      def changedUpdate(e:DocumentEvent):Unit = reshapeEditorLater
    }
  }

  def createText:PStyledText = {
      val newText = new PStyledText()
      val doc = editor.getUI().getEditorKit(editor).createDefaultDocument()
      if (doc.isInstanceOf[StyledDocument] && missingFontFamilyOrSize(doc)) {
        val eFont = editor.getFont()
        val sas = new SimpleAttributeSet()
        sas.addAttribute(StyleConstants.FontFamily, eFont.getFamily())
        sas.addAttribute(StyleConstants.FontSize, eFont.getSize())
        doc.asInstanceOf[StyledDocument].setParagraphAttributes(0, doc.getLength(), sas, false)
      }
      newText.setDocument(doc)
      newText
  }

  private def missingFontFamilyOrSize(doc:Document):Boolean = {
      !doc.getDefaultRootElement.getAttributes.isDefined(StyleConstants.FontFamily) ||
      !doc.getDefaultRootElement.getAttributes.isDefined(StyleConstants.FontSize)
  }

  override def mousePressed(event:PInputEvent):Unit = {
      val pickedNode = event.getPickedNode()

      stopEditing(event)

      if(event.getButton != MouseEvent.BUTTON3) {
        return
      }

      if(pickedNode.isInstanceOf[PStyledText]) {
        startEditing(event, pickedNode.asInstanceOf[PStyledText])
      }
      else if(pickedNode.isInstanceOf[PCamera]) {
        val newText = createText
        val pInsets = newText.getInsets
        newText.translate(event.getPosition().getX() - pInsets.left, event.getPosition().getY() - pInsets.top)
        startEditing(event, newText);
      }
  }

  def startEditing(event:PInputEvent, text:PStyledText):Unit = {
      val pInsets = text.getInsets
      val nodePt = new Point2D.Double(text.getX() + pInsets.left, text.getY() + pInsets.top)
      text.localToGlobal(nodePt);
      event.getTopCamera().viewToLocal(nodePt);

      editor.setDocument(text.getDocument())
      editor.setVisible(true)

      val bInsets = editor.getBorder().getBorderInsets(editor);
      editor.setLocation(nodePt.getX.toInt - bInsets.left, nodePt.getY.toInt - bInsets.top);
      reshapeEditorLater

      dispatchEventToEditor(event)
      canvas.repaint()

      text.setEditing(true)
      text.getDocument().addDocumentListener(docListener)
      editedText = text
  }

  def stopEditing(event:PInputEvent):Unit = {
      if(editedText == null) return

      editedText.getDocument().removeDocumentListener(docListener);
      editedText.setEditing(false);

      if(editedText.getDocument().getLength() == 0) {
        editedText.removeFromParent()
      }
      else {
        editedText.syncWithDocument()
      }

      if(editedText.getParent() == null) {
        editedText.setScale(1.0 / event.getCamera().getViewScale())
        canvas.getLayer().addChild(editedText)
      }
      editor.setVisible(false)
      canvas.repaint()

      editedText = null
  }

  def dispatchEventToEditor(event:PInputEvent):Unit = {
      // We have to nest the mouse press in two invoke laters so that it is
      // fired so that the component has been completely validated at the new
      // size and the mouse event has the correct offset
      SwingUtilities.invokeLater(new Runnable() {
        def run() = {
          SwingUtilities.invokeLater(new Runnable() {
            def run() = {
              val me = new MouseEvent(editor, MouseEvent.MOUSE_PRESSED, event.getWhen(), event
                  .getModifiers()
                  | InputEvent.BUTTON1_MASK, (event.getCanvasPosition().getX() - editor.getX()).toInt,
                  (event.getCanvasPosition().getY() - editor.getY()).toInt, 1, false);
              editor.dispatchEvent(me)
            }
          })
        }
      })
  }

  /**
   * Adjusts the shape of the editor to fit the current document.
   */
  def reshapeEditor:Unit = {
        import DefaultTextEditor.TEXT_EDIT_PADDING

        if(editedText != null) {
          var prefSize = editor.getPreferredSize();

          val textInsets = editedText.getInsets()
          val editorInsets = editor.getInsets()

          var width = 0
          if (editedText.getConstrainWidthToTextWidth()) {
            width = prefSize.getWidth.toInt
          }
          else {
            width = (editedText.getWidth() - textInsets.left - textInsets.right + editorInsets.left
                + editorInsets.right + TEXT_EDIT_PADDING).toInt
          }
          prefSize.setSize(width, prefSize.getHeight())
          editor.setSize(prefSize)

          prefSize = editor.getPreferredSize()
          var height = 0
          if (editedText.getConstrainHeightToTextHeight()) {
            height = prefSize.getHeight().toInt
          }
          else {
            height = (editedText.getHeight() - textInsets.top - textInsets.bottom + editorInsets.top
                + editorInsets.bottom + TEXT_EDIT_PADDING).toInt
          }
          prefSize.setSize(width, height);
          editor.setSize(prefSize);
        }
      }

      /**
       * Sometimes we need to invoke this later because the document events seem
       * to get fired before the text is actually incorporated into the document.
       */
      protected def reshapeEditorLater:Unit = {
          SwingUtilities.invokeLater(new Runnable() {
            def run():Unit = reshapeEditor
          });
      }


}

object DefaultTextEditor extends JTextPane {
  val serialVersionUID = 1L
  val TEXT_EDIT_PADDING = 3
  private val padding = new EmptyBorder(TEXT_EDIT_PADDING,
        TEXT_EDIT_PADDING, TEXT_EDIT_PADDING, TEXT_EDIT_PADDING)
  setBorder(new CompoundBorder(new LineBorder(Color.black), padding))
  
  def apply() = {
    this
  }

  /**
   * Set some rendering hints - if we don't then the rendering can be
   * inconsistent. Also, Swing doesn't work correctly with fractional
   * metrics.
   */
  override def paint(graphics:Graphics):Unit = {
    val g2 = graphics.asInstanceOf[Graphics2D]
                                   g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    //g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF)

    super.paint(graphics);
  }
}