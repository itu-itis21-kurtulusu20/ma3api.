package dev.esya.api.smartcard.dirak;

import org.junit.*;
import org.junit.rules.ExpectedException;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;

import java.io.IOException;
import java.security.spec.RSAKeyGenParameterSpec;

public class DirakSetAttributeTest {

    SmartCard sc;
    long slot;
    long sid;
    static final String PIN = "12345678";

    static long privateKeyID = 0L;
    static long publicKeyID = 0L;
    static final String keyLabel = "testKey";

    @Rule
    public ExpectedException expectedExceptionRule = ExpectedException.none();

    public DirakSetAttributeTest() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.DIRAKHSM);
    }

    @Before
    public void setUp() throws PKCS11Exception, IOException {
        sc = new SmartCard(CardType.DIRAKHSM);
        slot = CardTestUtil.getSlot(sc);
        sid = sc.openSession(slot);
        sc.login(sid, PIN);
    }

    @After
    public void cleanUp() throws PKCS11Exception {
        // delete the temporary test key pair
        if (privateKeyID > 0L) {
            sc.getPKCS11().C_DestroyObject(sid, privateKeyID);
            privateKeyID = 0L;
        }
        if (publicKeyID > 0L) {
            sc.getPKCS11().C_DestroyObject(sid, publicKeyID);
            publicKeyID = 0L;
        }

        sc.logout(sid);
        sc.closeSession(sid);
    }

    // ---

    @Test
    public void setSensitiveAttributeFalseToTrue() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false)});
        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true)});
    }

    @Test
    public void setSensitiveAttributeTrueToFalse() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true)});

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false)});
    }

    @Test
    public void setSensitiveAttributeTrueToTrue() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true)});

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true)});
    }

    @Test
    public void setExtractableAttributeToTrueWithSensitiveTrue() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true)});
        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true)});
    }

    @Test
    public void setExtractableAttributeToFalseWithSensitiveTrue() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true)});
        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false)});
    }

    @Test
    public void setExtractableAttributeFalseToTrue() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false)});

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true)});
    }

    @Test
    public void setExtractableAttributeTrueToFalse() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true)});
        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, false)});
    }

    @Test
    public void setNeverExtractableToTrue() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_NEVER_EXTRACTABLE, true)});
    }

    @Test
    public void setNeverExtractableToFalse() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_NEVER_EXTRACTABLE, false)});
    }

    // CKA_ALWAYS_SENSITIVE is read-only.
    @Test
    public void setAlwaysSensitiveTrue() throws PKCS11Exception, SmartCardException, IOException {
        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, true)});
    }

    // CKA_ALWAYS_SENSITIVE is read-only.
    @Test
    public void setAlwaysSensitiveFalse() throws PKCS11Exception, SmartCardException, IOException {
        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, true)});
    }

    // // CKA_ALWAYS_SENSITIVE must not be specified for object generation using C_GenerateKey(Pair) (PKCS#11 v2.40, table 10)
    // @Test
    // public void createKeyPairWithAlwaysSensitiveTrue() throws PKCS11Exception, SmartCardException, IOException {
    //     expectedExceptionRule.expect(PKCS11Exception.class);
    //     expectedExceptionRule.expectMessage("CKR_TEMPLATE_INCONSISTENT");
    //
    //     RSAKeyPairTemplate rsaKeyPairTemplate;
    //     {
    //         RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
    //         rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
    //         rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
    //         rsaKeyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, true));
    //     }
    //
    //     // create key pair
    //     sc.createKeyPair(sid, rsaKeyPairTemplate);
    //     privateKeyID = rsaKeyPairTemplate.getPrivateKeyTemplate().getKeyId();
    //     publicKeyID = rsaKeyPairTemplate.getPublicKeyTemplate().getKeyId();
    //
    //     // ---
    //
    //     // output what value is assigned to the attribute
    //     {
    //         CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
    //         };
    //         sc.getAttributeValue(sid, privateKeyID, template);
    //         System.out.println("CKA_SENSITIVE is " + ((boolean) template[0].pValue));
    //         System.out.println("CKA_ALWAYS_SENSITIVE is " + ((boolean) template[1].pValue));
    //     }
    // }
    //
    // // CKA_ALWAYS_SENSITIVE must not be specified for object generation using C_GenerateKey(Pair) (PKCS#11 v2.40, table 10)
    // @Test
    // public void createKeyPairWithAlwaysSensitiveFalse() throws PKCS11Exception, SmartCardException, IOException {
    //     expectedExceptionRule.expect(PKCS11Exception.class);
    //     expectedExceptionRule.expectMessage("CKR_TEMPLATE_INCONSISTENT");
    //
    //     RSAKeyPairTemplate rsaKeyPairTemplate;
    //     {
    //         RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
    //         rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
    //         rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
    //         rsaKeyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, false));
    //     }
    //
    //     // create key pair
    //     sc.createKeyPair(sid, rsaKeyPairTemplate);
    //     privateKeyID = rsaKeyPairTemplate.getPrivateKeyTemplate().getKeyId();
    //     publicKeyID =rsaKeyPairTemplate.getPublicKeyTemplate().getKeyId();
    //
    //     // ---
    //
    //     // output what value is assigned to the attribute
    //     {
    //         CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
    //         };
    //         sc.getAttributeValue(sid, privateKeyID, template);
    //         System.out.println("CKA_SENSITIVE is " + ((boolean) template[0].pValue));
    //         System.out.println("CKA_ALWAYS_SENSITIVE is " + ((boolean) template[1].pValue));
    //     }
    // }
    //
    // // CKA_ALWAYS_SENSITIVE must not be specified for object generation using C_GenerateKey(Pair) (PKCS#11 v2.40, table 10)
    // @Test
    // public void createSecretKeyWithAlwaysSensitiveTrue() throws PKCS11Exception, SmartCardException {
    //     expectedExceptionRule.expect(PKCS11Exception.class);
    //     expectedExceptionRule.expectMessage("CKR_TEMPLATE_INCONSISTENT");
    //
    //     AESKeyTemplate aesKeyTemplate;
    //     {
    //         aesKeyTemplate = new AESKeyTemplate(keyLabel, 16);
    //         // aesKeyTemplate.getAsTokenTemplate(false, true, false);
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,    false));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,  false));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP,    false));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP,  false));
    //
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, true));
    //     }
    //
    //     // create secret key
    //     secretKeyID = sc.createSecretKey(sid, aesKeyTemplate);
    //
    //     // ---
    //
    //     // output what value is assigned to the attribute
    //     {
    //         CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
    //         };
    //         sc.getAttributeValue(sid, secretKeyID, template);
    //         System.out.println("CKA_SENSITIVE is " + ((boolean) template[0].pValue));
    //         System.out.println("CKA_ALWAYS_SENSITIVE is " + ((boolean) template[1].pValue));
    //     }
    // }
    //
    // // CKA_ALWAYS_SENSITIVE must not be specified for object generation using C_GenerateKey(Pair) (PKCS#11 v2.40, table 10)
    // @Test
    // public void createSecretKeyWithAlwaysSensitiveFalse() throws PKCS11Exception, SmartCardException {
    //     expectedExceptionRule.expect(PKCS11Exception.class);
    //     expectedExceptionRule.expectMessage("CKR_TEMPLATE_INCONSISTENT");
    //
    //     AESKeyTemplate aesKeyTemplate;
    //     {
    //         aesKeyTemplate = new AESKeyTemplate(keyLabel, 16);
    //         // aesKeyTemplate.getAsTokenTemplate(false, true, false);
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SIGN,    false));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_VERIFY,  false));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ENCRYPT, true));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_DECRYPT, true));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_WRAP,    false));
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_UNWRAP,  false));
    //
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, false));
    //     }
    //
    //     // create secret key
    //     secretKeyID = sc.createSecretKey(sid, aesKeyTemplate);
    //
    //     // ---
    //
    //     // output what value is assigned to the attribute
    //     {
    //         CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
    //         };
    //         sc.getAttributeValue(sid, secretKeyID, template);
    //         System.out.println("CKA_SENSITIVE is " + ((boolean) template[0].pValue));
    //         System.out.println("CKA_ALWAYS_SENSITIVE is " + ((boolean) template[1].pValue));
    //     }
    // }

    // // CKA_ALWAYS_SENSITIVE must not be specified for object generation using C_GenerateKey(Pair) (PKCS#11 v2.40, table 10)
    // @Test
    // public void importKeyPairWithAlwaysSensitiveTrue() throws PKCS11Exception, SmartCardException, IOException {
    //     expectedExceptionRule.expect(PKCS11Exception.class);
    //     expectedExceptionRule.expectMessage("CKR_TEMPLATE_INCONSISTENT");
    //
    //     RSAKeyPairTemplate rsaKeyPairTemplate;
    //     {
    //         RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
    //         rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
    //         rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
    //         rsaKeyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, true));
    //     }
    //
    //     // create key pair
    //     sc.importKeyPair(sid, rsaKeyPairTemplate);
    //     privateKeyID = rsaKeyPairTemplate.getPrivateKeyTemplate().getKeyId();
    //     publicKeyID =rsaKeyPairTemplate.getPublicKeyTemplate().getKeyId();
    //
    //     // ---
    //
    //     // output what value is assigned to the attribute
    //     {
    //         CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
    //         };
    //         sc.getAttributeValue(sid, privateKeyID, template);
    //         System.out.println("CKA_SENSITIVE is " + ((boolean) template[0].pValue));
    //         System.out.println("CKA_ALWAYS_SENSITIVE is " + ((boolean) template[1].pValue));
    //     }
    // }
    //
    // // CKA_ALWAYS_SENSITIVE must not be specified for object generation using C_GenerateKey(Pair) (PKCS#11 v2.40, table 10)
    // @Test
    // public void importKeyPairWithAlwaysSensitiveFalse() throws PKCS11Exception, SmartCardException {
    //     expectedExceptionRule.expect(PKCS11Exception.class);
    //     expectedExceptionRule.expectMessage("CKR_TEMPLATE_INCONSISTENT");
    //
    //     RSAKeyPairTemplate rsaKeyPairTemplate;
    //     {
    //         RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
    //         rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
    //         rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
    //         rsaKeyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, false));
    //     }
    //
    //     // create key pair
    //     sc.importKeyPair(sid, rsaKeyPairTemplate);
    //     privateKeyID = rsaKeyPairTemplate.getPrivateKeyTemplate().getKeyId();
    //     publicKeyID =rsaKeyPairTemplate.getPublicKeyTemplate().getKeyId();
    //
    //     // ---
    //
    //     // output what value is assigned to the attribute
    //     {
    //         CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
    //         };
    //         sc.getAttributeValue(sid, privateKeyID, template);
    //         System.out.println("CKA_SENSITIVE is " + ((boolean) template[0].pValue));
    //         System.out.println("CKA_ALWAYS_SENSITIVE is " + ((boolean) template[1].pValue));
    //     }
    // }
    //
    // @Test
    // public void importSecretKeyWithAlwaysSensitiveTrue() throws PKCS11Exception, SmartCardException {
    //     expectedExceptionRule.expect(PKCS11Exception.class);
    //     expectedExceptionRule.expectMessage("CKR_TEMPLATE_INCONSISTENT");
    //
    //     AESKeyTemplate aesKeyTemplate;
    //     {
    //         aesKeyTemplate = new AESKeyTemplate(keyLabel, 16);
    //         aesKeyTemplate.getAsTokenTemplate(false, true, false);
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, true));
    //     }
    //
    //     // import secret key
    //     secretKeyID = sc.importSecretKey(sid, aesKeyTemplate);
    //
    //     // ---
    //
    //     // output what value is assigned to the attribute
    //     {
    //         CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
    //         };
    //         sc.getAttributeValue(sid, privateKeyID, template);
    //         System.out.println("CKA_SENSITIVE is " + ((boolean) template[0].pValue));
    //         System.out.println("CKA_ALWAYS_SENSITIVE is " + ((boolean) template[1].pValue));
    //     }
    // }
    //
    // @Test
    // public void importSecretKeyWithAlwaysSensitiveFalse() throws PKCS11Exception, SmartCardException {
    //     expectedExceptionRule.expect(PKCS11Exception.class);
    //     expectedExceptionRule.expectMessage("CKR_TEMPLATE_INCONSISTENT");
    //
    //     AESKeyTemplate aesKeyTemplate;
    //     {
    //         aesKeyTemplate = new AESKeyTemplate(keyLabel, 16);
    //         aesKeyTemplate.getAsTokenTemplate(false, true, false);
    //         aesKeyTemplate.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, false));
    //     }
    //
    //     // import secret key
    //     secretKeyID = sc.importSecretKey(sid, aesKeyTemplate);
    //
    //     // ---
    //
    //     // output what value is assigned to the attribute
    //     {
    //         CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
    //                 new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
    //         };
    //         sc.getAttributeValue(sid, privateKeyID, template);
    //         System.out.println("CKA_SENSITIVE is " + ((boolean) template[0].pValue));
    //         System.out.println("CKA_ALWAYS_SENSITIVE is " + ((boolean) template[1].pValue));
    //     }
    // }

    // CKA_ALWAYS_SENSITIVE must not be specified for object generation using C_GenerateKey(Pair) (PKCS#11 v2.40, table 10)
    @Test
    public void createKeyPairWithSensitiveTrue() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate rsaKeyPairTemplate;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
            rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
            rsaKeyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true));
        }

        // create key pair
        sc.createKeyPair(sid, rsaKeyPairTemplate);
        privateKeyID = rsaKeyPairTemplate.getPrivateKeyTemplate().getKeyId();
        publicKeyID = rsaKeyPairTemplate.getPublicKeyTemplate().getKeyId();

        // check the attributes
        {
            CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
            };

            sc.getAttributeValue(sid, privateKeyID, template);
            assert (((boolean) template[0].pValue) && ((boolean) template[1].pValue));
        }
    }

    // CKA_ALWAYS_SENSITIVE must not be specified for object generation using C_GenerateKey(Pair) (PKCS#11 v2.40, table 10)
    @Test
    public void createKeyPairWithSensitiveFalse() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate rsaKeyPairTemplate;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
            rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
            rsaKeyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false));
        }

        // create key pair
        sc.createKeyPair(sid, rsaKeyPairTemplate);
        privateKeyID = rsaKeyPairTemplate.getPrivateKeyTemplate().getKeyId();
        publicKeyID = rsaKeyPairTemplate.getPublicKeyTemplate().getKeyId();

        // check the attributes
        {
            CK_ATTRIBUTE[] template = new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE),
                new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE)
            };

            sc.getAttributeValue(sid, privateKeyID, template);
            assert (!(((boolean) template[0].pValue) || ((boolean) template[1].pValue)));
        }
    }

    @Test
    public void setAlwaysSensitiveTrueToFalse() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate rsaKeyPairTemplate;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
            rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
            rsaKeyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, true));
        }

        // create key pair
        sc.createKeyPair(sid, rsaKeyPairTemplate);
        privateKeyID = rsaKeyPairTemplate.getPrivateKeyTemplate().getKeyId();
        publicKeyID = rsaKeyPairTemplate.getPublicKeyTemplate().getKeyId();

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        // expecting ALWAYS_SENSITIVE to be true after creating the key with SENSITIVE true
        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, false)});
    }

    @Test
    public void setAlwaysSensitiveFalseToTrue() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate rsaKeyPairTemplate;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            rsaKeyPairTemplate = new RSAKeyPairTemplate(keyLabel, spec);
            rsaKeyPairTemplate.getAsTokenTemplate(true, false, false);
            rsaKeyPairTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false));
        }

        // create key pair
        sc.createKeyPair(sid, rsaKeyPairTemplate);
        privateKeyID = rsaKeyPairTemplate.getPrivateKeyTemplate().getKeyId();
        publicKeyID = rsaKeyPairTemplate.getPublicKeyTemplate().getKeyId();

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        // expecting ALWAYS_SENSITIVE to be false after creating the key with SENSITIVE false
        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_ALWAYS_SENSITIVE, true)});
    }

    @Test
    public void setModifiableTrue() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE, true)});
    }

    @Test
    public void setModifiableFalse() throws PKCS11Exception, SmartCardException, IOException {
        long[] keyIDs = createPermissiveKeyPair("testKey");
        privateKeyID = keyIDs[0];
        publicKeyID = keyIDs[1];

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE, false)});
    }

    // assuming that by default the created key (pair) has the CKA_MODIFIABLE attribute set to true, therefore being able to be modified successfully.
    @Test
    public void createKeyThenModifyAttribute() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate template;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            template = new RSAKeyPairTemplate(keyLabel, spec);
            template.getAsTokenTemplate(true, false, false);
        }

        // create key pair
        sc.createKeyPair(sid, template);
        privateKeyID = template.getPrivateKeyTemplate().getKeyId();
        publicKeyID = template.getPublicKeyTemplate().getKeyId();

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, "modifiedLabel")});
    }

    @Test
    public void createKeyWithModifiableFalseThenModifyAttribute() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate template;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            template = new RSAKeyPairTemplate(keyLabel, spec);
            template.getAsTokenTemplate(true, false, false);
            template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE, false));
        }

        // create key pair
        sc.createKeyPair(sid, template);
        privateKeyID = template.getPrivateKeyTemplate().getKeyId();
        publicKeyID = template.getPublicKeyTemplate().getKeyId();

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_LABEL, "modifiedLabel")});
    }

    @Test
    public void createKeyWithModifiableTrueThenSetModifiableFalse() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate template;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            template = new RSAKeyPairTemplate(keyLabel, spec);
            template.getAsTokenTemplate(true, false, false);
            template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE, true));
        }

        // create key pair
        sc.createKeyPair(sid, template);
        privateKeyID = template.getPrivateKeyTemplate().getKeyId();
        publicKeyID = template.getPublicKeyTemplate().getKeyId();

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE, false)});
    }

    @Test
    public void createKeyWithModifiableTrueThenModifyAttribute() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate template;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            template = new RSAKeyPairTemplate(keyLabel, spec);
            template.getAsTokenTemplate(true, false, false);
            template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE, true));
        }

        // create key pair
        sc.createKeyPair(sid, template);
        privateKeyID = template.getPrivateKeyTemplate().getKeyId();
        publicKeyID = template.getPublicKeyTemplate().getKeyId();

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_ID, StringUtil.hexToByte("98765432ABCDEF10"))});
    }

    @Test
    public void createKeyWithModifiableFalseThenSetModifiableTrue() throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate template;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            template = new RSAKeyPairTemplate(keyLabel, spec);
            template.getAsTokenTemplate(true, false, false);
            template.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE, false));
        }

        // create key pair
        sc.createKeyPair(sid, template);
        privateKeyID = template.getPrivateKeyTemplate().getKeyId();
        publicKeyID = template.getPublicKeyTemplate().getKeyId();

        expectedExceptionRule.expect(PKCS11Exception.class);
        expectedExceptionRule.expectMessage("CKR_ATTRIBUTE_READ_ONLY");

        sc.getPKCS11().C_SetAttributeValue(sid, privateKeyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(PKCS11Constants.CKA_MODIFIABLE, true)});
    }

    // ---

    void setPermissiveAttributes(KeyTemplate template) {
        template.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_SENSITIVE, false));
        template.add(new CK_ATTRIBUTE(PKCS11Constants.CKA_EXTRACTABLE, true));
    }

    // use for setting sensitivity and extractability values to initially enabling/permissive values
    long[] createPermissiveKeyPair(String keyLabel) throws PKCS11Exception, SmartCardException, IOException {
        RSAKeyPairTemplate template;
        {
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
            template = new RSAKeyPairTemplate(keyLabel, spec);
            template.getAsTokenTemplate(true, false, false);

            setPermissiveAttributes(template.getPrivateKeyTemplate());
        }

        // create key pair
        sc.createKeyPair(sid, template);

        long[] ids = new long[2];
        // the key ID (handle, according to current implementation) is set in the template from the createKeyPair call above
        ids[0] = template.getPrivateKeyTemplate().getKeyId();
        ids[1] = template.getPublicKeyTemplate().getKeyId();
        return ids;
    }
}
