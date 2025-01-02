using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.common.license
{
    public partial class LV
    {
        public class Products
        {
            public static List<Products> productList = new List<Products>();

            public static readonly Products SERTIFIKA_MAKAMI = new Products(ProductTypes.SERTIFIKA_MAKAMI);
            public static readonly Products KAYIT_MAKAMI = new Products(ProductTypes.KAYIT_MAKAMI);
            public static readonly Products ZAMAN_DAMGASI = new Products(ProductTypes.ZAMAN_DAMGASI);
            public static readonly Products OCSP_SERVISI = new Products(ProductTypes.OCSP_SERVISI);
            public static readonly Products ESYA_ISTEMCI = new Products(ProductTypes.ESYA_ISTEMCI);
            public static readonly Products KERMEN = new Products(ProductTypes.KERMEN);
            public static readonly Products KERMEN_LIGHT = new Products(ProductTypes.KERMEN_LIGHT);
            public static readonly Products ESYA_SUNUCU = new Products(ProductTypes.ESYA_SUNUCU);
            public static readonly Products TEST = new Products(ProductTypes.TEST);

            public static readonly Products ORTAK = new Products(ProductTypes.ORTAK);
            public static readonly Products AKILLIKART = new Products(ProductTypes.AKILLIKART);
            public static readonly Products SERTIFIKADOGRULAMA = new Products(ProductTypes.SERTIFIKADOGRULAMA);
            public static readonly Products CMSIMZA = new Products(ProductTypes.CMSIMZA);
            public static readonly Products CMSIMZAGELISMIS = new Products(ProductTypes.CMSIMZAGELISMIS);
            public static readonly Products XMLIMZA = new Products(ProductTypes.XMLIMZA);
            public static readonly Products XMLIMZAGELISMIS = new Products(ProductTypes.XMLIMZAGELISMIS);
            public static readonly Products CMSZARF = new Products(ProductTypes.CMSZARF);
            public static readonly Products MOBILIMZA = new Products(ProductTypes.MOBILIMZA);
            public static readonly Products ZD_ISTEMCI = new Products(ProductTypes.ZD_ISTEMCI);
            public static readonly Products GUVENLIPDF = new Products(ProductTypes.GUVENLIPDF);
            public static readonly Products ELIT = new Products(ProductTypes.ELIT);

            private enum ProductTypes
            {
                SERTIFIKA_MAKAMI = 10,
                KAYIT_MAKAMI = 11,
                ZAMAN_DAMGASI = 39,
                OCSP_SERVISI = 13,
                ESYA_ISTEMCI = 60,
                KERMEN = 50,
                KERMEN_LIGHT = 51,
                ESYA_SUNUCU = 80,
                TEST = 90,

                ORTAK = 40,
                AKILLIKART = 41,
                SERTIFIKADOGRULAMA = 42, 
                CMSIMZA = 43, 
                CMSIMZAGELISMIS = 44,
                XMLIMZA = 45, 
                XMLIMZAGELISMIS = 46, 
                CMSZARF = 47, 
                MOBILIMZA = 48,
                ZD_ISTEMCI = 52,
                GUVENLIPDF = 73,
                ELIT = 66
            }


            private readonly ProductTypes _mValue;

            private Products(ProductTypes aProductTypes)
            {
                _mValue = aProductTypes;
                productList.Add(this);
            }

            public int getID()
            {
                return (int)_mValue;
            }

            public static Products getProduct(int productID)
            {
                foreach (Products product in productList)
                {
                    if (product.getID() == productID)
                        return product;
                }
                throw  new LE("No product found with ID: " + productID);
            }

            public override string ToString()
            {
                return _mValue.ToString();
            }
        }
    }
}