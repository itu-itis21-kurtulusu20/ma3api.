package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * <p>The attribute-revocation-references attribute is an unsigned attribute.
 * Only a single instance of this attribute shall occur with an electronic
 * signature. It references the full set of the ACRL or OCSP responses that
 * have been used in the validation of the attribute certificate. This attribute
 * can be used to illustrate that the verifier has taken due diligence of the
 * available revocation information
 *
 * <p>This attribute is only used when a user attribute certificate is present
 * in the electronic signature and when that attribute certificate can be
 * revoked.
 */

public class AttributeRevRefAttr extends AttributeValue
{

	 public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_attrRevocationRefs;

     public AttributeRevRefAttr ()
     {
          super();
     }

     public void setValue () throws CMSSignatureException
     {
         //TODO
     }
	 /**
	 * Checks whether attribute is signed or not.
	 * @return false 
	 */ 
	public boolean isSigned() 
	{
		return false;
	}

	/**
	 * Returns AttributeOID of AttributeRevRefAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
}