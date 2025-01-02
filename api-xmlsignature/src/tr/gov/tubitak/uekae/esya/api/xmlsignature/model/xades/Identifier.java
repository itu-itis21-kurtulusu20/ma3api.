package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import org.w3c.dom.Element;

/**
 * <p>The Identifier element contains a permanent identifier. Once the
 * identifier is assigned, it can never be re-assigned again. It supports both
 * the mechanism that is used to identify objects in ASN.1 and the mechanism
 * that is usually used to identify objects in an XML environment:
 * <ul>
 * <li>in a XML environment objects are typically identified by means of a
 * Uniform Resource Identifier, URI. In this case, the content of
 * <code>Identifier</code> consists of the identifying URI, and the optional
 * <code>Qualifier</code> attribute does not appear;
 * <li>in ASN.1 an Object IDentifier (OID) is used to identify an object.
 * To support an OID, the content of Identifier consists of an OID, either
 * encoded as Uniform Resource Name (URN) or as Uniform Resource Identifier
 * (URI). The optional <code>Qualifier</code> attribute can be used to provide
 * a hint about the applied encoding (values OIDAsURN or OIDAsURI).
 * </ul>
 * <p>Should an OID and an URI exist identifying the same object, the present
 * document encourages the use of the URI as explained in the first bullet
 * above.
 *
 * <p><pre>
 * &lt;xsd:complexType name="IdentifierType"&gt;
 *   &lt;xsd:simpleContent&gt;
 *     &lt;xsd:extension base="xsd:anyURI"&gt;
 *       &lt;xsd:attribute name="Qualifier" type="QualifierType" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:simpleContent&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:simpleType name="QualifierType"&gt;
 *   &lt;xsd:restriction base="xsd:string"&gt;
 *     &lt;xsd:enumeration value="OIDAsURI"/&gt;
 *     &lt;xsd:enumeration value="OIDAsURN"/&gt;
 *   &lt;/xsd:restriction&gt;
 * &lt;/xsd:simpleType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Sep 17, 2009
 */
public class Identifier extends XAdESBaseElement
{
    public static final String QUALIFIER_OIDAsURI = "OIDAsURI";
    public static final String QUALIFIER_OIDAsURN = "OIDAsURN";


    private String mValue;
    private String mQualifier;

    public Identifier(Context aContext, int[] aOId)
    {
        this(aContext, OIDUtil.toURN(aOId), QUALIFIER_OIDAsURN);
    }

    public Identifier(Context aContext, String uri)
    {
        this(aContext, uri, QUALIFIER_OIDAsURI);
    }

    public Identifier(Context aBaglam, String aValue, String aQualifier)
    {
        super(aBaglam);
        mValue = aValue;
        mQualifier = aQualifier;

        mElement.setTextContent(aValue);
        if (mQualifier!=null){
            mElement.setAttributeNS(null, ATTR_QUALIFIER, mQualifier);
        }
    }

    /**
     * Construct Identifier from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public Identifier(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        mValue = mElement.getTextContent();
        mQualifier = mElement.getAttributeNS(null, ATTR_QUALIFIER);
    }

    public String getValue()
    {
        return mValue;
    }

    public String getQualifier()
    {
        return mQualifier;
    }

    public String getLocalName()
    {
        return TAGX_IDENTIFIER;  
    }
}
