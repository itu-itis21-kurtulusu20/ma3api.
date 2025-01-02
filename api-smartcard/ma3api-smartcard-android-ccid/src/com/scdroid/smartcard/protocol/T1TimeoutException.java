/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

/**
 * <tt>T1TimeoutException</tt> is thrown when an send- or receive-timeout occurs in the T1 protocol
 *
 * @author  Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1TimeoutException.java,v 1.1.1.1 1999/10/05 15:08:48 damke Exp $
 */

public class T1TimeoutException extends T1Exception {

  /**
	 * 
	 */
	private static final long serialVersionUID = 8749089794576250719L;

/**
   * Constructor accepting a message text
   *
   * @param   s - exception message
   */
  public T1TimeoutException(String s) { super(s); }
}
