package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>User notice that should be displayed when the signature is verified.
 *
 * <p>The <code>SPUserNotice</code> element is intended for being displayed
 * whenever the signature is validated. The <code>ExplicitText</code> element
 * contains the text of the notice to be displayed. Other notices could come
 * from the organization issuing the signature policy. The
 * <code>NoticeRef</code> element names an organization and identifies by
 * numbers (<code>NoticeNumbers</code> element) a group of textual statements
 * prepared by that organization, so that the application could get the
 * explicit notices from a notices file.
 *
 * <p>Below follows the schema definition:
 * <pre>
 * &lt;xsd:element name="SPUserNotice" type="SPUserNoticeType"/&gt;
 *
 * &lt;xsd:complexType name="SPUserNoticeType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="NoticeRef" type="NoticeReferenceType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="ExplicitText" type="xsd:string" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Oct 15, 2009
 */
public class SPUserNotice extends XAdESBaseElement
{
    private List<NoticeReference> mNoticeReferences = new ArrayList<NoticeReference>(0);
    private List<String> mExplicitTexts = new ArrayList<String>(0);

    public SPUserNotice(Context aContext,
                        List<NoticeReference> aNoticeReferences,
                        List<String> aExplicitTexts)
    {
        super(aContext);
        addLineBreak();

        if (aNoticeReferences!=null){
            mNoticeReferences.addAll(aNoticeReferences);
            for (NoticeReference noticeReference : aNoticeReferences){
                mElement.appendChild(noticeReference.getElement());
                addLineBreak();
            }
        }

        if (aExplicitTexts!=null){
            mExplicitTexts.addAll(aExplicitTexts);
            for (String explicitText : aExplicitTexts){
                insertTextElement(NS_XADES_1_3_2, TAGX_EXPLICITTEXT, explicitText);
            }
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
    public SPUserNotice(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element[] noticeRefElmArr = selectChildren(NS_XADES_1_3_2, TAGX_NOTICEREF);
        for (Element noticeRefElm : noticeRefElmArr) {
            mNoticeReferences.add(new NoticeReference(noticeRefElm, mContext));
        }

        Element[] explicitTextElmArr = selectChildren(NS_XADES_1_3_2, TAGX_EXPLICITTEXT);
        for (Element explicitTextElm : explicitTextElmArr) {
            mExplicitTexts.add(XmlUtil.getText(explicitTextElm));
        }
    }

    public List<NoticeReference> getNoticeReferences()
    {
        return mNoticeReferences;
    }

    public List<String> getExplicitTexts()
    {
        return mExplicitTexts;
    }

    public String getLocalName()
    {
        return TAGX_SPUSERNOTICE;
    }
}
