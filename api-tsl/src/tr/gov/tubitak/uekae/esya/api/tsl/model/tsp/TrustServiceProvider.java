package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp;

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
import tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.information.TSPInformation;
import tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.TSPServices;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TrustServiceProvider extends BaseElement {
	private TSPInformation tspInformation;
	private TSPServices tspServices;

	public TrustServiceProvider(Document document,
			TSPInformation iTSPInformation, TSPServices iTSPServices) {
		super(document);
		tspInformation = iTSPInformation;
		tspServices = iTSPServices;
		addLineBreak();
		mElement.appendChild(tspInformation.getElement());
		addLineBreak();
		mElement.appendChild(tspServices.getElement());
		addLineBreak();
	}

	public TrustServiceProvider(Element aElement) throws TSLException,
			XPathExpressionException, ESYAException {
		super(aElement);
		NodeList nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX + Constants.TAG_TSPINFORMATION);
		// nodeList = aElement.SelectNodes(Constants.TSL_PREFIX +
		// Constants.TAG_TSPINFORMATION, nsManager);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSPINFORMATION	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSPINFORMATION	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		tspInformation = new TSPInformation((Element) nodeList.item(0));
		// /
		nodeList = null;

		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_TSPSERVICES);
		// nodeList = aElement.SelectNodes(Constants.TSL_PREFIX +
		// Constants.TAG_TSPSERVICES, nsManager);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSPSERVICES	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSPSERVICES+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		tspServices = new TSPServices((Element) nodeList.item(0));
		// /
		// tspInformation = new
		// TSPInformation(XmlUtil.getNextElement(aElement.FirstChild));
		// tspServices = new
		// TSPServices(XmlUtil.getNextElement(tspInformation.Element.NextSibling));
	}

	public TrustServiceProvider(Document document)

	{
		super(document);
		addLineBreak();
	}

	public void RemoveAllChildNodes() {
		RemoveAll();// mElement.RemoveAll();
		tspInformation = null;
		tspServices = null;
		addLineBreak();
	}

	public void addTSPInformation(TSPInformation iTSPInformation) {
		tspInformation = iTSPInformation;
		mElement.appendChild(tspInformation.getElement());
		addLineBreak();
	}

	public void addTSPServices(TSPServices iTSPServices) {
		tspServices = iTSPServices;
		mElement.appendChild(tspServices.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TRUSTSERVICEPROVIDER;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public TSPInformation getTSPInformation() {
		return tspInformation;
	}

	public TSPServices getTSPServices() {
		return tspServices;
	}
}
