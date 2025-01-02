package tr.gov.tubitak.uekae.esya.asn.util;

import com.objsys.asn1j.runtime.Asn1Exception;
import com.objsys.asn1j.runtime.Asn1GeneralizedTime;
import com.objsys.asn1j.runtime.Asn1UTCTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.asn.x509.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
/**
 * ASN time yapılarıyla ilgili metodları içerir.
 * @author isilh
 *
 */
public class UtilTime {

    protected static Logger logger = LoggerFactory.getLogger(UtilTime.class);

    public static final GregorianCalendar RFC3280_TRESHOLDDATE;

    static {
        RFC3280_TRESHOLDDATE = new GregorianCalendar(new SimpleTimeZone(0, "TUBITAK UEKAE GMT"));
        RFC3280_TRESHOLDDATE.setTimeInMillis(2524608000000L);
    }


    public static Date timeToDate(Time aTime)
    {
        Calendar c = timeToCalendar(aTime);
        if (c!=null)
            return c.getTime();
        return null;
    }

    public static Calendar timeToCalendar(Time aTime)
    {
        if ((aTime == null) || (aTime.getElement() == null))
            return null;

        // ASN time işlemlerinin paralel yapılmasında hata alınıyordu o yüzden kontrol eklendi.
        // Hata toplu mobil imza atma sırasında farkedildi.
        synchronized (aTime) {
            try {
                switch (aTime.getChoiceID()) {
                    case Time._UTCTIME:
                        Calendar c = ((Asn1UTCTime) aTime.getElement()).getTime();
                        int year = c.get(Calendar.YEAR);
                        if ((year % 100) >= 50) {
                            Calendar copy = (Calendar) c.clone();
                            copy.set(Calendar.YEAR, year - 100);
                            return copy;
                        }

                        return c;

                    case Time._GENERALTIME:
                        return ((Asn1GeneralizedTime) aTime.getElement()).getTime();

                    default:
                        return null;
                }
            } catch (Asn1Exception aEx) {
                logger.error("Error in UtilTime", aEx);
                return null;
            }
        }
    }

    public static Time calendarToTimeFor3280(Calendar aaCal) {
        Time time = new Time();


        GregorianCalendar aCal = new GregorianCalendar(new SimpleTimeZone(0, "TUBITAK UEKAE GMT"));
        aCal.setTimeInMillis(aaCal.getTimeInMillis());

        /** RFC 3280'e gore 2050'den onceki tarihler icin UTCTime kullanilmali... */
        if (RFC3280_TRESHOLDDATE.before(aCal)) {
            Asn1GeneralizedTime generalTime = new Asn1GeneralizedTime();
            try {
                generalTime.setTime(aCal);
                generalTime.setFraction("");
            } catch (Asn1Exception ex) {
                throw new ESYARuntimeException("Time olustururken tarih hesaplanamadi.", ex);
            }
            time.set_generalTime(generalTime);
        } else {
            Asn1UTCTime utcTime = new Asn1UTCTime();
            try {
                utcTime.setTime(aCal);
            } catch (Asn1Exception ex) {
                throw new ESYARuntimeException("Time olustururken tarih hesaplanamadi.", ex);
            }
            time.set_utcTime(utcTime);
        }

        return time;

    }    
}
