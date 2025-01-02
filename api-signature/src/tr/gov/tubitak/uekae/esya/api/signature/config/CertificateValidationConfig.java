package tr.gov.tubitak.uekae.esya.api.signature.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.common.util.StringUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.signature.certval.CertValidationPolicies;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static tr.gov.tubitak.uekae.esya.api.signature.config.ConfigConstants.*;

/**
 * @author ayetgin
 */
public class CertificateValidationConfig extends BaseConfigElement
{
    public static final int DEFAULT_SIGNING_TIME_TOLERANCE_IN_SECONDS = 300;
    public static final int DEFAULT_LAST_REVOCATION_PERIOD_IN_SECONDS = 172800;
    public static final int DEFAULT_GRACE_PERIOD_IN_SECONDS = 86400;

    private static Logger logger = LoggerFactory.getLogger(CertificateValidationConfig.class);


    Map<String, String> certificateValidationPolicyFiles = new HashMap<String, String>();

    long gracePeriodInSeconds = DEFAULT_GRACE_PERIOD_IN_SECONDS;

    long lastRevocationPeriodInSeconds = DEFAULT_LAST_REVOCATION_PERIOD_IN_SECONDS;

    long signingTimeToleranceInSeconds = DEFAULT_SIGNING_TIME_TOLERANCE_IN_SECONDS;

    TurkishESigProfile validationProfile = null;

    boolean useValidationDataPublishedAfterCreation = false;

    boolean validateCertificateBeforeSigning = false;

    boolean validateWithResourcesWithinSignature = false;

    String folder;

    public CertificateValidationConfig()
    {
    }

    public CertificateValidationConfig(Element aElement) throws ConfigurationException
    {
        super(aElement);
        init(aElement);
    }

    public CertificateValidationConfig(Element aElement, String aFolder) throws ConfigurationException
    {
        super(aElement);
        folder = aFolder;
        init(aElement);
    }



    private void init(Element aElement) throws ConfigurationException {
        if (aElement!=null){
            Element[] policyNodes = XmlUtil.selectNodes(aElement.getFirstChild(), NS_MA3, TAG_CERTIFICATE_VALIDATION_POLICY);
            for (Element policyElm : policyNodes){
                String path = policyElm.getTextContent().trim();

                if(new File(path).exists() == false && !StringUtil.isNullorEmpty(folder)){
                    String tmpPath = folder + "\\" + path;
                    if(new File(tmpPath).exists() == true)
                        path = tmpPath;
                }


                String certType = policyElm.getAttribute(ATTR_FOR);
                CertValidationPolicies.CertificateType type = CertValidationPolicies.getCertificateType(certType);
                if (certificateValidationPolicyFiles.containsKey(certType)){
                    throw new ConfigurationException("Multiple certificate policies defined for type "+type);
                }
                logger.debug("Validation policy file : "+path+" for: '"+type+"'");
                certificateValidationPolicyFiles.put(certType, path);
            }

            //certificateValidationPolicyFile = getChildText(NS_MA3, TAG_CERTIFICATE_VALIDATION_POLICY);


            Integer gracePeriod = getChildInteger(TAG_GRACE_PERIOD_IN_SECONDS);
            if (gracePeriod==null)
                throw new ConfigurationException(MessageFormat.format("'{0}' not defined in Signature configuration", TAG_GRACE_PERIOD_IN_SECONDS));
            gracePeriodInSeconds = gracePeriod;
            logger.debug("Grace period in seconds : " + gracePeriodInSeconds);

            Integer lastRevocationPeriod  = getChildInteger(TAG_LAST_REVOCATION_PERIOD_IN_SECONDS);
            if (lastRevocationPeriod==null)
                throw new ConfigurationException(MessageFormat.format("'{0}' not defined in Signature configuration", TAG_LAST_REVOCATION_PERIOD_IN_SECONDS));
            lastRevocationPeriodInSeconds = lastRevocationPeriod;
            if (lastRevocationPeriodInSeconds<=gracePeriodInSeconds)
                throw new ConfigurationException("Last Revocation Period must be higher than grace period!");
            logger.debug("Last revocation period seconds : " + lastRevocationPeriodInSeconds);

            Boolean validationDataAfterCreation = getChildBoolean(TAG_USE_VALIDATION_DATA_PUBLISHED_AFTER_CREATION);
            if (validationDataAfterCreation!=null)
                useValidationDataPublishedAfterCreation = validationDataAfterCreation;
            logger.debug("Use validation data published  after creation : " + useValidationDataPublishedAfterCreation);

            Boolean validateCert = getChildBoolean(TAG_VALIDATE_CERT_BEFORE_SIGNING);
            if (validateCert!=null)
                validateCertificateBeforeSigning = validateCert;
            logger.debug("Validate certificate before signing : " + validateCertificateBeforeSigning);

            Integer signingTimeTolerance = getChildInteger(TAG_TOLERATE_SIGNING_TIME_IN_SECONDS);
            if(signingTimeTolerance==null)
                signingTimeTolerance = 300;
            signingTimeToleranceInSeconds = signingTimeTolerance;
            logger.debug("Signing time tolerance in seconds: " + signingTimeToleranceInSeconds);

            String validationProfileText = getChildText(NS_MA3,TAG_VALIDATION_PROFILE);
            if(validationProfileText != null) {
                logger.debug("Validation Profile: " + validationProfileText);
                validationProfile = TurkishESigProfile.getSignatureProfileFromName(validationProfileText);
            }
        }
    }


    public Map<String, String> getCertificateValidationPolicyFile()
    {
        return certificateValidationPolicyFiles;
    }

    public void setCertificateValidationPolicyFile(Map<String, String> aCertificateValidationPolicyFiles)
    {
        certificateValidationPolicyFiles = aCertificateValidationPolicyFiles;
    }

    public long getGracePeriodInSeconds()
    {
        return gracePeriodInSeconds;
    }


    public long getSigningTimeToleranceInSeconds()
    {
        if (signingTimeToleranceInSeconds < MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS || signingTimeToleranceInSeconds > MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS) {
            String errorMessage = "Signing time tolerance value must be between the values" + MIN_SIGNING_TIME_TOLERANCE_IN_SECONDS + " and " + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "!" + "The value" + MAX_SIGNING_TIME_TOLERANCE_IN_SECONDS + "corresponds to 1 day!";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        return signingTimeToleranceInSeconds;
    }

    public TurkishESigProfile getValidationProfile()
    {
        return validationProfile;
    }

    public void setValidationProfile(TurkishESigProfile aTurkishESigProfile)
    {
        validationProfile = aTurkishESigProfile;
    }

    public void setGracePeriodInSeconds(long aGracePeriodInSeconds)
    {
        gracePeriodInSeconds = aGracePeriodInSeconds;
    }

    public long getLastRevocationPeriodInSeconds()
    {
        return lastRevocationPeriodInSeconds;
    }

    public void setLastRevocationPeriodInSeconds(long aLastRevocationPeriodInSeconds)
    {
        lastRevocationPeriodInSeconds = aLastRevocationPeriodInSeconds;
    }

    public boolean isUseValidationDataPublishedAfterCreation()
    {
        return useValidationDataPublishedAfterCreation;
    }

    public void setUseValidationDataPublishedAfterCreation(boolean aUseValidationDataPublishedAfterCreation)
    {
        useValidationDataPublishedAfterCreation = aUseValidationDataPublishedAfterCreation;
    }

    public boolean isValidateCertificateBeforeSigning()
    {
        return validateCertificateBeforeSigning;
    }

    public void setValidateCertificateBeforeSigning(boolean aValidateCertificateBeforeSigning)
    {
        validateCertificateBeforeSigning = aValidateCertificateBeforeSigning;
    }

    public boolean isValidateWithResourcesWithinSignature() {
        return validateWithResourcesWithinSignature;
    }

    public void setValidateWithResourcesWithinSignature(boolean validateWithResourcesWithinSignature) {
        this.validateWithResourcesWithinSignature = validateWithResourcesWithinSignature;
    }
}
