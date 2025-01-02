package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TrustServiceProviderList extends BaseElement {
	private List<TrustServiceProvider> tspList = new ArrayList<TrustServiceProvider>();

	public TrustServiceProviderList(Document document,
			List<TrustServiceProvider> iTSPList) {
		super(document);
		tspList = iTSPList;
		addLineBreak();
		for (TrustServiceProvider tsp : tspList) {
			mElement.appendChild(tsp.getElement());
			addLineBreak();
		}
	}

	public TrustServiceProviderList(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TrustServiceProviderList(Element aElement) throws TSLException,
			XPathExpressionException, ESYAException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			NodeList childList = aElement.getElementsByTagName(Constants.TSL_PREFIX + Constants.TAG_TRUSTSERVICEPROVIDER);
			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				tspList.add(new TrustServiceProvider((Element) child));
			}
		} else {
			throw new TSLException(Constants.TAG_TRUSTSERVICEPROVIDERLIST
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void addTrustServiceProvider(TrustServiceProvider iTSP) {
		tspList.add(iTSP);
		mElement.appendChild(iTSP.getElement());
		addLineBreak();
	}

	public void removeAllTSPs() {
		tspList.clear();
		RemoveAll();// mElement.RemoveAll();
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TRUSTSERVICEPROVIDERLIST;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public List<TrustServiceProvider> getTSPList() {
		return tspList;
	}

	public TrustServiceProvider TSPAt(int pos) {
		if (pos >= tspList.size()) {
			return null;
		} else {
			return tspList.get(pos);
		}
	}
}
