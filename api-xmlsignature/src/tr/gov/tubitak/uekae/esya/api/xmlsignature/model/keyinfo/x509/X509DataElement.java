package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.x509;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;

/**
 * <p>Base Element for X509Data children elements.</p>
 *
 * <p>An <code>X509Data</code> element within <code>KeyInfo</code> contains one
 * or more identifiers of keys or X509 certificates (or certificates'
 * identifiers or a revocation list). The content of <code>X509Data</code> is:
 * </p>
 *
 * <ol><li>At least one element, from the following set of element
 * types; any of these may appear together or more than once iff
 * (if and only if) each instance describes or is related to the
 * same certificate:</li>
 *
 * <li >
 *   <ul>
 *      <li>The <code>X509IssuerSerial</code> element, which
 *      contains an X.509 issuer distinguished name/serial number
 *      pair. The distinguished name SHOULD be represented as a
 *      string that complies with section 3 of RFC4514
 *      , to be generated
 *      according to the "Distinguished Name Encoding Rules" section
 *      below,</li>
 *
 *      <li>The <code>X509SubjectName</code> element, which
 *      contains an X.509 subject distinguished name that SHOULD be
 *      represented as a string that complies with section 3 of
 *      RFC4514 [LDAP-DN],
 *      to be generated according to the
 *      "Distinguished Name Encoding Rules" section,</li>
 *
 *      <li>The <code>X509SKI</code> element, which contains the
 *      base64 encoded plain (i.e. non-DER-encoded) value of a X509
 *      V.3 SubjectKeyIdentifier extension.</li>
 *
 *      <li>The <code>X509Certificate</code> element, which
 *      contains a base64-encoded [X509v3] certificate, and</li>
 *
 *      <li>Elements from an external namespace which
 *      accompanies/complements any of the elements above.</li>
 *
 *      <li>The <code>X509CRL</code> element, which contains a
 *      base64-encoded certificate revocation list (CRL) [X509v3].</li>
 *    </ul>
 *  </li>
 * </ol>
 *
 * @see X509Certificate
 * @see X509CRL
 * @see X509IssuerSerial
 * @see X509SKI
 * @see X509SubjectName
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo.X509Data
 * @see tr.gov.tubitak.uekae.esya.api.xmlsignature.model.KeyInfo
 *
 * @author ahmety
 * date: Jun 16, 2009
 */
public abstract class X509DataElement extends BaseElement
{

    protected X509DataElement(Context aContext)
    {
        super(aContext);
    }

    /**
     *  Construct X509DataElement from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    protected X509DataElement(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
    }

    // base element
    public String getNamespace()
    {
        return Constants.NS_XMLDSIG;
    }


}
