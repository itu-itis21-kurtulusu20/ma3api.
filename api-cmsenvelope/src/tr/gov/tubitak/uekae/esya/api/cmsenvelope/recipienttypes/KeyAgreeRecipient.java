package tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes;

import com.objsys.asn1j.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectPublicKeyInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyAgreement;
import tr.gov.tubitak.uekae.esya.api.crypto.KeyPairGenerator;
import tr.gov.tubitak.uekae.esya.api.crypto.Wrapper;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.AsymmetricAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.KeyAgreementAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.WrapAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithECParameterSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithSharedInfo;
import tr.gov.tubitak.uekae.esya.api.crypto.provider.CryptoProvider;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.api.smartcard.util.ECUtil;
import tr.gov.tubitak.uekae.esya.asn.cms.*;
import tr.gov.tubitak.uekae.esya.asn.util.UtilCMS;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.asn.x509.Certificate;
import tr.gov.tubitak.uekae.esya.asn.x509.SubjectKeyIdentifier;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.ECParameterSpec;

@SuppressWarnings("serial")
public class KeyAgreeRecipient extends EnvelopeRecipient
{
	private static Logger logger = LoggerFactory.getLogger(KeyAgreeRecipient.class);

	private KeyAgreeRecipientInfo ri = new KeyAgreeRecipientInfo();
	
	private Certificate mRecipientCert;
	private KeyAgreementAlg mKeyAgreementAlg;
	private WrapAlg mWrapAlg;



	public void setCryptoProvider(CryptoProvider cryptoProvider) {
		this.cryptoProvider = cryptoProvider;
	}

	public KeyAgreeRecipient()
	{
		super();
		_ilkIsler();
	}
	
	public KeyAgreeRecipient(KeyAgreeRecipientInfo aRI)
	{
		super();
		ri = aRI;
		setElement(_KARI, ri);
		ri.version = new CMSVersion(3);
	}
	
	
	
	/***
	 *  orcun.ertugrul
	 *  Gereksiz C(1,1) sheme'sı kullanılacağına göre göndericinin sertifikası olmayacak.
	 *  Ephemeral Key yaratılacak. Alıcının sertifikası yeterli
	 * @throws CryptoException 
	 */
	/*public KeyAgreeRecipient(Certificate aSenderCertificate, Certificate... aRecipientCertificates) throws CryptoException
	{
		super();
		_ilkIsler();
		ECDSAPublicKey pk = (ECDSAPublicKey) KeyUtil.decodePublicKey(new ESubjectPublicKeyInfo(aSenderCertificate.tbsCertificate.subjectPublicKeyInfo));
		OriginatorPublicKey opk =new OriginatorPublicKey(aSenderCertificate.tbsCertificate.subjectPublicKeyInfo.algorithm, new Asn1BitString(pk.getEncoded().length << 3,pk.getEncoded()));
		setOriginatorIdentifierOrKey(opk);
	}*/
	
	
	
	public KeyAgreeRecipient(Certificate aRecipientCertificate,KeyAgreementAlg aKeyAgreementAlg, WrapAlg keyWrapAlg) throws CryptoException
	{
		super();
		if(aKeyAgreementAlg.getAgreementAlg() != AgreementAlg.ECCDH && aKeyAgreementAlg.getAgreementAlg() != AgreementAlg.ECDH)
			throw new CryptoException("Unknown algorithms");
		
		_ilkIsler();
		
		mRecipientCert = aRecipientCertificate;
		mKeyAgreementAlg = aKeyAgreementAlg;
		mWrapAlg = keyWrapAlg;
	}

	private PublicKey decodePublicKey(Certificate aRecipientCertificate) throws CryptoException {
		ESubjectPublicKeyInfo aSubjectPublicKeyInfo = new ESubjectPublicKeyInfo(aRecipientCertificate.tbsCertificate.subjectPublicKeyInfo);
		AsymmetricAlg alg = AsymmetricAlg.fromOID(aSubjectPublicKeyInfo.getAlgorithm().getAlgorithm().value);
		PublicKey aPublicKey = null;
		aPublicKey = getCryptoProvider().getKeyFactory().decodePublicKey(alg,aSubjectPublicKeyInfo.getEncoded());

		return aPublicKey;
	}
	
	public void calculateAndSetEncyptedKey(SecretKey symmetricKey) throws CryptoException
	{
		KeyPair ephemeralKey = null;
		try 
		{
			KeyPairGenerator kpg = getCryptoProvider().getKeyPairGenerator(AsymmetricAlg.ECDSA);
            AlgorithmIdentifier algId = mRecipientCert.tbsCertificate.subjectPublicKeyInfo.algorithm;
            EAlgorithmIdentifier eAlgorithmIdentifier = new EAlgorithmIdentifier(algId);
            ECParameterSpec ecparamspec = tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.card.ec.ECParameters.decodeParameters(eAlgorithmIdentifier.getParameters().value);
            ParamsWithECParameterSpec paramsWithECParameterSpec = new ParamsWithECParameterSpec(ecparamspec);
            ephemeralKey = kpg.generateKeyPair(paramsWithECParameterSpec);
		}
		catch (Exception e) 
		{
			throw new CryptoException("Domain OID error", e);
		}
			
		
		//Generate shared info bytes that will be used in key derivation
		int keyLength = KeyUtil.getKeyLength(mWrapAlg);
		byte []sharedInfoBytes = ECUtil.generateKeyAgreementSharedInfoBytes(mWrapAlg.getOID(), keyLength, null);
		AlgorithmParams sharedInfoParam = new ParamsWithSharedInfo(sharedInfoBytes);

		PublicKey receiverPubKey  =  decodePublicKey(mRecipientCert);
		KeyAgreement agreement = null;
		agreement = getCryptoProvider().getKeyAgreement(mKeyAgreementAlg);

		agreement.init(ephemeralKey.getPrivate(), sharedInfoParam);
		SecretKey secretKeyBytes = agreement.generateKey(receiverPubKey, mWrapAlg);

		byte [] wrappedBytes = null;

		try 
		{
			Wrapper wrapper =null;
			wrapper = getCryptoProvider().getWrapper(mWrapAlg);
            wrapper.init(secretKeyBytes);
            wrappedBytes = wrapper.wrap(symmetricKey);
        }
		catch (Exception e) 
		{
			throw new CryptoException("Key wrapping error", e);
		}
		
		OriginatorPublicKey originator = _createOriginator(ephemeralKey.getPublic());
		_setOriginatorIdentifierOrKey(originator);
		_setEncryptedKey(UtilCMS.issuerAndSerialNumberOlustur(mRecipientCert),wrappedBytes);
		try 
		{
            _setKeyEncryptionAlgorithm(mKeyAgreementAlg.getOID(),mWrapAlg.getOID());
        }
		catch (Asn1Exception e) 
		{
			throw new CryptoException("Algorithm asn1 convention error ", e);
		}
	}

    /*
    RFC 5753 -[Page 5]
    originator MUST be the alternative originatorKey.  The
      originatorKey algorithm field MUST contain the id-ecPublicKey
      object identifier (see Section 7.1.2).  The parameters associated
      with id-ecPublicKey MUST be absent, ECParameters, or NULL.  The
      parameters associated with id-ecPublicKey SHOULD be absent or
      ECParameters, and NULL is allowed to support legacy
      implementations.  The previous version of this document required
      NULL to be present.  If the parameters are ECParameters, then they
      MUST be namedCurve.  The originatorKey publicKey field MUST
      contain the DER encoding of the value of the ASN.1 type ECPoint
      (see Section 7.2), which represents the sending agent's ephemeral
      EC public key.  The ECPoint in uncompressed form MUST be
      supported.
     */
	private OriginatorPublicKey _createOriginator(PublicKey publicKey) throws CryptoException {

		ESubjectPublicKeyInfo eSubjectPublicKeyInfo;
		try 
		{
            eSubjectPublicKeyInfo = new ESubjectPublicKeyInfo(publicKey.getEncoded());
		}
		catch (Exception e) 
		{
			throw new CryptoException("generating originator", e);
		}

        AlgorithmIdentifier algId = eSubjectPublicKeyInfo.getAlgorithm().getObject();
        byte[] publicKeyByte = eSubjectPublicKeyInfo.getSubjectPublicKey();
        Asn1BitString subjectPublicKeyBitStr = new Asn1BitString(publicKeyByte.length * 8, publicKeyByte);
        OriginatorPublicKey originator =  new OriginatorPublicKey(algId,subjectPublicKeyBitStr);
        
        return originator;
	}

	public void setOriginatorIdentifierOrKey(IssuerAndSerialNumber aIssuerAndSerialNumber)
	{
		ri.originator = new OriginatorIdentifierOrKey();
		ri.originator.set_issuerAndSerialNumber(aIssuerAndSerialNumber);
	}
	
	public void setOriginatorIdentifierOrKey(SubjectKeyIdentifier aSubjectKeyIdentifier)
	{
		ri.originator = new OriginatorIdentifierOrKey();
		ri.originator.set_subjectKeyIdentifier(aSubjectKeyIdentifier);
	}
	
	private void _setOriginatorIdentifierOrKey(OriginatorPublicKey aOriginatorPublicKey)
	{
		ri.originator = new OriginatorIdentifierOrKey();
		ri.originator.set_originatorKey(aOriginatorPublicKey);
	}
	
	private void _setUKM(byte [] aUKM)
	{
		ri.ukm = new Asn1OctetString(aUKM);
	}
	
	private void _setKeyEncryptionAlgorithm(int [] aKeyAgreementOid, int [] aKeyWrapOid) throws Asn1Exception
	{
		AlgorithmIdentifier keyWrapIdentifier = new AlgorithmIdentifier(aKeyWrapOid, new Asn1OpenType(new byte[]{5,0}));
		Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
		keyWrapIdentifier.encode(buff);

		ri.keyEncryptionAlgorithm = new AlgorithmIdentifier(aKeyAgreementOid,new Asn1OpenType(buff.getMsgCopy()));
	}
	
	
	
	private void _setEncryptedKey(IssuerAndSerialNumber receipientIssuerSerial,byte [] encryptedKey)
	{
		KeyAgreeRecipientIdentifier recipientIdentifier = new KeyAgreeRecipientIdentifier();
		recipientIdentifier.set_issuerAndSerialNumber(receipientIssuerSerial);
		ri.recipientEncryptedKeys = new RecipientEncryptedKeys(1);
		ri.recipientEncryptedKeys.elements[0] = new RecipientEncryptedKey(recipientIdentifier, new Asn1OctetString(encryptedKey));
	}
	
	public byte [] getSenderPublicKey()
	{
		int choice = ri.originator.getChoiceID();
		if(choice == OriginatorIdentifierOrKey._ORIGINATORKEY)
		{
			return ((OriginatorPublicKey) ri.originator.getElement()).publicKey.value;
		}
		return null;
	}

    public OriginatorPublicKey getOriginatorSenderPublicKey()
    {
        int choice = ri.originator.getChoiceID();
        if(choice == OriginatorIdentifierOrKey._ORIGINATORKEY)
        {
            return ((OriginatorPublicKey) ri.originator.getElement());
        }
        return null;
    }
	
	public EIssuerAndSerialNumber getIssuerAndSerialNumber()
	{
		KeyAgreeRecipientIdentifier recipientIdentifier = ri.recipientEncryptedKeys.elements[0].rid;
		if(recipientIdentifier.getChoiceID() == KeyAgreeRecipientIdentifier._ISSUERANDSERIALNUMBER)
			return new EIssuerAndSerialNumber((IssuerAndSerialNumber)recipientIdentifier.getElement()); 
		return null;
	}

	public ESubjectKeyIdentifier getSubjectKeyIdentifier()
	{
		KeyAgreeRecipientIdentifier recipientIdentifier = ri.recipientEncryptedKeys.elements[0].rid;
		if(recipientIdentifier.getChoiceID() == RecipientIdentifier._SUBJECTKEYIDENTIFIER)
		{
			return new ESubjectKeyIdentifier((SubjectKeyIdentifier) recipientIdentifier.getElement());
		}
		return null;
	}
	
	public byte [] getWrappedKey()
	{
		return ri.recipientEncryptedKeys.elements[0].encryptedKey.value;
	}
	
	public int [] getKeyEncAlgOID()
	{
		return ri.keyEncryptionAlgorithm.algorithm.value;
	}
	
	
	public int [] getkeyWrapAlgOID() throws CryptoException 
	{
		Asn1DerDecodeBuffer derBuffer = new Asn1DerDecodeBuffer(ri.keyEncryptionAlgorithm.parameters.value);
		AlgorithmIdentifier keyWrapAlgID = new AlgorithmIdentifier();
		
		try 
		{
			keyWrapAlgID.decode(derBuffer);
		} 
		catch (Asn1Exception e) 
		{
			throw new CryptoException("Wrap Algorithm can not be parsed",e);
		} 
		catch(IOException e)
		{
			throw new CryptoException("Wrap Algorithm can not be parsed",e);
		}
		
		return keyWrapAlgID.algorithm.value;
	}
	
	public byte [] getUKM()
	{
		if(ri.ukm != null)
			return ri.ukm.value;
		else
			return null;
	}

	
	private void _ilkIsler()
	{
		setElement(_KARI, ri);
		ri.version = new CMSVersion(3);
	}
	
	public CMSVersion getCMSVersion()
	{
		return ri.version;
	}


}
