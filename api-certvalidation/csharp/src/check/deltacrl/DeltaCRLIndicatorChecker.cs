using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl
{
    /**
     * Checks the validity of the given delta-CRL by validating the following condition;
     * 
     * RFC 3280 5.2.4 Delta CRL Indicator says:
     * When a conforming CRL issuer generates a delta CRL, the delta CRL
     * MUST include a critical delta CRL indicator extension.
     * @author IH
     */
    public class DeltaCRLIndicatorChecker : DeltaCRLChecker
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        public class DeltaCRLIndicatorcheckStatus : CheckStatus
        {
            public static readonly DeltaCRLIndicatorcheckStatus DELTA_CRL_INDICATOR_EXISTS = new DeltaCRLIndicatorcheckStatus(_enum.DELTA_CRL_INDICATOR_EXISTS);
            public static readonly DeltaCRLIndicatorcheckStatus DELTA_CRL_INDICATOR_NOT_EXISTS = new DeltaCRLIndicatorcheckStatus(_enum.DELTA_CRL_INDICATOR_NOT_EXISTS);

            enum _enum
            {
                DELTA_CRL_INDICATOR_EXISTS,
                DELTA_CRL_INDICATOR_NOT_EXISTS
            }
            readonly _enum mValue;
            DeltaCRLIndicatorcheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.DELTA_CRL_INDICATOR_EXISTS:
                        return Resource.message(Resource.DELTA_CRL_INDICATOR_VAR);
                    case _enum.DELTA_CRL_INDICATOR_NOT_EXISTS:
                        return Resource.message(Resource.DELTA_CRL_INDICATOR_YOK);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * DeltaCRLIndicator eklentisini kontrol eder
         */
        protected override PathValidationResult _check(ECRL aDeltaCRL, CRLStatusInfo aCRLStatusInfo)
        {
            EExtension extension = aDeltaCRL.getCRLExtensions().getDeltaCRLIndicator();
            if (extension == null)
            {
                logger.Error("Delta silde Delta CRL Indicator extension yok");
                aCRLStatusInfo.addDetail(this, DeltaCRLIndicatorcheckStatus.DELTA_CRL_INDICATOR_NOT_EXISTS, false);
                return PathValidationResult.CRL_DELTACRLINDICATOR_CONTROL_FAILURE;
            }
            if (logger.IsDebugEnabled)
                logger.Debug("Delta silde Delta CRL Indicator extension var");
            aCRLStatusInfo.addDetail(this, DeltaCRLIndicatorcheckStatus.DELTA_CRL_INDICATOR_EXISTS, true);
            return PathValidationResult.SUCCESS;
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.DELTA_CRL_INDICATOR_KONTROLU);
        }

    }
}
