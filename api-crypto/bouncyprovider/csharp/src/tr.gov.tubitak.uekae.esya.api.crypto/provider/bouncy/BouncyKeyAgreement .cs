using Com.Objsys.Asn1.Runtime;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Security;
using tr.gov.tubitak.uekae.esya.api.common.bundle;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
using BouncyBigInteger = Org.BouncyCastle.Math.BigInteger;
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.bouncy
{
    public class BouncyKeyAgreement : IKeyAgreement
    {
        private IBasicAgreement mKeyAgreement;
        private readonly KeyAgreementAlg mKeyAgreementAlg;
        private IKey mKey;

        public BouncyKeyAgreement(KeyAgreementAlg aKeyAgreementAlg)
        {
            if (aKeyAgreementAlg == null)
                throw new ArgErrorException("KeyAgreementAlg null");

            if (aKeyAgreementAlg.getAgreementAlg() != AgreementAlg.ECCDH && aKeyAgreementAlg.getAgreementAlg() != AgreementAlg.ECDH)
                throw new UnknownElement(Resource.message(Resource.AGREEMENTALG_BILINMIYOR));

            if (aKeyAgreementAlg.getAgreementAlg() == AgreementAlg.ECCDH || aKeyAgreementAlg.getAgreementAlg() == AgreementAlg.ECDH)
                mKeyAgreementAlg = aKeyAgreementAlg;
        }

        public void init(IKey aKey, IAlgorithmParams aParams)
        {
            //mKeyAgreement.init(aKey, aParams);
            //mKeyAgreement.Init()
            mKey = aKey;
        }

        public byte[] generateKey(IKey aKey, IAlgorithm aAlg)
        {
            mKeyAgreement = AgreementUtilities.GetBasicAgreementWithKdf(BouncyProviderUtil.resolveAgreeAlgorithm(mKeyAgreementAlg), BouncyProviderUtil.ToBouncy(new Asn1ObjectIdentifier(aAlg.getOID())).Id);
            mKeyAgreement.Init(BouncyProviderUtil.resolvePrivateKey(mKey.getEncoded()));
            BouncyBigInteger key = mKeyAgreement.CalculateAgreement(BouncyProviderUtil.resolvePublicKey(aKey.getEncoded()));
            return key.ToByteArrayUnsigned();   //todo ToByteArray() mi kullanılmalı        
        }
    }
}
