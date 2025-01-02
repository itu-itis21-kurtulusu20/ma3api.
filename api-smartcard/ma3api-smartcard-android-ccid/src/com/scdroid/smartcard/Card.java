/*
 * Copyright 2013 FOXPLUS International Co., Ltd.
 *
 */
package com.scdroid.smartcard;

import com.scdroid.ccid.IReader;

/**
 * <p>
 * An abstract class to implement card class. For example, user can extends this class to implement an EMV Card Class.
 * And code the APDU commands to readable methods such as SelectFile(), ReadRecord() and VerifyPin() etc,.
 * <p>
 * To extend this class, subclass must implement {@link #IdentifyCard(byte[] ATR)} function, which identify the card is
 * the correct card by check ATR or select application id.
 * <p>
 * To implement APDU command function, subclass can choose command name as the function name. 
 * Construct the APDU command as a byte array. 
 * Send the command by {@link #Transmit(byte[] apdu, int swExpected, String commandName)}.
 * <p>
 * for example:
 * <pre> {@code
 * public byte[] ReadRecord(byte SFI, byte no, int len) throws SCException {
 *     // 0x00,0xb2,0x00,0x00,0x00
 *     byte[] cmd = new byte[] { 0x00, (byte) 0xB2, 0x00, 0x00, 0x00};
 *
 *     if (len > 255)
 *     	   throw new SCException("max read length > 255");
 *
 *     cmd[2] = no; // record number
 *     cmd[3] = (byte) ((SFI << 3) | 0x04); // file id
 *     cmd[4] = (byte) (len & 0xFF); // read length
 *
 *     byte[] rsp = Transmit(cmd, 0x9000, "READ RECORD");
 *
 *     byte[] data = new byte[rsp.length - 2];
 *     System.arraycopy(rsp, 0, data, 0, rsp.length - 2);
 *
 *     return data;
 * }
 * } </pre>
 */
public abstract class Card {
	
	
	public final static byte PROTOCOL_T0 = 0x01;
	public final static byte PROTOCOL_T1 = 0x02;
	public final static byte PROTOCOL_ANY = 0x03; //PROTOCOL_T0 | PROTOCOL_T0
	public final static byte PROTOCOL_RAW = 0x04;
	public final static byte PROTOCOL_T15 = 0x08;
	
	private IReader mReader = null;
	
	public Card () {
	}
	
	/** Not relevant to subclasses. */
	public void AttachReader(IReader reader) {
		mReader = reader;		
	}
	
	/** Not relevant to subclasses. */
	protected void finalize(){

	}

	/**
	 * Terminates a connection previously opened between the calling application and a smart card in the target reader
	 * @throws SCException
	 * smart card exception, check the message to see the exception detail
	 */
	public void Disconnect() throws SCException{
		mReader.DisConnectCard();
	}
	
	/**
	 * Sends a APDU command to the smart card and expects to receive data back from the card.
	 * @param apdu
	 * APDU commands data to be written to the card
	 * @return
	 * data returned from the card
	 * @throws SCException
	 * smart card exception, check the message to see the exception detail
	 */
	 public byte[] Transmit(byte[] apdu) throws SCException{
		 if (apdu == null) throw new NullPointerException();
		 
		 return mReader.Transmit(apdu);
	 }
	 
	/**
	 * Sends a APDU command to the smart card and expects to get correct card status bytes (SW1 & SW2).
	 * Also receive data back from the card.
	 * @param apdu
	 * APDU commands data to be written to the card
	 * @param swExpected
	 * card status bytes (SW1 SW2) expected, ex: 0x9000
	 * @param commandName
	 * name of the APDU command such as "SELECT FILE" or "READ RECORD" etc,. 
	 * name is used to show readable message when exception is thrown.
	 * @return
	 * data returned from the card
	 * @throws SCException
	 * smart card exception, check the message to see the exception detail
	 */
	private int mSW1SW2 = 0;
	public final byte[] Transmit(byte[] data, int swExpected, String commandName) throws SCException{
		byte[] rsp = null;

		try {
			rsp = Transmit(data);
		} catch (SCException e) {
			e.printStackTrace();
			
			if (commandName == null)
				throw e;
			else
				throw new SCException(commandName + " transmit failed: " + e.getMessage(), e);
		}

		if (rsp == null || rsp.length < 2)
			throw new SCException(commandName + " SW1/2 not available");

		int sw1 = rsp[rsp.length - 2] & 0xFF;
		int sw2 = rsp[rsp.length - 1] & 0xFF;
		mSW1SW2 = (sw1 << 8) | sw2;

		if (mSW1SW2 != swExpected)
			throw new SCException(commandName + " SW1/2 error: "
					+ String.format("%1$04X", mSW1SW2));

		return rsp;
	}

	public boolean isPresent() throws SCException {
		return mReader.isCardPresent();
	}
		
	/**
	 * get card status bytes(SW1 {@literal &} SW2) as int such as 0x9000
	 * @return
	 * card status bytes converted to int
	 */
	public int GetSW1SW2() {
		return mSW1SW2;
	}
	
	/**
	 * get card status bytes(SW1 {@literal &} SW2) as bytes array
	 * @return
	 * card status bytes array
	 */
	public byte[] GetSW1SW2Bytes() {
		byte[] swBytes = new byte[2];
		swBytes[0] = (byte) ((mSW1SW2 >> 8) & 0xFF);
		swBytes[1] = (byte) (mSW1SW2 & 0xFF);
		
		return swBytes;
	}

	/**
	 * Performs a SELECT command to select the ICC PSE, DDF, or ADF 
	 * @param data
	 * file id
	 * @return
	 * data returned from the card
	 * @throws SCException
	 * smart card exception, check the message to see the exception detail	 
	 */
	protected byte[] Select(byte[] data) throws SCException {
		if (data == null)
			throw new NullPointerException("aid must not be null");

		byte[] cmd = new byte[data.length + 6];
//		byte[] cmd = new byte[data.length + 5];

		cmd[0] = 0x00;
		cmd[1] = (byte) 0xA4;
		cmd[2] = 0x04;
		cmd[3] = 0x00;
		cmd[4] = (byte) data.length;
		System.arraycopy(data, 0, cmd, 5, data.length);
		cmd[cmd.length -1] = 0x00;

		return Transmit(cmd, 0x9000, "SELECT");

	}

	/**
	 * Performs a SELECT command to select an application.
	 * @param aid
	 * application id
	 * @throws SCException
	 * smart card exception, check the message to see the exception detail	 
	 */	
	protected void SelectApplication(byte[] aid) throws SCException {
		try {
			Select(aid);
		} catch (SCException e) {
			throw new SCException("no application", e);
		}		
	}
	
	/**
	 * Subclasses should override this method to identify the right card in the reader.
	 * Subclasses could check the ATR or Select AID and return true the tell the card is correct
	 * <p>
	 * example:
	 * <pre>
	 * 	protected boolean IdentifyCard(byte[] ATR){
	 * 	myATR = ATR;
	 * 	if (SelectPSE() != null)		
	 * 		return true;
	 * 		else
	 * 		return false;
	 * 	}
	 * </pre>
	 * @param ATR
	 * Answer to reset of the card
	 */
	public abstract boolean IdentifyCard(byte[] ATR);

}
