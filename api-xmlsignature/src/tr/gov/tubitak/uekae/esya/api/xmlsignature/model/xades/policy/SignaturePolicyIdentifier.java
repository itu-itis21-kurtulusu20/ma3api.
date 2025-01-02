package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.policy;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.profile.TurkishESigProfile;
import tr.gov.tubitak.uekae.esya.api.common.util.OIDUtil;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.DigestMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.Identifier;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * The signature policy is a set of rules for the creation and validation of an
 * electronic signature, under which the signature can be determined to be
 * valid. A given legal/contractual context MAY recognize a particular
 * signature policy as meeting its requirements.
 *
 * <p>The signature policy needs to be available in human readable form so that
 * it can be assessed to meet the requirements of the legal and contractual
 * context in which it is being applied. To facilitate the automatic processing
 * of an electronic signature the parts of the signature policy which specify
 * the electronic rules for the creation and validation of the electronic
 * signature also need to be in a computer processable form.
 *
 * <p>If no signature policy is identified then the signature may be assumed to
 * have been generated/verified without any policy constraints, and hence may
 * be given no specific legal or contractual significance through the context
 * of a signature policy.
 *
 * <p>The present document specifies two unambiguous ways for identifying the
 * signature policy that a signature follows:
 * <ul>
 * <li>The electronic signature can contain an explicit and unambiguous
 * identifier of a signature policy together with a hash value of the signature
 * policy, so it can be verified that the policy selected by the signer is the
 * one being used by the verifier. An explicit signature policy has a globally
 * unique reference, which, in this way, is bound to an electronic signature by
 * the signer as part of the signature calculation. In these cases, for a given
 * explicit signature policy there shall be one definitive form that has a
 * unique binary encoded value. Finally, a signature policy identified in this
 * way MAY be qualified by additional information.
 *
 * <li>Alternatively, the electronic signature can avoid the inclusion of the
 * aforementioned identifier and hash value. This will be possible when the
 * signature policy can be unambiguously derived from the semantics of the type
 * of data object(s) being signed, and some other information, e.g. national
 * laws or private contractual agreements, that mention that a given signature
 * policy MUST be used for this type of data content. In such cases, the
 * signature will contain a specific empty element indicating that this implied
 * way to identify the signature policy is used instead the identifier and hash
 * value.
 * </ul>
 *
 * <p> The signature policy identifier is a signed property qualifying the
 * signature.
 *
 * <p> At most one <code>SignaturePolicyIdentifier</code> element MAY be
 * present in the signature.
 *
 * <p> Below follows the Schema definition for this type.
 * <pre>
 * &lt;xsd:element name="SignaturePolicyIdentifier" type="SignaturePolicyIdentifierType"/&gt;
 *
 * &lt;xsd:complexType name="SignaturePolicyIdentifierType"&gt;
 *   &lt;xsd:choice&gt;
 *     &lt;xsd:element name="SignaturePolicyId" type="SignaturePolicyIdType"/&gt;
 *     &lt;xsd:element name="SignaturePolicyImplied"/&gt;
 *   &lt;/xsd:choice&gt;
 * &lt;/xsd:complexType&gt;
 *
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
 * <p>The <code>SignaturePolicyId</code> element will appear when the signature
 * policy is identified using the first alternative. The
 * <code>SigPolicyId</code> element contains an identifier that uniquely
 * identifies a specific version of the signature policy.
 *
 * <p>The <code>SigPolicyHash</code> element contains the identifier of the
 * hash algorithm and the hash value of the signature policy.
 *
 * <p>The <code>SigPolicyQualifier</code> element can contain additional
 * information qualifying the signature policy identifier. The optional
 * <code>ds:Transforms</code> element can contain the transformations performed
 * on the signature policy document before computing its hash. The processing
 * model for these transformations is described in [3].
 *
 * <p>Alternatively, the <code>SignaturePolicyImplied</code> element will
 * appear when the second alternative is used. This empty element indicates
 * that the data object(s) being signed and other external data imply the
 * signature policy.
 *
 *
 * @author ahmety
 * date: Oct 15, 2009
 */
public class SignaturePolicyIdentifier extends XAdESBaseElement
{
    private SignaturePolicyId mSignaturePolicyId;
    private boolean mSignaturePolicyImplied;


    public SignaturePolicyIdentifier(Context aContext, int[] aOID, String aDescription, String aPolicyURI)
            throws XMLSignatureException
    {
        this(aContext,aOID,aDescription,aPolicyURI,null);
    }

    public SignaturePolicyIdentifier(Context aContext, int[] aOID, String aDescription, String aPolicyURI, String userNotice)
            throws XMLSignatureException
    {
        super(aContext);
        PolicyId policyId = new PolicyId(aContext, new Identifier(aContext, aOID), aDescription, null);
        List<SignaturePolicyQualifier> qualifiers = null;
        if (aPolicyURI!=null){
            SPUserNotice spUserNotice = new SPUserNotice(aContext, null, Arrays.asList(userNotice));
            qualifiers = Arrays.asList(new SignaturePolicyQualifier(mContext, aPolicyURI, spUserNotice));
        }

        mSignaturePolicyId = new SignaturePolicyId(aContext, policyId, null, null, qualifiers);
        mElement.appendChild(mSignaturePolicyId.getElement());
        addLineBreak();
    }


    public SignaturePolicyIdentifier(Context context, tr.gov.tubitak.uekae.esya.api.signature.attribute.SignaturePolicyIdentifier spi){
        super(context);

        Identifier id = new Identifier(context, spi.getPolicyId().getValue());
        PolicyId pid = new PolicyId(context, id, null, null);

        List<SignaturePolicyQualifier> qualifiers = new ArrayList<SignaturePolicyQualifier>();
        if (spi.getPolicyURI()!=null){
            qualifiers.add(new SignaturePolicyQualifier(mContext, spi.getPolicyURI(), null));
        }
        if (spi.getUserNotice()!=null){
            SPUserNotice notice = new SPUserNotice(context, null, Arrays.asList(spi.getUserNotice()));
            qualifiers.add(new SignaturePolicyQualifier(mContext, null, notice));
        }

        mSignaturePolicyId = new SignaturePolicyId(context, pid, null, DigestMethod.resolveFromName(spi.getDigestAlg()), spi.getDigestValue(), qualifiers);

        mElement.appendChild(mSignaturePolicyId.getElement());
        addLineBreak();
    }

    /**
     * Policy identifier constructor for both implicit or explicit signature
     * policies.
     * @param aContext where this signature belongs
     * @param aPolicyId Either identifier o null for implied signature policy!
     */
    public SignaturePolicyIdentifier(Context aContext, SignaturePolicyId aPolicyId)
    {
        super(aContext);
        addLineBreak();

        mSignaturePolicyId = aPolicyId;
        mSignaturePolicyImplied = (aPolicyId==null);

        // add signature policy id
        if (mSignaturePolicyId!=null){
            mElement.appendChild(mSignaturePolicyId.getElement());
        }

        // if implicit
        if (mSignaturePolicyImplied){
            insertElement(NS_XADES_1_3_2, TAGX_SIGNATUREPOLICYIMPLIED);
        }

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
    public SignaturePolicyIdentifier(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element spiElm = selectChildElement(NS_XADES_1_3_2, TAGX_SIGNATUREPOLICYID);
        if (spiElm!=null){
            mSignaturePolicyId = new SignaturePolicyId(spiElm, mContext);
        }

        mSignaturePolicyImplied = (selectChildElement(NS_XADES_1_3_2, TAGX_SIGNATUREPOLICYIMPLIED) != null);
    }

    public SignaturePolicyId getSignaturePolicyId()
    {
        return mSignaturePolicyId;
    }

    public boolean isSignaturePolicyImplied()
    {
        return mSignaturePolicyImplied;
    }

    public String getLocalName()
    {
        return TAGX_SIGNATUREPOLICYIDENTIFIER;
    }

    public TurkishESigProfile getTurkishESigProfile(){
        String oidString = getSignaturePolicyId().getPolicyId().getIdentifier().getValue();
        int[] OID = OIDUtil.fromURN(oidString);
        return TurkishESigProfile.getSignatureProfileFromOid(OID);
    }
}
