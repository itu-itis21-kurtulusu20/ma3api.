package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.document.Document;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Transforms;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.resolver.Resolver;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.KriptoUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * The <code>SignaturePolicyId</code> element will appear when the signature
 * policy is identified using explicit mechanism. The <code>SigPolicyId</code>
 * element contains an identifier that uniquely identifies a specific version
 * of the signature policy.
 *
 * <p>The <code>SigPolicyHash</code> element contains the identifier of the
 * hash algorithm and the hash value of the signature policy.
 *
 * <p>The <code>SigPolicyQualifier</code> element can contain additional
 * information qualifying the signature policy identifier.
 *
 * <p>The optional <code>ds:Transforms</code> element can contain the
 * transformations performed on the signature policy document before computing
 * its hash.
 *
 * <p>Below follows the schema definition :
 * <pre>
 * &lt;xsd:complexType name="SignaturePolicyIdType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="SigPolicyId" type="ObjectIdentifierType"/&gt;
 *     &lt;xsd:element ref="ds:Transforms" minOccurs="0"/&gt;
 *     &lt;xsd:element name="SigPolicyHash" type="DigestAlgAndValueType"/&gt;
 *     &lt;xsd:element name="SigPolicyQualifiers" type="SigPolicyQualifiersListType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:complexType name="SigPolicyQualifiersListType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="SigPolicyQualifier" type="AnyType" maxOccurs="unbounded"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Oct 15, 2009
 */
public class  SignaturePolicyId extends XAdESBaseElement
{
    private PolicyId mPolicyId;
    private Transforms mTransforms;

    private SignaturePolicyHash mSignaturePolicyHash;
    private List<SignaturePolicyQualifier> mPolicyQualifiers = new ArrayList<SignaturePolicyQualifier>(0);



    public SignaturePolicyId(Context aContext,
                             PolicyId aPolicyId,
                             Transforms aTransforms,
                             DigestMethod aDigestMethod,
                             List<SignaturePolicyQualifier> aQualifiers)
            throws XMLSignatureException
    {
        super(aContext);
        // Retrieve the electronic document containing the details of the policy
        String policyUrl = aPolicyId.getIdentifier().getValue();
        Document doc = Resolver.resolve(policyUrl, aContext);
        if (doc==null){
            throw new XMLSignatureException("validation.policy.cantFind");
        }

        // apply transforms, check hash
        if (aTransforms!=null){
            doc = aTransforms.apply(doc);
        }

        DigestMethod digestMethod = aDigestMethod!=null ? aDigestMethod : aContext.getConfig().getAlgorithmsConfig().getDigestMethod();
        byte[] digestValue = KriptoUtil.digest(doc.getBytes(), digestMethod);


        constructElement(aPolicyId, aTransforms, digestMethod, digestValue, aQualifiers);
    }


    public SignaturePolicyId(Context aContext,
                             PolicyId aPolicyId,
                             Transforms aTransforms,
                             DigestMethod aDigestMethod,
                             byte[] digestValue,
                             List<SignaturePolicyQualifier> aQualifiers)
    {
        super(aContext);

        constructElement(aPolicyId, aTransforms, aDigestMethod, digestValue, aQualifiers);
    }

    private void constructElement(PolicyId aPolicyId, Transforms aTransforms,
                                  DigestMethod aDigestMethod, byte[] aDigestValue,
                                  List<SignaturePolicyQualifier> aQualifiers)
    {
        addLineBreak();

        mPolicyId = aPolicyId;
        mTransforms = aTransforms;
        //mDigestMethod = aDigestMethod;
        if (aQualifiers!=null)
            mPolicyQualifiers.addAll(aQualifiers);

        if (mPolicyId==null)
            throw new XMLSignatureRuntimeException("errors.nullElement", TAGX_SIGPOLICYID);

        mElement.appendChild(mPolicyId.getElement());
        addLineBreak();

        if (mTransforms!=null){
            mElement.appendChild(mTransforms.getElement());
            addLineBreak();
        }

        // policy hash
        mSignaturePolicyHash = new SignaturePolicyHash(mContext);

        mSignaturePolicyHash.setDigestMethod(aDigestMethod);
        mSignaturePolicyHash.setDigestValue(aDigestValue);

        mElement.appendChild(mSignaturePolicyHash.getElement());
        addLineBreak();

        // policy qualifiers
        if (aQualifiers!=null && aQualifiers.size()>0){
            Element qualifiersElm = insertElement(NS_XADES_1_3_2, TAGX_SIGPOLICYQUALIFIERS);
            addLineBreak(qualifiersElm);
            for (SignaturePolicyQualifier qualifier : aQualifiers){
                qualifiersElm.appendChild(qualifier.getElement());
                addLineBreak(qualifiersElm);
            }
        }
    }


    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public SignaturePolicyId(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        // policy id
        Element policyIdElm = selectChildElement(NS_XADES_1_3_2, TAGX_SIGPOLICYID);
        if (policyIdElm==null)
            throw new XMLSignatureException("xml.WrongContent", TAGX_SIGPOLICYID, getLocalName());
        mPolicyId = new PolicyId(policyIdElm, mContext);

        // transforms
        Element transformsElm = selectChildElement(NS_XMLDSIG, TAG_TRANSFORMS);
        if (transformsElm!=null)
            mTransforms = new Transforms(transformsElm, mContext);

        // policy hash : digest method, digest value
        Element sigPolichHashElm = selectChildElement(NS_XADES_1_3_2, TAGX_SIGPOLICYHASH);
        if (sigPolichHashElm==null)
            throw new XMLSignatureException("xml.WrongContent", TAGX_SIGPOLICYHASH, getLocalName());

        mSignaturePolicyHash = new SignaturePolicyHash(sigPolichHashElm, mContext);

        // qualifiers
        Element qualifiersElm = selectChildElement(NS_XADES_1_3_2, TAGX_SIGPOLICYQUALIFIERS);
        if (qualifiersElm!=null){
            Element[] qualifierElmArr = XmlUtil.selectNodes(qualifiersElm.getFirstChild(), NS_XADES_1_3_2, TAGX_SIGPOLICYQUALIFIER);
            for (Element qualifierElm : qualifierElmArr){
                mPolicyQualifiers.add(new SignaturePolicyQualifier(qualifierElm, mContext));
            }
        }
    }


    public PolicyId getPolicyId()
    {
        return mPolicyId;
    }

    public Transforms getTransforms()
    {
        return mTransforms;
    }

    public byte[] getDigestValue()
    {
        return mSignaturePolicyHash.getDigestValue();
    }

    public DigestMethod getDigestMethod()
    {
        return mSignaturePolicyHash.getDigestMethod();
    }

    public List<SignaturePolicyQualifier> getPolicyQualifiers()
    {
        return mPolicyQualifiers;
    }

    public String getFirstUserNoticeExplicitText()
    {
        List<SignaturePolicyQualifier> policyQualifiers = getPolicyQualifiers();
        if(policyQualifiers == null || policyQualifiers.size() == 0)
            return null;

       for(int i=0; i< policyQualifiers.size(); i++){
           if(policyQualifiers.get(i).getUserNotice() != null)
               return policyQualifiers.get(i).getUserNotice().getExplicitTexts().get(0);
       }

       return null;
    }

    public String getLocalName()
    {
        return TAGX_SIGNATUREPOLICYID;
    }
}
