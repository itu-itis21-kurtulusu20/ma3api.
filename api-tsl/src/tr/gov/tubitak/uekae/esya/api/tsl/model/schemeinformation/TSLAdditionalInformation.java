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

public class TSLAdditionalInformation extends BaseElement {
	List<TSLOtherInformation> otherInformationList = new ArrayList<TSLOtherInformation>();

	public TSLAdditionalInformation(Document document,
			List<TSLOtherInformation> iOtherInformationList) {
		super(document);
		otherInformationList = iOtherInformationList;
		addLineBreak();
		for (TSLOtherInformation tslOtherInformation : otherInformationList) {
			mElement.appendChild(tslOtherInformation.getElement());
			addLineBreak();
		}
	}

	public TSLAdditionalInformation(Document document) {
		super(document);
		addLineBreak();
	}

	public void addOtherInformation(TSLOtherInformation information) {
		otherInformationList.add(information);
		mElement.appendChild(information.getElement());
		addLineBreak();
	}

	public TSLAdditionalInformation(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				otherInformationList.add(new TSLOtherInformation(element));
			}
		} else {
			throw new TSLException(Constants.TAG_OTHERINFORMATION
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSLADDITIONALINFORMATION;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TSLOtherInformation> getOtherInformationList() {
		return otherInformationList;
	}

	public TSLOtherInformation OtherInformationAt(int pos) {
		if (pos >= otherInformationList.size()) {
			return null;
		} else {
			return otherInformationList.get(pos);
		}
	}

}
