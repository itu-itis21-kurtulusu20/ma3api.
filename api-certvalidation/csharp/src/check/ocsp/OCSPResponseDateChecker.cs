using System;
using System.Reflection;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.ocsp;
using tr.gov.tubitak.uekae.esya.api.common.src.util;
using tr.gov.tubitak.uekae.esya.asn.ocsp;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp
{
    /**
     * @author ayetgin
     */
    public class OCSPResponseDateChecker : OCSPResponseChecker
    {
        private static readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);

        protected static String PARAM_OCSP_EXPIRE_THRESHOLD = "ocsp-expire-threshold";
        protected static int DEFAULT_OCSP_EXPIRE_THRESHOLD = 600; // 10dk

        protected override PathValidationResult _check(EOCSPResponse aOCSPResponse, OCSPResponseStatusInfo aOCSPResponseInfo)
        {
            if (aOCSPResponse.getResponseStatus() != OCSPResponseStatus.successful().mValue)
            {
                logger.Error("Response Status SUCCESSFUL degil");
                aOCSPResponseInfo.addDetail(this, ResponseStatusChecker.ResponseStatusCheckStatus.MALFORMED_REQUEST, false);
                return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE;
            }

            int ocspExpireThreshold = getOcspExpireThreshold();

            DateTime baseValidationTimeWithExpireTreshold;
            {
                DateTime? baseValidationTime = mParentSystem.getBaseValidationTime();
                baseValidationTimeWithExpireTreshold = baseValidationTime.Value;
            };
            DateTime? lastRevocationTime = mParentSystem.getLastRevocationTime();

            DateTime? thisUpdate = aOCSPResponse.getBasicOCSPResponse().getProducedAt();

            //baseValidationTime.Value.add(Calendar.SECOND, (-1) *  ocspExpireThreshold);
            baseValidationTimeWithExpireTreshold = baseValidationTimeWithExpireTreshold.Subtract(new TimeSpan(0, 0, ocspExpireThreshold));
            
            /*
            if (baseValidationTime.Value.Subtract(thisUpdate.Value).Subtract(new TimeSpan(0, 0, ocspExpireThreshold)).Milliseconds > 0)
            {
                logger.Error("thisUpdate: " + thisUpdate +
                        "baseValidationTime: " + baseValidationTime + "lastRevocationTime: " + lastRevocationTime +
                        "ocspExpireThreshold: " + ocspExpireThreshold);
                aOCSPResponseInfo.addDetail(this, DateCheckStatus.INVALID_DATE, true);
                return PathValidationResult.OCSP_RESPONSEDATE_EXPIRED;
            }
            */

            if (baseValidationTimeWithExpireTreshold < thisUpdate && thisUpdate < lastRevocationTime)
            {
                aOCSPResponseInfo.addDetail(this, DateCheckStatus.VALID_DATE, true);
                return PathValidationResult.SUCCESS;
            }
            else
            {
                logger.Error("OCSP tarihi geçerli değil " + DateUtil.formatDateByDayMonthYear24hours(baseValidationTimeWithExpireTreshold.ToLocalTime()) + " - [ " + "thisUpdate: " + DateUtil.formatDateByDayMonthYear24hours(thisUpdate.Value.ToLocalTime()) +
                        " - " + " lastRevocationTime: " + DateUtil.formatDateByDayMonthYear24hours(lastRevocationTime.Value.ToLocalTime()) + " ]" +
                        " ocspExpireThreshold: " + ocspExpireThreshold + " sn");
                aOCSPResponseInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
                return PathValidationResult.OCSP_RESPONSEDATE_INVALID;
            }
                
        }

        private int getOcspExpireThreshold()
        {
            if (mCheckParams != null)
            {
                String ocspExpireThreshold = (String)mCheckParams.getParameter(PARAM_OCSP_EXPIRE_THRESHOLD);
                if (ocspExpireThreshold != null)
                    return Convert.ToInt32(ocspExpireThreshold);
            }
            return DEFAULT_OCSP_EXPIRE_THRESHOLD;
        }

    }
}