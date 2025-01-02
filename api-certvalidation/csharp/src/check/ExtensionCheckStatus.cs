using System;
//using tr.gov.tubitak.uekae.esya.api.certificate.i18n;
using tr.gov.tubitak.uekae.esya.api.common.bundle;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.check
{
    /**
    * @author ahmety
    * date: Feb 18, 2010
    */
    [Serializable]
    public class ExtensionCheckStatus : CheckStatus
    {
        public static readonly ExtensionCheckStatus NO_EXTENSION = new ExtensionCheckStatus(_enum.NO_EXTENSION);
        public static readonly ExtensionCheckStatus INVALID_EXTENSION = new ExtensionCheckStatus(_enum.INVALID_EXTENSION);
        public static readonly ExtensionCheckStatus DUPLICATE_EXTENSION = new ExtensionCheckStatus(_enum.DUPLICATE_EXTENSION);
        public static readonly ExtensionCheckStatus VALID_EXTENSIONS = new ExtensionCheckStatus(_enum.VALID_EXTENSIONS);

        enum _enum
        {
            NO_EXTENSION,
            INVALID_EXTENSION,
            DUPLICATE_EXTENSION,
            VALID_EXTENSIONS
        }

        readonly _enum mValue;
        
        ExtensionCheckStatus(_enum aEnum)
        {
            mValue = aEnum;
        }
        
        public String getText()
        {
            switch (mValue)
            {
                case _enum.NO_EXTENSION:
                    return Resource.message(Resource.EKLENTI_YOK);
                case _enum.INVALID_EXTENSION:
                    return Resource.message(Resource.GECERSIZ_EKLENTI);
                case _enum.DUPLICATE_EXTENSION:
                    return Resource.message(Resource.AYNI_EXTENSION_BIRDEN_FAZLA);
                case _enum.VALID_EXTENSIONS:
                    return Resource.message(Resource.EKLENTILER_GECERLI);

                default:
                    return Resource.message(Resource.KONTROL_SONUCU);
            }
        }
    }
}
