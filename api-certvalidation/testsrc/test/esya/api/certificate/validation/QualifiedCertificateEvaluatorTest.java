package test.esya.api.certificate.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.parser.BooleanExpressionParser;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.parser.QualifiedCertificateEvaluator;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class QualifiedCertificateEvaluatorTest {

    BooleanExpressionParser booleanExpressionParser;
    QualifiedCertificateEvaluator qualifiedCertificateEvaluator;
    String expression;
    Object expectedResult;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {"(0.4.0.1862.1.1 AND (2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1))", true},
                {"(0.4.0.1862.1.1 AND (2.16.792.1.61.0.1.5070.1.1 AND 2.16.792.1.61.0.1.5070.2.1))", false},
                {"(0.4.0.1862.1.1 OR (2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1))", true},
                {"(0.4.0.1862.1.1 OR (2.16.792.1.61.0.1.5070.1.1 AND 2.16.792.1.61.0.1.5070.2.1))", true},
                {"(0.4.0.1862.1.1 AND 2.16.792.1.61.0.1.5070.2.1)", true},
                {"(0.4.0.1862.1.1 OR 2.16.792.1.61.0.1.5070.2.1)", true},
                {"(0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.1.1)", false},
                {"(0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1)", true},
                {"(0.4.0.1862.1.1 OR 2.16.792.1.61.0.1.5070.1.1 AND 2.16.792.1.61.0.1.5070.2.1)", true},
                {"(0.4.0.1862.1.1 AND 2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1)", true},
                {"(0.4.0.1862.1.1 AND 2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1 OR 2.2)", true},
                {"0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.1.1 OR 2.16.792.1.61.0.1.5070.2.1", true}
                /*{"(HANDAN AND OSMAN)", new ESYAException()}
                {"0.4.0.1862.1.1 OR 2.16.792.1.61.0.1.5070.2.1)", new ESYAException()},
                {"((0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.2.1)", new ESYAException()},
                {"((0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.2.1", new ESYAException()},
                {"0.4.0.1862.1.2 OR 2.16.792.1.61.0.1.5070.2.1)", new ESYAException()}*/
        });
    }

    public QualifiedCertificateEvaluatorTest(String expression, Object expectedResult) {
        this.expression = expression;
        this.expectedResult = expectedResult;
    }

    @Before
    public void before() throws IOException {
        ECertificate eCertificate = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\Elektronik_Mühre_İlişkin_Usul_ve_Esaslar_Hakkında_Yönetmelik_14.09.2022.cer");

        qualifiedCertificateEvaluator = new QualifiedCertificateEvaluator(eCertificate);
        booleanExpressionParser = new BooleanExpressionParser();
    }

    @Test
    public void testQualifiedCertificateEvaluator() throws ESYAException {
        Object result = booleanExpressionParser.parse(expression, qualifiedCertificateEvaluator);
        Assert.assertEquals(expectedResult, result);
    }
}
