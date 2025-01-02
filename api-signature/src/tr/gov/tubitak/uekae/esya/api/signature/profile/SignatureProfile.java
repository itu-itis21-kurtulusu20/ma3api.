package tr.gov.tubitak.uekae.esya.api.signature.profile;

import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.SignaturePolicyIdentifier;

/**
 * @author ayetgin
 */
public class SignatureProfile extends SignaturePolicyIdentifier
{
    ProfileDocInfo profileDocInfo;

    SignatureProfile(OID aPolicyId, ProfileDocInfo profileInfo)
    {
        super(aPolicyId, DigestAlg.SHA256, profileInfo.getDigestOfProfile(DigestAlg.SHA256), profileInfo.getURI());
        profileDocInfo = profileInfo;

    }

    public ProfileDocInfo getProfileDocInfo()
    {
        return profileDocInfo;
    }

}
