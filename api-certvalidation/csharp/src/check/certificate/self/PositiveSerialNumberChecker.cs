using System;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.self
{
    /**
     * Checks if the serial number of the certificate is positive. 
     */
    public class PositiveSerialNumberChecker : CertificateSelfChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        [Serializable]
        public class SerialNumberPositiveCheckStatus : CheckStatus
        {
            public static readonly SerialNumberPositiveCheckStatus SERIAL_NO_POSITIVE = new SerialNumberPositiveCheckStatus(_enum.SERIAL_NO_POSITIVE);
            public static readonly SerialNumberPositiveCheckStatus SERIAL_NO_NEGATIVE = new SerialNumberPositiveCheckStatus(_enum.SERIAL_NO_NEGATIVE);
            enum _enum
            {
                SERIAL_NO_POSITIVE,
                SERIAL_NO_NEGATIVE
            }

            readonly _enum mValue;
            SerialNumberPositiveCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.SERIAL_NO_POSITIVE:
                        return Resource.message(Resource.SERI_NO_POZITIF);
                    case _enum.SERIAL_NO_NEGATIVE:
                        return Resource.message(Resource.SERI_NO_NEGATIF);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * Sertifikanın seri numarasını kontrol eder.
         * 4.1.2.2 Serial number MUST be a positive integer
         */
        protected override PathValidationResult _check(CertificateStatusInfo aCertStatusInfo)
        {
            logger.Debug("Seri numara kontrolü yapılacak.");

            ECertificate certificate = aCertStatusInfo.getCertificate();
            BigInteger serialNumber = certificate.getSerialNumber();

            if (!serialNumber.IsNegative())
            {
                if (logger.IsDebugEnabled)
                    logger.Debug("Sertifika seri numarası pozitif tam sayı: " + serialNumber);
                aCertStatusInfo.addDetail(this, SerialNumberPositiveCheckStatus.SERIAL_NO_POSITIVE, true);
                return PathValidationResult.SUCCESS;
            }
            logger.Error("Sertifika seri numarası negatif tam sayı: " + serialNumber);
            aCertStatusInfo.addDetail(this, SerialNumberPositiveCheckStatus.SERIAL_NO_NEGATIVE, false);

            return PathValidationResult.SERIALNUMBER_NOT_POSITIVE;
        }


        public override String getCheckText()
        {
            return Resource.message(Resource.SERI_NO_KONTROLU);
        }

    }
}
