using System;
using System.Xml;
using tr.gov.tubitak.uekae.esya.api.crypto.alg;

namespace tr.gov.tubitak.uekae.esya.api.signature.config
{
    /**
     * Algorithms that will be used for signature generation
     * @author ayetgin
     */
    public class AlgorithmsConfig : BaseConfigElement
    {
        private readonly String XML_WRONG_ELEMENT = "Cannot create a {0} from a {1} element. Incoming value: {2}";

        private DigestAlg digestAlg = DigestAlg.SHA256;
        private SignatureAlg signatureAlg = SignatureAlg.RSA_SHA256;
        private DigestAlg digestAlgForOCSP;

        public AlgorithmsConfig()
        {
        }

        /**
         * Constructor for read config from XML element
         * @throws ConfigurationException
         */
        public AlgorithmsConfig(XmlElement aElement)
            : base(aElement)
        {
            String digestAlgStr = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_DIGESTALG);
            try
            {
                if(digestAlgStr!=null)
                    digestAlg = DigestAlg.fromName(digestAlgStr);
            }
            catch (Exception x)
            {
                throw new ConfigurationException(String.Format(XML_WRONG_ELEMENT, "DigestAlg", ConfigConstants.TAG_DIGESTALG, digestAlgStr));
            }

            String signatureAlgStr = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_SIGNATUREALG);
            try
            {
                if(signatureAlgStr != null)
                    signatureAlg = SignatureAlg.fromName(signatureAlgStr);
            }
            catch (Exception x)
            {
                throw new ConfigurationException(String.Format(XML_WRONG_ELEMENT, "signatureAlg", ConfigConstants.TAG_SIGNATUREALG, signatureAlgStr));
            }

            digestAlgStr = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_OCSP_DIGEST_ALG);
            try
            {
                if (digestAlgStr != null)
                    digestAlgForOCSP = DigestAlg.fromName(digestAlgStr);
            }
            catch (Exception x)
            {
                throw new ConfigurationException(String.Format(XML_WRONG_ELEMENT, "DigestAlg", ConfigConstants.TAG_OCSP_DIGEST_ALG, digestAlgStr));
            }
        }

        /**
         * @return digest algorithm that will be used to calculate digest value for
         * data to signed
         */
        public DigestAlg getDigestAlg()
        {
            return digestAlg;
        }

        /**
         * @return algorithm that will be used for signature operation
         */
        public SignatureAlg getSignatureAlg()
        {
            return signatureAlg;
        }

        /**
         * @param digestAlg algorithm that will be used to calculate digest value
         *                   for data to signed
         */
        public void setDigestAlg(DigestAlg digestAlg)
        {
            this.digestAlg = digestAlg;
        }

        /**
         * @param signatureAlg algorithm that will be used for signature operation
         */
        public void setSignatureAlg(SignatureAlg signatureAlg)
        {
            this.signatureAlg = signatureAlg;
        }

        /**
        * @return digest algorithm that will be used for OCSP request
        */
        public DigestAlg getDigestAlgForOCSP()
        {
            return digestAlgForOCSP;
        }
        /**
         * @param digestAlg algorithm that will be used for OCSP request
         */
        public void setDigestAlgForOCSP(DigestAlg digestAlgForOCSP)
        {
            this.digestAlgForOCSP = digestAlgForOCSP;
        }
    }
}
