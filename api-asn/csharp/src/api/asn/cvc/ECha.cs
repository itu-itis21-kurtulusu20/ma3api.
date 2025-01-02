using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ECha : BaseASNWrapper<Cha>
    {
        public ECha(byte[] aEncoded)
            : base(aEncoded, new Cha())
        {
        }

        public ECha()
            : base(new Cha())
        {
        }

        public void setCha(byte[] aChaValue)
        {
            //        if (aChaValue.length != 7)
            //            throw new ESYAException("Cha field must be 7 bytes. Found: " + aChaValue.length);
            getObject().mValue = aChaValue;
        }

        public int getLength()
        {
            return getByteValues().Length;
        }

        public byte[] getByteValues()
        {
            return getObject().mValue;
        }

        public static ECha fromValue(byte[] aChaValue)
        {
            ECha cha = new ECha();
            cha.setCha(aChaValue);
            return cha;
        }

        public byte[] getTagLen()
        {
            return getTagLen(getLength());
        }

        public static byte[] getTagLen(int aLen)
        {
            Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
            encodeBuffer.EncodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 76, aLen);
            return encodeBuffer.MsgCopy;
        }
    }
}
