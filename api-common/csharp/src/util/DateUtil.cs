using System;

namespace tr.gov.tubitak.uekae.esya.api.common.src.util
{
    public class DateUtil
    {
        public static String dayMonthYear24hoursFormat = "dd/MM/yyyy HH:mm:ss";

        public static String formatDateByDayMonthYear24hours(DateTime dateTime)
        {
            return dateTime.ToString(dayMonthYear24hoursFormat);
        }
    }
}
