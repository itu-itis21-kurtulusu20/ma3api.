using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.util;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

namespace dev.esya.api.cmssignature.plugtest2010.creation
{
    [TestFixture]
    public class EPES : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR; // = getDirectory() + "CAdES_InitialPackage\\CAdES-EPES.SCOK\\TU\\";
        private readonly String SIGNATURE_POLICY_PATH; // = OUTPUT_DIR + "TARGET-SIGPOL-ETSI3.der";
        private readonly int[] SIGNATURE_POLICY_OID = new[] {1, 2, 3, 4, 5, 1};

        public EPES()
        {
            OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage\\CAdES-EPES.SCOK\\TU\\";
            SIGNATURE_POLICY_PATH = OUTPUT_DIR + "TARGET-SIGPOL-ETSI3.der";
        }

        //static
        //{
        //    Authenticator.setDefault(new EtsiTestAuthenticator()); 
        //}

        /**
         * Create EPES with MessageDigest, ESSSigningCertificate V1, ContentType, SigningTime, 
         * SignaturePolicyIdentifier attributes
         * SignaturePolicyIdentifier Oid=1.2.3.4.5.1
         * File=TARGET-SIGPOL-ETSI3.der
         */

        [Test]
        public void testCreateEPES1()
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



            //necessary parameters for signaturepolicyidentifier attribute which is mandatory for EPES
            byte[] policyValue = AsnIO.dosyadanOKU(SIGNATURE_POLICY_PATH);
            parameters[EParameters.P_POLICY_VALUE] = policyValue;
            parameters[EParameters.P_POLICY_ID] = SIGNATURE_POLICY_OID;
            parameters[EParameters.P_POLICY_DIGEST_ALGORITHM] = DigestAlg.SHA1;

            //add signer
            bs.addSigner(ESignatureType.TYPE_EPES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-EPES-1.p7s");
        }


        /**
         * Create EPES with MessageDigest, ESSSigningCertificate V1, ContentType, SigningTime, 
         * SignerLocation, SignerAttributes (ClaimedAttributes),  CommitmentTypeIndication, 
         * SignaturePolicyIdentifier attributes
         * SignaturePolicyIdentifier Oid=1.2.3.4.5.1
         * File=TARGET-SIGPOL-ETSI3.der
         */

        [Test]
        public void testCreateEPES2()
        {
            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //create the claimed role attribute for signerattributes attribute
            EAttribute attr1 = new EAttribute(new Attribute());
            attr1.setType(new Asn1ObjectIdentifier(new int[] { 1, 3, 6, 7, 8, 10 }));
            Asn1UTF8String role = new Asn1UTF8String("supervisor");
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            role.Encode(encBuf);
            attr1.addValue(encBuf.MsgCopy);


            //Since SigningTime, SignerLocation, SignerAttributes (ClaimedAttributes),  
            //CommitmentTypeIndication attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new SignerLocationAttr("TURKEY", "KOCAELİ", new String[] { "TUBITAK UEKAE", "GEBZE" }));
            optionalAttributes.Add(new SignerAttributesAttr(new EClaimedAttributes(new EAttribute[] { attr1 })));
            optionalAttributes.Add(new CommitmentTypeIndicationAttr(CommitmentType.CREATION));



            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //necessary parameters for signaturepolicyidentifier attribute which is mandatory for EPES
            byte[] policyValue = AsnIO.dosyadanOKU(SIGNATURE_POLICY_PATH);
            parameters[EParameters.P_POLICY_VALUE] = policyValue;
            parameters[EParameters.P_POLICY_ID] = SIGNATURE_POLICY_OID;
            parameters[EParameters.P_POLICY_DIGEST_ALGORITHM] = DigestAlg.SHA1;

            //add signer
            bs.addSigner(ESignatureType.TYPE_EPES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-EPES-2.p7s");
        }
    }
}