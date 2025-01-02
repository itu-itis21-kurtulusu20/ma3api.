package tr.gov.tubitak.uekae.esya.api.asn.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TurkishESigProfile {

	protected static Logger logger = LoggerFactory.getLogger(TurkishESigProfile.class);

	public static final List<TurkishESigProfile> profiles = new ArrayList<TurkishESigProfile>();

	protected static ElektronikImzaKullanimProfilleriRehberiDocInfo docInfo = new  ElektronikImzaKullanimProfilleriRehberiDocInfo();

	public static final int [] P2_1_OID = new int [] {2,16,792,1,61,0,1,5070,3,1,1};
	public static final int [] P3_1_OID = new int [] {2,16,792,1,61,0,1,5070,3,2,1};
	public static final int [] P4_1_OID = new int [] {2,16,792,1,61,0,1,5070,3,3,1};
	
	public static final TurkishESigProfile P1_1 = new TurkishESigProfile(null, docInfo, "P1_1");
	public static final TurkishESigProfile P2_1 = new TurkishESigProfile(P2_1_OID, docInfo, "P2_1");
	public static final TurkishESigProfile P3_1 = new TurkishESigProfile(P3_1_OID, docInfo, "P3_1");
	public static final TurkishESigProfile P4_1 = new TurkishESigProfile(P4_1_OID, docInfo, "P4_1");


	protected int [] mOid;
	protected ProfileDocInfo mDocInfo;
	protected String profileName;

	TurkishESigProfile(int [] aOid, ProfileDocInfo aDocInfo, String aProfileName) {
		mOid = aOid;
		mDocInfo = aDocInfo;
		profileName = aProfileName;
		profiles.add(this);
	}
	
	public int [] getOid()
	{
		return mOid;
	}

	public String getProfileName(){ return profileName; }
	/**
	 * 
	 * @param aDigestAlgOid
	 * @return
	 * @throws ESYAException when can not find digest of Profile according to given digest algorithm.
	 */
	public byte [] getDigestofProfile(int [] aDigestAlgOid) throws ESYAException {
		return mDocInfo.getDigestOfProfile(aDigestAlgOid);
	}
	
	public ProfileDocInfo getProfileDocInfo()
	{
		return mDocInfo;
	}

	public boolean equals(Object aProfile) {
		try {
			if (aProfile == null)
				return false;
			if (this.getClass() != aProfile.getClass())
				return false;
			TurkishESigProfile profile = (TurkishESigProfile) aProfile;
			if (Arrays.equals(mOid, profile.getOid()))
				return true;
			return false;
		} catch (Exception ex) {
			logger.warn("Warning in TurkishESigProfile", ex);
			return false;
		}
	}

	public int hashCode(){
		return super.hashCode();
	}

	public String getURI() throws ESYAException {
		return getProfileDocInfo().getURI();
	}

	public static TurkishESigProfile getSignatureProfileFromOid(int [] aOid) {
		for (TurkishESigProfile profile : profiles) {
			if(Arrays.equals(aOid, profile.getOid())) {
				return profile;
			}
		}
		return null;
	}

	public static TurkishESigProfile getSignatureProfileFromName(String profileName) {
		for (TurkishESigProfile profile : profiles) {
			if(profileName.equals(profile.getProfileName())){
				return profile;
			}
		}
		return null;
	}
}
