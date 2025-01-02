using System;
using System.Globalization;

namespace tr.gov.tubitak.uekae.esya.api.webservice.mssclient.tools
{
    public static class ETransIdManager
    {
        public static String GetNewTransId()
        {
            DateTime now = DateTime.Now;
            int year = now.Year;
            int month = now.Month;
            int day = now.Day;
            int hour = now.Hour;
            int minute = now.Minute;
            int second = now.Second;
            return "_" + day + "" + month + "" + year + "" + hour + "" + minute + "" + second;
        }
    }
}
