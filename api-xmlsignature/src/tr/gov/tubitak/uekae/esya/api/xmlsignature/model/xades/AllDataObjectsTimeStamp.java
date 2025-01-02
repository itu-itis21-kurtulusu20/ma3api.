package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.config.TimestampConfig;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;

import java.io.ByteArrayOutputStream;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.REFERENCE_TYPE_SIGNED_PROPS;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAGX_ALLDATAOBJECTSTIMESTAMP;

/**
 * <p>The <code>AllDataObjectsTimeStamp</code> element contains the time-stamp
 * computed before the signature production, over the sequence formed by ALL
 * the <code>ds:Reference</code> elements within the <code>ds:SignedInfo</code>
 * referencing whatever the signer wants to sign except the
 * <code>SignedProperties</code> element.
 *
 * <p>The <code>AllDataObjectsTimeStamp</code> element is a signed property.
 * Several instances of this property from different TSAs can occur within the
 * same XAdES.
 *
 * <p>Below follows the schema definition for this element.
 * <pre>
 * &lt;xsd:element name="AllDataObjectsTimeStamp" type="XAdESTimeStampType"/&gt;
 * </pre>
 *
 * <p>This property uses the Implicit mechanism. The input to the computation
 * of the digest value MUST be the result of processing the aforementioned
 * suitable <code>ds:Reference</code> elements in their order of appearance
 * within <code>ds:SignedInfo</code> as follows:
 * <ol>
 * <li>Process the retrieved <code>ds:Reference</code> element according to
 * the reference processing model of XMLDSIG.</li>
 * <li>If the result is a XML node set, canonicalize it. If
 * <code>ds:Canonicalization</code> is present, the algorithm indicated by this
 * element is used. If not, the standard canonicalization method specified by
 * XMLDSIG is used.</li>
 * <li>Concatenate the resulting octets to those resulting from previously
 * processed <code>ds:Reference</code> elements in <code>ds:SignedInfo</code>.</li>
 * </ol>
 *
 * @author ahmety
 * date: Sep 29, 2009
 */
public class AllDataObjectsTimeStamp extends XAdESTimeStamp
{

    public AllDataObjectsTimeStamp(Context aContext, XMLSignature aSignature)
            throws XMLSignatureException
    {
        this(aContext, aSignature,
             aContext.getConfig().getTimestampConfig().getDigestMethod(),
             aContext.getConfig().getTimestampConfig().getSettings());
    }

    public AllDataObjectsTimeStamp(Context aContext, XMLSignature aSignature, DigestMethod aDMForTimestamp, TSSettings aAyar)
            throws XMLSignatureException
    {
        super(aContext);
        addLineBreak();

        try {
            TimestampConfig tsConfig = mContext.getConfig().getTimestampConfig();
            DigestMethod dm = aDMForTimestamp!=null ? aDMForTimestamp : tsConfig.getDigestMethod();
            byte[] bytes2TimeStamp = getContentForTimeStamp(aSignature);
            byte[] digested = KriptoUtil.digest(bytes2TimeStamp, dm);

            TSSettings tsSettings = aAyar!=null? aAyar : tsConfig.getSettings();

            TSClient istemci = new TSClient();
            TimeStampResp response = istemci.timestamp(digested, tsSettings).getObject();

            EncapsulatedTimeStamp ets = new EncapsulatedTimeStamp(mContext, aSignature, this, response);
            addEncapsulatedTimeStamp(ets);
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", "AllDataObjects");
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
    public AllDataObjectsTimeStamp(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    public byte[] getContentForTimeStamp(XMLSignature aSignature)
            throws XMLSignatureException
    {
        SignedInfo signedInfo = aSignature.getSignedInfo();
        ByteArrayOutputStream bis = new ByteArrayOutputStream();
        /*
        Repeat for all the subsequent ds:Reference elements (in their order of
        appearance) within ds:SignedInfo if and only if Type attribute has not
        the value "http://uri.etsi.org/01903#SignedProperties".
        */
        for (int i=0; i<signedInfo.getReferenceCount(); i++){
            Reference ref = signedInfo.getReference(i);

            //dont process SignedProperties
            if ((ref.getType()==null) || (!ref.getType().equals(REFERENCE_TYPE_SIGNED_PROPS))){
                /*
                Process it according to the reference processing model of XMLDSIG.

                If the result is a node-set, canonicalize it using the algorithm
                indicated in CanonicalizationMethod element of the property, if
                present. If not, the standard canonicalization method as specified
                by XMLDSIG will be used.
                */
                Document doc = ref.getTransformedDocument();

                //Concatenate the resulting bytes in an octet stream.
                try {
                    bis.write(doc.getBytes());
                } catch (Exception x){
                    throw new XMLSignatureException(x, "errors.cantDigest", "AllDataObjectTimeStamp");
                }
            }
        }
        return bis.toByteArray();
    }

    public TimestampType getType() {
        return TimestampType.CONTENT_TIMESTAMP;
    }

    public String getLocalName()
    {
        return TAGX_ALLDATAOBJECTSTIMESTAMP;
    }
}
