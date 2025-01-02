/*
 * Copyright 2013 FOXPLUS International Co., Ltd.
 *
 */
package com.scdroid.ccid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * <tt>USBInfo</tt>
 * 
 * getting the information such as product name and vendor name from USB
 * description
 * 
 * @author Roman (roman@scdroid.com)
 * @version 1.26
 */
public class USBInfo {
	private static final String PATH_SYS_BUS_USB = "/sys/bus/usb/devices/";

	public String VID;
	public String PID;
	public String ReportedProductName;
	public String ReportedVendorName;
	public String SerialNumber;
	public String Speed;
	public String DeviceClass;
	public String DeviceProtocol;
	public String MaxPower;
	public String DeviceSubClass;
	public String BusNumber;
	public String DeviceNumber;
	public String UsbVersion;
	public String DevicePath;

	public USBInfo() {

	}

	public boolean LoadDevice(int v, int p) {
		String path = PATH_SYS_BUS_USB;
		try {
			File dir = new File(path);

			if (!dir.isDirectory()) {
				return false;
			}

			for (File child : dir.listFiles()) {

				if (".".equals(child.getName()) || "..".equals(child.getName())) {
					continue; // Ignore the self and parent aliases.
				}

				String parentPath = child.getAbsolutePath() + File.separator;

				DevicePath = parentPath;
				VID = readFileContents(parentPath + "idVendor");
				PID = readFileContents(parentPath + "idProduct");

				if (VID == "" || PID == "")
					continue;

				if (Integer.parseInt(VID, 16) != v
						&& Integer.parseInt(PID, 16) != p)
					continue;

				BusNumber = readFileContents(parentPath + "busnum");
				DeviceClass = readFileContents(parentPath + "bDeviceClass");
				DeviceNumber = readFileContents(parentPath + "devnum");
				DeviceProtocol = readFileContents(parentPath
						+ "bDeviceProtocol");
				DeviceSubClass = readFileContents(parentPath
						+ "bDeviceSubClass");
				MaxPower = readFileContents(parentPath + "bMaxPower");
				ReportedProductName = readFileContents(parentPath + "product");
				ReportedVendorName = readFileContents(parentPath
						+ "manufacturer");
				SerialNumber = readFileContents(parentPath + "serial");
				Speed = readFileContents(parentPath + "speed");
				UsbVersion = readFileContents(parentPath + "version");

				return true;
			}

			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private String readFileContents(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return "";
		}
		if (file.isDirectory()) {
			return "";
		}

		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));

			char[] buf = new char[1024];
			int numRead = 0;

			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}

			reader.close();

		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			return "";
		} catch (IOException e) {
			// e.printStackTrace();
			return "";
		}

		String res = fileData.toString();
		if (res == null) {
			res = "";
		}
		return res.trim();
	}
}