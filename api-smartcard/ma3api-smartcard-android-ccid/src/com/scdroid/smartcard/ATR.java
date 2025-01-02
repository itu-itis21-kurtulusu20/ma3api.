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

/**
 * <tt>ATR</tt>
 * 
 * ATR parsing functions
 * 
 * @author Roman (roman@scdroid.com)
 * @version 1.26
 */
public class ATR {

	public class TByte {
		public byte value;

		TByte(byte v) {
			value = v;
		}

		public String toString() {
			return String.format("%02X", value);
		}
	}

	private byte[] mATR = null;
	public TByte TS = null;
	public TByte T0 = null;
	public TByte TA[] = { null, null, null, null, null };
	public TByte TB[] = { null, null, null, null, null };
	public TByte TC[] = { null, null, null, null, null };
	public TByte TD[] = { null, null, null, null, null };

	boolean TAiT1found = false;
	boolean TBiT1found = false;
	boolean TCiT1found = false;

	public TByte HistoricalBytes[] = null;
	public TByte TCK = null;

	private byte mProtocol = 0x00;
	public byte DefaultProtocol = 0x00;
	public byte AvailableProtocols = 0x00;

	public final static byte T_0 = 0;
	public final static byte T_1 = 1;
	public final static byte T_15 = 15;

	public final static byte PROTOCOL_T0 = 0x01;
	public final static byte PROTOCOL_T1 = 0x02;
	public final static byte PROTOCOL_RAW = 0x04;
	public final static byte PROTOCOL_T15 = 0x08;

	public final static int ATR_MAX_PROTOCOLS = 7; /*
													 * Maximun number of
													 * protocols
													 */

	public boolean hasPPS1 = false;

	public int Fi = 0;
	public int Di = 0;
	public byte IFSC = 0x20;
	public byte IFSD = 0x20;
	// public byte IFSC = (byte) 0xFE;

	public byte BWI = 0x04; // default value
	public byte CWI = 0x0D; // default value

	// modify by romanb 20/13/03/27
	// public byte EDC = 0;
	public byte EDC = EDC_LRC; // default use LRC

	public final static byte EDC_CRC = 0x00;
	public final static byte EDC_LRC = 0x01;

	public final static byte CONVENTION_DIRECT = 0x3B; /* Direct convention */
	public final static byte CONVENTION_INVERSE = 0x3F; /* Inverse convention */

	public ATR(byte atr[]) {
		mATR = atr;
		parseATR(mATR);

	}

	/*****************************************************************************
	 * 
	 * parsing ATR functions
	 * 
	 ****************************************************************************/

	public void parseATR(byte atr[]) {

		byte TDi;
		boolean hasTCK = false;

		// store TS and T0
		TS = new TByte(atr[0]);
		T0 = new TByte(atr[1]);
		TDi = atr[1];

		// protocol number
		// int protocol = 1;

		int p = 1; // pointer of atr array
		int i = 1; // interface number
		while (p < atr.length) {
			byte Yi = (byte) ((TDi >> 4) & 0x0F);

			// Check TAi is present
			if ((Yi & 0x01) > 0) {
				p++;
				TA[i] = new TByte(atr[p]);
				parseTA(i);
			}

			// Check TBi is present
			if ((Yi & 0x02) > 0) {
				p++;
				TB[i] = new TByte(atr[p]);
				parseTB(i);
			}

			// Check TCi is present
			if ((Yi & 0x04) > 0) {
				p++;
				TC[i] = new TByte(atr[p]);
				parseTC(i);
			}

			// Check TDi is present
			if ((Yi & 0x08) > 0) {
				p++;
				TDi = atr[p];
				TD[i] = new TByte(TDi);
				parseTD(i);

				mProtocol = (byte) (TD[i].value & 0x0F);
				if (mProtocol != T_0)
					hasTCK = true;
			} else {
				break;
			}

			i++;
		}

		// If TDx is not set then the default protocol must be T0
		if (DefaultProtocol == 0x00) {
			DefaultProtocol = PROTOCOL_T0;
			AvailableProtocols |= PROTOCOL_T0;
		}

		// Store historical bytes
		int nh = T0.value & 0x0F;
		HistoricalBytes = new TByte[nh];
		for (int j = 0; j < nh; j++) {
			HistoricalBytes[j] = new TByte(atr[p + 1 + j]);
		}

		// Store TCK
		if (hasTCK)
			TCK = new TByte(atr[atr.length - 1]);

		// Get Fi Di
		// parseTA();
		// parseTB();
		// parseTC();
	}

	public final void setIFSD(byte ifsd) {
		IFSD = ifsd;
	}

	private final static int RFU = 0;

	private void parseTA(int i) {
		int FiDef[] = { 372, 372, 558, 744, 1116, 1488, 1860, RFU, RFU, 512,
				768, 1024, 1536, 2048, RFU, RFU };
		int DiDef[] = { RFU, 1, 2, 4, 8, 16, 32, 64, 12, 20, RFU, RFU, RFU,
				RFU, RFU, RFU };

		if (i == 1) {
			int f = (TA[1].value >> 4) & 0x0F;
			int d = TA[1].value & 0x0F;

			Fi = FiDef[f];
			Di = DiDef[d];
		} else if (i == 2) {
			// int T = TA[2].value & 0x0F;
			// "Specific mode: T=%d", T

			// if (T == T_0)
			// DefaultProtocol = AvailableProtocols = PROTOCOL_T0;
			// else if (T == T_1)
			// DefaultProtocol = AvailableProtocols = PROTOCOL_T1;

		} else if (i > 2) {
			if (mProtocol == T_1 && TAiT1found == false) {
				IFSC = TA[i].value;
				TAiT1found = true;
			} else {
				// TODO check class
				// byte f = (byte) (TA[i].value >> 6); //Clock stop:
				// byte d = (byte) (TA[i].value & 0x3F); //Class accepted by the
				// card:

			}
		}
	}

	private void parseTB(int i) {
		if (i > 2) {
			if (mProtocol == T_1 && TBiT1found == false) {
				BWI = (byte) ((TB[i].value >> 4) & 0x0F);
				CWI = (byte) (TB[i].value & 0x0F);
				TBiT1found = true;
			}
		}
	}

	private void parseTC(int i) {
		if (i == 1) {
			// Extra guard time
			byte EGT = TC[1].value;

		} else if (i > 2) {
			if (mProtocol == T_1 && TCiT1found == false) {
				if (TC[i].value == 0x01)
					EDC = EDC_CRC;
				else if (TC[i].value == 0x00)
					EDC = EDC_LRC;

				TCiT1found = true;
			}
		}
	}

	private void parseTD(int i) {
		byte T = (byte) (TD[i].value & 0x0F);

		if (i == 1) {
			// Set the default protocol TD1 (first TD only)
			if (T == T_0)
				DefaultProtocol = PROTOCOL_T0;
			else if (T == T_1)
				DefaultProtocol = PROTOCOL_T1;
		}

		if (T == T_0)
			AvailableProtocols |= PROTOCOL_T0;
		else if (T == T_1)
			AvailableProtocols |= PROTOCOL_T1;
		else if (T == T_15)
			AvailableProtocols |= PROTOCOL_T15;
	}

	/*****************************************************************************
	 * 
	 * print ATR functions
	 * 
	 ****************************************************************************/

	static String BINQ[] = { "0000", "0001", "0010", "0011", "0100", "0101",
			"0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101",
			"1110", "1111" };

	public void printATR() {

		// print ATR
		System.out.print("ATR =" + ToHexString(mATR));
		System.out.print("\n");

		// print TS
		System.out.print("TS = " + TS.toString());
		if (TS.value == 0x3B)
			System.out.print(" Direct Convention");
		else if (TS.value == 0x3F)
			System.out.print(" Inverse Convention");
		else
			System.out.print(" Invalid");

		// print T0
		int Yi = (TS.value >> 4) & 0x0F;
		System.out.print("\n");
		System.out.print("T0 = " + T0.toString());
		System.out.print(String.format(" Y(1): b%s, K: %d (historical bytes)",
				BINQ[Yi], HistoricalBytes.length));

		// print Interface bytes
		byte TDi = T0.value;
		int p = 1; // pointer of atr array
		int i = 1; // interface number
		while (p < mATR.length) {
			Yi = (TDi >> 4) & 0x0F;

			// Check TAi is present
			if ((Yi & 0x01) > 0) {
				p++;
				printTA(i);
			}

			// Check TBi is present
			if ((Yi & 0x02) > 0) {
				p++;
				printTB(i);
			}

			// Check TCi is present
			if ((Yi & 0x04) > 0) {
				p++;
				printTC(i);
			}

			// Check TDi is present
			if ((Yi & 0x08) > 0) {
				TDi = TD[i].value;
				printTD(i);
				mProtocol = (byte) (TD[i].value & 0x0F);
			} else {
				break;
			}
			i++;
		}

		// print Historical bytes
		System.out.print("\n");
		System.out.print("Historical bytes = ");
		System.out.print(ToHexString(HistoricalBytes));

		// TODO: analyse_histrorical_bytes

		// print TCK
		if (TCK != null) {
			System.out.print("\n");
			System.out.print("TCK = " + TCK.toString());

			byte LRC = compute_tck();
			if (TCK.value == LRC)
				System.out.print(" (correct checksum)");
			else
				System.out.print(String.format(
						" WRONG CHECKSUM, expected 0x%02X", LRC));
		}
	}

	private void printTA(int i) {
		System.out.print("\n");
		System.out.print(String.format("TA(%d) = %s", i, TA[i].toString()));

		if (i == 1) {
			System.out.print(String.format(" Fi=%d, Di=%d", Fi, Di));
			// TODO:
			// System.out.print(String.format(", %g cycles/ETU (%d bits/s at 4.00 MHz, %d bits/s for fMax=%d MHz)",
			// Fi, Di));

		}
	}

	private void printTB(int i) {
		System.out.print("\n");
		System.out.print(String.format("TB(%d) = %s", i, TB[i].toString()));

		if (i == 1) {

		}

		if (i > 2) {
			if (mProtocol == T_1) {
				BWI = (byte) ((TB[i].value >> 4) & 0x0F);
				CWI = (byte) (TB[i].value & 0x0F);
			}
		}
	}

	private void printTC(int i) {
		System.out.print("\n");
		System.out.print(String.format("TC(%d) = %s", i, TC[i].toString()));

		if (i == 1) {
			System.out.print(String
					.format(" Extra guard time: %d", TC[1].value));
			if (TC[1].value == 0xFF)
				System.out.print(" (special value)");
			// Extra guard time
			byte EGT = TC[1].value;

		} else if (i > 2) {
			if (TC[i].value == 0x01)
				EDC = EDC_CRC;
			else if (TC[i].value == 0x00)
				EDC = EDC_LRC;
		}
	}

	private void printTD(int i) {
		System.out.print("\n");
		System.out.print(String.format("TD(%d) = %s", i, TD[i].toString()));
		return;
	}

	private byte compute_tck() {
		byte rc = 0x00;
		for (int i = 1; i < (mATR.length - 1); i++)
			rc ^= mATR[i];
		return rc;
	}

	private String ToHexString(TByte[] data) {
		String HexString = "";

		for (int i = 0; i < data.length; i++) {
			HexString += " " + data[i].toString();
		}
		return HexString;
	}

	private String ToHexString(byte[] data) {
		String HexString = "";

		for (int i = 0; i < data.length; i++) {
			HexString += " " + String.format("%02X", data[i]);
		}
		return HexString;
	}

	public void setTA1(byte value) {
		TA[1] = new TByte(value);
	}
}
