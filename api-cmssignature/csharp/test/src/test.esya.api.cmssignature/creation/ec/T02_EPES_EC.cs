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
    public class EPES_EC : CMSSignatureTest
    {
        public static Object[] TestCases = { new object[] { SignatureAlg.ECDSA_SHA384 } };

        [Test, TestCaseSource("TestCases")]
        public void testCreateP1(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));

            Dictionary<String, Object> _params = new Dictionary<String, Object>();
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), optionalAttributes, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }
    }
}
