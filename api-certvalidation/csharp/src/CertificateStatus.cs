using System;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation
{
    /**
     * <p>Sertifika kontrol muhtemel sonuçlarını tanımlamaktadır.</p>
     * @author IH
     */
    [Serializable]
    public class CertificateStatus
    {
        public static readonly CertificateStatus VALID = new CertificateStatus(_enum.VALID);
        public static readonly CertificateStatus REVOCATION_CHECK_FAILURE = new CertificateStatus(_enum.REVOCATION_CHECK_FAILURE);
        public static readonly CertificateStatus CERTIFICATE_SELF_CHECK_FAILURE = new CertificateStatus(_enum.CERTIFICATE_SELF_CHECK_FAILURE);
        public static readonly CertificateStatus NO_TRUSTED_CERT_FOUND = new CertificateStatus(_enum.NO_TRUSTED_CERT_FOUND);
        public static readonly CertificateStatus PATH_VALIDATION_FAILURE = new CertificateStatus(_enum.PATH_VALIDATION_FAILURE);
        public static readonly CertificateStatus NOT_CHECKED = new CertificateStatus(_enum.NOT_CHECKED);

        enum _enum
        {
            VALID,
            REVOCATION_CHECK_FAILURE,
            CERTIFICATE_SELF_CHECK_FAILURE,
            NO_TRUSTED_CERT_FOUND,
            PATH_VALIDATION_FAILURE,
            NOT_CHECKED
        }
        
        readonly _enum mValue;
        
        CertificateStatus(_enum aEnum)
        {
            mValue = aEnum;
        }


        public String textAl()
        {
            switch (mValue)
            {
                case _enum.VALID:
                    return Resource.message(Resource.GECERLI);
                case _enum.REVOCATION_CHECK_FAILURE:
                    return Resource.message(Resource.IPTAL_KONTROLU_SORUNLU);
                case _enum.CERTIFICATE_SELF_CHECK_FAILURE:
                    return Resource.message(Resource.SERTIFIKA_SORUNLU);
                case _enum.NO_TRUSTED_CERT_FOUND:
                    return Resource.message(Resource.NO_TRUSTED_CERT_FOUND);
                case _enum.PATH_VALIDATION_FAILURE:
                    return Resource.message(Resource.ZINCIR_SORUNLU);
                case _enum.NOT_CHECKED:
                //todo burada gerekli sabit eklenmeli
                default:
                    return Resource.message(Resource.KONTROL_SONUCU);
            }
        }
    }
}
