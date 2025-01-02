package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.dom.Element;

import java.math.BigInteger;

/**
 * <code>CrlRef</code> element references one CRL. Each reference contains a
 * set of data (<code>CRLIdentifier</code> element) including the issuer
 * (<code>Issuer</code> element), the time when the CRL was issued (
 * <code>IssueTime</code> element) and optionally the number of the CRL
 * (<code>Number</code> element).
 *
 * <p><code>CRLIdentifier</code> element contents MUST follow the rules
 * established by XMLDSIG [3] in its clause 4.4.4 for strings representing
 * Distinguished Names.
 *
 * <p>Below follows the schema definition: 
 * <pre>
 * &lt;xsd:complexType name="CRLIdentifierType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="Issuer" type="xsd:string"/&gt;
 *     &lt;xsd:element name="IssueTime" type="xsd:dateTime" /&gt;
 *     &lt;xsd:element name="Number" type="xsd:integer" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Nov 10, 2009
 */
public class CRLIdentifier extends XAdESBaseElement
{
    private String mIssuer;
    private XMLGregorianCalendar mIssueTime;
    private BigInteger mNumber;

    private String mURI;

    public CRLIdentifier(Context aContext, String aIssuer, XMLGregorianCalendar aIssueTime, BigInteger aNumber, String aURI)
    {
        super(aContext);

        mIssuer = LDAPDNUtil.normalize(aIssuer);
        mIssueTime = aIssueTime;
        mNumber = aNumber;
        mURI = aURI;

        insertTextElement(NS_XADES_1_3_2, TAGX_ISSUER, mIssuer);
        insertTextElement(NS_XADES_1_3_2, TAGX_ISSUETIME, aIssueTime.toString());
        if (mNumber!=null){
            insertTextElement(NS_XADES_1_3_2, TAGX_NUMBER, mNumber.toString());
        }
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
    public CRLIdentifier(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element issuerElement = selectChildElement(NS_XADES_1_3_2, TAGX_ISSUER);
        mIssuer = XmlUtil.getText(issuerElement);

        Element issueTimeElement = selectChildElement(NS_XADES_1_3_2, TAGX_ISSUETIME);
        mIssueTime = XmlUtil.getDate(issueTimeElement);

        Element numberElement = selectChildElement(NS_XADES_1_3_2, TAGX_NUMBER);
        if (numberElement!=null){
            mNumber = new BigInteger(XmlUtil.getText(numberElement));
        }

        mURI = mElement.getAttributeNS(null, ATTR_URI);
    }

    public String getIssuer()
    {
        return mIssuer;
    }

    public XMLGregorianCalendar getIssueTime()
    {
        return mIssueTime;
    }

    public BigInteger getNumber()
    {
        return mNumber;
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
            builder.append("uri: ").append(mURI).append("\n");

        builder.append("issuer: ").append(mIssuer)
                .append("\nissue time: ").append(mIssueTime)
                .append("\nno: ").append(mNumber).append("\n");

        return builder.toString(); 
    }

    public String getLocalName()
    {
        return TAGX_CRLIDENTIFIER;
    }
}
