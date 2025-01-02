package tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.ECDHDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.IDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.RSADecryptorParams;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.CipherUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MSCerStoreDecryptor implements IDecryptorStore 
{
	private KeyStore mKS;

	public MSCerStoreDecryptor() throws CMSException 
	{
		try 
		{
			mKS = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
			mKS.load(null);
		} 
		catch (Exception e) 
		{
			throw new CMSException("Error in loading MS Certificate Store",e);
		} 

	}

	public SecretKey decrypt(ECertificate aCert,IDecryptorParams aParams) throws CryptoException
	{
        SecretKey decrypted = null;
		if(aParams instanceof RSADecryptorParams)
		{
			try 
			{
				Asn1DerEncodeBuffer buf = new Asn1DerEncodeBuffer();
				aCert.getObject().encode(buf);

				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				java.security.cert.Certificate sunCert = cf.generateCertificate(new ByteArrayInputStream(buf.getMsgCopy()));

				String alias = mKS.getCertificateAlias(sunCert);
				PrivateKey key = (PrivateKey) mKS.getKey(alias, null);

				byte[] encrypted = ((RSADecryptorParams)aParams).getEncryptedKey();
				Cipher cipher = Cipher.getInstance("RSA", "SunMSCAPI");
				cipher.init(Cipher.UNWRAP_MODE, key);
				decrypted = (SecretKey) cipher.unwrap(encrypted, "RSA", Cipher.SECRET_KEY);
			} 
			catch (Exception e) 
			{
				throw new CMSException("Decryption failed", e);
			}
		}
		else if(aParams instanceof ECDHDecryptorParams)
		{
			throw new CryptoException("SunMSCAPI has no support to ECDH");
				
		}
		return decrypted;
	}
	

	public ECertificate [] getEncryptionCertificates() throws CMSException 
	{
		try 
		{
			List<ECertificate> list = new ArrayList<ECertificate>();

			Enumeration<String> aliases = mKS.aliases();

			while (aliases.hasMoreElements()) 
			{
				String alias = aliases.nextElement();

				if (mKS.entryInstanceOf(alias, PrivateKeyEntry.class)) 
				{
					X509Certificate c = (X509Certificate) mKS.getCertificate(alias);

					ECertificate esyaCer = new ECertificate(c.getEncoded());

					list.add(esyaCer);
				}
			}

			return list.toArray(new ECertificate[0]);

		} 
		catch (Exception e) 
		{
			throw new CMSException("Retrieval of certificates with private keys failed", e);
		}

	}

	public byte[] decrypt(CipherAlg simAlg, AlgorithmParams simAlgParams, byte[] encryptedContent, SecretKey anahtar) throws CryptoException {
		return CipherUtil.decrypt(simAlg,simAlgParams,encryptedContent,anahtar);
	}
}
