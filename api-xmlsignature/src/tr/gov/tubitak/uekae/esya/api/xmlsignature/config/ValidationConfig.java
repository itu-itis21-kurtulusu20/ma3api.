package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.PolicyReader;
import tr.gov.tubitak.uekae.esya.api.certificate.validation.policy.ValidationPolicy;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.validator.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tr.gov.tubitak.uekae.esya.api.signature.config.ConfigConstants.*;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_PROFILE;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_VALIDATION_PROFILE;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.config.ConfigConstants.TAG_VALIDATORS;

/**
 * @author ahmety
 * date: Dec 4, 2009
 */
public class ValidationConfig extends BaseConfigElement
{
    private static Logger logger = LoggerFactory.getLogger(ValidationConfig.class);

    //old
    //private String mCertificateValidationPolicyFile;
    //private ValidationPolicy validationPolicy;

    // new
    //private Map<String, String> mCertificateValidationPolicyFiles = new HashMap<String, String>();
    private CertValidationPolicies mCertificateValidationPolicies;

    private String mFolder;

    private int mGracePeriodInSeconds;
    private int mLastRevocationPeriodInSeconds;
    private int mSignatureTimeToleranceInSeconds;
    private TurkishESigProfile mValidationProfile = null;

    private boolean mForceStrictReferences = false;
    private boolean mUseValidationDataPublishedAfterCreation = false;
    private boolean mTrustSigningTime = false;

    boolean validateCertificateBeforeSigning = false;
    boolean validateTimestampWhileSigning = false;

    private boolean mCheckPolicyURI = true;

    private Map<SignatureType, SignatureProfileValidationConfig> mProfiles = new HashMap<SignatureType, SignatureProfileValidationConfig>();
    private List<SignatureType> mProfileInheritanceCalculated = new ArrayList<SignatureType>();

    /*
    public ValidationConfig(String aCertificateValidationPolicyFile, ValidationPolicy aValidationPolicy, long aGracePeriodInSeconds, long aLastRevocationPeriodInSeconds, boolean aForceStrictReferences, boolean aUseValidationDataPublishedAfterCreation, boolean aCheckPolicyURI, Map<SignatureType, SignatureProfileValidationConfig> aProfiles)
    {
        mCertificateValidationPolicyFile = aCertificateValidationPolicyFile;

        validationPolicy = aValidationPolicy;
        validationPolicies = new CertValidationPolicies();
        validationPolicies.register(null, validationPolicy);

        mGracePeriodInSeconds = (int)aGracePeriodInSeconds;
        mLastRevocationPeriodInSeconds = (int)aLastRevocationPeriodInSeconds;
        mForceStrictReferences = aForceStrictReferences;
        mUseValidationDataPublishedAfterCreation = aUseValidationDataPublishedAfterCreation;
        mCheckPolicyURI = aCheckPolicyURI;
        mProfiles = aProfiles;
    } */

    public ValidationConfig(Map<String, String> aCertificateValidationPolicyFile,
                            CertValidationPolicies aValidationPolicy,
                            long aGracePeriodInSeconds,
                            long aLastRevocationPeriodInSeconds,
                            long aSignatureTimeToleranceInSeconds,
                            TurkishESigProfile aValidationProfile,
                            boolean aForceStrictReferences,
                            boolean aUseValidationDataPublishedAfterCreation,
                            boolean aValidateCertificateBeforeSigning,
                            boolean aTrustSigningTime,
                            boolean aCheckPolicyURI,
                            Map<SignatureType, SignatureProfileValidationConfig> aProfiles)
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

    public ValidationConfig(Element aElement) throws tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException
    {
        super(aElement);
        init(aElement);
    }

    public ValidationConfig(Element aElement, String aFolder) throws tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException
    {
        super(aElement);
        mFolder = aFolder;
        init(aElement);
    }

    public void init(Element aElement) throws tr.gov.tubitak.uekae.esya.api.signature.config.ConfigurationException {
        if (aElement!=null){
            //mCertificateValidationPolicyFile = getChildText(Constants.NS_MA3, TAG_CERTIFICATE_VALIDATION_POLICY);

            Element[] policyNodes = tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil.selectNodes(aElement.getFirstChild(), NS_MA3, TAG_CERTIFICATE_VALIDATION_POLICY);
            mCertificateValidationPolicies = new CertValidationPolicies();
            for (Element policyElm : policyNodes){
                String path = policyElm.getTextContent().trim();

                if(new File(path).exists() == false && !StringUtil.isNullorEmpty(mFolder)){
                    String tmpPath = mFolder + "\\" + path;
                    if(new File(tmpPath).exists() == true)
                        path = tmpPath;
                }


                String certType = policyElm.getAttribute(ATTR_FOR);
                //CertValidationPolicies.CertificateType type = CertValidationPolicies.getCertificateType(certType);
                /*if (mCertificateValidationPolicyFiles.containsKey(certType)){
                    throw new ConfigurationException("Multiple certificate policies defined for type "+type);
                }
                mCertificateValidationPolicyFiles.put(certType, path);  */
                try {
                    mCertificateValidationPolicies.register(certType, PolicyReader.readValidationPolicyFromURL(path));
                } catch (Exception x){
                    throw new ConfigurationException(x, "config.cantFind", path);
                }
            }


            Element validatorsElm = selectChildElement(Constants.NS_MA3, TAG_VALIDATORS);
            if (validatorsElm!=null){
                Element[] profileElmArr = XmlUtil.selectNodes(validatorsElm.getFirstChild(), Constants.NS_MA3, TAG_PROFILE);
                if (profileElmArr!=null){
                    for (Element profileElm : profileElmArr){
                        SignatureProfileValidationConfig vsp = new SignatureProfileValidationConfig(profileElm);
                        mProfiles.put(vsp.getType(), vsp);
                    }
                }
            }
            handleValidationInheritance();

            Integer gracePeriod = getChildInteger(ConfigConstants.TAG_GRACE_PERIOD_IN_SECONDS);
            if (gracePeriod==null)
                throw new ConfigurationException("config.notDefined", ConfigConstants.TAG_GRACE_PERIOD_IN_SECONDS);
            mGracePeriodInSeconds = gracePeriod;

            Integer lastRevocationPeriod  = getChildInteger(ConfigConstants.TAG_LAST_REVOCATION_PERIOD_IN_SECONDS);
            if (lastRevocationPeriod==null)
                throw new ConfigurationException("config.notDefined", ConfigConstants.TAG_LAST_REVOCATION_PERIOD_IN_SECONDS);

            mLastRevocationPeriodInSeconds = lastRevocationPeriod;

            if (mLastRevocationPeriodInSeconds<=mGracePeriodInSeconds)
                throw new ConfigurationException("config.inconsistentGracePeriod");

            Integer signatureTimeTolerance = getChildInteger(ConfigConstants.TAG_TOLERATE_SIGNING_TIME_IN_SECONDS);
            if (signatureTimeTolerance==null)
                signatureTimeTolerance = 300;
            mSignatureTimeToleranceInSeconds = signatureTimeTolerance;

            Boolean strictReferences  = getChildBoolean(ConfigConstants.TAG_FORCE_STRICT_REFERENCES);
            if (strictReferences!=null)
                mForceStrictReferences = strictReferences;

            Boolean trustSigningTime = getChildBoolean(ConfigConstants.TAG_TRUST_SIGNING_TIME);
            if(trustSigningTime!=null)
                mTrustSigningTime = trustSigningTime;

            Boolean validationDataAfterCreation = getChildBoolean(ConfigConstants.TAG_USE_VALIDATION_DATA_PUBLISHED_AFTER_CREATION);
            if (validationDataAfterCreation!=null)
                mUseValidationDataPublishedAfterCreation = validationDataAfterCreation;

            Boolean checkForPolicyURI = getChildBoolean(ConfigConstants.TAG_CHECK_POLICY_URI);
            if (checkForPolicyURI!=null)
                mCheckPolicyURI = checkForPolicyURI;

            Boolean validateCert = getChildBoolean(TAG_VALIDATE_CERT_BEFORE_SIGNING);
            if (validateCert!=null)
                validateCertificateBeforeSigning = validateCert;
            //logger.info("Validate certificate before signing : " + validateCertificateBeforeSigning);

            Boolean validateTS = getChildBoolean(TAG_VALIDATE_TS_WHILE_SIGNING);
            if (validateTS!=null)
                validateTimestampWhileSigning = validateTS;
            //logger.info("Validate timestamp while signing : " + validateTimestampWhileSigning);

            String validationProfileText = getChildText(NS_MA3, TAG_VALIDATION_PROFILE);
            if (validationProfileText != null)
                mValidationProfile = TurkishESigProfile.getSignatureProfileFromName(validationProfileText);;
        }
    }



    private void handleValidationInheritance() throws ConfigurationException {
        // check if any profile is missing
        SignatureType[] allTypes = SignatureType.values();
        for (SignatureType current : allTypes){
            if (!mProfiles.keySet().contains(current)){
                throw new ConfigurationException("config.missingValidationProfile", current.name());
            }
        }

        // check if any ancestor is missing
        for (SignatureProfileValidationConfig spvc : mProfiles.values()) {
            SignatureType type = spvc.getInheritValidatorsFrom();
            if ((type!=null) && (!mProfiles.keySet().contains(type))){
                throw new ConfigurationException("config.missingAncestorToInheritProfiles", spvc.getType(), type);
            }
        }

        // inherit ancestor validators
        for (SignatureType signatureType : mProfiles.keySet()){
            handleValidationInheritance(signatureType, new ArrayList<SignatureType>());
        }
    }

    private List<Class<? extends Validator>> handleValidationInheritance(SignatureType aType, List<SignatureType> aVisitedTypes)
        throws ConfigurationException
    {
        // cyclic dependency check
        if (aVisitedTypes.contains(aType)){
            throw new ConfigurationException("config.cyclicInheritanceForProfileValidators");
        }
        aVisitedTypes.add(aType);

        SignatureProfileValidationConfig config = mProfiles.get(aType);

        if (mProfileInheritanceCalculated.contains(aType)){
            return config.getValidators();
        }

        SignatureType toBeInherited = config.getInheritValidatorsFrom();
        if (toBeInherited != null){
            List<Class<? extends Validator>> additional = handleValidationInheritance(toBeInherited, aVisitedTypes);
            config.getValidators().addAll(0, additional);
        }

        mProfileInheritanceCalculated.add(aType);
        return config.getValidators();
    }

    public int getGracePeriodInSeconds()
    {
        return mGracePeriodInSeconds;
    }

    public TurkishESigProfile getValidationProfile() {
        return mValidationProfile;
    }

    public int getLastRevocationPeriodInSeconds()
    {
        return mLastRevocationPeriodInSeconds;
    }

    public int getSignatureTimeToleranceInSeconds() {

        if (mSignatureTimeToleranceInSeconds < MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS || mSignatureTimeToleranceInSeconds > MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS) {
            String errorMessage = "Signing time tolerance value must be between the values" + MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS + " and " + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "!" + "The value" + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "corresponds to 1 day!";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        return mSignatureTimeToleranceInSeconds;
    }

    public boolean isForceStrictReferences()
    {
        return mForceStrictReferences;
    }

    public boolean isTrustSigningTime()
    {
        return mTrustSigningTime;
    }

    public boolean isUseValidationDataPublishedAfterCreation()
    {
        return mUseValidationDataPublishedAfterCreation;
    }

    public boolean isCheckPolicyURI()
    {
        return mCheckPolicyURI;
    }

    public void setCheckPolicyURI(boolean aCheckPolicyURI)
    {
        mCheckPolicyURI = aCheckPolicyURI;
    }

    public SignatureProfileValidationConfig getProfile(SignatureType aType){
        return mProfiles.get(aType);
    }

    public void addProfile(SignatureType aType, SignatureProfileValidationConfig aProfile){
        mProfiles.put(aType, aProfile);
    }

    /**
     * @deprecated
     * @see #getCertValidationPolicies()
     *
    public String getCertificateValidationPolicyFile()
    {
        return mCertificateValidationPolicyFile;
    }

    **
     * @deprecated
     * @see #getCertValidationPolicies()
     *
    public ValidationPolicy getCertificateValidationPolicy() throws ESYAException {
        return getCertificateValidationPolicy(true);
    }

    **
     * @deprecated
     * @see #getCertValidationPolicies()
     *
    public ValidationPolicy getCertificateValidationPolicy(boolean useExternalResources) throws ESYAException {
        if (validationPolicy==null) {
            File file = new File(getCertificateValidationPolicyFile());
            boolean found = false;
            if(!file.exists()) {

                String certValPolFile = getCertificateValidationPolicyFile();
                // if policy file is on web
                if(certValPolFile.substring(0,4).equals("http")) {
                    try {
                        validationPolicy = PolicyReader.readValidationPolicyFromURL(certValPolFile);
                        found = true;
                    } catch (Exception e) {
                        throw new ESYAException("Cannot resolve policy on url" + getCertificateValidationPolicyFile(), e);
                    }
                }

                else {
                    if(mConfigFilePath==null) {
                            throw new ESYAException("Cannot find cert validation config file. Tried = " +
                                                getCertificateValidationPolicyFile());
                    }
                    else {
                        File fileConfig = new File(mConfigFilePath);
                        String absolutePath = fileConfig.getAbsolutePath();
                        String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
                        String valConfigPath = filePath + "/" + file.getName();
                        file = new File(valConfigPath);
                        if(!file.exists()) {
                                throw new ESYAException("Cannot find cert validation config file. Tried = " + valConfigPath + " and " +
                                                    getCertificateValidationPolicyFile());
                        }
                    }
                }

            }
            if(!found)
                validationPolicy = PolicyReader.readValidationPolicy(file.getAbsolutePath());
        }

        if (!useExternalResources){
        	return validationPolicy.withoutFinders();
        }
        
        return validationPolicy;
    }
    */

    public void setCertificateValidationPolicyFile(String aCertValPolicyFile)
    {
        mCertificateValidationPolicies = new CertValidationPolicies();
        try {
            mCertificateValidationPolicies.register("", PolicyReader.readValidationPolicy(aCertValPolicyFile));
        } catch (Exception x){
            throw new ESYARuntimeException(I18n.translate("config.cantFind", aCertValPolicyFile), x);
        }
        //mCertificateValidationPolicyFiles.put("", aCertValPolicyFile);
    }

    public void setCertificateValidationPolicy(ValidationPolicy aCertValPolicy)
    {
        mCertificateValidationPolicies = new CertValidationPolicies();
        mCertificateValidationPolicies.register("", aCertValPolicy);
        //mCertificateValidationPolicyFiles.put("", aCertValPolicy.get);
    }

    public CertValidationPolicies getCertValidationPolicies() {
        return mCertificateValidationPolicies;
    }

    public void setCertValidationPolicies(CertValidationPolicies validationPolicies) {
        this.mCertificateValidationPolicies = validationPolicies;
    }

    public boolean isValidateCertificateBeforeSigning()
    {
        return validateCertificateBeforeSigning;
    }

    public void setValidateCertificateBeforeSigning(boolean aValidateCertificateBeforeSigning)
    {
        validateCertificateBeforeSigning = aValidateCertificateBeforeSigning;
    }

    public boolean isValidateTimestampWhileSigning()
    {
        return validateTimestampWhileSigning;
    }

    public void setValidateTimestampWhileSigning(boolean aValidateTimestampWhileSigning)
    {
        validateTimestampWhileSigning = aValidateTimestampWhileSigning;
    }
}
