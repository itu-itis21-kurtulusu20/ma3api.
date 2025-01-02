using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common.license;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper
{
    public static class EMSSPClientUtil
    {
        public static void CheckLicense()
        {
            try
            {
                LV.getInstance().checkLicenceDates(LV.Products.MOBILIMZA);
            }
            catch (LE ex)
            {
                throw new SystemException("Lisans kontrolu basarisiz. " + ex.Message);
            }
        }
    }
}
