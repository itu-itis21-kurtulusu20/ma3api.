package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.IDecryptorStore;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.ECDHDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.IDecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.params.RSADecryptorParams;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyAgreeRecipient;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyTransRecipient;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.*;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectKeyIdentifier;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.LinkedList;


/**
 * Şifre çözme işlemlerinde ortak kullanılan metodları içeren sınıftır.
 * @author muratk
 *
 */
abstract class ParserBase extends CMSEnvelopeBase
{
	private static Logger logger = LoggerFactory.getLogger(ParserBase.class);

	public static final Asn1ObjectIdentifier OID_ENVELOPED_DATA = new Asn1ObjectIdentifier(_cmsValues.id_envelopedData);
	public static final Asn1ObjectIdentifier OID_DATA = new Asn1ObjectIdentifier(_cmsValues.id_data);

	protected ParserBase()
	{

	}

	/**
	 * Returns the recipient's IssuerAndSerialNumber or SubjectKeyIdentifier in RecipientInfo array.
	 * @return return type can be tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber or
	 * tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier according to cms
	 */
	protected Object[] _getRecipientsInEnvelope()
	{
		RecipientInfo[] aRecipientInfos = mEnvelopeData.getRecipientInfos().elements;
		Object[] recipients = new Object[aRecipientInfos.length];
		for(int i = 0; i < aRecipientInfos.length; i++)
		{
			int choice = aRecipientInfos[i].getChoiceID();
			EIssuerAndSerialNumber isAndSerial = null;
			ESubjectKeyIdentifier keyIdentifier = null;
			switch(choice)
			{
				case RecipientInfo._KTRI:
					KeyTransRecipient ktr = new KeyTransRecipient((KeyTransRecipientInfo) aRecipientInfos[i].getElement());
					isAndSerial = ktr.getIssuerAndSerialNumber();
					if(isAndSerial == null)
						keyIdentifier = ktr.getSubjectKeyIdentifier();
					break;

				case RecipientInfo._KARI:
					KeyAgreeRecipient kar = new KeyAgreeRecipient((KeyAgreeRecipientInfo) aRecipientInfos[i].getElement());
					isAndSerial = kar.getIssuerAndSerialNumber();
					if(isAndSerial == null)
						keyIdentifier = kar.getSubjectKeyIdentifier();
					break;
				default:
					continue;
			}

			if(isAndSerial != null)
			{
				recipients[i] = isAndSerial;
			}else if(keyIdentifier != null)
			{
				recipients[i] = keyIdentifier;
			}
		}
		return recipients;
	}

	/**
	 * @param aRecipientInfos recipientinfo set
	 * @param aRequestedCert issuer and serial of the recipient that is looking for
	 * @return null if can not find the recipient
	 */
	protected RecipientInfo findRecipient(RecipientInfo[] aRecipientInfos, ECertificate aRequestedCert)
	{
		EIssuerAndSerialNumber reqIsAndSerial = new EIssuerAndSerialNumber(aRequestedCert);
		SubjectKeyIdentifier reqSubKeyIden = aRequestedCert.getExtensions().getSubjectKeyIdentifier().getObject();

		RecipientInfo recipient = null;
		for(int i = 0; i < aRecipientInfos.length; i++)
		{
			int choice = aRecipientInfos[i].getChoiceID();
			EIssuerAndSerialNumber isAndSerial = null;
			ESubjectKeyIdentifier keyIdentifier = null;
			switch(choice)
			{
				case RecipientInfo._KTRI:
					KeyTransRecipient ktr = new KeyTransRecipient((KeyTransRecipientInfo) aRecipientInfos[i].getElement());
					isAndSerial = ktr.getIssuerAndSerialNumber();
					if(isAndSerial == null)
						keyIdentifier = ktr.getSubjectKeyIdentifier();
					break;

				case RecipientInfo._KARI:
					KeyAgreeRecipient kar = new KeyAgreeRecipient((KeyAgreeRecipientInfo) aRecipientInfos[i].getElement());
					isAndSerial = kar.getIssuerAndSerialNumber();
					if(isAndSerial == null)
						keyIdentifier = kar.getSubjectKeyIdentifier();
					break;
				default:
					continue;
			}

			if(isAndSerial != null)
			{
				if(isAndSerial.equals(reqIsAndSerial))
				{
					recipient = aRecipientInfos[i];
					break;
				}

			}else if(keyIdentifier != null)
			{
				if(keyIdentifier.equals(reqSubKeyIden))
				{
					recipient = aRecipientInfos[i];
					break;
				}
			}
		}

		return recipient;

	}

	protected IDecryptorParams _getDecryptorParams(RecipientInfo[] aRecipientInfos,
												   ECertificate aRecipient) throws CryptoException
	{
		IDecryptorParams params = null;

		RecipientInfo recipient = findRecipient(aRecipientInfos, aRecipient);

		if(recipient != null)
		{
			switch(recipient.getChoiceID())
			{
				case RecipientInfo._KTRI:

					KeyTransRecipient ktr = new KeyTransRecipient((KeyTransRecipientInfo)recipient.getElement());
					EAlgorithmIdentifier encryptionAlgorithIdentifier = ktr.getEncryptionAlgorithmIdentifier();


					byte[] sifreliAnahtar = ktr.getEncryptedKey();
					RSADecryptorParams rsaParams = new RSADecryptorParams(sifreliAnahtar);
					rsaParams.setAlgorithmIdentifier(encryptionAlgorithIdentifier);
					params = rsaParams;
					break;

				case RecipientInfo._KARI:

					KeyAgreeRecipient kar = new KeyAgreeRecipient((KeyAgreeRecipientInfo) recipient.getElement());

					OriginatorPublicKey originatorSenderPublicKey = kar.getOriginatorSenderPublicKey();
					AlgorithmIdentifier senderPublicKeyAlgId = originatorSenderPublicKey.algorithm;

					byte [] wrappedKey = kar.getWrappedKey();
					int  [] keyEncAlgOid = kar.getKeyEncAlgOID();
					int  [] keyWrapAlgOid  =  kar.getkeyWrapAlgOID();
					byte [] senderPublicKey = kar.getSenderPublicKey();
					byte [] ukm = kar.getUKM();

					params = new ECDHDecryptorParams(wrappedKey, keyEncAlgOid, keyWrapAlgOid, senderPublicKeyAlgId,senderPublicKey, ukm);

					break;
			}
		}
		return params;
	}

	protected void addKeyAgreeRecipientInfo(EnvelopeConfig config, SecretKey symmetricKey, ECertificate... aRecipientCerts) throws CMSException
	{
		try
		{
			EAlgorithmIdentifier simAlgIdent = mEnvelopeData.getEncryptedContentInfo().getEncryptionAlgorithm();
			Pair<CipherAlg,AlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(simAlgIdent);
			WrapAlg wrapAlg = KeyUtil.getConvenientWrapAlg(cipherAlg.first());

			for(ECertificate aRecipientCer : aRecipientCerts)
			{
				checkLicense(aRecipientCer);
				KeyAgreeRecipient kar = new KeyAgreeRecipient(aRecipientCer.getObject(), config.getEcKeyAgreementAlg(), wrapAlg);

				kar.calculateAndSetEncyptedKey(symmetricKey);
				mEnvelopeData.addRecipientInfo(kar);
			}
		}
		catch (Exception e)
		{
			throw new CMSException("Error in adding new key agree recipient to the enveloped data",e);
		}
	}


	protected void addTransRecipientInfo(EnvelopeConfig config, SecretKey symmetricKey, ECertificate... aRecipientCerts) throws CMSException
	{
		try
		{
			for(ECertificate aRecipientCer : aRecipientCerts)
			{
				checkLicense(aRecipientCer);
				KeyTransRecipient ktr = new KeyTransRecipient(aRecipientCer.getObject(),config);

				ktr.calculateAndSetEncyptedKey(symmetricKey);
				mEnvelopeData.addRecipientInfo(ktr);

			}
		}
		catch (Exception e)
		{
			throw new CMSException("Error in adding new key trans recipient to the enveloped data",e);
		}
	}

	protected void addRecipientInfo(EnvelopeConfig config,SecretKey symmetricKey, ECertificate... aNewRecipientCerts) throws CMSException
	{
		for(ECertificate cer: aNewRecipientCerts)
		{
			int [] certAlg = cer.getObject().tbsCertificate.subjectPublicKeyInfo.algorithm.algorithm.value;
			//RSA
			if(Arrays.equals(certAlg, _algorithmsValues.rsaEncryption))
				addTransRecipientInfo(config,symmetricKey, cer);
				//EC
			else if(Arrays.equals(certAlg, _algorithmsValues.id_ecPublicKey))
				addKeyAgreeRecipientInfo(config, symmetricKey, cer);
		}
	}

	protected void _removeRecipientInfos(ECertificate... aRemoveCerts) throws CMSException
	{
		RecipientInfo[] copyOfRecipients = mEnvelopeData.getRecipientInfos().elements.clone();

		LinkedList<RecipientInfo> newRIs = new LinkedList<RecipientInfo>(Arrays.asList(copyOfRecipients));

		for(ECertificate removeCert : aRemoveCerts)
		{
			RecipientInfo [] recipients = newRIs.toArray(new RecipientInfo[0]);
			RecipientInfo ri = findRecipient(recipients, removeCert);

			newRIs.remove(ri);
		}

		if (newRIs.isEmpty())
			throw new CMSException("There must be at least one Recipient");

		RecipientInfo[] tRIArr = newRIs.toArray(new RecipientInfo[0]);

		mEnvelopeData.setRecipientInfos(new RecipientInfos(tRIArr));
	}

	protected SecretKey getSymmetricKeyOfEnvelope(IDecryptorStore aDecryptor) throws CMSException {
		try
		{
			RecipientInfo [] recipientinfos = mEnvelopeData.getRecipientInfos().elements;
			ECertificate [] certs =  aDecryptor.getEncryptionCertificates();

			for(ECertificate decryptorCert:certs)
			{
				IDecryptorParams params = _getDecryptorParams(recipientinfos, decryptorCert);
				logAlgorithm(params);
				if (params != null)
				{
					checkLicense(decryptorCert);
					return aDecryptor.decrypt(decryptorCert, params);
				}
			}
		}
		catch(CryptoException ce)
		{
			throw new CMSException("Can not extract symmetric key from envelope",ce);
		}

		throw new CMSException("No key found for opening enveloped data");
	}

	protected IssuerAndSerialNumber getIssuerAndSerialNumber(Certificate cert)
	{
		return new IssuerAndSerialNumber(cert.tbsCertificate.issuer, cert.tbsCertificate.serialNumber);
	}

	private void logAlgorithm(IDecryptorParams params) {
		try {
			if (logger.isDebugEnabled()) {
				if (params instanceof RSADecryptorParams) {
					String cipherAlg = ((RSADecryptorParams) params).getCipherAlg().toString();
					logger.debug("Decryption algorithm: " + cipherAlg);
				}
			}
		}
		catch (Exception ex){
			logger.error("Error while getting algorithm!", ex);
		}

	}


}