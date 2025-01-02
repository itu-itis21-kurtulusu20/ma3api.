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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.util.Log;

import com.scdroid.smartcard.SCException;

/**
 * <tt>USBReader</tt>
 * 
 * reader IO implementation by android USB device API
 * 
 * @author Roman (roman@scdroid.com)
 * @version 1.26
 */
public class USBReader extends CCIDReader {
	private final PendingIntent mPermissionIntent;
	private final UsbManager mUsbManager;
	private UsbDevice mDevice = null;

	private UsbDeviceConnection mDeviceConnection = null;
	private UsbInterface mUsbInterface = null;
	private UsbEndpoint mEndpointOut = null;
	private UsbEndpoint mEndpointIn = null;

	private final int MAX_READ_TIMEOUT = 180000;
	private int mVID = 0;
	private int mPID = 0;;
	private String mVendorName = "SCDROID";
	private String mProductName = "CCID Reader";
	private String mPackageName = "";
	private String mLicenseKey = "";
	private int mReaderIndex = 0;

	private static final boolean ENABLE_VID_PID = false;
	private static final int VID_CONTROL = 0x4658;
	private static final int PID_CONTROL = 0;

	private static final boolean ENABLE_LICENSE_KEY = false;
	private static final String ACTION_SC_PERMISSION = "com.scdroid.smartcard.USB_PERMISSION";

	public USBReader(PendingIntent intent, UsbManager manager) {
		mPermissionIntent = intent;
		mUsbManager = manager;
	}

	public USBReader(PendingIntent intent, UsbManager manager,
			UsbDevice device, int index) {
		mPermissionIntent = intent;
		mUsbManager = manager;
		mDevice = device;
		mVID = device.getVendorId();
		mPID = device.getProductId();
		mReaderIndex = index;

		// TODO Filter supported Reader List Here
		// int vid = device.getVendorId();
		// int pid = device.getProductId();

		// get product name and vender name reported by usb
		// add by roman 2013/06/14
		USBInfo usb = new USBInfo();
		if (usb.LoadDevice(mVID, mPID)) {
			mVendorName = usb.ReportedVendorName;
			mProductName = usb.ReportedProductName;
		}
	}

	public final void logData(String message) {
		Log.i("USBReader", message);
	}

	public int getVID() {
		return mVID;
	}

	public int getPID() {
		return mPID;
	}

	/*
	 * public void setReaderIndex(int index) { mReaderIndex = index; }
	 * 
	 * public void setVendorName(String name) { mVendorName = name; } public
	 * void setProductName(String name) { mProductName = name; }
	 */

	public void setPackageName(String name) {
		mPackageName = name;
	}

	public void setLicenseKey(String key) {
		mLicenseKey = key;
	}

	public String getVendorName() {
		return mVendorName;
	}

	public String getProductName() {
		return mProductName;
	}

	public String getReaderName() {
		return mVendorName + " " + mProductName + " " + mReaderIndex;
	}

	/*
	 * private void SetReaderFeatures() { if (mVID == 0x058f && mPID == 0x9520)
	 * mFeatures = 0x000204be; else if (mVID == 0x058f && mPID == 0x9540)
	 * mFeatures = 0x000204be; //RELTEK else if (mVID == 0x0bda && mPID ==
	 * 0x0165) mFeatures = 0x00010030; else if (mVID == 0x0bda && mPID ==
	 * 0x0161) mFeatures = 0x00010030; //GemPC else if (mVID == 0x08e6 && mPID
	 * == 0x3437) mFeatures = 0x00010230; else mFeatures = 0x00;
	 * 
	 * }
	 */

	private static List<USBReader> getReaders(PendingIntent pendingIntent,
			UsbManager usbManager, String PackageName, String LicenseKey) {

		Log.i("USBReader", "SC&Droid CCID Library Version v" + VERSION);

		List<USBReader> readers = new ArrayList<USBReader>();

		for (UsbDevice device : usbManager.getDeviceList().values()) {
			int count = device.getInterfaceCount();
			for (int i = 0; i < count; i++) {
				UsbInterface intf = device.getInterface(i);
				int index = 0;
				if (intf.getInterfaceClass() == 0x0B
						&& intf.getInterfaceSubclass() == 0) {

					// modify by roman 2013/06/14
					USBReader reader = new USBReader(pendingIntent, usbManager,
							device, index);
					// modify by roman 2013/10/15
					reader.setPackageName(PackageName);
					reader.setLicenseKey(LicenseKey);
					// if (mLogLevel > 0)
					// Log.i("USBReader",
					// "CCID reader found: " + reader.getReaderName()
					// + " (VID="
					// + String.format("%1$04X", reader.getVID())
					// + " PID="
					// + String.format("%1$04X", reader.getPID())
					// + ")");

					readers.add(reader);
					index++;
				}
			}
		}
		return readers;
	}

	private static String getLicenseKey(PackageManager pm, String packageName) {
		String LicenseKey = "";
		try {
			ApplicationInfo ai = pm.getApplicationInfo(packageName,
					PackageManager.GET_META_DATA);

			LicenseKey = ai.metaData.getString("com.scdroid.ccid.key");

		} catch (Exception e) {
			return "";
		}

		return LicenseKey;
	}

	/**
	 * Get CCID readers list attached to your device
	 * <p>
	 * example:
	 * <pre>{@code
	 *   List<USBReader> ReaderList = USBReader.getReaders(this);
	 *   if (ReaderList.size() == 0) {
	 *       Log.v(TAG, "no reader connected");
	 *       return;
	 *   }
	 *
	 *   for (USBReader reader : ReaderList) {
	 *       Log.v(TAG, "find reader " + reader.getReaderName());
	 *   }
	 * }</pre>
	 */
	public static List<USBReader> getReaders(Context c) /* throws SCException */{
		PendingIntent pendingIntent = PendingIntent.getBroadcast(c, 0,
				new Intent(ACTION_SC_PERMISSION), 0);
		UsbManager usbManager = (UsbManager) c
				.getSystemService(Context.USB_SERVICE);

		String PackageName = c.getPackageName();
		String LicenseKey = getLicenseKey(c.getPackageManager(), PackageName);

		return getReaders(pendingIntent, usbManager, PackageName, LicenseKey);
	}

	/**
	 * Get CCID readers list attached to your device
	 * <p>
	 * example:
	 * <pre>{@code
	 *   <USBReader> ReaderList = USBReader.getReaders(this);
	 * 	 if (ReaderList.size() == 0) {
	 * 	 	Log.v(TAG, "no reader connected");
	 * 	 	return;
	 * 	 }
	 *
	 * 	 for (USBReader reader : ReaderList) {
	 * 	 	Log.v(TAG, "find reader " + reader.getReaderName());
	 * 	 }
	 * }</pre>
	 */

	public static List<USBReader> getReaders(Activity ac) /* throws SCException */{
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ac, 0,
				new Intent(ACTION_SC_PERMISSION), 0);
		UsbManager usbManager = (UsbManager) ac
				.getSystemService(Context.USB_SERVICE);
		String PackageName = ac.getPackageName();
		String LicenseKey = getLicenseKey(ac.getPackageManager(), PackageName);

		// mark by roman 2013/10/15
		// moved to open function
		// if (ENABLE_LICENSE_KEY)
		// checkLicenseKey(ac.getPackageManager(), ac.getPackageName());

		return getReaders(pendingIntent, usbManager, PackageName, LicenseKey);
	}

	private boolean findDevice() {
		// mPermissionIntent = PendingIntent.getBroadcast(this, 0, new
		// Intent(ACTION_USB_PERMISSION), 0);
		for (UsbDevice device : mUsbManager.getDeviceList().values()) {
			int count = device.getInterfaceCount();
			for (int i = 0; i < count; i++) {
				UsbInterface intf = device.getInterface(i);
				if (intf.getInterfaceClass() == 0x0B
						&& intf.getInterfaceSubclass() == 0) {

					// add by roman 2013/04/19
					// add usb information
					mVID = device.getVendorId();
					mPID = device.getProductId();

					// get product name and vender name reported by usb
					USBInfo usb = new USBInfo();
					if (usb.LoadDevice(mVID, mPID)) {
						mVendorName = usb.ReportedVendorName;
						mProductName = usb.ReportedProductName;
					}
					// add by roman 2013/04/19

					if (mLogLevel > 0)
						logData("CCID reader found: " + getReaderName()
								+ " (VID=" + String.format("%1$04X", mVID)
								+ " PID=" + String.format("%1$04X", mPID) + ")");

					mDevice = device;
					mUsbManager.requestPermission(mDevice, mPermissionIntent);
					return true;
				}
			}
		}
		return false;
	}

	private final int USB_REQ_GET_DESCRIPTOR = 0x06;
	private final int DESCRIPTION_TYPE_CONFIG = 0x02;

	private final int USB_REQ_GET_INTERFACE = 0x0A;

	// add by roman 2013/03/27
	private int dw2i(byte[] descriptor, int start, int offset) {
		int value = descriptor[start + offset] & 0xFF;
		value += (descriptor[start + offset + 1] << 8) & 0xFF00;
		value += (descriptor[start + offset + 2] << 16) & 0xFF0000;
		value += (descriptor[start + offset + 3] << 24) & 0xFF000000;
		return value;
	}

	private void GetCCIDFeatures(int nConfig) throws SCException {
		byte[] buffer = new byte[128];
		int len = 0;
		int start = 0;
		// int index = 18 + 40;

		// GetDescriptor
		int v = Build.VERSION.SDK_INT;
		if (v > Build.VERSION_CODES.HONEYCOMB_MR1) {
			buffer = mDeviceConnection.getRawDescriptors();
			len = buffer.length;
			start = 36;
			// index = 36 + 40;
		} else {

			int value = (DESCRIPTION_TYPE_CONFIG << 8) + nConfig;

			len = mDeviceConnection.controlTransfer(UsbConstants.USB_DIR_IN,
					USB_REQ_GET_DESCRIPTOR, 0x0200, 0, buffer, buffer.length,
					3000);
			start = 18;
			// index = 18 + 40;
		}

		if (len > 0) {

			if ((mLogLevel & LOG_CCID) == LOG_CCID)
				logData("usb config:", buffer, len);

			mMaxSlotIndex = buffer[start + 4];
			mVoltageSupport = buffer[start + 5];
			mDefaultClock = dw2i(buffer, start, 10);
			mMaxClock = dw2i(buffer, start, 14);
			mNumClockSupported = buffer[start + 18];
			mDataRate = dw2i(buffer, start, 19);
			mMaxDataRate = dw2i(buffer, start, 23);
			mNumDataRatesSupported = buffer[start + 27];
			mMaxIFSD = dw2i(buffer, start, 28);

			mSynchProtocols = dw2i(buffer, start, 32);
			mMechanical = dw2i(buffer, start, 36);
			mFeatures = dw2i(buffer, start, 40);

			mMaxCCIDMessageLength = dw2i(buffer, start, 44);
			// bClassGetResponse* 48 1
			// bClassEnveloppe* 49 1
			// wLcdLayout 50 2
			// bPinSupport 52 1
			// bMaxCCIDBusySlots* 53 1

			if ((mLogLevel & LOG_CCID) == LOG_CCID) {
				logData("CCID Version=" + String.format("%.2f", mCCIDVersion));
				logData("CCID bMaxSlotIndex="
						+ String.format("%d", mMaxSlotIndex));
				logData("CCID bVoltageSupport="
						+ String.format("%d", mVoltageSupport));
				logData("CCID mDefaultClock="
						+ String.format("%d", mDefaultClock) + " mMaxClock="
						+ String.format("%d", mMaxClock)
						+ " mNumClockSupported="
						+ String.format("%d", mNumClockSupported));
				// logData("CCID bNumClockSupported=" + String.format("%1$08X",
				// mNumClockSupported));
				logData("CCID mDataRate=" + String.format("%d", mDataRate)
						+ " mMaxDataRate=" + String.format("%d", mMaxDataRate)
						+ " mNumDataRatesSupported="
						+ String.format("%d", mNumDataRatesSupported));
				// logData("CCID bNumDataRatesSupported=" +
				// String.format("%1$08X", mNumDataRatesSupported));

				logData("CCID mMaxIFSD=" + String.format("%d", mMaxIFSD));
				logData("CCID mSynchProtocols="
						+ String.format("%1$08X", mSynchProtocols));
				logData("CCID mMechanical="
						+ String.format("%1$08X", mMechanical));
				logData("CCID Features=" + String.format("%1$08X", mFeatures));
				logData("CCID MaxCCIDMessageLength="
						+ String.format("%d", mMaxCCIDMessageLength));
			}
		}

		if (mFeatures == 0x00)
			throw new SCException("CCID dwFeatures not found");
	}

	private String MD5(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		byte[] sha1hash = md.digest();
		StringBuilder sb = new StringBuilder();

		for (byte b : sha1hash) // This is your byte[] result..
		{
			sb.append(String.format("%02X", b));
		}

		return sb.toString();
	}

	private void checkLicenseKey() throws SCException {
		String HashMD5 = "";
		String appKey = "";

		if (mLicenseKey == "")
			throw new SCException("Lincense key not found");

		try {
			HashMD5 = MD5(mPackageName + " licensed by SC&Droid");

		} catch (Exception e) {
			throw new SCException("Lincense key created fail");
		}

		if (!appKey.equals(HashMD5)) {
			Log.i("USBReader", "app = " + mPackageName);
			Log.i("USBReader", "key = " + mLicenseKey);
			throw new SCException("Lincense key verified fail");
		}
	}

	private void CheckVidPid() throws SCException {
		// check vid here
		if (VID_CONTROL != 0) {
			if (mVID != VID_CONTROL) {
				// Log.i("USBReader","VID = " + String.format("%1$04X", mVID));
				throw new SCException("Device not supported (VID)");
			}
		}

		// check pid here
		if (PID_CONTROL != 0) {
			if (mPID != PID_CONTROL) {
				// Log.i("USBReader","VID = " + String.format("%1$04X", mVID));
				throw new SCException("Deivce not supported (PID)");
			}
		}

	}

	public void Open() throws SCException {
		// add by roman 2013/04/19 backward compatible
		// for user only has one reader
		// don't need to get reader list
		// just open and we'll pick the first one for you
		if (mDevice == null) {

			if (!findDevice())
				throw new SCException("Device not found");
		}

		// add by roman 2013/10/15
		if (ENABLE_LICENSE_KEY)
			checkLicenseKey();

		// add by roman 2013/10/15
		if (ENABLE_VID_PID)
			CheckVidPid();

		if (!mUsbManager.hasPermission(mDevice))
			mUsbManager.requestPermission(mDevice, mPermissionIntent);

		mDeviceConnection = mUsbManager.openDevice(mDevice);
		if (mDeviceConnection == null)
			throw new SCException("openDevice fail, access not permitted");

		// SetReaderFeatures(); old functions
		GetCCIDFeatures(0);

		mUsbInterface = mDevice.getInterface(0);

		// force claimInterface to disconnect kernel driver if necessary
		if (!mDeviceConnection.claimInterface(mUsbInterface, true))
			throw new SCException("claimInterface fail");

		// look for our bulk end points1
		for (int i = 0; i < mUsbInterface.getEndpointCount(); i++) {
			UsbEndpoint ep = mUsbInterface.getEndpoint(i);
			if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
				if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
					mEndpointOut = ep;
				} else {
					mEndpointIn = ep;
				}
			}
		}
		if (mEndpointOut == null || mEndpointIn == null) {
			throw new SCException("not all endpoints found");
		}

	}

	public void Close() {
		if (mDeviceConnection != null) {
			if (mUsbInterface != null) {
				mDeviceConnection.releaseInterface(mUsbInterface);
				mUsbInterface = null;
			}
			mDeviceConnection.close();
		}

		mDevice = null;
		mDeviceConnection = null;
	}

	public void Write(byte[] cmd) throws SCException {
		if ((mLogLevel & LOG_CCID) == LOG_CCID)
			logData("PC:", cmd);

		int len = mDeviceConnection.bulkTransfer(mEndpointOut, cmd, cmd.length,
				5000);

		if (len < 0)
			throw new SCException("write fail");

	}

	private final int CMD_BUF_SIZE = 512;

	public byte[] Read() throws SCException {

		int len = 0;
		byte[] buffer = new byte[CMD_BUF_SIZE];

		len = mDeviceConnection.bulkTransfer(mEndpointIn, buffer, CMD_BUF_SIZE,
				MAX_READ_TIMEOUT);

		if (len < 0)
			throw new SCException("read fail");

		byte[] rsp = new byte[len];
		System.arraycopy(buffer, 0, rsp, 0, len);

		if ((mLogLevel & LOG_CCID) == LOG_CCID)
			logData("RDR:", rsp);

		if (((int) rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0
				|| ((int) rsp[STATUS_OFFSET] & CCID_ICC_ABSENT) != 0
				|| (((int) rsp[bMessageType_OFFSET] == RDR_to_PC_SlotStatus) && ((int) rsp[STATUS_OFFSET] & CCID_ICC_PRESENT_INACTIVE) != 0)) {

			// if (((int) rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0)
			// System.out
			// .println("# throw SCException cause of : rsp[STATUS_OFFSET] & CCID_COMMAND_FAILED) != 0");
			// else if (((int) rsp[STATUS_OFFSET] & CCID_ICC_ABSENT) != 0)
			// System.out
			// .println("# throw SCException cause of : rsp[STATUS_OFFSET] & CCID_ICC_ABSENT) != 0");
			// else if ((((int) rsp[bMessageType_OFFSET] ==
			// RDR_to_PC_SlotStatus) && ((int) rsp[STATUS_OFFSET] &
			// CCID_ICC_PRESENT_INACTIVE) != 0))
			// System.out
			// .println("# throw SCException cause of : rsp[bMessageType_OFFSET] == RDR_to_PC_SlotStatus) && ((int) rsp[STATUS_OFFSET] & CCID_ICC_PRESENT_INACTIVE) != 0");

			throw new SCException((byte) rsp[ERROR_OFFSET],
					(rsp[STATUS_OFFSET] & 0x01) == 1);// new byte[] {
														// rsp[STATUS_OFFSET],
			// rsp[ERROR_OFFSET] });
		}
		return rsp;
	}

	public void getReaderDataRates(byte numDataRateSupported) {

		int len = 0;
		int size = numDataRateSupported * 4;
		byte[] buffer = new byte[size];

		len = mDeviceConnection.controlTransfer(0xA1, 0x03, 0x00, 0x00, buffer,
				size, MAX_READ_TIMEOUT);

		if (len > 0) {

			arrayOfSupportedDataRates = new int[numDataRateSupported];
			for (int i = 0, j = 0; i < len; i += 4, j++) {
				arrayOfSupportedDataRates[j] = dw2i(buffer, i, 0);
			}

			Arrays.sort(arrayOfSupportedDataRates, 0, numDataRateSupported);
		}
	}
}
