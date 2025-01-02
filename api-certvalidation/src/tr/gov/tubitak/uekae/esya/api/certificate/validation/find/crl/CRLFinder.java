package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.crl;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ListItemSource;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;

import java.util.List;

/**
 * Base class for crl finders
 */
public abstract class CRLFinder extends Finder
{
    protected abstract List<ECRL> _findCRL(ECertificate aCertificate) throws ESYAException;

    public List<ECRL> findCRL(ECertificate aCertificate)
            throws ESYAException
    {
        return _findCRL(aCertificate);
    }

    public ItemSource<ECRL> findCRLSource(ECertificate aCertificate) throws ESYAException {
        List<ECRL> crlList = _findCRL(aCertificate);

        return new ListItemSource<ECRL>(crlList);
    }

}
