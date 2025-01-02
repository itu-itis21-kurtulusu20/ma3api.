using System;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;

namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncySigner : Signer
    {
        private Org.BouncyCastle.Crypto.ISigner mSignature;
        private readonly SignatureAlg mSignatureAlg;        
        private IAlgorithmParams mParams;


        public BouncySigner(SignatureAlg aSignatureAlg)
        {
            mSignatureAlg = aSignatureAlg;
            //mSignature = SignerUtilities.GetSigner(BouncyProviderUtil.resolveSignature(aSignatureAlg));
        }

        public void init(AsymmetricKeyParameter aBouncyPrivateKey)
        {
            string bouncySignatureName = mParams == null
                ? BouncyProviderUtil.resolveSignatureName(mSignatureAlg)
                : BouncyProviderUtil.resolveSignatureName(mSignatureAlg, mParams);
            mSignature = SignerUtilities.GetSigner(bouncySignatureName);
            mSignature.Init(true, aBouncyPrivateKey);
        }

        public override void init(IPrivateKey aPrivateKey)
        {
            string bouncySignatureName = mParams == null
                ? BouncyProviderUtil.resolveSignatureName(mSignatureAlg)
                : BouncyProviderUtil.resolveSignatureName(mSignatureAlg, mParams);
            mSignature = SignerUtilities.GetSigner(bouncySignatureName);            
            mSignature.Init(true, BouncyProviderUtil.resolvePrivateKey(aPrivateKey));                                         
        }

        //@Override
        public override void init(IPrivateKey aPrivateKey, IAlgorithmParams aParams)
        {
            mParams = aParams;
            init(aPrivateKey);
        }

        //@Override
        public override void reset()
        {
            mSignature.Reset();
        }

        public override void update(byte[] aData)
        {
            if (aData != null)
                update(aData, 0, aData.Length);
        }

        public override void update(byte[] aData, int aOffset, int aLength)
        {
            if (aData != null)
                mSignature.BlockUpdate(aData, aOffset, aLength);
        }

        public override byte[] sign(byte[] aData)
        {
            if (aData != null)
                update(aData);
            return (byte[])mSignature.GenerateSignature();
        }        

        public override SignatureAlg getSignatureAlgorithm()
        {
            return mSignatureAlg;
        }
        public override IAlgorithmParameterSpec getAlgorithmParameterSpec()
        {
             return mParams;
        }
    }
}
