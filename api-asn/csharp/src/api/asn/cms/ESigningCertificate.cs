using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESigningCertificate : BaseASNWrapper<SigningCertificate>
    {
        public ESigningCertificate(byte[] aBytes)
            : base(aBytes, new SigningCertificate())
        {
        }

        public ESigningCertificate(SigningCertificate aObject)
            : base(aObject)
        {
        }
    }
}
