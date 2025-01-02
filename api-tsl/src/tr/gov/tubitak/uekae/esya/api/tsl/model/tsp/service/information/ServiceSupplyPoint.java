package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class ServiceSupplyPoint extends BaseElement {
	private String mServiceSupplyPoint;

	public ServiceSupplyPoint(Document document, String iSSPoint)

	{
		super(document);
		mServiceSupplyPoint = iSSPoint;
		mElement.setTextContent(mServiceSupplyPoint);
	}

	public ServiceSupplyPoint(Document document)

	{
		super(document);
	}

	public ServiceSupplyPoint(Element aElement) throws TSLException

	{
		super(aElement);
		mServiceSupplyPoint = XmlUtil.getText(aElement);
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SERVICESUPPLYPOINT;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;
	}

	public String getSSPoint() {
		return mServiceSupplyPoint;
	}

	public void setSSPoint(String value) {
		mServiceSupplyPoint = value;
		mElement.setTextContent(mServiceSupplyPoint);
	}
}
