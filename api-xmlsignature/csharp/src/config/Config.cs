using System;
using System.Collections.Generic;
using System.IO;
using System.Xml;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{

	using Logger = log4net.ILog;
	using Document = XmlDocument;
	using Element = XmlElement;
	using ESYARuntimeException = tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
	using SignatureType = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
	using XMLSignatureRuntimeException = tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using NamespacePrefixMap = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml.NamespacePrefixMap;
	using I18n = tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

	//using DocumentBuilder = javax.xml.parsers.DocumentBuilder;
	//using DocumentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory;

	/// <summary>
	/// @author ahmety
	/// date: Dec 4, 2009
	/// </summary>
	public class Config : BaseConfigElement
	{
		private static readonly Logger logger = log4net.LogManager.GetLogger(typeof(Config));

	    private String mConfigFilePath;
		private HttpConfig mHttpConfig;
		private ResolversConfig mResolversConfig;
		private ValidationConfig mValidationConfig;
		private TimestampConfig mTimestampConfig;
		private AlgorithmsConfig mAlgorithmsConfig;
		private Parameters mParameters;

		private NamespacePrefixMap nsPrefixMap = new NamespacePrefixMap();
       

		public Config()
		{
			loadConfig((string)null);
		}

		public Config(string aPath)
		{
			loadConfig(aPath);
		}

        public Config(Stream configStream)
        {
            loadConfig(configStream);
        }

        protected Config(XmlDocument configDocument)
        {
            loadConfig(configDocument);
        }

        // no init constructor for not reading config file
        private Config(Object dummy)
        {
            
        }

		private void init(Document aDocument)
		{
			Element = aDocument.DocumentElement;

			Element resolverConfElement = selectChildElement(Constants.NS_MA3, ConfigConstants.TAG_RESOLVERS);
			mResolversConfig = new ResolversConfig(resolverConfElement);

            Element timestampConfElement = selectChildElement(Constants.NS_MA3, ConfigConstants.TAG_TIMESTAMPSERVER);
			mTimestampConfig = new TimestampConfig(timestampConfElement);

            Element algoConfElement = selectChildElement(Constants.NS_MA3, ConfigConstants.TAG_ALGORITHMS);
			mAlgorithmsConfig = new AlgorithmsConfig(algoConfElement);

            Element httpConfElement = selectChildElement(Constants.NS_MA3, ConfigConstants.TAG_HTTP);
			mHttpConfig = new HttpConfig(httpConfElement);

            Element paramsConfElement = selectChildElement(Constants.NS_MA3, ConfigConstants.TAG_PARAMETERS);
			mParameters = new Parameters(paramsConfElement);

            Element validationConfElement = selectChildElement(Constants.NS_MA3, ConfigConstants.TAG_VALIDATION);
		    if (!String.IsNullOrEmpty(mConfigFilePath))
		    {
		        string configFileDirectory = Path.GetDirectoryName(mConfigFilePath);
			    mValidationConfig = new ValidationConfig(validationConfElement, configFileDirectory);
            }
		    else
		    {
                mValidationConfig = new ValidationConfig(validationConfElement);
            }
        }

		public virtual HttpConfig HttpConfig
		{
			get
			{
				return mHttpConfig;
			}
            set { mHttpConfig = value; }
		}

		public virtual ResolversConfig ResolversConfig
		{
			get
			{
				return mResolversConfig;
			}
            set { mResolversConfig = value; }
		}

		public virtual ValidationConfig ValidationConfig
		{
			get
			{
				return mValidationConfig;
			}
            set { mValidationConfig = value; }
		}

		public virtual TimestampConfig TimestampConfig
		{
			get
			{
				return mTimestampConfig;
			}
			set
			{
				mTimestampConfig = value;
			}
		}


		public virtual AlgorithmsConfig AlgorithmsConfig
		{
			get
			{
				return mAlgorithmsConfig;
			}
            set { mAlgorithmsConfig = value; }
		}

		public virtual Parameters Parameters
		{
			get
			{
				return mParameters;
			}
            set { mParameters = value; }
		}

		public virtual NamespacePrefixMap NsPrefixMap
		{
			get
			{
				return nsPrefixMap;
			}
			set
			{
				this.nsPrefixMap = value;
			}
		}

        private void loadConfig(Stream configStream)
        {
            try
			{
				XmlDocument xmlDocument = new XmlDocument();
                xmlDocument.Load(configStream);
                init(xmlDocument);
			}
			catch (Exception x)
			{
				logger.Error("Cant load config...", x);
				throw new XMLSignatureRuntimeException(x, I18n.translate("config.cantLoad", I18n.translate("config")));
			}
        }
        private void loadConfig(XmlDocument xmlDocument)
        {
            try
            {
                init(xmlDocument);
            }
            catch (Exception x)
            {
                logger.Error("Cant load config...", x);
                throw new XMLSignatureRuntimeException(x, I18n.translate("config.cantLoad", I18n.translate("config")));
            }
        }

		private void loadConfig(string aConfigFilePath)
		{
			logger.Info("Load config from : " + aConfigFilePath);

			if (aConfigFilePath == null)
			{
				aConfigFilePath = ConfigConstants.CONFIG_FILE_NAME;
			}
            
		    mConfigFilePath = aConfigFilePath;
			try
			{
				XmlDocument xmlDocument = new XmlDocument();
                xmlDocument.Load(aConfigFilePath);
                init(xmlDocument);
			}
			catch (Exception x)
			{
				logger.Error("Cant load config...", x);
				throw new XMLSignatureRuntimeException(x, I18n.translate("config.cantLoad", I18n.translate("config")));
			}
		}

        public static Config noInit()
        {
            return new Config(new Object());
        }		
	}

}