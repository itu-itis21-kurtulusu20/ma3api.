using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ECpi : BaseASNWrapper<Cpi>
    {
        public ECpi(byte[] aEncoded)
            : base(aEncoded, new Cpi())
        {
        }

        public ECpi(byte aCpiValue)
            : base(new Cpi())
        {
            setCpi(aCpiValue);
        }

        public void setCpi(byte aCpiValue)
        {
            getObject().mValue = new byte[] { aCpiValue };
        }

        public int getLength()
        {
            return getByteValues().Length;
        }

        public byte[] getByteValues()
        {
            return getObject().mValue;
        }

        public byte[] getTagLen()
        {
            return getTagLen(getLength());
        }

        public static byte[] getTagLen(int aLen)
        {
            Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
            encodeBuffer.EncodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 41, aLen);
            return encodeBuffer.MsgCopy;
        }
    }
}
