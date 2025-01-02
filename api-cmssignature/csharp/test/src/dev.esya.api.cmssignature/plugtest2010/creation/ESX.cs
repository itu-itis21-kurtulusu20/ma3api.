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
    public class ESX : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR; // = getDirectory() + "CAdES_InitialPackage\\CAdES-X.SCOK\\TU\\";

        public ESX()
        {
            OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-X.SCOK\\TU\\";
        }

        //static
        //{
        //    Authenticator.setDefault(new EtsiTestAuthenticator()); 
        //}
        [Test]
        public void testCreateESX1()
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

            //parameters for ContentTimeStamp attribute
            TSSettings settings = getTSSettings();

            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = settings;

            //add signer
            bs.addSigner(ESignatureType.TYPE_ESX_Type1, getSignerCertificate(),
                         getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-X-1.p7s");
        }

        [Test]
        public void testCreateESX2()
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

            //parameters for ContentTimeStamp attribute
            TSSettings settings = getTSSettings();

            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = settings;

            //add signer
            bs.addSigner(ESignatureType.TYPE_ESX_Type2, getSignerCertificate(),
                         getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-X-2.p7s");
        }

        [Test]
        public void testCreateESX3()
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
            bs.addSigner(ESignatureType.TYPE_ESX_Type1, getSignerCertificate(),
                         getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-X-3.p7s");
        }

        [Test]
        public void testCreateESX4()
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
            bs.addSigner(ESignatureType.TYPE_ESX_Type2, getSignerCertificate(),
                         getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-X-4.p7s");
        }
    }
}