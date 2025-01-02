package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.Checker;

import java.io.Serializable;

/**
 * Stores the details of the result of control operations performed by the
 * Checker objects 
 */
public class CheckResult implements Serializable
{
    private Checker mChecker;

	private CheckStatus mResult;
	private String mCheckText;
	private String mResultText;
	private boolean mSuccessful;

    public CheckResult()
    {
    }

    public CheckResult(String aCheckText, String aResultText, CheckStatus aResult, boolean aSuccessful)
    {
        mResult = aResult;
        mCheckText = aCheckText;
        mResultText = aResultText;
        mSuccessful = aSuccessful;
    }

    public Checker getChecker()
    {
        return mChecker;
    }

    public void setChecker(Checker aChecker)
    {
        mChecker = aChecker;
    }

	public boolean isSuccessful()
	{
		return mSuccessful;
	}

	public void setSuccessful(boolean aBasarili)
	{
		mSuccessful = aBasarili;
	}

	public String getCheckText()
	{
		return mCheckText;
	}

	public void setCheckText(String aKontrolText)
	{
		mCheckText = aKontrolText;
	}

	public CheckStatus getResult()
	{
		return mResult;
	}

	public void setResult(CheckStatus aSonuc)
	{
		mResult = aSonuc;
	}

	public String getResultText()
	{
		return mResultText;
	}

	public void setResultText(String aSonucText)
	{
		mResultText = aSonucText;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb =new StringBuilder();
		
		sb.append(mCheckText + System.getProperty("line.separator"));
		sb.append("\t" + mResultText);
		
		return sb.toString();
	}
}

