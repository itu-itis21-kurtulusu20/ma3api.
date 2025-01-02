using System;
using Com.Objsys.Asn1.Runtime;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.asn.x509;
namespace tr.gov.tubitak.uekae.esya.asn.util
{
    public class UtilTime
    {
        //public static readonly DateTime? RFC3280_TRESHOLDDATE;

        //static UtilTime()
        //{
        //    RFC3280_TRESHOLDDATE = new DateTime(1950, 1, 0);
        //}
        public static DateTime? timeToDate(Time aTime)
        {
            if ((aTime == null) || (aTime.GetElement() == null))
                return null;

            // ASN time işlemlerinin paralel yapılmasında hata alınıyordu o yüzden kontrol eklendi.
            // Hata toplu mobil imza atma sırasında farkedildi.
            lock (aTime)
            {
                try
                {
                    Asn1Time c = null;
                    switch (aTime.ChoiceID)
                    {
                        case Time._UTCTIME:
                            /*
                             * The ITU-T working group on the X.500 directory and the ITU-T
    Q22/11 question (intelligent networks protocols) propose that an application
    should be able to sort the UTCTime dates and interpret them in
    the 1950-2049 interval, which would mean, for example, that the value
    \0105281429Z" corresponds to the 28th of May 2001.
                             */
                            c = ((Asn1UTCTime)aTime.GetElement());
                            if (c.Year % 100 >= 50)
                            {
                                c.Year -= 100;
                            }
                            return new DateTime(c.Year, c.Month, c.Day, c.Hour, c.Minute, c.Second, c.UTC ? DateTimeKind.Utc : DateTimeKind.Local).ToLocalTime();
                        case Time._GENERALTIME:
                            c = (Asn1GeneralizedTime)aTime.GetElement();
                            return new DateTime(c.Year, c.Month, c.Day, c.Hour, c.Minute, c.Second, c.UTC ? DateTimeKind.Utc : DateTimeKind.Local).ToLocalTime();

                        default:

                            return null;

                    }

                }
                catch (Asn1Exception aEx)
                {
                    //aEx.printStackTrace();
                    Console.WriteLine(aEx);
                    return null;
                }
            }
        }
        public static Time DateTimeToTimeFor3280(DateTime? aaCal)
        {
            Time time = new Time();


            //GregorianCalendar aCal = new GregorianCalendar(new SimpleTimeZone(0, "TUBITAK UEKAE GMT"));
            //aCal.setTimeInMillis(aaCal.getTimeInMillis());

            /** RFC 3280'e gore 2050'den onceki tarihler icin UTCTime kullanilmali... */
            if (aaCal.Value.ToUniversalTime().Year > 1949)
            {
                Asn1GeneralizedTime generalTime = new Asn1GeneralizedTime();
                try
                {
                    generalTime.SetTime(aaCal.Value);
                    generalTime.Fraction = "";
                }
                catch (Asn1Exception ex)
                {
                    throw new ESYARuntimeException("Time olustururken tarih hesaplanamadi.", ex);
                }
                time.Set_generalTime(generalTime);
            }
            else
            {
                Asn1UTCTime utcTime = new Asn1UTCTime();
                try
                {
                    utcTime.SetTime(aaCal.Value);
                }
                catch (Asn1Exception ex)
                {
                    throw new ESYARuntimeException("Time olustururken tarih hesaplanamadi.", ex);
                }
                time.Set_utcTime(utcTime);
            }

            return time;

        }
    }
}
