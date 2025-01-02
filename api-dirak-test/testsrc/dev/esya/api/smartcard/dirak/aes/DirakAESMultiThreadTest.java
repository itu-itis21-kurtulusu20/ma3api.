package dev.esya.api.smartcard.dirak.aes;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import gnu.crypto.pad.IPad;
import gnu.crypto.pad.PadFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ConstantsUtil;

import static org.junit.Assert.assertEquals;

public class DirakAESMultiThreadTest {

    static final String PASSWORD = "12345678";
    SmartCard sc = null;
    long sid = 0;
    long slotNo = 0;

    @Before
    public void setUp() throws Exception {
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = CardTestUtil.getSlot(sc);
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp() throws Exception {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    // ---

    @Test
    public void multiThreadAESTest() throws Exception {
        int keySize = 32;
        String keyLabel = "multithread_aes_" + keySize;
        boolean keyCreated = false;
        try {
            int THREAD_COUNT = 10;
            CardTestUtil.clearKeyPairIfExist(sc, sid, keyLabel);

            byte[] keyBytes = RandomUtil.generateRandom(keySize);
            AESKeyTemplate aesKeyTemplate = new AESKeyTemplate(keyLabel);
            aesKeyTemplate.getAsImportTemplate(keyBytes);

            sc.importSecretKey(sid, aesKeyTemplate);
            keyCreated = true;

            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++) {
                //her thread icin smartcard ve session olustur
                SmartCard sc = new SmartCard(CardType.DIRAKHSM);
                long sid = sc.openSession(slotNo);
                threads[i] = new Thread(new MultiThreadAESThread(i, sc, sid, keyLabel));
            }


            for (int i = 0; i < THREAD_COUNT; i++) {
                System.out.println("Running thread: " + i);
                threads[i].start();
            }

            for (int i = 0; i < THREAD_COUNT; i++) {
                threads[i].join();
                System.out.println("Thread Joined: " + i);
            }
        } finally {
            if (keyCreated)
                _deletePrivateObject(keyLabel);
        }
    }

    // ---

    public class MultiThreadAESThread implements Runnable {

        public int OPERATION_COUNT = 1024;

        int id;
        SmartCard sc;
        long sid;
        String keyLabel;


        public MultiThreadAESThread(int id, SmartCard sc, long sid, String keyLabel) {
            this.id = id;
            this.sc = sc;
            this.sid = sid;
            this.keyLabel = keyLabel;
        }

        public void run() {
            byte[] dataToBeEncrypted = RandomUtil.generateRandom(1024); // bir defa yap
            byte[] ivBytes = RandomUtil.generateRandom(16);

            for (int j = 0; j < OPERATION_COUNT; j++) {
                try {
                    System.out.println("Running Thread Id: " + id + " Operation Id: " + j);
                    multiThreadAESKeyAndTest(keyLabel, CipherAlg.AES256_CBC, dataToBeEncrypted, ivBytes, sid, sc);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public void multiThreadAESKeyAndTest(String keyLabel, CipherAlg cipherAlg, byte[] dataToBeEncrypted, byte[] ivBytes, long sid, SmartCard sc) throws Exception {
        byte[] dataToBeEncryptedPadded = dataToBeEncrypted;

        long mechanismCode = ConstantsUtil.convertSymmetricAlgToPKCS11Constant(cipherAlg.getName());

        CK_MECHANISM mechanism = new CK_MECHANISM(mechanismCode);

        //PKCS11 Pad'li ECB'i desteklemediği için kendimiz PKCS7 pad yaptık.
        if (mechanismCode == PKCS11Constants.CKM_AES_ECB) {
            IPad padOps = PadFactory.getInstance("pkcs7");
            padOps.init(16);
            byte[] pad = padOps.pad(dataToBeEncrypted, 0, dataToBeEncrypted.length);
            dataToBeEncryptedPadded = ByteUtil.concatAll(dataToBeEncrypted, pad);
        }

        if (mechanismCode == PKCS11Constants.CKM_AES_CBC || mechanismCode == PKCS11Constants.CKM_AES_CBC_PAD)
            mechanism.pParameter = ivBytes;

        byte[] encryptedData = sc.encryptData(sid, keyLabel, dataToBeEncryptedPadded, mechanism);

        assertEquals(true, encryptedData.length >= dataToBeEncrypted.length);
    }

    private void _deletePrivateObject(String label) throws Exception {
        sc.deletePrivateObject(sid, label);
    }
}
