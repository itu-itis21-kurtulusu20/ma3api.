
namespace tr.gov.tubitak.uekae.esya.api.infra.certstore.db.core.helper
{
    public class OzneTipi
    {
        public static readonly OzneTipi KOK_SERTIFIKA = new OzneTipi(_enum.KOK_SERTIFIKA);
        public static readonly OzneTipi SERTIFIKA = new OzneTipi(_enum.SERTIFIKA);
        public static readonly OzneTipi SIL = new OzneTipi(_enum.SIL);
        public static readonly OzneTipi OCSP_BASIC_RESPONSE = new OzneTipi(_enum.OCSP_BASIC_RESPONSE);
        public static readonly OzneTipi OCSP_RESPONSE = new OzneTipi(_enum.OCSP_RESPONSE);
        enum _enum
        {
            KOK_SERTIFIKA = 1,
            SERTIFIKA = 2,
            SIL = 3,
            OCSP_BASIC_RESPONSE = 4,
            OCSP_RESPONSE = 5
        }
        private readonly _enum mValue;
        private OzneTipi(_enum aEnum)
        {
            mValue = aEnum;
        }

        public int getIntValue()
        {
            return (int)mValue;
        }

        //TODO default kismina bak
        public static OzneTipi getNesne(int aTip)
        {
            switch (aTip)
            {
                case 1: return OzneTipi.KOK_SERTIFIKA;
                case 2: return OzneTipi.SERTIFIKA;
                case 3: return OzneTipi.SIL;
                case 4: return OzneTipi.OCSP_BASIC_RESPONSE;
                case 5: return OzneTipi.OCSP_RESPONSE;
                default: return null;
            }
        }
    }
}
