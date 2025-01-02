package tr.gov.tubitak.uekae.esya.api.xmlsignature.model.keyinfo;

import org.w3c.dom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.Context;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.model.BaseElement;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * <dl>
 * <dt>Identifier</dt>
 *
 * <dd><code>Type="<a name="RSAKeyValue"
 *   href="http://www.w3.org/2000/09/xmldsig#RSAKeyValue"
 *   >http://www.w3.org/2000/09/xmldsig#RSAKeyValue</a>"
 *   </code> (this can be used within a <code>RetrievalMethod</code> or
 *   <code>Reference</code> element to identify the referent's type)</dd>
 * </dl>
 *
 * <p>RSA key values have two fields: Modulus and Exponent.</p>
 *  <pre>
 * &lt;RSAKeyValue&gt;
 *    &lt;Modulus&gt;xA7SEU+e0yQH5rm9kbCDN9o3aPIo7HbP7tX6WOocLZAtNfyxSZDU16ksL6W
 *        jubafOqNEpcwR3RdFsT7bCqnXPBe5ELh5u4VEy19MzxkXRgrMvavzyBpVRgBUwUlV
 *        5foK5hhmbktQhyNdy/6LpQRhDUDsTvK+g9Ucj47es9AQJ3U=
 *   &lt;/Modulus&gt;
 *   &lt;Exponent&gt;AQAB&lt;/Exponent&gt;
 * &lt;/RSAKeyValue&gt;
 * </pre>
 *
 * <p>Arbitrary-length integers (e.g. "bignums" such as RSA moduli) are
 * represented in XML as octet strings as defined by the
 * <code>ds:CryptoBinary</code> type.</p>
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 *
 * <pre>
 * &lt;complexType name="RSAKeyValueType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Modulus" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/&gt;
 *         &lt;element name="Exponent" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 * @author ahmety
 * date: Jun 10, 2009
 */
public class RSAKeyValue extends BaseElement implements KeyValueElement
{

    private static Logger logger = LoggerFactory.getLogger(RSAKeyValue.class);

    private BigInteger mModulus;
    private BigInteger mExponent;

    private RSAPublicKey mPublicKey;

    public RSAKeyValue(Context aBaglam, RSAPublicKey aKey)
            throws XMLSignatureException
    {
        super(aBaglam);
        mModulus = aKey.getModulus();
        mExponent = aKey.getPublicExponent();

        if (logger.isDebugEnabled()){
            logger.debug("Constructing rsa key value from public key");
            logger.debug("modulus: "+mModulus);
            logger.debug("exponent: "+mExponent);
        }

        addLineBreak();
        addBigIntegerElement(mModulus, Constants.NS_XMLDSIG,
                             Constants.TAG_MODULUS);
        addLineBreak();
        addBigIntegerElement(mExponent, Constants.NS_XMLDSIG,
                             Constants.TAG_EXPONENT);
        addLineBreak();
    }

    /**
     *  Construct RSAKeyValue from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public RSAKeyValue(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mModulus = getBigIntegerFromElement(Constants.NS_XMLDSIG,
                                            Constants.TAG_MODULUS);
        mExponent = getBigIntegerFromElement(Constants.NS_XMLDSIG,
                                             Constants.TAG_EXPONENT);

        if (logger.isDebugEnabled()){
            logger.debug("Constructing rsa key value from xml element");
            logger.debug("modulus: "+mModulus);
            logger.debug("exponent: "+mExponent);
        }

    }

    public PublicKey getPublicKey() throws XMLSignatureException
    {
        if (mPublicKey==null){
            // generate public key from its components
            logger.debug("Generating rsa public key.");

            try {
                KeyFactory rsaFactory = KeyFactory.getInstance("RSA");

                // KeyFactory rsaFactory = KeyFactory.getInstance(JCE_RSA);
                RSAPublicKeySpec rsaKeyspec =
                                    new RSAPublicKeySpec(mModulus, mExponent);
                mPublicKey= (RSAPublicKey)rsaFactory.generatePublic(rsaKeyspec);
            }
            catch (Exception x) {
                throw new XMLSignatureException(x, "core.cantGeneratePublicKey", "RSA");
            }
        }
        return mPublicKey;
    }

    public BigInteger getModulus()
    {
        return mModulus;
    }

    public BigInteger getExponent()
    {
        return mExponent;
    }


    // baseElement metodlari
    public String getLocalName()
    {
        return Constants.TAG_RSAKEYVALUE;
    }
}
