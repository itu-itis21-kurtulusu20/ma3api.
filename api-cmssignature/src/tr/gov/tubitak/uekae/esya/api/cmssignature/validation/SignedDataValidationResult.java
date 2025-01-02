package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;

/**
 * Keeps results of the signatures a signed document.
 * @author orcun.ertugrul
 *
 */

public class SignedDataValidationResult implements IValidationResult, Serializable
{
	private String mDescription;
	private SignedData_Status mStatus = SignedData_Status.NOT_ALL_VALID;
	private List<SignatureValidationResult> sigValidationResults = new ArrayList<SignatureValidationResult>();
	
	/**
	 * 
	 * @return description of the result of signature
	 */
	public String getDescription()
	{
		return mDescription;
	}
	
	/**
	 * sets the description
	 * @param aDescription
	 */
	public void setDescription(String aDescription)
	{
		mDescription = aDescription;
	}
	
	/**
	 * 
	 * @return the signed data status
	 */
	public SignedData_Status getSDStatus()
	{
		return mStatus;
	}
	
	/**
	 * sets the status
	 * @param aStatus 
	 */
	public void setSDStatus(SignedData_Status aStatus)
	{
		mStatus = aStatus;
	}
	
	/**
	 * 
	 * @return the each result of signature
	 */
	public List<SignatureValidationResult> getSDValidationResults()
	{
		return sigValidationResults;
	}
	
	/**
	 * adds signature validation result
	 * @param aSVR
	 */
	public void addSignatureValidationResult(SignatureValidationResult aSVR)
	{
		sigValidationResults.add(aSVR);
	}

	/**
	 * Convert all validation result of signatures to string
	 */
	public String toString(){
        StringBuilder result = new StringBuilder();
        int i = 1;
        for(SignatureValidationResult svr:sigValidationResults)
        {

            result.append(i++).append(CMSSignatureI18n.getMsg(E_KEYS.SIGNATURE_CHECKED_RESULTS));
            result.append(CMSSignatureI18n.getMsg(E_KEYS.SIGNER_CERTIFICATE)).append(svr.getSignerCertificate().getSubject().stringValue()).append("\n");
            result.append(svr.toString());
        }
        return result.toString();
    }
	/**
	 * Prints all validation result of signatures
	 */
	public void printDetails()
	{
        System.out.println(toString());
	}
	
	

	
}
