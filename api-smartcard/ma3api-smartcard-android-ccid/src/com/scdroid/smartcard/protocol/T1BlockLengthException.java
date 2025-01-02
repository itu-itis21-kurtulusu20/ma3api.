/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

/**
 * <tt>T1BlockLengthException</tt> is thrown when length of byte array (raw data) is incorrect
 *
 * @author  Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1BlockLengthException.java,v 1.1.1.1 1999/10/05 15:08:48 damke Exp $
 */

public class T1BlockLengthException extends T1Exception {

  /**
	 * 
	 */
	private static final long serialVersionUID = -353901767389229663L;

/**
   * Constructor
   */
  public T1BlockLengthException() { super(); }

  /**
   * Constructor accepting a message text
   *
   * @param   msg - exception message
   */
  public T1BlockLengthException(String msg) { super(msg); }
}
