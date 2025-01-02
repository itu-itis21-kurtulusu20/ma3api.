

using System;
using System.Collections.Generic;
using NUnit.Framework;
using test.esya.api.cmssignature.validation;
using tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;
using tr.gov.tubitak.uekae.esya.api.cmssignature.signature;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace test.esya.api.cmssignature.conversion
{
    class T02_Conversion : CMSSignatureTest
    {
        private readonly String DIRECTORY = "T:\\api-cmssignature\\test-output\\csharp\\ma3\\conversion\\";

        private static object[] UpgradeCases =
        {
            new object [] { "BES.p7s", ESignatureType.TYPE_EST, "EST.p7s" },
            new object [] {"EPES.p7s",ESignatureType.TYPE_EST, "EST-2.p7s"},
            new object [] {"EST.p7s",ESignatureType.TYPE_EST, "EST-3.p7s"},

            new object [] {"BES.p7s",ESignatureType.TYPE_ESC, "ESC.p7s"},
            new object [] {"EPES.p7s",ESignatureType.TYPE_EST, "ESC-2.p7s"},
            new object [] {"EST.p7s",ESignatureType.TYPE_ESC, "ESC-3.p7s"},

            new object [] {"BES.p7s",ESignatureType.TYPE_ESX_Type1, "ESX1.p7s"},
            new object [] {"EPES.p7s",ESignatureType.TYPE_ESX_Type1, "ESX1-2.p7s"},
            new object [] {"EST.p7s",ESignatureType.TYPE_ESX_Type1, "ESX1-3.p7s"},
            new object [] {"ESC.p7s",ESignatureType.TYPE_ESX_Type1, "ESX1-4.p7s"},

            new object [] {"BES.p7s",ESignatureType.TYPE_ESX_Type2, "ESX2.p7s"},
            new object [] {"EPES.p7s",ESignatureType.TYPE_ESX_Type2, "ESX2-2.p7s"},
            new object [] {"EST.p7s",ESignatureType.TYPE_ESX_Type2, "ESX2-3.p7s"},
            new object [] {"ESC.p7s",ESignatureType.TYPE_ESX_Type2, "ESX2-4.p7s"},

            new object [] {"BES.p7s",ESignatureType.TYPE_ESXLong, "ESXLong.p7s"},
            new object [] {"EPES.p7s",ESignatureType.TYPE_ESXLong, "ESXLong-2.p7s"},
            new object [] {"EST.p7s",ESignatureType.TYPE_ESXLong, "ESXLong-3.p7s"},
            //{"ESC.p7s",ESignatureType.TYPE_ESXLong, "ESXLong-4.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.


            new object [] {"BES.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1.p7s"},
            new object [] {"EPES.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1-2.p7s"},
            new object [] {"EST.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1-3.p7s"},
            //{"ESC.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1-4.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.
            //{"ESX1.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType1-5.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.


            new object [] {"BES.p7s",ESignatureType.TYPE_ESXLong_Type2, "ESXLongType2.p7s"},
            new object [] {"EPES.p7s",ESignatureType.TYPE_ESXLong_Type2, "ESXLongType2-2.p7s"},
            new object [] {"EST.p7s",ESignatureType.TYPE_ESXLong_Type2, "ESXLongType2-3.p7s"},
            //{"ESC.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType2-4.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.
            //{"ESX2.p7s",ESignatureType.TYPE_ESXLong_Type1, "ESXLongType2-5.p7s"}, //ESC oluşturulurken kullanılan referanların kaydedilmesi ve conversion sırasında bulunması gerekiyor.


            new object [] {"BES.p7s",ESignatureType.TYPE_ESA, "ESA.p7s"},
            new object [] {"EPES.p7s",ESignatureType.TYPE_ESA, "ESA-2.p7s"},
            new object [] {"EST.p7s",ESignatureType.TYPE_ESA, "ESA-3.p7s"},
            //{"ESC.p7s",ESignatureType.TYPE_ESA, "ESA-4.p7s"}, // ↑ Referans Problemi
            //{"ESX1.p7s",ESignatureType.TYPE_ESA, "ESA-5.p7s"}, // ↑ Referans Problemi
            //{"ESX2.p7s",ESignatureType.TYPE_ESA, "ESA-6.p7s"}, // ↑ Referans Problemi
            new object [] {"ESXLong.p7s",ESignatureType.TYPE_ESA, "ESA-7.p7s"},
            new object [] {"ESA.p7s",ESignatureType.TYPE_ESA, "ESA-8.p7s"}
        };

        [Test, TestCaseSource("UpgradeCases")]
        public void TestUpgrade(string inputFile, ESignatureType outputType, string outputFile)
        {
            byte[] signatureFile = AsnIO.dosyadanOKU(DIRECTORY + inputFile);

            BaseSignedData bs = new BaseSignedData(signatureFile);

            Dictionary<String, Object> parameters = new Dictionary<String, Object>();

            //Time stamp will be needed for some conversion.
            parameters[EParameters.P_TSS_INFO] = getTSSettings();
            parameters[EParameters.P_CERT_VALIDATION_POLICY] = getPolicy();

            bs.getSignerList()[0].convert(outputType, parameters);

            AsnIO.dosyayaz(bs.getEncoded(), DIRECTORY + outputFile);

            ValidationUtil.checkSignatureIsValid(bs.getEncoded(), null);
        }



    }
}
