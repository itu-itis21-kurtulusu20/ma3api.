/*
 * Copyright 2013 FOXPLUS International Co., Ltd.
 *
 */
package com.scdroid.ccid;

import android.bluetooth.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.scdroid.ccid.CCIDReader;
import com.scdroid.smartcard.SCException;

import android.os.Build;
import android.util.Log;
import static java.lang.Thread.sleep;


/** <tt>BTReader</tt>
*
* SPP Bluetooth reader IO implementation by android Bluetooth API 
*
* @author  Roman (roman@scdroid.com)
* @version 1.26
*/
public class BTReader extends CCIDReader {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mSocket = null;
    BluetoothDevice mDevice = null;	
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    
    String  mDeviceName = "BT Reader";
    
	public BTReader (BluetoothAdapter adapter) {
		mBluetoothAdapter = adapter;
	}

	public void setDevice(BluetoothDevice device) {
		mDeviceName = device.getName();
		mDevice = device;
		
	}	
	
	public final void logData(String message) {
		Log.i("BTReader", message);
	}
	
	public String getReaderName() {
		return mDeviceName;
	}
	
	public static List<BTReader> getReaders(BluetoothAdapter adapter) {

	   	Log.i("BTReader", "SC&Droid CCID Library Version v" + VERSION);    	
	   	List<BTReader> readers = new ArrayList<BTReader>();

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        int index = 0;
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
            	String name = device.getName();
            	Log.i("BTReader", "Bluetooth Device Found: " + name);
            	
                if(name.equals("InfoThink 550BU") || name.equals("BT3040U") || name.startsWith("BT")) 
                {
                	BTReader reader = new BTReader(adapter);
                	reader.setDevice(device);
                	Log.i("USBReader", "BT reader found: " + device.getName());

					readers.add(reader);
					index ++;                
                }
            }
        }
        
	   	return readers;		
	}
	
	//add by roman 2013/07/22
	//add flag to make secure or insecure connection
	private void _open(boolean secure, int nReTry) throws SCException{
		
		if (mDevice == null)
			throw new SCException("no BlueTooth Reader paired");		
		
		// BluetoothDevice device = adapter.getRemoteDevice("00:11:22:33:44:55");
		
    	// Well known SPP UUID (will *probably* map to
    	// RFCOMM channel 1 (default) if not in use);
		
		// Standard SerialPortService ID
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); 
        try {
        	
        	if (secure)
        		mSocket = mDevice.createRfcommSocketToServiceRecord(uuid);
        	else
        		mSocket = mDevice.createInsecureRfcommSocketToServiceRecord(uuid);
		} catch (IOException e) {
			throw new SCException("createRfcommSocket fail: " + e.getMessage(), e);
		}        
        
        mBluetoothAdapter.cancelDiscovery();
        
        // Blocking connect, for a simple client nothing else can
        // happen until a successful connection is made, so we
        // don't care if it blocks.
        //mark by roman 2014/03/06
        //put nReTry as a parameter
        //int nReTry = 10;
        
        while (true) {
	        try {
				mSocket.connect();
		        mOutputStream = mSocket.getOutputStream();
		        mInputStream = mSocket.getInputStream();
		        
				//AU9520 feature 
				mFeatures = 0x000204be;
				mMaxCCIDMessageLength = 271;
				mMaxIFSD = 254;
				mDefaultClock = 3700;
				mMaxDataRate = 318280;
				
				break;
			} catch (IOException e) {
				if (nReTry == 0)
					throw new SCException("Bluetooth connect fail: " + e.getMessage(), e);
			}
			
			nReTry --;
        }
        
        logData("Bluetooth Opened");		
	}
	
	//createRfcommSocketToServiceRecord supported in API 7 and up
	//createInsecureRfcommSocketToServiceRecord supported in API 10 and up
	public void Open() throws SCException{
        if (Build.VERSION.SDK_INT >= 10 ) {
            // We're trying to create an insecure socket, which is only
            // supported in API 10 and up. Otherwise, we try a secure socket
            // which is in API 7 and up.
    		_open(false, 0);
	    } else {
    		//_open(true);
	    	//_open(true, 10);
	    	_open(true, 0);
	    }
	}
	
	public void OpenEx(int nReTry) throws SCException{
        if (Build.VERSION.SDK_INT >= 10 ) {
            // We're trying to create an insecure socket, which is only
            // supported in API 10 and up. Otherwise, we try a secure socket
            // which is in API 7 and up.
    		_open(false, nReTry);
	    } else {
    		//_open(true);
	    	_open(true, nReTry);
	    }   
	}
	

	public void Close(){
        try {
        	
        	if (mOutputStream != null)
        		mOutputStream.close();
        	
        	if (mInputStream != null)
        		mInputStream.close();
        	
        	if (mSocket != null)
        		mSocket.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}

        logData("Bluetooth Closed");
			
	}

	
	private final byte CalLRC(byte[] data, int len) {
		byte LRC = 0x00;
		
		for (int i = 0; i < len; i++)
			LRC ^= data[i];
		
		return LRC;
	}
	
	public void Write(byte[] cmd)throws SCException {
		byte[] buff = new byte[cmd.length + 1];
		System.arraycopy(cmd, 0, buff, 0, cmd.length);
		buff[cmd.length] = CalLRC(cmd, cmd.length);
		     
		try {
			
			sleep(20);
			
			if ((mLogLevel & LOG_CCID) == LOG_CCID)
				logData("PC:", buff);	

			mOutputStream.write(buff);
			mOutputStream.flush();
			 
			
		} catch (Exception e) {
			throw new SCException("write fail: " + e.getMessage(), e);
		}
	}
	
	/* GetLength (convert a 4 byte integer in USB format into an int) */
	private final int GetLength(byte[] rsp) {
		int len =  rsp[1] & 0xFF;
		len +=  (rsp[2] << 8) & 0xFF00;
		len +=  (rsp[3] << 16) & 0xFF0000;
		len +=  (rsp[4] << 24) & 0xFF000000;
		
		return len;
	} /* GetLength */
	
	public byte[] Read(int timeout) throws Exception {
		int bytesAvailable = 0;
		int nCount = 0;
		int readTotal = 0;
		byte data[] = new byte[384];
		byte buff[] = new byte[256];
		
		int lenExpected = 11;
		while (readTotal < lenExpected) {
			bytesAvailable = mInputStream.available();	
			
			if (bytesAvailable > 0) {
				int read = mInputStream.read(buff, 0, 256);
				System.arraycopy(buff, 0, data, readTotal, read);
				readTotal += read;
				
				if (readTotal > 4)
					lenExpected = 10 + GetLength(data) + 1;
				
			} else {
				sleep(1);
				nCount++;
				
				if (nCount > timeout)
					throw new Exception("read timeout (" + Integer.toString(timeout/1000) + " seconds), \n" );
			}
		}
		
		byte[] rsp = new byte[readTotal];
		System.arraycopy(data, 0, rsp, 0, readTotal);
        
        return rsp;
     
	}
	
	public byte[] Read() throws SCException {

		try {

			byte[] rsp = Read(3000);
			
			if ((mLogLevel & LOG_CCID) == LOG_CCID)
				logData("RDR:", rsp);
			
			
			byte LRC = CalLRC(rsp, rsp.length - 1);
			if (LRC != rsp[rsp.length -1])
				throw new SCException("respond CRC error");		
	        			
			return rsp;
		
		} catch (Exception e) {
			throw new SCException(e.getMessage());
		}		
	}
}
