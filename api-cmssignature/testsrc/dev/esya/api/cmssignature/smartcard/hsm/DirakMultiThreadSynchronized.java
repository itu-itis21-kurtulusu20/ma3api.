package dev.esya.api.cmssignature.smartcard.hsm;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import test.esya.api.cmssignature.CMSSignatureTest;
import test.esya.api.cmssignature.validation.ValidationUtil;
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

import java.io.FileInputStream;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;

public class DirakMultiThreadSynchronized extends CMSSignatureTest
{
    int threadId = 0;

    int THREAD_COUNT = 40;
    int SIGN_COUNT = 50;

    public String PIN = "123456";
    public long slot = 22;

    ValidationPolicy validationPolicy;

    private final String POLICY_DIRECTORY = "T:\\api-parent\\resources\\ug\\config\\";
    private final String POLICY_FILE =  POLICY_DIRECTORY + "certval-policy-test.xml";


    @Test
    public void testMultiThreadRun() throws Exception
    {
        validationPolicy = PolicyReader.readValidationPolicy(new FileInputStream(POLICY_FILE));

        P11SmartCard sc = new P11SmartCard(CardType.DIRAKHSM);
        sc.openSession(slot);
        sc.login(PIN);
        List<byte[]> certs = sc.getSignatureCertificates();
        ECertificate cert = new ECertificate(certs.get(0));
        X509Certificate x509signCert = cert.asX509Certificate();
        BaseSigner baseSigner = sc.getSigner(x509signCert, Algorithms.SIGNATURE_RSA_SHA256);

        SynchronisedBaseSigner synchronisedBaseSigner = new SynchronisedBaseSigner(baseSigner);


        Thread [] threads = new Thread[THREAD_COUNT];
        for(int i=0; i < THREAD_COUNT; i++)
        {

            threads[i] = new Thread(new DirakMultiThreadSynchronized.SigningThread(cert, synchronisedBaseSigner, i));
            threadId++;

        }

        //BasicConfigurator.configure();

        PropertyConfigurator.configure("C:\\a\\api_log.config");
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




    public void testCreateSign(ECertificate cert, BaseSigner baseSigner, int tid, int index)throws Exception
    {
        System.out.println("Thread Id: " + tid + " Index: " + index + " is starting");

        BaseSignedData bs = new BaseSignedData();
        bs.addContent(new SignableByteArray("test".getBytes()));

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, validationPolicy);
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        bs.addSigner(ESignatureType.TYPE_BES, cert, baseSigner, null, params);

        ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);

        System.out.println("Thread Id: " + tid + " Index: " + index + " is ending");
    }


    public class SigningThread implements Runnable
    {
        ECertificate cert;
        BaseSigner baseSigner;
        int tid;


        public SigningThread(ECertificate cert, BaseSigner baseSigner, int runnableID)
        {
            this.cert = cert;
            this.tid = runnableID;
            this.baseSigner = baseSigner;
        }

        @Override
        public void run() {
            System.out.println("Running ID: " + tid);
            for(int j = 0; j < SIGN_COUNT; j++)
            {
                try {
                    testCreateSign(cert, baseSigner, tid, j);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }


}
