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
    public class ESXLong2 : CMSSignatureTest
    {
        private readonly String INPUT_DIR; // = getDirectory() + "convertion\\plugtests\\esc\\";
        private readonly String OUTPUT_DIR; // = getDirectory() + "convertion\\plugtests\\esxlong2\\";

        public ESXLong2()
        {
            INPUT_DIR = getDirectory() + "convertion\\plugtests\\esc\\";
            OUTPUT_DIR = getDirectory() + "convertion\\plugtests\\esxlong2\\";
        }

        /**
         * Create ESXLong2 signature with just CRL revocation references
         * Possible input: Signature-C-C-1.p7s
         * The values of the references must be in the certificate store
         */

        [Test]
        public void testConvertESXLong2FromESC_2()
        {
            byte[] inputESC = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-C-1.p7s");
            BaseSignedData bs = new BaseSignedData(inputESC);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting referencetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the signaturetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESXLong_Type2, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-XL-2.p7s");
        }


        /**
         * Create ESXLong2 signature with just OCSP revocation references
         * Possible input: Signature-C-C-2.p7s
         * The values of the references must be in the certificate store
         */

        [Test]
        public void testConvertESXLong2FromESC_4()
        {
            byte[] inputESC = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-C-2.p7s");
            BaseSignedData bs = new BaseSignedData(inputESC);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting referencetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();

            //necessary for finding certificate and revocation values of the signaturetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESXLong_Type2, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-XL-4.p7s");
        }
    }
}