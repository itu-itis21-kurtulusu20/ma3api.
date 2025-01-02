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

public class TSLSchemeTypeCommunityRules extends BaseElement {
	private List<TSLURI> uriList = new ArrayList<TSLURI>();

	public TSLSchemeTypeCommunityRules(Document document, List<TSLURI> iURIList) {
		super(document);
		uriList = iURIList;

		addLineBreak();
		for (TSLURI tsluri : uriList) {
			mElement.appendChild(tsluri.getElement());
			addLineBreak();
		}
	}

	public TSLSchemeTypeCommunityRules(Document document) {
		super(document);
		addLineBreak();
	}

	public TSLSchemeTypeCommunityRules(Element aElement) throws TSLException {
		super(aElement);
		if (aElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);
			for (Element element : childNodes) {
				uriList.add(new TSLURI(element));
			}
		} else {
			throw new TSLException(Constants.TAG_SCHEMETYPECOMMUNITYRULES
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllURIs() {
		uriList.clear();
		RemoveAll();// mElement.RemoveAll();
		addLineBreak();
	}

	public void addRuleURI(TSLURI iTSLURI) {
		uriList.add(iTSLURI);
		mElement.appendChild(iTSLURI.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SCHEMETYPECOMMUNITYRULES;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public List<TSLURI> getRuleURIs() {
		return uriList;
	}

	public TSLURI RuleURIAt(int pos) {
		if (pos >= uriList.size()) {
			return null;
		} else {
			return uriList.get(pos);
		}
	}

	public String RuleURIAtStr(int pos) {
		if (pos >= uriList.size()) {
			return null;
		} else {
			return uriList.get(pos).getTslURI();
		}
	}
}
