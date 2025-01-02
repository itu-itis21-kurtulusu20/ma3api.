using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks signature-time-stamp attribute.It uses checkers TimeStampSignatureChecker,TimeStampTimeChecker,TimeStampMessageDigestChecker,SigningTimeChecker and 
 * TimeStampCertificateChecker to check
 * 	- the signature of timestamp
 * 	- the time ordering between timestamps
 *  - the hash value of messageImprint in timestamp
 *  - the time ordering between time in signing time attribute and time in signature-time-stamp
 *  - the certificate of the timestamp server  
 * @author aslihan.kubilay
 *
 */
    public class SignatureTimeStampAttrChecker : BaseChecker
    {
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.SIGNATURETIMESTAMP_ATTRIBUTE_CHECKER), typeof(SignatureTimeStampAttrChecker));
            List<EAttribute> tsAttrs = null;
            try
            {
                tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);

                if (tsAttrs.Count == 0)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_TS_NOT_FOUND)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
                    return false;
                }
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_TS_DECODE_ERROR), aEx));
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
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_TS_CHECK_UNSUCCESSFUL), aEx));
                    return false;
                }

                TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
                TimeStampTimeChecker tstc = new TimeStampTimeChecker(Types.TS_Type.EST, sd);
                TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(Types.TS_Type.EST, sd);
                TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);
                SigningTimeChecker stc = new SigningTimeChecker(sd);

                try
                { //if it is P4, validate signature timestamp without finders
                    if (aSigner.getSignerInfo().getProfile() == TurkishESigProfile.P4_1)
                    {
                        tscc.setCloseFinders(true);
                    }
                }
                catch (ESYAException e)
                {
                    aCheckerResult.addMessage(new ValidationMessage("Error while decoding turkish profile"));
                    return false;
                }

                checkers.Add(tssc);
                checkers.Add(tstc);
                checkers.Add(tsmdc);
                checkers.Add(tscc);
                checkers.Add(stc);

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
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_TS_CHECK_UNSUCCESSFUL)));
            }
            else
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_TS_CHECK_SUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            }

            return allResult;
        }

    }
}
