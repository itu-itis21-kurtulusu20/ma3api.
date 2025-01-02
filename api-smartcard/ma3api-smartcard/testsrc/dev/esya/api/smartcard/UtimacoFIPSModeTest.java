package dev.esya.api.smartcard;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.PKCS11ConstantsExtended;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme.RSAPSS_SS;

import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;

public class UtimacoFIPSModeTest {

    static final String PASSWORD = "123456";

    static SmartCard sc = null;
    static long sid = 0;
    static long slotNo = 0;


    @Before
    public void setUpClass() throws Exception
    {
        sc = new SmartCard(CardType.UTIMACO_R2);
        slotNo = sc.getSlotList()[5];
        sid = sc.openSession(slotNo);
        sc.login(sid, PASSWORD);
    }


    @After
    public void cleanUp()  throws Exception
    {
        sc.logout(sid);
        sc.closeSession(sid);
    }


    @Test
    public void testGeneratePKCSKey()throws  Exception{

        //Normalde long array olması lazım. Yalnız pkcs11 jni desteklemediği için byte array kullanıldı.
        //CKM_SHA256_RSA_PKCS_PSS, CKM_SHA512_RSA_PKCS_PSS
        byte [] mech = new byte []{67,0,0,0,69,0,0,0};

        boolean sign = true;
        boolean enc = false;

        String rsaKeyLabel = "rsa_pss";
        RSAKeyPairTemplate destRSAUnwrapperKeyTemplate = new RSAKeyPairTemplate(rsaKeyLabel, new RSAKeyGenParameterSpec(2048, null));
        destRSAUnwrapperKeyTemplate = destRSAUnwrapperKeyTemplate.getAsTokenTemplate(sign, enc, false);

        destRSAUnwrapperKeyTemplate.getPrivateKeyTemplate().add(new CK_ATTRIBUTE(PKCS11ConstantsExtended.CKA_ALLOWED_MECHANISMS, mech));

        KeySpec publicKeySpec = sc.createKeyPair(sid, destRSAUnwrapperKeyTemplate);
        PublicKey publicKey = KeyUtil.generatePublicKey(publicKeySpec);
    }



    @Test
    public void testPSSSign()throws  Exception{
        String keyLabel = "rsa_pss";

        byte [] data = new byte[32];
        RandomUtil.generateRandom(data);

        CK_MECHANISM mech =  RSAPSS_SS.getDefaultMechanismForPSS(DigestAlg.SHA256);

        byte[] signedData = sc.signData(sid, keyLabel, data, mech);
    }



}
