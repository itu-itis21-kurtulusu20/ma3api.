package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class ServiceSupplyPoints extends BaseElement {
	List<String> ssPoints = new ArrayList<String>();
	List<ServiceSupplyPoint> ssPointElements = new ArrayList<ServiceSupplyPoint>();

	public ServiceSupplyPoints(Document document, List<String> iSSPoints)
			throws TSLException {
		super(document);
		addLineBreak();
		ssPoints = iSSPoints;
		for (String ssPoint : ssPoints) {
			Element serviceSupplyPoint = mDocument.createElementNS(
					Constants.NS_TSL, Constants.TSL_PREFIX
							+ Constants.TAG_SERVICESUPPLYPOINT);
			ssPointElements.add(new ServiceSupplyPoint(serviceSupplyPoint));
			serviceSupplyPoint.setTextContent(ssPoint);
			mElement.appendChild(serviceSupplyPoint);
			addLineBreak();
		}
	}

	// parameter order changed
	public ServiceSupplyPoints(List<ServiceSupplyPoint> iSSPointElements,
			Document document) {
		super(document);
		addLineBreak();
		ssPointElements = iSSPointElements;
		for (ServiceSupplyPoint ssPointElement : ssPointElements) {
			ssPointElements.add(ssPointElement);
			mElement.appendChild(ssPointElement.getElement());
			addLineBreak();
		}
	}

	public ServiceSupplyPoints(Document document)

	{
		super(document);
		addLineBreak();
	}

	public ServiceSupplyPoints(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				ssPointElements.add(new ServiceSupplyPoint(element));
				ssPoints.add(element.getTextContent());
			}
		} else {
			throw new TSLException(Constants.TAG_SERVICESUPPLYPOINTS
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllServiceSupplyPoints() {
		RemoveAll();// /mElement.RemoveAll();
		ssPoints.clear();
		ssPointElements.clear();
		addLineBreak();
	}

	public void addServiceSupplyPoint(String iSSPoint) throws TSLException {
		ssPoints.add(iSSPoint);
		Element serviceSupplyPoint = mDocument.createElementNS(
				Constants.NS_TSL, Constants.TSL_PREFIX
						+ Constants.TAG_SERVICESUPPLYPOINT);
		serviceSupplyPoint.setTextContent(iSSPoint);
		ssPointElements.add(new ServiceSupplyPoint(serviceSupplyPoint));
		mElement.appendChild(serviceSupplyPoint);
		addLineBreak();
	}

	public void addServiceSupplyPointElement(ServiceSupplyPoint ssPointElement) {
		ssPointElements.add(ssPointElement);
		ssPoints.add(ssPointElement.getSSPoint());
		mElement.appendChild(ssPointElement.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SERVICESUPPLYPOINTS;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<String> getServiceSupplyPointsStr() {
		return ssPoints;
	}

	public List<ServiceSupplyPoint> getServiceSupplyPointElements() {
		return ssPointElements;
	}
}
