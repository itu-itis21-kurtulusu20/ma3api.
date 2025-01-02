package tr.gov.tubitak.uekae.esya.api.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static SimpleDateFormat dayMonthYear24hoursFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String formatDateByDayMonthYear24hours(Date date){
        return dayMonthYear24hoursFormatter.format(date.getTime());
    }
}
