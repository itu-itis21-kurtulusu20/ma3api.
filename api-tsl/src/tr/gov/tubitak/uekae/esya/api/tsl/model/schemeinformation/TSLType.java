package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLType extends BaseElement {
	private String mTSLType;

	public TSLType(Document document, String iTSLType) {
		super(document);
		mTSLType = iTSLType;
		mElement.setTextContent(mTSLType);
	}

	public TSLType(Document document) {
		super(document);
	}

	public TSLType(Element aElement) throws TSLException {
		super(aElement);
		mTSLType = XmlUtil.getText(aElement);
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSLTYPE;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public String getTslType() {
		return mTSLType;
	}

	public void setTslType(String value) {
		mTSLType = value;
		mElement.setTextContent(mTSLType);
	}
}
