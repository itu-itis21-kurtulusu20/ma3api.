package tr.gov.tubitak.uekae.esya.api.xmlsignature.model;

import org.w3c.dom.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.TransformType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

import java.util.Map;

/**
 * <p>Each Transform consists of an Algorithm attribute and content parameters,
 * if any, appropriate for the given algorithm. The Algorithm  attribute value
 * specifies the name of the algorithm to be performed, and the Transform
 * content provides additional data to govern the algorithm's processing of the
 * transform input.
 * <p>
 * <p>Each <code>Transform</code> consists of an <code>Algorithm</code>
 * attribute and content parameters, if any, appropriate for the given
 * algorithm. The <code>Algorithm</code> attribute value specifies the name of
 * the algorithm to be performed, and the <code>Transform</code> content
 * provides additional data to govern the algorithm's processing of the
 * transform input.
 * <p>
 * <p>Examples of transforms include but are not limited to base64 decoding
 * [MIME], canonicalization [XML-C14N], XPath filtering [XPath], and XSLT
 * [XSLT]. The generic definition of the Transform element also allows
 * application-specific transform algorithms.</p>
 * <p>
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <p>
 * <pre>
 * &lt;complexType name="TransformType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice maxOccurs="unbounded" minOccurs="0"&gt;
 *         &lt;any processContents='lax' namespace='##other'/&gt;
 *         &lt;element name="XPath" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/choice&gt;
 *       &lt;attribute name="Algorithm" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * @author ahmety
 *         date: Jun 12, 2009
 */
public class Transform extends BaseElement
{

    private String mAlgorithm;

    /*
        first element xpath expression if any
        second element params xml element
     */
    private Object[] mParameters;

    // used for xpath
    private Node mParameterNode;
    private boolean mXPath;

    /**
     * Transform constructor for given algorithm
     * @param aContext   of the signature containing transform
     * @param aAlgorithm transform algorithm
     * @throws XMLSignatureException if there is a problem about algorithm
     */
    public Transform(Context aContext, String aAlgorithm) throws XMLSignatureException
    {
        super(aContext);
        mXPath = false;
        construct(aAlgorithm, null);
    }

    /**
     * Transform constructor for XPath
     * @param aContext         of the signature containing transform
     * @param aAlgorithm       transform algorithm
     * @param aXPathExpression for xpath transform
     * @param aPrefixToNamespaceMap prefix-namespace pairs Xpath element
     */
    //@throws XMLSignatureException if there is a problem about algorithm
    public Transform(Context aContext, String aAlgorithm, String aXPathExpression, Map<String, String> aPrefixToNamespaceMap)
    {
        super(aContext);

        if (!TransformType.XPATH.getUrl().equals(aAlgorithm))
        {
            throw new XMLSignatureRuntimeException("Illegal use of xpath transform constructor");
        }
        mXPath = true;

        Element xpath = createElement(NS_XMLDSIG, TAG_XPATH);
        xpath.setTextContent(aXPathExpression);

        if (aPrefixToNamespaceMap!=null){
            for (String prefix : aPrefixToNamespaceMap.keySet()){
                XmlUtil.addNSAttribute(xpath, prefix, aPrefixToNamespaceMap.get(prefix));
            }
        }

        construct(aAlgorithm, xpath);
    }

    /**
     * Generic constructor for transformation with unknown transform parameter
     * object. If you use this constructor you should most probably register
     * your own <code>
     * {@link tr.gov.tubitak.uekae.esya.api.xmlsignature.transforms.Transformer}
     * </code> to TransformEngine to execute transformation when necessary.
     * @param aContext       of the signature containing transform
     * @param aAlgorithm     transform algorithm
     * @param aParamsElement xml element of transform parameters if any
     */
    //@throws XMLSignatureException  if there is a problem about algorithm
    public Transform(Context aContext, String aAlgorithm, Element aParamsElement)
    {
        super(aContext);
        construct(aAlgorithm, aParamsElement);
    }

    // internal construction method
    private void construct(String aAlgorithm, Element aParamsElement)
    {
        mAlgorithm = aAlgorithm;

        if ((mAlgorithm == null) || (mAlgorithm.length() == 0))
        {
            throw new XMLSignatureRuntimeException("xml.WrongContent", ATTR_ALGORITHM, TAG_TRANSFORM);
        }
        mElement.setAttributeNS(null, ATTR_ALGORITHM, aAlgorithm);

        if (aParamsElement != null){
            addLineBreak();
            mElement.appendChild(aParamsElement);
            addLineBreak();
        }
        mParameterNode = aParamsElement;
        mParameters = new Object[] {(mXPath ? getXPathExpression() : null), aParamsElement};

    }

    /**
     * Construct Transform from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws XMLSignatureException when structure is invalid or can not be
     *                               resolved appropriately
     */
    public Transform(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mAlgorithm = getAttribute(aElement, ATTR_ALGORITHM);

        if ((mAlgorithm == null) || (mAlgorithm.length() == 0))
        {
            throw new XMLSignatureException("xml.WrongContent", ATTR_ALGORITHM, TAG_TRANSFORM);
        }

        if (TransformType.XPATH.getUrl().equals(mAlgorithm))
        {
            mParameters = new Object[] {getXPathExpression(), mParameterNode};
        }

    }

    public String getAlgorithm()
    {
        return mAlgorithm;
    }

    public void setAlgorithm(String aAlgorithm)
    {
        mElement.setAttributeNS(null, ATTR_ALGORITHM, aAlgorithm);
        mAlgorithm = aAlgorithm;
    }

    public Object[] getParameters()
    {
        return mParameters;
    }

    private String getXPathExpression()
    {
        /*
        The XPath expression to be evaluated appears as the character content of
        a transform parameter child element named XPath.
        */
        Element paramElement = selectChildElement(NS_XMLDSIG, TAG_XPATH);

        mParameterNode = paramElement;

        if (paramElement==null)
            return null;

        return getXpathExpressionFromNode(paramElement.getFirstChild());
    }

    /**
     * Method getStrFromNode
     * @param xpathnode node to extract xpath expression
     * @return the string for the node.
     */
    private String getXpathExpressionFromNode(Node xpathnode)
    {
        if (xpathnode.getNodeType() == Node.TEXT_NODE) {

            // iterate over all siblings of the context node because
            // eventually, the text is "polluted" with pi's or comments
            StringBuffer sb = new StringBuffer();

            for (Node currentSibling = xpathnode.getParentNode().getFirstChild();
                 currentSibling != null;
                 currentSibling = currentSibling.getNextSibling())
            {
                if (currentSibling.getNodeType() == Node.TEXT_NODE)
                {
                    sb.append(((Text) currentSibling).getData());
                }
            }

            return sb.toString();
        }
        else if (xpathnode.getNodeType() == Node.ATTRIBUTE_NODE)
        {
            return xpathnode.getNodeValue();
        }
        else if (xpathnode.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE)
        {
            return xpathnode.getNodeValue();
        }

        return null;
    }


    // base element methods
    public String getLocalName()
    {
        return TAG_TRANSFORM;
    }
}
