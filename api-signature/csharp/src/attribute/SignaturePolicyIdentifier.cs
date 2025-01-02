using System;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.signature.attribute
{
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

        private readonly DigestAlg digestAlg;
        private readonly byte[] digestValue;

        // qualifiers
        readonly String policyURI;
        readonly String userNotice;

        public SignaturePolicyIdentifier(OID aPolicyId, DigestAlg aDigestAlg, byte[] aDigestValue, String aPolicyURI)
        {
            userNotice = null;
            policyId = aPolicyId;
            digestAlg = aDigestAlg;
            digestValue = aDigestValue;
            policyURI = aPolicyURI;
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

        public TurkishESigProfile getTurkishESigProfile()
        {
            return TurkishESigProfile.getSignatureProfile(getPolicyId().getValue());
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
}
