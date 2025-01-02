using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.x509;

namespace tr.gov.tubitak.uekae.esya.api.asn.x509
{
    public class ECertificateList : BaseASNWrapper<CertificateList>
    {
        public ECertificateList(CertificateList aObject)
            : base(aObject)
        {
            //super(aObject);
        }

        public ECertificateList(byte[] aBytes)
            : base(aBytes, new CertificateList())
        {
            //super(aBytes, new CertificateList());
        }

        public ECertificateList()
            : base(new CertificateList())
        {
            //super(new CertificateList());
            mObject.tbsCertList = new TBSCertList();
        }

        public EAlgorithmIdentifier getSignatureAlgorithm()
        {
            return new EAlgorithmIdentifier(mObject.signatureAlgorithm);
        }

        public void setSignatureAlgorithm(EAlgorithmIdentifier signatureAlgorithm)
        {
            this.mObject.signatureAlgorithm = signatureAlgorithm.getObject();
        }

        public byte[] getSigningPart()
        {
            return new ETBSCertList(mObject.tbsCertList).getBytes();
        }

        public byte[] getSignature()
        {
            return mObject.signature.mValue;
        }

        public void setSignature(byte[] signature)
        {
            this.mObject.signature = new Asn1BitString(signature.Length << 3, signature);
        }

        public void setTBSCertList(ETBSCertList tbsCertList)
        {
            mObject.tbsCertList = tbsCertList.getObject();
        }

        public ETBSCertList getTBSCertList()
        {
            if (mObject.tbsCertList == null)
                return null;
            else
                return new ETBSCertList(mObject.tbsCertList);
        }
    }
}
