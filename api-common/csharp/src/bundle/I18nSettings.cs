using System;
using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;

namespace tr.gov.tubitak.uekae.esya.api.common.bundle
{
    public static class I18nSettings
    {
        private static CultureInfo _msLocale;
        public static List<String> SUPPORTED_LANGUAGE_CODES = new String[] { "tr-TR", "en-US" }.ToList();

        static I18nSettings()
        {
            if (SUPPORTED_LANGUAGE_CODES.Contains(CultureInfo.CurrentCulture.Name))
                _msLocale = CultureInfo.CurrentCulture;
            else
                _msLocale = new CultureInfo("en-US");
        }

        public static void setLocale(CultureInfo aLocale)
        {
            _msLocale = aLocale;
        }

        public static CultureInfo getLocale()
        {
            return _msLocale;
        }

    }
}
