package tr.gov.tubitak.uekae.esya.api.cmssignature.attribute;


import java.io.IOException;

import tr.gov.tubitak.uekae.esya.api.asn.cms.EAttribute;
import tr.gov.tubitak.uekae.esya.api.cmssignature.CMSSignatureException;
import tr.gov.tubitak.uekae.esya.api.cmssignature.NullParameterException;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import com.objsys.asn1j.runtime.Asn1DerDecodeBuffer;
import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;

/**
 * The content-type attribute indicates the type of the signed content.(ts_101733v010801 5.7.1)
 * 
 * The content-type attribute MUST be a signed attribute (RFC 3852 11.1)
 * 
 * Even though the syntax is defined as a SET OF AttributeValue, a content-type attribute
 * MUST have a single attribute value; zero or multiple instances of AttributeValue are 
 * not permitted.(RFC 3852 11.1)
 * 
 * The SignedAttributes in a signerInfo MUST NOT include multiple instances of the 
 * content-type attribute.
 * 
 * @author aslihanu
 *
 */

public class ContentTypeAttr extends AttributeValue
{
	public static final Asn1ObjectIdentifier OID = AttributeOIDs.id_contentType;

     public ContentTypeAttr ()
     {
          super();
     }


     public ContentTypeAttr(Asn1ObjectIdentifier aContentType) throws CMSSignatureException
     {
          super();
          _setValue(aContentType);
     }


     public void setValue() throws CMSSignatureException 
     {
		Object contentType = mAttParams.get(AllEParameters.P_CONTENT_TYPE);

		if (contentType == null) 
		{
			throw new NullParameterException("P_CONTENT_TYPE parameter is not set");
		}

		Asn1ObjectIdentifier contentTypeOID = null;
		try 
		{
			contentTypeOID = (Asn1ObjectIdentifier) contentType;
		} 
		catch (ClassCastException ex) 
		{
			throw new CMSSignatureException("P_CONTENT_TYPE parameter is not of type Asn1ObjectIdentifier",ex);
		}
		
		setValue(contentTypeOID);
	}

	public void setValue(Asn1ObjectIdentifier contentTypeOID ) throws CMSSignatureException
	{
		_setValue(contentTypeOID);
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
	 * Returns AttributeOID of ContentTypeAttr attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getAttributeOID() 
	{
		return OID;
	}

	public static Asn1ObjectIdentifier toContentType(EAttribute aAttribute) throws ESYAException 
	{
		try 
		{
			Asn1DerDecodeBuffer buff = new Asn1DerDecodeBuffer(aAttribute.getValue(0));
			Asn1ObjectIdentifier objIden = new Asn1ObjectIdentifier();
			objIden.decode(buff);
			return objIden;
		} 
		catch (Asn1Exception e) 
		{
			throw new ESYAException("Asn1 decode error", e);
		} 
		catch (IOException e)
		{
			throw new ESYAException("IOException", e);
		}
	}


	
}