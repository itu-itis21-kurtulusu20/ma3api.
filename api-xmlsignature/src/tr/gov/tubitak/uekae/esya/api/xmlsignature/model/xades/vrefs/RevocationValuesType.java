package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.vrefs;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.asn.ocsp.EOCSPResponse;
import tr.gov.tubitak.uekae.esya.api.asn.x509.ECRL;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.Any;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades.XAdESBaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;

import java.util.ArrayList;
import java.util.List;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;

/**
 *
 * <p>Below follows the Schema definition for this element.
 * <pre>
 * &lt;xsd:complexType name="RevocationValuesType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="CRLValues" type="CRLValuesType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="OCSPValues" type="OCSPValuesType" minOccurs="0"/&gt;
 *     &lt;xsd:element name="OtherValues" type="OtherCertStatusValuesType" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="Id" type="xsd:ID" use="optional"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jan 6, 2010
 */
public abstract class RevocationValuesType extends XAdESBaseElement
{
    private List<EncapsulatedCRLValue> mCRLValues = new ArrayList<EncapsulatedCRLValue>(0);
    private List<EncapsulatedOCSPValue> mOCSPValues = new ArrayList<EncapsulatedOCSPValue>(0);
    private List<Any> mOtherValues = new ArrayList<Any>(0);


    protected RevocationValuesType(Context aContext)
    {
        super(aContext);
    }

    /**
     * Construct XADESBaseElement from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    protected RevocationValuesType(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);
        
        Element crlsElement = selectChildElement(NS_XADES_1_3_2, TAGX_CRLVALUES);
        Element ocspsElement = selectChildElement(NS_XADES_1_3_2, TAGX_OCSPVALUES);
        
        Element[] crlElements = null;
        Element[] ocspElements = null;
        
        if (crlsElement!=null)
        	crlElements = XmlUtil.selectNodes(crlsElement.getFirstChild(), NS_XADES_1_3_2, TAGX_ENCAPSULATEDCRLVALUE);
        
        if (ocspsElement!=null)
        	ocspElements = XmlUtil.selectNodes(ocspsElement.getFirstChild(), NS_XADES_1_3_2, TAGX_ENCAPSULATEDOCSPVALUE);

        //Element[] crlElements = selectChildren(NS_XADES_1_3_2, TAGX_ENCAPSULATEDCRLVALUE);
        //Element[] ocspElements = selectChildren(NS_XADES_1_3_2, TAGX_ENCAPSULATEDOCSPVALUE);

        if (crlElements!=null)
        for (Element child : crlElements) {
            EncapsulatedCRLValue crl = new EncapsulatedCRLValue(child, mContext);
            mCRLValues.add(crl);
        }

        if (ocspElements!=null)
        for (Element child : ocspElements) {
            EncapsulatedOCSPValue ocsp = new EncapsulatedOCSPValue(child, mContext);
            mOCSPValues.add(ocsp);
        }

    }

    private void setUpChildren(){
        XmlUtil.removeChildren(mElement);
        addLineBreak();
        if (mCRLValues.size()>0){
            Element crlsElement = insertElement(NS_XADES_1_3_2, TAGX_CRLVALUES);
            addLineBreak(crlsElement);
            for (EncapsulatedCRLValue crlValue : mCRLValues) {
                crlsElement.appendChild(crlValue.getElement());
                addLineBreak(crlsElement);
            }
            addLineBreak();
        }
        if (mOCSPValues.size()>0){
            Element ocspsElement = insertElement(NS_XADES_1_3_2, TAGX_OCSPVALUES);
            addLineBreak(ocspsElement);
            for (EncapsulatedOCSPValue ocspValue : mOCSPValues) {
                ocspsElement.appendChild(ocspValue.getElement());
                addLineBreak(ocspsElement);
            }
            addLineBreak();
        }
        if (mId!=null){
            mElement.setAttributeNS(null, ATTR_ID, mId);
        }
    }

    public int getCRLValueCount(){
        return mCRLValues.size();
    }

    public EncapsulatedCRLValue getCRLValue(int aIndex){
        return mCRLValues.get(aIndex);
    }

    public ECRL getCRL(int aIndex)
            throws XMLSignatureException
    {
        try {
            return new ECRL(mCRLValues.get(aIndex).getValue());
        } catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantDecode", "RevocationValues[crl;index:"+aIndex+"]", I18n.translate("CRL"));
        }
    }

    // todo cache
    public List<ECRL> getAllCRLs() throws XMLSignatureException
    {
        List<ECRL> crls = new ArrayList<ECRL>(getCRLValueCount());
        for (int i=0; i<getCRLValueCount(); i++){
            crls.add(getCRL(i));
        }
        return crls;
    }


    public void addCRLValue(EncapsulatedCRLValue aCRLValue){
        mCRLValues.add(aCRLValue);
        setUpChildren();
    }

    public void addCRL(ECRL aCRL){
        mCRLValues.add(new EncapsulatedCRLValue(mContext, aCRL));
        setUpChildren();
    }

    public int getOCSPValueCount(){
        return mOCSPValues.size();
    }

    public EncapsulatedOCSPValue getOCSPValue(int aIndex){
        return mOCSPValues.get(aIndex);
    }

    public EOCSPResponse getOCSPResponse(int aIndex)
            throws XMLSignatureException
    {
        try {
            return new EOCSPResponse(mOCSPValues.get(aIndex).getValue());
        } catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantDecode", "RevocationValues[ocsp;index:"+aIndex+"]", I18n.translate("OCSP"));
        }
    }

    // todo cache
    public List<EOCSPResponse> getAllOCSPResponses() throws XMLSignatureException
    {
        List<EOCSPResponse> ocsps = new ArrayList<EOCSPResponse>(getOCSPValueCount());
        for (int i=0; i<getOCSPValueCount(); i++){
            ocsps.add(getOCSPResponse(i));
        }
        return ocsps;
    }

    public void addOCSPValue(EncapsulatedOCSPValue aOCSPValue){
        mOCSPValues.add(aOCSPValue);
        setUpChildren();
    }

    public void addOCSPResponse(EOCSPResponse aOCSPResponse){
        mOCSPValues.add(new EncapsulatedOCSPValue(mContext, aOCSPResponse));
        setUpChildren();
    }

}
