using System;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.api.signature.config;

namespace tr.gov.tubitak.uekae.esya.api.signature
{
    /**
 * Runtime parameters for signature creation and validation
 * @author ayetgin
 */
    public class Context : ICloneable
    {
        Uri baseURI;
        Config config;
        Signable data;
        ValidationInfoResolver validationInfoResolver;
        DateTime validationTime;
        /**
         * Constructor for context for base URI equals to working dir: ".".
         * Config is also will be tried to read from default locations if not set
         * with Config#setConfig.
         * @see Config
         */
        public Context()
        {
            try
            {
                baseURI = new Uri(Environment.CurrentDirectory);
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x, "Should not happen!");
            }
        }

        public Context(Uri aBaseURI)
        {
            baseURI = aBaseURI;
        }

        public Context(Uri aBaseURI, Config aConfig)
        {
            baseURI = aBaseURI;
            config = aConfig;
        }

        public Signable getData()
        {
            return data;
        }

        public void setData(Signable aData)
        {
            data = aData;
        }

        public ValidationInfoResolver getValidationInfoResolver()
        {
            return validationInfoResolver;
        }

        public void setValidationInfoResolver(ValidationInfoResolver aValidationInfoResolver)
        {
            validationInfoResolver = aValidationInfoResolver;
        }
        public DateTime getValidationTime()
        {
            return validationTime;
        }

        public void setValidationTime(DateTime validationTime)
        {
            this.validationTime = validationTime;
        }
        public Uri getBaseURI()
        {
            return baseURI;
        }

        public void setBaseURI(Uri aBaseURI)
        {
            baseURI = aBaseURI;
        }

        public Config getConfig()
        {
            if (config == null)
                config = new Config();
            return config;
        }

        public void setConfig(Config aConfig)
        {
            config = aConfig;
        }

        public Object Clone()
        {
            try
            {
                return /*(Context) base.*/MemberwiseClone();
            }
            catch (Exception x)
            {
                throw new SignatureRuntimeException(x);
            }
        }
    }
}
