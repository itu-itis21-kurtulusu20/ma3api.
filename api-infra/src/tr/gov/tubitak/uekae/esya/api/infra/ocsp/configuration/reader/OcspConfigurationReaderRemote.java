package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.reader;

import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/22/14
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class OcspConfigurationReaderRemote extends AbstractOcspConfigurationReader {

	public OcspConfigurationReaderRemote(){

	}

	@Override
	public void read(InputStream stream) throws Exception{
		super.read(stream);
		putValueByTagNameToMap(OcspConfigTags.URL);
	}

	public String getUrl(){
		return getStringValue(OcspConfigTags.URL);
	}


}
