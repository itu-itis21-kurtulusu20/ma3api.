package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DataType;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;


/**
 * <p>A RetrievalMethod element within KeyInfo is used to convey a reference to
 * KeyInfo information that is stored at another location.</p>
 *
 * <p>RetrievalMethod uses the same syntax and dereferencing behavior as
 * Reference's URI, and "The Reference Processing Model" except that there is
 * no DigestMethod or DigestValue child elements and presence of the URI is
 * mandatory.
 *
 * <p>Type is an optional identifier for the type of data retrieved after all
 * transforms have been applied. The result of dereferencing a RetrievalMethod
 * Reference for all KeyInfo types defined by this specification with a
 * corresponding XML structure is an XML element or document with that element
 * as the root. The rawX509Certificate KeyInfo (for which there is no XML
 * structure) returns a binary X509 certificate.
 * 
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <p>
 * <pre>
 * &lt;complexType name="RetrievalMethodType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Transforms" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="URI" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * @author ahmety
 * date: Jun 10, 2009
 */
public class RetrievalMethod extends BaseElement implements KeyInfoElement
{

    public static final String TYPE_RAWX509 = NS_XMLDSIG + "rawX509Certificate";


    private String mURI;
    private String mType;

    private Transforms mTransforms;
    private byte[] mRawX509;

    /**
     * Construct RetrievalMethod from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws XMLSignatureException when structure is invalid or can not be
     *                               resolved appropriately
     */
    public RetrievalMethod(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mURI = getAttribute(aElement, ATTR_URI);
        mType = getAttribute(aElement, ATTR_TYPE);

        Element element = XmlUtil.getNextElement(aElement.getFirstChild());
        if (element != null && TAG_TRANSFORMS.equals(element.getLocalName())) {
            mTransforms = new Transforms(element, mContext);
        }
    }

    public String getURI()
    {
        return mURI;
    }

    public String getType()
    {
        return mType;
    }

    public Transforms getTransforms()
    {
        return mTransforms;
    }

    /**
     * Dereference RetrievalMethod URI, appy transforms and
     * @return final KeyInfo (rmation) Element, returns itself if Retrieval
     *         Method is of type #rawX509Certificate
     * @throws XMLSignatureException if any error occurred over
     *                               derefencing, transformation and final construction phase
     */
    public KeyInfoElement resolve() throws XMLSignatureException
    {
        Element found;
        Document doc;
        try {
            doc = Resolver.resolve(mURI, mContext);
            if (mTransforms!=null)
                doc = mTransforms.apply(doc);
        }
        catch (Exception x) {
            throw new XMLSignatureException(x, "core.cantResolveRetrievalMethod", mURI);
        }
        if (doc == null) {
            throw new XMLSignatureException("core.cantResolveRetrievalMethod", mURI);
        }


        if (TYPE_RAWX509.equals(mType)) {
            mRawX509 = doc.getBytes();
            return this;
        }

        /*
        The result of dereferencing a RetrievalMethod Reference for all KeyInfo
        types defined by this specification (section 4.4) with a corresponding
        XML structure is an XML element or document with that element as the
        root.
         */
        if (doc.getType() == DataType.NODESET){
            found = (Element) doc.getNodeList().item(0);
            return KeyInfo.resolve(found, mContext);
        }

        throw new XMLSignatureException("core.invalidRetrievalMethod", doc);

    }

    public byte[] getRawX509()
    {
        return mRawX509;
    }

    //base element
    public String getLocalName()
    {
        return TAG_RETRIEVALMETHOD;
    }
}
