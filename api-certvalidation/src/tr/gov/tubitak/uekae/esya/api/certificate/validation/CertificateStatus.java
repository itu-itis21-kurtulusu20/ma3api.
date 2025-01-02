package tr.gov.tubitak.uekae.esya.api.certificate.validation;

import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;

/**
 * <p>Sertifika kontrol muhtemel sonuçlarını tanımlamaktadır.</p>
 * @author IH
 */
public enum CertificateStatus
{
    VALID,
    REVOCATION_CHECK_FAILURE,
    CERTIFICATE_SELF_CHECK_FAILURE,
    NO_TRUSTED_CERT_FOUND,
    PATH_VALIDATION_FAILURE,
    NOT_CHECKED;

    public String textAl()
    {
        switch (this) {
            case VALID:
                return CertI18n.message(CertI18n.GECERLI);
            case REVOCATION_CHECK_FAILURE:
                return CertI18n.message(CertI18n.IPTAL_KONTROLU_SORUNLU);
            case CERTIFICATE_SELF_CHECK_FAILURE:
                return CertI18n.message(CertI18n.SERTIFIKA_SORUNLU);
            case NO_TRUSTED_CERT_FOUND:
            	return CertI18n.message(CertI18n.NOTRUSTEDCERTFOUND);
            case PATH_VALIDATION_FAILURE:
                return CertI18n.message(CertI18n.ZINCIR_SORUNLU);
            //todo  case NOT_CHECKED : return CertI18n.mesaj(CertI18n.KONTROL_YAPILMADI);

            default:
                return CertI18n.message(CertI18n.KONTROL_SONUCU);
        }
    }
}
