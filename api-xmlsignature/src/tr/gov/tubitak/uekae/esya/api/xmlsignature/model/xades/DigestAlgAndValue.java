package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.common.util.Base64;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.XMLUtils;
import org.w3c.dom.Element;


/**
 * Element for keeping digest of a structure.
 *
 * <p>Overriding classes must override <code>{@link #getLocalName} method.</code>
 *
 * @author ahmety
 * date: Nov 10, 2009
 */
public class DigestAlgAndValue extends XAdESBaseElement
{
    private DigestMethod mDigestMethod;
    private byte[] mDigestValue;


    private Element mDigestMethodElement, mDigestValueElement;



    public DigestAlgAndValue(Context aContext)
    {
        super(aContext);

        addLineBreak();

        mDigestMethodElement = insertElement(NS_XMLDSIG, TAG_DIGESTMETHOD);
        mDigestValueElement  = insertElement(NS_XMLDSIG, TAG_DIGESTVALUE);
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public DigestAlgAndValue(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mDigestMethodElement = XMLUtils.getNextElement(mElement.getFirstChild());
        mDigestValueElement = XMLUtils.getNextElement(mDigestMethodElement.getNextSibling());

        String digestAlg = mDigestMethodElement.getAttributeNS(null, ATTR_ALGORITHM);
        mDigestMethod = DigestMethod.resolve(digestAlg);
        mDigestValue = XmlUtil.getBase64DecodedText(mDigestValueElement);
    }

    public DigestMethod getDigestMethod()
    {
        return mDigestMethod;
    }

    public void setDigestMethod(DigestMethod aDigestMethod)
    {
        mDigestMethodElement.setAttributeNS(null, ATTR_ALGORITHM, aDigestMethod.getUrl());
        mDigestMethod = aDigestMethod;
    }

    public byte[] getDigestValue()
    {
        return mDigestValue;
    }

    public void setDigestValue(byte[] aDigestValue)
    {
        mDigestValueElement.setTextContent(Base64.encode(aDigestValue));
        mDigestValue = aDigestValue;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(mDigestMethod.getAlgorithm())
                .append(" : ")
                .append(Base64.encode(mDigestValue));

        return builder.toString();     
    }

    public String getLocalName()
    {
        return TAGX_DIGESTALGANDVALUE;
    }
}
