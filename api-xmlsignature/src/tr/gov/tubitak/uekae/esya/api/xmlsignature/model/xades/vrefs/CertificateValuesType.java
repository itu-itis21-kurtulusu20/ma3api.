package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XADES_1_3_2;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAGX_ENCAPSULATEDX509CERTIFICATE;

/**
 * When dealing with long term electronic signatures, all the data used in the
 * validation (including the certificate path) MUST be conveniently archived.
 * In principle, the CertificateValues element contains the full set of
 * certificates that have been used to validate the electronic signature,
 * including the signer's certificate. However, it is not necessary to include
 * one of those certificates into this property, if the certificate is already
 * present in the ds:KeyInfo element of the signature.
 *
 * <p>If <code>CompleteCertificateRefs</code> and <code>CertificateValues</code>
 * are present, all the certificates referenced in CompleteCertificateRefs MUST
 * be present either in the ds:KeyInfo element of the signature or in the
 * <code>CertificateValues</code> property element.
 *
 * <p>The <code>CertificateValues</code> is an optional unsigned property and
 * qualifies the XML signature.
 *
 * <p>There SHALL be at most one occurence of this property in the signature.
 *
 * <p>Below follows the schema description:
 * <pre>
 * &lt;xsd:complexType name="CertificateValuesType"&gt;
 *   &lt;xsd:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *     &lt;xsd:element name="EncapsulatedX509Certificate" type="EncapsulatedPKIDataType"/&gt;
 *     &lt;xsd:element name="OtherCertificate" type="AnyType"/&gt;
 *   &lt;/xsd:choice&gt;
 *   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * <p>The <code>EncapsulatedX509Certificate</code> element is able to contain
 * the base-64 encoding of a DER-encoded X.509 certificate. The
 * <code>OtherCertificate</code> element is a placeholder for potential future
 * new formats of certificates.
 *
 * <p>Should XML time-stamp tokens based in XMLDSIG be standardized and spread,
 * this type could also serve to contain the certification chain for any TSUs
 * providing such time-stamp tokens, if these certificates are not already
 * present in the time-stamp tokens themselves as part of the TSUs' signatures.
 * In this case, an element of this type could be added as an unsigned property
 * to the XML time-stamp token using the incorporation mechanisms defined in the
 * present document.
 *
 * <p><font color="red">Note: OtherCertificate is not supported for now...
 * </font>
 *
 * @author ahmety
 * date: Jan 6, 2010
 */
public abstract class CertificateValuesType extends XAdESBaseElement
{
    private static Logger logger = LoggerFactory.getLogger(CertificateValuesType.class);
    
    // base-64 encoding of a DER-encoded X.509 certificate
    private List<EncapsulatedX509Certificate> mEncapsulatedX509Certificates = new ArrayList<EncapsulatedX509Certificate>();

    public CertificateValuesType(Context aContext)
    {
        super(aContext);
    }

    public CertificateValuesType(Context aContext, ECertificate[] aCertificates)
            throws XMLSignatureException
    {
        super(aContext);
        addLineBreak();
        for (ECertificate sertifika : aCertificates){
            EncapsulatedX509Certificate ec = new EncapsulatedX509Certificate(mContext, sertifika);
            mEncapsulatedX509Certificates.add(ec);
            mElement.appendChild(ec.getElement());
            addLineBreak();
        }
    }

    public CertificateValuesType(Context aContext, AttributeCertificate[] aCertificates)
            throws XMLSignatureException
    {
        super(aContext);
        for (AttributeCertificate certificate : aCertificates){
            EncapsulatedX509Certificate ec = new EncapsulatedX509Certificate(mContext, certificate);
            mEncapsulatedX509Certificates.add(ec);
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
    public CertificateValuesType(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        Element[] children = selectChildren(NS_XADES_1_3_2, TAGX_ENCAPSULATEDX509CERTIFICATE);

        if (children!=null)
        for (Element child : children) {
            EncapsulatedX509Certificate ec = new EncapsulatedX509Certificate(child, mContext);
            mEncapsulatedX509Certificates.add(ec);
        }
    }

    public int getCertificateCount(){
        return mEncapsulatedX509Certificates.size();
    }

    public EncapsulatedX509Certificate getEncapsulatedCertificate(int aIndex){
        return mEncapsulatedX509Certificates.get(aIndex);
    }


    public void addCertificate(ECertificate aCertificate)
            throws XMLSignatureException
    {
        EncapsulatedX509Certificate ec = new EncapsulatedX509Certificate(mContext, aCertificate);
        mEncapsulatedX509Certificates.add(ec);
        mElement.appendChild(ec.getElement());
        addLineBreak();
    }

    public ECertificate getCertificate(int aIndex)
            throws XMLSignatureException
    {
        try {
            return new ECertificate(mEncapsulatedX509Certificates.get(aIndex).getValue());
        } catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantDecode", "CertificateValues[index:"+aIndex+"]", I18n.translate("certificate"));
        }
    }

    // todo cache
    public List<ECertificate> getAllCertificates()
            throws XMLSignatureException
    {
        List<ECertificate> certs = new ArrayList<ECertificate>(getCertificateCount());
        for (int i=0; i<getCertificateCount(); i++){
            certs.add(getCertificate(i));
        }
        return certs;
    }


}
