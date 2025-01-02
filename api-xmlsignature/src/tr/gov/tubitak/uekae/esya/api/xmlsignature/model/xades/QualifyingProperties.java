package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * <p>The <code>QualifyingProperties</code> element acts as a container element
 * for all the  qualifying information that should be added to an XML signature.
 *
 * <p>The qualifying properties are split into properties that are
 * cryptographically bound to (i.e. signed by) the XML signature
 * (<code>SignedProperties</code>), and properties that are not
 * cryptographically bound tothe XML signature
 * (<code>UnsignedProperties</code>). The <code>SignedProperties</code> MUST be
 * covered by a <code>ds:Reference</code> element of the XML signature.
 *
 * <p>The mandatory <code>Target</code> attribute MUST refer to the Id attribute
 * of the corresponding <code>ds:Signature</code>. Its value MUST be an URI
 * with a bare-name XPointer fragment. When this element is enveloped by the
 * XAdES signature, its not-fragment part MUST be empty. Otherwise, its
 * not-fragment part MAY NOT be empty.
 *
 * <p>The optional <code>Id</code> attribute can be used to make a reference to
 * the <code>QualifyingProperties</code> container.
 *
 * <p>It is strongly recommended not to include empty
 * <code>xades:SignedProperties</code> or empty
 * <code>xades:UnsignedProperties</code> elements within the signature.
 * Applications verifying XAdES signatures MUST ignore empty
 * <code>xades:SignedProperties</code> and empty
 * <code>xades:UnsignedProperties</code> elements.
 *
 * <p>The element has the following structure.
 * <p><pre>
 * &lt;xsd:element name="QualifyingProperties" type="QualifyingPropertiesType"/&gt;
 *
 * &lt;xsd:complexType name="QualifyingPropertiesType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="SignedProperties" type="SignedPropertiesType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="UnsignedProperties" type="UnsignedPropertiesType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="Target" type="xsd:anyURI" use="required"/&gt;
 *   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jun 22, 2009
 *
 */
public class QualifyingProperties extends XAdESBaseElement
{
    private SignedProperties mSignedProperties;
    private UnsignedProperties mUnsignedProperties;

    private String mTarget;
    private XMLSignature mSignature;

    public QualifyingProperties(XMLSignature aSignature, Context aContext)
    {
        super(aContext);

        aSignature.setQualifyingProperties(this);

        String xmlnsXAdESPrefix = aContext.getConfig().getNsPrefixMap().getPrefix(NS_XADES_1_3_2);
        mElement.setAttributeNS(NS_NAMESPACESPEC, "xmlns:"+xmlnsXAdESPrefix.intern(), NS_XADES_1_3_2);

        setTarget("#"+aSignature.getId());

        mSignedProperties = new SignedProperties(aContext);

        addLineBreak();
        mElement.appendChild(mSignedProperties.getElement());
        addLineBreak();

        mSignature = aSignature;
    }

    /**
     *  Construct QualifyingProperties from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public QualifyingProperties(Element aElement, Context aContext, XMLSignature aSignature)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        aSignature.setQualifyingProperties(this);

        String xadesPrefix = aElement.getPrefix();
        aContext.getConfig().getNsPrefixMap().setPrefix(Constants.NS_XADES_1_3_2, xadesPrefix);

        Element signedPropsElement = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNEDPROPERTIES);
        if (signedPropsElement!=null)
            mSignedProperties = new SignedProperties(signedPropsElement, mContext);

        Element unsignedPropsElement = selectChildElement(NS_XADES_1_3_2, TAGX_UNSIGNEDPROPERTIES);
        if (unsignedPropsElement!=null)
            mUnsignedProperties = new UnsignedProperties(unsignedPropsElement, mContext, aSignature);

        // signature:id - qualifying properties:target check
        // simdilik bunu istemediler
//        if( ! aSignature.getId().equals(this.getElement().getAttribute("Target").substring(1)))
//            throw new XMLSignatureException("Qualifying Properties Target does not match Signature Id.");

    }

    public SignedProperties getSignedProperties()
    {
        return mSignedProperties;
    }


    public UnsignedProperties getUnsignedProperties()
    {
        return mUnsignedProperties;
    }

    public UnsignedProperties createOrGetUnsignedProperties()
    {
        if (mUnsignedProperties==null)
        {
            mUnsignedProperties = new UnsignedProperties(mContext, mSignature);
            mElement.appendChild(mUnsignedProperties.getElement());
            addLineBreak();

        }
        return mUnsignedProperties;
    }

    public String getTarget()
    {
        return mTarget;
    }

    public void setTarget(String aTarget)
    {
        mElement.setAttributeNS(null, ATTR_TARGET, aTarget);
        mTarget = aTarget;
    }

    public UnsignedSignatureProperties getUnsignedSignatureProperties(){
        if (mUnsignedProperties!=null){
            return mUnsignedProperties.getUnsignedSignatureProperties();
        }
        return null;
    }

    public UnsignedDataObjectProperties getUnsignedDataObjectProperties(){
        if (mUnsignedProperties!=null){
            return mUnsignedProperties.getUnsignedDataObjectProperties();
        }
        return null;
    }

    public SignedSignatureProperties getSignedSignatureProperties(){
        if (mSignedProperties!=null){
            return mSignedProperties.getSignedSignatureProperties();
        }
        return null;

    }

    public SignedDataObjectProperties getSignedDataObjectProperties(){
        if (mSignedProperties!=null){
            return mSignedProperties.getSignedDataObjectProperties();
        }
        return null;
    }

    // base element
    public String getLocalName()
    {
        return TAGX_QUALIFYINGPROPERTIES;
    }

}
