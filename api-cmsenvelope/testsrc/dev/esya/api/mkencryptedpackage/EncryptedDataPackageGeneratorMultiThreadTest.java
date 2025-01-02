package dev.esya.api.mkencryptedpackage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.mkencryptedpackage.EncryptedDataPackageGenerator;
import tr.gov.tubitak.uekae.esya.api.mkencryptedpackage.EncryptedPackageConfig;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.sessionpool.HSMSessionPool;

import java.io.IOException;
import java.util.Random;

public class EncryptedDataPackageGeneratorMultiThreadTest {
    public static int exceptionCount;

    @Before
    public void createMasterKey() throws PKCS11Exception, IOException, SmartCardException {
        AESKeyTemplate masterAESKeyTemplate = new AESKeyTemplate("masterKey", 32);
        masterAESKeyTemplate.getAsTokenTemplate(false, false, true);

        SmartCard smartCard = new SmartCard(CardType.DIRAKHSM);
        long sessionID = smartCard.openSession(90);
        smartCard.login(sessionID, "123456");

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
    public void encryptionAndDecryptionTest() throws Exception {
        int THREAD_COUNT = 10;

        // --- CREATING THE NEEDED CLASS
        CardType cardType = CardType.DIRAKHSM;
        HSMSessionPool hsmSessionPool = new HSMSessionPool(cardType, 90, "123456");
        EncryptedDataPackageGenerator encryptedDataPackageGenerator = new EncryptedDataPackageGenerator(hsmSessionPool, EncryptedPackageConfig.VERSION_ONE);
        // --- CREATING THE NEEDED CLASS

        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i] = new Thread(new MultiThreadEncryptedDataPackageThread(encryptedDataPackageGenerator, i));
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            System.out.println("Running thread: " + i);
            threads[i].start();
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
            System.out.println("Thread Joined: " + i);
        }

        // --- END POOL
        hsmSessionPool.endPool();
        // --- END POOL

        if (exceptionCount != 0) {
            throw new RuntimeException("Exception count: " + exceptionCount);
        }
    }

    public class MultiThreadEncryptedDataPackageThread implements Runnable {

        public int OPERATION_COUNT = 200;

        int id;
        EncryptedDataPackageGenerator encryptedDataPackageGenerator;

        public MultiThreadEncryptedDataPackageThread(EncryptedDataPackageGenerator encryptedDataPackageGenerator, int id) {
            this.encryptedDataPackageGenerator = encryptedDataPackageGenerator;
            this.id = id;
        }

        public void run() {
            for (int j = 0; j < OPERATION_COUNT; j++) {
                try {
                    System.out.println("Running Thread Id: " + id + " Operation Id: " + j);
                    multiThreadTest(encryptedDataPackageGenerator);
                } catch (Exception e) {
                    exceptionCount++;
                }
            }
        }

        public void multiThreadTest(EncryptedDataPackageGenerator encryptedDataPackageGenerator) throws Exception {
            // --- GENERATING TO BE ENCRYPTED DATA
            int randomDataLen = new Random().nextInt(200) + 1;
            byte[] toBeEncrypted = RandomUtil.generateRandom(randomDataLen);
            // --- GENERATING TO BE ENCRYPTED DATA

            // --- ENCRYPTION AND DECRYPTION
            byte[] encryptedData = encryptedDataPackageGenerator.encrypt(toBeEncrypted, "masterKey");
            byte[] decryptedData = encryptedDataPackageGenerator.decrypt(encryptedData, "masterKey");
            // --- ENCRYPTION AND DECRYPTION

            // --- DATA COMPARISON
            Assert.assertArrayEquals(toBeEncrypted, decryptedData);
        }
    }
}
