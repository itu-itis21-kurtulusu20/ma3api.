
using System.Reflection;
using System.Xml;
using log4net;

namespace tr.gov.tubitak.uekae.esya.api.signature.config
{
    public class Parameters : BaseConfigElement
    {
        protected ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);


        bool checkPolicyUri = false;   // keep

        bool forceStrictReferenceUse = true; // keep

        bool validateTimestampWhileSigning = true; // keep

        private bool trustSigningTime = false;  // i guess we should keep this too :)

        bool writeReferencedValidationDataToFileOnUpgrade = false; // keep

        bool useCAdESATSv2 = false; // keep

        public Parameters()
        {
        }

        public Parameters(XmlElement aElement)
            : base(aElement)
        {
            if (aElement != null)
            {

                bool? strictReferences = getChildBoolean(ConfigConstants.TAG_FORCE_STRICT_REFERENCES);
                if (strictReferences != null)
                    forceStrictReferenceUse = strictReferences.Value;
                logger.Debug("Force strict reference use : " + forceStrictReferenceUse);

                bool? checkForPolicyURI = getChildBoolean(ConfigConstants.TAG_CHECK_POLICY_URI);
                if (checkForPolicyURI != null)
                    checkPolicyUri = checkForPolicyURI.Value;
                logger.Debug("Check for policy URI : " + checkPolicyUri);

                bool? validateTS = getChildBoolean(ConfigConstants.TAG_VALIDATE_TS_WHILE_SIGNING);
                if (validateTS != null)
                    validateTimestampWhileSigning = validateTS.Value;
                logger.Debug("Validate timestamp while signing : " + validateTimestampWhileSigning);

                bool? inSigningTimeWeTrust = getChildBoolean(ConfigConstants.TAG_TRUST_SIGNING_TIME);
                if (inSigningTimeWeTrust != null)
                    trustSigningTime = inSigningTimeWeTrust.Value;
                logger.Debug("Trust signing time : " + trustSigningTime);

                bool? writeValidationData = getChildBoolean(ConfigConstants.TAG_WRITE_VALIDATIONDATA_ON_UPGRADE);
                if (writeValidationData != null)
                    writeReferencedValidationDataToFileOnUpgrade = writeValidationData.Value;
                logger.Debug("Write referenced validation data to file on upgrade : " + writeReferencedValidationDataToFileOnUpgrade);

                bool? CAdESATSv2 = getChildBoolean(ConfigConstants.TAG_USE_CAdES_ATSv2);
                if (CAdESATSv2 != null)
                    useCAdESATSv2 = CAdESATSv2.Value;
                logger.Debug("Use archive timestamp v2 for ESA type : " + useCAdESATSv2);
            }

        }

        public bool isCheckPolicyUri()
        {
            return checkPolicyUri;
        }

        public void setCheckPolicyUri(bool aCheckPolicyUri)
        {
            checkPolicyUri = aCheckPolicyUri;
        }

        
        public bool isForceStrictReferenceUse()
        {
            return forceStrictReferenceUse;
        }

        public void setForceStrictReferenceUse(bool aForceStrictReferenceUse)
        {
            forceStrictReferenceUse = aForceStrictReferenceUse;
        }

        
        public bool isValidateTimestampWhileSigning()
        {
            return validateTimestampWhileSigning;
        }

        public void setValidateTimestampWhileSigning(bool aValidateTimestampWhileSigning)
        {
            validateTimestampWhileSigning = aValidateTimestampWhileSigning;
        }


        public bool isTrustSigningTime()
        {
            return trustSigningTime;
        }

        public void setTrustSigningTime(bool aTrustSigningTime)
        {
            trustSigningTime = aTrustSigningTime;
        }

        
        public bool isWriteReferencedValidationDataToFileOnUpgrade()
        {
            return writeReferencedValidationDataToFileOnUpgrade;
        }

        public void setWriteReferencedValidationDataToFileOnUpgrade(bool aWriteReferencedValidationDataToFileOnUpgrade)
        {
            writeReferencedValidationDataToFileOnUpgrade = aWriteReferencedValidationDataToFileOnUpgrade;
        }

        
        public bool isUseCAdESATSv2()
        {
            return useCAdESATSv2;
        }

        public void setUseCAdESATSv2(bool aUseCAdESATSv2)
        {
            useCAdESATSv2 = aUseCAdESATSv2;
        }
    }
}
