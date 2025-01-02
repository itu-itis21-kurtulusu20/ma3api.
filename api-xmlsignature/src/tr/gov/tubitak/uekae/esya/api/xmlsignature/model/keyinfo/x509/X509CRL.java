package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

/**
 * <code>X509Data</code> child element, which contains a base64-encoded
 * certificate revocation list (CRL)
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
 * @author ahmety
 * date: Jun 16, 2009
 */
public class X509CRL extends X509DataElement
{
    private byte[] mCRLBytes;

    public X509CRL(Context aContext, byte[] aCRLBytes)
    {
        super(aContext);
        mCRLBytes = aCRLBytes;
        XmlUtil.setBase64EncodedText(mElement, aCRLBytes);
    }

    /**
     *  Construct X509CRL from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public X509CRL(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        mCRLBytes = XmlUtil.getBase64DecodedText(mElement);
    }

    public byte[] getCRLBytes()
    {
        return mCRLBytes;
    }

    // base element
    public String getLocalName()
    {
        return Constants.TAG_X509CRL;
    }


}
