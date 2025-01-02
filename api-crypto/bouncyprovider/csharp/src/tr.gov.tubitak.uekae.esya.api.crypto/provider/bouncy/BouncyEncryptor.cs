using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using Org.BouncyCastle.Asn1;
using Org.BouncyCastle.Asn1.Nist;
using System;
using System.IO;
using Com.Objsys.Asn1.Runtime;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyEncryptor : Encryptor
    {

        public BouncyEncryptor(CipherAlg aCipherAlg)
            : base(aCipherAlg)
        {
            _mCipher = CipherUtilities.GetCipher(BouncyProviderUtil.resolveCipher(aCipherAlg));
        }


        public override void init(IKey aKey, IAlgorithmParams aParams)
        {
            init(aKey.getEncoded(), aParams);
        }

        public override void init(byte[] aKey, IAlgorithmParams aParams)
        {
            byte[] iv = null;
            byte[] aadBytes = null;
            DerObjectIdentifier oid = BouncyProviderUtil.resolveCipher(_mCipherAlg);
            KeyParameter keyParameter = ParameterUtilities.CreateKeyParameter(oid, aKey);
            ICipherParameters cipherParameters = null;
  
            if (aParams is ParamsWithGCMSpec)
            {               
                int tagLength = 128; // in bits
                iv = ((ParamsWithGCMSpec) aParams).getIV();

                MemoryStream aad = ((ParamsWithGCMSpec) aParams).getAAD();
                if (aad != null)                   
                  aadBytes = ((ParamsWithGCMSpec) aParams).getAAD().ToArray();

                cipherParameters = new AeadParameters(keyParameter, tagLength, iv, aadBytes);
            }

            else if (aParams is ParamsWithIV)
            {           
                iv = ((ParamsWithIV)aParams).getIV();
                Asn1OpenType asn1openType = _mCipherAlg.toAlgorithmIdentifier(iv).getParameters();
                if (asn1openType != null)
                    cipherParameters = ParameterUtilities.GetCipherParameters(oid, keyParameter, Asn1Object.FromByteArray(asn1openType.mValue));
                else
                    cipherParameters = keyParameter;
            }
            else
            {
                cipherParameters = keyParameter;
            }
            _mCipher.Init(true, cipherParameters);
        }
        public override int getBlockSize()
        {
            return _mCipherAlg.getBlockSize();
        }
    }
}
