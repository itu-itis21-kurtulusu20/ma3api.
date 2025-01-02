/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
/*
 * Copyright 2013 FOXPLUS International Co., Ltd.
 *
 */
package com.scdroid.ccid;

import com.scdroid.smartcard.ATR;
import com.scdroid.smartcard.Card;
import com.scdroid.smartcard.SCException;
import com.scdroid.smartcard.protocol.T1Block;
import com.scdroid.smartcard.protocol.T1BlockEDCErrorException;
import com.scdroid.smartcard.protocol.T1IOException;
import com.scdroid.smartcard.protocol.T1Protocol;

import java.util.Arrays;

/**
 * <tt>CCIDReader</tt>
 * 
 * implementation of CCID protocol (see CCID Rev 1.1)
 * 
 * @author Roman (roman@scdroid.com)
 * @version 1.26
 */
public abstract class CCIDReader extends T1Protocol implements Cloneable,
		IReader {

	private boolean initT1ProtocolDone = false;

	private byte mSlotIndex = 0x00;
	private byte mSeq = 1;

	/* Protocols */
	private byte mProtocol = 0;

	protected static final String VERSION = "1.2.6";

	protected double mCCIDVersion = (float) 1.10; // Integrated Circuit(s) Cards
													// Interface Devices (CCID)
													// Specification Release
													// Number.
	protected int mMaxSlotIndex = 0; // The index of the highest available slot
										// on this device.
	protected int mVoltageSupport = 1; // This value indicates what voltages the
										// CCID can supply to its slots.
	protected int mProtocols = 3; // indicates support for T = 0 and T = 1.
	protected int mDefaultClock = 3850; // 3.58MHz, Default ICC clock frequency
										// in KHz.
	protected int mMaxClock = 14320; // 14.32 MHz Maximum supported ICC clock
										// frequency in KHz. This is an integer
										// value
	protected byte mNumClockSupported = 0; // The number of clock frequencies
											// that are supported by the CCID.
	protected int mDataRate = 9600;// 9600bps, Default ICC I/O data rate in bps.
									// This
	protected int mMaxDataRate = 115200;// 115200bps, Maximum supported ICC I/O
										// data rate in bps
	protected byte mNumDataRatesSupported = 0; // The number of data rates that
												// are supported by the CCID.
	protected int mMaxIFSD = 0; // Indicates the maximum IFSD supported by CCID
								// for protocol T=1
	protected int mSynchProtocols = 0; // indicates support for the associated
										// protocol.
	protected int mMechanical = 0; // 00000000h No special characteristics
	protected int mFeatures = 0; // indicates what intelligent features the CCID
									// has.
	protected int mMaxCCIDMessageLength = 0; // For extended APDU level the
												// value shall be between 261 +
												// 10 (header) and 65544 +10,
	protected byte mClassGetResponse; // Significant only for CCID that offers
										// an APDU level for exchanges.
	protected byte mClassEnvelope;// Significant only for CCID that offers an
									// extended APDU level for exchanges.
	protected int mLcdLayout; // Number of lines and characters for the LCD
								// display used to send messages for PIN entry.
	protected byte mPINSupport; // indicates what PIN support features the CCID
								// has.
	protected byte mMaxCCIDBusySlots; // Maximum number5 of slots which can be
										// simultaneously busy.

	protected int[] arrayOfSupportedDataRates; // array of data rates supported
												// by the reader

	/* Features from dwFeatures */
	// private final int CCID_CLASS_AUTO_CONF_ATR = 0x00000002;
	private final int CCID_CLASS_AUTO_VOLTAGE = 0x00000008;
	// private final int CCID_CLASS_AUTO_BAUD = 0x00000020;
	private final int CCID_CLASS_AUTO_PPS_PROP = 0x00000040;
	private final int CCID_CLASS_AUTO_PPS_CUR = 0x00000080;
	// private final int CCID_CLASS_AUTO_IFSD = 0x00000400;
	private final int CCID_CLASS_CHARACTER = 0x00000000;
	private final int CCID_CLASS_TPDU = 0x00010000;
	private final int CCID_CLASS_SHORT_APDU = 0x00020000;
	private final int CCID_CLASS_EXTENDED_APDU = 0x00040000;
	private final int CCID_CLASS_EXCHANGE_MASK = 0x00070000;

	private boolean mPowerOn = false;

	protected int mLogLevel = 0;
	public static int LOG_INFO = 0x01;
	public static int LOG_APDU = 0x02;
	public static int LOG_CCID = 0x04;

	/** Smart card reader event message. */
	public static int CARD_EVENT_DETECED = 0x01;
	/** Waiting card detected */
	public static int CARD_EVENT_REMOVED = 0x02;

	/** Waiting card removed */

	public CCIDReader() {
		super(0, 0, 0);
	}

	protected final void finalize() {

	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getSlotCount() {
		return mMaxSlotIndex + 1;
	}

	@Override
	public void logLevel(int level) {
		mLogLevel = level;
	}

	protected final void logCCID(String message) {
		if ((mLogLevel & LOG_CCID) == LOG_CCID)
			logData("CCID:" + message);
	}

	protected final void logData(String module, String message) {
		if (mLogLevel > 0)
			logData(module + ":" + message);
	}

	protected final void logData(String title, byte[] data, int length) {
		String msg = "";

		for (int i = 0; i < length; i++)
			msg += String.format("%1$02X", data[i]) + " ";
		// msg += "0x" + String.format("%1$02X", data[i]) + " ";

		logData(title + msg);
	}

	protected final void logData(String title, byte[] data) {
		logData(title, data, data.length);
	}

	protected final int bMessageType_OFFSET = 0;
	protected final int STATUS_OFFSET = 7;
	protected final int ERROR_OFFSET = 8;
	private final int CHAIN_PARAMETER_OFFSET = 9;

	/* See CCID specs ch. 4.2.1 */
	protected final int CCID_ICC_PRESENT_ACTIVE = 0x00; /* 00 0000 00 */
	protected final int CCID_ICC_PRESENT_INACTIVE = 0x01; /* 00 0000 01 */
	protected final int CCID_ICC_ABSENT = 0x02; /* 00 0000 10 */
	private final int CCID_ICC_STATUS_MASK = 0x03; /* 00 0000 11 */

	protected final int CCID_COMMAND_FAILED = 0x40; /* 01 0000 00 */
	protected final int CCID_TIME_EXTENSION = 0x80; /* 10 0000 00 */

	protected final int RDR_to_PC_SlotStatus = 0x81;

	/*****************************************************************************
	 * 
	 * CCID_Transmit: implementation of PC_to_RDR_XfrBlock
	 * 
	 * @param apdu
	 *            : APDU command
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private void CCID_Transmit(byte[] apdu) throws SCException {
		byte[] cmd = new byte[10 + apdu.length];

		cmd[0] = 0x6F; /* PC_to_RDR_XfrBlock */
		SetLength(cmd, apdu.length); /* APDU length */
		cmd[5] = mSlotIndex; /* slot number */
		cmd[6] = mSeq++;
		cmd[7] = (byte) getBlockWaitingTime(); /* extend block waiting timeout */
		cmd[8] = 0; /* Expected length, in character mode only */
		cmd[9] = 0; /* RFU */

		// copy APDU to data block
		System.arraycopy(apdu, 0, cmd, 10, apdu.length);

		Write(cmd);
	}

	/*****************************************************************************
	 * 
	 * CCID_Transmit: implementation of PC_to_RDR_XfrBlock
	 * 
	 * @param chain
	 *            : chained data?
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private byte[] CCID_Receive(boolean chain) throws SCException {
		byte[] buff = Read();

		if (buff == null)
			throw new SCException("command timeout");

		if (buff.length < STATUS_OFFSET + 1) {
			throw new SCException("Not enough data received:" + buff.length
					+ " bytes");
		}

		// if (((int) buff[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0) {
		//
		// throw new SCException( new byte[] { buff[STATUS_OFFSET],
		// buff[ERROR_OFFSET]});
		//
		// switch (buff[STATUS_OFFSET]) {
		// case (byte) 0xEF: /* cancel */
		// return new byte[] { 0x64, 0x01 };
		// case (byte) 0xF0: /* timeout */
		// return new byte[] { 0x64, 0x00 };
		// case (byte) 0xFD: /* Parity error during exchange */
		// throw new SCException("Parity error");
		// default:
		// throw new SCException("CCID communication error: 0x"
		// + String.format("%1$02X", buff[ERROR_OFFSET]));
		// }
		// }

		if (((int) buff[STATUS_OFFSET] & CCID_TIME_EXTENSION) != 0) {
			// logCCID("Time extension requested: 0x"
			// + String.format("%1$02X", buff[ERROR_OFFSET]));
			return CCID_Receive();
		}

		int len = GetLength(buff);

		/* we have read less (or more) data than the CCID frame says to contain */
		// for AU9520 UART we'll get one more byte data
		// if (buff.length - 10 != len)
		if (buff.length - 10 < len)
			throw new SCException("Can't read all data (" + (buff.length - 10)
					+ " out of " + len + " expected)");

		byte[] data = new byte[len];
		System.arraycopy(buff, 10, data, 0, len);

		/*
		 * TODO: Extended APDU case? Only valid for RDR_to_PC_DataBlock frames
		 */
		// check cmd[CHAIN_PARAMETER_OFFSET];

		return data;
	}

	private byte[] CCID_Receive() throws SCException {
		return CCID_Receive(false);
	}

	/*****************************************************************************
	 * 
	 * T1SetParameters: setting the parameters for T1 card
	 * 
	 * @param _ATR
	 *            : ATR of the card.
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private void T1SetParameters(ATR _ATR, boolean isPPS) throws SCException {

		byte[] param = new byte[7];
		param[0] = 0x11; /* Fi/Di */
		param[1] = 0x10; /* TCCKS */
		param[2] = 0x00; /* GuardTime */
		param[3] = 0x4D; /* BWI/CWI */
		param[4] = 0x00; /* ClockStop */
		param[5] = 0x20; /* IFSC */
		param[6] = 0x00; /* NADValue */

		// add by roman 2013/03/27
		/* TA1 is not default */
		if (_ATR.hasPPS1 || (_ATR.TA[2] != null && _ATR.TA[1] != null))
			param[0] = _ATR.TA[1].value;

		/* CRC checksum? */
		if (_ATR.EDC == ATR.EDC_CRC)
			param[1] |= 0x01;
		// add by roman 2013/03/27

		/* the CCID should ignore this bit */
		if (_ATR.TS.value == ATR.CONVENTION_INVERSE)
			param[1] |= 0x02;

		/* get TC1 Extra guard time */
		if (_ATR.TC[1] != null) {
			if (!(isPPS == true && _ATR.TC[1].value == (byte) 0xFF)) {
				param[2] = _ATR.TC[1].value;
			}
		}

		/* TBi (i>2) present? BWI/CWI */
		param[3] = (byte) ((_ATR.BWI << 4) | (_ATR.CWI & 0x0F));

		if (_ATR.IFSC != 0x20)
			param[5] = _ATR.IFSC;

		SetParameters((byte) 0x01, param);

		if (isPPS == false && initT1ProtocolDone == false
				&& (mFeatures & CCID_CLASS_EXCHANGE_MASK) == CCID_CLASS_TPDU) {

			initT1Protocol(_ATR.IFSD, _ATR.IFSC);
			initT1ProtocolDone = true;
		}

		setBlockWaitingTime(_ATR.BWI);
	}

	/*****************************************************************************
	 * 
	 * T0SetParameters: setting the parameters for T0 card
	 * 
	 * @param _ATR
	 *            : ATR of the card.
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private void T0SetParameters(ATR _ATR, boolean beforePPS)
			throws SCException {

		byte[] param = new byte[5];
		param[0] = 0x11; /* Fi/Di */
		param[1] = 0x00; /* TCCKS */
		param[2] = 0x00; /* GuardTime */
		param[3] = 0x0A; /* WaitingInteger */
		param[4] = 0x00; /* ClockStop */

		if (beforePPS == false) {
			// add by roman 2013/03/27
			/* TA1 is not default */
			if (_ATR.hasPPS1 || (_ATR.TA[2] != null && _ATR.TA[1] != null))
				param[0] = _ATR.TA[1].value;

			if (_ATR.TS.value == ATR.CONVENTION_INVERSE)
				param[1] |= 0x02;

			/* TC2 WWT */
			if (_ATR.TC[2] != null)
				param[3] = _ATR.TC[2].value;
		}

		/* get TC1 Extra guard time */
		if (_ATR.TC[1] != null)
			param[2] = _ATR.TC[1].value;

		SetParameters((byte) 0x00, param);
	}

	// Protocol Parameter Selection
	private final int ATR_PROTOCOL_TYPE_T0 = 0;
	private final int ATR_PROTOCOL_TYPE_T1 = 1;

	// private final int IFD_NEGOTIATE_PTS1 = 1;
	// /** < negotiate PTS1 */
	// private final int IFD_NEGOTIATE_PTS2 = 2;
	// /** < negotiate PTS2 */
	// private final int IFD_NEGOTIATE_PTS3 = 4;
	// /** < negotiate PTS3 */

	/* Default values for paramenters */
	private final int ATR_DEFAULT_F = 372;
	private final int ATR_DEFAULT_D = 1;

	/*****************************************************************************
	 * 
	 * PPS_Exchange: setting the parameters for T0 card
	 * 
	 * @throws SCException
	 * 
	 *             When a CCID using TPDU exchange level declares neither of the
	 *             values 00000040h Automatic parameters negotiation or
	 *             00000080h Automatic PPS made by the CCID the PPS exchange
	 *             must be made using a TPDU for PPS exchange
	 * 
	 *             TPDU for PPS exchange has the following format: FF PPS0 PPS1
	 *             PPS2 PPS3 PCK, with PPS1, PPS2, PPS3 optional [ISO/IEC7816-3
	 *             §7]. FF PPS0_R PPS1_R PPS2_R PPS3_R PCK_R, with PPS1_R,
	 *             PPS2_R, PPS3_R optional [ISO/IEC7816-3 §7.4).
	 * 
	 ****************************************************************************/
	private boolean PPS_Exchange(ATR oATR, byte SelectedProtocol)
			throws SCException {

		if (mNumDataRatesSupported != 0) {
			getReaderDataRates(mNumDataRatesSupported);
		}

		/*
		 * Do not send CCID command SetParameters or PPS to the CCID The CCID
		 * will do this himself
		 */
		if ((mFeatures & CCID_CLASS_AUTO_PPS_PROP) > 0)
			return true;

		byte[] pps = new byte[6];

		extraEGT(oATR, SelectedProtocol);

		// if (ATR.PROTOCOL_T0 == SelectedProtocol) {
		if (oATR.TC[1] != null && oATR.TC[1].value != (byte) 0x00)
			T0SetParameters(oATR, true);
		// } else if (ATR.PROTOCOL_T1 == SelectedProtocol) {
		// T1SetParameters(oATR, true);
		// }

		if (SelectedProtocol == ATR.PROTOCOL_T0)
			pps[1] |= ATR_PROTOCOL_TYPE_T0;
		else if (SelectedProtocol == ATR.PROTOCOL_T1)
			pps[1] |= ATR_PROTOCOL_TYPE_T1;
		else
			throw new SCException("protocol not supported");

		// /* TA2 present -> specific mode */
		// if (oATR.TA[2] != null) {
		// if (pps[1] != (oATR.TA[2].value & 0x0F))
		// // return true;
		// throw new SCException(new byte[] { (byte) 0x41, (byte) 0xF6 });
		// }

		/* TCi (i>2) indicates CRC instead of LRC */
		// if (SelectedProtocol == ATR.PROTOCOL_T1) {
		// // from oATR.EDC we can know use CRC or LRC
		// // Done into ParseTC()
		// }

		// if ((Flags & IFD_NEGOTIATE_PTS1) > 0) {
		// /* just use the value passed in argument */
		// pps[1] |= 0x10; /* PTS1 presence */
		// pps[2] = PTS1;
		// } else
		{
			/* TA1 present */
			if (oATR.TA[1] != null) {

				int f = oATR.Fi;
				int d = oATR.Di;

				/* may happen with non ISO cards */
				if ((0 == f) || (0 == d)) {
					/* values for TA1=11 */
					f = 372;
					d = 1;
				}

				// TODO: check reader support card baudrate
				/* Baudrate = f x D/F */
				int card_baudrate = (1000 * mDefaultClock * d / f);
				int default_baudrate = (1000 * mDefaultClock * ATR_DEFAULT_D / ATR_DEFAULT_F);

				/* if the card does not try to lower the default speed */
				if ((card_baudrate > default_baudrate)
				/* and the reader is fast enough */
				&& (card_baudrate <= mMaxDataRate)) {

					/* the reader has no baud rates table */
					if (arrayOfSupportedDataRates == null
					/* or explicitely support it */
					|| findBaudRate(card_baudrate)) {
						pps[1] |= 0x10; /* PTS1 presence */
						pps[2] = oATR.TA[1].value;
						oATR.hasPPS1 = true;
					} else {
						/*
						 * TA2 present -> specific mode: the card is supporting
						 * only the baud rate specified in TA1 but reader does
						 * not support this value. Reject the card.
						 */
						if (oATR.TA[2] != null) {
							throw new SCException(
									"TA2 present -> specific mode: the card is supporting only the baud rate specified in TA1 but reader does not support this value. Reject the card.");
						}
					}
				} else {
					/* the card is too fast for the reader */
					if ((card_baudrate > (mMaxDataRate + 2))
					/* but TA1 <= 97 */
					&& (oATR.TA[1].value <= 0x97)
					/* and the reader has a baud rate table */
					&& arrayOfSupportedDataRates != null) {

						byte old_TA1 = oATR.TA[1].value;
						while (oATR.TA[1].value > 0x94) {
							/* use a lower TA1 */
							oATR.TA[1].value--;

							f = (oATR.TA[1].value >> 4) & 0x0F;
							d = oATR.TA[1].value & 0x0F;

							/* Baudrate = f x D/F */
							card_baudrate = (1000 * mDefaultClock * d / f);

							if (findBaudRate(card_baudrate)) {
								pps[1] |= 0x10; /* PTS1 presence */
								pps[2] = oATR.TA[1].value;

								break;
							}
						}

						/* restore original TA1 value */
						oATR.TA[1].value = old_TA1;
					}

				}
			}
		}

		// /* PTS2? */
		// if ((Flags & IFD_NEGOTIATE_PTS2) > 0) {
		// pps[1] |= 0x20; /* PTS2 presence */
		// pps[3] = PTS2;
		// }
		//
		// /* PTS3? */
		// if ((Flags & IFD_NEGOTIATE_PTS3) > 0) {
		// pps[1] |= 0x40; /* PTS3 presence */
		// pps[4] = PTS3;
		// }

		/* Generate PPS */
		pps[0] = (byte) 0xFF;

		/* Automatic PPS made by the ICC? */
		if ((mFeatures & CCID_CLASS_AUTO_PPS_CUR) == 0 && (oATR.TA[2] == null) /*
																				 * TA2
																				 * absent
																				 * :
																				 * negociable
																				 * mode
																				 */) {
			/*
			 * if the requested protocol is not the default one or a TA1/PPS1 is
			 * present
			 */
			byte RequestProtocol = ATR.PROTOCOL_T0;

			if ((pps[1] & 0x0F) == ATR_PROTOCOL_TYPE_T1)
				RequestProtocol = ATR.PROTOCOL_T1;

			if ((RequestProtocol != oATR.DefaultProtocol)
					|| (pps[1] & 0x10) > 0) {

				int len_request = PPS_GetLength(pps);
				byte[] pps_request = new byte[len_request];
				System.arraycopy(pps, 0, pps_request, 0, len_request);

				pps_request[len_request - 1] = PPS_GetPCK(pps, len_request - 1);

				byte[] confirm = XfrBlockTPDU_T0(pps_request);

				boolean ppsMatch = PPS_Match(pps_request, confirm);

				if (oATR.TA[1] == null) {
					oATR.setTA1((byte) 0x11);
				} else {
					oATR.TA[1].value = 0x11;
				}
				// pps[2] = 0x11; /* default TA1 */
				/* if PPS1 is echoed */
				if ((pps[1] & 0x10) > 0 && (confirm[1] & 0x10) > 0) {
					oATR.TA[1].value = confirm[2];
					// pps[2] = confirm[2];
				}

				return ppsMatch;
			}

		}

		return true;
	}

	private final boolean PPS_Match(byte[] ppsRequest, byte[] ppsConfirm) {

		if (ppsRequest[0] != (byte) 0xFF || ppsConfirm[0] != (byte) 0xFF) {
			return false;
		}

		// global test
		if (Arrays.equals(ppsRequest, ppsConfirm) == true)
			return true;

		// check protocol
		if ((ppsConfirm[1] & 0xF) != (ppsRequest[1] & 0xF))
			return false;

		// check pps1
		if ((ppsConfirm[1] & 0x10) == 0x10) {

			if ((ppsRequest[1] & 0x10) != 0x10) {
				return false;
			}

			if (ppsRequest[2] != ppsConfirm[2]) {
				return false;
			}
		}

		// check pps2
		if ((ppsConfirm[1] & 0x20) == 0x20) {
			if ((ppsRequest[1] & 0x20) != 0x20) {
				return false;
			}

			if (ppsRequest[3] != ppsConfirm[3]) {
				return false;
			}
		}

		// check pps3
		if ((ppsConfirm[1] & 0x40) == 0x40) {
			if ((ppsRequest[1] & 0x40) != 0x40) {
				return false;
			}

			if (ppsRequest[4] != ppsConfirm[4]) {
				return false;
			}
		}

		if (PPS_GetPCK(ppsRequest, ppsRequest.length) != 0
				|| PPS_GetPCK(ppsConfirm, ppsConfirm.length) != 0) {
			return false;
		}

		return true;
	}

	/*
	 * This function use an EGT value for cards who comply with followings
	 * criterias: - TA1 > 11 - current EGT = 0x00 or 0xFF - T=0 or (T=1 and CWI
	 * >= 2)
	 * 
	 * Without this larger EGT some non ISO 7816-3 smart cards may not
	 * communicate with the reader.
	 * 
	 * This modification is harmless, the reader will just be less restrictive
	 */
	private final void extraEGT(ATR oATR, byte SelectedProtocol)
			throws SCException {

		/* if TA1 not present */
		if (oATR.TA[1] == null) {
			return;
		}

		int f = oATR.Fi;
		int d = oATR.Di;

		/* may happen with non ISO cards */
		if ((0 == f) || (0 == d)) {
			return;
		}

		int card_baudrate = (1000 * mDefaultClock * d / f);
		int default_baudrate = (1000 * mDefaultClock * ATR_DEFAULT_D / ATR_DEFAULT_F);

		/* TA1 > 11? */
		if (card_baudrate <= default_baudrate)
			return;

		/* Current EGT = 0 or FF? */
		if (oATR.TC[1] != null
				&& (oATR.TC[1].value == (byte) 0x00 || oATR.TC[1].value == (byte) 0xFF)) {
			if (ATR.PROTOCOL_T0 == SelectedProtocol) {
				/* Init TC1 */
				oATR.TC[1].value = 2;
			}

			if (ATR.PROTOCOL_T1 == SelectedProtocol) {
				int i;

				/* TBi (i>2) present? BWI/CWI */
				for (i = 3; i < ATR.ATR_MAX_PROTOCOLS; i++) {
					/* CWI >= 2 ? */

					if (i < oATR.TB.length && oATR.TB[i] != null
							&& (oATR.TB[i].value & 0x0F) >= 2) {
						/* Init TC1 */
						oATR.TC[1].value = 2;
						/* only the first TBi (i>2) must be used */
						break;
					}
				}
			}
		}
	}

	private boolean findBaudRate(int baudrate) {

		if (arrayOfSupportedDataRates == null) {
			return false;

		}

		final int limit = arrayOfSupportedDataRates.length;
		final int ecart = 2;
		for (int i = 0; i < limit; ++i) {
			/*
			 * We must take into account that the card_baudrate integral value
			 * is an approximative result, computed from the d/f float result.
			 */
			if (baudrate < (arrayOfSupportedDataRates[i] + ecart)
					&& baudrate > (arrayOfSupportedDataRates[i] - ecart)) {

				return true;
			}
		}

		return false;
	}

	private int PPS_GetLength(byte[] block) {
		int len = 3;

		if ((block[1] & 0x10) > 0)
			len++;

		if ((block[1] & 0x20) > 0)
			len++;

		if ((block[1] & 0x40) > 0)
			len++;

		return len;
	}

	private byte PPS_GetPCK(byte[] block, int len) {
		byte pck;

		pck = block[0];
		for (int i = 1; i < len; i++)
			pck ^= block[i];

		return pck;
	}

	/*****************************************************************************
	 * 
	 * SetProtocolParameters: Set Protocol Parameters for T0 or T1 card
	 * 
	 * @param AnswerToReset
	 *            : ATR of the card.
	 * @param PreferredProtocols
	 *            : Preferred Protocols.
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private void SetProtocolParameters(byte[] AnswerToReset,
			byte PreferredProtocols) throws SCException {

		/* get card protocol T1 or T0 */
		ATR oATR = new ATR(AnswerToReset);

		oATR.printATR();

		// debug: oATR.printATR();

		// TODO: support T1 CRC
		// TCi (i>2) indicates CRC instead of LRC
		// oATR.EDC;

		/* set default protocol */
		mProtocol = oATR.DefaultProtocol;

		/* keep only the available protocols */
		PreferredProtocols &= oATR.AvailableProtocols;
		byte chosen = 0x00;

		/* we try to use T=1 first */
		if ((PreferredProtocols & ATR.PROTOCOL_T1) > 0) {
			chosen = ATR.PROTOCOL_T1;
		} else {
			if ((PreferredProtocols & ATR.PROTOCOL_T0) > 0)
				chosen = ATR.PROTOCOL_T0;
			else
				throw new SCException("app wants unsupported protocol");
		}

		if (chosen == ATR.PROTOCOL_T1) {
			oATR.setIFSD((byte) mMaxIFSD);
		}

		/*
		 * Do not send CCID command SetParameters or PPS to the CCID The CCID
		 * will do this himself
		 */
		if ((mFeatures & CCID_CLASS_AUTO_PPS_PROP) > 0) {
			mProtocol = chosen;

			return;
		}

		/* TA2 present -> specific mode */
		if (oATR.TA[2] != null) {
			byte T_TA2 = (byte) (oATR.TA[2].value & 0x0F);

			byte chosenTA2;
			switch (T_TA2) {
			case ATR.T_0:
				chosenTA2 = ATR.PROTOCOL_T0;
				break;
			case ATR.T_1:
				chosenTA2 = ATR.PROTOCOL_T1;
				break;
			default:
				throw new SCException((byte) 0xF6);// new byte[] { (byte) 0x41,
													// (byte) 0xF6 });
			}

			if ((chosenTA2 & oATR.AvailableProtocols) != chosenTA2)
				throw new SCException((byte) 0xF6);// new byte[] { (byte) 0x41,
													// (byte) 0xF6 });

			mProtocol = chosenTA2;
		} else {
			if (PPS_Exchange(oATR, chosen))
				mProtocol = chosen;
			else {
				throw new SCException((byte) 0xFE);// new byte[] { (byte) 0x41,
													// (byte) 0xFE });
			}
		}

		if (mProtocol == ATR.PROTOCOL_T1) {
			// oATR.setIFSD((byte) mMaxIFSD);

			// T=1 SetParameter
			T1SetParameters(oATR, false);

		} else {
			/* T=0 */
			T0SetParameters(oATR, false);
		}
	}

	/*****************************************************************************
	 * 
	 * PowerOn: implementation of PC_to_RDR_IccPowerOn
	 * 
	 * @param voltage
	 *            : Voltage that is applied to the ICC. 0:auto, 1:5V, 2:3V,
	 *            3:1.8V
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private final byte[] PowerOn(byte voltage) throws SCException {
		byte[] cmd = new byte[10];

		cmd[0] = 0x62; /* PC_to_RDR_IccPowerOn */
		cmd[1] = cmd[2] = cmd[3] = cmd[4] = 0; /* dwLength */
		cmd[5] = mSlotIndex; /* slot number */
		cmd[6] = mSeq++;
		cmd[7] = 0; /* automatic voltage selection */
		cmd[8] = cmd[9] = 0; /* RFU */

		if ((mFeatures & CCID_CLASS_AUTO_VOLTAGE) > 0)
			voltage = 0;

		cmd[7] = voltage;

		Write(cmd);
		byte[] rsp = Read();

		if (rsp.length < STATUS_OFFSET + 1)
			throw new SCException("Not enough data received:" + rsp.length
					+ " bytes");

		if (((int) rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0) {
			// throw new SCException("CCID communication error: 0x"
			// + String.format("%1$02X", rsp[ERROR_OFFSET]));
			throw new SCException((byte) rsp[ERROR_OFFSET]);// new byte[] {
															// rsp[ERROR_OFFSET]
															// });
		}

		/* extract the ATR */
		int len = GetLength(rsp); /* ATR length */
		if (len > rsp.length)
			len = rsp.length;

		byte[] data = new byte[len];
		System.arraycopy(rsp, 10, data, 0, len);

		return data;
	}

	/*****************************************************************************
	 * 
	 * PowerOn: Main PowerOn function
	 * 
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final byte[] PowerOn() throws SCException {

		// TODO: get voltage parameter from reader's database
		byte voltage = 3; /* start from 1.8V */

		Byte error = null;
		while (voltage > 0) {
			try {

				byte ATR[] = PowerOn(voltage);
				return ATR;
			} catch (SCException e) {
				e.printStackTrace();
				error = e.getErrorCode();
				/* continue with 3 volts and 5 volts */
				voltage--;
			}
		}

		if (error != null) {
			throw new SCException(error.byteValue());
		}

		throw new SCException("PowerOn fail");
	}

	/*****************************************************************************
	 * 
	 * PowerOff: implementation of PC_to_RDR_IccPowerOff
	 * 
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final void PowerOff() throws SCException {

		if (!mPowerOn)
			return;

		byte[] cmd = new byte[10];

		cmd[0] = 0x63; /* PC_to_RDR_IccPowerOff */
		cmd[1] = cmd[2] = cmd[3] = cmd[4] = 0; /* dwLength */
		cmd[5] = mSlotIndex; /* slot number */
		cmd[6] = mSeq++;
		cmd[7] = cmd[8] = cmd[9] = 0; /* RFU */

		// modify by roman 2013/04/19
		// byte[] rsp = exchangeData(cmd);
		Write(cmd);
		byte[] rsp = Read();

		if (rsp.length < STATUS_OFFSET + 1)
			throw new SCException("Not enough data received:" + rsp.length
					+ " bytes");

		if (((int) rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0)
			throw new SCException("CCID communication error: 0x"
					+ String.format("%1$02X", rsp[ERROR_OFFSET]));
		// modify by roman 2013/04/19
	}

	/*****************************************************************************
	 * 
	 * GetSlotStatus: implementation of PC_to_RDR_GetSlotStatus
	 * 
	 * @return byte of CCID_ICC_STATUS_MASK in CCID message
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final int GetSlotStatus() throws SCException {

		byte[] cmd = new byte[10];

		cmd[0] = 0x65; /* PC_to_RDR_GetSlotStatus */
		cmd[1] = cmd[2] = cmd[3] = cmd[4] = 0; /* dwLength */

		cmd[5] = mSlotIndex; /* slot number */
		cmd[6] = mSeq++;
		cmd[7] = cmd[8] = cmd[9] = 0; /* RFU */

		// modify by roman 2013/04/19
		// byte[] rsp = exchangeData(cmd);
		Write(cmd);
		byte[] rsp = Read();

		if (rsp.length < STATUS_OFFSET + 1) {
			throw new SCException("Not enough data received:" + rsp.length
					+ " bytes");
		}
		if (((int) rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0
				&& rsp[ERROR_OFFSET] != (byte) 0xFE) {
			throw new SCException("CCID communication error: 0x"
					+ String.format("%1$02X", rsp[ERROR_OFFSET]));
			// modify by roman 2013/04/19
		}
		return (int) rsp[STATUS_OFFSET] & CCID_ICC_STATUS_MASK;
	}

	/**
	 * Returns a concatenated response.
	 * 
	 * @param r1
	 *            the first part of the response.
	 * @param r2
	 *            the second part of the response.
	 * @param length
	 *            the number of bytes of the second part to be appended.
	 * @return a concatenated response.
	 */
	private final byte[] appendResponse(byte[] r1, byte[] r2, int length) {
		byte[] rsp = new byte[r1.length + length];
		System.arraycopy(r1, 0, rsp, 0, r1.length);
		System.arraycopy(r2, 0, rsp, r1.length, length);
		return rsp;
	}

	/*****************************************************************************
	 * 
	 * XfrBlockTPDU_T0: Send APDU command for T=0 Card, when sw1 == 0x6C auto
	 * set correct length in Le byte and send APDU again when sw1 == 0x61 auto
	 * send GET RESPONSE command 0x00, 0xC0, 0x00, 0x00, 0x00
	 * 
	 * @param apdu
	 *            : APDU Command.
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private final byte[] XfrBlockTPDU_T0(byte[] apdu) throws SCException {

		// logCCID("T=0: " + apdu.length + " bytes");

		if (apdu.length > mMaxCCIDMessageLength - 10)
			throw new SCException("Command too long (" + apdu.length
					+ " bytes) for max: " + (mMaxCCIDMessageLength - 10)
					+ " bytes");

		CCID_Transmit(apdu);
		byte[] data = CCID_Receive();

		// add to manage TPDU according to mFeatures
		if ((mFeatures & CCID_CLASS_EXCHANGE_MASK) != CCID_CLASS_TPDU) {
			// handle GET RESPONSE and command repetition
			if (data.length >= 2) {
				byte sw1 = (byte) (data[data.length - 2] & 0xFF);
				byte sw2 = (byte) (data[data.length - 1] & 0xFF);
				if (sw1 == 0x6C) {
					apdu[apdu.length - 1] = sw2;
					CCID_Transmit(apdu);
					data = CCID_Receive();
				} else if (sw1 == 0x61) {
					// TODO replace 0x00 par CLA... data[0] or data[data.length
					// - 4] ?

					byte[] getResponse = new byte[] { 0x00, (byte) 0xC0, 0x00,
							0x00, 0x00 };
					// System.out.println("#### get Response : "
					// + bytesToString(getResponse));
					byte[] response = new byte[data.length - 2];
					System.arraycopy(data, 0, response, 0, data.length - 2);

					getResponse[4] = sw2;
					data = XfrBlockTPDU_T0(getResponse);

					if (data.length >= 2 && data[data.length - 2] == 0x61) {
						response = appendResponse(response, data,
								data.length - 2);
					} else {
						response = appendResponse(response, data, data.length);
					}
					data = response;
				}
			}
		}

		return data;
	}

	/*****************************************************************************
	 * 
	 * exchangeData: implement T1Block exchangeData from T1Protocol class
	 * 
	 * @param sendBlock
	 *            : see T1Block.
	 * @return T1Block
	 * @throws T1IOException
	 * 
	 ****************************************************************************/
	protected T1Block exchangeData(T1Block sendBlock) throws SCException,
			T1BlockEDCErrorException {

		byte[] tpdu = sendBlock.getBlock();

		try {
			byte[] rsp = XfrBlockTPDU_T0(tpdu);
			T1Block recvBlock = new T1Block(rsp, T1Block.EDC_LDR);
			return recvBlock;

		} catch (T1BlockEDCErrorException e) {
			e.printStackTrace();
			throw e;
		} catch (SCException e) {
			e.printStackTrace();
			throw new SCException((byte) 0xFE);// new byte[] { (byte) 0X41,
												// (byte) 0xFE });
			// throw new T1IOException("XfrBlockTPDU_T1 fail");
		}
	}

	/*****************************************************************************
	 * 
	 * XfrBlockTPDU_T1: Send APDU command for T=1 Card, see transmitT1.
	 * 
	 * @param apdu
	 *            : APDU Command.
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private final byte[] XfrBlockTPDU_T1(byte[] apdu) throws SCException {
		byte[] rsp = transmitT1(apdu);
		return rsp;
	}

	/*****************************************************************************
	 * 
	 * XfrBlockAPDU_extended: Send extend APDU command for T=0 Card.
	 * 
	 * @param apdu
	 *            : APDU Command.
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private final byte[] XfrBlockAPDU_extended(byte[] apdu) throws SCException {

		logCCID("T=0 (extended): " + apdu.length + " bytes");

		/* send the APDU */
		int send = 0;

		/* we suppose one command is enough */
		int chain = 0x00;

		if (apdu.length > mMaxCCIDMessageLength - 10) {
			// TODO: implement chained command
			// send and receive data chain by chain
			throw new SCException("XfrBlockAPDU_extended not implemented");
		}

		return XfrBlockTPDU_T0(apdu);
	}

	/*****************************************************************************
	 * 
	 * XfrBlock: Main XfrBlock function.
	 * 
	 * @param apdu
	 *            : APDU Command.
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final byte[] XfrBlock(byte[] apdu) throws SCException {
		if (apdu == null)
			throw new NullPointerException();

		// logData("Reader", "CCID Features=" + String.format("%1$08X",
		// mFeatures));

		switch (mFeatures & CCID_CLASS_EXCHANGE_MASK) {
		case CCID_CLASS_TPDU:
			if (mProtocol == ATR.PROTOCOL_T0)
				return XfrBlockTPDU_T0(apdu);
			else if (mProtocol == ATR.PROTOCOL_T1)
				return XfrBlockTPDU_T1(apdu);
			else
				throw new SCException("IDF protocol not support");

		case CCID_CLASS_SHORT_APDU:
			return XfrBlockTPDU_T0(apdu);

		case CCID_CLASS_EXTENDED_APDU:
			return XfrBlockAPDU_extended(apdu);

		case CCID_CLASS_CHARACTER:
			throw new SCException("not support character level exchange");

		default:
			throw new SCException("IDF communication error");
		}
	}

	/*****************************************************************************
	 * 
	 * Transmit: function expose
	 * 
	 * @param apdu
	 *            : APDU Command.
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final byte[] Transmit(byte[] apdu) throws SCException {

		if ((mLogLevel & LOG_APDU) == LOG_APDU)
			logData("send:", apdu);

		byte[] data = XfrBlock(apdu);

		if ((mLogLevel & LOG_APDU) == LOG_APDU)
			logData("recv:", data);

		return data;
	}

	/*****************************************************************************
	 * 
	 * SetLength: set length of CCID message
	 * 
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private final void SetLength(byte[] cmd, int value) {
		cmd[1] = (byte) (value & 0xFF);
		cmd[2] = (byte) ((value >> 8) & 0xFF);
		cmd[3] = (byte) ((value >> 16) & 0xFF);
		cmd[4] = (byte) ((value >> 24) & 0xFF);
	} /* SetLength */

	/*****************************************************************************
	 * 
	 * SetLength: get length of CCID message, convert a 4 byte integer in USB
	 * format into an int
	 * 
	 * @return data length of the message
	 * @throws SCException
	 * 
	 ****************************************************************************/
	private final int GetLength(byte[] rsp) {
		int len = rsp[1] & 0xFF;
		len += (rsp[2] << 8) & 0xFF00;
		len += (rsp[3] << 16) & 0xFF0000;
		len += (rsp[4] << 24) & 0xFF000000;

		return len;
	} /* GetLength */

	/*****************************************************************************
	 * 
	 * SetParameters: implementation of PC_to_RDR_GetParameters
	 * 
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final byte[] GetParameters() throws SCException {
		byte[] cmd = new byte[10];

		cmd[0] = 0x6C; /* PC_to_RDR_GetParameters */
		cmd[1] = cmd[2] = cmd[3] = cmd[4] = 0; /* dwLength */
		cmd[5] = mSlotIndex; /* slot number */
		cmd[6] = mSeq++;
		cmd[7] = cmd[8] = cmd[9] = 0; /* RFU */

		Write(cmd);
		byte[] rsp = Read();

		if (rsp.length < STATUS_OFFSET + 1)
			throw new SCException("Not enough data received:" + rsp.length
					+ " bytes");

		if (((int) rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0)
			throw new SCException("CCID communication error: 0x"
					+ String.format("%1$02X", rsp[ERROR_OFFSET]));

		return rsp;
	} /* SetParameters */

	/*****************************************************************************
	 * 
	 * SetParameters: implementation of PC_to_RDR_SetParameters
	 * 
	 * @param protocol
	 *            , bProtocolNum 00h = Structure for protocol T=0, 01h =
	 *            Structure for protocol T=1
	 * @param param
	 *            , abProtocolDataStructure: Protocol Data Structure
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final void SetParameters(byte protocol, byte[] param)
			throws SCException {

		byte[] cmd = new byte[10 + param.length];

		cmd[0] = 0x61; /* PC_to_RDR_SetParameters */
		// cmd[1] = cmd[2] = cmd[3] = cmd[4] = 0; /* dwLength */
		SetLength(cmd, param.length); /* dwLength */

		cmd[5] = mSlotIndex; /* slot number */
		cmd[6] = mSeq++;
		cmd[7] = protocol; /* bProtocolNum */
		cmd[8] = cmd[9] = 0; /* RFU */

		System.arraycopy(param, 0, cmd, 10, param.length);

		Write(cmd);
		byte[] rsp = Read();

		if (rsp.length < STATUS_OFFSET + 1)
			throw new SCException("Not enough data received:" + rsp.length
					+ " bytes");

		if (((int) rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0) {
			if (0x00 == cmd[ERROR_OFFSET]) /* command not supported */
				throw new SCException("SetParameters not supported");

			/* a parameter is not changeable */
			if ((cmd[ERROR_OFFSET] >= 1) && (cmd[ERROR_OFFSET] <= 127))
				return;
			else
				throw new SCException("CCID communication error: 0x"
						+ String.format("%1$02X", rsp[ERROR_OFFSET]));
		}

	} /* SetParameters */

	/*****************************************************************************
	 * 
	 * Escape: implementation of PC_to_RDR_Escape
	 * 
	 * @param espcmd
	 *            : Escape Command.
	 * @return response data
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final byte[] Escape(byte[] espcmd) throws SCException {

		if (espcmd == null)
			throw new NullPointerException();

		byte[] cmd = new byte[10 + espcmd.length];

		cmd[0] = 0x6B; /* PC_to_RDR_Escape */
		SetLength(cmd, espcmd.length); /* dwLength */
		cmd[5] = mSlotIndex; /* slot number */
		cmd[6] = mSeq++;
		cmd[7] = cmd[8] = cmd[9] = 0; /* RFU */

		// copy Escape command to data block
		System.arraycopy(espcmd, 0, cmd, 10, espcmd.length);

		Write(cmd);
		byte[] rsp = Read();

		if (rsp.length < STATUS_OFFSET + 1)
			throw new SCException("Not enough data received:" + rsp.length
					+ " bytes");

		if (((int) rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0)
			throw new SCException("CCID communication error: 0x"
					+ String.format("%1$02X", rsp[ERROR_OFFSET]));

		/* copy the response */
		int len = GetLength(rsp);
		if (len > rsp.length)
			len = rsp.length;

		byte[] data = new byte[len];
		System.arraycopy(rsp, 10, data, 0, len);

		return data;
	} /* Escape */

//	private static void CopyBuffer(byte[] bufferIn, int offsetIn,
//			byte[] bufferOut, int offsetOut, int length) {
//		for (int i = 0; i < length; i++) {
//			bufferOut[i + offsetOut] = bufferIn[i + offsetIn];
//		}
//	}

	/*****************************************************************************
	 * 
	 * General functions for application using
	 * 
	 ****************************************************************************/
	/*****************************************************************************
	 * 
	 * ConnectCard: connect card by slot number and Preferred Protocols
	 * 
	 * @param Slot
	 *            : slot number, 0: slot 1, 1: slot2 ...
	 * @param PreferredProtocols
	 *            : Preferred Protocols ex: PROTOCOL_ANY = PROTOCOL_T0 |
	 *            PROTOCOL_T0
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final byte[] ConnectCard(int Slot, byte PreferredProtocols)
			throws SCException {

		// check slot here
		if (Slot > mMaxSlotIndex)
			throw new SCException("Max slot index supported is "
					+ mMaxSlotIndex);

		mSlotIndex = (byte) Slot;
		int CardPresent = GetSlotStatus();

		if (CardPresent == CCID_ICC_ABSENT)
			return null;

		// if card is not present return null
		byte[] ATR = PowerOn();
		mPowerOn = true;

		initT1ProtocolDone = false;

		// Automatic parameters negotiation & PPS
		SetProtocolParameters(ATR, PreferredProtocols);

		return ATR;
	}

	/*****************************************************************************
	 * 
	 * ConnectCard: connect card by default slot number=0 and Preferred
	 * Protocols backward compatible
	 * 
	 * @param PreferredProtocols
	 *            : Preferred Protocols ex: PROTOCOL_ANY = PROTOCOL_T0 |
	 *            PROTOCOL_T0
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final byte[] ConnectCard(byte PreferredProtocols) throws SCException {
		return ConnectCard(0, PreferredProtocols);
	}

	/*****************************************************************************
	 * 
	 * ConnectCard: connect and create specific card instance by slot number and
	 * Preferred Protocols
	 * 
	 * @param CardClass
	 *            : card class to be created ex: "com.scdroid.smartcard.EMVCard"
	 * @param Slot
	 *            : slot number, 0: slot 1, 1: slot2 ...
	 * @param PreferredProtocols
	 *            : Preferred Protocols ex: PROTOCOL_ANY = PROTOCOL_T0 |
	 *            PROTOCOL_T0
	 * @return card instance
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final Card ConnectCard(String CardClass, int Slot,
			byte PreferredProtocols) throws SCException {

		byte[] ATR = ConnectCard(Slot, PreferredProtocols);
		if (ATR == null)
			return null; // no card presented

		Card crd = null;
		Class<?> CardClazz;
		try {
			CardClazz = Class.forName(CardClass);
			crd = (Card) CardClazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new SCException("class not found", e);
		} catch (InstantiationException e) {
			throw new SCException("instantiation fail", e);
		} catch (IllegalAccessException e) {
			throw new SCException("illegal access", e);
		}

		// multu-slot support we need clone the reader object
		// then attached to the card class for further useage
		// crd.AttachReader(this);
		try {

			// clone the reader object
			crd.AttachReader((IReader) this.clone());
		} catch (CloneNotSupportedException e) {
			throw new SCException("Clone IReader fail");
		}

		if (crd.IdentifyCard(ATR)) {
			return crd;
		} else {
			throw new SCException("this card is not " + CardClass);
		}
	}

	/*****************************************************************************
	 * 
	 * ConnectCard: connect and create specific card instance by default slot
	 * number=0 and Preferred Protocols backward compatible
	 * 
	 * @param CardClass
	 *            : card class to be created ex: "com.scdroid.smartcard.EMVCard"
	 * @param PreferredProtocols
	 *            : Preferred Protocols ex: PROTOCOL_ANY = PROTOCOL_T0 |
	 *            PROTOCOL_T0
	 * @return card instance
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final Card ConnectCard(String CardClass, byte PreferredProtocols)
			throws SCException {
		return ConnectCard(CardClass, 0, PreferredProtocols);
	}

	/*****************************************************************************
	 * 
	 * isCardPresent: check if the card is in the reader
	 * 
	 * @return true or false
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public boolean isCardPresent() throws SCException {
		int CardPresent = GetSlotStatus();

		if (CardPresent == CCID_ICC_ABSENT)
			return false;

		return true;
	}

	/*****************************************************************************
	 * 
	 * DisConnectCard: Disconnect and power off the card
	 * 
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public final void DisConnectCard() throws SCException {
		PowerOff();
	}

	/*****************************************************************************
	 * 
	 * WaitCardEvent: Wait card inserted or removed event
	 * 
	 * @throws SCException
	 * 
	 ****************************************************************************/
	public void WaitCardEvent(int event) throws SCException {

		int CardStatus = 0;
		// TODO:
		// using interrupt to detect card event

		// use polling to detect card change
		while (true) {
			CardStatus = GetSlotStatus();

			if (event == CARD_EVENT_DETECED) {
				if (CardStatus == CCID_ICC_PRESENT_ACTIVE
						|| CardStatus == CCID_ICC_PRESENT_INACTIVE)
					return;
			} else {
				// CARD_REMOVED
				if (CardStatus == CCID_ICC_ABSENT)
					return;
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}
	}

	// overrided into USBReader
	public void getReaderDataRates(byte numDataRateSupported) {
		// System.out.println("# CCID reader get reader data rates");
	}
}
