using System;
using System.Collections.Generic;
using System.Text;
using System.Threading;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.api.smartcard.util;

namespace dev.esya.api.cmssignature.smartcard.hsm
{
    public class DirakHSMPoolTest
    {
        private Exception exceptionInThreads = null;

        int THREAD_COUNT = 40;
        int SIGN_COUNT = 500;

        public String PIN = "123456";
        public long slot = 22;

        ValidationPolicy validationPolicy;

        private String POLICY_FILE = "T:\\api-parent\\resources\\ug\\config\\certval-policy-test.xml";

        //HSMPool dirakSlot22Pool = null;

        private MultiSlotHSMPoolContainer multiSlotHsmPoolContainer;

        ECertificate certificate;

        [Test]
        public void testMultiThreadRun()
        {
            multiSlotHsmPoolContainer = new MultiSlotHSMPoolContainer();
            HSMPool dirakSlot22Pool = new HSMPool(CardType.DIRAKHSM, 22, "123456");
            multiSlotHsmPoolContainer.addHSMPool(dirakSlot22Pool);



            validationPolicy = PolicyReader.readValidationPolicy(POLICY_FILE);

            P11SmartCard sc = multiSlotHsmPoolContainer.checkOutItem(22);
            byte[] certBytes = sc.getSignatureCertificates()[0];
            dirakSlot22Pool.offer(sc);

            certificate = new ECertificate(certBytes);


            Thread[] threads = new Thread[THREAD_COUNT];
            for (int i = 0; i < THREAD_COUNT; i++)
            {
                threads[i] = new Thread(new ParameterizedThreadStart(signingThread));
            }

            
            for (int i = 0; i < THREAD_COUNT; i++)
            {
                Console.WriteLine("Running thread: " + i);
                threads[i].Start(i);
            }

            
            for (int i = 0; i < THREAD_COUNT; i++)
            {
                if (exceptionInThreads != null)
                    throw exceptionInThreads;
                threads[i].Join();
                Console.WriteLine("Thread Joined: " + i);
            }
        }

        public void signingThread(object tid)
        {
            int threadId = (int)tid;
            try
            {
                for (int i = 0; i < SIGN_COUNT; i++)
                {
                    createASignature(certificate, threadId, i);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Exception at thread: " + threadId);
                exceptionInThreads = e;


            }
        }

        public void createASignature(ECertificate cert, int tid, int index)
        {
            Console.WriteLine("Thread Id: " + tid + " Index: " + index + " is starting");

            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableByteArray(ASCIIEncoding.ASCII.GetBytes("Test")));

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = validationPolicy;
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            P11SmartCard sc = multiSlotHsmPoolContainer.checkOutItem(22);
            BaseSigner signer = sc.getSigner(cert, Algorithms.SIGNATURE_RSA_SHA256);

            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, parameters);

            multiSlotHsmPoolContainer.offer(sc);

            Console.WriteLine("Thread Id: " + tid + " Index: " + index + " is ending");
        }
    }
}
