package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate.cross;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

import java.util.List;

/**
 * <p>Base class for finding Cross Certificates</p>
 * 
 * @author IH
 */
public abstract class CrossCertificateFinder extends Finder {

    public List<ECertificate> findCrossCertificate() {
        return _findCrossCertificate();
    }

    protected abstract List<ECertificate> _findCrossCertificate();

}

