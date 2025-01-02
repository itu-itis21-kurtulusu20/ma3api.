package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.reader;

import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element.IOcspConfigElement;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 7/9/14
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class OcspConfigurationReaderLocal extends AbstractOcspConfigurationReader {

	public OcspConfigurationReaderLocal(){

	}

	@Override
	public void read(InputStream stream) throws Exception{
		super.read(stream);
		putValueByTagNameToMap(OcspConfigTags.OCSPCONFIGNAME);
		putValueByTagNameToMap(OcspConfigTags.DECRYPTOR);
	}

	public String getConfigName(){
		return getStringValue(OcspConfigTags.OCSPCONFIGNAME);
	}

	public IOcspConfigElement getDecryptor(){
		return multiMap.get(OcspConfigTags.DECRYPTOR)!=null ? multiMap.get(OcspConfigTags.DECRYPTOR).get(0) : null;
	}

}
