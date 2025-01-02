package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLURI extends BaseElement {
	private String tslURI;
	private String language;

	public TSLURI(Element aElement) throws TSLException {
		super(aElement);
		tslURI = XmlUtil.getText(aElement);
		if (aElement.hasAttribute(Constants.XML_PREFIX
				+ Constants.TSL_LANG_ATTR)) {
			language = aElement.getAttribute(Constants.XML_PREFIX
					+ Constants.TSL_LANG_ATTR);
		}
		// because tsluri tag may be without language attribute!!!
		// else
		// {
		// throw new TSLException("Language Attribute could not be found!");
		// }
	}

	public TSLURI(Document document, String iLang, String iTSLURI)
			throws TSLException {
		super(document);
		tslURI = iTSLURI;
		language = iLang;

		if (mElement.getAttributes().getLength() > 0) {
			throw new TSLException("should not have a default attribute");
		}
		mElement.setAttribute(Constants.XML_PREFIX + Constants.TSL_LANG_ATTR,
				language);
		mElement.setTextContent(tslURI);
	}

	public TSLURI(Document document, String iTSLURI) {
		super(document);
		tslURI = iTSLURI;
		language = null;

		mElement.setTextContent(tslURI);
	}

	@Override
	public String getLocalName() {
		return Constants.TSL_URI;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;

	}

	public String getTslURI() {
		return tslURI;
	}

	public String getLanguage() {
		return language;
	}
}
