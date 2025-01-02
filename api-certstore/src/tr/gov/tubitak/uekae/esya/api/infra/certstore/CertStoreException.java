package tr.gov.tubitak.uekae.esya.api.infra.certstore;



public class CertStoreException extends Exception
{
	public CertStoreException()
	{}

    public CertStoreException(String aMessage)
    {
      super(aMessage);
    }

    public CertStoreException(String aMessage, Throwable aCause)
    {
      super(aMessage, aCause);
    }

    public CertStoreException(Throwable aCause)
    {
      super(aCause);
    }
}
