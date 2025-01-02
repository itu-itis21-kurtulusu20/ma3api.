package dev.esya.api.cmssignature.pss;

import org.junit.Assert;
import org.junit.Test;
import test.esya.api.cmssignature.testconstants.TestConstants;
import test.esya.api.cmssignature.testconstants.UGTestConstants;
import test.esya.api.cmssignature.validation.ValidationUtil;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmssignature.SignableByteArray;
import tr.gov.tubitak.uekae.esya.api.cmssignature.attribute.EParameters;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.BaseSignedData;
import tr.gov.tubitak.uekae.esya.api.cmssignature.signature.ESignatureType;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedDataValidationResult;
import tr.gov.tubitak.uekae.esya.api.cmssignature.validation.SignedData_Status;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.common.crypto.BaseSigner;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.P11SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;

import java.io.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by sura.emanet on 26.09.2018.
 */
public class RSAPSSTest{

    public static int TEST_COUNT = 100;

    @Test
    public void test_Crypto_WithPSS_WithPfx() throws Exception  {

        TestConstants constants = new UGTestConstants();
        PrivateKey privKey = constants.getSignerPrivateKey();
        PublicKey pubKey = constants.getSignerCertificate().asX509Certificate().getPublicKey();

        RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256);

        byte[] data ;
        byte[] signature;
        boolean result;

        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("T:\\Temp\\Pss\\SignatureJava1.txt"),false));

        for(int i=0; i<TEST_COUNT; i++ ) {

            int randomDataLen = new Random().nextInt(200) + 1;
            data = RandomUtil.generateRandom(randomDataLen);

            bf.write("D:" + StringUtil.toHexString(data));
            bf.newLine();

            signature =SignUtil.sign(SignatureAlg.RSA_PSS, rsapssParams, data, privKey);

            bf.write("S:" + StringUtil.toHexString(signature));
            bf.newLine();

            result = SignUtil.verify(SignatureAlg.RSA_PSS, rsapssParams, data, signature, pubKey);
            Assert.assertTrue(result);
        }
        bf.close();
    }

    @Test
    public void test_Crypto_WithPSS_WithSmartCard() throws Exception
    {
        SmartCard sc = new SmartCard(CardType.AKIS);
        long slotNo = sc.getSlotList()[0];
        long sid = sc.openSession(slotNo);
        sc.login(sid, "12345");

        byte[] signature;
        boolean result;
        byte[] data ;

        RSAPSSParams pssParams = new RSAPSSParams(DigestAlg.SHA256);

        byte[] certBytes = sc.getSignatureCertificates(sid).get(0);
        ECertificate cert = new ECertificate(certBytes);
        PublicKey pubKey = cert.asX509Certificate().getPublicKey();

        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("T:\\Temp\\Pss\\SignatureJava2.txt"),false));

        for(int i=0;i<TEST_COUNT;i++){

            int randomDataLen = new Random().nextInt(200) + 1;
            data = RandomUtil.generateRandom(randomDataLen);

            bf.write("D:" + StringUtil.toHexString(data));
            bf.newLine();

            signature = SmartOp.sign(sc,sid,slotNo,cert.getSerialNumber().toByteArray(),data,SignatureAlg.RSA_PSS.getName(),pssParams);

            result = SignUtil.verify(SignatureAlg.RSA_PSS, pssParams, data, signature, pubKey);
            Assert.assertTrue(result);

            bf.write("S:" + StringUtil.toHexString(signature));
            bf.newLine();
        }

        bf.close();
        sc.logout(sid);
        sc.closeSession(sid);
    }



    @Test
    public void test_CMS_WithPSS_WithPFX() throws Exception
    {

        TestConstants constants = new UGTestConstants();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, constants.getPolicy());
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        RSAPSSParams pssParams = new RSAPSSParams(DigestAlg.SHA256);

        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("T:\\Temp\\Pss\\SignatureJava3.txt"),false));

        for (int i = 0; i < TEST_COUNT; i++)
        {
            int randomDataLen = new Random().nextInt(200) + 1;
            byte []data = RandomUtil.generateRandom(randomDataLen);

            bf.write("D:" + StringUtil.toHexString(data));
            bf.newLine();

            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableByteArray(data));
            bs.addSigner(ESignatureType.TYPE_BES, constants.getSignerCertificate(), constants.getSignerInterface(SignatureAlg.RSA_PSS, pssParams), null, params);

            SignedDataValidationResult sdvr = ValidationUtil.validate(bs.getEncoded(), null);
            Assert.assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

            bf.write("S:" + StringUtil.toHexString(bs.getEncoded()));
            bf.newLine();
        }
        bf.close();

    }



    @Test
    public void test_CMS_WithPSS_WithSmartCard() throws Exception
    {

        P11SmartCard p11SmartCard = new P11SmartCard(CardType.AKIS);
        long slotNo = 1;
        p11SmartCard.openSession(slotNo);
        p11SmartCard.login("12345");

        TestConstants constants = new UGTestConstants();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put(EParameters.P_CERT_VALIDATION_POLICY, constants.getPolicy());
        params.put(EParameters.P_VALIDATE_CERTIFICATE_BEFORE_SIGNING, false);

        RSAPSSParams pssParams = new RSAPSSParams(DigestAlg.SHA256);

        byte[] certBytes = p11SmartCard.getSignatureCertificates().get(0);
        ECertificate cert = new ECertificate(certBytes);
        BaseSigner signer = p11SmartCard.getSigner(cert.asX509Certificate(), Algorithms.SIGNATURE_RSA_PSS,pssParams);

        BufferedWriter bf = new BufferedWriter(new FileWriter(new File("T:\\Temp\\Pss\\SignatureJava4.txt"),false));

        for(int i=0; i<TEST_COUNT; i++){

            int randomDataLen = new Random().nextInt(200) + 1;
            byte []data = RandomUtil.generateRandom(randomDataLen);

            bf.write("D:" + StringUtil.toHexString(data));
            bf.newLine();

            BaseSignedData bs = new BaseSignedData();
            bs.addContent(new SignableByteArray(data));

            bs.addSigner(ESignatureType.TYPE_BES, cert, signer, null, params);

            SignedDataValidationResult sdvr = ValidationUtil.validate(bs.getEncoded(), null);
            Assert.assertEquals(SignedData_Status.ALL_VALID, sdvr.getSDStatus());

            bf.write("S:" + StringUtil.toHexString(bs.getEncoded()));
            bf.newLine();
        }

        bf.close();
        p11SmartCard.logout();
        p11SmartCard.closeSession();
    }



}






