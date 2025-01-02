using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.convertion.plugtests
{
    [TestFixture]
    public class ESA : CMSSignatureTest
    {
        private readonly String INPUT_DIR; // = getDirectory() + "creation\\plugtests\\";
        private readonly String OUTPUT_DIR; // = getDirectory() + "convertion\\plugtests\\esa\\";

        public ESA()
        {
            INPUT_DIR = getDirectory() + "creation\\plugtests\\";
            OUTPUT_DIR = getDirectory() + "convertion\\plugtests\\esa\\";
        }

        /**
         * Add ArchiveTimeStampV2 attribute to ESXLong1(with crl references) signature
         */

        [Test]
        public void testConvertToESAFromESXLong1_1()
        {
            byte[] inputESXLong = AsnIO.dosyadanOKU(INPUT_DIR + "esxlong1\\Signature-C-XL-1.p7s");

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
            byte[] inputESXLong = AsnIO.dosyadanOKU(INPUT_DIR + "esxlong2\\Signature-C-XL-2.p7s");

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
            byte[] inputESXLong = AsnIO.dosyadanOKU(INPUT_DIR + "esxlong1\\Signature-C-XL-3.p7s");

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
            byte[] inputESXLong = AsnIO.dosyadanOKU(INPUT_DIR + "esxlong2//Signature-C-XL-4.p7s");

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
            byte[] inputESA = AsnIO.dosyadanOKU(INPUT_DIR + "esa//Signature-C-A-3.p7s");

            BaseSignedData bs = new BaseSignedData(inputESA);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the archivetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);
            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-5.p7s");
        }

        /**
         * 
         * Add 2 ArchiveTimeStampV2 attributes to ESA signature
         */

        [Test]
        public void testConvertToESAFromESA_6()
        {
            byte[] inputESA = AsnIO.dosyadanOKU(INPUT_DIR + "esa//Signature-C-A-4.p7s");

            BaseSignedData bs = new BaseSignedData(inputESA);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the archivetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);
            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-6.p7s");
        }

        /**
         * Add ArchiveTimeStampV2 attribute to EST signature
         */

        [Test]
        public void testConvertToESAFromEST_7()
        {
            byte[] inputEST = AsnIO.dosyadanOKU(INPUT_DIR + "est\\Signature-C-T-1.p7s");

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
            byte[] inputESA = AsnIO.dosyadanOKU(INPUT_DIR + "esa\\Signature-C-A-7.p7s");

            BaseSignedData bs = new BaseSignedData(inputESA);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting archivetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the sarchivetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);
            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-8.p7s");
        }

        /**
         * Add 2 ArchiveTimeStampV2 attributes to EST signature
         */

        [Test]
        public void testConvertToESAFromEST_9()
        {
            byte[] inputEST = AsnIO.dosyadanOKU(INPUT_DIR + "est\\Signature-C-T-1.p7s");

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
            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESA, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-A-9.p7s");
        }
    }
}