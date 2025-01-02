package test.esya.api.cmsenvelope;

import gnu.crypto.agreement.BaseAgreement;
import gnu.crypto.agreement.ECCofactorDHAgreement;
import org.junit.Assert;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.gnu.GNUKeyFactory;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

class TestCase {
    String curveType; // Curve type (e.g., P-192, P-224)
    int count;
    String qcavsX;
    String qcavsY;
    String diut;
    String qiutX;
    String qiutY;
    String ziut;

    @Override
    public String toString() {
        return "TestCase{" +
                "curveType='" + curveType + '\'' +
                ", count=" + count +
                ", qcavsX='" + qcavsX + '\'' +
                ", qcavsY='" + qcavsY + '\'' +
                ", diut='" + diut + '\'' +
                ", qiutX='" + qiutX + '\'' +
                ", qiutY='" + qiutY + '\'' +
                ", ziut='" + ziut + '\'' +
                '}';
    }
}

public class NISTECKeyAgreementTest {

    public static boolean performECDHTest(String curveType, String qcavsX, String qcavsY, String diut, String expectedZiut) throws Exception {
        curveType = "NIST " + curveType;
        System.out.println(curveType);

        // Convert inputs to BigInteger
        BigInteger xCoord = new BigInteger(qcavsX, 16);
        BigInteger yCoord = new BigInteger(qcavsY, 16);
        BigInteger privateKey = new BigInteger(diut, 16);

        // Get KeyFactory for ECDH
        //KeyFactory keyFactory = KeyFactory.getInstance("EC");
        GNUKeyFactory gnuKeyFactory = new GNUKeyFactory();


        // Create Public Key (QCAVS)
        ECParameterSpec ecSpec = NamedCurve.getECParameterSpec(curveType);
        ECPoint point = new ECPoint(xCoord, yCoord);
        ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, ecSpec);
        //PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
        PublicKey pubKey = gnuKeyFactory.generatePublicKey(pubKeySpec);

        // Create Private Key (dIUT)
        ECPrivateKeySpec privKeySpec = new ECPrivateKeySpec(privateKey, ecSpec);
        //PrivateKey privKey = keyFactory.generatePrivate(privKeySpec);
        PrivateKey privKey = gnuKeyFactory.generatePrivateKey(privKeySpec);

        // Perform Key Agreement
        BaseAgreement keyAgreement = new ECCofactorDHAgreement();
        keyAgreement.init(privKey);
        byte[] agreementValue = keyAgreement.calculateAgreement(pubKey);

        // Convert shared secret to hex and compare with expectedZIUT
        String actualZIUT = StringUtil.toHexString(agreementValue);
        return actualZIUT.equalsIgnoreCase(expectedZiut);
    }

    @Test
    public void ECKeyAgreementTest() throws Exception {
        String fileName = "testdata/cmsenvelope/KAS_ECC_CDH_PrimitiveTest.txt";
        List<TestCase> testCases = new ArrayList<>();
        String currentCurveType = null;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            TestCase currentTestCase = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Check for curve type (e.g., [P-192])
                if (line.startsWith("[") && line.endsWith("]")) {
                    currentCurveType = line.substring(1, line.length() - 1);
                } else if (line.startsWith("COUNT")) {
                    if (currentTestCase != null) {
                        testCases.add(currentTestCase);
                    }
                    currentTestCase = new TestCase();
                    currentTestCase.curveType = currentCurveType;
                    currentTestCase.count = Integer.parseInt(line.split("=")[1].trim());
                } else if (line.startsWith("QCAVSx")) {
                    currentTestCase.qcavsX = line.split("=")[1].trim();
                } else if (line.startsWith("QCAVSy")) {
                    currentTestCase.qcavsY = line.split("=")[1].trim();
                } else if (line.startsWith("dIUT")) {
                    currentTestCase.diut = line.split("=")[1].trim();
                } else if (line.startsWith("QIUTx")) {
                    currentTestCase.qiutX = line.split("=")[1].trim();
                } else if (line.startsWith("QIUTy")) {
                    currentTestCase.qiutY = line.split("=")[1].trim();
                } else if (line.startsWith("ZIUT")) {
                    currentTestCase.ziut = line.split("=")[1].trim();
                }
            }

            if (currentTestCase != null) {
                testCases.add(currentTestCase);
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        for (TestCase testCase : testCases) {
            // Print parsed test cases
            //System.out.println(testCase);

            // Perform ECDH computation and validate against expectedZIUT
            boolean result = performECDHTest(testCase.curveType, testCase.qcavsX, testCase.qcavsY, testCase.diut, testCase.ziut);
            System.out.println("ECDH Test Passed: " + result);
            Assert.assertTrue(result);
        }
    }
}