using System;
using System.Collections.Generic;
using System.Linq;
using iaik.pkcs.pkcs11.wrapper;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11.scheme;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors.parameters;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider;
using tr.gov.tubitak.uekae.esya.api.smartcard.pkcs11;
using tr.gov.tubitak.uekae.esya.asn.algorithms;


namespace tr.gov.tubitak.uekae.esya.api.cmsenvelope.decryptors
{
    public class SCDecryptor : IDecryptorStore
    {
        public SmartCard mSmartCard = null;
        protected long mSessionID = -1;
        protected ECertificate[] mCerts;
        readonly ICryptoProvider mProvider;

        public SCDecryptor(SmartCard aSmartCard, long aSessionID)
        {
            mSmartCard = aSmartCard;
            mSessionID = aSessionID;
            mCerts = _readEncryptionCertificates();
        }

        public SCDecryptor(SmartCard aSmartCard, long aSessionID, ICryptoProvider mProvider,bool isReadCertificates) {
            mSmartCard = aSmartCard;
            mSessionID = aSessionID;
            this.mProvider = mProvider;
            if(isReadCertificates)
                mCerts = _readEncryptionCertificates();
        }

        public SCDecryptor(SmartCard aSmartCard, long aSessionID, bool isReadCertificates)
        {
            mSmartCard = aSmartCard;
            mSessionID = aSessionID;
            if(isReadCertificates)
              mCerts = _readEncryptionCertificates();
        }

        public SCDecryptor(SmartCard aSmartCard, long aSessionID, ICryptoProvider mProvider) {
            mSmartCard = aSmartCard;
            mSessionID = aSessionID;
            this.mProvider = mProvider;
            mCerts = _readEncryptionCertificates();
        }

        public byte[] decrypt(ECertificate aCert, IDecryptorParams params_)
        {
            ECertificate recipientCert = null;
            foreach (ECertificate cert in mCerts)
            {
                if (cert.Equals(aCert))
                {
                    recipientCert = aCert;
                    break;
                }
            }

            if (recipientCert != null)
            {
                byte[] serial = recipientCert.getSerialNumber().GetData();
                byte[] decryptedData = null;

                if (params_ is RSADecryptorParams)
                {
                    byte[] encryptedData = ((RSADecryptorParams)params_).getEncryptedKey();
                    try
                    {

                        Pair<CipherAlg, IAlgorithmParams> cipherAlg =  ((RSADecryptorParams)params_).getCipherAlg();
  
                        if(cipherAlg.first().getOID().SequenceEqual(_algorithmsValues.id_RSAES_OAEP))
                        {
                            OAEPPadding paddingAlg = (OAEPPadding)cipherAlg.first().getPadding();
                            long slotID = mSmartCard.getSessionInfo(mSessionID).slotID;
                            long[] supportedMechs = mSmartCard.getMechanismList(slotID);

                            RSAOAEP_ES rsaoaep = new RSAOAEP_ES(paddingAlg, supportedMechs);

                            byte[] paddedKey = mSmartCard.decryptDataWithCertSerialNo(mSessionID, serial, rsaoaep.getMechanism(),encryptedData);
                            decryptedData = rsaoaep.getResult(paddedKey);

                        }
                        else
                        {
                            decryptedData = mSmartCard.decryptDataWithCertSerialNo(mSessionID, serial, PKCS11Constants_Fields.CKM_RSA_PKCS, encryptedData);
                        }
                        return decryptedData;
                    }
                    catch (Exception aEx)
                    {
                        throw new CryptoException("Kartta sifre cozulurken hata olustu", aEx);
                    }                 
                }
                throw new CryptoException("Algorithm does not support");
            }

            throw new CryptoException("Certificate not found");
        }

        public ECertificate[] getEncryptionCertificates()
        {
            return mCerts;
        }

        private ECertificate[] _readEncryptionCertificates()
        {
            ECertificate[] certs = null;
            try
            {
                List<byte[]> certDatas = mSmartCard.getEncryptionCertificates(mSessionID);
                certs = new ECertificate[certDatas.Count];
                for (int i = 0; i < certs.Length; i++)
                    certs[i] = new ECertificate(certDatas[i]);
            }
            catch (PKCS11Exception e)
            {
                throw new CryptoException(e.Message);
            }
            catch (SmartCardException e)
            {
                throw new CryptoException(e.Message);
            }
            catch (ESYAException e)
            {
                throw new CryptoException("Can not decode cert");
            }
            return certs;
        }
    }
}
