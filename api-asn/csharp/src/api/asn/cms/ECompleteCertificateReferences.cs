
using tr.gov.tubitak.uekae.esya.asn.cms;
namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ECompleteCertificateReferences : BaseASNWrapper<CompleteCertificateRefs>
    {
        public ECompleteCertificateReferences(CompleteCertificateRefs aObject)
            : base(aObject)
        {
        }

        public ECompleteCertificateReferences(byte[] aBytes)
            : base(aBytes, new CompleteCertificateRefs())
        {
        }
        public EOtherCertID[] getCertIDs(){
            return wrapArray<EOtherCertID, OtherCertID>(mObject.elements, typeof(EOtherCertID));
    }

        public int getCount(){
            if (mObject.elements!=null)
                return mObject.elements.Length;
            return 0;
        }
    }
}
