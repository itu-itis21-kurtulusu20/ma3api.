using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace tr.gov.tubitak.uekae.esya.api.asn.profile
{
    public class TurkishESigProfile
    {
        public static readonly List<TurkishESigProfile> profiles = new List<TurkishESigProfile>();
	
	    protected static readonly ElektronikImzaKullanimProfilleriRehberiDocInfo docInfo = new  ElektronikImzaKullanimProfilleriRehberiDocInfo();
	
	    public static readonly int [] P2_1_OID = new int [] {2,16,792,1,61,0,1,5070,3,1,1};
	    public static readonly int[] P3_1_OID = new int [] {2,16,792,1,61,0,1,5070,3,2,1};
	    public static readonly int[] P4_1_OID = new int [] {2,16,792,1,61,0,1,5070,3,3,1};

        public static readonly TurkishESigProfile P1_1 = new TurkishESigProfile(null, docInfo, "P1_1");
        public static readonly TurkishESigProfile P2_1 = new TurkishESigProfile(P2_1_OID, docInfo, "P2_1");
        public static readonly TurkishESigProfile P3_1 = new TurkishESigProfile(P3_1_OID, docInfo, "P3_1");
        public static readonly TurkishESigProfile P4_1 = new TurkishESigProfile(P4_1_OID, docInfo, "P4_1");


        protected int [] mOid;
	    protected ProfileDocInfo mDocInfo;
        protected String profileName;

        TurkishESigProfile(int [] aOid, ProfileDocInfo aDocInfo, String aProfileName)
	    {
		    mOid = aOid;
		    mDocInfo = aDocInfo;
            profileName = aProfileName;
            profiles.Add(this);
	    }
	
	    public int [] getOid()
	    {
		    return mOid;
	    }

        public String getProfileName()
        {
            return profileName;
        }

        /**
	     * 
	     * @param aDigestAlgOid
	     * @return
	     * @throws ESYAException when can not find digest of Profile according to given digest algorithm.
	     */
        public byte [] getDigestofProfile(int [] aDigestAlgOid)
	    {
		    return mDocInfo.getDigestOfProfile(aDigestAlgOid);
	    }
	
	    public ProfileDocInfo getProfileDocInfo()
	    {
		    return mDocInfo;
	    }

        public String getURI()
        {
		   return getProfileDocInfo().getURI();
        }

        public bool equals(Object aProfile)
        {
            TurkishESigProfile profile = aProfile as TurkishESigProfile;
            return profile != null && mOid.SequenceEqual(profile.getOid());
        }

        public static TurkishESigProfile getSignatureProfile(int[] aOid)
        {
            foreach (TurkishESigProfile profile in profiles)
            {
                if (profile.getOid() != null && aOid.SequenceEqual(profile.getOid()))
                {
                    return profile;
                }
            }
            return null;
        }

        public static TurkishESigProfile getSignatureProfileFromName(String profileName)
        {
            foreach (TurkishESigProfile profile in profiles)
            {
                if (profileName.Equals(profile.getProfileName()))
                {
                    return profile;
                }
            }
            return null;
        }


        public static TurkishESigProfile getSignatureProfileFromOid(int[] aOid)
        {
            foreach (TurkishESigProfile profile in profiles)
            {
                int[] profileOid = profile.getOid();
                if (profileOid != null && aOid.SequenceEqual(profileOid))
                {
                    return profile;
                }
            }
            return null;
        }
    }
}
