package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.EAsnUtil;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EEncryptedContentInfo;
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
import tr.gov.tubitak.uekae.esya.api.crypto.BufferedCipher;
import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;
import tr.gov.tubitak.uekae.esya.asn.cms.*;
import tr.gov.tubitak.uekae.esya.asn.x509.AlgorithmIdentifier;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;


/**
 * CmsEnvelopeStreamParser is used to open the envelopes from an inputstream
 * @author muratk
 */

public class CmsEnvelopeStreamParser extends ParserBase
{
	private Asn1BerInputStream mSifrelenmisVeri = null;
	private OutputStream mCozulmusVeri = null;
	private Asn1BerOutputStream mUpdatedVeri = null;
	private byte[] finalBytesBuff = null;

	private Cipher mSimetrikKripto;
	private CipherAlg mCipherAlg;

	private IDecryptorStore mDecryptorStore = null;

	private int mContentInfoLen = 0;
	private int mContentLen = 0;
	private int mEnvDataLen = 0;
	AlgorithmParams mparams;


	/**
	 * Constructor for the CmsEnvelopeStreamParser
	 * @param aEnvelopedData  Enveloped Data as input stream
	 */
	public CmsEnvelopeStreamParser(InputStream aEnvelopedData) 
	{
		mSifrelenmisVeri = new Asn1BerInputStream(aEnvelopedData);
	}

	private void _readStreamUntilEncryptedContent() throws Asn1Exception, IOException, CryptoException
	{
		if(mEnvelopeData == null)
	    {
		   mEnvelopeData = new EEnvelopedData(new EnvelopedData());
	    }

		//contentinfo taglen
		try {
			mContentInfoLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);
		} catch (Exception e) {
			throw new NotEnvelopeDataException("Can not decode contentType! The file is not int the CMSEnvelope Format!", e);
		}

		//contenttype
		Asn1ObjectIdentifier contentTypeObjectIdentifier = _readContentType();

		int[] oid = contentTypeObjectIdentifier.value ;
		if (!(Arrays.equals(oid, _cmsValues.id_ct_authEnvelopedData) ||
				Arrays.equals(oid, _cmsValues.id_envelopedData)))
			throw new NotEnvelopeDataException("Encrypted content is not in Enveloped-data content-type! OID:" +  OIDUtil.toURN(oid));


		mContentInfo.setContentType(contentTypeObjectIdentifier);

		mContentLen = mSifrelenmisVeri.decodeTagAndLength(new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0));

		mEnvDataLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);

		mEnvelopeData.setVersion((int)_readVersiyon().value);

		mEnvelopeData.setOriginatorInfo(_readOriginatorInfo());

		mEnvelopeData.setRecipientInfos(_readRecipientInfo());

		int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);

		mEnvelopeData.setEncryptedContentInfo(new EEncryptedContentInfo(new EncryptedContentInfo()));

		mEnvelopeData.getEncryptedContentInfo().getObject().contentType = _readContentType();

		//contentEncryptionAlgorithm oku
		_readContentEncryptionAlgorithm();

	}

	private void _skipEncryptedContent() throws Asn1Exception, IOException, CryptoException 
	{
		int encryptedContentInfoLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);

		mEnvelopeData.setEncryptedContentInfo(new EEncryptedContentInfo(new EncryptedContentInfo()));

		mEnvelopeData.getEncryptedContentInfo().getObject().contentType = _readContentType();

		_readContentEncryptionAlgorithm();

		Asn1Tag tag = mSifrelenmisVeri.peekTag();
		Asn1Tag primitive = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 0);
		Asn1Tag construct = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);

		int encryptedContentLen = 0;
		if(tag.mForm == Asn1Tag.PRIM)
		{
			encryptedContentLen = mSifrelenmisVeri.decodeTagAndLength(primitive);
			mSifrelenmisVeri.skip(encryptedContentLen);
		}
		else
		{
			encryptedContentLen = mSifrelenmisVeri.decodeTagAndLength(construct);
			tag = mSifrelenmisVeri.peekTag();
			while(tag != null && tag.equals(Asn1OctetString.TAG))
			{
				int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1OctetString.TAG);
				byte [] buffer = new byte[len];
				mSifrelenmisVeri.read(buffer);
				tag = mSifrelenmisVeri.peekTag();
			}
		}

		if(encryptedContentLen == Asn1Status.INDEFLEN)
			EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);

		if(encryptedContentInfoLen == Asn1Status.INDEFLEN)
			EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);

	}

	private void _readStream() throws ESYAException, IOException {
		_readContentInfo();
	}

	private void _readContentInfo() throws ESYAException, IOException {
		if(mContentInfoLen == 0)
		{
			try {
				//contentinfo taglen
				mContentInfoLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);
				//contenttype
				Asn1ObjectIdentifier contentTypeObjectIdentifier = _readContentType();
				mContentInfo.setContentType(contentTypeObjectIdentifier);
			}catch (Exception ex){
				throw new ESYAException("Encrypted content is not in Enveloped-data content-type!", ex);
		    }
		}

		if(mEnvelopeData == null)
		{
			if (Arrays.equals(mContentInfo.getContentType().value, _cmsValues.id_ct_authEnvelopedData))
				mEnvelopeData = new EAuthenticatedEnvelopedData(new AuthEnvelopedData());
			else if((Arrays.equals(mContentInfo.getContentType().value, _cmsValues.id_envelopedData)))
				mEnvelopeData = new EEnvelopedData(new EnvelopedData());
			else
				throw new ESYAException("Encrypted content is not in Enveloped-data content-type. Content OID does not match!");
		}

		//content 
		_readContent();

		if(mContentInfoLen == Asn1Status.INDEFLEN)
			EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
	}

	private Asn1ObjectIdentifier _readContentType() throws Asn1Exception, IOException
	{
		int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1ObjectIdentifier.TAG);
		int[] oid = mSifrelenmisVeri.decodeOIDContents(len);
		return new Asn1ObjectIdentifier(oid);
	}

	private void _readContent() throws ESYAException, IOException {
		if(mContentLen == 0)
		{
			mContentLen = mSifrelenmisVeri.decodeTagAndLength(new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0));
		}

		try {
			_readEnvelopedData();
		}
		catch (Exception e) {
			throw new ESYAException("Can not decode EnvelopeData!",e);
		}

		if(mContentLen == Asn1Status.INDEFLEN)
			EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
	}

	//Asn1Exception, IOException, ESYAException
	private void _readEnvelopedData() throws ESYAException, IOException {
		if(mEnvDataLen == 0)
		{
			mEnvDataLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);
		}

		mEnvelopeData.setVersion((int)_readVersiyon().value);

		mEnvelopeData.setOriginatorInfo(_readOriginatorInfo());

		mEnvelopeData.setRecipientInfos(_readRecipientInfo());

		_readEncryptedContentInfo();

		mEnvelopeData.readAttrs(mSifrelenmisVeri);

		if(mEnvDataLen == Asn1Status.INDEFLEN)
			EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
	}

	private CMSVersion _readVersiyon() throws Asn1Exception, IOException
	{
		CMSVersion ver = new CMSVersion();
		ver.decode(mSifrelenmisVeri);
		return ver;
	}

	private OriginatorInfo _readOriginatorInfo() throws Asn1Exception, IOException
	{
		Asn1Tag tag = mSifrelenmisVeri.peekTag();
		Asn1Tag originarInfoTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);
		if(tag.equals(originarInfoTag))
		{
			int len = mSifrelenmisVeri.decodeTagAndLength(originarInfoTag);
			OriginatorInfo oi = new OriginatorInfo();
			oi.decode(mSifrelenmisVeri,false,len);
			return oi;
		}
		return null;

	}

	private RecipientInfos _readRecipientInfo() throws Asn1Exception, IOException, CryptoException
	{
		RecipientInfos recipientInfos = new RecipientInfos();

		recipientInfos.decode(mSifrelenmisVeri);

		return recipientInfos;
	}


	private void _readEncryptedContentInfo() throws Asn1Exception, IOException, ESYAException
	{
		if(mEnvelopeData.getEncryptedContentInfo().getObject() == null)
		{
			int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);

			mEnvelopeData.setEncryptedContentInfo(new EEncryptedContentInfo(new EncryptedContentInfo()));

			mEnvelopeData.getEncryptedContentInfo().setContentType(_readContentType());

			//contentEncryptionAlgorithm oku
			_readContentEncryptionAlgorithm();

			//simetrik algoritma ve recipientifoyu aldık, anahtarı çözelim
			SecretKey anahtar =_getSymmetricKey();

			//encryptedContent oku
			_readEncryptedContent(anahtar);

			if (len == Asn1Status.INDEFLEN)
				EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);

			mEnvelopeData.readAttrs(mSifrelenmisVeri);

			processTheLastRemainingCryptoOps();

			SecretKeyUtil.eraseSecretKey(anahtar);
		}
	}

	//Because of GCM operations, tag must be read first.
	//After reading the tag, the last remaining bytes will be decrypted.
	private void processTheLastRemainingCryptoOps() throws IOException, ESYAException {

		if(mCipherAlg.getMod() == Mod.GCM) {
			((ParamsWithGCMSpec) mparams).setTag(mEnvelopeData.getMac());
		}

		byte[] unpad = mSimetrikKripto.doFinal(finalBytesBuff);
		if (unpad != null)
			mCozulmusVeri.write(unpad, 0, unpad.length);
	}

	private void _readContentEncryptionAlgorithm() throws Asn1Exception, IOException
	{
		if(mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm == null)
		{
			AlgorithmIdentifier alg = new AlgorithmIdentifier();


			//AlgorithmIdentifier alg = new  EAlgorithmIdentifier(mCipherAlg.getOID(), params).getObject();
			alg.decode(mSifrelenmisVeri);
			mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm = alg;
		}
	}

	private SecretKey _getSymmetricKey() throws CMSException, IOException {
		if(mDecryptorStore != null)
			return getSymmetricKeyOfEnvelope(mDecryptorStore);

		throw new CMSException("Decryptor is not set");
	}


	private void _readEncryptedContent(SecretKey anahtar) throws Asn1Exception, IOException, CryptoException
	{

		//Simetrik Kriptonun anahtarini yerlestirelim
		AlgorithmIdentifier algID = mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm;
		Pair<CipherAlg, AlgorithmParams> alg = CipherAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(algID));
		mCipherAlg = alg.first();
		mparams = alg.second();

		mSimetrikKripto = new BufferedCipher(getCryptoProvider().getDecryptor(mCipherAlg)).getInternalCipher();
		mSimetrikKripto.init(anahtar, mparams);


		int len = 0;
		Asn1Tag tag = mSifrelenmisVeri.peekTag();
		Asn1Tag primitive = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 0);
		Asn1Tag construct = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);

		if(tag.mForm == Asn1Tag.PRIM)
		{
			len = mSifrelenmisVeri.decodeTagAndLength(primitive);
			_readPrimitiveOctetString(len);
		}
		else
		{
			len = mSifrelenmisVeri.decodeTagAndLength(construct);
			_readConstructedOctetString();
		}

		if(len == Asn1Status.INDEFLEN)
			EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
	}

	private void _readPrimitiveOctetString(int aLen) throws CryptoException, IOException, Asn1Exception
	{
		byte[] decryptedData;
		byte[] buffer;

		int bufferSize = mSimetrikKripto.getBlockSize() * 10000;
		buffer = new byte [bufferSize];
		int loopCount = aLen / bufferSize;

		for(int i=0; i < loopCount; i++)
		{
			mSifrelenmisVeri.read(buffer);
			decryptedData = mSimetrikKripto.process(buffer);
			mCozulmusVeri.write(decryptedData, 0, decryptedData.length);
		}

		int lastBlockLen = aLen % bufferSize;
		buffer = new byte[lastBlockLen];
		mSifrelenmisVeri.read(buffer);

		finalBytesBuff = Arrays.copyOfRange(buffer, 0, buffer.length);
	}

	private void _readConstructedOctetString() throws Asn1Exception, IOException, CryptoException
	{
		//mSimetrikKripto.sifreCozBasla();
		byte[] encrypted = null;
		byte[] decrypted = null;

		Asn1Tag tag = mSifrelenmisVeri.peekTag();
		while(tag != null && tag.equals(Asn1OctetString.TAG))
		{
			int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1OctetString.TAG);
			encrypted = new byte[len];
			mSifrelenmisVeri.read(encrypted);

			tag = mSifrelenmisVeri.peekTag();

			if(tag == null || !tag.equals(Asn1OctetString.TAG))
			{
				finalBytesBuff = Arrays.copyOfRange(encrypted, 0, encrypted.length);
			}
			else
			{
				decrypted = mSimetrikKripto.process(encrypted);
				if (decrypted != null)
				 mCozulmusVeri.write(decrypted, 0, decrypted.length);
			}
		}
	}	

	private UnprotectedAttributes _readUnProtectedAttribute() throws Asn1Exception, IOException
	{
		int available = mSifrelenmisVeri.available();
		if(available > 0)
		{
			Asn1Tag tag = mSifrelenmisVeri.peekTag();
			Asn1Tag unProtectedAttributeTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 1);
			if(tag != null && tag.equals(unProtectedAttributeTag))
			{
				int len = mSifrelenmisVeri.decodeTagAndLength(unProtectedAttributeTag);
				UnprotectedAttributes ua = new UnprotectedAttributes();
				ua.decode(mSifrelenmisVeri,false,len);
				return ua;
			}
		}
		return null;
	}

	private void _writeStream() throws IOException, Asn1Exception, CryptoException
	{
		_writeContentInfo();
	}

	private void _writeContentInfo() throws IOException, Asn1Exception, CryptoException
	{
		//contentinfo taglen
		mUpdatedVeri.encodeTagAndIndefLen(Asn1Tag.SEQUENCE);

		//contenttype
		_writeContentType(OID_ENVELOPED_DATA);

		//content 
		_writeContent();

		mUpdatedVeri.encodeTagAndLength(Asn1Tag.EOC, 0);

	}

	private void _writeContentType(Asn1ObjectIdentifier aOID) throws Asn1Exception, IOException
	{
		mUpdatedVeri.encode(aOID, true);
	}

	private void _writeContent() throws Asn1Exception, IOException, CryptoException
	{
		mUpdatedVeri.encodeTagAndIndefLen(new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0));

		_writeEnvelopedData();

		mUpdatedVeri.encodeTagAndLength(Asn1Tag.EOC, 0);
	}

	private void _writeEnvelopedData() throws Asn1Exception, IOException, CryptoException
	{
		//taglen yaz
		mUpdatedVeri.encodeTagAndIndefLen(Asn1Tag.SEQUENCE);

		_writeVersion();

		_writeOriginatorInfo();

		_writeRecipientInfo();

		_writeEncryptedContentInfoWithoutContent();

		_transferRestOfInputStream();
	}

	private void _writeEncryptedContentInfoWithoutContent() throws IOException
	{
		//taglen yaz
		mUpdatedVeri.encodeTagAndIndefLen(Asn1Tag.SEQUENCE);
		//contentType yaz
		_writeContentType(OID_DATA);

		//contentEncryptionAlgorithm yaz
		_writeContentEncryptionAlgorithm();
	}

	private void _writeContentEncryptionAlgorithm() throws Asn1Exception, IOException
	{
		Asn1BerEncodeBuffer enc = new Asn1BerEncodeBuffer();
		mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm.encode(enc);
		mUpdatedVeri.write(enc.getMsgCopy());
	}


	private void _writeVersion() throws Asn1Exception, IOException
	{
		mUpdatedVeri.encode(new CMSVersion(mEnvelopeData.getVersion()), true);
	}

	private void _writeOriginatorInfo() throws Asn1Exception, IOException
	{
		if(mEnvelopeData.getOriginatorInfo() != null)
		{
			Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
			int len = mEnvelopeData.getOriginatorInfo().encode(buffer,false);
			buffer.encodeTagAndLength (Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
			mUpdatedVeri.write(buffer.getMsgCopy());
		}
	}

	private void _writeRecipientInfo() throws Asn1Exception, IOException
	{
		Asn1BerEncodeBuffer enc = new Asn1BerEncodeBuffer();
		mEnvelopeData.getRecipientInfos().encode(enc);
		mUpdatedVeri.write(enc.getMsgCopy());
	}

	private void _transferRestOfInputStream() throws IOException, Asn1Exception, CryptoException
	{
		int bufferSize = 32768; // 32K
		int remainLen = mSifrelenmisVeri.available();


		byte [] block = new byte[bufferSize];
		int loopCount = remainLen / bufferSize;
		for(int i=0; i < loopCount; i++)
		{
			mSifrelenmisVeri.read(block);
			mUpdatedVeri.write(block);
		}

		//read the bytes that less then buffer size
		remainLen = mSifrelenmisVeri.available();
		while (remainLen > 0)
		{
			byte[] remainData = new byte[remainLen];
			mSifrelenmisVeri.read(remainData);
			mUpdatedVeri.write(remainData);
			remainLen = mSifrelenmisVeri.available();
		}
	}

	/**
	 * Returns the recipient's IssuerAndSerialNumber or SubjectKeyIdentifier. 
	 * @return The recipients of the envelope. Return type can be tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber or
	 * tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier according to cms
	 * @throws CMSException If an error occurs while retrieving the recipients
	 */
	public Object[] getRecipientInfos() throws CMSException
	{
		try {
			_readStreamUntilEncryptedContent();
		} catch (Exception e) {
			throw new CMSException("Error in retrieving recipient infos",e);
		} 

		return _getRecipientsInEnvelope();
	}


	
	/**
	 * Opens the envelope
	 * @param aPlainData The outputstream where the envelope content will be written
	 * @param aDecryptorStore The decryptor of the recipient used to open the envelope
	 * @throws CMSException If an error occurs while opening the envelope
	 */
	public void open(OutputStream aPlainData, IDecryptorStore aDecryptorStore) throws CMSException
	{
		mCozulmusVeri = aPlainData;
		mDecryptorStore = aDecryptorStore;

		try {
			_readStream();

		} catch (Exception e) {
			throw new CMSException("Error in opening enveloped data",e);
		}

		try {
			//streamleri kapat
			mSifrelenmisVeri.close();
			mCozulmusVeri.close();
		} catch (IOException e) {
			throw new CMSException("Error in closing streams!",e);
		}
	}	

	/**
	 * adds new recipient. according to recipient type, default values will be filled to the required fields.
	 * @param aUpdatedEnvelope The outputstream where the new cms will be written
	 * @param aDecryptorStore the store of certs and keys
	 * @param aNewRecipientCerts the new recipient certificate
	 * @throws CMSException 
	 */
	protected void addRecipientInfo(EnvelopeConfig config, OutputStream aUpdatedEnvelope,IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws CMSException
	{
		try 
		{
			_readStreamUntilEncryptedContent();

			AlgorithmIdentifier algID = mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm;
			Pair<CipherAlg, AlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(algID));
			EnvelopeConfig.checkSymetricAlgoritmIsSafe(cipherAlg.first());

			SecretKey symmetricKey = getSymmetricKeyOfEnvelope(aDecryptorStore);

			addRecipientInfo(config, symmetricKey, aNewRecipientCerts);

			//it was done due to meet the requirements of crypto analysis
			SecretKeyUtil.eraseSecretKey(symmetricKey);

			mUpdatedVeri = new Asn1BerOutputStream(aUpdatedEnvelope);

			_writeStream();

			//streamleri kapat
			mSifrelenmisVeri.close();

			mUpdatedVeri.close();

		}
		catch (Exception e) 
		{
			throw new CMSException("Error in adding new recipient to the enveloped data",e);
		} 
	}




	/**
	 * adds key trans recipient.
	 * @param aUpdatedEnvelope The outputstream where the new cms will be written
	 * @param aDecryptorStore the store of certs and keys
	 * @param aNewRecipientCerts the new recipient certificate
	 * @throws CMSException
	 */
	protected void addKeyTransRecipientInfo(EnvelopeConfig config, OutputStream aUpdatedEnvelope,IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws ESYAException
	{
		mDecryptorStore = aDecryptorStore;

		addKeyTransRecipientInfo(config,aUpdatedEnvelope, aNewRecipientCerts);
	}

	
	
	private void addKeyTransRecipientInfo(EnvelopeConfig config,OutputStream aUpdatedEnvelope,ECertificate... aNewRecipientCerts)throws CMSException
	{
		try 
		{
			_readStreamUntilEncryptedContent();

			SecretKey symmetricKey = _getSymmetricKey();

			addTransRecipientInfo(config, symmetricKey, aNewRecipientCerts);

			//it was done due to meet the requirements of crypto analysis
			SecretKeyUtil.eraseSecretKey(symmetricKey);

			mUpdatedVeri = new Asn1BerOutputStream(aUpdatedEnvelope);

			_writeStream();

			//streamleri kapat
			mSifrelenmisVeri.close();

			mUpdatedVeri.close();
		} 
		catch (Exception e) 
		{
			throw new CMSException("Error in adding new recipient to the enveloped data",e);
		} 
	}

	public void addRecipients(EnvelopeConfig config, OutputStream aUpdatedEnvelope,IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws ESYAException
	{
		if(config.isCertificateValidationActive()) {
			for (ECertificate cer : aNewRecipientCerts) {
				CertificateStatusInfo csi = CertificateValidation.validateCertificate(config.getValidationSystem(), cer);
				if (csi.getCertificateStatus() != CertificateStatus.VALID) {
					throw new CertValidationException(csi);
				}
			}
		}

		addRecipientInfo(config, aUpdatedEnvelope,aDecryptorStore, aNewRecipientCerts);
	}


	/**
	 * 
	 * @param aUpdatedEnvelope The outputstream where the new cms will be writtens
	 * @param aDecryptorStore the store of certs and keys
	 * @param config includes the algorithm that will be used on the key aggrements
	 * @param aNewRecipientCerts the new recipient certificates
	 * @throws CMSException
	 */
	protected void addKeyAgreeRecipientInfo(EnvelopeConfig config, OutputStream aUpdatedEnvelope,IDecryptorStore aDecryptorStore, ECertificate... aNewRecipientCerts) throws ESYAException
	{
		mDecryptorStore = aDecryptorStore;

		addKeyAgreeRecipientInfo(aUpdatedEnvelope, config, aNewRecipientCerts);
	}


	private void addKeyAgreeRecipientInfo(OutputStream aUpdatedEnvelope, EnvelopeConfig config, ECertificate... aNewRecipientCerts)throws CMSException
	{
		try 
		{
			_readStreamUntilEncryptedContent();

			SecretKey symmetricKey = _getSymmetricKey();

			addKeyAgreeRecipientInfo(config, symmetricKey, aNewRecipientCerts);

			//it was done due to meet the requirements of crypto analysis
			SecretKeyUtil.eraseSecretKey(symmetricKey);

			mUpdatedVeri = new Asn1BerOutputStream(aUpdatedEnvelope);

			_writeStream();

			//streamleri kapat
			mSifrelenmisVeri.close();

			mUpdatedVeri.close();

		}
		catch (Exception e) 
		{
			throw new CMSException("Error in adding new recipient to the enveloped data",e);
		} 
	}



	/**
	 * Removes recipients from the envelope
	 * @param aUpdatedEnvelope The stream where the envelope without the removed recipients will be written
	 * @param aRemoveCertificates The certificated of the recipients to be removed from the envelope
	 * @throws CMSException  If an error occurs while removing recipients to the envelope
	 */
	public void removeRecipientInfo(OutputStream aUpdatedEnvelope, ECertificate... aRemoveCertificates) throws CMSException
	{
		try {
			_readStreamUntilEncryptedContent();

			_removeRecipientInfos(aRemoveCertificates);

			mUpdatedVeri = new Asn1BerOutputStream(aUpdatedEnvelope);

			_writeStream();

			//streamleri kapat
			mSifrelenmisVeri.close();

			mUpdatedVeri.close();
		} catch (Exception e) {
			throw new CMSException("Error in adding removing recipient from the enveloped data",e);		
		} 
	}
}
