package tr.gov.tubitak.uekae.esya.api.xmlsignature.model;

import org.w3c.dom.Element;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.ParamsWithLength;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.C14nMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.SignatureMethod;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.XmlUtil;


/**
 * <p>The structure of <code>SignedInfo</code> includes the canonicalization
 * algorithm, a signature algorithm, and one or more references. The
 * <code>SignedInfo</code> element may contain an optional ID attribute that
 * will allow it to be referenced by other signatures and objects.</p>
 * <p>
 * <p><code>SignedInfo</code> does not include explicit signature or digest
 * properties (such as calculation time, cryptographic device serial number,
 * etc.). If an application needs to associate properties with the signature or
 * digest, it may include such information in a <code>SignatureProperties</code>
 * element within an <code>Object</code> element.</p>
 * <p>
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <p>
 * <pre>
 * &lt;complexType name="SignedInfoType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}CanonicalizationMethod"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}SignatureMethod"/&gt;
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Reference" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * @author ahmety
 * date: Jun 10, 2009
 */
public class SignedInfo extends Manifest
{

    /*
    CanonicalizationMethod is a required element that specifies the
    canonicalization algorithm applied to the SignedInfo element prior to
    performing signature calculations.    
     */
    private C14nMethod mCanonicalizationMethod;

    /*
    SignatureMethod is a required element that specifies the algorithm used for
    signature generation and validation. This algorithm identifies all
    cryptographic functions involved in the signature operation (e.g. hashing,
    public key algorithms, MACs, padding, etc.).    
     */
    private SignatureMethod mSignatureMethod;

    // for internal
    private Element mC14nElement, mSignMethodElement;
    private Integer mHMACOutputLength;


    public SignedInfo(Context aBaglam)
    {
        super(aBaglam);

        mC14nElement =        insertElement(Constants.NS_XMLDSIG,
                                            Constants.TAG_C14NMETHOD);
        mSignMethodElement =  insertElement(Constants.NS_XMLDSIG,
                                            Constants.TAG_SIGNATUREMETHOD);

        setCanonicalizationMethod(Constants.DEFAULT_REFERENCE_C14N);
        //SignatureAlg sa = getContext().getConfig().getAlgorithmsConfig().getSignatureAlg();
        //SignatureMethod sm = SignatureMethod.fromAlgorithmName(sa.getName());
        setSignatureMethod(/*sm*/Constants.DEFAULT_SIGNATURE_METHOD);
    }

    /**
     * Construct SignedInfo from existing
     * @param aElement xml element
     * @param aContext according to context
     * @throws XMLSignatureException when structure is invalid or can not be
     *                               resolved appropriately
     */
    public SignedInfo(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mC14nElement = XmlUtil.getNextElement(aElement.getFirstChild());
        String c14nAlg = getAttribute(mC14nElement, Constants.ATTR_ALGORITHM);
        mCanonicalizationMethod = C14nMethod.resolve(c14nAlg);

        mSignMethodElement =
                XmlUtil.getNextElement(mC14nElement.getNextSibling());
        String signMethodAlg =
                getAttribute(mSignMethodElement, Constants.ATTR_ALGORITHM);
        mSignatureMethod = SignatureMethod.resolve(signMethodAlg);

        if (mSignatureMethod.getAlgorithm().getName().toLowerCase().startsWith("hmac")) {
            Element hMACOutputLengthElm =
                    XmlUtil.getNextElement(mSignMethodElement.getFirstChild());
            if (hMACOutputLengthElm != null) {
                mHMACOutputLength = Integer.parseInt(
                        XmlUtil.getText(hMACOutputLengthElm));
            }
        }
    }

    public AlgorithmParams getSignatureAlgorithmParameters()
    {
        AlgorithmParams algorithmParams = null;
        if (mSignatureMethod.getAlgorithm().getName().toLowerCase().startsWith("hmac") && mHMACOutputLength != null){
            algorithmParams = new ParamsWithLength(mHMACOutputLength);
        }else if(mSignatureMethod.getSignatureAlg().getName().equals("RSAPSS")){
            algorithmParams = mSignatureMethod.getAlgorithmParams();
        }
        return algorithmParams;
    }

    public C14nMethod getCanonicalizationMethod()
    {
        return mCanonicalizationMethod;
    }

    public void setCanonicalizationMethod(C14nMethod aCanonicalizationMethod)
    {
        mC14nElement.setAttributeNS(null, Constants.ATTR_ALGORITHM,
                                    aCanonicalizationMethod.getURL());
        mCanonicalizationMethod = aCanonicalizationMethod;
    }

    public SignatureMethod getSignatureMethod()
    {
        return mSignatureMethod;
    }

    public void setSignatureMethod(SignatureMethod aSignatureMethod)
    {
        mSignMethodElement.setAttributeNS(null, Constants.ATTR_ALGORITHM,
                                          aSignatureMethod.getUrl());
        mSignatureMethod = aSignatureMethod;
    }


    public byte[] getCanonicalizedBytes()
            throws XMLSignatureException
    {
        return XmlUtil.outputDOM(mElement, mCanonicalizationMethod);
    }

    // baseElement metodlari
    public String getNamespace()
    {
        return Constants.NS_XMLDSIG;
    }

    public String getLocalName()
    {
        return Constants.TAG_SIGNEDINFO;
    }
}
