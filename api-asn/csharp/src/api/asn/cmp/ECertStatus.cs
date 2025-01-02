using tr.gov.tubitak.uekae.esya.asn.cmp;
using Com.Objsys.Asn1.Runtime;
namespace tr.gov.tubitak.uekae.esya.api.asn.cmp
{
    public class ECertStatus : BaseASNWrapper<CertStatus>
    {
        public ECertStatus(CertStatus aObject)
            : base(aObject)
        {
        }

        public ECertStatus(byte[] aBytes)
            : base(aBytes, new CertStatus())
        {
        }
    }
}
