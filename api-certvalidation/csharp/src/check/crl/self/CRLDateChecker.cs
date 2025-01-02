using System;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.common.src.util;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self
{
    /**
    * Checks the validity of date information in the CRL
    * todo fix last life span of crl 
    */
    public class CRLDateChecker : CRLSelfChecker
    {
        private static ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        
        /**
        * SIL tarihinin geçerli olup olmadığını kontrol eder
        */
        protected override PathValidationResult _check(ECRL aCRL, CRLStatusInfo aCRLStatusInfo)
        {
            try
            {
                DateTime? crlThisUpdate = aCRL.getThisUpdate();
                DateTime? crlNextUpdate = aCRL.getNextUpdate();

                bool doNotUsePastRevocationInfo = mParentSystem.isDoNotUsePastRevocationInfo();

                logger.Debug("doNotUsePastRevocationInfo: " + doNotUsePastRevocationInfo);

                DateTime? baseValidationTime = mParentSystem.getBaseValidationTime();
                DateTime? lastRevocationTime = mParentSystem.getLastRevocationTime();

                if (doNotUsePastRevocationInfo)
                {
                    if (baseValidationTime < crlThisUpdate && crlThisUpdate < lastRevocationTime)
                    {
                        aCRLStatusInfo.addDetail(this, DateCheckStatus.VALID_DATE, true);
                        return PathValidationResult.SUCCESS;
                    }
                    else
                    {
                        logger.Error("Sil tarihi geçerli değil " + DateUtil.formatDateByDayMonthYear24hours(baseValidationTime.Value.ToLocalTime()) + " - [ " + DateUtil.formatDateByDayMonthYear24hours(crlThisUpdate.Value.ToLocalTime()) + " - " + DateUtil.formatDateByDayMonthYear24hours(crlNextUpdate.Value.ToLocalTime()) + " ]");
                        aCRLStatusInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
                    }
                }
                else
                {
                    if (baseValidationTime < crlNextUpdate && crlThisUpdate < lastRevocationTime)
                    {
                        aCRLStatusInfo.addDetail(this, DateCheckStatus.VALID_DATE, true);
                        return PathValidationResult.SUCCESS;
                    }
                    else
                    {
                        logger.Error("Sil tarihi geçerli değil " + DateUtil.formatDateByDayMonthYear24hours(baseValidationTime.Value.ToLocalTime()) + " - [ " + DateUtil.formatDateByDayMonthYear24hours(crlThisUpdate.Value.ToLocalTime()) + " - " + DateUtil.formatDateByDayMonthYear24hours(crlNextUpdate.Value.ToLocalTime()) + " ]");
                        aCRLStatusInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
                    }
                }                
            }

            catch (Exception x)
            {
                logger.Error(x);
                aCRLStatusInfo.addDetail(this, DateCheckStatus.CORRUPT_DATE_INFO, false);
            }
            return PathValidationResult.CRL_EXPIRED;
        }

        public override String getCheckText()
        {
            return Resource.message(Resource.SIL_TARIH_KONTROLU);
        }
    }
}
