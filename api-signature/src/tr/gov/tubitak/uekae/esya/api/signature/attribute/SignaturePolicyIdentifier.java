package tr.gov.tubitak.uekae.esya.api.signature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.common.OID;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

/**
 * Signature Policy description object. Signature Policies are not embedded in
 * signature. Instead OID, digest is added. There are also optional fields like
 * policyURI and user notice. Note that its API users obligation what to do
 * with user notice. It might be required to display it to the end user if
 * related e-signature laws requires so.
 *
 * @author ayetgin
 */
public class SignaturePolicyIdentifier
{
    public OID policyId;

    private DigestAlg digestAlg;
    private byte[] digestValue;

    // qualifiers
    String policyURI;
    String userNotice;

    public SignaturePolicyIdentifier(OID aPolicyId, DigestAlg aDigestAlg, byte[] aDigestValue, String aPolicyURI)
    {
        this(aPolicyId, aDigestAlg, aDigestValue, aPolicyURI, null);
    }


    public SignaturePolicyIdentifier(OID aPolicyId, DigestAlg aDigestAlg, byte[] aDigestValue, String aPolicyURI, String aUserNotice)
    {
        userNotice = aUserNotice;
        policyId = aPolicyId;
        digestAlg = aDigestAlg;
        digestValue = aDigestValue;
        policyURI = aPolicyURI;
    }

    public OID getPolicyId()
    {
        return policyId;
    }

    public TurkishESigProfile getTurkishESigProfile(){
        return TurkishESigProfile.getSignatureProfileFromOid(getPolicyId().getValue());
    }

    public DigestAlg getDigestAlg()
    {
        return digestAlg;
    }

    public byte[] getDigestValue()
    {
        return digestValue;
    }

    public String getPolicyURI()
    {
        return policyURI;
    }

    public String getUserNotice()
    {
        return userNotice;
    }
}
