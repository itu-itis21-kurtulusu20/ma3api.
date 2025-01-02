package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import org.w3c.dom.Element;

/**
 * <pre>
 * &lt;xsd:element name="ReferenceInfo" type="ReferenceInfoType"/&gt;
 *
 * &lt;xsd:complexType name="ReferenceInfoType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element ref="ds:DigestMethod"/&gt;
 *     &lt;xsd:element ref="ds:DigestValue"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 *   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Sep 28, 2009
 */
public class ReferenceInfo extends XAdESBaseElement
{
    private DigestMethod mDigestMethod;
    private byte[] mDigestValue;
    private String mURI;

    public ReferenceInfo(Context aContext,
                         DigestMethod aDigestMethod,
                         byte[] aDigestValue,
                         String aURI)
    {
        super(aContext);
        mDigestMethod = aDigestMethod;
        mDigestValue = aDigestValue;
        mURI = aURI;

        setupChildren();
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public ReferenceInfo(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element digestMethodElm = XmlUtil.getNextElement(aElement.getFirstChild());

        String digestMethodAlg = getAttribute(digestMethodElm, ATTR_ALGORITHM);
        mDigestMethod = DigestMethod.resolve(digestMethodAlg);

        Element digestValueElm = XmlUtil.getNextElement(digestMethodElm.getNextSibling());
        mDigestValue = XmlUtil.getBase64DecodedText(digestValueElm);

        mURI = getAttribute(mElement, ATTR_URI);
    }

    private void setupChildren()
    {
        XmlUtil.removeChildren(mElement);
        addLineBreak();

        Element digestMethodElm = insertElement(NS_XMLDSIG, TAG_DIGESTMETHOD);
        digestMethodElm.setAttribute(ATTR_ALGORITHM, mDigestMethod.getUrl());

        Element digestValueElm = insertElement(NS_XMLDSIG, TAG_DIGESTVALUE);
        XmlUtil.setBase64EncodedText(digestValueElm, mDigestValue);

        if (mURI!=null){
            mElement.setAttributeNS(null, ATTR_URI, mURI);
        }
        if (mId!=null){
            mElement.setAttributeNS(null, ATTR_ID, mId);
        }
    }

    public DigestMethod getDigestMethod()
    {
        return mDigestMethod;
    }

    public void setDigestMethod(DigestMethod aDigestMethod)
    {
        mDigestMethod = aDigestMethod;
        setupChildren();
    }

    public byte[] getDigestValue()
    {
        return mDigestValue;
    }

    public void setDigestValue(byte[] aDigestValue)
    {
        mDigestValue = aDigestValue;
        setupChildren();
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String aURI)
    {
        mURI = aURI;
        setupChildren();
    }

    public String getLocalName()
    {
        return Constants.TAGX_REFERENCEINFO;  
    }
}
