using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common;

namespace tr.gov.tubitak.uekae.esya.api.signature.profile
{
    public class TurkishESigProfiles
    {
        private static OID P2_v1_OID = new OID(new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 1, 1 });
        private static OID P3_v1_OID = new OID(new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 2, 1 });
        private static OID P4_v1_OID = new OID(new int[] { 2, 16, 792, 1, 61, 0, 1, 5070, 3, 3, 1 });

        private static ProfileDocInfo profileDocV1 = new EImzaProfilleriDocInfo();

        public static SignatureProfile SIG_POLICY_ID_P2v1 = new SignatureProfile(P2_v1_OID, profileDocV1);
        public static SignatureProfile SIG_POLICY_ID_P3v1 = new SignatureProfile(P3_v1_OID, profileDocV1);
        public static SignatureProfile SIG_POLICY_ID_P4v1 = new SignatureProfile(P4_v1_OID, profileDocV1);

        private static Dictionary<OID, SignatureProfile> allProfiles = new Dictionary<OID, SignatureProfile>();

        static TurkishESigProfiles()
        {
            register(SIG_POLICY_ID_P2v1);
            register(SIG_POLICY_ID_P3v1);
            register(SIG_POLICY_ID_P4v1);
        }

        private static void register(SignatureProfile signatureProfile)
        {
            allProfiles[signatureProfile.getPolicyId()] = signatureProfile;
        }

        public static SignatureProfile resolve(OID oid)
        {
            SignatureProfile SigProfile = null;
            allProfiles.TryGetValue(oid, out SigProfile);
            return SigProfile;
        }

    }
}
