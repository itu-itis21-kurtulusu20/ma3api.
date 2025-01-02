using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;


namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public class TimeStampedCertsCrlsRefsAttrChecker : BaseChecker
    {
        /**
 * Checks time-stamped-certs-crls-references attribute.
  * It uses checkers TimeStampSignatureChecker,TimeStampTimeChecker,TimeStampMessageDigestChecker and TimeStampCertificateChecker to check
 * 	- the signature of timestamp
 * 	- the time ordering between timestamps
 *  - the hash value of messageImprint in timestamp
 *  - the certificate of the timestamp server
 * 
 * @author aslihan.kubilay
 *
 */
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER), typeof(TimeStampedCertsCrlsRefsAttrChecker));
            List<EAttribute> tsAttrs = null;
            try
            {
                tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certCRLTimestamp);

                if (tsAttrs.Count == 0)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_NOT_FOUND)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                    return false;
                }
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_DECODE_ERROR), aEx));
                return false;
            }

            bool allResult = true;
            foreach (EAttribute attr in tsAttrs)
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
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_DECODE_ERROR), aEx));
                    return false;
                }

                TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
                TimeStampTimeChecker tstc = new TimeStampTimeChecker(Types.TS_Type.ES_REFS, sd);
                TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(Types.TS_Type.ES_REFS, sd);
                TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);

                checkers.Add(tssc);
                checkers.Add(tstc);
                checkers.Add(tsmdc);
                checkers.Add(tscc);

                foreach (Checker checker in checkers)
                {
                    CheckerResult cresult = new CheckerResult();
                    checker.setParameters(getParameters());
                    bool result = checker.check(aSigner, cresult);
                    allResult = allResult && result;
                    aCheckerResult.addCheckerResult(cresult);
                }

            }
            if (!allResult)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
            }
            else
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_SUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            }

            return allResult;
        }
    }
}
