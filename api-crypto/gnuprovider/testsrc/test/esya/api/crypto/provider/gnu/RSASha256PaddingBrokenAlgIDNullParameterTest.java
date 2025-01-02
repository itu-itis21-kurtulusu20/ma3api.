package test.esya.api.crypto.provider.gnu;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

import static junit.framework.TestCase.assertTrue;

/**
 * @author ayetgin
 */
public class RSASha256PaddingBrokenAlgIDNullParameterTest {

    private static final String CERT_B64 = "MIIGhTCCBW2gAwIBAgIQWeOypuj6hZgJTA06Pyzb1TANBgkqhkiG9w0BAQUFADB7MQswCQYDVQQG\n" +
            "EwJUUjEoMCYGA1UEChMfRWxla3Ryb25payBCaWxnaSBHdXZlbmxpZ2kgQS5TLjFCMEAGA1UEAxM5\n" +
            "ZS1HdXZlbiBOaXRlbGlrbGkgRWxla3Ryb25payBTZXJ0aWZpa2EgSGl6bWV0IFNhZ2xheWljaXNp\n" +
            "MB4XDTEyMTAxMTA4MjYwNFoXDTEzMTAxMTA4MjYwNFowgZgxKzApBgNVBAoMIkVDWkFDSUJBxZ5J\n" +
            "IFTEsENBUkVUIFZFIFNBTi4gQS7Fni4xLTArBgkqhkiG9w0BCQEWHm11cmF0LmdpcmdpbkBlY3ph\n" +
            "Y2liYXNpLmNvbS50cjELMAkGA1UEBhMCVFIxFDASBgNVBAUTCzMxOTMzNDI1Njg0MRcwFQYDVQQD\n" +
            "DA5NVVJBVCBHxLBSR8SwTjCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAkHE/AhlTBIzvSpD4\n" +
            "NhmurdNpKk3vvBctmFpkAnHYIXaY5qCST+dXx/RaErtmU9u0l8xpUU6G4fauNlKE96t76oc5DOLK\n" +
            "QOY9HQW33mQonynf7j3KkLfGWSU3OKx0VcI5v3j1OAzlEBf+MQ633F7vipTZdUVLyyVaYqbf0G3/\n" +
            "WkkCAwEAAaOCA2kwggNlMA8GA1UdDwEB/wQFAwMHwAAwgYMGCCsGAQUFBwEDBHcwdTAIBgYEAI5G\n" +
            "AQEwaQYLYIYYAT0AAadOAQEMWkJ1IHNlcnRpZmlrYSwgNTA3MCBzYXlpbGkgRWxla3Ryb25payBJ\n" +
            "bXphIEthbnVudW5hIGfDtnJlIG5pdGVsaWtsaSBlbGVrdHJvbmlrIHNlcnRpZmlrYWRpcjApBgNV\n" +
            "HREEIjAggR5tdXJhdC5naXJnaW5AZWN6YWNpYmFzaS5jb20udHIwUwYDVR0JBEwwSjAQBggrBgEF\n" +
            "BQcJBDEEEwJUQzAdBggrBgEFBQcJATERGA8xOTc1MDYyODEyMDAwMFowFwYIKwYBBQUHCQIxCwwJ\n" +
            "xLBTVEFOQlVMMD4GCCsGAQUFBwEBBDIwMDAuBggrBgEFBQcwAYYiaHR0cDovL29jc3AyLmUtZ3V2\n" +
            "ZW4uY29tL29jc3AueHVkYTAfBgNVHSMEGDAWgBTjeK1r28C8UYceSBd+XsObvqD36zCCAXIGA1Ud\n" +
            "IASCAWkwggFlMIGxBgZghhgDAAEwgaYwNgYIKwYBBQUHAgEWKmh0dHA6Ly93d3cuZS1ndXZlbi5j\n" +
            "b20vZG9jdW1lbnRzL05FU1VFLnBkZjBsBggrBgEFBQcCAjBgGl5CdSBzZXJ0aWZpa2EsIDUwNzAg\n" +
            "c2F5xLFsxLEgRWxla3Ryb25payDEsG16YSBLYW51bnVuYSBnw7ZyZSBuaXRlbGlrbGkgZWxla3Ry\n" +
            "b25payBzZXJ0aWZpa2FkxLFyMIGuBglghhgDAAEBAQIwgaAwNwYIKwYBBQUHAgEWK2h0dHA6Ly93\n" +
            "d3cuZS1ndXZlbi5jb20vZG9jdW1lbnRzL0dLTkVTSS5wZGYwZQYIKwYBBQUHAgIwWRpXQnUgc2Vy\n" +
            "dGlmaWthLCBHS05FU0kga2Fwc2FtxLFuZGEgeWF5xLFubGFubcSxxZ8gYmlyIG5pdGVsaWtsaSBl\n" +
            "bGVrdHJvbmlrIHNlcnRpZmlrYWTEsXIuMFYGA1UdHwRPME0wS6BJoEeGRWh0dHA6Ly9zaWwuZS1n\n" +
            "dXZlbi5jb20vRWxla3Ryb25pa0JpbGdpR3V2ZW5saWdpQVNHS05FU0kvTGF0ZXN0Q1JMLmNybDAd\n" +
            "BgNVHQ4EFgQUygPE5YnzsGBmCh55tMCYOc2kymQwDQYJKoZIhvcNAQEFBQADggEBAH1lz4sN78iO\n" +
            "aLDF/XwNCMigGQc74UzFizphMdIMx16VG+BZTC3nQxwbfkQLI95GGHdYmn1KcYBhTpjVU4NPtR1N\n" +
            "zzX3nYgHeGZ7+UhuYfIOb1lA7jbCf35EaASQuRlp3KaMksK8zMJhXgpBHXm4qAaInebmJvmsEQjY\n" +
            "L+lZ42WK0Xnrx9WZmYQDeMxOLRZ7wm8EXn1QACqO/DTSX8db1JVRUYLZ2f+gAbx+8HXLa391Ndzs\n" +
            "AuAdu/GSfY6edqob5ZVybnY8Ujsy1vKCNHD5Ija0obByWRyi1GZBENnDAIkt7z2c0sIikrtJv401\n" +
            "oo2p5Bp7xD+zJ8Qn1cJ3xLma0Fc=";

    private static final String TO_BE_SIGNED_B64 = "MYIBTDAYBgkqhkiG9w0BCQMxCwYJKoZIhvcNAQcBMBwGCSqGSIb3DQEJBTEPFw0xMjEwMTkwOTQ4\n" +
            "NThaMC8GCSqGSIb3DQEJBDEiBCDbUoCMZQDVVg0pXSTfWb81aLtP076jcgkEeaBtfFGWJjCB4AYL\n" +
            "KoZIhvcNAQkQAi8xgdAwgc0wgcowgccwDQYJYIZIAWUDBAIBBQAEIGR6aFGIqa+IBctO9yix4Uxl\n" +
            "hBeZwmwhEha7hGt82aXgMIGTMH+kfTB7MQswCQYDVQQGEwJUUjEoMCYGA1UEChMfRWxla3Ryb25p\n" +
            "ayBCaWxnaSBHdXZlbmxpZ2kgQS5TLjFCMEAGA1UEAxM5ZS1HdXZlbiBOaXRlbGlrbGkgRWxla3Ry\n" +
            "b25payBTZXJ0aWZpa2EgSGl6bWV0IFNhZ2xheWljaXNpAhBZ47Km6PqFmAlMDTo/LNvV";

    private static final String SIGNATURE_B64 = "Mc5266ucOOrOJIV+uXWVt2xN9avI5Bs/cqOcE2Vrj8QLeDTgpu+B1hgoT6ygzXivrxA+BhCkjuTx\n" +
            "dgZZQVUiEUhvep/m1yWsJFh/0HLDPiRDWJ+92PzQR5r3Ad8XYkih+DFlyXlifUEuocGIL5MKRWO5\n" +
            "8Uw4E9e47vrCLVTAbS4=";


    ECertificate cert;
    byte[] tobesigned;
    byte[] signature;


    @Before
    public void setUp() throws Exception
    {
        try {
            cert = new ECertificate(Base64.decode(CERT_B64));
            tobesigned = Base64.decode(TO_BE_SIGNED_B64);
            signature = Base64.decode(SIGNATURE_B64);
        }
        catch(Exception x){
            x.printStackTrace();
        }

    }

    @AfterClass
    public static void afterClass() throws CryptoException {
        System.out.println("RSASha256PaddingBrokenAlgIDNullParameterTest - afterClass");
        Crypto.setProvider(Crypto.PROVIDER_GNU);
    }

    @Test
    public void testSun() throws Exception {
        Crypto.setProvider(Crypto.PROVIDER_SUN);
        boolean valid = SignUtil.verify(SignatureAlg.RSA_SHA256, null, tobesigned, signature, cert);
        System.out.println("Sun Valid ? "+valid);
        assertTrue("SUN", valid);
    }

    @Test
    public void testGNU() throws Exception {
        Crypto.setProvider(Crypto.PROVIDER_GNU);
        boolean valid = SignUtil.verify(SignatureAlg.RSA_SHA256, null, tobesigned, signature, cert);
        System.out.println("GNU Valid ? "+valid);
        assertTrue("GNU", valid);
    }

    /** requires openjdk source
    public void testOpenJDK7() throws Exception {
        Crypto.setProvider(Crypto.PROVIDER_SUN);

        RSASignatureImpl sig = new RSASignatureImpl();
        PublicKey pk = KeyUtil.decodePublicKey(cert.getSubjectPublicKeyInfo());
        sig.engineInitVerify(pk);
        sig.engineUpdate(tobesigned, 0, tobesigned.length);
        boolean valid = sig.engineVerify(signature);

        System.out.println("OpenJDK Valid ? "+valid);
        assertTrue("OpenJDK", valid);
    }

    class RSASignatureImpl extends RSASignature.SHA256withRSA {
        @Override
        public void engineInitVerify(PublicKey publicKey) throws InvalidKeyException
        {
            super.engineInitVerify(publicKey);
        }

        @Override
        public void engineUpdate(byte b) throws SignatureException
        {
            super.engineUpdate(b);
        }

        @Override
        protected void engineUpdate(byte[] b, int off, int len) throws SignatureException
        {
            super.engineUpdate(b, off, len);
        }

        @Override
        public boolean engineVerify(byte[] sigBytes) throws SignatureException
        {
            return super.engineVerify(sigBytes);
        }
    }
     */
}
