package au.com.eeks.ebase

import java.net.URI
import linkmanager.{LMEmpty, LinkManager}

/**
 * Abstract base class for nodes, which will carry some content within the derived
 * non-abstract classes. A node is e container for some information e.g. text, image,
 * URL, filepath and is connected to other nodes by parent and child links.
 * Author : Stefan Maetschke
 * Version: 1.00
 * Date   : 11/11/2010
 */

abstract class ENode {
  /** unique identifier */
  val uri:URI
  /** short label */
  var label:String
  /** longer description */
  var info:String
  /** parent links */
  val parents:LinkManager
  /** child links */
  val children:LinkManager
  /** reference to dag the node belongs to */
  val edag:EDag
}


class ENodeRoot(val edag:EDag) extends ENode {
  val uri     = new URI("urn:eeks:dag:root")
  var label   = "eeks"
  var info    = "eeks root node"
  val parents = LMEmpty
  val children = LMEmpty
}

