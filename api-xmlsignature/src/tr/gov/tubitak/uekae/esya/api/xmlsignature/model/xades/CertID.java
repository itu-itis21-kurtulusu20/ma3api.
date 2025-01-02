package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.cms.ESignerIdentifier;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertificateSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.XMLUtils;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;


import java.math.BigInteger;


/**
 * The <code>SigningCertificate</code> element contains the aforementioned
 * sequence of certificate identifiers and digests computed on the
 * certificates (Cert elements).
 *
 * <p>The element <code>IssuerSerial</code> contains the identifier of one
 * of the certificates referenced in the sequence. Should the
 * <code>ds:X509IssuerSerial</code> element appear in the signature to denote
 * the same certificate, its value MUST be consistent with the corresponding
 * <code>IssuerSerial</code> element.
 *
 * <p>The element <code>CertDigest</code> contains the digest of one of the certificates
 * referenced in the sequence. It contains two elements:
 * <code>ds:DigestMethod</code> indicates the digest algorithm and
 * <code>ds:DigestValue</code> contains the base-64 encoded value of the digest
 * computed on the DER-encoded certificate.
 *
 * <p>The optional <code>URI</code> attribute indicates where the referenced
 * certificate can be found.
 *
 * <p>Below follows the Schema definition.
 * <pre>
 * &lt;xsd:element name="Cert" type="CertIDType"/&gt;
 *
 * &lt;xsd:complexType name="CertIDType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="CertDigest" type="DigestAlgAndValueType"/&gt;
 *     &lt;xsd:element name="IssuerSerial" type="ds:X509IssuerSerialType"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:complexType name="DigestAlgAndValueType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element ref="ds:DigestMethod"/&gt;
 *     &lt;xsd:element ref="ds:DigestValue"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jun 22, 2009
 */
public class CertID extends XAdESBaseElement
{
    //digestAlgAndValue
    private CertDigest mCertificateDigest;

    // issuerSerial
    private String mX509IssuerName;
    protected BigInteger mX509SerialNumber;

    private String mURI;

    private Element mIssuerSerialElement,
                    mIssuerNameElement, mSerialNumberElement;

    public CertID(Context aContext)
    {
        super(aContext);

        addLineBreak();

        mCertificateDigest = new CertDigest(aContext);
        mElement.appendChild(mCertificateDigest.getElement());
        addLineBreak();


        mIssuerSerialElement = insertElement(NS_XADES_1_3_2, TAGX_ISSUERSERIAL);
        mIssuerNameElement   = createElement(NS_XMLDSIG, TAG_X509ISSUERNAME);
        mSerialNumberElement = createElement(NS_XMLDSIG, TAG_X509SERIALNUMBER);

        addLineBreak(mIssuerSerialElement);
        mIssuerSerialElement.appendChild(mIssuerNameElement);
        addLineBreak(mIssuerSerialElement);
        mIssuerSerialElement.appendChild(mSerialNumberElement);
        addLineBreak(mIssuerSerialElement);
    }

    public CertID(Context aBaglam, ECertificate aCertificate, DigestMethod aDigestAlg)
            throws XMLSignatureException
    {
        this(aBaglam);
        DigestMethod digestMethod = (aDigestAlg ==null) ? mContext.getConfig().getAlgorithmsConfig().getDigestMethod() : aDigestAlg;
        byte[] ozet = KriptoUtil.digest(aCertificate.getEncoded(), digestMethod );

        setDigestMethod(digestMethod);
        setDigestValue(ozet);

        setX509SerialNumber(aCertificate.getSerialNumber());
        setX509IssuerName(aCertificate.getIssuer().stringValue());

    }

    public CertID(Context aBaglam, byte[] certHash, DigestMethod aDigestAlg, ESignerIdentifier eSignerIdentifier) throws XMLSignatureException {
        this(aBaglam);
        DigestMethod digestMethod = (aDigestAlg == null) ? mContext.getConfig().getAlgorithmsConfig().getDigestMethod() : aDigestAlg;

        setDigestMethod(digestMethod);
        setDigestValue(certHash);

        setX509SerialNumber(eSignerIdentifier.getIssuerAndSerialNumber().getSerialNumber());
        setX509IssuerName(eSignerIdentifier.getIssuerAndSerialNumber().getIssuer().stringValue());
    }

    /**
     * Mevcut xml yapisindaki elemanı cozmek icin constructor
     * @param aElement xml element to unmarshall
     * @param aContext which owner signature belongs to
     * @throws XMLSignatureException if wrong element is given!
     */
    public CertID(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        // digest alg & value
        Element certDigestElement = XmlUtil.getNextElement(mElement.getFirstChild());
        mCertificateDigest = new CertDigest(certDigestElement, aContext);

        // issuer name & serial
        mIssuerSerialElement = XmlUtil.getNextElement(certDigestElement.getNextSibling());
        mIssuerNameElement = XMLUtils.getNextElement(mIssuerSerialElement.getFirstChild());
        mSerialNumberElement = XMLUtils.getNextElement(mIssuerNameElement.getNextSibling());

        //mX509IssuerName = RFC2253Parser.normalize(XmlUtil.getText(mIssuerNameElement));
        mX509IssuerName = LDAPDNUtil.normalize(XmlUtil.getText(mIssuerNameElement));
        mX509SerialNumber = new BigInteger(XmlUtil.getText(mSerialNumberElement));

        mURI = mElement.getAttributeNS(null, ATTR_URI);
    }

    public DigestMethod getDigestMethod()
    {
        return mCertificateDigest.getDigestMethod();
    }

    public void setDigestMethod(DigestMethod aDigestMethod)
    {
        mCertificateDigest.setDigestMethod(aDigestMethod);
    }

    public byte[] getDigestValue()
    {
        return mCertificateDigest.getDigestValue();
    }

    public void setDigestValue(byte[] aDigestValue)
    {
        mCertificateDigest.setDigestValue(aDigestValue);
    }

    public String getX509IssuerName()
    {
        return mX509IssuerName;
    }

    public void setX509IssuerName(String aX509IssuerName)
    {
        mIssuerNameElement.setTextContent(aX509IssuerName);
        mX509IssuerName = aX509IssuerName;
    }

    public BigInteger getX509SerialNumber()
    {
        return mX509SerialNumber;
    }

    public void setX509SerialNumber(BigInteger aX509SerialNumber)
    {
        mSerialNumberElement.setTextContent(aX509SerialNumber.toString());
        mX509SerialNumber = aX509SerialNumber;
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String aURI)
    {
        mElement.setAttributeNS(null, ATTR_URI, aURI);
        mURI = aURI;
    }

    public CertificateSearchCriteria toSearchCriteria(){
        CertificateSearchCriteria criteria = new CertificateSearchCriteria();
        if (mCertificateDigest!=null){
            criteria.setDigestAlg(mCertificateDigest.getDigestMethod().getAlgorithm());
            criteria.setDigestValue(mCertificateDigest.getDigestValue());
        }
        criteria.setIssuer(mX509IssuerName);
        criteria.setSerial(mX509SerialNumber);
        return criteria;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("CertId issuer: ")
                .append(mX509IssuerName)
                .append("; serial: ")
                .append(mX509SerialNumber.toString(16));
        return builder.toString();
    }

    //
    public String getLocalName()
    {
        return TAGX_CERTID;
    }
}
