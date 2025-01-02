package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 *
 * <h3>Not distributed case</h3>
 *
 * <p>When <code>RefsOnlyTimeStamp</code> and all the unsigned properties covered
 * by its time-stamp token have the same parent, this property uses the
 * Implicit mechanism. The input to the computation of the digest value MUST be
 * the result of taking those of the unsigned properties listed below that
 * appear before the <code>RefsOnlyTimeStamp</code> in their order of appearance
 * within the <code>UnsignedSignatureProperties</code> element, canonicalize
 * each one and concatenate the resulting octet streams:
 * <ul>
 * <li>The CompleteCertificateRefs element.
 * <li>The CompleteRevocationRefs element.
 * <li>The AttributeCertificateRefs element if this property is present.
 * <li>The AttributeRevocationRefs element if this property is present.
 * </ul>
 * <p>Below follows the list of data objects that contribute to the digest
 * computation:
 * ([CompleteCertificateRefs, CompleteRevocationRefs,
 *   AttributeCertificateRefs?, AttributeRevocationRefs?]).
 *
 * <h3>Distributed case</h3>
 *
 * <p>When RefsOnlyTimeStamp and some of the unsigned properties covered by its
 * time-stamp token DO NOT have the same parent, applications MUST build this
 * property generating one <code>Include</code> element per each unsigned
 * property that must be covered by the time-stamp token in the order they
 * appear listed below:
 * <ul>
 * <li>The CompleteCertificateRefs element.
 * <li>The CompleteRevocationRefs element.
 * <li>The AttributeCertificateRefs element if this property is present.
 * <li>The AttributeRevocationRefs element if this property is present.
 * </ul>
 *
 * <p>Applications MUST build URI attributes following the rules stated in
 * clause 7.1.4.3.1.
 *
 * <p>Generating applications MUST build digest computation input as indicated
 * below:
 * <ol>
 * <li>Initialize the final octet stream as an empty octet stream.
 * <li>Take each unsigned property listed above in the order they have been
 * listed above (this order MUST be the same as the order the
 * <code>Include</code> elements appear in the property). For each one extract
 * comment nodes, canonicalize and concatenate the resulting octet stream to
 * the final octet stream.
 * </ol>
 *
 * <p>Validating applications MUST build digest computation input as indicated
 * below:
 * <ol>
 * <li>Initialize the final octet stream as an empty octet stream.
 * <li>Process in order each <code>Include</code> element present as indicated
 * in clause 7.1.4.3.1. Concatenate the resulting octet stream to the final
 * octet stream.
 * </ol>
 *
 * <p>Below follows the list -in order- of the data objects that contribute to
 * the digest computation. Superindex e means that this property is referenced
 * using explicit mechanism, i.e. that the property contains a
 * <code>Include</code> element that references it:
 *
 * ( CompleteCertificateRefs e, CompleteRevocationRefs e,
 *   AttributeCertificateRefs e?, AttributeRevocationRefs e?).
 *
 * @author ahmety
 * date: Dec 17, 2009
 */
public class RefsOnlyTimeStamp extends XAdESTimeStamp implements UnsignedSignaturePropertyElement
{

    public RefsOnlyTimeStamp(Context aContext, XMLSignature aSignature,/* C14nMethod aC14nMethod,*/ DigestMethod aDMForTimestamp, TSSettings aAyar)
            throws XMLSignatureException
    {
        super(aContext);
        addLineBreak();

        try {
            DigestMethod dm = aDMForTimestamp!=null ? aDMForTimestamp : mContext.getConfig().getAlgorithmsConfig().getDigestMethod();
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
     * Construct GenericTimeStamp from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public RefsOnlyTimeStamp(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public byte[] getContentForTimeStamp(XMLSignature aSignature) throws XMLSignatureException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /*
        elow follows the list of data objects that contribute to the digest
        computation:
        ( [CompleteCertificateRefs, CompleteRevocationRefs,
           AttributeCertificateRefs?, AttributeRevocationRefs?] ) .
         */
        try {

            UnsignedSignatureProperties usp = aSignature.getQualifyingProperties().getUnsignedProperties().getUnsignedSignatureProperties();
            byte[] bytes;

            List<UnsignedSignaturePropertyElement> elements = usp.getProperties();
            for (UnsignedSignaturePropertyElement element : elements){
                /*if (element instanceof SignatureTimeStamp){
                    SignatureTimeStamp sts = (SignatureTimeStamp)element;
                    C14nMethod c14n = sts.getCanonicalizationMethod();
                    bytes = XmlUtil.outputDOM(sts.getElement(), c14n);
                    baos.write(bytes);
                }
                else*/ if (element instanceof CompleteCertificateRefs){
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
        return baos.toByteArray();
    }

    public TimestampType getType() {
        return TimestampType.REFERENCES_TIMESTAMP;
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_REFSONLYTIMESTAMP;
    }
}
