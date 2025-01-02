package tr.gov.tubitak.uekae.esya.api.crypto;

import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSCryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.nss.NSSLoader;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.HMACKeyTemplate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.File;
import java.security.Provider;
import java.util.Arrays;

/**
 * @author ayetgin
 */
public class NSSMacTest extends TestCase
{
    private static String TEST_DATA =  "Hey, privacy is not a perfume!";
    private static NSSCryptoProvider NSS_PROVIDER;

    static {
        NSS_PROVIDER = NSSTestUtil.constructProvider(null);
    }

   public void testMACing() throws CryptoException {
       Crypto.setProvider(NSS_PROVIDER);
        KeyFactory kf = Crypto.getKeyFactory();
        tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec secretKeySpec = new tr.gov.tubitak.uekae.esya.api.crypto.params.SecretKeySpec(CipherAlg.AES256_CBC,"TabloImzalama",256);

        //SecretKey tabloImzalama = kf.generateSecretKey(CipherAlg.AES256_CBC,256)   ;
        SecretKey tabloImzalama = kf.generateSecretKey(secretKeySpec)   ;
        System.out.print("basarılı");


        HMACKeyTemplate keyTemplate = new HMACKeyTemplate("TabloImzalama",256,"SHA256");

        MAC mac = Crypto.getMAC(MACAlg.HMAC_SHA256);
        mac.init(keyTemplate,null);
        //Mac mac = Mac.getInstance("HMACSHA256", p);
        //mac.init(ske.getSecretKey());

        byte[] value = mac.doFinal("test".getBytes());
        System.out.println(Arrays.toString(value));



    }


}
