package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;
import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import org.w3c.dom.Element;

import java.util.List;
import java.util.ArrayList;

/**
 * The commitment type can be indicated in the electronic signature either:
 * <ul>
 * <li>explicitly using a commitment type indication in the electronic signature;
 * <li>implicitly or explicitly from the semantics of the signed data object.
 * </ul>
 *
 * <p>If the indicated commitment type is explicit by means of a commitment
 * type indication in the electronic signature, acceptance of a verified
 * signature implies acceptance of the semantics of that commitment type.
 * The semantics of explicit commitment types indications shall be specified
 * either as part of the signature policy or MAY be registered for generic
 * use across multiple policies.
 *
 * <p>If a signature includes a commitment type indication other than one
 * of those recognized under the signature policy the signature shall be
 * treated as invalid.
 *
 * <p>How commitment is indicated using the semantics of the data object
 * being signed is outside the scope of the present document.
 *
 * <p>The commitment type MAY be:
 * <ul>
 * <li>defined as part of the signature policy, in which case the commitment
 * type has precise semantics that is defined as part of the signature policy;
 * <li>a registered type, in which case the commitment type has precise
 * semantics defined by registration, under the rules of the registration
 * authority. Such a registration authority may be a trading association or
 * a legislative authority.
 * </ul>
 *
 * <p>The definition of a commitment type includes:
 * <ul>
 * <li>the object identifier for the commitment;
 * <li>a sequence of qualifiers.
 * </ul>
 *
 * <p> The qualifiers can provide more information about the commitment, it
 * could provide, for example, information about the context be it
 * contractual/legal/application specific.
 *
 * <p>If an electronic signature does not contain a recognized commitment
 * type then the semantics of the electronic signature is dependent on the
 * data object being signed and the context in which it is being used.
 *
 * <p>This is a signed property that qualifies signed data object(s). In
 * consequence, an XML electronic signature aligned with the present document
 * MAY contain more than one CommitmentTypeIndication elements.
 *
 * <p>Below follows the schema definition for this element.
 * <pre>
 * &lt;xsd:element name="CommitmentTypeIndication" type="CommitmentTypeIndicationType"/&gt;
 *
 * &lt;xsd:complexType name="CommitmentTypeIndicationType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="CommitmentTypeId" type="ObjectIdentifierType"/&gt;
 *     &lt;xsd:choice&gt;
 *       &lt;xsd:element name="ObjectReference" type="xsd:anyURI" maxOccurs="unbounded"/&gt;
 *       &lt;xsd:element name="AllSignedDataObjects"/&gt;
 *     &lt;/xsd:choice&gt;
 *     &lt;xsd:element name="CommitmentTypeQualifiers" type="CommitmentTypeQualifiersListType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 *
 * &lt;xsd:complexType name="CommitmentTypeQualifiersListType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="CommitmentTypeQualifier" type="AnyType" minOccurs="0" maxOccurs="unbounded"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * <p>The CommitmentTypeId element univocally identifies the type of commitment
 * made by the signer. A number of commitments have been already identified
 * in TS 101 733 , namely:
 *
 * <ul>
 * <li>Proof of origin indicates that the signer recognizes to have created,
 * approved and sent the signed data object. The URI for this commitment is
 * <a href="http://uri.etsi.org/01903/v1.2.2#ProofOfOrigin">http://uri.etsi.org/01903/v1.2.2#ProofOfOrigin.</a>
 * <li>Proof of receipt indicates that signer recognizes to have received the
 * content of the signed data object. The URI for this commitment is
 * <a href="http://uri.etsi.org/01903/v1.2.2#ProofOfReceipt">http://uri.etsi.org/01903/v1.2.2#ProofOfReceipt.</a>
 * <li>Proof of delivery indicates that the TSP providing that indication has
 * delivered a signed data object in a local store accessible to the recipient
 * of the signed data object. The URI for this commitment is
 * <a href="http://uri.etsi.org/01903/v1.2.2#ProofOfDelivery">http://uri.etsi.org/01903/v1.2.2#ProofOfDelivery.</a>
 * <li>Proof of sender indicates that the entity providing that indication has
 * sent the signed data object (but not necessarily created it). The URI for
 * this commitment is
 * <a href="http://uri.etsi.org/01903/v1.2.2#ProofOfSender">http://uri.etsi.org/01903/v1.2.2#ProofOfSender.</a>
 * <li>Proof of approval indicates that the signer has approved the content of
 * the signed data object. The URI for this commitment is
 * <a href="http://uri.etsi.org/01903/v1.2.2#ProofOfApproval">http://uri.etsi.org/01903/v1.2.2#ProofOfApproval.</a>
 * <li>Proof of creation indicates that the signer has created the signed data
 * object (but not necessarily approved, nor sent it). The URI for this
 * commitment is
 * <a href="http://uri.etsi.org/01903/v1.2.2#ProofOfCreation">http://uri.etsi.org/01903/v1.2.2#ProofOfCreation.</a>
 * </ul>
 *
 * <p>One <code>ObjectReference</code> element refers to one
 * <code>ds:Reference</code> element of the <code>ds:SignedInfo</code>
 * corresponding with one data object qualified by this property. If some but
 * not all the signed data objects share the same commitment, one
 * <code>ObjectReference</code> element MUST appear for each one of them.
 * However, if all the signed data objects share the same commitment, the
 * <code>AllSignedDataObjects</code> empty element MUST be present.
 *
 * <p>The <code>CommitmentTypeQualifiers</code> element provides means to
 * include additional qualifying information on the commitment made by the
 * signer.
 *
 *
 * @author ahmety
 * date: Sep 23, 2009
 */
public class CommitmentTypeIndication extends XAdESBaseElement
{
    private CommitmentTypeId mCommitmentTypeId;

    private List<String> mObjectReferences = new ArrayList<String>();

    /*
        If all the signed data objects share the same commitment,
        the AllSignedDataObjects empty element MUST be present.
    */
    private boolean mAllSignedDataObjects;

    private List<CommitmentTypeQualifier> mCommitmentTypeQualifiers =
                                                new ArrayList<CommitmentTypeQualifier>(0);

    public CommitmentTypeIndication(Context aBaglam,
                                CommitmentTypeId aCommitmentTypeId,
                                List<String> aObjectReferences,
                                boolean aAllSignedDataObjects,
                                List<CommitmentTypeQualifier> aCommitmentTypeQualifiers)
            throws XMLSignatureException
    {
        super(aBaglam);
        mCommitmentTypeId = aCommitmentTypeId;
        mObjectReferences = aObjectReferences;
        mAllSignedDataObjects = aAllSignedDataObjects;
        mCommitmentTypeQualifiers = aCommitmentTypeQualifiers;

        if (aCommitmentTypeId == null){
            throw new XMLSignatureException("errors.null", "CommitmentTypeId");
        }

        if (((aObjectReferences==null) || (aObjectReferences.size()==0)) && (!aAllSignedDataObjects)) {
            throw new XMLSignatureException("core.model.commitmentNeedObjectReferences");
        }


        addLineBreak();
        mElement.appendChild(mCommitmentTypeId.getElement());
        addLineBreak();

        if ((mObjectReferences!=null) && (mObjectReferences.size()>0)){
            for (int i = 0; i < mObjectReferences.size(); i++) {
                String objectRef = mObjectReferences.get(i);
                insertTextElement(NS_XADES_1_3_2, TAGX_OBJECTREFERENCE, objectRef);
            }
        }

        if (mAllSignedDataObjects){
            insertElement(NS_XADES_1_3_2, TAGX_ALLSIGNEDDATAOBJECTS);
        }

        if (mCommitmentTypeQualifiers != null){
            for (int i = 0; i < mCommitmentTypeQualifiers.size(); i++) {
                CommitmentTypeQualifier ctq = mCommitmentTypeQualifiers.get(i);
                mElement.appendChild(ctq.getElement());
                addLineBreak();
            }
        }

    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws XMLSignatureException
     *          when structure is invalid or can not be resolved appropriately
     */
    public CommitmentTypeIndication(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element typeIdElm = selectChildElement(NS_XADES_1_3_2, TAGX_COMMITMENTTYPEID);
        if (typeIdElm != null){
            mCommitmentTypeId = new CommitmentTypeId(typeIdElm, mContext);
        }

        Element[] objectIdElm = selectChildren(NS_XADES_1_3_2, TAGX_OBJECTREFERENCE);
        if (objectIdElm != null && objectIdElm.length>0)
        {
            for (int i = 0; i < objectIdElm.length; i++)
            {
                String objId = XmlUtil.getText(objectIdElm[i]);
                mObjectReferences.add(objId);
            }
        }

        mAllSignedDataObjects = (null != selectChildElement(NS_XADES_1_3_2, TAGX_ALLSIGNEDDATAOBJECTS));

        Element qualifiersElm = selectChildElement(NS_XADES_1_3_2, TAGX_COMMITMENTTYPEQUALIFIERS);
        if (qualifiersElm!=null)
        {
            Element[] qualifierArr = XmlUtil.selectNodes(qualifiersElm.getFirstChild(),
                                                         NS_XADES_1_3_2, TAGX_COMMITMENTTYPEQUALIFIER);
            for (int i = 0; i < qualifierArr.length; i++)
            {
                CommitmentTypeQualifier ctq = new CommitmentTypeQualifier(qualifierArr[i], mContext);
                mCommitmentTypeQualifiers.add(ctq);
            }
        }
    }

    public ObjectIdentifier getCommitmentTypeId()
    {
        return mCommitmentTypeId;
    }

    public List<String> getObjectReferences()
    {
        return mObjectReferences;
    }

    public boolean isAllSignedDataObjects()
    {
        return mAllSignedDataObjects;
    }

    public List<CommitmentTypeQualifier> getCommitmentTypeQualifiers()
    {
        return mCommitmentTypeQualifiers;
    }

    public String getLocalName()
    {
        return TAGX_COMMITMENTTYPEINDICATION;
    }
}
