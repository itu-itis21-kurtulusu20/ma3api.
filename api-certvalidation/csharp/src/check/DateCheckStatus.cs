using System;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check
{
    [Serializable]
    public class DateCheckStatus : CheckStatus
    {
        public static readonly DateCheckStatus VALID_DATE = new DateCheckStatus(_enum.VALID_DATE);
        public static readonly DateCheckStatus INVALID_DATE = new DateCheckStatus(_enum.INVALID_DATE);
        public static readonly DateCheckStatus CORRUPT_DATE_INFO = new DateCheckStatus(_enum.CORRUPT_DATE_INFO);

        enum _enum
        {
            VALID_DATE,
            INVALID_DATE,
            CORRUPT_DATE_INFO
        }

        readonly _enum mValue;
        
        DateCheckStatus(_enum aEnum)
        {
            mValue = aEnum;
        }

        public String getText()
        {
            switch (mValue)
            {
                case _enum.VALID_DATE:
                    return Resource.message(Resource.VALID_DATE_INFO);
                case _enum.INVALID_DATE:
                    return Resource.message(Resource.INVALID_DATE_INFO);
                case _enum.CORRUPT_DATE_INFO:
                    return Resource.message(Resource.CORRUPT_DATE_INFO);
                default:
                    return Resource.message(Resource.KONTROL_SONUCU);
            }
        }
    }
}
