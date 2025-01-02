package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLOtherInformation extends BaseElement {
	private Element informationElement;

	public TSLOtherInformation(Document document, Element iInformationElement) {
		super(document);
		informationElement = iInformationElement;
		addLineBreak();
		mElement.appendChild(informationElement);
		addLineBreak();
	}

	public TSLOtherInformation(Document document)

	{
		super(document);
	}

	public TSLOtherInformation(Element aElement) throws TSLException {
		super(aElement);
		informationElement = XmlUtil.getNextElement(aElement.getFirstChild());
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_OTHERINFORMATION;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;

	}

	public Element getInformationElement() {
		return informationElement;
	}
}
