/*
 * Copyright 2013 FOXPLUS International Co., Ltd.
 *
 */
package com.scdroid.ccid;

/** <tt>IReader</tt>
 *
 * Reader interface definition 
 *
 * @author  Roman (roman@scdroid.com)
 * @version 1.26
 */
import com.scdroid.smartcard.Card;
import com.scdroid.smartcard.SCException;

public interface IReader {
	public void logLevel(int level);

	public void logData(String message);

	public void Open() throws SCException;

	public void Close();

	public void Write(byte[] cmd) throws SCException;

	public byte[] Read() throws SCException;

	public Card ConnectCard(String CardClass, byte PreferredProtocols)
			throws SCException;

	public void DisConnectCard() throws SCException;

	public byte[] Transmit(byte[] data) throws SCException;

	public void WaitCardEvent(int event) throws SCException;

	public boolean isCardPresent() throws SCException;

	// new function above version 1.2.5
	public Card ConnectCard(String CardClass, int Slot, byte PreferredProtocols)
			throws SCException;
}
