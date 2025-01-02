package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.common.ESYAException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.PKIEncodingType;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.EncapsulatedPKIData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

/**
 *
 * <code>EncapsulatedCRLValue</code> contains the base-64 encoding of a
 * DER-encoded X.509 CRL.
 * 
 * @author ahmety
 * date: Jan 8, 2010
 */
public class EncapsulatedCRLValue extends EncapsulatedPKIData
{

    public EncapsulatedCRLValue(Context aContext, ECRL aCrl)
    {
        super(aContext);
        setEncoding(PKIEncodingType.DER);
        setValue(aCrl.getEncoded());
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public EncapsulatedCRLValue(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    /**
     * Useless method, dont need to set PKIEncodingType on EncapsulatedCRLValue
     * becuase its has always "DER" encoding type.
     *
     * @param aEncoding
     */
    @Override
    public void setEncoding(PKIEncodingType aEncoding)
    {
        if (aEncoding!=PKIEncodingType.DER)
            throw new XMLSignatureRuntimeException("core.model.invalidEncapsulatedEncoding", I18n.translate("CRL"));
        super.setEncoding(PKIEncodingType.DER);
    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_ENCAPSULATEDCRLVALUE;
    }

    @Override
    public String toString() {
        try {
            return new ECRL(getValue()).toString();
        } catch (ESYAException e) {
            return super.toString();
        }
    }
}
