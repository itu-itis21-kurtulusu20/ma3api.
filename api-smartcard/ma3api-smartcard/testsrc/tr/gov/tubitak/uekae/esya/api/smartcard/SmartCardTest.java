package tr.gov.tubitak.uekae.esya.api.smartcard;



import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.rsa.RSAPublicKeyImpl;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.DigestUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCardException;
import tr.gov.tubitak.uekae.esya.asn.util.UtilName;

import com.objsys.asn1j.runtime.Asn1Exception;

public class SmartCardTest extends TestCase
{
	static SmartCard sc = null;
	
	static PublicKey mpubKey;
	static RSAPrivateCrtKey mprivKey;
	static byte [] mSignedData;
	static byte [] mEncryptedData;
	
	final String objectLabel = "SCTest";
	String mPassword = "12345";
	final String ayseCert = 	"-----BEGIN CERTIFICATE-----" +
						"MIIFcTCCBNqgAwIBAgIBKjANBgkqhkiG9w0BAQUFADBvMRMwEQYKCZImiZPyLGQB" +
						"GRYDbmV0MRYwFAYKCZImiZPyLGQBGRYGZGVuZW1lMQswCQYDVQQGEwJ0cjEPMA0G" +
						"A1UECgwGc2VyZGFyMQswCQYDVQQFEwI0MzEVMBMGA1UEAwwMMTRfRVlMw5xMX1NN" +
						"MB4XDTA3MDkxNDEyNTIxNloXDTEwMDYxMDEyNTIxNlowOzELMAkGA1UEBhMCVFIx" +
						"FDASBgNVBAUTCzEyMzQ1Njc4OTIzMRYwFAYDVQQDDA1hecWfZSBzYcSfbGFtMIIB" +
						"IjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjwuXCMAgq6uJiICP0g5vlsbJ" +
						"2XGDfE6wG5HgxyU385ThMjs3QRH1GXOPbVue7spNyx23kK9FyiCFw0GQLpf0mx9h" +
						"DANexL1gh1S9azFrM/L6B1cyZZYLxOlpJkLWBWGP/5L/h/fEPvTN13Bwn4aysllA" +
						"uj7d8IMSNJHiTbI6gmP6gMmGtDeoCeEiAMBNfIzi5XcrMVsxWOundI8lTZ8F2BnT" +
						"OWZLrNC/DZpPC1kndOEN6XaxKkvQi3ux7Q0XJ/K8EkRm1AFlwuL4Zw1lj0pGpLDN" +
						"OndJ2KOUjPEfHjMc/9T2ClMoopDzDPizaEsRQFWljXlzIUCf7JILpbMsnO21QQID" +
						"AQABo4ICyzCCAscwHwYDVR0jBBgwFoAURc3mnrpWSJ0IKUrEVjQDTU4W03QwHQYD" +
						"VR0OBBYEFGJBg20EgtTfdXSN3TRRQD17pU8wMA4GA1UdDwEB/wQEAwIGwDCCATQG" +
						"A1UdIASCASswggEnMIIBIwYLYIYYAQIBAQUHAQEwggESMC8GCCsGAQUFBwIBFiNo" +
						"dHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNX1NVRTCB3gYIKwYBBQUHAgIw" +
						"gdEegc4AQgB1ACAAcwBlAHIAdABpAGYAaQBrAGEALAAgAC4ALgAuAC4AIABzAGEA" +
						"eQExAGwBMQAgAEUAbABlAGsAdAByAG8AbgBpAGsAIAEwAG0AegBhACAASwBhAG4A" +
						"dQBuAHUAbgBhACAAZwD2AHIAZQAgAG8AbAB1AV8AdAB1AHIAdQBsAG0AdQFfACAA" +
						"bgBpAHQAZQBsAGkAawBsAGkAIABlAGwAZQBrAHQAcgBvAG4AaQBrACAAcwBlAHIA" +
						"dABpAGYAaQBrAGEAZAExAHIALjB0BgNVHR8EbTBrMCygKqAohiZodHRwOi8vd3d3" +
						"LnRlc3RzbS5uZXQudHIvVEVTVFNNU0lMLmNybDA7oDmgN4Y1bGRhcDovL2Rpemlu" +
						"LnRlc3RzbS5nb3YudHIvQz1UUixPPVRFU1RTTSxDTj1URVNUU01TSUwwgakGCCsG" +
						"AQUFBwEBBIGcMIGZMC8GCCsGAQUFBzAChiNodHRwOi8vd3d3LnRlc3RzbS5uZXQu" +
						"dHIvVEVTVFNNLmNydDA+BggrBgEFBQcwAoYybGRhcDovL2RpemluLnRlc3RzbS5u" +
						"ZXQudHIvQz1UUixPPVRFU1RTTSxDTj1URVNUU00wJgYIKwYBBQUHMAGGGmh0dHA6" +
						"Ly9vY3NwLnRlc3RzbS5uZXQudHIvMBsGCCsGAQUFBwEDAQH/BAwwCjAIBgYEAI5G" +
						"AQEwDQYJKoZIhvcNAQEFBQADgYEAg8P/xT80GBT3q+6HAb+UVXgfY4gzo6hfGQ4a" +
						"vUjpq8KA5WztP2x3xkreuN4bim64xjrO8QsSszSYwRBcyFqsyOPrZsPyaLxHY3AE" +
						"GklBmA1/9YuO6QHwAXOrmqfrdr8QwIm+4y+/GaPYEFVlwh6X3jYyU/4sV5LBfkv2" +
						"d8e4l1A=" +
						"-----END CERTIFICATE-----";
	
	static
	{
		try
		{
			sc = new SmartCard(CardType.AKIS);
		}
		catch(Exception aEx)
		{
			
		}
	}
	
	
	public void testCreateRSAKeyPair()
	throws PKCS11Exception, SmartCardException,IOException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		
		sc.login(sessionID, mPassword);
		RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, null);
		sc.createKeyPair(sessionID, objectLabel, spec, true, true);
		//sc.createRSAKeyPair(sessionID, objectLabel, 1024, true, true);
		boolean sonuc1 = sc.isObjectExist(sessionID, objectLabel);
		sc.logout(sessionID);
		boolean sonuc2 = sc.isObjectExist(sessionID, objectLabel);
		sc.closeSession(sessionID);
		assertEquals(true, sonuc1 && sonuc2);
	}
	
	
	public void testImportCertificate()
	throws PKCS11Exception,IOException,Asn1Exception, SmartCardException, ESYAException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		
		ECertificate eCert = new ECertificate(ayseCert.getBytes("ASCII"));
		
		sc.login(sessionID, mPassword);
		sc.importCertificate(sessionID, objectLabel, eCert.asX509Certificate());
		List<byte[]> certBytes = sc.readCertificate(sessionID, objectLabel);
		sc.logout(sessionID);
		sc.closeSession(sessionID);

		assertEquals(true, Arrays.equals(certBytes.get(0), eCert.getEncoded()));
	}
	
	
	public void testDeleteObject()
	throws PKCS11Exception,CryptoException, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		sc.deletePrivateObject(sessionID, objectLabel);
		sc.deletePublicObject(sessionID, objectLabel);
		boolean sonuc = sc.isAnyObjectExist(sessionID);
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		assertEquals(false,sonuc);
	}
	
	public void testImportCertificateWithKey()
	throws PKCS11Exception,IOException,Asn1Exception,NoSuchAlgorithmException, SmartCardException, ESYAException
	{
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		KeyPair kp = kpg.generateKeyPair();
		
		mprivKey = (RSAPrivateCrtKey) kp.getPrivate();
		mpubKey = (RSAPublicKey) kp.getPublic();
		
		ECertificate eCert = new ECertificate(ayseCert.getBytes("ASCII"));
		
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		sc.importCertificateAndKey(sessionID, objectLabel,objectLabel, mprivKey, eCert.asX509Certificate());
		sc.isObjectExist(sessionID,objectLabel);
		sc.logout(sessionID);
		
		sc.readPublicKeySpec(sessionID,objectLabel);
		sc.readCertificate(sessionID, objectLabel);
		sc.closeSession(sessionID);
		assertEquals(true, true);
	}
	
	
	public void testGetRandomData()
	throws PKCS11Exception
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		byte[] random = sc.getRandomData(sessionID, 32);
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		boolean sonuc = true;
		if(random == null || random.length == 0)
			sonuc = false;
		assertEquals(true, sonuc);
	}
	
	public void testSignData()
	throws PKCS11Exception,IOException,CryptoException, SmartCardException
	{
		byte[] imzalanacak = DigestUtil.digest( DigestAlg.SHA1, "aslihan".getBytes());
		byte[] oneklenti = new byte[] { (byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x05, (byte) 0x2B,
                (byte) 0x0E, (byte) 0x03, (byte) 0x02, (byte) 0x1A, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x14 };
		
		byte[] son = new byte[imzalanacak.length+oneklenti.length];
		
		System.arraycopy(oneklenti, 0, son, 0, oneklenti.length);
		System.arraycopy(imzalanacak,0, son, oneklenti.length, imzalanacak.length);
		
		byte[] sonuc1 = SignUtil.sign(SignatureAlg.RSA_SHA1, "aslihan".getBytes(), mprivKey);
		
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		byte[] sonuc2 = sc.signData(sessionID, objectLabel, son, PKCS11Constants.CKM_RSA_PKCS);
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		
		assertEquals(true, Arrays.equals(sonuc1, sonuc2));
		
		mSignedData = sonuc1;
	}
	
	public void testVerifyData()
	throws PKCS11Exception,IOException,CryptoException, SmartCardException
	{
		byte [] imzalanan = DigestUtil.digest(DigestAlg.SHA1, "aslihan".getBytes());
		byte[] oneklenti = new byte[] { (byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x05, (byte) 0x2B,
                (byte) 0x0E, (byte) 0x03, (byte) 0x02, (byte) 0x1A, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x14 };
		
		byte[] son = new byte[imzalanan.length+oneklenti.length];
		
		System.arraycopy(oneklenti, 0, son, 0, oneklenti.length);
		System.arraycopy(imzalanan,0, son, oneklenti.length, imzalanan.length);
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		sc.verifyData(sessionID, objectLabel, son, mSignedData, PKCS11Constants.CKM_RSA_PKCS);
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		assertEquals(true, true);
	}
	
	public void testEncryptData()
	throws PKCS11Exception,IOException,CryptoException, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
				
		byte[] sifreli = sc.encryptData(sessionID, objectLabel, "aslihan".getBytes(), PKCS11Constants.CKM_RSA_PKCS);
		
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		
		String decryptedStr = new String(CipherUtil.decryptRSA(sifreli, mprivKey));
		assertEquals("aslihan", decryptedStr);
		
		mEncryptedData = sifreli;
	}
	
	
	public void testDecryptData()
	throws PKCS11Exception,IOException,CryptoException, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		byte[] sonuc = sc.decryptData(sessionID, objectLabel, mEncryptedData, PKCS11Constants.CKM_RSA_PKCS);
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		assertEquals(true, new String(sonuc).equals("aslihan"));
	}
	
	public void testReadCert()
	throws PKCS11Exception,CryptoException, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		
		List<byte[]> value = sc.readCertificate(sessionID, objectLabel);
		boolean sonuc = true;
		if(value == null || value.size() ==0)
			sonuc = false;
		
		assertEquals(true, sonuc);
		sc.closeSession(sessionID);
	}
	
	public void testReadPublicKey()
	throws PKCS11Exception,CryptoException, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		
		RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sessionID, objectLabel);
		sc.closeSession(sessionID);
		
		assertEquals(true, ((RSAPublicKeyImpl)mpubKey).getModulus().equals(spec.getModulus()));
		assertEquals(true, ((RSAPublicKeyImpl)mpubKey).getPublicExponent().equals(spec.getPublicExponent()));
		
	}
	
	public void testDeleteData2()
	throws PKCS11Exception,CryptoException, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		sc.deletePrivateObject(sessionID, objectLabel);
		sc.deletePublicObject(sessionID, objectLabel);
		sc.logout(sessionID);
		sc.closeSession(sessionID);
	}
	
	public void testImportKeyPair()
	throws PKCS11Exception,IOException,Asn1Exception,NoSuchAlgorithmException, ESYAException, SmartCardException
	{
		ECertificate eCert = new ECertificate(ayseCert.getBytes("ASCII"));
		byte[] subject = UtilName.name2byte(eCert.getSubject().getObject());
		
		RSAPublicKey pubkey =(RSAPublicKey) eCert.asX509Certificate().getPublicKey();
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		RSAPrivateCrtKey prikey = (RSAPrivateCrtKey) kpg.generateKeyPair().getPrivate();
		
		KeyPair kp = new KeyPair(pubkey,prikey);
		
		
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		sc.importKeyPair(sessionID, objectLabel,kp, subject,true,false);
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		
	}
	
	
	public void testWritePrivateData()
	throws PKCS11Exception
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		sc.writePrivateData(sessionID, "PRIDATA", new byte[]{1,2,3,4,5,6});
		boolean sonuc = sc.isObjectExist(sessionID, "PRIDATA");
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		assertEquals(true, sonuc);
	}
	
	public void testWritePublicData()
	throws PKCS11Exception
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		sc.writePublicData(sessionID, "PUBDATA", new byte[]{7,8,9,10,11,12});
		boolean sonuc = sc.isObjectExist(sessionID, "PUBDATA");
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		assertEquals(true, sonuc);
	}
	
	public void testReadPrivateData()
	throws PKCS11Exception,CryptoException, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		List<byte[]> sonuc = sc.readPrivateData(sessionID, "PRIDATA");
		sc.logout(sessionID);
		sc.closeSession(sessionID);
		assertEquals(true, Arrays.equals(sonuc.get(0), new byte[]{1,2,3,4,5,6}));
	}
	
	public void testReadPublicData()
	throws PKCS11Exception,CryptoException, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		List<byte[]> sonuc = sc.readPublicData( sessionID, "PUBDATA");
		sc.closeSession(sessionID);
		assertEquals(true, Arrays.equals(sonuc.get(0), new byte[]{7,8,9,10,11,12}));
	}
	
	public void testDeleteData() throws PKCS11Exception, SmartCardException
	{
		long slot = getSlot();
		long sessionID = sc.openSession(slot);
		sc.login(sessionID, mPassword);
		sc.deletePrivateData(sessionID, "PRIDATA");
		sc.deletePublicData(sessionID, "PUBDATA");
		sc.logout(sessionID);
		sc.closeSession(sessionID);
	}
	
	
	private long getSlot()
	throws PKCS11Exception
	{
		long[] slots = sc.getTokenPresentSlotList();
		return slots[0];
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, PKCS11Exception, IOException, Asn1Exception, SmartCardException, ESYAException 
	{
		SmartCardTest sct = new SmartCardTest();
		sct.testImportCertificateWithKey();
		sct.testEncryptData();
		sct.testDecryptData();
	}
	
	
	
}
