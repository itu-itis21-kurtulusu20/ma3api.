package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLPostalAddresses extends BaseElement {

	private List<TSLPostalAddress> postalAddresses = new ArrayList<TSLPostalAddress>();

	public TSLPostalAddresses(Document document,
			List<TSLPostalAddress> iPostalAddresses) {
		super(document);
		postalAddresses = iPostalAddresses;
		addLineBreak();
		for (TSLPostalAddress tslPostalAddress : iPostalAddresses) {
			mElement.appendChild(tslPostalAddress.getElement());
			addLineBreak();
		}
	}

	public TSLPostalAddresses(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TSLPostalAddresses(Element aElement) throws TSLException,
			XPathExpressionException {
		super(aElement);
		List<Element> childNodes = XmlUtil.selectChildElements(aElement);

		if (childNodes.size() < 1) {
			throw new TSLException(Constants.TAG_POSTALADDRESS
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		for (Element element : childNodes) {
			postalAddresses.add(new TSLPostalAddress(element));
		}
	}

	public void removeAllPostalAddresses() {

		postalAddresses.clear();
		RemoveAll();// mElement.RemoveAll();
		addLineBreak();
	}

	public void addPostalAddress(TSLPostalAddress iPostalAddress) {
		postalAddresses.add(iPostalAddress);
		mElement.appendChild(iPostalAddress.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_POSTALADDRESSES;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;

	}

	public List<TSLPostalAddress> getPostalAddresses() {
		return postalAddresses;
	}
}
