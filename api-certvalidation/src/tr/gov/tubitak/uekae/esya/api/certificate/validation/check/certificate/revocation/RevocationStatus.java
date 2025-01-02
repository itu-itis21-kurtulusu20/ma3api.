package tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation;

import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;


/**
 * <p>Sertifika iptal kontrolü muhtemel sonuçlarını tanımlamaktadır.</p>
 *
 * @author IH
 */
public enum RevocationStatus {
    VALID,
    REVOKED,
    HOLD;

    public String textAl()
    {
        switch (this) {
            case VALID:
                return CertI18n.message(CertI18n.GECERLI);
            case REVOKED:
                return CertI18n.message(CertI18n.IPTAL_EDILMIS);
            case HOLD:
                return CertI18n.message(CertI18n.ASKIDA);

            default:
                return CertI18n.message(CertI18n.KONTROL_SONUCU);
        }
    }
}
