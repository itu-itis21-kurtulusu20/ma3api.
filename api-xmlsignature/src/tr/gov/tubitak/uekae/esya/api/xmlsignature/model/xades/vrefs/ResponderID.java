package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EResponseData;
import tr.gov.tubitak.uekae.esya.api.common.util.LDAPDNUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import org.w3c.dom.Element;

/**
 * The OCSP responder may be identified by its name, using the
 * <code>Byname</code> element within <code>ResponderID</code>. It may also be
 * identified by the digest of the server's public key computed as mandated in
 * RFC 2560, using the <code>ByKey</code> element. In this case the content of
 * the <code>ByKey</code> element will be the DER value of the
 * <code>byKey</code> field defined in RFC 2560, base-64 encoded. The contents
 * of <code>ByName</code> element MUST follow the rules established by XMLDSIG
 * in its clause 4.4.4 for strings representing Distinguished Names.
 *
 * <p>Below follows the schema definition:
 * <pre>
 * &lt;xsd:complexType name="ResponderIDType"&gt;
 *   &lt;xsd:choice&gt;
 *     &lt;xsd:element name="ByName" type="xsd:string"/&gt;
 *     &lt;xsd:element name="ByKey" type="xsd:base-64Binary"/&gt;
 *   &lt;/xsd:choice&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Nov 11, 2009
 */
public class ResponderID extends XAdESBaseElement
{
    private String mByName;
    private byte[] mByKey;

    public ResponderID(Context aContext, String aName)
    {
        super(aContext);
        constructByName(LDAPDNUtil.normalize(aName));
    }

    public ResponderID(Context aContext, byte[] aKey)
    {
        super(aContext);
        constructByKey(aKey);
    }

    public ResponderID(Context aContext, EResponseData aResponseData)
    {
        super(aContext);
        if (aResponseData.getResponderIDType() == tr.gov.tubitak.uekae.esya.asn.ocsp.ResponderID._BYNAME){
            String name = aResponseData.getResponderIdByName().stringValue();
            constructByName(name);
        } else {
            byte[] key = aResponseData.getResponderIdByKey();
            constructByKey(key);
        }
    }

    private void constructByName(String aName){
        mByName = aName;
        addLineBreak();
        insertTextElement(NS_XADES_1_3_2, TAGX_BYNAME, mByName);
        addLineBreak();
    }

    private void constructByKey(byte[] aKey){
        mByKey = aKey;
        addLineBreak();
        insertBase64EncodedElement(NS_XADES_1_3_2, TAGX_BYKEY, mByKey);
        addLineBreak();
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public ResponderID(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element nameElement = selectChildElement(NS_XADES_1_3_2, TAGX_BYNAME);
        Element keyElement = selectChildElement(NS_XADES_1_3_2, TAGX_BYKEY);

        if (nameElement==null && keyElement==null)
            throw new XMLSignatureException("core.model.invalidResponderId");

        if (nameElement!=null){
            mByName = XmlUtil.getText(nameElement);
        }

        if (keyElement!=null){
            mByKey = XmlUtil.getBase64DecodedText(keyElement);
        }
    }

    public String getByName()
    {
        return mByName;
    }

    public byte[] getByKey()
    {
        return mByKey;
    }

    public String getLocalName()
    {
        return TAGX_RESPONDERID;
    }
}
