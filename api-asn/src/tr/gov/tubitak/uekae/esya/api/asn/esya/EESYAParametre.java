package tr.gov.tubitak.uekae.esya.api.asn.esya;

import tr.gov.tubitak.uekae.esya.api.asn.BaseASNWrapper;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.asn.esya.ESYAParametre;

/**
 * User: zeldal.ozdemir
 * Date: 2/3/11
 * Time: 11:46 AM
 */
public class EESYAParametre extends BaseASNWrapper<ESYAParametre>{
    public EESYAParametre(ESYAParametre aObject) {
        super(aObject);
    }

    public EESYAParametre(byte[] aBytes) throws ESYAException {
        super(aBytes, new ESYAParametre());
    }

    public EESYAParametre(String paramName, byte[] paramValue) {
        super(new ESYAParametre(paramName, paramValue));
    }

    public String getParamName() {
    	if(mObject.paramName != null)
			return mObject.paramName.value;
		return null;
	}

    public byte[] getParamValue() {
    	if(mObject.paramValue != null)
			return mObject.paramValue.value;
		return null;
	}

}
