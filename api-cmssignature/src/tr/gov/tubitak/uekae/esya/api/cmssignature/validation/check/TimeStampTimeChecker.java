
package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.ESA;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.ESAv3;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.ESC;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.ES_REFS;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Checks time ordering between timestamps
 * @author aslihan.kubilay
 *
 */
public class TimeStampTimeChecker extends BaseChecker
{
	private Types.TS_Type mType = null;
	private ESignedData mSignedData = null;
	
	public TimeStampTimeChecker(Types.TS_Type aTSType,ESignedData aSignedData)
	{
		mType = aTSType;
		mSignedData = aSignedData;
	}
	
	
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.TIMESTAMP_TIME_CHECKER), TimeStampTimeChecker.class);
        Calendar tsTime = null;
        try
        {
            tsTime = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent()).getTime();
        }
        catch(Exception aEx)
        {
            aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_CHECKER_TIME_ERROR),aEx));
            return false;
        }
		
		
		if(mType==ESC || mType == ES_REFS)
		{
			boolean sonuc = false;
			try
			{
				List<EAttribute> tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
				
				if(tsAttrs.isEmpty())
					throw new ESYAException("Attributelar icinde "+AttributeOIDs.id_aa_signatureTimeStampToken + " oid li zaman damgasi ozelligi bulunamadi");
				sonuc = _checkTimeOrdering(tsTime,tsAttrs);
			}
			catch(Exception aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_CHECKER_COMPARISON_ERROR),aEx));
				return false;
			}
			
			if(!sonuc)
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_CHECKER_UNSUCCESSFUL)));
				return false;
			}
		}
		if(mType == ESA || mType == ESAv3)
		{
			try
			{
				
				List<EAttribute> tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
				boolean tstOrder;
				if(tsAttrs.isEmpty() && mType == ESA)
					throw new ESYAException("Attributelar icinde "+AttributeOIDs.id_aa_signatureTimeStampToken + " oid li zaman damgasi ozelligi bulunamadi");
				
				if (!tsAttrs.isEmpty()) {
					tstOrder = _checkTimeOrdering(tsTime, tsAttrs);
					if (!tstOrder) {
						aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_CHECKER_ARCHIVE_BEFORE_EST)));
						return false;
					}
				}
				
				tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_escTimeStamp);
				
				if(!tsAttrs.isEmpty()){

					tstOrder = _checkTimeOrdering(tsTime,tsAttrs);
								
					if(!tstOrder)
					{
						aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_CHECKER_ARCHIVE_BEFORE_ESC)));
						return false;
					}
				}
				
				tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_certCRLTimestamp);
				
				if(!tsAttrs.isEmpty()){

					tstOrder = _checkTimeOrdering(tsTime,tsAttrs);
								
					if(!tstOrder)
					{
						aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_CHECKER_ARCHIVE_BEFORE_REFS)));
						return false;
					}
				}
				

			}
			catch(Exception aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_CHECKER_COMPARISON_ERROR),aEx));
				return false;
			}
		}
		
		
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.TS_TIME_CHECKER_SUCCESSFUL)));
		aCheckerResult.setResultStatus(SUCCESS);
		return true;
	}
	
	
	private boolean _checkTimeOrdering(Calendar aTime,List<EAttribute> aTSAttrs)
	throws ESYAException
	{
		List<Calendar> timeList = new ArrayList<Calendar>();
		for(EAttribute attr:aTSAttrs)
		{
			EContentInfo ci = new EContentInfo(attr.getValue(0));
			ESignedData sd = new ESignedData(ci.getContent());
			ETSTInfo tstInfo = new ETSTInfo(sd.getEncapsulatedContentInfo().getContent());
			timeList.add(tstInfo.getTime());
		}
		
		for(Calendar inner:timeList)
		{
			if(inner.after(aTime))
				return false;
		}

		return true;
		
	}
	
	

}
