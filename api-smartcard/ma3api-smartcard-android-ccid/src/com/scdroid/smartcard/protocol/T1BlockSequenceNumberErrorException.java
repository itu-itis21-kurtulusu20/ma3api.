/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

public class T1BlockSequenceNumberErrorException extends T1Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6805330233009691251L;

	/**
	 * Constructor
	 */
	public T1BlockSequenceNumberErrorException() {
		super();
	}

	/**
	 * Constructor accepting a message text
	 * 
	 * @param msg
	 *            - exception message
	 */
	public T1BlockSequenceNumberErrorException(String msg) {
		super(msg);
	}
}
