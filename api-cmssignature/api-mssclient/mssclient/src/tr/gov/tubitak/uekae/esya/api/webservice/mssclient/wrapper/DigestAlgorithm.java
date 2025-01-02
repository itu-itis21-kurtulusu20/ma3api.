package tr.gov.tubitak.uekae.esya.api.webservice.mssclient.wrapper;

import tr.gov.tubitak.uekae.esya.api.crypto.alg.DigestAlg;

/**
 * @author ahmety
 * date: Apr 22, 2009
 */
//XML'den alındı.Kaynak -> DigestMethod
public enum DigestAlgorithm
{
    /*
    "http://www.w3.org/2001/04/xmldsig-more#md5"
    "http://www.w3.org/2001/04/xmlenc#ripemd160"
    "http://www.w3.org/2000/09/xmldsig#sha1"
    "http://www.w3.org/2001/04/xmlenc#sha256"
    "http://www.w3.org/2001/04/xmldsig-more#sha384"
    "http://www.w3.org/2001/04/xmlenc#sha512"
    */
    
	MD_5       ("http://www.w3.org/2001/04/xmldsig-more#md5",   DigestAlg.MD5),
	RIPEMD_160 ("http://www.w3.org/2001/04/xmlenc#ripemd160",   DigestAlg.RIPEMD),
	SHA_1      ("http://www.w3.org/2000/09/xmldsig#sha1",       DigestAlg.SHA1),
	SHA_224    ("http://www.w3.org/2001/04/xmldsig-more#sha224",DigestAlg.SHA224),
	SHA_256    ("http://www.w3.org/2001/04/xmlenc#sha256",      DigestAlg.SHA256),
	SHA_384    ("http://www.w3.org/2001/04/xmldsig-more#sha384",DigestAlg.SHA384),
	SHA_512    ("http://www.w3.org/2001/04/xmlenc#sha512",      DigestAlg.SHA512);

    private final String mUrl;
    private final DigestAlg mAlgorithm;

    DigestAlgorithm(String aUrl, DigestAlg aAlgorithm){
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
    
    public static DigestAlg resolve(String aUrl)
    {
        if (aUrl!=null){
            for (DigestAlgorithm alg :  values()) {
                if (alg.getUrl().equals(aUrl))
                    return alg.getAlgorithm();
            }
        }
        return null;
    }
}