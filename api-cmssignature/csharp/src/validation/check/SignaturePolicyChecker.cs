using System;
using System.Linq;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    /**
 * Checks if the hash value in SignaturePolicyIdentifier attribute match with the hash value
 * calculated by using the signature policy file
 * @author aslihan.kubilay
 *
 */
    public class SignaturePolicyChecker : BaseChecker
    {
        //@Override
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.SIGNATURE_POLICY_ATTRIBUTE_CHECKER), typeof(SignaturePolicyChecker));

            ESignaturePolicy sp;
            try
            {
                sp = aSigner.getSignerInfo().getPolicyAttr();
            }
            catch (ESYAException aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_POLICY_ATTRIBUTE_DECODE_ERROR), aEx));
                return false;
            }
            ESignaturePolicyId spi = sp.getSignaturePolicyId();

            if (spi != null)
            {
                bool sonuc;
                byte[] realHashOfDoc = null;
                DigestAlg declaredDigestAlg = DigestAlg.fromOID(spi.getHashInfo().getHashAlg().getAlgorithm().mValue);
                byte[] hash = spi.getHashInfo().getHashValue();
                try
                {
                    TurkishESigProfile signatureProfile = TurkishESigProfile.getSignatureProfile(spi.getPolicyObjectIdentifier().mValue);
                    if (signatureProfile != null)
                    {
                        realHashOfDoc = signatureProfile.getDigestofProfile(declaredDigestAlg.getOID());
                    }
                    else
                    {
                        Object policyFileValueObj = null;
                        getParameters().TryGetValue(AllEParameters.P_POLICY_VALUE, out policyFileValueObj);
                        if (policyFileValueObj == null)
                        {
                            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_POLICY_VALUE_NOT_FOUND)));
                            aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                            return false;
                        }
                        byte[] policyFileValue = (byte [])policyFileValueObj;
                        byte[] asnValue = _asnKontrol(policyFileValue);
                        if (asnValue != null)
                            policyFileValue = asnValue;
                        realHashOfDoc = DigestUtil.digest(declaredDigestAlg, policyFileValue);
                    }

                }
                catch (Exception aEx)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_POLICY_ATTRIBUTE_DIGEST_CALCULATION_ERROR), aEx));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                    return false;
                }

                sonuc = realHashOfDoc.SequenceEqual(hash);
                
                if (sonuc)
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_POLICY_ATTRIBUTE_CHECKER_SUCCESSFUL)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
                    return true;
                }
                else
                {
                    aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_POLICY_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
                    aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
                    return false;
                }
            }
            aCheckerResult.addMessage(new ValidationMessage("Imzadaki signature policy ozelliginin tipi _SIGNATUREPOLICYID degil"));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.UNSUCCESS);
            return false;
        }


        private byte[] _asnKontrol(byte[] aPolicy)
        {
            byte[] signaturePolicy = null;
            tr.gov.tubitak.uekae.esya.asn.signaturepolicies.SignaturePolicy sp;
            try
            {
                Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aPolicy);
                sp = new tr.gov.tubitak.uekae.esya.asn.signaturepolicies.SignaturePolicy();
                sp.Decode(decBuf);

                Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
                //sp.signPolicyHash = null; //plugtestte böyleydi
                sp.signPolicyInfo.Encode(encBuf, false);
                signaturePolicy = encBuf.MsgCopy;
            }
            catch (Exception aEx)
            {
                return null;
            }
            if (sp.signPolicyHash != null)
            {
                byte[] hashPolicy = sp.signPolicyHash.mValue;
                DigestAlg alg = DigestAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(sp.signPolicyHashAlg));

                byte[] realHash = DigestUtil.digest(alg, signaturePolicy);
                if (hashPolicy.SequenceEqual(realHash))
                    throw new Exception();
            }
            return signaturePolicy;
        }
    }
}
