package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.DateCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.ResponseStatusChecker.ResponseStatusCheckStatus;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.DateUtil;
import tr.gov.tubitak.uekae.esya.asn.ocsp.OCSPResponseStatus;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author ayetgin
 */
public class OCSPResponseDateChecker extends OCSPResponseChecker
{
	private static final Logger logger = LoggerFactory.getLogger(OCSPResponseDateChecker.class);
	
	protected static String PARAM_OCSP_EXPIRE_THRESHOLD =  "ocsp-expire-threshold";
	protected static int DEFAULT_OCSP_EXPIRE_THRESHOLD = 600; // 10dk

    @Override
    PathValidationResult _check(EOCSPResponse aOCSPResponse, OCSPResponseStatusInfo aOCSPResponseInfo) throws ESYAException
    {
    	if(aOCSPResponse.getResponseStatus() != OCSPResponseStatus._SUCCESSFUL)
    	{
    		 logger.error("Response Status SUCCESSFUL degil");
    		 aOCSPResponseInfo.addDetail(this, ResponseStatusCheckStatus.MALFORMED_REQUEST, false);
             return PathValidationResult.OCSP_RESPONSESTATUS_CONTROL_FAILURE;
    	}
    	
    	int ocspExpireThreshold = getOcspExpireThreshold();
    	
    	Calendar baseValidationTimeWithExpireTreshold = (Calendar) mParentSystem.getBaseValidationTime().clone();
    	Calendar lastRevocationTime = mParentSystem.getLastRevocationTime();
    	
    	
    	Calendar thisUpdate = aOCSPResponse.getBasicOCSPResponse().getProducedAt();
    
    	baseValidationTimeWithExpireTreshold.add(Calendar.SECOND, (-1) *  ocspExpireThreshold);

        /*
        if(Math.abs(baseValidationTime.getTimeInMillis() - thisUpdate.getTimeInMillis()) > ocspExpireThreshold)
        {
            logger.error("thisUpdate: " + thisUpdate +
                    "baseValidationTime: " + baseValidationTime + "lastRevocationTime: " + lastRevocationTime +
                    "ocspExpireThreshold: " + ocspExpireThreshold) ;
            aOCSPResponseInfo.addDetail(this, DateCheckStatus.INVALID_DATE, true);
            return PathValidationResult.OCSP_RESPONSEDATE_EXPIRED;
        }
        */
    	
    	if(baseValidationTimeWithExpireTreshold.compareTo(thisUpdate) < 0 && thisUpdate.compareTo(lastRevocationTime) < 0 )
    	{
    		aOCSPResponseInfo.addDetail(this, DateCheckStatus.VALID_DATE, true);
    		return PathValidationResult.SUCCESS;
    	}
    	else
    	{
			logger.error("OCSP tarihi geçerli değil " + DateUtil.formatDateByDayMonthYear24hours(baseValidationTimeWithExpireTreshold.getTime()) + " - [ " +"thisUpdate: " + DateUtil.formatDateByDayMonthYear24hours(thisUpdate.getTime()) +
    				" - "+ " lastRevocationTime: " + DateUtil.formatDateByDayMonthYear24hours(lastRevocationTime.getTime()) + " ]"+
    				" ocspExpireThreshold: " + ocspExpireThreshold+" sn") ;
    		aOCSPResponseInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
    		return PathValidationResult.OCSP_RESPONSEDATE_INVALID;
    	}

    }

	private int getOcspExpireThreshold() 
	{
		if(mCheckParams != null)
		{
			String ocspExpireThreshold = (String) mCheckParams.getParameter(PARAM_OCSP_EXPIRE_THRESHOLD);
			if(ocspExpireThreshold != null)
				return Integer.parseInt(ocspExpireThreshold);
		}
		
		return DEFAULT_OCSP_EXPIRE_THRESHOLD;
	}
}
