package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.deltacrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.FindSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl.CRLStatusInfo;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs delta-CRL validation. 
 */
public class DeltaCRLController
{
    private static Logger logger = LoggerFactory.getLogger(DeltaCRLController.class);

    List<ECertificate> mGuvenilirSertifikalar = new ArrayList<ECertificate>();

    public DeltaCRLController()
    {
    }

    public DeltaCRLController(List<ECertificate> aTrustedCertificates)
    {
        mGuvenilirSertifikalar = aTrustedCertificates;
    }

    /**
     * delta-SİL'in geçerliliğini kontrol eder
     */
    public CRLStatusInfo check(ValidationSystem aValidationSystem, ECRL aCRL) throws ESYAException
    {
        //durum bilgisi oluşturalım
        CRLStatusInfo statusInfo = new CRLStatusInfo(aCRL);

        String issuer = aCRL.getIssuer().stringValue();
        if (logger.isDebugEnabled())
            logger.debug(issuer + " issuerlu sil kontrolü yapılacak");

        FindSystem bs = aValidationSystem.getFindSystem();
        CheckSystem ks = aValidationSystem.getCheckSystem();

        if (ks.checkDeltaCRL(aCRL, statusInfo) == PathValidationResult.SUCCESS) {
            if (logger.isDebugEnabled())
                logger.debug(issuer + " : Delta sil kontrolleri tamam");
            //sili imzalayan sertifikayı bulalım
            long certificateOrder = ks.getConstraintCheckParam().getCertificateOrder();
            ks.getConstraintCheckParam().setCertificateOrder(-1);

            ECertificate issuingCertificate = bs.findCRLIssuerCertificate(aValidationSystem, statusInfo);

            ks.getConstraintCheckParam().setCertificateOrder(certificateOrder);

            if (issuingCertificate != null) {
                //geçerliyse imzalayan sertifika ile ilgili geçerlilik kontrollerini yapalım
                if (ks.checkCRLIssuer(aValidationSystem.getCheckSystem().getConstraintCheckParam(), aCRL, issuingCertificate, statusInfo) == PathValidationResult.SUCCESS) {
                    logger.debug(issuer + " : Delta sil durumu gecerli");
                    statusInfo.setCRLStatus(CRLStatus.VALID);
                }
            }
            else {
                logger.error(issuer + " : Sil imzalayan sertifika bulunamadı");
                statusInfo.setCRLStatus(CRLStatus.ISSUING_CERTIFICATE_INVALID);
            }
        }
        else {
            logger.error(issuer + " : Tek sil kontrolleri sorunlu");
            statusInfo.setCRLStatus(CRLStatus.CRL_INVALID);
        }
        return statusInfo;
    }

}
