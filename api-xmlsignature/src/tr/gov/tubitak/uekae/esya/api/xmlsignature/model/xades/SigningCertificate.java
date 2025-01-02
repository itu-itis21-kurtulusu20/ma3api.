package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The SigningCertificate property is designed to prevent the simple
 * substitution of the certificate. This property contains references to
 * certificates and digest values computed on them.
 *
 * <p>The certificate used to verify the signature SHALL be identified in
 * the sequence; the signature policy MAY mandate other certificates be
 * present, that MAY include all the certificates up to the point of trust.
 * 
 * <p>This is a signed property that qualifies the signature.
 *
 * <p>At most one SigningCertificate element MAY be present in the signature.
 *
 * <p>Below follows the Schema definition.
 *
 * <pre>
 * &lt;xsd:element name="SigningCertificate" type="CertIDListType"/&gt;
 * &lt;xsd:complexType name="CertIDListType"&gt;
 *      &lt;xsd:sequence&gt;
 *        &lt;xsd:element name="Cert" type="CertIDType" maxOccurs="unbounded"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *  &lt;xsd:complexType name="CertIDType"&gt;
 *      &lt;xsd:sequence&gt;
 *        &lt;xsd:element name="CertDigest" type="DigestAlgAndValueType"/&gt;
 *        &lt;xsd:element name="IssuerSerial" type="ds:X509IssuerSerialType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *      &lt;xsd:attribute name="URI" type="xsd:anyURI" use="optional"/&gt;
 *  &lt;/xsd:complexType&gt;
 *  &lt;xsd:complexType name="DigestAlgAndValueType"&gt;
 *      &lt;xsd:sequence&gt;
 *        &lt;xsd:element ref="ds:DigestMethod"/&gt;
 *        &lt;xsd:element ref="ds:DigestValue"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jun 22, 2009
 */
public class SigningCertificate extends XAdESBaseElement
{
    private ArrayList<CertID> mCerts = new ArrayList<CertID>(1);

    public SigningCertificate(Context aBaglam)
    {
        super(aBaglam);
        addLineBreak();
    }

    /**
     *  Construct SigningCertificate from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public SigningCertificate(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        Element[] elements = XmlUtil.selectNodes(aElement.getFirstChild(), NS_XADES_1_3_2, TAGX_CERTID);
        for (Element certid : elements)
        {
            CertID certID = new CertID(certid, aContext);
            mCerts.add(certID);

        }
    }

    public int getCertIDCount(){
        return mCerts.size();
    }

    public CertID getCertID(int aIndex){
        return mCerts.get(aIndex);
    }

    public void addCertID(CertID aCertID){
        getElement().appendChild(aCertID.getElement());
        addLineBreak();
        mCerts.add(aCertID);
    }

    @SuppressWarnings("unchecked")
    public List<CertID> getCertIDListCopy(){
        return (List<CertID>)mCerts.clone();
    }

    // base element
    public String getLocalName()
    {
        return TAGX_SIGNINGCERTIFICATE;
    }
}

