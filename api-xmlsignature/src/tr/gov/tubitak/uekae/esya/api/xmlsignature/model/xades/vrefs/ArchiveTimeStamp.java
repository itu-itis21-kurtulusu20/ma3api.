package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSClient;
import tr.gov.tubitak.uekae.esya.api.infra.tsclient.TSSettings;
import tr.gov.tubitak.uekae.esya.api.signature.attribute.TimestampType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Reference;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.SignedInfo;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.XMLObject;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignatureProperties;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.EncapsulatedTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.asn.pkixtsp.TimeStampResp;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.NS_NAMESPACESPEC;

/**
 * <p>Archive validation data consists of the complete validation data and the
 * complete certificate and revocation data, time-stamped together with the
 * electronic signature. The Archive validation data is necessary if the hash
 * function and the crypto algorithms that were used to create the signature are
 * no longer secure. Also, if it cannot be assumed that the hash function used
 * by the Time-Stamping Authority is secure, then nested time-stamps of archived
 * electronic signature are required.   
 *
 * <p>Nested time-stamps will also protect the verifier against key compromise
 * or cracking the algorithm on the old electronic signatures.
 *
 * <p>The process will need to be performed and iterated before the
 * cryptographic algorithms used for generating the previous time-stamp are no
 * longer secure. Archive validation data MAY thus bear multiple embedded
 * time-stamps.
 *
 * <p>The xadesv141:ArchiveTimeStamp element is an unsigned property qualifying
 * the signature. Below follows the schema definition for this element.
 *
 * <p>&lt;xsd:element name="ArchiveTimeStamp" type="XAdESTimeStampType"/&gt;
 *
 * <p>Should a CounterSignature unsigned property be time-stamped by the
 * xadesv141:ArchiveTimeStamp, any ulterior change of their contents (by
 * addition of unsigned properties if the counter-signature is a XAdES
 * signature, for instance) would make the validation of the
 * xadesv141:ArchiveTimeStamp, and in consequence of the countersigned XAdES
 * signature, fail. Implementers SHOULD, in consequence, not change the contents
 * of the CounterSignature property once it has been time-stamped by the
 * xadesv141:ArchiveTimeStamp. Implementors MAY, in these circumstances, to make
 * use of the detached counter-signature mechanism specified  (not supported!)
 *
 * <p> In addition it has to be noted that the present document allows to
 * counter-sign a previously time-stamped countersignature with another
 * CounterSignature property added to the embedding XAdES signature after the
 * time-stamp container.
 *
 * <p>Depending whether all the unsigned properties covered by the time-stamp
 * token and the xadesv141:ArchiveTimeStamp property itself have the same parent
 * or not, its contents may be different. Details are given in clauses below.
 *
 *  <p>NOTE: Readers are warned that once an xadesv141:ArchiveTimeStamp property
 * is added to the signature, any ulterior addition of a ds:Object to the
 * signature, would make the verification of such time-stamp fail.
 *
 * <p>When xadesv141:ArchiveTimeStamp and all the unsigned properties covered by
 * its time-stamp token have the same parent, this property uses the Implicit
 * mechanism for all the time-stamped data objects. The input to the computation
 * of the digest value MUST be built as follows:
 * <ol>
 * <li>1) Initialize the final octet stream as an empty octet stream.
 * <li>2) Take all the ds:Reference elements in their order of appearance within
 * ds:SignedInfo referencing whatever the signer wants to sign including the
 * SignedProperties element. Process each one as indicated below:
 * <ul>
 * <li>- Process the retrieved ds:Reference element according to the reference
 * processing model of XMLDSIG.
 * <li>- If the result is a XML node set, canonicalize it. If
 * ds:Canonicalization is present, the algorithm indicated by this element is
 * used. If not, the standard canonicalization method specified by XMLDSIG is
 * used.
 * <li>- Concatenate the resulting octets to the final octet stream.
 * </ul>
 * <li>3) Take the following XMLDSIG elements in the order they are listed below,
 * canonicalize each one and concatenate each resulting octet stream to the
 * final octet stream:
 * <ul>
 * <li>- The ds:SignedInfo element.
 * <li>- The ds:SignatureValue element.
 * <li>- The ds:KeyInfo element, if present.
 * </ul>
 * <li>4) Take the unsigned signature properties that appear before the current
 * xadesv141:ArchiveTimeStamp in the order they appear within the
 * xades:UnsignedSignatureProperties, canonicalize each one and concatenate each
 * resulting octet stream to the final octet stream. While concatenating the
 * following rules apply:
 * <ul>
 * <li>- The xades:CertificateValues property MUST be added if it is not already
 * present and the ds:KeyInfo element does not contain the full set of
 * certificates used to validate the electronic signature.
 * <li>- The xades:RevocationValues property MUST be added if it is not already
 * present and the ds:KeyInfo element does not contain the revocation
 * information that has to be shipped with the electronic signature.
 * <li>- The xades:AttrAuthoritiesCertValues property MUST be added if not already
 * present and the following conditions are true: there exist an attribute
 * certificate in the signature AND a number of certificates that have been used
 * in its validation do not appear in CertificateValues. Its content will
 * satisfy with the rules specified in clause 7.6.3.
 * <li>- The xades:AttributeRevocationValues property MUST be added if not already
 * present and there the following conditions are true: there exist an attribute
 * certificate AND some revocation data that have been used in its validation do
 * not appear in RevocationValues. Its content will satisfy with the rules
 * specified in clause 7.6.4.
 * </ul>
 * <li>5) Take all the ds:Object elements except the one containing
 * xades:QualifyingProperties element.
 * Canonicalize each one and concatenate each resulting octet stream to the
 * final octet stream. If ds:Canonicalization is present, the algorithm
 * indicated by this element is used. If not, the standard canonicalization
 * method specified by XMLDSIG is used.
 * </ol>
 * NOTE: XAdESv1.3.2 [12] specified a different strategy for concatenating
 * ds:Object elements present within the signature. Following that strategy,
 * when the last transformation of a certain ds:Reference is not a
 * canonicalization transformation and its output is an octet stream, it is, in
 * the general case, unfeasible to ascertain that this reference actually makes
 * the signature to sign a certain ds:Object. The present document overcomes
 * this situation by forcing that all the ds:Object elements other than the one
 * containing the qualifying properties, are canonicalized and concatenated,
 * which is simple although in enveloping signatures may lead to certain degree
 * of redundancy in the digest computation input. fail.
 *
 *
 * @author ahmety
 * date: Mar 28, 2011
 */
public class ArchiveTimeStamp extends XAdESTimeStamp implements UnsignedSignaturePropertyElement
{
    public ArchiveTimeStamp(Context aContext, XMLSignature aSignature, DigestMethod aDMForTimestamp, TSSettings aAyar)
            throws XMLSignatureException
    {
        super(aContext);

        String xmlnsXAdESPrefix = mContext.getConfig().getNsPrefixMap().getPrefix(getNamespace());
        mElement.setAttributeNS(NS_NAMESPACESPEC, "xmlns:" + xmlnsXAdESPrefix.intern(), getNamespace());

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
            throw new XMLSignatureException(x, "core.timestamp.cantTimestamp", Constants.TAGX_ARCHIVETIMESTAMP);
        }

    }

    public ArchiveTimeStamp(Element aElement, Context aContext) throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public byte[] getContentForTimeStamp(XMLSignature aSignature) throws XMLSignatureException
    {
        try {
            // init
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SignedInfo signedInfo = aSignature.getSignedInfo();
            // todo
            //C14nMethod c14nMethod = signedInfo.getCanonicalizationMethod();
            C14nMethod c14nMethod = getCanonicalizationMethod();

            // add all references resolved
            for (int i=0; i<signedInfo.getReferenceCount(); i++){
                Reference ref = signedInfo.getReference(i);
                Document doc = ref.getTransformedDocument();
                baos.write(doc.getBytes());
            }

            // signedInfo, signatureValue, keyInfo
            baos.write(signedInfo.getCanonicalizedBytes());
            baos.write(XmlUtil.outputDOM(aSignature.getSignatureValueElement(), c14nMethod));
            baos.write(XmlUtil.outputDOM(aSignature.getKeyInfo().getElement(), c14nMethod));


            // Take the unsigned signature properties that appear before the
            // current xadesv141:ArchiveTimeStamp in the order they appear,
            // canonicalize each one and concatenate each
            UnsignedSignatureProperties usp = aSignature.getQualifyingProperties().getUnsignedSignatureProperties();
            List<UnsignedSignaturePropertyElement> unsignedProperties = usp.getProperties();
            for (UnsignedSignaturePropertyElement unsignedProperty : unsignedProperties) {
                if (unsignedProperty.equals(this)){
                    break;
                }
                baos.write(XmlUtil.outputDOM(((BaseElement)unsignedProperty).getElement(), c14nMethod));
            }

            // Take all the ds:Object elements except the one containing
            // xades:QualifyingProperties element
            XMLObject qpObject = aSignature.getQualifyingPropertiesObject();
            for (int j=0; j<aSignature.getObjectCount(); j++){
                XMLObject object = aSignature.getObject(j);
                if (!object.equals(qpObject)){
                    baos.write(XmlUtil.outputDOM(object.getElement(), c14nMethod));
                }
            }

            //System.out.println("<< Content for archive time stamp >>\b"+new String(baos.toByteArray()));

            return baos.toByteArray();
        } catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantDigest", getLocalName());
        }
    }

    public TimestampType getType() {
        return TimestampType.ARCHIVE_TIMESTAMP_V2;
    }


    @Override
    public String getLocalName()
    {
        return Constants.TAGX_ARCHIVETIMESTAMP;  
    }

    @Override
    public String getNamespace()
    {
        return Constants.NS_XADES_1_4_1;
    }
}

