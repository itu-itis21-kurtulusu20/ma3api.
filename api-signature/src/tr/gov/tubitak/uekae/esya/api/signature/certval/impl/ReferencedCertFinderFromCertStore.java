package tr.gov.tubitak.uekae.esya.api.signature.certval.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStore;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.CertStoreException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci.OzetTipi;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.DepoKokSertifika;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.ops.CertStoreRootCertificateOps;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.template.CertificateSearchTemplate;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.signature.certval.ReferencedCertificateFinder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ayetgin
 * Finds the referenced certificate from the certificate store.
 */
public class ReferencedCertFinderFromCertStore implements ReferencedCertificateFinder
{
    private static final Logger logger = LoggerFactory.getLogger(ReferencedCertFinderFromCertStore.class);

    /**
     * Creates a certificate search template using given criteria. If digest algorithm is present,
     * then it is used for creating template; otherwise both serial and subject key identifier is
     * used if they are present.
     * @param aCriteria criteria to be used to create template
     * @return a certificate search template to be used for finding in certificate store
     */
    public CertificateSearchTemplate createSearchTemplate(CertificateSearchCriteria aCriteria)
    {
        CertificateSearchTemplate certSearchTemplate = new CertificateSearchTemplate();

        if (aCriteria.getDigestAlg() != null) {
            certSearchTemplate.setHashType(OzetTipi.fromDigestAlg(aCriteria.getDigestAlg()));
            certSearchTemplate.setHash(aCriteria.getDigestValue());
        }
        else { //*/
            /*String subject = aCriteria.getSubject();
            if (subject != null && subject.length() > 0){
                try {
                EName name = new EName(UtilName.string2Name(subject, true));
                certSearchTemplate.setSubject(name.getEncoded());
                }
                catch (Exception e) {
                    logger.error("Error decoding certificate subject",e);
                }
            }*/
            /*
            String issuer = aCriteria.getIssuer();
            if (issuer != null && issuer.length() > 0){
                try {
                EName name = new EName(UtilName.string2Name(issuer, true));
                certSearchTemplate.setIssuer(name.getEncoded());
                }
                catch (Exception e) {
                    logger.error("Error decoding certificate issuer",e);
                }
            }
            //*/
            BigInteger serial = aCriteria.getSerial();
            if (serial != null)
                certSearchTemplate.setSerial(serial.toByteArray());

            if (aCriteria.getSubjectKeyIdentifier() != null)
                certSearchTemplate.setSubjectKeyID(aCriteria.getSubjectKeyIdentifier());
        }

        return certSearchTemplate;
    }

    public List<ECertificate> find(CertificateSearchCriteria aCriteria)
            throws ESYAException {
        if (aCriteria == null)
            throw new ArgErrorException("Expected search criteria");


        // search criteria
        CertificateSearchTemplate certSearchTemplate = createSearchTemplate(aCriteria);

        return locate(certSearchTemplate);
    }

    protected List<ECertificate> locate(CertificateSearchTemplate aSearchTemplate) {
        CertStore certStore;
        try {
            certStore = new CertStore();
        } catch (
                CertStoreException aEx) {
            logger.error("Sertifika deposuna ulaşılamadı", aEx);
            return null;
        }

        List<ECertificate> certificates = new ArrayList<ECertificate>();
        CertStoreCertificateOps certStoreCertOps = new CertStoreCertificateOps(certStore);
        try {
            certificates = certStoreCertOps.listCertificates(aSearchTemplate);
        } catch (CertStoreException e) {
            logger.error("depodan sertifikalar listelenirken hata olustu");
            e.printStackTrace();
        }

        /* burasi zaten kodlanmis
        ItemSource<DepoSertifika> sertifikaItemSource = null;
        DepoSertifika depoSertifika;

        List<ECertificate> certificates = new ArrayList<ECertificate>();
        try {
            sertifikaItemSource = certStoreCertOps.listStoreCertificate(aSearchTemplate);
            depoSertifika = sertifikaItemSource.nextItem();

            while (depoSertifika != null) {
                try {
                    ECertificate certificate = new ECertificate(depoSertifika.getValue());
                    certificates.add(certificate);
                } catch (ESYAException aEx) {
                    logger.warn("Depodan alınan sertifika oluşturulurken hata oluştu", aEx);
                }
                depoSertifika = sertifikaItemSource.nextItem();
            }

        } catch (CertStoreException aEx) {
            logger.error("Sertifikalar listelenirken hata oluştu", aEx);
            return null;
        } catch (ESYAException aEx) {
            logger.error("İlk depo sertifika nesnesi alınırken hata oluştu", aEx);
            return null;
        } finally {
            if (sertifikaItemSource != null) sertifikaItemSource.close();
            /*try {
                certStore.closeConnection();
            } catch (CertStoreException e) {
                logger.error("Connection couldn't closed", e);
            }/
        }
        //*/


        // eger sertifika tablosunda bulunamadiysa koksertifika tablosuna bak
        if(certificates.isEmpty()) {

            CertStoreRootCertificateOps certStoreRootCertificateOps = new CertStoreRootCertificateOps(certStore);
            List<DepoKokSertifika> depoKokSertifikaList = new ArrayList<DepoKokSertifika>();
            try {
                depoKokSertifikaList = certStoreRootCertificateOps.listStoreRootCertificates(aSearchTemplate,null,null);
            }
            catch (CertStoreException aEx) {
                logger.error("Sertifikalar listelenirken hata oluştu", aEx);
            }
            for(DepoKokSertifika depoKokSertifika : depoKokSertifikaList) {
                try {
                    ECertificate certificate = new ECertificate(depoKokSertifika.getValue());
                    certificates.add(certificate);
                }
                catch (ESYAException aEx) {
                    logger.warn("Depodan alınan sertifika oluşturulurken hata oluştu", aEx);
                }

            }
        }

        try {
            certStore.closeConnection();
        } catch (CertStoreException e) {
            logger.error("Connection couldn't closed", e);
        }

        return certificates;
    }

}
