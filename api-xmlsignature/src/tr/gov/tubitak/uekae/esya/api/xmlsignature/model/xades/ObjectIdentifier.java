package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;

import java.util.List;
import java.util.ArrayList;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * <p>The <code>ObjectIdentifier</code> data type can be used to identify a
 * particular data object. It allows the specification of a unique and
 * permanent identifier of an object. In addition, it may also contain,
 * a textual description of the nature of the data object, and a number of
 * references to documents where additional information about the nature of the
 * data object can be found.
 *
 * <p><pre>
 * &lt;xsd:complexType name="ObjectIdentifierType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="Identifier" type="IdentifierType"/&gt;
 *     &lt;xsd:element name="Description" type="xsd:string" minOccurs="0"/&gt;
 *     &lt;xsd:element name="DocumentationReferences" type="DocumentationReferencesType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
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
 * <p>The optional <code>Description</code> element contains an informal text
 * describing the object identifier.
 *
 * <p>The optional <code>DocumentationReferences</code> element consists of an
 * arbitrary number of references pointing to further explanatory documentation
 * of the object identifier.
 * <p><pre>
 * &lt;xsd:complexType name="DocumentationReferencesType"&gt;
 *   &lt;xsd:sequence maxOccurs="unbounded"&gt;
 *   &lt;xsd:element name="DocumentationReference" type="xsd:anyURI"/&gt;
 * &lt;/xsd:sequence&gt;
 * </pre>
 *
 * @author ahmety
 * date: Sep 17, 2009
 */
public class ObjectIdentifier extends XAdESBaseElement
{
    private Identifier mIdentifier;
    private String mDescription;
    private List<String> mDocumentationReferences = new ArrayList<String>();

    public ObjectIdentifier(Context aBaglam,
                            Identifier aIdentifier, String aDescription,
                            List<String> aDocumentationReferences)
    {
        super(aBaglam);
        mIdentifier = aIdentifier;
        mDescription = aDescription;
        mDocumentationReferences = aDocumentationReferences;

        addLineBreak();
        mElement.appendChild(aIdentifier.getElement());
        addLineBreak();

        if (aDescription!=null){
            insertTextElement(NS_XADES_1_3_2, TAGX_DESCRIPTION, mDescription);
        }
        if (aDocumentationReferences!=null && aDocumentationReferences.size()>0){
            Element docsElm = insertElement(NS_XADES_1_3_2, TAGX_DOCUMENTATIONREFERENCES);
            addLineBreak(docsElm);
            for (int i = 0; i < aDocumentationReferences.size(); i++) {
                Element doc = createElement(NS_XADES_1_3_2, TAGX_DOCUMENTATIONREFERENCE);
                doc.setTextContent(aDocumentationReferences.get(i));
                docsElm.appendChild(doc);
                addLineBreak(docsElm);
            }
        }
    }

    /**
     * Construct ObjectIdentifier from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public ObjectIdentifier(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element identifierElm = selectChildElement(NS_XADES_1_3_2, TAGX_IDENTIFIER);
        if (identifierElm!=null){
            mIdentifier = new Identifier(identifierElm, mContext);
        }

        mDescription = getChildText(NS_XADES_1_3_2, TAGX_DESCRIPTION);

        Element docsElement = selectChildElement(NS_XADES_1_3_2, TAGX_DOCUMENTATIONREFERENCES);
        Element[] refs = XmlUtil.selectNodes(docsElement, NS_XADES_1_3_2, TAGX_DOCUMENTATIONREFERENCE);
        for (int i = 0; i < refs.length; i++) {
            mDocumentationReferences.add(XmlUtil.getText(refs[i]));
        }
    }

    public Identifier getIdentifier()
    {
        return mIdentifier;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public List<String> getDocumentationReferences()
    {
        return mDocumentationReferences;
    }

    public String getLocalName()
    {
        return TAGX_OBJECTIDENTIFIER;
    }
}
