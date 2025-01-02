package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

/**
 * <P>The MgmtData element within KeyInfo  is a string value used to convey
 * in-band key distribution or agreement data. For example, DH key exchange, RSA
 * key encryption, etc. Use of this element is <b>NOT RECOMMENDED.</b> It
 * provides a  syntactic hook where in-band key distribution or agreement
 * data can be placed. However, superior interoperable child elements of KeyInfo
 * for the  transmission of encrypted keys and for key agreement are being
 * specified by the W3C XML Encryption Working Group and they should be used
 * instead of MgmtData.
 * 
 * <p><b>This method is not supported by this API yet!</b>
 *
 * @author ahmety
 * date: Jun 16, 2009
 *
 * todo decide to implement this class
 */
public class MgmtData extends BaseElement implements KeyInfoElement
{

    public MgmtData(Element aElement, Context aBaglam)
            throws XMLSignatureException
    {
        super(aElement, aBaglam);
    }

    public String getLocalName()
    {
        return Constants.TAG_MGMTDATA;
    }
}
