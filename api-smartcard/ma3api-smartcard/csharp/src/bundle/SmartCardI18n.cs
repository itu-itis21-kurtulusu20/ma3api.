using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Resources;
using System.Text;
using tr.gov.tubitak.uekae.esya.api.common.bundle;


namespace tr.gov.tubitak.uekae.esya.api.smartcard.bundle
{
    public static class SmartCardI18n
    {
        private static readonly ResourceManager mResources = new ResourceManager("tr.gov.tubitak.uekae.esya.api.smartcard.Properties.Resource", Assembly.GetExecutingAssembly());

        public static readonly String INCORRECT_PIN_FINAL_TRY = "INCORRECT_PIN_FINAL_TRY";
        public static readonly String INCORRECT_PIN = "INCORRECT_PIN";
        public static readonly String PIN_LOCKED = "PIN_LOCKED";

        public static String getMsg(String aMessage)
        {            
            return (mResources.GetString(aMessage, I18nSettings.getLocale()));
        }
        public static String getMsg(String aMessage, String[] aArgs)
        {
            return String.Format(getMsg(aMessage), aArgs);
        }
    }
}
