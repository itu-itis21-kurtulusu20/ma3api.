using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.src.validation.check;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public class ArchiveTimeStampAttrChecker : BaseArchieveTimeStampAttrChecker
    {
        //    @Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            //aCheckerResult.setCheckerName("Archive TimeStamp Attribute Checker");
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.ARCHIVE_TIMESTAMP_ATTRIBUTE_CHECKER), typeof(ArchiveTimeStampAttrChecker));

            List<EAttribute> tsAttrs = null;
            try
            {
                tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestamp);

                if (tsAttrs.Count == 0)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_ARCHIVE_TSA_IN_SIGNEDDATA)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                    return false;
                }
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ARCHIVE_TSA_DECODE_ERROR), aEx));
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
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ARCHIVE_TSA_CHECK_UNSUCCESSFUL)));
            }
            else
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ARCHIVE_TSA_CHECK_SUCCESSFUL)));
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
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ARCHIVE_TSA_CHECK_UNSUCCESSFUL), aEx));
                    return false;
                }

                TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
                TimeStampTimeChecker tstc = new TimeStampTimeChecker(Types.TS_Type.ESA, sd);
                TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(Types.TS_Type.ESA, sd);
                TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);

                checkers.Add(tssc);
                checkers.Add(tstc);
                checkers.Add(tsmdc);
                checkers.Add(tscc);

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
