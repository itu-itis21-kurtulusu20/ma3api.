using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cvc;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ECed : BaseASNWrapper<Ced>
    {
        public ECed(byte[] aEncoded)
            : base(aEncoded, new Ced())
        {
        }

        public ECed()
            : base(new Ced())
        {
        }

        public ECed(DateTime? aStartDate)
            : base(new Ced())
        {

            //byte[] cxd = UtilBCD.date(aStartDate.get(Calendar.YEAR), aStartDate.get(Calendar.MONTH), aStartDate.get(Calendar.DAY_OF_MONTH));
            byte[] cxd = UtilBCD.date(aStartDate.Value.Year, aStartDate.Value.Month, aStartDate.Value.Day);
            setCed(cxd);
        }

        public ECed(int aYear, int aMonth, int aDay)
            : base(new Ced())
        {
            byte[] ced = UtilBCD.date(aYear, aMonth, aDay);
            setCed(ced);
        }

        public void setCed(byte[] aCedValue)
        {
            getObject().mValue = aCedValue;
        }

        public int getLength()
        {
            return getByteValues().Length;
        }

        public byte[] getByteValues()
        {
            return getObject().mValue;
        }

        public static byte[] getTagLen(int aLen)
        {
            Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
            encodeBuffer.EncodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 37, aLen);
            return encodeBuffer.MsgCopy;
        }

        public byte[] getTagLen()
        {
            return getTagLen(getLength());
        }

        public static ECed fromValue(byte[] aCedValue)
        {
            ECed ced = new ECed();
            ced.setCed(aCedValue);
            return ced;
        }
    }
}
