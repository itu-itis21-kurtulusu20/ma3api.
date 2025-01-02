using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.src.validation.check;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks archive-time-stamp attribute.
 * It uses checkers TimeStampSignatureChecker,TimeStampTimeChecker,TimeStampMessageDigestChecker and TimeStampCertificateChecker to check
 * 	- the signature of timestamp
 * 	- the time ordering between timestamps
 *  - the hash value of messageImprint in timestamp
 *  - the certificate of the timestamp server
 * 	
 * @author aslihan.kubilay
 *
 */
    public class ArchiveTimeStampV3AttrChecker : BaseArchieveTimeStampAttrChecker
    {
        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.ARCHIVE_TIMESTAMP_V3_ATTRIBUTE_CHECKER), typeof(ArchiveTimeStampV3AttrChecker));

            //Get all archivetimestamp attributes
            List<EAttribute> tsAttrs = null;
            try
            {

                tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);

                if (tsAttrs.Count == 0)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_ARCHIVE_TSA_V3_IN_SIGNEDDATA)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                    return false;
                }
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ARCHIVE_TSA_V3_DECODE_ERROR), aEx));
                return false;
            }

            bool allResult = true;
            foreach (EAttribute attr in tsAttrs)
            {
                bool result = checkOneTimeStampAttr(attr, aSigner, aCheckerResult);
                allResult = allResult && result;
            }

            if (!allResult)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ARCHIVE_TSA_V3_CHECK_UNSUCCESSFUL)));
            }
            else
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ARCHIVE_TSA_V3_CHECK_SUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            }

            return allResult;
        }

        public override bool checkOneTimeStampAttr(EAttribute attr, Signer aSigner, CheckerResult aCheckerResult)
        {            
            List<Checker> checkers = new List<Checker>();

            ESignedData sd = null;
            try
            {
                 EContentInfo ci = new EContentInfo(attr.getValue(0));
                 sd = new ESignedData(ci.getContent());
            }
            catch (Exception aEx)
            {
                 aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ARCHIVE_TSA_V3_CHECK_UNSUCCESSFUL), aEx));
                 return false;
            }

            TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
            TimeStampTimeChecker tstc = new TimeStampTimeChecker(Types.TS_Type.ESAv3, sd);
            TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(Types.TS_Type.ESAv3, sd);
            TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);
            ATSHashIndexAttrChecker ahic = new ATSHashIndexAttrChecker(sd);

            checkers.Add(tssc);
            checkers.Add(tstc);
            checkers.Add(tsmdc);
            checkers.Add(tscc);
            checkers.Add(ahic);

            bool allResult = true;
            foreach (Checker checker in checkers)
            {
               CheckerResult cresult = new CheckerResult();
               checker.setParameters(getParameters());
               bool result = checker.check(aSigner, cresult);
               allResult = allResult && result;
               aCheckerResult.addCheckerResult(cresult);
            }
            return allResult;
        }
    }
}
