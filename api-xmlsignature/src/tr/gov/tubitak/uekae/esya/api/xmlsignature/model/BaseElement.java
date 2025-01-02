package tr.gov.tubitak.uekae.esya.api.xmlsignature.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.math.BigInteger;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_NAMESPACESPEC;

/**
 * Base class for java classes that mapps to XML digital signature spec.
 *
 * @author ahmety
 * date: Jun 10, 2009
 */
public abstract class BaseElement
{

    protected Element mElement;
    //protected Document mDocument;
    protected Context mContext;

    protected String mId;

    

    /**
     * Construct new BaseElement according to context
     * @param aContext where some signature spesific properties reside.
     */
    public BaseElement(Context aContext)
    {
        //mDocument = aContext.getDocument();
        mContext = aContext;

        mElement = createElement(getNamespace(), getLocalName());
    }

    /**
     *  Construct BaseElement from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public BaseElement(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        if (aElement==null)
            throw new XMLSignatureException("errors.nullElement", getLocalName());

        //this.mDocument = aElement.getOwnerDocument();
        this.mElement  = aElement;
        this.mContext  = aContext;


        mId = getAttribute(aElement, Constants.ATTR_ID);
        if (mId!=null && !mId.equals("")){
            mContext.getIdRegistry().put(mId, aElement);
            mContext.getIdGenerator().update(mId);
        }

        checkNamespace();
    }

    /**
     * Get requested attribute value from given element as String
     * @param aElement element containing the attribute
     * @param aAttributeName name of the attribute
     * @return  null or attribute value
     */
    protected String getAttribute(Element aElement,String aAttributeName){
        Node attrNode = aElement.getAttributeNodeNS(null, aAttributeName);
        if (attrNode!=null){
            return attrNode.getNodeValue();
        }
        return null;
    }

    protected Element createElement(String aNamespace, String aName){
        String prefix = mContext.getConfig().getNsPrefixMap().getPrefix(aNamespace);
        Element e = null;
        if (prefix==null){
            e = getDocument().createElement(aName);
        }
        else {
        	e = getDocument().createElementNS(aNamespace, aName);
        	e.setPrefix(prefix);
        }
        //e.setPrefix(mContext.getConfig().getNsPrefixMap().getPrefix(aNamespace));
        e.setAttributeNS(NS_NAMESPACESPEC, prefix==null ? "xmlns" : "xmlns:"+prefix.intern(), aNamespace);

        return e;
    }

    protected Element insertElement(String aNamespace, String aName){
        Element e = createElement(aNamespace, aName);
        mElement.appendChild(e);
        addLineBreak();
        return e;
    }



    protected Element insertTextElement(String aNamespace, String aTagname, String aText)
    {
        Element element = insertElement(aNamespace, aTagname);
        element.setTextContent(aText);
        return element;
    }

    protected String getChildText(String aNamespace, String aTagname)
    {
        Element e = selectChildElement(aNamespace, aTagname);
        if (e!=null){
            return XmlUtil.getText(e);
        }
        return null;
    }

    protected Element insertBase64EncodedElement(String aNamespace, String aTagname, byte[] aValue)
    {
        return insertTextElement(aNamespace, aTagname, Base64.encode(aValue));
    }

    protected Element selectChildElement(String aNamespace, String aTagname){
        return XmlUtil.selectFirstElement(mElement.getFirstChild(), aNamespace, aTagname);
    }

    protected Element[] selectChildren(String aNamespace, String aTagname){
        return XmlUtil.selectNodes(mElement.getFirstChild(), aNamespace, aTagname);
    }

    /**
     * Check if the namespace and localname is valid.
     * @throws XMLSignatureException if wrong xml element given to constructor
     */
    protected void checkNamespace() throws XMLSignatureException
    {
        String localnameSHOULDBE = getLocalName();
        String namespaceSHOULDBE = getNamespace();

        String localnameIS = mElement.getLocalName();
        String namespaceIS = mElement.getNamespaceURI();
        if ((!namespaceIS.startsWith(namespaceSHOULDBE)) || !localnameSHOULDBE.equals(localnameIS))
        {
            throw new XMLSignatureException("xml.WrongElement",
                                   namespaceIS + ":" + localnameIS,
                                   namespaceSHOULDBE + ":" + localnameSHOULDBE);
        }
    }


    public Element getElement() { return mElement; }
    //public void setElement(Element aElement) { mElement = aElement; }

    public Document getDocument() { return mElement==null ? mContext.getDocument() : mElement.getOwnerDocument(); }
    //public void setDocument(Document aDocument) { mDocument = aDocument; }

    public Context getContext() { return mContext; }

    public String getId() { return mId; }

    /**
     * Set Id of the element.
     * @param aId value to set as "Id" attribute. If param is null existing
     *      attribute will be removed.
     */
    public void setId(String aId)
    {
        if (aId==null){
            mElement.removeAttributeNS(null, Constants.ATTR_ID);
        }
        else {
            mElement.setAttributeNS(null, Constants.ATTR_ID, aId);
            mId = aId;
        }
        mContext.getIdRegistry().put(aId, mElement);
    }

    // utility methods

    //Add line break text node to xml structure for pretty print
    protected void addLineBreak(){
        mElement.appendChild(getDocument().createTextNode("\n"));
    }

    //Add line break text node to given xml element for pretty print
    protected void addLineBreak(Element aElement){
        aElement.appendChild(getDocument().createTextNode("\n"));
    }

    protected void generateAndSetId(String aType){
        String id = mContext.getIdGenerator().uret(aType);
        setId(id);
        mContext.getIdRegistry().put(id, mElement);
    }

    protected void addBigIntegerElement(BigInteger aValue, String aNamespace, String aTagName)
    {
        if (aValue!=null)
        {
            Element e = getDocument().createElementNS(aNamespace, aTagName);
            e.setPrefix(mContext.getConfig().getNsPrefixMap().getPrefix(aNamespace));

            String val = Base64.encode(aValue.toByteArray());
            Text text = getDocument().createTextNode(val);
            e.appendChild(text);
            mElement.appendChild(e);
        }
    }

    // get base64 encoded content from child element as BigInteger
    protected BigInteger getBigIntegerFromElement(String aNamespace, String aLocalname )
            throws XMLSignatureException
    {
        Element text = selectChildElement(aNamespace, aLocalname );
        return new BigInteger(1, XmlUtil.getBase64DecodedText(text));
    }

    public String getNamespace(){
        return Constants.NS_XMLDSIG;
    }

    public abstract String getLocalName();


}
