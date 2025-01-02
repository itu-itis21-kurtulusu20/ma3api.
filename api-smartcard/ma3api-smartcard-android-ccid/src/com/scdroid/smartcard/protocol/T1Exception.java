/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

import com.scdroid.smartcard.SCException;

/**
 * <tt>T1Exception</tt> is the base exception for all T1 exceptions
 *
 * @author  Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1Exception.java,v 1.1.1.1 1999/10/05 15:08:48 damke Exp $
 */

public class T1Exception extends SCException {

  /**
	 * 
	 */
	private static final long serialVersionUID = 5314069793567448148L;

/**
   * Constructor
   */
  public T1Exception() { super(); }

  /**
   * Constructor accepting a message text
   *
   * @param   msg - exception message
   */
  public T1Exception(String msg) { super(msg); }
}
