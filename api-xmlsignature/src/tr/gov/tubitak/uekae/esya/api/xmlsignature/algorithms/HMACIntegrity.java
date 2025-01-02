package tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tr.gov.tubitak.uekae.esya.api.crypto.Crypto;
import tr.gov.tubitak.uekae.esya.api.crypto.MAC;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.XMLSignatureException;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.util.I18n;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;

/**
 * <p>MAC algorithms take two implicit parameters, their keying material
 * determined from <code>KeyInfo</code> and the octet stream output by <code>
 * CanonicalizationMethod</code>. MACs and signature algorithms are
 * syntactically identical but a MAC implies a shared secret key.
 *
 * <p>The <a href="http://www.ietf.org/rfc/rfc2104.txt">HMAC</a> algorithm
 * (RFC2104 ) takes the output (truncation) length in bits as a parameter; this
 * specification REQUIRES that the truncation length be a multiple of 8 (i.e.
 * fall on a byte boundary) because Base64 encoding operates on full bytes.
 * If the truncation parameter is not specified then all the bits of the hash
 * are output. Any signature with a truncation length that is less than half
 * the output length of the underlying hash algorithm MUST be deemed invalid. An
 * example of an HMAC <code>SignatureMethod</code> element:</p>
 *
 * <pre>   &lt;SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#hmac-sha1"&gt;
      &lt;HMACOutputLength&gt;128&lt;/HMACOutputLength&gt;
   &lt;/SignatureMethod&gt;
</pre>

 * <p>The output of the HMAC algorithm is ultimately the output (possibly
 * truncated) of the chosen digest algorithm. This value shall be base64
 * encoded in the same straightforward fashion as the output of the digest
 * algorithms.
 *
 * <pre>   Schema Definition:

   &lt;simpleType name="HMACOutputLengthType"&gt;
     &lt;restriction base="integer"/&gt;
   &lt;/simpleType&gt;
 * </pre>
 *
 * @author ahmety
 * date: Jul 30, 2009
 */
public class HMACIntegrity implements XmlSignatureAlgorithm
{
    private static Logger logger = LoggerFactory.getLogger(HMACIntegrity.class);

    public static final String PARAM_HMAC_OUTPUT_LEGTH = "hmac.output.lenght";

    private MACAlg mMACAlg;
    private MAC mMAC;

    public HMACIntegrity(MACAlg aMACAlg)
    {
        mMACAlg = aMACAlg;
    }

    public String getAlgorithmName()
    {
        return mMACAlg.getName(); 
    }

    public void initSign(Key aKey, AlgorithmParams aParameters) throws XMLSignatureException
    {
        _init(aKey, aParameters);
    }

    public void initVerify(Key aKey, AlgorithmParams aParameters) throws XMLSignatureException
    {
        _init(aKey, aParameters);
    }

    public void update(byte[] aData) throws XMLSignatureException
    {
        try {
            mMAC.process(aData);
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "errors.cantUpdate", "MAC");
        }
    }

    public byte[] sign() throws XMLSignatureException
    {
        try {
            return mMAC.doFinal(null);
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "Cant digest MAC");
        }
    }

    public boolean verify(byte[] aSignatureValue) throws XMLSignatureException
    {
        try {
            byte[] digest = mMAC.doFinal(null);
            if (logger.isDebugEnabled()){
                logger.debug("digest: "+new String(digest));
                logger.debug("signatureValue: "+new String(aSignatureValue));
            }
            return Arrays.equals(digest, aSignatureValue);
        }
        catch (Exception x){
            throw new XMLSignatureException(x, "Cant verify MAC");
        }

    }

    @SuppressWarnings("unchecked")
    private void _init(Key aKey, AlgorithmParams aParameters)
            throws XMLSignatureException
    {
        if (logger.isDebugEnabled())
            logger.debug("hmac init: "+aKey +", "+aParameters);

        if (!(aKey instanceof SecretKey)){
            throw new XMLSignatureException("core.invalid.secretkey", aKey.toString());
        }

        try {
            mMAC = Crypto.getMAC(mMACAlg);
        } catch (Exception x){
            throw new XMLSignatureException(x, "Cant resolve Mac "+mMACAlg);
        }

        try {
            mMAC.init(aKey, aParameters);
        }
        catch (Exception e) {
            throw new XMLSignatureException(e, "errors.cantInit", "HMAC "+ I18n.translate("signer"));
        }
    }

    public static class HMACwithMD5 extends HMACIntegrity{
        public HMACwithMD5() { super(MACAlg.HMAC_MD5); }
    }
    public static class HMACwithRIPEMD extends HMACIntegrity{
        public HMACwithRIPEMD() { super(MACAlg.HMAC_RIPEMD); }
    }
    public static class HMACwithSHA1 extends HMACIntegrity{
        public HMACwithSHA1() { super(MACAlg.HMAC_SHA1); }
    }
    public static class HMACwithSHA256 extends HMACIntegrity{
        public HMACwithSHA256() { super(MACAlg.HMAC_SHA256); }
    }
    public static class HMACwithSHA384 extends HMACIntegrity{
        public HMACwithSHA384() { super(MACAlg.HMAC_SHA384); }
    }
    public static class HMACwithSHA512 extends HMACIntegrity{
        public HMACwithSHA512() { super(MACAlg.HMAC_SHA512); }
    }
}
