package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.Algorithm;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.MACAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.SignatureAlg;
import tr.gov.tubitak.uekae.esya.api.crypto.params.AlgorithmParams;
import tr.gov.tubitak.uekae.esya.api.crypto.params.RSAPSSParams;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms.DSAXmlSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms.ECDSAXmlSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms.RSAXmlSignature;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.algorithms.XmlSignatureAlgorithm;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

import java.security.spec.AlgorithmParameterSpec;


/**
 * Defines known signature algorithms.
 *
 * @author ahmety
 * date: Apr 24, 2009
 */
public enum SignatureMethod
{

/*  "http://www.w3.org/2000/09/xmldsig#dsa-sha1"
    "http://www.w3.org/2001/04/xmldsig-more#rsa-md5"
    "http://www.w3.org/2001/04/xmldsig-more#rsa-ripemd160"
    "http://www.w3.org/2000/09/xmldsig#rsa-sha1"
    "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"
    "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384"
    "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"
    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1"
    "http://www.w3.org/2001/04/xmldsig-more#hmac-md5"
    "http://www.w3.org/2001/04/xmldsig-more#hmac-ripemd160"
    "http://www.w3.org/2000/09/xmldsig#hmac-sha1"
    "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256"
    "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384"
    "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512"
    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1"
    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224"
    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256"
    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384"
    "http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512"
    */



    DSA_SHA1    (Constants.NS_XMLDSIG     +"dsa-sha1",      SignatureAlg.DSA_SHA1,   DSAXmlSignature.DSAWithSHA1.class),
    // todo
    DSA_SHA256  (Constants.NS_XMLDSIG_11  +"dsa-sha256",    SignatureAlg.DSA_SHA1,   DSAXmlSignature.DSAWithSHA1.class),

    RSA_MD5     (Constants.NS_XMLDSIG_MORE+"rsa-md5",       SignatureAlg.RSA_MD5,    RSAXmlSignature.RSAwithMD5.class),
    // not known
    //RSA_RIPEMD  (Constants.NS_XMLDSIG_MORE+"rsa-ripemd160", EAlgorithmIdentifier.ASIM_ALGO_RSA,   "RIPEMD-160"),
    RSA_SHA1    (Constants.NS_XMLDSIG     +"rsa-sha1",      SignatureAlg.RSA_SHA1,   RSAXmlSignature.RSAwithSHA1.class),
    RSA_SHA256  (Constants.NS_XMLDSIG_MORE+"rsa-sha256",    SignatureAlg.RSA_SHA256, RSAXmlSignature.RSAwithSHA256.class),
    RSA_SHA384  (Constants.NS_XMLDSIG_MORE+"rsa-sha384",    SignatureAlg.RSA_SHA384, RSAXmlSignature.RSAwithSHA384.class),
    RSA_SHA512  (Constants.NS_XMLDSIG_MORE+"rsa-sha512",    SignatureAlg.RSA_SHA512, RSAXmlSignature.RSAwithSHA512.class),

    RSA_PSS_SHA1     (Constants.NS_XMLDSIG_MORE_2007+"sha1-rsa-MGF1",   new RSAPSSParams(DigestAlg.SHA1),    SignatureAlg.RSA_PSS,  RSAXmlSignature.RSAwithPSS.class),
    RSA_PSS_SHA256   (Constants.NS_XMLDSIG_MORE_2007+"sha256-rsa-MGF1", new RSAPSSParams(DigestAlg.SHA256),  SignatureAlg.RSA_PSS,  RSAXmlSignature.RSAwithPSS.class),
    RSA_PSS_SHA384   (Constants.NS_XMLDSIG_MORE_2007+"sha384-rsa-MGF1", new RSAPSSParams(DigestAlg.SHA384),  SignatureAlg.RSA_PSS,  RSAXmlSignature.RSAwithPSS.class),
    RSA_PSS_SHA512   (Constants.NS_XMLDSIG_MORE_2007+"sha512-rsa-MGF1", new RSAPSSParams(DigestAlg.SHA512),  SignatureAlg.RSA_PSS,  RSAXmlSignature.RSAwithPSS.class),

    ECDSA_SHA1  (Constants.NS_XMLDSIG_MORE+"ecdsa-sha1",    SignatureAlg.ECDSA_SHA1,   ECDSAXmlSignature.ECDSAwithSHA1.class),
    ECDSA_SHA256(Constants.NS_XMLDSIG_MORE+"ecdsa-sha256",  SignatureAlg.ECDSA_SHA256, ECDSAXmlSignature.ECDSAwithSHA256.class),
    ECDSA_SHA384(Constants.NS_XMLDSIG_MORE+"ecdsa-sha384",  SignatureAlg.ECDSA_SHA384, ECDSAXmlSignature.ECDSAwithSHA384.class),
    ECDSA_SHA512(Constants.NS_XMLDSIG_MORE+"ecdsa-sha512",  SignatureAlg.ECDSA_SHA512, ECDSAXmlSignature.ECDSAwithSHA512.class),

    /* Pss test edilirken SignatureAlgorithmsTest.java classında bulunan Hmac Testlerinin düzgün çalışmadığı görüldü.
       Geçici süreli kaldırıldı.

    HMAC_MD5    (Constants.NS_XMLDSIG_MORE+"hmac-md5",      MACAlg.HMAC_MD5,    HMACIntegrity.HMACwithMD5.class),
    HMAC_RIPEMD (Constants.NS_XMLDSIG_MORE+"hmac-ripemd160",MACAlg.HMAC_RIPEMD, HMACIntegrity.HMACwithRIPEMD.class),
    HMAC_SHA1   (Constants.NS_XMLDSIG     +"hmac-sha1",     MACAlg.HMAC_SHA1,   HMACIntegrity.HMACwithSHA1.class),
    HMAC_SHA256 (Constants.NS_XMLDSIG_MORE+"hmac-sha256",   MACAlg.HMAC_SHA256, HMACIntegrity.HMACwithSHA256.class),
    HMAC_SHA384 (Constants.NS_XMLDSIG_MORE+"hmac-sha384",   MACAlg.HMAC_SHA384, HMACIntegrity.HMACwithSHA384.class),
    HMAC_SHA512 (Constants.NS_XMLDSIG_MORE+"hmac-sha512",   MACAlg.HMAC_SHA512, HMACIntegrity.HMACwithSHA512.class),*/
    ;

    private String mUrl;
    private Algorithm mAlgorithm;
    private DigestAlg mDigestAlg;
    private Class<? extends XmlSignatureAlgorithm> mSignatureClass;
    private Algorithm mSignatureAlg;
    private AlgorithmParams mAlgorithmParams;

    SignatureMethod(String aUrl, SignatureAlg aSignatureAlg, Class<? extends XmlSignatureAlgorithm> aClass)
    {
        mUrl = aUrl;
        mSignatureAlg = aSignatureAlg;
        mAlgorithm = aSignatureAlg.getAsymmetricAlg();
        mDigestAlg = aSignatureAlg.getDigestAlg();
        mSignatureClass = aClass;
    }

    SignatureMethod(String aUrl, RSAPSSParams pssParams, SignatureAlg aSignatureAlg, Class<? extends XmlSignatureAlgorithm> aClass)
    {
        mUrl = aUrl;
        mSignatureAlg = aSignatureAlg;
        mAlgorithm = aSignatureAlg.getAsymmetricAlg();
        mAlgorithmParams = pssParams;
        mDigestAlg = pssParams.getDigestAlg();
        mSignatureClass = aClass;
    }

    SignatureMethod(String aUrl, MACAlg aMacAlg, Class<? extends XmlSignatureAlgorithm> aClass)
    {
        mUrl = aUrl;
        mSignatureAlg = aMacAlg;
        mAlgorithm = aMacAlg;
        mDigestAlg = aMacAlg.getDigestAlg();
        mSignatureClass = aClass;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public Algorithm getAlgorithm()
    {
        return mAlgorithm;
    }

    public Algorithm getSignatureAlg()
    {
        return mSignatureAlg;
    }

    public AlgorithmParams getAlgorithmParams(){
        return mAlgorithmParams;
    }

    public DigestAlg getDigestAlg()
    {
        return mDigestAlg;
    }

    /**
     * return SignatureMethod from algorithm URL
     * @param aUrl of SignatureMethod
     * @return matching SignatureMethod
     * @throws UnknownAlgorithmException if no matching method found!
     */
    public static SignatureMethod resolve(String aUrl) throws UnknownAlgorithmException
    {
        if (aUrl!=null){
            for (SignatureMethod alg :  values()) {
                if (alg.getUrl().equals(aUrl))
                    return alg;
            }
        }
        throw new UnknownAlgorithmException(null, "unknown.algorithm",aUrl);
    }

    public XmlSignatureAlgorithm getSignatureImpl() {
        try {
            return mSignatureClass.newInstance();
        } catch (Exception x){
            // very low probability of no default arg constructor !..
            throw new XMLSignatureRuntimeException(x, "Can't construct implementor for "+getUrl());
        }
    }

    public static SignatureMethod fromAlgorithmName(String name){
        for (SignatureMethod method : values()){
             if (name.equals(method.getSignatureImpl().getAlgorithmName()))
                 return method;
        }
        return null;
    }

    public static SignatureMethod fromAlgorithmAndParams(String name, AlgorithmParameterSpec algorithmParams) {

        if (name.equals("RSAPSS")) {
            for (SignatureMethod method : values()) {
                if (name.equals(method.getSignatureImpl().getAlgorithmName())) {
                    RSAPSSParams methodParams;
                    if (method.getAlgorithmParams() instanceof RSAPSSParams) {
                        methodParams = (RSAPSSParams)method.getAlgorithmParams();
                        if (methodParams.equals(algorithmParams))
                            return method;
                    }
                }
            }
        }
        return fromAlgorithmName(name);
    }
}
