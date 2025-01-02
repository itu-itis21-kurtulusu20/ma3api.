package tr.gov.tubitak.uekae.esya.api.signature.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import static tr.gov.tubitak.uekae.esya.api.signature.config.ConfigConstants.*;

/**
 * @author ayetgin
 */
public class Parameters extends BaseConfigElement
{
    private static Logger logger = LoggerFactory.getLogger(Parameters.class);


    boolean checkPolicyUri = false;   // keep

    boolean forceStrictReferenceUse = true; // keep

    boolean validateTimestampWhileSigning = true; // keep

    boolean trustSigningTime = false;   // default false, what does 'keep' mean?

    boolean writeReferencedValidationDataToFileOnUpgrade = false; // keep

    boolean useCAdESATSv2 = false; // keep
    
    public Parameters()
    {
    }

    public Parameters(Element aElement) throws ConfigurationException
    {
        super(aElement);

        if (aElement!=null){

            Boolean strictReferences  = getChildBoolean(TAG_FORCE_STRICT_REFERENCES);
            if (strictReferences!=null)
                forceStrictReferenceUse = strictReferences;
            logger.debug("Force strict reference use : " + forceStrictReferenceUse);

            Boolean checkForPolicyURI = getChildBoolean(TAG_CHECK_POLICY_URI);
            if (checkForPolicyURI!=null)
                checkPolicyUri = checkForPolicyURI;
            logger.debug("Check for policy URI : "+checkPolicyUri);

            Boolean validateTS = getChildBoolean(TAG_VALIDATE_TS_WHILE_SIGNING);
            if (validateTS!=null)
                validateTimestampWhileSigning = validateTS;
            logger.debug("Validate timestamp while signing : " + validateTimestampWhileSigning);

            Boolean trustSigningTime = getChildBoolean(TAG_TRUST_SIGNING_TIME);
            if(trustSigningTime!=null)
                this.trustSigningTime = trustSigningTime;
            logger.debug("Trust signing time : " + this.trustSigningTime);

            Boolean writeValidationData = getChildBoolean(TAG_WRITE_VALIDATIONDATA_ON_UPGRADE);
            if (writeValidationData!=null)
                writeReferencedValidationDataToFileOnUpgrade = writeValidationData;
            logger.debug("Write referenced validation data to file on upgrade : " + writeReferencedValidationDataToFileOnUpgrade);
            
            Boolean CAdESATSv2 = getChildBoolean(TAG_USE_CAdES_ATSv2);
            if (CAdESATSv2 != null)
            	useCAdESATSv2 = CAdESATSv2;
            logger.debug("Use archive timestamp v2 for ESA type : " + useCAdESATSv2);
        }

    }

    public boolean isCheckPolicyUri()
    {
        return checkPolicyUri;
    }

    public void setCheckPolicyUri(boolean aCheckPolicyUri)
    {
        checkPolicyUri = aCheckPolicyUri;
    }

    public boolean isForceStrictReferenceUse()
    {
        return forceStrictReferenceUse;
    }

    public void setForceStrictReferenceUse(boolean aForceStrictReferenceUse)
    {
        forceStrictReferenceUse = aForceStrictReferenceUse;
    }


    public boolean isValidateTimestampWhileSigning()
    {
        return validateTimestampWhileSigning;
    }

    public void setValidateTimestampWhileSigning(boolean aValidateTimestampWhileSigning)
    {
        validateTimestampWhileSigning = aValidateTimestampWhileSigning;
    }


    public boolean isTrustSigningTime()
    {
        return trustSigningTime;
    }

    public void setTrustSigningTime(boolean aTrustSigningTime)
    {
        trustSigningTime = aTrustSigningTime;
    }


    public boolean isWriteReferencedValidationDataToFileOnUpgrade()
    {
        return writeReferencedValidationDataToFileOnUpgrade;
    }

    public void setWriteReferencedValidationDataToFileOnUpgrade(boolean aWriteReferencedValidationDataToFileOnUpgrade)
    {
        writeReferencedValidationDataToFileOnUpgrade = aWriteReferencedValidationDataToFileOnUpgrade;
    }
    

    public boolean isUseCAdESATSv2()
    {
        return useCAdESATSv2;
    }

    public void setUseCAdESATSv2(boolean aUseCAdESATSv2)
    {
        useCAdESATSv2 = aUseCAdESATSv2;
    }
}
