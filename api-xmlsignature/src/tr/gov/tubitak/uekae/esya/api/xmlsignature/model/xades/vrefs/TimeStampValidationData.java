package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_NAMESPACESPEC;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XADES_1_3_2;

/**
 * This element is specified to serve as an optional  container for validation data 
 * required for carrying a full verification of time-stamp tokens embedded within any 
 * of the different time-stamp containers defined in the present document.
 * 
 *  <p>Below follows the schema definition for this element.
 *  
 * <pre>
&lt;xsd:element name="TimeStampValidationData" type="ValidationDataType"/&gt;

 &lt;xsd:complexType name="ValidationDataType"&gt;
 &lt;xsd:sequence&gt;
 &lt;xsd:element ref="xades:CertificateValues" minOccurs="0"/&gt;
 &lt;xsd:element ref="xades:RevocationValues" minOccurs="0"/&gt;
 &lt;/xsd:sequence&gt;
 &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/&gt;
 &lt;/xsd:complexType&gt;
 *</pre> 
 * 
 * 
 * @author ayetgin
 */
public class TimeStampValidationData extends XAdESBaseElement implements UnsignedSignaturePropertyElement
{
    private CertificateValues mCertificateValues;
    private RevocationValues mRevocationValues;

    private String mURI;

    public TimeStampValidationData(Context aContext, 
    							   CertificateValues aCertificateValues,
    							   RevocationValues aRevocationValues)
    {
        super(aContext);

        String xmlnsXAdESPrefix = mContext.getConfig().getNsPrefixMap().getPrefix(getNamespace());
        mElement.setAttributeNS(NS_NAMESPACESPEC, "xmlns:"+xmlnsXAdESPrefix.intern(), getNamespace());

        mCertificateValues = aCertificateValues;
        mRevocationValues = aRevocationValues;
        addLineBreak();
        mElement.appendChild(aCertificateValues.getElement());
        addLineBreak();
        mElement.appendChild(aRevocationValues.getElement());
        addLineBreak();
    }

    public TimeStampValidationData(Element aElement, Context aContext) throws XMLSignatureException
    {
        super(aElement, aContext);
        
        mURI = getAttribute(aElement, Constants.ATTR_URI);
        
        Element certElement = selectChildElement(NS_XADES_1_3_2, Constants.TAGX_CERTIFICATEVALUES);
        Element revElement = selectChildElement(NS_XADES_1_3_2, Constants.TAGX_REVOCATIONVALUES);
        
        if (certElement!=null)
        	mCertificateValues = new CertificateValues(certElement, aContext);
        if (revElement!=null)
        	mRevocationValues = new RevocationValues(revElement, aContext);

    }

    public CertificateValues getCertificateValues() {
		return mCertificateValues;
	}

	public RevocationValues getRevocationValues() {
		return mRevocationValues;
	}

	public void setURI(String aURI) {
		mURI = aURI;
		mElement.setAttributeNS(null, Constants.ATTR_URI, aURI);
	}

	public String getURI() {
		return mURI;
	}

	@Override
    public String getLocalName()
    {
        return Constants.TAGX_TIMESTAMPVALIDATIONDATA;  
    }

    @Override
    public String getNamespace()
    {
        return Constants.NS_XADES_1_4_1;
    }
}
