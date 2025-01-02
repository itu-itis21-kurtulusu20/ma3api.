package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.IEnvelopeData;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;

import javax.crypto.SecretKey;


/**
 * CmsEnvelopeGenerator is used to generate the envelopes in the memory
 * @author muratk
 *
 */

public class CmsEnvelopeGenerator  extends GeneratorBase{
	
	private byte[] mSifrelenecek;


	/**
	 * Constructor for CmsEnvelopeGenerator
	 * @param aPlainData The plain data that will be enveloped
	 * @param aAlgorithm The symmetric algorithm that will be used while enveloped generation.
	 * @throws ArgErrorException 
	 */
	public CmsEnvelopeGenerator(byte[] aPlainData,CipherAlg aAlgorithm) throws ArgErrorException
	{
		super(aAlgorithm);
		if(aAlgorithm == null)
			throw new ArgErrorException("CipherAlg can not be null");
		mSifrelenecek = aPlainData;
		mSymmetricAlgorithm = aAlgorithm;
	}

	/**
	 * AES256_CBC is used as default cipher algorithm.
	 * @param aPlainData The plain data that will be enveloped
	 */
	public	CmsEnvelopeGenerator(byte[] aPlainData)
	{
		super(CipherAlg.AES256_CBC);
		mSifrelenecek = aPlainData;
		mSymmetricAlgorithm = CipherAlg.AES256_CBC;
	}

	/**
	 * Generates the envelope. A random symmetric key is generated.
	 * @return Encoded ContentInfo for enveloped data
	 * @throws CMSException If an error occurs while generating the envelope
	 */
	public byte[] generate() throws CMSException
	{
		SecretKey key = null;
		try 
		{
			int keyLength = KeyUtil.getKeyLength(mSymmetricAlgorithm);
			key = getCryptoProvider().getKeyFactory().generateSecretKey(mSymmetricAlgorithm,keyLength); // check
		} 
		catch (CryptoException e) 
		{
			throw new CMSException("Can not randomize symmetric key", e);
		}

		if(ByteUtil.isAllZero(key.getEncoded()))
			throw new CMSException("The key must be a non-zero value");
		else
		    return generate(key);
	}

	/**
	 * Generates the envelope. The given symmetric algorithm and key is used for symmetric encryption 
	 * @param aKey The key of the symmetric algorithm
	 * @return Encoded ContentInfo for enveloped data
	 * @throws CMSException If an error occurs while generating the envelope
	 */
	public byte[] generate(SecretKey aKey) throws CMSException
	{
		byte [] sifreliVeri = null;
		try 
		{
			EnvelopeConfig.checkSymetricAlgoritmIsSafe(mSymmetricAlgorithm);

			_kriptoOrtakIsler(mSymmetricAlgorithm, aKey);

			sifreliVeri = mSymmetricCrypto.doFinal(mSifrelenecek);

			if(mSymmetricAlgorithm.getMod() == Mod.GCM){
				mEnvelopeData.setMac(((ParamsWithGCMSpec) mSymmetricAlgParams).getTag());
			}

			_setEncryptedContent(sifreliVeri);
			
			_setEnvelopedData(mEnvelopeData);
			
			return mContentInfo.getEncoded();
		} 
		catch (Exception e) 
		{
			throw new CMSException("Error in generating enveloped data",e);
		}
	}

	private void _setEnvelopedData(IEnvelopeData aZarfVeri) throws CMSException{
		mContentInfo.setContent(aZarfVeri.getEncoded());
	}

	private void _setEncryptedContent(byte[] aSifreliIcerik)
	{
		if(aSifreliIcerik != null)
		{
			mEnvelopeData.getEncryptedContentInfo().setEncryptedContent(aSifreliIcerik);
		}
	}

}
