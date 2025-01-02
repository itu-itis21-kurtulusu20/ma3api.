package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import com.objsys.asn1j.runtime.Asn1DerEncodeBuffer;
import com.objsys.asn1j.runtime.Asn1Integer;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EEncryptedContentInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.EAuthenticatedEnvelopedData;
import tr.gov.tubitak.uekae.esya.api.asn.cms.envelope.EEnvelopedData;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateStatus;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.CertificateValidation;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate.CertificateStatusInfo;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.exception.CertValidationException;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyAgreeRecipient;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes.KeyTransRecipient;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithIV;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.algorithms.GCMParameters;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.*;
import tr.gov.tubitak.uekae.esya.asn.x509.Attribute;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.CertificateList;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Şifreleme  işlemlerinde ortak kullanılan metodları içeren sınıftır. 
 * @author muratk
 *
 */

abstract class GeneratorBase extends CMSEnvelopeBase
{

	public static final Asn1ObjectIdentifier OID_DATA = new Asn1ObjectIdentifier(_cmsValues.id_data);
	public static final Asn1ObjectIdentifier OID_DHSINGLEPASS_STDH = new Asn1ObjectIdentifier(new int[]{1,3,133,16,840,63,0,2});

	protected CipherAlg mSymmetricAlgorithm;

	protected BufferedCipher mSymmetricCrypto;
	protected AlgorithmParams mSymmetricAlgParams;


	protected GeneratorBase(CipherAlg aAlgorithm)
	{
		if(aAlgorithm.getMod() == Mod.GCM)
			mEnvelopeData =  new EAuthenticatedEnvelopedData(new AuthEnvelopedData());
		else
			mEnvelopeData =  new EEnvelopedData(new EnvelopedData());
	}

	/**
	 * adds key tran recipient.
	 * @param aRecipients recipient certificate
	 * @throws CryptoException
	 */
	protected void addKeyTransRecipientInfo(EnvelopeConfig config, ECertificate...aRecipients) throws CryptoException {
		for(ECertificate cer: aRecipients)
		{
			checkLicense(cer);
			KeyTransRecipient keyTransRecipient = new KeyTransRecipient(cer.getObject(), config);
			keyTransRecipient.setCryptoProvider(cryptoProvider);
			mEnvelopeData.addRecipientInfo(keyTransRecipient);
		}
	}

    /**
     * adds key tran recipient.
     * @param keyTransRecipients key tran recipients
     * @throws CryptoException
     */
    protected void addKeyTransRecipientInfo(KeyTransRecipient...keyTransRecipients) throws CryptoException
    {
        for (KeyTransRecipient keyTransRecipient : keyTransRecipients) {
            mEnvelopeData.addRecipientInfo(keyTransRecipient);
        }
    }

	/**
	 * adds key agree recipient.
	 * @param config includes the algorithm that will be used on the key aggrement
	 * @param aRecipients recipient certificate
	 * @throws CryptoException
	 */

	protected void addKeyAgreeRecipientInfo( EnvelopeConfig config, ECertificate...aRecipients) throws CryptoException
	{
		for(ECertificate cer: aRecipients)
		{
			checkLicense(cer);

			WrapAlg wrapAlg = KeyUtil.getConvenientWrapAlg(mSymmetricAlgorithm);
			if(wrapAlg == null)
				throw new CryptoException("This symmetric algorithm is not supported for KeyAgreement");
			KeyAgreeRecipient keyAgreeRecipient = new KeyAgreeRecipient(cer.getObject(), config.getEcKeyAgreementAlg(), wrapAlg);
			keyAgreeRecipient.setCryptoProvider(cryptoProvider);
			mEnvelopeData.addRecipientInfo(keyAgreeRecipient);
		}
	}


	protected void addRecipientInfos(EnvelopeConfig config, ECertificate...aRecipients) throws CryptoException{
		for(ECertificate cer: aRecipients)
		{
			int [] certAlg = cer.getObject().tbsCertificate.subjectPublicKeyInfo.algorithm.algorithm.value;
			//RSA
			if(Arrays.equals(certAlg, _algorithmsValues.rsaEncryption))
				addKeyTransRecipientInfo(config, aRecipients);
				//EC
			else if(Arrays.equals(certAlg, _algorithmsValues.id_ecPublicKey))
				addKeyAgreeRecipientInfo(config, cer);
		}
	}

	/**
	 * Adds new recipient without certificate checking. According to recipient type, default values will be filled to the required fields.
	 * @param aRecipients recipient certificate
	 * @throws CryptoException
	 */
	protected void addRecipientInfos(ECertificate...aRecipients) throws CryptoException {
		addRecipientInfos(new EnvelopeConfig(), aRecipients);
	}

	/**
	 * Adds new recipient if the certificate is valid.
	 * @param aRecipients
	 * @throws ESYAException
     */
	public void addRecipients(EnvelopeConfig config, ECertificate...aRecipients) throws ESYAException {

		if(config.isCertificateValidationActive()) {
			for (ECertificate cer : aRecipients) {
				CertificateStatusInfo csi = CertificateValidation.validateCertificate(config.getValidationSystem(), cer);
				if (csi.getCertificateStatus() != CertificateStatus.VALID) {
					throw new CertValidationException(csi);
				}
			}
		}
		addRecipientInfos(config, aRecipients);
	}

	/**
	 * adds unprotected attribute
	 * @param aAttribute the attribute
	 */
	public void addUnProtectedAttribute(Attribute... aAttribute) throws ESYAException {
		if (mEnvelopeData.getUnprotectedAttributes() == null)
		{
			mEnvelopeData.setUnprotectedAttributes(new UnprotectedAttributes(aAttribute));
		} 
		else
		{
			ArrayList<Attribute> allAttribute = new ArrayList<Attribute>();
			List<Attribute> l1 = Arrays.asList(aAttribute);
			List<Attribute> l2 = Arrays.asList(mEnvelopeData.getUnprotectedAttributes().elements);
			allAttribute.addAll(l1);
			allAttribute.addAll(l2);
			mEnvelopeData.getUnprotectedAttributes().elements = allAttribute.toArray(new Attribute[0]);
		}
	}

	/**
	 * adds originator info
	 * @param aCertificates certificates that will be added to CMS
	 * @param aCRLs CRLs that will be added to CMS
	 */
	public void addOriginatorInfo(Certificate[] aCertificates, CertificateList[] aCRLs)
	{
		CertificateSet certSet = null;

		if (aCertificates != null)
		{
			CertificateChoices[] cChoices = new CertificateChoices[aCertificates.length];
			for (int i=0 ; i<aCertificates.length; i++)
			{
				cChoices[i] = new CertificateChoices(CertificateChoices._CERTIFICATE,aCertificates[i]);
			}
			certSet = new CertificateSet(cChoices);
		}

		RevocationInfoChoices revocationChoices = null;

		if (aCRLs != null)
		{
			RevocationInfoChoice[] rChoices = new RevocationInfoChoice[aCRLs.length]; 
			for (int i=0 ; i<aCRLs.length; i++)
			{
				rChoices[i] = new RevocationInfoChoice(RevocationInfoChoice._CRL, aCRLs[i]);
			}

			revocationChoices = new RevocationInfoChoices(rChoices);					
		}

		if (certSet != null || revocationChoices != null)
		{
			mEnvelopeData.setOriginatorInfo(new OriginatorInfo(certSet,revocationChoices));
		}
		
	}

	protected void _kriptoOrtakIsler(CipherAlg aAlgoritma, SecretKey aAnahtar) throws ESYAException, IOException {
		byte []iv = null;
		if(mSymmetricAlgParams == null) {
			iv = new byte[aAlgoritma.getBlockSize()];
		     getCryptoProvider().getRandomGenerator().nextBytes(iv);

			if (ByteUtil.isAllZero(iv))
				throw new CryptoException("IV must be a non-zero value");

			if(aAlgoritma.getMod() == Mod.CBC)
				mSymmetricAlgParams = new ParamsWithIV(iv);
			else if(aAlgoritma.getMod() == Mod.GCM)
				mSymmetricAlgParams = new ParamsWithGCMSpec(iv);
		}

		if(mSymmetricAlgParams != null && mSymmetricAlgParams instanceof  ParamsWithIV) {
			iv = ((ParamsWithIV) mSymmetricAlgParams).getIV();

		}

		if(cryptoProvider!=null) {
			mSymmetricCrypto = new BufferedCipher(cryptoProvider.getEncryptor(aAlgoritma));
		}
		else {
			mSymmetricCrypto = Crypto.getEncryptor(aAlgoritma);
		}
		mSymmetricCrypto.init(aAnahtar, mSymmetricAlgParams);

		//contentinfo type
		_setContentType();

		//version
		_setVersion();

		_recipientInfolariGuncelle(aAnahtar);

		//it was done due to meet the requirements of crypto analysis
		SecretKeyUtil.eraseSecretKey(aAnahtar);

		//encryptedContentInfo
		//cms must contain iv info.

		EAlgorithmIdentifier params;
		if(aAlgoritma.getMod() == Mod.GCM) {

			GCMParameters gcmParams = new GCMParameters(new Asn1OctetString(((ParamsWithGCMSpec)mSymmetricAlgParams).getIV()),new Asn1Integer((16)));
			Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
			gcmParams.encode(buff);

			params = new EAlgorithmIdentifier(mSymmetricAlgorithm.getOID(),buff.getMsgCopy());

		}
		else
			params = aAlgoritma.toAlgorithmIdentifier(iv);

		_setEncryptedContent(null, params);
	}

	protected void _setVersion(){
		mEnvelopeData.setVersion(_getVersion());
	}

	protected void _recipientInfolariGuncelle(SecretKey aSifrelenecekAnahtar) throws CryptoException{
		for(int i=0; i < mEnvelopeData.getRecipientInfoCount(); i++)
		{
			RecipientInfo ri = mEnvelopeData.getRecipientInfo(i);
			if (ri instanceof KeyTransRecipient){
				KeyTransRecipient ktr = (KeyTransRecipient)ri;
				ktr.calculateAndSetEncyptedKey(aSifrelenecekAnahtar);
			}else if (ri instanceof  KeyAgreeRecipient){
				KeyAgreeRecipient kar = (KeyAgreeRecipient)ri;
				kar.calculateAndSetEncyptedKey(aSifrelenecekAnahtar);
			}
		}
	}

	protected void _setEncryptedContent(byte[] aSifreliIcerik, EAlgorithmIdentifier aSifrelemeAlgoritmasi)
	{
		mEnvelopeData.setEncryptedContentInfo(
				new EEncryptedContentInfo(OID_DATA, aSifrelemeAlgoritmasi, aSifreliIcerik));

	}

	protected void _setContentType()
	{
		    mContentInfo.setContentType(mEnvelopeData.getOID());
	}


}
