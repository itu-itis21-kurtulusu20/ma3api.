using System;
using log4net;
using tr.gov.tubitak.uekae.esya.api.common.license;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
    static class EXMLLisansManager
    {
        private static ILog logger = log4net.LogManager.GetLogger(typeof(XMLSignature));
        public static void checkHasBaseXmlSignatureLicense()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.XMLIMZA);
            }
            catch (LE exc)
            {
                logger.Fatal("Lisans kontrolu basarisiz.");
                throw new Exception("Lisans kontrolu basarisiz.", exc);
            }
        }

    }
}
