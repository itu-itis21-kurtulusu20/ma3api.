using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.pkixtsp;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.asn.algorithms;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public class TurkishProfileAttributesChecker : BaseChecker
    {
        private static readonly int WAIT_TIME = 300;   //for flexible signing time
        private readonly bool signOrValidate = false;

        public TurkishProfileAttributesChecker()
        {
        }

        public TurkishProfileAttributesChecker(bool aSign)
        {
            signOrValidate = aSign;
        }

        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.TURKISH_PROFILE_ATTRIBUTES_CHECKER), typeof(TurkishProfileAttributesChecker));

            if (aSigner.isTurkishProfile())
            {
                DigestAlg digestAlg = getPolicyDigestAlg(aSigner);
                if (digestAlg == null || digestAlg.getOID() != _algorithmsValues.id_sha256)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.PROFILE_POLICY_HASH_NOT_SHA256)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                    return false;
                }

                List<EAttribute> stAttr = aSigner.getSignerInfo().getSignedAttribute(AttributeOIDs.id_signingTime);
                if (stAttr == null || stAttr.Count == 0)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNING_TIME_ATTRIBUTE_MISSING)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                    return false;
                }

                List<EAttribute> certIdAttr =
                    aSigner.getSignerInfo().getSignedAttribute(AttributeOIDs.id_aa_signingCertificateV2);
                if (certIdAttr == null || certIdAttr.Count == 0)
                {
                    aCheckerResult.addMessage(
                        new ValidationMessage(Msg.getMsg(Msg.SIGNING_CERTIFICATE_V2_ATTRIBUTE_MISSING)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                    return false;
                }

            }

            if (aSigner.isTurkishProfile() || getParameters().ContainsKey(AllEParameters.P_VALIDATION_PROFILE))
            {
                try
                {
                    TurkishESigProfile profile = null;
                    Object mProfile = null;
                    getParameters().TryGetValue(AllEParameters.P_VALIDATION_PROFILE, out mProfile);
                    profile = (TurkishESigProfile) mProfile;

                    if (profile == null)
                        profile = aSigner.getSignerInfo().getProfile();

                    if (profile != TurkishESigProfile.P1_1)
                    {
                        if (!(signOrValidate && (aSigner.getType() == ESignatureType.TYPE_BES ||
                                                 aSigner.getType() == ESignatureType.TYPE_EPES)))
                        {
                            bool result = false;
                            DateTime? signingTime = aSigner.getSignerInfo().getSigningTime();
                            signingTime = signingTime.Value.AddSeconds(-1 * WAIT_TIME);
                            DateTime? timestampTime = getTime(aSigner);
                            if (signingTime.Value.CompareTo(timestampTime) > 0)
                                result = true;

                            signingTime = signingTime.Value.AddSeconds(7200 + WAIT_TIME);
                            if (timestampTime.Value.CompareTo(signingTime) > 0)
                                result = true;

                            if (result)
                            {
                                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TS_TIME_NOT_AFTER_2H)));
                                aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                                return false;
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_TIME_ERROR)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                    return false;
                }
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.TURKISH_PROFILE_ATTRIBUTES_CHECKER_SUCCESSFUL)));
                aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
                return true;
            }           
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NOT_A_TURKISH_PROFILE)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            return true;
        }
        /**
         * Returns  time of signature time stamp
         * @return Calendar
         * @throws ESYAException
         */
        public DateTime? getTime(Signer aSigner)
        {
            try
            {
                EAttribute attr = aSigner.getSignerInfo().getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken)[0];
                EContentInfo contentInfo = new EContentInfo(attr.getValue(0));
                ESignedData sd = new ESignedData(contentInfo.getContent());
                ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
                return tstInfo.getTime();
            }
            catch (Asn1Exception ex)
            {
                throw new ESYAException("Cant decode timestamp time", ex);
            }
        }
        private DigestAlg getPolicyDigestAlg(Signer aSigner)
        {
            ESignaturePolicy sp;
            try
            {
                sp = aSigner.getSignerInfo().getPolicyAttr();
                ESignaturePolicyId spi = sp.getSignaturePolicyId();
                return DigestAlg.fromOID(spi.getHashInfo().getHashAlg().getAlgorithm().mValue);
            }
            catch (Exception e)
            {
                return null;
            }
        }
        /*
        private bool isTurkishProfile(Signer aSigner)
        {
            try
            {
                TurkishESigProfile tp = aSigner.getSignerInfo().getProfile();
                if (tp == null)
                    return false;
            }
            catch (Exception e)
            {
                return false;
            }
            return true;
        }*/
    }
}
