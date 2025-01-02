package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TSPServices extends BaseElement {
	private List<TSPService> tspServiceList = new ArrayList<TSPService>();

	public TSPServices(Document document, List<TSPService> iTSPServiceList) {
		super(document);
		tspServiceList = iTSPServiceList;
		addLineBreak();
		for (TSPService tspService : tspServiceList) {
			mElement.appendChild(tspService.getElement());
			addLineBreak();
		}
	}

	public TSPServices(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TSPServices(Element aElement) throws TSLException,
			XPathExpressionException, ESYAException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			NodeList childList = aElement.getElementsByTagName(Constants.TSL_PREFIX + Constants.TAG_TSPSERVICE);
			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				tspServiceList.add(new TSPService((Element) child));
			}
		} else {
			throw new TSLException(Constants.TAG_TSPSERVICES
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void addTSPService(TSPService iTSPService) {
		tspServiceList.add(iTSPService);
		mElement.appendChild(iTSPService.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSPSERVICES;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSPService> getTSPSerivceList() {
		return tspServiceList;
	}

	public TSPService TSPServiceAt(int pos) {
		if (pos >= tspServiceList.size()) {
			return null;
		} else {
			return tspServiceList.get(pos);
		}
	}
}
