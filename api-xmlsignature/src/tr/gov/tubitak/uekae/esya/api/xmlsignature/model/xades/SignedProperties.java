package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.IdGenerator;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * <p><code>The SignedProperties</code> element contains a number of properties
 * that are collectively signed by the XMLDSIG signature.
 *
 * <p>The <code>SignedProperties</code> element MAY contain properties that
 * qualify the XMLDSIG signature itself or the signer. If present, they are
 * included as content of the <code>SignedSignatureProperties</code> element.
 *
 * <p>NOTE: If the <code>ds:KeyInfo</code> element is build according as
 * specified in clause 4.4.1, it could happen that no signed signature property
 * is required, and no <code>SignedSignatureProperties</code> element would be
 * needed in the XAdES signature.

 * <p>The <code>SignedProperties</code> element MAY also contain properties that
 * qualify some of the signed data objects. These properties appear as content
 * of the <code>SignedDataObjectProperties</code> element.

 * <p>The optional <code>Id</code> attribute can be used to make a reference to
 * the <code>SignedProperties</code> element.
 *
 * <p>Below follows the schema definition for SignedProperties element.
 *
 * <p><pre>
 * &lt;xsd:element name="SignedProperties" type="SignedPropertiesType" /&gt;
 *
 * &lt;xsd:complexType name="SignedPropertiesType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="SignedSignatureProperties"
 *          type="SignedSignaturePropertiesType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="SignedDataObjectProperties"
 *          type="SignedDataObjectPropertiesType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jun 22, 2009
 */
public class SignedProperties extends XAdESBaseElement
{
    private SignedSignatureProperties mSignedSignatureProperties;
    private SignedDataObjectProperties mSignedDataObjectProperties;

    public SignedProperties(Context aBaglam)
    {
        super(aBaglam);

        generateAndSetId(IdGenerator.TYPE_SIGNED_PROPERTIES);

        mSignedSignatureProperties = new SignedSignatureProperties(mContext);

        setupChildren();
    }

    /**
     *  Construct SignedProperties from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public SignedProperties(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element spe = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNEDSIGNATUREPROPERTIES);
        if (spe!=null)
        {
            mSignedSignatureProperties = new SignedSignatureProperties(spe, aContext);
        }

        Element sdop = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNEDATAOBJECTPROPERIES);
        if (sdop!=null)
        {
            mSignedDataObjectProperties = new SignedDataObjectProperties(sdop, aContext);
        }

        if (mId!=null){
            mElement.setAttributeNS(null, ATTR_ID, mId);
        }
    }

    public SignedSignatureProperties getSignedSignatureProperties()
    {
        return mSignedSignatureProperties;
    }

    public void setSignedSignatureProperties(SignedSignatureProperties aSSProperties)
    {
        mSignedSignatureProperties = aSSProperties;
        setupChildren();
    }

    public SignedDataObjectProperties getSignedDataObjectProperties()
    {
        return mSignedDataObjectProperties;
    }

    public SignedDataObjectProperties createOrGetSignedDataObjectProperties()
    {
        if(mSignedDataObjectProperties==null){
            mSignedDataObjectProperties = new SignedDataObjectProperties(mContext);
            setupChildren();
        }
        return mSignedDataObjectProperties;
    }

    public void setSignedDataObjectProperties(SignedDataObjectProperties aSDOProperties)
    {
        mSignedDataObjectProperties = aSDOProperties;
        setupChildren();
    }

    private void setupChildren()
    {
        XmlUtil.removeChildren(mElement);
        addLineBreak();

        if (mSignedSignatureProperties!=null){
            mElement.appendChild(mSignedSignatureProperties.getElement());
            addLineBreak();
        }

        if (mSignedDataObjectProperties!=null){
            mElement.appendChild(mSignedDataObjectProperties.getElement());
            addLineBreak();
        }

        if (mId!=null){
            mElement.setAttributeNS(null, ATTR_ID, mId);
        }
    }

    // base element
    public String getLocalName()
    {
        return TAGX_SIGNEDPROPERTIES;
    }
}
