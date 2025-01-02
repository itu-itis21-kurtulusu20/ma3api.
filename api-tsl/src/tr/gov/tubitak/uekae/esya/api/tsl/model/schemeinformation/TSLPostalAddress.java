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
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLPostalAddress extends BaseElement {
	private String language;

	private String streetAddress, locality, postalCode, countryName;

	private Element streetAddressElement, localityElement, postalCodeElement,
			countryNameElement;

	public TSLPostalAddress(Document document, String iStreetAddress,
			String iLocality, String iPostalCode, String iCountryName,
			String iLanguage) throws TSLException {
		super(document);
		language = iLanguage;

		streetAddress = iStreetAddress;
		locality = iLocality;
		postalCode = iPostalCode;
		countryName = iCountryName;

		addLineBreak();
		streetAddressElement = insertTextElement(Constants.NS_TSL,
				Constants.TAG_STREETADDRESS, streetAddress);
		localityElement = insertTextElement(Constants.NS_TSL,
				Constants.TAG_LOCALITY, locality);
		postalCodeElement = insertTextElement(Constants.NS_TSL,
				Constants.TAG_POSTALCODE, postalCode);
		countryNameElement = insertTextElement(Constants.NS_TSL,
				Constants.TAG_COUNTRYNAME, countryName);

		if (mElement.getAttributes().getLength() > 0) {
			throw new TSLException("should not have a default attribute");
		}
		mElement.setAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR,
				language);
	}

	public TSLPostalAddress(Element aElement) throws TSLException,
			XPathExpressionException

	{
		super(aElement);
		NodeList nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX + Constants.TAG_STREETADDRESS);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_STREETADDRESS	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_STREETADDRESS+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		streetAddressElement = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_LOCALITY);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_LOCALITY	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_LOCALITY	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		localityElement = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX		+ Constants.TAG_POSTALCODE);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_POSTALCODE	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_POSTALCODE	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		postalCodeElement = (Element) nodeList.item(0);
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_COUNTRYNAME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_COUNTRYNAME	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_COUNTRYNAME	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		countryNameElement = (Element) nodeList.item(0);

		streetAddress = XmlUtil.getText(streetAddressElement);
		locality = XmlUtil.getText(localityElement);
		postalCode = XmlUtil.getText(postalCodeElement);
		countryName = XmlUtil.getText(countryNameElement);

		if (aElement.hasAttribute(Constants.XML_PREFIX
				+ Constants.TSL_LANG_ATTR)) {
			language = aElement.getAttribute(Constants.XML_PREFIX
					+ Constants.TSL_LANG_ATTR);
		} else {
			throw new TSLException("Language Attribute could not be found!");
		}
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_POSTALADDRESS;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;

	}

	public String getLanguage() {
		return language;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getLocality() {
		return locality;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return countryName;
	}
}
