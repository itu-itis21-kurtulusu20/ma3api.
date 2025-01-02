package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLLocation extends BaseElement {
	private String mTSLLocation;

	public TSLLocation(Document document, String iTSLLocation) {
		super(document);
		mTSLLocation = iTSLLocation;
		mElement.setTextContent(mTSLLocation);
	}

	public TSLLocation(Element aElement) throws TSLException {
		super(aElement);
		mTSLLocation = XmlUtil.getText(aElement);
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSLLOCATION;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public String gettslLocation() {
		return mTSLLocation;
	}
}
