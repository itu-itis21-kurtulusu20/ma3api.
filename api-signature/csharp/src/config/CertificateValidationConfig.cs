using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Xml;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.common.util;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.signature.certval;

namespace tr.gov.tubitak.uekae.esya.api.signature.config
{
    public class CertificateValidationConfig : BaseConfigElement
    {

        public static readonly int DEFAULT_SIGNING_TIME_TOLERANCE_IN_SECONDS = 300;
        public static readonly int DEFAULT_LAST_REVOCATION_PERIOD_IN_SECONDS = 172800;
        public static readonly int DEFAULT_GRACE_PERIOD_IN_SECONDS = 86400;

        private readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
        Dictionary<String, String> certificateValidationPolicyFiles=new Dictionary<string, string>();

        int gracePeriodInSeconds = DEFAULT_GRACE_PERIOD_IN_SECONDS;

        int lastRevocationPeriodInSeconds = DEFAULT_LAST_REVOCATION_PERIOD_IN_SECONDS;

        bool useValidationDataPublishedAfterCreation = false;

        bool validateCertificateBeforeSigning = false;

        bool validateWithResourcesWithinSignature = false;

        int signingTimeToleranceInSeconds = DEFAULT_SIGNING_TIME_TOLERANCE_IN_SECONDS;

        TurkishESigProfile validationProfile = null;

        private readonly String folder;

        public CertificateValidationConfig()
        {
        }

        public CertificateValidationConfig(XmlElement aElement)
            : base(aElement)
        {
           init(aElement);
        }

        public CertificateValidationConfig(XmlElement aElement, String aFolder)
            : base(aElement)
        {
            folder = aFolder;
            init(aElement);
        }

        private void init(XmlElement aElement)
        {
            if (aElement != null)
            {
                XmlElement[] policyNodes = XmlUtil.selectNodes(aElement.FirstChild, ConfigConstants.NS_MA3, ConfigConstants.TAG_CERTIFICATE_VALIDATION_POLICY);
                foreach (XmlElement policyElm in policyNodes)
                {
                    String path = policyElm.InnerText.Trim();

                    if (File.Exists(path) == false && !String.IsNullOrEmpty(folder))
                    {
                        String tmpPath = folder + "\\" + path;
                        if (File.Exists(tmpPath))
                            path = tmpPath;
                    }


                    String certType = policyElm.GetAttribute(ConfigConstants.ATTR_FOR);
                    CertValidationPolicies.CertificateType type = CertValidationPolicies.getCertificateType(certType);
                    if (certificateValidationPolicyFiles.ContainsKey(certType))
                    {
                        throw new ConfigurationException("Multiple certificate policies defined for type " + type);
                    }
                    logger.Debug("Validation policy file : " + path + " for: '" + type + "'");
                    certificateValidationPolicyFiles[certType]= path;
                }

                //certificateValidationPolicyFile = getChildText(NS_MA3, TAG_CERTIFICATE_VALIDATION_POLICY);


                int? gracePeriod = getChildInteger(ConfigConstants.TAG_GRACE_PERIOD_IN_SECONDS);
                if (gracePeriod == null)
                    throw new ConfigurationException(String.Format("'{0}' not defined in Signature configuration", ConfigConstants.TAG_GRACE_PERIOD_IN_SECONDS));
                gracePeriodInSeconds = gracePeriod.Value;
                logger.Debug("Grace period in seconds : " + gracePeriodInSeconds);

                int? lastRevocationPeriod = getChildInteger(ConfigConstants.TAG_LAST_REVOCATION_PERIOD_IN_SECONDS);
                if (lastRevocationPeriod == null)
                    throw new ConfigurationException(String.Format("'{0}' not defined in Signature configuration", ConfigConstants.TAG_LAST_REVOCATION_PERIOD_IN_SECONDS));
                lastRevocationPeriodInSeconds = lastRevocationPeriod.Value;
                if (lastRevocationPeriodInSeconds <= gracePeriodInSeconds)
                    throw new ConfigurationException("config.inconsistentGracePeriod");
                logger.Debug("Last revocation period seconds : " + lastRevocationPeriodInSeconds);

                bool? validationDataAfterCreation = getChildBoolean(ConfigConstants.TAG_USE_VALIDATION_DATA_PUBLISHED_AFTER_CREATION);
                if (validationDataAfterCreation != null)
                    useValidationDataPublishedAfterCreation = validationDataAfterCreation.Value;
                logger.Info("Use validation data published  after creation : " + useValidationDataPublishedAfterCreation);

                bool? validateCert = getChildBoolean(ConfigConstants.TAG_VALIDATE_CERT_BEFORE_SIGNING);
                if (validateCert != null)
                    validateCertificateBeforeSigning = validateCert.Value;
                logger.Debug("Validate certificate before signing : " + validateCertificateBeforeSigning);

                int? signingTimeTolerance = getChildInteger(ConfigConstants.TAG_TOLERATE_SIGNING_TIME_IN_SECONDS);
                if (signingTimeTolerance == null)         
                  signingTimeToleranceInSeconds = 300;
                signingTimeToleranceInSeconds = signingTimeTolerance.Value;
                logger.Debug("Signing time tolerance in seconds: " + signingTimeToleranceInSeconds);

                String validationProfileText = getChildText(ConfigConstants.NS_MA3, ConfigConstants.TAG_VALIDATION_PROFILE);
                if (validationProfileText != null)
                {
                    logger.Debug("Validation Profile: " + validationProfileText);
                    validationProfile = TurkishESigProfile.getSignatureProfileFromName(validationProfileText);
                }
            }
        }

        public Dictionary<String, String> getCertificateValidationPolicyFile()
        {
            return certificateValidationPolicyFiles;
        }

        public void setCertificateValidationPolicyFile(Dictionary<String, String> aCertificateValidationPolicyFile)
        {
            certificateValidationPolicyFiles = aCertificateValidationPolicyFile;
        }

        public TurkishESigProfile getValidationProfile()
        {
            return validationProfile;
        }

        public void setValidationProfile(TurkishESigProfile aTurkishESigProfile)
        {
            validationProfile = aTurkishESigProfile;
        }

        public long getGracePeriodInSeconds()
        {
            return gracePeriodInSeconds;
        }

        public long getSigningTimeToleranceInSeconds()
        {        
            if (signingTimeToleranceInSeconds < MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS || signingTimeToleranceInSeconds > MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS)
            {
                String errorMessage = "Signing time tolerance value must be between the values" + MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS + " and " + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "!" + "The value" + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "corresponds to 1 day!";
                logger.Error(errorMessage);
                throw new ArgErrorException(errorMessage);
            }

            return signingTimeToleranceInSeconds;
        }

        public void setGracePeriodInSeconds(int aGracePeriodInSeconds)
        {
            gracePeriodInSeconds = aGracePeriodInSeconds;
        }

        public long getLastRevocationPeriodInSeconds()
        {
            return lastRevocationPeriodInSeconds;
        }

        public void setLastRevocationPeriodInSeconds(int aLastRevocationPeriodInSeconds)
        {
            lastRevocationPeriodInSeconds = aLastRevocationPeriodInSeconds;
        }

        public bool isUseValidationDataPublishedAfterCreation()
        {
            return useValidationDataPublishedAfterCreation;
        }

        public void setUseValidationDataPublishedAfterCreation(bool aUseValidationDataPublishedAfterCreation)
        {
            useValidationDataPublishedAfterCreation = aUseValidationDataPublishedAfterCreation;
        }

        public bool isValidateCertificateBeforeSigning()
        {
            return validateCertificateBeforeSigning;
        }

        public void setValidateCertificateBeforeSigning(bool aValidateCertificateBeforeSigning)
        {
            validateCertificateBeforeSigning = aValidateCertificateBeforeSigning;
        }
        public bool isValidateWithResourcesWithinSignature()
        {
            return validateWithResourcesWithinSignature;
        }

        public void setValidateWithResourcesWithinSignature(bool validateWithResourcesWithinSignature)
        {
            this.validateWithResourcesWithinSignature = validateWithResourcesWithinSignature;
        }
    }
}
