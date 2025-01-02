package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLName extends BaseElement {
	private String tslName;
	private String language;

	public TSLName(Element aElement) throws TSLException {
		super(aElement);
		tslName = XmlUtil.getText(aElement);
		if (aElement.hasAttribute(Constants.XML_PREFIX
				+ Constants.TSL_LANG_ATTR)) {
			language = aElement.getAttribute(Constants.XML_PREFIX
					+ Constants.TSL_LANG_ATTR);
		} else {
			throw new TSLException("Language Attribute could not be found!");
		}
	}

	public TSLName(Document document, String iLang, String iTSLName)
			throws TSLException {
		super(document);
		tslName = iTSLName;
		language = iLang;

		if (mElement.getAttributes().getLength() > 0) {
			throw new TSLException("should not have a default attribute");
		}
		mElement.setAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR,
				language);
		mElement.setTextContent(tslName);
	}

	@Override
	public String getLocalName() {
		return Constants.TSL_NAME;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public String getTslName() {
		return tslName;
	}

	public String getLanguage() {
		return language;
	}
}
