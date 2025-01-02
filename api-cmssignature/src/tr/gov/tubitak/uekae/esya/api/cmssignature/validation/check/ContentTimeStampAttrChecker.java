package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;

import java.util.ArrayList;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;

public class ContentTimeStampAttrChecker  extends BaseChecker{

    protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER), ContentTimeStampAttrChecker.class);
		List<EAttribute> tsAttrs = null;
		try
		{
			tsAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_aa_ets_contentTimestamp);
			
			if(tsAttrs.isEmpty())
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_CONTENT_TIMESTAMP_ATTRIBUTE_IN_SIGNEDDATA)));
				aCheckerResult.setResultStatus(NOTFOUND);
				return false;
			}
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TIMESTAMP_ATTRIBUTE_DECODE_ERROR),aEx));
			return false;
		}
		
		boolean allResult = true; 
		for(EAttribute attr:tsAttrs)
		{
			List<Checker> checkers = new ArrayList<Checker>();
			
			ESignedData sd = null;
			try
			{
				EContentInfo ci = new EContentInfo(attr.getValue(0));
				sd = new ESignedData(ci.getContent());
			}
			catch(Exception aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_UNSUCCESSFUL),aEx));
				return false;
			}
			
			TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
			TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(TS_Type.CONTENT,sd);
			TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);
			
			checkers.add(tssc);
			checkers.add(tsmdc);
			checkers.add(tscc);
			
			for(Checker checker:checkers)
			{
				CheckerResult cresult = new CheckerResult();
				checker.setParameters(getParameters());
				boolean result = checker.check(aSigner, cresult);
				allResult = allResult && result;
				aCheckerResult.addCheckerResult(cresult);
			}
			
		}
		if(!allResult)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
		}
		else
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_SUCCESSFUL)));
			aCheckerResult.setResultStatus(SUCCESS);
		}
		
		return allResult;
	}
    
}
