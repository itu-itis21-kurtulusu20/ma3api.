package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * <p>When presenting signed data to a human user it may be important that there
 * is no ambiguity as to the presentation of the signed data object to the
 * relying party. In order for the appropriate representation (text, sound or
 * video) to be selected by the relying party a content hint MAY be indicated
 * by the signer. If a relying party system does not use the format
 * specified to present the data object to the relying party, the electronic
 * signature may not be valid. Such behaviour may have been established by the
 * signature policy, for instance.
 *
 * <p>The <code>DataObjectFormat</code> element provides information that
 * describes the format of the signed data object. This element SHOULD be
 * present when the signed data is to be presented to human users on
 * validation if the presentation format is not implicit within the data that
 * has been signed. This is a signed property that qualifies one specific
 * signed data object. In consequence, an XML electronic signature aligned with
 * the present document MAY contain more than one <code>DataObjectFormat</code>
 * elements, each one qualifying one signed data object.
 *
 * <p>Below follows the schema definition for this element.
 * <pre>
 * &lt;xsd:element name="DataObjectFormat" type="DataObjectFormatType"/&gt;
 *
 * &lt;xsd:complexType name="DataObjectFormatType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="Description" type="xsd:string" minOccurs="0"/&gt;
 *     &lt;xsd:element name="ObjectIdentifier" type="ObjectIdentifierType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="MimeType" type="xsd:string" minOccurs="0"/&gt;
 *     &lt;xsd:element name="Encoding" type="xsd:anyURI" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="ObjectReference" type="xsd:anyURI" use="required"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * <p>The mandatory <code>ObjectReference</code> attribute MUST reference the
 * <code>ds:Reference</code> element of the <code>ds:Signature</code>
 * corresponding with the data object qualified by this property.
 *
 * <p> This element can convey:
 * <ul>
 * <li>textual information related to the signed data object in element
 * <code>Description</code>;
 * <li>an identifier indicating the type of the signed data object in element
 * <code>ObjectIdentifier</code>;
 * <li>an indication of the MIME type of the signed data object in element
 * <code>MimeType</code>;
 * <li>an indication of the encoding format of the signed data object in
 * element <code>Encoding</code>.
 * </ul>
 * <p>At least one element of <code>Description</code>,
 * <code>ObjectIdentifier</code> and <code>MimeType</code> MUST be present
 * within the property.
 *
 * @author ahmety
 * date: Sep 17, 2009
 */
public class DataObjectFormat extends XAdESBaseElement
{
    // mandatory attribute
    private String mObjectReference;

    // one of Description, ObjectIdentifier and MimeType is mandatory
    private String mDescription;
    private ObjectIdentifier mObjectIdentifier;
    private String mMIMEType;
    private String mEncoding;

    // todo check if reference exists!
    public DataObjectFormat(Context aContext,
                            String aObjectReference,
                            String aDescription,
                            ObjectIdentifier aObjectIdentifier,
                            String aMIMEType,
                            String aEncoding)
            throws XMLSignatureException
    {
        super(aContext);
        addLineBreak();

        mObjectReference = aObjectReference;
        mDescription = aDescription;
        mObjectIdentifier = aObjectIdentifier;
        mMIMEType = aMIMEType;
        mEncoding = aEncoding;
        
        if (mObjectReference==null)
        {
            throw new XMLSignatureException("errors.null", "ObjectReference");
        }
        if ((mDescription==null) && (mObjectIdentifier==null) && (mMIMEType==null))
        {
            throw new XMLSignatureException("core.model.missingDataObjectFormatContent");

        }


        mElement.setAttributeNS(null, ATTR_OBJECTREFERENCE, mObjectReference);

        if (mDescription!=null){
            insertTextElement(NS_XADES_1_3_2, TAGX_DESCRIPTION, mDescription);
        }
        if (mObjectIdentifier!=null){
            mElement.appendChild(mObjectIdentifier.getElement());
            addLineBreak();
        }
        if (mMIMEType!=null){
            insertTextElement(NS_XADES_1_3_2, TAGX_MIMETYPE, mMIMEType);
        }
        if (mEncoding!=null){
            insertTextElement(NS_XADES_1_3_2, TAGX_ENCODING, mEncoding);
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
    public DataObjectFormat(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        // manadatory reference attribute
        mObjectReference = mElement.getAttributeNS(null, ATTR_OBJECTREFERENCE);
        if (mObjectReference==null || mObjectReference.trim().length()<1)
        {
            throw new XMLSignatureException("errors.null", "ObjectReference");
        }

        Element objIdElm = selectChildElement(NS_XADES_1_3_2, TAGX_OBJECTIDENTIFIER);

        mDescription = getChildText(NS_XADES_1_3_2, TAGX_DESCRIPTION);
        mMIMEType    = getChildText(NS_XADES_1_3_2, TAGX_MIMETYPE);
        mEncoding    = getChildText(NS_XADES_1_3_2, TAGX_ENCODING);

        if (mDescription==null && objIdElm==null && mMIMEType==null)
        {
            throw new XMLSignatureException("core.model.missingDataObjectFormatContent");
        }

        if (objIdElm!=null){
            mObjectIdentifier = new ObjectIdentifier(objIdElm, mContext);
        }
    }

    public String getObjectReference()
    {
        return mObjectReference;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public ObjectIdentifier getObjectIdentifier()
    {
        return mObjectIdentifier;
    }

    public String getMIMEType()
    {
        return mMIMEType;
    }

    public String getEncoding()
    {
        return mEncoding;
    }

    public String getLocalName()
    {
        return TAGX_DATAOBJECTFORMAT;
    }

}
