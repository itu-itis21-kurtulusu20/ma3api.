package tr.gov.tubitak.uekae.esya.api.cmssignature.validation.check;

import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CMSSignatureI18n;
import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.IValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.Types.CheckerResult_Status;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultDetail;
import tr.gov.tubitak.uekae.esya.api.signature.ValidationResultType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckerResult implements IValidationResult, Serializable, ValidationResultDetail
{
	private int mCheckerLevel = 0;
	private String mCheckerName;
//	private List<ValidationMessage> mMessages = new ArrayList<ValidationMessage>();
	private CheckerResult_Status mResultStatus = CheckerResult_Status.UNSUCCESS;
    private Object mResultObject;
	private List<IValidationResult> mResults = new ArrayList<IValidationResult>();
	private List<CheckerResult> mCheckerResults = null;
	private Class mCheckerClass;
	
	 /**
		 * Returns checker name
		 * @return 
		 */
	public String getCheckerName()
	{
		return mCheckerName;
	}
	/**
	 * Returns checker message
	 * @return 
	 */
    public String getCheckMessage()
    {
        return mCheckerName;
    }

    public void setCheckerName(String aCheckerName, Class aCheckerClass)
	{
		mCheckerName = aCheckerName;
		mCheckerClass = aCheckerClass;
	}
    /**
	 * Returns checker class
	 * @return 
	 */
	public Class getCheckerClass()
	{
		return mCheckerClass;
	}
	/**
	 * Returns checker class
	 * @return 
	 */
    public Class getValidatorClass()
    {
        return mCheckerClass;
    }

    /*public List<ValidationMessage> getMessages()
     {
         return mMessages;
     }

     public void addMessage(ValidationMessage aMessage)
     {
         mMessages.add(aMessage);
     }*/
    /**
	 * Returns checker result status
	 * @return 
	 */
	public CheckerResult_Status getResultStatus()
	{
		return mResultStatus;
	}


	
	public void setResultStatus(CheckerResult_Status aResultStatus)
	{
		mResultStatus = aResultStatus;
	}
	/**
	 * Returns checker result object
	 * @return 
	 */
    public Object getResultObject()
    {
        return mResultObject;
    }
    /**
   	 * Set result object of checker
   	 * @param aResultObject
   	 */
    public void setResultObject(Object aResultObject)
    {
        mResultObject = aResultObject;
    }

	public void setCheckerLevel(int mCheckerLevel) {
		this.mCheckerLevel = mCheckerLevel;
	}

	/**
	 * Returns messages  of result 
	 * @return 
	 */
    public List<IValidationResult> getMessages()
	{
		return mResults;
	}
    /**
   	 * Add messages  of result 
   	 * @param aResult
   	 */
	public void addMessage(IValidationResult aResult)
	{
		mResults.add(aResult);
	}
    /**
     * Clear all messages 
     */	
	protected void removeMessages()
	{
		mResults.clear();
	}	
	 /**
   	 * Add checker result
   	 * @param aCheckerResult
   	 */
	public void addCheckerResult(CheckerResult aCheckerResult)
	{
		if(mCheckerResults==null)
			mCheckerResults = new ArrayList<CheckerResult>();
		
		mCheckerResults.add(aCheckerResult);
	}
	 /**
		 * Returns results of checker
		 * @return 
		 */
	public List<CheckerResult> getCheckerResults()
	{
		return mCheckerResults;
	}
	/**
	 * Returns results of checker as string
	 * @return 
	 */
    public String getCheckResult()
    {
        StringBuilder buffer = new StringBuilder();
        for(IValidationResult vr:mResults)
        {
            buffer.append(vr.toString());
        }

        return buffer.toString();
    }
    /**
	 * Returns results of checker
	 * @return 
	 */
    public List<CheckerResult> getDetails(){
        return mCheckerResults == null ? Collections.EMPTY_LIST : mCheckerResults;
    }
    /**
	 * Returns result type of checker(VALID,INVALID,INCOMPLETE)
	 * @return 
	 */
    public ValidationResultType getResultType()
    {
        switch (mResultStatus){
           case SUCCESS   : return ValidationResultType.VALID;
           case UNSUCCESS : return ValidationResultType.INVALID;
           default        : return ValidationResultType.INCOMPLETE;
        }
    }
    
    /**
	 * Convert checker result of signatures to string
	 */
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
       
        for(int i = 0; i < mCheckerLevel; i++)
    		result.append("\t");
        
        result.append(mResultStatus.getIdentifier()).append(" ").append(mCheckerName).append("\n");
        

        for(IValidationResult vr:mResults)
        {
        	
        	for(int i = 0; i < mCheckerLevel; i++)
        		result.append("\t");
        	
        	result.append("\t");
            result.append(vr.toString());
        }


        if(mCheckerResults!=null)
        {
        	
        	for(int i = 0; i < mCheckerLevel; i++)
        		result.append("\t");
        	
            result.append(CMSSignatureI18n.getMsg(E_KEYS.SUB_CHECKER_RESULTS));

            for(CheckerResult cr:mCheckerResults)
            {
            	cr.setCheckerLevel(mCheckerLevel + 1);
                result.append(cr.toString());
            }

        }
        return result.toString();
    }
    /**
	 * Prints checker result
	 */
    public void printDetails()
	{
		System.out.println(toString());
	}
}
