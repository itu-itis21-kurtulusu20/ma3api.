package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;

import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * Checks if the content-type attribute has its value (i.e. ContentType) equal to the eContentType of the EncapsulatedContentInfo 
 * value being signed
 * @author aslihan.kubilay
 *
 */
public class ContentTypeAttrChecker extends BaseChecker
{
	//As stated in RFC 3852 [4], the content-type attribute must have its value (i.e. ContentType) equal to the
	//eContentType of the EncapsulatedContentInfo value being signed. (ETSI TS 101 733 V1.7.4 5.7.1)
	protected boolean _check(Signer aSigner,CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TYPE_ATTRIBUTE_CHECKER), ContentTypeAttrChecker.class);
		ESignedData signedData = (ESignedData)getParameters().get(AllEParameters.P_SIGNED_DATA);
		
		List<EAttribute> ctAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_contentType);
		
		if(ctAttrs.isEmpty())
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_CONTENT_TYPE_ATTRIBUTE_IN_SIGNED_DATA)));
			aCheckerResult.setResultStatus(NOTFOUND);
			return false;
		}
		
		EAttribute	ctAttr = ctAttrs.get(0);
		
		Asn1ObjectIdentifier oid = new Asn1ObjectIdentifier();
		try
		{
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(ctAttr.getValue(0));
			oid.decode(decBuf);
		}
		catch(Exception tEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TYPE_ATTRIBUTE_DECODE_ERROR),tEx));
			return false;
		}
		
		if(!signedData.getEncapsulatedContentInfo().getContentType().equals(oid))
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TYPE_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
			return false;
		}
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TYPE_ATTRIBUTE_CHECKER_SUCCESSFUL)));
		aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
		return true;
	}

}
