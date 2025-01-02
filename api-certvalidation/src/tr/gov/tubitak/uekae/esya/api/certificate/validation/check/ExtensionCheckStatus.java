package tr.gov.tubitak.uekae.esya.api.certificate.validation.check;

import tr.gov.tubitak.uekae.esya.api.common.bundle.cert.CertI18n;

/**
* @author ahmety
* date: Feb 18, 2010
*/
public enum ExtensionCheckStatus implements CheckStatus
{
    NO_EXTENSION,
    INVALID_EXTENSION,
    DUPLICATE_EXTENSION,
    VALID_EXTENSIONS;

    public String getText()
    {
        switch (this) {
            case NO_EXTENSION:
                return CertI18n.message(CertI18n.EKLENTI_YOK);
            case INVALID_EXTENSION:
                return CertI18n.message(CertI18n.GECERSIZ_EKLENTI);
            case DUPLICATE_EXTENSION:
                return CertI18n.message(CertI18n.AYNI_EXTENSION_BIRDEN_FAZLA);
            case VALID_EXTENSIONS:
                return CertI18n.message(CertI18n.EKLENTILER_GECERLI);

            default:
                return CertI18n.message(CertI18n.KONTROL_SONUCU);
        }
    }
}
