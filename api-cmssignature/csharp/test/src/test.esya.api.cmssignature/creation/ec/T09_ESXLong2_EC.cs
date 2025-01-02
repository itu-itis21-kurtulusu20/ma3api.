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
    public class ESXLong2_EC : CMSSignatureTest
    {
        public static Object[] TestCases = { new object[] { SignatureAlg.ECDSA_SHA384 } };

        // create signeddata with one esxlong2 signature
        [Test, TestCaseSource("TestCases")]
        public void testCreateESXLong2(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> _params = new Dictionary<String, Object>();

            // necessary for getting signaturetimestamp and reference timestamp
            _params.Add(EParameters.P_TSS_INFO, getTSSettings());

            // necessary for validation of signer certificate according to time in signaturetimestamp attribute
            // while validation,references and values are also gathered
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            // add signer
            bs.addSigner(ESignatureType.TYPE_ESXLong_Type2, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }
    }
}
