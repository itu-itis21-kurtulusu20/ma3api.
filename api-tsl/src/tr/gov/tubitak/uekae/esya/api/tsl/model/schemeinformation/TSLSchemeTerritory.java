package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLSchemeTerritory extends BaseElement {
	private String language;

	public TSLSchemeTerritory(Document document, String iLanguage) {
		super(document);
		language = iLanguage;
		mElement.setTextContent(language);
	}

	public TSLSchemeTerritory(Document document) {
		super(document);
	}

	public TSLSchemeTerritory(Element aElement) throws TSLException {
		super(aElement);
		language = XmlUtil.getText(aElement);
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_SCHEMETERRITORY;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public String getTerritoryLanguage() {
		return language;
	}

	public void setTerritoryLanguage(String value) {
		language = value;
		mElement.setTextContent(language);
	}

}
