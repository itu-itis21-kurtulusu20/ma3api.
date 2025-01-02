using System;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyVerifier : IVerifier
    {
        private Org.BouncyCastle.Crypto.ISigner mSignature;
        private readonly SignatureAlg mSignatureAlg;
        private IAlgorithmParams mParams;


        public BouncyVerifier(SignatureAlg aSignatureAlg)
        {
            mSignatureAlg = aSignatureAlg;
            //mSignature = SignerUtilities.GetSigner(BouncyProviderUtil.resolveSignature(aSignatureAlg));
        }

        public void init(IPublicKey aPublicKey)
        {
            mSignature = SignerUtilities.GetSigner(mParams == null ? BouncyProviderUtil.resolveSignatureName(mSignatureAlg) : BouncyProviderUtil.resolveSignatureName(mSignatureAlg, mParams));

            mSignature.Init(false, BouncyProviderUtil.resolvePublicKey(aPublicKey));
        }

        public void init(IPublicKey aPublicKey, IAlgorithmParams aParams)
        {            
            mParams = aParams;
            init(aPublicKey);            
        }

        public void reset()
        {
            mSignature.Reset();
        }

        public void update(byte[] aData)
        {
            update(aData, 0, aData.Length);
        }

        public void update(byte[] aData, int aOffset, int aLength)
        {
            mSignature.BlockUpdate(aData, aOffset, aLength);
        }

        public bool verifySignature(byte[] aSignature)
        {
            return mSignature.VerifySignature(aSignature);
        }
    }
}
