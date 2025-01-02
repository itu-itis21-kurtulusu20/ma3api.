package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

/**
 * The <code>EncapsulatedOCSPValue</code> element contains the base-64 encoding
 * of a DER-encoded <code>OCSPResponse</code> defined in RFC 2560.
 *
 * @author ahmety
 * date: Jan 8, 2010
 */
public class EncapsulatedOCSPValue extends EncapsulatedPKIData
{

    public EncapsulatedOCSPValue(Context aContext, EOCSPResponse aOCSPResponse)
    {
        super(aContext);
        setEncoding(PKIEncodingType.DER);
        setValue(aOCSPResponse.getEncoded());
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public EncapsulatedOCSPValue(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    /**
     * Useless method, dont need to set PKIEncodingType on EncapsulatedOCSPValue
     * becuase its has always "DER" encoding type.
     *
     * @param aEncoding
     */
    @Override
    public void setEncoding(PKIEncodingType aEncoding)
    {
        if (aEncoding!=PKIEncodingType.DER)
            throw new ESYARuntimeException(I18n.translate("core.model.invalidEncapsulatedEncoding", I18n.translate("OCSP")));
        super.setEncoding(PKIEncodingType.DER);    
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_ENCAPSULATEDOCSPVALUE;
    }
}
