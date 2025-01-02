using System;
using System.Reflection;
using System.Resources;
using System.Runtime.CompilerServices;
using System.Globalization;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.util
{
    using Logger = log4net.ILog;
    using LogManager = log4net.LogManager;
	using I18nSettings = tr.gov.tubitak.uekae.esya.api.common.bundle.I18nSettings;

   	/// <summary>
	/// @author ahmety
	/// date: Jun 15, 2009
	/// </summary>
	public static class I18n
	{
        private static readonly ResourceManager mResources = new ResourceManager("tr.gov.tubitak.uekae.esya.api.xmlsignature.Properties.Resource", Assembly.GetExecutingAssembly()); 
		internal static readonly bool missingResources = false;
        internal static readonly Logger logger = LogManager.GetLogger(typeof(I18n));
	    internal static readonly CultureInfo mLocale;

		static I18n()
		{
			try
			{
			    mLocale = CultureInfo.CurrentCulture;
			}
			catch (Exception x)
			{
                logger.Warn("Error configuring locale from settings ", x);
			}
		}

          public static String message(String aMessage)
        {
            return (mResources.GetString(aMessage, I18nSettings.getLocale()));
        }
        public static String message(String aMessage, String[] aArgs)
        {
            //return message(String.Format(aMessage, aArgs));
            return String.Format(message(aMessage), aArgs);            
        }

		public static string translate(string aMessageId, params object[] aArgs)
		{
		    try
		    {
                if (aArgs == null || aArgs.Length == 0)
                {
                    return message(aMessageId);
                }
                else
                {
                    string[] args = new string[aArgs.Length];
                    for (int k = 0; k < aArgs.Length; k++)
                    {
                        args[k] = aArgs[k].ToString();
                    }
                    return message(aMessageId, (args));
                }

		    }
		    catch (Exception exc)
		    {
		        return aMessageId;
		    }
		    
		}
	}

}