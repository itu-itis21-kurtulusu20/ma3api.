using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Xml;
using log4net;
using tr.gov.tubitak.uekae.esya.api.certificate.validation.policy;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace tr.gov.tubitak.uekae.esya.api.signature.config
{
    /**
 * ESYA Signature API configuration
 * @author ayetgin
 */
    public class Config : BaseConfigElement
    {
        private readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        private CertValidationPolicies certificateValidationPolicies;

        private HttpConfig httpConfig;

        private TimestampConfig timestampConfig;
        private AlgorithmsConfig algorithmsConfig;

        private CertificateValidationConfig certificateValidationConfig;

        private Parameters parameters;

        private readonly String configFilePath;

        public Config()
        {
            configFilePath = ConfigConstants.CONFIG_FILE_NAME;
            if (File.Exists(configFilePath))
            {
                loadConfig(new FileStream(configFilePath, FileMode.Open, FileAccess.Read));
                logger.Info("Config loaded according to: "+configFilePath);
            }
        }

        public Config(String configPath)
        {
            configFilePath = configPath;
            loadConfig(new FileStream(configPath, FileMode.Open, FileAccess.Read));
        }

        protected Config(XmlElement aElement)
            : base(aElement)
        {
        }


        public void loadConfig(Stream configStream)
        {
            try
            {
                XmlDocument xmlDocument = new XmlDocument();
                xmlDocument.Load(configStream);
                init(xmlDocument);
            }
            catch (Exception x)
            {
                logger.Error("Cannot load config...", x);
                throw new SignatureRuntimeException(x, "config.cantLoad");
            }
        }

        private void init(XmlDocument aDocument)
        {
            Element = aDocument.DocumentElement;

            //Element resolverConfElement = selectChildElement(NS_MA3, TAG_RESOLVERS);
            // todo resolversConfig = new ResolversConfig(resolverConfElement);

            XmlElement timestampConfElement = selectChildElement(ConfigConstants.NS_MA3, ConfigConstants.TAG_TIMESTAMPSERVER);
            timestampConfig = new TimestampConfig(timestampConfElement);

            XmlElement algoConfElement = selectChildElement(ConfigConstants.NS_MA3, ConfigConstants.TAG_ALGORITHMS);
            algorithmsConfig = new AlgorithmsConfig(algoConfElement);

            XmlElement httpConfElement = selectChildElement(ConfigConstants.NS_MA3, ConfigConstants.TAG_HTTP);
            httpConfig = new HttpConfig(httpConfElement);

            XmlElement paramsConfElement = selectChildElement(ConfigConstants.NS_MA3, ConfigConstants.TAG_PARAMS);
            parameters = new Parameters(paramsConfElement);

            XmlElement certValConfElement = selectChildElement(ConfigConstants.NS_MA3, ConfigConstants.TAG_CERTVALIDATION);
            if (!String.IsNullOrEmpty(configFilePath))
            {
                string configFileDirectory = Path.GetDirectoryName(configFilePath);
                certificateValidationConfig = new CertificateValidationConfig(certValConfElement, configFileDirectory);
            }
            else
            {
                certificateValidationConfig = new CertificateValidationConfig(certValConfElement);
            }
        }

        private void loadConfig()
        {
            try
            {
                Dictionary<String, String> policyFiles = certificateValidationConfig.getCertificateValidationPolicyFile();
                certificateValidationPolicies = new CertValidationPolicies();
                foreach (String certType in policyFiles.Keys){
                    ValidationPolicy policy = PolicyReader.readValidationPolicyFromURL(policyFiles[certType]);
                    certificateValidationPolicies.register(certType, policy);
                }
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x, "Cant read certificate validation policy ");
            }
        }


        public CertValidationPolicies getCertificateValidationPolicies()
        {
            if (certificateValidationPolicies == null)
            {
                loadConfig();
            }
            return certificateValidationPolicies;
        }

        public void setCertificateValidationPolicies(CertValidationPolicies aCertificateValidationPolicy)
        {
            certificateValidationPolicies = aCertificateValidationPolicy;
        }

        public HttpConfig getHttpConfig()
        {
            return httpConfig;
        }

        public void setHttpConfig(HttpConfig aHttpConfig)
        {
            httpConfig = aHttpConfig;
        }

        public CertificateValidationConfig getCertificateValidationConfig()
        {
            return certificateValidationConfig;
        }

        public void setCertificateValidationConfig(CertificateValidationConfig aCertificateValidationConfig)
        {
            certificateValidationConfig = aCertificateValidationConfig;
        }

        public Parameters getParameters()
        {
            return parameters;
        }

        public void setParameters(Parameters aParameters)
        {
            parameters = aParameters;
        }

        public TimestampConfig getTimestampConfig()
        {
            return timestampConfig;
        }

        public void setTimestampConfig(TimestampConfig aTimestampConfig)
        {
            timestampConfig = aTimestampConfig;
        }

        public AlgorithmsConfig getAlgorithmsConfig()
        {
            return algorithmsConfig;
        }

        public void setAlgorithmsConfig(AlgorithmsConfig aAlgorithmsConfig)
        {
            algorithmsConfig = aAlgorithmsConfig;
        }

    }
}
