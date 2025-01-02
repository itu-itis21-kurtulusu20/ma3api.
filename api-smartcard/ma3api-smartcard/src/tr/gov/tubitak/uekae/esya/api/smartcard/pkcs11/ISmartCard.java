package tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;

import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.UnwrapObjectsResults;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.WrappedObjectsWithAttributes;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.SecretKey;

import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.Map;

/**
 * <b>Author</b>    : zeldal.ozdemir <br>
 * <b>Project</b>   : MA3   <br>
 * <b>Copyright</b> : TUBITAK Copyright (c) 2006-2012 <br>
 * <b>Date</b>: 11/7/12 - 6:18 PM <p>
 * <b>Description</b>: <br>
 */
public interface ISmartCard {

    long[] getTokenPresentSlotList()
   throws PKCS11Exception;

    List<Pair<Long,String>> getTokenPresentSlotListWithDescription() throws PKCS11Exception;

    long[] getSlotList()
   throws PKCS11Exception;

    CK_SLOT_INFO getSlotInfo(long aSlotID)
   throws PKCS11Exception;

    CK_SESSION_INFO getSessionInfo(long aSessionID)
   throws PKCS11Exception;

    boolean isTokenPresent(long aSlotID)
   throws PKCS11Exception;

    CK_TOKEN_INFO getTokenInfo(long aSlotID)
   throws PKCS11Exception;

    long[] getMechanismList(long aSlotID)
   throws PKCS11Exception;

    long openSession(long aSlotID)
   throws PKCS11Exception;

    void closeSession(long aSessionID)
   throws PKCS11Exception;

    void login(long aSessionID, String aCardPIN)
   throws PKCS11Exception;

    void logout(long aSessionID)
   throws PKCS11Exception;

    boolean isAnyObjectExist(long aSessionID)
   throws PKCS11Exception;

    void importCertificate(long aSessionID, String aCertLabel, X509Certificate aSertifika)
   throws PKCS11Exception;

    long[] createKeyPair(long aSessionID, String aKeyLabel, AlgorithmParameterSpec aParamSpec, boolean aIsSign, boolean aIsEncrypt)
   throws PKCS11Exception,SmartCardException,IOException;

    KeySpec createKeyPair(long aSessionID, KeyPairTemplate template) throws PKCS11Exception,SmartCardException,IOException;

    byte [] generateRSAPrivateKey(long aSessionID, int keySize) throws ESYAException;

    KeyPair generateECKeyPair(long aSessionID, ECParameterSpec ecParameterSpec) throws ESYAException;

    @Deprecated
    byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, long aMechanism, byte[] aToBeSigned)
	throws PKCS11Exception,SmartCardException;

    byte[] signDataWithKeyID(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, byte[] aToBeSigned)
			throws PKCS11Exception,SmartCardException;

    byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aToBeSigned)
            throws PKCS11Exception,SmartCardException;

    @Deprecated
    byte[] decryptDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, long aMechanism, byte[] aData)
            throws PKCS11Exception,SmartCardException;

    byte[] decryptDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aData)
            throws PKCS11Exception,SmartCardException;

    List<byte[]> getCertificates(long aSessionID)
            throws PKCS11Exception,SmartCardException;

    List<byte[]> getSignatureCertificates(long aSessionID)
            throws PKCS11Exception,SmartCardException;

    List<byte[]> getEncryptionCertificates(long aSessionID)
            throws PKCS11Exception,SmartCardException;

    String [] getSignatureKeyLabels(long aSessionID)
            throws PKCS11Exception,SmartCardException;

    long getPrivateKeyObjIDFromCertificateSerial(long aSessionID, byte[] aCertSerialNo)
            throws SmartCardException,PKCS11Exception;

    long getPrivateKeyObjIDFromPrivateKeyLabel(long aSessionID, String aLabel)
            throws SmartCardException,PKCS11Exception;

    long getPublicKeyObjIDFromPublicKeyLabel(long aSessionID, String aLabel)
            throws SmartCardException,PKCS11Exception;

    String [] getEncryptionKeyLabels(long aSessionID)
            throws PKCS11Exception,SmartCardException;

    boolean isObjectExist(long aSessionID, String aLabel)
            throws PKCS11Exception;

    void writePrivateData(long aSessionID, String aLabel, byte[] aData)
            throws PKCS11Exception;

    void writePublicData(long aSessionID, String aLabel, byte[] aData)
            throws PKCS11Exception;

    List<byte[]> readPrivateData(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException;

    List<byte[]> readPublicData(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException;

    boolean isPublicKeyExist(long aSessionID, String aLabel)
            throws PKCS11Exception;

    boolean isPrivateKeyExist(long aSessionID, String aLabel)
            throws PKCS11Exception;

    boolean isCertificateExist(long aSessionID, String aLabel)
            throws PKCS11Exception;

    List<byte[]> readCertificate(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException;

    byte[] readCertificate(long aSessionID, byte[] aCertSerialNo)
            throws PKCS11Exception,SmartCardException;

    KeySpec readPublicKeySpec(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException;

    KeySpec readPublicKeySpec(long aSessionID, byte[] aCertSerialNo)
            throws PKCS11Exception,SmartCardException;

    void updatePrivateData(long aSessionID, String aLabel, byte[] aValue)
            throws PKCS11Exception,SmartCardException;

    void updatePublicData(long aSessionID, String aLabel, byte[] aValue)
            throws PKCS11Exception,SmartCardException;

    void deletePrivateObject(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException;

    void deleteObject(long sessionId, long objectHandle) throws PKCS11Exception;

    void deletePublicObject(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException;

    void deletePrivateData(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException;

    void deletePublicData(long aSessionID, String aLabel)
            throws PKCS11Exception,SmartCardException;

    byte[] getRandomData(long aSessionID, int aDataLength)
            throws PKCS11Exception;

    byte[] getTokenSerialNo(long aSlotID)
            throws PKCS11Exception;

    @Deprecated
    byte[] signData(long aSessionID, String aKeyLabel, byte[] aToBeSigned, long aMechanism)
            throws PKCS11Exception,SmartCardException;

    byte[] signData(long aSessionID, String aKeyLabel, byte[] aToBeSigned, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException;

    void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aSignature, long aMechanism)
            throws PKCS11Exception,SmartCardException;

    void verifyData(long aSessionID, long aKeyID, byte[] aData, byte[] aSignature, long aMechanism)
        throws PKCS11Exception, SmartCardException;

    void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aSignature, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException;

    void verifyData(long aSessionID, long aKeyID, byte[] aData, byte[] aSignature, CK_MECHANISM aMechanism)
        throws PKCS11Exception, SmartCardException;

    @Deprecated
    byte[] encryptData(long aSessionID, String aKeyLabel, byte[] aData, long aMechanism)
            throws PKCS11Exception,SmartCardException;

    byte[] encryptData(long aSessionID, String aKeyLabel, byte[] aData, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException;

    byte[] encryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism)
        throws PKCS11Exception, SmartCardException;

    void encryptData(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException;

    @Deprecated
    byte[] decryptData(long aSessionID, String aKeyLabel, byte[] aData, long aMechanism)
            throws PKCS11Exception,SmartCardException;

    byte[] decryptData(long aSessionID, String aKeyLabel, byte[] aData, CK_MECHANISM aMechanism)
            throws PKCS11Exception,SmartCardException;

    byte[] decryptData(long aSessionID, long aKeyID, byte[] aData, CK_MECHANISM aMechanism)
        throws PKCS11Exception, SmartCardException;

    void decryptData(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, InputStream inputStream, OutputStream outputStream) throws PKCS11Exception, SmartCardException, IOException;

    long[] importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, PrivateKey aPrivKey, X509Certificate aCert)
            throws PKCS11Exception,SmartCardException,IOException;

    @Deprecated
    long[] importKeyPair(long aSessionID, String aLabel, KeyPair aKeyPair, byte[] aSubject, boolean aIsSign, boolean aIsEncrypt)
            throws PKCS11Exception,SmartCardException,IOException;

    void changePassword(String aOldPass, String aNewPass, long aSessionID)
            throws PKCS11Exception;

    void formatToken(String aSOpin, String aNewPIN, String aLabel, int slotID)
            throws PKCS11Exception;

    void setSOPin(byte[] aSOPin, byte[] aNewSOPin, long aSessionHandle)
            throws PKCS11Exception;

    void changeUserPin(byte[] aSOPin, byte[] aUserPin, long aSessionHandle)
            throws PKCS11Exception;

    boolean setContainer(byte[] aContainerLabel, long aSessionHandle);

    boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName, X509Certificate aPbCertificate, int aSignOrEnc);

    boolean importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName, byte[] aPbCertificate, int aSignOrEnc);

    long createSecretKey(long aSessionID, SecretKey aKey)
            throws PKCS11Exception;

    long importSecretKey(long aSessionID, SecretKey aKey)
            throws PKCS11Exception;

    long[] objeAra(long aSessionID, CK_ATTRIBUTE[] aTemplate)
            throws PKCS11Exception;

    void getAttributeValue(long aSessionID, long aObjectID, CK_ATTRIBUTE[] aTemplate)
            throws PKCS11Exception;

    void changeLabel(long aSessionID, String aOldLabel, String aNewLabel)
            throws PKCS11Exception,SmartCardException;

    byte [] getModulusOfKey(long aSessionID, long aObjID) throws SmartCardException, PKCS11Exception;

    byte[] wrapKey(long sessionID, CK_MECHANISM mechanism, String wrapperKeyLabel, String labelOfKeyToWrap)
            throws PKCS11Exception,SmartCardException;

    byte[] wrapKey(long sessionID, CK_MECHANISM mechanism, long wrapperKeyID, long keyID)
            throws PKCS11Exception, SmartCardException;

    byte[] wrapKey(long sessionID, CK_MECHANISM mechanism, KeyTemplate wrapperKeyTemplate, KeyTemplate wrappingKeyTemplate)
            throws PKCS11Exception,SmartCardException;

    WrappedObjectsWithAttributes wrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, long[] objectIDs)
            throws PKCS11Exception, SmartCardException;

    long unwrapKey(long sessionID, CK_MECHANISM mechanism, String unwrapperKeyLabel, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate)
            throws PKCS11Exception,SmartCardException;

    long unwrapKey(long sessionID, CK_MECHANISM mechanism, long unwrapperKeyID, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate)
            throws PKCS11Exception,SmartCardException;

    long unwrapKey(long sessionID, CK_MECHANISM mechanism, KeyTemplate unwrapperKeyTemplate, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate)
            throws PKCS11Exception,SmartCardException;

    long unwrapKey(long sessionID, CK_MECHANISM mechanism, byte[] certSerialNumber, byte[] wrappedKey, KeyTemplate unwrappedKeyTemplate)
            throws PKCS11Exception, SmartCardException;

    UnwrapObjectsResults unwrapObjectsWithAttributes(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, byte[] wrappedBytes)
            throws PKCS11Exception, ESYAException;

    CardType getCardType();

    long[] importKeyPair(long sessionID, KeyPairTemplate template) throws PKCS11Exception, SmartCardException;

    long createSecretKey(long sessionID, SecretKeyTemplate template) throws PKCS11Exception, SmartCardException;

    boolean isSupportsWrapUnwrap(long sessionID);
    String [] getWrapperKeyLabels(long aSessionID) throws PKCS11Exception,SmartCardException;
    public String [] getUnwrapperKeyLabels(long aSessionID) throws PKCS11Exception,SmartCardException;

    CK_ATTRIBUTE[] getAllAttributes(long sessionID, long objectID) throws PKCS11Exception;
    Map<Long,String> getAttributeNames();

    long deriveKey(long sessionId, CK_MECHANISM derive_mechanism, long privateKeyHandle, KeyTemplate unwrappedKeyTemplate) throws PKCS11Exception;

    PKCS11Ops getPKCS11Ops();
}
