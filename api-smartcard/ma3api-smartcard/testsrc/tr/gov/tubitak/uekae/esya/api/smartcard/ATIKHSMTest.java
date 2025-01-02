package tr.gov.tubitak.uekae.esya.api.smartcard;

import gnu.crypto.key.rsa.GnuRSAPublicKey;

import java.io.ByteArrayInputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.SignUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.CardType;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.SmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ops.key.HMACSecretKey;

public class ATIKHSMTest extends TestCase{
    
    
    private static final String testCer = "MIIGBDCCBOygAwIBAgICCgIwDQYJKoZIhvcNAQEFBQAwfDETMBEGCgmSJomT8ixkARkWA05FVDES"+
"MBAGCgmSJomT8ixkARkWAlVHMRIwEAYDVQQKDAlUw5xCxLBUQUsxDjAMBgNVBAsMBVVFS0FFMS0w"+
"KwYDVQQDDCTDnHLDvG4gR2VsacWfdGlybWUgU2VydGlmaWthIE1ha2FtxLEwHhcNMTEwNjI0MTE0"+
"ODQ3WhcNMTQwMzIwMTI0ODQ3WjBCMQswCQYDVQQGEwJUUjEUMBIGA1UEBRMLNzg5NDU2MTIzMTIx"+
"HTAbBgNVBAMMFMOWemfDvHIgTXVzdGFmYSBTdWN1MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIB"+
"CgKCAQEAr+itKHM+V4Qdac8d3CU52kt+i0r1v50kJNhnCofnVcemMJhWygQc4L49UfTnFBIXXH04"+
"5KYxMEss6IY53uaGvl130mrYXWhrByNUrHd6jhkL1C1GuT2vBheRtWMXihsIMkeDZS06VK1Ha7on"+
"OhNQbH+WO8dlql2bbYuMSws/BYchIHg6piPUoYUyfVxnbA2f9guHwe2f/wf8Bh4z275wv8UAGipm"+
"8HQDNUbgFXwvow1e+36aXuy7zjjyuoSNMrcNYCPN4LiRN3vv+sr/f/mivq8s3UYXCUj9NidYCLcr"+
"IfP82PYG3I54vAGzvVhGbY4EEP9do/jw8+IOt5wbTV/GaQIDAQABo4ICyDCCAsQwHwYDVR0jBBgw"+
"FoAU/OhOzZyRByyhQdk8YXJ/lRAv96swHQYDVR0OBBYEFC/uqb//5KF4Ipk9bnMYag092gKSMA4G"+
"A1UdDwEB/wQEAwIGwDCCATQGA1UdIASCASswggEnMIIBIwYLYIYYAQIBAQUHAQEwggESMC8GCCsG"+
"AQUFBwIBFiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNX1NVRTCB3gYIKwYBBQUHAgIw"+
"gdEegc4AQgB1ACAAcwBlAHIAdABpAGYAaQBrAGEALAAgAC4ALgAuAC4AIABzAGEAeQExAGwBMQAg"+
"AEUAbABlAGsAdAByAG8AbgBpAGsAIAEwAG0AegBhACAASwBhAG4AdQBuAHUAbgBhACAAZwD2AHIA"+
"ZQAgAG8AbAB1AV8AdAB1AHIAdQBsAG0AdQFfACAAbgBpAHQAZQBsAGkAawBsAGkAIABlAGwAZQBr"+
"AHQAcgBvAG4AaQBrACAAcwBlAHIAdABpAGYAaQBrAGEAZAExAHIALjB0BgNVHR8EbTBrMCygKqAo"+
"hiZodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNU0lMLmNybDA7oDmgN4Y1bGRhcDovL2Rp"+
"emluLnRlc3RzbS5nb3YudHIvQz1UUixPPVRFU1RTTSxDTj1URVNUU01TSUwwgakGCCsGAQUFBwEB"+
"BIGcMIGZMC8GCCsGAQUFBzAChiNodHRwOi8vd3d3LnRlc3RzbS5uZXQudHIvVEVTVFNNLmNydDA+"+
"BggrBgEFBQcwAoYybGRhcDovL2RpemluLnRlc3RzbS5uZXQudHIvQz1UUixPPVRFU1RTTSxDTj1U"+
"RVNUU00wJgYIKwYBBQUHMAGGGmh0dHA6Ly9vY3NwLnRlc3RzbS5uZXQudHIvMBgGCCsGAQUFBwED"+
"BAwwCjAIBgYEAI5GAQEwDQYJKoZIhvcNAQEFBQADggEBADOM7PirdSHjZBRsxmX0shdaSTl2DACB"+
"z2wlb/Y2RyVEapzc4ji9CCJEIhTs7q9812of5FRXlsItT+PFRvuiLNRArLQPxxgAZwOTtowMWIzH"+
"tw+el8bcqMXSmt7/G/YhN82vO7MZJ//3fzFk1vBWL2Uf38rtfR2PmeWGhiyQoL8xSQo0dBDuD649"+
"gKSmk0NupuLuSCG8/Y36EovWHJLxw0RT8RBLHZcb0/PRzCrIKDCwTKunK7bENqmz6cyk5fhhxA6d"+
"wTWqDg6Hw1Cnt66ZDZPSFuUeHj6cgIbgNY2temEhwj6fSE59CGR8WwHOAwVEnZRbi3fsoz7KIDv0"+
"IZBmMvc=";
    
    private static final String testPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCv6K0ocz5XhB1pzx3cJTnaS36L"+
"SvW/nSQk2GcKh+dVx6YwmFbKBBzgvj1R9OcUEhdcfTjkpjEwSyzohjne5oa+XXfSathdaGsHI1Ss"+
"d3qOGQvULUa5Pa8GF5G1YxeKGwgyR4NlLTpUrUdruic6E1Bsf5Y7x2WqXZtti4xLCz8FhyEgeDqm"+
"I9ShhTJ9XGdsDZ/2C4fB7Z//B/wGHjPbvnC/xQAaKmbwdAM1RuAVfC+jDV77fppe7LvOOPK6hI0y"+
"tw1gI83guJE3e+/6yv9/+aK+ryzdRhcJSP02J1gItysh8/zY9gbcjni8AbO9WEZtjgQQ/12j+PDz"+
"4g63nBtNX8ZpAgMBAAECggEBAKb55UwtQHMQTF9Ao+ZxS54j5UXRbL5rKoDzDbRYVsX9Eoq0QEXY"+
"a5UF3+0o3CQYHCbGErgv7ScbZNB/gPVNu39995w7oY/g6x9GcTyY2TODINBR/f0eSUIuIzibjB+j"+
"Ez+u1FG5AdKY/N+MP2oIJWIoJfIujxmNa1krios9bKAQFfkNc1sXGQ5014umzSagmWwPZrgV09Hu"+
"Ik4N4rgxt0faM2swq3smSJNonfoockwo9Xb9TVW8uWgUTdOotj/ihkeGdCFWy4w0vx4csjY0Qbtc"+
"hNW5XtxAKC15nwcyJu3AuPi59TvSNF80LJxwi6IvSquNPYrtUlD8M48JCfR41IUCgYEA5DU8UNB6"+
"M/Sj3oMYODvONxpuCzpCip8AE8i0kvp11Bnir/1oPvokPCL0gB0uhpx8BieYGNtvgxdZtxGiYqKB"+
"JkFo2Fh40kibcCi241eT+joNgPJTD8mYBFo/kiDxHVSayBLY77YpYx0uTrj42bmukXr9lLCy4zuM"+
"3DS6lulr7qsCgYEAxVTuIIGkOlqLYxQFjngYZc5KgEO3ber7y5j8imPtA8R3RskomJx9shobW5Ns"+
"LjblKGrN5mix8cL1nJadMlFkwNQJga/sb1RW0Spi9INEIeAwTmqNJqhB6AJJzWZa8J0bnZ0tIrbo"+
"xM8wYas+99Ii02Im9w0+txtlGaRyIdSLTzsCgYAtJGq+Ab9qr6YKyhvsY8gzFkNWbTvkd/dn8nfl"+
"6y2Lu2MgNRx9+LVaP//lp+AgOKw/+20W3bF9WQ0iLZbVtBegHahDw5yC3GIDGcqzxgs7oGgzbbwI"+
"j3RGyCNzIJkRmD7V/QR0xrABLzCN2gE/8H8bwByRYTLByHHgzX1rhNkY6QKBgDG+pDzrkYPoWWUD"+
"ohb1LWlUpLFK4M3Dw+/iRB966z/c4hilEyfNo14neKgQNOA9lG0o53jjAaCpfhMYYM5TeGunyDG6"+
"MIcsIqqd3c433RARHPxXnfeVyO98zDAMUnZ/lHuaKMusgmdCt7aXXctJXOAeySXUX+/25vic3Oys"+
"UOYLAoGBAMT52RvDW7XePPGT6WC3Yub/Jj2tT+/jF8qLEufBP9VC+Uk2THeoK0lfPZUl6h8okSrt"+
"oavimivyYPgHmHDeDq46VSKqsxRay5KKL45ZylgsSu65d8mgBCxWkqseJka8Kwd5/X2lnqclGGh3"+
"YvLNxxDsIEpAWSUX+r/Ha6pCtcos";
    
    private static final String testPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr+itKHM+V4Qdac8d3CU52kt+i0r1v50k"+
"JNhnCofnVcemMJhWygQc4L49UfTnFBIXXH045KYxMEss6IY53uaGvl130mrYXWhrByNUrHd6jhkL"+
"1C1GuT2vBheRtWMXihsIMkeDZS06VK1Ha7onOhNQbH+WO8dlql2bbYuMSws/BYchIHg6piPUoYUy"+
"fVxnbA2f9guHwe2f/wf8Bh4z275wv8UAGipm8HQDNUbgFXwvow1e+36aXuy7zjjyuoSNMrcNYCPN"+
"4LiRN3vv+sr/f/mivq8s3UYXCUj9NidYCLcrIfP82PYG3I54vAGzvVhGbY4EEP9do/jw8+IOt5wb"+
"TV/GaQIDAQAB";
    
   
    
    static SmartCard sc = null;
    static final String PASSWORD = "1234"; 
    static final String CREATED_KEY_LABEL_FOR_SIGNING = "SigningRSAKeyCreated";
    static final String CREATED_KEY_LABEL_FOR_ENCRYPTION = "EncryptionRSAKeyCreated";
    static final String IMPORTED_KEY_LABEL = "RSAKeyImported";
    static final String IMPORTED_KEY_LABEL_2 = "RSAKeyImported2";
    static final String IMPORTED_CERT_LABEL = "CertImported";
    static final String IMPORTED_CERT_LABEL_2 = "CertImported2";
    static final String CREATED_HMAC_KEY_LABEL = "HMACKeyCreated";
    static final String IMPORTED_HMAC_KEY_LABEL = "HMACKeyImported";
    
    static byte[] signature = null;
    static byte[] tobesigned = null;
    
    static byte[] encrypted = null;
    static byte[] tobeencrypted = null;
    
    static
    {
	try
	{
	    
	    sc = new SmartCard(CardType.ATIKHSM);
	}
	catch(Exception aEx)
	{
	    aEx.printStackTrace();
	}
    }
    
    private long getSlot()
    throws PKCS11Exception
    {
	long[] slots = sc.getTokenPresentSlotList();
	return slots[0];
    }
    
    
    public void testCreateRSAKeysForSigning()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
	    sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_SIGNING, spec, true, false);
	    boolean priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
	    boolean pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
	    assertEquals(true, priFound&&pubFound);
	    
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testSigning()
    throws Exception
    {
	long sid = -1;
	try
	{
	    tobesigned = new byte[22];
	    new Random().nextBytes(tobesigned);
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    signature = sc.signData(sid, CREATED_KEY_LABEL_FOR_SIGNING, tobesigned, PKCS11Constants.CKM_SHA1_RSA_PKCS);
	    RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, CREATED_KEY_LABEL_FOR_SIGNING);
	    boolean result = _gnuVerify(SignatureAlg.RSA_SHA1,spec, tobesigned, signature,"testSigning");
	    assertEquals(true, result);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    private boolean _gnuVerify(SignatureAlg aAlg,RSAPublicKeySpec aSpec,byte[] aData,byte[] aSignature,String aName)
    throws Exception
    {
	GnuRSAPublicKey pk = new GnuRSAPublicKey(aSpec.getModulus(), aSpec.getPublicExponent());
	return  SignUtil.verify(aAlg, aData, aSignature, pk);
    }
    
    public void testVerifying()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    sc.verifyData(sid, CREATED_KEY_LABEL_FOR_SIGNING, tobesigned, signature, PKCS11Constants.CKM_SHA1_RSA_PKCS);
	    assertEquals(true, true);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
      //imzalama anahtari ile sifrelemeye izin vermemeli
    public void testEncryptionWithSigningKey()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    try
	    {
		sc.encryptData(sid, CREATED_KEY_LABEL_FOR_SIGNING, "test".getBytes(), PKCS11Constants.CKM_SHA1_RSA_PKCS);
	    }
	    catch(PKCS11Exception aEx)
	    {
		if(aEx.getErrorCode()== PKCS11Constants.CKR_KEY_FUNCTION_NOT_PERMITTED)
		{
		    assertEquals(true, true);return;
		}
		
	    }
	    assertEquals(true, false);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testDeleteKeys()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    sc.deletePrivateObject(sid,CREATED_KEY_LABEL_FOR_SIGNING);
	    sc.deletePublicObject(sid,CREATED_KEY_LABEL_FOR_SIGNING);
	   
	    boolean found1 = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
	    boolean found2 = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_SIGNING);
	    
	    assertEquals(false, found1 && found2);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
   
    public void testCreateRSAKeysForEncryption()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, null);
	    sc.createKeyPair(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, spec, false, true);
	    boolean priFound = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    boolean pubFound = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    assertEquals(true, priFound&&pubFound);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    
    public void testEncryption()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    tobeencrypted = new byte[125];
	    new Random().nextBytes(tobeencrypted);
	    encrypted = sc.encryptData(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, tobeencrypted, PKCS11Constants.CKM_RSA_PKCS);
	    assertEquals(true, true);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testDecryption()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] result = sc.decryptData(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, encrypted, PKCS11Constants.CKM_RSA_PKCS);
	    assertEquals(true, Arrays.equals(tobeencrypted, result));
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    //gnu da encrypt edilen datayi decrypt yapma
    public void testDecryption2()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    RSAPublicKeySpec spec = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    GnuRSAPublicKey pubkey = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());
	    byte[] encrypted = CipherUtil.encrypt(CipherAlg.RSA_PKCS1,null,tobeencrypted , pubkey);
	    byte[] result = sc.decryptData(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, encrypted, PKCS11Constants.CKM_RSA_PKCS);

	    assertEquals(true, Arrays.equals(tobeencrypted, result));
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
   //sifreleme anahtari ile imzalamaya izin vermemesi gerekiyor
    
    public void testSigningWithEncryptionKeys()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    try
	    {
		sc.signData(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION, "test".getBytes(), PKCS11Constants.CKM_SHA1_RSA_PKCS);
	    }
	    catch(PKCS11Exception aEx)
	    {
		if(aEx.getErrorCode()==PKCS11Constants.CKR_KEY_FUNCTION_NOT_PERMITTED)
		{
		    assertEquals(true,true);
		    return;
		}
	    }
	    assertEquals(true,false);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testDeleteKeys2()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    boolean r1 = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    boolean r2 = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    sc.deletePrivateObject(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    sc.deletePublicObject(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    boolean r3 = sc.isPrivateKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    boolean r4 = sc.isPublicKeyExist(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);
	    assertEquals(true, r1 && r2 && !r3 && !r4);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
}
   
    public void testImportCertificate()
    throws Exception
    {
	long sid = -1;
	try
	{
	    CertificateFactory cf = CertificateFactory.getInstance("X.509");
	    X509Certificate cert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(Base64.decode(testCer)));
	    
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    boolean r1 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
	    sc.importCertificate(sid, IMPORTED_CERT_LABEL, cert);
	    boolean r2 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
	    assertEquals(true, !r1 && r2);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testImportKeysForSigning()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    PublicKey pubkey = KeyUtil.decodePublicKey(AsymmetricAlg.RSA, Base64.decode(testPublicKey));
	    PrivateKey prikey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, Base64.decode(testPrivateKey));
	    
	    KeyPair kp = new KeyPair(pubkey, prikey);
	    
	    boolean r1 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
	    boolean r2 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
	    sc.importKeyPair(sid, IMPORTED_KEY_LABEL, kp, null, true, false);
	    boolean r3 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
	    boolean r4 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
	    sc.logout(sid);
	    assertEquals(true, !r1 && !r2 && r3 && r4);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    
    public void testReadPublicKeySpec()
    throws Exception
    {
	long sid = -1;
	try
	{
	    RSAPublicKey pubkey = (RSAPublicKey) KeyUtil.decodePublicKey(AsymmetricAlg.RSA, Base64.decode(testPublicKey));
	    
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    RSAPublicKeySpec spec =  (RSAPublicKeySpec) sc.readPublicKeySpec(sid,IMPORTED_KEY_LABEL);
	    boolean r1= pubkey.
	    getModulus().equals(spec.getModulus());
	    boolean r2 = pubkey.getPublicExponent().equals(spec.getPublicExponent());
	    assertEquals(true, r1 && r2);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
	
	
	
    }
    
    public void testSignWithImportedKeys()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    tobesigned = new byte[200];
	    new Random().nextBytes(tobesigned);
	  
	    signature = sc.signData(sid, IMPORTED_KEY_LABEL, tobesigned, PKCS11Constants.CKM_SHA1_RSA_PKCS);
	  
	    RSAPublicKeySpec spec =(RSAPublicKeySpec) sc.readPublicKeySpec(sid, IMPORTED_KEY_LABEL);
	    boolean result = _gnuVerify(SignatureAlg.RSA_SHA1,spec, tobesigned, signature,"testSignWithImportedKeys");
	    assertEquals(true, result);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    
    public void testVerifyWithImportedKeys()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    sc.verifyData(sid, IMPORTED_KEY_LABEL, tobesigned, signature,PKCS11Constants.CKM_SHA1_RSA_PKCS);
	    assertEquals(true, true);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testGNUSignHSMVerify()
    throws Exception
    {
	long sid = -1;
	try
	{
        	byte[] tobesigned = new byte[147];
        	new Random().nextBytes(tobesigned);
        	PrivateKey pk = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, Base64.decode(testPrivateKey));
        	byte[] sig = SignUtil.sign(SignatureAlg.RSA_NONE, tobesigned, pk);
        	
        	long slot = getSlot();
        	sid = sc.openSession(slot);
        	sc.login(sid, PASSWORD);
        	sc.verifyData(sid, IMPORTED_KEY_LABEL, tobesigned, sig,PKCS11Constants.CKM_RSA_PKCS);
        	assertEquals(true, true);
        	sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
	
    }
    
    public void testSignWithImportedKeys2()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    tobesigned = new byte[105];
	    new Random().nextBytes(tobesigned);
	 
	    signature = sc.signData(sid, IMPORTED_KEY_LABEL, tobesigned, PKCS11Constants.CKM_RSA_PKCS);
	 
	    RSAPublicKeySpec spec =(RSAPublicKeySpec) sc.readPublicKeySpec(sid, IMPORTED_KEY_LABEL);
	    boolean result = _gnuVerify(SignatureAlg.RSA_NONE,spec, tobesigned, signature,"testSignWithImportedKeys2");
	    assertEquals(true, result);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testVerifyWithImportedKeys2()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    sc.verifyData(sid, IMPORTED_KEY_LABEL, tobesigned, signature,PKCS11Constants.CKM_RSA_PKCS);
	    assertEquals(true, true);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testSignWithBadLengthInput()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] tobesigned = new byte[401];
	    new Random().nextBytes(tobesigned);
	    try
	    {
		sc.signData(sid, IMPORTED_KEY_LABEL, tobesigned, PKCS11Constants.CKM_RSA_PKCS);
	    }
	    catch(PKCS11Exception aEx)
	    {
		assertEquals(true, true);return;
	    }
	    assertEquals(true, false);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
	
	
    }
    
    public void testDeleteKeys3()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    boolean r1 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
	    boolean r2 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
	    sc.deletePrivateObject(sid, IMPORTED_KEY_LABEL);
	    sc.deletePublicObject(sid, IMPORTED_KEY_LABEL);
	    boolean r3 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
	    boolean r4 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
	    assertEquals(true, r1 && r2 && !r3 && !r4);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testImportKeysForEncryption()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    PublicKey pubkey = KeyUtil.decodePublicKey(AsymmetricAlg.RSA, Base64.decode(testPublicKey));
	    PrivateKey prikey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, Base64.decode(testPrivateKey));
	    
	    KeyPair kp = new KeyPair(pubkey, prikey);
	    
	    boolean r1 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
	    boolean r2 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
	    sc.importKeyPair(sid, IMPORTED_KEY_LABEL, kp, null, false, true);
	    boolean r3 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
	    boolean r4 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
	    sc.logout(sid);
	    assertEquals(true, !r1 && !r2 && r3 && r4);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testEncryptionWithImportedKeys()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    tobeencrypted = new byte[125];
	    new Random().nextBytes(tobeencrypted);
	 
	    encrypted = sc.encryptData(sid, IMPORTED_KEY_LABEL, tobeencrypted, PKCS11Constants.CKM_RSA_PKCS);
	 
	    byte[] result = sc.decryptData(sid, IMPORTED_KEY_LABEL, encrypted, PKCS11Constants.CKM_RSA_PKCS);
	 
	    assertEquals(true, Arrays.equals(result, tobeencrypted));
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testEncryptionWithImportedKeys2()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    tobeencrypted = new byte[54];
	    new Random().nextBytes(tobeencrypted);
	    RSAPublicKeySpec spec  = (RSAPublicKeySpec) sc.readPublicKeySpec(sid, IMPORTED_KEY_LABEL);
	    GnuRSAPublicKey pubk = new GnuRSAPublicKey(spec.getModulus(), spec.getPublicExponent());
	    encrypted = CipherUtil.encrypt(CipherAlg.RSA_PKCS1, null, tobeencrypted, pubk);
	    
	    byte[] result = sc.decryptData(sid, IMPORTED_KEY_LABEL, encrypted, PKCS11Constants.CKM_RSA_PKCS);
	    assertEquals(true, Arrays.equals(result, tobeencrypted));
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testEncryptionWithBadLengthInput()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] tobeencrypted = new byte[255];
	    new Random().nextBytes(tobeencrypted);
	    try
	    {
		sc.encryptData(sid, IMPORTED_KEY_LABEL, tobeencrypted, PKCS11Constants.CKM_RSA_PKCS);
	    }
	    catch(PKCS11Exception aEx)
	    {
		assertEquals(true, true);return;
	    }
	    assertEquals(true, false);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testDecryptionWithBadLengthInput()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] tobedecrypted = new byte[259];
	    new Random().nextBytes(tobedecrypted);
	    try
	    {
		sc.decryptData(sid, IMPORTED_KEY_LABEL, tobedecrypted, PKCS11Constants.CKM_RSA_PKCS);
	    }
	    catch(PKCS11Exception aEx)
	    {
		assertEquals(true, true);return;
	    }
	    assertEquals(true, false);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testDeleteKeys4()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    
	    boolean r1 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
	    boolean r2 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
	    sc.deletePrivateObject(sid, IMPORTED_KEY_LABEL);
	    sc.deletePublicObject(sid, IMPORTED_KEY_LABEL);
	    boolean r3 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL);
	    boolean r4 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL);
	    assertEquals(true, r1 && r2 && !r3 && !r4);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testReadCertificate()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    List<byte[]> certs = sc.readCertificate(sid, IMPORTED_CERT_LABEL);
	    
	    ECertificate cer = new ECertificate(certs.get(0));
	    ECertificate cer2 = new ECertificate(testCer);
	    assertEquals(true, cer.equals(cer2));
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testDeleteCertificate()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    boolean r1 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
	    sc.deletePublicObject(sid, IMPORTED_CERT_LABEL);
	    boolean r2 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL);
	    assertEquals(true, r1 && !r2);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testHmacKeyGeneration()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    HMACSecretKey secretkey = new HMACSecretKey(CREATED_HMAC_KEY_LABEL, 32);
	    boolean r1 = sc.isObjectExist(sid, CREATED_HMAC_KEY_LABEL);
	    sc.createSecretKey(sid, secretkey);
	    boolean r2 = sc.isObjectExist(sid, CREATED_HMAC_KEY_LABEL);
	    assertEquals(true, !r1 && r2);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testHmacSign()
    throws Exception
    {
	long sid = -1;
	try
	{
	    byte[] tobesigned = new byte[27];
	    new Random().nextBytes(tobesigned);
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] sig = sc.signData(sid, CREATED_HMAC_KEY_LABEL, tobesigned, PKCS11Constants.CKM_SHA256_HMAC);
	    sc.verifyData(sid, CREATED_HMAC_KEY_LABEL, tobesigned, sig, PKCS11Constants.CKM_SHA256_HMAC);
	    assertEquals(true, true);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testHmacImport()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] key = new byte[32];
	    new Random().nextBytes(key);
	   
	    HMACSecretKey secretkey = new HMACSecretKey(IMPORTED_HMAC_KEY_LABEL, key);
	    boolean r1 = sc.isObjectExist(sid, IMPORTED_HMAC_KEY_LABEL);
	    sc.importSecretKey(sid, secretkey);
	    boolean r2 = sc.isObjectExist(sid, IMPORTED_HMAC_KEY_LABEL);
	    assertEquals(true, !r1 && r2);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testHmacSignWithImportedKeys()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] tobesigned = new byte[231];
	    new Random().nextBytes(tobesigned);
	    
	    byte[] sig = sc.signData(sid, IMPORTED_HMAC_KEY_LABEL, tobesigned, PKCS11Constants.CKM_SHA256_HMAC);
	   
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testDeleteHmacKeys()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    sc.deletePrivateObject(sid, CREATED_HMAC_KEY_LABEL);
	    sc.deletePrivateObject(sid, IMPORTED_HMAC_KEY_LABEL);
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    } 
    
    public void testImportCertificateWithKeys()
    throws Exception
    {
	long sid = -1;
	try
	{
	   PrivateKey prikey = KeyUtil.decodePrivateKey(AsymmetricAlg.RSA, Base64.decode(testPrivateKey));
	   CertificateFactory cf = CertificateFactory.getInstance("X.509");
	   X509Certificate cert = (X509Certificate)cf.generateCertificate(new ByteArrayInputStream(Base64.decode(testCer)));
	    
	   long slot = getSlot();
	   sid = sc.openSession(slot);
	   sc.login(sid, PASSWORD);
	   boolean r1 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL_2);
	   boolean r2 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL_2);
	   boolean r3 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL_2);
	   sc.importCertificateAndKey(sid, IMPORTED_CERT_LABEL_2, IMPORTED_KEY_LABEL_2, prikey, cert);
	   boolean r4 = sc.isPrivateKeyExist(sid, IMPORTED_KEY_LABEL_2);
	   boolean r5 = sc.isPublicKeyExist(sid, IMPORTED_KEY_LABEL_2);
	   boolean r6 = sc.isCertificateExist(sid, IMPORTED_CERT_LABEL_2);
	   assertEquals(true, !r1 && !r2 && !r3 && r4 && r5 && r6);
	   sc.logout(sid);  
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testSignDataWithCertSerialNo()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    ECertificate cer = new ECertificate(testCer);
	    sc.login(sid, PASSWORD);
	    byte[] tobesigned = new byte[123];
	    new Random().nextBytes(tobesigned);
	    byte[] sig = sc.signDataWithCertSerialNo(sid, cer.getSerialNumber().toByteArray(), PKCS11Constants.CKM_SHA1_RSA_PKCS,tobesigned);
	    boolean result = SignUtil.verify(SignatureAlg.RSA_SHA1, tobesigned, sig, cer);
	    assertEquals(true,result);
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testWritePrivateData()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] data = new byte[] {1,2,3,4};
	    sc.writePrivateData(sid, "PRIDATA",data );
	    byte[] result = sc.readPrivateData(sid, "PRIDATA").get(0);
	    assertEquals(true, Arrays.equals(data, result));
	    sc.logout(sid);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void testWritePublicData()
    throws Exception
    {
	
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	    byte[] data = new byte[] {5,6,7,8};
	    sc.writePublicData(sid, "PUBDATA",data );
	    sc.logout(sid);
	    byte[] result = sc.readPublicData(sid, "PUBDATA").get(0);
	    assertEquals(true, Arrays.equals(data, result));
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    
    public void testDeleteData()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.deletePublicData(sid, "PUBDATA");
	    boolean r1 = sc.isObjectExist(sid, "PUBDATA");
	    sc.login(sid, PASSWORD);
	    sc.deletePrivateData(sid, "PRIDATA");
	    boolean r2 = sc.isObjectExist(sid, "PRIDATA");
	    sc.logout(sid);
	   
	    assertEquals(true, !r1 && !r2);
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
    public void deleteObject()
    throws Exception
    {
	long sid = -1;
	try
	{
	    long slot = getSlot();
	    sid = sc.openSession(slot);
	    sc.login(sid, PASSWORD);
	   try{sc.deletePrivateObject(sid, IMPORTED_KEY_LABEL);}catch(Exception aEx){}
	   try{sc.deletePublicObject(sid, IMPORTED_KEY_LABEL);}catch(Exception aEx){}
	   try{sc.deletePublicObject(sid, IMPORTED_CERT_LABEL);}catch(Exception aEx){}
	   try{sc.deletePrivateObject(sid, CREATED_KEY_LABEL_FOR_SIGNING);}catch(Exception aEx){}
	   try{sc.deletePublicObject(sid, CREATED_KEY_LABEL_FOR_SIGNING);}catch(Exception aEx){}
	   try{sc.deletePrivateObject(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);}catch(Exception aEx){}
	   try{sc.deletePublicObject(sid, CREATED_KEY_LABEL_FOR_ENCRYPTION);}catch(Exception aEx){}
	   try{sc.deletePrivateObject(sid, IMPORTED_KEY_LABEL_2);}catch(Exception aEx){}
	   try{sc.deletePublicObject(sid, IMPORTED_KEY_LABEL_2);}catch(Exception aEx){}
	   try{sc.deletePublicObject(sid, IMPORTED_CERT_LABEL_2);}catch(Exception aEx){}
	   try{sc.deletePrivateObject(sid, CREATED_HMAC_KEY_LABEL);}catch(Exception aEx){}
	   try{sc.deletePrivateObject(sid, IMPORTED_HMAC_KEY_LABEL);}catch(Exception aEx){}
	    sc.logout(sid);
	    
	}
	finally
	{
	    if(sid!=-1)
		sc.closeSession(sid);
	}
    }
    
}
