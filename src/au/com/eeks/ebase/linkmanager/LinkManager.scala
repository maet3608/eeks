package au.com.eeks.ebase.linkmanager

import au.com.eeks.ebase.{EDB, ENode}

/**
 * Abstract base class to manage links to nodes, e.g. links that refer to
 * nodes that represent files within the file system or nodes within e
 * database.
 * Author : Stefan Maetschke
 * Version: 1.00
 * Date   : 11/11/2010
 */

abstract class LinkManager extends Iterable[ENode]


/**
 * Link manager that always returns no links.
 */
object LMEmpty extends LinkManager {
  def iterator = List[ENode]().iterator
}