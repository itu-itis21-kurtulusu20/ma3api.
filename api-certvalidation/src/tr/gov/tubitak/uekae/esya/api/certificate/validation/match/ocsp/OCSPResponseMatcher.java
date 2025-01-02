package tr.gov.tubitak.uekae.esya.api.certificate.validation.match.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.match.Matcher;

/**
 * @author ayetgin
 */
public abstract class OCSPResponseMatcher extends Matcher
{
    protected abstract boolean _matchOCSPResponse(ECertificate aCertificate, ECertificate aIssuer, EOCSPResponse aOCSPResponse);

    public boolean matchOCSPResponse(ECertificate aCertificate, ECertificate aIssuer, EOCSPResponse aOCSPResponse)
    {
        return (_matchOCSPResponse(aCertificate, aIssuer,aOCSPResponse));
    }

}
