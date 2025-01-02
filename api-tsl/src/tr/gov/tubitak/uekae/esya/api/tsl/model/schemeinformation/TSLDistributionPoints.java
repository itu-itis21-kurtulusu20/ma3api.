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

public class TSLDistributionPoints extends BaseElement {
	List<TSLURI> distributionPoints = new ArrayList<TSLURI>();

	public TSLDistributionPoints(Document document,
			List<TSLURI> iDistributionPoints) {
		super(document);
		distributionPoints = iDistributionPoints;
		addLineBreak();
		for (TSLURI distributionPoint : distributionPoints) {
			mElement.appendChild(distributionPoint.getElement());
			addLineBreak();

		}
	}

	public TSLDistributionPoints(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TSLDistributionPoints(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				distributionPoints.add(new TSLURI(element));
			}
		} else {
			throw new TSLException(Constants.TAG_DISTRIBUTIONPOINTS
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllDistributionPoints() {
		distributionPoints.clear();
		RemoveAll();// mElement.RemoveAll();
		addLineBreak();
	}

	public void addDistributionPoint(TSLURI uri) {
		distributionPoints.add(uri);
		mElement.appendChild(uri.getElement());
		addLineBreak();
	}

	public void addDistributionPoint(String uriStr) {
		TSLURI uri = new TSLURI(mElement.getOwnerDocument(), uriStr);
		distributionPoints.add(uri);
		mElement.appendChild(uri.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_DISTRIBUTIONPOINTS;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;
	}

	public List<TSLURI> getDistributionPoints() {
		return distributionPoints;
	}

	public TSLURI DistributionPointAt(int pos) {
		if (pos >= distributionPoints.size()) {
			return null;
		} else {
			return distributionPoints.get(pos);
		}
	}

	public String DistributionPointStrAt(int pos) {
		if (pos >= distributionPoints.size()) {
			return null;
		} else {
			return distributionPoints.get(pos).getTslURI();
		}
	}
}
