package tr.gov.tubitak.uekae.esya.api.tsl.model.schemeinformation;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import tr.gov.tubitak.uekae.esya.api.tsl.TSLException;
import tr.gov.tubitak.uekae.esya.api.tsl.model.core.BaseElement;
import tr.gov.tubitak.uekae.esya.api.tsl.util.Constants;
import tr.gov.tubitak.uekae.esya.api.tsl.util.TSL_DIL;
import tr.gov.tubitak.uekae.esya.api.tsl.util.XmlUtil;

public class TSLPolicyOrLegalNotice extends BaseElement {
	private List<TSLLegalNotice> legalNotices = new ArrayList<TSLLegalNotice>();

	public TSLPolicyOrLegalNotice(Document document,
			List<TSLLegalNotice> iLegalNotices) {
		super(document);
		legalNotices = iLegalNotices;

		addLineBreak();
		for (TSLLegalNotice tslLegalNotice : legalNotices) {
			mElement.appendChild(tslLegalNotice.getElement());
			addLineBreak();
		}
	}

	public TSLPolicyOrLegalNotice(Document document) {
		super(document);
		addLineBreak();
	}

	public TSLPolicyOrLegalNotice(Element aElement) throws TSLException {
		super(aElement);
		if (mElement.hasChildNodes()) {
			List<Element> childNodes = XmlUtil.selectChildElements(aElement);

			for (Element element : childNodes) {
				legalNotices.add(new TSLLegalNotice(element));
			}
		} else {
			throw new TSLException(Constants.TAG_TSLLEGALNOTICE
					+ TSL_DIL.NODE_CANNOT_BE_FOUND);
		}
	}

	public void RemoveAllNotices() {
		legalNotices.clear();
		RemoveAll();// mElement.RemoveAll();
		addLineBreak();
	}

	public void addLegalNotice(TSLLegalNotice iLegalNotice) {
		legalNotices.add(iLegalNotice);
		mElement.appendChild(iLegalNotice.getElement());
		addLineBreak();
	}

	public void addLegalNoticeString(String iLanguage, String iLegalNoticeStr)
			throws TSLException {
		TSLLegalNotice legalNotice = new TSLLegalNotice(
				mElement.getOwnerDocument(), iLanguage, iLegalNoticeStr);
		legalNotices.add(legalNotice);
		mElement.appendChild(legalNotice.getElement());
		addLineBreak();
	}

	@Override
	public String getLocalName() {
		return Constants.TAG_POLICYORLEGALNOTICE;
	}

	@Override
	public String getNamespace() {
		return Constants.NS_TSL;
	}

	public List<TSLLegalNotice> getLegalNoticeList() {
		return legalNotices;
	}

	public TSLLegalNotice LegalNoticeAt(int pos) {
		if (pos >= legalNotices.size()) {
			return null;
		} else {
			return legalNotices.get(pos);
		}
	}
}
