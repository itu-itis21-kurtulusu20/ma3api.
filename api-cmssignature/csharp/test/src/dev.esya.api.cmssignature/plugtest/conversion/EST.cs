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
    public class EST : CMSSignatureTest
    {
        private readonly String OUTPUT_DIR; // = getDirectory() + "convertion\\plugtests\\est\\";

        public EST()
        {
            OUTPUT_DIR = getDirectory() + "convertion\\plugtests\\est\\";
        }

        /**
         * Signature with MessageDigest, ESSSigningCertificate V1, ContentType, 
         * SigningTime, SignatureTimeStamp attributes.
         * Possible input data Signature-C-BES-2.p7s
         */

        [Test]
        public void testConvertToESTFromBES()
        {
            byte[] inputBES = AsnIO.dosyadanOKU(getDirectory() + "creation\\plugtests\\bes\\Signature-C-BES-2.p7s");

            BaseSignedData sd = new BaseSignedData(inputBES);


            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //necessary for getting signaturetimestamp
            parameters[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            parameters[EParameters.P_TSS_INFO] = getTSSettings();


            //necessary for validating signer certificate according to time of signaturetimestamp
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            sd.getSignerList()[0].convert(ESignatureType.TYPE_EST, parameters);

            AsnIO.dosyayaz(sd.getEncoded(), OUTPUT_DIR + "Signature-C-T-1.p7s");
        }
    }
}