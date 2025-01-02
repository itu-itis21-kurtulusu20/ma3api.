using System;
using tr.gov.tubitak.uekae.esya.api.common.crypto;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.crypto.parameters;
using tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes;
/**
 * @author ayetgin
 */

//todo Annotation!
//@ApiClass
namespace tr.gov.tubitak.uekae.esya.api.crypto
{
    public abstract class Signer : BaseSigner
    {
        public abstract byte[] sign(byte[] aData);
        
        public abstract void init(IPrivateKey aPrivateKey);
        public abstract void init(IPrivateKey aPrivateKey, IAlgorithmParams aParams);

        public abstract void reset();
        public abstract void update(byte[] aData);
        public abstract void update(byte[] aData, int aOffset, int aLength);

        public abstract SignatureAlg getSignatureAlgorithm();

        public String getSignatureAlgorithmStr()
        {
            return getSignatureAlgorithm().getName();
        }

        public abstract IAlgorithmParameterSpec getAlgorithmParameterSpec();
    }
}
