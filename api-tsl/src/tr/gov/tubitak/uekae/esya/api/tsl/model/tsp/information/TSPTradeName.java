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

public class TSPTradeName extends BaseElement {
	private List<TSLName> tspTradeNames = new ArrayList<TSLName>();

	public TSPTradeName(Document document, List<TSLName> iTSPTradeNames)

	{
		super(document);
		tspTradeNames = iTSPTradeNames;
		addLineBreak();
		for (TSLName tspTradeName : tspTradeNames) {
			mElement.appendChild(tspTradeName.getElement());
			addLineBreak();
		}
	}

	public TSPTradeName(Document document) {
		super(document);
		addLineBreak();
	}

	public TSPTradeName(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				tspTradeNames.add(new TSLName(element));
			}
		} else {
			throw new TSLException(Constants.TAG_TSPTRADENAME
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllTradeNames() {
		RemoveAll();// mElement.RemoveAll();
		tspTradeNames.clear();
		addLineBreak();
	}

	public void addTSPTradeName(TSLName iTSLTradeName) {
		tspTradeNames.add(iTSLTradeName);
		mElement.appendChild(iTSLTradeName.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSPTRADENAME;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSLName> getTSPTradeNames() {
		return tspTradeNames;
	}

	public TSLName TSPTradeNameAt(int pos) {
		if (pos >= tspTradeNames.size()) {
			return null;
		} else {
			return tspTradeNames.get(pos);
		}
	}
}
