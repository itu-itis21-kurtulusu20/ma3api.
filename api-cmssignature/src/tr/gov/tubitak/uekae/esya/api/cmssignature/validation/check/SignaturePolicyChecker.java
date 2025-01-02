package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignaturePolicy;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignaturePolicyId;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.UNSUCCESS;

/**
 * Checks if the hash value in SignaturePolicyIdentifier attribute match with the hash value
 * calculated by using the signature policy file
 * @author aslihan.kubilay
 *
 */
public class SignaturePolicyChecker extends BaseChecker
{
	protected static Logger logger = LoggerFactory.getLogger(SignaturePolicyChecker.class);

	@Override
	protected boolean _check(Signer aSigner,CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_POLICY_ATTRIBUTE_CHECKER), SignaturePolicyChecker.class);

		ESignaturePolicy sp;
		try 
		{
			sp = aSigner.getSignerInfo().getPolicyAttr();
		} 
		catch (ESYAException aEx) 
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_POLICY_ATTRIBUTE_DECODE_ERROR),aEx));
			aCheckerResult.setResultStatus(UNSUCCESS);
			return false;
		}
		ESignaturePolicyId spi = sp.getSignaturePolicyId();
		
		if(spi != null)
		{
			boolean sonuc = false;
			byte [] realHashOfDoc = null;
			DigestAlg declaredDigestAlg = DigestAlg.fromOID(spi.getHashInfo().getHashAlg().getAlgorithm().value);
			byte [] hash = spi.getHashInfo().getHashValue();
			
			try
			{
				TurkishESigProfile signatureProfile = TurkishESigProfile.getSignatureProfileFromOid(spi.getPolicyObjectIdentifier().value);
				if(signatureProfile != null)
				{
					realHashOfDoc = signatureProfile.getDigestofProfile(declaredDigestAlg.getOID());
				}
				else
				{
					byte[] policyFileValue = (byte[]) getParameters().get(AllEParameters.P_POLICY_VALUE);
					if(policyFileValue == null)
					{
						aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_POLICY_VALUE_NOT_FOUND)));
						aCheckerResult.setResultStatus(UNSUCCESS);
						return false;
					}
					byte[] asnValue = _asnKontrol(policyFileValue);
					if (asnValue != null)
						policyFileValue = asnValue;
					realHashOfDoc = DigestUtil.digest(declaredDigestAlg,policyFileValue);
				}
			
			}
			catch(Exception aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_POLICY_ATTRIBUTE_DIGEST_CALCULATION_ERROR),aEx));
				aCheckerResult.setResultStatus(UNSUCCESS);
				return false;
			}
			
			sonuc = Arrays.equals(hash, realHashOfDoc);
			if(sonuc)
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_POLICY_ATTRIBUTE_CHECKER_SUCCESSFUL)));
				aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
				return true;
			}
			else
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_POLICY_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
				aCheckerResult.setResultStatus(UNSUCCESS);
				return false;
			}
		}
		aCheckerResult.addMessage(new ValidationMessage("Imzadaki signature policy ozelliginin tipi _SIGNATUREPOLICYID degil"));
		aCheckerResult.setResultStatus(UNSUCCESS);
		return false;
	}

	private byte[] _asnKontrol(byte[] aPolicy) throws ESYAException
    {
		byte[] signaturePolicy = null;
		tr.gov.tubitak.uekae.esya.asn.signaturepolicies.SignaturePolicy sp;
		try {
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aPolicy);
			sp = new tr.gov.tubitak.uekae.esya.asn.signaturepolicies.SignaturePolicy();
			sp.decode(decBuf);

			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			//sp.signPolicyHash = null; //plugtestte böyleydi
			sp.encode(encBuf, false);
			signaturePolicy = encBuf.getMsgCopy();
		} catch (Exception aEx) {
			logger.warn("Warning in SignaturePolicyChecker", aEx);
			return null;
		}
		if (sp.signPolicyHash != null) {
			byte[] hashPolicy = sp.signPolicyHash.value;
			DigestAlg alg = DigestAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(sp.signPolicyHashAlg));

			byte[] realHash = DigestUtil.digest(alg, signaturePolicy);
			if (!Arrays.equals(hashPolicy, realHash))
				throw new ESYAException();
		}

		return signaturePolicy;
    }
}
