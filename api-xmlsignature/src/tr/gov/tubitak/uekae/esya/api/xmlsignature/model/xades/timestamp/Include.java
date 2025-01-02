package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

import org.w3c.dom.Element;

/**
 * <code>Include</code> elements explicitly identify data objects that are
 * time-stamped. Their order of appearance indicates how the data objects
 * contribute in the generation of the input to the digest computation.
 *
 * <p>The URI attribute in <code>Include</code> element identifies one
 * time-stamped data object. Its value MUST follow the rules indicated below:
 * <ul>
 * <li>It MUST have an empty not-fragment part and a bare-name XPointer fragment
 * when the Include and the time-stamped data object are in the same document.
 * <li>It MUST have a not empty not-fragment part and a bare-name XPointer
 * fragment when the Include and the time-stamped data object are not in the
 * same document.
 * <li>When not empty, its not-fragment part MUST be equal to:
 * <ul>
 * <li>the not-fragment part of the Target attribute of the QualifyingProperties
 * enclosing the Include element if the time-stamped data object is enveloped by
 * the XAdES signature; or
 * <li>the not-fragment part of the URI attribute of the QualifyingPropertiesReference
 * element referencing the QualifyingProperties element enveloping the
 * time-stamped data object if this QualifyingProperties element is not
 * enveloped by the XAdES signature.
 * </ul>
 * </ul>
 *
 * Applications aligned with the present document MUST parse the retrieved
 * resource, and then process the bare-name XPointer as explained below to get
 * a XPath node-set suitable for use by Canonical XML. For processing the
 * bare-name XPointer applications MUST use as XPointer evaluation context the
 * root node of the XML document that contains the element referenced by the
 * not-fragment part of URI. Applications MUST derive an XPath node-set from
 * the resultant location-set as indicated below:
 * <ol>
 * <li>Replace the element node E retrieved by the bare-name XPointer with E
 * plus all descendants of E (text, comments, PIs, elements) and all namespace
 * and attribute nodes of E and its descendant elements.
 * <li>Delete all the comment nodes.
 * </ol>
 *
 * <p>In time-stamps that cover <code>ds:Reference</code> elements, the
 * attribute referencedData MAY be present. If present with value set to "true",
 * the time-stamp is computed on the result of processing the corresponding
 * <code>ds:Reference</code> element according to the XMLDSIG processing model.
 * If the attribute is not present or is present with value "false", the
 * time-stamp is computed on the ds:Reference element itself. When appearing in
 * a time-stamp container property, each Include element MUST be processed in
 * order as detailed below:
 * <ol>
 * <li>Retrieve the data object referenced in the URI attribute following the
 * referencing mechanism indicated above.
 * <li>If the retrieved data is a ds:Reference element and the referencedData
 * attribute is set to the value "true", take the result of processing the
 * retrieved ds:Reference element according to the reference processing model of
 * XMLDSIG; otherwise take the ds:Reference element itself.
 * <li>If the resulting data is an XML node set, canonicalize it. If
 * ds:Canonicalization is present, the algorithm indicated by this element is
 * used. If not, the standard canonicalization method specified by XMLDSIG is used.
 * <li>Concatenate the resulting octets to those resulting from previous
 * processing as indicated in the corresponding time-stamp container property.
 * </ol>
 *
 * <p>Below follows the schema definition for the data type.
 *
 * <pre>
 * &lt;xsd:complexType name="IncludeType"&gt;
 *   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="required"/&gt;
 *   &lt;xsd:attribute name="referencedData" type="xsd:boolean" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Sep 28, 2009
 */
public class Include extends XAdESBaseElement
{
    private String mURI;
    private Boolean mReferencedData;

    public Include(Context aContext, String aURI, Boolean aReferencedData)
    {
        super(aContext);
        mURI = aURI;
        mReferencedData = aReferencedData;

        mElement.setAttributeNS(null, ATTR_URI, mURI);
        if (aReferencedData!=null){
            mElement.setAttributeNS(null, ATTR_REFERENCEDDATA, aReferencedData.toString());
        }
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public Include(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        mURI = mElement.getAttributeNS(null, ATTR_URI);
        String isReferenced = mElement.getAttributeNS(null, ATTR_REFERENCEDDATA);
        if (isReferenced!=null){
            mReferencedData = Boolean.valueOf(isReferenced);
        }
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String aURI)
    {
        mURI = aURI;
        mElement.setAttributeNS(null, ATTR_URI, mURI);
    }

    public Boolean getReferencedData()
    {
        return mReferencedData;
    }

    public void setReferencedData(Boolean aReferencedData)
    {
        mReferencedData = aReferencedData;
        if (aReferencedData==null){
            mElement.removeAttributeNS(null, ATTR_REFERENCEDDATA);
        }
        else {
            mElement.setAttributeNS(null, ATTR_REFERENCEDDATA, aReferencedData.toString());
        }
    }

    public String getLocalName()
    {
        return TAGX_INCLUDE;
    }
}
