package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.w3c.dom.Element;

/**
 * The <code>NoticeRef</code> element names an organization and identifies by
 * numbers (<code>NoticeNumbers</code> element) a group of textual statements
 * prepared by that organization, so that the application could get the
 * explicit notices from a notices file.
 *
 * <p>Below follows the schema definition:
 * <pre>
 * &lt;xsd:complexType name="NoticeReferenceType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="Organization" type="xsd:string"/&gt;
 *     &lt;xsd:element name="NoticeNumbers" type="IntegerListType"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Oct 15, 2009
 */
public class NoticeReference extends XAdESBaseElement
{
    private String mOrganization;
    private List<Integer> mNoticeNumbers = new ArrayList<Integer>(0);

    public NoticeReference(Context aContext, String aOrganization, Integer[] aNoticeNumbers)
    {
        super(aContext);

        if (aOrganization==null)
            throw new XMLSignatureRuntimeException("errors.nullElement", TAGX_ORGANIZATION);

        if (aNoticeNumbers==null || aNoticeNumbers.length<1)
            throw new XMLSignatureRuntimeException("errors.nullElement", TAGX_NOTICENUMBERS);

        mOrganization = aOrganization;
        mNoticeNumbers.addAll(Arrays.asList(aNoticeNumbers));

        addLineBreak();
        insertTextElement(NS_XADES_1_3_2, TAGX_ORGANIZATION, mOrganization);

        Element numbersElm = insertElement(NS_XADES_1_3_2, TAGX_NOTICENUMBERS);
        addLineBreak(numbersElm);

        for (Integer noticeNumber : mNoticeNumbers){
            Element numberElm =  createElement(NS_XADES_1_3_2, TAGX_INT);
            numberElm.setTextContent(noticeNumber+"");
            numbersElm.appendChild(numberElm);
            addLineBreak(numbersElm);
        }
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public NoticeReference(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mOrganization = getChildText(NS_XADES_1_3_2, TAGX_ORGANIZATION);

        Element numbersElm = selectChildElement(NS_XADES_1_3_2, TAGX_NOTICENUMBERS);
        Element[] numberElmArr = XmlUtil.selectNodes(numbersElm.getFirstChild(), NS_XADES_1_3_2, TAGX_INT);
        for (Element numberElm : numberElmArr){
            String noticeNumberStr = XmlUtil.getText(numberElm);
            Integer noticeNumber = Integer.valueOf(noticeNumberStr);
            mNoticeNumbers.add(noticeNumber);
        }
    }


    public String getOrganization()
    {
        return mOrganization;
    }

    public List<Integer> getNoticeNumbers()
    {
        return mNoticeNumbers;
    }

    public String getLocalName()
    {
        return TAGX_NOTICEREF;
    }
}
