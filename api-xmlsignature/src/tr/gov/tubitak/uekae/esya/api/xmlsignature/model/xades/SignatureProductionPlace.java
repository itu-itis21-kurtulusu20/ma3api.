package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.xades;

import static tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants.*;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>In some transactions the purported place where the signer was at the
 * time of signature creation MAY need to be indicated. In order to provide
 * this information a new property MAY be included in the signature. This
 * property specifies an address associated with the signer at a particular
 * geographical (e.g. city) location.
 *
 * <p>This is a signed property that qualifies the signer.
 *
 * <p>There SHALL be at most one occurence of this property in the signature. 
 *
 * <p>Below follows the schema definition for this element.
 *
 *
 * <pre>
 * &lt;xsd:element name="SignatureProductionPlace" type="SignatureProductionPlaceType"/&gt;
 *   &lt;xsd:complexType name="SignatureProductionPlaceType"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element name="City" type="xsd:string" minOccurs="0"/&gt;
 *     &lt;xsd:element name="StateOrProvince" type="xsd:string" minOccurs="0"/&gt;
 *     &lt;xsd:element name="PostalCode" type="xsd:string" minOccurs="0"/&gt;
 *     &lt;xsd:element name="CountryName" type="xsd:string" minOccurs="0"/&gt;
 *   &lt;/xsd:sequence&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 * 
 * @author ahmety
 * date: Sep 3, 2009
 */
public class SignatureProductionPlace extends XAdESBaseElement
{
    private static final Logger logger = LoggerFactory.getLogger(SignatureProductionPlace.class);

    private String mCity;
    private String mStateOrProvince;
    private String mPostalCode;
    private String mCountryName;

    public SignatureProductionPlace(Context aBaglam,
                                    String aCity,
                                    String aStateOrProvince,
                                    String aPostalCode,
                                    String aCountryName)
    {
        super(aBaglam);

        addLineBreak();
        if (aCity != null){
            mCity = aCity;
            insertTextElement(NS_XADES_1_3_2, TAGX_CITY, aCity);
        }
        if (aStateOrProvince != null){
            mStateOrProvince = aStateOrProvince;
            insertTextElement(NS_XADES_1_3_2, TAGX_STATEORPROVINCE, aStateOrProvince);
        }
        if (aPostalCode != null){
            mPostalCode = aPostalCode;
            insertTextElement(NS_XADES_1_3_2, TAGX_POSTALCODE, aPostalCode);
        }
        if (aCountryName != null){
            mCountryName = aCountryName;
            insertTextElement(NS_XADES_1_3_2, TAGX_COUNTRYNAME, aCountryName);
        }

    }


    /**
     * Construct SignatureProductionPlace from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException
     *          when structure is invalid or can not be
     *          resolved appropriately
     */
    public SignatureProductionPlace(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mCity =             getChildText(NS_XADES_1_3_2, TAGX_CITY);
        mStateOrProvince =  getChildText(NS_XADES_1_3_2, TAGX_STATEORPROVINCE);
        mPostalCode =       getChildText(NS_XADES_1_3_2, TAGX_POSTALCODE);
        mCountryName =      getChildText(NS_XADES_1_3_2, TAGX_COUNTRYNAME);

        logger.info("Signature ProductionPlace is: "
                       + "[ city: "             + mCity
                       + ", state/province: "   + mStateOrProvince
                       + ", postal code: "      + mPostalCode
                       + ", country: "          + mCountryName + "]");

    }

    public String getCity()
    {
        return mCity;
    }

    public String getStateOrProvince()
    {
        return mStateOrProvince;
    }

    public String getPostalCode()
    {
        return mPostalCode;
    }

    public String getCountryName()
    {
        return mCountryName;
    }

    public String getLocalName()
    {
        return TAGX_SIGNATUREPRODUCTIONPLACE;
    }
}
