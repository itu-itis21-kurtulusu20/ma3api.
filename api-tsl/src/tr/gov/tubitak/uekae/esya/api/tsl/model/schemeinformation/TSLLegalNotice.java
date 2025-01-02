package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLLegalNotice extends BaseElement {
	private String legalNotice;
	private String language;

	public TSLLegalNotice(Document document, String iLanguage,
			String iLegalNotice) throws TSLException {
		super(document);
		language = iLanguage;
		legalNotice = iLegalNotice;

		if (mElement.getAttributes().getLength() > 0) {
			throw new TSLException("should not have a default attribute");
		}
		mElement.setAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR,
				language);
		mElement.setTextContent(legalNotice);

	}

	public TSLLegalNotice(Element aElement) throws TSLException {
		super(aElement);
		legalNotice = XmlUtil.getText(aElement);
		if (aElement.hasAttribute(Constants.XML_PREFIX
				+ Constants.TSL_LANG_ATTR)) {
			language = aElement.getAttribute(Constants.XML_PREFIX
					+ Constants.TSL_LANG_ATTR);
		} else {
			throw new TSLException("Language Attribute could not be found!");
		}
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_TSLLEGALNOTICE;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public String getLegalNotice() {
		return legalNotice;
	}

	public String getLanguage() {
		return language;
	}
}
