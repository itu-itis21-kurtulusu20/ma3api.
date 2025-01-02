package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;


import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.ESC;

import java.util.ArrayList;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;

/**
 * Checks CAdES-C-time-stamp Attribute.
 * It uses checkers TimeStampSignatureChecker,TimeStampTimeChecker,TimeStampMessageDigestChecker and TimeStampCertificateChecker to check
 * 	- the signature of timestamp
 * 	- the time ordering between timestamps
 *  - the hash value of messageImprint in timestamp
 *  - the certificate of the timestamp server
 * 	
 * @author aslihan.kubilay
 *
 */
public class CAdES_C_TimeStampAttrChecker extends BaseChecker
{
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.CADES_C_TIMESTAMP_ATTRIBUTE_CHECKER), CAdES_C_TimeStampAttrChecker.class);
		List<EAttribute> tsAttrs = null;
		try
		{
			//Get all cadecstimestamps
			tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_escTimeStamp);
			
			if(tsAttrs.isEmpty())
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_CADESC_TSA_IN_SIGNEDDATA)));
				aCheckerResult.setResultStatus(NOTFOUND);
				return false;
			}
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CADESC_TSA_DECODE_ERROR),aEx));
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
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CADESC_TSA_CHECK_UNSUCCESSFUL),aEx));
				return false;
			}
			
			TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);//for timestamp signature check
			TimeStampTimeChecker tstc = new TimeStampTimeChecker(ESC,sd);//for checking time ordering with other types of timestamps
			TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(ESC,sd);//for checking messageimprint of timestamp
			TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);//for checking timestamp server's certificate
			
			checkers.add(tssc);
			checkers.add(tstc);
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
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CADESC_TSA_CHECK_UNSUCCESSFUL)));
		}
		else
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.CADESC_TSA_CHECK_SUCCESSFUL)));
			aCheckerResult.setResultStatus(SUCCESS);
		}
		
		return allResult;
	}
}
