/**
 *-------------------------------------------------------
 * Copyright (c) 2014 GEMALTO. All Rights Reserved.
 *-------------------------------------------------------
 */
package com.scdroid.smartcard.protocol;

import com.scdroid.smartcard.SCException;

//import opencard.core.util.Tracer;

/**
 * <tt>T1Protocol</tt> is a small subset of the T1 block protocol.
 * 
 * NOTE: T1Protocol IS NOT IN FINAL STATE!!!!!!!!!!!!!!!
 * 
 * - chaining is not supported - EDC-byte calculation only with LDR (XORed), CRC
 * is not provided.
 * 
 * see ISO7816_3P9
 * 
 * @author Stephan Breideneich (sbreiden@de.ibm.com)
 * @version $Id: T1Protocol.java,v 1.3 1999/11/03 12:37:19 damke Exp $
 */
public abstract class T1Protocol {

	// private static Tracer ctracer = new Tracer(T1Protocol.class);

	private T1Block blockToAck = null;
	private boolean needMoreData = false;

	/**
	 * Constructor setting host- and remoteaddress to 0
	 */
	T1Protocol() {
		setBlockWaitingTime(0);
	}

	/**
	 * Constructor with specification of the host- and remoteaddress
	 */
	protected T1Protocol(int stdHostAddress, int stdRemoteAddress, int timeout) {
		hostAddress = stdHostAddress;
		remoteAddress = stdRemoteAddress;
		setBlockWaitingTime(timeout);
	}

	private final void logData(String title, String message) {
		// Log.i("usbReader", message);
		System.out.print(title + " " + message);
		System.out.println();
	}

	/**
	 * <tt>open</tt> should be called after creating a new object of this class.
	 * 
	 * <tt>open</tt> sends S-BLOCK(RESYNCH REQUEST) and waits for
	 * S-BLOCK(RESYNCH RESPONSE).
	 * 
	 * @exception T1Exception
	 *                thrown when error occured.
	 */
	// protected void open() throws T1Exception {
	// initProtocol();
	// }

	/**
	 * <tt>close</tt> should be called to deinitialize the object
	 */
	// protected void close() {
	// }

	/**
	 * <tt>getBlockWaitingTime</tt> returns the currently used block waiting
	 * time in milliseconds.
	 */
	protected int getBlockWaitingTime() {
		return blockWaitingTime;
	}

	/**
	 * <tt>setBlockWaitingTime</tt> sets the block waiting time in milliseconds.
	 */
	protected void setBlockWaitingTime(int timeout) {
		blockWaitingTime = timeout;
	}

	/**
	 * <tt>getHostAddress</tt>
	 */
	protected int getHostAddress() {
		return hostAddress;
	}

	/**
	 * <tt>getRemoteAddress</tt>
	 */
	protected int getRemoteAddress() {
		return remoteAddress;
	}

	/**
	 * <tt>getSendSequenceCounter</tt>
	 */
	protected int getSendSequenceCounter() {
		return sBlockCounter;
	}

	/**
	 * <tt>setSendSequenceCounter</tt>
	 */
	protected void setSendSequenceCounter(int val) {
		sBlockCounter = val;
	}

	/**
	 * <tt>incSendSequenceCounter</tt>
	 */
	protected void incSendSequenceCounter() {
		sBlockCounter++;
	}

	/**
	 * <tt>getRecvSequenceCounter</tt>
	 */
	protected int getRecvSequenceCounter() {
		return rBlockCounter;
	}

	/**
	 * <tt>setRecvSequenceCounter</tt>
	 */
	protected void setRecvSequenceCounter(int val) {
		rBlockCounter = val;
	}

	/**
	 * <tt>incRecvSequenceCounter</tt>
	 */
	protected void incRecvSequenceCounter() {
		rBlockCounter = (rBlockCounter + 1) % 2;
	}

	/**
	 * <tt>transmit</tt> sends the data to the terminal and waits for result
	 * until timeout is reached. transmit handles a subset of T1
	 * error-recognition and -recovering. <tt>transmit</tt> uses the standard
	 * host- and remote-addresses (configured by constructor)
	 * 
	 * @param sendData
	 *            data for the terminal
	 * @throws SCException
	 */
	protected synchronized byte[] transmitT1(byte[] sendData)
			throws SCException {

		return transmit(getRemoteAddress(), sendData);
	}

	/**
	 * <tt>transmit</tt> sends the data to the terminal and waits for result
	 * until timeout is reached. transmit handles a subset of T1
	 * error-recognition and -recovering. <tt>transmit</tt> uses the standard
	 * host- and remote-addresses (configured by constructor)
	 * 
	 * @param remoteAddress
	 * @param sendData
	 *            data for the terminal
	 * @throws SCException
	 */
	protected synchronized byte[] transmit(int remoteAddress, byte[] sendData)
			throws SCException {

		return transmit(getHostAddress(), remoteAddress, sendData);
	}

	/**
	 * <tt>transmit</tt> sends the data to the terminal and waits for result
	 * until timeout is reached. transmit handles a subset of T1
	 * error-recognition and -recovering.
	 * 
	 * @param hostAddress
	 * @param remoteAddress
	 *            use another remote-address as previously given by constructor.
	 * @param sendData
	 *            data for the terminal
	 * @throws SCException
	 */
	protected synchronized byte[] transmit(int hostAddress, int remoteAddress,
			byte[] sendData) throws SCException {

		// bytes to send to the receiver
		int bytesLeft = sendData.length;

		// length of the current data field to send
		int dataLen = 0;

		byte[] data = null;

		T1Block recvBlock = null;

		while (bytesLeft > 0) {

			boolean chaining;

			// copy subpackage into new buffer - make decision about chaining
			if (bytesLeft > ifsc) {
				dataLen = ifsc;
				chaining = true;
				// throw new
				// T1IOException("block too long - chaining is not supported!");
			} else {
				dataLen = bytesLeft;
				chaining = false;
			}

			data = new byte[dataLen];
			System.arraycopy(sendData, sendData.length - bytesLeft, data, 0,
					dataLen);

			// subtract size of sent data
			bytesLeft -= dataLen;

			// pack sendData into T1 I-block
			T1Block sendBlock = T1BlockFactory.createIBlock(hostAddress,
					remoteAddress, T1Block.EDC_LDR, // use XOR-algorithm
					getSendSequenceCounter(), chaining, // chaining used?
					data);

			// set current I-Block
			blockSequence[getSendSequenceCounter() % 2] = sendBlock;

			// modify by roman 2012/12/11
			// set retry to 8 when IFSC = 32
			// recvBlock = internalTransmit(5, sendBlock);
			// exchange the data

			recvBlock = internalTransmit(3, sendBlock);

			if (sendBlock.getBlockType() == T1Block.I_BLOCK) {
				incSendSequenceCounter();
			}
			// recvBlock = internalTransmit(8, sendBlock);

			// if (sendBlock.getBlockType() == T1Block.I_BLOCK)
			// incSendSequenceCounter();

		} // while (bytesLeft > 0)

		// return the application data byte array
		return recvBlock.getDATA();
	}

	protected boolean isBlockComplete(byte[] rawBytes, int len) {
		boolean complete = false;

		if (rawBytes != null)
			if ((len >= 4) && (rawBytes.length >= 4)) {
				int hdrlen = rawBytes[2] & 0xFF;
				if (hdrlen + 4 == len) {
					complete = true;
				}
			}

		return complete;
	}

	/*******************************************
	 * P R I V A T E & P R O T E C T E D *
	 *******************************************/

	private int hostAddress = 0;
	private int remoteAddress = 0;

	private int blockWaitingTime = 0;

	private int sBlockCounter = 0;
	private int rBlockCounter = 0;

	private int ifsc = 32;
	private int ifsd = 254;

	private T1Block[] blockSequence = new T1Block[2];

	/**
	 * <tt>initProtocol</tt> initialize protocol. <tt>initProtocol</tt> sends
	 * S-block (RESYNCH REQUEST) to the receiver and waits for S-block (RESYNCH
	 * RESPONSE). if successful, <tt>initProtocol</tt> resets the send- and
	 * receive sequence counter. if not, an exception is thrown after 3
	 * attempts. see ISO7816-3 - 9.6.2.3.2 rule 6.4
	 * 
	 * @exception T1Exception
	 *                thrown when error occurred.
	 */
	protected void initT1Protocol(byte maxIFSD, byte IFSC) throws SCException {

		ifsc = IFSC & 0xFF;

		// create S-block with RESYNCH REQUEST
		T1Block resyncRequest = T1BlockFactory
				.createSBlock(hostAddress, remoteAddress, T1Block.EDC_LDR,
						T1Block.S_RESYNCH_REQUEST, null);

		// create S-block with INFORMATION FIELD SIZE REQUEST (limited to 0x7F
		// bytes per block)
		T1Block ifsRequest = T1BlockFactory.createSBlock(hostAddress,
				remoteAddress, T1Block.EDC_LDR, T1Block.S_IFS_REQUEST,
				new byte[] { (byte) maxIFSD });

		// three attempts possible
		for (int i = 0; i < 3; i++) {
			// transmit S-block - answer must be S-block with RESYNCH_RESPONSE
			T1Block result = null;
			try {
				/*
				 * mark by roman 2011/08/19 don't need resyncRequest
				 * 
				 * logData("initProtocol",
				 * "Send S-BLOCK to reader for init-request."); result =
				 * internalTransmit(5, resyncRequest);
				 * 
				 * if (result.getBlockType() == T1Block.S_BLOCK) if
				 * (result.getControlBits() == T1Block.S_RESYNCH_RESPONSE) {
				 * sBlockCounter = 0; rBlockCounter = 0;
				 */
				// send InformationFieldSize-request to terminal (new field size
				// 0x7F bytes)
				result = internalTransmit(3, ifsRequest);

				if (result.getBlockType() == T1Block.S_BLOCK)
					if (result.getControlBits() == T1Block.S_IFS_RESPONSE) {
						if (result.getLEN() == 1) {

							byte b = result.getDATA()[0];
							ifsd = (int) (b & 0x7F);
							if ((b & 0x80) == 0x80)
								ifsd += 0x80;

							logData("initProtocol", "IFS set to " + ifsd
									+ " + bytes length");

							return;
						} else
							logData("initProtocol", "IFS response error");
					}
				// }
			} catch (Exception e) {
				e.printStackTrace();
				logData("initProtocol", "Reader initialization failed.");
			}
		}

		throw new SCException((byte) 0xFE, true);
		// throw new T1Exception(
		// "no correct answer on resync request - protocol init failed!");
	}

	/**
	 * <tt>internalTransmit</tt>
	 * 
	 * exchanges blocks with T1 protocol handling.
	 * 
	 * @param retryCount
	 *            number of retries left for transmitting data if retryCount = 0
	 *            reached, transfer failed
	 * 
	 * @param dataBlock
	 *            application data block.
	 * @throws SCException
	 * @exception T1Exception
	 *                thrown when error occurred.
	 */
	protected T1Block internalTransmit(int retryCount, T1Block dataBlock)
			throws SCException {
		T1Block recvBlock = null;

		// exchange data with receiver

		// for I-blocks increase the send-sequence-counter
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		if (retryCount == 0)
			throw new SCException((byte) 0xFE, true);
		// throw new T1Exception("to many retries - transfer aborted");

		try {

			// exchange data with terminal layer

			recvBlock = exchangeData(dataBlock);

			if (recvBlock == null)
				throw new T1BlockEDCErrorException();

			// check the type of the recvBlock
			switch (recvBlock.getBlockType()) {

			// I-block returned - data exchange was successful
			case T1Block.I_BLOCK:

				if ((recvBlock.getRequestedSequenceNumber() >> 2) != (getRecvSequenceCounter())) {
					throw new T1BlockSequenceNumberErrorException();
				}

				// increase the receiver sequence count on every correctly
				// received I-block
				incRecvSequenceCounter();

				blockToAck = null;
				this.needMoreData = false;

				// return the received I-block
				if (!recvBlock.hasMoreData())
					return recvBlock;

				T1Block moreDataRequest = T1BlockFactory.createRBlock(
						hostAddress, remoteAddress, T1Block.EDC_LDR,
						getRecvSequenceCounter(), T1Block.ERROR_NONE);

				this.needMoreData = true;

				T1Block recvMoreDataBlock = internalTransmit(retryCount,// - 1,
						moreDataRequest);

				recvBlock.addDATA(recvMoreDataBlock.getDATA());

				return recvBlock;

			case T1Block.R_BLOCK:

				if (this.blockToAck != null
						&& (getSendSequenceCounter() % 2) != recvBlock
								.getRequestedSequenceNumber()) {
					this.blockToAck = null;
				}

				if ((getSendSequenceCounter() % 2) == recvBlock
						.getRequestedSequenceNumber()
						|| this.blockToAck != null || this.needMoreData) {
					// resend
					if (this.blockToAck != null) {
						recvBlock = internalTransmit(retryCount, blockToAck);
					} else if (this.needMoreData) {
						recvBlock = internalTransmit(retryCount - 1, dataBlock);
					} else {
						recvBlock = internalTransmit(retryCount - 1,
								blockSequence[recvBlock
										.getRequestedSequenceNumber() % 2]);
					}
				} else {
					return recvBlock;
				}
				//
				// }
				// // resend the last block
				// recvBlock = internalTransmit(
				// retryCount - 1,
				// blockSequence[recvBlock.getRequestedSequenceNumber() % 2]);
				break;

			case T1Block.S_BLOCK:

				// WTX request from ICC
				if (recvBlock.getControlBits() == T1Block.S_WTX_REQUEST) {
					// create WTX-responseblock
					setBlockWaitingTime(recvBlock.getDATA()[0]);
					T1Block wtxResponse = T1BlockFactory.createSBlock(
							dataBlock.getSourceAddress(),
							dataBlock.getDestinationAddress(), T1Block.EDC_LDR,
							T1Block.S_WTX_RESPONSE, recvBlock.getDATA());
					return internalTransmit(retryCount, wtxResponse);
				}

				if (recvBlock.getControlBits() == T1Block.S_IFS_REQUEST) {
					ifsc = recvBlock.getDATA()[0] & 0xFF;
					T1Block ifsResponse = T1BlockFactory.createSBlock(
							dataBlock.getSourceAddress(),
							dataBlock.getDestinationAddress(), T1Block.EDC_LDR,
							T1Block.S_IFS_RESPONSE, recvBlock.getDATA());
					return internalTransmit(retryCount - 1, ifsResponse);
				}

				// check for
				// - last sent block == S_BLOCK?
				// - received S_Block == RESYNCH RESPONSE?
				if (dataBlock.getBlockType() == T1Block.S_BLOCK)
					if (dataBlock.getControlBits() == T1Block.S_RESYNCH_REQUEST)
						if (recvBlock.getControlBits() == T1Block.S_RESYNCH_RESPONSE)
							return recvBlock;

			}
		} catch (T1UnknownBlockException ube) {
			ube.printStackTrace();
		} catch (T1BlockSequenceNumberErrorException snee) {
			this.blockToAck = dataBlock;

			snee.printStackTrace();
			T1Block resendRequest = T1BlockFactory.createRBlock(hostAddress,
					remoteAddress, T1Block.EDC_LDR, getRecvSequenceCounter(),
					T1Block.ERROR_OTHER);
			// incSendSequenceCounter();
			blockSequence[getSendSequenceCounter() % 2] = resendRequest;
			recvBlock = internalTransmit(retryCount - 1, resendRequest);
		} catch (T1BlockEDCErrorException beee) {

			this.blockToAck = dataBlock;

			beee.printStackTrace();
			T1Block resendRequest = T1BlockFactory.createRBlock(hostAddress,
					remoteAddress, T1Block.EDC_LDR, getRecvSequenceCounter(),
					T1Block.ERROR_EDC);
			// incSendSequenceCounter();
			blockSequence[getSendSequenceCounter() % 2] = resendRequest;
			recvBlock = internalTransmit(retryCount - 1, resendRequest);
		}

		// no usable result reached
		return recvBlock;
	}

	/**
	 * <tt>exchangeData</tt>
	 * 
	 * responsible for the real data-transfer.
	 * 
	 * @param sendBlock
	 *            the T1-block with the send-data inside.
	 * @exception T1TimeoutException
	 *                thrown when time is elapsed receiving a T1-block
	 * @exception T1BlockLengthException
	 *                thrown when difference detected between calculated and
	 *                received block length
	 * @exception T1UnknownBlockException
	 *                thrown when blocktype could not be recognized
	 * @exception T1BlockEDCErrorException
	 *                thrown when error detection code differs from the
	 *                calculated value
	 * @throws SCException
	 */
	protected abstract T1Block exchangeData(T1Block sendBlock)
			throws T1IOException, T1TimeoutException, T1BlockLengthException,
			T1UnknownBlockException, T1BlockEDCErrorException, SCException;

}
