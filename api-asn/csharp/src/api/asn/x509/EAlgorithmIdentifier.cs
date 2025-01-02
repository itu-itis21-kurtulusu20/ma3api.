using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;
using System.Linq;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class EAlgorithmIdentifier : BaseASNWrapper<AlgorithmIdentifier>
    {
        public static readonly byte[] ASN_NULL = new byte[] { 0x05, 0x00 };

        // wrapper methods start here
        public EAlgorithmIdentifier(byte[] aEncodedAlgID)
            : base(aEncodedAlgID, new AlgorithmIdentifier())
        {
        }
        public EAlgorithmIdentifier(AlgorithmIdentifier aObject)
            : base(aObject)
        {
        }
        public EAlgorithmIdentifier(int[] aOID)
            : base(new AlgorithmIdentifier(aOID))
        {
            //super(new AlgorithmIdentifier(aOID));
        }
        public EAlgorithmIdentifier(int[] aAlgorithm, byte[] aParams):
            base(new AlgorithmIdentifier(aAlgorithm, new Asn1OpenType(aParams)))
        {
            //base(new AlgorithmIdentifier(aAlgorithm, new Asn1OpenType(aParams)));
        }

        public EAlgorithmIdentifier(Asn1ObjectIdentifier aIdentifier, Asn1OpenType aOpenType)
            : base(new AlgorithmIdentifier(aIdentifier, aOpenType))
        {
            //base(new AlgorithmIdentifier(aIdentifier, aOpenType));
        }

        //public EAlgorithmIdentifier(int[] algValue) : this(new AlgorithmIdentifier(algValue)) { }

        public Asn1ObjectIdentifier getAlgorithm()
        {
            return mObject.algorithm;
        }

        public void setAlgorithm(Asn1ObjectIdentifier aAlgorithm)
        {
            mObject.algorithm = aAlgorithm;
        }
        public bool hasParameters()
        {
            return mObject.parameters != null && !mObject.parameters.mValue.SequenceEqual(ASN_NULL);
        }

        public Asn1OpenType getParameters()
        {
            return mObject.parameters;
        }

        public void setParameters(Asn1OpenType aParameters)
        {
            mObject.parameters = aParameters;
        }
        public bool isAlgorithmEquals(Asn1ObjectIdentifier aObjectIdentifier)
        {
            if (mObject == null || mObject.algorithm == null || aObjectIdentifier == null)
                return false;
            return mObject.algorithm.Equals(aObjectIdentifier);
        }

    }
}
