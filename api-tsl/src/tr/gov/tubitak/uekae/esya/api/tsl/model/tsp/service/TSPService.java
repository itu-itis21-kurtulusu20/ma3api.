package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information.ServiceInformation;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TSPService extends BaseElement {
	private ServiceInformation serviceInformation;

	public TSPService(Document document, ServiceInformation iServiceInformation) {
		super(document);
		serviceInformation = iServiceInformation;
		addLineBreak();
		mElement.appendChild(serviceInformation.getElement());
		addLineBreak();
	}

	public TSPService(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TSPService(Element aElement) throws TSLException,
			XPathExpressionException, ESYAException {
		super(aElement);
		NodeList nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_SERVICEINFORMATION);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SERVICEINFORMATION	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SERVICEINFORMATION	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		serviceInformation = new ServiceInformation((Element) nodeList.item(0));
		// /
		nodeList = null;
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSPSERVICE;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public ServiceInformation getServiceInformation() {
		return serviceInformation;
	}

	public void setServiceInformation(ServiceInformation value) {
		if (serviceInformation != null) {
			mElement.replaceChild(value.getElement(),
					serviceInformation.getElement());
			serviceInformation = value;
		} else {
			serviceInformation = value;
			mElement.appendChild(serviceInformation.getElement());
			addLineBreak();
		}
	}

}
