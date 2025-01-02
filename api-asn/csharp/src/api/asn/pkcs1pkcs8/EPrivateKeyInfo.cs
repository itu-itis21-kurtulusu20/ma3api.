using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.pkcs1pkcs8;
namespace tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8
{
    public class EPrivateKeyInfo : BaseASNWrapper<PrivateKeyInfo>
    {
        public EPrivateKeyInfo(PrivateKeyInfo aObject)
            : base(aObject)
        {
        }

        public EPrivateKeyInfo(byte[] aObjectBytes)
            : base(aObjectBytes, new PrivateKeyInfo())
        {
        }

        public EPrivateKeyInfo(EVersion aVersion, EAlgorithmIdentifier aAlg, byte[] aPrivateKeyBytes)
            : base(new PrivateKeyInfo(aVersion.getObject(), aAlg.getObject(), new Asn1OctetString(aPrivateKeyBytes)))
        {
        }

        public EAlgorithmIdentifier getAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.privateKeyAlgorithm);
        }

        public byte[] getPrivateKey()
        {
            return mObject.privateKey.mValue;
        }

        public int getKeyLength()
        {
            return mObject.privateKey.Length * 8;
        }
    }
}
