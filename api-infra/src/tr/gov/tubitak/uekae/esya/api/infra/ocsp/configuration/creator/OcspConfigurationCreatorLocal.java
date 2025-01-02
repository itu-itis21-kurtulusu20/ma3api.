package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.creator;

import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.ConfigNameElement;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.DecryptorConfigElement;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 7/9/14
 * Time: 10:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class OcspConfigurationCreatorLocal extends AbstractOcspConfigurationCreator {

	public OcspConfigurationCreatorLocal() throws Exception {
		super();
	}

	public void setConfigName(String configName){
		if (configName!=null){
			new ConfigNameElement(configName).constructElement(doc);
		}
	}

	public void addDecryptor(DecryptorConfigElement decryptorConfigElement){
		 appendToConfig(decryptorConfigElement);
	}
}
