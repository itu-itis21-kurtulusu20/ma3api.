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

public class TSLSchemeOperatorName extends BaseElement {
	private List<TSLName> operatorNames = new ArrayList<TSLName>();

	public TSLSchemeOperatorName(Document document, List<TSLName> iOperatorNames) {
		super(document);
		addLineBreak();
		operatorNames = iOperatorNames;

		for (int i = 0; i < operatorNames.size(); i++) {
			mElement.appendChild(operatorNames.get(i).getElement());
			addLineBreak();
		}
	}

	public TSLSchemeOperatorName(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TSLSchemeOperatorName(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			NodeList childList = aElement.getElementsByTagName("*");
			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				operatorNames.add(new TSLName((Element) child));
			}
		} else {
			throw new TSLException(Constants.TAG_SCHEMEOPERATORNAME
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllOperatorNames() {
		RemoveAll();// mElement.RemoveAll();
		operatorNames.clear();
		addLineBreak();
	}

	public void addOperatorName(TSLName tslName) {
		operatorNames.add(tslName);
		mElement.appendChild(tslName.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SCHEMEOPERATORNAME;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public List<TSLName> getOperatorNames() {
		return operatorNames;
	}

	public void setOperatorNames(List<TSLName> value) {
		RemoveAll();// mElement.RemoveAll();
		addLineBreak();
		operatorNames = value;

		for (int i = 0; i < operatorNames.size(); i++) {
			mElement.appendChild(operatorNames.get(i).getElement());
			addLineBreak();
		}
	}

}
