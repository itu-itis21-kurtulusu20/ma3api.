package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.TimeStampValidationData;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * <p>This element contains properties that qualify some of the signed
 * data objects.
 *
 * <p>The optional <code>Id</code> attribute can be used to make a reference
 * to the <code>SignedDataObjectProperties</code> element.
 *
 * <p>These properties qualify the signed data object after all the required
 * transforms have been made.
 *
 * <p><pre>
 * &lt;xsd:element name="SignedDataObjectProperties" type="SignedDataObjectPropertiesType"/&gt;
 *
 * &lt;xsd:complexType name="SignedDataObjectPropertiesType"&gt;
 *     &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="DataObjectFormat"
 *             type="DataObjectFormatType" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="CommitmentTypeIndication"
 *             type="CommitmentTypeIndicationType" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="AllDataObjectsTimeStamp"
 *             type="XAdESTimeStampType" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="IndividualDataObjectsTimeStamp"
 *             type="XAdESTimeStampType" minOccurs="0" maxOccurs="unbounded"/&gt;
 *     &lt;/xsd:sequence&gt;
 *     &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Sep 1, 2009
 */
public class SignedDataObjectProperties extends XAdESBaseElement
{

    private List<DataObjectFormat> mDataObjectFormats = new ArrayList<DataObjectFormat>(0);
    private List<CommitmentTypeIndication> mCommitmentTypeIndications = new ArrayList<CommitmentTypeIndication>(0);
    private List<AllDataObjectsTimeStamp> mAllDataObjectsTimeStamps = new ArrayList<AllDataObjectsTimeStamp>(0);
    private List<IndividualDataObjectsTimeStamp> mIndividualDataObjectsTimeStamps = new ArrayList<IndividualDataObjectsTimeStamp>(0);


    public SignedDataObjectProperties(Context aBaglam)
    {
        super(aBaglam);
    }


    /**
     *  Construct SignedDataObjectProperties from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public SignedDataObjectProperties(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        Element[] dofElms = selectChildren(NS_XADES_1_3_2, TAGX_DATAOBJECTFORMAT);
        if (dofElms!=null){
            for (int i = 0; i < dofElms.length; i++) {
                mDataObjectFormats.add(new DataObjectFormat(dofElms[i], mContext));
            }
        }

        Element[] commitmentTypeElms = selectChildren(NS_XADES_1_3_2, TAGX_COMMITMENTTYPEINDICATION);
        if (commitmentTypeElms!=null){
            for (int i = 0; i < commitmentTypeElms.length; i++) {
                mCommitmentTypeIndications.add(new CommitmentTypeIndication(commitmentTypeElms[i], mContext));
            }
        }

        Element[] allDataObjsElms = selectChildren(NS_XADES_1_3_2, TAGX_ALLDATAOBJECTSTIMESTAMP);
        if (allDataObjsElms!=null){
            for (int i = 0; i < allDataObjsElms.length; i++) {
                mAllDataObjectsTimeStamps.add(new AllDataObjectsTimeStamp(allDataObjsElms[i], mContext));
            }
        }

        Element[] individualDataObjectsElms = selectChildren(NS_XADES_1_3_2, TAGX_INDIVIDUALDATAOBJECTSTIMESTAMP);
        if (individualDataObjectsElms!=null){
            for (int i = 0; i < individualDataObjectsElms.length; i++) {
                mIndividualDataObjectsTimeStamps.add(new IndividualDataObjectsTimeStamp(individualDataObjectsElms[i], mContext));
            }
        }
    }

    private void setupChildren()
    {
        XmlUtil.removeChildren(mElement);    

        addLineBreak();

        for (DataObjectFormat dof : mDataObjectFormats){
            mElement.appendChild(dof.getElement());
            addLineBreak();
        }
        for (CommitmentTypeIndication cti : mCommitmentTypeIndications){
            mElement.appendChild(cti.getElement());
            addLineBreak();
        }
        for (AllDataObjectsTimeStamp adots : mAllDataObjectsTimeStamps){
            mElement.appendChild(adots.getElement());
            addLineBreak();
        }
        for (IndividualDataObjectsTimeStamp idots : mIndividualDataObjectsTimeStamps){
            mElement.appendChild(idots.getElement());
            addLineBreak();
        }

        if (mId!=null){
            mElement.setAttributeNS(null, ATTR_ID, mId);
        }
    }

    public int getDataObjectFormatCount()
    {
        return mDataObjectFormats.size();
    }

    public DataObjectFormat getDataObjectFormat(int aIndex)
    {
        return mDataObjectFormats.get(aIndex);
    }

    public void addDataObjectFormat(DataObjectFormat aDataObjectFormat)
    {
        mDataObjectFormats.add(aDataObjectFormat);
        setupChildren();
    }

    public int getCommitmentTypeIndicationCount()
    {
        return mCommitmentTypeIndications.size();
    }

    public CommitmentTypeIndication getCommitmentTypeIndication(int aIndex)
    {
        return mCommitmentTypeIndications.get(aIndex);
    }

    public void addCommitmentTypeIndication(CommitmentTypeIndication aCommitmentTypeIndication)
    {
        mCommitmentTypeIndications.add(aCommitmentTypeIndication);
        setupChildren();
    }

    public int getAllDataObjectsTimeStampCount()
    {
        return mAllDataObjectsTimeStamps.size();
    }

    public AllDataObjectsTimeStamp getAllDataObjectsTimeStamp(int aIndex)
    {
        return mAllDataObjectsTimeStamps.get(aIndex);
    }

    public List<AllDataObjectsTimeStamp> getAllDataObjectsTimeStamps()
    {
        return mAllDataObjectsTimeStamps;
    }

    /**
     * Add TimestampValidationData manually
     * @param aAllDataObjectsTimeStamp
     * @see TimeStampValidationData
     */
    public void addAllDataObjectsTimeStamp(AllDataObjectsTimeStamp aAllDataObjectsTimeStamp)
    {
    	// todo         addTimestampValidationData ;
        mAllDataObjectsTimeStamps.add(aAllDataObjectsTimeStamp);
        setupChildren();
    }

    public int getIndividualDataObjectsTimeStampCount()
    {
        return mIndividualDataObjectsTimeStamps.size();
    }

    public IndividualDataObjectsTimeStamp getIndividualDataObjectsTimeStamp(int aIndex)
    {
        return mIndividualDataObjectsTimeStamps.get(aIndex);
    }

    public List<IndividualDataObjectsTimeStamp> getIndividualDataObjectsTimeStamps()
    {
        return mIndividualDataObjectsTimeStamps;
    }

    /**
     * Add TimestampValidationData manually
     * @param aIndividualDataObjectsTimeStamp
     * @see TimeStampValidationData
     */
    public void addIndividualDataObjectsTimeStamp(IndividualDataObjectsTimeStamp aIndividualDataObjectsTimeStamp)
    {
    	// todo         addTimestampValidationData ;
        mIndividualDataObjectsTimeStamps.add(aIndividualDataObjectsTimeStamp);
        setupChildren();
    }

    public String getLocalName()
    {
        return TAGX_SIGNEDATAOBJECTPROPERIES;
    }
}
