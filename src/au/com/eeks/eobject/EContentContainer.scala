package au.com.eeks.eobject

/**
 * A container with e some content but that does not allow to create
 * sub-containers. Typically used to wrap Drag&Drop or Copy&Paste content.
 */
class EContentContainer(name:String) extends ETitledContainer(name)
        with EZoomable with EPannable with EDraggable with EDeletable {

  setBounds(0,0,300,200)

  /** adds content to the container and can resize according to content size */
  def add(content:EContent, resize:Boolean) = {
    super.add(content)
    if(resize)
      setBounds(getX, getY, content.getWidth+insets.left+insets.right,
                content.getHeight+insets.top+insets.bottom)
    content.setOffset(getX+insets.left, getY+insets.top)            
  }

}