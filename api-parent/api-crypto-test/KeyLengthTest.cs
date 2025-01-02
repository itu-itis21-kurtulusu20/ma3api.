using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace api_crypto_test
{
    [TestFixture]
    public class KeyLengthTest
    {
        private static object[] _testCases =
        {
            // 2048
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA7.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_6.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_21.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA3.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_2.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_16.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_9.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA12.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_12.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA6.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_5.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_20.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_19.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA2.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_1.p12", "123456", 2048},
            new object[] {"T:/api-signature/testresources/suleyman.uslu_283255@ug.net.pfx", "283255", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_15.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA9.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_8.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA11.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_11.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA5.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_4.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/unit-test-resources/pfx/sifreleme_RSA_sura_506436.pfx", "506436", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_18.p12", "123456", 2048},
            new object[] {"T:/api-xmlsignature/docs/certstore/yavuz.kahveci_238778@ug.net.pfx", "238778", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_14.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA8.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_7.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA10.p12", "123456", 2048},
            new object[] {"T:/api-xmlsignature/docs/certstore/yavuz.kahveci_437710_iptal@ug.net.pfx", "437710", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_10.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA4.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_3.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_17.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCB.p12", "123456", 2048},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCA1_13.p12", "123456", 2048},
            new object[] {"T:/api-xmlsignature/docs/certstore/ramazan.girgin_327147@ug.net.pfx", "327147", 2048},

            // 1024
            new object[] {"T:/api-parent/resources/unit-test-resources/pfx/ahmet.yetgin_890111@ug.net.pfx", "890111", 1024},

            // 521
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCD1_3_ec521.p12", "123456", 521},

            // 384
            new object[] {"T:/api-parent/resources/ug/pfx/esya-test-system/ECDSA_P384_666377.pfx", "666377", 384},
            new object[] {"T:/api-parent/resources/ug/pfx/esya-test-system/test@test.gov.tr_724328.pfx", "724328", 384},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCD1_2.p12", "123456", 384},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCD1_2_ec384.p12", "123456", 384},
            new object[] {"T:/api-parent/resources/unit-test-resources/pfx/sifreleme_EC_sura_607983.pfx", "607983", 384},

            // 256
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCD1_1_ec256.p12", "123456", 256},
            new object[] {"T:/api-parent/resources/ug/pfx/test-suite/QCD1_1.p12", "123456", 256},
        };

        [Test]
        [TestCaseSource(nameof(_testCases))]
        public void CertKeyLengthTest(string pfxPath, string password, int expectedKeyLength)
        {
            IPfxParser pfxParser = new BouncyPfxParser(pfxPath, password);
            ECertificate certificate = pfxParser.getFirstCertificate();
            Assert.NotNull(certificate);

            int keyLength = KeyUtil.getKeyLength(certificate);
            Assert.AreEqual(expectedKeyLength, keyLength);
        }

        [Test]
        [TestCaseSource(nameof(_testCases))]
        public void CertKeyLengthTest2(string pfxPath, string password, int expectedKeyLength)
        {
            IPfxParser pfxParser = new BouncyPfxParser(pfxPath, password);
            ECertificate certificate = pfxParser.getFirstCertificate();
            Assert.NotNull(certificate);

            IPublicKey key = KeyUtil.decodePublicKey(certificate.getSubjectPublicKeyInfo());
            int keyLength = KeyUtil.getKeyLength(key);

            Console.WriteLine($"Key length is {keyLength}");
            Assert.AreEqual(expectedKeyLength, keyLength);
        }
    }
}
