package dev.esya.api.smartcard.dirak;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.P11SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

public class DirakMultiThreadSignTest {

    int THREAD_COUNT = 20;
    int SIGN_COUNT = 100;

    static int keySize = 32;
    static final String IMPORTED_KEY_LABEL = "RSAKeyImported_" + StringUtil.toHexString(RandomUtil.generateRandom(2)) + "_" + DirakTestConstants.osNameAndArch;
    static final String IMPORTED_CERT_LABEL = "CertImported_" + StringUtil.toHexString(RandomUtil.generateRandom(2)) + "_" + DirakTestConstants.osNameAndArch;
    static final String AES_KEY_LABEL = "AES_" + keySize + "_" + StringUtil.toHexString(RandomUtil.generateRandom(2)) + "_" + DirakTestConstants.osNameAndArch;
    static final String PASSWORD = "12345678";

    SmartCard sc = null;
    static X509Certificate cert;
    long slotNo = 0;
    long sid = 0;

    @Before
    public void setUp() throws Exception {
        System.out.println("settingUp starts");
        System.out.println("OS Name And Architecture : " + DirakTestConstants.osNameAndArch);
        sc = new SmartCard(CardType.DIRAKHSM);
        slotNo = CardTestUtil.getSlot(sc);
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
        ImportCertificateWithKeys(sc);
        System.out.println("settingUp finishes");
    }

    @After
    public void cleanUp() throws Exception {
        System.out.println("cleanUp starts");
        CardTestUtil.clearKeyPairIfExist(sc, sid, IMPORTED_KEY_LABEL);
        if (sc.isCertificateExist(sid, IMPORTED_CERT_LABEL))
            sc.deleteCertificate(sid, IMPORTED_CERT_LABEL);
        if (sc.isObjectExist(sid, AES_KEY_LABEL)) {
            sc.deletePrivateObject(sid, AES_KEY_LABEL);
            System.out.println("fff");
        }

        sc.logout(sid);
        sc.closeSession(sid);
        System.out.println("cleanUp finishes");
    }

    //MULTITHREAD TESTS

    @Test
    public void testSmartCardOpThread() throws Exception {
        SmartCardOpThread[] smartCardOpThreads = new SmartCardOpThread[THREAD_COUNT];
        Thread[] threads = new Thread[THREAD_COUNT];


        for (int i = 0; i < THREAD_COUNT; i++) {
            smartCardOpThreads[i] = new SmartCardOpThread(i);
            threads[i] = new Thread(smartCardOpThreads[i]);
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            System.out.println("Running thread: " + i);
            threads[i].start();
        }

        boolean errorOccured = false;

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
            Exception exception = smartCardOpThreads[i].getException();
            if (exception != null) {
                errorOccured = true;
            }

            System.out.println("Thread Joined: " + i);
        }

        Assert.assertFalse(errorOccured);
    }

    @Test
    public void testUseSameSessionInThreads() throws Exception {
        int threadCount = 2;

        BaseSignerSignThread[] signThreads = new BaseSignerSignThread[threadCount];

        P11SmartCard sc = new P11SmartCard(CardType.DIRAKHSM);
        sc.openSession(slotNo);
        sc.login(PASSWORD);

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            List<byte[]> certs = sc.getSignatureCertificates();

            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(certs.get(0)));
            BaseSigner baseSigner = sc.getSigner(cert, Algorithms.SIGNATURE_RSA_SHA256);

            signThreads[i] = new BaseSignerSignThread(cert, baseSigner, i, sc);
            threads[i] = new Thread(signThreads[i]);
        }

        for (int i = 0; i < threadCount; i++) {
            System.out.println("Running thread: " + i);
            threads[i].start();
        }

        boolean expectedErrorOccured = false;

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
            Exception exception = signThreads[i].getException();
            if (exception != null && exception.getMessage().contains("CKR_OPERATION_ACTIVE")) {
                expectedErrorOccured = true;
            }

            System.out.println("Thread Joined: " + i);
        }

        Assert.assertTrue(expectedErrorOccured);
    }


    /*
    * Bu testte her imza için yeni bir session açılmıyor.
    *
    **/
    @Test
    public void testBaseSignerSignThread() throws Exception {
        Thread[] threads = new Thread[THREAD_COUNT];
        BaseSignerSignThread[] signThreads = new BaseSignerSignThread[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {

            P11SmartCard sc = new P11SmartCard(CardType.DIRAKHSM);
            sc.openSession(slotNo);
            sc.login(PASSWORD);
            List<byte[]> certs = sc.getSignatureCertificates();

            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(certs.get(0)));
            BaseSigner baseSigner = sc.getSigner(cert, Algorithms.SIGNATURE_RSA_SHA256);

            signThreads[i] = new BaseSignerSignThread(cert, baseSigner, i, sc);
            threads[i] = new Thread(signThreads[i]);
        }

        for (int i = 0; i < THREAD_COUNT; i++) {
            System.out.println("Running thread: " + i);
            threads[i].start();
        }

        boolean errorOccured = false;

        for (int i = 0; i < THREAD_COUNT; i++) {
            threads[i].join();
            Exception exception = signThreads[i].getException();
            if (exception != null) {
                errorOccured = true;
            }

            System.out.println("Thread Joined: " + i);
        }

        Assert.assertFalse(errorOccured);
    }

    public class BaseSignerSignThread implements Runnable {
        int id;
        BaseSigner baseSigner;
        X509Certificate cert;
        P11SmartCard sc;

        Exception ex;

        public BaseSignerSignThread(X509Certificate cert, BaseSigner baseSigner, int id, P11SmartCard sc) {
            this.cert = cert;
            this.id = id;
            this.baseSigner = baseSigner;
            this.sc = sc;
        }


        public void run() {
            System.out.println("Running ID: " + id);
            for (int j = 0; j < SIGN_COUNT; j++) {
                try {
                    System.out.println("Thread Id: " + id + " Index: " + j + " is starting");

                    baseSigner.sign(RandomUtil.generateRandom(32));

                    System.out.println("Thread Id: " + id + " Index: " + j + " is ending");
                } catch (Exception e) {
                    this.ex = e;
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        public Exception getException() {
            return this.ex;
        }
    }

    public class SmartCardOpThread implements Runnable {

        int id;

        Exception ex = null;

        public SmartCardOpThread(int id) {
            this.id = id;
        }

        public void run() {

            System.out.println("Running ID: " + id);
            for (int j = 0; j < SIGN_COUNT; j++) {
                try {
                    System.out.println("Thread Id: " + id + " Index: " + j + " is starting");
                    P11SmartCard smartCard = new P11SmartCard(CardType.DIRAKHSM);
                    smartCard.openSession(slotNo);
                    smartCard.login(PASSWORD);
                    List<byte[]> certs = smartCard.getSignatureCertificates();
                    X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(certs.get(0)));
                    BaseSigner baseSigner = smartCard.getSigner(cert, Algorithms.SIGNATURE_RSA_SHA256);
                    baseSigner.sign(RandomUtil.generateRandom(32));
                    smartCard.closeSession();
                    System.out.println("Thread Id: " + id + " Index: " + j + " is ending");
                } catch (Exception e) {
                    this.ex = e;
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        public Exception getException() {
            return this.ex;
        }
    }

    public void ImportCertificateWithKeys(SmartCard sc)
        throws Exception {
        //Varsa import etmeden onceden sertifika ve anahtarlar silinir!
        CardTestUtil.clearKeyPairIfExist(sc, sid, IMPORTED_KEY_LABEL);
        if (sc.isCertificateExist(sid, IMPORTED_CERT_LABEL))
            sc.deleteCertificate(sid, IMPORTED_CERT_LABEL);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.decode(DirakTestConstants.testCer)));
        PrivateKey prikey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, Base64.decode(DirakTestConstants.testPrivateKey));

        boolean r1 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
        boolean r2 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
        boolean r3 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
        sc.importCertificateAndKey(sid, IMPORTED_CERT_LABEL, IMPORTED_KEY_LABEL, prikey, cert);
        boolean r4 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
        boolean r5 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
        boolean r6 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);

        Assert.assertTrue(!r1 && !r2 && !r3 && r4 && r5 && r6);
    }
}
