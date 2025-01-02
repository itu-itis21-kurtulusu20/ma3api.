package tr.gov.tubitak.uekae.esya.api.tsl;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation.TSLSchemeInformation;
import tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.TrustServiceProviderList;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSLUtil;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;

public class TrustServiceStatusList extends BaseElement {
	private boolean mIsSigned = false;

	private TSLSchemeInformation schemeInformation;
	private TrustServiceProviderList tspList;
	private Element signature;

	public TrustServiceStatusList(Document document,
			TSLSchemeInformation iSchemeInformation,
			TrustServiceProviderList iTSPList) {
		super(document);
		generateAndSetId("ID");
		schemeInformation = iSchemeInformation;
		tspList = iTSPList;
		addLineBreak();
		mElement.appendChild(schemeInformation.getElement());
		addLineBreak();
		mElement.appendChild(tspList.getElement());
		addLineBreak();
		mElement.setAttribute(Constants.ATTR_TSL, Constants.NS_TSL);
		mElement.setAttribute(Constants.ATTR_XMLDSIG, Constants.NS_XMLDSIG);
		mElement.setAttribute(Constants.ATTR_ECC, Constants.NS_ECC);
		mElement.setAttribute(Constants.ATTR_TSLX, Constants.NS_TSLX);
		mElement.setAttribute(Constants.ATTR_XADES, Constants.NS_XADES);
		mElement.setAttribute(Constants.ATTR_ID, getId());
		mElement.setAttribute(Constants.ATTR_TSLTAG, Constants.TSLTAG);
	}

	public TrustServiceStatusList(Document document,
			TSLSchemeInformation iSchemeInformation) {
		super(document);
		generateAndSetId("ID");
		schemeInformation = iSchemeInformation;
		addLineBreak();
		mElement.appendChild(schemeInformation.getElement());
		addLineBreak();
		mElement.setAttribute(Constants.ATTR_TSL, Constants.NS_TSL);
		mElement.setAttribute(Constants.ATTR_XMLDSIG, Constants.NS_XMLDSIG);
		mElement.setAttribute(Constants.ATTR_ECC, Constants.NS_ECC);
		mElement.setAttribute(Constants.ATTR_TSLX, Constants.NS_TSLX);
		mElement.setAttribute(Constants.ATTR_XADES, Constants.NS_XADES);
		mElement.setAttribute(Constants.ATTR_ID, getId());
		mElement.setAttribute(Constants.ATTR_TSLTAG, Constants.TSLTAG);
	}

	public TrustServiceStatusList(Document document) {
		super(document);
		generateAndSetId("ID");
		addLineBreak();
		mElement.setAttribute(Constants.ATTR_TSL, Constants.NS_TSL);
		mElement.setAttribute(Constants.ATTR_XMLDSIG, Constants.NS_XMLDSIG);
		mElement.setAttribute(Constants.ATTR_ECC, Constants.NS_ECC);
		mElement.setAttribute(Constants.ATTR_TSLX, Constants.NS_TSLX);
		mElement.setAttribute(Constants.ATTR_XADES, Constants.NS_XADES);
		mElement.setAttribute(Constants.ATTR_ID, getId());
		mElement.setAttribute(Constants.ATTR_TSLTAG, Constants.TSLTAG);
	}

	public TrustServiceStatusList(Element aElement)
			throws XPathExpressionException, TSLException, ESYAException {
		super(aElement);
		
		NodeList nodeList = null;
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX + Constants.TAG_SCHEMEINFORMATION);
		schemeInformation = new TSLSchemeInformation((Element) nodeList.item(0));

		nodeList = null;
		nodeList = aElement.getElementsByTagName(Constants.TSL_PREFIX + Constants.TAG_TRUSTSERVICEPROVIDERLIST);
		if (nodeList != null && nodeList.getLength() != 0) {
			if (nodeList.getLength() > 1) {
				throw new TSLException(Constants.TAG_TRUSTSERVICEPROVIDERLIST + TSL_DIL.NODE_MORE_THAN_ONE);
			}
			tspList = new TrustServiceProviderList((Element) nodeList.item(0));
		}
		
		nodeList = null;
		nodeList = aElement.getElementsByTagName(Constants.DS_PREFIX + Constants.TAG_SIGNATURE);
		if (nodeList != null && nodeList.getLength() != 0) {
			if (nodeList.getLength() > 1) {
				throw new TSLException(Constants.TAG_SIGNATURE + TSL_DIL.NODE_MORE_THAN_ONE);
			}
			signature = (Element) nodeList.item(0);
			mIsSigned = true;
		}
	}

	public void ClearTSL() {
		RemoveAll();// mElement.RemoveAll();

		schemeInformation = null;
		tspList = null;
		signature = null;

		mIsSigned = false;
		addLineBreak();
	}

	public String getId() {
		return mId;
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TRUSTSERVICESTATUSLIST;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public TSLSchemeInformation getSchemeInformation() {
		return schemeInformation;
	}

	public void getSchemeInformation(TSLSchemeInformation value) {
		{
			if (schemeInformation != null) {
				mElement.replaceChild(value.getElement(),
						schemeInformation.getElement());
				schemeInformation = value;
			} else {
				schemeInformation = value;
				mElement.appendChild(schemeInformation.getElement());
				addLineBreak();
			}
		}
	}

	public TrustServiceProviderList getTSPList() {
		return tspList;
	}

	public void setTSPList(TrustServiceProviderList value) {
		{
			if (tspList != null) {
				mElement.replaceChild(value.getElement(), tspList.getElement());
				tspList = value;
			} else {
				tspList = value;
				mElement.appendChild(tspList.getElement());
				addLineBreak();
			}
		}
	}

	public Element getSignatureElement() {
		return signature;
	}

	public void setSignatureElement(Element value) {
		{
			if (signature != null) {
				mElement.replaceChild(value, signature);
				signature = value;
				mIsSigned = true;
			} else {
				signature = value;
				mElement.appendChild(signature);
				addLineBreak();
				mIsSigned = true;
			}
		}
	}

	public boolean isSigned() {
		return mIsSigned;
	}

}
