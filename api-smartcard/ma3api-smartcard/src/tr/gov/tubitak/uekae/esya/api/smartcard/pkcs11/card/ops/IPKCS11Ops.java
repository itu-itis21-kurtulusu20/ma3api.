package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops;

import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.UnwrapObjectsResults;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.WrappedObjectsWithAttributes;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.SecretKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.KeySpec;
import java.util.List;


public interface IPKCS11Ops
{
	long[] getTokenPresentSlotList() throws PKCS11Exception;
    List<Pair<Long,String>> getTokenPresentSlotListWithDescription() throws PKCS11Exception;
	void initialize() throws PKCS11Exception,IOException;
	long[] getSlotList() throws PKCS11Exception;
	CK_SLOT_INFO getSlotInfo(long aSlotID) throws PKCS11Exception;
	boolean isTokenPresent(long aSlotID) throws PKCS11Exception;
	CK_TOKEN_INFO getTokenInfo(long aSlotID) throws PKCS11Exception;
	CK_SESSION_INFO getSessionInfo(long aSessionID) throws PKCS11Exception;
	long[] getMechanismList(long aSlotID) throws PKCS11Exception;
	long openSession(long aSlotID) throws PKCS11Exception;
	void closeSession(long aSessionID) throws PKCS11Exception;
	void login(long aSessionID,String aCardPIN) throws PKCS11Exception;
	void logout(long aSessionID) throws PKCS11Exception;
	boolean isAnyObjectExist(long aSessionID) throws PKCS11Exception;
	void importCertificate(long aSessionID,String aCertLabel,X509Certificate aSertifika) throws PKCS11Exception;
	//void createRSAKeyPair(long aSessionID,String aKeyLabel, int aModulusBits, boolean aIsSign, boolean aIsEncrypt) throws PKCS11Exception;
	long[] createKeyPair(long aSessionID, String aKeyLabel, AlgorithmParameterSpec aParamSpec, boolean aIsSign, boolean aIsEncrypt) throws PKCS11Exception,IOException,SmartCardException;
	byte[] signDataWithCertSerialNo(long aSessionID,byte[] aSerialNumber,CK_MECHANISM aMechanism,byte[] aImzalanacak) throws PKCS11Exception,SmartCardException;
	byte[] signDataWithKeyID(long aSessionID,long aKeyID,CK_MECHANISM aMechanism,byte[] aImzalanacak) throws PKCS11Exception,SmartCardException;
	byte[] decryptDataWithCertSerialNo(long aSessionID,byte[] aSerialNumber,CK_MECHANISM aMechanism,byte[] aData) throws PKCS11Exception,SmartCardException;
	List<byte[]> getCertificates(long aSessionID) throws PKCS11Exception,SmartCardException;
	List<byte[]> getSignatureCertificates(long aSessionID) throws PKCS11Exception,SmartCardException;
	List<byte[]> getEncryptionCertificates(long aSessionID) throws PKCS11Exception,SmartCardException;
	String [] getSignatureKeyLabels(long aSessionID)throws PKCS11Exception,SmartCardException;
	String [] getEncryptionKeyLabels(long aSessionID)throws PKCS11Exception,SmartCardException;
	boolean isObjectExist(long aSessionID,String aLabel) throws PKCS11Exception;
	void writePrivateData(long aSessionID,String aLabel,byte[] aData) throws PKCS11Exception;
	void writePublicData(long aSessionID, String aLabel,byte[] aData) throws PKCS11Exception;
	List<byte[]> readPrivateData(long aSessionID,String aLabel) throws PKCS11Exception,SmartCardException;
	List<byte[]> readPublicData(long aSessionID,String aLabel) throws PKCS11Exception,SmartCardException;
	List<byte[]> readCertificate(long aSessionID,String aLabel) throws PKCS11Exception,SmartCardException;
	byte[] readCertificate(long aSessionID,byte[] aCertSerialNo) throws PKCS11Exception,SmartCardException;
	KeySpec readPublicKeySpec(long aSessionID,String aLabel)throws PKCS11Exception,SmartCardException;
	KeySpec readPublicKeySpec(long aSessionID,byte[] aCertSerialNo) throws SmartCardException,PKCS11Exception;
	long getPrivateKeyObjIDFromCertificateSerial(long aSessionID, byte[] aCertSerialNo) throws SmartCardException,PKCS11Exception;
	long getObjIDFromPrivateKeyLabel(long aSessionID, String aLabel) throws SmartCardException,PKCS11Exception;
    long getObjIDFromPublicKeyLabel(long aSessionID, String aLabel) throws SmartCardException,PKCS11Exception;
    long getObjIDFromSecretKeyLabel(long aSessionID, String aLabel) throws SmartCardException,PKCS11Exception;
	byte [] getModulusOfKey(long aSessionID, long aObjID)  throws SmartCardException,PKCS11Exception;
	void updatePrivateData(long aSessionID,String aLabel,byte[] aValue) throws PKCS11Exception,SmartCardException;
	void updatePublicData(long aSessionID,String aLabel,byte[] aValue) throws PKCS11Exception,SmartCardException;
	void deletePrivateObject(long aSessionID,String aLabel) throws PKCS11Exception,SmartCardException;
	void deleteObject(long sessionId, long objectHandle) throws PKCS11Exception;
	void deletePublicObject(long aSessionID,String aLabel) throws PKCS11Exception,SmartCardException;
	void deletePrivateData(long aSessionID,String aLabel) throws PKCS11Exception,SmartCardException;
	void deletePublicData(long aSessionID,String aLabel) throws PKCS11Exception,SmartCardException;
	byte[] getRandomData(long aSessionID,int aDataLength) throws PKCS11Exception;
	byte[] getTokenSerialNo(long aSlotID) throws PKCS11Exception;
	byte[] signData(long aSessionID,String aKeyLabel,byte[] aImzalanacak,CK_MECHANISM aMechanism) throws PKCS11Exception,SmartCardException;

    byte[] signAndRecoverData(long aSessionID,String aKeyLabel,byte[] aImzalanacak,CK_MECHANISM aMechanism) throws PKCS11Exception,SmartCardException;
    byte[] verifyAndRecoverData(long aSessionID, String aKeyLabel, byte[] aSignature, CK_MECHANISM aMechanism) throws PKCS11Exception,SmartCardException;


    void verifyData(long aSessionID,String aKeyLabel,byte[] aData,byte[] aImza,long aMechanism) throws PKCS11Exception,SmartCardException;
	void verifyData(long aSessionID,String aKeyLabel,byte[] aData,byte[] aImza,CK_MECHANISM aMechanism) throws PKCS11Exception,SmartCardException;
	void verifyData(long aSessionID, long aKeyID, byte[] aData, byte[] aImza, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException;
	byte[] encryptData(long aSessionID,String aKeyLabel,byte[] aData,CK_MECHANISM aMechanism) throws PKCS11Exception,SmartCardException;
	byte[] encryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException;
	void encryptData(long aSessionID, long keyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException;

	byte[] decryptData(long aSessionID,String aKeyLabel,byte[] aData,CK_MECHANISM aMechanism) throws PKCS11Exception,SmartCardException;
	byte[] decryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism) throws PKCS11Exception, SmartCardException;
	void decryptData(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException;

	long[] importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, PrivateKey aPrivKey, X509Certificate aCert) throws PKCS11Exception,IOException,SmartCardException;
	//void importRSAKeyPair(long aSessionID,String aLabel, RSAPrivateCrtKey aPrivKey, byte[] aSubject,boolean aIsSign,boolean aIsEncrypt) throws PKCS11Exception,SmartCardException;
	long[] importKeyPair(long aSessionID, String aLabel, KeyPair aPrivKey, byte[] aSubject, boolean aIsSign, boolean aIsEncrypt) throws PKCS11Exception,SmartCardException,IOException;

    void changePassword(String aOldPass,String aNewPass,long aSessionID) throws PKCS11Exception;
	void formatToken(String aSOpin, String aNewPIN, String aLabel, int slotID) throws PKCS11Exception;
	void setSOPin (byte[] aSOPin, byte[] aNewSOPin, long aSessionHandle) throws PKCS11Exception;
	void changeUserPin (byte[] aSOPin, byte[] aUserPin,long aSessionHandle) throws PKCS11Exception;
	boolean setContainer (byte[] aContainerLabel, long aSessionHandle);
	boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,X509Certificate aPbCertData, int aSignOrEnc);
	boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,byte[] aPbCertData, int aSignOrEnc);

	boolean isPrivateKeyExist(long aSessionID,String aLabel) throws PKCS11Exception;
	boolean isPublicKeyExist(long aSessionID,String aLabel) throws PKCS11Exception;
	boolean isCertificateExist(long aSessionID,String aLabel) throws PKCS11Exception;

	long createSecretKey(long aSessionId, SecretKey aKey) throws PKCS11Exception;
	long importSecretKey(long aSessionId, SecretKey aKey) throws PKCS11Exception;
	long importSecretKey(long aSessionId, SecretKeyTemplate aKey) throws PKCS11Exception,SmartCardException;
	void changeLabel(long aSessionId,String aOldLabel,String aNewLabel) throws PKCS11Exception,SmartCardException;

	byte[] generateRSAPrivateKey(long aSessionID,int keySize) throws ESYAException;
	KeyPair generateECKeyPair(long aSessionID, ECParameterSpec ecParameterSpec) throws ESYAException;

	long[] objeAra(long aSessionID,CK_ATTRIBUTE[] aTemplate) throws PKCS11Exception;
	void getAttributeValue(long aSessionID,long aObjectID,CK_ATTRIBUTE[] aTemplate) throws PKCS11Exception;
	byte[] wrapKey(long aSessionID,CK_MECHANISM aMechanism,String aWrapperKeyLabel,String keyLabel) throws PKCS11Exception,SmartCardException;
	byte[] wrapKey(long aSessionID,CK_MECHANISM aMechanism, KeyTemplate wrapperKeyFacade, KeyTemplate keyFacade) throws PKCS11Exception,SmartCardException;
	long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, String unwrapperKeyLabel, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate) throws PKCS11Exception, SmartCardException;
	long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, KeyTemplate unwrapperKeyTemplate, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate) throws PKCS11Exception, SmartCardException;

    long[] importKeyPair(long sessionID, KeyPairTemplate template) throws PKCS11Exception, SmartCardException;
    KeySpec createKeyPair(long aSessionID, KeyPairTemplate template) throws PKCS11Exception,IOException,SmartCardException;
    long createSecretKey(long sessionID, SecretKeyTemplate template) throws PKCS11Exception, SmartCardException;

    long unwrapKey(long aSessionID, CK_MECHANISM aMechanism, byte[] certSerialNumber, byte[] aWrappedKey, KeyTemplate aUnwrappedKeyTemplate)
	throws PKCS11Exception,SmartCardException;

    int deleteCertificate(long aSessionID,String aKeyLabel)
    throws PKCS11Exception;

    String[] getWrapperKeyLabels(long aSessionID) throws PKCS11Exception, SmartCardException;
    String[] getUnwrapperKeyLabels(long aSessionID) throws PKCS11Exception, SmartCardException;

	public WrappedObjectsWithAttributes wrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, long[] objectIDs)
			throws PKCS11Exception, SmartCardException;

	public UnwrapObjectsResults unwrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, byte[] wrappedBytes)
			throws PKCS11Exception, ESYAException;

	CK_ATTRIBUTE[] getAllAttributes(long sessionID, long objectID) throws PKCS11Exception;

	long deriveKey(long sessionId, CK_MECHANISM derive_mechanism, long privateKeyHandle, KeyTemplate unwrappedKeyTemplate) throws PKCS11Exception;
}
