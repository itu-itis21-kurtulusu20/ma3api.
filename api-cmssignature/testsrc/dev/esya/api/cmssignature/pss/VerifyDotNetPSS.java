package dev.esya.api.cmssignature.pss;


import org.junit.Assert;
import org.junit.Test;
import test.esya.api.cmssignature.testconstants.TestConstants;
import test.esya.api.cmssignature.testconstants.UGTestConstants;
import test.esya.api.cmssignature.validation.ValidationUtil;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.PublicKey;

public class VerifyDotNetPSS {

    @Test
    public void verifyDotNet_Cryto_WithPSS_WithPfx() throws Exception {
        TestConstants constants = new UGTestConstants();
        PublicKey pubKey = constants.getSignerCertificate().asX509Certificate().getPublicKey();

        testVerifyDotNetPSSAlgorithm(pubKey, "SignatureDotNet1.txt");
    }

    @Test
    public void verifyDotNet_Cryto_WithPSS_WithSmartCard() throws Exception {
        SmartCard sc = new SmartCard(CardType.AKIS);
        long slotNo = sc.getSlotList()[0];
        long sid = sc.openSession(slotNo);
        sc.login(sid, "12345");

        byte[] certBytes = sc.getSignatureCertificates(sid).get(0);
        ECertificate cert = new ECertificate(certBytes);
        PublicKey pubKey = cert.asX509Certificate().getPublicKey();

        testVerifyDotNetPSSAlgorithm(pubKey, "SignatureDotNet2.txt");
    }

    @Test
    public void verifyDotNet_CMS_WithPSS_WithPfx() throws Exception {
        verifyCMSSignature("SignatureDotNet3.txt");
    }

    @Test
    public void verifyDotNet_CMS_WithPSS_WithSmartCard() throws Exception {
        verifyCMSSignature("SignatureDotNet4.txt");
    }

    private void testVerifyDotNetPSSAlgorithm(PublicKey pubKey, String fileName) throws Exception {
        String signature;
        byte[] signatureBytes;

        String data;
        byte[] dataBytes;

        RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);
        BufferedReader reader = new BufferedReader(new FileReader(new File("T:\\Temp\\Pss\\" + fileName)));

        for (int i = 0; i < RSAPSSTest.TEST_COUNT; i++) {
            data = reader.readLine();

            if (data != null && data.startsWith("D:")) {
                dataBytes = StringUtil.hexToByte(data.substring(2));
                signature = reader.readLine();

                if (signature != null && signature.startsWith("S:")) {

                    signatureBytes = StringUtil.hexToByte(signature.substring(2));

                    Boolean result = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, dataBytes, signatureBytes, pubKey);
                    Assert.assertTrue(result);

                } else
                    throw new NullPointerException("Error in signature file");
            } else
                throw new NullPointerException("Error in data file");
        }
    }

    public void verifyCMSSignature(String fileName) throws Exception
    {
        String signature;
        byte[] signatureBytes;

        String data;
        byte[] dataBytes;

        SignedDataValidationResult sdvr;

        BufferedReader reader = new BufferedReader(new FileReader(new File("T:\\Temp\\Pss\\" + fileName)));

        for (int i = 0; i < RSAPSSTest.TEST_COUNT; i++)
        {
            data = reader.readLine();
            if (data != null && data.startsWith("D:"))
            {
                dataBytes = StringUtil.hexToByte(data.substring(2));
                signature = reader.readLine();

                if (signature != null && signature.startsWith("S:"))
                {
                    signatureBytes = StringUtil.hexToByte(signature.substring(2));

                    sdvr = ValidationUtil.validate(signatureBytes, null);
                    Assert.assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());
                }

                else
                    throw new NullPointerException("Error in signature file");
            }
            else
                throw new NullPointerException("Error in data file");
        }
    }
}
