package tr.gov.tubitak.uekae.esya.api.cmssignature.bundle;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.E_KEYS;
import tr.gov.tubitak.uekae.esya.api.common.bundle.I18nSettings;

public class CMSSignatureI18n 
{
	private static String BUNDLE_PATH = "tr.gov.tubitak.uekae.esya.api.cmssignature.bundle.CmsSignatureBundle";

	private static java.util.ResourceBundle msBundle;

	public static String getMsg(Enum<E_KEYS> aKey,String... arglar)
	{
		MessageFormat formatter = new MessageFormat(msBundle.getString(aKey.name()));
		return(formatter.format(arglar));
	}

	static 
	{
		msBundle = ResourceBundle.getBundle(BUNDLE_PATH, I18nSettings.getLocale());

		I18nSettings.addLocaleSetListener(new I18nSettings.LocaleChangeListener()
		{
			public void localeChanged(Locale aNewLocale)
			{
				msBundle = ResourceBundle.getBundle(BUNDLE_PATH, aNewLocale);
			}
		});
	}
}
