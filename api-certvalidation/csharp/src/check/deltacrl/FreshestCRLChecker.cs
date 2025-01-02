using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.asn.x509;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl
{
    /**
     * Checks the validity of the following condition for the delta-CRL RFC 3280
     * 5.2.6 Freshest CRL says: This extension MUST NOT appear in delta CRLs.
     */
    public class FreshestCRLChecker : DeltaCRLChecker
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        [Serializable]
        public class FreshestCRLCheckStatus : CheckStatus
        {

            public static readonly FreshestCRLCheckStatus FRESHEST_CRL_EXISTS = new FreshestCRLCheckStatus(_enum.FRESHEST_CRL_EXISTS);
            public static readonly FreshestCRLCheckStatus FRESHEST_CRL_NOT_EXISTS = new FreshestCRLCheckStatus(_enum.FRESHEST_CRL_NOT_EXISTS);

            enum _enum
            {
                FRESHEST_CRL_EXISTS,
                FRESHEST_CRL_NOT_EXISTS
            }
            
            readonly _enum mValue;
            
            FreshestCRLCheckStatus(_enum aEnum)
            {
                mValue = aEnum;
            }
            
            public String getText()
            {
                switch (mValue)
                {
                    case _enum.FRESHEST_CRL_EXISTS:
                        return Resource.message(Resource.FRESHEST_CRL_VAR);
                    case _enum.FRESHEST_CRL_NOT_EXISTS:
                        return Resource.message(Resource.FRESHEST_CRL_YOK);

                    default:
                        return Resource.message(Resource.KONTROL_SONUCU);
                }
            }
        }

        /**
         * FreshestCRL eklentisini kontrol eder
         */
        protected override PathValidationResult _check(ECRL aDeltaCRL, CRLStatusInfo aCRLStatusInfo)
        {
            Extension extension = aDeltaCRL.getCRLExtensions().getFreshestCRL();
            if (extension == null)
            {
                logger.Debug("Delta silde FreshestCRL extension yok");
                aCRLStatusInfo.addDetail(this, FreshestCRLCheckStatus.FRESHEST_CRL_NOT_EXISTS, true);
                return PathValidationResult.SUCCESS;
            }
            logger.Error("Delta silde FreshestCRL extension var");
            aCRLStatusInfo.addDetail(this, FreshestCRLCheckStatus.FRESHEST_CRL_EXISTS, false);
            return PathValidationResult.CRL_FRESHESTCRL_CONTROL_FAILURE;
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.FRESHEST_CRL_KONTROLU);
        }
    }
}
