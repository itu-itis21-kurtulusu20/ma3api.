using System;
using System.Collections.Generic;
using System.Linq;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.parameters;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope
{
    /**
     * Şifre çözme işlemlerinde ortak kullanılan metodları içeren sınıftır. 
     * @author muratk
     *
     */
    public abstract class ParserBase : CMSEnvelopeBase
    {
        public static readonly Asn1ObjectIdentifier OID_ENVELOPED_DATA = new Asn1ObjectIdentifier(_cmsValues.id_envelopedData);
        public static readonly Asn1ObjectIdentifier OID_DATA = new Asn1ObjectIdentifier(_cmsValues.id_data);

        protected ParserBase()
        {
           
        }

        /**
         * Returns the recipient's IssuerAndSerialNumber or SubjectKeyIdentifier in RecipientInfo array. 
         * @param aRecipientInfos 
         * @return return type can be tr.gov.tubitak.uekae.esya.api.asn.cms.EIssuerAndSerialNumber or
         * tr.gov.tubitak.uekae.esya.api.asn.x509.ESubjectKeyIdentifier according to cms
         */
        protected Object[] _getRecipientsInEnvelope()
        {
            RecipientInfo[] aRecipientInfos = mEnvelopeData.getRecipientInfos().elements;
            Object[] recipients = new Object[aRecipientInfos.Length];
            for (int i = 0; i < aRecipientInfos.Length; i++)
            {
                int choice = aRecipientInfos[i].ChoiceID;
                EIssuerAndSerialNumber isAndSerial = null;
                ESubjectKeyIdentifier keyIdentifier = null;
                switch (choice)
                {
                    case RecipientInfo._KTRI:
                        KeyTransRecipient ktr = new KeyTransRecipient((KeyTransRecipientInfo)aRecipientInfos[i].GetElement());
                        isAndSerial = ktr.getIssuerAndSerialNumber();
                        if (isAndSerial == null)
                            keyIdentifier = ktr.getSubjectKeyIdentifier();
                        break;

                    case RecipientInfo._KARI:
                        KeyAgreeRecipient kar = new KeyAgreeRecipient((KeyAgreeRecipientInfo)aRecipientInfos[i].GetElement());
                        isAndSerial = kar.getIssuerAndSerialNumber();
                        if (isAndSerial == null)
                            keyIdentifier = kar.getSubjectKeyIdentifier();
                        break;
                    default:
                        continue;
                }

                if (isAndSerial != null)
                {
                    recipients[i] = isAndSerial;
                }
                else if (keyIdentifier != null)
                {
                    recipients[i] = keyIdentifier;
                }
            }
            return recipients;
        }

        /**
         * @param aRecipientInfos recipientinfo set
         * @param aIssuerSerial issuer and serial of the recipient that is looking for
         * @return null if can not find the recipient
         */
        protected RecipientInfo findRecipient(RecipientInfo[] aRecipientInfos, ECertificate aRequestedCert)
        {
            EIssuerAndSerialNumber reqIsAndSerial = new EIssuerAndSerialNumber(aRequestedCert);
            SubjectKeyIdentifier reqSubKeyIden = aRequestedCert.getExtensions().getSubjectKeyIdentifier().getObject();

            RecipientInfo recipient = null;
            for (int i = 0; i < aRecipientInfos.Length; i++)
            {
                int choice = aRecipientInfos[i].ChoiceID;
                EIssuerAndSerialNumber isAndSerial = null;
                ESubjectKeyIdentifier keyIdentifier = null;
                switch (choice)
                {
                    case RecipientInfo._KTRI:
                        KeyTransRecipient ktr = new KeyTransRecipient((KeyTransRecipientInfo)aRecipientInfos[i].GetElement());
                        isAndSerial = ktr.getIssuerAndSerialNumber();
                        if (isAndSerial == null)
                            keyIdentifier = ktr.getSubjectKeyIdentifier();
                        break;

                    case RecipientInfo._KARI:
                        KeyAgreeRecipient kar = new KeyAgreeRecipient((KeyAgreeRecipientInfo)aRecipientInfos[i].GetElement());
                        isAndSerial = kar.getIssuerAndSerialNumber();
                        if (isAndSerial == null)
                            keyIdentifier = kar.getSubjectKeyIdentifier();
                        break;
                    default:
                        continue;
                }

                if (isAndSerial != null)
                {
                    if (isAndSerial.Equals(reqIsAndSerial))
                    {
                        recipient = aRecipientInfos[i];
                        break;
                    }

                }
                else if (keyIdentifier != null && keyIdentifier.Equals(reqSubKeyIden))
                {             
                        recipient = aRecipientInfos[i];
                        break;                   
                }
            }

            return recipient;

        }

        protected IDecryptorParams _getDecryptorParams(RecipientInfo[] aRecipientInfos,
                ECertificate aRecipient)
        {
            IDecryptorParams params_ = null;

            RecipientInfo recipient = findRecipient(aRecipientInfos, aRecipient);

            if (recipient != null)
            {
                switch (recipient.ChoiceID)
                {
                    case RecipientInfo._KTRI:

                        KeyTransRecipient ktr = new KeyTransRecipient((KeyTransRecipientInfo)recipient.GetElement());
                        EAlgorithmIdentifier encryptionAlgorithIdentifier = ktr.getEncryptionAlgorithmIdentifier();
                                        
                        byte[] sifreliAnahtar = ktr.getEncryptedKey();
                        RSADecryptorParams rsaParams = new RSADecryptorParams(sifreliAnahtar);
                        rsaParams.setAlgorithmIdentifier(encryptionAlgorithIdentifier);
				        params_ = rsaParams;                 
                        break;

                    case RecipientInfo._KARI:

                        KeyAgreeRecipient kar = new KeyAgreeRecipient((KeyAgreeRecipientInfo)recipient.GetElement());

                        byte[] wrappedKey = kar.getWrappedKey();
                        int[] keyEncAlgOid = kar.getKeyEncAlgOID();
                        int[] keyWrapAlgOid = kar.getkeyWrapAlgOID();
                        byte[] senderPublicKey = kar.getSenderPublicKey();
                        byte[] ukm = kar.getUKM();

                        params_ = new ECDHDecryptorParams(wrappedKey, keyEncAlgOid, keyWrapAlgOid, senderPublicKey, ukm);

                        break;
                }
            }
            return params_;
        }

        protected void addKeyAgreeRecipientInfo(EnvelopeConfig config, byte[] symmetricKey, params ECertificate[] aRecipientCerts)
        {
            try
            {
                EAlgorithmIdentifier simAlgIdent = mEnvelopeData.getEncryptedContentInfo().getEncryptionAlgorithm();
                Pair<CipherAlg, IAlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(simAlgIdent);
                WrapAlg wrapAlg = KeyUtil.getConvenientWrapAlg(cipherAlg.first());

                foreach (ECertificate aRecipientCer in aRecipientCerts)
                {
                    checkLicense(aRecipientCer);
                    KeyAgreeRecipient kar = new KeyAgreeRecipient(aRecipientCer.getObject(), config.getEcKeyAgreementAlg(), wrapAlg);

                    kar.calculateAndSetEncyptedKey(symmetricKey);
                    mEnvelopeData.addRecipientInfo(kar);
                }
            }
            catch (Exception e)
            {
                throw new CMSException("Error in adding new key agree recipient to the enveloped data", e);
            }
        }


        protected void addTransRecipientInfo(EnvelopeConfig config, byte[] symmetricKey, params ECertificate[] aRecipientCerts)
        {
            try
            {
                foreach (ECertificate aRecipientCer in aRecipientCerts)
                {
                    checkLicense(aRecipientCer);
                    KeyTransRecipient ktr = new KeyTransRecipient(aRecipientCer.getObject(),config);

                    ktr.calculateAndSetEncyptedKey(symmetricKey);
                    mEnvelopeData.addRecipientInfo(ktr);
                }
            }
            catch (Exception e)
            {
                throw new CMSException("Error in adding new key trans recipient to the enveloped data", e);
            }
        }

        protected void addRecipientInfo(EnvelopeConfig config, byte[] symmetricKey, params ECertificate[] aNewRecipientCerts)
        {
            foreach (ECertificate cer in aNewRecipientCerts)
            {
                int[] certAlg = cer.getObject().tbsCertificate.subjectPublicKeyInfo.algorithm.algorithm.mValue;
                //RSA
                //if(Arrays.equals(certAlg, _algorithmsValues.rsaEncryption))
                if (certAlg.SequenceEqual(_algorithmsValues.rsaEncryption))
                    addTransRecipientInfo(config, symmetricKey, cer);
                //EC
                else if (certAlg.SequenceEqual(_algorithmsValues.id_ecPublicKey))
                    addKeyAgreeRecipientInfo(config, symmetricKey, cer);
            }
        }

        protected void _removeRecipientInfos(params ECertificate[] aRemoveCerts)
        {
            RecipientInfo[] copyOfRecipients = (RecipientInfo[])mEnvelopeData.getRecipientInfos().elements.Clone();

            LinkedList<RecipientInfo> newRIs = new LinkedList<RecipientInfo>(copyOfRecipients);

            foreach (ECertificate removeCert in aRemoveCerts)
            {
                RecipientInfo[] recipients = newRIs.ToArray();
                RecipientInfo ri = findRecipient(recipients, removeCert);

                newRIs.Remove(ri);
            }

            if (newRIs.Count == 0)
                throw new CMSException("There must be at least one Recipient");

            RecipientInfo[] tRIArr = newRIs.ToArray();

            mEnvelopeData.setRecipientInfos(new RecipientInfos(tRIArr));
        }

        protected byte[] getSymmetricKeyOfEnvelope(IDecryptorStore aDecryptor)
        {
            try
            {               
                RecipientInfo[] recipientinfos = mEnvelopeData.getRecipientInfos().elements;
                ECertificate[] certs = aDecryptor.getEncryptionCertificates();

                foreach (ECertificate decryptorCert in certs)
                {
                    IDecryptorParams params_ = _getDecryptorParams(recipientinfos, decryptorCert);
                    if (params_ != null)
                    {
                        checkLicense(decryptorCert);
                        return aDecryptor.decrypt(decryptorCert, params_);
                    }
                }
            }
            catch (CryptoException ce)
            {
                throw new CMSException("Can not extract symmetric key from envelope", ce);
            }

            throw new CMSException("Your decryptor store does not contain one of the receipt of CMS!");
        }

        protected IssuerAndSerialNumber getIssuerAndSerialNumber(Certificate cert)
        {
            return new IssuerAndSerialNumber(cert.tbsCertificate.issuer, cert.tbsCertificate.serialNumber);
        }
    }
}
