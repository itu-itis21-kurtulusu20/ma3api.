package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.AsnUtil;
import tr.gov.tubitak.uekae.esya.asn.attrcert.AttributeCertificate;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;


/**
 * @author ahmety
 * date: Jan 8, 2010
 */
public class EncapsulatedX509Certificate extends EncapsulatedPKIData
{

    public EncapsulatedX509Certificate(Context aContext, ECertificate aCertificate)
    {
        super(aContext);
        setEncoding(PKIEncodingType.DER);
        setValue(aCertificate.getEncoded());
    }

    public EncapsulatedX509Certificate(Context aContext, AttributeCertificate aCertificate)
            throws XMLSignatureException
    {
        super(aContext);
        setEncoding(PKIEncodingType.DER);
        setValue(AsnUtil.encode(aCertificate, mEncoding));
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public EncapsulatedX509Certificate(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_ENCAPSULATEDX509CERTIFICATE;  
    }
}
