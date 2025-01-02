using System;
using NUnit.Framework;

namespace tr.gov.tubitak.uekae.esya.api.common.license
{
    [TestFixture]
    public class LicenseValidatorTest
    {
        [Test]
        [SetCulture("tr-TR")]
        public void testLicenseValidator_tr()
        {
            licenseValidator();
        }
        [Test]
        [SetCulture("en-US")]
        public void testLicenseValidator_en()
        {
            licenseValidator();
        }
        private static void licenseValidator()
        {
            String attr = null;
            //using (FileStream fs = new FileStream(@"E:\KODLAR\bilen.ogretmen_ESYA_MA3API_int\MA3\api-common\lisans\lisans.xml", FileMode.Open))
            //{
            //    LicenseUtil.setLicenseXml(fs);
            //}

            LV licenseValidator = LV.getInstance();

            licenseValidator.checkLicenseDates(LV.Products.SERTIFIKA_MAKAMI.getID());
            attr = licenseValidator.getUrunAtt(LV.Products.SERTIFIKA_MAKAMI.getID(), "IP");
            Console.WriteLine(attr);
        }
        [Test]        
        [SetCulture("tr-TR")]
        public void testNotExistUrun_tr()
        {
            notExistUrun();
        }

        [Test]
        [SetCulture("en-US")]
        public void testNotExistUrun_en()
        {
            notExistUrun();
        }
        private static void notExistUrun()
        {
            try
            {
                String attr = null;
                LV licenseValidator = LV.getInstance();

                licenseValidator.checkLicenseDates(LV.Products.ESYA_ISTEMCI.getID());
                attr = licenseValidator.getUrunAtt(LV.Products.ESYA_ISTEMCI.getID(), "IP");
                Console.WriteLine(attr);
            }
            catch (Exception e)
            {
                return;
            }

            throw new Exception("Function must be fail");
        }
    }
}
