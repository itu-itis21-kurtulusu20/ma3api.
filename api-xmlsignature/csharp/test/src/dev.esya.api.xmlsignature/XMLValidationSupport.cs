using System;
using System.Globalization;
using System.Threading;
using log4net.Config;
using NUnit.Framework;
using test.esya.api.xmlsignature.validation;

namespace dev.esya.api.xmlsignature
{
    class XMLValidationSupport
    {
        [Test]
        public void validateYTE()
        {
            CultureInfo trCultureInfo = new CultureInfo("tr-TR");
            Thread.CurrentThread.CurrentCulture = trCultureInfo;
            Thread.CurrentThread.CurrentUICulture = trCultureInfo;
            BasicConfigurator.Configure();
            String signatureFileName = "C:\\Users\\orcun.ertugrul\\Desktop\\YTE\\counter_signature_arasi_10_dkdan_az(force_true_iken_dogrulaniyor).xml";
            XMLValidationUtil.checkSignatureIsValid("C:\\Users\\orcun.ertugrul\\Desktop\\YTE", signatureFileName);
        }

        [Test]
        public void ValidateAPIBES()
        {
            String signatureFileName = "C:\\a\\XAdES\\bes.xml";
            XMLValidationUtil.checkSignatureIsValid("C:\\Users\\orcun.ertugrul\\Desktop\\YTE", signatureFileName);
        }
    }
}
