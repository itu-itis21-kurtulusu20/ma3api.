using System;
using System.Collections.Generic;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.parameters;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.util;
using tr.gov.tubitak.uekae.esya.asn.cms;
using tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.asn.util;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors
{
    public class MemoryDecryptor : IDecryptorStore
    {
        Dictionary<ECertificate, IPrivateKey> mDecryptors;
        /**
         * 
         * @param aReceiverPrivate Private key.
         */
        public MemoryDecryptor(params Pair<ECertificate, IPrivateKey>[] decryptors)
        {
            init(decryptors);
        }

        public MemoryDecryptor(ECertificate cert, IPrivateKey privateKey)
        {
            Pair<ECertificate, IPrivateKey> decryptors = new Pair<ECertificate, IPrivateKey>(cert, privateKey);
            init(decryptors);
        }

        private void init(params Pair<ECertificate, IPrivateKey>[] decryptors)
        {
            mDecryptors = new Dictionary<ECertificate, IPrivateKey>();
            for (int i = 0; i < decryptors.Length; i++)
            {
                mDecryptors[decryptors[i].getmObj1()] = decryptors[i].getmObj2();
            }
        }


        public byte[] decrypt(ECertificate aCert, IDecryptorParams aParams)
        {
            IPrivateKey privKey = null;
            mDecryptors.TryGetValue(aCert, out privKey);
            byte[] symmetricKey = null;

            if (aParams is ECDHDecryptorParams)
            {
                ECDHDecryptorParams ecdhParams = (ECDHDecryptorParams)aParams;

                IPublicKey senderPublicKey = _getPublicKey(privKey, ecdhParams.getSenderPublicKey());
                int keyLength = KeyUtil.getKeyLength(WrapAlg.fromOID(ecdhParams.getKeyWrapAlgOid()));
                byte[] sharedInfoBytes = _getSharedInfoBytes(ecdhParams.getKeyWrapAlgOid(), keyLength, ecdhParams.getukm());

                WrapAlg wrapAlg = WrapAlg.fromOID(ecdhParams.getKeyWrapAlgOid());
                KeyAgreementAlg agreementAlg = KeyAgreementAlg.fromOID(ecdhParams.getKeyEncAlgOid());
                IAlgorithmParams sharedInfoParam = new ParamsWithSharedInfo(sharedInfoBytes);

                IKeyAgreement agreement = Crypto.getKeyAgreement(agreementAlg);
                agreement.init(privKey, sharedInfoParam);
                byte[] secretKeyBytes = agreement.generateKey(senderPublicKey, wrapAlg);

                try
                {
                    byte[] wrappedKey = ecdhParams.getWrappedKey();
                    IWrapper wrapper = Crypto.getUnwrapper(WrapAlg.fromOID(ecdhParams.getKeyWrapAlgOid()));
                    wrapper.init(secretKeyBytes);
                    symmetricKey = wrapper.process(wrappedKey);
                }
                catch (Exception e)
                {
                    throw new CryptoException("Can not unwrap key", e);
                }
            }
            else if (aParams is RSADecryptorParams)
            {
                RSADecryptorParams rsaParams = (RSADecryptorParams)aParams;

                symmetricKey = CipherUtil.decryptRSA(rsaParams.getEncryptedKey(), (PrivateKey)privKey, rsaParams.GetAlgorithmIdentifier());

            }
            else
            {
                throw new CryptoException("Unknown parameter type");
            }
            return symmetricKey;
        }

        //Generate shared info bytes that will be used in key derivation
        private byte[] _getSharedInfoBytes(int[] keyWrapAlgOid, int keyLength, byte[] ukm)
        {
            byte[] encodedSharedInfo = null;

            ECC_CMS_SharedInfo info = new ECC_CMS_SharedInfo();
            Asn1OpenType openType = new Asn1OpenType(new byte[] { 5, 0 });
            info.keyInfo = new AlgorithmIdentifier(new Asn1ObjectIdentifier(keyWrapAlgOid), openType);
            info.suppPubInfo = new Asn1OctetString(integerToBytes(keyLength));
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


        //Generate sender public key from receiver private key algorithm identifier and sender public key bytes
        private IPublicKey _getPublicKey(IPrivateKey receiverPrivate, byte[] senderPublicKeyBytes)
        {
            IPublicKey senderPublicKey = null;
            try
            {
                PrivateKeyInfo privInfo = new PrivateKeyInfo();
                AsnIO.arraydenOku(privInfo, receiverPrivate.getEncoded());

                Asn1BitString subjectPublicKey = new Asn1BitString(senderPublicKeyBytes.Length * 8, senderPublicKeyBytes);
                SubjectPublicKeyInfo publicKeyInfo = new SubjectPublicKeyInfo(privInfo.privateKeyAlgorithm, subjectPublicKey);
                Asn1DerEncodeBuffer buff = new Asn1DerEncodeBuffer();
                publicKeyInfo.Encode(buff);

                IKeyFactory keyFactory = Crypto.getKeyFactory();
                senderPublicKey = keyFactory.decodePublicKey(AsymmetricAlg.ECDSA, buff.MsgCopy);

            }
            catch (Exception e)
            {
                throw new CryptoException("Can not encode PublicKey", e);
            }
            return senderPublicKey;
        }

        private byte[] integerToBytes(int keySize)
        {
            byte[] val = new byte[4];

            val[0] = (byte)(keySize >> 24);
            val[1] = (byte)(keySize >> 16);
            val[2] = (byte)(keySize >> 8);
            val[3] = (byte)keySize;

            return val;
        }

        public ECertificate[] getEncryptionCertificates()
        {
            ECertificate[] encryptionCertificates = new ECertificate[mDecryptors.Keys.Count];
            mDecryptors.Keys.CopyTo(encryptionCertificates, 0);
            return encryptionCertificates;
        }
    }
}
