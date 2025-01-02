package test.esya.api.crypto.provider.gnu;

import gnu.crypto.Registry;
import gnu.crypto.sig.rsa.RSAPKCS1V1_5Signature;
import gnu.crypto.util.Base64;
import junit.framework.TestCase;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.PfxParser;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RSAPKCS1V1_5SignatureTest extends TestCase
{
	public void testRSAwithNone() throws Exception
	{
		PfxParser pfxParser = new PfxParser(new ByteArrayInputStream(Base64.decode(PFXs.GOK_UG_852175)), PFXs.GOK_UG_PASSWORD.toCharArray());
		
		PrivateKey privKey = pfxParser.getCertificatesAndKeys().get(0).getObject2();
		PublicKey pubKey = pfxParser.getCertificatesAndKeys().get(0).getObject1().asX509Certificate().getPublicKey();
//		ECertificate cert = pfxParser.getCertificatesAndKeys().get(0).getObject1();
		
		
		byte [] data  = new byte [117];
		new Random().nextBytes(data);
		
		//SunJCE provider
		Signature sig = Signature.getInstance("noneWithRSA");
		Provider noneProvider = sig.getProvider();
		sig.initSign(privKey);
		sig.update(data);
		byte [] signature = sig.sign();
		
		
		
		RSAPKCS1V1_5Signature gnuSig = new  RSAPKCS1V1_5Signature(Registry.NONE_HASH);
		Map attributes = new HashMap();
		attributes.put(RSAPKCS1V1_5Signature.SIGNER_KEY, privKey);
		gnuSig.setupSign(attributes);
		gnuSig.update(data,0,data.length);
		byte [] signature2  = (byte[]) gnuSig.sign();
		
		assertTrue(Arrays.equals(signature, signature2));
		
		//verify
		{
			sig = Signature.getInstance("noneWithRSA");
			sig.initVerify(pubKey);
			sig.update(data);
			boolean verified = sig.verify(signature2);
			
			assertEquals(true, verified);
			
			gnuSig = new  RSAPKCS1V1_5Signature(Registry.NONE_HASH);
			attributes = new HashMap();
			attributes.put(RSAPKCS1V1_5Signature.VERIFIER_KEY, pubKey);
			gnuSig.setupVerify(attributes);
			gnuSig.update(data,0,data.length);
			boolean verified2   = gnuSig.verify(signature);
			assertTrue(verified2);
		}
	}

    public void testRSAwithNone2() throws Exception
    {
        PfxParser pfxParser = new PfxParser(new ByteArrayInputStream(Base64.decode(PFXs.GOK_UG_852175)), PFXs.GOK_UG_PASSWORD.toCharArray());

        PrivateKey privKey = pfxParser.getCertificatesAndKeys().get(0).getObject2();
        PublicKey pubKey = pfxParser.getCertificatesAndKeys().get(0).getObject1().asX509Certificate().getPublicKey();

        byte [] data  = RandomUtil.generateRandom(117);

        //SunJCE provider
        Signature sig = Signature.getInstance("noneWithRSA");
        sig.initSign(privKey);
        sig.update(data);
        byte [] signatureSun = sig.sign();


        byte[] signatureGnu = SignUtil.sign(SignatureAlg.RSA_NONE, data, privKey);

        assertTrue(Arrays.equals(signatureSun, signatureGnu));

        boolean verified = SignUtil.verify(SignatureAlg.RSA_NONE, data, signatureSun, pubKey);
        assertTrue(verified);
    }
}
