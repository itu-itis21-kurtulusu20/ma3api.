using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.asn.pkcs1pkcs8;
using tr.gov.tubitak.uekae.esya.asn.algorithms;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ESubjectPublicKeyInfo : BaseASNWrapper<SubjectPublicKeyInfo>
    {
        public ESubjectPublicKeyInfo(SubjectPublicKeyInfo aObject)
            : base(aObject)
        {
        }

        public ESubjectPublicKeyInfo(byte[] aPubBytes)
            : base(aPubBytes, new SubjectPublicKeyInfo())
        {
        }

        public static ESubjectPublicKeyInfo createESubjectPublicKeyInfo(ERSAPublicKey aRSAPublicKey)
        {
            return createESubjectPublicKeyInfo(new AlgorithmIdentifier(_algorithmsValues.rsaEncryption, new Asn1OpenType(EAlgorithmIdentifier.ASN_NULL)), aRSAPublicKey);
        }
        public static ESubjectPublicKeyInfo createESubjectPublicKeyInfo(AlgorithmIdentifier aAlgId, ERSAPublicKey aRSAPublicKey)
        {
            byte[] rsaPublicKey = aRSAPublicKey.getBytes();
            Asn1BitString subjectPublicKey = new Asn1BitString(rsaPublicKey.Length<<3, rsaPublicKey);           
            return new ESubjectPublicKeyInfo(new SubjectPublicKeyInfo(aAlgId, subjectPublicKey));
        }

        public EAlgorithmIdentifier getAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.algorithm);
        }

        public void setAlgorithm(EAlgorithmIdentifier aAlgorithm)
        {
            mObject.algorithm = aAlgorithm.getObject();
        }

        public int getKeyLength()
        {
            return mObject.subjectPublicKey.numbits;
        }

        public byte[] getSubjectPublicKey()
        {
            return mObject.subjectPublicKey.mValue;
        }
    }
}
