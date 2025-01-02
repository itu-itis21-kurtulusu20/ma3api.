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
    public class BES_EC : CMSSignatureTest
    {
        public static Object[] TestCases = { new object[] { SignatureAlg.ECDSA_SHA384 } };

        [Test, TestCaseSource("TestCases")]
        public void CreateSign(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void CreateParallelSignature(SignatureAlg signatureAlg)
        {
            //first signature
            BaseSignedData first_bs = new BaseSignedData();
            first_bs.addContent(getSimpleContent());

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            first_bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params_);
            byte[] signatureBytes = first_bs.getEncoded();

            //second signature
            BaseSignedData second_bs = new BaseSignedData(signatureBytes);
            second_bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params_);

            ValidationUtil.checkSignatureIsValid(second_bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void CreateSerialSignature(SignatureAlg signatureAlg)
        {
            // first signature
            BaseSignedData first_bs = new BaseSignedData();
            first_bs.addContent(getSimpleContent());

            Dictionary<String, Object> params_ = new Dictionary<String, Object>();
            params_.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            first_bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, params_);
            byte[] signatureBytes = first_bs.getEncoded();

            // second signature
            BaseSignedData second_bs = new BaseSignedData(signatureBytes);
            second_bs.getSignerList()[0].addCounterSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256), null, params_);

            ValidationUtil.checkSignatureIsValid(second_bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void CreateDetachedSignature(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent(), false);

            Dictionary<String, Object> _params = new Dictionary<String, Object>();
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            bs.addSigner(ESignatureType.TYPE_BES, getECSignerCertificate(), getECSignerInterface(signatureAlg), null, _params);
            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
        }
    }
}
