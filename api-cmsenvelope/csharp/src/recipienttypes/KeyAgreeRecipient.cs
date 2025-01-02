using System;
using System.IO;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.cms;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.recipienttypes
{
    public class KeyAgreeRecipient : RecipientInfo
    {
        private readonly KeyAgreeRecipientInfo ri = new KeyAgreeRecipientInfo();

        private readonly Certificate mRecipientCert;
        private readonly KeyAgreementAlg mKeyAgreementAlg;
        private readonly WrapAlg mWrapAlg;

        public KeyAgreeRecipient()
            : base()
        {
            //super();
            _ilkIsler();
        }

        public KeyAgreeRecipient(KeyAgreeRecipientInfo aRI)
            : base()
        {
            //super();
            ri = aRI;
            SetElement(_KARI, ri);
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



        public KeyAgreeRecipient(Certificate aRecipientCertificate, KeyAgreementAlg aKeyAgreementAlg, WrapAlg keyWrapAlg)
            : base()
        {
            if (aKeyAgreementAlg.getAgreementAlg() != AgreementAlg.ECCDH && aKeyAgreementAlg.getAgreementAlg() != AgreementAlg.ECDH)
                throw new CryptoException("Unknown algorithms");

            _ilkIsler();

            mRecipientCert = aRecipientCertificate;
            mKeyAgreementAlg = aKeyAgreementAlg;
            mWrapAlg = keyWrapAlg;
        }

        public void calculateAndSetEncyptedKey(byte[] symmetricKey)
        {
            KeyPair ephemeralKey = null;
            try
            {
                ParamsWithAlgorithmIdentifier paramsWithAlgIden = new ParamsWithAlgorithmIdentifier(mRecipientCert.tbsCertificate.subjectPublicKeyInfo.algorithm);
                IKeyPairGenerator kpg = Crypto.getKeyPairGenerator(AsymmetricAlg.ECDSA);
                ephemeralKey = kpg.generateKeyPair(paramsWithAlgIden);
            }
            catch (Exception e)
            {
                throw new CryptoException("Domain OID error", e);
            }


            //Generate shared info bytes that will be used in key derivation
            int keyLength = KeyUtil.getKeyLength(mWrapAlg);
            byte[] sharedInfoBytes = _getSharedInfoBytes(mWrapAlg.getOID(), keyLength, null);
            IAlgorithmParams sharedInfoParam = new ParamsWithSharedInfo(sharedInfoBytes);

            IPublicKey receiverPubKey = KeyUtil.decodePublicKey(new ESubjectPublicKeyInfo(mRecipientCert.tbsCertificate.subjectPublicKeyInfo));

            IKeyAgreement agreement = Crypto.getKeyAgreement(mKeyAgreementAlg);
            agreement.init(ephemeralKey.getPrivate(), sharedInfoParam);
            byte[] secretKeyBytes = agreement.generateKey(receiverPubKey, mWrapAlg);

            byte[] wrappedBytes = null;

            try
            {
                IWrapper wrapper = Crypto.getWrapper(mWrapAlg);
                wrapper.init(secretKeyBytes);
                wrappedBytes = wrapper.process(symmetricKey);
            }
            catch (Exception e)
            {
                throw new CryptoException("Key wrapping error", e);
            }

            OriginatorPublicKey originator = _createOriginator(ephemeralKey.getPublic());
            _setOriginatorIdentifierOrKey(originator);
            _setEncryptedKey(UtilCMS.issuerAndSerialNumberOlustur(mRecipientCert), wrappedBytes);
            try
            {
                _setKeyEncryptionAlgorithm(mKeyAgreementAlg.getOID(), mWrapAlg.getOID());
            }
            catch (Asn1Exception e)
            {
                throw new CryptoException("Algorithm asn1 convention error ", e);
            }
        }

        private OriginatorPublicKey _createOriginator(IPublicKey publicKey)
        {
            SubjectPublicKeyInfo spki;
            try
            {
                byte[] spkiBytes = publicKey.getEncoded();
                spki = new SubjectPublicKeyInfo();
                AsnIO.arraydenOku(spki, spkiBytes);
            }
            catch (Exception e)
            {
                throw new CryptoException("generating originator", e);
            }

            OriginatorPublicKey originator = new OriginatorPublicKey(
                     new AlgorithmIdentifier(spki.algorithm.algorithm, UtilOpenType.Asn1NULL),
                     spki.subjectPublicKey);

            return originator;
        }

        public void setOriginatorIdentifierOrKey(IssuerAndSerialNumber aIssuerAndSerialNumber)
        {
            ri.originator = new OriginatorIdentifierOrKey();
            ri.originator.Set_issuerAndSerialNumber(aIssuerAndSerialNumber);
        }

        public void setOriginatorIdentifierOrKey(SubjectKeyIdentifier aSubjectKeyIdentifier)
        {
            ri.originator = new OriginatorIdentifierOrKey();
            ri.originator.Set_subjectKeyIdentifier(aSubjectKeyIdentifier);
        }

        private void _setOriginatorIdentifierOrKey(OriginatorPublicKey aOriginatorPublicKey)
        {
            ri.originator = new OriginatorIdentifierOrKey();
            ri.originator.Set_originatorKey(aOriginatorPublicKey);
        }

        private void _setUKM(byte[] aUKM)
        {
            ri.ukm = new Asn1OctetString(aUKM);
        }

        private void _setKeyEncryptionAlgorithm(int[] aKeyAgreementOid, int[] aKeyWrapOid)
        {
            AlgorithmIdentifier keyWrapIdentifier = new AlgorithmIdentifier(aKeyWrapOid, new Asn1OpenType(new byte[] { 5, 0 }));
            Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
            keyWrapIdentifier.Encode(buff);

            ri.keyEncryptionAlgorithm = new AlgorithmIdentifier(aKeyAgreementOid, new Asn1OpenType(buff.MsgCopy));
        }



        private void _setEncryptedKey(IssuerAndSerialNumber receipientIssuerSerial, byte[] encryptedKey)
        {
            KeyAgreeRecipientIdentifier recipientIdentifier = new KeyAgreeRecipientIdentifier();
            recipientIdentifier.Set_issuerAndSerialNumber(receipientIssuerSerial);
            ri.recipientEncryptedKeys = new RecipientEncryptedKeys(1);
            ri.recipientEncryptedKeys.elements[0] = new RecipientEncryptedKey(recipientIdentifier, new Asn1OctetString(encryptedKey));
        }

        public byte[] getSenderPublicKey()
        {
            int choice = ri.originator.ChoiceID;
            if (choice == OriginatorIdentifierOrKey._ORIGINATORKEY)
            {
                return ((OriginatorPublicKey)ri.originator.GetElement()).publicKey.mValue;
            }
            return null;
        }

        public EIssuerAndSerialNumber getIssuerAndSerialNumber()
        {
            KeyAgreeRecipientIdentifier recipientIdentifier = ri.recipientEncryptedKeys.elements[0].rid;
            if (recipientIdentifier.ChoiceID == KeyAgreeRecipientIdentifier._ISSUERANDSERIALNUMBER)
                return new EIssuerAndSerialNumber((IssuerAndSerialNumber)recipientIdentifier.GetElement());
            return null;
        }

        public ESubjectKeyIdentifier getSubjectKeyIdentifier()
        {
            KeyAgreeRecipientIdentifier recipientIdentifier = ri.recipientEncryptedKeys.elements[0].rid;
            if (recipientIdentifier.ChoiceID == RecipientIdentifier._SUBJECTKEYIDENTIFIER)
            {
                return new ESubjectKeyIdentifier((SubjectKeyIdentifier)recipientIdentifier.GetElement());
            }
            return null;
        }

        public byte[] getWrappedKey()
        {
            return ri.recipientEncryptedKeys.elements[0].encryptedKey.mValue;
        }

        public int[] getKeyEncAlgOID()
        {
            return ri.keyEncryptionAlgorithm.algorithm.mValue;
        }


        public int[] getkeyWrapAlgOID()
        {
            Asn1DerDecodeBuffer derBuffer = new Asn1DerDecodeBuffer(ri.keyEncryptionAlgorithm.parameters.mValue);
            AlgorithmIdentifier keyWrapAlgID = new AlgorithmIdentifier();

            try
            {
                keyWrapAlgID.Decode(derBuffer);
            }
            catch (Asn1Exception e)
            {
                throw new CryptoException("Wrap Algorithm can not be parsed", e);
            }
            catch (IOException e)
            {
                throw new CryptoException("Wrap Algorithm can not be parsed", e);
            }

            return keyWrapAlgID.algorithm.mValue;
        }

        public byte[] getUKM()
        {
            if (ri.ukm != null)
                return ri.ukm.mValue;
            else
                return null;
        }


        private void _ilkIsler()
        {
            SetElement(_KARI, ri);
            ri.version = new CMSVersion(3);
        }


        private byte[] _getSharedInfoBytes(int[] keyWrapAlgOid, int keyLength, byte[] ukm)
        {
            byte[] encodedSharedInfo = null;

            ECC_CMS_SharedInfo info = new ECC_CMS_SharedInfo();
            info.keyInfo = new AlgorithmIdentifier(new Asn1ObjectIdentifier(keyWrapAlgOid), UtilOpenType.Asn1NULL);
            info.suppPubInfo = new Asn1OctetString(_integerToBytes(keyLength));
            if (ukm != null)
                info.entityUInfo = new Asn1OctetString(ukm);

            try
            {
                Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
                info.Encode(buff);
                encodedSharedInfo = buff.MsgCopy;
            }
            catch (Exception e)
            {
                encodedSharedInfo = null;
            }
            return encodedSharedInfo;
        }

        private byte[] _integerToBytes(int keySize)
        {
            byte[] val = new byte[4];

            val[0] = (byte)(keySize >> 24);
            val[1] = (byte)(keySize >> 16);
            val[2] = (byte)(keySize >> 8);
            val[3] = (byte)keySize;

            return val;
        }

        public CMSVersion getCMSVersion()
        {
            return ri.version;
        }
    }
}
