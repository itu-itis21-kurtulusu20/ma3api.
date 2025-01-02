package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;

import java.security.PublicKey;

import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

/**
 * 
 * @author aslihan.kubilay
 *
 */
public class CryptoChecker extends BaseChecker
{

	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_CHECKER), CryptoChecker.class);

		byte[]signedData = aSigner.getSignerInfo().getSignedAttributes().getEncoded();

		if(signedData == null)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNED_ATTRIBUTES_ENCODE_ERROR))); 
			return false;
		}

		boolean verified = false;
		try 
		{
			PublicKey pubKey = KeyUtil.decodePublicKey(aSigner.getSignerCertificate().getSubjectPublicKeyInfo());
			DigestAlg digestAlg = DigestAlg.fromAlgorithmIdentifier(aSigner.getSignerInfo().getDigestAlgorithm());

			Pair<SignatureAlg, AlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(aSigner.getSignerInfo().getSignatureAlgorithm());
			
			SignatureAlg sigAlg = pair.first(); 
			//Bazı imza üretiticileri signature alg kısmına sadece asimetrik algoritmayı koyuyorlar.
			if (sigAlg == SignatureAlg.RSA_NONE && digestAlg != null)
			{
				sigAlg = SignatureAlg.fromName(sigAlg.getAsymmetricAlg().getName() + "-with-" + 
						digestAlg.getName().replace("-", ""));
			}
			
			if (digestAlg == null)
				throw new CMSSignatureException("Cant resolve digest algorithm from SignerInfo"+aSigner.getSignerInfo().getDigestAlgorithm());
			if (pair.first() == null)
				throw new CMSSignatureException("Cant resolve signature algorithm from SignerInfo "+aSigner.getSignerInfo().getSignatureAlgorithm());
			
			
			verified = SignUtil.verify(sigAlg,pair.second(), signedData, aSigner.getSignerInfo().getSignature(), pubKey);
		} 
		catch (Exception aEx) 
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_VERIFICATION_ERROR),aEx));
			return false;
		}


		if(!verified)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_VERIFICATION_UNSUCCESSFUL)));
			return false;
		}

		aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_VERIFICATION_SUCCESSFUL)));
		return true;

	}
}
