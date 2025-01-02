package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.Constants;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EExtendedKeyUsage;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.CheckStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.PathValidationResult;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateController;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.issuer.IssuerCheckParameters;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.ocsp.OCSPSignatureChecker.OCSPSignatureCheckStatus;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;

/**
 * Checks if the signing certificate of the OCSP Response is valid. 
 */
public class SigningCertificateChecker extends OCSPResponseChecker
{
	 private static final Logger logger = LoggerFactory.getLogger(SigningCertificateChecker.class);

    public enum SigningCertificateCheckStatus implements CheckStatus
    {

        VALID,
        INVALID;

        public String getText()
        {
            switch (this) {
                case VALID:
                    return CertI18n.message(CertI18n.IMZALAYAN_SERTIFIKA_GECERLI);
                case INVALID:
                    return CertI18n.message(CertI18n.IMZALAYAN_SERTIFIKA_GECERSIZ);

                default:
                    return CertI18n.message(CertI18n.KONTROL_SONUCU);
            }
        }
    }


    /**
     * OCSP Cevabını imzalayan sertifikanın geçerliliğini kontrol eder.
     */
    protected PathValidationResult _check(EOCSPResponse aOCSPResponse,
                                          OCSPResponseStatusInfo aOCSPResponseInfo)
            throws ESYAException
    {
        if (mParentSystem == null)
            return null;

        ECertificate certificate = aOCSPResponse.getBasicOCSPResponse().getCertificate(0);
        
        // sertifikadaki ExtendedKeyUsage değerini kontrol et
        if (!_ocspSertifikasiMi(certificate)) {
            logger.error("Imzalayici sertifika OCSP imzalayacı özelliğine sahip değil");
            aOCSPResponseInfo.addDetail(this, OCSPSignatureCheckStatus.CERTIFICATE_IS_NOT_OCSP_CERTIFICATE, false);
            return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
        }

        IssuerCheckParameters ukp = mParentSystem.getCheckSystem().getConstraintCheckParam();
        long kacinciSertifika = ukp.getCertificateOrder();
        ukp.setCertificateOrder(-1);

        CertificateController kontrolcu = new CertificateController();
        CertificateStatusInfo pDurumBilgi = kontrolcu.check(mParentSystem, certificate);
        aOCSPResponseInfo.setSigningCertficateInfo(pDurumBilgi);

        ukp.setCertificateOrder(kacinciSertifika);

        if (pDurumBilgi.getCertificateStatus() == CertificateStatus.VALID) {
            aOCSPResponseInfo.addDetail(this, SigningCertificateCheckStatus.VALID, true);
            return PathValidationResult.SUCCESS;
        }
        else {
            aOCSPResponseInfo.addDetail(this, SigningCertificateCheckStatus.INVALID, false);
            return PathValidationResult.OCSP_SIGNATURE_CONTROL_FAILURE;
        }
    }
    
    /**
     * Sertifikanın OCSP-Cevabı imzalama özelliği var mı kontrol eder
     */
    private boolean _ocspSertifikasiMi(ECertificate aSertifika)
    {
        EExtendedKeyUsage eku = aSertifika.getExtensions().getExtendedKeyUsage();
        // todo return (eku != null) && eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);
        return (eku == null) || eku.hasElement(Constants.IMP_ID_KP_OCSPSIGNING);

    }


    public String getCheckText()
    {
        return CertI18n.message(CertI18n.OCSP_IMZALAYAN_SERTIFIKA_KONTROLU);
    }

}
