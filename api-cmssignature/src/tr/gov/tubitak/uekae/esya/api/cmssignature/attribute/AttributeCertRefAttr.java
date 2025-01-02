package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * <p>The attribute-certificate-references attribute is an unsigned attribute.
 * It references the full set of AA certificates that have been used to validate
 * the attribute certificate. Only a single instance of this attribute shall
 * occur with an electronic signature.
 *
 * <p>This attribute is only used when a user attribute certificate is present 
 * in the electronic signature.
 */

public class AttributeCertRefAttr extends AttributeValue {

	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_ets_attrCertificateRefs;
	
	
	public AttributeCertRefAttr() 
    {    
		super();     
    }
	
	public void setValue() 
	throws CMSSignatureException 
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
	 * Returns AttributeOID of AttributeCertRefAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}

}
