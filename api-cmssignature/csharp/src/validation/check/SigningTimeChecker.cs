using System;
using System.Collections.Generic;
using System.Reflection;
using Com.Objsys.Asn1.Runtime;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.src.util;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks time ordering between time in signingtime attribute and time in signaturetimestamp attribute
 * @author aslihan.kubilay
 *
 */
    public class SigningTimeChecker : BaseChecker
    {
        public static readonly int MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS = 86340;
        public static readonly int MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS = 0;
        public static readonly int MILLIS_IN_SECOND = 1000;

        private readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private readonly ESignedData mSignedData = null;
      
        public SigningTimeChecker(ESignedData aSignedData)
        {
            mSignedData = aSignedData;
        }

        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.SIGNING_TIME_CHECKER), typeof(SigningTimeChecker));

            List<EAttribute> stAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_signingTime);
            if (stAttrs.Count != 0)
            {
                EAttribute stAttr = stAttrs[0];
                Time time = new Time();
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(stAttr.getValue(0));
                try
                {
                    time.Decode(decBuf);
                }
                catch (Exception aEx)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNING_TIME_ATTRIBUTE_DECODE_ERROR), aEx));
                    return false;
                }

                DateTime? signingTime = UtilTime.timeToDate(time);

                ETSTInfo tstInfo = null;
                try
                {
                    tstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
                }
                catch (Exception aEx)
                {
                    aCheckerResult.addMessage(new ValidationMessage("Error in decoding tstinfo", aEx));
                    return false;
                }

                DateTime? stsTime = null;
                try
                {
                    stsTime = tstInfo.getTime();
                }
                catch (Exception aEx)
                {
                    aCheckerResult.addMessage(new ValidationMessage("Error in getting time information from tstinfo", aEx));
                    return false;
                }
        
                int signingTimeTolerance = (int)getParameters()[EParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS];
                if (signingTimeTolerance < MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS || signingTimeTolerance > MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS)
                {
                    String errorMessage = "Signing time tolerance value must be between the values" + MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS + " and " + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "!" + "The value" + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "corresponds to 1 day!";
                    logger.Error(errorMessage);
                    throw new ArgErrorException(errorMessage);
                }

                long stsTimeInLong = stsTime.Value.Ticks / 10000;
                long signingTimeInLong = signingTime.Value.Ticks / 10000;
                int signingTimeToleranceInMilliSeconds = signingTimeTolerance * MILLIS_IN_SECOND;
                long timeDifference = signingTimeInLong - stsTimeInLong; // Negatif ise zaten olması gerektiği gibi.


                if (timeDifference > signingTimeToleranceInMilliSeconds)
                {
                    string signingTimeStr = DateUtil.formatDateByDayMonthYear24hours(signingTime.Value);
                    string stsTimeStr = DateUtil.formatDateByDayMonthYear24hours(stsTime.Value);
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNING_TIME_CHECKER_UNSUCCESSFUL, new string[] {signingTimeStr, stsTimeStr})));
                    return false;
                }         
                else
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNING_TIME_CHECKER_SUCCESSFUL)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
                    return true;
                }
            }

            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_SIGNING_TIME_ATTRIBUTE)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            return true;
        }
    }
}
