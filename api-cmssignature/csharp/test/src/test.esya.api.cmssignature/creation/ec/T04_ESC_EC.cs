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
    public class ESC_EC : CMSSignatureTest
    {
        public static Object[] TestCases = { new object[] { SignatureAlg.ECDSA_SHA384 } };

        [Test, TestCaseSource("TestCases")]
        public void testCreateESC(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> _params = new Dictionary<String, Object>();

            _params.Add(EParameters.P_TSS_INFO, getTSSettings());
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            // by default,qc statement is checked. In order to use test certificates,set this parameter
            // add signer
            bs.addSigner(ESignatureType.TYPE_ESC, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }
    }
}
