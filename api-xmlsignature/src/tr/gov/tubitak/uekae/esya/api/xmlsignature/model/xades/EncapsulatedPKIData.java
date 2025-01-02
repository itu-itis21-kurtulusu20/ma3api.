package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import org.w3c.dom.Element;

/**
 * <p>The <code>EncapsulatedPKIDataType</code> is used to incorporate non XML
 * pieces of PKI data into an XML structure. Examples of such PKI data that are
 * widely used at the time being include X.509 certificates and revocation
 * lists, OCSP responses, attribute certificates and time-stamp tokens.
 *
 * <p>
 * <pre>
 * &lt;xsd:element name="EncapsulatedPKIData" type="EncapsulatedPKIDataType"/&gt;
 *
 * &lt;xsd:complexType name="EncapsulatedPKIDataType"&gt;
 *   &lt;xsd:simpleContent&gt;
 *     &lt;xsd:extension base="xsd:base-64Binary"&gt;
 *       &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 *       &lt;xsd:attribute name="Encoding" type="xsd:anyURI" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:simpleContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * <p>The content of this data type is the piece of PKI data, base-64 encoded.
 *
 * <p>For encoding types see <code>{@link PKIEncodingType}</code>
 *
 * <p>If the Encoding attribute is not present, then it is assumed that the PKI
 * data is ASN.1 data encoded in DER.
 *
 * <p>NOTE: The present document restricts the encoding options to only one for
 * certain types of the aforementioned PKI data in those sections that specify
 * XAdES properties related to these data.
 *
 * <p>The optional ID attribute can be used to make a reference to an element
 * of this data type.
 *
 * @author ahmety
 * date: Sep 29, 2009
 */
public abstract class EncapsulatedPKIData extends XAdESBaseElement
{
    protected PKIEncodingType mEncoding;
    protected byte[] mValue;

    public EncapsulatedPKIData(Context aContext)
    {
        super(aContext);
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public EncapsulatedPKIData(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        mValue = XmlUtil.getBase64DecodedText(mElement);
        mEncoding = PKIEncodingType.resolve(mElement.getAttribute(Constants.ATTR_ENCODING));
    }

    public PKIEncodingType getEncoding()
    {
        return mEncoding;
    }

    public void setEncoding(PKIEncodingType aEncoding)
    {
        mEncoding = aEncoding;
        mElement.setAttributeNS(null, Constants.ATTR_ENCODING, aEncoding.getURI());
    }

    public byte[] getValue()
    {
        return mValue;
    }

    public void setValue(byte[] aValue)
    {
        mValue = aValue;
        XmlUtil.setBase64EncodedText(mElement, aValue);
    }
}
