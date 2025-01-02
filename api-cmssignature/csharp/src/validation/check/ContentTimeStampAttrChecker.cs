using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;

namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
    class ContentTimeStampAttrChecker : BaseChecker
    {
        protected override bool _check(Signer aSigner, CheckerResult aCheckerResult)
	{
        aCheckerResult.setCheckerName(Msg.getMsg(Msg.CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER), typeof(ContentTimeStampAttrChecker));
		List<EAttribute> tsAttrs = null;
		try
		{
			tsAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_aa_ets_contentTimestamp);
			
			if(tsAttrs.Count==0)
			{
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_CONTENT_TIMESTAMP_ATTRIBUTE_IN_SIGNEDDATA)));
				aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
				return false;
			}
		}
		catch(Exception aEx)
		{
			 aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CONTENT_TIMESTAMP_ATTRIBUTE_DECODE_ERROR), aEx));
			return false;
		}
		
		bool allResult = true; 
		foreach (EAttribute attr in tsAttrs)
		{
			List<Checker> checkers = new List<Checker>();
			
			ESignedData sd = null;
			try
			{
				EContentInfo ci = new EContentInfo(attr.getValue(0));
				sd = new ESignedData(ci.getContent());
			}
			catch(Exception aEx)
			{
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_UNSUCCESSFUL), aEx));
				return false;
			}
			
			TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
			TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(Types.TS_Type.CONTENT,sd);
			TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);
			
			checkers.Add(tssc);
			checkers.Add(tsmdc);
			checkers.Add(tscc);
			
			foreach(Checker checker in checkers)
			{
				CheckerResult cresult = new CheckerResult();
				checker.setParameters(getParameters());
				bool result = checker.check(aSigner, cresult);
				allResult = allResult && result;
				aCheckerResult.addCheckerResult(cresult);
			}
			
		}
		if(!allResult)
		{
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_UNSUCCESSFUL)));
		}
		else
		{
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CONTENT_TIMESTAMP_ATTRIBUTE_CHECKER_SUCCESSFUL)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
		}
		
		return allResult;
	}
    }
}
