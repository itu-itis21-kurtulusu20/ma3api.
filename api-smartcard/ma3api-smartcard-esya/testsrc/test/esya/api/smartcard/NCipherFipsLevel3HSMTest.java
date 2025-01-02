/**
 * Created by orcun.ertugrul on 28-Nov-17.
 */

package test.esya.api.smartcard;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.MAC;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ESYASmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate;
import util.CardTestUtil;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NCipherFipsLevel3HSMTest
{
    static final String PASSWORD = "123456";

    static boolean fipsMode = true;
    static SmartCard sc = null;
    static long sid = 0;
    static long slotNo = 0;


    @Before
    public void setUpClass() throws Exception
    {
        sc = new ESYASmartCard(CardType.NCIPHER);
        slotNo = getSlot();
        sid = sc.openSession(slotNo);
        sc.setFipsMode(fipsMode);
        sc.login(sid, PASSWORD);
    }

    @After
    public void cleanUp()  throws Exception
    {
        sc.logout(sid);
        sc.closeSession(sid);
    }

    private static long getSlot()
            throws PKCS11Exception
    {
        long[] slots = sc.getTokenPresentSlotList();
        return slots[1];
    }


    public void testImportHMAC(DigestAlg digestAlg) throws Exception
    {
        String hmacLabel = "HMAC_Import_" + System.currentTimeMillis();
        try
        {
            int hMACSize = 128;
            int resultLen;
            MACAlg macAlg;
            long pkcs11_mech = 0;
            byte[] hMACKey = RandomUtil.generateRandom(hMACSize);

            if (digestAlg == DigestAlg.SHA1) {
                macAlg = MACAlg.HMAC_SHA1;
                resultLen = 20;
                pkcs11_mech = PKCS11Constants.CKM_SHA_1_HMAC;
            } else if (digestAlg == DigestAlg.SHA256) {
                macAlg = MACAlg.HMAC_SHA256;
                resultLen = 32;
                pkcs11_mech = PKCS11Constants.CKM_SHA256_HMAC;
            } else
                throw new Exception("UnSupported Digest Alg for HMAC: " + digestAlg.getName());

            HMACKeyTemplate secretKeyTemplate = new HMACKeyTemplate(hmacLabel, hMACKey, digestAlg.getName());
            secretKeyTemplate.getAsSignerTemplate();
            secretKeyTemplate.getAsCreationTemplate();

            sc.importSecretKey(sid, secretKeyTemplate);

            //Sign And Verify Block START
            int toBeHMACDataLen = 10 + new Random().nextInt(512);
            byte[] toBeHMACData = RandomUtil.generateRandom(toBeHMACDataLen);

            MAC mac1 = Crypto.getMAC(macAlg);
            mac1.init(hMACKey, new ParamsWithLength(resultLen));
            byte[] hMACResult1 = mac1.doFinal(toBeHMACData);

            byte[] hMACResult2 = sc.signData(sid, hmacLabel, toBeHMACData, pkcs11_mech);

            assertEquals(true, Arrays.equals(hMACResult1, hMACResult2));
            //Sign And Verify Block END
        }
        finally
        {
            try
            {
                sc.deletePrivateObject(sid, hmacLabel);
            }
            finally {}
        }
    }

    public void testImportRSA(int keySize) throws Exception
    {
        String privKeyLabel = "RSA_Private_Import_" + System.currentTimeMillis();
        String pubKeyLabel  = "RSA_Public_Import_" + System.currentTimeMillis();

        try
        {
            KeyPair kp = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, keySize);

            RSAPrivateKeyTemplate privateKeyTemplate = new RSAPrivateKeyTemplate(privKeyLabel, (RSAPrivateCrtKey) kp.getPrivate(), null);
            privateKeyTemplate.getAsTokenTemplate(true, false, true);


            RSAPublicKeyTemplate publicKeyTemplate = new RSAPublicKeyTemplate(pubKeyLabel, (java.security.interfaces.RSAPublicKey) kp.getPublic());
            publicKeyTemplate.getAsTokenTemplate(true, false, false);

            RSAKeyPairTemplate keyPairTemplate = new RSAKeyPairTemplate(publicKeyTemplate, privateKeyTemplate);

            sc.importKeyPair(sid, keyPairTemplate);


            int randomLen = new Random().nextInt(100);
            byte[] dataToBeSigned = RandomUtil.generateRandom(randomLen);

            CK_MECHANISM mechanism = new CK_MECHANISM(0L);
            mechanism.mechanism = PKCS11Constants.CKM_SHA1_RSA_PKCS;

            byte[] signature = sc.signData(sid, privKeyLabel, dataToBeSigned, mechanism);

            sc.verifyData(sid, pubKeyLabel, dataToBeSigned, signature, mechanism.mechanism);

            boolean verified = SignUtil.verify(SignatureAlg.RSA_SHA1, dataToBeSigned, signature, kp.getPublic());

            assertEquals(true, verified);
        }
        finally
        {
            try {sc.deletePrivateObject(sid, privKeyLabel);} catch (Exception e) {}
            try {sc.deletePublicObject(sid, pubKeyLabel);} catch (Exception e) {}
        }
    }


    @Test
    public void testHMAC_SHA1() throws Exception
    {
        testImportHMAC(DigestAlg.SHA1);
    }

    @Test
    public void testImportRSA_1024() throws Exception
    {
        testImportRSA(1024);
    }

    @Test
    public void testImportRSA_2048() throws Exception
    {
        testImportRSA(2048);
    }

    @Test
    public void test_CreateECKey_secp192() throws  Exception
    {
        testCreateECKeys("secp192r1");
    }


    public void testCreateECKeys(String curveName)  throws  Exception
    {
       CardTestUtil.testCreateECKeys(sc, "ecCreateKey_" + curveName, curveName);
    }

}
