package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer;

import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRLDistributionPoints;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;

/**
 * Checks CDP extension in the CRL is valid. 
 */
public class CRLDistributionPointsChecker extends IssuerChecker {

    /**
     * The cRLDistributionPoints extension is a SEQUENCE of
     * DistributionPoint. A DistributionPoint consists of three fields,
     * each of which is optional: distributionPoint, reasons, and cRLIssuer.
     * While each of these fields is optional, a DistributionPoint MUST NOT
     * consist of only the reasons field; either distributionPoint or
     * cRLIssuer MUST be present. If the certificate issuer is not the CRL
     * issuer, then the cRLIssuer field MUST be present and contain the Name
     * of the CRL issuer. If the certificate issuer is also the CRL issuer,
     * then the cRLIssuer field MUST be omitted and the distributionPoint
     * field MUST be present. If the distributionPoint field is omitted,
     * cRLIssuer MUST be present and include a Name corresponding to an
     * X.500 or LDAP directory entry where the CRL is located.
     *
     * @param aCheckParameters          Zincir Kontrol Parametreleri
     * @param aUstSertifika          Sertifika zincirindeki bir üst sertifika
     * @param aCertificate             Doğrulaması yapılan sertifika
     * @param aCertStatusInfo Sertifika Durum Bilgisi
     * @return boolean Kontrol başarılı mı?
     */
    protected PathValidationResult _check(IssuerCheckParameters aCheckParameters,
                                          ECertificate aUstSertifika, ECertificate aCertificate,
                                          CertificateStatusInfo aCertStatusInfo)
    {
        /* NOT IMPLEMENTED YET */

        ECRLDistributionPoints cdp = aUstSertifika.getExtensions().getCRLDistributionPoints();
        if (cdp == null) {
            /* CDP IS ABSENT*/
            return PathValidationResult.SUCCESS;
        }

        for (int i = 0; i < cdp.getCRLDistributionPointCount(); i++) {
            /* Buraya bakilacak...*/
        }


        return PathValidationResult.SUCCESS;
    }

}
