package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.ES_REFS;

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
 * Checks time-stamped-certs-crls-references attribute.
  * It uses checkers TimeStampSignatureChecker,TimeStampTimeChecker,TimeStampMessageDigestChecker and TimeStampCertificateChecker to check
 * 	- the signature of timestamp
 * 	- the time ordering between timestamps
 *  - the hash value of messageImprint in timestamp
 *  - the certificate of the timestamp server
 * 
 * @author aslihan.kubilay
 *
 */
public class TimeStampedCertsCrlsRefsAttrChecker extends BaseChecker
{
	
	protected boolean _check(Signer aSignerInfo,CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER), TimeStampedCertsCrlsRefsAttrChecker.class);
		List<EAttribute> tsAttrs = null;
		try
		{
			tsAttrs = aSignerInfo.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certCRLTimestamp);
			
			if(tsAttrs.isEmpty())
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_NOT_FOUND)));
				aCheckerResult.setResultStatus(NOTFOUND);
				return false;
			}
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_DECODE_ERROR),aEx));
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
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_DECODE_ERROR),aEx));
				return false;
			}
			
			TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
			TimeStampTimeChecker tstc = new TimeStampTimeChecker(ES_REFS,sd);
			TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(ES_REFS,sd);
			TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);
			
			checkers.add(tssc);
			checkers.add(tstc);
			checkers.add(tsmdc);
			checkers.add(tscc);
			
			for(Checker checker:checkers)
			{
				CheckerResult cresult = new CheckerResult();
				checker.setParameters(getParameters());
				boolean result = checker.check(aSignerInfo, cresult);
				allResult = allResult && result;
				aCheckerResult.addCheckerResult(cresult);
			}
			
		}
		if(!allResult)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
		}
		else
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMPED_CERTS_CRLS_REFS_ATTRIBUTE_CHECKER_SUCCESSFUL)));
			aCheckerResult.setResultStatus(SUCCESS);
		}
		
		return allResult;
	}
}
