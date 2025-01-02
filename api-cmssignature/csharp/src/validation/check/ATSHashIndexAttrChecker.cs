using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public class ATSHashIndexAttrChecker : BaseChecker
    {
        private readonly ESignedData mSignedData = null;
        private readonly List<Asn1OctetString> allUnsignedAttrHash = new List<Asn1OctetString>();
        public ATSHashIndexAttrChecker(ESignedData aSignedData)
        {
            mSignedData = aSignedData;
        }

        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.ATS_HASH_INDEX_ATTRIBUTE_CHECKER), typeof(ATSHashIndexAttrChecker));

            EATSHashIndex atsHashIndex;
            try
            {
                EAttribute atsHashIndexAttr = mSignedData.getSignerInfo(0).getUnsignedAttribute(AttributeOIDs.id_aa_ATSHashIndex)[0];
                atsHashIndex = new EATSHashIndex(atsHashIndexAttr.getValue(0));

            }
            catch (Exception e)
            {
                aCheckerResult.addMessage(new ValidationMessage("Error while getting ats-hash-index attribute"));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                return false;
                //throw new ESYARuntimeException("Error while getting ats-hash-index attribute", e);
            }

            List<Asn1OctetString> unsignedAttrHash = new List<Asn1OctetString>(atsHashIndex.getUnsignedAttrsHashIndex());
            DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(atsHashIndex.gethashIndAlgorithm());

            List<Attribute> unsignedAttr = new List<Attribute>(aSigner.getSignerInfo().getUnsignedAttributes().getObject().elements);
            Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
            try
            {
                foreach (Attribute attr in unsignedAttr)
                {
                    if (attr != null)
                    {
                        if (!attr.type.Equals(AttributeOIDs.id_countersignature)
                                && !attr.type.Equals(AttributeOIDs.id_aa_ets_archiveTimestampV3))
                        {
                            attr.Encode(encBuf);
                            Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(digestAlg, encBuf.MsgCopy));
                            allUnsignedAttrHash.Add(digest);
                            encBuf.Reset();
                            if (!unsignedAttrHash.Contains(digest))
                            {
                                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.UNSIGNED_ATTRIBUTE_NOT_INCLUDED)));
                                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                                return false;
                            }
                        }

                    }
                }
            }
            catch (ESYAException e)
            {
                aCheckerResult.addMessage(new ValidationMessage("Error while calculating digest of unsigned attributes"));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                return false;
                //throw new ESYARuntimeException("Error while calculating digest of unsigned attributes", e);
            }
            if (!_checkArchiveTSAttr(aSigner, unsignedAttrHash, digestAlg) || !_checkCounterSignatureAttr(aSigner, digestAlg))
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.UNSIGNED_ATTRIBUTE_NOT_INCLUDED)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                return false;
            }
            foreach (Asn1OctetString hash in unsignedAttrHash)
            {
                if (!allUnsignedAttrHash.Contains(hash))
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.UNSIGNED_ATTRIBUTE_MISSING)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                    return false;
                }
            }

            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.ATS_HASH_INDEX_ATTRIBUTE_CHECKER_SUCCESSFUL)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            return true;

        }
        private bool _checkArchiveTSAttr(Signer aSigner, List<Asn1OctetString> hashList, DigestAlg aDigestAlg)
        {
            try
            {
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                ETSTInfo tstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
                DateTime tsTime = tstInfo.getTime();

                List<EAttribute> atsv3Attributes = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
                foreach (EAttribute attr in atsv3Attributes)
                {
                    DateTime? time = SignatureTimeStampAttr.toTime(attr);
                    if (tsTime.CompareTo(time.Value) > 0)
                    {
                        attr.getObject().Encode(encBuf);
                        Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(aDigestAlg, encBuf.MsgCopy));
                        allUnsignedAttrHash.Add(digest);
                        encBuf.Reset();
                        if (!hashList.Contains(digest))
                        {
                            return false;
                        }
                    }
                }
            }
            catch (ESYAException e)
            {
                // logger
                return false;
            }
            return true;
        }

        private bool _checkCounterSignatureAttr(Signer aSigner, DigestAlg aDigestAlg)
        {
            try
            {
                List<EAttribute> counterAttributes = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_countersignature);
                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();

                foreach (EAttribute attr in counterAttributes)
                {
                    attr.getObject().Encode(encBuf);
                    Asn1OctetString digest = new Asn1OctetString(DigestUtil.digest(aDigestAlg, encBuf.MsgCopy));
                    allUnsignedAttrHash.Add(digest);
                    encBuf.Reset();
                }
            }
            catch (ESYAException e)
            {
                // logger
                return false;
            }
            return true;
        }
    }
}
