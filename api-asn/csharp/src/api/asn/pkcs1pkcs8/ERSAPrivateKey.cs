using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8;

namespace tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8
{
    public class ERSAPrivateKey : BaseASNWrapper<RSAPrivateKey>
    {
        public ERSAPrivateKey(RSAPrivateKey aRsaPrivateKey)
            : base(aRsaPrivateKey)
        {
        }

        public ERSAPrivateKey(EPrivateKeyInfo aPrivateKeyInfo)
            : this(aPrivateKeyInfo.getPrivateKey())
        {
        }

        public ERSAPrivateKey(byte[] aBytes)
            : base(aBytes, new RSAPrivateKey())
        {
        }

        public BigInteger getPrivateExponent()
        {
            return mObject.privateExponent.mValue;
        }

        public BigInteger getModulus()
        {
            return mObject.modulus.mValue;
        }

        public BigInteger getPublicExponent()
        {
            return mObject.publicExponent.mValue;
        }

        public BigInteger getPrimeP()
        {
            return mObject.prime1.mValue;
        }

        public BigInteger getPrimeQ()
        {
            return mObject.prime2.mValue;
        }

        public BigInteger getPrimeExponentP()
        {
            return mObject.exponent1.mValue;
        }

        public BigInteger getPrimeExponentQ()
        {
            return mObject.exponent2.mValue;
        }

        public BigInteger getCrtCoefficient()
        {
            return mObject.coefficient.mValue;
        }

    }
}
