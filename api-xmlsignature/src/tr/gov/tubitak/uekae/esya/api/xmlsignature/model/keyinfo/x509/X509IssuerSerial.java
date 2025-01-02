package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;


import java.math.BigInteger;

/**
 * <p><code>X509Data</code> child element, which contains an X.509 issuer
 * distinguished name/serial number pair. The distinguished name SHOULD be
 * represented as a string that complies with section 3 of RFC4514 [LDAP-DN],
 * to be generated according to the [Distinguished Name Encoding Rules]
 * described in [XMLdSig].
 *
 * <p>Below follows the schema definition.
 *
 * <pre>
 * &lt;complexType name="X509IssuerSerialType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="X509IssuerName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="X509SerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
 * @author ahmety
 * date: Jun 16, 2009
 */
public class X509IssuerSerial extends X509DataElement
{

    private String mIssuerName;
    private BigInteger mSerialNumber;

    // internal
    private Element mIssuerElement, mSerialElement;




    public X509IssuerSerial(Context aContext, ECertificate aCertificate){
        this(aContext,
             aCertificate.getIssuer().stringValue(),
             aCertificate.getSerialNumber());

    }


    public X509IssuerSerial(Context aContext, String aIssuerName, BigInteger aSerialNumber)
    {
        super(aContext);
        addLineBreak();
        mIssuerName = aIssuerName;
        mSerialNumber = aSerialNumber;

        mIssuerElement = insertTextElement(NS_XMLDSIG, TAG_X509ISSUERNAME,
                                           LDAPDNUtil.normalize(aIssuerName));
        mSerialElement = insertBase64EncodedElement(NS_XMLDSIG, TAG_X509SERIALNUMBER,
                                                    mSerialNumber.toByteArray());

    }

    /**
     *  Construct X509IssuerSerial from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public X509IssuerSerial(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mIssuerElement = XmlUtil.getNextElement(aElement.getFirstChild());
        mSerialElement = XmlUtil.getNextElement(mIssuerElement);

        mIssuerName = XmlUtil.getText(mIssuerElement);
        mSerialNumber = new BigInteger(1, XmlUtil.getText(mSerialElement).getBytes());
    }

    public String getIssuerName()
    {
        return mIssuerName;
    }

    public BigInteger getSerialNumber()
    {
        return mSerialNumber;
    }

    // base element
    public String getLocalName()
    {
        return TAG_X509ISSUERSERIAL;
    }

}
