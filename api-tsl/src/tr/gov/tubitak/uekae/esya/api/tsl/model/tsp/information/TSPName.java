package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.information;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation.TSLName;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSPName extends BaseElement {
	private List<TSLName> tspNames = new ArrayList<TSLName>();

	public TSPName(Document document, List<TSLName> iTSPNames) {
		super(document);
		tspNames = iTSPNames;
		addLineBreak();
		for (TSLName tspName : tspNames) {
			mElement.appendChild(tspName.getElement());
			addLineBreak();
		}
	}

	public TSPName(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TSPName(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				tspNames.add(new TSLName(element));
			}
		} else {
			throw new TSLException(Constants.TAG_TSPNAME
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllTSPNames() {
		RemoveAll();// mElement.RemoveAll();
		tspNames.clear();
		addLineBreak();
	}

	public void addTSPName(TSLName iTSLName) {
		tspNames.add(iTSLName);
		mElement.appendChild(iTSLName.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSPNAME;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSLName> getTSPNames() {
		return tspNames;
	}

	public TSLName TSPNameAt(int pos) {
		if (pos >= tspNames.size()) {
			return null;
		} else {
			return tspNames.get(pos);
		}
	}
}
