package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import java.util.Calendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLListIssueDateTime extends BaseElement {
	private XMLGregorianCalendar issueDateTime;
	private String greagorianDateTime;

	public TSLListIssueDateTime(Document document, Calendar iIssueDateTime) {
		super(document);
		issueDateTime = XmlUtil.createDate(iIssueDateTime);
		greagorianDateTime = issueDateTime.toString();
		mElement.setTextContent(greagorianDateTime);
	}

	public TSLListIssueDateTime(Document document,
			String iGregorianIssueDateTime) throws TSLException

	{
		super(document);
		greagorianDateTime = iGregorianIssueDateTime;
		mElement.setTextContent(greagorianDateTime);
		issueDateTime = XmlUtil.getDate(mElement);
	}

	public TSLListIssueDateTime(Document document)

	{
		super(document);
	}

	public TSLListIssueDateTime(Element aElement) throws TSLException {
		super(aElement);
		greagorianDateTime = XmlUtil.getText(aElement);
		issueDateTime = XmlUtil.getDate(aElement);
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_LISTISSUEDATETIME;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public String getGregorianDateTime() {
		return greagorianDateTime;
	}

	public XMLGregorianCalendar getCalendar() {
		return issueDateTime;
	}
}
