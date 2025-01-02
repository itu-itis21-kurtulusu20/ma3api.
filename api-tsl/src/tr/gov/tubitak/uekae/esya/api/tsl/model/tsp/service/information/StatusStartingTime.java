package tr.gov.tubitak.uekae.esya.api.tsl.model.tsp.service.information;

import java.util.Calendar;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class StatusStartingTime extends BaseElement {
	private XMLGregorianCalendar startingDateTime;
	private String greagorianDateTime;

	public StatusStartingTime(Document document, Calendar iIssueDateTime) {
		super(document);
		iIssueDateTime.add(Calendar.MILLISECOND, -iIssueDateTime.MILLISECOND); // AddMilliseconds();
		startingDateTime = XmlUtil.createDate(iIssueDateTime);
		greagorianDateTime = startingDateTime.toString();
		mElement.setTextContent(greagorianDateTime);
	}

	public StatusStartingTime(Document document, String iGregorianIssueDateTime)
			throws TSLException

	{
		super(document);
		greagorianDateTime = iGregorianIssueDateTime;
		mElement.setTextContent(greagorianDateTime);
		startingDateTime = XmlUtil.getDate(mElement);
	}

	public StatusStartingTime(Element aElement) throws TSLException

	{
		super(aElement);
		greagorianDateTime = XmlUtil.getText(aElement);
		startingDateTime = XmlUtil.getDate(aElement);
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_STATUSSTARTINGTIME;
	}

	@Override
	public String getNamespace() {

		return Constants.NS_TSL;

	}

	public String getGregorianDateTime() {
		return greagorianDateTime;
	}

	public XMLGregorianCalendar getDateTime() {
		return startingDateTime;
	}
}
