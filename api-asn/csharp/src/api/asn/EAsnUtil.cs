using Com.Objsys.Asn1.Runtime;

namespace tr.gov.tubitak.uekae.esya.api.asn
{
    public class EAsnUtil
    {
        public static int decodeTagAndLengthWithCheckingTag(Asn1BerDecodeBuffer decodeBuffer, Asn1Tag expectedTag) 
        {
            Asn1Tag temp = new Asn1Tag();
            int len = decodeBuffer.DecodeTagAndLength(temp);
            if(temp.Equals(expectedTag))
                return len;
            else
                throw new Asn1TagMatchFailedException(decodeBuffer, expectedTag, temp);
        }
    }
}
