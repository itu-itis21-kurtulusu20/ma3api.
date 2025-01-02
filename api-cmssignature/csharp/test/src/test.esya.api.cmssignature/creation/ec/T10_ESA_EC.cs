using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

namespace test.esya.api.cmssignature.creation.ec.ma3
{
    [TestFixture]
    public class ESA_EC : CMSSignatureTest
    {
        public static Object[] TestCases = { new object[] { SignatureAlg.ECDSA_SHA384 } };

        [Test, TestCaseSource("TestCases")]
        // create archive signature
        public void testCreateESA(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> _params = new Dictionary<String, Object>();
            _params.Add(EParameters.P_TSS_INFO, getTSSettings());
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            bs.addSigner(ESignatureType.TYPE_ESA, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void testDetachedESA(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent(), false);

            Dictionary<String, Object> _params = new Dictionary<String, Object>();
            _params.Add(EParameters.P_TSS_INFO, getTSSettings());
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            bs.addSigner(ESignatureType.TYPE_ESA, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
        }
    }
}
