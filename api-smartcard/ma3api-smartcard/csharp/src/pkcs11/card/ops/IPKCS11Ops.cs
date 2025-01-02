using System;
using System.Collections.Generic;

using iaik.pkcs.pkcs11.wrapper;
//using Net.Pkcs11Interop.Common;
//using Net.Pkcs11Interop.LowLevelAPI41;

using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.modifications;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.src.api.asn.algorithms;


namespace tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops
{
    public interface IPKCS11Ops
    {
        long[] getTokenPresentSlotList();
        void initialize();
        long[] getSlotList();
        CK_SLOT_INFO getSlotInfo(long aSlotID);
        bool isTokenPresent(long aSlotID);
        CK_TOKEN_INFO getTokenInfo(long aSlotID);
        long[] getMechanismList(long aSlotID);
        CK_SESSION_INFO getSessionInfo(long aSessionID);
        long openSession(long aSlotID);
        void closeSession(long aSessionID);
        void login(long aSessionID, String aCardPIN);
        void logout(long aSessionID);
        bool isAnyObjectExist(long aSessionID);
        void importCertificate(long aSessionID, String aCertLabel, ECertificate aSertifika);
        long[] createRSAKeyPair(long aSessionID, String aKeyLabel, int aModulusBits, bool aIsSign, bool aIsEncrypt);
        ESubjectPublicKeyInfo createECKeyPair(long aSessionID, String aKeyLabel, EECParameters ecParameters, bool aIsSign, bool aIsEncrypt);
        byte[] signDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM aMechanism, byte[] aImzalanacak);
        byte[] signDataWithKeyID(long aSessionID, long aKeyID, CK_MECHANISM aMechanism, byte[] aImzalanacak);

        byte[] decryptDataWithCertSerialNo(long aSessionID, byte[] aSerialNumber, CK_MECHANISM mech, byte[] aData);

        List<byte[]> getCertificates(long aSessionID);
        List<byte[]> getSignatureCertificates(long aSessionID);
        List<byte[]> getEncryptionCertificates(long aSessionID);

        long getPrivateKeyObjIDFromCertificateSerial(long aSessionID, byte[] aCertSerialNo);
        long getObjIDFromPrivateKeyLabel(long aSessionID, String aLabel);
        byte[] getModulusOfKey(long aSessionID, long aObjID);


        String[] getSignatureKeyLabels(long aSessionID);
        String[] getEncryptionKeyLabels(long aSessionID);

        bool isObjectExist(long aSessionID, String aLabel);
        void writePrivateData(long aSessionID, String aLabel, byte[] aData);
        void writePublicData(long aSessionID, String aLabel, byte[] aData);
        List<byte[]> readPrivateData(long aSessionID, String aLabel);
        List<byte[]> readPublicData(long aSessionID, String aLabel);
        List<byte[]> readCertificate(long aSessionID, String aLabel);
        byte[] readCertificate(long aSessionID, byte[] aCertSerialNo);
        //todo bu iki metodun geri donus degerlerine karar verilmeli
        /*PublicKey*/
        /*ESubjectPublicKeyInfo*/
        ERSAPublicKey readRSAPublicKey(long aSessionID, String aLabel);
        /*PublicKey*/
        //ESubjectPublicKeyInfo readECPublicKey(long aSessionID, String aLabel);
        void updatePrivateData(long aSessionID, String aLabel, byte[] aValue);
        void updatePublicData(long aSessionID, String aLabel, byte[] aValue);
        int deleteCertificate(long aSessionID, String aKeyLabel);
        void deletePrivateObject(long aSessionID, String aLabel);
        void deletePublicObject(long aSessionID, String aLabel);
        void deletePrivateData(long aSessionID, String aLabel);
        void deletePublicData(long aSessionID, String aLabel);
        byte[] getRandomData(long aSessionID, int aDataLength);
        byte[] getTokenSerialNo(long aSlotID);
        byte[] signData(long aSessionID, String aKeyLabel, byte[] aImzalanacak, CK_MECHANISM aMechanism);
        void verifyData(long aSessionID, String aKeyLabel, byte[] aData, byte[] aImza, CK_MECHANISM aMechanism);
        byte[] encryptData(long aSessionID, String aKeyLabel, byte[] aData, CK_MECHANISM aMechanism);
        byte[] decryptData(long aSessionID, String aKeyLabel, byte[] aData, CK_MECHANISM aMechanism);

        void importCertificateAndKey(long aSessionID, String aCertLabel, String aKeyLabel, /*PrivateKey*/
                                                                                           /*AsymmetricKeyParameter*/EPrivateKeyInfo aPrivKeyInfo, ECertificate aCert);

        long[] importKeyPair(long aSessionID, String aLabel, ESubjectPublicKeyInfo aPubKeyInfo, EPrivateKeyInfo aPriKeyInfo, EECParameters ecParameters, byte[] aSubject, bool aIsSign, bool aIsEncrypt);
        long[] importRSAKeyPair(long aSessionID, String aLabel, EPrivateKeyInfo aPrivKeyInfo, byte[] aSubject, bool aIsSign, bool aIsEncrypt);

        long[] importECKeyPair(long aSessionID, String aLabel, ESubjectPublicKeyInfo aPubKeyInfo, EPrivateKeyInfo aPriKeyInfo, EECParameters ecParameters, byte[] aSubject, bool aIsSign, bool aIsEncrypt);

        void changePassword(String aOldPass, String aNewPass, long aSessionID);
        void formatToken(String aSOpin, String aNewPIN, String aLabel, int slotID);
        void setSOPin(byte[] aSOPin, byte[] aNewSOPin, long aSessionHandle);
        void changeUserPin(byte[] aSOPin, byte[] aUserPin, long aSessionHandle);
        bool setContainer(byte[] aContainerLabel, long aSessionHandle);

        bool importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,
                                            byte[] aPbCertData, int aSignOrEnc);

        bool importCertificateAndKeyWithCSP(byte[] aAnahtarCifti, int aAnahtarLen, String aScfname, String aContextName,
                                            ECertificate aPbCertificate, int aSignOrEnc);

        bool isPrivateKeyExist(long aSessionID, String aLabel);
        bool isPublicKeyExist(long aSessionID, String aLabel);
        bool isCertificateExist(long aSessionID, String aLabel);

        /**
	 * Returns private key in encoded Asn1 format.
	 * @param keySize
	 * @return
	 * @throws ESYAException
	 */
        byte[] generateRSAPrivateKey(int keySize);
        long[] objeAra(long aSessionID, CK_ATTRIBUTE[] aTemplate);
        void getAttributeValue(long aSessionID, long aObjectID, CK_ATTRIBUTE[] aTemplate);

        byte[] wrapKey(long aSessionID, CK_MECHANISM aMechanism, String wrapperKeyLabel, String aKeyLabel);

        long importSecretKey(long aSessionId, SecretKeyTemplate aKey);
        long createSecretKey(long sessionID, SecretKeyTemplate template);

    }
}