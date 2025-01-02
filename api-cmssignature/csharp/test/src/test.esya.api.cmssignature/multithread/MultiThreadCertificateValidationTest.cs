using System;
using System.Collections.Generic;
using System.Threading;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

namespace test.esya.api.cmssignature.multithread
{
    class MultiThreadCertificateValidationTest : CMSSignatureTest
    {

        public static bool errorOccured = false;

        [Test]
        public void multiThreadCertificateValidationTest()
        {
            ECertificate eCertificate = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");
            ValidationPolicy validationPolicy = getPolicy();

            int numberOfThreads = 50;

            List<Thread> threads = new List<Thread>();


            for (int i = 0; i < numberOfThreads; i++)
            {
                Thread thread = new Thread(new ThreadStart(() => Runnable(eCertificate, validationPolicy)));
                threads.Add(thread);
            }

            for (int i = 0; i < numberOfThreads; i++)
            {
                threads[i].Start();
            }

            for (int i = 0; i < numberOfThreads; i++)
            {
                threads[i].Join();
            }

            Assert.False(errorOccured);

        }

        void Runnable(ECertificate eCertificate, ValidationPolicy validationPolicy)
        {
            try
            {
                Dictionary<string, object> parameters = new Dictionary<string, object>();
                parameters[EParameters.P_CERT_VALIDATION_POLICY] = validationPolicy;
                parameters[EParameters.P_GRACE_PERIOD] = 86400L;

                for (int i = 0; i < 50; i++)
                {
                    CertRevocationInfoFinder certRevocationInfoFinder = new CertRevocationInfoFinder(true);
                    certRevocationInfoFinder.validateCertificate(eCertificate, parameters, DateTime.Now);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                errorOccured = true;
            }
        }
    }
}
