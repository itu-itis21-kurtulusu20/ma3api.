package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/20/14
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IOcspConfigElement {
	Element constructElement(Document document);
	void addElement(OcspConfigTags elementName, String value) throws Exception;
	OcspConfigTags getRootTag();
	OcspConfigTags[] getElementNames();
	String getElementValueByTagName(OcspConfigTags tag);
	boolean isMultiple();
}
