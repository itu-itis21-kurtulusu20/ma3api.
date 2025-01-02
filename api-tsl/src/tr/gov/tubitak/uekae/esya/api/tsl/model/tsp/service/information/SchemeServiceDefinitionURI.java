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

public class SchemeServiceDefinitionURI extends BaseElement {
	private List<TSLURI> schemeServiceDefinitionURIs = new ArrayList<TSLURI>();

	public SchemeServiceDefinitionURI(Document document,
			List<TSLURI> iSchemeServiceDefinitionURIs) {
		super(document);
		schemeServiceDefinitionURIs = iSchemeServiceDefinitionURIs;
		addLineBreak();
		for (TSLURI serviceDefinitionURI : schemeServiceDefinitionURIs) {
			mElement.appendChild(serviceDefinitionURI.getElement());
			addLineBreak();
		}
	}

	public SchemeServiceDefinitionURI(Document document)

	{
		super(document);
		addLineBreak();
	}

	public SchemeServiceDefinitionURI(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			NodeList childList = aElement.getElementsByTagName("*");
			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				schemeServiceDefinitionURIs.add(new TSLURI((Element) child));
			}
		} else {
			throw new TSLException(Constants.TAG_SCHEMESERVICEDEFINITIONURI
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllServiceDefinitionURI() {
		RemoveAll(); // mElement.RemoveAll();
		schemeServiceDefinitionURIs.clear();
		addLineBreak();
	}

	public void addSchemeServiceDefinitionURI(TSLURI schemeServiceDefinitonURI) {
		schemeServiceDefinitionURIs.add(schemeServiceDefinitonURI);
		mElement.appendChild(schemeServiceDefinitonURI.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SCHEMESERVICEDEFINITIONURI;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSLURI> getSchemeServiceDefinitionURIs() {
		return schemeServiceDefinitionURIs;
	}

	public TSLURI SchemeServiceDefinitionURIAt(int pos) {
		if (pos >= schemeServiceDefinitionURIs.size()) {
			return null;
		} else {
			return schemeServiceDefinitionURIs.get(pos);
		}
	}

	public String SchemeServiceDefinitionURIStrAt(int pos) {
		if (pos >= schemeServiceDefinitionURIs.size()) {
			return null;
		} else {
			return schemeServiceDefinitionURIs.get(pos).getTslURI();
		}
	}
}
