package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;


/**
 * <p>Denotes URI identifying the encoding used in the original PKI data.
 *
 * <p>If the Encoding attribute is not present, then it is assumed that the PKI
 * data is ASN.1 data encoded in DER.
 *
 * <p>So far, the following URIs have been identified:
 * <ul>
 * <li>http://uri.etsi.org/01903/v1.2.2#DER for denoting that the original PKI
 * data were ASN.1 data encoded in DER.
 * <li>http://uri.etsi.org/01903/v1.2.2#BER for denoting that the original PKI
 * data were ASN.1 data encoded in BER.
 * <li>http://uri.etsi.org/01903/v1.2.2#CER for denoting that the original PKI
 * data were ASN.1 data encoded in CER.
 * <li>http://uri.etsi.org/01903/v1.2.2#PER for denoting that the original PKI
 * data were ASN.1 data encoded in PER.
 * <li>http://uri.etsi.org/01903/v1.2.2#XER for denoting that the original PKI
 * data were ASN.1 data encoded in XER.
 * </ul>
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData
 * @author ahmety
 * date: Sep 14, 2009
 */
public enum PKIEncodingType
{
    DER                 ("http://uri.etsi.org/01903/v1.2.2#DER", Asn1DerEncodeBuffer.class, Asn1DerDecodeBuffer.class),
    BER                 ("http://uri.etsi.org/01903/v1.2.2#BER", Asn1BerEncodeBuffer.class, Asn1BerDecodeBuffer.class),
    NOT_SUPPORTED_CER   ("http://uri.etsi.org/01903/v1.2.2#CER", null, null),
    NOT_SUPPORTED_PER   ("http://uri.etsi.org/01903/v1.2.2#PER", null, null),
    XER                 ("http://uri.etsi.org/01903/v1.2.2#XER", Asn1XerEncodeBuffer.class, null /*Asn1XerDecodeBuffer.class*/) ;

    private String mURI;
    private Class<? extends Asn1EncodeBuffer> mEncodeBufferClass;
    private Class<? extends Asn1DecodeBuffer> mDecodeBufferClass;

    PKIEncodingType(String aURI, Class<? extends Asn1EncodeBuffer> aEncodeBufferClass, Class<? extends Asn1DecodeBuffer> aDecodeBufferClass)
    {
        mURI = aURI;
        mEncodeBufferClass = aEncodeBufferClass;
        mDecodeBufferClass = aDecodeBufferClass;
    }

    public String getURI()
    {
        return mURI;
    }

    public static PKIEncodingType resolve(String aURI)
            throws XMLSignatureException
    {
        if (aURI==null || aURI.equals("") || aURI.trim().length()<1){
            return DER;
        }
        for (PKIEncodingType encoding :  values()) {
            if (encoding.getURI().equals(aURI))
                return encoding;
        }

        throw new XMLSignatureException("unknown.encoding ", aURI);
    }

    public Asn1EncodeBuffer createEncodeBuffer() throws XMLSignatureException {
        if (mEncodeBufferClass!=null){
            try {
                return mEncodeBufferClass.newInstance();
            } catch (Exception x){
                throw new XMLSignatureException(x, "core.cantCreateBuffer", I18n.translate("encode"), getURI());
            }
        }
        else {
            throw new XMLSignatureException("core.cantCreateBuffer", I18n.translate("encode"), getURI());
        }
    }

    public Asn1DecodeBuffer createDecodeBuffer(byte[] aBytes) throws XMLSignatureException {
        if (mDecodeBufferClass!=null){
            try {
                return mDecodeBufferClass.getConstructor(byte[].class).newInstance(aBytes);
            } catch (Exception x){
                throw new XMLSignatureException(x, "core.cantCreateBuffer", I18n.translate("decode"), getURI());
            }
        }
        else {
            throw new XMLSignatureException("core.cantCreateBuffer", I18n.translate("decode"), getURI());
        }
    }

}
