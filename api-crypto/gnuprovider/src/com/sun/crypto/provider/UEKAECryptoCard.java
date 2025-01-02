package com.sun.crypto.provider;

	public class UEKAECryptoCard {
	
	public static native long openInterface(int cardInstance)
		throws RuntimeException;
	
	public static native void closeInterface(long deviceHandle)
		throws RuntimeException;
	
	public static native void initCipher(long deviceHandle, byte iv[],
		byte key[], int operation) throws RuntimeException;
	
	public static native void update(long deviceHandle, byte inBuffer[],
		int inOffset, byte outBuffer[], int outOffset, int size)
		throws RuntimeException;
	
	public static native void getRandomBytes(long deviceHandle,
		byte outBuffer[], int offset, int size) throws RuntimeException;
	
	public static native void readMemory(long deviceHandle, int memoryOffset,
		byte outBuffer[], int offset, int size) throws RuntimeException;
	
	public static native void writeMemory(long deviceHandle, int memoryOffset,
		byte inBuffer[], int offset, int size) throws RuntimeException;
	
	public static native int getDeviceVersion(long deviceHandle)
		throws RuntimeException;
	
	public static native void lockDevice(long deviceHandle, int timeout)
		throws RuntimeException;
	
	public static native void unlockDevice(long deviceHandle)
		throws RuntimeException;
	
    
    static{
		try {
			System.loadLibrary("PCICryptJava");

		} catch (UnsatisfiedLinkError e) {
			throw new SecurityException("Cipher olusturulamadi: Kutuphane bulunamadi ", e);
		}
	}
	
}
