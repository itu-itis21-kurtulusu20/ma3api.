using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.common.util;

namespace api_cmssignature_test.src.dev.esya.api.cmssignature
{
    class LicenseUtilTest
    {
        [Test]
        public void testLicenseUtil()
        {
            LicenseUtil.setLicenseXml(new FileStream("T:\\api-parent\\lisans\\lisans.xml", FileMode.Open, FileAccess.Read));

            DateTime expireDate = LicenseUtil.getExpirationDate();

            Assert.NotNull(expireDate);
            Assert.NotNull(expireDate.Day);
            Assert.True(expireDate.Day > 0);

            Console.WriteLine("License expiration date :" + expireDate.ToString("dd/MM/yyyy"));
        }
    }
}
