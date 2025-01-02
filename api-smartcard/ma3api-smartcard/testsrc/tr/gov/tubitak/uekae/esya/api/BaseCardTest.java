package tr.gov.tubitak.uekae.esya.api;

import gnu.crypto.key.ecdsa.ECDSAPrivateKey;
import gnu.crypto.key.ecdsa.ECDSAPublicKey;
import org.junit.*;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartOp;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.NamedCurve;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.KeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.ec.ECPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPrivateKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import util.CustomCurve;
import util.CustomECDSAPrivateKey;
import util.CustomECDSAPublicKey;
import util.EcAsHexHolder;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ramazan.girgin on 12/8/2015.
 */
public class BaseCardTest {
    protected static SmartCard sc = null;
    protected static long sessionId = -1;
    static long slotNo = 2;
    static CardType cardType = CardType.AKIS;
    static String slotPin = "12345";
    static SmartCardHelper smartCardHelper;
    List<String> deleteLabelList=new ArrayList<String>();


    @BeforeClass
    public static void setUp() throws Exception {
        if (smartCardHelper == null) {
            smartCardHelper = new SmartCardHelper().login();
            sc = smartCardHelper.getSc();
            sessionId = smartCardHelper.getSessionId();
            slotNo = smartCardHelper.getSlotNo();
        }
    }

    @AfterClass
    public static void cleanUp() {
        if (sessionId != -1 && sc != null) {
            try {
                sc.closeSession(sessionId);
                System.out.println("Kartta oturum kapatıldı");
            } catch (PKCS11Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Before
    public void testMethodSetUp(){
        System.out.println("******************* TEST  BASLANGICI ****************");
    }

    @After
    public void testMethodCleanUp(){
        for (String objectLabel : deleteLabelList) {
            smartCardHelper.deleteAllObjectWithLabel(objectLabel);
        }
        System.out.println("-------------------------- TEST  SONU ----------------------");
    }
    private static class SmartCardHelper {
        private SmartCard sc;
        private long sessionId;

        public SmartCard getSc() {
            return sc;
        }

        public long getSlotNo() {
            return slotNo;
        }

        public long getSessionId() {
            return sessionId;
        }

        public SmartCardHelper login() throws PKCS11Exception, IOException {
            sc = new SmartCard(cardType);
            try {
                sessionId = sc.openSession(slotNo);
                System.out.println(slotNo+" numaralı slotta oturum açıldı.");
                sc.login(sessionId, slotPin);
                System.out.println(slotNo + " numaralı slota login olundu..");
            } catch (PKCS11Exception exc) {
                if (exc.getErrorCode() != PKCS11Constants.CKR_USER_ALREADY_LOGGED_IN) {
                    throw exc;
                }
            }
            return this;
        }

        public void deleteAllObjectWithLabel(String objectLabel){
            try {
                sc.deletePublicObject(sessionId,objectLabel);
                System.out.println(objectLabel+" etiketli public obje karttan silindi");
            } catch (Exception e) {
            }
            try {
                sc.deletePrivateObject(sessionId, objectLabel);
                System.out.println(objectLabel + " etiketli private obje karttan silindi");
            } catch (Exception e) {
            }
            try {
                int deletedCount = sc.deleteCertificate(sessionId, objectLabel);
                if(deletedCount>0)
                    System.out.println(objectLabel + " etiketli sertifika karttan silindi");

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sc.deletePrivateData(sessionId, objectLabel);
                System.out.println(objectLabel + " etiketli private data karttan silindi");

            } catch (Exception e) {
            }
            try {
                sc.deletePublicData(sessionId, objectLabel);
                System.out.println(objectLabel + " etiketli public data karttan silindi");

            } catch (Exception e) {
            }
        }
    }
}
