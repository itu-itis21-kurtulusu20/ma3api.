package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;
import org.w3c.dom.Element;

/**
 * An important property for long standing signatures is that a signature,
 * having been found once to be valid, shall continue to be so months or years
 * later.
 *
 * <p>A signer, verifier or both MAY be required to provide on request, proof
 * that a digital signature was created or verified during the validity period
 * of all the certificates that make up the certificate path. In this case, the
 * signer, verifier or both will also be required to provide proof that all the
 * user and CA certificates used were not revoked when the signature was
 * created or verified.
 *
 * <p>It would be quite unacceptable to consider a signature as invalid even if
 * the keys or certificates were only compromised later. Thus there is a need to
 * be able to demonstrate that the signature key was valid around the time that
 * the signature was created to provide long term evidence of the validity of a
 * signature. Time-stamping by a Time-Stamping Authority (TSA) can provide such
 * evidence.
 *
 * <p>Time-stamping an electronic signature before the revocation of the
 * signer's private key and before the end of the validity of the certificate
 * provides evidence that the signature has been created while the certificate
 * was valid and before it was revoked.
 *
 * <p>If a recipient wants to keep the result of the validation of an
 * electronic signature valid, he will have to ensure that he has obtained a
 * valid time-stamp for it, before that key (and any key involved in the
 * validation) is revoked. The sooner the time-stamp is obtained after the
 * signing time, the better.
 *
 * <p>It is important to note that signatures MAY be generated "off-line" and
 * time-stamped at a later time by anyone, for example by the signer or any
 * recipient interested in the signature. The time-stamp can thus be provided by
 * the signer together with the signed data object, or obtained by the recipient
 * following receipt of the signed data object.
 *
 * <p>The validation mandated by the signature policy can specify a maximum
 * acceptable time difference which is allowed between the time indicated in
 * the <code>SigningTime</code> element and the time indicated by the
 * <code>SignatureTimeStamp</code> element. If this delay is exceeded then the
 * electronic signature shall be considered as invalid.
 *
 * <p>The <code>SignatureTimeStamp</code> encapsulates the time-stamp over the
 * <code>ds:SignatureValue</code> element.
 *
 * <p>This property uses the implicit mechanism as the time-stamped data object
 * is always the same. For building the input to the digest computation,
 * applications MUST:
 * <ol>
 * <li>Take the <code>ds:SignatureValue</code> element and its contents.
 * <li>If the <code>ds:Canonicalization</code> element is present canonicalize
 * it using the indicated algorithm. If not, use the standard canonicalization
 * method specified by XMLDSIG.
 * </ol>
 *
 * <p>The <code>SignatureTimeStamp</code> element is an unsigned property
 * qualifying the signature. A XAdES-T form MAY contain several
 * <code>SignatureTimeSamp</code> elements, obtained from different TSAs.
 *
 * <p>Below follows the schema definition for this element.
 * <pre>
 * &lt;xsd:element name="SignatureTimeStamp" type="XAdESTimeStampType"/&gt;
 * </pre>
 *
 * @author ahmety
 * date: Nov 4, 2009
 */
public class SignatureTimeStamp extends XAdESTimeStamp implements UnsignedSignaturePropertyElement
{

    public SignatureTimeStamp(Context aContext, XMLSignature aSignature, DigestMethod aDMForTimestamp, TSSettings aAyar)
            throws XMLSignatureException
    {
        this(aContext, aSignature, null, aDMForTimestamp, aAyar);
    }

    public SignatureTimeStamp(Context aContext, XMLSignature aSignature, C14nMethod c14nMethod, DigestMethod aDMForTimestamp, TSSettings aAyar)
            throws XMLSignatureException
    {
        super(aContext);
        addLineBreak();

        if (aSignature.getSignatureValue()==null){
            throw new XMLSignatureException("core.timestamp.cantAddBeforeSignatureValue");
        }
        try {
            setCanonicalizationMethod(c14nMethod);
            DigestMethod dm = aDMForTimestamp!=null ? aDMForTimestamp : mContext.getConfig().getTimestampConfig().getDigestMethod();
            byte[] bytes2TimeStamp = getContentForTimeStamp(aSignature);
            byte[] digested = KriptoUtil.digest(bytes2TimeStamp, dm);
            TSClient istemci = new TSClient();
            TimeStampResp response = istemci.timestamp(digested, aAyar).getObject() ;

            EncapsulatedTimeStamp ets = new EncapsulatedTimeStamp(mContext, aSignature, this, response);
            addEncapsulatedTimeStamp(ets);
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", "SignatureTimeStamp");
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
    public SignatureTimeStamp(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    public byte[] getContentForTimeStamp(XMLSignature aSignature) throws XMLSignatureException
    {
        /*
        This property uses the implicit mechanism as the time-stamped data object
        is always the same. For building the input to the digest computation,
        applications MUST:

        1. Take the <code>ds:SignatureValue</code> element and its contents.

        2. If the <code>ds:Canonicalization</code> element is present canonicalize
            it using the indicated algorithm. If not, use the standard
            canonicalization method specified by XMLDSIG.
        */
        Element e = aSignature.getSignatureValueElement();

        return XmlUtil.outputDOM(e, getCanonicalizationMethod());
    }

    public TimestampType getType() {
        return TimestampType.SIGNATURE_TIMESTAMP;
    }

    public String getLocalName()
    {
        return Constants.TAGX_SIGNATURETIMESTAMP;
    }
}
