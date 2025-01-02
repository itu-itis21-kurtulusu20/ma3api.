package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.creator;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/20/14
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IOcspConfigurationCreator {
	void createConfiguration(IOcspConfigHolder configHolder) throws ESYAException;
	String send(String url) throws ESYAException;
	void saveToFile(File file) throws ESYAException;
	byte [] getStreamAsByteArray() throws ESYAException;
}
