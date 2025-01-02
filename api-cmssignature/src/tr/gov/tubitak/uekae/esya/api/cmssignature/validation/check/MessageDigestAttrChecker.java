package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.ISignable;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1OctetString;

public class MessageDigestAttrChecker extends BaseChecker
{
	protected boolean _check(Signer aSigner,CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.MESSAGE_DIGEST_ATTRIBUTE_CHECKER), MessageDigestAttrChecker.class);
		ESignedData signedData = (ESignedData)getParameters().get(AllEParameters.P_SIGNED_DATA);
		Object parentSignerInfo = getParameters().get(AllEParameters.P_PARENT_SIGNER_INFO);

		List<EAttribute> mdAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_messageDigest);
		if(mdAttrs.isEmpty())
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_MESSAGE_DIGEST_ATTRIBUTE_FOUND)));
			aCheckerResult.setResultStatus(NOTFOUND);
			return false;
		}

		EAttribute mdAttr = mdAttrs.get(0);

		Asn1OctetString octetS = new Asn1OctetString();
		try
		{
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(mdAttr.getValue(0));
			octetS.decode(decBuf);
		}
		catch(Exception tEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.MESSAGE_DIGEST_ATTRIBUTE_DECODE_ERROR),tEx));
			return false;
		}


		byte [] contentDigest;
		
		DigestAlg digestAlg = DigestAlg.fromOID(aSigner.getSignerInfo().getDigestAlgorithm().getAlgorithm().value);
		if(digestAlg==null)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNER_DIGEST_ALGORITHM_UNKNOWN)));
			return false;
		}

		try
		{
			if (parentSignerInfo!=null && parentSignerInfo instanceof ESignerInfo) 
			{
				contentDigest = DigestUtil.digest(digestAlg, ((ESignerInfo)parentSignerInfo).getSignature());
			}
			else if(signedData.getEncapsulatedContentInfo().getContent() != null)
			{
				contentDigest = DigestUtil.digest(digestAlg, signedData.getEncapsulatedContentInfo().getContent());
			}
			else
			{
				ISignable contentI = null;
				Object contentO = getParameters().get(AllEParameters.P_EXTERNAL_CONTENT);

				if(contentO == null)
				{
					contentO = getParameters().get(AllEParameters.P_CONTENT);
				}

				if(contentO == null)
				{
					aCheckerResult.addMessage(new ValidationMessage("Imzalanan veri parametresi null"));
					return false;
				}
				try
				{
					contentI = (ISignable) contentO;
					contentDigest = contentI.getMessageDigest(digestAlg);
				} 
				catch (ClassCastException ex)
				{
					aCheckerResult.addMessage(new ValidationMessage("The external content object is not in the type of ISignable.",ex));
					return false;
				} 
				catch (IOException e) 
				{
					aCheckerResult.addMessage(new ValidationMessage("External can not be read",e));
					return false;
				}
			}
		}
		catch(CryptoException ex)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.MESSAGE_DIGEST_CHECKER_ERROR),ex));
			return false;
		}
		

		
		boolean	isMatching = Arrays.equals(octetS.value, contentDigest);


		if(!isMatching)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.MESSAGE_DIGEST_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
			return false;
		}

		aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.MESSAGE_DIGEST_ATTRIBUTE_CHECKER_SUCCESSFUL)));
		return true;
	}

}
