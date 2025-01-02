package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;


/**
 * X509Data child element, which contains the base64 encoded plain
 * (i.e. non-DER-encoded) value of a X509 V.3 SubjectKeyIdentifier extension
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
 * @author ahmety
 * date: Jun 16, 2009
 */
public class X509SKI extends X509DataElement
{

    private byte[] mSKIBytes;

    public X509SKI(Context aContext, ECertificate aCertificate)
    {
        super(aContext);
        mSKIBytes = getSKI(aCertificate);
        XmlUtil.setBase64EncodedText(mElement, mSKIBytes);
    }

    /**
     *  Construct X509SKI from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public X509SKI(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        mSKIBytes = XmlUtil.getBase64DecodedText(aElement);
    }

    public byte[] getSKIBytes()
    {
        return mSKIBytes;
    }

    // base element
    public String getLocalName()
    {
        return Constants.TAG_X509SKI;
    }

    public byte[] getSKI(ECertificate aCertificate){
        return aCertificate.getSubjectPublicKeyInfo().getEncoded();
    }
}
