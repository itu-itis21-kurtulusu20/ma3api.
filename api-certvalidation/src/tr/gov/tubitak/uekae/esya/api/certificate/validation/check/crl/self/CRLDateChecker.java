package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.self;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.DateCheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;
import tr.gov.tubitak.uekae.esya.api.common.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Checks the validity of date information in the CRL
 * todo fix last life span of crl 
 */
public class CRLDateChecker extends CRLSelfChecker
{
    private static Logger logger = LoggerFactory.getLogger(CRLDateChecker.class);

    /**
     * SIL tarihinin geçerli olup olmadığını kontrol eder
     */
    protected PathValidationResult _check(ECRL aCRL, CRLStatusInfo aCRLStatusInfo)
    {
        try {
            Calendar crlThisUpdate = aCRL.getThisUpdate();
            Calendar crlNextUpdate = aCRL.getNextUpdate();

            boolean doNotUsePastRevocationInfo = mParentSystem.isDoNotUsePastRevocationInfo();
            
            logger.debug("doNotUsePastRevocationInfo: " + doNotUsePastRevocationInfo);
            
            Calendar baseValidationTime = mParentSystem.getBaseValidationTime();
            Calendar lastRevocationTime = mParentSystem.getLastRevocationTime();

            if(mParentSystem.isDoNotUsePastRevocationInfo())
            {
            	if(baseValidationTime.compareTo(crlThisUpdate) <= 0 && crlThisUpdate.compareTo(lastRevocationTime) <= 0)
            	{
            		aCRLStatusInfo.addDetail(this, DateCheckStatus.VALID_DATE, true);
                    return PathValidationResult.SUCCESS;
            	}
            	else
            	{
                    logger.error("Sil tarihi geçerli değil " + DateUtil.formatDateByDayMonthYear24hours(baseValidationTime.getTime()) + " - [ " + DateUtil.formatDateByDayMonthYear24hours(crlThisUpdate.getTime())+" - "+DateUtil.formatDateByDayMonthYear24hours(crlNextUpdate.getTime())+" ]");
                    aCRLStatusInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
            	}
            }
            else
            {
            	if(baseValidationTime.compareTo(crlNextUpdate) <= 0 && crlThisUpdate.compareTo(lastRevocationTime) <= 0)
            	{
            		aCRLStatusInfo.addDetail(this, DateCheckStatus.VALID_DATE, true);
                    return PathValidationResult.SUCCESS;
            	}
            	else
            	{
            	    logger.error("Sil tarihi geçerli değil " + DateUtil.formatDateByDayMonthYear24hours(baseValidationTime.getTime()) + " - [ " + DateUtil.formatDateByDayMonthYear24hours(crlThisUpdate.getTime())+" - "+DateUtil.formatDateByDayMonthYear24hours(crlNextUpdate.getTime())+" ]");
                    aCRLStatusInfo.addDetail(this, DateCheckStatus.INVALID_DATE, false);
            	}
            }
            
        }
        catch (Exception x) {
            logger.error("Error in CRLDateChecker" + x.getMessage(), x);
            aCRLStatusInfo.addDetail(this, DateCheckStatus.CORRUPT_DATE_INFO, false);
        }
        return PathValidationResult.CRL_EXPIRED;
    }

    public String getCheckText()
    {
        return CertI18n.message(CertI18n.SIL_TARIH_KONTROLU);
    }
}