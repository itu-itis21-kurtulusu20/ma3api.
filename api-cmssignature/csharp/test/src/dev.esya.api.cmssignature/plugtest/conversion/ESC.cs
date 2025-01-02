using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.convertion.plugtests
{
    [TestFixture]
    public class ESC : CMSSignatureTest
    {
        private readonly String INPUT_DIR; // = getDirectory() + "convertion\\plugtests\\est\\";
        private readonly String OUTPUT_DIR; // = getDirectory() + "convertion\\plugtests\\esc\\";

        public ESC()
        {
            INPUT_DIR = getDirectory() + "convertion\\plugtests\\est\\";
            OUTPUT_DIR = getDirectory() + "convertion\\plugtests\\esc\\";
        }

        /**
         * ESC With just crl references(not ocsp)
         * Possible input data: Signature-C-T-1.p7s 
         */

        [Test]
        public void testConvertToESCFromEST_1()
        {
            byte[] inputEST = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-T-1.p7s");

            BaseSignedData bs = new BaseSignedData(inputEST);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for signer certificate validation to find references
            //because in specification,just crls wanted,policy file must be given with just crl finders
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicyCRL();

            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESC, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-C-1.p7s");
        }


        /**
         * ESC With just basicocsp references(not crl)
         * Possible input data: Signature-C-T-1.p7s 
         */

        [Test]
        public void testConvertToESCFromEST_2()
        {
            byte[] inputEST = AsnIO.dosyadanOKU(INPUT_DIR + "Signature-C-T-1.p7s");

            BaseSignedData bs = new BaseSignedData(inputEST);

            //create necessary parameters for convertion
            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for signer certificate validation to find references
            //because in specification,just ocsps wanted,policy file must be given with just ocsp finders
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicyOCSP();


            bs.getSignerList()[0].convert(ESignatureType.TYPE_ESC, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), OUTPUT_DIR + "Signature-C-C-2.p7s");
        }
    }
}