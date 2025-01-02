using tr.gov.tubitak.uekae.esya.asn.cms;

namespace tr.gov.tubitak.uekae.esya.api.asn.cms
{
    public class ESigningCertificateV2 : BaseASNWrapper<SigningCertificateV2>
    {
        public ESigningCertificateV2(byte[] aBytes)
            : base(aBytes, new SigningCertificateV2())
        {
        }

        public ESigningCertificateV2(SigningCertificateV2 aObject)
            : base(aObject)
        {
        }

        public byte[] getFirstHash()
        {
            return mObject.certs.elements[0].certHash.mValue;
        }
    }
}
