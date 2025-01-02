package tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.common.bundle.I18nSettings;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author ahmety
 * date: Jun 15, 2009
 */
public class I18n
{
    static ResourceBundle mBundle;
    static boolean missingResources = false;

    static Logger logger = LoggerFactory.getLogger(I18n.class);
    static Locale mLocale;

    static {
        try {
            mLocale = I18nSettings.getLocale();
        }
        catch (Exception x){
            logger.warn("Error configuring locale from settings ", x);
        }
        I18nSettings.addLocaleSetListener(new I18nSettings.LocaleChangeListener(){
            public void localeChanged(Locale aNewLocale)
            {
                mLocale = aNewLocale;
                init();
            }
        });
    }

    private static final String KAYNAK = "tr/gov/tubitak/uekae/esya/api/xmlsignature/resource/xmlimza";


    private static synchronized void init() {
        try {
            mBundle = ResourceBundle.getBundle(KAYNAK, mLocale);
        } catch (Exception x){
            missingResources = true;
            logger.error("Cant initialize locale successfully! ", x);
            mBundle = ResourceBundle.getBundle(KAYNAK);
        }
    }

    public static String translate(String aMessageId, Object... aArgs){
        if (mBundle==null && (!missingResources)){
            init();
        }
        try {
            if(mBundle == null)
             throw new ESYAException("mBundle should not be null");

            return MessageFormat.format(mBundle.getString(aMessageId), aArgs);

        } catch (Exception e){
            logger.warn("No message with ID '" + aMessageId + "' found in resource bundle '" + KAYNAK + "'", e);
            return aMessageId;
        }

    }

    public static void main(String[] args)
    {
        System.out.println(translate("unknown.algorithm","a"));
    }

}
