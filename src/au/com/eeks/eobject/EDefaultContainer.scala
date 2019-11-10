package au.com.eeks.eobject

import edu.umd.cs.piccolo.event.PInputEvent
import java.awt.event.KeyEvent


/**
 * Describes the typical container that allows the creation of sub-containers.
 */
class EDefaultContainer(name:String) extends ETitledContainer(name)
        with EZoomable with EPannable with EDraggable
        with EDeletable with EContainerable with EPastable {

  setBounds(0,0,300,200)
 
}

