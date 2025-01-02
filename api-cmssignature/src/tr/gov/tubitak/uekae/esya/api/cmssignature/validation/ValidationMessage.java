package tr.gov.tubitak.uekae.esya.api.cmssignature.validation;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public class ValidationMessage implements IValidationResult, Serializable
{
	private String mMessage;
	private Throwable mThrowable;
	
	public ValidationMessage(String aMessage)
	{
		mMessage = aMessage;
	}
	
	public ValidationMessage(String aMessage,Throwable aThrowable)
	{
		mMessage = aMessage;
		mThrowable = aThrowable;
	}
	
	public String getMessage()
	{
		return mMessage;
	}
	
	public Throwable getThrowable()
	{
		return mThrowable;
	}

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append(mMessage).append("\n");
        if(mThrowable!=null)
        {
            StringWriter writer = new StringWriter();
            PrintWriter pwriter = new PrintWriter(writer);
            mThrowable.printStackTrace(pwriter);
            result.append(writer.toString());
        }
        return result.toString();
    }

    public void printDetails()
	{
        System.out.println(toString());
	}
	
}
