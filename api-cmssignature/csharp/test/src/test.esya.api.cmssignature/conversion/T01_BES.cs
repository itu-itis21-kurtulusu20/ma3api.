using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace test.esya.api.cmssignature.conversion
{
    [TestFixture]
    public class T01_BES : CMSSignatureTest
    {
        private readonly String DIRECTORY = "T:\\api-cmssignature\\test-output\\csharp\\ma3\\conversion\\";

        [Test]
        public void testCreateBES()
        {
            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                          null, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), DIRECTORY + "BES.p7s");

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }

        [Test]
        public void testCreateP1()
        {
            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //Since SigningTime attribute is optional,add it to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));

            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), DIRECTORY + "EPES.p7s");

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }
    }
}