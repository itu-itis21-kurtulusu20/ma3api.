package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.EEnvelopedData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.SCKeyUnwrapperStore;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.keytemplate.KeyTemplate;

import javax.crypto.SecretKey;
import java.security.Key;


/**
 * CmsEnvelopeParser is used to open the envelopes in the memory
 * @author muratk
 *
 */

public class CmsKeyEnvelopeParser extends ParserBase
{
	/**
	 * Constructor for the CmsEnvelopeParser
	 * @param aEnvelopedData Enveloped Data as byte array
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException If an error occurs while parsing the envelope
	 */
	public CmsKeyEnvelopeParser(byte[] aEnvelopedData) throws CMSException
	{
		try {

			mContentInfo = new EContentInfo(aEnvelopedData);

			mEnvelopeData = new EEnvelopedData(mContentInfo.getContent());

		} catch (Exception e) {
			throw new CMSException("Error in parsing encrypted value",e);
		}
	}

	/**
	 * Retrieves the recipient's IssuerAndSerialNumber or SubjectKeyIdentifier.
	 * @return The recipients of the envelope. Return type can be tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber or
	 * tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier according to cms.
	 */
	public Object[] getRecipientInfos()
	{
		return _getRecipientsInEnvelope();
	}



	/**
	 * Opens the envelope
	 * @param aDecryptorStore The decryptor of the recipient used to open the envelope
	 * @return The content of the envelope
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException If an error occurs while opening the envelope
	 */
	public Key open(SCKeyUnwrapperStore aDecryptorStore, KeyTemplate unwrappedKeyTemplate) throws CMSException
	{
		try
		{
			SecretKey anahtar = getSymmetricKeyOfEnvelope(aDecryptorStore);
			if(anahtar == null)
				throw new CMSException("No key found for opening enveloped data");
			EAlgorithmIdentifier simAlg = mEnvelopeData.getEncryptedContentInfo().getEncryptionAlgorithm();
            return aDecryptorStore.unwrapKey(simAlg, mEnvelopeData.getEncryptedContentInfo().getEncryptedContent(), anahtar, unwrappedKeyTemplate);
		}
		catch (Exception e)
		{
			throw new CMSException("Error in opening enveloped data",e);
		}

	}


	/**
	 * adds new recipient. according to recipient type, default values will be filled to the required fields.
	 * @param aDecryptorStore the store of certs and keys
	 * @param aNewRecipientCerts the new recipients' certificate
	 * @return
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException
	 */
	protected byte[] addRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws CMSException {
		SecretKey symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
		addRecipientInfo(config, symmetricKey, aNewRecipientCerts);
		mContentInfo.setContent(mEnvelopeData.getEncoded());
		return _getContentInfoAsEncoded();
	}



	/**
	 *
	 * @param aDecryptorStore the store of certs and keys
	 * @param agreementAlg  algorithm that will be used on the key aggrement
	 * @param aNewRecipientCerts the new recipient's certificates
	 * @return
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException
	 */
	public byte[] addKeyAgreeRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, KeyAgreementAlg agreementAlg,ECertificate... aNewRecipientCerts) throws CMSException {
		SecretKey symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
		addKeyAgreeRecipientInfo(config, symmetricKey, aNewRecipientCerts);
		mContentInfo.setContent(mEnvelopeData.getEncoded());
		return _getContentInfoAsEncoded();
	}


	/**
	 * Adds new recipients to the envelope
	 * @param aDecryptorStore The decryptor of the recipient used to open the envelope
	 * @param aNewRecipientCerts The certificates of the new recipients
	 * @return The envelope with new recipients
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException  If an error occurs while adding recipients to the envelope
	 */
	public byte[] addKeyTransRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws CMSException {
		SecretKey symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
		addTransRecipientInfo(config, symmetricKey, aNewRecipientCerts);
		mContentInfo.setContent(mEnvelopeData.getEncoded());
		return _getContentInfoAsEncoded();
	}

	/**
	 * Removes recipients from the envelope
	 * @param aRemoveCertificate The certificated of the recipients to be removed from the envelope
	 * @return The envelope without the removed recipients
	 * @throws tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException If an error occurs while removing recipients to the envelope
	 */
	public byte[] removeRecipientInfo(ECertificate... aRemoveCertificate) throws CMSException
	{
		
		_removeRecipientInfos(aRemoveCertificate);
		try 
		{
			mContentInfo.setContent(mEnvelopeData.getEncoded());
			return _getContentInfoAsEncoded();
		} 
		catch (Exception e) 
		{
			throw new CMSException("Error in adding removing recipient from the enveloped data",e);
		}
	}

	
	
	private byte[] _getContentInfoAsEncoded() 
	{
		return mContentInfo.getEncoded();
	}	
}
