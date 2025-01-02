package tr.gov.tubitak.uekae.esya.api.tsl;

public class TSLException extends Exception {
	public TSLException() {
	}

	public TSLException(String aMessage) {
		super(aMessage);
	}

	public TSLException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public TSLException(Throwable aCause, String aMessage)

	{
		super(aMessage, aCause);
	}

	public TSLException(Throwable aCause)

	{
		super(aCause.getMessage(), aCause);
	}

}
