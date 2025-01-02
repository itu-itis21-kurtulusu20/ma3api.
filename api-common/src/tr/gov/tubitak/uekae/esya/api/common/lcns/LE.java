package tr.gov.tubitak.uekae.esya.api.common.lcns;

public class LE extends Exception 
{
	public LE() 
	{}

    public LE(String aMessage) 
    {
      super(aMessage);
    }

    public LE(String aMessage, Throwable aCause) 
    {
      super(aMessage, aCause);
    }

    public LE(Throwable aCause) 
    {
      super(aCause);
    }

}
