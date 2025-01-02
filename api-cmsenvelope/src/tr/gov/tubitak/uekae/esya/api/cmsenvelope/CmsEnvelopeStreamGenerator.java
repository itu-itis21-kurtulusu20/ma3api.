package tr.gov.tubitak.uekae.esya.api.cmsenvelope;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.EAlgorithmIdentifier;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.util.ByteUtil;
import tr.gov.tubitak.uekae.esya.api.crypto.Cipher;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.CipherAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.Mod;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.ArgErrorException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CMSException;
import tr.gov.tubitak.uekae.esya.api.crypto.exceptions.CryptoException;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithGCMSpec;
import tr.gov.tubitak.uekae.esya.api.crypto.util.KeyUtil;
import tr.gov.tubitak.uekae.esya.asn.algorithms.GCMParameters;
import tr.gov.tubitak.uekae.esya.asn.cms.CMSVersion;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * CmsEnvelopeGenerator is used to generate the envelopes from an input stream
 * @author muratk
 *
 */

public class CmsEnvelopeStreamGenerator extends GeneratorBase{

	private InputStream mPlainStream = null; 
	private Asn1BerOutputStream mEncryptedStream = null;
	
	/**
	 * Constructor for CmsEnvelopeStreamGenerator
	 * @param aPlainData The plain data inputstream that will be enveloped
	 * @param aAlgorithm The symmetric algorithm that will be used while enveloped generation.
	 * @throws ArgErrorException 
	 */
	public CmsEnvelopeStreamGenerator(InputStream aPlainData,CipherAlg aAlgorithm) throws ArgErrorException
	{
		super(aAlgorithm);
		if(aAlgorithm == null)
			throw new ArgErrorException("CipherAlg can not be null");
		
		mPlainStream = aPlainData;
		mSymmetricAlgorithm = aAlgorithm;
	}

	/**
	 * AES256_CBC is used as default cipher algorithm.
	 * @param aPlainData The plain data inputstream that will be enveloped
	 */
	public CmsEnvelopeStreamGenerator(InputStream aPlainData)
	{
		super(CipherAlg.AES256_CBC);
		mPlainStream = aPlainData;
		mSymmetricAlgorithm = CipherAlg.AES256_CBC;
	}

	/**
	 * Generates the envelope. A random symmetric key is generated.
	 * @param aEnvelopedData  The output stream where the encoded ContentInfo for enveloped data will be written
	 * @throws CMSException If an error occurs while generating the envelope
	 */
	public void generate(OutputStream aEnvelopedData) throws CMSException
	{
		SecretKey key = null;
		try 
		{
			int keyLength = KeyUtil.getKeyLength(mSymmetricAlgorithm);
			key = getCryptoProvider().getKeyFactory().generateSecretKey(mSymmetricAlgorithm,keyLength); // check
		}
		catch (CryptoException e) 
		{
			throw new CMSException("Can not generate symmetric key", e);
		}

		if(ByteUtil.isAllZero(key.getEncoded()))
			throw new CMSException("The key must be a non-zero value");
		else
			generate(aEnvelopedData, key);
	}

	/**
	 *  Generates the envelope. The given symmetric algorithm and key is used for symmetric encryption
	 * @param aEnvelopedData The output stream where the encoded ContentInfo for enveloped data will be written
	 * @param aKey The key of the symmetric algorithm
	 * @throws CMSException
	 */
	public void generate(OutputStream aEnvelopedData, SecretKey aKey) throws CMSException
	{
		mEncryptedStream = new Asn1BerOutputStream(aEnvelopedData);
		
		//kripto işleri
		try 
		{
			EnvelopeConfig.checkSymetricAlgoritmIsSafe(mSymmetricAlgorithm);

			_kriptoOrtakIsler(mSymmetricAlgorithm, aKey);

			//streame yaz
			_writeStream();

			//streamleri kapat
			mPlainStream.close();
			
			mEncryptedStream.close();
			
		} 
		catch (Exception e) 
		{
			throw new CMSException("Error in generating enveloped data",e);
		}
	}


	private void _writeStream() throws Exception {
		_writeContentInfo();
	}
	
	private void _writeContentInfo() throws IOException, ESYAException {
		//contentinfo taglen
		mEncryptedStream.encodeTagAndIndefLen(Asn1Tag.SEQUENCE);
		
		//contenttype
		_writeContentType(mEnvelopeData.getOID());
		
		//content 
		_writeContent();
		
		mEncryptedStream.encodeTagAndLength(Asn1Tag.EOC, 0);
		
	}
	
	private void _writeContentType(Asn1ObjectIdentifier aOID) throws Asn1Exception, IOException
	{
		mEncryptedStream.encode(aOID, true);
	}
	
	private void _writeContent() throws IOException, ESYAException {
		mEncryptedStream.encodeTagAndIndefLen(new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0));
		
		_writeEnvelopedData();
		
		mEncryptedStream.encodeTagAndLength(Asn1Tag.EOC, 0);
	}
	
	private void _writeEnvelopedData() throws IOException, ESYAException {
		mEncryptedStream.encodeTagAndIndefLen(Asn1Tag.SEQUENCE);
		
		_writeVersion();
		
		_writeOriginatorInfo();
		
		_writeRecipientInfo();
		
		_writeEncryptedContentInfo();

		if(mSymmetricAlgorithm.getMod() == Mod.GCM ){
			mEnvelopeData.setMac(((ParamsWithGCMSpec) mSymmetricAlgParams).getTag());
		}

		mEnvelopeData.writeAttr(mEncryptedStream);
		
		mEncryptedStream.encodeTagAndLength(Asn1Tag.EOC, 0);
	}
	
	private void _writeVersion() throws Asn1Exception, IOException {
		mEncryptedStream.encode(new CMSVersion(_getVersion()), true);
	}
	
	private void _writeOriginatorInfo() throws Asn1Exception, IOException
	{
		if(mEnvelopeData.getOriginatorInfo() != null)
		{
			Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();

			int len = mEnvelopeData.getOriginatorInfo().encode(buffer,false);

			buffer.encodeTagAndLength (Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
			
			mEncryptedStream.write(buffer.getMsgCopy());
		}
	}
	
	private void _writeRecipientInfo() throws Asn1Exception, IOException
	{
		Asn1BerEncodeBuffer enc = new Asn1BerEncodeBuffer();
		mEnvelopeData.getRecipientInfos().encode(enc);
		mEncryptedStream.write(enc.getMsgCopy());
	}
	
	private void _writeEncryptedContentInfo() throws IOException, CryptoException {
		//taglen yaz
		mEncryptedStream.encodeTagAndIndefLen(Asn1Tag.SEQUENCE);
		//contentType yaz
		_writeContentType(OID_DATA);
		
		//contentEncryptionAlgorithm yaz
		_writeContentEncryptionAlgorithm();

		//encryptedContent yaz
		_writeEncryptedContent();
		
		mEncryptedStream.encodeTagAndLength(Asn1Tag.EOC, 0);
	}
	
	private void _writeContentEncryptionAlgorithm() throws Asn1Exception, IOException {

		Asn1BerEncodeBuffer enc = new Asn1BerEncodeBuffer();

		if(mSymmetricAlgorithm.getMod() == Mod.GCM) {

			GCMParameters params = new GCMParameters(new Asn1OctetString(((ParamsWithGCMSpec)mSymmetricAlgParams).getIV()),new Asn1Integer(16));
			Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
			params.encode(buff);

			mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm = new EAlgorithmIdentifier(mSymmetricAlgorithm.getOID(),buff.getMsgCopy()).getObject();
		}

		mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm.encode(enc);
		mEncryptedStream.write(enc.getMsgCopy());

	}

	private void _writeEncryptedContent() throws IOException, CryptoException {
		mEncryptedStream.encodeTagAndIndefLen(Asn1Tag.CTXT, Asn1Tag.CONS, 0);
		
		Cipher internalCipher = mSymmetricCrypto.getInternalCipher();
		
		int cipherBlockSize = internalCipher.getBlockSize();
		int bufferSize = cipherBlockSize * 10000;
		
		byte[] buffer = new byte[bufferSize];
		int readLen = mPlainStream.read(buffer);

		byte [] encrypted;
		while(readLen == bufferSize) 
		{
			encrypted = internalCipher.process(buffer);
			_writeOctetString(encrypted);
			readLen = mPlainStream.read(buffer);
		}

		int mulOfBlockSize = (readLen/cipherBlockSize) * cipherBlockSize;
		byte []lastBlocks = new byte[mulOfBlockSize];
		System.arraycopy(buffer, 0, lastBlocks, 0, mulOfBlockSize);	
		encrypted = internalCipher.process(lastBlocks);
		if(encrypted != null && encrypted.length!=0)
		 _writeOctetString(encrypted);

		int lastBlockSize = readLen - mulOfBlockSize;
		byte []lastBlock = new byte[lastBlockSize];
		System.arraycopy(buffer, mulOfBlockSize, lastBlock, 0, lastBlockSize);	
		encrypted = internalCipher.doFinal(lastBlock);
		if(encrypted != null && encrypted.length!=0)
		 _writeOctetString(encrypted);

		mEncryptedStream.encodeTagAndLength(Asn1Tag.EOC, 0);
	}

	private void _writeOctetString(byte[] aIcerik) throws Asn1Exception, IOException 
	{
		mEncryptedStream.encodeOctetString(aIcerik, true, Asn1OctetString.TAG);
	}

}
