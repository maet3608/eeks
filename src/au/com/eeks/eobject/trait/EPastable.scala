package au.com.eeks.eobject

import au.com.eeks.utils.DataTransfer
import java.awt.event.KeyEvent
import edu.umd.cs.piccolo.event.PInputEvent
import java.awt.event.InputEvent.CTRL_MASK
import java.awt.Toolkit
import java.awt.datatransfer.{Clipboard, Transferable, StringSelection, ClipboardOwner}

/**
 * Allows Copy and Paste
 */
trait EPastable extends EFocusable {

  /** Pastes when CTRL-V is pressed */
  override def keyPressed(event:PInputEvent) = {
    super.keyPressed(event)
    if(event.getKeyCode == KeyEvent.VK_V && event.getModifiers == CTRL_MASK) 
      DataTransfer.readContents(Toolkit.getDefaultToolkit.getSystemClipboard.getContents(null))
  }

}