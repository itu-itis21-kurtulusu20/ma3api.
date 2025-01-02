using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.asn.cvc;
using tr.gov.tubitak.uekae.esya.asn.util;

namespace tr.gov.tubitak.uekae.esya.api.asn.cvc
{
    public class ECar : BaseASNWrapper<Car>
    {
        public ECar(byte[] aEncoded)
            : base(aEncoded, new Car())
        {
            //super(aEncoded, new Car());
        }

        public ECar()
            : base(new Car())
        {
            //super(new Car());
        }
        //todo aYear parametresi aYear: A date and time expressed in the number of 100-nanosecond intervals that have elapsed since January 1, 0001 at 00:00:00.000 in the Gregorian calendar. 
        public ECar(String aSmName, char aServiceIndicator, char aDiscretionaryData, String aAlgorithmReference, long aYear)
            : base(new Car())
        {
            //super(new Car());
            byte[] car;

            byte serviceIndicator = (byte)(aServiceIndicator & 0xF0);
            byte discretionaryData = (byte)(aDiscretionaryData & 0x0F);
            byte logicalOR = (byte)(serviceIndicator | discretionaryData);

            DateTime? calendar = new DateTime(aYear, DateTimeKind.Utc);
            //calendar.setTime(new Date(aYear));
            //int year = calendar.get(Calendar.YEAR);
            int year = calendar.Value.Year;

            car = ByteUtil.concatAll(StringUtil.ToByteArray(aSmName), new byte[] { logicalOR }, StringUtil.ToByteArray(aAlgorithmReference), new byte[] { UtilBCD.year(year) });
            setCar(car);

        }

        public void setCar(byte[] aCarValue)
        {
            if (aCarValue.Length != 8)
                throw new ESYAException("Car field must be 8 bytes. Found: " + aCarValue.Length);
            getObject().mValue = aCarValue;
        }

        public int getLength()
        {
            return getByteValues().Length;
        }

        public byte[] getByteValues()
        {
            return getObject().mValue;
        }

        public static ECar fromValue(byte[] aCarValue)
        {
            ECar car = new ECar();
            car.setCar(aCarValue);
            return car;
        }

        public byte[] getTagLen()
        {
            return getTagLen(getLength());
        }

        public static byte[] getTagLen(int aLen)
        {
            Asn1BerEncodeBuffer encodeBuffer = new Asn1BerEncodeBuffer();
            encodeBuffer.EncodeTagAndLength(Asn1Tag.APPL, Asn1Tag.PRIM, 2, aLen);
            return encodeBuffer.MsgCopy;
        }
    }
}
