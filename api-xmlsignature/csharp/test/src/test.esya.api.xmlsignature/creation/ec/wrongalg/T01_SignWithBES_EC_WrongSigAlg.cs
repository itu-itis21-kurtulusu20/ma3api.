using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;

namespace test.esya.api.xmlsignature.creation.ec.wrongalg
{
    [TestFixture]
    public class BES_EC : XMLSignatureTestBase
    {
        public static Object[] TestCases =
        {
            new object[] {SignatureAlg.ECDSA_SHA1},
            new object[] {SignatureAlg.ECDSA_SHA224},
            new object[] {SignatureAlg.ECDSA_SHA256},
            new object[] {SignatureAlg.ECDSA_SHA512}
        };

        [Test, TestCaseSource("TestCases"), ExpectedException(typeof(XMLSignatureException))]
        public void testCreateEnveloping(SignatureAlg signatureAlg)
        {
            new ec.BES_EC().testCreateEnveloping(signatureAlg);
        }

        [Test, TestCaseSource("TestCases"), ExpectedException(typeof(XMLSignatureException))]
        public void testCreateEnveloped(SignatureAlg signatureAlg)
        {
            new ec.BES_EC().testCreateEnveloped(signatureAlg);
        }

        [Test, TestCaseSource("TestCases"), ExpectedException(typeof(XMLSignatureException))]
        public void testCreateDetached(SignatureAlg signatureAlg)
        {
            new ec.BES_EC().testCreateDetached(signatureAlg);
        }

        [Test, TestCaseSource("TestCases"), ExpectedException(typeof(XMLSignatureException))]
        public void createCounterSignature(SignatureAlg signatureAlg)
        {
            new ec.BES_EC().createCounterSignature(signatureAlg);
        }
    }
}
