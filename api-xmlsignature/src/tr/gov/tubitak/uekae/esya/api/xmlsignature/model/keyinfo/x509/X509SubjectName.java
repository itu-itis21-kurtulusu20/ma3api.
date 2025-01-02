package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

/**
 * <code>X509Data</code> child element, which contains an X.509 subject
 * distinguished name that SHOULD be represented as a string that complies with
 * section 3 of RFC4514 [LDAP-DN], to be generated according to the
 * "Distinguished Name Encoding Rules" section<p>
 *
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
 * @author ahmety
 * date: Jun 16, 2009
 */
public class X509SubjectName extends X509DataElement
{
    private String mSubjectName;

    public X509SubjectName(Context aContext, String aSubjectName)
    {
        super(aContext);
        mSubjectName = aSubjectName;
        mElement.setTextContent(LDAPDNUtil.normalize(aSubjectName));
    }

    /**
     *  Construct KeyInfo from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public X509SubjectName(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        mSubjectName = XmlUtil.getText(aElement);
    }

    public String getSubjectName()
    {
        return mSubjectName;
    }

    // base element
    public String getLocalName()
    {
        return Constants.TAG_X509SUBJECTNAME;
    }

}
