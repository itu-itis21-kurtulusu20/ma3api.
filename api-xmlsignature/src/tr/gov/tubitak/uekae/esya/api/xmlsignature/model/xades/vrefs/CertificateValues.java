package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECertificate;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;


/**
 * @author ahmety
 * date: Dec 17, 2009
 *
 * todo
 */
public class CertificateValues extends CertificateValuesType implements UnsignedSignaturePropertyElement
{

    private static Logger logger = LoggerFactory.getLogger(CertificateValues.class);

    public CertificateValues(Context aContext)
    {
        super(aContext);
    }

    public CertificateValues(Context aContext, ECertificate[] aCertificates)
            throws XMLSignatureException
    {
        super(aContext, aCertificates);
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public CertificateValues(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        if (logger.isDebugEnabled()){
            logger.debug("CERTIFICATE VALUES");
            for (int i=0; i<getCertificateCount(); i++){
                logger.debug(getCertificate(i).toString());
            }
        }

    }

    @Override
    public String getLocalName()
    {
        return Constants.TAGX_CERTIFICATEVALUES;  
    }
}
