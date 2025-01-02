package com.scdroid.smartcard;

public class EMVCard extends Card {
	protected byte[] myATR;
	//1PAY.SYS.DDF01
	public final static byte[] PAY1_SYS_DDF01 = new byte[] {0x31, 0x50, 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53, 0x2E, 0x44, 0x44, 0x46, 0x30, 0x31};
	//2PAY.SYS.DDF01
	public final static byte[] PAY2_SYS_DDF01 = new byte[] {0x32, 0x50, 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53, 0x2E, 0x44, 0x44, 0x46, 0x30, 0x31};
	
	public EMVCard() {
	}
	
	public boolean IdentifyCard(byte[] ATR){
		myATR = ATR;
		//if (SelectPSE() != null)		
			return true;
		//else
			//return false;
	}

	/*****************************************************************************
	 *
	 *					Basic. functions
	 *
	 ****************************************************************************/		
	public void SelectFile(byte[] id) throws SCException {
		byte[] cmd = null;
		
		
		try {
			Transmit(cmd, 0x9000,  "SELECT FILE");			
		} catch (SCException e) {
			throw new SCException("no file: 0x" + String.format("%1$02X%2$02X", id[0] ,id[1]), e);
		}
	}
	
	public byte[] Select_1PAY_SYS_DDF01() {

		try {
			return Select(PAY1_SYS_DDF01);
		} catch (SCException e) {
		}
		
		return null;
	}
	
	
	public byte[] Select_2PAY_SYS_DDF01() {

		try {
			return Select(PAY2_SYS_DDF01);
		} catch (SCException e) {
		}
		
		return null;
	}
	public void SelectMF(int id) throws SCException {
		byte[] cmd = null;
		
		if (id == 0) {
			 cmd = new byte[] { 0x00, (byte)0xA4, 0x00, 0x00, 0x00};
		} else {
			 cmd = new byte[] { 0x00, (byte)0xA4, 0x00, 0x00, 0x02, 0x3F, 0x00};		
		}
		 
		try {
			Transmit(cmd, 0x9000,  "SELECT MASTER FILE");			
		} catch (SCException e) {
			if (id == 0)
				throw new SCException("no master file", e);
			else
				throw new SCException("no master file 0x3F00", e);
		}		
	}


	//00010100 = P2
	//00010    = SFI (= 2 << 3)
	//	   100 = "File number can be found in P1" (=4)
	public byte[] ReadRecord(byte SFI, byte no, int len) throws SCException {
		//0x00,0xb2,0x00,0x00,0x00
		byte[] cmd = new byte[] { 0x00, (byte) 0xB2, 0x00, 0x00, 0x00};		

		if (len > 255)
			throw new SCException("max read lenght > 255");

	    cmd[2] = no;	//file id
	    cmd[3] = (byte) ((SFI << 3) | 0x04);	//record number
	    cmd[4] = (byte) (len & 0xFF); //data length
	    
		byte[] rsp = Transmit(cmd, 0x9000,  "READ RECORD");
		
		byte[] data = new byte[rsp.length - 2];		
		System.arraycopy(rsp, 0, data, 0, rsp.length - 2);
		
		return data;		
	}
	
	public byte[] GetProcessingOptions(byte[] PDOL) throws SCException {
		int datalen = 2;
		
		if (PDOL != null)
			datalen = 2 + PDOL.length;
		
		byte[] cmd = new byte[6 + datalen];
		
		cmd[0] = (byte) 0x80;
		cmd[1] = (byte) 0xA8;
		cmd[2] = 0x00;
		cmd[3] = 0x00;
		cmd[4] = (byte) datalen; //Lc
		cmd[5] = (byte) 0x83;
		cmd[6] = 0x00;
		
		if (PDOL != null) {
			System.arraycopy(PDOL, 0, cmd, 7, PDOL.length);
			cmd[6] = (byte) PDOL.length;
		}
		
		cmd[cmd.length -1] = 0x00; //Le

		byte [] rsp = Transmit(cmd, 0x9000, "GET PROCESSING OPTIONS");

		return rsp;
	}
	
	public void internalAuthenticate(byte[] data) throws SCException {
		//0x00,0x88,0x00,0x00,datelen, data, 0x00
		if (data == null)
			throw new NullPointerException("data must not be null");
		
		byte[] cmd = new byte[data.length + 6];
		
		cmd[0] = 0x00;
		cmd[1] = (byte) 0x88;
		cmd[2] = 0x00;
		cmd[3] = 0x00;
		cmd[4] = (byte) data.length;
		System.arraycopy(data, 0, cmd, 5, data.length);
		cmd[cmd.length - 1] = 0x00;
		
		Transmit(cmd, 0x9000,  "INTERNAL AUTHENTICATE");
	}

	
	public byte[] getChallenge() throws SCException {
		//0x00,0x84,0x00,0x00,0x00
		byte[] cmd = new byte[] { 0x00, (byte)0x84, 0x00, 0x00, 0x00};		
		byte[] rsp = Transmit(cmd, 0x9000,  "GET CHALLENGE");
		
		byte[] random = new byte[8];
		System.arraycopy(rsp, 0, random, 0, 8);
		
		return random;
	}
	/*
	 * ATC (tag '9F36')
	 * Last Online ATC Register (tag '9F13')
	 * PIN Try Counter (tag '9F17')
	 * Log Format (tag '9F4F')
	 */
	public byte[] GetData(byte[] tag, int LE) throws SCException {
		//0x80,0xCA,tag,Le
		byte[] cmd = new byte[] { (byte) 0x80, (byte)0xCA, 0x00, 0x00, 0x00};
		
		cmd[2] = tag[0];
		cmd[3] = tag[1];
		cmd[4] = (byte) LE;
		
		return Transmit(cmd, 0x9000,  "GET DATA");
	}
	
	public byte[] GetData(byte[] tag) throws SCException {
		return GetData(tag, 0);
	}
	
    /**
     * The VERIFY command is used for OFFLINE authentication.
     * The Transaction PIN Data (input) is compared with the Reference PIN Data
     * stored in the application (ICC).
     *
     * TODO:
     * Plaintext PIN has been tested and verified OK. Enciphered PIN not implemented
     *
     * @param pin the PIN to verify
     * @param transmitInPlaintext 
     * @return
     */
	private int mPinTryCounter = -1;
	public void VerifyPIN(byte[] pin, boolean transmitInPlaintext) throws SCException {
		//0x00,0x20,0x00,Qualifier,pin length, pin code
		
        //String pinStr = String.valueOf(pin);
        int len = pin.length;
        if(len < 2 || len > 6){ //0x0C
            throw new SCException("Invalid PIN length. Must be in the range 4 to 12. Length="+len);
        }
        
		byte[] cmd = new byte[5 + 8];
		
		cmd[0] = 0x00;
		cmd[1] = 0x20;
		cmd[2] = 0x00;
		
		//qualifier of the reference data (P2):
		if (transmitInPlaintext)
			cmd[3] = (byte) 0x80;
		else
			cmd[3] = (byte) 0x88;

		cmd[4] = 0x08; //Lc
		
		if (transmitInPlaintext) {
			//fill pin padding to 0xFF
			for (int i =0; i < 7; i++)
				cmd[6 + i] = (byte) 0xFF;					
			
	        cmd[5] = (byte)0x20; //Control field (binary 0010xxxx)
	        cmd[5] |= len * 2;
	        
	        System.arraycopy(pin, 0, cmd, 6, len);
		} else {
			 //Encipher PIN
			throw new SCException("Encipher PIN not implemented");
		}

		try {
			Transmit(cmd, 0x9000,  "VERIFY PIN");
		} catch (SCException e) {
			int sw = GetSW1SW2();
			
			//Command not allowed; authentication method blocked
			if (sw == 0x6983)
				throw new SCException("No more retries left. CVM blocked");
			
			byte sw1 = (byte) ((sw >> 8) & 0xFF);
			byte sw2 = (byte) (sw & 0xFF);
			
			if (sw1 == (byte) 0x63 && (sw2 & 0xF0) == (byte) 0xC0) {
				mPinTryCounter = (sw2 & 0x0F);
				throw new SCException("Verify pin fail, pin retries left =" + mPinTryCounter);				
			}
			
			throw new SCException("VERIFY PIN SW1/2 error: " + String.format("%1$04X", sw));			
		}		
		
	}
	
	//PIN Try Counter (tag '9F17')
	public int GetPinTryCounter() throws SCException{
		
		if (mPinTryCounter != -1)
			return mPinTryCounter;
		
		byte[] data = null;
		
		try {
			data = GetData( new byte[] {(byte) 0x9F, 0x17});	
		} catch (SCException e) {
			//Try again with new length
			data = GetData( new byte[] {(byte) 0x9F, 0x17}, 4);			
		}
		//ex 9F 17 01 01
		if (data[0] == (byte)0x9F && data[1] == (byte)0x17)
			return data[3];
		else
			throw new SCException("PIN Try Counter tag on found, data = " + ToHexString(data));
	}
	
	public void VerifyPIN(String pin, boolean transmitInPlaintext) throws SCException {
		VerifyPIN(fromHexString(pin), transmitInPlaintext);
	}
	
	public void VerifyPIN(long pin, boolean transmitInPlaintext) throws SCException {
		String pinStr = String.valueOf(pin);
		VerifyPIN(fromHexString(pinStr), transmitInPlaintext);
	}

	/*****************************************************************************
	 *
	 *					Misc. functions
	 *
	 ****************************************************************************/	
	private byte[] fromHexString(String HexString){
		int len = HexString.length() / 2;

		byte[] bytes = new byte[len];

		for (int i = 0; i < len; i++)
			bytes[i] = (byte) Integer.parseInt(HexString.substring(2 * i, 2 * i + 2), 16);

		return bytes;		
	}
	
	private String ToHexString(byte[] data) {
		String HexString = "";
		
		for (int i = 0; i < data.length; i++) {
			HexString += " " + String.format("%02X", data[i]);
		}
		return HexString;
	}
}
