package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.information;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation.TSLElectronicAddress;
import tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation.TSLPostalAddresses;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TSPAddress extends BaseElement {
	private TSLPostalAddresses postalAddresses;
	private TSLElectronicAddress electronicAddress;

	public TSPAddress(Document document, TSLPostalAddresses iPostalAddresses,
			TSLElectronicAddress iElectronicAddress) {
		super(document);
		postalAddresses = iPostalAddresses;
		electronicAddress = iElectronicAddress;

		addLineBreak();
		mElement.appendChild(postalAddresses.getElement());
		addLineBreak();
		mElement.appendChild(electronicAddress.getElement());
		addLineBreak();
	}

	public TSPAddress(Element aElement) throws XPathExpressionException,
			TSLException {
		super(aElement);
		NodeList nodeList = null;
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_POSTALADDRESSES);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_POSTALADDRESSES	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_POSTALADDRESSES	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		postalAddresses = new TSLPostalAddresses((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_ELECTRONICADDRESS);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_ELECTRONICADDRESS	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_ELECTRONICADDRESS	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		electronicAddress = new TSLElectronicAddress((Element) nodeList.item(0));
	}

	public TSLPostalAddresses getPostalAddresses() {
		return postalAddresses;
	}

	public TSLElectronicAddress getElectronicAddress() {
		return electronicAddress;
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSPADDRESS;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}
}
