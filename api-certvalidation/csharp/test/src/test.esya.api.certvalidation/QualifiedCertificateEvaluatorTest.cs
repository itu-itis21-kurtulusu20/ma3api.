using System;
using NUnit.Framework;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.parser;

namespace tr.gov.tubitak.uekae.esya.api.certificate.validation.src.test.esya.api.certvalidation
{
    public class QualifiedCertificateEvaluatorTest
    {

        BooleanExpressionParser booleanExpressionParser;
        QualifiedCertificateEvaluator qualifiedCertificateEvaluator;

        public static Object[] TestCases =
        {
            new object[] {"(0.4.0.1862.1.1 AND (2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1))", true},
            new object[] {"(0.4.0.1862.1.1 AND (2.16.792.1.61.0.1.5070.1.1 AND 2.16.792.1.61.0.1.5070.2.1))", false},
            new object[] {"(0.4.0.1862.1.1 OR (2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1))", true},
            new object[] {"(0.4.0.1862.1.1 OR (2.16.792.1.61.0.1.5070.1.1 AND 2.16.792.1.61.0.1.5070.2.1))", true},
            new object[] {"(0.4.0.1862.1.1 AND 2.16.792.1.61.0.1.5070.2.1)", true},
            new object[] {"(0.4.0.1862.1.1 OR 2.16.792.1.61.0.1.5070.2.1)", true},
            new object[] {"(0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.1.1)", false},
            new object[] {"(0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1)", true},
            new object[] {"(0.4.0.1862.1.1 OR 2.16.792.1.61.0.1.5070.1.1 AND 2.16.792.1.61.0.1.5070.2.1)", true},
            new object[] {"(0.4.0.1862.1.1 AND 2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1)", true},
            new object[] {"(0.4.0.1862.1.1 AND 2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1 OR 2.2)", true},
            new object[] {"0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1", true}
            /*new object[] {"(HANDAN AND OSMAN)", true}
            new object[] {"0.4.0.1862.1.1 OR 2.16.792.1.61.0.1.5070.2.1)", new ESYAException()},
            new object[] {"((0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.2.1)", new ESYAException()},
            new object[] {"((0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.2.1", new ESYAException()},
            new object[] {"0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.2.1)", new ESYAException()}*/
        };

        [SetUp]
        public void before()
        {
            ECertificate eCertificate = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\Elektronik_Mühre_İlişkin_Usul_ve_Esaslar_Hakkında_Yönetmelik_14.09.2022.cer");

            qualifiedCertificateEvaluator = new QualifiedCertificateEvaluator(eCertificate);
            booleanExpressionParser = new BooleanExpressionParser();
        }

        [Test, TestCaseSource("TestCases")]
        public void testQualifiedCertificateEvaluator(String expression, Object expectedResult)
        {
            Object result = booleanExpressionParser.parse(expression, qualifiedCertificateEvaluator);
            Assert.AreEqual(expectedResult, result);
        }

    }
}
