package tr.gov.tubitak.uekae.esya.api.certificate.validation.find.certificate;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.Finder;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.find.ListItemSource;
import tr.gov.tubitak.uekae.esya.api.common.util.ItemSource;

import java.util.List;

/**
 * Base class for certificate finders
 */
public abstract class CertificateFinder extends Finder
{

    /**
     * Find issuer certificate of the input certificate
     * @param aCertificate input
     */
    public List<ECertificate> findCertificate(ECertificate aCertificate)
    {
        return _findCertificate(aCertificate);
    }

    /**
     * Verilen sertifikanın Ust SM sertifikasını bulur.
     * @param aCertificate Certificate
     * @return ECertificate SM Sertifikası
     */
    public ItemSource<ECertificate> findCertificateSource(ECertificate aCertificate)
    {
        //String subject = aCertificate.getSubject().stringValue();
        //DEBUGLOGYAZ(LOGNAME,QString("%1 sertifika SM sertifikası bulunacak").arg(subject));

        List<ECertificate> certs= _findCertificate(aCertificate);
        return new ListItemSource<ECertificate>(certs);
    }

    /**
     * Ust SM sertifikasını bulur.
     * Silin Ust SM sertifikasını bulacak olan classlar tarafından overwrite edilmeli.
     * @return
     */
    public List<ECertificate> findCertificate()
    {
        return _findCertificate();
    }

    /**
     * Verilen sertifikanın Ust SM sertifikasını bulur.
     * @return ECertificate SM Sertifikası
     */
    public ItemSource<ECertificate> findCertificateSource()
    {
        List<ECertificate> certs= _findCertificate();
        return new ListItemSource<ECertificate>(certs);
    }
    /**
     * Verilen sertifikanın Ust SM sertifikasını bulur.
     * Sertifikanın Ust SM sertifiaksını bulacak olan classlar tarafından overwrite edilmeli.
     * @param aSertifika
     * @return
     */
    protected abstract List<ECertificate> _findCertificate(ECertificate aSertifika);

    /**
     * Ust SM sertifikasını bulur.
     * Silin Ust SM sertifiaksını bulacak olan classlar tarafından overwrite edilmeli.
     * @return
     */
    protected List<ECertificate> _findCertificate()
    {
        return null;
    }
    
    /*public abstract List<ECertificate> searchCertificates(CertificateSearchCriteria aCriteria)
            throws ESYAException;

    public CertificateSearchTemplate createSearchTemplate(CertificateSearchCriteria aCriteria)
            throws ESYAException {
        CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();

        String subject = aCriteria.getSubject();
        if (subject != null && subject.length() > 0)
            certSearchTemplate.setSubject(subject.getBytes());

        String issuer = aCriteria.getIssuer();
        if (issuer != null && issuer.length() > 0)
            certSearchTemplate.setIssuer(issuer.getBytes());

        BigInteger serial = aCriteria.getSerial();
        if (serial != null)
            certSearchTemplate.setSerial(serial.toByteArray());

        if (aCriteria.getSubjectKeyIdentifier() != null)
            certSearchTemplate.setSubjectKeyID(aCriteria.getSubjectKeyIdentifier());

        if (aCriteria.getDigestAlg() != null) {
            certSearchTemplate.setHashType(OzetTipi.fromDigestAlg(aCriteria.getDigestAlg()));
            certSearchTemplate.setHash(aCriteria.getDigestValue());
        }

        return certSearchTemplate;
    }  */

}
