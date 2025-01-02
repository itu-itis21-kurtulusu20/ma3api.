package tr.gov.tubitak.uekae.esya.api.xmlsignature;


/**
 * The <code>C14nMethod</code> (CanonicalizationMethod) is the algorithm that is
 * used to canonicalize the <code>SignedInfo</code> element before it is
 * digested as  part of the signature operation.
 *
 * @author ahmety
 * date: Apr 20, 2009
 */
public enum C14nMethod
{

    /**
     * The <a href="http://www.w3.org/TR/2001/REC-xml-c14n-20010315">Canonical
     * XML (without comments)</a> canonicalization method algorithm URI.
     */
    INCLUSIVE("http://www.w3.org/TR/2001/REC-xml-c14n-20010315"),

    /**
     * The <a href="http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments">
     * Canonical XML with comments</a> canonicalization method algorithm URI.
     */
    INCLUSIVE_WITH_COMMENTS("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments"),

    /**
     * The <a href="http://www.w3.org/2001/10/xml-exc-c14n#">Exclusive
     * Canonical XML (without comments)</a> canonicalization method algorithm
     * URI.
     */
    EXCLUSIVE("http://www.w3.org/2001/10/xml-exc-c14n#"),

    /**
     * The <a href="http://www.w3.org/2001/10/xml-exc-c14n#WithComments">
     * Exclusive Canonical XML with comments</a> canonicalization method
     * algorithm URI.
     */
    EXCLUSIVE_WITH_COMMENTS("http://www.w3.org/2001/10/xml-exc-c14n#WithComments"),


    // todo rename
    V1_1("http://www.w3.org/2006/12/xml-c14n11"),

    V1_1_WITH_COMMENTS ("http://www.w3.org/2006/12/xml-c14n11#WithComments");



    private final String mURL;

    C14nMethod(String aURL){
        this.mURL = aURL;
    }

    public String getURL()
    {
        return mURL;
    }

    public static C14nMethod resolve(String aURL) throws UnknownAlgorithmException
    {
        if (aURL!=null){
            for (C14nMethod alg :  values()) {
                if (alg.getURL().equals(aURL))
                    return alg;
            }
        }
        throw new UnknownAlgorithmException(null, "unknown.algorithm",aURL);
    }

    public static boolean isSupported(String aURL) {
        if (aURL!=null){
            for (C14nMethod alg :  values()) {
                if (alg.getURL().equals(aURL))
                    return true;
            }
        }
        return false;
    }


}
