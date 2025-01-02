package tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.element;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import tr.gov.tubitak.uekae.esya.api.common.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;
import tr.gov.tubitak.uekae.esya.api.infra.ocsp.configuration.OcspConfigTags;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ozgur.sucu
 * Date: 5/22/14
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractConfigElement implements IOcspConfigElement {
	protected Map<OcspConfigTags, String> map = new HashMap<OcspConfigTags, String>();

	public void addElement(OcspConfigTags tag, String value){
		map.put(tag, value);
	}

	public Element constructElement(Document document){
		NodeList elementsByTagName = document.getElementsByTagName(getRootTag().getTagName());
		Element rootElement=null;
		if (elementsByTagName!=null && elementsByTagName.getLength() >0){
			rootElement = (Element)elementsByTagName.item(0);
		}
		if (elementsByTagName.getLength() == 0 ||isMultiple()){
			rootElement = document.createElement(getRootTag().getTagName());
		}
		for (OcspConfigTags tags : map.keySet()){
			XmlUtil.addElement(rootElement, tags.getTagName(), map.get(tags));
		}
		return rootElement;
	}

	public String getElementValueByTagName(OcspConfigTags tag){
		return map.get(tag);
	}
}
