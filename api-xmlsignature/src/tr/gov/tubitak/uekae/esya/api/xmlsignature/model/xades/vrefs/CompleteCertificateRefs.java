package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.CertID;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.List;
import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 * <code>CompleteCertificateRefs</code> element is the XML element able to carry
 * the references to the CA certificates.
 *
 * <p>This is an optional unsigned property that qualifies the signature.
 *
 * <p>There SHALL be at most one occurence of this property in the signature.
 *
 * <pre>
 * &lt;xsd:element name="CompleteCertificateRefs" type="CompleteCertificateRefsType"/&gt;
 *
 * &lt;xsd:complexType name="CompleteCertificateRefsType"&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element name="CertRefs" type="CertIDListType" /&gt;
 *     &lt;/xsd:sequence&gt;
 *     &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * <p>The <code>CertRefs</code> element contains a sequence of <code>Cert</code> 
 * elements already defined in clause 7.2.2, incorporating the digest of each
 * certificate and the issuer and serial number identifier.
 *
 * <p>Should XML time-stamp tokens based in XMLDSIG be standardized and spread,
 * this type could also serve to contain references to the certification chain
 * for any TSUs providing such time-stamp tokens. In this case, an element of
 * this type could be added as an unsigned property to the XML time-stamp token
 * using the incorporation mechanisms defined in the present document.
 *
 * @author ahmety
 * date: Nov 9, 2009
 */
public class CompleteCertificateRefs extends XAdESBaseElement implements UnsignedSignaturePropertyElement
{

    private List<CertID> mCertificateReferences = new ArrayList<CertID>(0);

    private Element mRefsElement;

    public CompleteCertificateRefs(Context aContext)
    {
        super(aContext);
        addLineBreak();
        mRefsElement = insertElement(NS_XADES_1_3_2, TAGX_CERTREFS);
        addLineBreak(mRefsElement);
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public CompleteCertificateRefs(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mRefsElement = selectChildElement(NS_XADES_1_3_2, TAGX_CERTREFS);

        Element[] elements = XmlUtil.selectNodes(mRefsElement.getFirstChild(), NS_XADES_1_3_2, TAGX_CERTID);
        for (Element certid : elements)
        {
            CertID certID = new CertID(certid, aContext);
            mCertificateReferences.add(certID);
        }
    }

    public int getCertificateReferenceCount(){
        return mCertificateReferences.size();
    }

    public CertID getCertificateReference(int aIndex){
        return mCertificateReferences.get(aIndex);
    }

    public void addCertificateReference(CertID aCertificateReference){
        mCertificateReferences.add(aCertificateReference);
        mRefsElement.appendChild(aCertificateReference.getElement());
        addLineBreak(mRefsElement);
    }
    

    public String getLocalName()
    {
        return TAGX_COMPLETECERTREFS;
    }
}
