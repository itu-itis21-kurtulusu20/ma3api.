using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.tools;
using tr.gov.tubitak.uekae.esya.api.tsl;
using tr.gov.tubitak.uekae.esya.api.xmlsignature;

namespace tr.gov.tubitak.uekae.esya.api.tsl
{
    [TestFixture]
    public class Test
    {
        [Test]
        public static void testTSL()
        {
            //XmlConfigurator.Configure(new FileInfo("C:\\ClearCase_Storage\\yavuz.kahveci_ESYA_MA3API_int\\MA3\\TrustServiceStatusList\\TrustServiceStatusList\\config\\log4net.xml"));
            try
            {
                Chronometer c = new Chronometer("Validate Certificate");
                c.start();
                TSL tsl = TSL.parse("C:\\Users\\beytullah.yigit\\Desktop\\tsl-de-signed.xml");
                List<ECertificate> validCertList = (List<ECertificate>)tsl.getValidCertificates();
                Console.WriteLine(validCertList.Count);
                bool validationRslt = tsl.validateTSL();
                Console.WriteLine(c.stopSingleRun()); 
                bool isSigned = tsl.isSigned();
                bool isUptoDate = tsl.isTSLUptoDate();
              // ValidationResult signatureValidation = tsl.validateSignature();
             //  if (signatureValidation.getResultType() == ValidationResultType.VALID)
              //  { Console.WriteLine("heyyo"); }
               // Console.WriteLine( signatureValidation.toXml());
               IList<ECertificate> certList = tsl.getAllCertificates();
               // IList<ECertificate> validCertList = tsl.getValidCertificates();

            }
            catch (TSLException ex)
            {
                Console.WriteLine(ex.Message);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
            }

        }
    }
}
