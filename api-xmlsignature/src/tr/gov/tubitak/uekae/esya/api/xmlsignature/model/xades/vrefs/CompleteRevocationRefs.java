package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.UnsignedSignaturePropertyElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

/**
 * Full set of references to the revocation data that have been used in the
 * validation of the signer and CAs certificates, provide means to retrieve the
 * actual revocation data archived elsewhere in case of dispute and, in this
 * way, to illustrate that the verifier has taken due diligence of the
 * available revocation information.
 *
 * <p>Currently two major types of revocation data are managed in most of the
 * systems, namely CRLs and responses of on-line certificate status servers,
 * obtained through protocols designed for these purposes, like OCSP protocol.
 *
 * <p>This clause defines the <code>CompleteRevocationRefs</code> element that
 * will carry the full set of revocation information used for the validation
 * of the electronic signature.
 *
 * <p>This is an optional unsigned property that qualifies the signature.
 *
 * <p>There SHALL be at most one occurence of this property in the signature.
 * This occurrence SHALL NOT be empty.
 *
 * <p>Below follows the Schema definition for this element.
 * <pre>
 * &lt;xsd:element name="CompleteRevocationRefs" type="CompleteRevocationRefsType"/&gt;
 *
 * &lt;xsd:complexType name="CompleteRevocationRefsType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="CRLRefs" type="CRLRefsType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="OCSPRefs" type="OCSPRefsType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="OtherRefs" type="OtherCertStatusRefsType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:complexType name="CRLRefsType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="CRLRef" type="CRLRefType" maxOccurs="unbounded"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:complexType name="OCSPRefsType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="OCSPRef" type="OCSPRefType" maxOccurs="unbounded"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:complexType name="OtherCertStatusRefsType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="OtherRef" type="AnyType" maxOccurs="unbounded"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * <p>The CompleteRevocationRefs element can contain:
 * <ul>
 * <li>sequences of references to CRLs (CRLRefs element);
 * <li>sequences of references to OCSPResponse data as defined in RFC 2560 [8] (OCSPRefs element);
 * <li>other references to alternative forms of revocation data (OtherRefs element).
 * </ul>
 *
 * @author ahmety
 * date: Nov 9, 2009
 */
public class CompleteRevocationRefs extends XAdESBaseElement implements UnsignedSignaturePropertyElement
{
    private List<CRLReference> mCRLReferences = new ArrayList<CRLReference>(0);
    private List<OCSPReference> mOCSPReferences = new ArrayList<OCSPReference>(0);
    private List<OtherCertStatusReference> mOtherReferences = new ArrayList<OtherCertStatusReference>(0);


    public CompleteRevocationRefs(Context aContext)
    {
        super(aContext);
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
    public CompleteRevocationRefs(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element crlElements = selectChildElement(NS_XADES_1_3_2, TAGX_CRLREFS);
        if (crlElements!=null){
            List<Element> crls = XmlUtil.selectChildElements(crlElements);
            for (Element crlElement : crls) {
                CRLReference crlRef = new CRLReference(crlElement,  mContext);
                mCRLReferences.add(crlRef);
            }
        }
        Element ocspElements = selectChildElement(NS_XADES_1_3_2, TAGX_OCSPREFS);
        if (ocspElements!=null){
            List<Element> ocsps = XmlUtil.selectChildElements(ocspElements);
            for (Element ocspElement : ocsps) {
                OCSPReference ocspRef = new OCSPReference(ocspElement,  mContext);
                mOCSPReferences.add(ocspRef);
            }
        }
        Element otherRefElements = selectChildElement(NS_XADES_1_3_2, TAGX_OTHERREFS);
        if (otherRefElements!=null){
            List<Element> otherRefs = XmlUtil.selectChildElements(otherRefElements);
            for (Element otherRefElement : otherRefs) {
                OtherCertStatusReference otherRef = new OtherCertStatusReference(otherRefElement,  mContext);
                mOtherReferences.add(otherRef);
            }
        }

    }

    private void setupChildren(){
        XmlUtil.removeChildren(mElement);
        addLineBreak();

        if (mCRLReferences.size()>0){
            Element crls = insertElement(NS_XADES_1_3_2, TAGX_CRLREFS);
            addLineBreak(crls);
            for (CRLReference crlRef : mCRLReferences){
                crls.appendChild(crlRef.getElement());
                addLineBreak(crls);
            }
        }

        if (mOCSPReferences.size()>0){
            Element ocsps = insertElement(NS_XADES_1_3_2, TAGX_OCSPREFS);
            addLineBreak(ocsps);
            for (OCSPReference ocspRef : mOCSPReferences){
                ocsps.appendChild(ocspRef.getElement());
                addLineBreak(ocsps);
            }
        }

        if (mOtherReferences.size()>0){
            Element others = insertElement(NS_XADES_1_3_2, TAGX_OTHERREFS);
            addLineBreak(others);
            for (OtherCertStatusReference otherRef : mOtherReferences){
                others.appendChild(otherRef.getElement());
                addLineBreak(others);
            }
        }
        if (mId!=null){
            mElement.setAttributeNS(null, ATTR_ID, mId);
        }
        
    }

    public int getCRLReferenceCount(){
        return mCRLReferences.size();
    }

    public int getOCSPReferenceCount(){
        return mOCSPReferences.size();
    }

    public int getOtherCertStatusReferenceCount(){
        return mOtherReferences.size();
    }

    public CRLReference getCRLReference(int aIndex){
        return mCRLReferences.get(aIndex);
    }

    public OCSPReference getOCSPReference(int aIndex){
        return mOCSPReferences.get(aIndex);
    }

    public OtherCertStatusReference getOtherCertStatusReference(int aIndex){
        return mOtherReferences.get(aIndex);
    }

    public void addCRLReference(CRLReference aCRLReference){
        mCRLReferences.add(aCRLReference);
        setupChildren();
    }

    public void addOCSPReference(OCSPReference aOCSPReference){
        mOCSPReferences.add(aOCSPReference);
        setupChildren();
    }

    public void addOtherCertStatusReference(OtherCertStatusReference aOtherReference){
        mOtherReferences.add(aOtherReference);
        setupChildren();
    }


    public String getLocalName()
    {
        return TAGX_COMPLETEREVOCATIONREFS;
    }
}
