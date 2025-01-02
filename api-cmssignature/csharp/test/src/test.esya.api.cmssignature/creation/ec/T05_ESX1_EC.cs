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
    public class ESX1_EC : CMSSignatureTest {
    
        public static Object[] TestCases = { new object[] { SignatureAlg.ECDSA_SHA384 } };
    
        [Test, TestCaseSource("TestCases")]
        // create signeddata with one esx1 signature
        public void testCreateESX1(SignatureAlg signatureAlg) {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());
    
            Dictionary<String, Object> _params = new Dictionary<String, Object>();
    
            // necessary for getting signaturetimestamp and cadesc timestamp
            _params.Add(EParameters.P_TSS_INFO, getTSSettings());
            _params.Add(EParameters.P_TS_DIGEST_ALG, DigestAlg.SHA1);
            // necessary for validation of signer certificate according to time in signaturetimestamp attribute
            // while validation,references are also gathered
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());
    
            // add signer
            bs.addSigner(ESignatureType.TYPE_ESX_Type1, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, _params);
    
            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }
    }
}
