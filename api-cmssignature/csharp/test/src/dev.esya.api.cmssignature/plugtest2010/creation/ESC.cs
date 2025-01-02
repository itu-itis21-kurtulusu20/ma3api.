using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.tsclient;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.plugtest2010.creation
{
    [TestFixture]
    public class ESC : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR; // = getDirectory() + "CAdES_InitialPackage\\CAdES-C.SCOK\\TU\\";

        public ESC()
        {
            OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-C.SCOK\\TU\\";
        }

        //static
        //{
        //    Authenticator.setDefault(new EtsiTestAuthenticator()); 
        //}
        [Test]
        public void testESC1()
        {
            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));


            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicyCRL();

            parameters[EParameters.P_REFERENCE_DIGEST_ALG] = DigestAlg.SHA1;

            //parameters for ContentTimeStamp attribute
            TSSettings settings = getTSSettings();

            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = settings;

            //add signer
            bs.addSigner(ESignatureType.TYPE_ESC, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);


            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-C-1.p7s");
        }

        [Test]
        public void testESC2()
        {
            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));

            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicyOCSP();
            
            //parameters for ContentTimeStamp attribute
            TSSettings settings = getTSSettings();

            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = settings;

            //add signer
            bs.addSigner(ESignatureType.TYPE_ESC, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-C-2.p7s");
        }
    }
}