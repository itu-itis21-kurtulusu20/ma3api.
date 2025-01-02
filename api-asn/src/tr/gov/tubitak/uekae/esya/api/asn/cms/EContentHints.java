package tr.gov.tubitak.uekae.esya.api.asn.cms;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.cms.ContentHints;

import com.objsys.asn1j.runtime.Asn1ObjectIdentifier;
import com.objsys.asn1j.runtime.Asn1UTF8String;

public class EContentHints extends BaseASNWrapper<ContentHints>
{
	public EContentHints(byte[] aBytes)	throws ESYAException
	{
		super(aBytes, new ContentHints());
	}
	/**
	 * 
	 * @param aContentDescription defines content type. mandatory
	 * @param aContentType defines explanation of hint. optional
	 */
	public EContentHints(String aContentDescription, Asn1ObjectIdentifier aContentType)
	{
		super(new ContentHints(new Asn1UTF8String(aContentDescription), aContentType));
	}
	/**
	 * Returns Content Description of ContentHints attribute
	 * @return
	 */
	public String getContentDescription()
	{
		return mObject.contentDescription.value;
	}
	/**
	 * Returns Content Type of ContentHints attribute
	 * @return
	 */
	public Asn1ObjectIdentifier getContentType()
	{
		return mObject.contentType;
	}
}
