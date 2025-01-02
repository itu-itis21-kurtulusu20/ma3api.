package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import java.util.Calendar;
import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLNextUpdate extends BaseElement {
	private boolean mIsClosed = true;
	private Element dateTimeElement;
	private XMLGregorianCalendar nextUpdate;
	private String gregorianNextUpdate;

	public TSLNextUpdate(Document document, Calendar iNextUpdate) {
		super(document);
		mIsClosed = false;
		nextUpdate = XmlUtil.createDate(iNextUpdate);
		gregorianNextUpdate = nextUpdate.toString();
		addLineBreak();
		dateTimeElement = insertTextElement(Constants.NS_TSL,
				Constants.TAG_DATETIME, gregorianNextUpdate);
	}

	public TSLNextUpdate(Document document, String iGregorianDateTime)
			throws TSLException

	{
		super(document);
		mIsClosed = true;
		gregorianNextUpdate = iGregorianDateTime;
		addLineBreak();
		dateTimeElement = insertTextElement(Constants.NS_TSL,
				Constants.TAG_DATETIME, gregorianNextUpdate);
		nextUpdate = XmlUtil.getDate(dateTimeElement);
	}

	public TSLNextUpdate(Document document) {
		super(document);
	}

	public TSLNextUpdate(Element aElement) throws TSLException {
		super(aElement);
		dateTimeElement = XmlUtil.getNextElement(aElement.getFirstChild());
		if (dateTimeElement != null) {
			mIsClosed = false;
			gregorianNextUpdate = XmlUtil.getText(dateTimeElement);
			nextUpdate = XmlUtil.getDate(dateTimeElement);
		}
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_NEXTUPDATE;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public String getGregorianNextUpdate() {
		return gregorianNextUpdate;
	}

	public XMLGregorianCalendar getNextUpdate() {
		return nextUpdate;
	}

	public boolean isClosed() {
		return mIsClosed;
	}
}
