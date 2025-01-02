using System;
using System.Collections.Generic;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;


namespace tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check
{
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
    public class CAdES_C_TimeStampAttrChecker : BaseChecker
    {
        protected override bool _check(Signer aSigner,CheckerResult aCheckerResult)
	{
		//aCheckerResult.setCheckerName("CAdES_C_TimeStamp Attribute Checker");
        aCheckerResult.setCheckerName(Msg.getMsg(Msg.CADES_C_TIMESTAMP_ATTRIBUTE_CHECKER), typeof(CAdES_C_TimeStampAttrChecker));
		List<EAttribute> tsAttrs = null;
		try
		{
			//Get all cadecstimestamps
            tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_escTimeStamp);
			
			if(tsAttrs.Count == 0)
			{
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.NO_CADESC_TSA_IN_SIGNEDDATA)));
				aCheckerResult.setResultStatus(Types.CheckerResult_Status.NOTFOUND);
				return false;
			}
		}
		catch(Exception aEx)
		{
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CADESC_TSA_DECODE_ERROR), aEx));
			return false;
		}
		
		bool allResult = true; 
		foreach(EAttribute attr in tsAttrs)
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
                aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CADESC_TSA_CHECK_UNSUCCESSFUL), aEx));
				return false;
			}
			
			TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);//for timestamp signature check
            TimeStampTimeChecker tstc = new TimeStampTimeChecker(Types.TS_Type.ESC, sd);//for checking time ordering with other types of timestamps
            TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(Types.TS_Type.ESC, sd);//for checking messageimprint of timestamp
			TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);//for checking timestamp server's certificate
			
			checkers.Add(tssc);
			checkers.Add(tstc);
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
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CADESC_TSA_CHECK_UNSUCCESSFUL)));
		}
		else
		{
            aCheckerResult.addMessage(new ValidationMessage(Msg.getMsg(Msg.CADESC_TSA_CHECK_SUCCESSFUL)));
            aCheckerResult.setResultStatus(Types.CheckerResult_Status.SUCCESS);
		}
		
		return allResult;
	}
    }
}
