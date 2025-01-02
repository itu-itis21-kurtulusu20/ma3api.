package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ocsp;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ListItemSource;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;

import java.util.Arrays;

/**
 * Base class for OCSP Response Finders.
 *
 * @author IH
 */
public abstract class OCSPResponseFinder extends Finder {

    protected abstract EOCSPResponse _findOCSPResponse(ECertificate aSertifika, ECertificate aIssuerCert);

    public EOCSPResponse findOCSPResponse(ECertificate aCertificate, ECertificate aIssuerCert) {
        return _findOCSPResponse(aCertificate, aIssuerCert);
    }

    public ItemSource<EOCSPResponse> findOCSPSource(ECertificate aCertificate, ECertificate aIssuerCert) {
        return new ListItemSource<EOCSPResponse>(Arrays.asList(_findOCSPResponse(aCertificate, aIssuerCert)));
    }
}
