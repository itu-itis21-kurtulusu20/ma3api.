package tr.gov.tubitak.uekae.esya.api.xmlsignature;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;
import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;


/**
 * @author ahmety
 * date: Apr 22, 2009
 */
public enum DigestMethod
{
    /*
    "http://www.w3.org/2001/04/xmldsig-more#md5"
    "http://www.w3.org/2001/04/xmlenc#ripemd160"
    "http://www.w3.org/2000/09/xmldsig#sha1"
    "http://www.w3.org/2001/04/xmlenc#sha256"
    "http://www.w3.org/2001/04/xmldsig-more#sha384"
    "http://www.w3.org/2001/04/xmlenc#sha512"
    */

    MD_5       (Constants.NS_XMLDSIG_MORE+"md5",                DigestAlg.MD5),
    RIPEMD_160 ("http://www.w3.org/2001/04/xmlenc#ripemd160",   DigestAlg.RIPEMD),
    SHA_1      (Constants.NS_XMLDSIG+"sha1",                    DigestAlg.SHA1),
    SHA_224    (Constants.NS_XMLDSIG_MORE+"sha224",             DigestAlg.SHA224),
    SHA_256    ("http://www.w3.org/2001/04/xmlenc#sha256",      DigestAlg.SHA256),
    SHA_384    (Constants.NS_XMLDSIG_MORE+"sha384",             DigestAlg.SHA384),
    SHA_512    ("http://www.w3.org/2001/04/xmlenc#sha512",      DigestAlg.SHA512);

    private final String mUrl;
    private final DigestAlg mAlgorithm;

    DigestMethod(String aUrl, DigestAlg aAlgorithm){
        this.mUrl = aUrl;
        this.mAlgorithm = aAlgorithm;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public DigestAlg getAlgorithm()
    {
        return mAlgorithm;
    }

    public static DigestMethod resolve(String aUrl) throws UnknownAlgorithmException
    {
        if (aUrl!=null){
            for (DigestMethod alg :  values()) {
                if (alg.getUrl().equals(aUrl))
                    return alg;
            }
        }
        throw new UnknownAlgorithmException(null, "unknown.algorithm",aUrl);
    }

    public static DigestMethod resolveFromName(DigestAlg aAlg) throws UnknownAlgorithmException
    {
        if(aAlg == null)
         throw new ESYARuntimeException("Algorithm name should not be null");
        else {
            for (DigestMethod digestMethod :  values()) {
                if (digestMethod.getAlgorithm().equals(aAlg))
                    return digestMethod;
            }
        }
        throw new UnknownAlgorithmException(null, "unknown.algorithm",aAlg.getName());
    }
}
