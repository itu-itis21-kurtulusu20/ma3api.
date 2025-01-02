package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.signature.SignatureRuntimeException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.timestamp.XAdESTimeStamp;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 * <p>This element contains properties that qualify the XML signature that has
 * been specified with the <code>Target</code> attribute of the
 * <code>QualifyingProperties</code> container element. The content of this
 * element is not covered by the XML signature.
 *
 * <p><pre>
 *&lt;xsd:element name="UnsignedSignatureProperties" type="UnsignedSignaturePropertiesType"/&gt;
 *
 *&lt;xsd:complexType name="UnsignedSignaturePropertiesType"&gt;
 *  &lt;xsd:choice maxOccurs="unbounded"&gt;
 *    &lt;xsd:element name="CounterSignature" type="CounterSignatureType" /&gt;
 *    &lt;xsd:element name="SignatureTimeStamp" type="XAdESTimeStampType/&gt;
 *    &lt;xsd:element name="CompleteCertificateRefs" type="CompleteCertificateRefsType"/&gt;
 *    &lt;xsd:element name="CompleteRevocationRefs" type="CompleteRevocationRefsType"/&gt;
 *    &lt;xsd:element name="AttributeCertificateRefs" type="CompleteCertificateRefsType"/&gt;
 *    &lt;xsd:element name="AttributeRevocationRefs" type="CompleteRevocationRefsType"/&gt;
 *    &lt;xsd:element name="SigAndRefsTimeStamp" type="XAdESTimeStampType"/&gt;
 *    &lt;xsd:element name="RefsOnlyTimeStamp" type="XAdESTimeStampType"/&gt;
 *    &lt;xsd:element name="CertificateValues" type="CertificateValuesType"/&gt;
 *    &lt;xsd:element name="RevocationValues" type="RevocationValuesType"/&gt;
 *    &lt;xsd:element name="AttrAuthoritiesCertValues" type="CertificateValuesType"/&gt;
 *    &lt;xsd:element name="AttributeRevocationValues" type="RevocationValuesType"/&gt;
 *    &lt;xsd:element name="ArchiveTimeStamp" type="XAdESTimeStampType"/&gt;
 *    &lt;xsd:any namespace="##other" /&gt;
 *  &lt;/xsd:choice&gt;
 *  &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 *&lt;/xsd:complexType&gt;
 * </pre>
 * 
 * <p>The optional <code>Id</code> attribute can be used to make a reference
 * to the <code>UnsignedSignatureProperties</code> element.
 *
 * @author ahmety
 * date: Sep 1, 2009
 */
public class UnsignedSignatureProperties extends XAdESBaseElement
{
    private static final Logger logger = LoggerFactory.getLogger(UnsignedSignatureProperties.class);

    private List<CounterSignature> mCounterSignatures = new ArrayList<CounterSignature>(0);
    private List<XMLSignature> mCounterXMLSignatures = new ArrayList<XMLSignature>(0);

    private List<TimeStampValidationData> mTimeStampValidationDatas = new ArrayList<TimeStampValidationData>(0);

    private Map<XAdESTimeStamp,TimeStampValidationData> mTS2ValidationData = new HashMap<XAdESTimeStamp,TimeStampValidationData>();
    /** 
    * A XAdES-T form MAY contain several <code>SignatureTimeSamp</code> elements,
     * obtained from different TSAs
    */
    private List<SignatureTimeStamp> mSignatureTimeStamps = new ArrayList<SignatureTimeStamp>(0);

    // C form
    private CompleteCertificateRefs mCompleteCertificateRefs;
    private CompleteRevocationRefs mCompleteRevocationRefs;
    private AttributeCertificateRefs mAttributeCertificateRefs;
    private AttributeRevocationRefs mAttributeRevocationRefs;

    // X 
    private List<SigAndRefsTimeStamp> mSigAndRefsTimeStamps = new ArrayList<SigAndRefsTimeStamp>(0);
    private List<RefsOnlyTimeStamp> mRefsOnlyTimeStamps = new ArrayList<RefsOnlyTimeStamp>(0);

    // X-L
    private CertificateValues mCertificateValues;
    private RevocationValues mRevocationValues;
    private AttrAuthoritiesCertValues mAttrAuthoritiesCertValues;
    private AttributeRevocationValues mAttributeRevocationValues;

    // A
    private List<ArchiveTimeStamp> mArchiveTimeStamps = new ArrayList<ArchiveTimeStamp>(0);

    // properties in order
    private List<UnsignedSignaturePropertyElement> mProperties = new ArrayList<UnsignedSignaturePropertyElement>(0);

    private XMLSignature mSignature;
    
    public UnsignedSignatureProperties(Context aBaglam, XMLSignature aSignature)
    {
        super(aBaglam);
        addLineBreak();
        mSignature = aSignature;
    }

    /**
     *  Construct UnsignedSignatureProperties from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @param aSignature which UnsignedSignatureProperties belongs
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public UnsignedSignatureProperties(Element aElement, Context aContext, XMLSignature aSignature)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        mSignature = aSignature;
        init();
    }

    // delay construction of conntent because some children may reference a not
    // yet constructed conted via RetriecalMethod!
    private void init() throws XMLSignatureException{
    
        List<Element> children = XmlUtil.selectChildElements(mElement);
        for (Element element : children) {
            resolve(element);
        }
    }
    
    private XAdESTimeStamp mLastTimeStampProcessed = null;
    private int mElementCountAfterTimestamp = 0;

    public void resolve(Element aElement)
        throws XMLSignatureException
    {
        String name = aElement.getLocalName();

        if (name.equals(TAGX_COUNTERSIGNATURE)){
            CounterSignature cs = new CounterSignature(aElement, mContext);
            mCounterSignatures.add(cs);
            mCounterXMLSignatures.add(cs.getSignature());
            mProperties.add(cs);
        }
        else if (name.equals(TAGX_SIGNATURETIMESTAMP)){
            SignatureTimeStamp sts = new SignatureTimeStamp(aElement, mContext);
            mSignatureTimeStamps.add(sts);
            mProperties.add(sts);
            mLastTimeStampProcessed = sts;
            mElementCountAfterTimestamp = 0;
        }
        else if (name.equals(TAGX_COMPLETECERTREFS)){
            mCompleteCertificateRefs = new CompleteCertificateRefs(aElement, mContext);
            mProperties.add(mCompleteCertificateRefs);
        }
        else if (name.equals(TAGX_COMPLETEREVOCATIONREFS)){
            mCompleteRevocationRefs = new CompleteRevocationRefs(aElement, mContext);
            mProperties.add(mCompleteRevocationRefs);
        }
        else if (name.equals(TAGX_ATTRIBUTECERTIFICATEREFS)){
            mAttributeCertificateRefs = new AttributeCertificateRefs(aElement, mContext);
            mProperties.add(mAttributeCertificateRefs);
        }
        else if (name.equals(TAGX_ATTRIBUTEREVOCATIONREFS)){
            mAttributeRevocationRefs = new AttributeRevocationRefs(aElement, mContext);
            mProperties.add(mAttributeRevocationRefs);
        }
        else if (name.equals(TAGX_SIGANDREFSTIMESTAMP)){
            SigAndRefsTimeStamp srt = new SigAndRefsTimeStamp(aElement, mContext);
            mSigAndRefsTimeStamps.add(srt);
            mProperties.add(srt);
            mLastTimeStampProcessed = srt;
            mElementCountAfterTimestamp = 0;
        }
        else if (name.equals(TAGX_REFSONLYTIMESTAMP)){
            RefsOnlyTimeStamp rts = new RefsOnlyTimeStamp(aElement, mContext);
            mRefsOnlyTimeStamps.add(rts);
            mProperties.add(rts);
            mLastTimeStampProcessed = rts;
            mElementCountAfterTimestamp = 0;
        }
        else if (name.equals(TAGX_CERTIFICATEVALUES)){
            mCertificateValues = new CertificateValues(aElement, mContext);
            mProperties.add(mCertificateValues);
        }
        else if (name.equals(TAGX_REVOCATIONVALUES)){
            mRevocationValues = new RevocationValues(aElement, mContext);
            mProperties.add(mRevocationValues);
        }
        else if (name.equals(TAGX_TIMESTAMPVALIDATIONDATA)){
        	TimeStampValidationData tsvd = new TimeStampValidationData(aElement, mContext);
            mTimeStampValidationDatas.add(tsvd);
            String uri = tsvd.getURI();
            if (uri!=null && uri.length()>0){
            	uri = uri.substring(1);
            	XAdESTimeStamp found = null;
            	SignedDataObjectProperties sdop = mSignature.getQualifyingProperties().getSignedDataObjectProperties();
            	if (sdop!=null){
	            	for (int j=0; j<sdop.getIndividualDataObjectsTimeStampCount(); j++){
	            		IndividualDataObjectsTimeStamp idots = sdop.getIndividualDataObjectsTimeStamp(j);
	            		String id = idots.getId();
	            		if (id!=null && id.equals(uri)){
	            			found = idots;
	            			break;
	            		}	
	            	}
	            	if (found==null) {
		            	for (int j=0; j<sdop.getAllDataObjectsTimeStampCount(); j++){
		            		AllDataObjectsTimeStamp adots = sdop.getAllDataObjectsTimeStamp(j);
		            		String id = adots.getId();
		            		if (id!=null && id.equals(uri)){
		            			found = adots;
		            			break;
		            		}	
		            	}
	            	}
            	}
            	
            	if (found == null){
                    for (UnsignedSignaturePropertyElement element : mProperties){
                        if (element instanceof XAdESTimeStamp){
                            XAdESTimeStamp timeStamp = (XAdESTimeStamp)element;
                            String id = timeStamp.getId();
                            if (id!=null && id.equals(uri)){
                                found = timeStamp;
                                break;
                            }
                        }
                    }
            	}
            	if (found==null)
            		throw new XMLSignatureException("validation.timestamp.vdata.cantResolveByURI", uri);
                mTS2ValidationData.put(found, tsvd);
            	
            } else if (mLastTimeStampProcessed!=null && mElementCountAfterTimestamp==1) {
            	mTS2ValidationData.put(mLastTimeStampProcessed, tsvd);
            } else {
            	// todo: improve message  
                throw new XMLSignatureException("validation.timestamp.vdata.cantResolve", uri);
            }
            
            // todo resolve where this tsvd belongs
            // mTS2ValidationData.put(timestamp, tsvd);
            mProperties.add(tsvd);
        }
        else if (name.equals(TAGX_ARCHIVETIMESTAMP)){
            ArchiveTimeStamp ats;
            String namespace = aElement.getNamespaceURI();

            if (namespace.equals(NS_XADES_1_4_1))
                ats = new ArchiveTimeStamp(aElement, mContext);
            else
                ats = new ArchiveTimeStamp132(aElement, mContext);
            mArchiveTimeStamps.add(ats);
            mProperties.add(ats);
            mLastTimeStampProcessed = ats;
            mElementCountAfterTimestamp = 0;
        }
        else {
            // todo resolve other whenever implemented
            //mOtherProperties.add(aElement);
            logger.warn("Not yet identified unsigned property type: "+name);
        }
        mElementCountAfterTimestamp++;
    }

    public List<UnsignedSignaturePropertyElement> getProperties()
    {
        return mProperties;
    }

    public void addCounterSignature(XMLSignature aSignature){
        CounterSignature cs = new CounterSignature(mContext, aSignature);
        mCounterSignatures.add(cs);
        mCounterXMLSignatures.add(aSignature);
        addElement(cs);
    }

    public void removeCounterSignature(XMLSignature aSignature){
        for (CounterSignature cs : mCounterSignatures){
            if (cs.getSignature().equals(aSignature)){
                mElement.removeChild(cs.getElement());
                mCounterSignatures.remove(cs);
                mCounterXMLSignatures.remove(aSignature);
                return;
            }
        }
        throw new SignatureRuntimeException("Cant find signature in counter signatures!");
    }

    public int getCounterSignatureCount(){
        return mCounterSignatures.size();
    }

    public CounterSignature getCounterSignature(int aIndex){
        return mCounterSignatures.get(aIndex);
    }

    public List<XMLSignature> getAllCounterSignatures(){
        return mCounterXMLSignatures;
    }

    public int getSignatureTimeStampCount(){
        return mSignatureTimeStamps.size();
    }

    public SignatureTimeStamp getSignatureTimeStamp(int aIndex)
    {
        return mSignatureTimeStamps.get(aIndex);
    }

    public List<SignatureTimeStamp> getSignatureTimeStamps() {
        return mSignatureTimeStamps;
    }

    public void addSignatureTimeStamp(SignatureTimeStamp aSignatureTimeStamp)
    {
        mSignatureTimeStamps.add(aSignatureTimeStamp);
        addElement(aSignatureTimeStamp);
    }

    public CompleteCertificateRefs getCompleteCertificateRefs()
    {
        return mCompleteCertificateRefs;
    }

    public void setCompleteCertificateRefs(CompleteCertificateRefs aCompleteCertificateRefs)
    {
        mCompleteCertificateRefs = aCompleteCertificateRefs;
        addElement(aCompleteCertificateRefs);
    }

    public CompleteRevocationRefs getCompleteRevocationRefs()
    {
        return mCompleteRevocationRefs;
    }

    public void setCompleteRevocationRefs(CompleteRevocationRefs aCompleteRevocationRefs)
    {
        mCompleteRevocationRefs = aCompleteRevocationRefs;
        addElement(aCompleteRevocationRefs);
    }

    public AttributeCertificateRefs getAttributeCertificateRefs()
    {
        return mAttributeCertificateRefs;
    }

    public void setAttributeCertificateRefs(AttributeCertificateRefs aAttributeCertificateRefs)
    {
        mAttributeCertificateRefs = aAttributeCertificateRefs;
        addElement(aAttributeCertificateRefs);
    }

    public AttributeRevocationRefs getAttributeRevocationRefs()
    {
        return mAttributeRevocationRefs;
    }

    public void setAttributeRevocationRefs(AttributeRevocationRefs aAttributeRevocationRefs)
    {
        mAttributeRevocationRefs = aAttributeRevocationRefs;
        addElement(mAttributeRevocationRefs);
    }

    public int getSigAndRefsTimeStampCount(){
        return mSigAndRefsTimeStamps.size();
    }

    public SigAndRefsTimeStamp getSigAndRefsTimeStamp(int aIndex){
        return mSigAndRefsTimeStamps.get(aIndex);
    }

    public List<SigAndRefsTimeStamp> getSigAndRefsTimeStamps() {
        return mSigAndRefsTimeStamps;
    }

    public void addSigAndRefsTimeStamp(SigAndRefsTimeStamp aSrts){
        mSigAndRefsTimeStamps.add(aSrts);
        addElement(aSrts);
    }

    public int getRefsOnlyTimeStampCount(){
        return mRefsOnlyTimeStamps.size();
    }

    public RefsOnlyTimeStamp getRefsOnlyTimeStamp(int aIndex){
        return mRefsOnlyTimeStamps.get(aIndex);
    }

    public List<RefsOnlyTimeStamp> getRefsOnlyTimeStamps() {
        return mRefsOnlyTimeStamps;
    }

    public void addRefsOnlyTimeStamp(RefsOnlyTimeStamp aRots){
        mRefsOnlyTimeStamps.add(aRots);
        addElement(aRots);
    }

    public AttrAuthoritiesCertValues getAttrAuthoritiesCertValues()
    {
        return mAttrAuthoritiesCertValues;
    }

    public void setAttrAuthoritiesCertValues(AttrAuthoritiesCertValues aAttrAuthoritiesCertValues)
    {
        mAttrAuthoritiesCertValues = aAttrAuthoritiesCertValues;
        addElement(mAttrAuthoritiesCertValues);
    }

    public CertificateValues getCertificateValues()
    {
        return mCertificateValues;
    }

    public void setCertificateValues(CertificateValues aCertificateValues)
    {
        mCertificateValues = aCertificateValues;
        addElement(aCertificateValues);
    }

    public RevocationValues getRevocationValues()
    {
        return mRevocationValues;
    }

    public void setRevocationValues(RevocationValues aRevocationValues)
    {
        mRevocationValues = aRevocationValues;
        addElement(aRevocationValues);
    }

    public AttributeRevocationValues getAttributeRevocationValues()
    {
        return mAttributeRevocationValues;
    }

    public void setAttributeRevocationValues(AttributeRevocationValues aAttributeRevocationValues)
    {
        mAttributeRevocationValues = aAttributeRevocationValues;
        addElement(aAttributeRevocationValues);
    }

    public int getArchiveTimeStampCount(){
        return mArchiveTimeStamps.size();
    }

    public void addArchiveTimeStamp(ArchiveTimeStamp aArchiveTimeStamp){
        mArchiveTimeStamps.add(aArchiveTimeStamp);
        addElement(aArchiveTimeStamp);
    }

    public ArchiveTimeStamp getArchiveTimeStamp(int aIndex){
        return mArchiveTimeStamps.get(aIndex);
    }

    public List<ArchiveTimeStamp> getArchiveTimeStamps() {
        return mArchiveTimeStamps;
    }

    /**
     * Add timestamp validation data of timestamp existing in signature.
     *
     * When a XAdES signature requires to include all the validation data
     * required for a full verification of a time-stamp token embedded in any of
     * the following containers: SignatureTimeStamp, RefsOnlyTimeStamp,
     * SigAndRefsTimeStamp, or ArchiveTimeStamp, and that validation data is not
     * present in other parts of the signature, a new
     * xadesv141:TimeStampValidationData element SHALL be created containing the
     * missing validation data information and it SHALL be added as a child of
     * UnsignedSignatureProperties elements immediately after the respective
     * time-stamp token container element.
     *
     * @param aTSValidationData to add
     * @param aTimeStamp which validation data belongs
     * @throws XMLSignatureException if the timestamp is not in the signature
     */
    public void addTimeStampValidationData(TimeStampValidationData aTSValidationData, XAdESTimeStamp aTimeStamp)
            throws XMLSignatureException
    {
        String tsId = aTimeStamp.getId();
        if (tsId!=null && tsId.length()>0){
            aTSValidationData.setURI("#"+tsId);
        }

        if ((aTimeStamp instanceof IndividualDataObjectsTimeStamp) || (aTimeStamp instanceof AllDataObjectsTimeStamp)){
            mElement.appendChild(aTSValidationData.getElement());
            mProperties.add(aTSValidationData);
            addLineBreak();
        }
        else {
            int indexOfTS = mProperties.indexOf((UnsignedSignaturePropertyElement)aTimeStamp);
            if (indexOfTS == -1)
                throw new XMLSignatureException("validation.timestamp.vdata.orderError");
            mProperties.add(indexOfTS+1, aTSValidationData);
            Element nextElement = XmlUtil.getNextElement(aTimeStamp.getElement().getNextSibling());
            mElement.insertBefore(aTSValidationData.getElement(), nextElement);
            mElement.insertBefore(getDocument().createTextNode("\n"), nextElement);
        }

        mTimeStampValidationDatas.add(aTSValidationData);
        mTS2ValidationData.put(aTimeStamp, aTSValidationData);
    }


    public TimeStampValidationData getValidationDataForTimestamp(XAdESTimeStamp aTimestamp){
        return mTS2ValidationData.get(aTimestamp);
    }
    
    public TimeStampValidationData getTimeStampValidationData(int aIndex){
    	return mTimeStampValidationDatas.get(aIndex);
    }

    public Map<XAdESTimeStamp,TimeStampValidationData> getAllTimeStampValidationData(){
    	return mTS2ValidationData;
    }

    public int getTimestampValidationDataCount(){
    	return mTimeStampValidationDatas.size();
    }

    protected void addElement(UnsignedSignaturePropertyElement aElement){
        mProperties.add(aElement);
        addLineBreak();
        mElement.appendChild(((BaseElement)aElement).getElement());
    }
    
    
    public String getLocalName()
    {
        return TAGX_UNSIGNEDSIGNATUREPROPERTIES;
    }
}
