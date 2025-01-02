using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.exception;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.cms.envelope;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.certificate.validation;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.check.certificate;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;
using Attribute = tr.gov.tubitak.uekae.esya.asn.x509.Attribute;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    /**
     *  Şifreleme  işlemlerinde ortak kullanılan metodları içeren sınıftır. 
     * @author muratk
     *
     */
    abstract public class GeneratorBase : CMSEnvelopeBase
    {       
        public static readonly Asn1ObjectIdentifier OID_DATA = new Asn1ObjectIdentifier(_cmsValues.id_data);
        public static readonly Asn1ObjectIdentifier OID_DHSINGLEPASS_STDH = new Asn1ObjectIdentifier(new int[] { 1, 3, 133, 16, 840, 63, 0, 2 });

        protected CipherAlg mSymmetricAlgorithm;

        protected BufferedCipher mSymmetricCrypto;

        protected IAlgorithmParams mSymmetricAlgParams;

        protected GeneratorBase(CipherAlg aAlgorithm)
        {
            if (aAlgorithm.getMod() == Mod.GCM)
                mEnvelopeData = new EAuthenticatedEnvelopedData(new AuthEnvelopedData());
            else
                mEnvelopeData = new EEnvelopedData(new EnvelopedData());
        }

        /**
         * adds key tran recipient.
         * @param aRecipients recipient certificate
         * @throws CryptoException
         */
        protected void addKeyTransRecipientInfo(EnvelopeConfig config, params ECertificate[] aRecipients)
        {
            foreach (ECertificate cer in aRecipients)
            {
                checkLicense(cer);
                mEnvelopeData.addRecipientInfo(new KeyTransRecipient(cer.getObject(), config));
            }
        }

        /**
         * adds key agree recipient.
         * @param aKeyAgreementAlg algorithm that will be used on the key aggrement
         * @param aRecipients recipient certificate
         * @throws CryptoException
         */
        protected void addKeyAgreeRecipientInfo(EnvelopeConfig config, params ECertificate[] aRecipients)
        {
            foreach (ECertificate cer in aRecipients)
            {
                checkLicense(cer);
                WrapAlg wrapAlg = KeyUtil.getConvenientWrapAlg(mSymmetricAlgorithm);
                if (wrapAlg == null)
                    throw new CryptoException("This symmetric algorithm is not supported for KeyAgreement");
                mEnvelopeData.addRecipientInfo(new KeyAgreeRecipient(cer.getObject(), config.getEcKeyAgreementAlg(), wrapAlg));
            }
        }

        /**
	    * Adds new recipient without certificate checking. According to recipient type, default values will be filled to the required fields.
	    * @param aRecipients recipient certificate
	    *@throws CryptoException
	    */
	    protected void addRecipientInfos(params ECertificate[] aRecipients) {
		   addRecipientInfos(new EnvelopeConfig(), aRecipients);
	    }

        public void addRecipients(EnvelopeConfig config, params ECertificate[] aRecipients)
        {
            if (config.isCertificateValidationActive())
            {
                foreach (ECertificate cer in aRecipients)
                {
                    CertificateStatusInfo csi = CertificateValidation.validateCertificate(config.getValidationSystem(), cer);
                    if (csi.getCertificateStatus() != CertificateStatus.VALID)
                    {
                        throw new CertValidationException(csi);
                    }
                }
            }
            addRecipientInfos(config,aRecipients);
        }

        /**
         * adds new recipient. according to recipient type, default values will be filled to the required fields.
         * @param aRecipients recipient certificate
         * @throws CryptoException
         */
        protected void addRecipientInfos(EnvelopeConfig config, params ECertificate[] aRecipients)
        {
            foreach (ECertificate cer in aRecipients)
            {
                int[] certAlg = cer.getObject().tbsCertificate.subjectPublicKeyInfo.algorithm.algorithm.mValue;
                //RSA			
                if (certAlg.SequenceEqual(_algorithmsValues.rsaEncryption))
                    addKeyTransRecipientInfo(config,aRecipients);
                //EC
                else if (certAlg.SequenceEqual(_algorithmsValues.id_ecPublicKey))
                    addKeyAgreeRecipientInfo(config, cer);
            }
        }

        /**
         * adds unprotected attribute
         * @param aAttribute the attribute
         */
        public void addUnProtectedAttribute(params Attribute[] aAttribute)
        {
            if (mEnvelopeData.getUnprotectedAttributes() == null)
            {
                mEnvelopeData.setUnprotectedAttributes(new UnprotectedAttributes(aAttribute));
            }
            else
            {
                List<Attribute> allAttribute = new List<Attribute>();
                List<Attribute> l1 = new List<Attribute>(aAttribute);
                List<Attribute> l2 = new List<Attribute>(mEnvelopeData.getUnprotectedAttributes().elements);
                allAttribute.AddRange(l1);
                allAttribute.AddRange(l2);
                mEnvelopeData.getUnprotectedAttributes().elements = allAttribute.ToArray();
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
                CertificateChoices[] cChoices = new CertificateChoices[aCertificates.Length];
                for (int i = 0; i < aCertificates.Length; i++)
                {
                    cChoices[i] = new CertificateChoices(CertificateChoices._CERTIFICATE, aCertificates[i]);
                }
                certSet = new CertificateSet(cChoices);
            }

            RevocationInfoChoices revocationChoices = null;

            if (aCRLs != null)
            {
                RevocationInfoChoice[] rChoices = new RevocationInfoChoice[aCRLs.Length];
                for (int i = 0; i < aCRLs.Length; i++)
                {
                    rChoices[i] = new RevocationInfoChoice(RevocationInfoChoice._CRL, aCRLs[i]);
                }

                revocationChoices = new RevocationInfoChoices(rChoices);
            }

            if (certSet != null || revocationChoices != null)
            {
                mEnvelopeData.setOriginatorInfo(new OriginatorInfo(certSet, revocationChoices));
            }

        }

        protected void _kriptoOrtakIsler(CipherAlg aAlgoritma, byte[] aAnahtar)
        {
            //RFC 3852(Chapter 14 Security Considerations)
            //IV must be randomly generated.
            byte[] iv = null;
            if (mSymmetricAlgParams == null)
            {
                iv = RandomUtil.generateRandom(aAlgoritma.getBlockSize());

                if (ByteUtil.isAllZero(iv))
                    throw new CryptoException("IV must be a non-zero value");

                if (aAlgoritma.getMod() == Mod.CBC)
                    mSymmetricAlgParams = new ParamsWithIV(iv);
                else if (aAlgoritma.getMod() == Mod.GCM)
                    mSymmetricAlgParams = new ParamsWithGCMSpec(iv);
            }

            if (mSymmetricAlgParams != null && mSymmetricAlgParams is ParamsWithIV) {
                iv = ((ParamsWithIV)mSymmetricAlgParams).getIV();

            }

            mSymmetricCrypto = Crypto.getEncryptor(aAlgoritma);
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

            EAlgorithmIdentifier params_;
            if (aAlgoritma.getMod() == Mod.GCM)
            {
               GCMParameters gcmParams = new GCMParameters(new Asn1OctetString(((ParamsWithGCMSpec)mSymmetricAlgParams).getIV()), new Asn1Integer((16)));
               Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
               gcmParams.Encode(buff);

               params_ = new EAlgorithmIdentifier(mSymmetricAlgorithm.getOID(), buff.MsgCopy);
            }
            else
                params_ = aAlgoritma.toAlgorithmIdentifier(iv);

            _setEncryptedContent(null, params_);
        }

        private void _setVersion()
        {
            mEnvelopeData.setVersion(_getVersion());
        }

        private void _recipientInfolariGuncelle(byte[] aSifrelenecekAnahtar)
        {
            for (int i = 0; i < mEnvelopeData.getRecipientInfoCount(); i++)
            {
                RecipientInfo ri = mEnvelopeData.getRecipientInfo(i);
                if (ri is KeyTransRecipient)
                {
                    KeyTransRecipient ktr = (KeyTransRecipient)ri;
                    ktr.calculateAndSetEncyptedKey(aSifrelenecekAnahtar);
                }
                else if (ri is KeyAgreeRecipient)
                {
                    KeyAgreeRecipient kar = (KeyAgreeRecipient)ri;
                    kar.calculateAndSetEncyptedKey(aSifrelenecekAnahtar);
                }
            }
        }

        private void _setEncryptedContent(byte[] aSifreliIcerik, EAlgorithmIdentifier aSifrelemeAlgoritmasi)
        {
            mEnvelopeData.setEncryptedContentInfo(
                    new EEncryptedContentInfo(OID_DATA, aSifrelemeAlgoritmasi, aSifreliIcerik));

        }

        private void _setContentType()
        {
            mContentInfo.setContentType(mEnvelopeData.getOID());
        }
    }
}
