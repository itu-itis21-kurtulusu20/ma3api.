
using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature.config;

namespace test.esya.api.signature
{
    [TestFixture]
    public class ConfigTest
    {
        [Test]
        public void load()
        {
            Config c = new Config();
            Assert.AreNotEqual(null, c);
            Assert.AreEqual(c.getAlgorithmsConfig().getDigestAlg(), DigestAlg.SHA256);
            Assert.AreEqual(c.getAlgorithmsConfig().getSignatureAlg(), SignatureAlg.RSA_SHA256);
        }

        [Test]
        public void testTimeConfig()
        {
            Config c = new Config();
            CertificateValidationConfig config = c.getCertificateValidationConfig();

            Console.WriteLine($"Grace period: {config.getGracePeriodInSeconds()}");
            Console.WriteLine($"Last revocation period: {config.getLastRevocationPeriodInSeconds()}");
            Console.WriteLine($"Signing time tolerance: {config.getSigningTimeToleranceInSeconds()}");
        }
    }
}
