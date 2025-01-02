using System;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using Org.BouncyCastle.Utilities;
using tr.gov.tubitak.uekae.esya.api.asn;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.exception;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;

//@ApiClass
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    /**
     * CmsEnvelopeParser is used to open the envelopes in the memory
     * @author muratk
     *
     */
    public class CmsEnvelopeParser : ParserBase
    {
        /**
	 * Constructor for the CmsEnvelopeParser
	 * @param aEnvelopedData Enveloped Data as byte array
	 * @throws CMSException If an error occurs while parsing the envelope 
	 */
        public CmsEnvelopeParser(byte[] aEnvelopedData)
        {
            checkItIsEnvelopeDataFormat(aEnvelopedData);
        
            try
            {
                mContentInfo = new EContentInfo(aEnvelopedData);

                if (Arrays.AreEqual(mContentInfo.getContentType().mValue, _cmsValues.id_ct_authEnvelopedData))
                    mEnvelopeData = new EAuthenticatedEnvelopedData(mContentInfo.getContent());           
                else if (Arrays.AreEqual(mContentInfo.getContentType().mValue, _cmsValues.id_envelopedData))
                    mEnvelopeData = new EEnvelopedData(mContentInfo.getContent());
            }
            catch (Exception e)
            {
                throw new CMSException("Can not decode EnvelopeData!", e);
            }

            if (mEnvelopeData == null)
                throw new CMSException("Encrypted content is not in Enveloped-data content-type. Content OID does not match!");   
        }

        public void checkItIsEnvelopeDataFormat(byte[] aEnvelopedData)
        {
            int[] oid = new int[0];
            try
            {
                Asn1BerInputStream stream = new Asn1BerInputStream(new MemoryStream(aEnvelopedData, 0, 128));
                int contentLen = EAsnUtil.decodeTagAndLengthWithCheckingTag(stream, Asn1Tag.SEQUENCE);
                int len = EAsnUtil.decodeTagAndLengthWithCheckingTag(stream, Asn1ObjectIdentifier._TAG);
                oid = stream.DecodeOIDContents(len);
            }
            catch (Exception e)
            {
                throw new NotEnvelopeDataException("Can not decode contentType! The file is not int the CMSEnvelope Format!", e);
            }

            if (!(Arrays.AreEqual(oid, _cmsValues.id_ct_authEnvelopedData) ||
                      Arrays.AreEqual(oid, _cmsValues.id_envelopedData)))
                throw new NotEnvelopeDataException("Encrypted content is not in Enveloped-data content-type! OID: " + OIDUtil.toURN(oid));
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
        public byte[] open(IDecryptorStore aDecryptorStore)
        {
            try
            {
                byte[] anahtar = getSymmetricKeyOfEnvelope(aDecryptorStore);
                byte[] decryptedBytes = null;

                if (anahtar == null)
                    throw new CMSException("No key found for opening enveloped data");

                EAlgorithmIdentifier simAlgIden = mEnvelopeData.getEncryptedContentInfo().getEncryptionAlgorithm();
                Pair<CipherAlg, IAlgorithmParams> cipherAlgAlgorithmParamsPair = CipherAlg.fromAlgorithmIdentifier(simAlgIden);
                CipherAlg simAlg = cipherAlgAlgorithmParamsPair.first();
                IAlgorithmParams params_ = cipherAlgAlgorithmParamsPair.second();

                if (simAlg.getMod() == Mod.GCM)
                    ((ParamsWithGCMSpec)params_).setTag(mEnvelopeData.getMac());

                //giris parametresi olarak gelen anahtar icindeki gereksiz karakterler atilmali
                int keySizeInBytes = KeyUtil.getKeyLength(simAlg) >> 3;
                Array.Resize<byte>(ref anahtar, keySizeInBytes);

                //it was done due to meet the requirements of crypto analysis               
                decryptedBytes = CipherUtil.decrypt(simAlg, params_, mEnvelopeData.getEncryptedContentInfo().getEncryptedContent(), anahtar);
                SecretKeyUtil.eraseSecretKey(anahtar);
                return decryptedBytes;

            }
            catch (Exception e)
            {
                throw new CMSException("Error in opening enveloped data", e);
            }
        }

        /**
         * adds new recipient. according to recipient type, default values will be filled to the required fields.
         * @param aDecryptorStore the store of certs and keys
         * @param aNewRecipientCerts the new recipients' certificate
         * @return
         * @throws CMSException
         */
        protected byte[] addRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, params ECertificate[] aNewRecipientCerts)
        {

            EAlgorithmIdentifier algID = mEnvelopeData.getEncryptedContentInfo().getEncryptionAlgorithm();
            Pair<CipherAlg, IAlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(algID);
            EnvelopeConfig.checkSymmetricAlgorithmIsSafe(cipherAlg.first());

            byte[] symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
            addRecipientInfo(config, symmetricKey, aNewRecipientCerts);

            SecretKeyUtil.eraseSecretKey(symmetricKey);

            mContentInfo.setContent(mEnvelopeData.getEncoded());
            return _getContentInfoAsEncoded();
        }

        public byte[] addRecipients(EnvelopeConfig config, IDecryptorStore aDecryptorStore, params ECertificate[] aNewRecipientCerts)
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
		   return addRecipientInfo(config, aDecryptorStore, aNewRecipientCerts);
	    }


        /**
         * 
         * @param aDecryptorStore the store of certs and keys
         * @param agreementAlg  algorithm that will be used on the key aggrement
         * @param aNewRecipientCerts the new recipient's certificates
         * @return
         * @throws CMSException
         */
        protected byte[] addKeyAgreeRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, params ECertificate[] aNewRecipientCerts)
        {
            byte[] symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
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
        protected byte[] addKeyTransRecipientInfo(EnvelopeConfig config, IDecryptorStore aDecryptorStore, params ECertificate[] aNewRecipientCerts)
        {
            byte[] symmetricKey = this.getSymmetricKeyOfEnvelope(aDecryptorStore);
            addTransRecipientInfo(config, symmetricKey, aNewRecipientCerts);
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
        public byte[] removeRecipientInfo(params ECertificate[] aRemoveCertificate)
        {
            _removeRecipientInfos(aRemoveCertificate);
            try
            {
                mContentInfo.setContent(mEnvelopeData.getEncoded());
                return _getContentInfoAsEncoded();
            }
            catch (Exception e)
            {
                throw new CMSException("Error in adding removing recipient from the enveloped data", e);
            }
        }

        private byte[] _getContentInfoAsEncoded()
        {
            return mContentInfo.getBytes();
        }
    }
}
