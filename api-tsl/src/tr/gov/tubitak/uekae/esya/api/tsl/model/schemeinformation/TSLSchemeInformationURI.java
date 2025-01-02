package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TSLSchemeInformationURI extends BaseElement {
	private List<TSLURI> informationURIs = new ArrayList<TSLURI>();

	public TSLSchemeInformationURI(Document document,
			List<TSLURI> iInformationURIs) {
		super(document);
		addLineBreak();
		informationURIs = iInformationURIs;

		for (int i = 0; i < informationURIs.size(); i++) {
			mElement.appendChild(informationURIs.get(i).getElement());
			addLineBreak();
		}
	}

	public TSLSchemeInformationURI(Document document) {
		super(document);
		addLineBreak();
	}

	public TSLSchemeInformationURI(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			NodeList childList = aElement.getElementsByTagName("*");
			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				informationURIs.add(new TSLURI((Element) child));
			}
		} else {
			throw new TSLException(Constants.TAG_SCHEMEINFORMATIONURI
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllURIs() {
		informationURIs.clear();
		RemoveAll();// mElement.RemoveAll();
		addLineBreak();
	}

	public void addInformationURI(TSLURI tslURI) {
		informationURIs.add(tslURI);
		mElement.appendChild(tslURI.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SCHEMEINFORMATIONURI;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSLURI> getInformationURIs() {
		return informationURIs;
	}

	public TSLURI InformationURIAt(int pos) {
		if (pos >= informationURIs.size()) {
			return null;
		} else {
			return informationURIs.get(pos);
		}
	}

	public String InformationURIStrAt(int pos) {
		if (pos >= informationURIs.size()) {
			return null;
		} else {
			return informationURIs.get(pos).getTslURI();
		}
	}
}
