package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;


import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.asn.util.AsnIO;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;

/**
 * The content-identifier attribute provides an identifier for the signed content, for use when a reference may be
 * later required to that content; for example, in the content-reference attribute in other signed data sent later. The
 * content-identifier shall be a signed attribute.
 * 
 * The minimal content-identifier attribute should contain a concatenation of user-specific identification
 * information (such as a user name or public keying material identification information), a GeneralizedTime string,
 * and a random number.
 * 
 * (etsi 101733v010801 5.10.2)
 * @author aslihan.kubilay
 *
 */

public class ContentIdentifierAttr
extends AttributeValue
{
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_contentIdentifier;

	protected byte  []  mIdentifier;

	public ContentIdentifierAttr (byte  [] aIdentifier) throws NullParameterException 
	{
		super();
		mIdentifier = aIdentifier;
		if(mIdentifier == null)
			throw new NullParameterException("Identifier must be set");
	}
	/**
   	 * Set identifier
   	 */
	public void setValue () throws CMSSignatureException
	{
		 Asn1OctetString cidentifier = new Asn1OctetString(mIdentifier);
		
		_setValue(cidentifier);
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
	 * Returns Attribute OID of Content Identifier attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	/**
	 * Returns  identifier
	 * @param aAttribute EAttribute
	 * @return byte []
	 * @throws CMSSignatureException
	 */
	public static byte [] toIdentifier(EAttribute aAttribute) throws CMSSignatureException 
	{
		try
		{
			Asn1OctetString identifier = new Asn1OctetString();
			identifier = (Asn1OctetString) AsnIO.derOku(identifier, aAttribute.getValue(0));
			return identifier.value;
		}
		catch(Exception ex)
		{
			throw new CMSSignatureException(ex);
		}
	}
}