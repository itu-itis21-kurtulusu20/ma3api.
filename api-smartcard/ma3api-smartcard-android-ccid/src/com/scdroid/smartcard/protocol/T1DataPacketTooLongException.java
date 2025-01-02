/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

/**
 * <tt>T1DataPacketTooLongException</tt> 
 * is thrown when the info-field of an I-Block is greater than 254 bytes
 *
 * @author  Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1DataPacketTooLongException.java,v 1.1.1.1 1999/10/05 15:08:48 damke Exp $
 */

public class T1DataPacketTooLongException extends RuntimeException {

  /**
	 * 
	 */
	private static final long serialVersionUID = 2353460932717876929L;

/**
   * Constructor accepting a message text
   *
   */
  public T1DataPacketTooLongException() { super(); }

  /**
   * Constructor accepting a message text
   *
   * @param   s - exception message
   */
  public T1DataPacketTooLongException(String s) { super(s); }
}
