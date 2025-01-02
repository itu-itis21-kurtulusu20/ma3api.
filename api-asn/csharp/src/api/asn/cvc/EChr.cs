using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.asn.cvc;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class EChr : BaseASNWrapper<Chr>
    {
        public EChr(byte[] aEncoded)
            : base(aEncoded, new Chr())
        {
        }

        public EChr()
            : base(new Chr())
        {
        }

        public EChr(String aChrStr)
            : base(new Chr())
        {

            byte[] chr = StringUtil.ToByteArray(aChrStr);
            if (chr.Length < 12)
            {
                byte[] paddedChr = new byte[12];
                Array.Copy(chr, 0, paddedChr, 12 - chr.Length, chr.Length);
                setChr(paddedChr);
            }
            else
            {
                setChr(chr);
            }

        }


        public void setChr(byte[] aChrValue)
        {
            if (aChrValue.Length != 12)
                throw new ESYAException("Chr field must be 12 bytes. Found: " + aChrValue.Length);
            getObject().mValue = aChrValue;
        }

        public int getLength()
        {
            return getByteValues().Length;
        }

        public byte[] getByteValues()
        {
            return getObject().mValue;
        }

        public static EChr fromValue(byte[] aChrValue)
        {
            EChr chr = new EChr();
            chr.setChr(aChrValue);
            return chr;
        }

        public byte[] getTagLen()
        {
            return getTagLen(getLength());
        }

        public static byte[] getTagLen(int aLen)
        {
            Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
            encodeBuffer.EncodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 32, aLen);
            return encodeBuffer.MsgCopy;
        }
    }
}
