package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.dom.Element;

/**
 *
 * <p>Below follows the schema definiton:
 * <pre>
 * &lt;xsd:complexType name="OCSPIdentifierType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="ResponderID" type="ResponderIDType"/&gt;
 *     &lt;xsd:element name="ProducedAt" type="xsd:dateTime"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Nov 11, 2009
 */
public class OCSPIdentifier extends XAdESBaseElement
{
    private ResponderID mResponderID;
    private XMLGregorianCalendar mProducedAt;

    private String mURI;

    public OCSPIdentifier(Context aContext, ResponderID aResponderID, XMLGregorianCalendar aProducedAt, String aURI)
    {
        super(aContext);
        addLineBreak();

        mURI = aURI;
        mResponderID = aResponderID;
        mProducedAt = aProducedAt;

        mElement.appendChild(mResponderID.getElement());
        addLineBreak();

        insertTextElement(NS_XADES_1_3_2, TAGX_PRODUCEDAT, aProducedAt.toString());

        if (mURI!=null){
            mElement.setAttributeNS(null, ATTR_URI, mURI);
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
    public OCSPIdentifier(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element responderElement = selectChildElement(NS_XADES_1_3_2, TAGX_RESPONDERID);
        mResponderID = new ResponderID(responderElement, mContext);

        Element timeElement = selectChildElement(NS_XADES_1_3_2, TAGX_PRODUCEDAT);
        mProducedAt = XmlUtil.getDate(timeElement);

        mURI = mElement.getAttributeNS(null, ATTR_URI);
    }

    public ResponderID getResponderID()
    {
        return mResponderID;
    }

    public XMLGregorianCalendar getProducedAt()
    {
        return mProducedAt;
    }

    public String getURI()
    {
        return mURI;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if (mURI!=null)
            builder.append("uri : ").append(mURI).append("\n");
        if (mResponderID.getByKey() != null)
            builder.append("key : ").append(Base64.encode(mResponderID.getByKey()));
        if (mResponderID.getByName() != null)
            builder.append("name : ").append(mResponderID.getByName());
        builder.append("\nproduced at : ").append(mProducedAt);

        return builder.toString();     
    }

    public String getLocalName()
    {
        return TAGX_OCSPIDENTIFIER;
    }
}
