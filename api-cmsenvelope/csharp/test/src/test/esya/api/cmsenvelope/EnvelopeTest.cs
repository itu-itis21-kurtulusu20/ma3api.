using test.esya.api.cmsenvelope.integration;

namespace test.esya.api.cmsenvelope
{
    public class EnvelopeTest
    {
        public static void Main()
        {
            CMSEnvelopeCertificateValidationTest unitTest = new CMSEnvelopeCertificateValidationTest();
            unitTest.testEncryptionWithCertificateValidationValid();
        }
    }
}
