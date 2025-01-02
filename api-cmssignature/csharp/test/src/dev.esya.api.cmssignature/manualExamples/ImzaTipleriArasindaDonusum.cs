using System;
using System.Collections.Generic;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace dev.esya.api.cmssignature.manualExamples
{
    public class ImzaTipleriArasindaDonusum
    {
        private readonly String POLICY_FILE = TestConstants.getDirectory() + "testdata\\support\\policy.xml";
        private readonly String BES_SIGNATURE_FILE = TestConstants.getDirectory() + "testdata\\support\\manual\\1.p7s";

        private readonly String CONVERTED_TO_EST_FILE = TestConstants.getDirectory() +
                                                        "testdata\\support\\manual\\10.p7s";

        public void testImzaTipleriArasindaDonusum()
        {
            byte[] inputBES = AsnIO.dosyadanOKU(BES_SIGNATURE_FILE);

            BaseSignedData sd = new BaseSignedData(inputBES);


            Dictionary<String, Object> params_ = new Dictionary<String, Object>();

            //necessary for getting signaturetimestamp
            params_[EParameters.P_TS_DIGEST_ALG] = DigestAlg.SHA1;
            params_[EParameters.P_TSS_INFO] = getTSSettings();

            ValidationPolicy policy =
                PolicyReader.readValidationPolicy(new FileStream(POLICY_FILE, FileMode.Open, FileAccess.Read));

            //necessary for validating signer certificate according to time of //signaturetimestamp
            params_[EParameters.P_CERT_VALIDATION_POLICY] = policy;

            sd.getSignerList()[0].convert(ESignatureType.TYPE_EST, params_);

            AsnIO.dosyayaz(sd.getEncoded(), CONVERTED_TO_EST_FILE);
        }

        private Object getTSSettings()
        {
            return TestConstants.getTSSettings();
        }
    }
}