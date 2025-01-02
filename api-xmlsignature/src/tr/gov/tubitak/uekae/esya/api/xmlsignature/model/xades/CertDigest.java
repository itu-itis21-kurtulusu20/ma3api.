package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import org.w3c.dom.Element;

/**
 * @author ahmety
 * date: Nov 11, 2009
 */
public class CertDigest extends DigestAlgAndValue
{

    public CertDigest(Context aContext)
    {
        super(aContext);
    }

    public CertDigest(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    public String getLocalName()
    {
        return Constants.TAGX_CERTDIGEST;
    }
}
