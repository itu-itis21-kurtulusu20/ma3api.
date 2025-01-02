package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import com.objsys.asn1j.runtime.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.UnsupportedOperationException;

/**
 * @author ahmety
 * date: Oct 8, 2009
 */
public class AsnUtil
{

    public static byte[] encode(Asn1Type aObj, PKIEncodingType aEncoding)
        throws XMLSignatureException
    {
        try
        {
            PKIEncodingType encoding = aEncoding==null ? PKIEncodingType.DER : aEncoding;
            Asn1EncodeBuffer encodeBuffer = encoding.createEncodeBuffer();
            if (encodeBuffer instanceof Asn1BerEncodeBuffer){
                Asn1BerEncodeBuffer buffer = (Asn1BerEncodeBuffer)encodeBuffer;
                aObj.encode(buffer);
                return buffer.getMsgCopy();
            }
            else {// Asn1XerEncodeBuffer, Asn1XMLDecodeBuffer etc..{
                // todo
                throw new UnsupportedOperationException("Not yet implemented encoding type! "+aEncoding);
            }
        } catch (Exception aEx)
        {
            throw new XMLSignatureException(aEx, "errors.cantEncode", "ASN", aObj);
        }

    }
    //NOTE = DO Not use with ordinary ASN element.(See AsnIO.derOku)
    public static void decode(Asn1Type aObj, byte[] aEncoded, PKIEncodingType aEncoding)
        throws XMLSignatureException
    {
        try
        {
            PKIEncodingType encoding = (aEncoding==null) ? PKIEncodingType.DER : aEncoding;
            Asn1DecodeBuffer decodeBuffer = encoding.createDecodeBuffer(aEncoded);
            if (decodeBuffer instanceof Asn1BerDecodeBuffer){
                Asn1BerDecodeBuffer buffer = (Asn1BerDecodeBuffer)decodeBuffer;
                aObj.decode(buffer);
            }
            else {// Asn1XMLDecodeBuffer etc..{
                // todo
                throw new UnsupportedOperationException("Not yet implemented encoding type! "+aEncoding);
            }
        } catch (Exception aEx)
        {
            throw new XMLSignatureException(aEx, "errors.cantDecode", "ASN", aObj);
        }

    }

}
