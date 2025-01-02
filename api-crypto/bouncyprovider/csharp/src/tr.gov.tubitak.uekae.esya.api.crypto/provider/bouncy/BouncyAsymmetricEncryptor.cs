using System;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.asn.algorithms;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyAsymmetricEncryptor : Encryptor
    {       
        public BouncyAsymmetricEncryptor(CipherAlg aCipherAlg):base(aCipherAlg)
        {
            if (OIDUtil.Equals(aCipherAlg.getOID(), _algorithmsValues.id_RSAES_OAEP))
            {
                string algorithmString = getOAEPAlgorithmStr(aCipherAlg);
                _mCipher = CipherUtilities.GetCipher(algorithmString);
            }                
            else
                _mCipher = CipherUtilities.GetCipher(BouncyProviderUtil.resolveCipher(aCipherAlg));     
        }

        public static string getOAEPAlgorithmStr(CipherAlg aCipherAlg)
        {
            OAEPPadding padding =(OAEPPadding) aCipherAlg.getPadding();
            
            if (padding.getDigestAlg().Equals(DigestAlg.SHA1))
                return "RSA//OAEPWITHSHA1ANDMGF1PADDING";
            else if (padding.getDigestAlg().Equals(DigestAlg.SHA256))
                return "RSA//OAEPWITHSHA256ANDMGF1PADDING";
            else if (padding.getDigestAlg().Equals(DigestAlg.SHA512))
                return "RSA//OAEPWITHSHA512ANDMGF1PADDING";
            else
                throw new Exception("Error in OAEP algorithm name..");
        }

        public override void init(IKey aKey, IAlgorithmParams aParams)
        {
            if (!(aKey is IPublicKey))
                throw new CryptoException("Key type is " + aKey.getAlgorithm() + ", not proper for asymmetric encryption");
            //mEncryptor.Init(true, BouncyProviderUtil.resolvePublicKey((IPublicKey)aKey));
            init(aKey.getEncoded(), aParams);
        }

        public override void init(byte[] aKey, IAlgorithmParams aParams)
        {
            ////todo BouncyKeyFactory implement edilince burayı tekrar düşün
            _mCipher.Init(true, BouncyProviderUtil.resolvePublicKey(aKey));
        }

        public override int getBlockSize()
        {
            return 0;
        }
    }
}
