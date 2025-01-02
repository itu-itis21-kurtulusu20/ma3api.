package tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

/**
 * Base class for certificate saver classes 
 */
public abstract class CertificateSaver extends Saver
{
    /**
    * Sertifikayı alt sınıflarda belirtildiği şekilde kaydeder
    */
    public void addCertificate(ECertificate aCertificate) throws ESYAException
    {
        _addCertificate(aCertificate);
    }

	protected abstract void _addCertificate(ECertificate aCertificate) throws ESYAException;

}
