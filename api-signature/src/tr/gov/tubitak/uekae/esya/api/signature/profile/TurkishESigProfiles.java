package tr.gov.tubitak.uekae.esya.api.signature.profile;

import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.common.OID;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ayetgin
 */
public class TurkishESigProfiles
{
    public static ProfileDocInfo PROFILE_DOC_V1 = new EImzaProfilleriDocInfo();

    public static SignatureProfile SIG_POLICY_ID_P2v1 = new SignatureProfile(new OID(TurkishESigProfile.P2_1_OID), PROFILE_DOC_V1);
    public static SignatureProfile SIG_POLICY_ID_P3v1 = new SignatureProfile(new OID(TurkishESigProfile.P3_1_OID), PROFILE_DOC_V1);
    public static SignatureProfile SIG_POLICY_ID_P4v1 = new SignatureProfile(new OID(TurkishESigProfile.P4_1_OID), PROFILE_DOC_V1);

    private static Map<OID, SignatureProfile> allProfiles = new HashMap<OID, SignatureProfile>();

    static {
        register(SIG_POLICY_ID_P2v1);
        register(SIG_POLICY_ID_P3v1);
        register(SIG_POLICY_ID_P4v1);
    }

    private static void register(SignatureProfile signatureProfile)
    {
        allProfiles.put(signatureProfile.getPolicyId(), signatureProfile);
    }

    public static SignatureProfile resolve(OID oid)
    {
        return allProfiles.get(oid);
    }

}
