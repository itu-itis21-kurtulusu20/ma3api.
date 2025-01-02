package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class OtherTSLPointer extends BaseElement {
	private TSLServiceDigitalIdentities serviceDigitalIdentities;
	private TSLLocation mTSLLocation;
	private TSLAdditionalInformation additionalInformation;

	public OtherTSLPointer(Document document,
			TSLServiceDigitalIdentities iServiceDigitalIdentities,
			TSLLocation iTslLocation,
			TSLAdditionalInformation iAdditionalInformation) {
		super(document);
		serviceDigitalIdentities = iServiceDigitalIdentities;
		mTSLLocation = iTslLocation;
		additionalInformation = iAdditionalInformation;

		addLineBreak();
		mElement.appendChild(serviceDigitalIdentities.getElement());
		addLineBreak();
		mElement.appendChild(mTSLLocation.getElement());
		addLineBreak();
		mElement.appendChild(additionalInformation.getElement());
		addLineBreak();
	}

	public OtherTSLPointer(Element aElement) throws TSLException,
			XPathExpressionException, ESYAException {
		super(aElement);
		NodeList nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX+ Constants.TAG_SERVICEDIGITALIDENTITIES);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITIES+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_SERVICEDIGITALIDENTITIES	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		serviceDigitalIdentities = new TSLServiceDigitalIdentities(	(Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_TSLLOCATION);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSLLOCATION+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSLLOCATION+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		mTSLLocation = new TSLLocation((Element) nodeList.item(0));
		// /
		nodeList = null;
		// /
		nodeList =  aElement.getElementsByTagName(Constants.TSL_PREFIX	+ Constants.TAG_TSLADDITIONALINFORMATION);
		if (nodeList == null || nodeList.getLength() == 0) {
			throw new TSLException(Constants.TAG_TSLADDITIONALINFORMATION	+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
		if (nodeList.getLength() > 1) {
			throw new TSLException(Constants.TAG_TSLADDITIONALINFORMATION	+ TSL_DIL.NODE_MORE_THAN_ONE);
		}

		additionalInformation = new TSLAdditionalInformation((Element) nodeList.item(0));
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_OTHERTSLPOINTER;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public TSLServiceDigitalIdentities getServiceDigitalIdentities() {
		return serviceDigitalIdentities;
	}

	public TSLLocation getTslLocation() {
		return mTSLLocation;
	}

	public TSLAdditionalInformation getAdditionalInformation() {
		return additionalInformation;
	}
}
