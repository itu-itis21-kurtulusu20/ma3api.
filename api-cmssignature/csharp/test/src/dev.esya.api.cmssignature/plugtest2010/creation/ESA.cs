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
    public class ESA : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR; // = getDirectory() + "CAdES_InitialPackage//CAdES-A.SCOK//TU//";

        public ESA()
        {
            OUTPUT_DIR = getDirectory() + "CAdES_InitialPackage//CAdES-A.SCOK//TU//";
        }

        //static
        //{
        //    Authenticator.setDefault(new EtsiTestAuthenticator()); 
        //}

        public BaseSignedData getEsxLongType1withCRL()
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
            bs.addSigner(ESignatureType.TYPE_ESXLong_Type1, getSignerCertificate(),
                         getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, parameters);

            return bs;
        }

        public BaseSignedData getEsxLongType2withCRL()
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
            bs.addSigner(ESignatureType.TYPE_ESXLong_Type2, getSignerCertificate(),
                         getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, parameters);

            return bs;
        }

        public BaseSignedData getEsxLongType1withOCSP()
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
            bs.addSigner(ESignatureType.TYPE_ESXLong_Type1, getSignerCertificate(),
                         getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, parameters);

            return bs;
        }

        public BaseSignedData getEsxLongType2withOCSP()
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
            bs.addSigner(ESignatureType.TYPE_ESXLong_Type2, getSignerCertificate(),
                         getSignerInterface(SignatureAlg.RSA_SHA1), optionalAttributes, parameters);

            return bs;
        }

        public BaseSignedData getEsxLongwithCRL()
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
            bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            return bs;
        }

        public BaseSignedData getEsxLongwithOCSP()
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
            bs.addSigner(ESignatureType.TYPE_ESXLong, getSignerCertificate(), getSignerInterface(SignatureAlg.RSA_SHA1),
                         optionalAttributes, parameters);

            return bs;
        }

        /**
         * Add ArchiveTimeStampV2 attribute to ESXLong1(with crl references) signature
         */

        [Test]
        public void testConvertToESAFromESXLong1_1()
        {
            byte[] inputESXLong = getEsxLongType1withCRL().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputESXLong);


            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the esctimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-1.p7s");
        }


        /**
         * Add ArchiveTimeStampV2 attribute to ESXLong2(with crl references) signature
         */

        [Test]
        public void testConvertToESAFromESXLong2_2()
        {
            byte[] inputESXLong = getEsxLongType2withCRL().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputESXLong);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the referencetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-2.p7s");
        }


        /**
         * Add ArchiveTimeStampV2 attribute to ESXLong1(with ocsp references) signature
         */

        [Test]
        public void testConvertToESAFromESXLong1_3()
        {
            byte[] inputESXLong = getEsxLongType1withOCSP().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputESXLong);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the esctimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-3.p7s");
        }

        /**
         * Add ArchiveTimeStampV2 attribute to ESXLong2(with ocsp references) signature
         */

        [Test]
        public void testConvertToESAFromESXLong2_4()
        {
            byte[] inputESXLong = getEsxLongType2withOCSP().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputESXLong);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the referencetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-4.p7s");
        }

        /**
         * Add 2 ArchiveTimeStampV2 attributes to ESA signature
         */

        [Test]
        public void testConvertToESAFromESA_5()
        {
            byte[] inputESA = getEsxLongType1withOCSP().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputESA);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the archivetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);


            //Thread.currentThread().sleep(2000);


            //create necessary parameters for convertion
            Dictionary<String, Object> params2 = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            params2[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            params2[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the archivetimestamp
            params2[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
            bs2.getSignerList()[0].convert(ESignatureType.TYPE_ESA, params2);

            AsnIO.dosyayaz(bs2.getEncoded(), OUTPUT_DIR + "Signature-C-A-5.p7s");
        }

        /**
         * 
         * Add 2 ArchiveTimeStampV2 attributes to ESA signature
         */

        [Test]
        public void testConvertToESAFromESA_6()
        {
            byte[] inputESA = getEsxLongType2withOCSP().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputESA);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the archivetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            //Thread.currentThread().sleep(2000);

            //create necessary parameters for convertion
            Dictionary<String, Object> params2 = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            params2[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            params2[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the archivetimestamp
            params2[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
            bs2.getSignerList()[0].convert(ESignatureType.TYPE_ESA, params2);

            AsnIO.dosyayaz(bs2.getEncoded(), OUTPUT_DIR + "Signature-C-A-6.p7s");
        }

        /**
         * Add ArchiveTimeStampV2 attribute to EST signature
         */

        [Test]
        public void testConvertToESAFromEST_7()
        {
            byte[] inputEST = getEsxLongwithCRL().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputEST);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the signaturetimestamp
            //necessary for finding certificate and revocation values of the signer certificate
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-7.p7s");
        }


        /**
         * Add 2 ArchiveTimeStampV2 attributes to ESA signature
         */

        [Test]
        public void testConvertToESAFromESA_8()
        {
            byte[] inputESA = getEsxLongwithCRL().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputESA);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the sarchivetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            //Thread.currentThread().sleep(2000);

            //create necessary parameters for convertion
            Dictionary<String, Object> params2 = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            params2[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            params2[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the sarchivetimestamp
            params2[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();
            BaseSignedData bs2 = new BaseSignedData(bs.getEncoded());
            bs2.getSignerList()[0].convert(ESignatureType.TYPE_ESA, params2);

            AsnIO.dosyayaz(bs2.getEncoded(), OUTPUT_DIR + "Signature-C-A-8.p7s");
        }

        /**
         * Add ArchiveTimeStampV2 attributes to ESX signature
         */

        [Test]
        public void testConvertToESAFromEST_9()
        {
            byte[] inputEST = getEsxLongwithOCSP().getEncoded();

            BaseSignedData bs = new BaseSignedData(inputEST);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the signaturetimestamp
            //necessary for finding certificate and revocation values of the signer certificate
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-9.p7s");
        }
    }
}