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

public class TSLSchemeName extends BaseElement {
	private List<TSLName> schemeNames = new ArrayList<TSLName>();

	public TSLSchemeName(Document document, List<TSLName> iSchemeNames) {
		super(document);
		addLineBreak();
		schemeNames = iSchemeNames;

		for (TSLName schemeName : schemeNames) {
			mElement.appendChild(schemeName.getElement());
			addLineBreak();
		}
	}

	public TSLSchemeName(Document document) {
		super(document);
		addLineBreak();
	}

	public TSLSchemeName(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				schemeNames.add(new TSLName(element));
			}
		} else {
			throw new TSLException(Constants.TAG_SCHEMENAME
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void addName(TSLName tslName) {
		schemeNames.add(tslName);
		mElement.appendChild(tslName.getElement());
		addLineBreak();
	}

	public void RemoveAllNames() {
		schemeNames.clear();
		RemoveAll();// mElement.RemoveAll();
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SCHEMENAME;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public List<TSLName> getSchemeNames() {
		return schemeNames;
	}
}
