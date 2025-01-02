using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.exception;
using Com.Objsys.Asn1.Runtime;
using Org.BouncyCastle.Utilities;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.util;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    /**
     * CmsEnvelopeStreamParser is used to open the envelopes from an inputstream
     * @author muratk
     */
    public class CmsEnvelopeStreamParser : ParserBase
    {
        private readonly Asn1BerInputStream mSifrelenmisVeri = null;
        private Stream mCozulmusVeri = null;
        private Asn1BerOutputStream mUpdatedVeri = null;
        private byte[] finalBytesBuff = null;

        private Cipher mSimetrikKripto;
        private CipherAlg mCipherAlg;

        private IDecryptorStore mDecryptorStore = null;

        private int mContentInfoLen = 0;
        private int mContentLen = 0;
        private int mEnvDataLen = 0;
        IAlgorithmParams mparams;

        /**
         * Constructor for the CmsEnvelopeStreamParser
         * @param aEnvelopedData Enveloped Data as input stream
         */
        public CmsEnvelopeStreamParser(Stream aEnvelopedData)
        {
            mSifrelenmisVeri = new Asn1BerInputStream(aEnvelopedData);
        }

        private void _readStreamUntilEncryptedContent()
        {
            if (mEnvelopeData == null)
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
            int[] oid = contentTypeObjectIdentifier.mValue;
            if (!(Arrays.AreEqual(oid, _cmsValues.id_ct_authEnvelopedData) ||
                      Arrays.AreEqual(oid, _cmsValues.id_envelopedData)))
                throw new NotEnvelopeDataException("Encrypted content is not in Enveloped-data content-type! OID: " + OIDUtil.toURN(oid));


            mContentInfo.setContentType(contentTypeObjectIdentifier);

            mContentLen = mSifrelenmisVeri.DecodeTagAndLength(new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0));
            mEnvDataLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);
            mEnvelopeData.setVersion((int)_readVersiyon().mValue);        
            mEnvelopeData.setOriginatorInfo(_readOriginatorInfo());         
            mEnvelopeData.setRecipientInfos(_readRecipientInfo());

            EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);
            mEnvelopeData.setEncryptedContentInfo(new EEncryptedContentInfo(new EncryptedContentInfo()));       
            mEnvelopeData.getEncryptedContentInfo().getObject().contentType = _readContentType();

            //contentEncryptionAlgorithm oku
            _readContentEncryptionAlgorithm();
        }


        private void _skipEncryptedContent()
        {
            int encryptedContentInfoLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);

            mEnvelopeData.setEncryptedContentInfo(new EEncryptedContentInfo(new EncryptedContentInfo()));

            mEnvelopeData.getEncryptedContentInfo().getObject().contentType = _readContentType();

            _readContentEncryptionAlgorithm();

            Asn1Tag tag = mSifrelenmisVeri.PeekTag();
            Asn1Tag primitive = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 0);
            Asn1Tag construct = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);

            int encryptedContentLen = 0;
            if (tag.mForm == Asn1Tag.PRIM)
            {
                encryptedContentLen = mSifrelenmisVeri.DecodeTagAndLength(primitive);
                mSifrelenmisVeri.Skip(encryptedContentLen);
            }
            else
            {
                encryptedContentLen = mSifrelenmisVeri.DecodeTagAndLength(construct);
                tag = mSifrelenmisVeri.PeekTag();
                while (tag != null && tag.Equals(Asn1OctetString._TAG))
                {
                    int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1OctetString._TAG); 
                    byte[] buffer = new byte[len];
                    mSifrelenmisVeri.Read(buffer, 0, buffer.Length);
                    tag = mSifrelenmisVeri.PeekTag();
                }
            }

            if (encryptedContentLen == Asn1Status.INDEFLEN)
                EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);

            if (encryptedContentInfoLen == Asn1Status.INDEFLEN)
                EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
        }

        private void _readStream()
        {        
            _readContentInfo();
        }

        private void _readContentInfo()
        {
            if (mContentInfoLen == 0)
            {               
                try
                {
                    //contentinfo taglen
                    mContentInfoLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);
                    //contenttype
                    mContentInfo.setContentType(_readContentType());
                }
                catch (Exception ex)
                {
                    throw new ESYAException("Encrypted content is not in Enveloped-data content-type!", ex);
                }
            }

            if (mEnvelopeData == null)
            {

                if (Arrays.AreEqual(mContentInfo.getContentType().mValue, _cmsValues.id_ct_authEnvelopedData))
                    mEnvelopeData = new EAuthenticatedEnvelopedData(new AuthEnvelopedData());
                else if ((Arrays.AreEqual(mContentInfo.getContentType().mValue, _cmsValues.id_envelopedData)))
                    mEnvelopeData = new EEnvelopedData(new EnvelopedData());
                else
                    throw new ESYAException("Encrypted content is not in Enveloped-data content-type. Content OID does not match!");
            }

            //content 
            _readContent();

            if (mContentInfoLen == Asn1Status.INDEFLEN)
                EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
        }

        private Asn1ObjectIdentifier _readContentType()
        {
            int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1ObjectIdentifier._TAG);
            int[] oid = mSifrelenmisVeri.DecodeOIDContents(len);
            return new Asn1ObjectIdentifier(oid);
        }

        private void _readContent()
        {
            if (mContentLen == 0)
            {
                mContentLen = mSifrelenmisVeri.DecodeTagAndLength(new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0));
            }

            try
            {
                _readEnvelopedData();
            }
            catch (Exception e)
            {
                throw new ESYAException("Can not decode EnvelopeData!", e);
            }

            if (mContentLen == Asn1Status.INDEFLEN)
                EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
        }

        private void _readEnvelopedData()
        {
            if (mEnvDataLen == 0)
            {
                mEnvDataLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);
            }

            mEnvelopeData.setVersion((int)_readVersiyon().mValue);

            mEnvelopeData.setOriginatorInfo(_readOriginatorInfo());
          
            mEnvelopeData.setRecipientInfos(_readRecipientInfo());

            _readEncryptedContentInfo();

            if (mEnvDataLen == Asn1Status.INDEFLEN)
                EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
        }

        private CMSVersion _readVersiyon()
        {
            CMSVersion ver = new CMSVersion();
            ver.Decode(mSifrelenmisVeri);
            return ver;
        }

        private OriginatorInfo _readOriginatorInfo()
        {
            Asn1Tag tag = mSifrelenmisVeri.PeekTag();
            Asn1Tag originarInfoTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);
            if (tag.Equals(originarInfoTag))
            {
                int len = mSifrelenmisVeri.DecodeTagAndLength(originarInfoTag);
                OriginatorInfo oi = new OriginatorInfo();
                oi.Decode(mSifrelenmisVeri, false, len);
                return oi;
            }
            return null;

        }

        private RecipientInfos _readRecipientInfo()
        {
            RecipientInfos recipientInfos = new RecipientInfos();

            recipientInfos.Decode(mSifrelenmisVeri);

            return recipientInfos;
        }

        private void _readEncryptedContentInfo()
        {   
            if (mEnvelopeData.getEncryptedContentInfo().getObject() == null)
           {
                int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.SEQUENCE);

                mEnvelopeData.setEncryptedContentInfo(new EEncryptedContentInfo(new EncryptedContentInfo()));
             
                mEnvelopeData.getEncryptedContentInfo().setContentType(_readContentType());

                //contentEncryptionAlgorithm oku
                _readContentEncryptionAlgorithm();

                //simetrik algoritma ve recipientifoyu aldık, anahtarı çözelim
                byte[] anahtar = _getSymmetricKey();

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
        private void processTheLastRemainingCryptoOps()
        {
            byte[] finalBytes = null;

            if (mEnvelopeData is EAuthenticatedEnvelopedData)
            {
                byte[] tag = mEnvelopeData.getMac();
                finalBytes = ByteUtil.concatAll(finalBytesBuff, tag);
            }
            else            
                finalBytes = finalBytesBuff;

            byte[] unpad = mSimetrikKripto.doFinal(finalBytes);
            if (unpad != null)
                mCozulmusVeri.Write(unpad, 0, unpad.Length);
        }

        private void _readContentEncryptionAlgorithm()
        {
            if (mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm == null)
            {
                AlgorithmIdentifier alg = new AlgorithmIdentifier();
                alg.Decode(mSifrelenmisVeri);             
                mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm = alg;
            }
        }

        private byte[] _getSymmetricKey()
        {
            if (mDecryptorStore != null)
                return getSymmetricKeyOfEnvelope(mDecryptorStore);

            throw new CMSException("Decryptor is not set");
        }

        private void _readEncryptedContent(byte[] anahtar)
        {
            //Simetrik Kriptonun anahtarini yerlestirelim
            AlgorithmIdentifier algID = mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm;
            Pair<CipherAlg, IAlgorithmParams> alg = CipherAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(algID));
            mCipherAlg = alg.first();
            mparams = alg.second();
            mSimetrikKripto = Crypto.getDecryptor(mCipherAlg).getInternalCipher();

            //giris parametresi olarak gelen anahtar icindeki gereksiz karakterler atilmali
            int keySizeInBytes = KeyUtil.getKeyLength(mCipherAlg) >> 3;
            Array.Resize<byte>(ref anahtar, keySizeInBytes);

            mSimetrikKripto.init(anahtar, mparams);

            int len = 0;
            Asn1Tag tag = mSifrelenmisVeri.PeekTag();
            Asn1Tag primitive = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.PRIM, 0);
            Asn1Tag construct = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0);

            if (tag.mForm == Asn1Tag.PRIM)
            {
                len = mSifrelenmisVeri.DecodeTagAndLength(primitive);
                _readPrimitiveOctetString(len);
            }
            else
            {
                len = mSifrelenmisVeri.DecodeTagAndLength(construct);
                _readConstructedOctetString();
            }

            if (len == Asn1Status.INDEFLEN)
                EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1Tag.EOC);
        }

        private void _readPrimitiveOctetString(int aLen)
        {
            byte[] decryptedData;
            byte[] buffer;

            int bufferSize = mSimetrikKripto.getBlockSize() * 10000;
            buffer = new byte[bufferSize];
            int loopCount = aLen / bufferSize;

            for (int i = 0; i < loopCount; i++)
            {
                mSifrelenmisVeri.Read(buffer);
                decryptedData = mSimetrikKripto.process(buffer);
                mCozulmusVeri.Write(decryptedData, 0, decryptedData.Length);
            }

            int lastBlockLen = aLen % bufferSize;
            buffer = new byte[lastBlockLen];
            mSifrelenmisVeri.Read(buffer);

            finalBytesBuff = ByteUtil.copyofRange(buffer, 0, buffer.Length);
        }
     
        private void _readConstructedOctetString()
        {           
            byte[] encrypted = null;
            byte[] decrypted = null;

            Asn1Tag tag = mSifrelenmisVeri.PeekTag();
            while (tag != null && tag.Equals(Asn1OctetString._TAG))
            {
                int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(mSifrelenmisVeri, Asn1OctetString._TAG);  
                encrypted = new byte[len];
                mSifrelenmisVeri.Read(encrypted);

                tag = mSifrelenmisVeri.PeekTag();

                if (tag == null || !tag.Equals(Asn1OctetString._TAG))
                    finalBytesBuff = ByteUtil.copyofRange(encrypted, 0, encrypted.Length);               
                else
                {
                    decrypted = mSimetrikKripto.process(encrypted);
                    if (decrypted != null)
                        mCozulmusVeri.Write(decrypted, 0, decrypted.Length);
                }
            }
        }

        private UnprotectedAttributes _readUnProtectedAttribute()
        {
            int available = mSifrelenmisVeri.Available();
            if (available > 0)
            {
                Asn1Tag tag = mSifrelenmisVeri.PeekTag();
                Asn1Tag unProtectedAttributeTag = new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 1);
                if (tag != null && tag.Equals(unProtectedAttributeTag))
                {
                    int len = mSifrelenmisVeri.DecodeTagAndLength(unProtectedAttributeTag);
                    UnprotectedAttributes ua = new UnprotectedAttributes();
                    ua.Decode(mSifrelenmisVeri, false, len);
                    return ua;
                }
            }
            return null;
        }

        private void _writeStream()
        {
            _writeContentInfo();
        }

        private void _writeContentInfo()
        {
            //contentinfo taglen
            mUpdatedVeri.EncodeTagAndIndefLen(Asn1Tag.SEQUENCE);

            //contenttype
            _writeContentType(OID_ENVELOPED_DATA);

            //content 
            _writeContent();

            mUpdatedVeri.EncodeTagAndLength(Asn1Tag.EOC, 0);

        }

        private void _writeContentType(Asn1ObjectIdentifier aOID)
        {
            mUpdatedVeri.Encode(aOID, true);
        }

        private void _writeContent()
        {
            mUpdatedVeri.EncodeTagAndIndefLen(new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0));

            _writeEnvelopedData();

            mUpdatedVeri.EncodeTagAndLength(Asn1Tag.EOC, 0);
        }

        private void _writeEnvelopedData()
        {
            //taglen yaz
            mUpdatedVeri.EncodeTagAndIndefLen(Asn1Tag.SEQUENCE);

            _writeVersion();

            _writeOriginatorInfo();

            _writeRecipientInfo();

            _writeEncryptedContentInfoWithoutContent();

            _transferRestOfInputStream();
        }
        
        private void _writeEncryptedContentInfoWithoutContent()
	    {
		     //taglen yaz
		     mUpdatedVeri.EncodeTagAndIndefLen(Asn1Tag.SEQUENCE);
		    //contentType yaz
		    _writeContentType(OID_DATA);

		    //contentEncryptionAlgorithm yaz
		    _writeContentEncryptionAlgorithm();
	    }

	    private void _writeContentEncryptionAlgorithm()
	    {
		    Asn1BerEncodeBuffer enc = new Asn1BerEncodeBuffer();
	        mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm.Encode(enc);
            mUpdatedVeri.Write(enc.MsgCopy);
	    }

        private void _writeVersion()
        {
            mUpdatedVeri.Encode(new CMSVersion(mEnvelopeData.getVersion()), true);
        }

        private void _writeOriginatorInfo()
        {

            if (mEnvelopeData.getOriginatorInfo() != null)
            {
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();
                int len = mEnvelopeData.getOriginatorInfo().Encode(buffer, false);
                buffer.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);
                mUpdatedVeri.Write(buffer.MsgCopy);
            }
        }

        private void _writeRecipientInfo()
        {
            Asn1BerEncodeBuffer enc = new Asn1BerEncodeBuffer();
            mEnvelopeData.getRecipientInfos().Encode(enc);
            mUpdatedVeri.Write(enc.MsgCopy);
        }

        private void _transferRestOfInputStream()
        {
            int bufferSize = 32768; // 32K
            int remainLen = mSifrelenmisVeri.Available();


            byte[] block = new byte[bufferSize];
            int loopCount = remainLen / bufferSize;
            for (int i = 0; i < loopCount; i++)
            {
                mSifrelenmisVeri.Read(block);
                mUpdatedVeri.Write(block, 0, block.Length);
            }

            //read the bytes that less then buffer size
            remainLen = mSifrelenmisVeri.Available();
            while (remainLen > 0)
            {
                byte[] remainData = new byte[remainLen];
                mSifrelenmisVeri.Read(remainData);
                mUpdatedVeri.Write(remainData, 0, remainData.Length);
                remainLen = mSifrelenmisVeri.Available();
            }
        }

        /**
         * Returns the recipient's IssuerAndSerialNumber or SubjectKeyIdentifier. 
         * @return The recipients of the envelope. Return type can be tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber or
         * tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier according to cms
         * @throws CMSException If an error occurs while retrieving the recipients
         */
        public Object[] getRecipientInfos()
        {
            try
            {
                _readStreamUntilEncryptedContent();
            }
            catch (Exception e)
            {
                throw new CMSException("Error in retrieving recipient infos", e);
            }

            return _getRecipientsInEnvelope();
        }


        /**
         * Opens the envelope
         * @param aPlainData The outputstream where the envelope content will be written
         * @param aDecryptorStore The decryptor of the recipient used to open the envelope
         * @throws CMSException If an error occurs while opening the envelope
         */
        public void open(Stream aPlainData, IDecryptorStore aDecryptorStore)
        {
            mCozulmusVeri = aPlainData;
            mDecryptorStore = aDecryptorStore;

            try
            {
                _readStream();
            }
            catch (Exception e)
            {
                throw new CMSException("Error in opening enveloped data", e);
            }

            try
            {
                //streamleri kapat
                mSifrelenmisVeri.Close();
                mCozulmusVeri.Close();
            }
            catch (IOException e)
            {
                throw new CMSException("Error in closing streams!", e);
            }
        }


        /**
         * adds new recipient. according to recipient type, default values will be filled to the required fields.
         * @param aUpdatedEnvelope The outputstream where the new cms will be written
         * @param aDecryptorStore the store of certs and keys
         * @param aNewRecipientCerts the new recipient certificate
         * @throws CMSException 
         */
        protected void addRecipientInfo(EnvelopeConfig config, Stream aUpdatedEnvelope, IDecryptorStore aDecryptorStore, params ECertificate[] aNewRecipientCerts)
        {
            try
            {
                _readStreamUntilEncryptedContent();

                AlgorithmIdentifier algID = mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm;
                Pair<CipherAlg, IAlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(new EAlgorithmIdentifier(algID));
                EnvelopeConfig.checkSymmetricAlgorithmIsSafe(cipherAlg.first());

                byte[] symmetricKey = getSymmetricKeyOfEnvelope(aDecryptorStore);

                addRecipientInfo(config, symmetricKey, aNewRecipientCerts);

                //it was done due to meet the requirements of crypto analysis
                SecretKeyUtil.eraseSecretKey(symmetricKey);

                mUpdatedVeri = new Asn1BerOutputStream(aUpdatedEnvelope);

                _writeStream();

                //streamleri kapat
                mSifrelenmisVeri.Close();

                mUpdatedVeri.Close();

            }
            catch (Exception e)
            {
                throw new CMSException("Error in adding new recipient to the enveloped data", e);
            }
        }


        /**
         * adds key trans recipient.
         * @param aUpdatedEnvelope The outputstream where the new cms will be written
         * @param aDecryptorStore the store of certs and keys
         * @param aNewRecipientCerts the new recipient certificate
         * @throws CMSException
         */
        protected void addKeyTransRecipientInfo(EnvelopeConfig config, Stream aUpdatedEnvelope, IDecryptorStore aDecryptorStore, params ECertificate[] aNewRecipientCerts)
        {
            mDecryptorStore = aDecryptorStore;

            addKeyTransRecipientInfo(config, aUpdatedEnvelope, aNewRecipientCerts);
        }

        private void addKeyTransRecipientInfo(EnvelopeConfig config, Stream aUpdatedEnvelope, params ECertificate[] aNewRecipientCerts)
        {
            try
            {
                _readStreamUntilEncryptedContent();

                byte[] symmetricKey = _getSymmetricKey();

                addTransRecipientInfo(config, symmetricKey, aNewRecipientCerts);

                //it was done due to meet the requirements of crypto analysis
                SecretKeyUtil.eraseSecretKey(symmetricKey);

                mUpdatedVeri = new Asn1BerOutputStream(aUpdatedEnvelope);

                _writeStream();

                //streamleri kapat
                mSifrelenmisVeri.Close();

                mUpdatedVeri.Close();
            }
            catch (Exception e)
            {
                throw new CMSException("Error in adding new recipient to the enveloped data", e);
            }
        }


        /**
         * 
         * @param aUpdatedEnvelope The outputstream where the new cms will be writtens
         * @param aDecryptorStore the store of certs and keys
         * @param aAgreementAlg Algorithm that will be used on the key aggrements
         * @param aNewRecipientCerts the new recipient certificates
         * @throws CMSException
         */
        protected void addKeyAgreeRecipientInfo(Stream aUpdatedEnvelope, IDecryptorStore aDecryptorStore, EnvelopeConfig config, params ECertificate[] aNewRecipientCerts)
        {
            mDecryptorStore = aDecryptorStore;

            addKeyAgreeRecipientInfo (aUpdatedEnvelope, config, aNewRecipientCerts);
        }


        private void addKeyAgreeRecipientInfo(Stream aUpdatedEnvelope, EnvelopeConfig config, params ECertificate[] aNewRecipientCerts)
        {
            try
            {
                _readStreamUntilEncryptedContent();

                byte[] symmetricKey = _getSymmetricKey();

                addKeyAgreeRecipientInfo(config, symmetricKey, aNewRecipientCerts);

                //it was done due to meet the requirements of crypto analysis
                SecretKeyUtil.eraseSecretKey(symmetricKey);

                mUpdatedVeri = new Asn1BerOutputStream(aUpdatedEnvelope);

                _writeStream();

                //streamleri kapat
                mSifrelenmisVeri.Close();

                mUpdatedVeri.Close();

            }
            catch (Exception e)
            {
                throw new CMSException("Error in adding new recipient to the enveloped data", e);
            }
        }

        public void addRecipients(EnvelopeConfig config, Stream aUpdatedEnvelope,IDecryptorStore aDecryptorStore,  params ECertificate[] aNewRecipientCerts)
	    {
            if (config.isCertificateValidationActive())
            {
                foreach (ECertificate cer in aNewRecipientCerts)
                {
                    CertificateStatusInfo csi = CertificateValidation.validateCertificate(config.getValidationSystem(), cer);
                    if (csi.getCertificateStatus() != CertificateStatus.VALID)
                    {
                        throw new CertValidationException(csi);
                    }
                }
            }
		    addRecipientInfo(config, aUpdatedEnvelope,aDecryptorStore, aNewRecipientCerts);
	    }

        /**
         * Removes recipients from the envelope
         * @param aUpdatedEnvelope The stream where the envelope without the removed recipients will be written
         * @param aRemoveCertificate The certificated of the recipients to be removed from the envelope
         * @throws CMSException  If an error occurs while removing recipients to the envelope
         */
        public void removeRecipientInfo(Stream aUpdatedEnvelope, params ECertificate[] aRemoveCertificates)
        {
            try
            {
                _readStreamUntilEncryptedContent();

                _removeRecipientInfos(aRemoveCertificates);

                mUpdatedVeri = new Asn1BerOutputStream(aUpdatedEnvelope);

                _writeStream();

                //streamleri kapat
                mSifrelenmisVeri.Close();

                mUpdatedVeri.Close();
            }
            catch (Exception e)
            {
                throw new CMSException("Error in adding removing recipient from the enveloped data", e);
            }
        }
    }
}
