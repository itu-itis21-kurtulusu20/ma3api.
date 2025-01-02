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
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;


/**
 * <dl>
 *   <dt>Identifier</dt> <dd>
 *      <code>Type="<a name="DSAKeyValue"
 *          href="http://www.w3.org/2000/09/xmldsig#DSAKeyValue"
 *          >http://www.w3.org/2000/09/xmldsig#DSAKeyValue</a>"
 *      </code> (this can be used within a <code>RetrievalMethod</code> or
 *  <code>Reference</code> element to identify the referent's type)</dd>
 * </dl>
 *
 * <p>DSA keys and the DSA signature algorithm are specified in [DSS]. DSA
 * public key values can have the following fields:</p>
 *
 * <dl>
    <dt><code>P</code></dt>
    <dd>a prime modulus meeting the [DSS] requirements</dd>
    <dt><code>Q</code></dt>
    <dd>an integer in the range 2**159 &lt; Q &lt; 2**160 which is a prime
    divisor of P-1</dd>
    <dt><code>G</code></dt>
    <dd>an integer with certain properties with respect to P and Q</dd>
    <dt><code>Y</code></dt>
    <dd>G**X mod P (where X is part of the private key and not made public)</dd>
    <dt><code>J</code></dt>
    <dd>(P - 1) / Q</dd>
    <dt><code>seed</code></dt>
    <dd>a DSA prime generation seed</dd>
    <dt><code>pgenCounter</code></dt>
    <dd>a DSA prime generation counter</dd>
  </dl>

 * <p>Parameter J is available for inclusion solely for efficiency as it is
 * calculatable from P and Q. Parameters seed and pgenCounter are used in the
 * DSA prime number generation algorithm  specified in [DSS]. As such, they are
 * optional but must either both be present or both be absent. This prime
 * generation algorithm is designed to provide assurance that a weak prime is
 * not being used and it yields a P and Q value. Parameters P, Q, and G can be
 * public and common to a group of users. They might be known from application
 * context. As such, they are optional but P and Q must either both appear or
 * both be absent. If all of <code>P</code>, <code>Q</code>, <code>seed</code>,
 * and <code>pgenCounter</code> are present, implementations are not required to
 * check if they are consistent and are free to use either <code>P</code> and
 * <code>Q</code> or <code>seed</code> and <code>pgenCounter</code>. All
 * parameters are encoded as base64 [<a href="#ref-MIME">MIME</a>]
 * values.</p>
 *
 * <p>Arbitrary-length integers (e.g. "bignums" such as RSA moduli) are
 * represented in XML as octet strings as defined by the
 * <a href="#sec-CryptoBinary"><code>ds:CryptoBinary</code> type</a>.</p>
 *
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <pre>
 * &lt;complexType name="DSAKeyValueType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;sequence minOccurs="0"&gt;
 *           &lt;element name="P" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/&gt;
 *           &lt;element name="Q" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;element name="G" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/&gt;
 *         &lt;element name="Y" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/&gt;
 *         &lt;element name="J" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary" minOccurs="0"/&gt;
 *         &lt;sequence minOccurs="0"&gt;
 *           &lt;element name="Seed" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/&gt;
 *           &lt;element name="PgenCounter" type="{http://www.w3.org/2000/09/xmldsig#}CryptoBinary"/&gt;
 *         &lt;/sequence&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>

 * @author ahmety
 * date: Jun 10, 2009
 */
public class DSAKeyValue extends BaseElement implements KeyValueElement
{
    private static Logger logger = LoggerFactory.getLogger(DSAKeyValue.class);

    protected BigInteger mP;
    protected BigInteger mQ;
    protected BigInteger mG;
    protected BigInteger mY;

    //protected String mJ;
    //protected String mSeed;
    //protected String mPgenCounter;

    protected DSAPublicKey mPublicKey;

    public DSAKeyValue(Context aBaglam, DSAPublicKey aKey)
            throws XMLSignatureException
    {
        super(aBaglam);

        mPublicKey = aKey;
        
        mG = aKey.getParams().getG();
        mP = aKey.getParams().getP();
        mQ = aKey.getParams().getQ();
        mY = aKey.getY();

        if (logger.isDebugEnabled()){
            logger.debug("Constructing dsa key value from public key");
            logger.debug("p: "+mP);
            logger.debug("q: "+mQ);
            logger.debug("g: "+mG);
            logger.debug("y: "+mY);
        }
        addLineBreak();
        addBigIntegerElement(mG, Constants.NS_XMLDSIG, Constants.TAG_G);
        addLineBreak();
        addBigIntegerElement(mP, Constants.NS_XMLDSIG, Constants.TAG_P);
        addLineBreak();
        addBigIntegerElement(mQ, Constants.NS_XMLDSIG, Constants.TAG_Q);
        addLineBreak();
        addBigIntegerElement(mY, Constants.NS_XMLDSIG, Constants.TAG_Y);
        addLineBreak();
    }

    /**
     *  Construct DSAKeyValue from existing
     *  @param aElement xml element
     *  @param aContext according to context
     *  @throws XMLSignatureException when structure is invalid or can not be
     *      resolved appropriately
     */
    public DSAKeyValue(Element aElement, Context aContext)
            throws XMLSignatureException
    {
        super(aElement, aContext);

        mG = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_G);
        mP = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_P);
        mQ = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_Q);
        mY = getBigIntegerFromElement(Constants.NS_XMLDSIG, Constants.TAG_Y);
        if (logger.isDebugEnabled()){
            logger.debug("Constructing dsa key value from xml element");
            logger.debug("p: "+mP);
            logger.debug("q: "+mQ);
            logger.debug("g: "+mG);
            logger.debug("y: "+mY);
        }
    }


    public PublicKey getPublicKey() throws XMLSignatureException
    {
        if (mPublicKey==null){
            // generate public key from its components
            logger.debug("Generating dsa public key.");
            try {
                DSAPublicKeySpec pkspec = new DSAPublicKeySpec(mY, mP, mQ, mG);
                KeyFactory dsaFactory = KeyFactory.getInstance("DSA");

                mPublicKey = (DSAPublicKey)dsaFactory.generatePublic(pkspec);
            } catch (Exception x){
                logger.error("Error occurred in dsa public key gen.", x);
                throw new XMLSignatureException(x, "core.cantGeneratePublicKey", "DSA");
            }
        }
        return mPublicKey;
    }

    public BigInteger getP()
    {
        return mP;
    }

    public BigInteger getQ()
    {
        return mQ;
    }

    public BigInteger getG()
    {
        return mG;
    }

    public BigInteger getY()
    {
        return mY;
    }


    // baseElement metodlari
    public String getLocalName()
    {
        return Constants.TAG_DSAKEYVALUE;
    }
}
