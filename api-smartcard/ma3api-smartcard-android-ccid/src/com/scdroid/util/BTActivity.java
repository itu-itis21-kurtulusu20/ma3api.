/*
 * Copyright 2013 FOXPLUS International Co., Ltd.
 *
 */
package com.scdroid.util;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.scdroid.ccid.BTReader;
import com.scdroid.ccid.IReader;
import com.scdroid.ccid.USBReader;
import com.scdroid.smartcard.Card;
import com.scdroid.smartcard.SCException;

/**
 * A convenience class for easy creation of smart card applications.
 * <p>
 * It is used by creating a concrete Activity in your application, which extends
 * this class. This class then takes care of proper creation and abortion of the
 * reader connection and of a dedicated thread for smart card communication.
 * <p>
 * In the intended usage the client should extend this class and implement
 * {@link #createSCThread()}, which should return an implementation of the
 * {@link SCThread} abstract class and and the {@link #handleSCMessage(Message)} function, which
 * receives the card {@link Events} message such as card inserted. 
 * <p>
 * In this implementation, the client implements the {@link SCThread#RunCommands()} method, 
 * which gets called as soon as card inserted into the reader.
 * <p>
 * To start the smart card thread, client could call {@link #ExecuteCard()} from UI control, ex: Button onClick.   
 * <pre>
 * {@link SCThread} is created to run following jobs:
 * 1.1 Sending {@link Events#WAITING_READER_CONNECT}
 * 1.2 Reader is connected to the phone
 * 1.3 Sending {@link Events#READER_CONNECTED}
 * 2.1 Sending {@link Events#WAITING_CARD_INSERT}
 * 2.2 Card is inserted into the reader 
 * 2.3 Sending {@link Events#CARD_INSERTED}
 * 3.1 Sending {@link Events#RUN_COMMANDS_BEFORE}
 * 3.2 Call {@link SCThread#RunCommands()} function to execute the card commands
 * 3.3 Sending {@link Events#RUN_COMMANDS_AFTER}
 * </pre>
 *  */
public abstract class BTActivity extends Activity {
	
	private static final String TAG = "BTActivity";
	private SCThread mThread = null;
    private boolean mIsThreadFinished = true;
    private boolean mDebugCommand = false;
    
    protected BluetoothAdapter mBluetoothAdapter;
    
	/** Smart card reader event message. */
    public enum Events {
		/** Waiting reader connected to the smart phone */
		WAITING_READER_CONNECT,
		/** Reader is connected. */
		READER_CONNECTED,
		/** Waiting card inserted to the reader. */
		WAITING_CARD_INSERT,
		/** Card is inserted. */
		CARD_INSERTED,
		/** Waiting card removed from the reader. */
		WAITING_CARD_REMOVE,
		/** Card is removed. */
		CARD_REMOVED,
		/** NFC initiator is waiting target. */
		WAITING_TARGET,
		/** NFC emulating target. */
		EMULATING_TARGET,
		/** Before {@link SCThread#RunCommands()} is finished. */
		RUN_COMMANDS_BEFORE,
		/** After {@link SCThread#RunCommands()} is finished. */
		RUN_COMMANDS_AFTER,		
		/** When {@link SCThread#RunCommands()} is caught an error. */
		RUN_COMMANDS_ERROR,
		APPLICATION_EVENT
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
        	Log.v(TAG, "No bluetooth adapter available");
        }
        
    }
  
	
	/**
	 * Subclasses should call this method from their own onPause() if
	 * overloaded. It takes care of disconnecting from the reader.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
		if (mIsThreadFinished == false) {
			mThread.abort();
			try {
				mThread.join();
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Subclasses must call this method to start the smart card thread {@link SCThread}.
	 */
	protected void ExecuteCard() {
        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
            //add by roman 2013/09/25
            //fix bug if bluetooth is disable
            return;
        }		
		
		if (mIsThreadFinished == true) {
			mThread = createSCThread();
			mThread.start();
			mIsThreadFinished = false;
		}
	}

	/**
	 * Subclasses should implement this method by returning a concrete subclass
	 * of {@link SCThread}.
	 * 
	 * @return An implementation of {@link SCThread}
	 */
	protected abstract SCThread createSCThread();

	/**
	 * Subclasses may override this method for alternate creation modes of the
	 * reader instance.
	 * 
	 * @return A reader instance.
	 * @throws SCException 
	 */
	protected IReader createReader() throws InterruptedException, SCException {

			Log.v(TAG, "Searching BTReader Reader");

    		List<BTReader> ReaderList = BTReader.getReaders(mBluetoothAdapter); 

    		if (ReaderList.size() == 0)
    			throw new SCException("Paired Bluetooth reader not found");
    		
    		return ReaderList.get(0);	        
	
	}
	
	protected void DebugCommand() {
		mDebugCommand = true;
	}
	
	/**
	 * SmartCard message Handler
	 * <p>
	 * example:
	 * <pre>
	 *{@code @Override}
	 *protected void handleSCMessage(Message msg) {
	 *  Events what = Events.values()[msg.what];
	 * 
	 *  switch (what) 
	 *  {
	 *  case WAITING_READER_CONNECT:	
	 * 	mMessage.setText("Connecting reader.");
	 * 	mWaitCard.setVisibility(View.VISIBLE); 
	 *  break;
	 *  case READER_CONNECTED:
	 * 	mWaitCard.setVisibility(View.GONE); 
	 * 	break;
	 *  case WAITING_CARD_INSERT:	
	 * 	mMessage.setText("Please inser card.");
	 * 	mWaitCard.setVisibility(View.VISIBLE); 
	 *  break;
	 *  case WAITING_CARD_REMOVE:	
	 * 	mMessage.setText("Please remove card.");
	 * 	mWaitCard.setVisibility(View.VISIBLE); 
	 *  break;
	 *  case CARD_INSERTED:
	 *  case CARD_REMOVED:
	 *  case RUN_COMMANDS_AFTER:
	 * 	mWaitCard.setVisibility(View.GONE); 
	 * 	break;
	 *  case RUN_COMMANDS_BEFORE:
	 * 	mMessage.setText("Reading Card.");
	 * 	mWaitCard.setVisibility(View.VISIBLE); 
	 * 	break;        	
	 *  case RUN_COMMANDS_ERROR:
	 * 	mWaitCard.setVisibility(View.GONE); 
	 * 	Exception e = (Exception)msg.obj;
	 * 	mMessage.setText(e.getMessage());
	 * 	break;
	 * 
	 * }	
	 *}
	 * </pre>
	 */
	protected abstract void handleSCMessage(Message msg);
	
	private Handler mHandler = new Handler()
    {

        public void handleMessage(Message msg) 
        {
        	handleSCMessage(msg);
        }

    };
    
	protected final void SendMessage(Events what, Object obj) {
		 Message m = new Message();
		 m.what = what.ordinal();
		 
		 if (obj != null)
			 m.obj = obj;
		 
		 if (mHandler != null)
			 mHandler.sendMessage(m);
	}
	
	protected final void SendMessage(int what, Object obj) {
		 Message m = new Message();
		 m.what = what;
		 
		 if (obj != null)
			 m.obj = obj;
		 
		 if (mHandler != null)
			 mHandler.sendMessage(m);
	}
	
	protected final void SendMessage(Events what) {
		SendMessage(what, null);
	}
	
	/**
	 * An abstract class, which facilitates a thread dedicated for smart card
	 * communication.
	 */
	protected abstract class SCThread extends Thread {
		/** Subclasses should use this field for controlling the IOIO. */
		private IReader mReader = null;
		private boolean mConnected = false;

		/** Not relevant to subclasses. */
		@Override
		public final void run() {
			super.run();
			try {
				synchronized (this) {
					mReader = createReader();
					
					if (mDebugCommand)
						mReader.logLevel(USBReader.LOG_APDU);
				}
				SendMessage(Events.WAITING_READER_CONNECT);
				mReader.Open();
				SendMessage(Events.READER_CONNECTED);
				
				mConnected = true;
				
				SendMessage(Events.RUN_COMMANDS_BEFORE);
				RunCommands();
				SendMessage(Events.RUN_COMMANDS_AFTER);
				
				mReader.Close();

				SendMessage(Events.APPLICATION_EVENT);
				Log.v(TAG, "APPLICATION_EVENT");
			} catch (Exception e) {
				SendMessage(Events.RUN_COMMANDS_ERROR, e);
				e.printStackTrace();				

			} finally {
				mIsThreadFinished = true;
				
				if (mConnected) {
					mConnected = false;
					mReader.Close();
				}				
			}
		}

		/**
		 * Establishes the connection to the card in the reader and get the card instance implemented by user.
		 * 
		 * @param CardClass
		 * card class name to be created. ex: "com.infothink.smartcard.EMVCard"
		 * @param PreferredProtocols
		 * A bitmask of acceptable protocols for the connection. Possible values may be combined with the OR operation:
		 * {@link Card#PROTOCOL_T0}, {@link Card#PROTOCOL_T1} and {@link Card#PROTOCOL_ANY}
		 * @return Card instance created from CardClass and identified ok by {@link Card#IdentifyCard}.
		 * @throws SCException
		 */
		protected final Card ConnectCard(String CardClass, byte PreferredProtocols) throws SCException{
			
			SendMessage(Events.WAITING_CARD_INSERT);
			mReader.WaitCardEvent(USBReader.CARD_EVENT_DETECED);
			Card crd = mReader.ConnectCard(CardClass, PreferredProtocols);
			SendMessage(Events.CARD_INSERTED);
			return crd;
		}
		

		/**
		 * method to wait card inserted event. If card is inserted this function returns immediately.
		 * @throws SCException
		 * smart card exception, check the message to see the exception detail
		 * @see #WaitCardRemoved()
		 */
		protected void WaitCardInserted() throws SCException{
			SendMessage(Events.WAITING_CARD_INSERT);
			mReader.WaitCardEvent(USBReader.CARD_EVENT_DETECED);
			SendMessage(Events.CARD_INSERTED);
		}
		
		/**
		 * method to wait card removed event. If card is removed this function returns immediately.
		 * @throws SCException
		 * smart card exception, check the message to see the exception detail
		 * @see #WaitCardInserted()
		 */
		protected void WaitCardRemoved() throws SCException{
			SendMessage(Events.WAITING_CARD_REMOVE);
			mReader.WaitCardEvent(USBReader.CARD_EVENT_REMOVED);
			SendMessage(Events.CARD_REMOVED);
		}

		/**
		 * Subclasses should override this method for performing card operations to
		 * be done. 
		 * <p>
		 * Error message {@link Events#RUN_COMMANDS_ERROR} will be send to {@link BTActivity#handleSCMessage(Message)} method
		 * if any exception thrown. Also client can handle the exception itself by try{} catch{}.
		 * <p>
		 * Typically, this will be the main logic of the application, sending the card
		 * APUD commands and getting the responds.
		 * <p>
		 * example:
		 * <pre>
		 * {@code @Override}
		 * protected void RunCommands() throws SCException {
		 * EMVCard card = (EMVCard)ConnectCard("com.infothink.smartcard.EMVCard", Card.PROTOCOL_ANY); 
		 * 
		 * if (card == null) {
		 *   ShowMessage("please insert card");
		 *   return;
		 * }
		 * ....
		 * do all card commands, ex:
		 * card.SelectPSE();
		 * card.SelectApplication();
		 * card.initiateApplication();
		 * ....
		 * 
		 * card.Disconnect();
		 * }
		 * </pre>
		 * 
		 * @see SCThread#ConnectCard(String CardClass, byte PreferredProtocols)
		 */
		protected void RunCommands() throws SCException {
		}

		/** Not relevant to subclasses. */
		public synchronized final void abort() {
			if (mReader != null) {
				mReader.Close();
			}
			
			if (mConnected) {
				interrupt();
			}
		}
	}

}
