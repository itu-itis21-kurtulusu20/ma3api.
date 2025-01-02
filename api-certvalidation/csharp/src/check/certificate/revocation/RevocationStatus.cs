using System;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.revocation
{
    /**
     * <p>Declares possible results of certificate revocation check</p>
     *
     * @author IH
     */
    public class RevocationStatus
    {
        public static readonly RevocationStatus VALID = new RevocationStatus(_enum.VALID);
        public static readonly RevocationStatus REVOKED = new RevocationStatus(_enum.REVOKED);
        public static readonly RevocationStatus HOLD = new RevocationStatus(_enum.HOLD);

        enum _enum
        {
            VALID,
            REVOKED,
            HOLD
        }

        readonly _enum mValue;
        RevocationStatus(_enum aEnum)
        {
            mValue = aEnum;
        }

        public String textAl()
        {
            switch (mValue)
            {
                case _enum.VALID:
                    //return Resource.message(Resource.GECERLI);
                    return Resource.message(Resource.GECERLI);
                case _enum.REVOKED:
                    //return Resource.message(Resource.IPTAL_EDILMIS);
                    return Resource.message(Resource.IPTAL_EDILMIS);
                case _enum.HOLD:
                    return Resource.message(Resource.ASKIDA);

                default:
                    //return Resource.message(Resource.KONTROL_SONUCU);
                    return Resource.message(Resource.KONTROL_SONUCU);
            }
        }
    }
}
