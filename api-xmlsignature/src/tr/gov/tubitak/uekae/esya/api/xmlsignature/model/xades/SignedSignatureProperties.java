package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy.SignaturePolicyIdentifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import javax.xml.datatype.XMLGregorianCalendar;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * <p>This element contains properties that qualify the XML signature that has
 * been specified with the Target attribute of the
 * <code>QualifyingProperties</code> container element.
 *
 * <p>
 * <pre>
 * &lt;xsd:complexType name="SignedSignaturePropertiesType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="SigningTime" type="xsd:dateTime" minOccurs="0"/&gt;
 *     &lt;xsd:element name="SigningCertificate" type="CertIDListType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="SignaturePolicyIdentifer" type="SignaturePolicyIdentifierType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="SignatureProductionPlace" type="SignatureProductionPlaceType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="SignerRole" type="SignerRoleType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jun 22, 2009
 */
public class SignedSignatureProperties extends XAdESBaseElement
{
    private static final Logger logger = LoggerFactory.getLogger(SignedSignatureProperties.class);

    protected XMLGregorianCalendar mSigningTime;
    protected SigningCertificate  mSigningCertificate;
    protected SignaturePolicyIdentifier mSignaturePolicyIdentifier;
    protected SignatureProductionPlace mSignatureProductionPlace;
    protected SignerRole mSignerRole;

    public SignedSignatureProperties(Context aContext)
    {
        super(aContext);
    }

    /**
     *  Construct SignedSignatureProperties from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public SignedSignatureProperties(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        Element timeElement = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNINGTIME);
        if (timeElement!=null){
            //String time = XmlUtil.getText(timeElement);
            mSigningTime = XmlUtil.getDate(timeElement);
            logger.info("Signing time is: "+mSigningTime);
        }
        Element certElement = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNINGCERTIFICATE);
        if (certElement!=null){
            mSigningCertificate = new SigningCertificate(certElement, mContext);
            logger.debug("Signed signature certificate property exists.");
        }

        Element policyElement = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNATUREPOLICYIDENTIFIER);
        if (policyElement!=null){
            mSignaturePolicyIdentifier = new SignaturePolicyIdentifier(policyElement, mContext);
            logger.debug("Signature policiy identifier property exists.");
        }

        Element placeElement = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNATUREPRODUCTIONPLACE);
        if (placeElement!=null){
            mSignatureProductionPlace = new SignatureProductionPlace(placeElement, mContext);
        }

        Element roleElement = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNERROLE);
        if (roleElement!=null){
            mSignerRole = new SignerRole(roleElement, mContext);
        }

    }


    public XMLGregorianCalendar getSigningTime()
    {
        return mSigningTime;
    }

    public void setSigningTime(XMLGregorianCalendar aSigningTime)
    {
        mSigningTime = aSigningTime;
        setupChildren();
    }

    public SigningCertificate getSigningCertificate()
    {
        return mSigningCertificate;
    }

    public void setSigningCertificate(SigningCertificate aSigningCertificate)
    {
        mSigningCertificate = aSigningCertificate;
        setupChildren();
    }

    public SignaturePolicyIdentifier getSignaturePolicyIdentifier()
    {
        return mSignaturePolicyIdentifier;
    }

    public void setSignaturePolicyIdentifier(SignaturePolicyIdentifier aSignaturePolicyIdentifier)
    {
        mSignaturePolicyIdentifier = aSignaturePolicyIdentifier;
        setupChildren();
    }

    public SignerRole getSignerRole()
    {
        return mSignerRole;
    }

    public void setSignerRole(SignerRole aSignerRole)
    {
        mSignerRole = aSignerRole;
        setupChildren();
    }

    public SignatureProductionPlace getSignatureProductionPlace()
    {
        return mSignatureProductionPlace;
    }

    public void setSignatureProductionPlace(SignatureProductionPlace aPlace)
    {
        mSignatureProductionPlace = aPlace;
        setupChildren();
    }


    private void setupChildren()
    {
        XmlUtil.removeChildren(mElement);
        addLineBreak();

        if (mSigningTime!=null){
            insertTextElement(NS_XADES_1_3_2, TAGX_SIGNINGTIME, mSigningTime.toString());
        }

        if (mSigningCertificate!=null){
            mElement.appendChild(mSigningCertificate.getElement());
            addLineBreak();
        }

        if (mSignaturePolicyIdentifier!=null){
            mElement.appendChild(mSignaturePolicyIdentifier.getElement());
            addLineBreak();
        }

        if (mSignatureProductionPlace!=null){
            mElement.appendChild(mSignatureProductionPlace.getElement());
            addLineBreak();
        }

        if (mSignerRole!=null){
            mElement.appendChild(mSignerRole.getElement());
            addLineBreak();
        }

        if (mId!=null){
            mElement.setAttributeNS(null, ATTR_ID, mId);
        }
    }

    public String getLocalName()
    {
        return TAGX_SIGNEDSIGNATUREPROPERTIES;
    }
}
