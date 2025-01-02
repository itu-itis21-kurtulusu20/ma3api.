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
    public class EST_EC : CMSSignatureTest
    {
        public static Object[] TestCases = { new object[] { SignatureAlg.ECDSA_SHA384 } };

        // create signeddata with one est signature
        [Test, TestCaseSource("TestCases")]
        public void test01_CreateEST(SignatureAlg signatureAlg)
        {
            byte[] signature = createESTSignature(signatureAlg);

            ValidationUtil.checkSignatureIsValid(signature, null);
        }

        // create signeddata with one est signature with signingtime
        [Test, TestCaseSource("TestCases")]
        public void test02_CreateESTWithSigningTime(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            List<IAttribute> optionalAttrs = new List<IAttribute>();
            optionalAttrs.Add(new SigningTimeAttr(DateTime.UtcNow));

            Dictionary<String, Object> _params = new Dictionary<String, Object>();
            // necessary for getting signaturetimestamp
            _params.Add(EParameters.P_TSS_INFO, getTSSettings());
            // necessary for validation of signer certificate according to time in signaturetimestamp attribute
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            // add signer
            bs.addSigner(ESignatureType.TYPE_EST, getECSignerCertificate(), getECSignerInterface(signatureAlg),
                optionalAttrs, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        // create signeddata with one est signature with signingtime and
        [Test, TestCaseSource("TestCases")]
        public void test03_CreateESTWithSigningTimeAndSignerLocation(SignatureAlg signatureAlg)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            List<IAttribute> optionalAttrs = new List<IAttribute>();
            optionalAttrs.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttrs.Add(new SignerLocationAttr(null, "Turkey", null));

            Dictionary<String, Object> _params = new Dictionary<String, Object>();
            _params.Add(EParameters.P_TSS_INFO, getTSSettings());
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            bs.addSigner(ESignatureType.TYPE_EST, getECSignerCertificate(), getECSignerInterface(signatureAlg),
                optionalAttrs, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        // Add another parallel est signature to signeddata
        [Test, TestCaseSource("TestCases")]
        public void test04_AddSecondEST(SignatureAlg signatureAlg)
        {
            byte[] inAddSigedData = createESTSignature(signatureAlg);

            BaseSignedData bs = new BaseSignedData(inAddSigedData);

            Dictionary<String, Object> _params = new Dictionary<String, Object>();
            _params.Add(EParameters.P_TSS_INFO, getTSSettings());
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            bs.addSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256),
                null, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        // add est counter signature to the first signer
        [Test, TestCaseSource("TestCases")]
        public void test05_AddESTCounterSignatureToEST(SignatureAlg signatureAlg)
        {
            byte[] inAddSigedData = createESTSignature(signatureAlg);

            BaseSignedData bs = new BaseSignedData(inAddSigedData);

            Dictionary<String, Object> _params = new Dictionary<String, Object>();
            _params.Add(EParameters.P_TSS_INFO, getTSSettings());
            _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

            List<Signer> signers = bs.getSignerList();
            signers[0].addCounterSigner(ESignatureType.TYPE_EST, getSignerCertificate(),
                getSignerInterface(SignatureAlg.RSA_SHA256), null, _params);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        public byte[] createESTSignature(SignatureAlg signatureAlg)
        {
            {
                BaseSignedData bs = new BaseSignedData();
                bs.addContent(getSimpleContent());

                Dictionary<String, Object> _params = new Dictionary<String, Object>();
                _params.Add(EParameters.P_TSS_INFO, getTSSettings());
                _params.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

                bs.addSigner(ESignatureType.TYPE_EST, getECSignerCertificate(), getECSignerInterface(signatureAlg),
                    null, _params);

                return bs.getEncoded();
            }
        }
    }
}
