using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{

    /**
     * Checks time ordering between timestamps
     * @author aslihan.kubilay
     *
     */
    public class TimeStampTimeChecker : BaseChecker
    {
        private readonly Types.TS_Type? mType = null;
        private readonly ESignedData mSignedData = null;

        public TimeStampTimeChecker(Types.TS_Type aTSType, ESignedData aSignedData)
        {
            mType = aTSType;
            mSignedData = aSignedData;
        }


        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.TIMESTAMP_TIME_CHECKER), typeof(TimeStampTimeChecker));
            DateTime? tsTime = null;
            try
            {
                tsTime = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent()).getTime();
            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_TIME_CHECKER_TIME_ERROR), aEx));
                return false;
            }


            if (mType == Types.TS_Type.ESC || mType == Types.TS_Type.ES_REFS)
            {
                bool sonuc = false;
                try
                {
                    List<EAttribute> tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);

                    if (tsAttrs.Count == 0)
                        throw new Exception("Attributelar icinde " + AttributeOIDs.id_aa_signatureTimeStampToken + " oid li zaman damgasi ozelligi bulunamadi");
                    sonuc = _checkTimeOrdering(tsTime, tsAttrs);
                }
                catch (Exception aEx)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_TIME_CHECKER_COMPARISON_ERROR), aEx));
                    return false;
                }

                if (!sonuc)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_TIME_CHECKER_UNSUCCESSFUL)));
                    return false;
                }
            }
            if (mType == Types.TS_Type.ESA || mType == Types.TS_Type.ESAv3)
            {
                try
                {

                    List<EAttribute> tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
                    bool tstOrder;
                    if (tsAttrs.Count == 0 && mType == Types.TS_Type.ESA)
                        throw new Exception("Attributelar icinde " + AttributeOIDs.id_aa_signatureTimeStampToken +
                                            " oid li zaman damgasi ozelligi bulunamadi");

                    if (tsAttrs.Count != 0)
                    {
                        tstOrder = _checkTimeOrdering(tsTime, tsAttrs);
                        if (!tstOrder)
                        {
                            aCheckerResult.addMessage(
                                new ValidationMessage(Msg.getMsg(Msg.TS_TIME_CHECKER_ARCHIVE_BEFORE_EST)));
                            return false;
                        }
                    }

                tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_escTimeStamp);

                    if (tsAttrs.Count != 0)
                    {

                        tstOrder = _checkTimeOrdering(tsTime, tsAttrs);

                        if (!tstOrder)
                        {
                            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_TIME_CHECKER_ARCHIVE_BEFORE_ESC)));
                            return false;
                        }
                    }

                    tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certCRLTimestamp);

                    if (tsAttrs.Count != 0)
                    {

                        tstOrder = _checkTimeOrdering(tsTime, tsAttrs);

                        if (!tstOrder)
                        {
                            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_TIME_CHECKER_ARCHIVE_BEFORE_REFS)));
                            return false;
                        }
                    }


                }
                catch (Exception aEx)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_TIME_CHECKER_COMPARISON_ERROR), aEx));
                    return false;
                }
            }


            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_TIME_CHECKER_SUCCESSFUL)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            return true;
        }


        private bool _checkTimeOrdering(DateTime? aTime, List<EAttribute> aTSAttrs)
        {
            List<DateTime> timeList = new List<DateTime>();
            foreach (EAttribute attr in aTSAttrs)
            {
                EContentInfo ci = new EContentInfo(attr.getValue(0));
                ESignedData sd = new ESignedData(ci.getContent());
                ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                timeList.Add(tstInfo.getTime());
            }

            foreach (DateTime inner in timeList)
            {
                if (inner > aTime)
                    return false;
            }

            return true;

        }
    }
}
