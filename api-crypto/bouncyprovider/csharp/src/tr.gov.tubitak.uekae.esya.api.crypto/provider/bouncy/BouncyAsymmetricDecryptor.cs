using System;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.asn.algorithms;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyAsymmetricDecryptor : Decryptor
    {
        //private CipherAlg mCipherAlg;
        //private IBufferedCipher mDecryptor;
       

        public BouncyAsymmetricDecryptor(CipherAlg aCipherAlg)
            : base(aCipherAlg)
        {
 
            if (OIDUtil.Equals(aCipherAlg.getOID(), _algorithmsValues.id_RSAES_OAEP))
            {
                string algorithmString = BouncyAsymmetricEncryptor.getOAEPAlgorithmStr(aCipherAlg);
                _mCipher = CipherUtilities.GetCipher(algorithmString);
            }
            else
                _mCipher = CipherUtilities.GetCipher(BouncyProviderUtil.resolveCipher(aCipherAlg));   

        }

        public override void init(IKey aKey, IAlgorithmParams aParams)
        {
            //mDecryptor.Init(false, BouncyProviderUtil.resolvePrivateKey((IPrivateKey)aKey));
            init(aKey.getEncoded(), aParams);       
        }

        public override void init(byte[] aKey, IAlgorithmParams aParams)
        {
            //init((new GNUKeyFactory()).decodePrivateKey(AsymmetricAlg.RSA, aKey), aParams);
            //todo BouncyKeyFactory implement edilince burayı tekrar düşün
            _mCipher.Init(false, BouncyProviderUtil.resolvePrivateKey(aKey));
        }
        public override int getBlockSize()
        {
            return 0;
        }        
    }
}
