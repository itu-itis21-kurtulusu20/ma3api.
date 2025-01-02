using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;
using tr.gov.tubitak.uekae.esya.api.signature.attribute;

namespace tr.gov.tubitak.uekae.esya.api.signature.profile
{
    public class SignatureProfile : SignaturePolicyIdentifier
    {
        readonly ProfileDocInfo profileDocInfo;

        internal SignatureProfile(OID aPolicyId, ProfileDocInfo profileInfo)
            : base(aPolicyId, DigestAlg.SHA256, profileInfo.getDigestOfProfile(DigestAlg.SHA256), profileInfo.getURI())
        {
            profileDocInfo = profileInfo;
        }

        public ProfileDocInfo getProfileDocInfo()
        {
            return profileDocInfo;
        }

    }
}
