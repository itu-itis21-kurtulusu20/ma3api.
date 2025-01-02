package tr.gov.tubitak.uekae.esya.api.cmssignature;


import tr.gov.tubitak.uekae.esya.api.signature.SignatureException;
/**
 * Base exception class for CMS-Signature API related errors.
 */
public class CMSSignatureException extends SignatureException
{
	public CMSSignatureException()
	{}

    public CMSSignatureException(String aMessage) 
    {
      super(aMessage);
    }

    public CMSSignatureException(String aMessage, Throwable aCause) 
    {
      super(aMessage, aCause);
    }

    public CMSSignatureException(Throwable aCause) 
    {
      super(aCause);
    }
}
