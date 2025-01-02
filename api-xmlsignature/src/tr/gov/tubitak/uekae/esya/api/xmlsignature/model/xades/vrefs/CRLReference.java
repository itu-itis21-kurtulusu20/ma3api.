package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CRLSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DigestAlgAndValue;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * CrlRef element references one CRL. Each reference contains:
 * <ul>
 * <li>the digest of the entire DER encoded CRL (<code>DigestAlgAndValue</code>
 * element);
 * <li>a set of data (<code>CRLIdentifier</code> element) including the issuer
 * (<code>Issuer</code> element), the time when the CRL was issued
 * (<code>IssueTime</code> element) and optionally the number of the CRL
 * (<code>Number</code> element).
 * <code>CRLIdentifier</code> element contents MUST follow the rules
 * established by XMLDSIG [3] in its clause 4.4.4 for strings representing
 * Distinguished Names. In addition, this element can be dropped if the CRL
 * could be inferred from other information. Its URI attribute could serve to
 * indicate where the identified CRL is archived.
 * </ul>
 *
 * <p>NOTE: The <code>number</code> element is an optional hint helping
 * applications to get the CRL whose digest matches the value present in the
 * reference.
 *
 * <p>Below follows the schema dsefinition:
 * <pre>
 * &lt;xsd:complexType name="CRLRefType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="DigestAlgAndValue" type="DigestAlgAndValueType"/&gt;
 *     &lt;xsd:element name="CRLIdentifier" type="CRLIdentifierType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Nov 10, 2009
 */
public class CRLReference extends XAdESBaseElement
{
    private DigestAlgAndValue mDigestAlgAndValue;
    private CRLIdentifier mCRLIdentifier;
    private String mURI;

    public CRLReference(Context aContext,
                        DigestMethod aDigestMethod,
                        byte[] aDigestValue,
                        CRLIdentifier aCRLIdentifier)
            throws XMLSignatureException
    {
        this(aContext, aDigestMethod);

        if (aDigestValue==null){
            throw new XMLSignatureException("errors.null", "CrlReference.digestValue");
        }

        mDigestAlgAndValue.setDigestValue(aDigestValue);

        if (aCRLIdentifier!=null){
            mElement.appendChild(aCRLIdentifier.getElement());
            addLineBreak();
        }

        mCRLIdentifier = aCRLIdentifier;
    }

    public CRLReference(Context aContext,
                        ECRL aCRL,
                        DigestMethod aDigestMethod,
                        String aURI)
            throws XMLSignatureException
    {
        this(aContext, aDigestMethod);

        if (aCRL==null){
            throw new XMLSignatureException("errors.null", I18n.translate("CRL"));
        }

        mDigestAlgAndValue.setDigestValue(KriptoUtil.digest(aCRL.getEncoded(),
                                                            mDigestAlgAndValue.getDigestMethod()));

        mCRLIdentifier =
                new CRLIdentifier(mContext,
                                  LDAPDNUtil.normalize(aCRL.getIssuer().stringValue()),
                                  XmlUtil.createDate(aCRL.getThisUpdate()),
                                  aCRL.getCRLNumber(),
                                  aURI);
        mElement.appendChild(mCRLIdentifier.getElement());
        addLineBreak();
    }

    private CRLReference(Context aContext, DigestMethod aDigestMethod){
        super(aContext);
        addLineBreak();

        DigestMethod digestMethod = (aDigestMethod==null) ? mContext.getConfig().getAlgorithmsConfig().getDigestMethod() : aDigestMethod;
        mDigestAlgAndValue = new DigestAlgAndValue(mContext);
        mDigestAlgAndValue.setDigestMethod(digestMethod);

        mElement.appendChild(mDigestAlgAndValue.getElement());
        addLineBreak();
    }

    /**
     * Construct CRLReference from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public CRLReference(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element davElement = selectChildElement(NS_XADES_1_3_2, TAGX_DIGESTALGANDVALUE);
        mDigestAlgAndValue = new DigestAlgAndValue(davElement, mContext);

        Element idElement = selectChildElement(NS_XADES_1_3_2, TAGX_CRLIDENTIFIER);
        if (idElement!=null){
            mCRLIdentifier = new CRLIdentifier(idElement, mContext);
        }
    }

    public DigestMethod getDigestMethod()
    {
        return mDigestAlgAndValue.getDigestMethod();
    }

    public byte[] getDigestValue()
    {
        return mDigestAlgAndValue.getDigestValue();
    }

    public CRLIdentifier getCRLIdentifier()
    {
        return mCRLIdentifier;
    }

    public CRLSearchCriteria toSearchCriteria(){
        return new CRLSearchCriteria(LDAPDNUtil.normalize(mCRLIdentifier.getIssuer()), 
                                     mCRLIdentifier.getIssueTime().toGregorianCalendar().getTime(),
                                     mCRLIdentifier.getNumber(),
                                     mDigestAlgAndValue.getDigestMethod().getAlgorithm(),
                                     mDigestAlgAndValue.getDigestValue());
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(mCRLIdentifier.toString());
        builder.append(mDigestAlgAndValue.toString());
        return builder.toString();    
    }

    public String getLocalName()
    {
        return TAGX_CRLREF;
    }

}
