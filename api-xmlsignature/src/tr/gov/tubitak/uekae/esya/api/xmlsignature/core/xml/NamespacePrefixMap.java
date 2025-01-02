package tr.gov.tubitak.uekae.esya.api.xmlsignature.core.xml;

import tr.gov.tubitak.uekae.esya.api.xmlsignature.core.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Regisrty for own constructed xml signature prefixes.
 *
 * @author ahmety
 * date: Apr 3, 2009
 */
public class NamespacePrefixMap
{
    private Map<String, String>
            uRI2prefix = new HashMap<String, String>();

    public NamespacePrefixMap(){
    	init();
    }

    private void init() {
        uRI2prefix.put(Constants.NS_XMLDSIG, "ds");
        uRI2prefix.put(Constants.NS_XMLDSIG_11, "dsig11");
        uRI2prefix.put(Constants.NS_XMLDSIG_MORE, "dsmore");
        uRI2prefix.put(Constants.NS_XADES_1_3_2, "xades");
        uRI2prefix.put(Constants.NS_XADES_1_4_1, "xades141");
    }


    public String getPrefix(String aNamespaceURI)
    {
        if (uRI2prefix.containsKey(aNamespaceURI)){
            return uRI2prefix.get(aNamespaceURI);
        }

        return null;
    }

    public void setPrefix(String aNamespaceURI, String prefix)
    {
    	uRI2prefix.put(aNamespaceURI, prefix);
    }

}
