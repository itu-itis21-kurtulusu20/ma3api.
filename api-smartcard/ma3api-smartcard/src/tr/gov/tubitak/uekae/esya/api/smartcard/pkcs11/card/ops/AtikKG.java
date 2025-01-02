package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.UnwrapObjectsResults;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.WrappedObjectsWithAttributes;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ExceptionFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.SecretKey;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.KeySpec;
import java.util.List;

/**
 * Generates private key in Atik HSM. You can access supported key size from fields
 * of class.
 * @author orcun.ertugrul
 *
 */
public class AtikKG implements IPKCS11Ops
{
	protected static Logger logger = LoggerFactory.getLogger(AtikKG.class);
	//supported key size
	public static final int RSA_2048 = 2048;
	
	protected static final String LIBNAME = "atikKg_jni";
	protected int KEY_SIZE_2048 = 1200;
	protected static int SUCCESS = 0;
	protected static final String FS = System.getProperty("file.separator");
	
	static
	{
	   String libPath = "";
	   String os = System.getProperty("os.name").toLowerCase(); 
 	   String osLibName = System.mapLibraryName(LIBNAME);
	   
	   String userdir = System.getProperty("user.dir");//.toLowerCase(); 
	   System.out.println(userdir);
  	   libPath = userdir + FS + osLibName;
  	   //Once Uygulamanin calistigi dizine bakiyoruz
  	   if (!new File(libPath).exists())
  	   {
  	      if(os.indexOf("windows") >=0)
  		   //windows ise system32 altındaki  alıyoruz
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
		   logger.error("Error in AtikKG", e);
 	   }
 	 }
	
	private native static int generateRSAPrivateKeyNative(byte[] keyByte, int[] keyLen, long  HSMHandle);
	private native static long  openHSMNative(int index);
	private native static int closeHSMNative(long HSMHandle);
	private native static int getHSMCountNative();
	
	public byte[] generateRSAPrivateKey(long aSessionID, int keySize) throws ESYAException
	{
		int[] keyLen = new int[1];
		
		if(keySize == 2048)
		{
			keyLen[0] = KEY_SIZE_2048;
		}
		else
		{
			throw new ESYAException("Invalid key size.");
		}
		
		byte[] keyByte = new byte[keyLen[0]];
		
		int returnValue = generateRSAPrivateKeyNative( keyByte, keyLen, aSessionID);
		
		if ( returnValue != SUCCESS )
		{
			throw new ESYAException("HSM Key Generation Error occurred. Error code is :" + returnValue);
		}
		byte[] resultKeyByte = new byte[keyLen[0]];
		System.arraycopy(keyByte, 0, resultKeyByte, 0, keyLen[0]);
	
		return resultKeyByte;	
	}

	public KeyPair generateECKeyPair(long aSessionID, ECParameterSpec ecParameterSpec) throws ESYAException{
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public long[] getTokenPresentSlotList() throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

    public List<Pair<Long, String>> getTokenPresentSlotListWithDescription() throws PKCS11Exception {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }


    public void initialize() throws PKCS11Exception, IOException {
		//throw new RuntimeException("ERROR Operation:Operation is not supported in AtikKG");
		
	}

    public long[] getSlotList() throws PKCS11Exception
	{
		int count = getHSMCountNative();
		long [] slots = new long[count]; 
		for(int i=0; i < count; i++)
		{
			slots[i] =  i + 1;
		}
		return slots;
	}

	
	public CK_SLOT_INFO getSlotInfo(long aSlotID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean isTokenPresent(long aSlotID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public CK_TOKEN_INFO getTokenInfo(long aSlotID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public CK_SESSION_INFO getSessionInfo(long aSessionID)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public long[] getMechanismList(long aSlotID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public long openSession(long aSlotID) throws PKCS11Exception 
	{
		long handle  = openHSMNative((int)(aSlotID-1));
		
		if ( handle == 0 )
		{
			throw PKCS11ExceptionFactory.getPKCS11Exception(PKCS11Constants.CKR_FUNCTION_FAILED);
		}		
		return handle;}

	
	public void closeSession(long aSessionID) throws PKCS11Exception 
	{
		int returnValue = closeHSMNative(aSessionID);
		
		if ( returnValue != SUCCESS )
		{
			throw PKCS11ExceptionFactory.getPKCS11Exception(PKCS11Constants.CKR_FUNCTION_FAILED);
		}		
	}

	
	public void login(long aSessionID, String aCardPIN) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void logout(long aSessionID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean isAnyObjectExist(long aSessionID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void importCertificate(long aSessionID, String aCertLabel,
			X509Certificate aSertifika) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public long[] createKeyPair(long aSessionID, String aKeyLabel,
                                AlgorithmParameterSpec aParamSpec, boolean aIsSign, boolean aIsEncrypt)
			throws PKCS11Exception,IOException,SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

    public KeySpec createKeyPair(long aSessionID, KeyPairTemplate template) throws PKCS11Exception, IOException, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public byte[] signDataWithCertSerialNo(long aSessionID,
			byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aImzalanacak)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public byte[] decryptDataWithCertSerialNo(long aSessionID,
			byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aData)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public List<byte[]> getCertificates(long aSessionID) throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}


	public List<byte[]> getSignatureCertificates(long aSessionID)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public List<byte[]> getEncryptionCertificates(long aSessionID)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public String[] getSignatureKeyLabels(long aSessionID)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public String[] getEncryptionKeyLabels(long aSessionID)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean isObjectExist(long aSessionID, String aLabel)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void writePrivateData(long aSessionID, String aLabel, byte[] aData)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void writePublicData(long aSessionID, String aLabel, byte[] aData)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public List<byte[]> readPrivateData(long aSessionID, String aLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public List<byte[]> readPublicData(long aSessionID, String aLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public List<byte[]> readCertificate(long aSessionID, String aLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public byte[] readCertificate(long aSessionID, byte[] aCertSerialNo)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public KeySpec readPublicKeySpec(long aSessionID, String aLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void updatePrivateData(long aSessionID, String aLabel, byte[] aValue)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void updatePublicData(long aSessionID, String aLabel, byte[] aValue)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void deletePrivateObject(long aSessionID, String aLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public void deleteObject(long sessionId, long objectHandle) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	public void deletePublicObject(long aSessionID, String aLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	public void deletePrivateData(long aSessionID, String aLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void deletePublicData(long aSessionID, String aLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public byte[] getRandomData(long aSessionID, int aDataLength)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public byte[] getTokenSerialNo(long aSlotID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public byte[] signData(long aSessionID, String aKeyLabel,
			byte[] aImzalanacak, CK_MECHANISM aMechanism) throws PKCS11Exception,
			SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

    public byte[] signAndRecoverData(long aSessionID, String aKeyLabel, byte[] aImzalanacak, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public byte[] verifyAndRecoverData(long aSessionID, String aKeyLabel, byte[] aSignature, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }


    public void verifyData(long aSessionID, String aKeyLabel, byte[] aData,
			byte[] aImza, long aMechanism) throws PKCS11Exception,
			SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	public void verifyData(long aSessionID, String aKeyLabel, byte[] aData,
						   byte[] aImza, CK_MECHANISM aMechanism) throws PKCS11Exception,
			SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public void verifyData(long aSessionID, long aKeyID, byte[] aData, byte[] aImza, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	public byte[] encryptData(long aSessionID, String aKeyLabel, byte[] aData,
			CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public byte[] encryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public void encryptData(long aSessionID, long keyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	public byte[] decryptData(long aSessionID, String aKeyLabel, byte[] aData,
			CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public byte[] decryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public void decryptData(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	public long[] importCertificateAndKey(long aSessionID, String aCertLabel,
										  String aKeyLabel, PrivateKey aPrivKey, X509Certificate aCert)
			throws PKCS11Exception, IOException, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}
	
	public void changePassword(String aOldPass, String aNewPass, long aSessionID)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void formatToken(String aSOpin, String aNewPIN, String aLabel,
			int slotID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void setSOPin(byte[] aSOPin, byte[] aNewSOPin, long aSessionHandle)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public void changeUserPin(byte[] aSOPin, byte[] aUserPin,
			long aSessionHandle) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean setContainer(byte[] aContainerLabel, long aSessionHandle) {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti,
			int aAnahtarLen, String aScfname, String aContextName,
			X509Certificate aPbCertData, int aSignOrEnc) {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti,
			int aAnahtarLen, String aScfname, String aContextName,
			byte[] aPbCertData, int aSignOrEnc) {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean isPrivateKeyExist(long aSessionID, String aLabel)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean isPublicKeyExist(long aSessionID, String aLabel)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	
	public boolean isCertificateExist(long aSessionID, String aLabel)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}
	
	public long[] importKeyPair(long aSessionID, String aLabel, KeyPair aPrivKey, byte[] aSubject, boolean aIsSign, boolean aIsEncrypt)
	throws PKCS11Exception,SmartCardException,IOException
	{
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}
	
	public long createSecretKey(long aSessionId, SecretKey aKey)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
		
	}

	public long importSecretKey(long aSessionId, SecretKey aKey)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
		
	}

	public long importSecretKey(long aSessionId, SecretKeyTemplate aKey) throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	public KeySpec readPublicKeySpec(long aSessionID, byte[] aCertSerialNo)
			throws SmartCardException, PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}
	public long[] objeAra(long aSessionID, CK_ATTRIBUTE[] aTemplate)
		throws PKCS11Exception {
	   
	    throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}
	public void getAttributeValue(long aSessionID, long aObjectID,
		CK_ATTRIBUTE[] aTemplate) throws PKCS11Exception {
	    throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	    
	}

    public byte[] wrapKey(long aSessionID, CK_MECHANISM aMechanism, String aWrappingKeyLabel, String aKeyLabel) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, String aUnwrappingKeyLabel, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public void unwrapKey(long aSessionID, CK_MECHANISM aMechanism, String aUnwrappingKeyLabel, byte[] aWrappedKey, CK_ATTRIBUTE[] aUnwrappedKeyTemplate) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public void importWrappingRSAPublicKey(long aSessionID, String aLabel, RSAPublicKey publicKey) throws PKCS11Exception {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public void importRSAPublicKey(long aSessionID, String aLabel, RSAPublicKey publicKey, boolean aIsSign, boolean aIsEncrypt) throws PKCS11Exception {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public long[] importKeyPair(long sessionID, KeyPairTemplate template) {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public long createSecretKey(long sessionID, SecretKeyTemplate template) {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, byte[] certSerialNumber, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public byte[] getModulusOfKey(long aSessionID, long aObjID)
			throws SmartCardException, PKCS11Exception 
	{
		 throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}
	
	public long getPrivateKeyObjIDFromCertificateSerial(long aSessionID,
			byte[] aCertSerialNo) throws SmartCardException, PKCS11Exception {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}
	public long getObjIDFromPrivateKeyLabel(long aSessionID, String aLabel)
			throws SmartCardException, PKCS11Exception 
	{
		 throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public long getObjIDFromSecretKeyLabel(long aSessionID, String aLabel) throws SmartCardException, PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:getObjIDFromSecretKeyLabel is not supported in AtikKG");
	}

	public long getObjIDFromPublicKeyLabel(long aSessionID, String aLabel) throws SmartCardException, PKCS11Exception {
        throw new ESYARuntimeException("ERROR Operation:getObjIDFromPublicKeyLabel is not supported in AtikKG");
    }

    public void changeLabel(long aSessionId, String aOldLabel, String aNewLabel)
			throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
		
	}

	public byte[] signDataWithKeyID(long aSessionID, long aKeyID,
			CK_MECHANISM aMechanism, byte[] aImzalanacak)
			throws PKCS11Exception, SmartCardException 
	{
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

    public byte[] wrapKey(long aSessionID, CK_MECHANISM aMechanism, KeyTemplate wrapperKeyFacade, KeyTemplate keyFacade) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

	public long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, KeyTemplate unwrapperKeyTemplate, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }
	public int deleteCertificate(long aSessionID, String aKeyLabel)
			throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

    public String[] getWrapperKeyLabels(long aSessionID) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

    public String[] getUnwrapperKeyLabels(long aSessionID) throws PKCS11Exception, SmartCardException {
        throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
    }

	@Override
	public WrappedObjectsWithAttributes wrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, long[] objectIDs) throws PKCS11Exception, SmartCardException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public UnwrapObjectsResults unwrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, byte[] wrappedBytes) throws PKCS11Exception, ESYAException {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public CK_ATTRIBUTE[] getAllAttributes(long sessionID, long objectID) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}

	@Override
	public long deriveKey(long sessionId, CK_MECHANISM derive_mechanism, long privateKeyHandle, KeyTemplate unwrappedKeyTemplate) throws PKCS11Exception {
		throw new ESYARuntimeException("ERROR Operation:Operation is not supported in AtikKG");
	}
}
