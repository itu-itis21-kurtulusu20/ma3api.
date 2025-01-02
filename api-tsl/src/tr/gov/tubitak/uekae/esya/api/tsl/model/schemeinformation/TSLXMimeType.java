package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLXMimeType extends BaseElement {
	private String mimeType;

	public TSLXMimeType(Document document, String iMimeType) {
		super(document);
		mimeType = iMimeType;
		mElement.setTextContent(mimeType);
	}

	public TSLXMimeType(Element aElement) throws TSLException {
		super(aElement);
		mimeType = XmlUtil.getText(aElement);
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_MIMETYPE;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSLX;
	}

	public String getMimeType() {
		return mimeType;
	}
}