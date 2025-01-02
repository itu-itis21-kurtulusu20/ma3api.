package tr.gov.tubitak.uekae.esya.api.certificate.validation.check;

import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;

public enum DateCheckStatus implements CheckStatus
{
    VALID_DATE,
    INVALID_DATE,
    CORRUPT_DATE_INFO;

    public String getText()
    {
        switch(this)
        {
        case VALID_DATE: return CertI18n.message(CertI18n.VALID_DATE_INFO);
        case INVALID_DATE: return CertI18n.message(CertI18n.INVALID_DATE_INFO);
        case CORRUPT_DATE_INFO: return CertI18n.message(CertI18n.CORRUPT_DATE_INFO);

        default	: return CertI18n.message(CertI18n.KONTROL_SONUCU);
        }
    }
}
