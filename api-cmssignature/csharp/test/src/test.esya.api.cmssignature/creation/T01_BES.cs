using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using NUnit.Framework;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;

namespace test.esya.api.cmssignature.creation.ma3
{
    [TestFixture]
    public class BES : CMSSignatureTest
    {
        public static Object[] TestCases =
        {
            new object[] {SignatureAlg.RSA_SHA1, null},
            new object[] {SignatureAlg.RSA_SHA256, null},
            new object[] {SignatureAlg.RSA_SHA384, null},
            new object[] {SignatureAlg.RSA_SHA512, null},


            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA1)},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA256)},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA384)},
            new object[] {SignatureAlg.RSA_PSS, new RSAPSSParams(DigestAlg.SHA512)}
        };


        [Test,TestCaseSource("TestCases")]
        public void CreateSign(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(signatureAlg, rsaPssParams), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
         }

        [Test, TestCaseSource("TestCases")]
        public void CreateSignWithCRL(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            bs.addSigner(ESignatureType.TYPE_BES, getSecondSignerCertificate(), getSecondSignerInterface(signatureAlg, rsaPssParams), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void CreateParallelSignature(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            //first signature
            byte[] signatureBytes = CreateBasicCMSSignature();

            //second signature
            BaseSignedData bs = new BaseSignedData(signatureBytes);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(signatureAlg, rsaPssParams), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void CreateSerialSignature(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            byte[] signatureBytes = CreateBasicCMSSignature();

            //second signature
            BaseSignedData bs = new BaseSignedData(signatureBytes);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].addCounterSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(signatureAlg, rsaPssParams), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test, TestCaseSource("TestCases")]
        public void CreateDetachedSignature(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent(), false);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING] = false;

            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(signatureAlg, rsaPssParams), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
        }

        [Test, TestCaseSource("TestCases")]
        public void createSignatureInTwoSteps(SignatureAlg signatureAlg, RSAPSSParams rsaPssParams)
	    {
		    BaseSignedData bs = new BaseSignedData();
	    	bs.addContent(getSimpleContent(), false);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            parameters.Add(EParameters.P_CERT_VALIDATION_POLICY, getPolicy());

	    	byte [] dtbs = bs.initAddingSigner(ESignatureType.TYPE_BES, getSignerCertificate(), signatureAlg, rsaPssParams, null, parameters);
	    	byte[] bsdBytes = bs.getEncoded();

	    	BaseSigner signer = getSignerInterface(signatureAlg, rsaPssParams);
	    	byte[] signature = signer.sign(dtbs);

	    	finishSigning(bsdBytes, signature);
	    }

        private void finishSigning(byte[] bsdBytes, byte[] signature)
        {
            BaseSignedData bs = new BaseSignedData(bsdBytes);
            bs.finishAddingSigner(signature);
		    ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
	    }
    }
}