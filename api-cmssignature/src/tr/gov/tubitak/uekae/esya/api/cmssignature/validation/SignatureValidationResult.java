package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CertificateCheckerResultObject;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.CheckerResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check.ValidationState;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.Signature_Status;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;

/**
 * Keeps result of signature
 * @author orcun.ertugrul
 *
 */

public class SignatureValidationResult implements IValidationResult, Serializable, tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult
{
	private String mName;
	private String mDescription;
	private Signature_Status mSignatureStatus;
	private ECertificate mCertificate;
    private CertificateStatusInfo mCertStatusInfo;
	private List<CheckerResult> mCheckDetails = null;
	private List<SignatureValidationResult> mCounterSigValidationRslts = null;
	private ValidationState mValidationState;
	private Calendar mSigningTime;
	
	
	/**
	 * gets name
	 * @return
	 */
	public String getName()
	{
		return mName;
	}
	
	/**
	 * sets name
	 * @param aName
	 */
	public void setName(String aName)
	{
		mName = aName;
	}
	
	/**
	 * sets description of result
	 * @return
	 */
	public String getDescription()
	{
		return mDescription;
	}
	
	/**
	 * gets description of result
	 * @param aDesc
	 */
	public void setDescription(String aDesc)
	{
		mDescription = aDesc;
	}
	
	/**
	 * gets signature status
	 * @return
	 */
	public Signature_Status getSignatureStatus()
	{
		return mSignatureStatus;
	}

    public ValidationResultType getResultType()
    {
        switch (mSignatureStatus){
            case INCOMPLETE : return ValidationResultType.INCOMPLETE;
            case INVALID    : return ValidationResultType.INVALID;
            case VALID      : return ValidationResultType.VALID;
        }
        throw new SignatureRuntimeException("Unknown signature status : "+mSignatureStatus);
    }

    /**
	 * sets signature status
	 * @param aStatus
	 */
	public void setSignatureStatus(Signature_Status aStatus)
	{
		mSignatureStatus = aStatus;
	}
	
	/**
	 * gets signer certificate
	 * @return
	 */
	public ECertificate getSignerCertificate()
	{
		return mCertificate;
	}
	
	/**
	 * sets signer certificate
	 * @param aCert
	 */
	public void setSignerCertificate(ECertificate aCert)
	{
		mCertificate = aCert;
	}

	/**
	 * gets certificate status
	 * @return
	 */
    public CertificateStatusInfo getCertStatusInfo()
    {
        return mCertStatusInfo;
    }

    public CertificateStatusInfo getCertificateStatusInfo()
    {
        return mCertStatusInfo;
    }

    public Calendar getSigningTime()
    {
    	return mSigningTime;
    }

    /**
     * sets certificate status
     * @param aCertStatusInfo
     */
    public void setCertStatusInfo(CertificateStatusInfo aCertStatusInfo)
    {
        mCertStatusInfo = aCertStatusInfo;
    }

    /**
     * gets all checker's results
     * @return
     */
    public List<CheckerResult> getCheckerResults()
	{
		return mCheckDetails;
	}
    
    /***
     * sets validation state
     * @param validationState
     */
    public void setValidationState(ValidationState validationState)
    {
    	mValidationState = validationState;
    }
    
    /**
     * gets validation state
     * @return
     */
    public ValidationState getValidationState()
    {
    	return mValidationState;
    }
	
    /**
     * adds checker result
     * @param aResult
     */
	public void addCheckResult(CheckerResult aResult)
	{
		if(mCheckDetails==null)
		{
			mCheckDetails = new ArrayList<CheckerResult>();
		}
        if (aResult.getResultObject()!=null){
            if (aResult.getResultObject() instanceof CertificateCheckerResultObject){
            	CertificateCheckerResultObject certChecherResulObj =  (CertificateCheckerResultObject)aResult.getResultObject();
                mCertStatusInfo = certChecherResulObj.getCertStatusInfo();
                mSigningTime = certChecherResulObj.getSigningTime();
            }
        }
		mCheckDetails.add(aResult);
	}
	
	/**
	 * gets counter signature validation results
	 * @return
	 */
	public List<SignatureValidationResult> getCounterSigValidationResults()
	{
		return mCounterSigValidationRslts;
	}

    public List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult> getCounterSignatureValidationResults()
    {
        if (mCounterSigValidationRslts==null)
            return Collections.EMPTY_LIST;
        return (List<tr.gov.tubitak.uekae.esya.api.signature.SignatureValidationResult>)new ArrayList(mCounterSigValidationRslts);
    }

    /**
	 * adds counter signature validation result
	 * @param aResult
	 */
	public void addCounterSigValidationResult(SignatureValidationResult aResult)
	{
		if(mCounterSigValidationRslts==null)
		{
			mCounterSigValidationRslts = new ArrayList<SignatureValidationResult>();
		}
		mCounterSigValidationRslts.add(aResult);
	}

	/**
	 * returns checkers' results of signature and counter signatures
	 */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append(getValidationDetails());
        
        if(mCounterSigValidationRslts!=null)
        {
            result.append(CMSSignatureI18n.getMsg(E_KEYS.COUNTER_SIGNATURE_VERIFICATION_RESULTS));
            int i = 1;
            for(SignatureValidationResult svr:mCounterSigValidationRslts)
            {
                result.append(i++).append(CMSSignatureI18n.getMsg(E_KEYS.COUNTER_SIGNATURE_CHECKED));
                result.append(CMSSignatureI18n.getMsg(E_KEYS.SIGNER_CERTIFICATE))
                        .append(svr.getSignerCertificate().getSubject().stringValue())
                        .append("\n");
                result.append(svr.toString());
            }
        }
        return result.toString();
    }

    public String getCheckMessage()
    {
        return mName!=null ? mName : "Imza kontrolu"; //todo i18n
    }

    public String getCheckResult()
    {
        return mDescription!=null ? mDescription : mSignatureStatus.name();
    }

    public List<ValidationResultDetail> getDetails()
    {
        List<ValidationResultDetail> result = new ArrayList<ValidationResultDetail>(mCheckDetails.size());
        result.addAll(mCheckDetails);
        return result;
    }

    /**
     * returns checkers' results of only this signature
     * @return
     */
    public String getValidationDetails()
    {
    	 StringBuilder result = new StringBuilder();
         
    	 if(mValidationState == ValidationState.PREMATURE)
         	result.append(CMSSignatureI18n.getMsg(E_KEYS.PRE_VERIFICATION_DONE));
         if(mCheckDetails!=null)
         {
             result.append(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_CHECKER_RESULTS));
             for(CheckerResult cr:mCheckDetails)
             {
                 result.append(cr.toString());
             }
         }
         return result.toString();
    }

    public void printDetails()
	{
        System.out.println(toString());
	}
}
