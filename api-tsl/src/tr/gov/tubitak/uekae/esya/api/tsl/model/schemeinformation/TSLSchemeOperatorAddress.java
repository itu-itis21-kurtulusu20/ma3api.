package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TSLSchemeOperatorAddress extends BaseElement {
	private TSLPostalAddresses postalAddresses;
	private TSLElectronicAddress electronicAddress;

	public TSLSchemeOperatorAddress(Document document,
			TSLPostalAddresses iPostalAddresses,
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

	public TSLSchemeOperatorAddress(Element aElement) throws TSLException,
			XPathExpressionException {
		super(aElement);
		NodeList nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_POSTALADDRESSES);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_POSTALADDRESSES+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_POSTALADDRESSES+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		postalAddresses = new TSLPostalAddresses((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_ELECTRONICADDRESS);
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

	public TSLElectronicAddress getElectronic() {
		return electronicAddress;
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SCHEMEOPERATORADDRESS;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;

	}
}
