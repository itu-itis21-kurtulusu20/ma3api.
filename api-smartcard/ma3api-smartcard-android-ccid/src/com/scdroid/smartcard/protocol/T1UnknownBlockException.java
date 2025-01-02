/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

/**
 * <tt>T1UnknownBlockException</tt> is thrown when an IO error occurs in the T1 protocol
 *
 * @author  Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1UnknownBlockException.java,v 1.1.1.1 1999/10/05 15:08:48 damke Exp $
 */

public class T1UnknownBlockException extends T1Exception {

  /**
	 * 
	 */
	private static final long serialVersionUID = 8162755261746815083L;

/**
   * Constructor accepting a message text
   *
   */
  public T1UnknownBlockException() { super(); }

  /**
   * Constructor accepting a message text
   *
   * @param   s - exception message
   */
  public T1UnknownBlockException(String s) { super(s); }
}
