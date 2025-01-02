using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace test.esya.api.cmssignature.creation.ec.wrongalg.ma3
{
    [TestFixture]
    public class BES_EC : CMSSignatureTest
    {
        public static Object[] TestCases =
        {
            new object[] {SignatureAlg.ECDSA_SHA1},
            new object[] {SignatureAlg.ECDSA_SHA224},
            new object[] {SignatureAlg.ECDSA_SHA256},
            new object[] {SignatureAlg.ECDSA_SHA512}
        };

        [Test, TestCaseSource("TestCases"), ExpectedException(typeof(CMSSignatureException))]
        public void CreateSign(SignatureAlg signatureAlg)
        {
            new ec.ma3.BES_EC().CreateSign(signatureAlg);
        }

        [Test, TestCaseSource("TestCases"), ExpectedException(typeof(CMSSignatureException))]
        public void CreateParallelSignature(SignatureAlg signatureAlg)
        {
            new ec.ma3.BES_EC().CreateParallelSignature(signatureAlg);
        }

        [Test, TestCaseSource("TestCases"), ExpectedException(typeof(CMSSignatureException))]
        public void CreateSerialSignature(SignatureAlg signatureAlg)
        {
            new ec.ma3.BES_EC().CreateSerialSignature(signatureAlg);
        }

        [Test, TestCaseSource("TestCases"), ExpectedException(typeof(CMSSignatureException))]
        public void CreateDetachedSignature(SignatureAlg signatureAlg)
        {
            new ec.ma3.BES_EC().CreateDetachedSignature(signatureAlg);
        }
    }
}
