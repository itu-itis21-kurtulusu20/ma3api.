package tr.gov.tubitak.uekae.esya.api.xmlsignature.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.InMemoryDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.Arrays;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;


/**
 * <p><code>Reference</code> is an element that may occur one or more times. It
 * specifies a digest algorithm and digest value, and optionally an identifier
 * of the object being signed, the type of the object, and/or a list of
 * transforms to be applied prior to digesting. The identification (URI) and
 * transforms describe how the digested content (i.e., the input to the digest
 * method) was created. The <code>Type</code> attribute facilitates the
 * processing of referenced data. For example, while this specification makes no
 * requirements over external data, an application may wish to signal that the
 * referent is a <code>Manifest</code>. An optional ID attribute permits a
 * <code>Reference</code> to be referenced from elsewhere.</p>
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="ReferenceType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Transforms" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestMethod"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}DigestValue"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *       &lt;attribute name="URI" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anyURI" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jun 10, 2009
 */
public class Reference extends BaseElement
{
    private static final Logger logger = LoggerFactory.getLogger(Reference.class);

    private Transforms mTransforms;
    private boolean transformsAdded;

    private DigestMethod mDigestMethod;
    private Manifest mReferencedManifest;
    private byte[] mDigestValue;
    private String mURI;
    private String mType;

    public Document mReferencedDocument;
    public Document mTransformedDocument;

    public static final String MANIFEST_URI = NS_XMLDSIG + TAG_MANIFEST;

    // internal
    private Element mDigestMethodElement, mDigestValueElement;

    public Reference(Context aBaglam)
    {
        super(aBaglam);
        addLineBreak();

        mDigestMethodElement = insertElement(NS_XMLDSIG, TAG_DIGESTMETHOD);
        mDigestValueElement  = insertElement(NS_XMLDSIG, TAG_DIGESTVALUE);
    }

    /**
     *  Construct Reference from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public Reference(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mURI  = getAttribute(aElement, ATTR_URI);
        mType = getAttribute(aElement, ATTR_TYPE);

        Element element = XmlUtil.getNextElement(aElement.getFirstChild());
        if (TAG_TRANSFORMS.equals(element.getLocalName()))
        {
            mTransforms = new Transforms(element, mContext);
            element = XmlUtil.getNextElement(element.getNextSibling());
            transformsAdded = true;
        }

        mDigestMethodElement = element;
        String digestMethodAlg = getAttribute(mDigestMethodElement, ATTR_ALGORITHM);
        mDigestMethod = DigestMethod.resolve(digestMethodAlg);

        mDigestValueElement = XmlUtil.getNextElement(mDigestMethodElement.getNextSibling());
        mDigestValue = XmlUtil.getBase64DecodedText(mDigestValueElement);



        if (MANIFEST_URI.equals(mType))
        {
            Element manifestElement = XmlUtil.findByIdAttr(getDocument().getDocumentElement(), mURI.substring(1));
            mReferencedManifest = new Manifest(manifestElement, aContext);
        }
    }

    public Document getReferencedDocument() throws XMLSignatureException
    {
        if (mReferencedDocument==null){
            try {
        	    mReferencedDocument = Resolver.resolve(this, mContext);
            }
            catch (XMLSignatureException e) {
				logger.error("Error occurred while deferencing "+mURI, e);
				throw e;
			}
        }
        return mReferencedDocument;
    }


    /**
     * apply transforms, if resulting document is NodeSet canonicalize it using
     * default c14nMethod
     *
     * @return transforms applied document
     */
    public Document getTransformedDocument() throws XMLSignatureException {
         return getTransformedDocument(C14nMethod.INCLUSIVE);
    }

    public Document getTransformedDocument(C14nMethod aC14nMethod) throws XMLSignatureException {
        if (mTransformedDocument==null){
            if (mTransforms!=null){
                mTransformedDocument = mTransforms.apply(getReferencedDocument());
            }
            else {
                mTransformedDocument = getReferencedDocument();
            }
            /*
            Users may specify alternative transforms that override these
            defaults in transitions between transforms that expect different
            inputs. !!!The final octet stream contains the data octets!!! being
            secured. The digest algorithm specified by DigestMethod is then
            applied to these data octets, resulting in the DigestValue.
            */
            if (mTransformedDocument.getType()== DataType.NODESET){
                byte[] bytes = XmlUtil.outputDOM(mTransformedDocument.getNodeList(), aC14nMethod);
                mTransformedDocument = new InMemoryDocument(bytes, mTransformedDocument.getURI(), mTransformedDocument.getMIMEType(), mTransformedDocument.getEncoding());
            }
        }
        return mTransformedDocument;
    }

    public Transforms getTransforms()
    {
        return mTransforms;
    }

    public void setTransforms(Transforms aTransforms)
    {
        if (transformsAdded) {
            throw new XMLSignatureRuntimeException("Transforms already added!");
        }
        transformsAdded = true;
        mTransforms = aTransforms;
        mElement.insertBefore(aTransforms.getElement(), mDigestMethodElement);
        mElement.insertBefore(getDocument().createTextNode("\n"), mDigestMethodElement);
    }

    public DigestMethod getDigestMethod()
    {
        return mDigestMethod;
    }

    public void setDigestMethod(DigestMethod aDigestMethod)
    {
        mDigestMethodElement.setAttributeNS(null, ATTR_ALGORITHM, aDigestMethod.getUrl());
        mDigestMethod = aDigestMethod;
    }

    public byte[] getDigestValue()
    {
        return mDigestValue;
    }

    public void setDigestValue(byte[] aDigestValue)
    {
        XmlUtil.setBase64EncodedText(mDigestValueElement, aDigestValue);
        mDigestValue = aDigestValue;
    }

    public void generateDigestValue()
            throws XMLSignatureException
    {
        try {
            Document doc = getTransformedDocument();
            byte[] bytes;
            if (logger.isDebugEnabled()){
                logger.debug("Generate digest for reference id :"+mId);
                logger.debug("Reference.uri :"+mURI);
                logger.debug("Reference.digestMethod :"+mDigestMethod.getAlgorithm());
                logger.debug("Reference.data (trimmed) :" + StringUtil.substring(doc.getBytes(), 256));
            }

/*            if (doc.getType().equals(DataType.OCTETSTREAM))
            {
                bytes = KriptoUtil.digest(doc.getStream(), mDigestMethod);
            }
            else*/
            {
                bytes = KriptoUtil.digest(doc.getBytes(), mDigestMethod);
            }
            if (logger.isDebugEnabled()){
                logger.debug("Generated digest is :"+ Base64.encode(bytes));
            }
            setDigestValue(bytes);
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantDigest", "Reference(id:'"+mId+"', uri:'"+mURI+")");
        }
    }

    public boolean validateDigestValue() throws XMLSignatureException
    {
        try {
            Document doc = getTransformedDocument();
            if (logger.isDebugEnabled()){
                logger.debug("Validating reference id :"  + mId);
                logger.debug("Reference.uri :"            + mURI);
                logger.debug("Reference.digestMethod :"   + mDigestMethod.getAlgorithm());
                logger.debug("Reference.data (trimmed) :" + StringUtil.substring(doc.getBytes(), 256));
            }

            byte[] bytes;

            bytes = KriptoUtil.digest(doc.getBytes(), mDigestMethod);

            if (logger.isDebugEnabled()){
                logger.debug("Original digest: " + Base64.encode(getDigestValue()));
                logger.debug("Calculated digest: " + Base64.encode(bytes));
            }
            boolean digestOk = Arrays.equals(bytes, getDigestValue());
            if (!digestOk) {
                logger.info("Reference( id :'"+mId+"' uri: '" + mURI +"' could not be validated.");
                return false;
            } else {
                logger.info("Reference( id :'"+mId+"' uri: '" + mURI + "' validated.");
            }
        }
        catch (Exception x) {
            logger.info("Referans( id :'"+mId+"' uri: '" + mURI + "' could not be processed.", x);
            return false;
        }
        return true;
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String aURI)
    {
        mElement.setAttributeNS(null, ATTR_URI, aURI);
        mURI = aURI;
        // clear cached data
        mReferencedDocument = null;
        mTransformedDocument = null;
    }

    public String getType()
    {
        return mType;
    }

    public void setType(String aType)
    {
        mElement.setAttributeNS(null, ATTR_TYPE, aType);
        mType = aType;
    }

    public Manifest getReferencedManifest()
    {
        return mReferencedManifest;
    }

    // baseElement metodlari
    public String getLocalName()
    {
        return TAG_REFERENCE;
    }
}
