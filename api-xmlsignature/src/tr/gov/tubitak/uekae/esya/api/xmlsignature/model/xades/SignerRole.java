package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>While the name of the signer is important, the position of the signer
 * within a company or an organization can be even more important. Some
 * contracts may only be valid if signed by a user in a particular role, e.g. a
 * Sales Director. In many cases who the sales Director really is, is not that
 * important but being sure that the signer is empowered by his company to be
 * the Sales Director is fundamental.
 *
 * <p>The present document defines two different ways for providing this
 * feature:
 * <ul><li> using a claimed role name;
 *     <li> using an attribute certificate containing a certified role.
 * </ul>
 * <p>The signer MAY state his own role without any certificate to corroborate
 * this claim, in which case the claimed role can be added to the signature
 * as a signed qualifying property.
 *
 * <p>Unlike public key certificates that bind an identifier to a public key,
 * Attribute Certificates bind the identifier of a certificate to some
 * attributes of its owner, like a role. The Attribute Authority will be most
 * of the time under the control of an organization or a company that is best
 * placed to know which attributes are relevant for which individual. The
 * Attribute Authority MAY use or point to public key certificates issued by
 * any CA, provided that the appropriate trust may be placed in that CA.
 * Attribute Certificates MAY have various periods of validity. That period may
 * be quite short, e.g. one day. While this requires that a new Attribute
 * Certificate is obtained every day, valid for that day, this can be
 * advantageous since revocation of such certificates may not be needed. When
 * signing, the signer will have to specify which Attribute Certificate it
 * selects.
 *
 * <p>This is an optional signed property that qualifies the signer. There SHALL
 * be at most one occurence of this property in the signature.
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;xsd:element name="SignerRole" type="SignerRoleType"/&gt;
 *
 * &lt;xsd:complexType name="SignerRoleType"&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element name="ClaimedRoles" type="ClaimedRolesListType" minOccurs="0"/&gt;
 *       &lt;xsd:element name="CertifiedRoles" type="CertifiedRolesListType" minOccurs="0"/&gt;
 *     &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:complexType name="ClaimedRolesListType"&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element name="ClaimedRole" type="AnyType" maxOccurs="unbounded"/&gt;
 *     &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * 
 * &lt;xsd:complexType name="CertifiedRolesListType"&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element name="CertifiedRole" type="EncapsulatedPKIDataType" maxOccurs="unbounded"/&gt;
 *     &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Sep 3, 2009
 */
public class SignerRole extends XAdESBaseElement
{
    private static final Logger logger = LoggerFactory.getLogger(SignerRole.class);

    private List<ClaimedRole> mClaimedRoles = new ArrayList<ClaimedRole>(0);
    private List<CertifiedRole> mCertifiedRoles = new ArrayList<CertifiedRole>(0);

    public SignerRole(Context aContext, ClaimedRole[] aClaimedRoles)
            throws XMLSignatureException
    {
        this(aContext, aClaimedRoles, null);
    }

    public SignerRole(Context aContext, CertifiedRole[] aCertifiedRoles)
            throws XMLSignatureException
    {
        this(aContext, null, aCertifiedRoles);
    }

    public SignerRole(Context aContext, ClaimedRole[] aClaimedRoles, CertifiedRole[] aCertifiedRoles)
            throws XMLSignatureException
    {
        super(aContext);
        addLineBreak();

        if (aClaimedRoles!=null)
        {
            Element claimedRolesElement = insertElement(NS_XADES_1_3_2, TAGX_CLAIMEDROLES);
            addLineBreak(claimedRolesElement);

            for (ClaimedRole aClaimedRole : aClaimedRoles) {
                claimedRolesElement.appendChild(aClaimedRole.getElement());
                addLineBreak(claimedRolesElement);
                mClaimedRoles.add(aClaimedRole);
            }
        }


        if (aCertifiedRoles!=null)
        {
            Element certifiedRolesElement = insertElement(NS_XADES_1_3_2, TAGX_CERTIFIEDROLES);
            addLineBreak(certifiedRolesElement);

            for (CertifiedRole aCertifiedRole : aCertifiedRoles) {
                certifiedRolesElement.appendChild(aCertifiedRole.getElement());
                addLineBreak(certifiedRolesElement);
                mCertifiedRoles.add(aCertifiedRole);
            }
        }
    }

    /**
     * Construct SignerRole from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public SignerRole(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        // claimed roles..
        Element claimedElm = selectChildElement(NS_XADES_1_3_2, TAGX_CLAIMEDROLES);
        if (claimedElm!=null){
            Element[] roleArr = XmlUtil.selectNodes(claimedElm.getFirstChild(), NS_XADES_1_3_2, TAGX_CLAIMEDROLE);
            for (Element roleElm : roleArr){
                ClaimedRole role = new ClaimedRole(roleElm, mContext);
                mClaimedRoles.add(role);
                logger.info("Found claimed role: "+ role);
            }
        }

        // certified roles with attribute certificate
        Element certifiedElm = selectChildElement(NS_XADES_1_3_2, TAGX_CERTIFIEDROLES);
        if (certifiedElm!=null){
            Element[] roleArr = XmlUtil.selectNodes(certifiedElm.getFirstChild(), NS_XADES_1_3_2, TAGX_CERTIFIEDROLE);
            for (Element roleElm : roleArr){
                CertifiedRole cr = new CertifiedRole(roleElm, mContext);
                mCertifiedRoles.add(cr);
                logger.info("Found certified role: "+ cr);
            }
        }


    }

    public List<ClaimedRole> getClaimedRoles()
    {
        return mClaimedRoles;
    }

    public List<CertifiedRole> getCertifiedRoles()
    {
        return mCertifiedRoles;
    }

    //base element
    public String getLocalName()
    {
        return TAGX_SIGNERROLE;
    }
}
