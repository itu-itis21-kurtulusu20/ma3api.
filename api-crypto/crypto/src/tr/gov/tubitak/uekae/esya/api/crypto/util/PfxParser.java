package tr.gov.tubitak.uekae.esya.api.crypto.util;

import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author ayetgin
 */


public class PfxParser
{
	protected InputStream mPFX;
	
	protected char[] mPassword;
	
	List<Pair<ECertificate, PrivateKey>> keyCertPairs = new ArrayList<Pair<ECertificate,PrivateKey>>();
	Pair<ECertificate, PrivateKey> signingKeyCertPair = new Pair<ECertificate,PrivateKey>();


    public PfxParser(InputStream aPFX, String aPassword) throws CryptoException{
        this(aPFX, aPassword.toCharArray());
    }


	/**
	 * Create pfx parser from inputstream with password
	  * @param aPFX Input stream of pfx file
	  * @param aPassword password of pfx
	  * @throws CryptoException
	  */
	public PfxParser(InputStream aPFX, char[] aPassword) throws CryptoException{
		mPFX = aPFX;
		mPassword = aPassword;
		
		_init();
	}
	
	private void _init() throws CryptoException
	{
		KeyStore ks;
		try 
		{
			ks = KeyStore.getInstance("PKCS12");
		
			ks.load(mPFX, mPassword);
			
			Enumeration<String> aliases = ks.aliases();

			while (aliases.hasMoreElements()) 
			{
				String alias = aliases.nextElement();
				
				X509Certificate c = (X509Certificate) ks.getCertificate(alias);

				ECertificate esyaCer = null;

				//Pfx may not contain certificate.
				if(c != null)
					esyaCer = new ECertificate(c.getEncoded());
				
				PrivateKey key = (PrivateKey)ks.getKey(alias, mPassword);
				
				Pair<ECertificate, PrivateKey> pair = new Pair<ECertificate, PrivateKey>(esyaCer, key); 
				
				keyCertPairs.add(pair);
			}
		} 
		catch (Exception e) 
		{
			throw new CryptoException("Error in parsing PFX",e);
		}
	
	}	
	/**
	 * Returns certificates and keys from pfx
	 * @return
	 */
	public List<Pair<ECertificate, PrivateKey>> getCertificatesAndKeys()
	{
		return keyCertPairs;
	}

	public ECertificate getFirstCertificate()
	{
		return keyCertPairs.get(0).getObject1();
	}

	public Pair<ECertificate, PrivateKey>  getFirstSigningKeyCertPair(){

		signingKeyCertPair = null;
		for (Pair<ECertificate, PrivateKey> certificatesAndKeys :keyCertPairs) {
			if(certificatesAndKeys.first().getExtensions().getKeyUsage().isDigitalSignature()){
				signingKeyCertPair = certificatesAndKeys;
				break;
			}
		}

		return signingKeyCertPair;
	}

	public PrivateKey getFirstPrivateKey()
	{
		return keyCertPairs.get(0).getObject2();
	}

    public static void main(String[] args) throws Exception
    {
        PfxParser parser = new PfxParser(new FileInputStream("T:\\api-xmlsignature\\testdata\\pt2010\\CryptographicMaterial\\SCOK\\AhmetKeyCert.p12"), "123456".toCharArray());
        System.out.println(Base64.encode(parser.getCertificatesAndKeys().get(0).second().getEncoded()));

    }
}
