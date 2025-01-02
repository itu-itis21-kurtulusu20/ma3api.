package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ESYAPKCS11
{
	protected static Logger logger = LoggerFactory.getLogger(ESYAPKCS11.class);
	private static final String LIBNAME = "miniesyajni";
	private static final String FS = System.getProperty("file.separator");
	static
	{
	   String libPath = "";
	   String os = System.getProperty("os.name").toLowerCase(); 
 	   String osLibName = System.mapLibraryName(LIBNAME);
	   
	   String userdir = System.getProperty("user.dir");//.toLowerCase();        	   
  	   libPath = userdir + FS + osLibName;
  	   //Once Uygulamanin calistigi dizine bakiyoruz
  	   if (!new File(libPath).exists())
  	   {
  	      if(os.indexOf("windows") >=0)
  		   //windows ise system32 altındaki esyajni.dll dosyasını alıyoruz
  	    	  libPath = System.getenv("SystemRoot") + FS + "system32" + FS + osLibName;
  	      else
  	    	  libPath = FS + "lib" + FS +osLibName;
	   }
 	   
 	   try
 	   {
 		   System.load(libPath);
 	   }
 	   catch(Exception e)
 	   {
		   logger.error("Error in ESYAPKCS11", e);
 	   }
 	 }
	
	public ESYAPKCS11(String aLibName)
	{
		initPKCS11Library(System.mapLibraryName(aLibName));
	}
	
	private static native int initPKCS11Library(String aLibName);
	
	public native int changePassword (String aOldPass, String aNewPass,int aSessionHandle);

	public native int formatToken (String aSOpin, String aNewPIN, String aLabel, int slotID);
	
	public native int setSOPin (byte[] aSOPin, int aSOPinLen, byte[] aNewSOPin,int aNewSOPinLen, int aSessionHandle);

	public  native int importCertificateAndKeyWithCSP(byte[] aAnahtarCifti,int aAnahtarCiftiLen, int aAnahtarLen, String aScfname,int aScfnameLen, String aContextName,int aContextNameLen,byte[] aPbCertData, int aSignOrEnc);

	public native int changeUserPin (byte[] aSOPin,int aSOPinLen, byte[] aUserPin, int aUserPinLen, int aSessionHandle);

	public  native int setContainer (byte[] aContainerLabel, int aContainerLabelLen, int aSessionHandle, String aLibName);
	
	public  native int ChangePUK (byte[] aOldPUK, int aOldPUKLength, byte[] aNewPUK, int aNewPUKLength, int aSessionHandle, String aLibName);
	
	public  native int UnBlockPIN (byte[] aPUK, int aPUKLength, byte[] aUserPIN, int aUserPINLength, int aSessionHandle, String aLibName);
	

	
	
}
