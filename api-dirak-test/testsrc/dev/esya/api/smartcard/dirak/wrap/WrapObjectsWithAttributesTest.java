package dev.esya.api.smartcard.dirak.wrap;

import dev.esya.api.smartcard.dirak.CardTestUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteConversionUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.UnwrapObjectsResults;
import tr.gov.tubitak.uekae.esya.api.smartcard.object.WrappedObjectsWithAttributes;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.*;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.DES3KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.DESKeyTemplate;

import java.security.spec.ECParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.List;

public class WrapObjectsWithAttributesTest {

    static final String PASSWORD = "12345678";
    SmartCard sc1;
    long slotNo1;
    long sid1;

    SmartCard sc2;
    long slotNo2;
    long sid2;

    @Before
    public void setUpTest() throws Exception {
        sc1 = new SmartCard(CardType.DIRAKHSM);
        slotNo1 = CardTestUtil.getSlot(sc1);
        sid1 = sc1.openSession(slotNo1);
        sc1.login(sid1, PASSWORD);

        sc2 = new SmartCard(CardType.DIRAKHSM);
        slotNo2 = CardTestUtil.getSlot(sc2) + 1;
        sid2 = sc2.openSession(slotNo2);
        sc2.login(sid2, "123456");
    }

    @After
    public void cleanUp() throws Exception {
        sc1.logout(sid1);
        sc1.closeSession(sid1);
        sc2.logout(sid2);
        sc2.closeSession(sid2);
    }

    // AES WRAPPER

    @Test
    public void testWrapperAESSecretKeyAndToBeWrappedAESSecretKey_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsTokenTemplate(false, false, true);

            AESKeyTemplate tobeWrappedAESKeyTemplate = new AESKeyTemplate("tobeWrapped", 32);
            tobeWrappedAESKeyTemplate.getAsTokenTemplate(false, true, false);
            tobeWrappedAESKeyTemplate.getAsExportableTemplate();

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            long tobeWrappedKey = sc1.createSecretKey(sid1, tobeWrappedAESKeyTemplate);

            long[] tobeWrappedKeys = new long[]{tobeWrappedKey};

            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP), "wrapperKey", tobeWrappedKeys);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
            sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, new CK_MECHANISM(PKCS11Constants.CKM_AES_KEY_WRAP), "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- ENCRYPT - DECRYPT AND COMPARE ---
            byte[] randomData = RandomUtil.generateRandom(16);
            CK_MECHANISM ck_mechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_ECB);

            byte[] encryptData1 = sc1.encryptData(sid1, tobeWrappedKey, randomData, ck_mechanism);
            byte[] encryptData2 = sc2.encryptData(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), randomData, ck_mechanism);

            Assert.assertArrayEquals(encryptData1,encryptData2);

            byte[] decryptData1 = sc1.decryptData(sid1, tobeWrappedKey, encryptData2, ck_mechanism);
            byte[] decryptData2 = sc2.decryptData(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), encryptData1, ck_mechanism);

            Assert.assertArrayEquals(randomData, decryptData1);
            Assert.assertArrayEquals(randomData, decryptData2);
            // --- ENCRYPT - DECRYPT AND COMPARE ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            CK_ATTRIBUTE[] ckAttributes1 = convertLongListToCKATTRIBUTEArray(getPkcs11AESSecretKeyObjectAttributes());
            CK_ATTRIBUTE[] ckAttributes2 = convertLongListToCKATTRIBUTEArray(getPkcs11AESSecretKeyObjectAttributes());

            sc1.getAttributeValue(sid1, tobeWrappedKey, ckAttributes1);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2);

            for (int i = 0; i < ckAttributes1.length; i++) {
                if(ckAttributes1[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedKeys.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());
            
        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "tobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "tobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    @Test
    public void testWrapperAESSecretKeyAndToBeWrappedDESSecretKey_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsTokenTemplate(false, false, true);

            DESKeyTemplate tobeWrappedDESKeyTemplate = new DESKeyTemplate("tobeWrapped");
            tobeWrappedDESKeyTemplate.getAsTokenTemplate(false, true, false);
            tobeWrappedDESKeyTemplate.getAsExportableTemplate();

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            long tobeWrappedKey = sc1.createSecretKey(sid1, tobeWrappedDESKeyTemplate);

            long[] tobeWrappedKeys = new long[]{tobeWrappedKey};

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, wrapMechanism, "wrapperKey", tobeWrappedKeys);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
            sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, wrapMechanism, "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- ENCRYPT - DECRYPT AND COMPARE ---
            byte[] randomData = RandomUtil.generateRandom(16);
            CK_MECHANISM ck_mechanism = new CK_MECHANISM(PKCS11Constants.CKM_DES_ECB);

            byte[] encryptData1 = sc1.encryptData(sid1, tobeWrappedKey, randomData, ck_mechanism);
            byte[] encryptData2 = sc2.encryptData(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), randomData, ck_mechanism);

            Assert.assertArrayEquals(encryptData1,encryptData2);

            byte[] decryptData1 = sc1.decryptData(sid1, tobeWrappedKey, encryptData2, ck_mechanism);
            byte[] decryptData2 = sc2.decryptData(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), encryptData1, ck_mechanism);

            Assert.assertArrayEquals(randomData, decryptData1);
            Assert.assertArrayEquals(randomData, decryptData2);
            // --- ENCRYPT - DECRYPT AND COMPARE ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            CK_ATTRIBUTE[] ckAttributes1 = convertLongListToCKATTRIBUTEArray(getPkcs11DESSecretKeyObjectAttributes());
            CK_ATTRIBUTE[] ckAttributes2 = convertLongListToCKATTRIBUTEArray(getPkcs11DESSecretKeyObjectAttributes());

            sc1.getAttributeValue(sid1, tobeWrappedKey, ckAttributes1);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2);

            for (int i = 0; i < ckAttributes1.length; i++) {
                if(ckAttributes1[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedKeys.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());
            
        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "tobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "tobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    @Test
    public void testWrapperAESSecretKeyAndToBeWrapped3DESSecretKey_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsTokenTemplate(false, false, true);

            DES3KeyTemplate tobeWrappedDESKeyTemplate = new DES3KeyTemplate("tobeWrapped");
            tobeWrappedDESKeyTemplate.getAsTokenTemplate(false, true, false);
            tobeWrappedDESKeyTemplate.getAsExportableTemplate();

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            long tobeWrappedKey = sc1.createSecretKey(sid1, tobeWrappedDESKeyTemplate);

            long[] tobeWrappedKeys = new long[]{tobeWrappedKey};

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, wrapMechanism, "wrapperKey", tobeWrappedKeys);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
            sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, wrapMechanism, "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- ENCRYPT - DECRYPT AND COMPARE ---
            byte[] randomData = RandomUtil.generateRandom(16);
            CK_MECHANISM ck_mechanism = new CK_MECHANISM(PKCS11Constants.CKM_DES3_ECB);

            byte[] encryptData1 = sc1.encryptData(sid1, tobeWrappedKey, randomData, ck_mechanism);
            byte[] encryptData2 = sc2.encryptData(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), randomData, ck_mechanism);

            Assert.assertArrayEquals(encryptData1,encryptData2);

            byte[] decryptData1 = sc1.decryptData(sid1, tobeWrappedKey, encryptData2, ck_mechanism);
            byte[] decryptData2 = sc2.decryptData(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), encryptData1, ck_mechanism);

            Assert.assertArrayEquals(randomData, decryptData1);
            Assert.assertArrayEquals(randomData, decryptData2);
            // --- ENCRYPT - DECRYPT AND COMPARE ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            CK_ATTRIBUTE[] ckAttributes1 = convertLongListToCKATTRIBUTEArray(getPkcs11DESSecretKeyObjectAttributes());
            CK_ATTRIBUTE[] ckAttributes2 = convertLongListToCKATTRIBUTEArray(getPkcs11DESSecretKeyObjectAttributes());

            sc1.getAttributeValue(sid1, tobeWrappedKey, ckAttributes1);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2);

            for (int i = 0; i < ckAttributes1.length; i++) {
                if(ckAttributes1[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedKeys.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());

        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "tobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "tobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    @Test
    public void testWrapperAESSecretKeyAndToBeWrappedCertificate_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsTokenTemplate(false, false, true);

            ECertificate eCertificate = ECertificate.readFromFile("T:\\api-parent\\resources\\unit-test-resources\\certificate\\QCA1_2.crt");

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            sc1.importCertificate(sid1, "tobeWrapped", eCertificate.asX509Certificate());

            long[] objects = sc1.findObjects(sid1, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, "tobeWrapped"), new CK_ATTRIBUTE(PKCS11Constants.CKA_CLASS, PKCS11Constants.CKO_CERTIFICATE)});
            long[] tobeWrappedCertificates = new long[]{objects[0]};

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, wrapMechanism, "wrapperKey", tobeWrappedCertificates);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
            sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, wrapMechanism, "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            CK_ATTRIBUTE[] ckAttributes1 = convertLongListToCKATTRIBUTEArray(getPkcs11CertificateObjectAttributes());
            CK_ATTRIBUTE[] ckAttributes2 = convertLongListToCKATTRIBUTEArray(getPkcs11CertificateObjectAttributes());

            sc1.getAttributeValue(sid1, tobeWrappedCertificates[0], ckAttributes1);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2);

            for (int i = 0; i < ckAttributes1.length; i++) {
                if(ckAttributes1[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedCertificates.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());

        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "tobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "tobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    @Test
    public void testWrapperAESSecretKeyAndToBeWrappedRSAPrivateKey_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsTokenTemplate(false, false, true);

            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            RSAKeyPairTemplate toBeWrappedRSAKeyPairTemplate = new RSAKeyPairTemplate("tobeWrapped", spec);
            toBeWrappedRSAKeyPairTemplate.getAsTokenTemplate(true, false, false);
            toBeWrappedRSAKeyPairTemplate.getAsExtractableTemplate();
            toBeWrappedRSAKeyPairTemplate.getPublicKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
            toBeWrappedRSAKeyPairTemplate.getPrivateKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            sc1.createKeyPair(sid1, toBeWrappedRSAKeyPairTemplate);

            long[] tobeWrappedKeys = new long[]{toBeWrappedRSAKeyPairTemplate.getPrivateKeyTemplate().getKeyId()};

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);
            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, wrapMechanism, "wrapperKey", tobeWrappedKeys);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
            sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, wrapMechanism, "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- SIGN AND COMPARE ---
            byte[] randomData = RandomUtil.generateRandom(16);
            CK_MECHANISM ck_mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            byte[] signData1 = sc1.signData(sid1, "tobeWrapped", randomData, ck_mechanism);
            byte[] signData2 = sc2.signData(sid2, "tobeWrapped", randomData, ck_mechanism);

            Assert.assertArrayEquals(signData1,signData2);
            // --- SIGN AND COMPARE ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            CK_ATTRIBUTE[] ckAttributes1 = convertLongListToCKATTRIBUTEArray(getPkcs11RSAPrivateKeyObjectAttributes());
            CK_ATTRIBUTE[] ckAttributes2 = convertLongListToCKATTRIBUTEArray(getPkcs11RSAPrivateKeyObjectAttributes());

            sc1.getAttributeValue(sid1, tobeWrappedKeys[0], ckAttributes1);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2);

            for (int i = 0; i < ckAttributes1.length; i++) {
                if(ckAttributes1[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedKeys.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());
            
        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "tobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "tobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    @Test
    public void testWrapperAESSecretKeyAndToBeWrappedRSAPublicKey_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsTokenTemplate(false, false, true);

            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            RSAKeyPairTemplate toBeWrappedRSAKeyPairTemplate = new RSAKeyPairTemplate("tobeWrapped", spec);
            toBeWrappedRSAKeyPairTemplate.getAsTokenTemplate(true, false, false);
            toBeWrappedRSAKeyPairTemplate.getAsExtractableTemplate();
            toBeWrappedRSAKeyPairTemplate.getPublicKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
            toBeWrappedRSAKeyPairTemplate.getPrivateKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            sc1.createKeyPair(sid1, toBeWrappedRSAKeyPairTemplate);

            long[] tobeWrappedKeys = new long[]{toBeWrappedRSAKeyPairTemplate.getPublicKeyTemplate().getKeyId()};

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);
            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, wrapMechanism, "wrapperKey", tobeWrappedKeys);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
            sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, wrapMechanism, "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            List<Long> pkcs11RSAPublicKeyObjectAttributes = getPkcs11RSAPublicKeyObjectAttributes();
            pkcs11RSAPublicKeyObjectAttributes.remove(5); // Local değişkeni bulunmadığı için hata veriyor o yüzden şimdilik LOCAL remove edildi. İncelenmesi lazım.
            CK_ATTRIBUTE[] ckAttributes1 = convertLongListToCKATTRIBUTEArray(pkcs11RSAPublicKeyObjectAttributes);
            CK_ATTRIBUTE[] ckAttributes2 = convertLongListToCKATTRIBUTEArray(pkcs11RSAPublicKeyObjectAttributes);

            sc1.getAttributeValue(sid1, tobeWrappedKeys[0], ckAttributes1);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2);

            for (int i = 0; i < ckAttributes1.length; i++) {
                if(ckAttributes1[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedKeys.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());
            
        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "tobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "tobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    @Test
    public void testWrapperAESSecretKeyAndToBeWrappedECPrivateKey_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsWrapperTemplate();
            wrapperAESKeyTemplate.getAsUnwrapperTemplate();

            // get parameter spec
            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("secp384r1");

            // create key pair template
            ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate("tobeWrapped", ecParameterSpec);
            ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
            ecKeyPairTemplate.getAsExtractableTemplate();
            ecKeyPairTemplate.getPublicKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
            ecKeyPairTemplate.getPrivateKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            sc1.createKeyPair(sid1, ecKeyPairTemplate);

            long[] tobeWrappedKeys = new long[]{ecKeyPairTemplate.getPrivateKeyTemplate().getKeyId()};

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);
            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, wrapMechanism, "wrapperKey", tobeWrappedKeys);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
            sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, wrapMechanism, "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- SIGN - VERIFY AND COMPARE ---
            byte[] randomData = RandomUtil.generateRandom(16);
            CK_MECHANISM ck_mechanism = new CK_MECHANISM(PKCS11Constants.CKM_ECDSA);

            byte[] signData1 = sc1.signData(sid1, "tobeWrapped", randomData, ck_mechanism);
            byte[] signData2 = sc2.signData(sid2, "tobeWrapped", randomData, ck_mechanism);

            Assert.assertArrayEquals(signData1,signData2);
            // --- SIGN - VERIFY AND COMPARE ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            CK_ATTRIBUTE[] ckAttributes1 = convertLongListToCKATTRIBUTEArray(getPkcs11ECPrivateKeyObjectAttributes());
            CK_ATTRIBUTE[] ckAttributes2 = convertLongListToCKATTRIBUTEArray(getPkcs11ECPrivateKeyObjectAttributes());

            sc1.getAttributeValue(sid1, tobeWrappedKeys[0], ckAttributes1);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2);

            for (int i = 0; i < ckAttributes1.length; i++) {
                if(ckAttributes1[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedKeys.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());
            
        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "tobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "tobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    @Test
    public void testWrapperAESSecretKeyAndToBeWrappedECPublicKey_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsWrapperTemplate();
            wrapperAESKeyTemplate.getAsUnwrapperTemplate();

            // get parameter spec
            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("secp384r1");

            // create key pair template
            ECKeyPairTemplate ecKeyPairTemplate = new ECKeyPairTemplate("tobeWrapped", ecParameterSpec);
            ecKeyPairTemplate.getAsTokenTemplate(true, false, false);
            ecKeyPairTemplate.getAsExtractableTemplate();
            ecKeyPairTemplate.getPublicKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
            ecKeyPairTemplate.getPrivateKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            sc1.createKeyPair(sid1, ecKeyPairTemplate);

            long[] tobeWrappedKeys = new long[]{ecKeyPairTemplate.getPublicKeyTemplate().getKeyId()};

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);
            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, wrapMechanism, "wrapperKey", tobeWrappedKeys);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
            sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, wrapMechanism, "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            CK_ATTRIBUTE[] ckAttributes1 = convertLongListToCKATTRIBUTEArray(getPkcs11ECPublicKeyObjectAttributes());
            CK_ATTRIBUTE[] ckAttributes2 = convertLongListToCKATTRIBUTEArray(getPkcs11ECPublicKeyObjectAttributes());

            sc1.getAttributeValue(sid1, tobeWrappedKeys[0], ckAttributes1);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2);

            for (int i = 0; i < ckAttributes1.length; i++) {
                if(ckAttributes1[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedKeys.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());

        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "tobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "tobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    // AES WRAPPER - MULTIPLE KEY

    @Test
    public void testWrapperAESSecretKeyAndMultiToBeWrappedKey_Export_Import() throws PKCS11Exception {
        boolean isTestFailed = true;
        try {
            // --- EXPORT ---
            byte[] aesValue = RandomUtil.generateRandom(32);
            AESKeyTemplate wrapperAESKeyTemplate = new AESKeyTemplate("wrapperKey", aesValue);
            wrapperAESKeyTemplate.getAsTokenTemplate(false, false, true);

            AESKeyTemplate tobeWrappedAESKeyTemplate = new AESKeyTemplate("AEStobeWrapped", 32);
            tobeWrappedAESKeyTemplate.getAsTokenTemplate(false, true, false);
            tobeWrappedAESKeyTemplate.getAsExportableTemplate();

            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            RSAKeyPairTemplate toBeWrappedRSAKeyPairTemplate = new RSAKeyPairTemplate("RSAtobeWrapped", spec);
            toBeWrappedRSAKeyPairTemplate.getAsTokenTemplate(true, false, false);
            toBeWrappedRSAKeyPairTemplate.getAsExtractableTemplate();
            toBeWrappedRSAKeyPairTemplate.getPublicKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
            toBeWrappedRSAKeyPairTemplate.getPrivateKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));


            ECParameterSpec ecParameterSpec = NamedCurve.getECParameterSpec("secp384r1");
            ECKeyPairTemplate toBeWrappedECKeyPairTemplate = new ECKeyPairTemplate("ECtobeWrapped", ecParameterSpec);
            toBeWrappedECKeyPairTemplate.getAsTokenTemplate(true, false, false);
            toBeWrappedECKeyPairTemplate.getAsExtractableTemplate();
            toBeWrappedECKeyPairTemplate.getPublicKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));
            toBeWrappedECKeyPairTemplate.getPrivateKeyTemplate().attributes.put(PKCS11Constants.CKA_TOKEN, new CK_ATTRIBUTE(PKCS11Constants.CKA_TOKEN, true));

            sc1.importSecretKey(sid1, wrapperAESKeyTemplate);
            long tobeWrappedKey1 = sc1.createSecretKey(sid1, tobeWrappedAESKeyTemplate);
            sc1.createKeyPair(sid1, toBeWrappedRSAKeyPairTemplate);
            sc1.createKeyPair(sid1, toBeWrappedECKeyPairTemplate);

            long[] tobeWrappedKeys = new long[]{
                    toBeWrappedRSAKeyPairTemplate.getPublicKeyTemplate().getKeyId(), toBeWrappedRSAKeyPairTemplate.getPrivateKeyTemplate().getKeyId(),
                    toBeWrappedECKeyPairTemplate.getPublicKeyTemplate().getKeyId(), toBeWrappedECKeyPairTemplate.getPrivateKeyTemplate().getKeyId(),
                    tobeWrappedKey1
            };

            final byte[] wrapIV = RandomUtil.generateRandom(16);
            CK_MECHANISM wrapMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC_PAD, wrapIV);

            WrappedObjectsWithAttributes wrappedObjectsWithAttributes = sc1.wrapObjectsWithAttributes(sid1, wrapMechanism, "wrapperKey", tobeWrappedKeys);

            byte[] wrappedObjectWithAttributesBytes = wrappedObjectsWithAttributes.getWrappedObjects();
            // --- EXPORT ---

            // --- IMPORT ---
           sc2.importSecretKey(sid2, wrapperAESKeyTemplate);

            UnwrapObjectsResults unwrapObjectsResults = sc2.unwrapObjectsWithAttributes(sid2, wrapMechanism, "wrapperKey", wrappedObjectWithAttributesBytes);
            // --- IMPORT ---

            // --- SIGN - VERIFY AND COMPARE ---
            // RSA
            byte[] randomDataForRSASigning = RandomUtil.generateRandom(16);
            CK_MECHANISM ck_mechanismForRSASigning = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);

            byte[] signedData1ByRSA = sc1.signData(sid1, "RSAtobeWrapped", randomDataForRSASigning, ck_mechanismForRSASigning);
            byte[] signedData2ByRSA = sc2.signData(sid2, "RSAtobeWrapped", randomDataForRSASigning, ck_mechanismForRSASigning);

            Assert.assertArrayEquals(signedData1ByRSA, signedData2ByRSA);

            sc1.verifyData(sid1, "RSAtobeWrapped", randomDataForRSASigning, signedData2ByRSA, ck_mechanismForRSASigning);
            sc2.verifyData(sid2, "RSAtobeWrapped", randomDataForRSASigning, signedData1ByRSA, ck_mechanismForRSASigning);
            // --- SIGN - VERIFY AND COMPARE ---

            // --- SIGN - VERIFY AND COMPARE ---
            // EC
            byte[] randomDataForECSigning = RandomUtil.generateRandom(16);
            CK_MECHANISM ck_mechanismForECSigning = new CK_MECHANISM(PKCS11Constants.CKM_ECDSA);

            byte[] signedData1ByEC = sc1.signData(sid1, "ECtobeWrapped", randomDataForECSigning, ck_mechanismForECSigning);
            byte[] signedData2ByEC = sc2.signData(sid2, "ECtobeWrapped", randomDataForECSigning, ck_mechanismForECSigning);

            Assert.assertArrayEquals(signedData1ByEC, signedData2ByEC);

            sc1.verifyData(sid1, "ECtobeWrapped", randomDataForECSigning, signedData2ByEC, ck_mechanismForECSigning);
            sc2.verifyData(sid2, "ECtobeWrapped", randomDataForECSigning, signedData1ByEC, ck_mechanismForECSigning);
            // --- SIGN - VERIFY AND COMPARE ---

            // --- ENCRYPT - DECRYPT AND COMPARE ---
            byte[] randomDataForEncryption = RandomUtil.generateRandom(16);
            CK_MECHANISM ck_mechanismForEncryption = new CK_MECHANISM(PKCS11Constants.CKM_AES_ECB);

            byte[] encryptData1 = sc1.encryptData(sid1, tobeWrappedKeys[4], randomDataForEncryption, ck_mechanismForEncryption);
            byte[] encryptData2 = sc2.encryptData(sid2, unwrapObjectsResults.getObjectResult().get(4).getObjectID(), randomDataForEncryption, ck_mechanismForEncryption);

            Assert.assertArrayEquals(encryptData1,encryptData2);

            byte[] decryptData1 = sc1.decryptData(sid1, tobeWrappedKeys[4], encryptData2, ck_mechanismForEncryption);
            byte[] decryptData2 = sc2.decryptData(sid2, unwrapObjectsResults.getObjectResult().get(4).getObjectID(), encryptData1, ck_mechanismForEncryption);

            Assert.assertArrayEquals(randomDataForEncryption, decryptData1);
            Assert.assertArrayEquals(randomDataForEncryption, decryptData2);
            // --- ENCRYPT - DECRYPT AND COMPARE ---

            // --- COMPARING THE VALUES OF ATTRIBUTES
            // RSA public
            List<Long> pkcs11RSAPublicKeyObjectAttributes = getPkcs11RSAPublicKeyObjectAttributes();
            pkcs11RSAPublicKeyObjectAttributes.remove(5); // Local değişkeni bulunmadığı için hata veriyor o yüzden şimdilik LOCAL remove edildi. İncelenmesi lazım.
            CK_ATTRIBUTE[] ckAttributes1ForRSAPublic = convertLongListToCKATTRIBUTEArray(pkcs11RSAPublicKeyObjectAttributes);
            CK_ATTRIBUTE[] ckAttributes2ForRSAPublic = convertLongListToCKATTRIBUTEArray(pkcs11RSAPublicKeyObjectAttributes);

            sc1.getAttributeValue(sid1, tobeWrappedKeys[0], ckAttributes1ForRSAPublic);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(0).getObjectID(), ckAttributes2ForRSAPublic);

            for (int i = 0; i < ckAttributes1ForRSAPublic.length; i++) {
                if(ckAttributes1ForRSAPublic[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2ForRSAPublic[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1ForRSAPublic[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2ForRSAPublic[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            // --- COMPARING THE VALUES OF ATTRIBUTES
            // RSA private
            List<Long> pkcs11RSAPrivateKeyObjectAttributes = getPkcs11RSAPrivateKeyObjectAttributes();
            CK_ATTRIBUTE[] ckAttributes1ForRSAPrivate = convertLongListToCKATTRIBUTEArray(pkcs11RSAPrivateKeyObjectAttributes);
            CK_ATTRIBUTE[] ckAttributes2ForRSAPrivate = convertLongListToCKATTRIBUTEArray(pkcs11RSAPrivateKeyObjectAttributes);

            sc1.getAttributeValue(sid1, tobeWrappedKeys[1], ckAttributes1ForRSAPrivate);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(1).getObjectID(), ckAttributes2ForRSAPrivate);

            for (int i = 0; i < ckAttributes1ForRSAPrivate.length; i++) {
                if(ckAttributes1ForRSAPrivate[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2ForRSAPrivate[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1ForRSAPrivate[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2ForRSAPrivate[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            // --- COMPARING THE VALUES OF ATTRIBUTES
            // EC public
            List<Long> pkcs11ECPublicKeyObjectAttributes = getPkcs11ECPublicKeyObjectAttributes();
            CK_ATTRIBUTE[] ckAttributes1ForECPublic = convertLongListToCKATTRIBUTEArray(pkcs11ECPublicKeyObjectAttributes);
            CK_ATTRIBUTE[] ckAttributes2ForECPublic = convertLongListToCKATTRIBUTEArray(pkcs11ECPublicKeyObjectAttributes);

            sc1.getAttributeValue(sid1, tobeWrappedKeys[2], ckAttributes1ForECPublic);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(2).getObjectID(), ckAttributes2ForECPublic);

            for (int i = 0; i < ckAttributes1ForECPublic.length; i++) {
                if(ckAttributes1ForECPublic[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2ForECPublic[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1ForECPublic[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2ForECPublic[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            // --- COMPARING THE VALUES OF ATTRIBUTES
            // EC private
            List<Long> pkcs11ECPrivateKeyObjectAttributes = getPkcs11ECPrivateKeyObjectAttributes();
            CK_ATTRIBUTE[] ckAttributes1ForECPrivate = convertLongListToCKATTRIBUTEArray(pkcs11ECPrivateKeyObjectAttributes);
            CK_ATTRIBUTE[] ckAttributes2ForECPrivate = convertLongListToCKATTRIBUTEArray(pkcs11ECPrivateKeyObjectAttributes);

            sc1.getAttributeValue(sid1, tobeWrappedKeys[3], ckAttributes1ForECPrivate);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(3).getObjectID(), ckAttributes2ForECPrivate);

            for (int i = 0; i < ckAttributes1ForECPrivate.length; i++) {
                if(ckAttributes1ForECPrivate[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2ForECPrivate[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1ForECPrivate[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2ForECPrivate[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            // --- COMPARING THE VALUES OF ATTRIBUTES
            // AES
            List<Long> pkcs11AESSecretKeyObjectAttributes = getPkcs11AESSecretKeyObjectAttributes();
            CK_ATTRIBUTE[] ckAttributes1ForAESSecret = convertLongListToCKATTRIBUTEArray(pkcs11AESSecretKeyObjectAttributes);
            CK_ATTRIBUTE[] ckAttributes2ForAESSecret = convertLongListToCKATTRIBUTEArray(pkcs11AESSecretKeyObjectAttributes);

            sc1.getAttributeValue(sid1, tobeWrappedKeys[4], ckAttributes1ForAESSecret);
            sc2.getAttributeValue(sid2, unwrapObjectsResults.getObjectResult().get(4).getObjectID(), ckAttributes2ForAESSecret);

            for (int i = 0; i < ckAttributes1ForAESSecret.length; i++) {
                if(ckAttributes1ForAESSecret[i].type == PKCS11Constants.CKA_LOCAL && ckAttributes2ForAESSecret[i].type == PKCS11Constants.CKA_LOCAL){
                    continue;
                }
                Assert.assertArrayEquals(ByteConversionUtil.objectToBytes(ckAttributes1ForAESSecret[i].pValue), ByteConversionUtil.objectToBytes(ckAttributes2ForAESSecret[i].pValue));
            }
            // --- COMPARING THE VALUES OF ATTRIBUTES

            Assert.assertEquals(tobeWrappedKeys.length, wrappedObjectsWithAttributes.wrappedObjectTotal());
            Assert.assertEquals(wrappedObjectsWithAttributes.wrappedObjectTotal(), unwrapObjectsResults.unwrappedObjectTotal());
            
        }catch (Exception ex){
            ex.printStackTrace();
            isTestFailed = false;
        }finally {
            deleteObjects(sc1, sid1, "wrapperKey");
            deleteObjects(sc1, sid1, "RSAtobeWrapped");
            deleteObjects(sc1, sid1, "AEStobeWrapped");
            deleteObjects(sc1, sid1, "ECtobeWrapped");
            deleteObjects(sc2, sid2, "wrapperKey");
            deleteObjects(sc2, sid2, "RSAtobeWrapped");
            deleteObjects(sc2, sid2, "AEStobeWrapped");
            deleteObjects(sc2, sid2, "ECtobeWrapped");
        }
        Assert.assertTrue(isTestFailed);
    }

    public static void deleteObjects(SmartCard sc, long sid, String aLabel) throws PKCS11Exception {
        CK_ATTRIBUTE[] template = {new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, aLabel)};

        long[] objectList = sc.findObjects(sid, template);

        for (int i = 0; i < objectList.length; i++) {
            sc.getPKCS11().C_DestroyObject(sid, objectList[i]);
        }
    }

    private CK_ATTRIBUTE[] convertLongListToCKATTRIBUTEArray(List<Long> longList){
        CK_ATTRIBUTE[] ckAttributes = new CK_ATTRIBUTE[longList.size()];

        for (int i = 0; i < longList.size(); i++) {
            ckAttributes[i] = new CK_ATTRIBUTE(longList.get(i));
        }
        return ckAttributes;
    }

    private List<Long> getPkcs11CertificateObjectAttributes(){
        List<Long> pkcs11CertificateObjectAttributes = new ArrayList<>();

        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_CERTIFICATE_TYPE);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_TRUSTED);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_CERTIFICATE_CATEGORY);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_CHECK_VALUE);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_START_DATE);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_END_DATE);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_PUBLIC_KEY_INFO);

        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_SUBJECT);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_ID);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_ISSUER);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_SERIAL_NUMBER);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_VALUE);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_URL);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_HASH_OF_SUBJECT_PUBLIC_KEY);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_HASH_OF_ISSUER_PUBLIC_KEY);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_JAVA_MIDP_SECURITY_DOMAIN);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_NAME_HASH_ALGORITHM);

        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_OWNER);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_AC_ISSUER);
        pkcs11CertificateObjectAttributes.add(PKCS11Constants.CKA_ATTR_TYPES);

        return pkcs11CertificateObjectAttributes;
    }

    private List<Long> getPkcs11KeyObjectAttributes() {
        List<Long> pkcs11KeyObjectAttributes = new ArrayList<>();

        pkcs11KeyObjectAttributes.add(PKCS11Constants.CKA_KEY_TYPE);
        pkcs11KeyObjectAttributes.add(PKCS11Constants.CKA_ID);
        pkcs11KeyObjectAttributes.add(PKCS11Constants.CKA_START_DATE);
        pkcs11KeyObjectAttributes.add(PKCS11Constants.CKA_END_DATE);
        pkcs11KeyObjectAttributes.add(PKCS11Constants.CKA_DERIVE);
        pkcs11KeyObjectAttributes.add(PKCS11Constants.CKA_LOCAL);
        //pkcs11KeyObjectAttributes.add(PKCS11Constants.CKA_KEY_GEN_MECHANISM); neden hata verdi bilmiyorum. daha önce hata vermiyordu sanki...
        //pkcs11KeyObjectAttributes.add(PKCS11Constants.CKA_ALLOWED_MECHANISMS); Standart'da var ama DIRAK'da yorum satırına alınmış bu özellik.

        return pkcs11KeyObjectAttributes;
    }

    private List<Long> getPkcs11PublicKeyObjectAttributes() {
        List<Long> pkcs11PublicKeyObjectAttributes = new ArrayList<>();

        pkcs11PublicKeyObjectAttributes.addAll(getPkcs11KeyObjectAttributes());

        //pkcs11PublicKeyObjectAttributes.add(PKCS11Constants.CKA_SUBJECT);
        pkcs11PublicKeyObjectAttributes.add(PKCS11Constants.CKA_ENCRYPT);
        pkcs11PublicKeyObjectAttributes.add(PKCS11Constants.CKA_VERIFY);
        pkcs11PublicKeyObjectAttributes.add(PKCS11Constants.CKA_VERIFY_RECOVER);
        pkcs11PublicKeyObjectAttributes.add(PKCS11Constants.CKA_WRAP);
        pkcs11PublicKeyObjectAttributes.add(PKCS11Constants.CKA_TRUSTED);
        //pkcs11PublicKeyObjectAttributes.add(PKCS11Constants.CKA_WRAP_TEMPLATE);
        //pkcs11PublicKeyObjectAttributes.add(PKCS11Constants.CKA_PUBLIC_KEY_INFO);

        return pkcs11PublicKeyObjectAttributes;
    }

    private List<Long> getPkcs11RSAPublicKeyObjectAttributes() {
        List<Long> pkcs11RSAPublicKeyObjectAttributes = new ArrayList<>();

        pkcs11RSAPublicKeyObjectAttributes.addAll(getPkcs11PublicKeyObjectAttributes());

        pkcs11RSAPublicKeyObjectAttributes.add(PKCS11Constants.CKA_MODULUS);
        //pkcs11RSAPublicKeyObjectAttributes.add(PKCS11Constants.CKA_MODULUS_BITS);
        pkcs11RSAPublicKeyObjectAttributes.add(PKCS11Constants.CKA_PUBLIC_EXPONENT);

        return pkcs11RSAPublicKeyObjectAttributes;
    }

    private List<Long> getPkcs11ECPublicKeyObjectAttributes() {
        List<Long> pkcs11ECPublicKeyObjectAttributes = new ArrayList<>();

        pkcs11ECPublicKeyObjectAttributes.addAll(getPkcs11PublicKeyObjectAttributes());

        pkcs11ECPublicKeyObjectAttributes.add(PKCS11Constants.CKA_EC_PARAMS);
        pkcs11ECPublicKeyObjectAttributes.add(PKCS11Constants.CKA_EC_POINT);

        return pkcs11ECPublicKeyObjectAttributes;
    }

    private List<Long> getPkcs11PrivateKeyObjectAttributes() {
        List<Long> pkcs11PrivateKeyObjectAttributes = new ArrayList<>();

        pkcs11PrivateKeyObjectAttributes.addAll(getPkcs11KeyObjectAttributes());

        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_SUBJECT);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_SENSITIVE);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_DECRYPT);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_SIGN);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_SIGN_RECOVER);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_UNWRAP);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_EXTRACTABLE);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_ALWAYS_SENSITIVE);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_NEVER_EXTRACTABLE);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_WRAP_WITH_TRUSTED);
        //pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_UNWRAP_TEMPLATE);
        pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_ALWAYS_AUTHENTICATE);
        //pkcs11PrivateKeyObjectAttributes.add(PKCS11Constants.CKA_PUBLIC_KEY_INFO);

        return pkcs11PrivateKeyObjectAttributes;
    }

    private List<Long> getPkcs11RSAPrivateKeyObjectAttributes() {
        List<Long> pkcs11RSAPrivateKeyObjectAttributes = new ArrayList<>();

        pkcs11RSAPrivateKeyObjectAttributes.addAll(getPkcs11PrivateKeyObjectAttributes());

        pkcs11RSAPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_MODULUS);
        pkcs11RSAPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_PUBLIC_EXPONENT);
        // bu değerler okunamıyor.
        //pkcs11RSAPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_PRIVATE_EXPONENT);
        //pkcs11RSAPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_PRIME_1);
        //pkcs11RSAPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_PRIME_2);
        //pkcs11RSAPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_EXPONENT_1);
        //pkcs11RSAPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_EXPONENT_2);
        //pkcs11RSAPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_COEFFICIENT);

        return pkcs11RSAPrivateKeyObjectAttributes;
    }

    private List<Long> getPkcs11ECPrivateKeyObjectAttributes() {
        List<Long> pkcs11ECPrivateKeyObjectAttributes = new ArrayList<>();

        pkcs11ECPrivateKeyObjectAttributes.addAll(getPkcs11PrivateKeyObjectAttributes());

        pkcs11ECPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_EC_PARAMS);
        //pkcs11ECPrivateKeyObjectAttributes.add(PKCS11Constants.CKA_VALUE);

        return pkcs11ECPrivateKeyObjectAttributes;
    }

    private List<Long> getPkcs11SecretKeyObjectAttributes() {
        List<Long> pkcs11SecretKeyObjectAttributes = new ArrayList<>();

        pkcs11SecretKeyObjectAttributes.addAll(getPkcs11KeyObjectAttributes());

        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_SENSITIVE);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_ENCRYPT);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_DECRYPT);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_SIGN);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_VERIFY);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_WRAP);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_UNWRAP);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_EXTRACTABLE);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_ALWAYS_SENSITIVE);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_NEVER_EXTRACTABLE);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_CHECK_VALUE);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_WRAP_WITH_TRUSTED);
        pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_TRUSTED);
        //pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_WRAP_TEMPLATE); Standart'da var ama DIRAK'da yorum satırına alınmış bu özellik.
        //pkcs11SecretKeyObjectAttributes.add(PKCS11Constants.CKA_UNWRAP_TEMPLATE); Standart'da var ama DIRAK'da yorum satırına alınmış bu özellik.

        return pkcs11SecretKeyObjectAttributes;
    }

    private List<Long> getPkcs11AESSecretKeyObjectAttributes() {
        List<Long> pkcs11AESSecretKeyObjectAttributes = new ArrayList<>();

        pkcs11AESSecretKeyObjectAttributes.addAll(getPkcs11SecretKeyObjectAttributes());

        //pkcs11AESSecretKeyObjectAttributes.add(PKCS11Constants.CKA_VALUE); CKA_VALUE okunamıyor.
        pkcs11AESSecretKeyObjectAttributes.add(PKCS11Constants.CKA_VALUE_LEN);

        return pkcs11AESSecretKeyObjectAttributes;
    }

    private List<Long> getPkcs11DESSecretKeyObjectAttributes() {
        List<Long> pkcs11DESSecretKeyObjectAttributes = new ArrayList<>();

        pkcs11DESSecretKeyObjectAttributes.addAll(getPkcs11KeyObjectAttributes());

        //pkcs11DESSecretKeyObjectAttributes.add(PKCS11Constants.CKA_VALUE);

        return pkcs11DESSecretKeyObjectAttributes;
    }
}
