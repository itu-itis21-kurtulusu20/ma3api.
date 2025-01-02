package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponseData;
import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.signature.certval.OCSPSearchCriteria;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DigestAlgAndValue;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_XADES_1_3_2;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAGX_DIGESTALGANDVALUE;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAGX_OCSPIDENTIFIER;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAGX_OCSPREF;

/**
 * <code>OcspRef</code> element references one OCSP response. Each reference
 * contains:
 * <ul>
 * <li>a set of data (<code>OCSPIdentifier</code> element) that includes an
 * identifier of the responder and an indication of the time when the response
 * was generated. The responder may be identified by its name, using the
 * <code>Byname</code> element within <code>ResponderID</code>. It may also be
 * identified by the digest of the server's public key computed as mandated in
 * RFC 2560 [8] , using the <code>ByKey</code> element. In this case the
 * content of the <code>ByKey</code> element will be the DER value of the
 * <code>byKey</code> field defined in RFC 2560, base-64 encoded. The contents
 * of <code>ByName</code> element MUST follow the rules established by XMLDSIG
 * in its clause 4.4.4 for strings representing Distinguished Names. The
 * generation time indication appears in the <code>ProducedAt</code> element
 * and corresponds to the "ProducedAt" field of the referenced response. The
 * optional <code>URI</code> attribute could serve to indicate where the OCSP
 * response identified is archived;
 * <li>the digest computed on the DER encoded <code>OCSPResponse</code> defined
 * in RFC 2560, appearing within <code>DigestAlgAndValue</code> element.
 * Applications claiming alignment with the present document SHOULD include
 * the <code>DigestAlgAndValue</code> element within each <code>OCSPRef</code>
 * element.
 * </ul>
 *
 * <p>Below follows the schema definition:
 * <pre>
 * &lt;xsd:complexType name="OCSPRefType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="OCSPIdentifier" type="OCSPIdentifierType"/&gt;
 *     &lt;xsd:element name="DigestAlgAndValue" type="DigestAlgAndValueType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Nov 10, 2009
 */
public class OCSPReference extends XAdESBaseElement
{
    private OCSPIdentifier mOCSPIdentifier;
    private DigestAlgAndValue mDigestAlgAndValue;

    public OCSPReference(Context aContext, 
    					 DigestMethod aDigestMethod, 
    					 byte[] aDigestValue, 
    					 OCSPIdentifier aOCSPId)
    {
        super(aContext);
        addLineBreak();

        mOCSPIdentifier = aOCSPId;
        mDigestAlgAndValue.setDigestValue(aDigestValue);

        if (mOCSPIdentifier!=null){
            mElement.appendChild(mOCSPIdentifier.getElement());
            addLineBreak();
        }
        addDigestInfo(aDigestMethod, aDigestValue);
    }

    public OCSPReference(Context aContext,
                         EOCSPResponse aOcspResponse,
                         DigestMethod aDigestMethod,
                         String aURI)
            throws XMLSignatureException
    {
    	super(aContext);
        EResponseData rd = aOcspResponse.getBasicOCSPResponse().getTbsResponseData();
        ResponderID responderID  = new ResponderID(aContext, rd);

        XMLGregorianCalendar producedAt;

        try {
            Calendar time = aOcspResponse.getBasicOCSPResponse().getProducedAt();
            producedAt = XmlUtil.createDate(time);
        }catch (Exception e){
            throw new XMLSignatureException("core.ocsp.cantResolveProducedAt", e);
        }

        mOCSPIdentifier = new OCSPIdentifier(mContext, responderID, producedAt, aURI);

        mElement.appendChild(mOCSPIdentifier.getElement());
        addLineBreak();
        byte[] digest = KriptoUtil.digest(aOcspResponse.getEncoded(),aDigestMethod); 
        addDigestInfo(aDigestMethod, digest);
    }

    private void addDigestInfo(DigestMethod aDigestMethod, byte[] aDigestValue){
        mDigestAlgAndValue = new DigestAlgAndValue(mContext);
        mDigestAlgAndValue.setDigestMethod(aDigestMethod);
        mElement.appendChild(mDigestAlgAndValue.getElement());
        mDigestAlgAndValue.setDigestValue(aDigestValue);
        mElement.appendChild(mDigestAlgAndValue.getElement());
        addLineBreak();
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public OCSPReference(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        Element ocspIdElement = selectChildElement(NS_XADES_1_3_2, TAGX_OCSPIDENTIFIER);
        mOCSPIdentifier = new OCSPIdentifier(ocspIdElement, mContext);
        Element daavElement = selectChildElement(NS_XADES_1_3_2, TAGX_DIGESTALGANDVALUE);
        if (daavElement!=null){
            mDigestAlgAndValue = new DigestAlgAndValue(daavElement, mContext);
        }
    }

    public OCSPIdentifier getOCSPIdentifier()
    {
        return mOCSPIdentifier;
    }

    public DigestAlgAndValue getDigestAlgAndValue()
    {
        return mDigestAlgAndValue;
    }

    public OCSPSearchCriteria toSearchCriteria(){
        ResponderID rid = mOCSPIdentifier.getResponderID();
        OCSPSearchCriteria criteria =
                new OCSPSearchCriteria(rid.getByName(),
                                       rid.getByKey(),
                                       mDigestAlgAndValue.getDigestMethod().getAlgorithm(),
                                       mDigestAlgAndValue.getDigestValue(),
                                       mOCSPIdentifier.getProducedAt().toGregorianCalendar().getTime());
        return criteria;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(mOCSPIdentifier.toString()).append("\n");
        builder.append(mDigestAlgAndValue.getDigestMethod().getAlgorithm())
                .append(" : ")
                .append(Base64.encode(mDigestAlgAndValue.getDigestValue())).append("\n");
        return builder.toString();
    }

    public String getLocalName()
    {
        return TAGX_OCSPREF;
    }
}
