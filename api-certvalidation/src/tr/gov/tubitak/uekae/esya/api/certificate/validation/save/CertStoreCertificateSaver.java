package tr.gov.tubitak.uekae.esya.api.certificate.validation.save;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;

/**
 * Saves the certificates found during Certificate/CrL Validation operation to
 * the local Certificate Store.
 *
 * @author IH
 */
public class CertStoreCertificateSaver extends CertificateSaver
{
    public CertStoreCertificateSaver()
    {
        super();
    }

    protected void _addCertificate(ECertificate aCertificate) throws ESYAException
    {
        CertStore depo;
        try
        {
            depo = new CertStore();
        }
        catch (CertStoreException aEx)
        {
            LOGGER.error("Sertifika deposuna ulaşılamadı", aEx);
            throw new ESYAException("Sertifika deposuna ulaşılamadı", aEx);
        }
        CertStoreCertificateOps si = new CertStoreCertificateOps(depo);
        
        try
        {
            // depo dizin numarasını 1 yaptım ama??
            si.writeCertificate(aCertificate, 1L);
        }
        catch (CertStoreException aEx)
        {
            LOGGER.error("Depoya sertifika yazılırken hata oluştu", aEx);
            throw new ESYAException("Depoya sertifika yazılırken hata oluştu", aEx);
        }
    }
}
