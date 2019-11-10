package au.com.eeks.ebase

/**
 * A directed, acyclic graph of nodes.
 * Author : Stefan Maetschke
 * Version: 1.00
 * Date   : 11/11/2010
 */

class EDag {
  val root:ENode = new ENodeRoot(this)
}