using System;
using System.IO;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.crypto;

namespace tr.gov.tubitak.uekae.esya.api.infra.certstore
{

    public class CertStoreTest
    {
        public readonly static String CERT_STORE_FILE_PATH = "SertifikaDeposu.svt";
        static CertStoreTest()
        {
            try
            {
                Crypto.setProvider(Crypto.PROVIDER_BOUNCY);                
            }
            catch (Exception ex)
            {                
                Assert.Fail("PROVIDER_BOUNCY could not be set");                
            }
        }

        [SetUp]
        public void setUp()
        {
            File.Delete(CERT_STORE_FILE_PATH);
        }

        [TearDown]
        public void tearDown()
        {
        }

    }
}
