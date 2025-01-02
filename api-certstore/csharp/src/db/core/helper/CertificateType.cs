
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class CertificateType
    {
        public static readonly CertificateType ROOTCERT = new CertificateType(CertificateTypes.ROOTCERT);
        public static readonly CertificateType CROSSCERT = new CertificateType(CertificateTypes.CROSSCERT);
        public static readonly CertificateType CACERT = new CertificateType(CertificateTypes.CACERT);
        public static readonly CertificateType ROLECERT = new CertificateType(CertificateTypes.ROLECERT);   
        enum CertificateTypes
        {
            ROOTCERT = 1,
            CROSSCERT = 2,
            CACERT = 3,
            ROLECERT = 4
        }
        private readonly CertificateTypes mValue;
        private CertificateType(CertificateTypes aEnum)
        {
            mValue = aEnum;
        }
        public int getIntValue()
        {            
            return (int)mValue;
        }

        //TODO default kismina bak
        public static CertificateType getNesne(int aTip)
        {
            switch (aTip)
            {
                case 1: return ROOTCERT;
                case 2: return CROSSCERT;
                case 3: return CACERT;
                case 4: return ROLECERT;
                default: return null;
            }
        }

        public static CertificateType getNesne(long aTip)
        {
            return getNesne((int)aTip);
        }
    }
}
