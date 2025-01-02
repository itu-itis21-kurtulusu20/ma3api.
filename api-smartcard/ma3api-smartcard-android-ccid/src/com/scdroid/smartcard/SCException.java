/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
/*
 * Copyright 2013 FOXPLUS International Co., Ltd.
 *
 */
package com.scdroid.smartcard;

public class SCException extends Exception {

	Byte errorCode;
	boolean mustDisconnectCard = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7466330288320126152L;

	public SCException() {
		super();
	}

	public SCException(String string) {
		super(string);
	}

	public SCException(String string, Exception e) {
		super(string, e);
	}

	public SCException(final byte errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public final Byte getErrorCode() {
		return this.errorCode;
	}

	public SCException(final byte errorCode, final boolean mustDisconnectCard) {
		this.errorCode = errorCode;
		this.mustDisconnectCard = mustDisconnectCard;
	}

	public final boolean mustDisconnectCard() {
		return this.mustDisconnectCard;
	}
}
