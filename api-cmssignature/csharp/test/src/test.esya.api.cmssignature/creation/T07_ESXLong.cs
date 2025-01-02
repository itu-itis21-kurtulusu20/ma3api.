using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace test.esya.api.cmssignature.creation.ma3
{
    [TestFixture]
    public class ESXLong : CMSSignatureTest
    {
        
        //create signeddata with one esxlong signature
        [Test]
        public void testCreateESXLong()
        {
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necassary for getting signaturetimestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            
            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            //while validation,references and values are also gathered
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test]
        public void testCreateSerial()
        {
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necassary for getting signaturetimestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            
            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            //while validation,references and values are also gathered
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         null, parameters);

            BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());

            parameters[EParameters.P_EXTERNAL_CONTENT] = getSimpleContent();

            bs2.getSignerList()[0].addCounterSigner(ESignatureType.TYPE_ESXLong, getSecondSignerCertificate(),
                                                    getSecondSignerInterface(SignatureAlg.RSA_SHA1), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }


        [Test]
        public void testCreate_P4()
        {
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necassary for getting signaturetimestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA256;

            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            //while validation,references and values are also gathered
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new SignaturePolicyIdentifierAttr(TurkishESigProfile.P4_1));


            //add signer
            bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA256),
                         optionalAttributes, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test]
        public void createSignatureInTwoSteps()
        {
            BaseSignedData bs = new BaseSignedData();
            bs.addContent(getSimpleContent(), false);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            byte[] dtbs = bs.initAddingSigner(ESignatureType.TYPE_BES, getSignerCertificate(), SignatureAlg.RSA_SHA256, null, null, parameters);
            byte[] bsdBytes = bs.getEncoded();

            BaseSigner signer = getSignerInterface(SignatureAlg.RSA_SHA256, null);
            byte[] signature = signer.sign(dtbs);

            finishSigning(bsdBytes, signature, parameters);
        }

        private void finishSigning(byte[] bsdBytes, byte[] signature, Dictionary<String, Object> parameters)
        {
            BaseSignedData bs = new BaseSignedData(bsdBytes);
            bs.finishAddingSigner(signature);
            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESXLong, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
        }
    }
}