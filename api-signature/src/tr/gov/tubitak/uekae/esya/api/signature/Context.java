package tr.gov.tubitak.uekae.esya.api.signature;

import tr.gov.tubitak.uekae.esya.api.signature.certval.ValidationInfoResolver;
import tr.gov.tubitak.uekae.esya.api.signature.config.Config;

import java.net.URI;
import java.util.Calendar;

/**
 * Runtime parameters for signature creation and validation
 * @author ayetgin
 */
public class Context implements Cloneable
{
    URI baseURI;
    Config config;
    Signable data;
    ValidationInfoResolver validationInfoResolver;
    Calendar validationTime;
    boolean isPAdES, isValidationTimeSigningTime;

	/**
     * Constructor for context for base URI equals to working dir: ".".
     * Config is also will be tried to read from default locations if not set
     * with Config#setConfig.
     * @see Config
     */
    public Context()
    {
        try {
            baseURI= new URI(".");
        }
        catch (Exception x){
            throw new SignatureRuntimeException("Should not happen!", x);
        }
    }

    public boolean isPAdES() {
    	return isPAdES;
    }

    public void setPAdES(boolean isPAdES) {
    	this.isPAdES = isPAdES;
    }

    public boolean isValidationTimeSigningTime() {
    	return isValidationTimeSigningTime;
    }

    public void setValidationTimeSigningTime(boolean isValidationTimeSigningTime) {
    	this.isValidationTimeSigningTime = isValidationTimeSigningTime;
    }
	
	public Context(URI aBaseURI)
    {
        baseURI = aBaseURI;
    }

    public Context(URI aBaseURI, Config aConfig)
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

    public Calendar getValidationTime() {
        return validationTime;
    }

    public void setValidationTime(Calendar validationTime) {
        this.validationTime = validationTime;
    }

    public URI getBaseURI()
    {
        return baseURI;
    }

    public void setBaseURI(URI aBaseURI)
    {
        baseURI = aBaseURI;
    }

    public Config getConfig()
    {
        if (config==null)
            config = new Config();
        return config;
    }

    public void setConfig(Config aConfig)
    {
        config = aConfig;
    }

    @Override
    public Context clone() {
        try  {
            return (Context)super.clone();
        } catch (Exception x){
            throw new SignatureRuntimeException(x);
        }
    }
}
