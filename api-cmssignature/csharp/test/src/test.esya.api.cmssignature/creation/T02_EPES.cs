
using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace test.esya.api.cmssignature.creation.ma3
{
    public class EPES : CMSSignatureTest
    {
        [Test]
        public void TestRSAPKCS()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(),
                    getSignerInterface(SignatureAlg.RSA_SHA256),
                    optionalAttributes, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }
    }
}
