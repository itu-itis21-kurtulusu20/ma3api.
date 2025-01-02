package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.SignatureTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAGX_SIGANDREFSTIMESTAMP;

/**
 * This property contains a time-stamp token that covers the following data
 * objects: <code>ds:SignatureValue</code> element, all present
 * <code>SignatureTimeStamp</code> elements,
 * <code>CompleteCertificateRefs</code>, <code>CompleteRevocationRefs</code>,
 * and when present, <code>AttributeCertificateRefs</code> and
 * <code>AttributeRevocationRefs</code>.
 *
 * <p>Depending whether all the aforementioned time-stamped unsigned properties
 * and the <code>SigAndRefsTimeStamp</code> property itself have the same parent
 * or not, its contents may be different. Details are given in clauses below.
 *
 * <h3>Not distributed case</h3>
 *
 * When <code>SigAndRefsTimeStamp</code> and all the unsigned properties covered
 * by its time-stamp token have the same parent, this property uses the Implicit
 * mechanism. The input to the computation of the digest value MUST be the
 * result of taking in order each of the data objects listed below, canonicalize
 * each one and concatenate the resulting octet streams:
 *
 * <ol>
 * <li>The <code>ds:SignatureValue</code> element.
 * <li>Those among the following unsigned properties that appear before
 *  <code>SigAndRefsTimeStamp</code>, in their order of appearance within the
 *  <code>UnsignedSignatureProperties</code> element:
 *   <ul>
 *   <li><code>SignatureTimeStamp</code> elements.
 *   <li>The <code>CompleteCertificateRefs</code> element.
 *   <li>The <code>CompleteRevocationRefs</code> element.
 *   <li>The <code>AttributeCertificateRefs</code> element if this property is
 *      present.
 *   <li>The <code>AttributeRevocationRefs</code> element if this property is
 *      present.
 *   </ul>
 * </ol>
 *
 * <p>Below follows the list -in order- of data objects that contribute to the
 * digest computation. Elements within [] contribute in their order of
 * appearance within the <code>UnsignedSignatureProperties</code> element, not
 * in the order they are enumerated below:
 *
 * <p>(ds:SignatureValue, [SignatureTimeStamp+, CompleteCertificateRefs, 
 * CompleteRevocationRefs, AttributeCertificateRefs?,
 * AttributeRevocationRefs?]).
 *
 *
 * <h3>Distributed case</h3>
 *
 * When SigAndRefsTimeStamp and some of the unsigned properties covered by its
 * time-stamp token DO NOT have the same parent, applications MUST build this
 * property as indicated below:
 *
 * <ol>
 * <li>No <code>Include</code> element will be added for
 *  <code>ds:SignatureValue</code>. All applications MUST implicitly assume its
 * contribution to the digest input (see below in this clause).
 * <li>Generate one <code>Include</code> element per each unsigned property that
 * MUST be covered by the time-stamp token in the order they appear listed
 * below:
 *   <ul>
 *   <li>The SignatureTimeStamp elements.
 *   <li>The CompleteCertificateRefs element.
 *   <li>The CompleteRevocationRefs element.
 *   <li>The AttributeCertificateRefs element if this property is present.
 *   <li>The AttributeRevocationRefs element if this property is present.
 *   </ul>
 * </ol>
 *
 * <p>Applications MUST build URI attributes following the rules stated in
 * clause 7.1.4.3.1.
 *
 * <p>Generating applications MUST build digest computation input as indicated
 * below:
 * <ol>
 * <li>Initialize the final octet stream as an empty octet stream.
 * <li>Take the <code>ds:SignatureValue</code> element and its content.
 * Canonicalize it and put the result in the final octet stream.
 * <li>Take each unsigned property listed above in the order they have been
 * listed above(this order MUST be the same as the order the
 * <code>Include</code> elements appear in the property). For each one extract
 * comment nodes, canonicalize and concatenate the resulting octet string to
 * the final octet stream.
 * </ol>
 *
 * <p>Validating applications MUST build digest computation input as indicated
 * below:
 * <ol>
 * <li>Initialize the final octet stream to empty.
 * <li>Take the <code>ds:SignatureValue</code>. Canonicalize it and put the
 * result in the final octet stream.
 * <li>Process in order each <code>Include</code> element present as indicated
 * in clause 7.1.4.3.1. Concatenate the resulting octet strings to the final
 * octet stream.
 * </ol>
 * 
 * <p>Below follows the list of the data objects that contribute to the digest
 * computation. Super index e means that this property is referenced using
 * explicit mechanism, i.e. that the property contains an <code>Include</code>
 * element that references it:
 *
 * <p>(ds:SignatureValue, SignatureTimeStamp e+, CompleteCertificateRefs e,
 *  CompleteRevocationRefs e, AttributeCertificateRefs e?,
 * AttributeRevocationRefs e?).
 *
 * @author ahmety
 * date: Dec 17, 2009
 */
public class SigAndRefsTimeStamp extends XAdESTimeStamp implements UnsignedSignaturePropertyElement
{

    public SigAndRefsTimeStamp(Context aContext, XMLSignature aSignature,/* C14nMethod aC14nMethod,*/ DigestMethod aDMForTimestamp, TSSettings aAyar)
            throws XMLSignatureException
    {
        super(aContext);
        addLineBreak();

        try {
            DigestMethod dm = aDMForTimestamp!=null ? aDMForTimestamp : mContext.getConfig().getTimestampConfig().getDigestMethod();
            byte[] bytes2TimeStamp = getContentForTimeStamp(aSignature);
            byte[] digested = KriptoUtil.digest(bytes2TimeStamp, dm);
            TSClient istemci = new TSClient();
            TimeStampResp response = istemci.timestamp(digested, aAyar).getObject() ;

            EncapsulatedTimeStamp ets = new EncapsulatedTimeStamp(mContext, aSignature, this, response);
            addEncapsulatedTimeStamp(ets);
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", getLocalName());
        }
    }

    /**
     * Construct SigAndRefsTimeStamp from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public SigAndRefsTimeStamp(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public byte[] getContentForTimeStamp(XMLSignature aSignature) throws XMLSignatureException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /*
        Below follows the list -in order- of data objects that contribute to the
        digest computation. Elements within [] contribute in their order of
        appearance within the <code>UnsignedSignatureProperties</code> element,
        not in the order they are enumerated below:

        (ds:SignatureValue, [SignatureTimeStamp+, CompleteCertificateRefs,
         CompleteRevocationRefs, AttributeCertificateRefs?,
         AttributeRevocationRefs?]).
         */
        try {
            Element signatureValueElement = aSignature.getSignatureValueElement();
            byte[] bytes = XmlUtil.outputDOM(signatureValueElement, mCanonicalizationMethod);
            baos.write(bytes);

            UnsignedSignatureProperties usp = aSignature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties();

            List<UnsignedSignaturePropertyElement> elements = usp.getProperties();
            for (UnsignedSignaturePropertyElement element : elements){
                if (element instanceof SignatureTimeStamp){
                    SignatureTimeStamp sts = (SignatureTimeStamp)element;
                    bytes = XmlUtil.outputDOM(sts.getElement(), mCanonicalizationMethod);
                    baos.write(bytes);
                }
                else if (element instanceof CompleteCertificateRefs){
                    CompleteCertificateRefs ccr = (CompleteCertificateRefs)element;
                    bytes = XmlUtil.outputDOM(ccr.getElement(), mCanonicalizationMethod);
                    baos.write(bytes);
                }
                else if (element instanceof CompleteRevocationRefs){
                    CompleteRevocationRefs crf = (CompleteRevocationRefs)element;
                    bytes = XmlUtil.outputDOM(crf.getElement(), mCanonicalizationMethod);
                    baos.write(bytes);

                }
                else if (element instanceof AttributeCertificateRefs){
                    AttributeCertificateRefs acr = ((AttributeCertificateRefs) element);
                    bytes = XmlUtil.outputDOM(acr.getElement(), mCanonicalizationMethod);
                    baos.write(bytes);
                }
                else if (element instanceof AttributeRevocationRefs){
                    AttributeRevocationRefs arr = (AttributeRevocationRefs) element;
                    bytes = XmlUtil.outputDOM(arr.getElement(), mCanonicalizationMethod);
                    baos.write(bytes);

                }
            }
        } catch (Exception x){
            throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", getLocalName());
        }
        //System.out.println("SigAndRefs: "+new String(baos.toByteArray()));
        return baos.toByteArray();
    }

    public TimestampType getType() {
        return TimestampType.SIG_AND_REFERENCES_TIMESTAMP;
    }

    @Override
    public String getLocalName()
    {
        return TAGX_SIGANDREFSTIMESTAMP;
    }
}
