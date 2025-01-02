package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.asn.cms.EContentReference;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentReference;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1OctetString;

/**
 * The content-reference attribute is a link from one SignedData to another. It may be used to link a reply to the
 * original message to which it refers, or to incorporate by reference one SignedData into another. The content reference
 * attribute shall be a signed attribute.
 * (etsi 101733v010801 5.10.1)
 * @author aslihan.kubilay
 *
 */

public class ContentRefAttr
extends AttributeValue
{
	//	The contentReference attribute is a link from one SignedData to
	//	another. It may be used to link a reply to the original message to
	//	which it refers, or to incorporate by reference one SignedData into
	//	another. The first SignedData MUST include a contentIdentifier signed
	//   attribute, which SHOULD be constructed as specified in section 2.7.
	//   The second SignedData links to the first by including a
	//   ContentReference signed attribute containing the content type,
	//   content identifier, and signature value from the first SignedData
	//   (rfc 2634 2.11)


	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_aa_contentReference;

	protected Asn1ObjectIdentifier mContentType;
	protected byte [] mSignedContentIdentifier;
	protected byte [] mOriginatorSignatureValue;

	public ContentRefAttr (Asn1ObjectIdentifier aContentType, byte [] aSignedContentIdentifier, byte  [] aOriginatorSignatureValue) 
	throws NullParameterException
	{
		super();
		mContentType = aContentType;
		mSignedContentIdentifier = aSignedContentIdentifier;
		mOriginatorSignatureValue = aOriginatorSignatureValue;
		if(aContentType == null || aSignedContentIdentifier == null || aOriginatorSignatureValue == null)
			throw new NullParameterException("One of parameters is not set");
	}


	public void setValue () throws CMSSignatureException
	{
		Asn1OctetString identifier = new Asn1OctetString(mSignedContentIdentifier);

		Asn1OctetString signature = new Asn1OctetString(mOriginatorSignatureValue);

		ContentReference cr = new ContentReference(mContentType, identifier, signature);
		
		_setValue(cr);
	}
	 /**
	 * Checks whether attribute is signed or not.
	 * @return true 
	 */ 
	public boolean isSigned() 
	{
		return true;
	}
	/**
	 * Returns AttributeOID of ContentRefAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}
	/**
	 * Returns  content reference of ArchiveTimeStampAttr attribute
	 * @param aAttribute EAttribute
	 * @return EContentReference
	 * @throws ESYAException
	 */
	public static EContentReference toContentReference(EAttribute aAttribute) throws ESYAException
	{
		return new EContentReference(aAttribute.getValue(0));
	}
}