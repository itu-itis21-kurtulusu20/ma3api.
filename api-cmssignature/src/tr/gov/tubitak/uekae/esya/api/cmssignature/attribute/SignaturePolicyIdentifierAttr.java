package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESigPolicyQualifierInfo;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignaturePolicy;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignaturePolicyId;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.asn.cms.DisplayText;
import tr.gov.tubitak.uekae.esya.asn.cms.SPUserNotice;
import tr.gov.tubitak.uekae.esya.asn.cms._etsi101733Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A signature policy defines the rules for creation and validation of
 * an electronic signature, and is included as a signed attribute with every Explicit Policy-based Electronic Signature. The
 * signature-policy-identifier shall be a signed attribute.
 * (etsi 101733v010801 5.8.1)
 * @author aslihan.kubilay
 *
 */

public class SignaturePolicyIdentifierAttr
extends AttributeValue
{
	protected static Logger logger = LoggerFactory.getLogger(SignaturePolicyIdentifierAttr.class);
	private DigestAlg mDigestAlg = DigestAlg.SHA256;
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_sigPolicyId;
	
	private byte [] mHash;
	private int [] mPolicyOid;

    private String spURI;
    private String userNotice;

    /**
	 * Create SignaturePolicyIdentifierAttr with given parameters
	  * @param aProfile Turkish e-signature profile
	  */
	public SignaturePolicyIdentifierAttr (TurkishESigProfile aProfile) throws ESYAException
	{
		mHash = aProfile.getDigestofProfile(mDigestAlg.getOID());
		mPolicyOid = aProfile.getOid();
		spURI = aProfile.getURI();
	}

	/**
	 * Create SignaturePolicyIdentifierAttr with given parameters
	  * @param aProfile Turkish e-signature profile
	  * @param aDigestAlg Digest algorithm 
	  */
	public SignaturePolicyIdentifierAttr (TurkishESigProfile aProfile, DigestAlg aDigestAlg) throws ESYAException
	{
		mDigestAlg = aDigestAlg;
		mPolicyOid = aProfile.getOid(); 
		mHash = aProfile.getDigestofProfile(mDigestAlg.getOID());
		spURI = aProfile.getURI();
	}

	/**
	 * Create SignaturePolicyIdentifierAttr with given parameters
	  * @param hashOfPolicyDoc hash of policy document
	  * @param aDigestAlg Digest algorithm 
	  * @param aOid  Oid of policy
	  */
    public SignaturePolicyIdentifierAttr (byte [] hashOfPolicyDoc, DigestAlg aDigestAlg, int [] aOid,String aSpURI)
    {
        mDigestAlg = aDigestAlg;
        mPolicyOid = aOid;
        mHash = hashOfPolicyDoc;
		spURI = aSpURI;
    }

    /**
	 * Create SignaturePolicyIdentifierAttr with given parameters
	  * @param hashOfPolicyDoc hash of policy document
	  * @param aDigestAlg Digest algorithm 
	  * @param aOid  Oid of policy
	  * @param spURI
	  * @param userNotice
	  */
    public SignaturePolicyIdentifierAttr (byte [] hashOfPolicyDoc, DigestAlg aDigestAlg, int [] aOid, String spURI, String userNotice)
    {
        mDigestAlg = aDigestAlg;
        mPolicyOid = aOid;
        mHash = hashOfPolicyDoc;
        this.spURI = spURI;
        this.userNotice = userNotice;
    }

	public String getSpURI() {
		return spURI;
	}

	protected void setSpURI(String spURI) {
		this.spURI = spURI;
	}

	/**
   	 * Set policy id,hash of policy,digest algorithm
   	 */
	public void setValue() 
			throws CMSSignatureException
	{

		//check if policy file is defined using asn1
		/*If the signature policy is defined using ASN.1, then the hash is calculated on the value without the outer type and length
		 *fields, and the hashing algorithm shall be as specified in the field sigPolicyHash.
		 * If the signature policy is defined using another structure, the type of structure and the hashing algorithm shall be either
		 * specified as part of the signature policy, or indicated using a signature policy qualifier.
		 */
//		byte[] policyASN = _asnKontrol(policyDegerBA);
//		if(policyASN != null)
//			policyDegerBA = policyASN;
//		Object digestAlgO = mAttParams.get(AllEParameters.P_POLICY_DIGEST_ALGORITHM);
//		if (digestAlgO != null)
//		{
//			try
//			{
//				mDigestAlg = (DigestAlg) digestAlgO;
//			} 
//			catch (ClassCastException aEx)
//			{
//				throw new CMSSignatureException("P_POLICY_DIGEST_ALGORITHM parametresi DigestAlg tipinde değil",aEx);
//			}
//		}
		
		if(mHash == null)
			throw new CMSSignatureException("Özet değeri NULL olamaz");
		

        ESignaturePolicyId pid = new ESignaturePolicyId(mPolicyOid, mDigestAlg.getOID(), mHash);
        ESignaturePolicy policy = new ESignaturePolicy(pid);

        if (spURI != null){
            Asn1IA5String uri= new Asn1IA5String(spURI);
            ESigPolicyQualifierInfo qualifier = new ESigPolicyQualifierInfo(_etsi101733Values.id_spq_ets_uri, uri);

            policy.getSignaturePolicyId().addPolicyQualifier(qualifier);
        }

        if (userNotice != null){
            DisplayText dt = new DisplayText();
            dt.set_utf8String(new Asn1UTF8String(userNotice));
            SPUserNotice notice = new SPUserNotice(null, dt);
            ESigPolicyQualifierInfo qualifier = new ESigPolicyQualifierInfo(_etsi101733Values.id_spq_ets_unotice, notice);

            policy.getSignaturePolicyId().addPolicyQualifier(qualifier);
        }

		_setValue(policy.getObject());
    }


	/*If the signature policy is defined using ASN.1, then the hash is calculated on the value without the outer type and length
	 *fields, and the hashing algorithm shall be as specified in the field sigPolicyHash.
	 * If the signature policy is defined using another structure, the type of structure and the hashing algorithm shall be either
	 * specified as part of the signature policy, or indicated using a signature policy qualifier.
	 */
	private byte[] _asnKontrol(byte[] aPolicy)
	{
		try
		{
			Asn1DerDecodeBuffer decBuf = new Asn1DerDecodeBuffer(aPolicy);
			tr.gov.tubitak.uekae.esya.asn.signaturepolicies.SignaturePolicy sp = new tr.gov.tubitak.uekae.esya.asn.signaturepolicies.SignaturePolicy();
			sp.decode(decBuf);
			Asn1DerEncodeBuffer encBuf = new Asn1DerEncodeBuffer();
			sp.signPolicyInfo.encode(encBuf,false);
			return encBuf.getMsgCopy();
		}
		catch(Exception e)
		{
			logger.warn("Warning in SignaturePolicyIdentifierAttr", e);
			return null;
		}
	}

	 /**
		 * Checks whether attribute is signed or not.
		 * @return True 
		 */

	public boolean isSigned() 
	{
		return true;
	}

	 /**
	 * Returns Attribute OID of content time stamp attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}

	/**
	 * Returns  policy ID of attribute
	 * @param aAttribute EAttribute
	 * @return 
	 * @throws ESYAException
	 */
	public static ESignaturePolicyId toSignaturePolicyId(EAttribute aAttribute) 
			throws ESYAException
	{
		return new ESignaturePolicyId(aAttribute.getValue(0));
	}


}