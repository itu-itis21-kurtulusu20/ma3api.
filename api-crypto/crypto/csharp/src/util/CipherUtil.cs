using System;
using System.IO;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.common.util.bag;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto.util
{
    public static class CipherUtil
    {
        private static readonly int BLOCK_SIZE = 64000;

        public static byte[] encrypt(CipherAlg aCipherAlg, IAlgorithmParams aParams, byte[] aData, byte[] aSecretKey)
        {
            BufferedCipher encryptor = Crypto.getEncryptor(aCipherAlg);
            encryptor.init(aSecretKey, aParams);
            return encryptor.doFinal(aData);
        }

        public static byte[] decrypt(EAlgorithmIdentifier aAlgorithm, byte[] aData, byte[] aSecretKey)
        {
            Pair<CipherAlg, IAlgorithmParams> alg = CipherAlg.fromAlgorithmIdentifier(aAlgorithm);
            return decrypt(alg.first(), alg.second(), aData, aSecretKey);
        }

        public static byte[] decrypt(CipherAlg aCipherAlg, IAlgorithmParams aParams, byte[] aData, byte[] aSecretKey)
        {
            BufferedCipher cipher = Crypto.getDecryptor(aCipherAlg);
            cipher.init(aSecretKey, aParams);
            return cipher.doFinal(aData);
        }

        public static void encrypt(CipherAlg aCipherAlg, IAlgorithmParams aParams, Stream aToBeEncrypted, Stream aEncrypted, byte[] aKey)
        {
            BufferedCipher cipher = Crypto.getEncryptor(aCipherAlg);
            cipher.init(aKey, aParams);
            int read;
            cipher.setStream(aEncrypted);

            byte[] block = new byte[BLOCK_SIZE];
            while ((read = aToBeEncrypted.Read(block, 0, BLOCK_SIZE)) > 0)
            {
                if (read < BLOCK_SIZE)
                {   //son blok okundu
                    Array.Resize<byte>(ref block, read);
                    cipher.doFinal(block);
                    break;
                }
                else
                {   //sonuncu blok disindaki bir blok okundu
                    cipher.process(block);
                }
                //block.Initialize();      dizi elemanlarını default degerlerine dondurur                
            }
        }

        public static void decrypt(CipherAlg aCipherAlg, IAlgorithmParams aParams, Stream aEncrypted, Stream aDecrypted, byte[] aKey)
        {
            BufferedCipher encryptor = Crypto.getDecryptor(aCipherAlg);
            encryptor.init(aKey, aParams);
            encryptor.setStream(aDecrypted);
            int read;
            byte[] block = new byte[BLOCK_SIZE];
            while ((read = aEncrypted.Read(block, 0, BLOCK_SIZE)) > 0)
            {
                if (read < BLOCK_SIZE)
                {   //son blok okundu
                    Array.Resize<byte>(ref block, read);
                    encryptor.doFinal(block);
                    break;
                }
                else
                {   //sonuncu blok disindaki bir blok okundu
                    encryptor.process(block);
                }
                //block.Initialize();      dizi elemanlarını default degerlerine dondurur                
            }

        }

        public static byte[] encrypt(CipherAlg aCipherAlg, IAlgorithmParams aParams, byte[] aData, IPublicKey aSifPubKey)
        { 
            {
                BufferedCipher cipher = Crypto.getAsymmetricEncryptor(aCipherAlg);
                //cipher.init(aSifPubKey, aParams);        
                //todo init işlemi aParams'la da yapılabilir, farklı algoritmalar için IAlgorithmParams interface'i implement edilerek getParams metodu cagrilir. (IAlgorithmParams'in getParams() metodu olabilir)
                cipher.init(aSifPubKey, aParams);
                return cipher.doFinal(aData);
            }
        }
        public static byte[] encrypt(EAlgorithmIdentifier aAlgorithm, byte[] aData, PublicKey aSifPubKey)
        {
            Pair<CipherAlg, IAlgorithmParams> alg = CipherAlg.fromAlgorithmIdentifier(aAlgorithm);

            BufferedCipher cipher = Crypto.getAsymmetricEncryptor(alg.first());
            cipher.init(aSifPubKey, alg.second());
            return cipher.doFinal(aData);
        }

        public static byte[] encrypt(CipherAlg aCipherAlg, IAlgorithmParams aParams, byte[] aData, ECertificate aCertificate)
        {
            if (!isEnciphermentCertificate(aCertificate))
                throw new ArgErrorException(Resource.message(Resource.CERT_HATALI));
            return encrypt(aCipherAlg, aParams, aData, new PublicKey(aCertificate.getSubjectPublicKeyInfo()));
        }

        public static byte[] decrypt(CipherAlg aCipherAlg, IAlgorithmParams aParams, byte[] aData, IPrivateKey aSignPrivateKey)
        {
            BufferedCipher cipher = Crypto.getAsymmetricDecryptor(aCipherAlg);
            cipher.init(aSignPrivateKey, aParams);
            return cipher.doFinal(aData);
        }

        /**
        * use this method when the encoding is not known...
        */
        public static byte[] decryptRSA(byte[] aData, IPrivateKey aKey)
        {
            CipherAlg alg;

            alg = CipherAlg.RSA_PKCS1;
            try
            {
                return decrypt(alg, null, aData, aKey);
            }
            catch (CryptoException)
            {
                // not rsa pkcs1
            }

            alg = CipherAlg.RSA_OAEP_SHA256;
            try
            {
                return decrypt(alg, null, aData, aKey);
            }
            catch (CryptoException ex)
            {
                // not rsa oaep
                throw new CryptoException("Cant decrypt data, possibly encoding can not be determined.", ex);
            }

        }

        public static byte[] decryptRSA(byte[] aData, PrivateKey aKey, EAlgorithmIdentifier algorithmIdentifier) 
        {
            Pair<CipherAlg, IAlgorithmParams> cipherAlg = CipherAlg.fromAlgorithmIdentifier(algorithmIdentifier);
            return decrypt(cipherAlg.first(), cipherAlg.second(), aData, aKey);
        }

        //todo Bouncy icin ayrı bir PBE implementasyonuna ihtiyacım yok (icinde zaten var), o nedenle bu class'in decrypt metodu kullanılsın
        //todo test edilmeli?? 13.07.2010
        public static byte[] decrypt(PBEAlg aCipherAlg, IAlgorithmParams aParams, PBEKeySpec aKeySpec, byte[] aData)
        {
            BufferedCipher cipher = Crypto.getDecryptor(aCipherAlg.getCipherAlg());
            ISecretKey key = (ISecretKey)Crypto.getKeyFactory().generateSecretKey(aKeySpec);
            cipher.init(key, aParams);
            return cipher.doFinal(aData);
        }
        //todo test edilmeli?? 13.07.2010
        public static byte[] encrypt(PBEAlg aCipherAlg, IAlgorithmParams aParams, PBEKeySpec aKeySpec, byte[] aData)
        {
            BufferedCipher cipher = Crypto.getEncryptor(aCipherAlg.getCipherAlg());
            ISecretKey key = (ISecretKey)Crypto.getKeyFactory().generateSecretKey(aKeySpec);
            cipher.init(key, aParams);
            return cipher.doFinal(aData);

        }

        public static bool isEnciphermentCertificate(ECertificate aCertificate)
        {
            EKeyUsage keyUsage = aCertificate.getExtensions().getKeyUsage();
            return (keyUsage != null) && (keyUsage.isDataEncipherment() || keyUsage.isKeyEncipherment());
        }

    }
}
