package tr.gov.tubitak.uekae.esya.api.common.bundle;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author ayetgin
 */
public class I18nSettings
{

    public static List SUPPORTED_LANGUAGE_CODES = Arrays.asList(new String [] {"tr", "en"});
    static List<LocaleChangeListener> msListeners = new ArrayList<LocaleChangeListener>();
    static Locale msLocale;

    static {
        if(SUPPORTED_LANGUAGE_CODES.contains(Locale.getDefault().getLanguage()))
            msLocale = Locale.getDefault();
        else
            msLocale = new Locale("en", "US");

    }

    public static void addLocaleSetListener(LocaleChangeListener aListener){
        if (aListener!=null)
            msListeners.add(aListener);
        else
            throw new ESYARuntimeException("Cant add null as listener! ");
    }

    public static void setLocale(Locale aLocale)
    {
        if (aLocale!=null){
            msLocale = aLocale;
            for (LocaleChangeListener listener : msListeners) {
                listener.localeChanged(msLocale);
            }
        }
    }

    public static Locale getLocale()
    {
        return msLocale;
    }


    public static interface LocaleChangeListener
    {
        void localeChanged(Locale aNewLocale);
    }

}
