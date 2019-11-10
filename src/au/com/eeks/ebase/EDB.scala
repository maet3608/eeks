package au.com.eeks.ebase

import java.net.URI

/**
 * Database interface to store and retrieve nodes from some persistent storage
 * Author : Stefan Maetschke
 * Version: 1.00
 * Date   : 11/11/2010
 */

abstract class EDB {
  /** Retrieves the node with the given uri from the database */
  def get(uri:URI):ENode
}