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
import java.util.Map;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.ESA;

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
public class ArchiveTimeStampV2AttrChecker extends BaseArchiveTimeStampAttrChecker
{

	@Override
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TIMESTAMP_V2_ATTRIBUTE_CHECKER), ArchiveTimeStampV2AttrChecker.class);
		
		//Get all archivetimestamp attributes
		List<EAttribute> tsAttrs = null;
		try
		{
			tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_ets_archiveTimestampV2);
			
			if(tsAttrs.isEmpty())
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_ARCHIVE_TSA_V2_IN_SIGNEDDATA)));
				aCheckerResult.setResultStatus(NOTFOUND);
				return false;
			}
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TSA_V2_DECODE_ERROR),aEx));
			return false;
		}
		
		boolean allResult = true;
		for (EAttribute attr : tsAttrs) {
			boolean result = checkOneTimeStampAttr(attr, aSigner, aCheckerResult);
			allResult = allResult && result;
		}

		if(!allResult)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TSA_V2_CHECK_UNSUCCESSFUL)));
		}
		else
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TSA_V2_CHECK_SUCCESSFUL)));
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
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.ARCHIVE_TSA_V2_CHECK_UNSUCCESSFUL),aEx));
			return false;
		}

		TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
		TimeStampTimeChecker tstc = new TimeStampTimeChecker(ESA,sd);
		TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(ESA,sd);
		TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);



		checkers.add(tssc);
		checkers.add(tstc);
		checkers.add(tsmdc);
		checkers.add(tscc);


		boolean allResult = true;
		for(Checker checker:checkers)
		{
			CheckerResult cresult = new CheckerResult();
			Map<String, Object> parameters = getParameters();
			checker.setParameters(parameters);
			boolean result = checker.check(aSigner, cresult);
			allResult = allResult && result;
			aCheckerResult.addCheckerResult(cresult);
		}

		return allResult;
	}

}
