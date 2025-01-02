package dev.esya.api.cmssignature.smartcard.hsm;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.P11SmartCard;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Random;

public class DirakOpenSessionInThread
{
    static int THREAD_COUNT = 20;
    static int SIGN_COUNT = 50;

    static String PIN = "123456";
    static long slot = 22;

    static X509Certificate x509signCert;

    public static void main(String[] args) throws Exception
    {
        P11SmartCard p11SmartCard = openSession();
        byte[] bytes = p11SmartCard.getSignatureCertificates().get(0);
        x509signCert = new ECertificate(bytes).asX509Certificate();

        Thread [] threads = new Thread[THREAD_COUNT];
        for(int i=0; i < THREAD_COUNT; i++)
        {
            final int finalI = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        for (int j = 0; j < SIGN_COUNT; j++) {
                            System.out.println("Start ThreadID: " + finalI + " Sign: " + j);
                            sign();
                            System.out.println("End ThreadID: " + finalI + " Sign: " + j);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
        }

        for(int i=0; i < THREAD_COUNT; i++)
        {
            System.out.println("Running thread: " + i);
            threads[i].start();
        }


        for(int i=0; i < THREAD_COUNT; i++)
        {
            threads[i].join();
            System.out.println("Thread Joined: " + i);
        }
    }

    public static Object syncObj = new Object();



    public static void sign() throws Exception
    {

        P11SmartCard sc = openSession();

        List<byte[]> certs;
        synchronized (syncObj)
        {
            certs = sc.getSignatureCertificates();
        }


        Thread.sleep(new Random().nextInt(1000));

        BaseSigner signer = sc.getSigner(x509signCert, Algorithms.SIGNATURE_RSA_SHA256);

        synchronized (syncObj)
        {
            //signer.sign(RandomUtil.generateRandom(50));
        }

        closeSession(sc);
    }

    public static P11SmartCard openSession() throws Exception
    {
        synchronized (syncObj)
        {
            P11SmartCard sc = new P11SmartCard(CardType.DIRAKHSM);
            sc.openSession(slot);
            sc.login(PIN);
            return sc;
        }
    }

    public static void closeSession(P11SmartCard sc) throws Exception
    {
        synchronized (syncObj)
        {
            sc.closeSession();
        }
    }
}
