using System;
using System.Collections.Generic;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature
{

	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

	/// <summary>
	/// @author ahmety
	/// date: May 4, 2009
	/// </summary>
	public class TransformType
	{

	    public static readonly TransformType BASE64 = new TransformType(tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG + "base64");
		public static readonly TransformType ENVELOPED = new TransformType( tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XMLDSIG + "enveloped-signature");
	    public static readonly TransformType XPATH = new TransformType("http://www.w3.org/TR/1999/REC-xpath-19991116");
	    public static readonly TransformType XSLT = new TransformType("http://www.w3.org/TR/1999/REC-xslt-19991116");

        public static IEnumerable<TransformType> Values
        {
            get
            {
                yield return BASE64;
                yield return ENVELOPED;
                yield return XPATH;
                yield return XSLT;
            }
        }

	    public string Url
	    {
	        get { return mUrl; }
	    }

	    private readonly String mUrl;

		TransformType(String url)
    	{
    		this.mUrl = url;
    	}

	}
}