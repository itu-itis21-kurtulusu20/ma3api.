using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.asn.cvc;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ECxd : BaseASNWrapper<Cxd>
    {
        public ECxd(byte[] aEncoded)
            : base(aEncoded, new Cxd())
        {
        }

        public ECxd()
            : base(new Cxd())
        {
        }

        public ECxd(DateTime? aEndDate)
            : base(new Cxd())
        {
            //byte[] cxd = UtilBCD.date(aEndDate.get(Calendar.YEAR), aEndDate.get(Calendar.MONTH), aEndDate.get(Calendar.DAY_OF_MONTH));
            byte[] cxd = UtilBCD.date(aEndDate.Value.Year, aEndDate.Value.Month, aEndDate.Value.Day);
            setCxd(cxd);
        }

        public ECxd(int aYear, int aMonth, int aDay)
            : base(new Cxd())
        {
            byte[] cxd = UtilBCD.date(aYear, aMonth, aDay);
            setCxd(cxd);
        }

        public void setCxd(byte[] aCxdValue)
        {
            getObject().mValue = aCxdValue;
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
            encodeBuffer.EncodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 36, aLen);
            return encodeBuffer.MsgCopy;
        }

        public byte[] getTagLen()
        {
            return getTagLen(getLength());
        }

        public static ECxd fromValue(byte[] aCxdValue)
        {
            ECxd cxd = new ECxd();
            cxd.setCxd(aCxdValue);
            return cxd;
        }
    }
}
