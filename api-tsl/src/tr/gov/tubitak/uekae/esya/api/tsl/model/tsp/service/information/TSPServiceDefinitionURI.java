package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation.TSLURI;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TSPServiceDefinitionURI extends BaseElement {
	private List<TSLURI> tspServiceDefinitionURIs = new ArrayList<TSLURI>();

	public TSPServiceDefinitionURI(Document document,
			List<TSLURI> iTSPServiceDefinitionURIs) {
		super(document);
		tspServiceDefinitionURIs = iTSPServiceDefinitionURIs;
		addLineBreak();
		for (TSLURI tspServiceDefinitionURI : tspServiceDefinitionURIs) {
			mElement.appendChild(tspServiceDefinitionURI.getElement());
			addLineBreak();
		}
	}

	public TSPServiceDefinitionURI(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TSPServiceDefinitionURI(Element aElement) throws TSLException

	{
		super(aElement);
		if (mElement.hasChildNodes()) {
			NodeList childList = aElement.getElementsByTagName("*");
			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				tspServiceDefinitionURIs.add(new TSLURI((Element) child));
			}
		} else {
			throw new TSLException(Constants.TAG_TSPSERVICEDEFINITONURI
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllServiceDefinitionURI() {
		RemoveAll();// mElement.RemoveAll();
		tspServiceDefinitionURIs.clear();
		addLineBreak();
	}

	public void addServiceDefinitionURI(TSLURI tspServiceDefinitonURI) {
		tspServiceDefinitionURIs.add(tspServiceDefinitonURI);
		mElement.appendChild(tspServiceDefinitonURI.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSPSERVICEDEFINITONURI;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSLURI> getTSPServiceDefinitionURIs() {
		return tspServiceDefinitionURIs;
	}

	public TSLURI TSPServiceDefinitionURIAt(int pos) {
		if (pos >= tspServiceDefinitionURIs.size()) {
			return null;
		} else {
			return tspServiceDefinitionURIs.get(pos);
		}
	}

	public String TSPServiceDefinitionURIStrAt(int pos) {
		if (pos >= tspServiceDefinitionURIs.size()) {
			return null;
		} else {
			return tspServiceDefinitionURIs.get(pos).getTslURI();
		}
	}
}
