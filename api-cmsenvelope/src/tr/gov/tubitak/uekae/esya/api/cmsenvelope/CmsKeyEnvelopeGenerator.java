package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.IEnvelopeData;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;

import javax.crypto.SecretKey;
import java.security.Key;


/**
 * CmsEnvelopeGenerator is used to generate the envelopes in the memory
 * @author muratk
 *
 */

public class CmsKeyEnvelopeGenerator extends GeneratorBase{

	private Key mSifrelenecek;
    private Wrapper wrapper;

	/**
	 * Constructor for CmsEnvelopeGenerator
	 * @param key The plain data that will be enveloped
	 * @param aAlgorithm The symmetric algorithm that will be used while enveloped generation.
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException
	 */
	public CmsKeyEnvelopeGenerator(Key key, CipherAlg aAlgorithm) throws ArgErrorException
	{
		super(aAlgorithm);
		if(aAlgorithm == null)
			throw new ArgErrorException("CipherAlg can not be null");
		mSifrelenecek = key;
		mSymmetricAlgorithm = aAlgorithm;
	}

	/**
	 * AES256_CBC is used as default cipher algorithm.
	 * @param key The plain data that will be enveloped
	 */
	public CmsKeyEnvelopeGenerator(Key key)
	{
		super(CipherAlg.AES256_CBC);
        mSifrelenecek = key;
		mSymmetricAlgorithm = CipherAlg.AES256_CBC;
	}



	/**
	 * Generates the envelope. A random symmetric key is generated.
	 * @return Encoded ContentInfo for enveloped data
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException If an error occurs while generating the envelope
	 */
	public byte[] generate() throws CMSException
	{
		SecretKey key = null;
		try
		{
			int keyLength = KeyUtil.getKeyLength(mSymmetricAlgorithm);
			key = getCryptoProvider().getKeyFactory().generateSecretKey(mSymmetricAlgorithm,keyLength); // check

            /*
            String keyLabel = "tmpSymKey"+System.currentTimeMillis();
            SecretKeySpec secretKeySpec = new SecretKeySpec(mSymmetricAlgorithm, keyLabel,KeyUtil.getKeyLength(mSymmetricAlgorithm));
            key = KeyUtil.generateSecretKey(secretKeySpec);
            */
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
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException If an error occurs while generating the envelope
	 */
	public byte[] generate(SecretKey aKey) throws CMSException
	{
		try 
		{
			_kriptoOrtakIsler(mSymmetricAlgorithm, aKey);

			byte [] sifreliVeri = wrapper.wrap(mSifrelenecek);
			
			_setEncryptedContent(sifreliVeri);
          
			_setEnvelopedData(mEnvelopeData);
			
			return mContentInfo.getEncoded();
		} 
		catch (Exception e) 
		{
			throw new CMSException("Error in generating enveloped data",e);
		}
	}

	protected void _kriptoOrtakIsler(CipherAlg aAlgoritma, SecretKey aAnahtar) throws ESYAException {
        //RFC 3852(Chapter 14 Security Considerations)
        //IV must be randomly generated.
        byte []iv = RandomUtil.generateRandom(aAlgoritma.getBlockSize());
        ParamsWithIV ivParams = new ParamsWithIV(iv);

		if(ByteUtil.isAllZero(iv))
			throw new CryptoException("IV must be a non-zero value");

        WrapAlg wrapAlg = KeyUtil.getConvenientWrapAlg(mSymmetricAlgorithm);

        wrapper = Crypto.getWrapper(wrapAlg);
        wrapper.init(aAnahtar, ivParams);

        //contentinfo type
        _setContentType();

        //version
        _setVersion();

        _recipientInfolariGuncelle(aAnahtar);

        //encryptedContentInfo
        //cms must contain iv info.
        _setEncryptedContent(null, aAlgoritma.toAlgorithmIdentifier(iv));
    }

	private void _setEnvelopedData(IEnvelopeData aZarfVeri) throws CMSException {
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
