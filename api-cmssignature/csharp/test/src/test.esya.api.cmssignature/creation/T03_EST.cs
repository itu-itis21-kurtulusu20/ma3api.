using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace test.esya.api.cmssignature.creation.ma3
{
    [TestFixture]
    public class EST : CMSSignatureTest
    {
       
        //create signeddata with one est signature
        [Test]
        public void testCreateEST()
        {
            byte [] signature = createESTSignature();

            ValidationUtil.checkSignatureIsValid(signature, null);
        }


        //create signeddata with one est signature with signingtime
        [Test]
        public void testCreateESTWithSigningTime()
        {
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(getSimpleContent());

            List<IAttribute> optionalAttrs = new List<IAttribute>();
            optionalAttrs.Add(new SigningTimeAttr(DateTime.UtcNow));

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            
            //necassary for getting signaturetimestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;

            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttrs, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        //create signeddata with one est signature with signingtime and 
        [Test]
        public void testCreateESTWithSigningTimeAndSignerLocation()
        {
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(getSimpleContent());

            List<IAttribute> optionalAttrs = new List<IAttribute>();
            optionalAttrs.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttrs.Add(new SignerLocationAttr(null, "Turkey", null));

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

           
            //necassary for getting signaturetimestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;

            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();            

            //add signer
            bs.addSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttrs, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }


        //Add another parallel est signature to signeddata
        [Test]
        public void testAddSecondEST()
        {
            byte[] inputSD = createESTSignature();

            BaseSignedData bs = new BaseSignedData(inputSD);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            
            //necassary for getting signaturetimestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;

            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.addSigner(ESignatureType.TYPE_EST, getSecondSignerCertificate(),
                         getSecondSignerInterface(SignatureAlg.RSA_SHA1), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }


        //add est counter signature to the first signer
        [Test]
        public void testAddESTCounterSignatureToEST()
        {
            byte[] inputSD = createESTSignature();

            BaseSignedData bs = new BaseSignedData(inputSD);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();
            
            //necassary for getting signaturetimestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;

            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //get signatures of the signeddata
            List<Signer> signers = bs.getSignerList();

            //add EST CounterSignature to the first signature
            signers[0].addCounterSigner(ESignatureType.TYPE_EST, getSecondSignerCertificate(),
                                        getSecondSignerInterface(SignatureAlg.RSA_SHA1), null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }


        public byte[] createESTSignature()
        {
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necassary for getting signaturetimestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;

            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_EST, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         null, parameters);

            return bs.getEncoded();
        }

    }
}