package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLElectronicAddress extends BaseElement {
	private List<TSLURI> addresses = new ArrayList<TSLURI>();

	public TSLElectronicAddress(Document document, List<TSLURI> iAddresses) {
		super(document);
		addresses = iAddresses;
		addLineBreak();
		for (TSLURI address : addresses) {
			mElement.appendChild(address.getElement());
			addLineBreak();
		}
	}

	public TSLElectronicAddress(Document document) {
		super(document);
		addLineBreak();
	}

	public TSLElectronicAddress(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				addresses.add(new TSLURI(element));
			}
		} else {
			throw new TSLException(Constants.TAG_ELECTRONICADDRESS
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void addAddress(TSLURI iAddress) {
		addresses.add(iAddress);
		mElement.appendChild(iAddress.getElement());
		addLineBreak();
	}

	public void removeAllAddresses() {
		RemoveAll();// mElement.RemoveAll();
		addresses.clear();
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_ELECTRONICADDRESS;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSLURI> getAddresses() {
		return addresses;
	}

	public TSLURI AddressAt(int pos) {
		if (pos >= addresses.size()) {
			return null;
		} else {
			return addresses.get(pos);
		}
	}

	public String AddressStrAt(int pos) {
		if (pos >= addresses.size()) {
			return null;
		} else {
			return addresses.get(pos).getTslURI();
		}
	}
}
