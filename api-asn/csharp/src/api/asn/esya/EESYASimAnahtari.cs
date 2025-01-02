using tr.gov.tubitak.uekae.esya.api.asn.x509;
using tr.gov.tubitak.uekae.esya.asn.esya;

namespace tr.gov.tubitak.uekae.esya.api.asn.esya
{
    public class EESYASimAnahtari : BaseASNWrapper<ESYASimAnahtari>
    {
        public EESYASimAnahtari(byte[] aBytes)
            :base(aBytes, new ESYASimAnahtari()){}

        public EESYASimAnahtari(ESYASimAnahtari aObject)
            :base(aObject){}

        public EESYASimAnahtari(long anahNo, EAlgorithmIdentifier hashAlg, EAlgorithmIdentifier simetrikAlg, byte[] anahtarBytes)
            :base(new ESYASimAnahtari(anahNo, hashAlg.getObject(), simetrikAlg.getObject(), anahtarBytes)){}

        public byte[] getAnahtarBytes()
        {
            return mObject.anahtarBytes.mValue;
        }

        public long getAnahtarNo()
        {
            return mObject.anahNo.mValue;
        }

        public EAlgorithmIdentifier getAlgorithmIdentifier()
        {
            return new EAlgorithmIdentifier(mObject.simetrikAlg);
        }
    }
}
