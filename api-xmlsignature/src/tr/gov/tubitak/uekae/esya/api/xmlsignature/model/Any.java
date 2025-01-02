package tr.gov.tubitak.uekae.esya.api.xmlsignature.model;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The <code>AnyType</code> Schema data type has a content model that allows a
 * sequence of arbitrary XML elements that (mixed with text) is of unrestricted
 * length. It also allows for text content only. Additionally, an element of
 * this data type can bear an unrestricted number of arbitrary attributes. It
 * is used throughout the remaining parts of this clause wherever the content
 * of an XML element has been left open.
 *
 * <pre>
 * &lt;xsd:complexType name="AnyType" mixed="true"&gt;
 *   &lt;xsd:sequence minOccurs="0" maxOccurs="unbounded"&gt;
 *     &lt;xsd:any namespace="##any" processContents="lax"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:anyAttribute namespace="##any"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Sep 18, 2009
 */
public abstract class Any extends BaseElement
{

    /**
     * Construct new BaseElement according to context
     * @param aContext where some signature spesific properties reside.
     */
    protected Any(Context aContext)
    {
        super(aContext);
    }

    /**
     * Construct Any from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    protected Any(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    public NodeList getContent()
    {
        return mElement.getChildNodes();
    }

    /**
     * Add String to contents
     *
     * @param aContent that will be added to content/s.
     */
    public void addContent(String aContent)
    {
        mElement.appendChild(getDocument().createTextNode(aContent));
        //addLineBreak();
    }

    /**
     * Add bytes as base64 to contents
     *
     * @param aBytes that will be added to content/s.
     */
    public void addContentBase64(byte[] aBytes)
    {
        XmlUtil.setBase64EncodedText(mElement, aBytes);
        addLineBreak();
    }

    /**
     * Add element to contents
     *
     * @param aElement that will be added to content/s.
     */
    public void addContent(BaseElement aElement)
    {
        mElement.appendChild(aElement.getElement());
        addLineBreak();
    }

    /**
     * Add node to contents
     *
     * @param aNode that will be added to content/s.
     */
    public void addContent(Node aNode)
    {
        mElement.appendChild(getDocument().importNode(aNode, true));
        addLineBreak();
    }

}
