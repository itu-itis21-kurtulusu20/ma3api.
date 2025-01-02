using System;
using System.Collections.Generic;
using System.IO;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.infra.certstore;
using tr.gov.tubitak.uekae.esya.api.infra.certstore.ops;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.convertion.plugtests
{
    [TestFixture]
    public class ESX1 : CMSSignatureTest
    {
        private readonly String INPUT_DIR; // = getDirectory() + "convertion\\plugtests\\esc\\";
        private readonly String OUTPUT_DIR; // = getDirectory() + "convertion\\plugtests\\esx1\\";

        public ESX1()
        {
            INPUT_DIR = getDirectory() + "convertion\\plugtests\\esc\\";
            OUTPUT_DIR = getDirectory() + "convertion\\plugtests\\esx1\\";
        }

        [Test]
        public void testAddToCertStore()
        {
            CertStore cs = new CertStore();
            CertStoreCRLOps crlOps = new CertStoreCRLOps(cs);

            crlOps.writeCRL(AsnIO.dosyadanOKU("E:\\test\\imza\\creation\\plugtests\\certscrls\\UGSIL.crl"), 1L);

            CertStoreOCSPOps ocspOps = new CertStoreOCSPOps(cs);
            ocspOps.writeOCSPResponseAndCertificate(new EOCSPResponse(AsnIO.dosyadanOKU("C://basicocspresp.dat")), ECertificate.readFromFile("E:\\test\\imza\\creation\\plugtests\\yasemin_sign.cer"));

            CertStoreCertificateOps cerOps = new CertStoreCertificateOps(cs);
            cerOps.writeCertificate(
                AsnIO.dosyadanOKU("E:\\test\\imza\\creation\\plugtests\\certscrls\\root\\UGKOK.crt"), 1);
            cerOps.writeCertificate(
                AsnIO.dosyadanOKU("E:\\test\\imza\\creation\\plugtests\\certscrls\\root\\UGOCSP.cer"), 1);
        }

        /**
         * Create ESX1 signature with just CRL revocation references
         * Possible input: Signature-C-C-1.p7s
         */

        [Test]
        public void testConvertESX1FromESC_1()
        {
            byte[] inputESC = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-C-1.p7s");
            BaseSignedData bs = new BaseSignedData(inputESC);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting esctimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the signaturetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESX_Type1, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-X-1.p7s");
        }


        /**
         * Create ESX1 signature with just OCSP revocation references
         * Possible input: Signature-C-C-2.p7s
         */

        [Test]
        public void testConvertESX1FromESC_3()
        {
            byte[] inputESC = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-C-2.p7s");
            BaseSignedData bs = new BaseSignedData(inputESC);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting esctimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the signaturetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESX_Type1, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-X-3.p7s");
        }
    }
}