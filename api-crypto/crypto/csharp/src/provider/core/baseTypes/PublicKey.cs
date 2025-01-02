using System;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
/**
 * Member olarak asn1 wrapper'i olan ESubjectPublicKeyInfo içeren ve IPublicKey'i implement eder
 * 
 * */
namespace tr.gov.tubitak.uekae.esya.api.crypto.provider.core.baseTypes
{
    public class PublicKey : IPublicKey
    {
        private readonly ESubjectPublicKeyInfo _mSubjectPublicKeyInfo;

        public PublicKey(ESubjectPublicKeyInfo aSubjectPublicKeyInfo)
        {
            _mSubjectPublicKeyInfo = aSubjectPublicKeyInfo;
        }
        public PublicKey(byte[] aPubBytes)
        {
            _mSubjectPublicKeyInfo = new ESubjectPublicKeyInfo(aPubBytes);
        }

        public String getAlgorithm()
        {
            return AsymmetricAlg.fromOID(_mSubjectPublicKeyInfo.getAlgorithm().getAlgorithm().mValue).getName();
        }

        public String getFormat()
        {
            return "X.509";
        }
        public byte[] getEncoded()
        {
            return _mSubjectPublicKeyInfo.getBytes();
        }
    }
}
