package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.crl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CheckSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.FindSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.ValidationSystem;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs CRL validation. 
 */
public class CRLController
{

    private static Logger logger = LoggerFactory.getLogger(CRLController.class);

    protected List<ECertificate> mTrustedCertificates = new ArrayList<ECertificate>();

    public CRLController()
    {
    }

    public CRLController(List<ECertificate> aTrustedCertificates)
    {
        mTrustedCertificates = aTrustedCertificates;
    }

    /**
     * SİL doğrulaması yapar
     */
    public CRLStatusInfo check(ValidationSystem aValidationSystem, ECRL aCRL)
            throws ESYAException
    {
        if (logger.isDebugEnabled()){
            logger.debug("----------------- Check CRL ---\n");
            logger.debug(aCRL.toString());
            logger.debug("------------------------------ ");
        }


        CRLStatusInfo crlStatusInfo = aValidationSystem.getCRLValidationCache().getCheckResult(aCRL);

        // Daha önceden kontrol edilmiş ise hafızamdaki sonucu dönüyorum
        if (crlStatusInfo != null)
            return crlStatusInfo.clone();

        // durum bilgisi oluşturalım
        crlStatusInfo = new CRLStatusInfo(aCRL);

        ////////
        //crlStatusInfo.setCRLInfo(SilDurumBilgisi::VALID);
        //return crlStatusInfo;
        /////////

        FindSystem bs = aValidationSystem.getFindSystem();
        CheckSystem ks = aValidationSystem.getCheckSystem();

        // guvenilir sertifika listesini oluşturalım.
        bs.findTrustedCertificates();


        if (ks.checkCRLSelf(aCRL, crlStatusInfo) == PathValidationResult.SUCCESS) {
            // sili imzalayan sertifikayı bulalım
            IssuerCheckParameters icp = ks.getConstraintCheckParam();
            long kacinciSertifika = icp.getCertificateOrder();
            icp.setCertificateOrder(-1);

            ECertificate issuerCertificate = bs.findCRLIssuerCertificate(aValidationSystem, crlStatusInfo);

            icp.setCertificateOrder(kacinciSertifika);

            if (issuerCertificate != null) {
                if (ks.checkCRLIssuer(aValidationSystem.getCheckSystem().getConstraintCheckParam(), aCRL, issuerCertificate, crlStatusInfo) == PathValidationResult.SUCCESS) {
                    crlStatusInfo.setCRLStatus(CRLStatus.VALID);
                    issuerCertificate = null;
                    aValidationSystem.getCRLValidationCache().addCheckResult(crlStatusInfo.clone());
                    return crlStatusInfo;
                }
                else {
                    crlStatusInfo.setCRLStatus(CRLStatus.CRL_INVALID);
                }
            }
            else {
                logger.error("CRL issuing certificate invalid");
                crlStatusInfo.setCRLStatus(CRLStatus.ISSUING_CERTIFICATE_INVALID);
            }
            issuerCertificate = null;
        }
        else {
            crlStatusInfo.setCRLStatus(CRLStatus.CRL_INVALID);
        }

        aValidationSystem.getCRLValidationCache().addCheckResult(crlStatusInfo.clone());
        return crlStatusInfo;
    }

}
