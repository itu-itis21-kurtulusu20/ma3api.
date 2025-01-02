package tr.gov.tubitak.uekae.esya.api.mkencryptedpackage;

import com.objsys.asn1j.runtime.Asn1BerInputStream;
import com.objsys.asn1j.runtime.Asn1BerOutputStream;
import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1Tag;
import com.objsys.asn1j.runtime.Asn1OctetString;
import com.objsys.asn1j.runtime.Asn1Status;
import com.objsys.asn1j.runtime.Asn1Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.pkcs11.wrapper.CK_GCM_PARAMS;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.EAsnUtil;
import tr.gov.tubitak.uekae.esya.api.asn.scencryptedpackage.EEncryptedDataPackage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.PKCS11Ops;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.HSMSessionPool;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.ObjectSessionInfo;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ConstantsUtil;
import tr.gov.tubitak.uekae.esya.asn.scencryptedpackage.EncryptedDataPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class EncryptedDataPackageGenerator {

    private static Logger logger = LoggerFactory.getLogger(EncryptedDataPackageGenerator.class);

    HSMSessionPool hsmSessionPool;
    ISmartCard smartCard;
    EncryptedPackageConfig encryptedPackageConfig;

    public EncryptedDataPackageGenerator(HSMSessionPool hsmSessionPool, EncryptedPackageConfig encryptedPackageConfig) throws IOException, ESYAException, PKCS11Exception {
        this.hsmSessionPool = hsmSessionPool;
        this.smartCard = new SmartCard(hsmSessionPool.getCardType());
        this.encryptedPackageConfig = encryptedPackageConfig;

        if (encryptedPackageConfig.getDataCipherAlg().getMod() != Mod.GCM)
            throw new ESYAException("Only GCM Mode Supported");
    }

    public byte[] encrypt(byte[] toBeEncryptedData, String masterKeyLabel) throws Exception {
        ObjectSessionInfo objectSessionInfo = null;
        byte[] iv, encryptedData, wrappedKey;
        long encryptionSecretKeyID = 0;
        try {
            objectSessionInfo = hsmSessionPool.checkOutItem(masterKeyLabel);
            long session = objectSessionInfo.getSession();

            encryptionSecretKeyID = this.createSecretKey(session);
            iv = this.generateIV(session, encryptedPackageConfig.getIvLength());
            encryptedData = this.encryptData(session, toBeEncryptedData, iv, encryptionSecretKeyID);
            wrappedKey = this.wrapKey(session, objectSessionInfo.getObjectId(), encryptionSecretKeyID);
        } catch (Exception ex) {
            //Session kapandığı için encryptionSecretKeyID session bazlı olduğu için silinmesi bekleniyor.
            objectSessionInfo = hsmSessionPool.resetFailedSession(objectSessionInfo, masterKeyLabel);
            long session = objectSessionInfo.getSession();

            encryptionSecretKeyID = this.createSecretKey(session);
            iv = this.generateIV(session, encryptedPackageConfig.getIvLength());
            encryptedData = this.encryptData(session, toBeEncryptedData, iv, encryptionSecretKeyID);
            wrappedKey = this.wrapKey(session, objectSessionInfo.getObjectId(), encryptionSecretKeyID);
        } finally {
            tryDeleteTempKey(objectSessionInfo.getSession(), encryptionSecretKeyID);
            hsmSessionPool.offer(objectSessionInfo);
        }

        EEncryptedDataPackage encryptedDataPackage = new EEncryptedDataPackage(encryptedPackageConfig.getVersion(), wrappedKey, iv, encryptedData);
        byte[] encryptedDataPackageEncoded = encryptedDataPackage.getEncoded();

        return encryptedDataPackageEncoded;
    }

    public void encrypt(InputStream is, OutputStream ous, String masterKeyLabel) throws Exception {
        ObjectSessionInfo objectSessionInfo = null;
        byte[] iv, wrappedKey;
        long session, encryptionSecretKeyID = 0;
        try {
            objectSessionInfo = hsmSessionPool.checkOutItem(masterKeyLabel);
            session = objectSessionInfo.getSession();

            encryptionSecretKeyID = this.createSecretKey(session);
            iv = this.generateIV(session, encryptedPackageConfig.getIvLength());
            wrappedKey = this.wrapKey(session, objectSessionInfo.getObjectId(), encryptionSecretKeyID);
        } catch (Exception ex) {
            try {
                //Session kapandığı için encryptionSecretKeyID session bazlı olduğu için silinmesi bekleniyor.
                objectSessionInfo = hsmSessionPool.resetFailedSession(objectSessionInfo, masterKeyLabel);
                session = objectSessionInfo.getSession();

                encryptionSecretKeyID = this.createSecretKey(session);
                iv = this.generateIV(session, encryptedPackageConfig.getIvLength());
                wrappedKey = this.wrapKey(session, objectSessionInfo.getObjectId(), encryptionSecretKeyID);
            } catch (Exception ex1) {
                tryDeleteTempKey(objectSessionInfo.getSession(), encryptionSecretKeyID);
                hsmSessionPool.offer(objectSessionInfo);
                throw ex1;
            }
        }

        EncryptedDataPackage dataPackage = new EncryptedDataPackage();
        dataPackage.version = new Asn1Integer(encryptedPackageConfig.getVersion());
        dataPackage.wrappedKey = new Asn1OctetString(wrappedKey);
        dataPackage.iv = new Asn1OctetString(iv);

        try {
            Asn1BerOutputStream encryptedOutputStream = new Asn1BerOutputStream(ous);
            encryptedOutputStream.encodeTagAndIndefLen(Asn1Tag.SEQUENCE);
            encryptedOutputStream.encodeTag(Asn1Tag.CTXT, Asn1Tag.PRIM, 0);
            encryptedOutputStream.encodeIntValue(encryptedPackageConfig.getVersion(), true);
            encryptedOutputStream.encodeTag(Asn1Tag.CTXT, Asn1Tag.PRIM, 1);
            encryptedOutputStream.encode(dataPackage.wrappedKey, false);
            encryptedOutputStream.encodeTag(Asn1Tag.CTXT, Asn1Tag.PRIM, 2);
            encryptedOutputStream.encode(dataPackage.iv, false);

            // -- CONTENT ENCRYPTION
            encryptedOutputStream.encodeTagAndIndefLen(Asn1Tag.CTXT, Asn1Tag.CONS, 4);

            encryptDataAndWriteStream(session, is, encryptedOutputStream, iv, encryptionSecretKeyID);

            encryptedOutputStream.encodeTagAndLength(Asn1Tag.EOC, 0);
            // -- CONTENT ENCRYPTION

            encryptedOutputStream.encodeTagAndLength(Asn1Tag.EOC, 0);

            encryptedOutputStream.close();
            is.close();
            ous.close();
        } finally {
            tryDeleteTempKey(objectSessionInfo.getSession(), encryptionSecretKeyID);
            hsmSessionPool.offer(objectSessionInfo);
        }
    }

    public byte[] decrypt(byte[] encryptedDataPackage, String masterKeyLabel) throws Exception {
        EEncryptedDataPackage encDataPackage = new EEncryptedDataPackage(encryptedDataPackage);
        byte[] wrappedKey = encDataPackage.getWrappedKey();

        long decryptionKeyID = 0;
        ObjectSessionInfo objectSessionInfo = null;
        byte[] decryptedData;

        try {
            objectSessionInfo = hsmSessionPool.checkOutItem(masterKeyLabel);
            decryptionKeyID = unwrap(objectSessionInfo, encryptedPackageConfig.getKeyWrapAlg(), wrappedKey);
            decryptedData = decryptData(objectSessionInfo.getSession(), encDataPackage.getEncryptedData(), encDataPackage.getIv(), decryptionKeyID);
        } catch (Exception ex) {
            //Session kapandığı için encryptionSecretKeyID session bazlı olduğu için silinmesi bekleniyor.
            objectSessionInfo = hsmSessionPool.resetFailedSession(objectSessionInfo, masterKeyLabel);
            decryptionKeyID = unwrap(objectSessionInfo, encryptedPackageConfig.getKeyWrapAlg(), wrappedKey);
            decryptedData = decryptData(objectSessionInfo.getSession(), encDataPackage.getEncryptedData(), encDataPackage.getIv(), decryptionKeyID);
        } finally {
            tryDeleteTempKey(objectSessionInfo.getSession(), decryptionKeyID);
            hsmSessionPool.offer(objectSessionInfo);
        }

        return decryptedData;
    }

    public void decrypt(InputStream is, OutputStream ous, String masterKeyLabel) throws Exception {
        Asn1BerInputStream encryptedInputStream = new Asn1BerInputStream(is);

        int encryptedDataPackageLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(encryptedInputStream, Asn1Tag.SEQUENCE);

        Asn1Tag primitive = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 0);

        int versionLength = encryptedInputStream.decodeTagAndLength(primitive);
        encryptedInputStream.decodeIntValue(versionLength, false);

        int wrappedKeyLength = encryptedInputStream.decodeTagAndLength(primitive);
        byte[] wrappedKey = new byte[wrappedKeyLength];
        encryptedInputStream.read(wrappedKey);

        int ivLength = encryptedInputStream.decodeTagAndLength(primitive);
        byte[] iv = new byte[ivLength];
        encryptedInputStream.read(iv);

        long decryptionKeyID = 0;
        ObjectSessionInfo objectSessionInfo = null;

        try {
            objectSessionInfo = hsmSessionPool.checkOutItem(masterKeyLabel);
            decryptionKeyID = unwrap(objectSessionInfo, encryptedPackageConfig.getKeyWrapAlg(), wrappedKey);
        } catch (Exception ex) {
            try {
                //Session kapandığı için encryptionSecretKeyID session bazlı olduğu için silinmesi bekleniyor.
                objectSessionInfo = hsmSessionPool.resetFailedSession(objectSessionInfo, masterKeyLabel);
                decryptionKeyID = unwrap(objectSessionInfo, encryptedPackageConfig.getKeyWrapAlg(), wrappedKey);
            } catch (Exception ex1) {
                tryDeleteTempKey(objectSessionInfo.getSession(), decryptionKeyID);
                hsmSessionPool.offer(objectSessionInfo);
                throw ex1;
            }
        }

        try {
            decryptDataAndWriteStream(objectSessionInfo.getSession(), encryptedInputStream, ous, iv, decryptionKeyID);
        } finally {
            tryDeleteTempKey(objectSessionInfo.getSession(), decryptionKeyID);
            hsmSessionPool.offer(objectSessionInfo);
        }
    }

    private byte[] decryptData(long sessionID, byte[] encryptedData, byte[] iv, long decryptionKeyID) throws ESYAException, PKCS11Exception {
        long pkcs11Mechanism = ConstantsUtil.convertSymmetricAlgToPKCS11Constant(encryptedPackageConfig.getDataCipherAlg().getName());
        CK_GCM_PARAMS gcmParams = new CK_GCM_PARAMS(encryptedPackageConfig.getTagLength(), iv, null);

        CK_MECHANISM decryption_ck_mechanism = new CK_MECHANISM(pkcs11Mechanism, gcmParams);

        byte[] decryptedData = this.smartCard.decryptData(sessionID, decryptionKeyID, encryptedData, decryption_ck_mechanism);

        return decryptedData;
    }

    private void decryptDataAndWriteStream(long sessionID, Asn1BerInputStream encryptedInputStream, OutputStream ous, byte[] iv, long decryptionKeyID) throws ESYAException, IOException, PKCS11Exception {
        long pkcs11Mechanism = ConstantsUtil.convertSymmetricAlgToPKCS11Constant(encryptedPackageConfig.getDataCipherAlg().getName());
        CK_GCM_PARAMS gcmParams = new CK_GCM_PARAMS(encryptedPackageConfig.getTagLength(), iv, null);

        CK_MECHANISM decryption_ck_mechanism = new CK_MECHANISM(pkcs11Mechanism, gcmParams);

        PKCS11Ops pkcs11Ops = ((PKCS11Ops) this.smartCard.getCardType().getCardTemplate().getPKCS11Ops());
        PKCS11 pkcs11 = pkcs11Ops.getmPKCS11();

        pkcs11.C_DecryptInit(sessionID, decryption_ck_mechanism, decryptionKeyID);

        byte[] encrypted;
        byte[] decrypted = null;

        Asn1Tag construct = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 4);

        int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(encryptedInputStream, construct);

        if (len == Asn1Status.INDEFLEN) {// asn1 stream ile oluşturulmuş
            Asn1Tag tag = encryptedInputStream.peekTag();

            if (tag == null || !tag.equals(Asn1OctetString.TAG))
                throw new CryptoException("Encrypted data is missing");

            while (tag != null && tag.equals(Asn1OctetString.TAG)) {
                len = EAsnUtil.decodeTagAndLengthWithCheckingTag(encryptedInputStream, Asn1OctetString.TAG);

                encrypted = new byte[len];
                decrypted = new byte[len + 128]; // Output, işleme giren datadan uzun olabiliyor.

                encryptedInputStream.read(encrypted);

                int decryptedLen = pkcs11.C_DecryptUpdate(sessionID, 0, encrypted, 0, len, 0, decrypted, 0, decrypted.length);
                if (decryptedLen > 0)
                    ous.write(decrypted, 0, decryptedLen);

                tag = encryptedInputStream.peekTag();
            }

            int decryptedLen = pkcs11.C_DecryptFinal(sessionID, 0, decrypted, 0, decrypted.length);
            ous.write(decrypted, 0, decryptedLen);
        } else {// hazır asn1 ile oluşturulmuş

            int maxChunkSize = pkcs11Ops.getMaxChunkSize();

            int loopCount = len / maxChunkSize;

            byte[] buffer;
            if (loopCount == 0) {
                decrypted = new byte[len + 128];
                buffer = new byte[len];
            } else {
                decrypted = new byte[maxChunkSize + 128];
                buffer = new byte[maxChunkSize];
            }

            do {
                encryptedInputStream.read(buffer);
                int decryptedLen = pkcs11.C_DecryptUpdate(sessionID, 0, buffer, 0, len, 0, decrypted, 0, decrypted.length);
                if (decryptedLen > 0)
                    ous.write(decrypted, 0, decryptedLen);

                loopCount--;
            } while (loopCount > 0);

            int decryptedLen = pkcs11.C_DecryptFinal(sessionID, 0, decrypted, 0, decrypted.length);
            ous.write(decrypted, 0, decryptedLen);
        }

    }

    private long unwrap(ObjectSessionInfo objectSessionInfo, WrapAlg wrapAlg, byte[] wrappedKey) throws ESYAException, PKCS11Exception {
        long pkcs11Mechanism = ConstantsUtil.convertWrapAlgToPKCS11Constant(wrapAlg.getName());
        CK_MECHANISM unwrapMech = ConstantsUtil.addParamsToMech(pkcs11Mechanism, null);

        KeyTemplate unwrappedKeyTemplate = new AESKeyTemplate("unwrappedKey");
        unwrappedKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, false));
        unwrappedKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));

        long decryptionKeyID = smartCard.unwrapKey(objectSessionInfo.getSession(), unwrapMech, objectSessionInfo.getObjectId(), wrappedKey, unwrappedKeyTemplate);

        return decryptionKeyID;
    }

    private long createSecretKey(long sessionID) throws PKCS11Exception, SmartCardException {
        int keyLength = KeyUtil.getKeyLength(encryptedPackageConfig.getDataCipherAlg()) / 8;
        String keyLabel = "encryptionKey-" + StringUtil.toHexString(RandomUtil.generateRandom(4));
        AESKeyTemplate encryptionKeyTemplate = new AESKeyTemplate(keyLabel, keyLength);
        encryptionKeyTemplate.getAsTokenTemplate(false, true, false);
        encryptionKeyTemplate.getAsExportableTemplate();

        long encryptionKeyID = this.smartCard.createSecretKey(sessionID, encryptionKeyTemplate);

        return encryptionKeyID;
    }

    private void tryDeleteTempKey(long sessionID, long keyID) throws PKCS11Exception {
        if (keyID != 0) {
            try {
                PKCS11 pkcs11 = ((PKCS11Ops) this.smartCard.getCardType().getCardTemplate().getPKCS11Ops()).getmPKCS11();
                pkcs11.C_DestroyObject(sessionID, keyID);
            } catch (Exception ex) {
                logger.error("Can not delete key", ex);
            }
        }
    }

    private byte[] generateIV(long sessionID, int ivLengthInBits) throws PKCS11Exception {
        if (ivLengthInBits > 0) {
            int ivLengthInByte = ivLengthInBits / 8;
            return this.smartCard.getRandomData(sessionID, ivLengthInByte);
        }
        return null;
    }

    private byte[] encryptData(long sessionID, byte[] toBeEncryptedData, byte[] iv, long encryptionKeyID) throws ESYAException, PKCS11Exception {
        long pkcs11EncryptMechanism = ConstantsUtil.convertSymmetricAlgToPKCS11Constant(encryptedPackageConfig.getDataCipherAlg().getName());
        CK_GCM_PARAMS gcmParams = new CK_GCM_PARAMS(encryptedPackageConfig.getTagLength(), iv, null);

        CK_MECHANISM encryption_ck_mechanism = new CK_MECHANISM(pkcs11EncryptMechanism, gcmParams);

        byte[] encryptedData = this.smartCard.encryptData(sessionID, encryptionKeyID, toBeEncryptedData, encryption_ck_mechanism);

        return encryptedData;
    }

    private byte[] wrapKey(long sessionID, long masterKeyID, long wrappingKeyID) throws ESYAException, PKCS11Exception {
        long pkcs11WrapMechanism = ConstantsUtil.convertWrapAlgToPKCS11Constant(encryptedPackageConfig.getKeyWrapAlg().getName());
        CK_MECHANISM wrap_ck_mechanism = ConstantsUtil.addParamsToMech(pkcs11WrapMechanism, null);

        byte[] wrappedKey = this.smartCard.wrapKey(sessionID, wrap_ck_mechanism, masterKeyID, wrappingKeyID);

        return wrappedKey;
    }

    private void writeOctetString(OutputStream ous, byte[] content) throws Asn1Exception, IOException {
        Asn1BerOutputStream asn1BerOutputStream = (Asn1BerOutputStream) ous;
        asn1BerOutputStream.encodeOctetString(content, true, Asn1OctetString.TAG);
    }

    private void encryptDataAndWriteStream(long sessionID, InputStream is, OutputStream ous, byte[] iv, long encryptionKeyID) throws ESYAException, PKCS11Exception, IOException {
        long pkcs11EncryptMechanism = ConstantsUtil.convertSymmetricAlgToPKCS11Constant(encryptedPackageConfig.getDataCipherAlg().getName());
        CK_GCM_PARAMS gcmParams = new CK_GCM_PARAMS(encryptedPackageConfig.getTagLength(), iv, null);

        CK_MECHANISM encryption_ck_mechanism = new CK_MECHANISM(pkcs11EncryptMechanism, gcmParams);

        PKCS11Ops pkcs11Ops = ((PKCS11Ops) this.smartCard.getCardType().getCardTemplate().getPKCS11Ops());
        PKCS11 pkcs11 = pkcs11Ops.getmPKCS11();

        pkcs11.C_EncryptInit(sessionID, encryption_ck_mechanism, encryptionKeyID);

        final int max_chunk_size = pkcs11Ops.getMaxChunkSize();
        byte[] inputBuf = new byte[max_chunk_size];
        byte[] outputBuf = new byte[max_chunk_size + 128]; // Output, işleme giren data'tan uzun olabiliyor.

        int encryptedLen;
        int readLen;

        // encrypt
        do {
            readLen = is.read(inputBuf, 0, max_chunk_size);
            if (readLen == -1)
                break;

            encryptedLen = pkcs11.C_EncryptUpdate(sessionID, 0, inputBuf, 0, readLen, 0, outputBuf, 0, outputBuf.length);
            if (encryptedLen != 0) {
                byte[] encryptedData;
                if (encryptedLen == outputBuf.length)
                    encryptedData = outputBuf;
                else
                    encryptedData = Arrays.copyOfRange(outputBuf, 0, encryptedLen);
                writeOctetString(ous, encryptedData);
            }
        } while (readLen > 0);

        // finalize
        encryptedLen = pkcs11.C_EncryptFinal(sessionID, 0, outputBuf, 0, outputBuf.length);
        byte[] encryptedData = Arrays.copyOfRange(outputBuf, 0, encryptedLen);
        writeOctetString(ous, encryptedData);

    }
}
