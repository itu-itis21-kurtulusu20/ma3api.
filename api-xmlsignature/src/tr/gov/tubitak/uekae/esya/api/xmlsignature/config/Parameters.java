package tr.gov.tubitak.uekae.esya.api.xmlsignature.config;

import org.w3c.dom.Element;

/**
 * @author ayetgin
 */
public class Parameters extends BaseConfigElement
{
    private boolean mWriteReferencedValidationDataToFileOnUpgrade;
    private boolean mAddTimestampValidationData;

    public Parameters()
    {
    }

    public Parameters(Element aElement)
    {
        super(aElement);
        mWriteReferencedValidationDataToFileOnUpgrade = getParamBoolean(ConfigConstants.TAG_WRITE_REFERENCED_PKI_DATA);
        mAddTimestampValidationData = getParamBoolean(ConfigConstants.TAG_ADD_TIMESTAMP_PKI_DATA);
    }

    public boolean isWriteReferencedValidationDataToFileOnUpgrade()
    {
        return mWriteReferencedValidationDataToFileOnUpgrade;
    }
    
    public boolean isAddTimestampValidationData(){
    	return mAddTimestampValidationData;
    }
    
    public void setAddTimestampValidationData(boolean aAddTimestampValidationData){
    	mAddTimestampValidationData = aAddTimestampValidationData;
    }

    public void setWriteReferencedValidationDataToFileOnUpgrade(boolean aWriteReferencedValidationDataToFileOnUpgrade)
    {
        mWriteReferencedValidationDataToFileOnUpgrade = aWriteReferencedValidationDataToFileOnUpgrade;
    }
}
