package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.IEnvelopeData;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyTransRecipient;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.RandomUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.ISmartCard;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAKeyPairTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.asymmetric.rsa.RSAPublicKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.AESKeyTemplate;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.symmetric.SecretKeyTemplate;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientInfo;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;


/**
 * CmsEnvelopeGenerator is used to generate the envelopes in the memory
 * @author muratk
 *
 */

public class CmsSmartCardKeyEnvelopeGenerator extends GeneratorBase{

    private String labelOfKeyForEncrypt;

    ISmartCard smartCard;
    long sessionId;

    ParamsWithIV ivParams;
    //private Wrapper wrapper;

	/**
	 * Constructor for CmsEnvelopeGenerator
	 * @param smartCard The plain data that will be enveloped
	 * @param aAlgorithm The symmetric algorithm that will be used while enveloped generation.
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException
	 */
	public CmsSmartCardKeyEnvelopeGenerator(ISmartCard smartCard,long sessionId,String labelOfKeyForEncrypt, CipherAlg aAlgorithm) throws ArgErrorException
	{
		super(aAlgorithm);
		if(aAlgorithm == null)
			throw new ArgErrorException("CipherAlg can not be null");
        this.smartCard = smartCard;
        this.sessionId = sessionId;
        this.labelOfKeyForEncrypt = labelOfKeyForEncrypt;
		mSymmetricAlgorithm = aAlgorithm;
	}

	/**
	 * Generates the envelope. A random symmetric key is generated.
	 * @return Encoded ContentInfo for enveloped data
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException If an error occurs while generating the envelope
	 */
	public byte[] generate() throws CMSException
	{
        SecretKeyTemplate symetricKeyTemplate;
        try
        {
            String symetricKeyLabel= "tmpKey-"+System.currentTimeMillis();
            symetricKeyTemplate= new AESKeyTemplate(symetricKeyLabel,mSymmetricAlgorithm.getBlockSize());
            symetricKeyTemplate.getAsExportableTemplate();
            symetricKeyTemplate.getAsWrapperTemplate();
            smartCard.createSecretKey(sessionId,symetricKeyTemplate);
		}
		catch (Exception e)
		{
			throw new CMSException("Error creating symmetric key in smart card", e);
		}
		return generate(symetricKeyTemplate);
	}

	/**
	 * Generates the envelope. The given symmetric algorithm and key is used for symmetric encryption
	 * @param symetricKeyTemplate The key of the symmetric algorithm
	 * @return Encoded ContentInfo for enveloped data
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException If an error occurs while generating the envelope
	 */
	public byte[] generate(SecretKeyTemplate symetricKeyTemplate) throws CMSException
	{
		try 
		{
			_kriptoOrtakIsler(mSymmetricAlgorithm, symetricKeyTemplate);

            CK_MECHANISM symetricMechanism = new CK_MECHANISM(PKCS11Constants.CKM_AES_CBC);
            symetricMechanism.pParameter = ivParams.getIV();

            byte [] sifreliVeri = smartCard.wrapKey(sessionId,symetricMechanism,symetricKeyTemplate.getLabel(), labelOfKeyForEncrypt);
			
			_setEncryptedContent(sifreliVeri);
			
			_setEnvelopedData(mEnvelopeData);
			
			return mContentInfo.getEncoded();
		} 
		catch (Exception e) 
		{
			throw new CMSException("Error in generating enveloped data",e);
		}
	}

	protected void _kriptoOrtakIsler(CipherAlg aAlgoritma, SecretKeyTemplate symetricKeyTemplate) throws ESYAException
    {
        //RFC 3852(Chapter 14 Security Considerations)
        //IV must be randomly generated.
        byte []iv = RandomUtil.generateRandom(aAlgoritma.getBlockSize());
        ivParams = new ParamsWithIV(iv);

        //contentinfo type
        _setContentType();

        //version
        _setVersion();

        _recipientInfolariGuncelle(symetricKeyTemplate);

        //encryptedContentInfo
        //cms must contain iv info.
        _setEncryptedContent(null, aAlgoritma.toAlgorithmIdentifier(iv));
    }


	private void _setEnvelopedData(IEnvelopeData aZarfVeri) {
		mContentInfo.setContent(aZarfVeri.getEncoded());
	}

    protected void _recipientInfolariGuncelle(SecretKey aSifrelenecekAnahtar) throws CryptoException
    {
        SecretKeyTemplate symetricKeyTemplate = (SecretKeyTemplate) aSifrelenecekAnahtar;
        for(int i=0; i < mEnvelopeData.getRecipientInfoCount(); i++)
        {
            RecipientInfo ri = mEnvelopeData.getRecipientInfo(i);
            if (ri instanceof KeyTransRecipient){
                KeyTransRecipient ktr = (KeyTransRecipient)ri;
                try {
                    //Recipient public key'i hsm içine taşıyalım.
                    PublicKey publicKey = ktr.getPublicKey();
                    String wrapperKeyLabel = "tmpRecipientPublic-"+System.currentTimeMillis();
                    RSAPublicKeyTemplate publicKeyTemplate = new RSAPublicKeyTemplate(wrapperKeyLabel, (RSAPublicKey) publicKey);
                    publicKeyTemplate = publicKeyTemplate.getAsWrapperTemplate();
                    RSAKeyPairTemplate rsaKeyPairTemplate = new RSAKeyPairTemplate(publicKeyTemplate,null);
                    smartCard.importKeyPair(sessionId,rsaKeyPairTemplate);

                    CK_MECHANISM mechanism = new CK_MECHANISM(PKCS11Constants.CKM_RSA_PKCS);
                    byte[] smartCardWrapped = smartCard.wrapKey(sessionId,mechanism, wrapperKeyLabel,symetricKeyTemplate.getLabel());
                    ktr.setEncryptedKey(smartCardWrapped);

                } catch (Exception e) {
                    throw new CryptoException("Error while wrapping symetric key with user key",e);
                }
            }else{
                throw new CryptoException("Unsupported Recipient type for Key envelope with smart card");
            }
        }
    }
	

	private void _setEncryptedContent(byte[] aSifreliIcerik)
	{
		if(aSifreliIcerik != null)
		{
			mEnvelopeData.getEncryptedContentInfo().setEncryptedContent(aSifreliIcerik);
		}
	}
	
}
