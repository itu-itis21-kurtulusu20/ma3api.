using System;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check
{
    [Serializable]
    public class NameCheckStatus : CheckStatus
    {
        public static readonly NameCheckStatus CERTIFICATE_NO_ISSUER = new NameCheckStatus(_enum.CERTIFICATE_NO_ISSUER);
        public static readonly NameCheckStatus ISSUERCERTIFICATE_NO_SUBJECT = new NameCheckStatus(_enum.ISSUERCERTIFICATE_NO_SUBJECT);
        public static readonly NameCheckStatus ISSUER_SUBJECT_MATCH_OK = new NameCheckStatus(_enum.ISSUER_SUBJECT_MATCH_OK);
        public static readonly NameCheckStatus ISSUER_SUBJECT_MISMATCH = new NameCheckStatus(_enum.ISSUER_SUBJECT_MISMATCH);
        
        enum _enum
        {
            CERTIFICATE_NO_ISSUER,
            ISSUERCERTIFICATE_NO_SUBJECT,
            ISSUER_SUBJECT_MATCH_OK,
            ISSUER_SUBJECT_MISMATCH
        }

        readonly _enum mValue;
        
        NameCheckStatus(_enum aEnum)
        {
            mValue = aEnum;
        }
        
        public String getText()
        {
            switch (mValue)
            {
                case _enum.CERTIFICATE_NO_ISSUER: return Resource.message(Resource.SERTIFIKA_ISSUER_YOK);
                case _enum.ISSUERCERTIFICATE_NO_SUBJECT: return Resource.message(Resource.SMSERTIFIKA_SUBJECT_YOK);
                case _enum.ISSUER_SUBJECT_MATCH_OK: return Resource.message(Resource.ISSUER_SUBJECT_UYUMLU);
                case _enum.ISSUER_SUBJECT_MISMATCH: return Resource.message(Resource.ISSUER_SUBJECT_UYUMSUZ);

                default: return Resource.message(Resource.KONTROL_SONUCU);
            }
        }
    }
}
