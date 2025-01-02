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
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.DOMDocument;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.Include;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;

import java.io.ByteArrayOutputStream;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.REFERENCE_TYPE_SIGNED_PROPS;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.TAGX_INDIVIDUALDATAOBJECTSTIMESTAMP;

/**
 * The <code>IndividualDataObjectsTimeStamp</code> element contains the
 * time-stamp computed before the signature production, over a sequence formed
 * by SOME <code>ds:Reference</code> elements within the
 * <code>ds:SignedInfo</code>. Note that this sequence cannot contain a
 * <code>ds:Reference</code> computed on the <code>SignedProperties</code>
 * element.
 *
 * <p>The <code>IndividualDataObjectsTimeStamp</code> element is a signed
 * property that qualifies the signed data object(s).
 *
 * <p>Several instances of this property can occur within the same XAdES.
 *
 * <p>Below follows the schema definition for this element.
 * <pre>
 * &lt;xsd:element name="IndividualDataObjectsTimeStamp" type="XAdESTimeStampType"/&gt;
 * </pre>
 * <p>This property uses the explicit (Include) mechanism. Generating
 * applications MUST compose the <code>Include</code> elements to refer to
 * those <code>ds:Reference</code> elements that are to be time-stamped. Their
 * corresponding referencedData attribute MUST be present and set to "true".
 *
 * <p>The digest computation input MUST be the result of processing the
 * selected <code>ds:Reference</code> within <code>ds:SignedInfo</code> as
 * follows:
 * <ol>
 * <li>Process the retrieved ds:Reference element according to the reference
 * processing model of XMLDSIG.
 * <li>If the result is a XML node set, canonicalize it. If
 * <code>ds:Canonicalization</code> is present, the algorithm indicated by this
 * element is used. If not, the standard canonicalization method specified by
 * XMLDSIG is used
 * <li>Concatenate the resulting octets to those resulting from previously
 * processed <code>ds:Reference</code> elements in ds:SignedInfo.
 * </ol>
 *
 * @author ahmety
 * date: Sep 29, 2009
 */
public class IndividualDataObjectsTimeStamp extends XAdESTimeStamp
{

    public IndividualDataObjectsTimeStamp(Context aContext)
    {
        super(aContext);
    }

    /**
     * Construct GenericTimeStamp from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public IndividualDataObjectsTimeStamp(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    public void addEncapsulatedTimeStamp(XMLSignature aSignature, DigestMethod aDMForTimeStamp, TSSettings aAyar)
            throws XMLSignatureException
    {
        try {
            DigestMethod dm = (aDMForTimeStamp!=null) ? aDMForTimeStamp : mContext.getConfig().getAlgorithmsConfig().getDigestMethod();
            byte[] bytes2TimeStamp = getContentForTimeStamp(aSignature);
            byte[] digested = KriptoUtil.digest(bytes2TimeStamp, dm);
            TSClient istemci = new TSClient();
            TimeStampResp response = istemci.timestamp(digested, aAyar).getObject() ;

            EncapsulatedTimeStamp ets = new EncapsulatedTimeStamp(mContext, aSignature, this, response);
            addEncapsulatedTimeStamp(ets);
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", "IndivualDataObjects");
        }
    }

    public void addEncapsulatedTimeStamp(XMLSignature aSignature)
            throws XMLSignatureException
    {
        TimestampConfig tsConfig = mContext.getConfig().getTimestampConfig();
        DigestMethod digestMethod = tsConfig.getDigestMethod();
        TSSettings tsSettings = tsConfig.getSettings();
        addEncapsulatedTimeStamp(aSignature, digestMethod, tsSettings);
    }

    public byte[] getContentForTimeStamp(XMLSignature aSignature)
            throws XMLSignatureException
    {
        ByteArrayOutputStream bis = new ByteArrayOutputStream();
        SignedInfo signedInfo = aSignature.getSignedInfo();

        /*
        Repeat steps for all the subsequent Include elements (in their order of
        appearance) within the time-stamp token container.
        */
        for (int j=0; j<getIncludeCount(); j++){

            Include include = getInclude(j);

            String id2seek = include.getURI().substring(1);
            Document doc = null;

            /*
            Check that the retrieved element is actually a ds:Reference element
            of the ds:SignedInfo of the qualified signature(we do that by iterating
            references, instead of using resolvers!)
            */
            for (int i=0; i<signedInfo.getReferenceCount();i++){
                Reference reference = signedInfo.getReference(i);
                if (id2seek.equals(reference.getId())){

                    /*
                    Check that that its Type attribute (if present) does not have
                    the value "http://uri.etsi.org/01903#SignedProperties".
                    */
                    String type = reference.getType();
                    if (type!=null && type.equals(REFERENCE_TYPE_SIGNED_PROPS))
                        throw new XMLSignatureException("core.model.individualDataObjectsCantReferenceQP");

                    /*
                    If the retrieved data is a ds:Reference element and the
                    referencedData attribute is set to the value "true", take
                    the result of processing the retrieved ds:Reference element
                    according to the reference processing model of XMLdSIG;
                    otherwise take the ds:Reference element itself.

                    If the resulting data is an XML node set, canonicalize it.
                    If ds:Canonicalization is present, the algorithm indicated
                    by this element is used. If not, the standard
                    canonicalization method specified by XMLDSIG is used.
                    */
                    Boolean referenced = include.getReferencedData();
                    if (referenced!=null && referenced){
                        doc = reference.getTransformedDocument();
                    } else {
                        doc = new DOMDocument(reference.getElement(), reference.getURI());
                    }
                }
            }

            if (doc==null){
                throw new XMLSignatureException("errors.cantFind", id2seek);
            }

            //Concatenate the resulting bytes in an octet stream.
            try {
                bis.write(doc.getBytes());
            } catch (Exception x){
                throw new XMLSignatureException(x, "errors.cantDigest", id2seek);
            }
        }

        return bis.toByteArray();

    }

    public TimestampType getType() {
        return TimestampType.CONTENT_TIMESTAMP;
    }

    public String getLocalName()
    {
        return TAGX_INDIVIDUALDATAOBJECTSTIMESTAMP;
    }
}
