package tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

public abstract class OCSPResponseSaver extends Saver
{
	public OCSPResponseSaver()
	{
		super();
	}

    public void addOCSP(EOCSPResponse aOcspResponse, ECertificate aCert) throws ESYAException
	{
    	_addOCSP(aOcspResponse, aCert);
	}

	protected abstract void _addOCSP(EOCSPResponse aOcspResponse, ECertificate aCert)
	throws ESYAException;
}
