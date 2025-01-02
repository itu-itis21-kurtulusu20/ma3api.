package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.ESAv3;

/**
 * Checks archive-time-stamp attribute.
 * It uses checkers TimeStampSignatureChecker,TimeStampTimeChecker,TimeStampMessageDigestChecker and TimeStampCertificateChecker to check
 * 	- the signature of timestamp
 * 	- the time ordering between timestamps
 *  - the hash value of messageImprint in timestamp
 *  - the certificate of the timestamp server
 * 	
 * @author aslihan.kubilay
 *
 */
public class ArchiveTimeStampV3AttrChecker extends BaseArchiveTimeStampAttrChecker
{
	@Override
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TIMESTAMP_V3_ATTRIBUTE_CHECKER), ArchiveTimeStampV3AttrChecker.class);
		
		//Get all archivetimestamp attributes
		List<EAttribute> tsAttrs = null;
		try
		{
			
			tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV3);
			
			if(tsAttrs.isEmpty())
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_ARCHIVE_TSA_V3_IN_SIGNEDDATA)));
				aCheckerResult.setResultStatus(NOTFOUND);
				return false;
			}
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TSA_V3_DECODE_ERROR),aEx));
			return false;
		}
		
		boolean allResult = true;
		for (EAttribute attr : tsAttrs) {
			boolean result = checkOneTimeStampAttr(attr, aSigner, aCheckerResult);
			allResult = allResult && result;
		}


		if(!allResult)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TSA_V3_CHECK_UNSUCCESSFUL)));
		}
		else
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TSA_V3_CHECK_SUCCESSFUL)));
			aCheckerResult.setResultStatus(SUCCESS);
		}
		
		return allResult;
	}

	public boolean checkOneTimeStampAttr(EAttribute attr, Signer aSigner, CheckerResult aCheckerResult) {

		List<Checker> checkers = new ArrayList<Checker>();

		ESignedData sd = null;
		try
		{
			EContentInfo ci = new EContentInfo(attr.getValue(0));
			sd = new ESignedData(ci.getContent());
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TSA_V3_CHECK_UNSUCCESSFUL),aEx));
			return false;
		}

		TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
		TimeStampTimeChecker tstc = new TimeStampTimeChecker(ESAv3,sd);
		TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(ESAv3,sd);
		TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);
		ATSHashIndexAttrChecker ahic = new ATSHashIndexAttrChecker(sd);

		checkers.add(tssc);
		checkers.add(tstc);
		checkers.add(tsmdc);
		checkers.add(tscc);
		checkers.add(ahic);

		boolean allResult = true;
		for(Checker checker:checkers)
		{
			CheckerResult cresult = new CheckerResult();
			checker.setParameters(getParameters());
			boolean result = checker.check(aSigner, cresult);
			allResult = allResult && result;
			aCheckerResult.addCheckerResult(cresult);
		}

		return allResult;
	}


}
