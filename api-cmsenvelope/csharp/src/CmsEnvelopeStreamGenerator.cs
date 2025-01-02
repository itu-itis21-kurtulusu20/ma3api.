using System;
using System.IO;

using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    /**
     * CmsEnvelopeGenerator is used to generate the envelopes from an input stream
     * @author muratk
     *
     */
    public class CmsEnvelopeStreamGenerator : GeneratorBase
    {
        private readonly Stream mPlainStream = null;
        private Asn1BerOutputStream mEncryptedStream = null;

        /**
         * Constructor for CmsEnvelopeStreamGenerator
         * @param aPlainData The plain data inputstream that will be enveloped
         * @param aAlgorithm The symmetric algorithm that will be used while enveloped generation.
         * @throws ArgErrorException 
         */
        public CmsEnvelopeStreamGenerator(Stream aPlainData, CipherAlg aAlgorithm):base(aAlgorithm)
        {
            if (aAlgorithm == null)
                throw new ArgErrorException("CipherAlg can not be null");

            mPlainStream = aPlainData;
            mSymmetricAlgorithm = aAlgorithm;
        }

        /**
         * AES128_CBC is used as default cipher algorithm.
         * @param aPlainData The plain data inputstream that will be enveloped
         */
        public CmsEnvelopeStreamGenerator(Stream aPlainData):base(CipherAlg.AES256_CBC)
        {
            mPlainStream = aPlainData;
            mSymmetricAlgorithm = CipherAlg.AES256_CBC;
        }

        /**
         * Generates the envelope. A random symmetric key is generated.
         * @param aEnvelopedData  The output stream where the encoded ContentInfo for enveloped data will be written
         * @throws CMSException If an error occurs while generating the envelope
         */
        public void generate(Stream aEnvelopedData)
        {
            byte[] key = null;
            try
            {
                key = KeyUtil.generateKey(mSymmetricAlgorithm, KeyUtil.getKeyLength(mSymmetricAlgorithm));
            }
            catch (CryptoException e)
            {
                throw new CMSException("Can not generate symmetric key", e);
            }

            if (ByteUtil.isAllZero(key))
                throw new CMSException("The key must be a non-zero value");
            generate(aEnvelopedData, key);
        }

        /**
         *  Generates the envelope. The given symmetric algorithm and key is used for symmetric encryption
         * @param aEnvelopedData The output stream where the encoded ContentInfo for enveloped data will be written
         * @param aAlgorithm The symmetric algorithm used for data encryption
         * @param aKey The key of the symmetric algorithm
         * @throws CMSException
         */
        public void generate(Stream aEnvelopedData, byte[] aKey)
        {
            mEncryptedStream = new Asn1BerOutputStream(aEnvelopedData);

            //kripto işleri
            try
            {
                EnvelopeConfig.checkSymmetricAlgorithmIsSafe(mSymmetricAlgorithm);

                _kriptoOrtakIsler(mSymmetricAlgorithm, aKey);

                //streame yaz
                _writeStream();

                //streamleri kapat
                mPlainStream.Close();

                mEncryptedStream.Close();

            }
            catch (Exception e)
            {
                throw new CMSException("Error in generating enveloped data", e);
            }
        }

        private void _writeStream()
        {
            _writeContentInfo();
        }

        private void _writeContentInfo()
        {
            //contentinfo taglen
            mEncryptedStream.EncodeTagAndIndefLen(Asn1Tag.SEQUENCE);

            //contenttype
            _writeContentType(mEnvelopeData.getOID());

            //content 
            _writeContent();

            mEncryptedStream.EncodeTagAndLength(Asn1Tag.EOC, 0);

        }

        private void _writeContentType(Asn1ObjectIdentifier aOID)
        {
            mEncryptedStream.Encode(aOID, true);
        }

        private void _writeContent()
        {
            mEncryptedStream.EncodeTagAndIndefLen(new Asn1Tag(Asn1Tag.CTXT, Asn1Tag.CONS, 0));

            _writeEnvelopedData();

            mEncryptedStream.EncodeTagAndLength(Asn1Tag.EOC, 0);
        }

        private void _writeEnvelopedData()
        {
            mEncryptedStream.EncodeTagAndIndefLen(Asn1Tag.SEQUENCE);

            _writeVersion();

            _writeOriginatorInfo();

            _writeRecipientInfo();

            _writeEncryptedContentInfo();

            if (mSymmetricAlgorithm.getMod() == Mod.GCM)
            {
                mEnvelopeData.setMac(((ParamsWithGCMSpec)mSymmetricAlgParams).getTag());
            }

            mEnvelopeData.writeAttr(mEncryptedStream);

            mEncryptedStream.EncodeTagAndLength(Asn1Tag.EOC, 0);
        }

        private void _writeVersion()
        {
            mEncryptedStream.Encode(new CMSVersion(_getVersion()), true);
        }

        private void _writeOriginatorInfo()
        {
            if (mEnvelopeData.getOriginatorInfo() != null)
            {
                Asn1BerEncodeBuffer buffer = new Asn1BerEncodeBuffer();

                int len = mEnvelopeData.getOriginatorInfo().Encode(buffer, false);

                buffer.EncodeTagAndLength(Asn1Tag.CTXT, Asn1Tag.CONS, 0, len);

                mEncryptedStream.Write(buffer.MsgCopy);
            }
        }

        private void _writeRecipientInfo()
        {
            Asn1BerEncodeBuffer enc = new Asn1BerEncodeBuffer();
            mEnvelopeData.getRecipientInfos().Encode(enc);
            mEncryptedStream.Write(enc.MsgCopy);
        }

        private void _writeEncryptedContentInfo()
        {
            //taglen yaz
            mEncryptedStream.EncodeTagAndIndefLen(Asn1Tag.SEQUENCE);
            //contentType yaz
            _writeContentType(OID_DATA);

            //contentEncryptionAlgorithm yaz
            _writeContentEncryptionAlgorithm();

            //encryptedContent yaz
            _writeEncryptedContent();

            mEncryptedStream.EncodeTagAndLength(Asn1Tag.EOC, 0);
        }

        private void _writeContentEncryptionAlgorithm()
        {
            Asn1BerEncodeBuffer enc = new Asn1BerEncodeBuffer();
            if (mSymmetricAlgorithm.getMod() == Mod.GCM)
            {
                GCMParameters params_ = new GCMParameters(new Asn1OctetString(((ParamsWithGCMSpec)mSymmetricAlgParams).getIV()), new Asn1Integer(16));
                Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
                params_.Encode(buff);

                mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm = new EAlgorithmIdentifier(mSymmetricAlgorithm.getOID(), buff.MsgCopy).getObject();
            }

            mEnvelopeData.getEncryptedContentInfo().getObject().contentEncryptionAlgorithm.Encode(enc);
            mEncryptedStream.Write(enc.MsgCopy);
        }

        private void _writeEncryptedContent()
        {
            mEncryptedStream.EncodeTagAndIndefLen(Asn1Tag.CTXT, Asn1Tag.CONS, 0);

            Cipher internalCipher = mSymmetricCrypto.getInternalCipher();

            int cipherBlockSize = internalCipher.getBlockSize();
            int bufferSize = cipherBlockSize * 10000;

            byte[] buffer = new byte[bufferSize];
            int readLen = mPlainStream.Read(buffer, 0, buffer.Length);

            byte[] encrypted;
            while (readLen == bufferSize)
            {
                encrypted = internalCipher.process(buffer);
                _writeOctetString(encrypted);
                readLen = mPlainStream.Read(buffer, 0, buffer.Length);
            }

            int mulOfBlockSize = (readLen / cipherBlockSize) * cipherBlockSize;
            byte[] lastBlocks = new byte[mulOfBlockSize];
            Array.Copy(buffer, 0, lastBlocks, 0, mulOfBlockSize);
            encrypted = internalCipher.process(lastBlocks);
            if(encrypted != null && encrypted.Length != 0)
             _writeOctetString(encrypted);

            int lastBlockSize = readLen - mulOfBlockSize;
            byte[] lastBlock = new byte[lastBlockSize];
            Array.Copy(buffer, mulOfBlockSize, lastBlock, 0, lastBlockSize);
            encrypted = internalCipher.doFinal(lastBlock);

            if (internalCipher.getCipherAlgorithm().getMod() == Mod.GCM)
            {
                int blockSize = internalCipher.getBlockSize();
                if (encrypted.Length < blockSize)
                    throw new ESYAException("Remaining bytes must be greater than block size");

                int dataLength = encrypted.Length - blockSize;
                byte[] data = ByteUtil.copyofRange(encrypted, 0, dataLength);
                byte[] tag = ByteUtil.copyofRange(encrypted, dataLength, encrypted.Length);

                encrypted = data;
                ((ParamsWithGCMSpec)mSymmetricAlgParams).setTag(tag);
            }

            if (encrypted != null && encrypted.Length != 0)
                _writeOctetString(encrypted);

            mEncryptedStream.EncodeTagAndLength(Asn1Tag.EOC, 0);
        }

        private void _writeOctetString(byte[] aIcerik)
        {
            mEncryptedStream.EncodeOctetString(aIcerik, true, Asn1OctetString._TAG);
        }
    }
}
