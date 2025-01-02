/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

/**
 * <tt>T1BlockEDCErrorException</tt> is thrown when length of byte array (raw data) is incorrect
 *
 * @author  Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1BlockEDCErrorException.java,v 1.1.1.1 1999/10/05 15:08:48 damke Exp $
 */

public class T1BlockEDCErrorException extends T1Exception {

  /**
	 * 
	 */
	private static final long serialVersionUID = 8725137938549906335L;

/**
   * Constructor
   */
  public T1BlockEDCErrorException() { super(); }

  /**
   * Constructor accepting a message text
   *
   * @param   msg - exception message
   */
  public T1BlockEDCErrorException(String msg) { super(msg); }
}
