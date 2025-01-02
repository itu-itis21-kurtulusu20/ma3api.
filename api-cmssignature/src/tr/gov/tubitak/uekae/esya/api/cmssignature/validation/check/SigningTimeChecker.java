package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignedData;
import tr.gov.tubitak.uekae.esya.api.asn.pkixtsp.ETSTInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AllEParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.AttributeOIDs;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.Signer;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.ValidationMessage;
import tr.gov.tubitak.uekae.esya.api.common.util.DateUtil;
import tr.gov.tubitak.uekae.esya.asn.util.UtilTime;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Checks time ordering between time in signingtime attribute and time in signaturetimestamp attribute
 * @author aslihan.kubilay
 *
 */
public class SigningTimeChecker extends BaseChecker
{

	public static final int MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS  = 86340;
	public static final int MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS = 0;
	public static final int MILLIS_IN_SECOND = 1000;

	private static Logger logger = LoggerFactory.getLogger(SigningTimeChecker.class);
	private ESignedData mSignedData = null;

	public SigningTimeChecker(ESignedData aSignedData)
	{
		mSignedData = aSignedData;
	}

	@Override
	protected boolean _check(Signer aSigner,CheckerResult aCheckerResult){
		aCheckerResult.setCheckerName(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_CHECKER), SigningTimeChecker.class);

		List<EAttribute> stAttrs = aSigner.getSignedAttribute(AttributeOIDs.id_signingTime);
		if(!stAttrs.isEmpty())
		{
			EAttribute stAttr = stAttrs.get(0);
			Time time = new Time();
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(stAttr.getValue(0));
			try
			{
				time.decode(decBuf);
			}
			catch(Exception aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_ATTRIBUTE_DECODE_ERROR),aEx));
				return false;
			}
			
			Calendar signingTime = UtilTime.timeToCalendar(time);
		
			ETSTInfo tstInfo = null;
			try
			{
				tstInfo = new ETSTInfo(mSignedData.getEncapsulatedContentInfo().getContent());
			}
			catch(Exception aEx)
			{
				aCheckerResult.addMessage(new ValidationMessage("Error in decoding tstinfo",aEx));
				return false;
			}
		
		 Calendar stsTime = null;
		 try
		 {
			 stsTime = tstInfo.getTime();
		 }
		 catch(Exception aEx)
		 {
			 aCheckerResult.addMessage(new ValidationMessage("Error in getting time information from tstinfo",aEx));
			 return false;
		 }

        Long signingTimeTolerance = (Long) getParameters().get(AllEParameters.P_TOLERATE_SIGNING_TIME_BY_SECONDS);
		if (signingTimeTolerance.intValue() < MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS || signingTimeTolerance.intValue() > MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS) {
			String errorMessage = "Signing time tolerance value must be between the values" + MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS + " and " + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS  + "!" + "The value" + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "corresponds to 1 day!";
			logger.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);
		}

		 long stsTimeInLong = stsTime.getTimeInMillis();
		 long signingTimeInLong = signingTime.getTimeInMillis();
		 int signingTimeToleranceInMilliSeconds = signingTimeTolerance.intValue()* MILLIS_IN_SECOND;
		 long timeDifference = signingTimeInLong -  stsTimeInLong; // Negatif ise zaten olması gerektiği gibi.

         if(timeDifference > signingTimeToleranceInMilliSeconds)
		 {
			 String signingTimeStr = DateUtil.formatDateByDayMonthYear24hours(signingTime.getTime());
			 String stsTimeStr = DateUtil.formatDateByDayMonthYear24hours(stsTime.getTime());
			 aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_CHECKER_UNSUCCESSFUL, signingTimeStr, stsTimeStr)));
			 return false;
		 }
		 else
		 {
			 aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.SIGNING_TIME_CHECKER_SUCCESSFUL)));
			 aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
			 return true;
		 }
		}
		
		aCheckerResult.addMessage(new ValidationMessage(CMSSignatureI18n.getMsg(E_KEYS.NO_SIGNING_TIME_ATTRIBUTE)));
		aCheckerResult.setResultStatus(CheckerResult_Status.SUCCESS);
		return true;
	}
}
