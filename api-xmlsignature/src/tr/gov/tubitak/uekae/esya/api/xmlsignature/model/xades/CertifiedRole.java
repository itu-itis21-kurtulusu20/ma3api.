package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.AsnUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;

/**
 * The <code>CertifiedRole</code> element contains the base-64 encoding of
 * DER-encoded attribute certificates for the signer.
 *
 * @author ahmety
 * date: Sep 14, 2009
 */
public class CertifiedRole extends EncapsulatedPKIData
{

    private AttributeCertificate mCertificate;


    public CertifiedRole(Context aBaglam, AttributeCertificate aCertificate)
            throws XMLSignatureException
    {
        this(aBaglam, aCertificate, null);
    }

    public CertifiedRole(Context aBaglam,
                         AttributeCertificate aCertificate,
                         PKIEncodingType aEncoding)
            throws XMLSignatureException
    {
        super(aBaglam);

        mEncoding = (aEncoding==null) ? DEFAULT_PKI_ENCODING : aEncoding;

        mCertificate = aCertificate;

        setValue(AsnUtil.encode(mCertificate, mEncoding));

        mElement.setAttributeNS(null, ATTR_ENCODING, mEncoding.getURI());

    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public CertifiedRole(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        byte[] roleBytes = XmlUtil.getBase64DecodedText(mElement);
        String encodingURI = mElement.getAttributeNS(null, ATTR_ENCODING);

        PKIEncodingType encoding = PKIEncodingType.resolve(encodingURI);

        mCertificate = new AttributeCertificate();

        AsnUtil.decode(mCertificate, roleBytes, encoding);
    }

    public PKIEncodingType getEncoding()
    {
        return mEncoding;
    }

    public AttributeCertificate getCertificate()
    {
        return mCertificate;
    }

    public String getLocalName()
    {
        return TAGX_CERTIFIEDROLE;
    }
}
