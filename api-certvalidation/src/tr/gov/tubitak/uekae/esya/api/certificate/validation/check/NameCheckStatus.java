package tr.gov.tubitak.uekae.esya.api.certificate.validation.check;

import tr.gov.tubitak.uekae.esya.api.certificate.i18n.CertI18n;

public enum NameCheckStatus implements CheckStatus
{
    CERTIFICATE_NO_ISSUER,
    ISSUERCERTIFICATE_NO_SUBJECT,
    ISSUER_SUBJECT_MATCH_OK,
    ISSUER_SUBJECT_MISMATCH;

    public String getText()
    {
        switch(this)
        {
        case CERTIFICATE_NO_ISSUER: return CertI18n.message(CertI18n.SERTIFIKA_ISSUER_YOK);
        case ISSUERCERTIFICATE_NO_SUBJECT: return CertI18n.message(CertI18n.SMSERTIFIKA_SUBJECT_YOK);
        case ISSUER_SUBJECT_MATCH_OK: return CertI18n.message(CertI18n.ISSUER_SUBJECT_UYUMLU);
        case ISSUER_SUBJECT_MISMATCH: return CertI18n.message(CertI18n.ISSUER_SUBJECT_UYUMSUZ);

        default	: return CertI18n.message(CertI18n.KONTROL_SONUCU);
        }
    }

}
