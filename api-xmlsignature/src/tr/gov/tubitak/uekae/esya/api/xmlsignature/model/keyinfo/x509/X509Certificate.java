package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.c14n.core.utils.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;


/**
 * <code>X509Data</code> child element, which contains a base64-encoded
 * [X509v3] certificate
 * 
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
 * @author ahmety
 * date: Jun 16, 2009
 */
public class X509Certificate extends X509DataElement
{
    private static final Logger logger = LoggerFactory.getLogger(X509Certificate.class);

    private byte[] mCertificateBytes;
    private ECertificate mCertificate;


    public X509Certificate(Context aBaglam, byte[] aCertificateBytes) throws XMLSignatureException
    {
        super(aBaglam);
        mCertificateBytes = aCertificateBytes;
        XmlUtil.setBase64EncodedText(mElement, aCertificateBytes);
        try {
            mCertificate = new ECertificate(mCertificateBytes);
        } catch (Exception x){
            logger.error("Error resolving x509 certificate", x);
            throw new XMLSignatureException(x, "errors.cantDecode", "byte[]", I18n.translate("certificate"));
        }
    }

    public X509Certificate(Context aBaglam, ECertificate aCertificate)
    {
        super(aBaglam);
        mCertificate = aCertificate;
        mCertificateBytes = aCertificate.getEncoded();
        XmlUtil.setBase64EncodedText(mElement, mCertificateBytes);
    }

    /**
     *  Construct X509Certificate from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public X509Certificate(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        mCertificateBytes = XmlUtil.getBase64DecodedText(mElement);
        try {
            mCertificate = new ECertificate(mCertificateBytes);
        } catch (Exception x){
            logger.error("Error resolving x509 certificate", x);
            throw new XMLSignatureException(x, "errors.cantDecode", aElement.getTagName(), I18n.translate("certificate"));
        }
    }

    public byte[] getCertificateBytes()
    {
        return mCertificateBytes;
    }

    public ECertificate getCertificate()
    {
        return mCertificate;
    }

    public void setCertificateBytes(ECertificate aCertificate)
    {
        mCertificate = aCertificate;
        byte[] certBytes = aCertificate.getEncoded();
        XmlUtil.setBase64EncodedText(mElement, certBytes);
        mCertificateBytes = certBytes;
    }

    @Override
    public String toString()
    {
        return mCertificate.toString();
    }

    // base element
    public String getLocalName()
    {
        return Constants.TAG_X509CERTIFICATE;
    }
}
