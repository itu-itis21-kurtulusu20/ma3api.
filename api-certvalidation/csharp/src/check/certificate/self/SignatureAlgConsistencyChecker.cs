using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    /**
     * Checks if the signature Algorithms specified in the certificate's signature
     * field and in the signatureAlgorithm field are equal. 
     */
    public class SignatureAlgConsistencyChecker : CertificateSelfChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        [Serializable]
        public class SignatureAlgConsistencyCheckStatus : CheckStatus
        {

            public static readonly SignatureAlgConsistencyCheckStatus SIGNATURE_ALG_MATCHES = new SignatureAlgConsistencyCheckStatus(_enum.SIGNATURE_ALG_MATCHES);
            public static readonly SignatureAlgConsistencyCheckStatus SIGNATURE_ALG_MISMATCH = new SignatureAlgConsistencyCheckStatus(_enum.SIGNATURE_ALG_MISMATCH);
            enum _enum
            {
                SIGNATURE_ALG_MATCHES,
                SIGNATURE_ALG_MISMATCH
            }
            readonly _enum mValue;
            SignatureAlgConsistencyCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.SIGNATURE_ALG_MATCHES:
                        return Resource.message(Resource.SIGNATURE_ALG_AYNI);
                    case _enum.SIGNATURE_ALG_MISMATCH:
                        return Resource.message(Resource.SIGNATURE_ALG_FARKLI);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * Sertifika icindeki tbscertificate.signature sertifika icindeki signatureAlgorithm de�erlerini kontrol eder
         * 4.1.2.3 Signature field MUST contain the same algorithm identifier as the
         * signatureAlgorithm field in the sequence Certificate
         */
        protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
        {
            if (logger.IsDebugEnabled)
                logger.Debug("Sertifika içindeki algoritmalar kontrol edilecek");

            ECertificate cert = aCertStatusInfo.getCertificate();
            EAlgorithmIdentifier tbsAlg = new EAlgorithmIdentifier(cert.getObject().tbsCertificate.signature);

            if (!tbsAlg.Equals(cert.getSignatureAlgorithm()))
            {
                logger.Error("Sertifika içindeki algoritmalar farkli");
                aCertStatusInfo.addDetail(this, SignatureAlgConsistencyCheckStatus.SIGNATURE_ALG_MISMATCH, false);
                return PathValidationResult.SIGNATURE_ALGORITHM_DIFFERENT;
            }
            else
            {
                if (logger.IsDebugEnabled)
                    logger.Debug("Sertifika içindeki algoritmalar aynı");
                aCertStatusInfo.addDetail(this, SignatureAlgConsistencyCheckStatus.SIGNATURE_ALG_MATCHES, true);
                return PathValidationResult.SUCCESS;
            }
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.SIGNATURE_ALG_AYNIMI_KONTROLU);
        }

    }
}
