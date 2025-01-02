using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Xml;
using log4net;
using tr.gov.tubitak.uekae.esya.api.asn.profile;
using tr.gov.tubitak.uekae.esya.api.common;
using tr.gov.tubitak.uekae.esya.api.crypto.exceptions;
using tr.gov.tubitak.uekae.esya.api.signature.certval;
using tr.gov.tubitak.uekae.esya.api.xmlsignature.util;

namespace tr.gov.tubitak.uekae.esya.api.xmlsignature.config
{
	using Element = XmlElement;
	using PolicyReader = tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
	using ValidationPolicy = tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
	using SignatureType = tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
	using Constants = tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
	using XmlCommonUtil = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;

    /// <summary>
    /// @author ahmety
    /// date: Dec 4, 2009
    /// </summary>
    public class ValidationConfig : BaseConfigElement
	{

	    private readonly ILog logger = LogManager.GetLogger(MethodBase.GetCurrentMethod().DeclaringType);
       
	    // old
        //private string mCertificateValidationPolicyFile;
        //private ValidationPolicy validationPolicy;

        // new
        //private IDictionary<String, String> mCertificateValidationPolicyFiles = new Dictionary<string, string>();
        private CertValidationPolicies mCertificateValidationPolicies;

	    //private string mConfigFilePath;

	    /*public string ConfigFilePath
	    {
	        get { return mConfigFilePath; }
	        set { mConfigFilePath = value; }
	    }*/

	    private int ? mGracePeriodInSeconds;
		private int ? mLastRevocationPeriodInSeconds;
	    private int ? mSignatureTimeToleranceInSeconds;
        private TurkishESigProfile mValidationProfile = null;

        private bool ? mForceStrictReferences = false;
		private bool ? mUseValidationDataPublishedAfterCreation = true;
        private bool? mTrustSigningTime = false;

        private bool validateCertificateBeforeSigning = false;
        private bool validateTimestampWhileSigning = false;

		private bool ? mCheckPolicyURI = true;

	    private readonly string mFolder = null;

		private readonly IDictionary<SignatureType, SignatureProfileValidationConfig> mProfiles = new Dictionary<SignatureType, SignatureProfileValidationConfig>();
		private readonly IList<SignatureType> mProfileInheritanceCalculated = new List<SignatureType>();

        /*public ValidationConfig(String aCertificateValidationPolicyFile, 
                                ValidationPolicy aValidationPolicy, 
                                long aGracePeriodInSeconds, 
                                long aLastRevocationPeriodInSeconds, 
                                bool aForceStrictReferences, 
                                bool aUseValidationDataPublishedAfterCreation, 
                                bool aCheckPolicyURI, 
                                IDictionary<SignatureType, SignatureProfileValidationConfig> aProfiles)
        {
            mCertificateValidationPolicyFile = aCertificateValidationPolicyFile;
            validationPolicy = aValidationPolicy;
            mGracePeriodInSeconds = (int)aGracePeriodInSeconds;
            mLastRevocationPeriodInSeconds = (int)aLastRevocationPeriodInSeconds;
            mForceStrictReferences = aForceStrictReferences;
            mUseValidationDataPublishedAfterCreation = aUseValidationDataPublishedAfterCreation;
            mCheckPolicyURI = aCheckPolicyURI;
            mProfiles = aProfiles;
        }*/

        public ValidationConfig(IDictionary<String, String> aCertificateValidationPolicyFile,
                                CertValidationPolicies aValidationPolicy,
                                long aGracePeriodInSeconds,
                                long aLastRevocationPeriodInSeconds,
                                long aSignatureTimeToleranceInSeconds,
                                TurkishESigProfile aValidationProfile,
                                bool aForceStrictReferences,
                                bool aUseValidationDataPublishedAfterCreation,
                                bool aValidateCertificateBeforeSigning,
                                bool aTrustSigningTime,
                                bool aCheckPolicyURI,
                                IDictionary<SignatureType, SignatureProfileValidationConfig> aProfiles)
        {
            //mCertificateValidationPolicyFiles = aCertificateValidationPolicyFile;
            mCertificateValidationPolicies = aValidationPolicy;
            mGracePeriodInSeconds = (int)aGracePeriodInSeconds;
            mLastRevocationPeriodInSeconds = (int)aLastRevocationPeriodInSeconds;
            mSignatureTimeToleranceInSeconds = (int)aSignatureTimeToleranceInSeconds;
            mValidationProfile = aValidationProfile;
            mForceStrictReferences = aForceStrictReferences;
            mUseValidationDataPublishedAfterCreation = aUseValidationDataPublishedAfterCreation;
            validateCertificateBeforeSigning = aValidateCertificateBeforeSigning;
            mTrustSigningTime = aTrustSigningTime;
            mCheckPolicyURI = aCheckPolicyURI;
            mProfiles = aProfiles;
        }

	    public ValidationConfig(Element aElement, string aFolder) : base(aElement)
	    {
	        mFolder = aFolder;
            init(aElement);
	    }

        public ValidationConfig(Element aElement) : base(aElement)
        {
            init(aElement);
        }

        public void init(Element aElement)
	    {
            if (aElement != null)
            {
                //mCertificateValidationPolicyFile = getChildText(Constants.NS_MA3,ConfigConstants.TAG_CERTIFICATE_VALIDATION_POLICY);

                Element[] policyNodes = common.util.XmlUtil.selectNodes(aElement.FirstChild,
                                                                        signature.config.ConfigConstants.NS_MA3,
                                                                        signature.config.ConfigConstants.TAG_CERTIFICATE_VALIDATION_POLICY);
                mCertificateValidationPolicies = new CertValidationPolicies();

                foreach (Element policyElm in policyNodes)
                {
                    String path = policyElm.InnerText;
                    String certType = policyElm.GetAttribute(signature.config.ConfigConstants.ATTR_FOR);
                    /*CertValidationPolicies.CertificateType type = CertValidationPolicies.getCertificateType(certType);
                    if (mCertificateValidationPolicyFiles.ContainsKey(certType)){
                        throw new ConfigurationException("Multiple certificate policies defined for type "+type);
                    }
                    mCertificateValidationPolicyFiles.Add(certType, path);*/
                    try
                    {
                        if (File.Exists(path) == false && !String.IsNullOrEmpty(mFolder))
                        {
                            String tmpPath = mFolder + "\\" + path;
                            if (File.Exists(tmpPath))
                                path = tmpPath;
                        }
                        
                        mCertificateValidationPolicies.register(certType, PolicyReader.readValidationPolicyFromURL(path));
                    }
                    catch (Exception x)
                    {
                        throw new ConfigurationException(x, "config.cantFind", path);
                    }
                }

                Element validatorsElm = selectChildElement(Constants.NS_MA3, ConfigConstants.TAG_VALIDATORS);
                if (validatorsElm != null)
                {
                    Element[] profileElmArr = XmlCommonUtil.selectNodes(validatorsElm.FirstChild, Constants.NS_MA3, ConfigConstants.TAG_PROFILE);
                    if (profileElmArr != null)
                    {
                        foreach (Element profileElm in profileElmArr)
                        {
                            SignatureProfileValidationConfig vsp = new SignatureProfileValidationConfig(profileElm);
                            SignatureType? signatureType = vsp.SignType;
                            if (signatureType != null)
                                mProfiles[(SignatureType)vsp.SignType] = vsp;
                        }
                    }
                }
                handleValidationInheritance();

                int? gracePeriod = getChildInteger(ConfigConstants.TAG_GRACE_PERIOD_IN_SECONDS);
                if (gracePeriod == null)
                {
                    throw new ConfigurationException("config.notDefined", ConfigConstants.TAG_GRACE_PERIOD_IN_SECONDS);
                }
                mGracePeriodInSeconds = gracePeriod;

                int? lastRevocationPeriod = getChildInteger(ConfigConstants.TAG_LAST_REVOCATION_PERIOD_IN_SECONDS);
                if (lastRevocationPeriod == null)
                {
                    throw new ConfigurationException("config.notDefined", ConfigConstants.TAG_LAST_REVOCATION_PERIOD_IN_SECONDS);
                }

                mLastRevocationPeriodInSeconds = lastRevocationPeriod;

                if (mLastRevocationPeriodInSeconds <= mGracePeriodInSeconds)
                {
                    throw new ConfigurationException("config.inconsistentGracePeriod");
                }

                int? signatureTimeTolerance = getChildInteger(ConfigConstants.TAG_TOLERATE_SIGNING_TIME_IN_SECONDS);
                if (signatureTimeTolerance == null)
                {
                    signatureTimeTolerance = 300;
                }
                mSignatureTimeToleranceInSeconds = signatureTimeTolerance.Value;

                bool? strictReferences = getChildBoolean(ConfigConstants.TAG_FORCE_STRICT_REFERENCES);
                if (strictReferences != null)
                {
                    mForceStrictReferences = strictReferences;
                }

                bool? trustSigningTime = getChildBoolean(ConfigConstants.TAG_TRUST_SIGNING_TIME);
                if (trustSigningTime != null)
                {
                    mTrustSigningTime = trustSigningTime;
                }

                bool? validationDataAfterCreation = getChildBoolean(ConfigConstants.TAG_USE_VALIDATION_DATA_PUBLISHED_AFTER_CREATION);
                if (validationDataAfterCreation != null)
                {
                    mUseValidationDataPublishedAfterCreation = validationDataAfterCreation;
                }

                bool? checkForPolicyURI = getChildBoolean(ConfigConstants.TAG_CHECK_POLICY_URI);
                if (checkForPolicyURI != null)
                {
                    mCheckPolicyURI = checkForPolicyURI;
                }

                bool? validateTS = getChildBoolean(signature.config.ConfigConstants.TAG_VALIDATE_TS_WHILE_SIGNING);
                if (validateTS != null)
                    validateTimestampWhileSigning = validateTS.Value;
                //logger.Info("Validate timestamp while signing : " + validateTimestampWhileSigning);

                bool? validateCert = getChildBoolean(signature.config.ConfigConstants.TAG_VALIDATE_CERT_BEFORE_SIGNING);
                if (validateCert != null)
                    validateCertificateBeforeSigning = validateCert.Value;
                //logger.Info("Validate certificate before signing : " + validateCertificateBeforeSigning);

                String validationProfileText = getChildText(signature.config.ConfigConstants.NS_MA3, signature.config.ConfigConstants.TAG_VALIDATION_PROFILE);
                if (validationProfileText != null)
                    mValidationProfile = TurkishESigProfile.getSignatureProfileFromName(validationProfileText); ;
            }
        }

	
		private void handleValidationInheritance()
		{
			// check if any profile is missing
            Array allTypes = Enum.GetValues(typeof(SignatureType));
		    //SignatureType[] allTypes = Enum.GetValues(typeof(SignatureType));
			foreach (SignatureType current in allTypes)
			{
				if (!mProfiles.Keys.Contains(current))
				{
					throw new ConfigurationException("config.missingValidationProfile", current.GetType());
				}
			}

			// check if any ancestor is missing
			foreach (SignatureProfileValidationConfig spvc in mProfiles.Values)
			{
				SignatureType ? type = spvc.InheritValidatorsFrom;
				if ((type != null) && (!mProfiles.Keys.Contains((SignatureType) type)))
				{
					throw new ConfigurationException("config.missingAncestorToInheritProfiles", spvc.SignType, type);
				}
			}

			// inherit ancestor validators
			foreach (SignatureType signatureType in mProfiles.Keys)
			{
				handleValidationInheritance(signatureType, new List<SignatureType>());
			}
		}

    	private IList<Type> handleValidationInheritance(SignatureType aType, IList<SignatureType> aVisitedTypes)
		{
			// cyclic dependency check
			if (aVisitedTypes.Contains(aType))
			{
				throw new ConfigurationException("config.cyclicInheritanceForProfileValidators");
			}
			aVisitedTypes.Add(aType);

			SignatureProfileValidationConfig config = mProfiles[aType];

			if (mProfileInheritanceCalculated.Contains(aType))
			{
				return config.Validators;
			}

			SignatureType ? toBeInherited = config.InheritValidatorsFrom;
			if (toBeInherited != null)
			{
				IList<Type> additional = handleValidationInheritance((SignatureType) toBeInherited, aVisitedTypes);
			    ((List<Type>)config.Validators).InsertRange(0, additional);
			}

			mProfileInheritanceCalculated.Add(aType);
			return config.Validators;
		}

		public virtual int GracePeriodInSeconds
		{
			get
			{
				return (int)mGracePeriodInSeconds;
			}
		}

		public virtual int LastRevocationPeriodInSeconds
		{
			get
			{
				return (int)mLastRevocationPeriodInSeconds;
			}
		}

	    public virtual int getSignatureTimeToleranceInSeconds()
	    {	   
	        if (mSignatureTimeToleranceInSeconds < MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS || mSignatureTimeToleranceInSeconds > MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS)
	        {
	            String errorMessage = "Signing time tolerance value must be between the values" + MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS + " and " + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "!" + "The value" + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "corresponds to 1 day!";
	            logger.Error(errorMessage);
	            throw new ArgErrorException(errorMessage);
	        }

            return (int)mSignatureTimeToleranceInSeconds;
	    }

        public virtual bool ForceStrictReferences
		{
			get
			{
				return (bool)mForceStrictReferences;
			}
		}

        public virtual bool TrustSigningTime
        {
            get { return (bool) mTrustSigningTime; }
        }

		public virtual bool UseValidationDataPublishedAfterCreation
		{
			get
			{
				return (bool) mUseValidationDataPublishedAfterCreation;
			}
		}

		public virtual bool CheckPolicyURI
		{
			get
			{
				return (bool)mCheckPolicyURI;
			}
			set
			{
				mCheckPolicyURI = value;
			}
		}


		public virtual SignatureProfileValidationConfig getProfile(SignatureType aType)
		{
			return mProfiles[aType];
		}

        public virtual TurkishESigProfile getValidationProfile()
        {
            return mValidationProfile;
        }

        public virtual void addProfile(SignatureType aType, SignatureProfileValidationConfig aProfile)
		{
			mProfiles[aType] = aProfile;
		}

		public virtual string CertificateValidationPolicyFile
		{
			/*get
			{
				//return mCertificateValidationPolicyFile;
			}*/
			set
			{
                mCertificateValidationPolicies = new CertValidationPolicies();
			    try
			    {
                    mCertificateValidationPolicies.register("",PolicyReader.readValidationPolicy(value));
			    }
			    catch (Exception x)
			    {
			        throw new ESYARuntimeException(I18n.translate("config.cantFind", value), x);
			    }
				//mCertificateValidationPolicyFile = value;
				//validationPolicy = null;
			}
		}

		public virtual ValidationPolicy CertificateValidationPolicy
		{
			/*get
			{
                //return getCertificateValidationPolicy(true);
            }*/
			set
			{
                mCertificateValidationPolicies = new CertValidationPolicies();
                mCertificateValidationPolicies.register("", value);
				//mCertificateValidationPolicyFile = null;
				//validationPolicy = value;
			}
		}

        /*public ValidationPolicy getCertificateValidationPolicy(bool useExternalResources)
        {
            if (validationPolicy==null)
            {
                FileInfo fileInfo = new FileInfo(CertificateValidationPolicyFile);
                bool found = false;
                if (!fileInfo.Exists)
                {
                    String certValPoFile = CertificateValidationPolicyFile;
                    // if policy file is on web
                    if (certValPoFile.Substring(0, 4).Equals("http"))
                    {
                        try
                        {
                            validationPolicy = PolicyReader.readValidationPolicyFromURL(certValPoFile);
                            found = true;
                        }
                        catch (Exception e)
                        {
                            throw new ESYAException("Cannot resolve policy on url" + CertificateValidationPolicyFile);
                        }
                    }
                    else
                    {
                        if (mConfigFilePath == null)
                        {
                            throw new Exception("Can not find cert validation config file. Tried = " +
                                                CertificateValidationPolicyFile);
                        }
                        else
                        {
                            FileInfo fileInfoConfig = new FileInfo(mConfigFilePath);
                            String valConfigPath = fileInfoConfig.Directory + "/" + fileInfo.Name;
                            fileInfo = new FileInfo(valConfigPath);
                            if (!fileInfo.Exists)
                            {
                                throw new Exception("Can not find cert validation config file. Tried = " + valConfigPath +
                                                    "and" +
                                                    CertificateValidationPolicyFile);
                            }
                        }
                    }
                }
                if(!found)
                    validationPolicy = PolicyReader.readValidationPolicy(fileInfo.FullName);
            }

            if (!useExternalResources)
            {// DONE sertifika dogrulaman�n yeni versiyonu ile duzelt.
                //return null;
                return validationPolicy.withoutFinders();
            }
            
            return validationPolicy;
        }*/

        public CertValidationPolicies getCertValidationPolicies()
        {
            return mCertificateValidationPolicies;
        }

        public void setCertValidationPolicies(CertValidationPolicies validationPolicies)
        {
            this.mCertificateValidationPolicies = validationPolicies;
        }

        public bool isValidateCertificateBeforeSigning()
        {
            return validateCertificateBeforeSigning;
        }

        public void setValidateCertificateBeforeSigning(bool aValidateCertificateBeforeSigning)
        {
            validateCertificateBeforeSigning = aValidateCertificateBeforeSigning;
        }

        public bool isValidateTimestampWhileSigning()
        {
            return validateTimestampWhileSigning;
        }

        public void setValidateTimestampWhileSigning(bool aValidateTimestampWhileSigning)
        {
            validateTimestampWhileSigning = aValidateTimestampWhileSigning;
        }
    }
}