using System;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
/**
 * 
 * @author aslihan.kubilay
 *
 */
namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    public class CryptoChecker : BaseChecker
    {
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
        {
            aCheckerResult.setCheckerName(Msg.getMsg(Msg.SIGNATURE_CHECKER), typeof(CryptoChecker));//TODO bundle

            byte[] signedData = aSigner.getSignerInfo().getSignedAttributes().getBytes();

            if (signedData == null)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNED_ATTRIBUTES_ENCODE_ERROR))); //TODO bundle
                return false;
            }

            bool verified = false;
            try
            {
                PublicKey pubKey = new PublicKey(aSigner.getSignerCertificate().getSubjectPublicKeyInfo());
                DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(aSigner.getSignerInfo().getDigestAlgorithm());
                //SignatureAlg sigAlg = SignatureAlg.fromAlgorithmIdentifier(aSignerInfo.getSignatureAlgorithm());
                Pair<SignatureAlg, IAlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(aSigner.getSignerInfo().getSignatureAlgorithm());
                SignatureAlg sigAlg = pair.first(); 

                //Bazi imza uretiticileri signature alg kismina sadece asimetrik algoritmayi koyuyorlar.
                if (sigAlg == SignatureAlg.RSA_NONE && digestAlg != null)
                {
                    sigAlg = SignatureAlg.fromName(sigAlg.getAsymmetricAlg().getName() + "-with-" +
                            digestAlg.getName().Replace("-",""));
                }

                if (digestAlg == null)
                    throw new CMSSignatureException("Cant resolve digest algorithm from SignerInfo" + aSigner.getSignerInfo().getDigestAlgorithm());
                if (pair.first() == null)
                    throw new CMSSignatureException("Cant resolve signature algorithm from SignerInfo " + aSigner.getSignerInfo().getSignatureAlgorithm());

                //verified = SignUtil.verify(sigAlg, signedData, aSignerInfo.getSignature(), pubKey);
                verified = SignUtil.verify(sigAlg, pair.second(), signedData, aSigner.getSignerInfo().getSignature(), pubKey);

            }
            catch (Exception aEx)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_VERIFICATION_ERROR), aEx)); //TODO bundle
                return false;
            }

            if (!verified)
            {
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_VERIFICATION_UNSUCCESSFUL))); //TODO bundle
                return false;
            }

            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.SIGNATURE_VERIFICATION_SUCCESSFUL)));
            return true;

        }
    }
}
