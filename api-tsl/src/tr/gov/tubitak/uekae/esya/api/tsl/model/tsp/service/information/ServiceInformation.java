package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information;

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
import tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation.TSLServiceDigitalIdentity;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class ServiceInformation extends BaseElement {
	private Element serviceTypeIdentifier;
	private ServiceName serviceName;
	private TSLServiceDigitalIdentity serviceDigitalIdentity;
	private Element serviceStatus;
	private StatusStartingTime statusStartingTime;
	private SchemeServiceDefinitionURI schemeServiceDefinitionURI;
	private ServiceSupplyPoints serviceSupplyPoints;
	private TSPServiceDefinitionURI tspServiceDefinitionURI;

	public ServiceInformation(Document document) {
		super(document);
		addLineBreak();
	}

	public ServiceInformation(Element aElement)
			throws XPathExpressionException, TSLException, ESYAException {
		super(aElement);
		NodeList nodeList= aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_SERVICETYPEIDENTIFIER);
		// nodeList = aElement.SelectNodes(Constants.TSL_PREFIX +
		// Constants.TAG_SERVICETYPEIDENTIFIER, nsManager);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SERVICETYPEIDENTIFIER	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SERVICETYPEIDENTIFIER	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		serviceTypeIdentifier = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SERVICENAME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SERVICENAME		+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SERVICENAME	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		serviceName = new ServiceName((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SERVICEDIGITALIDENTITY);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITY	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITY	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		serviceDigitalIdentity = new TSLServiceDigitalIdentity(	(Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SERVICESTATUS);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SERVICESTATUS
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SERVICESTATUS
					+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		serviceStatus = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_STATUSSTARTINGTIME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_STATUSSTARTINGTIME	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_STATUSSTARTINGTIME	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		statusStartingTime = new StatusStartingTime((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SCHEMESERVICEDEFINITIONURI);
		if (nodeList != null && nodeList.getLength() != 0) {
			if (nodeList.getLength() > 1) {
				throw new TSLException(Constants.TAG_SCHEMESERVICEDEFINITIONURI+ TSL_DIL.NODE_MORE_THAN_ONE);
			}
			schemeServiceDefinitionURI = new SchemeServiceDefinitionURI((Element) nodeList.item(0));
		}
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_SERVICESUPPLYPOINTS);
		if (nodeList != null && nodeList.getLength() != 0) {
			if (nodeList.getLength() > 1) {
				throw new TSLException(Constants.TAG_SERVICESUPPLYPOINTS	+ TSL_DIL.NODE_MORE_THAN_ONE);
			}
			serviceSupplyPoints = new ServiceSupplyPoints(	(Element) nodeList.item(0));
		}
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_TSPSERVICEDEFINITONURI);
		if (serviceTypeIdentifier.getTextContent()	.contains("NationalRootCA-QC")) {
			if (nodeList == null || nodeList.getLength() == 0) {
				throw new TSLException(Constants.TAG_TSPSERVICEDEFINITONURI	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
			}
			if (nodeList.getLength() > 1) {
				throw new TSLException(Constants.TAG_TSPSERVICEDEFINITONURI	+ TSL_DIL.NODE_MORE_THAN_ONE);
			}

			tspServiceDefinitionURI = new TSPServiceDefinitionURI(	(Element) nodeList.item(0));
		} else {
			if (nodeList != null && nodeList.getLength() != 0) {
				if (nodeList.getLength() > 1) {
					throw new TSLException(Constants.TAG_SERVICESUPPLYPOINTS	+ TSL_DIL.NODE_MORE_THAN_ONE);
				}
				tspServiceDefinitionURI = new TSPServiceDefinitionURI((Element) nodeList.item(0));
			}
		}
	}

	public Element getServiceTypeIdentifier() {

		return serviceTypeIdentifier;
	}

	public void setServiceTypeIdentifier(Element value) {
		if (serviceTypeIdentifier != null) {
			mElement.replaceChild(value, serviceTypeIdentifier);
			serviceTypeIdentifier = value;
		} else {
			serviceTypeIdentifier = value;
			mElement.appendChild(serviceTypeIdentifier);
			addLineBreak();
		}
	}

	public ServiceName getServiceName() {
		return serviceName;
	}

	public void setServiceName(ServiceName value) {
		if (serviceName != null) {
			mElement.replaceChild(value.getElement(), serviceName.getElement());
			serviceName = value;
		} else {
			serviceName = value;
			mElement.appendChild(serviceName.getElement());
			addLineBreak();
		}
	}

	public TSLServiceDigitalIdentity getServiceDigitalIdentity() {

		return serviceDigitalIdentity;
	}

	public void setServiceDigitalIdentity(TSLServiceDigitalIdentity value) {
		if (serviceDigitalIdentity != null) {
			mElement.replaceChild(value.getElement(),
					serviceDigitalIdentity.getElement());
			serviceDigitalIdentity = value;
		} else {
			serviceDigitalIdentity = value;
			mElement.appendChild(serviceDigitalIdentity.getElement());
			addLineBreak();
		}
	}

	public Element getServiceStatus() {

		return serviceStatus;
	}

	public void setServiceStatus(Element value) {
		if (serviceStatus != null) {
			mElement.replaceChild(value, serviceStatus);
			serviceStatus = value;
		} else {
			serviceStatus = value;
			mElement.appendChild(serviceStatus);
			addLineBreak();
		}
	}

	public StatusStartingTime getStatusStartingTime() {

		return statusStartingTime;
	}

	public void setStatusStartingTime(StatusStartingTime value) {
		if (statusStartingTime != null) {
			mElement.replaceChild(value.getElement(),
					statusStartingTime.getElement());
			statusStartingTime = value;
		} else {
			statusStartingTime = value;
			mElement.appendChild(statusStartingTime.getElement());
			addLineBreak();
		}

	}

	public SchemeServiceDefinitionURI getSchemeServiceDefinitionUri() {

		return schemeServiceDefinitionURI;
	}

	public void setSchemeServiceDefinitionUri(SchemeServiceDefinitionURI value) {
		if (schemeServiceDefinitionURI != null) {
			mElement.replaceChild(value.getElement(),
					schemeServiceDefinitionURI.getElement());
			schemeServiceDefinitionURI = value;
		} else {
			schemeServiceDefinitionURI = value;
			mElement.appendChild(schemeServiceDefinitionURI.getElement());
			addLineBreak();
		}
	}

	public ServiceSupplyPoints getServiceSupplyPoints() {
		return serviceSupplyPoints;
	}

	public void setServiceSupplyPoints(ServiceSupplyPoints value) {
		if (serviceSupplyPoints != null) {
			mElement.replaceChild(value.getElement(),
					serviceSupplyPoints.getElement());
			serviceSupplyPoints = value;
		} else {
			serviceSupplyPoints = value;
			mElement.appendChild(serviceSupplyPoints.getElement());
			addLineBreak();
		}
	}

	public TSPServiceDefinitionURI getTspServiceDefinitionUri() {

		return tspServiceDefinitionURI;
	}

	public void setTspServiceDefinitionUri(TSPServiceDefinitionURI value) {
		if (tspServiceDefinitionURI != null) {
			mElement.replaceChild(value.getElement(),
					tspServiceDefinitionURI.getElement());
			tspServiceDefinitionURI = value;
		} else {
			tspServiceDefinitionURI = value;
			mElement.appendChild(tspServiceDefinitionURI.getElement());
			addLineBreak();
		}
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SERVICEINFORMATION;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}
}
