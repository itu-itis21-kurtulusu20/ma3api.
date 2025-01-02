package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation.TSLName;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class ServiceName extends BaseElement {
	private List<TSLName> serviceNames = new ArrayList<TSLName>();

	public ServiceName(Document document, List<TSLName> iServiceNames) {
		super(document);
		serviceNames = iServiceNames;
		addLineBreak();
		for (TSLName tspName : serviceNames) {
			mElement.appendChild(tspName.getElement());
			addLineBreak();
		}
	}

	public ServiceName(Document document)

	{
		super(document);
		addLineBreak();
	}

	public ServiceName(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				serviceNames.add(new TSLName(element));
			}
		} else {
			throw new TSLException(Constants.TAG_SERVICENAME
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllServiceNames() {
		RemoveAll();// mElement.RemoveAll();
		serviceNames.clear();
		addLineBreak();
	}

	public void addServiceName(TSLName iServiceName) {
		serviceNames.add(iServiceName);
		mElement.appendChild(iServiceName.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SERVICENAME;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSLName> getServiceNames() {
		return serviceNames;
	}

	public TSLName ServiceNameAt(int pos) {
		if (pos >= serviceNames.size()) {
			return null;
		} else {
			return serviceNames.get(pos);
		}
	}
}
