package dev.esya.api.mkencryptedpackage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.mkencryptedpackage.EncryptedDataPackageGenerator;
import tr.gov.tubitak.uekae.esya.api.mkencryptedpackage.EncryptedPackageConfig;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.HSMSessionPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class EncryptedDataPackageGeneratorTest {

    @Before
    public void createMasterKey() throws PKCS11Exception, IOException, SmartCardException {
        SmartCard smartCard = new SmartCard(CardType.DIRAKHSM);
        long sessionID = smartCard.openSession(90);
        smartCard.login(sessionID, "123456");

        AESKeyTemplate masterAESKeyTemplate = new AESKeyTemplate("masterKey", 32);
        masterAESKeyTemplate.getAsTokenTemplate(false, false, true);

        smartCard.createSecretKey(sessionID, masterAESKeyTemplate);

        smartCard.logout(sessionID);
        smartCard.closeSession(sessionID);
    }

    @After
    public void deleteMasterKey() throws PKCS11Exception, IOException, SmartCardException {
        SmartCard smartCard = new SmartCard(CardType.DIRAKHSM);
        long sessionID = smartCard.openSession(90);
        smartCard.login(sessionID, "123456");

        smartCard.deletePrivateObject(sessionID, "masterKey");

        smartCard.logout(sessionID);
        smartCard.closeSession(sessionID);
    }

    @Test
    public void byteEncryptionByteDecryptionTest() throws Exception {
        // --- GENERATING TO BE ENCRYPTED DATA
        int randomDataLen = new Random().nextInt(200) + 1;
        byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
        // --- GENERATING TO BE ENCRYPTED DATA

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, EncryptedPackageConfig.VERSION_ONE);
        // --- CREATING THE NEEDED CLASS

        // --- ENCRYPTION AND DECRYPTION
        byte[] encryptedData = encryptedDataPackageGenerator.encrypt(toBeEncrypted, "masterKey");
        byte[] decryptedData = encryptedDataPackageGenerator.decrypt(encryptedData, "masterKey");
        // --- ENCRYPTION AND DECRYPTION

        // --- END POOL
       hsmSessionPool.endPool();
        // --- END POOL

        // --- DATA COMPARISON
        Assert.assertArrayEquals(toBeEncrypted, decryptedData);
    }

    @Test
    public void byteEncryptionStreamDecryptionTest() throws Exception {
        // --- GENERATING TO BE ENCRYPTED DATA
        int randomDataLen = new Random().nextInt(200) + 1;
        byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
        // --- GENERATING TO BE ENCRYPTED DATA

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, EncryptedPackageConfig.VERSION_ONE);
        // --- CREATING THE NEEDED CLASS

        // --- ENCRYPTION AND DECRYPTION
        byte[] encryptedData = encryptedDataPackageGenerator.encrypt(toBeEncrypted, "masterKey");

        InputStream decryptionInputStream = new ByteArrayInputStream(encryptedData);
        ByteArrayOutputStream decryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.decrypt(decryptionInputStream, decryptionOutputStream, "masterKey");

        byte[] decryptedData = decryptionOutputStream.toByteArray();
        // --- ENCRYPTION AND DECRYPTION

        // --- END POOL
        hsmSessionPool.endPool();
        // --- END POOL

        // --- DATA COMPARISON
        Assert.assertArrayEquals(toBeEncrypted, decryptedData);
    }

    @Test
    public void streamEncryptionStreamDecryptionTest() throws Exception {
        // --- GENERATING TO BE ENCRYPTED DATA
        int randomDataLen = new Random().nextInt(200) + 1;
        byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
        // --- GENERATING TO BE ENCRYPTED DATA

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, EncryptedPackageConfig.VERSION_ONE);
        // --- CREATING THE NEEDED CLASS

        // --- ENCRYPTION AND DECRYPTION
        InputStream encryptionInputStream = new ByteArrayInputStream(toBeEncrypted);
        ByteArrayOutputStream encryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.encrypt(encryptionInputStream, encryptionOutputStream, "masterKey");

        InputStream decryptionInputStream = new ByteArrayInputStream(encryptionOutputStream.toByteArray());
        ByteArrayOutputStream decryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.decrypt(decryptionInputStream, decryptionOutputStream, "masterKey");

        byte[] decryptedData = decryptionOutputStream.toByteArray();
        // --- ENCRYPTION AND DECRYPTION

        // --- END POOL
        hsmSessionPool.endPool();
        // --- END POOL

        // --- DATA COMPARISON
        Assert.assertArrayEquals(toBeEncrypted, decryptedData);
    }

    @Test
    public void streamEncryptionByteDecryption() throws Exception {
        // --- GENERATING TO BE ENCRYPTED DATA
        int randomDataLen = new Random().nextInt(200) + 1;
        byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
        // --- GENERATING TO BE ENCRYPTED DATA

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, EncryptedPackageConfig.VERSION_ONE);
        // --- CREATING THE NEEDED CLASS

        // --- ENCRYPTION AND DECRYPTION
        InputStream encryptionInputStream = new ByteArrayInputStream(toBeEncrypted);
        ByteArrayOutputStream encryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.encrypt(encryptionInputStream, encryptionOutputStream, "masterKey");

        byte[] encryptedData = encryptionOutputStream.toByteArray();

        byte[] decryptedData = encryptedDataPackageGenerator.decrypt(encryptedData, "masterKey");

        // --- END POOL
        hsmSessionPool.endPool();
        // --- END POOL

        // --- DATA COMPARISON
        Assert.assertArrayEquals(toBeEncrypted, decryptedData);
    }

    @Test
    public void streamEncryptionByteDecryption_WithDataGreaterThanMaxChunkSize() throws Exception {
        // --- GENERATING TO BE ENCRYPTED DATA
        int randomDataLen = new Random().nextInt(4465) + 65536;
        byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
        // --- GENERATING TO BE ENCRYPTED DATA

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, EncryptedPackageConfig.VERSION_ONE);
        // --- CREATING THE NEEDED CLASS

        // --- ENCRYPTION AND DECRYPTION
        InputStream encryptionInputStream = new ByteArrayInputStream(toBeEncrypted);
        ByteArrayOutputStream encryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.encrypt(encryptionInputStream, encryptionOutputStream, "masterKey");

        byte[] encryptedData = encryptionOutputStream.toByteArray();

        byte[] decryptedData = encryptedDataPackageGenerator.decrypt(encryptedData, "masterKey");

        // --- END POOL
        hsmSessionPool.endPool();
        // --- END POOL

        // --- DATA COMPARISON
        Assert.assertArrayEquals(toBeEncrypted, decryptedData);
    }

    @Test
    public void streamEncryptionStreamDecryption_WithDataGreaterThanMaxChunkSize() throws Exception {
        // --- GENERATING TO BE ENCRYPTED DATA
        int randomDataLen = new Random().nextInt(4465) + 65536;
        byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
        // --- GENERATING TO BE ENCRYPTED DATA

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, EncryptedPackageConfig.VERSION_ONE);
        // --- CREATING THE NEEDED CLASS

        // --- ENCRYPTION AND DECRYPTION
        InputStream encryptionInputStream = new ByteArrayInputStream(toBeEncrypted);
        ByteArrayOutputStream encryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.encrypt(encryptionInputStream, encryptionOutputStream, "masterKey");

        InputStream decryptionInputStream = new ByteArrayInputStream(encryptionOutputStream.toByteArray());
        ByteArrayOutputStream decryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.decrypt(decryptionInputStream, decryptionOutputStream, "masterKey");

        byte[] decryptedData = decryptionOutputStream.toByteArray();
        // --- ENCRYPTION AND DECRYPTION

        // --- END POOL
        hsmSessionPool.endPool();
        // --- END POOL

        // --- DATA COMPARISON
        Assert.assertArrayEquals(toBeEncrypted, decryptedData);
    }

    @Test
    public void byteEncryptionByteDecryption_WithDataGreaterThanMaxChunkSize() throws Exception {
        // --- GENERATING TO BE ENCRYPTED DATA
        int randomDataLen = new Random().nextInt(4465) + 65536;
        byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
        // --- GENERATING TO BE ENCRYPTED DATA

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, EncryptedPackageConfig.VERSION_ONE);
        // --- CREATING THE NEEDED CLASS

        // --- ENCRYPTION AND DECRYPTION
        byte[] encryptedData = encryptedDataPackageGenerator.encrypt(toBeEncrypted, "masterKey");
        byte[] decryptedData = encryptedDataPackageGenerator.decrypt(encryptedData, "masterKey");
        // --- ENCRYPTION AND DECRYPTION

        // --- END POOL
        hsmSessionPool.endPool();
        // --- END POOL

        // --- DATA COMPARISON
        Assert.assertArrayEquals(toBeEncrypted, decryptedData);
    }

    @Test
    public void streamEncryptionStreamDecryptionWithFourByteID() throws Exception {
        // --- GENERATING TO BE ENCRYPTED DATA
        int randomDataLen = new Random().nextInt(200) + 1;
        byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
        // --- GENERATING TO BE ENCRYPTED DATA

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedPackageConfig encryptedPackageConfig = new EncryptedPackageConfig(Integer.MAX_VALUE, CipherAlg.AES256_GCM, WrapAlg.AES256, 96, 96);
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, encryptedPackageConfig);
        // --- CREATING THE NEEDED CLASS

        // --- ENCRYPTION AND DECRYPTION
        InputStream encryptionInputStream = new ByteArrayInputStream(toBeEncrypted);
        ByteArrayOutputStream encryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.encrypt(encryptionInputStream, encryptionOutputStream, "masterKey");

        InputStream decryptionInputStream = new ByteArrayInputStream(encryptionOutputStream.toByteArray());
        ByteArrayOutputStream decryptionOutputStream = new ByteArrayOutputStream();

        encryptedDataPackageGenerator.decrypt(decryptionInputStream, decryptionOutputStream, "masterKey");

        byte[] decryptedData = decryptionOutputStream.toByteArray();
        // --- ENCRYPTION AND DECRYPTION

        // --- END POOL
        hsmSessionPool.endPool();
        // --- END POOL

        // --- DATA COMPARISON
        Assert.assertArrayEquals(toBeEncrypted, decryptedData);
    }
}
