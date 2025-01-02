package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.NOTFOUND;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status.SUCCESS;
import static tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.TS_Type.EST;

/**
 * Checks signature-time-stamp attribute.It uses checkers TimeStampSignatureChecker,TimeStampTimeChecker,TimeStampMessageDigestChecker,SigningTimeChecker and 
 * TimeStampCertificateChecker to check
 * 	- the signature of timestamp
 * 	- the time ordering between timestamps
 *  - the hash value of messageImprint in timestamp
 *  - the time ordering between time in signing time attribute and time in signature-time-stamp
 *  - the certificate of the timestamp server  
 * @author aslihan.kubilay
 *
 */
public class SignatureTimeStampAttrChecker extends BaseChecker
{
	protected static Logger logger = LoggerFactory.getLogger(SignatureTimeStampAttrChecker.class);
	protected boolean _check(Signer aSigner, CheckerResult aCheckerResult)
	{
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURETIMESTAMP_ATTRIBUTE_CHECKER), SignatureTimeStampAttrChecker.class);
		List<EAttribute> tsAttrs = null;
		try
		{
			tsAttrs = aSigner.getUnsignedAttribute(AttributeOIDs.id_aa_signatureTimeStampToken);
			
			if(tsAttrs.isEmpty())
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_TS_NOT_FOUND)));
				aCheckerResult.setResultStatus(NOTFOUND);
				return false;
			}
		}
		catch(Exception aEx)
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_TS_DECODE_ERROR),aEx));
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
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_TS_CHECK_UNSUCCESSFUL),aEx));
				return false;
			}
			
			TimeStampSignatureChecker tssc = new TimeStampSignatureChecker(sd);
			TimeStampTimeChecker tstc = new TimeStampTimeChecker(EST,sd);
			TimeStampMessageDigestChecker tsmdc = new TimeStampMessageDigestChecker(EST,sd);
			TimeStampCertificateChecker tscc = new TimeStampCertificateChecker(sd);
			SigningTimeChecker stc = new SigningTimeChecker(sd);

            try { //if it is P4, validate signature timestamp without finders
				TurkishESigProfile profile = (TurkishESigProfile) getParameters().get(AllEParameters.P_VALIDATION_PROFILE);
				if(profile == null)
					profile = aSigner.getSignerInfo().getProfile();

                if(profile == TurkishESigProfile.P4_1){
                    tscc.setCloseFinders(true);
                }
            } catch (ESYAException e) {
				logger.error("Error in SignatureTimeStampAttrChecker", e);
                aCheckerResult.addMessage(new ValidationMessage("Error while decoding turkish profile"));
                return false;
            }

			checkers.add(tssc);
			checkers.add(tstc);
			checkers.add(tsmdc);
			checkers.add(tscc);
			checkers.add(stc);

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
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_TS_CHECK_UNSUCCESSFUL)));
		}
		else
		{
			aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_TS_CHECK_SUCCESSFUL)));
			aCheckerResult.setResultStatus(SUCCESS);
		}
		
		return allResult;
	}

}
