package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.DigestAlgAndValue;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import org.w3c.dom.Element;

/**
 * @author ahmety
 * date: Nov 11, 2009
 */
public class SignaturePolicyHash extends DigestAlgAndValue
{

    public SignaturePolicyHash(Context aContext)
    {
        super(aContext);
    }

    public SignaturePolicyHash(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    public String getLocalName()
    {
        return Constants.TAGX_SIGPOLICYHASH;
    }
}
