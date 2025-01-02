/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

/** <tt>T1Protocol</tt> is a small subset of the T1 block protocol.
 *
 *  - chaining is not supported
 *  - EDC-byte calculation only with LDR (XORed), CRC is not provided.
 *  <p>
 *  see     ISO7816_3P9
 *  @author  Stephan Breideneich (sbreiden@de.ibm.com)
 *  @version $Id: T1BlockNotImplementedFeatureException.java,v 1.2 1999/11/03 12:37:19 damke Exp $
 *
 */
public class T1BlockNotImplementedFeatureException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = 3837083910499913531L;

T1BlockNotImplementedFeatureException(String s) {
    super(s);
  }
}
