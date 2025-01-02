package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.information;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TSPInformation extends BaseElement {
	private TSPName tspName;
	private TSPTradeName tspTradeName;
	private TSPAddress tspAddress;
	private TSPInformationURI tspInformationUri;

	public TSPInformation(Document document, TSPName iTSPName,
			TSPTradeName iTSPTradeName, TSPAddress iTSPAddress,
			TSPInformationURI iTSPInformationURI) {
		super(document);
		tspName = iTSPName;
		tspTradeName = iTSPTradeName;
		tspAddress = iTSPAddress;
		tspInformationUri = iTSPInformationURI;
		addLineBreak();
		mElement.appendChild(tspName.getElement());
		addLineBreak();
		mElement.appendChild(tspTradeName.getElement());
		addLineBreak();
		mElement.appendChild(tspAddress.getElement());
		addLineBreak();
		mElement.appendChild(tspInformationUri.getElement());
		addLineBreak();
	}

	public TSPInformation(Document document)

	{
		super(document);
		addLineBreak();
	}

	public TSPInformation(Element aElement) throws TSLException,
			XPathExpressionException {
		super(aElement);
		NodeList nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX + Constants.TAG_TSPNAME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSPNAME	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSPNAME	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		tspName = new TSPName((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_TSPTRADENAME);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSPTRADENAME	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSPTRADENAME	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		tspTradeName = new TSPTradeName((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_TSPADDRESS);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSPADDRESS	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSPADDRESS	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		tspAddress = new TSPAddress((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_TSPINFORMATIONURI);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSPINFORMATIONURI	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSPINFORMATIONURI+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		tspInformationUri = new TSPInformationURI((Element) nodeList.item(0));
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSPINFORMATION;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;

	}

	public TSPName getTspName() {
		return tspName;
	}

	public void setTspName(TSPName value) {
		if (tspName != null) {
			mElement.replaceChild(value.getElement(), tspName.getElement());
			tspName = value;
		} else {
			tspName = value;
			mElement.appendChild(tspName.getElement());
			addLineBreak();
		}
	}

	public TSPTradeName getTspTradeName() {
		return tspTradeName;
	}

	public void setTspTradeName(TSPTradeName value) {
		if (tspTradeName != null) {
			mElement.replaceChild(value.getElement(), tspTradeName.getElement());
			tspTradeName = value;
		} else {
			tspTradeName = value;
			mElement.appendChild(tspTradeName.getElement());
			addLineBreak();
		}
	}

	public TSPAddress getTspAddress() {
		return tspAddress;
	}

	public void setTspAddress(TSPAddress value) {
		if (tspAddress != null) {
			mElement.replaceChild(value.getElement(), tspAddress.getElement());
			tspAddress = value;
		} else {
			tspAddress = value;
			mElement.appendChild(tspAddress.getElement());
			addLineBreak();
		}
	}

	public TSPInformationURI getTspInformationUri() {
		return tspInformationUri;
	}

	public void setTspInformationUri(TSPInformationURI value) {
		if (tspInformationUri != null) {
			mElement.replaceChild(value.getElement(),
					tspInformationUri.getElement());
			tspInformationUri = value;
		} else {
			tspInformationUri = value;
			mElement.appendChild(tspInformationUri.getElement());
			addLineBreak();
		}
	}
}
