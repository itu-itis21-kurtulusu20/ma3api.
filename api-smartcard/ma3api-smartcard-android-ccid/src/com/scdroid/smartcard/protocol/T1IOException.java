/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

/**
 * <tt>T1IOException</tt> is thrown when an IO error occurs in the T1 protocol
 *
 * @author  Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1IOException.java,v 1.1.1.1 1999/10/05 15:08:48 damke Exp $
 */

public class T1IOException extends T1Exception {

  /**
	 * 
	 */
	private static final long serialVersionUID = 7080716027816983192L;

/**
   * Constructor accepting a message text
   *
   * @param   s - exception message
   */
  public T1IOException(String s) { super(s); }
}
