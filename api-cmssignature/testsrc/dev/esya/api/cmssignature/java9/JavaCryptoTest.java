package dev.esya.api.cmssignature.java9;

import gnu.crypto.key.rsa.GnuRSAPublicKey;
import org.junit.Assert;
import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class JavaCryptoTest
{
    @Test
    public void decodeEncodedPublicKey() throws Exception
    {
        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        byte[] encoded = keyPair.getPublic().getEncoded();

        X509EncodedKeySpec rsaPublicKeySpec = new X509EncodedKeySpec(encoded);

        KeyFactory instance = KeyFactory.getInstance("RSA", "SunRsaSign");
        RSAPublicKey publicKey = (RSAPublicKey) instance.generatePublic(rsaPublicKeySpec);


        Assert.assertEquals(publicKey.getClass().getName(), "sun.security.rsa.RSAPublicKeyImpl");
    }


    @Test
    public void decodeRSAPrivateCrtKeyImpl() throws Exception
    {
        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);
        byte[] encoded = keyPair.getPrivate().getEncoded();


        PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(encoded);

        KeyFactory instance = KeyFactory.getInstance("RSA", "SunRsaSign");
        PrivateKey privateKey1 =  instance.generatePrivate(encodedKeySpec);
        RSAPrivateCrtKeySpec jcaPrivKey = instance.getKeySpec(privateKey1, RSAPrivateCrtKeySpec.class);


        RSAPrivateCrtKeyImpl privKey = (RSAPrivateCrtKeyImpl)RSAPrivateCrtKeyImpl.newKey(encoded);

        Assert.assertEquals(jcaPrivKey.getModulus(), privKey.getModulus());
        Assert.assertEquals(jcaPrivKey.getPublicExponent(), privKey.getPublicExponent());
        Assert.assertEquals(jcaPrivKey.getPrivateExponent(), privKey.getPrivateExponent());
        Assert.assertEquals(jcaPrivKey.getPrimeP(), privKey.getPrimeP());
        Assert.assertEquals(jcaPrivKey.getPrimeQ(), privKey.getPrimeQ());
        Assert.assertEquals(jcaPrivKey.getPrimeExponentP(), privKey.getPrimeExponentP());
        Assert.assertEquals(jcaPrivKey.getPrimeExponentQ(), privKey.getPrimeExponentQ());
        Assert.assertEquals(jcaPrivKey.getCrtCoefficient(), privKey.getCrtCoefficient());
    }

    @Test
    public void base64Test() throws Exception
    {
        byte[] bytes = RandomUtil.generateRandom(64);
        String encoded = Base64.encode(bytes);

        BASE64Decoder base64decoder = new BASE64Decoder();
        byte[] decoded = base64decoder.decodeBuffer(encoded);

        Assert.assertArrayEquals(bytes, decoded);
    }

    @Test
    public void testCreatePublicKeyFromModulusAndExponent() throws Exception
    {
        KeyPair keyPair = KeyUtil.generateKeyPair(AsymmetricAlg.RSA, 2048);

        GnuRSAPublicKey gnuRSAPublicKey = (GnuRSAPublicKey) keyPair.getPublic();

        BigInteger modulus = gnuRSAPublicKey.getModulus();
        BigInteger publicExponent = gnuRSAPublicKey.getPublicExponent();

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA", "SunRsaSign").generatePublic(rsaPublicKeySpec);

        byte [] data = RandomUtil.generateRandom(64);
        byte[] signature = SignUtil.sign(SignatureAlg.RSA_SHA256, data, keyPair.getPrivate());

        boolean verify = SignUtil.verify(SignatureAlg.RSA_SHA256, data, signature, rsaPublicKey);

        Assert.assertTrue(verify);
    }

    @Test
    public void pfxToRSAPrivateCrtKeySpec() throws Exception
    {
        PfxParser parser = new PfxParser(new FileInputStream("T:\\api-parent\\resources\\ug\\pfx\\test-suite\\QCA1_1.p12"), "123456");

        PrivateKey privateKey = parser.getFirstPrivateKey();

        KeyFactory instance = KeyFactory.getInstance("RSA", "SunRsaSign");
        RSAPrivateCrtKeySpec aPrivKey = instance.getKeySpec(privateKey, RSAPrivateCrtKeySpec.class);


        Assert.assertTrue(aPrivKey.getPrivateExponent().bitCount() > 1000);
    }

}
