package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

/**
 * @author ahmety
 * date: May 4, 2009
 */
public enum TransformType
{

    BASE64 (Constants.NS_XMLDSIG + "base64"),

    ENVELOPED (Constants.NS_XMLDSIG + "enveloped-signature"),

    XPATH ("http://www.w3.org/TR/1999/REC-xpath-19991116"),

    XSLT ("http://www.w3.org/TR/1999/REC-xslt-19991116");


    private String mUrl;

    TransformType(String url)
    {
        this.mUrl = url;
    }

    public String getUrl()
    {
        return mUrl;
    }
}
