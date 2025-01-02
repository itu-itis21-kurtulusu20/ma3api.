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
    public class ESA : CMSSignatureTest
    {
       
        //create archive signature
        [Test]
        public void testCreateESA()
        {
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(getSimpleContent());

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necassary for getting signaturetimestamp and archive timestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            
            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            //while validation,references and values are also gathered
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_ESA, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }


        [Test]
        public void testDetachedESA()
        {
            BaseSignedData bs = new BaseSignedData();

            bs.addContent(getSimpleContent(), false);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necassary for getting signaturetimestamp and archive timestamp
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;

            //necessary for validation of signer certificate according to time in signaturetimestamp attribute
            //while validation,references and values are also gathered
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_ESA, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         null, parameters);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), getSimpleContent());
        }



    }
}