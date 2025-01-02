package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;


/**
 * @author ahmety
 * date: Oct 15, 2009
 */
public class SignaturePolicyQualifier extends Any
{
    private String mURI;
    private SPUserNotice mUserNotice;

    /**
     * Construct new BaseElement according to context
     * @param aContext where some signature spesific properties reside.
     * @param aURI where the policy stays
     * @param aUserNotice User notice that should be displayed when the
     *      signature is verified.
     */
    public SignaturePolicyQualifier(Context aContext, String aURI, SPUserNotice aUserNotice)
    {
        super(aContext);

        mURI = aURI;
        mUserNotice = aUserNotice;

        addLineBreak();
        if (mURI!=null){
            insertTextElement(NS_XADES_1_3_2, TAGX_SPURI, mURI);
        }
        if (mUserNotice!=null){
            mElement.appendChild(mUserNotice.getElement());
            addLineBreak();
        }
    }

    /**
     * Construct Any from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public SignaturePolicyQualifier(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mURI = getChildText(NS_XADES_1_3_2, TAGX_SPURI);

        Element noticeElm = selectChildElement(NS_XADES_1_3_2, TAGX_SPUSERNOTICE);
        if (noticeElm!=null)
            mUserNotice = new SPUserNotice(noticeElm, mContext);
    }

    public String getURI()
    {
        return mURI;
    }

    public SPUserNotice getUserNotice()
    {
        return mUserNotice;
    }

    public String getUserNoticeFirstExplicitText()
    {
        return mUserNotice.getExplicitTexts().get(0);
    }

    @Override
    public String getNamespace()
    {
        return NS_XADES_1_3_2;
    }

    public String getLocalName()
    {
        return TAGX_SIGPOLICYQUALIFIER;
    }
}
