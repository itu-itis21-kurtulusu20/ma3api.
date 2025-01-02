package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import com.objsys.asn1j.runtime.Asn1BerInputStream;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1Tag;
import tr.gov.tubitak.uekae.esya.api.asn.EAsnUtil;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.EAuthenticatedEnvelopedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.EEnvelopedData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.exception.CertValidationException;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;
import tr.gov.tubitak.uekae.esya.asn.cms._cmsValues;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


/**
 * CmsEnvelopeParser is used to open the envelopes in the memory
 * @author muratk
 *
 */

public class CmsEnvelopeParser extends ParserBase
{
	/**
	 * Constructor for the CmsEnvelopeParser
	 * @param aEnvelopedData Enveloped Data as byte array
	 * @throws CMSException If an error occurs while parsing the envelope
	 */
	public CmsEnvelopeParser(byte[] aEnvelopedData) throws CMSException
	{
		checkItIsEnvelopeDataFormat(aEnvelopedData);

		try
		{
			mContentInfo = new EContentInfo(aEnvelopedData);

			if(Arrays.equals(mContentInfo.getContentType().value, _cmsValues.id_ct_authEnvelopedData))
				mEnvelopeData = new EAuthenticatedEnvelopedData(mContentInfo.getContent());
			else if((Arrays.equals(mContentInfo.getContentType().value, _cmsValues.id_envelopedData)))
				mEnvelopeData = new EEnvelopedData(mContentInfo.getContent());
		}
		catch (Exception e)
		{
			throw new CMSException("Can not decode EnvelopeData!", e);
		}

		if (mEnvelopeData == null)
			throw new CMSException("Encrypted content is not in Enveloped-data content-type. Content OID does not match!");
	}


	public void checkItIsEnvelopeDataFormat(byte[] aEnvelopedData) throws CMSException {

		int[] oid = new int[0];
		try
		{
			byte [] oidBytes = Arrays.copyOfRange(aEnvelopedData, 0, 128);
			InputStream inputStream = new ByteArrayInputStream(oidBytes);
			Asn1BerInputStream stream = new Asn1BerInputStream(inputStream);

			int contentLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(stream, Asn1Tag.SEQUENCE);
			int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(stream, Asn1ObjectIdentifier.TAG);

			oid = stream.decodeOIDContents(len);
		}
		catch (Exception e)
		{
			throw new NotEnvelopeDataException("Can not decode contentType! The file is not int the CMSEnvelope Format!", e);
		}

		if (!(Arrays.equals(oid, _cmsValues.id_ct_authEnvelopedData) ||
				Arrays.equals(oid, _cmsValues.id_envelopedData)))
			throw new NotEnvelopeDataException("Encrypted content is not in Enveloped-data content-type! OID:" +  OIDUtil.toURN(oid));
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
	 * @throws CMSException If an error occurs while opening the envelope
	 */
	public byte[] open(IDecryptorStore aDecryptorStore) throws CMSException
	{
		try
		{
			SecretKey anahtar = getSymmetricKeyOfEnvelope(aDecryptorStore);
			byte[] decryptedBytes = null;

			if(anahtar == null)
				throw new CMSException("No key found for opening enveloped data");

			EAlgorithmIdentifier simAlgIden = mEnvelopeData.getEncryptedContentInfo().getEncryptionAlgorithm();

			Pair<CipherAlg, AlgorithmParams> cipherAlgAlgorithmParamsPair = CipherAlg.fromAlgorithmIdentifier(simAlgIden);
			CipherAlg simAlg = cipherAlgAlgorithmParamsPair.first();
			AlgorithmParams params = cipherAlgAlgorithmParamsPair.second();

			if(simAlg.getMod() == Mod.GCM)
				((ParamsWithGCMSpec)params).setTag(mEnvelopeData.getMac());

			decryptedBytes = aDecryptorStore.decrypt(simAlg, params, mEnvelopeData.getEncryptedContentInfo().getEncryptedContent(), anahtar);

			//it was done due to meet the requirements of crypto analysis
			SecretKeyUtil.eraseSecretKey(anahtar);
			return decryptedBytes;
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
	 * @throws CMSException
	 */
	protected byte[] addRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws CryptoException, IOException {
		AlgorithmIdentifier algID = mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm;
		Pair<CipherAlg, AlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(algID));
		EnvelopeConfig.checkSymetricAlgoritmIsSafe(cipherAlg.first());

		SecretKey symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
		addRecipientInfo(config, symmetricKey, aNewRecipientCerts);

		SecretKeyUtil.eraseSecretKey(symmetricKey);

		mContentInfo.setContent(mEnvelopeData.getEncoded());
		return _getContentInfoAsEncoded();
	}

	public byte[] addRecipients(EnvelopeConfig config, IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws ESYAException, IOException {
		if(config.isCertificateValidationActive()) {
			for (ECertificate cer : aNewRecipientCerts) {
				CertificateStatusInfo csi = CertificateValidation.validateCertificate(config.getValidationSystem(), cer);
				if (csi.getCertificateStatus() != CertificateStatus.VALID) {
					throw new CertValidationException(csi);
				}
			}
		}
		return addRecipientInfo(config, aDecryptorStore, aNewRecipientCerts);
	}

	/**
	 *
	 * @param aDecryptorStore the store of certs and keys
	 * @param config includes the algorithm that will be used on the key aggrement
	 * @param aNewRecipientCerts the new recipient's certificates
	 * @return
	 * @throws CMSException
	 */
	protected byte[] addKeyAgreeRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws ESYAException, IOException {
		SecretKey symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
		addKeyAgreeRecipientInfo(config, symmetricKey, aNewRecipientCerts);
		SecretKeyUtil.eraseSecretKey(symmetricKey);
		mContentInfo.setContent(mEnvelopeData.getEncoded());
		return _getContentInfoAsEncoded();
	}


	/**
	 * Adds new recipients to the envelope
	 * @param aDecryptorStore The decryptor of the recipient used to open the envelope
	 * @param aNewRecipientCerts The certificates of the new recipients
	 * @return The envelope with new recipients
	 * @throws CMSException  If an error occurs while adding recipients to the envelope
	 */
	protected byte[] addKeyTransRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws ESYAException, IOException {

		SecretKey symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
		addTransRecipientInfo(config,symmetricKey, aNewRecipientCerts);
		SecretKeyUtil.eraseSecretKey(symmetricKey);
		mContentInfo.setContent(mEnvelopeData.getEncoded());
		return _getContentInfoAsEncoded();
	}

	/**
	 * Removes recipients from the envelope
	 * @param aRemoveCertificate The certificated of the recipients to be removed from the envelope
	 * @return The envelope without the removed recipients
	 * @throws CMSException If an error occurs while removing recipients to the envelope
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
