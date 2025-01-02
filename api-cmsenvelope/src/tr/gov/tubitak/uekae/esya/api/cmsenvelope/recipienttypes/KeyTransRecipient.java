package tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes;

import com.objsys.asn1j.runtime.Asn1OctetString;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.cmsenvelope.EnvelopeConfig;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.bag.Pair;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.OAEPPadding;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.asn.algorithms._algorithmsValues;
import tr.gov.tubitak.uekae.esya.asn.cms.CMSVersion;
import tr.gov.tubitak.uekae.esya.asn.cms.IssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.asn.cms.KeyTransRecipientInfo;
import tr.gov.tubitak.uekae.esya.asn.cms.RecipientIdentifier;
import tr.gov.tubitak.uekae.esya.asn.util.UtilCMS;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectKeyIdentifier;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Arrays;

@SuppressWarnings("serial")
public class KeyTransRecipient extends EnvelopeRecipient
{
	private KeyTransRecipientInfo ri = new KeyTransRecipientInfo();

	private PublicKey mPublicKey ;

	public KeyTransRecipient()
	{
		super();
		setElement(_KTRI, ri);
	}

	public KeyTransRecipient(KeyTransRecipientInfo aRI)
	{
		super();
		ri = aRI;
		setElement(_KTRI, ri);

	}

	public KeyTransRecipient(Certificate aRecipientCertificate, EnvelopeConfig config) throws CryptoException
	{
		super();
		setElement(_KTRI, ri);
		setRecipientIdentifier(UtilCMS.issuerAndSerialNumberOlustur(aRecipientCertificate));
		setKeyEncryptionAlgorithm(config.getRsaKeyTransAlg());

		PublicKey aPublicKey = decodePublicKey(aRecipientCertificate);
		setPublicKey(aPublicKey);
	}

	public void setCryptoProvider(CryptoProvider cryptoProvider) {
		this.cryptoProvider = cryptoProvider;
	}

	private PublicKey decodePublicKey(Certificate aRecipientCertificate) throws CryptoException {
		ESubjectPublicKeyInfo aSubjectPublicKeyInfo = new ESubjectPublicKeyInfo(aRecipientCertificate.tbsCertificate.subjectPublicKeyInfo);
		AsymmetricAlg alg = AsymmetricAlg.fromOID(aSubjectPublicKeyInfo.getAlgorithm().getAlgorithm().value);
		PublicKey aPublicKey = getCryptoProvider().getKeyFactory().decodePublicKey(alg,aSubjectPublicKeyInfo.getEncoded());

		return aPublicKey;
	}

	public void calculateAndSetEncyptedKey(SecretKey symmetricKey) throws CryptoException {
		Pair<CipherAlg, AlgorithmParams> cipher = CipherAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(ri.keyEncryptionAlgorithm));
		byte[] encryptedKey= null;
		CipherAlg cipherAlg = cipher.first();
		AlgorithmParams params = cipher.second();

		WrapAlg wrapperAlg = new WrapAlg(cipherAlg);
		Wrapper wrapper = getCryptoProvider().getWrapper(wrapperAlg);
		wrapper.init(mPublicKey, params);
		encryptedKey = wrapper.wrap(symmetricKey);

		encryptedKey = trimTheFirstLeadingZero(encryptedKey);

		setEncryptedKey(encryptedKey);

	}

	public void setEncryptedKey(byte[] aEncryptedKey)
	{
		ri.encryptedKey = new Asn1OctetString(aEncryptedKey);
	}

	public byte[] getEncryptedKey()
	{
		return ri.encryptedKey.value;
	}

	public void setKeyEncryptionAlgorithm(CipherAlg keyTransAlg) {
		AlgorithmIdentifier algID = null;

		if(Arrays.equals(keyTransAlg.getOID(), _algorithmsValues.rsaEncryption))
		{
			algID = new AlgorithmIdentifier(keyTransAlg.getOID());
		}
		else if(Arrays.equals(keyTransAlg.getOID(), _algorithmsValues.id_RSAES_OAEP))
		{
			byte [] params = ((OAEPPadding)keyTransAlg.getPadding()).toRSAES_OAEP_params();

			algID = new EAlgorithmIdentifier(keyTransAlg.getOID(), params).getObject();
		}
		else
		{
			throw new ESYARuntimeException("Unknown KeyTrans Algorithm !!!");
		}

		ri.keyEncryptionAlgorithm = algID;

	}

	public void setPublicKey(PublicKey aPublicKey)
	{
		mPublicKey = aPublicKey;
	}

	public void setRecipientIdentifier(IssuerAndSerialNumber aIssuerAndSerialNumber)
	{
		ri.rid = new RecipientIdentifier();
		ri.rid.set_issuerAndSerialNumber(aIssuerAndSerialNumber);
		//If the RecipientIdentifier is the CHOICE issuerAndSerialNumber, then the version MUST be 0.
		ri.version = new CMSVersion(0);
	}

	public void setRecipientIdentifier(SubjectKeyIdentifier aSubjectKeyIdentifier)
	{
		ri.rid = new RecipientIdentifier();
		ri.rid.set_subjectKeyIdentifier(aSubjectKeyIdentifier);
		//If the RecipientIdentifier is subjectKeyIdentifier, then the version MUST be 2.
		ri.version = new CMSVersion(2);
	}

	public EIssuerAndSerialNumber getIssuerAndSerialNumber()
	{
		int choice = ri.rid.getChoiceID();
		if(choice == RecipientIdentifier._ISSUERANDSERIALNUMBER)
		{
			return new EIssuerAndSerialNumber((IssuerAndSerialNumber) ri.rid.getElement());
		}
		return null;
	}

	public EAlgorithmIdentifier getEncryptionAlgorithmIdentifier()
	{
		return new EAlgorithmIdentifier(getRecipientInfo().keyEncryptionAlgorithm);
	}

	public ESubjectKeyIdentifier getSubjectKeyIdentifier()
	{
		int choice = ri.rid.getChoiceID();
		if(choice == RecipientIdentifier._SUBJECTKEYIDENTIFIER)
		{
			return new ESubjectKeyIdentifier((SubjectKeyIdentifier) ri.rid.getElement());
		}
		return null;
	}

	public KeyTransRecipientInfo getRecipientInfo()
	{
		return ri;
	}

	public PublicKey getPublicKey()
	{
		return mPublicKey;
	}


	public CMSVersion getCMSVersion()
	{
		return ri.version;
	}

	public byte[] trimTheFirstLeadingZero(byte[] encryptedKey) {

		if((encryptedKey.length %8) == 1 && encryptedKey[0] == 0) {
			byte[] keyTrimmed = new byte[encryptedKey.length-1];
			System.arraycopy(encryptedKey, 1, keyTrimmed, 0, encryptedKey.length - 1);
			return keyTrimmed;
		}
        else
        	return encryptedKey;
	}

}
