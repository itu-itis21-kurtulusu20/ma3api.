package dev.esya.api.cmssignature.smartcard.hsm;

import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.P11SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.HSMPool;

import java.io.FileInputStream;
import java.security.cert.X509Certificate;
import java.util.HashMap;

public class DirakHSMPoolTest extends CMSSignatureTest
{
    int threadId = 0;

    int THREAD_COUNT = 40;
    int SIGN_COUNT = 500;

    public String PIN = "123456";
    public long slot = 22;

    ValidationPolicy validationPolicy;

    private final String POLICY_FILE =  "T:\\api-parent\\resources\\ug\\config\\certval-policy-test.xml";

    HSMPool dirakSlot22Pool = null;

    ECertificate certificate;
    //Java can not convert from ECertificate to X509Certificate in multi thread.
    X509Certificate x509Certificate;

    @Test
    public void testMultiThreadRun() throws Exception
    {
        dirakSlot22Pool = new HSMPool(CardType.DIRAKHSM, 22, "123456");

        validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));

        P11SmartCard sc = dirakSlot22Pool.checkOutItem();
        byte[] certBytes = sc.getSignatureCertificates().get(0);
        dirakSlot22Pool.offer(sc);

        certificate = new ECertificate(certBytes);
        x509Certificate = certificate.asX509Certificate();


        Thread [] threads = new Thread[THREAD_COUNT];
        for(int i=0; i < THREAD_COUNT; i++)
        {
            threads[i] = new Thread(new DirakHSMPoolTest.SigningThread(i));
            threadId++;

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


    public void testCreateSign(ECertificate cert, X509Certificate x509Certificate, int tid, int index)throws Exception
    {
        System.out.println("Thread Id: " + tid + " Index: " + index + " is starting");

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(new SignableByteArray("test".getBytes()));

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        P11SmartCard p11SmartCard = dirakSlot22Pool.checkOutItem();

        BaseSigner signer = p11SmartCard.getSigner(x509Certificate, Algorithms.SIGNATURE_RSA_SHA256);

        bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params);

        dirakSlot22Pool.offer(p11SmartCard);

        System.out.println("Thread Id: " + tid + " Index: " + index + " is ending");
    }




    public class SigningThread implements Runnable
    {
        int runnableID;

        public SigningThread( int runnableID)
        {
            this.runnableID = runnableID;
        }

        @Override
        public void run() {
            System.out.println("Running ID: " + runnableID);
            for(int j = 0; j < SIGN_COUNT; j++)
            {
                try {
                    testCreateSign(certificate, x509Certificate, runnableID, j);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
