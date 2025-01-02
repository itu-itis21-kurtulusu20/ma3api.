package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class PointersToOtherTSL extends BaseElement {
	List<OtherTSLPointer> otherTSLPointerList = new ArrayList<OtherTSLPointer>();

	public PointersToOtherTSL(Document document,
			List<OtherTSLPointer> iOtherTSLPointerList) {
		super(document);
		otherTSLPointerList = iOtherTSLPointerList;
		addLineBreak();
		for (OtherTSLPointer otherTslPointer : otherTSLPointerList) {
			mElement.appendChild(otherTslPointer.getElement());
			addLineBreak();
		}
	}

	public PointersToOtherTSL(Document document) {
		super(document);
		addLineBreak();
	}

	public PointersToOtherTSL(Element aElement) throws TSLException,
			XPathExpressionException, ESYAException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				otherTSLPointerList.add(new OtherTSLPointer(element));
			}
		} else {
			throw new TSLException(Constants.TAG_OTHERTSLPOINTER
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void addTSLPointer(OtherTSLPointer tslPointer) {
		otherTSLPointerList.add(tslPointer);
		mElement.appendChild(tslPointer.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_POINTERSTOOTHERTSL;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<OtherTSLPointer> getOtherTSLPointerList() {
		return otherTSLPointerList;
	}

	public OtherTSLPointer OtherTSLPointerAt(int pos) {
		if (pos >= otherTSLPointerList.size()) {
			return null;
		} else {
			return otherTSLPointerList.get(pos);
		}
	}
}
