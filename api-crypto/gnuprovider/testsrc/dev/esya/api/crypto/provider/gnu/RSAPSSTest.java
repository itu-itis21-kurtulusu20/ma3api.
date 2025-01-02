package tr.gov.tubitak.uekae.esya.api.crypto.sig;

import junit.framework.TestCase;
import org.junit.Ignore;
import tr.gov.tubitak.uekae.esya.api.common.crypto.Algorithms;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Signer;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MGF;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

//import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
//import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
//import tr.gov.tubitak.uekae.esya.api.smartcard.util.SCSignerWithKeyLabel;

/**
 * @author ayetgin
 */
@Ignore("Development tests")
public class RSAPSSTest extends TestCase
{
    //    private static PublicKey publicKey;
//
//    private static BaseSigner createSignerKarttan() throws IOException, CryptoException
//    {
//
////     // Signer for Registrar whom CA can trust (Kayıtçı) to sign PKI Requests (Certification requests for both CVC and X509)
//
//       SCSignerWithKeyLabel signer = null;
//       SmartCard sc;
//       long sessionID;
//        try {
//           sc = new SmartCard(CardType.UTIMACO);
//
//         //  long slot =SmartOp.findSlotNumber(CardType.UTIMACO);
//           long slot = 61;
//           sessionID = sc.openSession(slot);
//           sc.login(sessionID, "123456");
//
//           String anahAdi = "yetkili_imza_kayitci2";
//           String signAlg = SignatureAlg.RSA_PSS.getName();
//           signer = new SCSignerWithKeyLabel(sc, sessionID, slot, anahAdi, signAlg,new PSSParameterSpec(10));
//
//            KeySpec spec  = sc.readPublicKeySpec(sessionID, anahAdi);
//            publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);
//            //certificate = new ECertificate(spec));
//            System.out.println(spec);
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//
//        return signer;
//
//
//    }
//
//    public void test() throws Exception {
//        BaseSigner signer = createSignerKarttan();
//        byte[] signed = signer.sign("12345678".getBytes());
//
//        AlgorithmParameterSpec alg =  signer.getAlgorithmParameterSpec();
//        ERSASSA_PSS_params params = new ERSASSA_PSS_params((PSSParameterSpec)alg);
//        EAlgorithmIdentifier id = new EAlgorithmIdentifier(SignatureAlg.RSA_PSS.getOID(), params.getEncoded());
//
//        Pair<SignatureAlg, AlgorithmParams> pair = SignatureAlg.fromAlgorithmIdentifier(id);
//        System.out.println(pair.getObject1());
//        System.out.println(pair.getObject2());
//
//        boolean verified = SignUtil.verify(pair.getObject1(), pair.getObject2(), "12345678".getBytes(), signed, publicKey);
//        System.out.println(verified);
//
//    }
    public void testPSSWithPrivateKey() throws CryptoException, IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

        /*String pfxPath = "C:\\tmp\\ramazan.girgin_327147@ug.net.pfx";
        PfxParser pfxParser = new PfxParser(new FileInputStream(pfxPath),"327147".toCharArray());
        List<Pair<ECertificate, PrivateKey>> certificatesAndKeys = pfxParser.getCertificatesAndKeys();
        for (Pair<ECertificate, PrivateKey> certificatesAndKey : certificatesAndKeys) {
            PrivateKey privateKey = certificatesAndKey.getObject2();
            Signer signer = Crypto.getSigner(SignatureAlg.RSA_PSS);
            RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256, MGF.fromName(Algorithms.DIGEST_SHA256), 32, 0);
            signer.init(privateKey, rsapssParams);
            byte[] signed = signer.sign("test".getBytes());
        } */

        String keyStorePath = "G:\\Config\\WSTESTCLIENT_LOCAL\\webservice.jks";
        KeyStore ks = KeyStore.getInstance("jks");
        String password="123456";
        ks.load(new FileInputStream(keyStorePath), password.toCharArray());
        PrivateKey privateKey  =  (PrivateKey) ks.getKey("webservice",password.toCharArray());
        Signer signer = Crypto.getSigner(SignatureAlg.RSA_PSS);
        RSAPSSParams rsapssParams = new RSAPSSParams(DigestAlg.SHA256, MGF.fromName(Algorithms.DIGEST_SHA256), 32, 0);
        signer.init(privateKey, rsapssParams);

        byte[] signed = signer.sign("test".getBytes());
    }
}
