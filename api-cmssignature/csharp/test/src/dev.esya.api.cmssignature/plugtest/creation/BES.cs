using System;
using System.Collections.Generic;
using System.Text;
using Com.Objsys.Asn1.Runtime;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.asn.attrcert;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.util;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

namespace dev.esya.api.cmssignature.creation.plugtests
{
    [TestFixture]
    public class BES : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR;

        private readonly String ATTRIBUTE_CERTIFICATE_PATH;

        public BES()
        {
            OUTPUT_DIR = getDirectory() + "creation\\plugtests\\bes\\";
            ATTRIBUTE_CERTIFICATE_PATH = getDirectory() + "creation\\plugtests\\attributecertificate.cer";
        }

        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType 
         * attributes
         */

        [Test]
        public void testCreateBES1()
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
            //Since the specified attributes are mandatory for bes,null is given as parameter for optional attributes
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         null, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-1.p7s");
        }


        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime 
         * attributes
         */

        [Test]
        public void testCreateBES2()
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
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-2.p7s");
        }


        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
         * SignerLocation attributes
         */

        [Test]
        public void testCreateBES3()
        {
            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //Since SigningTime and SigningLocation attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new SignerLocationAttr("TURKEY", "KOCAELI", new String[] { "TUBITAK UEKAE", "GEBZE" }));


            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();


            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-3.p7s");
        }


        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
         * SignerAttributes (ClaimedAttributes) attributes
         */

        [Test]
        public void testCreateBES4()
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


            //Since SigningTime and SignerAttributes attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new SignerAttributesAttr(new EClaimedAttributes(new EAttribute[] { attr1 })));

            //create parameters necessary for signature creation
            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            params_[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, params_);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-4.p7s");
        }


        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
         * SignerAttributes (CertifiedAttributes) attributes
         */

        [Test]
        public void testCreateBES5()
        {
            //getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");

            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //Put attributecertificate to parameters necessary for SignerAttributes attribute
            EAttributeCertificate ac = new EAttributeCertificate();
            AsnIO.dosyadanOKU(ac.getObject(), ATTRIBUTE_CERTIFICATE_PATH);

            //Since SigningTime and SignerAttributes attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new SignerAttributesAttr(ac));


            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            
            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-5.p7s");
        }


        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
         * ContentHints attributes
         */

        [Test]
        public void testCreateBES6()
        {
            //getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");

            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //Since SigningTime and ContentHints attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new ContentHintsAttr(new EContentHints("text/plain", new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 7, 1 }))));

            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();


            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-6.p7s");
        }


        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
         * ContentIdentifier attributes
         */

        [Test]
        public void testCreateBES7()
        {
            //getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");

            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //Since SigningTime and ContentIdentifier attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new ContentIdentifierAttr(ASCIIEncoding.ASCII.GetBytes("PL123456789")));

            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            
            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-7.p7s");
        }

        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
         * CommitmentTypeIndication attributes
         */

        [Test]
        public void testCreateBES8()
        {
            //getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");

            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //Since SigningTime and CommitmentTypeIndication attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new CommitmentTypeIndicationAttr(CommitmentType.CREATION));


            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            
            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-8.p7s");
        }

        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
         * ContentTimeStamp attributes
         */

        [Test]
        public void testCreateBES10()
        {
            //getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");

            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //Since SigningTime and ContentTimeStamp attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new ContentTimeStampAttr());

            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //parameters for ContentTimeStamp attribute           
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-10.p7s");
        }

        /**
         * Create BES with MessageDigest, ESSSigningCertificateV1, ContentType, SigningTime,
         * CounterSignature attributes
         */

        [Test]
        public void testCreateBES11()
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

            List<IAttribute> optionalAttributes2 = new List<IAttribute>();
            optionalAttributes2.Add(new SigningTimeAttr(DateTime.UtcNow));
            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes2, parameters);


            //add countersignature with the same certificate and same paremeters.
            //different certificate and parameters may be given
            bs.getSignerList()[0].addCounterSigner(ESignatureType.TYPE_BES, getSignerCertificate(),
                                                   getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes,
                                                   parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-11.p7s");
        }

        /**
         * Create BES with MessageDigest, ESSSigningCertificate V1, ContentType, SigningTime, SignerLocation, 
         * SignerAttributes (ClaimedAttributes), ContentHints, ContentIdentifier, 
         * CommitmentTypeIndication, ContentTimeStamp       
        */

        [Test]
        public void testCreateBES15()
        {
            //getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");

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

            //Specified attributes are optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));
            optionalAttributes.Add(new SignerLocationAttr("TURKEY", "KOCAELİ", new String[] { "TUBITAK UEKAE", "GEBZE" }));
            optionalAttributes.Add(new SignerAttributesAttr(new EClaimedAttributes(new EAttribute[] { attr1 })));
            optionalAttributes.Add(new ContentHintsAttr(new EContentHints("text/plain", new Asn1ObjectIdentifier(new int[] { 1, 2, 840, 113549, 1, 7, 1 }))));
            optionalAttributes.Add(new ContentIdentifierAttr(ASCIIEncoding.ASCII.GetBytes("PL123456789")));
            optionalAttributes.Add(new CommitmentTypeIndicationAttr(CommitmentType.CREATION));
            optionalAttributes.Add(new ContentTimeStampAttr());

            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            
            //parameters for ContentTimeStamp attribute            
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();


            /*List<IAttribute> optionalAttributes2 = new ArrayList<IAttribute>();
            optionalAttributes2.add(new SigningTimeAttr());
            optionalAttributes2.add(new SignerLocationAttr());
            optionalAttributes2.add(new SignerAttributesAttr());
            optionalAttributes2.add(new ContentHintsAttr());
            optionalAttributes2.add(new ContentIdentifierAttr());
            optionalAttributes2.add(new CommitmentTypeIndicationAttr());
            optionalAttributes2.add(new ContentTimeStampAttr());*/
            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);


            //add countersignature with the same certificate and same paremeters.
            //different certificate and parameters may be given
            bs.getSignerList()[0].addCounterSigner(ESignatureType.TYPE_BES, getSignerCertificate(),
                                                   getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes,
                                                   parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-15.p7s");
        }

        /**
         * Create BES with MessageDigest, ESSSigningCertificateV2, ContentType, SigningTime 
         * attributes
         */

        [Test]
        public void testCreateBES16()
        {
            //getPolicy() = new FileInputStream(CMSSignatureTest.INPUT_DIRECTORY_PATH+"creation//plugtests//policy.xml");

            BaseSignedData bs = new BaseSignedData();

            //add content which will be signed
            bs.addContent(getSimpleContent());

            //SigningTime attribute is optional,add them to optional attributes list
            List<IAttribute> optionalAttributes = new List<IAttribute>();
            optionalAttributes.Add(new SigningTimeAttr(DateTime.UtcNow));

            //create parameters necessary for signature creation
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for certificate validation.By default,certificate validation is done.But if the user 
            //does not want certificate validation,he can add P_VALIDATE_CERTIFICATE_BEFORE_SIGNING parameter with its value set to false
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            //Since reference digest alg is  different from SHA-1,the mandatory attribute SigningCertificateV2 will be added to signature
            //instead of SigningCertificateV1
            parameters[EParameters.P_REFERENCE_DIGEST_ALG] = DigestAlg.SHA256;

            //add signer
            bs.addSigner(ESignatureType.TYPE_BES, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            //write the contentinfo to file
            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-BES-16.p7s");
        }
    }
}