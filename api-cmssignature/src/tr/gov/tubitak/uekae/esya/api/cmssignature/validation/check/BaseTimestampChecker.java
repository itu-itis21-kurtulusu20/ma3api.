package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;


import java.util.ArrayList;
import java.util.List;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;

public abstract class BaseTimestampChecker extends BaseChecker {

	public BaseTimestampChecker(){
		
	}
	public boolean check(
					CheckerResult checkerResult, 
					List<EAttribute> tsAttrs, Signer si,
					TS_Type tsType,
					E_KEYS keyUnsuccesfull, E_KEYS keySuccesfull){
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
				checkerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(keyUnsuccesfull),aEx));
				return false;
			}
			
			TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);//for timestamp signature check
			TimeStampTimeChecker tstc = new TimeStampTimeChecker(tsType,sd);//for checking time ordering with other types of timestamps
			TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(tsType,sd);//for checking messageimprint of timestamp
			TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);//for checking timestamp server's certificate
			
			checkers.add(tssc);
			checkers.add(tstc);
			checkers.add(tsmdc);
			checkers.add(tscc);
			
			for(Checker checker:checkers)
			{
				CheckerResult cresult = new CheckerResult();
				checker.setParameters(getParameters());
				boolean result = checker.check(si, cresult);
				allResult = allResult && result;
				checkerResult.addCheckerResult(cresult);
			}
			
		}
		if(!allResult)
		{
			checkerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(keyUnsuccesfull)));
		}
		else
		{
			checkerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(keySuccesfull)));
			checkerResult.setResultStatus(SUCCESS);
		}
		
		return allResult;
		
	}
}
